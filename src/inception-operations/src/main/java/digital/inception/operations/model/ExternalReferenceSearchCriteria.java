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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code ExternalReferenceSearchCriteria} class holds the external reference search criteria to
 * apply when searching for documents or workflows.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "The external reference search criteria to apply when searching for documents or workflows")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "value"})
@XmlRootElement(
    name = "ExternalReferenceSearchCriteria",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "ExternalReferenceSearchCriteria",
    namespace = "https://inception.digital/operations",
    propOrder = {"type", "value"})
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused", "WeakerAccess"})
public class ExternalReferenceSearchCriteria implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the external reference type. */
  @Schema(description = "The code for the external reference type")
  @JsonProperty
  @XmlElement(name = "Type")
  @Size(min = 1, max = 50)
  protected String type;

  /** The value for the external reference. */
  @Schema(description = "The value for the external reference")
  @JsonProperty
  @XmlElement(name = "Value")
  @Size(min = 1, max = 200)
  protected String value;

  /** Constructs a new {@code ExternalReferenceSearchCriteria}. */
  public ExternalReferenceSearchCriteria() {}

  /**
   * Constructs a new {@code ExternalReferenceSearchCriteria}.
   *
   * @param type the code for the external reference type
   * @param value the value for the external reference
   */
  public ExternalReferenceSearchCriteria(String type, String value) {
    this.type = type;
    this.value = value;
  }

  /**
   * Returns the code for the external reference type.
   *
   * @return the code for the external reference type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the value for the external reference.
   *
   * @return the value for the external reference
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the code for the external reference type.
   *
   * @param type the code for the external reference type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Set the value for the external reference.
   *
   * @param value the value for the external reference
   */
  public void setValue(String value) {
    this.value = value;
  }
}
