/*
 * Copyright 2021 Marcus Portmann
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
import digital.inception.core.wbxml.Element;
import java.io.Serializable;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeData</code> class holds the information for a code.
 *
 * @author Marcus Portmann
 */
public class CodeData implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ID uniquely identifying the category the code is associated with. */
  private final String codeCategoryId;

  /** The ID uniquely identifying the code. */
  private final String id;

  /** The name of the code. */
  private final String name;

  /** The value for the code. */
  private final String value;

  /**
   * Constructs a new <code>CodeData</code>.
   *
   * @param code the <code>Code</code> instance containing the code data
   */
  CodeData(Code code) {
    this.id = String.valueOf(code.getId());
    this.codeCategoryId = code.getCodeCategoryId();
    this.name = code.getName();
    this.value = code.getValue();
  }

  /**
   * Constructs a new <code>CodeData</code>.
   *
   * @param element the WBXML element containing the code data
   */
  CodeData(Element element) {
    try {
      this.id = element.getChildText("Id");
      this.codeCategoryId = element.getChildText("CodeCategoryId");
      this.name =
          StringUtils.hasText(element.getChildText("Name")) ? element.getChildText("Name") : "";
      this.value =
          StringUtils.hasText(element.getChildText("Value")) ? element.getChildText("Value") : "";
    } catch (Throwable e) {
      throw new RuntimeException("Failed to extract the code data from the WBXML", e);
    }
  }

  /**
   * Returns the ID uniquely identifying the category the code is associated with.
   *
   * @return the ID uniquely identifying the category the code is associated with
   */
  public String getCodeCategoryId() {
    return codeCategoryId;
  }

  /**
   * Returns the ID uniquely identifying the code.
   *
   * @return the ID uniquely identifying the code
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the code.
   *
   * @return the name of the code
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value for the code.
   *
   * @return the value for the code
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns the WBXML element containing the code data.
   *
   * @return the WBXML element containing the code data
   */
  Element toElement() {
    Element codeElement = new Element("Code");

    codeElement.addContent(new Element("Id", StringUtils.hasText(id) ? id : ""));
    codeElement.addContent(new Element("CodeCategoryId", codeCategoryId));
    codeElement.addContent(new Element("Name", StringUtils.hasText(name) ? name : ""));
    codeElement.addContent(new Element("Value", StringUtils.hasText(value) ? value : ""));

    return codeElement;
  }
}
