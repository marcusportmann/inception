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
 * The Education class holds the information for an employment for a person.
 *
 * @author Marcus Portmann
 */
export class Employment {

  /**
   * The employer address city.
   */
  employerAddressCity?: string;

  /**
   * The employer address country.
   */
  employerAddressCountry?: string;

  /**
   * The employer address line 1.
   */
  employerAddressLine1?: string;

  /**
   * The employer address line 2.
   */
  employerAddressLine2?: string;

  /**
   * The employer address line 3.
   */
  employerAddressLine3?: string;

  /**
   * The employer address line 4.
   */
  employerAddressLine4?: string;

  /**
   * The employer address postal code.
   */
  employerAddressPostalCode?: string;

  /**
   * The employer address region.
   */
  employerAddressRegion?: string;

  /**
   * The employer address suburb.
   */
  employerAddressSuburb?: string;

  /**
   * The employer contact person.
   */
  employerContactPerson?: string;

  /**
   * The employer e-mail address.
   */
  employerEmailAddress?: string;

  /**
   * The employer name.
   */
  employerName: string;

  /**
   * The employer phone number.
   */
  employerPhoneNumber?: string;

  /**
   * The end date for the employment.
   */
  endDate?: Date;

  /**
   * The ID for the employment.
   */
  id: string;

  /**
   * The code for the occupation for the employment.
   */
  occupation?: string;

  /**
   * The start date for the employment.
   */
  startDate?: Date;

  /**
   * The code for the employment type for the employment.
   */
  type?: string;

  /**
   * Constructs a new Employment.
   *
   * @param id                        The ID for the employment.
   * @param employerName              The employer name.
   * @param employerPhoneNumber       The employer phone number.
   * @param employerEmailAddress      The employer e-mail address.
   * @param employerContactPerson     The employer contact person.
   * @param employerAddressLine1      The employer address line 1.
   * @param employerAddressLine2      The employer address line 2.
   * @param employerAddressLine3      The employer address line 3.
   * @param employerAddressLine4      The employer address line 4.
   * @param employerAddressSuburb     The employer address suburb.
   * @param employerAddressCity       The employer address city.
   * @param employerAddressRegion     The employer address region.
   * @param employerAddressCountry    The employer address country.
   * @param employerAddressPostalCode The employer address postal code.
   * @param startDate                 The start date for the employment.
   * @param endDate                   The end date for the employment.
   * @param type                      The code for the employment type for the employment.
   * @param occupation                The code for the occupation for the employment.
   */
  constructor(id: string, employerName: string, employerPhoneNumber?: string,
              employerEmailAddress?: string, employerContactPerson?: string,
              employerAddressLine1?: string, employerAddressLine2?: string,
              employerAddressLine3?: string, employerAddressLine4?: string,
              employerAddressSuburb?: string, employerAddressCity?: string,
              employerAddressRegion?: string, employerAddressCountry?: string,
              employerAddressPostalCode?: string, startDate?: Date, endDate?: Date, type?: string,
              occupation?: string) {
    this.id = id;
    this.employerName = employerName;
    this.employerPhoneNumber = employerPhoneNumber;
    this.employerEmailAddress = employerEmailAddress;
    this.employerContactPerson = employerContactPerson;
    this.employerAddressLine1 = employerAddressLine1;
    this.employerAddressLine2 = employerAddressLine2;
    this.employerAddressLine3 = employerAddressLine3;
    this.employerAddressLine4 = employerAddressLine4;
    this.employerAddressSuburb = employerAddressSuburb;
    this.employerAddressCity = employerAddressCity;
    this.employerAddressRegion = employerAddressRegion;
    this.employerAddressCountry = employerAddressCountry;
    this.employerAddressPostalCode = employerAddressPostalCode;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
