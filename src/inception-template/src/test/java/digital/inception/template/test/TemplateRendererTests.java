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

package digital.inception.template.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import digital.inception.template.TemplateRenderer;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.util.HashMap;
import java.util.Map;
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
import org.w3c.dom.Document;

/**
 * The {@code TemplateRendererTests} class contains the JUnit tests for The {@code TemplateRenderer}
 * class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class TemplateRendererTests {

  /** The Template Renderer. */
  @Autowired private TemplateRenderer templateRenderer;

  /** Test the template renderer. */
  @Test
  public void templateRendererTest() throws Exception {
    // Example 1: XML data with XPath
    String xmlData =
        """
                <document>
                    <title>Hello XML World</title>
                    <content>This is XML content</content>
                    <author>John Doe</author>
                    <metadata>
                        <created>2023-12-07</created>
                    </metadata>
                </document>
                """;
    Document xmlDoc = TemplateRenderer.parseXml(xmlData);

    String xmlTemplate =
        """
                <html>
                    <head><title>{{ xpath:/document/title }}</title></head>
                    <body>
                        <h1>{{ xpath:/document/title }}</h1>
                        <p>{{ uppercase(xpath:/document/content) }}</p>
                        <p>Author: {{ xpath:/document/author }}</p>
                        <p>Created: {{ xpath:/document/metadata/created }}</p>
                        <p>Generated: {{ now('yyyy-MM-dd') }}</p>
                    </body>
                </html>
                """;

    String xmlResult = templateRenderer.render(xmlTemplate, xmlDoc);
    System.out.println("=== XML Result ===");
    System.out.println(xmlResult);

    // Example 2: JSON data with JSONPath
    String jsonData =
        """
                {
                    "title": "Hello JSON World",
                    "content": "This is JSON content",
                    "author": "Jane Smith",
                    "metadata": {
                        "created": "2023-12-07",
                        "tags": ["template", "engine", "json"]
                    }
                }
                """;

    String jsonTemplate =
        """
                <html>
                    <head><title>{{ json:$.title }}</title></head>
                    <body>
                        <h1>{{ json:$.title }}</h1>
                        <p>{{ uppercase(json:$.content) }}</p>
                        <p>Author: {{ json:$.author }}</p>
                        <p>Created: {{ json:$.metadata.created }}</p>
                        <p>Content length: {{ length(json:$.content) }}</p>
                        <p>Generated: {{ now('yyyy-MM-dd HH:mm') }}</p>
                    </body>
                </html>
                """;

    String jsonResult = templateRenderer.render(jsonTemplate, jsonData);
    System.out.println("\n=== JSON Result ===");
    System.out.println(jsonResult);

    // Example 3: Map data with JSONPath
    Map<String, Object> mapData = new HashMap<>();
    mapData.put("title", "Hello Map World");
    mapData.put("content", "This is map content");
    mapData.put("score", 95);

    String mapTemplate =
        """
                <div>
                    <h2>{{ map:$.title }}</h2>
                    <p>{{ lowercase(map:$.content) }}</p>
                    <p>Score: {{ map:$.score }}/100</p>
                    <p>Status: {{ default(map:$.status, 'Unknown') }}</p>
                </div>
                """;

    String mapResult = templateRenderer.render(mapTemplate, mapData);
    System.out.println("\n=== Map Result ===");
    System.out.println(mapResult);

    String numberOfBooksTemplate = "{{ xpath:count(/catalog/book) }}";

    String bookTitleTemplate = "{{ xpath:/catalog/book/title }}";

    Document document = TemplateRenderer.parseXml(getXMLData());

    assertEquals("12", templateRenderer.render(numberOfBooksTemplate, document));
    assertEquals("XML Developer's Guide", templateRenderer.render(bookTitleTemplate, document));

    String eachTemplate =
        """
              {{#each xpath:/catalog/book as book}}
              Book {{$index}}: {{xpath:@/title}}
              {{else}}
              No books found.

              {{/each}}
              """;

    xmlDoc = TemplateRenderer.parseXml(getXMLData());

    String eachResult = templateRenderer.render(eachTemplate, xmlDoc);
    System.out.println("=== Each Result ===");
    System.out.println(eachResult);
  }

  private static String getXMLData() {
    return """
                <?xml version="1.0"?>
                <catalog>
                   <book id="bk101">
                      <author>Gambardella, Matthew</author>
                      <title>XML Developer's Guide</title>
                      <genre>Computer</genre>
                      <price>44.95</price>
                      <publish_date>2000-10-01</publish_date>
                      <description>An in-depth look at creating applications\s
                      with XML.</description>
                   </book>
                   <book id="bk102">
                      <author>Ralls, Kim</author>
                      <title>Midnight Rain</title>
                      <genre>Fantasy</genre>
                      <price>5.95</price>
                      <publish_date>2000-12-16</publish_date>
                      <description>A former architect battles corporate zombies,\s
                      an evil sorceress, and her own childhood to become queen\s
                      of the world.</description>
                   </book>
                   <book id="bk103">
                      <author>Corets, Eva</author>
                      <title>Maeve Ascendant</title>
                      <genre>Fantasy</genre>
                      <price>5.95</price>
                      <publish_date>2000-11-17</publish_date>
                      <description>After the collapse of a nanotechnology\s
                      society in England, the young survivors lay the\s
                      foundation for a new society.</description>
                   </book>
                   <book id="bk104">
                      <author>Corets, Eva</author>
                      <title>Oberon's Legacy</title>
                      <genre>Fantasy</genre>
                      <price>5.95</price>
                      <publish_date>2001-03-10</publish_date>
                      <description>In post-apocalypse England, the mysterious\s
                      agent known only as Oberon helps to create a new life\s
                      for the inhabitants of London. Sequel to Maeve\s
                      Ascendant.</description>
                   </book>
                   <book id="bk105">
                      <author>Corets, Eva</author>
                      <title>The Sundered Grail</title>
                      <genre>Fantasy</genre>
                      <price>5.95</price>
                      <publish_date>2001-09-10</publish_date>
                      <description>The two daughters of Maeve, half-sisters,\s
                      battle one another for control of England. Sequel to\s
                      Oberon's Legacy.</description>
                   </book>
                   <book id="bk106">
                      <author>Randall, Cynthia</author>
                      <title>Lover Birds</title>
                      <genre>Romance</genre>
                      <price>4.95</price>
                      <publish_date>2000-09-02</publish_date>
                      <description>When Carla meets Paul at an ornithology\s
                      conference, tempers fly as feathers get ruffled.</description>
                   </book>
                   <book id="bk107">
                      <author>Thurman, Paula</author>
                      <title>Splish Splash</title>
                      <genre>Romance</genre>
                      <price>4.95</price>
                      <publish_date>2000-11-02</publish_date>
                      <description>A deep sea diver finds true love twenty\s
                      thousand leagues beneath the sea.</description>
                   </book>
                   <book id="bk108">
                      <author>Knorr, Stefan</author>
                      <title>Creepy Crawlies</title>
                      <genre>Horror</genre>
                      <price>4.95</price>
                      <publish_date>2000-12-06</publish_date>
                      <description>An anthology of horror stories about roaches,
                      centipedes, scorpions  and other insects.</description>
                   </book>
                   <book id="bk109">
                      <author>Kress, Peter</author>
                      <title>Paradox Lost</title>
                      <genre>Science Fiction</genre>
                      <price>6.95</price>
                      <publish_date>2000-11-02</publish_date>
                      <description>After an inadvertant trip through a Heisenberg
                      Uncertainty Device, James Salway discovers the problems\s
                      of being quantum.</description>
                   </book>
                   <book id="bk110">
                      <author>O'Brien, Tim</author>
                      <title>Microsoft .NET: The Programming Bible</title>
                      <genre>Computer</genre>
                      <price>36.95</price>
                      <publish_date>2000-12-09</publish_date>
                      <description>Microsoft's .NET initiative is explored in\s
                      detail in this deep programmer's reference.</description>
                   </book>
                   <book id="bk111">
                      <author>O'Brien, Tim</author>
                      <title>MSXML3: A Comprehensive Guide</title>
                      <genre>Computer</genre>
                      <price>36.95</price>
                      <publish_date>2000-12-01</publish_date>
                      <description>The Microsoft MSXML3 parser is covered in\s
                      detail, with attention to XML DOM interfaces, XSLT processing,\s
                      SAX and more.</description>
                   </book>
                   <book id="bk112">
                      <author>Galos, Mike</author>
                      <title>Visual Studio 7: A Comprehensive Guide</title>
                      <genre>Computer</genre>
                      <price>49.95</price>
                      <publish_date>2001-04-16</publish_date>
                      <description>Microsoft Visual Studio 7 is explored in depth,
                      looking at how Visual Basic, Visual C++, C#, and ASP+ are\s
                      integrated into a comprehensive development\s
                      environment.</description>
                   </book>
                </catalog>
                """;
  }
}
