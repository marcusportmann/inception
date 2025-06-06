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

/**
 * The CodeCategory class holds the information for a code category.
 *
 * @author Marcus Portmann
 */
export class CodeCategory {

  /**
   * The code data for the code category.
   */
  data: string | null = null;

  /**
   * The ID for the code category.
   */
  id: string;

  /**
   * The date and time the code category was last modified.
   */
  lastModified: Date | null = null;

  /**
   * The name of the code category.
   */
  name: string;

  /**
   * Constructs a new CodeCategory.
   *
   * @param id           The ID for the code category.
   * @param name         The name of the code category.
   * @param data         The code data for the code category.
   * @param lastModified The date and time the code category was last modified.
   */
  constructor(id: string, name: string, data?: string, lastModified?: Date) {
    this.id = id;
    this.name = name;
    this.data = !!data ? data : null;
    this.lastModified = !!lastModified ? lastModified : null;
  }
}
