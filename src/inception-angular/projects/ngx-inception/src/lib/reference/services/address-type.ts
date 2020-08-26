/*
 * Copyright 2020 Marcus Portmann
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
 * The AddressType class holds the information for an address type.
 *
 * @author Marcus Portmann
 */
export class AddressType {

  /**
   * The code for the address type.
   */
  code: string;

  /**
   * The description for the address type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the address type.
   */
  localeId: string;

  /**
   * The name of the address type.
   */
  name: string;

  /**
   * The sort index for the address type.
   */
  sortIndex: number;

  /**
   * Constructs a new AddressType.
   *
   * @param code        The code for the address type.
   * @param localeId    The Unicode locale identifier for the address type.
   * @param sortIndex   The sort index for the address type.
   * @param name        The name of the address type.
   * @param description The description for the address type.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
  }
}
