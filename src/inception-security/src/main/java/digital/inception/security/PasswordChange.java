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
 * The <code>PasswordChange</code> class holds the information for a password change.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "PasswordChange")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "expirePassword", "lockUser", "newPassword", "oldPassword", "reason",
    "resetPasswordHistory", "securityCode" })
@XmlRootElement(name = "PasswordChange", namespace = "http://security.inception.digital")
@XmlType(name = "PasswordChange", namespace = "http://security.inception.digital",
    propOrder = { "expirePassword", "lockUser", "newPassword", "oldPassword", "reason",
        "resetPasswordHistory", "securityCode" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused" })
public class PasswordChange
{
  private static final long serialVersionUID = 1000000;

  /**
   * Expire the user's password when performing an administrative password change.
   */
  @ApiModelProperty(
      value = "Expire the user's password when performing an administrative password change")
  @JsonProperty
  @XmlElement(name = "ExpirePassword")
  private Boolean expirePassword;

  /**
   * Lock the user when performing an administrative password change.
   */
  @ApiModelProperty(value = "Lock the user when performing an administrative password change")
  @JsonProperty
  @XmlElement(name = "LockUser")
  private Boolean lockUser;

  /**
   * The new password.
   */
  @ApiModelProperty(value = "The new password", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "NewPassword", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String newPassword;

  /**
   * The old password when performing a user password change.
   */
  @ApiModelProperty(value = "The old password when performing a user password change")
  @JsonProperty
  @XmlElement(name = "OldPassword")
  @Size(min = 1, max = 100)
  private String oldPassword;

  /**
   * The reason for changing the password.
   */
  @ApiModelProperty(value = "The reason for changing the password", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Reason", required = true)
  @NotNull
  private PasswordChangeReason reason;

  /**
   * Reset the user's password history when performing an administrative password change.
   */
  @ApiModelProperty(
      value = "Reset the user's password history when performing an administrative password change")
  @JsonProperty
  @XmlElement(name = "ResetPasswordHistory")
  private Boolean resetPasswordHistory;

  /**
   * The security code when performing a forgotten password change.
   */
  @ApiModelProperty(value = "The security code when performing a forgotten password change.")
  @JsonProperty
  @XmlElement(name = "SecurityCode")
  private String securityCode;

  /**
   * Constructs a new <code>PasswordChange</code>.
   */
  public PasswordChange() {}

  /**
   * Constructs a new <code>PasswordChange</code> for a USER password change.
   *
   * @param newPassword the new password
   * @param oldPassword the old password when performing the user password change
   */
  public PasswordChange(String newPassword, String oldPassword)
  {
    this.newPassword = newPassword;
    this.oldPassword = oldPassword;
    this.reason = PasswordChangeReason.USER;
  }

  /**
   * Constructs a new <code>PasswordChange</code> for an ADMINISTRATIVE password change.
   *
   * @param newPassword          the new password
   * @param expirePassword       expire the user's password when performing the administrative
   *                             password change
   * @param lockUser             lock the user when performing the administrative password change
   * @param resetPasswordHistory reset the user's password history when performing the
   *                            administrative password change
   */
  public PasswordChange(String newPassword, Boolean expirePassword, Boolean lockUser,
      Boolean resetPasswordHistory)
  {
    this.newPassword = newPassword;
    this.expirePassword = expirePassword;
    this.lockUser = lockUser;
    this.resetPasswordHistory = resetPasswordHistory;
    this.reason = PasswordChangeReason.ADMINISTRATIVE;
  }

  /**
   * Returns whether the user's password should be expired when performing an administrative
   * password change.
   *
   * @return <code>true</code> if the user's password should be expired when performing an
   *         administrative password changeor <code>false</code> otherwise
   */
  public Boolean getExpirePassword()
  {
    return expirePassword;
  }

  /**
   * Returns whether the user should be locked when performing an administrative password change.
   *
   * @return <code>true</code> if the user should be locked when performing an administrative
   *         password change or <code>false</code> otherwise
   */
  public Boolean getLockUser()
  {
    return lockUser;
  }

  /**
   * Returns the new password.
   *
   * @return the new password
   */
  public String getNewPassword()
  {
    return newPassword;
  }

  /**
   * Returns the old password when performing a user password change.
   *
   * @return the old password when performing a user password change
   */
  public String getOldPassword()
  {
    return oldPassword;
  }

  /**
   * Returns the reason for changing the password.
   *
   * @return the reason for changing the password
   */
  public PasswordChangeReason getReason()
  {
    return reason;
  }

  /**
   * Returns whether the user's password history should be reset when performing an administrative
   * password change.
   *
   * @return <code>true</code> if the user's password history should be reset when performing an
   *         administrative password changeor <code>false</code> otherwise
   */
  public Boolean getResetPasswordHistory()
  {
    return resetPasswordHistory;
  }

  /**
   * Returns the security code when performing a forgotten password change.
   *
   * @return the security code when performing a forgotten password change
   */
  public String getSecurityCode()
  {
    return securityCode;
  }

  /**
   * Set whether the user's password should be expired when performing an administrative password
   * change.
   *
   * @param expirePassword expire the user's password when performing an administrative password
   *                       change
   */
  public void setExpirePassword(Boolean expirePassword)
  {
    this.expirePassword = expirePassword;
  }

  /**
   * Set whether the user should be locked when performing an administrative password change.
   *
   * @param lockUser lock the user when performing an administrative password change
   */
  public void setLockUser(Boolean lockUser)
  {
    this.lockUser = lockUser;
  }

  /**
   * Set the new password when performing a user password change.
   *
   * @param newPassword the new password when performing a new password change
   */
  public void setNewPassword(String newPassword)
  {
    this.newPassword = newPassword;
  }

  /**
   * Set the old password when performing a user password change.
   *
   * @param oldPassword the old password when performing a user password change
   */
  public void setOldPassword(String oldPassword)
  {
    this.oldPassword = oldPassword;
  }

  /**
   * Set the reason for changing the password.
   *
   * @param reason the reason for changing the password
   */
  public void setReason(PasswordChangeReason reason)
  {
    this.reason = reason;
  }

  /**
   * Set whether the user's password history should be reset when performing an administrative
   * password change.
   *
   * @param resetPasswordHistory reset the user's password history when performing an administrative
   *                             password change
   */
  public void setResetPasswordHistory(Boolean resetPasswordHistory)
  {
    this.resetPasswordHistory = resetPasswordHistory;
  }

  /**
   * Set the security code when performing a forgotten password change.
   *
   * @param securityCode the security code when performing a forgotten password change
   */
  public void setSecurityCode(String securityCode)
  {
    this.securityCode = securityCode;
  }
}