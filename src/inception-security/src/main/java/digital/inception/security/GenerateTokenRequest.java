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

package digital.inception.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The <b>GenerateTokenRequest</b> class holds the information for a request to generate a token.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to generate a token")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "name", "description", "validFrom", "expires", "claims"})
@XmlRootElement(name = "GenerateTokenRequest", namespace = "http://inception.digital/security")
@XmlType(
    name = "GenerateTokenRequest",
    namespace = "http://inception.digital/security",
    propOrder = {"type", "name", "description", "validFrom", "expires", "claims"})
@XmlAccessorType(XmlAccessType.FIELD)
public class GenerateTokenRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The claims for the token. */
  @Schema(description = "The claims for the token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Claims", required = true)
  @XmlElement(name = "Claim", required = true)
  private List<TokenClaim> claims = new ArrayList<>();

  /** The description for the token. */
  @Schema(description = "The description for the token")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 200)
  private String description;

  /** The date the token expires. */
  @Schema(description = "The date the token expires")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "Expires")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate expires;

  /** The name of the token. */
  @Schema(description = "The name of the token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The token type. */
  @Schema(description = "The token type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  private TokenType type;

  /** The date the token is valid from. */
  @Schema(description = "The date the token is valid from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "ValidFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate validFrom;

  /** Constructs a new <b>GenerateTokenRequest</b>. */
  public GenerateTokenRequest() {}

  /**
   * Constructs a new <b>GenerateTokenRequest</b>.
   *
   * @param type the token type
   * @param name the name of the token
   * @param description the description for the token
   * @param validFrom the date the token is valid from
   * @param expires the date the token expires
   * @param claims the claims for the token
   */
  public GenerateTokenRequest(
      TokenType type,
      String name,
      String description,
      LocalDate validFrom,
      LocalDate expires,
      List<TokenClaim> claims) {
    this.type = type;
    this.name = name;
    this.description = description;
    this.validFrom = validFrom;
    this.expires = expires;
    this.claims = claims;
  }

  /**
   * Constructs a new <b>GenerateTokenRequest</b>.
   *
   * @param type the token type
   * @param name the name of the token
   * @param description the description for the token
   * @param validFrom the date the token is valid from
   * @param claims the claims for the token
   */
  public GenerateTokenRequest(
      TokenType type,
      String name,
      String description,
      LocalDate validFrom,
      List<TokenClaim> claims) {
    this.type = type;
    this.name = name;
    this.description = description;
    this.validFrom = validFrom;
    this.claims = claims;
  }

  /**
   * Constructs a new <b>GenerateTokenRequest</b>.
   *
   * @param type the token type
   * @param name the name of the token
   * @param description the description for the token
   * @param claims the claims for the token
   */
  public GenerateTokenRequest(
      TokenType type, String name, String description, List<TokenClaim> claims) {
    this.type = type;
    this.name = name;
    this.description = description;
    this.claims = claims;
  }

  /**
   * Returns the claims for the token.
   *
   * @return the claims for the token
   */
  public List<TokenClaim> getClaims() {
    return claims;
  }

  /**
   * Returns the description for the token.
   *
   * @return the description for the token
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the date the token expires.
   *
   * @return the date the token expires
   */
  public LocalDate getExpires() {
    return expires;
  }

  /**
   * Returns the name of the token.
   *
   * @return the name of the token
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the token type.
   *
   * @return the token type
   */
  public TokenType getType() {
    return type;
  }

  /**
   * Returns the date the token is valid from.
   *
   * @return the date the token is valid from
   */
  public LocalDate getValidFrom() {
    return validFrom;
  }

  /**
   * Set the claims for the token.
   *
   * @param claims the claims for the token
   */
  public void setClaims(List<TokenClaim> claims) {
    this.claims = claims;
  }

  /**
   * Set the description for the token.
   *
   * @param description the description for the token
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the date the token expires.
   *
   * @param expires the date the token expires
   */
  public void setExpires(LocalDate expires) {
    this.expires = expires;
  }

  /**
   * Set the name of the token.
   *
   * @param name the name of the token
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the token type.
   *
   * @param type the token type
   */
  public void setType(TokenType type) {
    this.type = type;
  }

  /**
   * Set the date the token is valid from.
   *
   * @param validFrom the date the token is valid from
   */
  public void setValidFrom(LocalDate validFrom) {
    this.validFrom = validFrom;
  }
}
