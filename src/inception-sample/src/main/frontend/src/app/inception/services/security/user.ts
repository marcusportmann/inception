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

import {UserStatus} from './user-status';

/**
 * The User class holds the information for a user.
 *
 * @author Marcus Portmann
 */
export class User {

  /**
   * The e-mail address for the user.
   */
  email: string;

  /**
   * The optional external reference for the user.
   */
  externalReference?: string;

  /**
   * The first name for the user.
   */
  firstName: string;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user.
   */
  id: string;

  /**
   * The last name for the user.
   */
  lastName: string;

  /**
   * The mobile number for the user.
   */
  mobileNumber: string;

  /**
   * The password or password hash for the user.
   */
  password: string;

  /**
   * The number of failed authentication attempts as a result of an incorrect password for the user.
   */
  passwordAttempts: number;

  /**
   * The optional date and time the password for the user expires.
   */
  passwordExpiry?: Date;

  /**
   * The phone number for the user.
   */
  phoneNumber: string;

  /**
   * Is the user read-only.
   */
  readOnly?: boolean;

  /**
   * The status for the user.
   */
  status: UserStatus;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory the user
   * is associated with.
   */
  userDirectoryId: string;

  /**
   * The username for the user.
   */
  username: string;

  /**
   * Constructs a new User.
   *
   * @param id                The Universally Unique Identifier (UUID) used to uniquely identify the
   *                          user.
   * @param userDirectoryId   The Universally Unique Identifier (UUID) used to uniquely identify the
   *                          user directory the user is associated with.
   * @param username          The username for the user.
   * @param firstName         The first name for the user.
   * @param lastName          The last name for the user.
   * @param mobileNumber      The mobile number for the user.
   * @param phoneNumber       The phone number for the user.
   * @param email             The e-mail address for the user.
   * @param password          The password or password hash for the user.
   * @param passwordAttempts  The number of failed authentication attempts as a result of an
   *                          incorrect password for the user.
   * @param status            The status for the user.
   * @param readOnly          Is the user read-only.
   * @param passwordExpiry    The optional date and time the password for the user expires.
   * @param externalReference The optional external reference for the user.
   */
  constructor(id: string, userDirectoryId: string, username: string, firstName: string,
              lastName: string, mobileNumber: string, phoneNumber: string, email: string,
              password: string, passwordAttempts: number, status: UserStatus, readOnly?: boolean,
              passwordExpiry?: Date, externalReference?: string) {
    this.id = id;
    this.userDirectoryId = userDirectoryId;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.mobileNumber = mobileNumber;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.password = password;
    this.passwordAttempts = passwordAttempts;
    this.status = status;
    this.readOnly = readOnly;
    this.passwordExpiry = passwordExpiry;
    this.externalReference = externalReference;
  }
}
