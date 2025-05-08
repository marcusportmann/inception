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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
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
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code PasswordReset} class holds the information for a password rest.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A password reset")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "username",
  "requested",
  "completed",
  "expired",
  "status",
  "securityCodeHash"
})
@XmlRootElement(name = "GroupMember", namespace = "https://inception.digital/security")
@XmlType(
    name = "PasswordReset",
    namespace = "https://inception.digital/security",
    propOrder = {
      "id",
      "username",
      "requested",
      "completed",
      "expired",
      "status",
      "securityCodeHash"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
@Entity
@Table(name = "security_password_resets")
public class PasswordReset implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date and time the password reset was completed. */
  @Schema(description = "The date and time the password reset was completed")
  @JsonProperty
  @XmlElement(name = "Completed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "completed")
  private OffsetDateTime completed;

  /** The date and time the password reset expired. */
  @Schema(description = "The date and time the password reset expired")
  @JsonProperty
  @XmlElement(name = "Expired")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "expired")
  private OffsetDateTime expired;

  /** The ID for the password reset. */
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The date and time the password reset was requested. */
  @Schema(
      description = "The date and time the password reset was requested",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Requested", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "requested", nullable = false)
  private OffsetDateTime requested;

  /** The security code hash. */
  @Schema(description = "The security code hash", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SecurityCodeHash", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "security_code_hash", length = 100, nullable = false)
  private String securityCodeHash;

  /** The status of the password reset. */
  @Schema(
      description = "The status of the password reset",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Reason", required = true)
  @NotNull
  @Column(name = "status", length = 50, nullable = false)
  private PasswordResetStatus status;

  /** The username for the user associated with the password reset. */
  @Schema(
      description = "The username for the user associated with the password reset",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Username", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "username", length = 100, nullable = false)
  private String username;

  /** Creates a new {@code PasswordReset} instance. */
  public PasswordReset() {}

  /**
   * Creates a new {@code PasswordReset} instance.
   *
   * @param username the username for the user associated with the password reset
   * @param securityCodeHash the security code hash
   */
  public PasswordReset(String username, String securityCodeHash) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.username = username;
    this.securityCodeHash = securityCodeHash;
    this.requested = OffsetDateTime.now();
    this.status = PasswordResetStatus.REQUESTED;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    PasswordReset other = (PasswordReset) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the password reset was completed.
   *
   * @return the date and time the password reset was completed
   */
  public OffsetDateTime getCompleted() {
    return completed;
  }

  /**
   * Returns the date and time the password reset expired.
   *
   * @return the date and time the password reset expired
   */
  public OffsetDateTime getExpired() {
    return expired;
  }

  /**
   * Returns the ID for the password reset.
   *
   * @return the ID for the password reset
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the date and time the password reset was requested.
   *
   * @return the date and time the password reset was requested
   */
  public OffsetDateTime getRequested() {
    return requested;
  }

  /**
   * Returns the security code hash.
   *
   * @return the security code hash
   */
  public String getSecurityCodeHash() {
    return securityCodeHash;
  }

  /**
   * Returns the status of the password reset.
   *
   * @return the status of the password reset
   */
  public PasswordResetStatus getStatus() {
    return status;
  }

  /**
   * Returns the username for the user associated with the password reset.
   *
   * @return the username for the user associated with the password reset
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Set the date and time the password reset was completed.
   *
   * @param completed the date and time the password reset was completed
   */
  public void setCompleted(OffsetDateTime completed) {
    this.completed = completed;
  }

  /**
   * Set the date and time the password reset expired.
   *
   * @param expired the date and time the password reset expired
   */
  public void setExpired(OffsetDateTime expired) {
    this.expired = expired;
  }

  /**
   * Set the ID for the password reset.
   *
   * @param id the ID for the password reset
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the date and time the password reset was requested.
   *
   * @param requested the date and time the password reset was requested
   */
  public void setRequested(OffsetDateTime requested) {
    this.requested = requested;
  }

  /**
   * Set the security code hash.
   *
   * @param securityCodeHash the security code hash
   */
  public void setSecurityCodeHash(String securityCodeHash) {
    this.securityCodeHash = securityCodeHash;
  }

  /**
   * Set the status of the password reset.
   *
   * @param status the status of the password reset
   */
  public void setStatus(PasswordResetStatus status) {
    this.status = status;
  }

  /**
   * Set the username for the user associated with the password reset.
   *
   * @param username the username for the user associated with the password reset
   */
  public void setUsername(String username) {
    this.username = username;
  }
}
