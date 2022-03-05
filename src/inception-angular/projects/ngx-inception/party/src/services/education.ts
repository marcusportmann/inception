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
 * The Education class holds the information for an education obtained by a person.
 *
 * @author Marcus Portmann
 */
export class Education {

  /**
   * The code for the field of study.
   */
  fieldOfStudy?: string;

  /**
   * The first year attended.
   */
  firstYearAttended?: number;

  /**
   * The ID for the education.
   */
  id: string;

  /**
   * The ISO 3166-1 alpha-2 code for the country the educational institution is located in.
   */
  institutionCountry?: string;

  /**
   * The name of the educational institution.
   */
  institutionName: string;

  /**
   * The last year attended.
   */
  lastYearAttended?: number;

  /**
   * The name of the qualification.
   */
  qualificationName?: string;

  /**
   * The code for the qualification type.
   */
  qualificationType: string;

  /**
   * The year the qualification was obtained.
   */
  qualificationYear: number;

  /**
   * Constructs a new Education.
   *
   * @param id                 The ID for the education.
   * @param institutionName    The name of the educational institution.
   * @param qualificationType  The code for the qualification type.
   * @param qualificationYear  The year the qualification was obtained.
   * @param qualificationName  The name of the qualification.
   * @param fieldOfStudy       The code for the field of study.
   * @param firstYearAttended  The first year attended.
   * @param lastYearAttended   The last year attended.
   * @param institutionCountry The ISO 3166-1 alpha-2 code for the country the educational
   *                           institution is located in.
   */
  constructor(id: string, institutionName: string, qualificationType: string,
              qualificationYear: number, qualificationName?: string, fieldOfStudy?: string,
              firstYearAttended?: number, lastYearAttended?: number, institutionCountry?: string) {
    this.id = id;
    this.institutionName = institutionName;
    this.qualificationType = qualificationType;
    this.qualificationYear = qualificationYear;
    this.qualificationName = qualificationName;
    this.fieldOfStudy = fieldOfStudy;
    this.firstYearAttended = firstYearAttended;
    this.lastYearAttended = lastYearAttended;
    this.institutionCountry = institutionCountry;
  }
}
