/*
 * Copyright 2021 Marcus Portmann
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
 * The ResidencePermit class holds the information for a residence permit issued to a person.
 *
 * @author Marcus Portmann
 */
export class ResidencePermit {

  /**
   * The ISO 3166-1 alpha-2 code for the country of issue for the residence permit.
   */
  countryOfIssue: string;

  /**
   * The date of expiry for the residence permit.
   */
  dateOfExpiry?: Date;

  /**
   * The date of issue for the residence permit.
   */
  dateOfIssue: Date;

  /**
   * The ID for the residence permit.
   */
  id: string;

  /**
   * The number for the residence permit.
   */
  number: string;

  /**
   * The code for the residence permit type.
   */
  type: string;

  /**
   * Constructs a new ResidencePermit.
   *
   * @param id             The ID for the residence permit.
   * @param type           The code for the residence permit type.
   * @param countryOfIssue The ISO 3166-1 alpha-2 code for the country of issue for the residence
   *                       permit.
   * @param dateOfIssue    The date of issue for the residence permit.
   * @param number         The number for the residence permit.
   * @param dateOfExpiry   The date of expiry for the residence permit.
   */
  constructor(id: string, type: string, countryOfIssue: string, dateOfIssue: Date, number: string,
              dateOfExpiry?: Date) {
    this.id = id;
    this.type = type;
    this.countryOfIssue = countryOfIssue;
    this.dateOfIssue = dateOfIssue;
    this.number = number;
    this.dateOfExpiry = dateOfExpiry;
  }
}
