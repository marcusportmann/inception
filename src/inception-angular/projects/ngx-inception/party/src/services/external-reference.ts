/*
 * Copyright 2022 Marcus Portmann
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
 * The ExternalReference class holds the information for an external reference for an organization
 * or person.
 *
 * @author Marcus Portmann
 */
export class ExternalReference {

  /**
   * The code for the external reference type.
   */
  type: string;

  /**
   * The value for the external reference.
   */
  value: string;

  /**
   * Constructs a new ExternalReference.
   *
   * @param type  The code for the external reference type.
   * @param value The value for the external reference.
   */
  constructor(type: string, value: string) {
    this.type = type;
    this.value = value;
  }
}
