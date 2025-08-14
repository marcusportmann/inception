/*
 * Copyright Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.operations.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import digital.inception.core.file.FileType;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.MimeData;
import digital.inception.core.util.ResourceUtil;
import digital.inception.core.util.StringUtil;
import digital.inception.core.util.TenantUtil;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.exception.InteractionAttachmentNotFoundException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InteractionSourceNotFoundException;
import digital.inception.operations.model.CreateInteractionNoteRequest;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionAttachmentSummary;
import digital.inception.operations.model.InteractionMimeType;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourceAttribute;
import digital.inception.operations.model.InteractionSourceType;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.InteractionType;
import digital.inception.operations.model.MailboxProtocol;
import digital.inception.operations.model.UpdateInteractionNoteRequest;
import digital.inception.operations.service.BackgroundInteractionSourceSynchronizer;
import digital.inception.operations.service.InteractionService;
import digital.inception.operations.util.MessageUtil;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.StringUtils;

/**
 * The {@code EndToEndTests} class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, OperationsConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
    })
public class EndToEndTests {

  private static final boolean ENABLE_GREEN_MAIL_SECURITY = false;

  private static final String FROM_EMAIL_ADDRESS = "bsmith@example.com";

  private static final String FROM_NAME = "Bob Smith";

  private static final String TO_EMAIL_ADDRESS = "service@fitlife.com";

  private static final String TO_NAME = "FitLife Customer Service";

  private static final String TO_PASSWORD = "Password1";

  private static final String TO_USERNAME = "service@fitlife.com";

  /** The secure random number generator. */
  private static final SecureRandom secureRandom = new SecureRandom();

  /** The Background Interaction Source Synchronizer. */
  @Autowired
  private BackgroundInteractionSourceSynchronizer backgroundInteractionSourceSynchronizer;

  private GreenMail greenMail;

  /** The Interaction Service. */
  @Autowired private InteractionService interactionService;

  /** The end-to-end test. */
  @Test
  public void endToEndTest() throws Exception {

    /*
     * Create the mailbox interaction source, which will consume email messages from the in-memory
     * GreenMail mail server and turn them into interactions with associated interaction
     * attachments.
     */
    InteractionSource mailboxInteractionSource =
        getFitLifeCustomerServiceMailboxInteractionSource();

    interactionService.createInteractionSource(
        TenantUtil.DEFAULT_TENANT_ID, mailboxInteractionSource);

    // Create the Original Outlook HTML Message With Image
    Message originalOutlookHTMLMessageWithImage =
        getOriginalOutlookHTMLMessageWithImage(greenMail.getSmtp().createSession());

    // Send the message
    Transport.send(originalOutlookHTMLMessageWithImage);

    // Wait for mail to arrive
    assertTrue(greenMail.waitForIncomingEmail(5000, 1));

    Integer numberOfNewInteractions =
        backgroundInteractionSourceSynchronizer.synchronizeInteractionSources();

    assertEquals(1, numberOfNewInteractions);

    InteractionSummaries retrievedInteractionSummaries =
        interactionService.getInteractionSummaries(
            TenantUtil.DEFAULT_TENANT_ID,
            mailboxInteractionSource.getId(),
            null,
            null,
            null,
            null,
            null,
            null);

    assertEquals(1, retrievedInteractionSummaries.getInteractionSummaries().size());

    UUID retrievedInteractionId =
        retrievedInteractionSummaries.getInteractionSummaries().getFirst().getId();

    Interaction retrievedInteraction =
        interactionService.getInteraction(TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId);

    InteractionAttachmentSummaries retrievedInteractionAttachmentSummaries =
        interactionService.getInteractionAttachmentSummaries(
            TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId, null, null, null, null, null);

    assertEquals(
        10, retrievedInteractionAttachmentSummaries.getInteractionAttachmentSummaries().size());

    // Create a interaction note for the interaction
    CreateInteractionNoteRequest createInteractionNoteRequest =
        new CreateInteractionNoteRequest(
            retrievedInteraction.getId(), "This is the interaction note content.");

    InteractionNote interactionNote =
        interactionService.createInteractionNote(
            TenantUtil.DEFAULT_TENANT_ID, createInteractionNoteRequest, "TEST1");


    // Update the interaction note for the interaction
    UpdateInteractionNoteRequest updateInteractionNoteRequest =
        new UpdateInteractionNoteRequest(
            interactionNote.getId(), "This is the interaction note content.");

    InteractionNote updatedInteractionNote =
        interactionService.updateInteractionNote(
            TenantUtil.DEFAULT_TENANT_ID, updateInteractionNoteRequest, "TEST2");

    // Retrieve the interaction notes for the interaction
    InteractionNotes interactionNotes =
        interactionService.getInteractionNotes(
            TenantUtil.DEFAULT_TENANT_ID,
            retrievedInteraction.getId(),
            null,
            InteractionNoteSortBy.CREATED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, interactionNotes.getTotal());





    interactionService.deleteInteractionNote(TenantUtil.DEFAULT_TENANT_ID, interactionNote.getId());

    for (InteractionAttachmentSummary interactionAttachmentSummary : retrievedInteractionAttachmentSummaries.getInteractionAttachmentSummaries()) {
      interactionService.deleteInteractionAttachment(
          TenantUtil.DEFAULT_TENANT_ID, interactionAttachmentSummary.getId());
    }

    interactionService.deleteInteraction(TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId);

    interactionService.deleteInteractionSource(
        TenantUtil.DEFAULT_TENANT_ID, mailboxInteractionSource.getId());
  }



  @AfterEach
  protected void tearDown() {
    // Stop the GreenMail server
    greenMail.stop();
  }


  private InteractionSource getFitLifeCustomerServiceMailboxInteractionSource() {
    return InteractionSource.createMailboxInteractionSource(
        UUID.randomUUID(),
        TenantUtil.DEFAULT_TENANT_ID,
        "FitLife Customer Service Mailbox",
        ENABLE_GREEN_MAIL_SECURITY ? MailboxProtocol.STANDARD_IMAPS : MailboxProtocol.STANDARD_IMAP,
        "localhost",
        ENABLE_GREEN_MAIL_SECURITY ? 3993 : 3143,
        TO_USERNAME,
        TO_PASSWORD,
        TO_EMAIL_ADDRESS,
        true,
        false,
        true);
  }

  private Message getOriginalOutlookHTMLMessageWithImage(Session session) throws Exception {
    MimeMessage message = new MimeMessage(session);

    message.setFrom(new InternetAddress(FROM_EMAIL_ADDRESS, FROM_NAME));

    message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO_EMAIL_ADDRESS, TO_NAME));

    message.setSubject("This is the test subject");

    // Create the HTML body part
    MimeBodyPart htmlBodyPart = new MimeBodyPart();
    htmlBodyPart.setContent(
        new String(
            ResourceUtil.getClasspathResource("OriginalOutlookHtmlMessage.html"),
            StandardCharsets.UTF_8),
        "text/html");

    // Create the embedded image body part
    BodyPart imageBodyPart = new MimeBodyPart();
    imageBodyPart.setFileName("image001.jpg");
    imageBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Image.jpeg"), "image/jpeg; name=image001.jpg")));
    imageBodyPart.setHeader("Content-ID", "<image001.jpg>");
    imageBodyPart.setDisposition(MimeBodyPart.INLINE);

    // Combine the HTML and image into a multipart/related mime multipart
    Multipart relatedMultiPart = new MimeMultipart("related");
    relatedMultiPart.addBodyPart(htmlBodyPart);
    relatedMultiPart.addBodyPart(imageBodyPart);

    // Create a wrapper for the multipart/related mime multipart
    MimeBodyPart relatedBodyPart = new MimeBodyPart();
    relatedBodyPart.setContent(relatedMultiPart);

    // Create the PDF attachment body part
    BodyPart pdfBodyPart = new MimeBodyPart();
    pdfBodyPart.setFileName("MultiPagePdf.pdf");
    pdfBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("MultiPagePdf.pdf"),
                "application/pdf; name=MultiPagePdf.pdf")));
    pdfBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the TIFF attachment body part
    BodyPart tiffBodyPart = new MimeBodyPart();
    tiffBodyPart.setFileName("MultiPageTiff.tif");
    tiffBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("MultiPageTiff.tif"),
                "image/tiff; name=MultiPageTiff.tif")));
    tiffBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the BMP attachment body part
    BodyPart bmpBodyPart = new MimeBodyPart();
    bmpBodyPart.setFileName("Test.bmp");
    bmpBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.bmp"), "image/bmp; name=Test.bmp")));
    bmpBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the GIF attachment body part
    BodyPart gifBodyPart = new MimeBodyPart();
    gifBodyPart.setFileName("Test.gif");
    gifBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.gif"), "image/gif; name=Test.gif")));
    gifBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the JPEG attachment body part
    BodyPart jpgBodyPart = new MimeBodyPart();
    jpgBodyPart.setFileName("Test.jpg");
    jpgBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.jpg"), "image/jpeg; name=Test.jpg")));
    jpgBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the PNG attachment body part
    BodyPart pngBodyPart = new MimeBodyPart();
    pngBodyPart.setFileName("Test.png");
    pngBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.png"), "image/png; name=Test.png")));
    pngBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the WEBP attachment body part
    BodyPart webpBodyPart = new MimeBodyPart();
    webpBodyPart.setFileName("Test.webp");
    webpBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.webp"), "image/webp; name=Test.webp")));
    webpBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the Microsoft Word attachment body part
    BodyPart microsoftWordBodyPart = new MimeBodyPart();
    microsoftWordBodyPart.setFileName("Test.docx");
    microsoftWordBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.docx"),
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document; name=Test.docx")));
    microsoftWordBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the Microsoft PowerPoint attachment body part
    BodyPart microsoftPowerPointBodyPart = new MimeBodyPart();
    microsoftPowerPointBodyPart.setFileName("Test.pptx");
    microsoftPowerPointBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.pptx"),
                "application/vnd.openxmlformats-officedocument.presentationml.presentation; name=Test.pptx")));
    microsoftPowerPointBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the Microsoft Excel attachment body part
    BodyPart microsoftExcelBodyPart = new MimeBodyPart();
    microsoftExcelBodyPart.setFileName("Test.xlsx");
    microsoftExcelBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.xlsx"),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; name=Test.xlsx")));
    microsoftExcelBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the text attachment body part
    BodyPart textBodyPart = new MimeBodyPart();
    textBodyPart.setFileName("Test.txt");
    textBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.txt"), "text/plain; name=Test.txt")));
    textBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Create the CSV attachment body part
    BodyPart csvBodyPart = new MimeBodyPart();
    csvBodyPart.setFileName("Test.csv");
    csvBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Test.csv"), "text/csv; name=Test.csv")));
    csvBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    /*
     * Combine the multipart/related body part and the attachment body parts into a multipart/mixed mime multipart
     */
    Multipart mixedMultipart = new MimeMultipart("mixed");
    mixedMultipart.addBodyPart(relatedBodyPart);
    mixedMultipart.addBodyPart(pdfBodyPart);
    mixedMultipart.addBodyPart(tiffBodyPart);
    mixedMultipart.addBodyPart(bmpBodyPart);
    mixedMultipart.addBodyPart(gifBodyPart);
    mixedMultipart.addBodyPart(jpgBodyPart);
    mixedMultipart.addBodyPart(pngBodyPart);
    mixedMultipart.addBodyPart(webpBodyPart);
    mixedMultipart.addBodyPart(microsoftWordBodyPart);
    mixedMultipart.addBodyPart(microsoftPowerPointBodyPart);
    mixedMultipart.addBodyPart(microsoftExcelBodyPart);
    mixedMultipart.addBodyPart(textBodyPart);
    mixedMultipart.addBodyPart(csvBodyPart);

    // Send the complete message parts
    message.setContent(mixedMultipart);

    return message;
  }

  private String randomId() {
    return String.format("%04X", secureRandom.nextInt(0x10000));
  }

  private void waitForInteractionToProcess(UUID tenantId, UUID interactionId) throws Exception {
    for (int i = 0; i < 50; i++) {
      Interaction interaction = interactionService.getInteraction(tenantId, interactionId);

      if (interaction.getStatus() == InteractionStatus.AVAILABLE) {
        return;
      }

      Thread.sleep(250);
    }

    throw new RuntimeException(
        "Timed out waiting for the interaction ("
        + interactionId
        + ") for tenant ("
        + tenantId
        + ") to process");
  }

  @BeforeEach
  protected void setUp() {
    // Start a GreenMail server with the IMAP protocol
    if (ENABLE_GREEN_MAIL_SECURITY) {
      greenMail = new GreenMail(ServerSetupTest.SMTPS_IMAPS);
    } else {
      greenMail = new GreenMail(ServerSetupTest.SMTP_IMAP);
    }

    GreenMailUser greenMailUser = greenMail.setUser(TO_EMAIL_ADDRESS, TO_USERNAME, TO_PASSWORD);

    greenMail.start();
  }

}
