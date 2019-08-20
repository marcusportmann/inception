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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import digital.inception.core.xml.LocalDateTimeAdapter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <code>User</code> class holds the information for a user.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "User")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "userDirectoryId", "username", "firstName", "lastName", "mobileNumber",
    "phoneNumber", "email", "password", "passwordAttempts", "passwordExpiry", "status", "readOnly",
    "externalReference" })
@XmlRootElement(name = "User", namespace = "http://security.inception.digital")
@XmlType(name = "User", namespace = "http://security.inception.digital",
    propOrder = { "id", "userDirectoryId", "username", "firstName", "lastName", "mobileNumber",
        "phoneNumber", "email", "password", "passwordAttempts", "passwordExpiry", "status",
        "readOnly", "externalReference" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class User
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The e-mail address for the user.
   */
  @ApiModelProperty(value = "The e-mail address for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Email", required = true)
  @NotNull
  @Size(max = 4000)
  private String email;

  /**
   * The optional external reference for the user.
   */
  @ApiModelProperty(value = "The optional external reference for the user")
  @JsonProperty
  @XmlElement(name = "ExternalReference")
  private String externalReference;

  /**
   * The first name for the user.
   */
  @ApiModelProperty(value = "The first name for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "FirstName", required = true)
  @NotNull
  @Size(max = 4000)
  private String firstName;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /**
   * The last name for the user.
   */
  @ApiModelProperty(value = "The last name for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LastName", required = true)
  @NotNull
  @Size(max = 4000)
  private String lastName;

  /**
   * The mobile number for the user.
   */
  @ApiModelProperty(value = "The mobile number for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MobileNumber", required = true)
  @NotNull
  @Size(max = 4000)
  private String mobileNumber;

  /**
   * The password or password hash for the user.
   */
  @ApiModelProperty(value = "The password or password hash for the user")
  @JsonProperty
  @XmlElement(name = "Password")
  @Size(max = 4000)
  private String password;

  /**
   * The number of failed authentication attempts as a result of an incorrect password for the user.
   */
  @ApiModelProperty(
      value = "The number of failed authentication attempts as a result of an incorrect password for the user",
      example = "0")
  @JsonProperty
  @XmlElement(name = "PasswordAttempts")
  private Integer passwordAttempts;

  /**
   * The optional date and time the password for the user expires.
   */
  @ApiModelProperty(value = "The optional date and time the password for the user expires")
  @JsonProperty
  @XmlElement(name = "PasswordExpiry")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private LocalDateTime passwordExpiry;

  /**
   * The phone number for the user.
   */
  @ApiModelProperty(value = "The phone number for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PhoneNumber", required = true)
  @NotNull
  @Size(max = 4000)
  private String phoneNumber;

  /**
   * Is the user read-only.
   */
  @ApiModelProperty(value = "Is the user read-only")
  @JsonProperty
  @XmlElement(name = "ReadOnly")
  private Boolean readOnly;

  /**
   * The status for the user.
   */
  @ApiModelProperty(value = "The status for the user",
      allowableValues = "0 = Inactive, 1 = Active, 2 = Locked, 3 = Expired", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  private UserStatus status;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory the user
   * is associated with.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory the user is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  private UUID userDirectoryId;

  /**
   * The username for the user.
   */
  @ApiModelProperty(value = "The username for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Username", required = true)
  @NotNull
  @Size(max = 4000)
  private String username;

  /**
   * Constructs a new <code>User</code>.
   */
  public User() {}

  /**
   * Returns the e-mail address for the user
   *
   * @return the e-mail address for the user
   */
  public String getEmail()
  {
    return email;
  }

  /**
   * Returns the optional external reference for the user.
   *
   * @return the optional external reference for the user
   */
  public String getExternalReference()
  {
    return externalReference;
  }

  /**
   * Returns the first name for the user
   *
   * @return the first name for the user
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the last name for the user
   *
   * @return the last name for the user
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * Returns the mobile number for the user
   *
   * @return the mobile number for the user
   */
  public String getMobileNumber()
  {
    return mobileNumber;
  }

  /**
   * Returns the password or password hash for the user
   *
   * @return the password or password hash for the user
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Returns the number of failed authentication attempts as a result of an incorrect password for
   * the user
   *
   * @return the number of failed authentication attempts as a result of an incorrect password for
   *         the user
   */
  public Integer getPasswordAttempts()
  {
    return passwordAttempts;
  }

  /**
   * Returns the optional date and time the password for the user expires
   *
   * @return the optional date and time the password for the user expires
   */
  public LocalDateTime getPasswordExpiry()
  {
    return passwordExpiry;
  }

  /**
   * Returns the phone number for the user.
   *
   * @return the phone number for the user
   */
  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  /**
   * Returns the status for the user.
   *
   * @return the status for the user
   */
  public UserStatus getStatus()
  {
    return status;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * the user is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *         the user is associated with
   */
  public UUID getUserDirectoryId()
  {
    return userDirectoryId;
  }

  /**
   * Returns the username for the user.
   *
   * @return the username for the user
   */
  public String getUsername()
  {
    if (username != null)
    {
      username = username.toLowerCase();
    }

    return username;
  }

  /**
   * Has the password for the user expired?
   *
   * @return <code>true</code> if the password for the user has expired or <code>false</code>
   *         otherwise
   */
  public boolean hasPasswordExpired()
  {
    if (passwordExpiry != null)
    {
      return LocalDateTime.now().isAfter(passwordExpiry);
    }

    return false;
  }

  /**
   * Is the user active?
   *
   * @return <code>true</code> if the user is active or <code>false</code> otherwise
   */
  @JsonIgnore
  @XmlTransient
  public boolean isActive()
  {
    return (status == UserStatus.ACTIVE);
  }

  /**
   * Is the user expired?
   *
   * @return <code>true</code> if the user is expired or <code>false</code> otherwise
   */
  @JsonIgnore
  @XmlTransient
  public boolean isExpired()
  {
    return (status == UserStatus.EXPIRED);
  }

  /**
   * Is the user locked?
   *
   * @return <code>true</code> if the user is locked or <code>false</code> otherwise
   */
  @JsonIgnore
  @XmlTransient
  public boolean isLocked()
  {
    return (status == UserStatus.LOCKED);
  }

  /**
   * Returns <code>true</code> if the user is read-only or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the user is read-only or <code>false</code> otherwise
   */
  public Boolean isReadOnly()
  {
    return readOnly;
  }

  /**
   * Set the e-mail address for the user.
   *
   * @param email the e-mail address for the user
   */
  public void setEmail(String email)
  {
    this.email = email;
  }

  /**
   * Set the optional external reference for the user.
   *
   * @param externalReference the optional external reference for the user
   */
  public void setExternalReference(String externalReference)
  {
    this.externalReference = externalReference;
  }

  /**
   * Set the first name for the user.
   *
   * @param firstName the first name for the user
   */
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the user.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the user
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the last name for the user.
   *
   * @param lastName the last name for the user
   */
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  /**
   * Set the mobile number for the user.
   *
   * @param mobileNumber the mobile number for the user
   */
  public void setMobileNumber(String mobileNumber)
  {
    this.mobileNumber = mobileNumber;
  }

  /**
   * Set the password or password hash for the user.
   *
   * @param password the password or password hash for the user
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Set the password attempts for the user.
   *
   * @param passwordAttempts the password attempts for the user
   */
  public void setPasswordAttempts(Integer passwordAttempts)
  {
    this.passwordAttempts = passwordAttempts;
  }

  /**
   * Set the optional password expiry for the user
   *
   * @param passwordExpiry the optional password expiry for the user
   */
  public void setPasswordExpiry(LocalDateTime passwordExpiry)
  {
    this.passwordExpiry = passwordExpiry;
  }

  /**
   * Set the phone number for the user.
   *
   * @param phoneNumber the phone number for the user
   */
  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Set whether the user is read-only.
   *
   * @param readOnly <code>true</code> if the user is read-only or <code>false</code> otherwise
   */
  public void setReadOnly(Boolean readOnly)
  {
    this.readOnly = readOnly;
  }

  /**
   * Set the status for the user.
   *
   * @param status the status for the user
   */
  public void setStatus(UserStatus status)
  {
    this.status = status;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the user directory the
   * user is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory the user is associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }

  /**
   * Set the username for the user.
   *
   * @param username the username for the user
   */
  public void setUsername(String username)
  {
    this.username = username;
  }
}
