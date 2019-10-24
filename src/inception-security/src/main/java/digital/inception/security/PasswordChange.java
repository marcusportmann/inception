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
@JsonPropertyOrder({ "expirePassword", "lockUser", "password", "reason", "resetPasswordHistory" })
@XmlRootElement(name = "GroupMember", namespace = "http://security.inception.digital")
@XmlType(name = "PasswordChange", namespace = "http://security.inception.digital",
    propOrder = { "expirePassword", "lockUser", "password", "reason", "resetPasswordHistory" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused" })
public class PasswordChange
{
  private static final long serialVersionUID = 1000000;

  /**
   * Expire the user's password.
   */
  @ApiModelProperty(value = "Expire the user's password", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ExpirePassword", required = true)
  @NotNull
  private boolean expirePassword;

  /**
   * Lock the user.
   */
  @ApiModelProperty(value = "Lock the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LockUser", required = true)
  @NotNull
  private boolean lockUser;

  /**
   * The password.
   */
  @ApiModelProperty(value = "The password", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Password", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String password;

  /**
   * The reason for changing the password.
   */
  @ApiModelProperty(value = "The reason for changing the password", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Reason", required = true)
  @NotNull
  private PasswordChangeReason reason;

  /**
   * Reset the user's password history.
   */
  @ApiModelProperty(value = "Reset the user's password history", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ResetPasswordHistory", required = true)
  @NotNull
  private boolean resetPasswordHistory;

  /**
   * Constructs a new <code>PasswordChange</code>.
   */
  public PasswordChange() {}

  /**
   * Constructs a new <code>PasswordChange</code>.
   *
   * @param password             the password
   * @param expirePassword       expire the user's password
   * @param lockUser             lock the user
   * @param resetPasswordHistory reset the user's password history
   * @param reason               the reason for changing the password
   */
  public PasswordChange(String password, boolean expirePassword, boolean lockUser,
      boolean resetPasswordHistory, PasswordChangeReason reason)
  {
    this.password = password;
    this.expirePassword = expirePassword;
    this.lockUser = lockUser;
    this.resetPasswordHistory = resetPasswordHistory;
    this.reason = reason;
  }

  /**
   * Returns whether the user's password should be expired.
   *
   * @return <code>true</code> if the user's password should be expired or <code>false</code>
   *         otherwise
   */
  public boolean getExpirePassword()
  {
    return expirePassword;
  }

  /**
   * Returns whether the user should be locked.
   *
   * @return <code>true</code> if the user should be locked or <code>false</code> otherwise
   */
  public boolean getLockUser()
  {
    return lockUser;
  }

  /**
   * Returns the password.
   *
   * @return the password
   */
  public String getPassword()
  {
    return password;
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
   * Returns whether the user's password history should be reset.
   *
   * @return <code>true</code> if the user's password history should be reset or <code>false</code>
   *         otherwise
   */
  public boolean getResetPasswordHistory()
  {
    return resetPasswordHistory;
  }

  /**
   * Set whether the user's password should be expired.
   *
   * @param expirePassword expire the user's password
   */
  public void setExpirePassword(boolean expirePassword)
  {
    this.expirePassword = expirePassword;
  }

  /**
   * Set whether the user should be locked.
   *
   * @param lockUser lock the user
   */
  public void setLockUser(boolean lockUser)
  {
    this.lockUser = lockUser;
  }

  /**
   * Set the password.
   *
   * @param password the password
   */
  public void setPassword(String password)
  {
    this.password = password;
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
   * Set whether the user's password history should be reset.
   *
   * @param resetPasswordHistory reset the user's password history
   */
  public void setResetPasswordHistory(boolean resetPasswordHistory)
  {
    this.resetPasswordHistory = resetPasswordHistory;
  }
}
