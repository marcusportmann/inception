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

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.UUID;

/**
 * The <code>User</code> class stores the information for a user.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public class User
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String email;
  private String firstName;
  private UUID id;
  private boolean isReadOnly;
  private String lastName;
  private String mobileNumber;
  private String password;
  private Integer passwordAttempts;
  private LocalDateTime passwordExpiry;
  private String phoneNumber;
  private HashMap<String, String> properties = new HashMap<>();
  private UserStatus status;
  private UUID userDirectoryId;
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
   * Returns the password hash for the user
   *
   * @return the password hash for the user
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
   * Returns the value of the user property with the specified name or <code>null</code> if the
   * user property does not exist.
   *
   * @param name the name of the user property
   *
   * @return the value of the user property with the specified name or <code>null</code> if the
   *         user property does not exist
   */
  public String getProperty(String name)
  {
    return properties.get(name);
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
   * Returns <code>true</code> if the user has a property with the specified name or
   * <code>false</code> otherwise.
   *
   * @param name the name of the user property
   *
   * @return <code>true</code> if the user has a property with the specified name or
   *         <code>false</code> otherwise
   */
  public boolean hasProperty(String name)
  {
    return properties.containsKey(name);
  }

  /**
   * Is the user active?
   *
   * @return <code>true</code> if the user is active or <code>false</code> otherwise
   */
  public boolean isActive()
  {
    return (status == UserStatus.ACTIVE);
  }

  /**
   * Is the user expired?
   *
   * @return <code>true</code> if the user is expired or <code>false</code> otherwise
   */
  public boolean isExpired()
  {
    return (status == UserStatus.EXPIRED);
  }

  /**
   * Is the user locked?
   *
   * @return <code>true</code> if the user is locked or <code>false</code> otherwise
   */
  public boolean isLocked()
  {
    return (status == UserStatus.LOCKED);
  }

  /**
   * Returns <code>true</code> if the user is read-only or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the user is read-only or <code>false</code> otherwise
   */
  public boolean isReadOnly()
  {
    return isReadOnly;
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
   * Set the password for the user.
   *
   * @param password the password for the user
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
  public void setPasswordAttempts(int passwordAttempts)
  {
    this.passwordAttempts = passwordAttempts;
  }

  /**
   * Set the password expiry for the user
   *
   * @param passwordExpiry the password expiry for the user
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
   * Set the value of the user property.
   *
   * @param name  the name of the user property
   * @param value the value of the user property
   */
  public void setProperty(String name, String value)
  {
    properties.put(name, value);
  }

  /**
   * Set whether the user is read-only.
   *
   * @param isReadOnly <code>true</code> if the user is read-only or <code>false</code> otherwise
   */
  public void setReadOnly(boolean isReadOnly)
  {
    this.isReadOnly = isReadOnly;
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
