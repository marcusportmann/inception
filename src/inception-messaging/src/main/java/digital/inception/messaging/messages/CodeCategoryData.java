/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.messaging.messages;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.codes.Code;
import digital.inception.codes.CodeCategory;
import digital.inception.core.util.ISO8601Util;
import digital.inception.core.wbxml.Element;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeCategoryData</code> class holds the information for a code category.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("WeakerAccess")
public class CodeCategoryData implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The XML or JSON data for the code category. */
  private final String codeData;

  /** The codes for the code category. */
  private final List<CodeData> codes;

  /** The ID uniquely identifying the code category. */
  private final String id;

  /** The date and time the code category was last updated. */
  private final LocalDateTime lastUpdated;

  /** The name of the code category. */
  private final String name;

  /**
   * Constructs a new <code>CodeCategoryData</code>.
   *
   * @param element the WBXML element containing the code category data
   */
  CodeCategoryData(Element element) {
    this.id = element.getChildText("Id");
    this.name = element.getChildText("Name");

    String lastUpdatedValue = element.getChildText("LastUpdated");

    if (lastUpdatedValue.contains("T")) {
      try {
        this.lastUpdated = ISO8601Util.toLocalDateTime(lastUpdatedValue);
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to parse the LastUpdated ISO8601 timestamp ("
                + lastUpdatedValue
                + ") for the code category data",
            e);
      }
    } else {
      this.lastUpdated =
          LocalDateTime.ofInstant(
              Instant.ofEpochSecond(Long.parseLong(lastUpdatedValue)), ZoneId.systemDefault());
    }

    if (element.hasChild("CodeData")) {
      this.codeData = element.getChildText("CodeData");
    } else {
      this.codeData = "";
    }

    this.codes = new ArrayList<>();

    if (element.hasChild("Codes")) {
      List<Element> codeElements = element.getChild("Codes").getChildren("Code");

      this.codes.addAll(codeElements.stream().map(CodeData::new).collect(Collectors.toList()));
    }
  }

  /**
   * Constructs a new <code>CodeCategoryData</code>.
   *
   * @param codeCategory the code category
   * @param codeData the XML or JSON data for the code category
   * @param codes the codes for the code category
   */
  public CodeCategoryData(CodeCategory codeCategory, String codeData, List<Code> codes) {
    this.id = codeCategory.getId();
    this.name = codeCategory.getName();
    this.lastUpdated = codeCategory.getUpdated();
    this.codeData = StringUtils.hasText(codeData) ? codeData : "";
    this.codes = new ArrayList<>();

    if (codes != null) {
      this.codes.addAll(codes.stream().map(CodeData::new).collect(Collectors.toList()));
    }
  }

  /**
   * Returns the XML or JSON data for the code category.
   *
   * @return the XML or JSON data for the code category
   */
  public String getCodeData() {
    return codeData;
  }

  /**
   * Returns the codes for the code category.
   *
   * @return the codes for the code category
   */
  public List<CodeData> getCodes() {
    return codes;
  }

  /**
   * Returns the ID uniquely identifying the code category.
   *
   * @return the ID uniquely identifying the code category
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @return the date and time the code category was last updated
   */
  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }

  /**
   * Returns the name of the code category.
   *
   * @return the name of the code category
   */
  public String getName() {
    return name;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();

    buffer.append("CodeCategory {");
    buffer.append("id=\"").append(getId()).append("\", ");
    buffer.append("name=\"").append(getName()).append("\", ");
    buffer
        .append("lastUpdated=\"")
        .append(ISO8601Util.fromLocalDateTime(getLastUpdated()))
        .append("\", ");
    buffer
        .append("codeData=\"")
        .append((getCodeData() != null) ? getCodeData().length() : 0)
        .append(" characters of XML or JSON code data\"");

    if ((getCodes() != null) && (getCodes().size() > 0)) {
      buffer.append(", codes = {");

      int count = 0;

      for (CodeData code : getCodes()) {
        if (count > 0) {
          buffer.append(", Code {");
        } else {
          buffer.append("Code {");
        }

        buffer.append("id=\"").append(code.getId()).append("\", ");
        buffer.append("categoryId=\"").append(code.getCodeCategoryId()).append("\", ");
        buffer.append("name=\"").append(code.getName()).append("\", ");
        buffer.append("value=\"").append(code.getValue()).append("\"");

        buffer.append("}");

        count++;
      }

      buffer.append("}");
    }

    buffer.append("}");

    return buffer.toString();
  }

  /**
   * Returns the WBXML element containing the code category data.
   *
   * @return the WBXML element containing the code category data
   */
  Element toElement() {
    Element codeCategoryElement = new Element("CodeCategory");

    codeCategoryElement.addContent(new Element("Id", id));
    codeCategoryElement.addContent(new Element("Name", StringUtils.hasText(name) ? name : ""));
    codeCategoryElement.addContent(
        new Element(
            "LastUpdated",
            (lastUpdated == null)
                ? ISO8601Util.now()
                : ISO8601Util.fromLocalDateTime(lastUpdated)));

    if (codeData != null) {
      codeCategoryElement.addContent(new Element("CodeData", codeData));
    }

    Element codesElement = new Element("Codes");

    if (codes != null) {
      for (CodeData code : codes) {
        codesElement.addContent(code.toElement());
      }
    }

    codeCategoryElement.addContent(codesElement);

    return codeCategoryElement;
  }
}
