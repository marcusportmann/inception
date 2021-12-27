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
 * The TaxNumber class holds the information for a tax number associated with an organization or
 * person.
 *
 * Even though the country of issue is specified as part of the reference data for a tax number
 * type, it is included here to eliminate the additional join. The additional join will also be
 * problematic if the reference data is stored in a separate database.
 *
 * @author Marcus Portmann
 */
export class TaxNumber {

  /**
   * The ISO 3166-1 alpha-2 code for the country of issue for the tax number.
   */
  countryOfIssue: string;

  /**
   * The tax number.
   */
  number?: string;

  /**
   * The code for the tax number type.
   */
  type: string;

  /**
   * Constructs a new TaxNumber.
   *
   * @param type           The code for the tax number type.
   * @param countryOfIssue The ISO 3166-1 alpha-2 code for the country of issue for the tax number.
   * @param number         The tax number.
   */
  constructor(type: string, countryOfIssue: string, number?: string) {
    this.type = type;
    this.number = number;
    this.countryOfIssue = countryOfIssue;
  }
}
