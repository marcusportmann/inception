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

import jakarta.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * The {@code HtmlToSimplifiedHtml} class converts HTML to simplified HTML using the JSoup library.
 *
 * @author Marcus Portmann
 */
public final class HtmlToSimplifiedHtml {
  private static final Map<Character, String> HTML_ESCAPE_MAP = new HashMap<>();

  /** Private constructor to prevent instantiation. */
  private HtmlToSimplifiedHtml() {}

  /**
   * Convert the HTML content to simplified HTML.
   *
   * @param html the HTML
   * @return the HTML content converted to simplified HTML
   */
  public static String convertToSimplifiedHtml(String html) {
    if (!StringUtils.hasText(html)) {
      return html;
    }

    html = html.replace("“", "\"");
    html = html.replace("”", "\"");

    Parser parser = Parser.htmlParser().setTrackErrors(100);

    Document document = Jsoup.parse(html, Parser.xmlParser());

    // Remove nested HTML elements
    document
        .children()
        .forEach(
            topLevelElement -> {
              topLevelElement
                  .children()
                  .forEach(
                      childElement -> {
                        Elements nestedHtmlElements = childElement.getElementsByTag("html");
                        nestedHtmlElements.forEach(Node::remove);
                      });
            });

    StringBuilder buffer = new StringBuilder();

    document
        .children()
        .traverse(
            new NodeVisitor() {

              private boolean inLink = false;

              private boolean isBlank = false;

              @Override
              public void head(@Nullable Node node, int depth) {
                if (node instanceof Element element) {
                  switch (element.tagName()) {
                    case "a":
                      inLink = true;

                      String wholeText = element.wholeText();
                      String href = element.attr("href");

                      if (StringUtils.hasText(wholeText)) {
                        if (StringUtils.hasText(href)) {
                          buffer.append("<a href=\"");
                          buffer.append(href);
                          buffer.append("\">");
                          buffer.append(element.wholeText());
                          buffer.append("</a>");
                        } else {
                          buffer.append(element.wholeText());
                        }
                      }
                      break;
                    case "b", "strong":
                      buffer.append("<b>");
                      break;
                    case "blockquote":
                      buffer.append("<blockquote>");
                      break;
                    case "body":
                      buffer.append("<body>");
                      break;
                    case "br":
                      buffer.append("<br>");
                      break;
                    case "div":
                      /*
                       * Skip <div> if it only contains another <div>.
                       */
                      Elements childElements = element.children();

                      //                      if ((childElements.size() == 1)
                      //                          &&
                      // ("div".equalsIgnoreCase(childElements.first().tagName()))) {
                      //                      }
                      //                      else {
                      //                        buffer.append("<div>");
                      //                      }

                      buffer.append("<div>");

                      //                      if (element.hasAttr("style")) {
                      //                        String styleValue =
                      // element.attribute("style").getValue();
                      //                        if (styleValue.contains("border-top:solid")
                      //                            || styleValue.contains("border-style: solid")) {
                      //                          buffer.append("<hr>\n");
                      //                        }
                      //                      }
                      break;
                    case "h1":
                      buffer.append("<h1>");
                      break;
                    case "h2":
                      buffer.append("<h2>");
                      break;
                    case "h3":
                      buffer.append("<h3>");
                      break;
                    case "h4":
                      buffer.append("<h4>");
                      break;
                    case "h5":
                      buffer.append("<h5>");
                      break;
                    case "h6":
                      buffer.append("<h6>");
                      break;
                    case "html":
                      buffer.append("<html>");
                      break;
                    case "i", "em":
                      buffer.append("<i>");
                      break;
                    case "li":
                      buffer.append("<li>");
                      break;
                    case "ol":
                      buffer.append("<ol>");
                      break;
                    case "p":
                      //                      if (!element.wholeText().isEmpty()) {
                      //                        buffer.append("<p>");
                      //                      }

                      if (!element.text().isEmpty()) {
                        buffer.append("<p>");
                      } else {
                        isBlank = true;
                      }

                      break;
                    case "span":
                      break;
                    case "table":
                      buffer.append("<table>");
                      break;
                    case "td":
                      buffer.append("<td>");
                      break;
                    case "th":
                      buffer.append("<th>");
                      break;
                    case "tr":
                      buffer.append("<tr>");
                      break;
                    case "u":
                      buffer.append("<u>");
                      break;
                    case "ul":
                      buffer.append("<ul>");
                      break;
                  }
                } else if (node instanceof TextNode textNode) {
                  if (inLink || isBlank) {
                    return;
                  }

                  if (!textNode.isBlank()) {
                    if (("&#xa0;".equals(textNode.outerHtml()))
                        || ("&nbsp;".equals(textNode.outerHtml()))) {
                      if (textNode.parent() instanceof Element parentElement) {
                        String parentTagName = parentElement.tagName();
                        if (parentTagName.equalsIgnoreCase("div")
                            || parentTagName.equalsIgnoreCase("span")) {
                          buffer.append("&#xa0;");
                        }
                      }
                    } else {
                      buffer.append(
                          HtmlUtils.htmlEscape(textNode.text(), StandardCharsets.UTF_8.name()));
                    }
                  }
                }
              }

              @Override
              public void tail(@Nullable Node node, int depth) {
                if (node instanceof Element element) {
                  switch (element.tagName()) {
                    case "a":
                      inLink = false;
                      break;
                    case "b", "strong":
                      buffer.append("</b>");
                      break;
                    case "blockquote":
                      buffer.append("</blockquote>");
                      break;
                    case "body":
                      buffer.append("</body>");
                      break;
                    case "div":
                      /*
                       * Skip <div> if it only contains another <div>.
                       */
                      Elements childElements = element.children();

                      //                      if ((childElements.size() == 1)
                      //                          &&
                      // ("div".equalsIgnoreCase(childElements.first().tagName()))) {
                      //                      }
                      //                      else {
                      //                        buffer.append("</div>");
                      //                      }

                      buffer.append("</div>");

                      break;
                    case "html":
                      buffer.append("</html>");
                      break;
                    case "i", "em":
                      buffer.append("</i>");
                      break;
                    case "li":
                      buffer.append("</li>");
                      break;
                    case "ol":
                      buffer.append("</ol>");
                      break;
                    case "p":
                      if (!element.text().isEmpty()) {
                        buffer.append("</p>");
                      } else {
                        isBlank = false;
                      }

                      break;
                    case "span":
                      break;
                    case "table":
                      buffer.append("</table>");
                      break;
                    case "td":
                      buffer.append("</td>");
                      break;
                    case "th":
                      buffer.append("</th>");
                      break;
                    case "tr":
                      buffer.append("</tr>");
                      break;
                    case "u":
                      buffer.append("</u>");
                      break;
                    case "ul":
                      buffer.append("</ul>");
                      break;
                  }
                }
              }
            });

    // return buffer.toString();

    //    if (false) {
    //      Tidy tidy = new Tidy();
    //      tidy.setDropProprietaryTags(false);
    //      // tidy.setPrintBodyOnly(true);
    //      tidy.setWraplen(0);
    //      tidy.setSmartIndent(true);
    //
    //      ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //
    //      tidy.parse(
    //          new ByteArrayInputStream(buffer.toString().getBytes(StandardCharsets.UTF_8)), baos);
    //
    //      return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    //    }

    if (false) {
      Document newDocument = Jsoup.parse(buffer.toString(), Parser.xmlParser());

      return document.html();
    }

    return buffer.toString();
  }
}
