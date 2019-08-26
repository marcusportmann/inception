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

import {UserDirectory} from './user-directory';
import {UserDirectoryParameter} from './user-directory-parameter';

/**
 * The UserDirectoryUtil class provides utility functions that are useful when working with
 * UserDirectory instances.
 *
 * @author Marcus Portmann
 */
export class UserDirectoryUtil {

  /**
   * Retrieve the parameter with the specified name for the specified user directory.
   *
   * @param userDirectory The user directory.
   * @param name          The name of the parameter.
   *
   * @return The parameter with the specified name for the specified user directory or null if the
   *         parameter cannot be found.
   */
  static getParameter(userDirectory: UserDirectory, name: string): string | null {
    for (const userDirectoryParameter of userDirectory.parameters) {
      if (userDirectoryParameter.name === name) {
        return userDirectoryParameter.value;
      }
    }

    return null;
  }

  /**
   * Check whether a parameter with the specified name exists for the specified user directory.
   *
   * @param userDirectory The user directory.
   * @param name          The name of the parameter.
   *
   * @return True if a parameter with the specified name exists for the specified user directory or
   *         false otherwise.
   */
  static hasParameter(userDirectory: UserDirectory, name: string): boolean {
    for (const userDirectoryParameter of userDirectory.parameters) {
      if (userDirectoryParameter.name === name) {
        return true
      }
    }

    return false;
  }

  /**
   * Set the parameter with the specified name for the specified user directory.
   *
   * @param userDirectory The user directory.
   * @param name          The name of the parameter.
   * @param value         The value for the parameter.
   *
   * @return The parameter with the specified name for the specified user directory or null if the
   *         parameter cannot be found.
   */
  static setParameter(userDirectory: UserDirectory, name: string, value: string): void {
    for (const userDirectoryParameter of userDirectory.parameters) {
      if (userDirectoryParameter.name === name) {
        userDirectoryParameter.value = value;
        return;
      }
    }

    userDirectory.parameters = userDirectory.parameters.concat(
      new UserDirectoryParameter(name, value));
  }
}
