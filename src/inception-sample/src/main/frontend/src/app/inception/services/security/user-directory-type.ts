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
 * The UserDirectoryType class holds the information for a user directory type.
 *
 * @author Marcus Portmann
 */
export class UserDirectoryType {

  /**
   * The ID used to uniquely identify the user directory type.
   */
  id: string;

  /**
   * The name of the user directory type.
   */
  name: string;

  /**
   * The fully qualified name of the Java class that implements the user directory type.
   */
  userDirectoryClassName: string;

  /**
   * Constructs a new UserDirectoryParameter.
   *
   * @param id                     The ID used to uniquely identify the user directory type.
   * @param name                   The name of the user directory type.
   * @param userDirectoryClassName The fully qualified name of the Java class that implements the user directory type.
   */
  constructor(id: string, name: string, userDirectoryClassName: string) {
    this.id = id;
    this.name = name;
    this.userDirectoryClassName = userDirectoryClassName;
  }
}
