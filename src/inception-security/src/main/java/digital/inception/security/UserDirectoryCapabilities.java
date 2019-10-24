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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>UserDirectoryCapabilities</code> class holds the information that describes the
 * capabilities supported by a user directory.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "UserDirectoryCapabilities")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "supportsAdminChangePassword", "supportsChangePassword",
    "supportsGroupAdministration", "supportsPasswordExpiry", "supportsPasswordHistory",
    "supportsUserAdministration" })
@XmlRootElement(name = "UserDirectoryCapabilities", namespace = "http://security.inception.digital")
@XmlType(name = "UserDirectoryCapabilities", namespace = "http://security.inception.digital",
    propOrder = { "supportsAdminChangePassword", "supportsChangePassword",
        "supportsGroupAdministration", "supportsPasswordExpiry", "supportsPasswordHistory",
        "supportsUserAdministration" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused" })
public class UserDirectoryCapabilities
{
  /**
   * The user directory supports the admin change password capability.
   */
  @ApiModelProperty(value = "The the user directory supports the admin change password capability",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SupportsAdminChangePassword", required = true)
  @NotNull
  private boolean supportsAdminChangePassword;

  /**
   * The user directory supports the change password capability.
   */
  @ApiModelProperty(value = "The user directory supports the change password capability",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SupportsChangePassword", required = true)
  @NotNull
  private boolean supportsChangePassword;

  /**
   * The user directory supports the group administration capability.
   */
  @ApiModelProperty(value = "The user directory supports the group administration capability",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SupportsGroupAdministration", required = true)
  @NotNull
  private boolean supportsGroupAdministration;

  /**
   * The user directory supports the password expiry capability.
   */
  @ApiModelProperty(value = "The user directory supports the password expiry capability",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SupportsPasswordExpiry", required = true)
  @NotNull
  private boolean supportsPasswordExpiry;

  /**
   * The user directory supports the password history capability.
   */
  @ApiModelProperty(value = "The user directory supports the password history capability",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SupportsPasswordHistory", required = true)
  @NotNull
  private boolean supportsPasswordHistory;

  /**
   * The user directory supports the user administration capability.
   */
  @ApiModelProperty(value = "The user directory supports the user administration capability",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SupportsUserAdministration", required = true)
  @NotNull
  private boolean supportsUserAdministration;

  /**
   * The user directory supports the user locks capability.
   */
  @ApiModelProperty(value = "The user directory supports the user locks capability",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SupportsUserLocks", required = true)
  @NotNull
  private boolean supportsUserLocks;

  /**
   * Constructs a new <code>UserDirectoryCapabilities</code>.
   */
  public UserDirectoryCapabilities() {}

  /**
   * Constructs a new <code>UserDirectoryCapabilities</code>.
   *
   * @param supportsAdminChangePassword the user directory supports the admin change
   *                                    password capability
   * @param supportsChangePassword      the user directory supports the change password capability
   * @param supportsGroupAdministration the user directory supports the group administration
   *                                    capability
   * @param supportsPasswordExpiry      the user directory supports the password expiry capability
   * @param supportsPasswordHistory     the user directory supports the password history capability
   * @param supportsUserAdministration  the user directory supports the user administration
   *                                    capability
   * @param supportsUserLocks           the user directory supports the user locks capability
   */
  public UserDirectoryCapabilities(boolean supportsAdminChangePassword,
      boolean supportsChangePassword, boolean supportsGroupAdministration,
      boolean supportsPasswordExpiry, boolean supportsPasswordHistory,
      boolean supportsUserAdministration, boolean supportsUserLocks)
  {
    this.supportsAdminChangePassword = supportsAdminChangePassword;
    this.supportsChangePassword = supportsChangePassword;
    this.supportsGroupAdministration = supportsGroupAdministration;
    this.supportsPasswordExpiry = supportsPasswordExpiry;
    this.supportsPasswordHistory = supportsPasswordHistory;
    this.supportsUserAdministration = supportsUserAdministration;
    this.supportsUserLocks = supportsUserLocks;
  }

  /**
   * Returns whether the user directory supports the admin change password capability.
   *
   * @return <code>true</code> if the user directory supports the admin change password capability
   *         or <code>false</code> otherwise
   */
  public boolean getSupportsAdminChangePassword()
  {
    return supportsAdminChangePassword;
  }

  /**
   * Returns whether the user directory supports the change password capability.
   *
   * @return <code>true</code> if the user directory supports the change password capability or
   *         <code>false</code> otherwise
   */
  public boolean getSupportsChangePassword()
  {
    return supportsChangePassword;
  }

  /**
   * Returns whether the user directory supports the group administration capability.
   *
   * @return <code>true</code> if the user directory supports the group administration capability or
   *         <code>false</code> otherwise
   */
  public boolean getSupportsGroupAdministration()
  {
    return supportsGroupAdministration;
  }

  /**
   * Returns whether the user directory supports the password expiry capability.
   *
   * @return <code>true</code> if the user directory supports the password expiry capability or
   *         <code>false</code> otherwise
   */
  public boolean getSupportsPasswordExpiry()
  {
    return supportsPasswordExpiry;
  }

  /**
   * Returns whether the user directory supports the password history capability.
   *
   * @return <code>true</code> if the user directory supports the password history capability or
   *         <code>false</code> otherwise
   */
  public boolean getSupportsPasswordHistory()
  {
    return supportsPasswordHistory;
  }

  /**
   * Returns whether the user directory supports the user administration capability.
   *
   * @return <code>true</code> if the user directory supports the user administration capability or
   *         <code>false</code> otherwise
   */
  public boolean getSupportsUserAdministration()
  {
    return supportsUserAdministration;
  }

  /**
   * Returns whether the user directory supports the user locks capability.
   *
   * @return <code>true</code> if the user directory supports the user locks capability or
   *         <code>false</code>
   */
  public boolean getSupportsUserLocks()
  {
    return supportsUserLocks;
  }
}
