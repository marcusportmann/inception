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
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionMimeType;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourceAttribute;
import digital.inception.operations.model.InteractionSourceType;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.InteractionType;
import digital.inception.operations.model.MailboxProtocol;
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
 * The {@code InteractionServiceTests} class contains the JUnit tests for the {@code
 * InteractionService} class.
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

  /** The secure random number generator. */
  private static final SecureRandom secureRandom = new SecureRandom();

  /** The Background Interaction Source Synchronizer. */
  @Autowired
  private BackgroundInteractionSourceSynchronizer backgroundInteractionSourceSynchronizer;

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

    // Send the message
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

  /** Test the interaction functionality. */
  @Test
  public void interactionTest() throws Exception {
    InteractionSource interactionSource =
        InteractionSource.createVirtualInteractionSource(
            UUID.randomUUID(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Virtual Interaction Source " + randomId());

    interactionService.createInteractionSource(TenantUtil.DEFAULT_TENANT_ID, interactionSource);

    InteractionSource retrievedInteractionSource =
        interactionService.getInteractionSource(
            TenantUtil.DEFAULT_TENANT_ID, interactionSource.getId());

    compareInteractionSources(interactionSource, retrievedInteractionSource);

    Interaction interaction =
        new Interaction(
            UUID.randomUUID(),
            TenantUtil.DEFAULT_TENANT_ID,
            interactionSource.getId(),
            InteractionType.OTHER,
            "test_sender",
            List.of("test_recipient"),
            "Test subject " + randomId(),
            "This is the test content.",
            InteractionMimeType.TEXT_PLAIN,
            InteractionStatus.AVAILABLE);

    interactionService.createInteraction(TenantUtil.DEFAULT_TENANT_ID, interaction);

    Interaction retrievedInteraction =
        interactionService.getInteraction(TenantUtil.DEFAULT_TENANT_ID, interaction.getId());

    compareInteractions(interaction, retrievedInteraction);

    interactionService.deleteInteraction(
        TenantUtil.DEFAULT_TENANT_ID, retrievedInteraction.getId());

    assertThrows(
        InteractionNotFoundException.class,
        () -> interactionService.getInteraction(TenantUtil.DEFAULT_TENANT_ID, interaction.getId()));

    interactionSource.setName("Updated " + interactionSource.getName());

    interactionService.updateInteractionSource(TenantUtil.DEFAULT_TENANT_ID, interactionSource);

    retrievedInteractionSource =
        interactionService.getInteractionSource(
            TenantUtil.DEFAULT_TENANT_ID, interactionSource.getId());

    compareInteractionSources(interactionSource, retrievedInteractionSource);

    interactionService.deleteInteractionSource(
        TenantUtil.DEFAULT_TENANT_ID, interactionSource.getId());

    assertThrows(
        InteractionSourceNotFoundException.class,
        () ->
            interactionService.getInteractionSource(
                TenantUtil.DEFAULT_TENANT_ID, interactionSource.getId()));
  }

  /** Test the mailbox interaction source functionality. */
  @Test
  public void mailboxInteractionSourceTest() throws Exception {

    InteractionSource mailboxInteractionSource =
        getFitLifeCustomerServiceMailboxInteractionSource();

    interactionService.createInteractionSource(
        TenantUtil.DEFAULT_TENANT_ID, mailboxInteractionSource);

    InteractionSource retrievedInteractionSource =
        interactionService.getInteractionSource(
            TenantUtil.DEFAULT_TENANT_ID, mailboxInteractionSource.getId());

    compareInteractionSources(mailboxInteractionSource, retrievedInteractionSource);

    List<InteractionSource> retrievedInteractionSources =
        interactionService.getInteractionSources(TenantUtil.DEFAULT_TENANT_ID);

    assertEquals(1, retrievedInteractionSources.size());

    compareInteractionSources(mailboxInteractionSource, retrievedInteractionSources.getFirst());

    retrievedInteractionSources =
        interactionService.getInteractionSources(
            TenantUtil.DEFAULT_TENANT_ID, InteractionSourceType.MAILBOX);

    assertEquals(1, retrievedInteractionSources.size());

    compareInteractionSources(mailboxInteractionSource, retrievedInteractionSources.getFirst());

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

    numberOfNewInteractions =
        backgroundInteractionSourceSynchronizer.synchronizeInteractionSources();

    assertEquals(0, numberOfNewInteractions);

    waitForInteractionToProcess(TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId);

    retrievedInteractionSummaries =
        interactionService.getInteractionSummaries(
            TenantUtil.DEFAULT_TENANT_ID,
            mailboxInteractionSource.getId(),
            InteractionStatus.AVAILABLE,
            "test",
            InteractionSortBy.OCCURRED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, retrievedInteractionSummaries.getInteractionSummaries().size());

    retrievedInteractionSummaries =
        interactionService.getInteractionSummaries(
            TenantUtil.DEFAULT_TENANT_ID,
            mailboxInteractionSource.getId(),
            InteractionStatus.AVAILABLE,
            "bob",
            InteractionSortBy.OCCURRED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, retrievedInteractionSummaries.getInteractionSummaries().size());

    retrievedInteractionSummaries =
        interactionService.getInteractionSummaries(
            TenantUtil.DEFAULT_TENANT_ID,
            mailboxInteractionSource.getId(),
            InteractionStatus.AVAILABLE,
            "xxx",
            InteractionSortBy.OCCURRED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(0, retrievedInteractionSummaries.getInteractionSummaries().size());

    assertTrue(
        interactionService.interactionExistsWithId(
            TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId));

    Interaction retrievedInteraction =
        interactionService.getInteraction(TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId);

    assertNull(
        retrievedInteraction.getAssigned(),
        "Invalid value for the \"assigned\" interaction property");
    assertNull(
        retrievedInteraction.getAssignedTo(),
        "Invalid value for the \"assignedTo\" interaction property");
    assertTrue(
        StringUtils.hasText(retrievedInteraction.getContent()),
        "Invalid value for the \"content\" interaction property");
    assertTrue(
        retrievedInteraction
            .getContent()
            .startsWith(
                "<html><body><div><p>Hello,</p><p>This is a simple HTML message from Outlook.</p>"),
        "Invalid value for the \"content\" interaction property");
    assertTrue(
        StringUtils.hasText(retrievedInteraction.getConversationId()),
        "Invalid value for the \"conservationId\" interaction property");
    assertNotNull(
        retrievedInteraction.getId(), "Invalid value for the \"id\" interaction property");
    assertEquals(
        InteractionMimeType.TEXT_HTML,
        retrievedInteraction.getMimeType(),
        "Invalid value for the \"mimeType\" interaction property");
    assertNull(
        retrievedInteraction.getPartyId(),
        "Invalid value for the \"partyId\" interaction property");
    assertEquals(
        1,
        retrievedInteraction.getRecipients().size(),
        "Invalid value for the \"recipients\" interaction property");
    assertEquals(
        "FitLife Customer Service <service@fitlife.com>",
        retrievedInteraction.getRecipients().getFirst(),
        "Invalid value for the \"recipients\" interaction property");
    assertEquals(
        "Bob Smith <bsmith@example.com>",
        retrievedInteraction.getSender(),
        "Invalid value for the \"sender\" interaction property");
    assertEquals(
        mailboxInteractionSource.getId(),
        retrievedInteraction.getSourceId(),
        "Invalid value for the \"sourceId\" interaction property");
    assertTrue(
        StringUtils.hasText(retrievedInteraction.getSourceReference()),
        "Invalid value for the \"sourceReference\" interaction property");
    assertEquals(
        InteractionStatus.AVAILABLE,
        retrievedInteraction.getStatus(),
        "Invalid value for the \"status\" interaction property");
    assertEquals(
        "This is the test subject",
        retrievedInteraction.getSubject(),
        "Invalid value for the \"subject\" interaction property");
    assertEquals(
        TenantUtil.DEFAULT_TENANT_ID,
        retrievedInteraction.getTenantId(),
        "Invalid value for the \"tenantId\" interaction property");
    assertNotNull(
        retrievedInteraction.getOccurred(),
        "Invalid value for the \"occurred\" interaction property");
    assertEquals(
        InteractionType.EMAIL,
        retrievedInteraction.getType(),
        "Invalid value for the \"type\" interaction property");

    assertTrue(
        interactionService.interactionExistsWithSourceIdAndSourceReference(
            TenantUtil.DEFAULT_TENANT_ID,
            retrievedInteraction.getSourceId(),
            retrievedInteraction.getSourceReference()));

    InteractionAttachmentSummaries retrievedInteractionAttachmentSummaries =
        interactionService.getInteractionAttachmentSummaries(
            TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId, null, null, null, null, null);

    assertEquals(
        10, retrievedInteractionAttachmentSummaries.getInteractionAttachmentSummaries().size());

    UUID retrievedInteractionAttachmentId =
        retrievedInteractionAttachmentSummaries
            .getInteractionAttachmentSummaries()
            .getFirst()
            .getId();

    UUID anotherRetrievedInteractionAttachmentId =
        retrievedInteractionAttachmentSummaries
            .getInteractionAttachmentSummaries()
            .getLast()
            .getId();

    assertTrue(
        interactionService.interactionAttachmentExistsWithId(
            TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionAttachmentId));

    InteractionAttachment retrievedInteractionAttachment =
        interactionService.getInteractionAttachment(
            TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionAttachmentId);

    assertEquals(
        280201,
        retrievedInteractionAttachment.getData().length,
        "Invalid value for the \"data\" interaction attachment property");
    assertEquals(
        FileType.IMAGE_JPEG,
        retrievedInteractionAttachment.getFileType(),
        "Invalid value for the \"data\" interaction attachment property");
    assertEquals(
        "7216ZhR4cFO/xjdEaY4ubHUiXtKmqzuDiQtxfNtvZDk=",
        retrievedInteractionAttachment.getHash(),
        "Invalid value for the \"hash\" interaction attachment property");
    assertEquals(
        retrievedInteraction.getId(),
        retrievedInteractionAttachment.getInteractionId(),
        "Invalid value for the \"interactionId\" interaction attachment property");
    assertEquals(
        "image001.jpg",
        retrievedInteractionAttachment.getName(),
        "Invalid value for the \"name\" interaction attachment property");
    assertEquals(
        mailboxInteractionSource.getId(),
        retrievedInteractionAttachment.getSourceId(),
        "Invalid value for the \"sourceId\" interaction attachment property");
    assertEquals(
        TenantUtil.DEFAULT_TENANT_ID,
        retrievedInteractionAttachment.getTenantId(),
        "Invalid value for the \"tenantId\" interaction attachment property");

    assertTrue(
        interactionService.interactionAttachmentExistsWithInteractionIdAndHash(
            TenantUtil.DEFAULT_TENANT_ID,
            retrievedInteraction.getId(),
            retrievedInteractionAttachment.getHash()));

    interactionService.deleteInteractionAttachment(
        TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionAttachmentId);

    assertThrows(
        InteractionAttachmentNotFoundException.class,
        () ->
            interactionService.deleteInteractionAttachment(
                TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionAttachmentId));

    assertThrows(
        InteractionAttachmentNotFoundException.class,
        () ->
            interactionService.getInteractionAttachment(
                TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionAttachmentId));

    interactionService.deleteInteraction(TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId);

    assertThrows(
        InteractionNotFoundException.class,
        () ->
            interactionService.deleteInteraction(
                TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId));

    assertThrows(
        InteractionNotFoundException.class,
        () ->
            interactionService.getInteraction(
                TenantUtil.DEFAULT_TENANT_ID, retrievedInteractionId));

    assertThrows(
        InteractionAttachmentNotFoundException.class,
        () ->
            interactionService.getInteractionAttachment(
                TenantUtil.DEFAULT_TENANT_ID, anotherRetrievedInteractionAttachmentId));

    interactionService.deleteInteractionSource(
        TenantUtil.DEFAULT_TENANT_ID, mailboxInteractionSource.getId());

    assertThrows(
        InteractionSourceNotFoundException.class,
        () ->
            interactionService.getInteractionSource(
                TenantUtil.DEFAULT_TENANT_ID, mailboxInteractionSource.getId()));
  }

  /** Test the WhatsApp interaction source functionality. */
  @Test
  public void whatsAppInteractionSourceTest() throws Exception {

    InteractionSource whatsAppInteractionSource =
        InteractionSource.createWhatsAppInteractionSource(
            UUID.randomUUID(),
            TenantUtil.DEFAULT_TENANT_ID,
            "FitLife Customer Service WhatsApp",
            true);

    interactionService.createInteractionSource(
        TenantUtil.DEFAULT_TENANT_ID, whatsAppInteractionSource);

    InteractionSource retrievedInteractionSource =
        interactionService.getInteractionSource(
            TenantUtil.DEFAULT_TENANT_ID, whatsAppInteractionSource.getId());

    compareInteractionSources(whatsAppInteractionSource, retrievedInteractionSource);

    List<InteractionSource> retrievedInteractionSources =
        interactionService.getInteractionSources(
            TenantUtil.DEFAULT_TENANT_ID, InteractionSourceType.WHATSAPP);

    assertEquals(1, retrievedInteractionSources.size());

    compareInteractionSources(whatsAppInteractionSource, retrievedInteractionSources.getFirst());

    interactionService.deleteInteractionSource(
        TenantUtil.DEFAULT_TENANT_ID, whatsAppInteractionSource.getId());

    assertThrows(
        InteractionSourceNotFoundException.class,
        () ->
            interactionService.getInteractionSource(
                TenantUtil.DEFAULT_TENANT_ID, whatsAppInteractionSource.getId()));
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

  @AfterEach
  protected void tearDown() {
    // Stop the GreenMail server
    greenMail.stop();
  }

  private void compareInteractionSources(
      InteractionSource interactionSource1, InteractionSource interactionSource2) {
    assertEquals(
        interactionSource1.getId(),
        interactionSource2.getId(),
        "The ID values for the interaction sources do not match");
    assertEquals(
        interactionSource1.getTenantId(),
        interactionSource2.getTenantId(),
        "The Tenant ID values for the interaction sources do not match");
    assertEquals(
        interactionSource1.getType(),
        interactionSource2.getType(),
        "The type values for the interaction sources do not match");
    assertEquals(
        interactionSource1.getName(),
        interactionSource2.getName(),
        "The name values for the interaction sources do not match");
    assertEquals(
        interactionSource1.getAttributes().size(),
        interactionSource2.getAttributes().size(),
        "The number of attributes for the interaction sources do not match");

    for (InteractionSourceAttribute interactionSourceAttribute1 :
        interactionSource1.getAttributes()) {
      boolean foundInteractionSourceAttribute = false;
      for (InteractionSourceAttribute interactionSourceAttribute2 :
          interactionSource2.getAttributes()) {
        if (StringUtil.equalsIgnoreCase(
            interactionSourceAttribute1.getCode(), interactionSourceAttribute2.getCode())) {
          foundInteractionSourceAttribute = true;

          if (!Objects.equals(
              interactionSourceAttribute1.getValue(), interactionSourceAttribute2.getValue())) {
            fail(
                "The \""
                    + interactionSourceAttribute1.getCode()
                    + "\" attributes for the interaction sources do not match");
          }

          break;
        }
      }

      if (!foundInteractionSourceAttribute) {
        fail(
            "The \""
                + interactionSourceAttribute1.getCode()
                + "\" attributes for the interaction sources do not match");
      }
    }
  }

  private void compareInteractions(Interaction interaction1, Interaction interaction2) {
    assertEquals(
        interaction1.getAssigned(),
        interaction2.getAssigned(),
        "The assigned values for the interactions do not match");
    assertEquals(
        interaction1.getAssignedTo(),
        interaction2.getAssignedTo(),
        "The assigned to values for the interactions do not match");
    assertEquals(
        interaction1.getContent(),
        interaction2.getContent(),
        "The content values for the interactions do not match");
    assertEquals(
        interaction1.getConversationId(),
        interaction2.getConversationId(),
        "The conversation ID values for the interactions do not match");
    assertEquals(
        interaction1.getId(),
        interaction2.getId(),
        "The ID values for the interactions do not match");
    assertEquals(
        interaction1.getLastProcessed(),
        interaction2.getLastProcessed(),
        "The last processed values for the interactions do not match");
    assertEquals(
        interaction1.getLockName(),
        interaction2.getLockName(),
        "The lock name values for the interactions do not match");
    assertEquals(
        interaction1.getLocked(),
        interaction2.getLocked(),
        "The locked values for the interactions do not match");
    assertEquals(
        interaction1.getMimeType(),
        interaction2.getMimeType(),
        "The mime type values for the interactions do not match");
    assertEquals(
        interaction1.getPartyId(),
        interaction2.getPartyId(),
        "The party ID values for the interactions do not match");
    assertEquals(
        interaction1.getProcessed(),
        interaction2.getProcessed(),
        "The processed values for the interactions do not match");
    assertEquals(
        interaction1.getProcessingAttempts(),
        interaction2.getProcessingAttempts(),
        "The processing attempts values for the interactions do not match");
    assertEquals(
        interaction1.getProcessingTime(),
        interaction2.getProcessingTime(),
        "The processing time values for the interactions do not match");
    assertEquals(
        interaction1.getRecipients().size(),
        interaction2.getRecipients().size(),
        "The recipients values for the interactions do not match");
    assertEquals(
        interaction1.getSender(),
        interaction2.getSender(),
        "The sender values for the interactions do not match");
    assertEquals(
        interaction1.getSourceId(),
        interaction2.getSourceId(),
        "The source ID values for the interactions do not match");
    assertEquals(
        interaction1.getSourceReference(),
        interaction2.getSourceReference(),
        "The source reference values for the interactions do not match");
    assertEquals(
        interaction1.getStatus(),
        interaction2.getStatus(),
        "The status values for the interactions do not match");
    assertEquals(
        interaction1.getSubject(),
        interaction2.getSubject(),
        "The subject values for the interactions do not match");
    assertEquals(
        interaction1.getTenantId(),
        interaction2.getTenantId(),
        "The tenant ID values for the interactions do not match");
    assertEquals(
        interaction1.getOccurred(),
        interaction2.getOccurred(),
        "The occurred values for the interactions do not match");
    assertEquals(
        interaction1.getType(),
        interaction2.getType(),
        "The type values for the interactions do not match");
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
}
