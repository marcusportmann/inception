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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.MimeData;
import digital.inception.core.util.ResourceUtil;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.MailboxInteractionSource;
import digital.inception.operations.model.MailboxInteractionSourceNotFoundException;
import digital.inception.operations.model.MailboxProtocol;
import digital.inception.operations.model.WhatsAppInteractionSource;
import digital.inception.operations.model.WhatsAppInteractionSourceNotFoundException;
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
import java.util.List;
import java.util.Properties;
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

/**
 * The <b>InteractionServiceTests</b> class contains the JUnit tests for the <b>InteractionService<b> class.
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
public class InteractionServiceTests {

  private static final boolean ENABLE_GREEN_MAIL_SECURITY = false;

  private static final String FROM_EMAIL_ADDRESS = "bsmith@example.com";

  private static final String FROM_NAME = "Bob Smith";

  private static final String TO_EMAIL_ADDRESS = "service@fitlife.com";

  private static final String TO_NAME = "FitLife Customer Service";

  private static final String TO_PASSWORD = "Password1";

  private static final String TO_USERNAME = "service@fitlife.com";

  private static int emailCount;

  private GreenMail greenMail;

  /** The Operations Service. */
  @Autowired private InteractionService interactionService;

  /** Test sending and receiving a multipart/mixed message using the GreenMail server. */
  @Test
  public void greenMailMultipartMixedTest() throws Exception {
    // Create the Original Outlook HTML Message With Image
    Message originalOutlookHTMLMessageWithImage =
        getOriginalOutlookHTMLMessageWithImage(
            ENABLE_GREEN_MAIL_SECURITY
                ? greenMail.getSmtps().createSession()
                : greenMail.getSmtp().createSession());

    // Send message
    Transport.send(originalOutlookHTMLMessageWithImage);

    // Wait for mail to arrive
    assertTrue(greenMail.waitForIncomingEmail(5000, 1));

    Properties properties = new Properties();

    if (ENABLE_GREEN_MAIL_SECURITY) {
      properties.put("mail.store.protocol", "imaps");
      properties.put("mail.imaps.host", "localhost");
      properties.put("mail.imaps.port", "3993");
      properties.put("mail.imaps.ssl.enable", "true");
      properties.put("mail.imaps.starttls.enable", "true");
      properties.put("mail.imaps.ssl.checkserveridentity", "false");
      properties.put("mail.imaps.ssl.trust", "*");
    } else {
      properties.put("mail.store.protocol", "imap");
      properties.put("mail.imap.host", "localhost");
      properties.put("mail.imap.port", "3143");
    }

    // Create session and store
    Session session = Session.getInstance(properties);
    Store store = session.getStore(ENABLE_GREEN_MAIL_SECURITY ? "imaps" : "imap");

    store.connect("localhost", ENABLE_GREEN_MAIL_SECURITY ? 3993 : 3143, TO_USERNAME, TO_PASSWORD);

    // Access inbox
    Folder inboxFolder = store.getFolder("INBOX");
    inboxFolder.open(Folder.READ_ONLY);

    Message[] retrievedMessages = inboxFolder.getMessages();

    assertEquals(1, retrievedMessages.length);

    System.out.println("[DEBUG] Message: " + MessageUtil.messageToString(retrievedMessages[0]));

    MimeData messageContent = MessageUtil.getMessageContent(retrievedMessages[0]);

    List<MimeData> messageAttachments = MessageUtil.getMessageAttachments(retrievedMessages[0]);

    assertEquals(10, messageAttachments.size());

    String content = messageContent.getDataAsString();

    assertEquals(
        new String(
            ResourceUtil.getClasspathResource("OriginalOutlookHtmlMessage.html"),
            StandardCharsets.UTF_8),
        content);

    inboxFolder.close(false);
    store.close();
  }

  /** Test the IMAP interaction functionality. */
  @Test
  public void imapInteractionTest() throws Exception {}

  /** Test the mailbox interaction source functionality. */
  @Test
  public void mailboxInteractionSourceTest() throws Exception {

    MailboxInteractionSource mailboxInteractionSource =
        getFitLifeCustomerServiceMailboxInteractionSource();

    interactionService.createMailboxInteractionSource(mailboxInteractionSource);

    MailboxInteractionSource retrievedMailboxInteractionSource =
        interactionService.getMailboxInteractionSource(mailboxInteractionSource.getId());

    compareMailboxInteractionSources(mailboxInteractionSource, retrievedMailboxInteractionSource);

    List<MailboxInteractionSource> retrievedMailboxInteractionSources =
        interactionService.getMailboxInteractionSources();

    assertEquals(1, retrievedMailboxInteractionSources.size());

    compareMailboxInteractionSources(
        mailboxInteractionSource, retrievedMailboxInteractionSources.getFirst());

    // Create the Original Outlook HTML Message With Image
    Message originalOutlookHTMLMessageWithImage =
        getOriginalOutlookHTMLMessageWithImage(greenMail.getSmtp().createSession());

    // Send message
    Transport.send(originalOutlookHTMLMessageWithImage);

    // Wait for mail to arrive
    assertTrue(greenMail.waitForIncomingEmail(5000, 1));

    int numberOfNewInteractions =
        interactionService.synchronizeMailboxInteractionSource(mailboxInteractionSource);

    assertEquals(1, numberOfNewInteractions);

    numberOfNewInteractions =
        interactionService.synchronizeMailboxInteractionSource(mailboxInteractionSource);

    assertEquals(0, numberOfNewInteractions);

    InteractionSummaries retrievedInteractionSummaries =
        interactionService.getInteractionSummaries(
            mailboxInteractionSource.getId(), null, null, null, null, null, null);

    retrievedInteractionSummaries =
        interactionService.getInteractionSummaries(
            mailboxInteractionSource.getId(),
            InteractionStatus.RECEIVED,
            "test",
            InteractionSortBy.TIMESTAMP,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, retrievedInteractionSummaries.getInteractionSummaries().size());

    retrievedInteractionSummaries =
        interactionService.getInteractionSummaries(
            mailboxInteractionSource.getId(),
            InteractionStatus.RECEIVED,
            "bob",
            InteractionSortBy.TIMESTAMP,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, retrievedInteractionSummaries.getInteractionSummaries().size());

    retrievedInteractionSummaries =
        interactionService.getInteractionSummaries(
            mailboxInteractionSource.getId(),
            InteractionStatus.RECEIVED,
            "xxx",
            InteractionSortBy.TIMESTAMP,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(0, retrievedInteractionSummaries.getInteractionSummaries().size());

    interactionService.deleteMailboxInteractionSource(mailboxInteractionSource.getId());

    assertThrows(
        MailboxInteractionSourceNotFoundException.class,
        () -> {
          interactionService.getMailboxInteractionSource(mailboxInteractionSource.getId());
        });
  }

  /** Test the Microsoft 365 IMAP interaction functionality. */
  @Test
  public void microsoft365ImapInteractionTest() throws Exception {
    MailboxInteractionSource mailboxInteractionSource = getEBIndexingTestMailboxInteractionSource();

    interactionService.synchronizeMailboxInteractionSource(mailboxInteractionSource);

    // interactionService.synchronizeMailboxInteractionSource(mailboxInteractionSource);
  }

  /** Test the WhatsApp interaction source functionality. */
  @Test
  public void whatsAppInteractionSourceTest() throws Exception {

    WhatsAppInteractionSource whatsAppInteractionSource =
        new WhatsAppInteractionSource(
            "fit_life_customer_service_whatsapp", "FitLife Customer Service WhatsApp");

    interactionService.createWhatsAppInteractionSource(whatsAppInteractionSource);

    WhatsAppInteractionSource retrievedWhatsAppInteractionSource =
        interactionService.getWhatsAppInteractionSource(whatsAppInteractionSource.getId());

    compareWhatsAppInteractionSources(
        whatsAppInteractionSource, retrievedWhatsAppInteractionSource);

    List<WhatsAppInteractionSource> retrievedWhatsAppInteractionSources =
        interactionService.getWhatsAppInteractionSources();

    assertEquals(1, retrievedWhatsAppInteractionSources.size());

    compareWhatsAppInteractionSources(
        whatsAppInteractionSource, retrievedWhatsAppInteractionSources.getFirst());

    interactionService.deleteWhatsAppInteractionSource(whatsAppInteractionSource.getId());

    assertThrows(
        WhatsAppInteractionSourceNotFoundException.class,
        () -> {
          interactionService.getWhatsAppInteractionSource(whatsAppInteractionSource.getId());
        });
  }

  @BeforeEach
  protected void setUp() {
    // Start a GreenMail server with the IMAP protocol
    if (ENABLE_GREEN_MAIL_SECURITY) {
      greenMail = new GreenMail(ServerSetupTest.SMTPS_IMAPS);
    } else {
      greenMail = new GreenMail(ServerSetupTest.SMTP_IMAP);
    }

    GreenMailUser toUser = greenMail.setUser(TO_EMAIL_ADDRESS, TO_USERNAME, TO_PASSWORD);

    greenMail.start();
  }

  @AfterEach
  protected void tearDown() {
    // Stop the GreenMail server
    greenMail.stop();
  }

  private void compareMailboxInteractionSources(
      MailboxInteractionSource mailboxInteractionSource1,
      MailboxInteractionSource mailboxInteractionSource2) {
    assertEquals(
        mailboxInteractionSource1.getId(),
        mailboxInteractionSource2.getId(),
        "The ID values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getType(),
        mailboxInteractionSource2.getType(),
        "The type values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getName(),
        mailboxInteractionSource2.getName(),
        "The name values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getProtocol(),
        mailboxInteractionSource2.getProtocol(),
        "The protocol values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getHost(),
        mailboxInteractionSource2.getHost(),
        "The host values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getPort(),
        mailboxInteractionSource2.getPort(),
        "The port values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getAuthority(),
        mailboxInteractionSource2.getAuthority(),
        "The authority values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getPrincipal(),
        mailboxInteractionSource2.getPrincipal(),
        "The principal values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getCredential(),
        mailboxInteractionSource2.getCredential(),
        "The credential values for the mailbox interaction");
    assertEquals(
        mailboxInteractionSource1.getEmailAddress(),
        mailboxInteractionSource2.getEmailAddress(),
        "The email address values for the mailbox interaction");
  }

  private void compareWhatsAppInteractionSources(
      WhatsAppInteractionSource whatsAppInteractionSource1,
      WhatsAppInteractionSource whatsAppInteractionSource2) {
    assertEquals(
        whatsAppInteractionSource1.getId(),
        whatsAppInteractionSource2.getId(),
        "The ID values for the WhatsApp interaction");
    assertEquals(
        whatsAppInteractionSource1.getType(),
        whatsAppInteractionSource2.getType(),
        "The type values for the WhatsApp interaction");
    assertEquals(
        whatsAppInteractionSource1.getName(),
        whatsAppInteractionSource2.getName(),
        "The name values for the WhatsApp interaction");
  }

  private MailboxInteractionSource getFitLifeCustomerServiceMailboxInteractionSource() {
    return new MailboxInteractionSource(
        "fit_life_customer_service_mailbox",
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

    message.setSubject("This is test subject");

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
}
