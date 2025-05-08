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
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.springframework.util.StringUtils;

/**
 * The {@code HtmlToMarkdown} class converts HTML to Markdown using the JSoup library.
 *
 * @author Marcus Portmann
 */
public final class HtmlToMarkdown {
  /** Private constructor to prevent instantiation. */
  private HtmlToMarkdown() {}

  /**
   * Convert the HTML content to Markdown.
   *
   * @param html the HTML
   * @return the HTML content converted to Markdown
   */
  public static String convertToMarkdown(String html) {
    if (!StringUtils.hasText(html)) {
      return html;
    }

    html = html.replace("“", "\"");
    html = html.replace("”", "\"");

    Safelist safelist = Safelist.relaxed();

    html = Jsoup.clean(html, safelist);

    Document document = Jsoup.parse(html);

    List<MarkdownLine> markdownLines = new ArrayList<>();

    document
        .body()
        .traverse(
            new NodeVisitor() {
              private MarkdownLine currentMarkdownLine = new MarkdownLine();

              private boolean inBoldSection = false;

              private boolean inHtmlSection = false;

              private boolean inLink = false;

              private boolean inOrderedList = false;

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
                          if (inHtmlSection) {
                            currentMarkdownLine.appendContent("<a href=\"");
                            currentMarkdownLine.appendContent(href);
                            currentMarkdownLine.appendContent("\">");
                            currentMarkdownLine.appendContent(element.wholeText());
                            currentMarkdownLine.appendContent("</a>");
                          } else {
                            currentMarkdownLine.appendContent("[");
                            currentMarkdownLine.appendContent(element.wholeText());
                            currentMarkdownLine.appendContent("](");
                            currentMarkdownLine.appendContent(href);
                            currentMarkdownLine.appendContent(")");
                          }
                        } else {
                          currentMarkdownLine.appendContent(element.wholeText());
                        }
                      }
                      break;
                    case "b", "strong":
                      inBoldSection = true;
                      currentMarkdownLine.appendContent(inHtmlSection ? "<b>" : "**");
                      break;
                    case "blockquote":
                      currentMarkdownLine.appendContent("<blockquote>");
                      newMarkdownLine();
                      break;
                    case "br":
                      newMarkdownLine();
                      break;
                    case "div":
                      /*
                       * Skip new line if this is a <div> containing only a <div>, since the nested
                       * <div> will result in a newline.
                       */
                      Elements childElements = element.children();

                      if ((childElements.size() == 1)
                          && ("div".equalsIgnoreCase(childElements.first().tagName()))) {
                      }
                      /*
                       * Skip new line if this is a <div> containing only a <br>, since the <br>
                       * will result in a newline.
                       */
                      else if ((!element.hasText()) && (!element.select("> br").isEmpty())) {
                      } else {
                        newMarkdownLine();
                      }

                      if (element.hasAttr("style")) {
                        String styleValue = element.attribute("style").getValue();
                        if (styleValue.contains("border-top:solid")
                            || styleValue.contains("border-style: solid")) {
                          currentMarkdownLine.appendContent("___");
                          newMarkdownLine();
                        }
                      }
                      break;
                    case "h1":
                      currentMarkdownLine.appendContent("# ");
                      break;
                    case "h2":
                      currentMarkdownLine.appendContent("## ");
                      break;
                    case "h3":
                      currentMarkdownLine.appendContent("### ");
                      break;
                    case "h4":
                      currentMarkdownLine.appendContent("#### ");
                      break;
                    case "h5":
                      currentMarkdownLine.appendContent("##### ");
                      break;
                    case "h6":
                      currentMarkdownLine.appendContent("###### ");
                      break;
                    case "i", "em":
                      currentMarkdownLine.appendContent("_");
                      break;
                    case "li":
                      currentMarkdownLine.appendContent(inOrderedList ? "<li>" : "* ");
                      break;
                    case "ol":
                      inOrderedList = true;
                      currentMarkdownLine.appendContent("<ol>");
                      newMarkdownLine();
                      break;
                    case "p":
                      break;
                    case "span":
                      break;
                    case "table":
                      inHtmlSection = true;
                      currentMarkdownLine.appendContent("<table>");
                      newMarkdownLine();
                      break;
                    case "td":
                      currentMarkdownLine.appendContent("<td>");
                      break;
                    case "th":
                      currentMarkdownLine.appendContent("<th>");
                      break;
                    case "tr":
                      currentMarkdownLine.appendContent("<tr>");
                      break;
                    case "u":
                      currentMarkdownLine.appendContent("<u>");
                      break;
                  }
                } else if (node instanceof TextNode textNode) {
                  String text = textNode.text();

                  if (inLink) {
                    return;
                  }

                  // Add spacing after the bold markdown tag if required
                  if (!inBoldSection) {
                    if (currentMarkdownLine.endsWith("**") && (!text.startsWith(" "))) {
                      currentMarkdownLine.appendContent(" ");
                    }
                  }

                  if (!textNode.isBlank()) {
                    if (inBoldSection) {
                      text = text.stripTrailing();
                      if (!text.isBlank()) {
                        currentMarkdownLine.appendContent(text);
                      }
                    } else {
                      currentMarkdownLine.appendContent(text);
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
                      inBoldSection = false;
                      currentMarkdownLine.appendContent(inHtmlSection ? "</b>" : "**");
                      break;
                    case "blockquote":
                      newMarkdownLine();
                      currentMarkdownLine.appendContent("</blockquote>");
                      break;
                    case "div":
                      break;
                    case "i", "em":
                      currentMarkdownLine.appendContent("_");
                      break;
                    case "li":
                      if (inOrderedList) {
                        currentMarkdownLine.appendContent("</li>");
                      }
                      newMarkdownLine();
                      break;
                    case "ol":
                      currentMarkdownLine.appendContent("</ol>");
                      newMarkdownLine();
                      break;
                    case "p":
                      if (!element.wholeText().isEmpty()) {
                        newMarkdownLine();
                      }
                      break;
                    case "span":
                      break;
                    case "table":
                      inHtmlSection = false;
                      currentMarkdownLine.appendContent("</table>");
                      newMarkdownLine();
                      break;
                    case "td":
                      currentMarkdownLine.appendContent("</td>");
                      break;
                    case "th":
                      currentMarkdownLine.appendContent("</th>");
                      break;
                    case "tr":
                      currentMarkdownLine.appendContent("</tr>");
                      newMarkdownLine();
                      break;
                    case "u":
                      currentMarkdownLine.appendContent("</u>");
                      break;
                  }
                }
              }

              private void newMarkdownLine() {
                /*
                 * Do nothing if the previous two markdown lines are blank and the current markdown
                 * line is also blank. This prevents a large number of empty lines in the document.
                 */
                if ((currentMarkdownLine.isEmpty())
                    && (markdownLines.size() > 1)
                    && (markdownLines.getLast().isEmpty())
                    && (markdownLines.get(markdownLines.size() - 2).isEmpty())) {
                  return;
                }

                markdownLines.add(currentMarkdownLine);
                currentMarkdownLine = new MarkdownLine();
              }
            });

    StringBuilder markdown = new StringBuilder();

    //    int lineCount = 1;
    //    for (MarkdownLine markdownLine : markdownLines) {
    //      markdown.append(
    //          String.format(
    //              "%04d: %s  %s", lineCount, markdownLine.getContent(), System.lineSeparator()));
    //      lineCount++;
    //    }

    int lineCount = 1;
    for (MarkdownLine markdownLine : markdownLines) {
      markdown.append(String.format("%s  %s", markdownLine.getContent(), System.lineSeparator()));
      lineCount++;
    }

    return markdown.toString().trim();
  }

  public static void main(String[] args) {
    String html =
        "<h1>Hello World</h1><p>This is a <strong>bold</strong> paragraph with a <a href=\"https://example.com\">link</a>.</p>";
    String markdown = HtmlToMarkdown.convertToMarkdown(html);
    System.out.println(markdown);
  }

  /** The {@code MarkdownLine} class holds a line of markdown content. */
  private static class MarkdownLine {

    private final StringBuilder buffer;

    /** Constructs a new {@code MarkdownLine}. */
    public MarkdownLine() {
      buffer = new StringBuilder();
    }

    /**
     * Append the content to the markdown line.
     *
     * @param content the content
     */
    public void appendContent(String content) {
      if (content.isBlank()) {
        if (!buffer.isEmpty()) {
          buffer.append(" ");
        }
      } else {
        buffer.append(content);
      }
    }

    /**
     * Returns whether the markdown line ends with the specified suffix.
     *
     * @param suffix the suffix
     * @return {@code true} if the markdown line ends with the specified suffix {@code false}
     */
    public boolean endsWith(String suffix) {
      int lastIndexOf = buffer.lastIndexOf(suffix);
      int expectedIndex = buffer.length() - suffix.length();

      if ((lastIndexOf >= 0) && (expectedIndex >= 0)) {
        return lastIndexOf == expectedIndex;
      } else {
        return false;
      }
    }

    /**
     * Returns the content for the markdown line.
     *
     * @return the content for the markdown line
     */
    public String getContent() {
      return buffer.toString();
    }

    /**
     * Returns the content length for the markdown line.
     *
     * @return the content length for the markdown line
     */
    public int getContentLength() {
      return buffer.length();
    }

    /**
     * Returns whether the markdown line is empty.
     *
     * @return {@code true} if the markdown line empty or {@code false} otherwise
     */
    public boolean isEmpty() {
      return buffer.isEmpty();
    }
  }
}
