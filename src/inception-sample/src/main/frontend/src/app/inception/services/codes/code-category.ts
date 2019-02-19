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
 * The CodeCategory class stores the information for a code category.
 *
 * @author Marcus Portmann
 */
export class CodeCategory {

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category.
   */
  id: string;

  /**
   * The name of the code category.
   */
  name: string;

  /**
   * The date and time the code category was updated.
   */
  updated: Date;

  /**
   * Constructs a new Organization.
   *
   * @param {string} id      The Universally Unique Identifier (UUID) used to uniquely identify the
   *                         code category.
   * @param {string} name    The name of the code category.
   * @param {string} updated The date and time the code category was updated.
   */
  constructor(id: string, name: string, updated: Date) {
    this.id = id;
    this.name = name;
    this.updated = this.updated;
  }
}
