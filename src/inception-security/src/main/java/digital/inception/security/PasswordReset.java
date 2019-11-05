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

package digital.inception.security;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import digital.inception.core.xml.LocalDateTimeAdapter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.time.LocalDateTime;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <code>PasswordReset</code> class holds the information for a password rest.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "PasswordReset")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "username", "requested", "completed", "expired", "status",
    "securityCodeHash" })
@XmlRootElement(name = "GroupMember", namespace = "http://security.inception.digital")
@XmlType(name = "PasswordReset", namespace = "http://security.inception.digital",
    propOrder = { "username", "requested", "completed", "expired", "status", "securityCodeHash" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused" })
@Entity
@Table(schema = "security", name = "password_resets")
@IdClass(PasswordResetId.class)
public class PasswordReset
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The date and time the password reset was completed.
   */
  @ApiModelProperty(value = "The date and time the password reset was completed")
  @JsonProperty
  @XmlElement(name = "Completed")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "completed")
  private LocalDateTime completed;

  /**
   * The date and time the password reset expired.
   */
  @ApiModelProperty(value = "The date and time the password reset expired")
  @JsonProperty
  @XmlElement(name = "Expired")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "expired")
  private LocalDateTime expired;

  /**
   * The date and time the password reset was requested.
   */
  @ApiModelProperty(value = "The date and time the password reset was requested")
  @JsonProperty
  @XmlElement(name = "Requested")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Id
  @Column(name = "requested", nullable = false)
  private LocalDateTime requested;

  /**
   * The security code hash.
   */
  @ApiModelProperty(value = "The security code hash", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SecurityCodeHash", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "security_code_hash", nullable = false, length = 100)
  private String securityCodeHash;

  /**
   * The status of the password reset.
   */
  @ApiModelProperty(value = "The status of the password reset", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Reason", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private PasswordResetStatus status;

  /**
   * The username for the user associated with the password reset.
   */
  @ApiModelProperty(value = "The username for the user associated with the password reset",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Username", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "username", nullable = false, length = 100)
  private String username;

  /**
   * Constructs a new <code>PasswordReset</code>.
   */
  public PasswordReset() {}

  /**
   * Constructs a new <code>PasswordReset</code>.
   *
   * @param username         the username for the user associated with the password reset
   * @param securityCodeHash the security code hash
   */
  public PasswordReset(String username, String securityCodeHash)
  {
    this.username = username;
    this.securityCodeHash = securityCodeHash;
    this.requested = LocalDateTime.now();
    this.status = PasswordResetStatus.REQUESTED;
  }

  /**
   * Returns the date and time the password reset was completed.
   *
   * @return the date and time the password reset was completed
   */
  public LocalDateTime getCompleted()
  {
    return completed;
  }

  /**
   * Returns the date and time the password reset expired.
   *
   * @return the date and time the password reset expired
   */
  public LocalDateTime getExpired()
  {
    return expired;
  }

  /**
   * Returns the date and time the password reset was requested.
   *
   * @return the date and time the password reset was requested
   */
  public LocalDateTime getRequested()
  {
    return requested;
  }

  /**
   * Returns the security code hash.
   *
   * @return the security code hash
   */
  public String getSecurityCodeHash()
  {
    return securityCodeHash;
  }

  /**
   * Returns the status of the password reset.
   *
   * @return the status of the password reset
   */
  public PasswordResetStatus getStatus()
  {
    return status;
  }

  /**
   * Returns the username for the user associated with the password reset.
   *
   *  @return the username for the user associated with the password reset
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Set the date and time the password reset was completed.
   *
   * @param completed the date and time the password reset was completed
   */
  public void setCompleted(LocalDateTime completed)
  {
    this.completed = completed;
  }

  /**
   * Set the date and time the password reset expired.
   *
   * @param expired the date and time the password reset expired
   */
  public void setExpired(LocalDateTime expired)
  {
    this.expired = expired;
  }

  /**
   * Set the date and time the password reset was requested.
   *
   * @param requested the date and time the password reset was requested
   */
  public void setRequested(LocalDateTime requested)
  {
    this.requested = requested;
  }

  /**
   * Set the security code hash.
   *
   * @param securityCodeHash the security code hash
   */
  public void setSecurityCodeHash(String securityCodeHash)
  {
    this.securityCodeHash = securityCodeHash;
  }

  /**
   * Set the status of the password reset.
   *
   * @param status the status of the password reset
   */
  public void setStatus(PasswordResetStatus status)
  {
    this.status = status;
  }

  /**
   * Set the username for the user associated with the password reset.
   *
   * @param username the username for the user associated with the password reset
   */
  public void setUsername(String username)
  {
    this.username = username;
  }
}
