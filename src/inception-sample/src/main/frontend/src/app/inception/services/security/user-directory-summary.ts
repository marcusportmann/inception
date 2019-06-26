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
 * The UserDirectorySummary class holds the summary information for a user directory.
 *
 * @author Marcus Portmann
 */
export class UserDirectorySummary {

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  id: string;

  /**
   * The name of the user directory.
   */
  name: string;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory type.
   */
  typeId: string;


  /**
   * Constructs a new UserDirectorySummary.
   *
   * @param id     The Universally Unique Identifier (UUID) used to uniquely identify the user
   *               directory.
   * @param name   The name of the user directory.
   * @param typeId The Universally Unique Identifier (UUID) used to uniquely identify the user
   *               directory type.
   */
  constructor(id: string, name: string, typeId: string) {
    this.id = id;
    this.name = name;
    this.typeId = typeId;
  }
}
