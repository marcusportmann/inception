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

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import digital.inception.core.util.ISO8601Util;
import digital.inception.core.util.ResourceUtil;
import digital.inception.test.MarkovTextGenerator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;

/** The <b>MultiPagePdfTest</b> class. */
public class MultiPagePdfTest {

  public static void main(String[] args) throws IOException {
    // Create a new document
    Document document = new Document();

    boolean useMarkovTextGenerator = true;

    try {
      // Create a PdfWriter instance, linking document and output stream
      PdfWriter.getInstance(document, new FileOutputStream("MultiPagePdf.pdf"));

      // Define the font for the document
      BaseFont arialBaseFont =
          BaseFont.createFont(
              "Arial.ttf",
              BaseFont.IDENTITY_H,
              BaseFont.EMBEDDED,
              true,
              ResourceUtil.getClasspathResource("Arial.ttf"),
              null);

      Font headerFont = new Font(arialBaseFont, 16, Font.BOLD);

      Font normalFont = new Font(arialBaseFont, 12);

      // Open the document for writing content
      document.open();

      Paragraph headerParagraph =
          new Paragraph(
              "Generated at " + ISO8601Util.fromOffsetDateTime(OffsetDateTime.now()), headerFont);
      headerParagraph.setSpacingAfter(16);

      document.add(headerParagraph);

      Paragraph paragraph;
      if (useMarkovTextGenerator) {
        MarkovTextGenerator markovTextGenerator = MarkovTextGenerator.getJulesVerneGenerator();

        for (int paragraphCount = 1; paragraphCount <= 10; paragraphCount++) {
          Optional<String> paragraphContentOptional = markovTextGenerator.generate(1500);

          if (paragraphContentOptional.isPresent()) {
            paragraph = new Paragraph(paragraphContentOptional.get(), normalFont);

            paragraph.setSpacingAfter(12);

            document.add(paragraph);
          }
        }
      } else {
        StringBuilder buffer = new StringBuilder();
        for (int wordCount = 1; wordCount <= 800; wordCount++) {
          if (!buffer.isEmpty()) {
            buffer.append(" ");
          }

          buffer.append("word").append(wordCount);
        }

        paragraph = new Paragraph(buffer.toString(), normalFont);

        document.add(paragraph);
      }

      // Close the document
      document.close();
      System.out.println("PDF created with multiple pages.");
    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }
}
