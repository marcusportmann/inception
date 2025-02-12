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

package digital.inception.operations.util;

import digital.inception.core.util.MimeData;
import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The <b>MessageUtil</b> class is a utility class which provides methods for working with Java Mail
 * messages.
 *
 * @author Marcus Portmann
 */
public final class MessageUtil {

  /** The HTML MIME types. */
  public static final List<String> HTML_MIME_TYPES = List.of("text/html");

  /** The image attachment MIME types. */
  public static final List<String> IMAGE_MIME_TYPES =
      Arrays.asList(
          "image/jpeg", "image/png", "image/tiff", "image/gif", "image/bmp", "image/webp");

  /** The Microsoft Office MIME types. */
  public static final List<String> MICROSOFT_OFFICE_MIME_TYPES =
      Arrays.asList(
          "application/msword",
          "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
          "application/vnd.ms-excel",
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
          "application/vnd.ms-powerpoint",
          "application/vnd.openxmlformats-officedocument.presentationml.presentation");

  /** The PDF MIME types. */
  public static final List<String> PDF_MIME_TYPES =
      Arrays.asList("application/pdf", "application/pdfa", "application/x-pdf");

  /** The plain text MIME types. */
  public static final List<String> PLAIN_TEXT_MIME_TYPES = List.of("text/plain");

  /** The supported image attachment MIME types. */
  public static final List<String> SUPPORTED_IMAGE_ATTACHMENT_MIME_TYPES =
      Arrays.asList("image/jpeg", "image/png", "image/tiff");

  /** The supported text attachment MIME types. */
  public static final List<String> SUPPORTED_TEXT_ATTACHMENT_MIME_TYPES =
      Arrays.asList("text/plain", "text/xml", "text/csv");

  /** Private constructor to prevent instantiation. */
  private MessageUtil() {}

  /**
   * Retrieve the attachments for the message.
   *
   * @param message the message
   * @param minimumImageAttachmentSize the minimum image attachment size
   * @return the attachments for the message
   */
  public static List<MimeData> getMessageAttachments(
      Message message, int minimumImageAttachmentSize) {
    try {
      List<MimeData> attachments = new ArrayList<>();

      if (message.isMimeType("multipart/*")) {
        if (message.getContent() instanceof Multipart multipart) {
          extractAttachmentsFromMultipart(multipart, attachments, minimumImageAttachmentSize);
        }
      }

      return attachments;
    } catch (Throwable e) {
      throw new MessageException(
          "Failed to retrieve the attachments for the message (" + messageToString(message) + ")",
          e);
    }
  }

  /**
   * Retrieve the attachments for the message.
   *
   * @param message the message
   * @return the attachments for the message
   */
  public static List<MimeData> getMessageAttachments(Message message) {
    try {
      List<MimeData> attachments = new ArrayList<>();

      if (message.isMimeType("multipart/*")) {
        if (message.getContent() instanceof Multipart multipart) {
          extractAttachmentsFromMultipart(multipart, attachments, 0);
        }
      }

      return attachments;
    } catch (Throwable e) {
      throw new MessageException(
          "Failed to retrieve the attachments for the message (" + messageToString(message) + ")",
          e);
    }
  }

  /**
   * Retrieve the content for the message.
   *
   * @param message the message
   * @return the content for the message
   */
  public static MimeData getMessageContent(Message message) {
    try {
      if (message.isMimeType("text/html") || message.isMimeType("text/plain")) {
        try (InputStream inputStream = message.getInputStream()) {
          return new MimeData(message.getContentType(), inputStream.readAllBytes());
        }
      } else if (message.isMimeType("multipart/*")) {
        if (message.getContent() instanceof Multipart multipart) {
          MimeData htmlTextMessageContent =
              extractContentWithMimeTypeFromMultipart(multipart, "text/html");

          if (htmlTextMessageContent != null) {
            return htmlTextMessageContent;
          }

          MimeData plainTextMessageContent =
              extractContentWithMimeTypeFromMultipart(multipart, "text/plain");

          if (plainTextMessageContent != null) {
            return plainTextMessageContent;
          }
        }
      }

      throw new MessageException(
          "Unsupported message content type (" + message.getContentType() + ")");
    } catch (Throwable e) {
      throw new MessageException(
          "Failed to retrieve the content for the message (" + messageToString(message) + ")", e);
    }
  }

  /**
   * Returns whether the body part is HTML.
   *
   * @param bodyPart the body part
   * @return <b>true</b> if the body part is HTML or <b>false</b> otherwise
   * @throws MessagingException if an error occurs while checking the body part
   */
  public static boolean isHTML(BodyPart bodyPart) throws MessagingException {
    return HTML_MIME_TYPES.contains(bodyPart.getContentType().split(";")[0].toLowerCase());
  }

  /**
   * Returns whether the body part is an image.
   *
   * @param bodyPart the body part
   * @return <b>true</b> if the body part is an image or <b>false</b> otherwise
   * @throws MessagingException if an error occurs while checking the body part
   */
  public static boolean isImage(BodyPart bodyPart) throws MessagingException {
    return IMAGE_MIME_TYPES.contains(bodyPart.getContentType().split(";")[0].toLowerCase());
  }

  /**
   * Returns whether the body part is a Microsoft Office file.
   *
   * @param bodyPart the body part
   * @return <b>true</b> if the body part is a Microsoft Office file or <b>false</b> otherwise
   * @throws MessagingException if an error occurs while checking the body part
   */
  public static boolean isMicrosoftOfficeFile(BodyPart bodyPart) throws MessagingException {
    return MICROSOFT_OFFICE_MIME_TYPES.contains(
        bodyPart.getContentType().split(";")[0].toLowerCase());
  }

  /**
   * Returns whether the body part is a PDF.
   *
   * @param bodyPart the body part
   * @return <b>true</b> if the body part is a PDF or <b>false</b> otherwise
   * @throws MessagingException if an error occurs while checking the body part
   */
  public static boolean isPDF(BodyPart bodyPart) throws MessagingException {
    return PDF_MIME_TYPES.contains(bodyPart.getContentType().split(";")[0].toLowerCase());
  }

  /**
   * Returns whether the body part is plain text.
   *
   * @param bodyPart the body part
   * @return <b>true</b> if the body part is plain text or <b>false</b> otherwise
   * @throws MessagingException if an error occurs while checking the body part
   */
  public static boolean isPlainText(BodyPart bodyPart) throws MessagingException {
    return PLAIN_TEXT_MIME_TYPES.contains(bodyPart.getContentType().split(";")[0].toLowerCase());
  }

  /**
   * Returns whether the body part is a supported image attachment.
   *
   * @param bodyPart the body part
   * @return <b>true</b> if the body part is a supported image attachment or <b>false</b> otherwise
   * @throws MessagingException if an error occurs while checking the body part
   */
  public static boolean isSupportedImageAttachment(BodyPart bodyPart) throws MessagingException {
    return SUPPORTED_IMAGE_ATTACHMENT_MIME_TYPES.contains(
        bodyPart.getContentType().split(";")[0].toLowerCase());
  }

  /**
   * Returns whether the body part is a supported text attachment.
   *
   * @param bodyPart the body part
   * @return <b>true</b> if the body part is a supported text attachment or <b>false</b> otherwise
   * @throws MessagingException if an error occurs while checking the body part
   */
  public static boolean isSupportedTextAttachment(BodyPart bodyPart) throws MessagingException {
    if ("attachment".equalsIgnoreCase(bodyPart.getDisposition())) {
      return SUPPORTED_TEXT_ATTACHMENT_MIME_TYPES.contains(
          bodyPart.getContentType().split(";")[0].toLowerCase());
    } else {
      return false;
    }
  }

  /**
   * Returns a string representation of the message.
   *
   * @param message the message
   * @return a string representation of the message
   */
  public static String messageToString(Message message) {
    String from;

    try {
      from = addressesToString(message.getFrom());
    } catch (Throwable e) {
      from = "UNKNOWN";
    }

    String to;
    try {
      to = addressesToString(message.getAllRecipients());
    } catch (Throwable e) {
      to = "UNKNOWN";
    }

    String contentType;

    try {
      contentType =
          message
              .getContentType()
              .replace("\r\n", "")
              .replace("\n", "")
              .replace("\t", "")
              .replace("\"", "'");
    } catch (Throwable e) {
      contentType = "UNKNOWN";
    }

    String subject;

    try {
      subject = message.getSubject();
    } catch (Throwable e) {
      subject = "UNKNOWN";
    }

    return "Subject=\"%s\", Content Type=\"%s\", From=\"%s\", To=\"%s\""
        .formatted(subject, contentType, from, to);
  }

  private static String addressesToString(Address[] addresses) {
    StringBuilder buffer = new StringBuilder();

    for (Address address : addresses) {
      if (!buffer.isEmpty()) {
        buffer.append(";");
      }

      buffer.append(address.toString());
    }

    return buffer.toString();
  }

  private static void extractAttachmentsFromMultipart(
      Multipart multipart, List<MimeData> attachments, int minimumImageAttachmentSize)
      throws IOException, MessagingException {
    for (int i = 0; i < multipart.getCount(); i++) {
      BodyPart bodyPart = multipart.getBodyPart(i);

      if (bodyPart.isMimeType("multipart/*")) {
        if (bodyPart.getContent() instanceof Multipart nestedMultipart) {
          extractAttachmentsFromMultipart(nestedMultipart, attachments, minimumImageAttachmentSize);
        }
      } else if (isSupportedTextAttachment(bodyPart)) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          MimeData attachment = new MimeData(bodyPart.getContentType(), inputStream.readAllBytes());
          attachments.add(attachment);
        }
      } else if (isSupportedImageAttachment(bodyPart)) {
        if (bodyPart.getSize() > minimumImageAttachmentSize) {
          try (InputStream inputStream = bodyPart.getInputStream()) {
            MimeData attachment =
                new MimeData(bodyPart.getContentType(), inputStream.readAllBytes());
            attachments.add(attachment);
          }
        }
      } else if (isPDF(bodyPart) || isMicrosoftOfficeFile(bodyPart)) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          MimeData attachment = new MimeData(bodyPart.getContentType(), inputStream.readAllBytes());
          attachments.add(attachment);
        }
      }
    }
  }

  private static MimeData extractContentWithMimeTypeFromMultipart(
      Multipart multipart, String mimeType) throws IOException, MessagingException {
    for (int i = 0; i < multipart.getCount(); i++) {
      BodyPart bodyPart = multipart.getBodyPart(i);
      if (bodyPart.isMimeType(mimeType) && (bodyPart.getDisposition() == null)) {
        try (InputStream inputStream = bodyPart.getInputStream()) {
          return new MimeData(bodyPart.getContentType(), inputStream.readAllBytes());
        }
      } else if (bodyPart.isMimeType("multipart/*")) {
        if (bodyPart.getContent() instanceof Multipart nestedMultipart) {
          MimeData content = extractContentWithMimeTypeFromMultipart(nestedMultipart, mimeType);
          if (content != null) {
            return content;
          }
        }
      }
    }
    return null;
  }
}
