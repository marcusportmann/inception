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

package digital.inception.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * The <b>TokenClaim</b> class stores the details for a claim associated with a token.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A token claim")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "values"})
@XmlRootElement(name = "TokenClaim", namespace = "https://inception.digital/security")
@XmlType(
    name = "TokenClaim",
    namespace = "https://inception.digital/security",
    propOrder = {"name", "values"})
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenClaim implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The name of the token claim. */
  @Schema(description = "The name of the token claim", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The values for the token claim. */
  @Schema(
      description = "The values for the token claim",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Values", required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  private List<String> values;

  /** Constructs a new <b>TokenClaim</b>. */
  public TokenClaim() {}

  /**
   * Constructs a new <b>TokenClaim</b>.
   *
   * @param name the name of the token claim
   * @param values the values for the token claim
   */
  public TokenClaim(String name, List<String> values) {
    this.name = name;
    this.values = values;
  }

  /**
   * Constructs a new <b>TokenClaim</b>.
   *
   * @param name the name of the token claim
   * @param values the values for the token claim
   */
  public TokenClaim(String name, String[] values) {
    this.name = name;
    this.values = List.of(values);
  }

  /**
   * Returns the name of the token claim.
   *
   * @return the name of the token claim
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the values for the token claim.
   *
   * @return the values for the token claim
   */
  public List<String> getValues() {
    return values;
  }

  /**
   * Set the name of the token claim.
   *
   * @param name the name of the token claim
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the values for the token claim.
   *
   * @param values the values for the token claim
   */
  public void setValues(List<String> values) {
    this.values = values;
  }
}
