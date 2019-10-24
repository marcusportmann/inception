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

import {PasswordChangeReason} from "./password-change-reason";

/**
 * The PasswordChange class holds the information for a password change.
 *
 * @author Marcus Portmann
 */
export class PasswordChange {

  /**
   * Expire the user's password.
   */
  expirePassword: boolean;

  /**
   * Lock the user.
   */
  lockUser: boolean

  /**
   * The password.
   */
  password: string;

  /**
   * The reason for changing the password.
   */
  reason: PasswordChangeReason

  /**
   * Reset the user's password history.
   */
  resetPasswordHistory: boolean;

  /**
   * Constructs a new PasswordChange.
   *
   * @param password             The password.
   * @param expirePassword       Expire the user's password.
   * @param lockUser             Lock the user.
   * @param resetPasswordHistory Reset the user's password history.
   * @param reason               The reason for changing the password.
   */
  constructor(password: string, expirePassword: boolean, lockUser: boolean,
              resetPasswordHistory: boolean, reason: PasswordChangeReason) {
    this.password = password;
    this.expirePassword = expirePassword;
    this.lockUser = lockUser;
    this.resetPasswordHistory = resetPasswordHistory;
    this.reason = reason;
  }
}
