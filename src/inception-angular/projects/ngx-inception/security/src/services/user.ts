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

import {UserStatus} from './user-status';

/**
 * The User class holds the information for a user.
 *
 * @author Marcus Portmann
 */
export class User {

  /**
   * The email address for the user.
   */
  email: string | null = null;

  /**
   * The mobile number for the user.
   */
  mobileNumber: string | null = null;

  /**
   * The name of the user.
   */
  name: string;

  /**
   * The password or password hash for the user.
   */
  password: string;

  /**
   * The number of failed authentication attempts as a result of an incorrect password for the user.
   */
  passwordAttempts: number | null = null;

  /**
   * The date and time the password for the user expires.
   */
  passwordExpiry: Date | null = null;

  /**
   * The phone number for the user.
   */
  phoneNumber: string | null = null;

  /**
   * The preferred name for the user.
   */
  preferredName: string | null = null;

  /**
   * The status for the user.
   */
  status: UserStatus;

  /**
   * The ID for the user directory the user is associated with.
   */
  userDirectoryId: string;

  /**
   * The username for the user.
   */
  username: string;

  /**
   * Constructs a new User.
   *
   * @param userDirectoryId   The ID for the user directory the user is associated with.
   * @param username          The username for the user.
   * @param name              The name of the user.
   * @param preferredName     The preferred name for the user.
   * @param mobileNumber      The mobile number for the user.
   * @param phoneNumber       The phone number for the user.
   * @param email             The email address for the user.
   * @param status            The status for the user.*
   * @param password          The password or password hash for the user.
   * @param passwordAttempts  The number of failed authentication attempts as a result of an
   *                          incorrect password for the user.
   * @param passwordExpiry    The date and time the password for the user expires.
   */
  constructor(userDirectoryId: string, username: string, name: string, preferredName: string,
              mobileNumber: string, phoneNumber: string, email: string, status: UserStatus,
              password: string, passwordAttempts?: number, passwordExpiry?: Date) {
    this.userDirectoryId = userDirectoryId;
    this.username = username;
    this.name = name;
    this.preferredName = preferredName;
    this.mobileNumber = mobileNumber;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.status = status;
    this.password = password;
    this.passwordAttempts = !!passwordAttempts ? passwordAttempts : null;
    this.passwordExpiry = !!passwordExpiry ? passwordExpiry : null;
  }
}
