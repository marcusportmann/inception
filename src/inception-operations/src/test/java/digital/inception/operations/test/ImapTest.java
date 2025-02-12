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

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.IClientCredential;
import digital.inception.core.util.MimeData;
import digital.inception.core.util.ResourceUtil;
import digital.inception.operations.util.HtmlToMarkdown;
import digital.inception.operations.util.HtmlToSimplifiedHtml;
import digital.inception.operations.util.MessageUtil;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.BodyPart;
import jakarta.mail.Flags.Flag;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.ContentType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/** The <b>ImapTest</b> class. */
public class ImapTest {
  /** The Application ID (Client ID) of the Azure Enterprise Application */
  private static final String CLIENT_ID = "REPLACE_WITH_AZURE_ENTERPRISE_APPLICATION_ID";

  private static final String CLIENT_SECRET = "REPLACE_WITH_AZURE_ENTERPRISE_APPLICATION_SECRET";

  private static final String FROM_EMAIL_ADDRESS = "Inception_Indexing_Test@inception.digital";

  private static final String FROM_NAME = "Inception_Indexing_Test";

  /** The Office 365 tenant ID. */
  private static final String TENANT_ID = "REPLACE_WITH_OFFICE_365_TENANT_ID";

  private static final String TO_EMAIL_ADDRESS = "marcus@inception.digital";

  private static final String TO_NAME = "Marcus Portmann";

  private static final boolean verbose = false;

  public static void main(String[] args) {
    try {
      IAuthenticationResult accessToken =
          getAuthenticationWithClientCredentialGrant(TENANT_ID, CLIENT_ID, CLIENT_SECRET);

      String imapFolder = "Inbox";

      if (verbose) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "TRACE");
      }

      Properties properties = new Properties();
      if (verbose) {
        properties.put("mail.debug", "true");
        properties.put("mail.debug.auth", "true");
      }

      properties.put("mail.smtp.host", "smtp.office365.com");
      properties.put("mail.smtp.port", "587");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.starttls.enable", "true"); // TLS
      properties.put("mail.smtp.user", "true"); // TLS

      properties.put("mail.store.protocol", "imaps");
      properties.put("mail.imaps.host", "outlook.office365.com");
      properties.put("mail.imaps.port", "993");
      properties.put("mail.imaps.ssl.enable", "true");
      properties.put("mail.imaps.starttls.enable", "true");
      properties.put("mail.imaps.auth", "true");
      properties.put("mail.imaps.auth.mechanisms", "XOAUTH2");
      properties.put("mail.imaps.user", FROM_EMAIL_ADDRESS);

      properties.put("mail.imaps.auth.plain.disable", "true");
      properties.put("mail.imaps.auth.xoauth2.disable", "false");

      System.out.println("Trying to access IMAP mailbox with properties ");
      properties.forEach((k, v) -> System.out.println(k + "=" + v));

      Authenticator authenticator =
          new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
              try {
                String accessToken =
                    getAuthenticationWithClientCredentialGrant(TENANT_ID, CLIENT_ID, CLIENT_SECRET)
                        .accessToken();
                return new PasswordAuthentication(FROM_EMAIL_ADDRESS, accessToken);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }
          };

      // access mailbox....
      Session session = Session.getInstance(properties, authenticator);
      session.setDebug(verbose);

      if (false) {
        sendTestMessage(session);
        return;
      }

      Store store = session.getStore("imaps");

      store.connect();
      System.out.println("[OK] Mail Store connected.");
      System.out.println("Now opening " + imapFolder);

      Folder inboxFolder = store.getFolder(imapFolder);
      // inboxFolder.open(Folder.READ_ONLY);
      inboxFolder.open(Folder.READ_WRITE);
      Message[] messages = inboxFolder.getMessages();

      boolean processAllMessages = true;

      int messageNumber = 0;

      for (Message message : messages) {
        if (message.isSet(Flag.SEEN) && (!processAllMessages)) {
          System.out.println(
              "[DEBUG][" + messageNumber + "] Seen Message Subject: " + message.getSubject());

          // NOTE: IF a message cannot be processed it can be marked as not being seen,
          //       so it can be processed later.
          message.setFlag(Flag.SEEN, false);
        } else {
          System.out.println(
              "[DEBUG][" + messageNumber + "] Message: " + MessageUtil.messageToString(message));

          if (true) {
            Enumeration<Header> messageHeaders = message.getAllHeaders();

            while (messageHeaders.hasMoreElements()) {
              Header messageHeader = messageHeaders.nextElement();

              System.out.println(
                  "[DEBUG]["
                      + messageNumber
                      + "]   Message Header: "
                      + messageHeader.getName()
                      + ") = ("
                      + messageHeader.getValue()
                      + ")");
            }
          }

          //          if (messageNumber == 10) {
          //
          //          } else {
          //            messageNumber++;
          //            continue;
          //          }

          if (true) {
            MimeData messageContent = MessageUtil.getMessageContent(message);

            writeFile(
                "Content-%d%s"
                    .formatted(
                        messageNumber, messageContent.isMimeType("text/html") ? ".html" : ".txt"),
                messageContent.getData());

            List<MimeData> messageAttachments =
                MessageUtil.getMessageAttachments(message, 10 * 1024);

            if (message.getContent() instanceof MimeMultipart messageMimeMultipartContent) {
              processMessageMimeMultipart(messageMimeMultipartContent, messageNumber, 0);
            }
          }
        }

        System.out.println();
        System.out.println();

        messageNumber++;
      }

      inboxFolder.close(false);
      store.close();
    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  private static String bodyPartToString(BodyPart bodyPart) throws IOException, MessagingException {
    String contentType =
        bodyPart
            .getContentType()
            .replace("\r\n", "")
            .replace("\n", "")
            .replace("\t", "")
            .replace("\"", "'");

    return ("Content Type=\"%s\", Content Class=\"%s\", Disposition=\"%s\", Size=\"%d\", "
            + "File Name=\"%s\", Line Count=\"%d\", Description=\"%s\"")
        .formatted(
            contentType,
            bodyPart.getContent().getClass().getName(),
            bodyPart.getDisposition(),
            bodyPart.getSize(),
            bodyPart.getFileName(),
            bodyPart.getLineCount(),
            bodyPart.getDescription());
  }

  private static IAuthenticationResult getAuthenticationWithClientCredentialGrant(
      String tenantId, String clientId, String clientSecret) throws Exception {
    ConfidentialClientApplication app = null;

    String authority = String.format("https://login.microsoftonline.com/%s/", tenantId);

    IClientCredential clientCredential = ClientCredentialFactory.createFromSecret(clientSecret);

    try {
      app =
          ConfidentialClientApplication.builder(clientId, clientCredential)
              .authority(authority)
              .build();
    } catch (MalformedURLException e) {
      throw new RuntimeException("Authority URL is malformed: " + authority, e);
    }

    String scopes = "https://outlook.office365.com/.default";

    ClientCredentialParameters clientCredentialParam =
        ClientCredentialParameters.builder(
                Arrays.stream(scopes.split(",")).map(String::trim).collect(Collectors.toSet()))
            .build();

    CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
    try {
      return future.get();
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException("Failed to acquire the OAuth token: " + e.getMessage(), e);
    }
  }

  private static void processMessageMimeMultipart(
      MimeMultipart mimeMultipart, int messageNumber, int level)
      throws IOException, MessagingException {
    for (int bodyPartIndex = 0; bodyPartIndex < mimeMultipart.getCount(); bodyPartIndex++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(bodyPartIndex);

      System.out.printf(
          "[DEBUG][%d][%d][%d] %sBody Part: %s%n",
          messageNumber,
          level,
          bodyPartIndex,
          "  ".repeat(Math.max(0, level)),
          bodyPartToString(bodyPart));

      if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          if (bodyPart.isMimeType("text/plain")) {
            writeFile(
                "Content-%d-%d-%d.txt".formatted(messageNumber, level, bodyPartIndex), inputStream);
          } else if (bodyPart.isMimeType("text/html")) {
            byte[] data = inputStream.readAllBytes();

            writeFile("Content-%d-%d-%d.html".formatted(messageNumber, level, bodyPartIndex), data);

            ContentType contentType = new ContentType(bodyPart.getContentType());

            Charset charset =
                Charset.forName(contentType.getParameter("charset"), StandardCharsets.UTF_8);

            String markdownData = HtmlToMarkdown.convertToMarkdown(new String(data, charset));

            writeFile(
                "Content-%d-%d-%d.md".formatted(messageNumber, level, bodyPartIndex),
                markdownData.getBytes(StandardCharsets.UTF_8));

            String simplifiedHtml =
                HtmlToSimplifiedHtml.convertToSimplifiedHtml(new String(data, charset));

            writeFile(
                "Content-%d-%d-%d-Simplified.html".formatted(messageNumber, level, bodyPartIndex),
                simplifiedHtml.getBytes(StandardCharsets.UTF_8));
          }
        }
      } else if (bodyPart.isMimeType("application/pdf")) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          writeFile("PDF-%d-%d-%d.pdf".formatted(messageNumber, level, bodyPartIndex), inputStream);
        }
      } else if (bodyPart.isMimeType("image/bmp")) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          writeFile(
              "Image-%d-%d-%d.bmp".formatted(messageNumber, level, bodyPartIndex), inputStream);
        }
      } else if (bodyPart.isMimeType("image/gif")) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          writeFile(
              "Image-%d-%d-%d.gif".formatted(messageNumber, level, bodyPartIndex), inputStream);
        }
      } else if (bodyPart.isMimeType("image/jpeg")) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          writeFile(
              "Image-%d-%d-%d.jpeg".formatted(messageNumber, level, bodyPartIndex), inputStream);
        }
      } else if (bodyPart.isMimeType("image/png")) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          writeFile(
              "Image-%d-%d-%d.png".formatted(messageNumber, level, bodyPartIndex), inputStream);
        }
      } else if (bodyPart.isMimeType("image/tiff")) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          writeFile(
              "Image-%d-%d-%d.tiff".formatted(messageNumber, level, bodyPartIndex), inputStream);
        }
      } else if (bodyPart.isMimeType("multipart/mixed")
          || bodyPart.isMimeType("multipart/related")) {
        if (bodyPart.getContent() instanceof MimeMultipart messageBodyPartMimeMultipartContent) {
          processMessageMimeMultipart(
              messageBodyPartMimeMultipartContent, messageNumber, level + 1);
        }
      }
    }
  }

  private static void sendTestMessage(Session session)
      throws MessagingException, UnsupportedEncodingException {
    Properties properties = new Properties();
    if (verbose) {
      properties.put("mail.debug", "true");
      properties.put("mail.debug.auth", "true");
    }

    MimeMessage message = new MimeMessage(session);

    // message.setFrom(new InternetAddress(FROM_EMAIL_ADDRESS, FROM_NAME));
    message.setFrom(new InternetAddress("marcus@inception.digital"));

    message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO_EMAIL_ADDRESS, TO_NAME));

    message.setSubject("This is test subject");

    // Create the HTML body part
    MimeBodyPart htmlBodyPart = new MimeBodyPart();
    String htmlText =
        "<h1>This is the HTML message body</h1>"
            + "<p>This is an image embedded in the email:</p>"
            + "<img src=\"cid:image001.jpg\">";
    htmlBodyPart.setContent(htmlText, "text/html");

    // Create the embedded image body part
    BodyPart imageBodyPart = new MimeBodyPart();
    imageBodyPart.setFileName("image001.jpg");
    imageBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("Image.jpeg"), "image/jpeg; name=image001.jpg")));
    imageBodyPart.setHeader("Content-ID", "<image001.jpg>");
    imageBodyPart.setDisposition(MimeBodyPart.INLINE);

    // Create the PDF attachment body part
    BodyPart pdfBodyPart = new MimeBodyPart();
    pdfBodyPart.setFileName("MultiPagePdf.pdf");
    pdfBodyPart.setDataHandler(
        new DataHandler(
            new ByteArrayDataSource(
                ResourceUtil.getClasspathResource("MultiPagePdf.pdf"),
                "application/pdf; name=MultiPagePdf.pdf")));
    pdfBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

    // Combine the HTML and image into a multipart/related mime multipart
    Multipart relatedMultiPart = new MimeMultipart("related");
    relatedMultiPart.addBodyPart(htmlBodyPart);
    relatedMultiPart.addBodyPart(imageBodyPart);

    // Create a wrapper for the multipart/related mime multipart
    MimeBodyPart relatedBodyPart = new MimeBodyPart();
    relatedBodyPart.setContent(relatedMultiPart);

    // Combine the multipart/related mime multipart and PDF into a multipart/mixed mime multipart
    Multipart mixedMultipart = new MimeMultipart("mixed");
    mixedMultipart.addBodyPart(relatedBodyPart);
    mixedMultipart.addBodyPart(pdfBodyPart);

    // Send the complete message parts
    message.setContent(mixedMultipart);

    // Send message
    Transport.send(message);
  }

  private static void writeFile(String filename, InputStream inputStream) {
    try (OutputStream outputStream = new FileOutputStream(filename)) {
      outputStream.write(inputStream.readAllBytes());
    } catch (Throwable e) {
      System.err.println("Failed to write the data to the file (" + filename + ")");
      e.printStackTrace(System.err);
    }
  }

  private static void writeFile(String filename, byte[] data) {
    try (OutputStream outputStream = new FileOutputStream(filename)) {
      outputStream.write(data);
    } catch (Throwable e) {
      System.err.println("Failed to write the data to the file (" + filename + ")");
      e.printStackTrace(System.err);
    }
  }
}
