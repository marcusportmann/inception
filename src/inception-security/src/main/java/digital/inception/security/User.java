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

import java.io.Serializable;

import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

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
@JsonPropertyOrder({ "userDirectoryId", "username", "firstName", "lastName", "mobileNumber",
    "phoneNumber", "email", "status", "password", "passwordAttempts", "passwordExpiry" })
@XmlRootElement(name = "User", namespace = "http://security.inception.digital")
@XmlType(name = "User", namespace = "http://security.inception.digital",
    propOrder = { "userDirectoryId", "username", "firstName", "lastName", "mobileNumber",
        "phoneNumber", "email", "status", "password", "passwordAttempts", "passwordExpiry" })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "security", name = "users")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class User
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The e-mail address for the user.
   */
  @ApiModelProperty(value = "The e-mail address for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Email", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "email", nullable = false, length = 100)
  private String email;

  /**
   * The first name for the user.
   */
  @ApiModelProperty(value = "The first name for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "FirstName", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  /**
   * The groups the user is associated with.
   */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(mappedBy = "users")
  private Set<Group> groups = new HashSet<>();

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user.
   */
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /**
   * The last name for the user.
   */
  @ApiModelProperty(value = "The last name for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LastName", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  /**
   * The mobile number for the user.
   */
  @ApiModelProperty(value = "The mobile number for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MobileNumber", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "mobile", nullable = false, length = 100)
  private String mobileNumber;

  /**
   * The password or password hash for the user.
   *
   * NOTE: The password is not required as part of the JSON or XML representation of the user,
   *       other than when creating the user, so the field is nullable but the database column is
   *       not.
   */
  @ApiModelProperty(value = "The password or password hash for the user")
  @JsonProperty
  @XmlElement(name = "Password")
  @Size(max = 100)
  @Column(name = "password", nullable = false, length = 100)
  private String password;

  /**
   * The number of failed authentication attempts as a result of an incorrect password for the user.
   */
  @ApiModelProperty(
      value = "The number of failed authentication attempts as a result of an incorrect password for the user",
      example = "0")
  @JsonProperty
  @XmlElement(name = "PasswordAttempts")
  @Column(name = "password_attempts", nullable = false)
  private Integer passwordAttempts;

  /**
   * The date and time the password for the user expires.
   */
  @ApiModelProperty(value = "The date and time the password for the user expires")
  @JsonProperty
  @XmlElement(name = "PasswordExpiry")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "password_expiry", nullable = false)
  private LocalDateTime passwordExpiry;

  /**
   * The phone number for the user.
   */
  @ApiModelProperty(value = "The phone number for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PhoneNumber", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "phone", nullable = false, length = 100)
  private String phoneNumber;

  /**
   * The status for the user.
   */
  @ApiModelProperty(value = "The status for the user",
      allowableValues = "0 = Inactive, 1 = Active, 2 = Locked, 3 = Expired", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
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
  @Column(name = "user_directory_id", nullable = false)
  private UUID userDirectoryId;

  /**
   * The username for the user.
   */
  @ApiModelProperty(value = "The username for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Username", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "username", nullable = false, length = 100)
  private String username;

  /**
   * Constructs a new <code>User</code>.
   */
  public User() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the object argument otherwise
   *         <code>false</code>
   */
  @Override
  public boolean equals(Object object)
  {
    if (this == object)
    {
      return true;
    }

    if (object == null)
    {
      return false;
    }

    if (getClass() != object.getClass())
    {
      return false;
    }

    User other = (User) object;

    return (id != null) && id.equals(other.id);
  }

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
   * Returns the first name for the user
   *
   * @return the first name for the user
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Returns the groups the user is associated with.
   *
   * @return the groups the user is associated with
   */
  public Set<Group> getGroups()
  {
    return groups;
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
   * Returns the date and time the password for the user expires
   *
   * @return the date and time the password for the user expires
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
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode()
  {
    return (id == null)
        ? 0
        : id.hashCode();
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
   * Set the e-mail address for the user.
   *
   * @param email the e-mail address for the user
   */
  public void setEmail(String email)
  {
    this.email = email;
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
   * Set the groups the user is associated with.
   *
   * @param groups the groups the user is associated with
   */
  public void setGroups(Set<Group> groups)
  {
    this.groups = groups;
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
   * Set the date and time the password for the user expires.
   *
   * @param passwordExpiry the date and time the password for the user expires
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
