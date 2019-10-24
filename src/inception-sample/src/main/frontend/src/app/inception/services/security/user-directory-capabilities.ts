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

/**
 * The UserDirectoryCapabilities class holds the information that describes the capabilities
 * supported by a user directory.
 *
 * @author Marcus Portmann
 */
export class UserDirectoryCapabilities {

  /**
   * The user directory supports the admin change password capability.
   */
  supportsAdminChangePassword: boolean;

  /**
   * The user directory supports the change password capability.
   */
  supportsChangePassword: boolean;

  /**
   * The user directory supports the group administration capability.
   */
  supportsGroupAdministration: boolean;

  /**
   * The user directory supports the password expiry capability.
   */
  supportsPasswordExpiry: boolean;

  /**
   * The user directory supports the password history capability.
   */
  supportsPasswordHistory: boolean;

  /**
   * The user directory supports the user administration capability.
   */
  supportsUserAdministration: boolean;

  /**
   * The user directory supports the user locks capability.
   */
  supportsUserLocks: boolean;

  /**
   * Constructs a new UserDirectoryCapabilities.
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
   * * @param supportsUserLocks         the user directory supports the user locks capability
   */
  constructor(supportsAdminChangePassword: boolean, supportsChangePassword: boolean,
              supportsGroupAdministration: boolean, supportsPasswordExpiry: boolean,
              supportsPasswordHistory: boolean, supportsUserAdministration: boolean,
              supportsUserLocks: boolean) {
    this.supportsAdminChangePassword = supportsAdminChangePassword;
    this.supportsChangePassword = supportsChangePassword;
    this.supportsGroupAdministration = supportsGroupAdministration;
    this.supportsPasswordExpiry = supportsPasswordExpiry;
    this.supportsPasswordHistory = supportsPasswordHistory;
    this.supportsUserAdministration = supportsUserAdministration;
    this.supportsUserLocks = supportsUserLocks;
  }
}
