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
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
 * The <b>TokenSummary</b> class holds the summary information for a token.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A token summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "type",
  "name",
  "description",
  "issued",
  "status",
  "validFrom",
  "expires",
  "revoked"
})
@XmlRootElement(name = "TokenSummary", namespace = "http://inception.digital/security")
@XmlType(
    name = "TokenSummary",
    namespace = "http://inception.digital/security",
    propOrder = {
      "id",
      "type",
      "name",
      "description",
      "issued",
      "status",
      "validFrom",
      "expires",
      "revoked"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "tokens")
public class TokenSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The description for the token. */
  @Schema(description = "The description for the token")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 200)
  @Column(name = "description", length = 200)
  private String description;

  /** The date the token expires. */
  @Schema(description = "The date the token expires")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "expires")
  private LocalDate expires;

  /** The ID for the token. */
  @Schema(description = "The ID for the token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "id", nullable = false)
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
  @Column(name = "issued", nullable = false)
  private OffsetDateTime issued;

  /** The name of the token. */
  @Schema(description = "The name of the token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The date the token was revoked. */
  @Schema(description = "The date the token was revoked")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "revoked")
  private LocalDate revoked;

  /** The token type. */
  @Schema(description = "The token type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", nullable = false)
  private TokenType type;

  /** The date the token is valid from. */
  @Schema(description = "The date the token is valid from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "valid_from")
  private LocalDate validFrom;

  /** Constructs a new <b>TokenSummary</b>. */
  public TokenSummary() {}

  /**
   * Constructs a new <b>TokenSummary</b>.
   *
   * @param id the ID for the token
   * @param type the token type
   * @param name the name of the token
   * @param description the description for the token
   * @param issued the date and time the token was issued
   * @param validFrom the date the token is valid from
   * @param expires the date the token expires
   */
  public TokenSummary(
      String id,
      TokenType type,
      String name,
      String description,
      OffsetDateTime issued,
      LocalDate validFrom,
      LocalDate expires) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.description = description;
    this.issued = issued;
    this.validFrom = validFrom;
    this.expires = expires;
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

    TokenSummary other = (TokenSummary) object;

    return Objects.equals(id, other.id);
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
  public LocalDate getRevoked() {
    return revoked;
  }

  /**
   * Returns the token status.
   *
   * @return the token status
   */
  @JsonProperty
  @XmlElement(name = "Status", required = true)
  public TokenStatus getStatus() {
    LocalDate now = LocalDate.now();

    if (revoked != null) {
      return TokenStatus.REVOKED;
    } else if ((expires != null) && (now.isEqual(expires) || now.isAfter(expires))) {
      return TokenStatus.EXPIRED;
    } else if ((validFrom != null) && (now.isBefore(validFrom))) {
      return TokenStatus.PENDING;
    } else {
      return TokenStatus.ACTIVE;
    }
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
   * @param revoked the date the token was revoked
   */
  public void setRevoked(LocalDate revoked) {
    this.revoked = revoked;
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
