/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.validation;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ValidationErrorAttribute</code> attribute represents an attribute associated with the
 * validation error.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "ValidationErrorAttribute")
@JsonPropertyOrder({"name", "value"})
@XmlRootElement(name = "ValidationErrorAttribute",
    namespace = "http://validation.inception.digital")
@XmlType(name = "ValidationErrorAttribute", namespace = "http://validation.inception.digital",
    propOrder = {"name", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class ValidationErrorAttribute
    implements Serializable, Cloneable {

  private static final long serialVersionUID = 1000000;

  /**
   * The name for the validation error attribute.
   */
  @XmlElement(name = "Name", required = true)
  private String name;

  /**
   * The value for the validation error attribute.
   */
  @XmlElement(name = "Value")
  private String value;

  /**
   * Constructs a new <code>ValidationErrorAttribute</code>.
   */
  public ValidationErrorAttribute() {
  }

  /**
   * Constructs a new <code>ValidationErrorAttribute</code>.
   *
   * @param name  the name for the validation error attribute
   * @param value the value for the validation error attribute
   */
  public ValidationErrorAttribute(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the name for the validation error attribute.
   *
   * @return the name for the validation error attribute
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name for the validation error attribute.
   *
   * @param name the name for the validation error attribute
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the value for the validation error attribute.
   *
   * @return the value for the validation error attribute
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the value for the validation error attribute.
   *
   * @param value the value for the validation error attribute
   */
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  protected Object clone()
      throws CloneNotSupportedException {
    return new ValidationErrorAttribute(name, value);
  }
}
