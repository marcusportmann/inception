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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The <b>RevokedToken</b> class holds the information for a revoked token.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A revoked token")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "type",
  "name",
  "issued",
  "validFromDate",
  "expiryDate",
  "revocationDate"
})
@XmlRootElement(name = "RevokedToken", namespace = "http://inception.digital/security")
@XmlType(
    name = "RevokedToken",
    namespace = "http://inception.digital/security",
    propOrder = {"id", "type", "name", "issued", "validFromDate", "expiryDate", "revocationDate"})
@XmlAccessorType(XmlAccessType.FIELD)
public class RevokedToken implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date the token expires. */
  @Schema(description = "The ISO 8601 format date value for the date the token expires")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "ExpiryDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate expiryDate;

  /** The ID for the token. */
  @Schema(description = "The ID for the token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String id;

  /** The date and time the token was issued. */
  @Schema(
      description = "The date and time the token was issued",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Issued")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  private OffsetDateTime issued;

  /** The name of the token. */
  @Schema(description = "The name of the token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The date the token was revoked. */
  @Schema(description = "The ISO 8601 format date value for the date the token was revoked")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "RevocationDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate revocationDate;

  /** The token type. */
  @Schema(description = "The token type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  private TokenType type;

  /** The date the token is valid from. */
  @Schema(description = "The ISO 8601 format date value for the date the token is valid from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "ValidFromDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate validFromDate;

  /** Constructs a new <b>RevokedToken</b>. */
  public RevokedToken() {}

  /**
   * Constructs a new <b>Token</b>.
   *
   * @param id the ID for the token
   * @param type the token type
   * @param name the name of the token
   * @param issued the date and time the token was issued
   * @param validFromDate the date the token is valid from
   * @param expiryDate the date the token expires
   * @param revocationDate the date the token was revoked
   */
  public RevokedToken(
      String id,
      TokenType type,
      String name,
      OffsetDateTime issued,
      LocalDate validFromDate,
      LocalDate expiryDate,
      LocalDate revocationDate) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.issued = issued;
    this.validFromDate = validFromDate;
    this.expiryDate = expiryDate;
    this.revocationDate = revocationDate;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    RevokedToken other = (RevokedToken) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date the token expires.
   *
   * @return the date the token expires
   */
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  /**
   * Returns the ID for the token.
   *
   * @return the ID for the token
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the date and time the token was issued.
   *
   * @return the date and time the token was issued
   */
  public OffsetDateTime getIssued() {
    return issued;
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
   * Returns the date the token was revoked.
   *
   * @return the date the token was revoked
   */
  public LocalDate getRevocationDate() {
    return revocationDate;
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
  public LocalDate getValidFromDate() {
    return validFromDate;
  }

  /**
   * Set the date the token expires.
   *
   * @param expiryDate the date the token expires
   */
  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Set the ID for the token.
   *
   * @param id the ID for the token
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the date and time the token was issued.
   *
   * @param issued the date and time the token was issued
   */
  public void setIssued(OffsetDateTime issued) {
    this.issued = issued;
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
   * Set the date the token was revoked.
   *
   * @param revocationDate the date the token was revoked
   */
  public void setRevocationDate(LocalDate revocationDate) {
    this.revocationDate = revocationDate;
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
   * @param validFromDate the date the token is valid from
   */
  public void setValidFromDate(LocalDate validFromDate) {
    this.validFromDate = validFromDate;
  }
}
