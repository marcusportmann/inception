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

import digital.inception.core.util.ResourceUtil;
import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import java.nio.charset.StandardCharsets;

/**
 * The {@code OperationsTestUtil} class.
 *
 * @author Marcus Portmann
 */
public final class OperationsTestUtil {

  /**
   * Retrieve the original HTML mail message with an embedded image.
   *
   * @param session the Java Mail session
   * @param fromEmailAddress the from email address
   * @param fromName the from name
   * @param toEmailAddress the to email address
   * @param toName the to name
   * @return the original HTML mail message with an embedded image
   * @throws Exception if the original HTML mail message with an embedded image could not be
   *     retrieved
   */
  public static Message getOriginalOutlookHTMLMessageWithEmbeddedImage(
      Session session,
      String fromEmailAddress,
      String fromName,
      String toEmailAddress,
      String toName)
      throws Exception {
    MimeMessage message = new MimeMessage(session);

    message.setFrom(new InternetAddress(fromEmailAddress, fromName));

    message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress, toName));

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
}
