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
 * The IdentityDocument class holds the information for a legal document which may be used to
 * verify aspects of a party's identity.
 *
 * @author Marcus Portmann
 */
export class IdentityDocument {

  /**
   * The ISO 3166-1 alpha-2 code for the country of issue for the identity document.
   */
  countryOfIssue: string;

  /**
   * The ISO 8601 format date of expiry for the identity document.
   */
  dateOfExpiry?: string;

  /**
   * The ISO 8601 format date of issue for the identity document.
   */
  dateOfIssue: string;

  /**
   * The ISO 8601 format date the identity document was provided.
   */
  dateProvided?: string;

  /**
   * The ID for the identity document.
   */
  id: string;

  /**
   * The number for the identity document.
   */
  number: string;

  /**
   * The code for the identity document type.
   */
  type: string;

  /**
   * Constructs a new IdentityDocument.
   *
   * @param id             The ID for the identity document.
   * @param type           The code for the identity document type.
   * @param countryOfIssue The ISO 3166-1 alpha-2 code for the country of issue for the identity
   *                       document.
   * @param dateOfIssue    The ISO 8601 format date of issue for the identity document.
   * @param number         The number for the identity document.
   * @param dateOfExpiry   The ISO 8601 format date of expiry for the identity document.
   */
  constructor(id: string, type: string, countryOfIssue: string, dateOfIssue: string, number: string,
              dateOfExpiry?: string) {
    this.id = id;
    this.type = type;
    this.countryOfIssue = countryOfIssue;
    this.dateOfIssue = dateOfIssue;
    this.number = number;
    this.dateOfExpiry = dateOfExpiry;
  }
}
