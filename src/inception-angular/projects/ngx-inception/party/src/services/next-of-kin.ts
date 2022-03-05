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
 * The NextOfKin class holds the information for a next of kin for a person.
 *
 * @author Marcus Portmann
 */
export class NextOfKin {

  /**
   * The next of kin address city.
   */
  addressCity?: string;

  /**
   * The next of kin address country.
   */
  addressCountry?: string;

  /**
   * The next of kin address line 1.
   */
  addressLine1?: string;

  /**
   * The next of kin address line 2.
   */
  addressLine2?: string;

  /**
   * The next of kin address line 3.
   */
  addressLine3?: string;

  /**
   * The next of kin address line 4.
   */
  addressLine4?: string;

  /**
   * The next of kin address postal code.
   */
  addressPostalCode?: string;

  /**
   * The next of kin address region.
   */
  addressRegion?: string;

  /**
   * The next of kin address suburb.
   */
  addressSuburb?: string;

  /**
   * The e-mail address for the next of kin.
   */
  emailAddress?: string;

  /**
   * The given name, firstname, forename, or Christian name for the next of kin.
   */
  givenName?: string;

  /**
   * The home phone number for the next of kin.
   */
  homeNumber?: string;

  /**
   * The ID for the next of kin.
   */
  id: string;

  /**
   * The mobile number for the next of kin.
   */
  mobileNumber?: string;

  /**
   * The name of the next of kin.
   */
  name: string;

  /**
   * The surname, last name, or family name for the next of kin.
   */
  surname?: string;

  /**
   * The code for the next of kin type for the next of kin.
   */
  type: string;

  /**
   * The work phone number for the next of kin.
   */
  workNumber?: string;

  /**
   * Constructs a new NextOfKin.
   *
   * @param id                The ID for the next of kin.
   * @param type              The code for the next of kin type for the next of kin.
   * @param name              The name of the next of kin.
   * @param givenName         The given name, firstname, forename, or Christian name for the next of
   *                          kin.
   * @param surname           The surname, last name, or family name for the next of kin.
   * @param homeNumber        The home phone number for the next of kin.
   * @param workNumber        The work phone number for the next of kin.
   * @param mobileNumber      The mobile number for the next of kin.
   * @param emailAddress      The e-mail address for the next of kin.
   * @param addressLine1      The next of kin address line 1.
   * @param addressLine2      The next of kin address line 2.
   * @param addressLine3      The next of kin address line 3.
   * @param addressLine4      The next of kin address line 4.
   * @param addressSuburb     The next of kin address suburb.
   * @param addressCity       The next of kin address city.
   * @param addressRegion     The next of kin address region.
   * @param addressCountry    The next of kin address country.
   * @param addressPostalCode The next of kin address postal code.
   */
  constructor(id: string, type: string, name: string, givenName?: string, surname?: string,
              homeNumber?: string, workNumber?: string, mobileNumber?: string,
              emailAddress?: string, addressLine1?: string, addressLine2?: string,
              addressLine3?: string, addressLine4?: string, addressSuburb?: string,
              addressCity?: string, addressRegion?: string, addressCountry?: string,
              addressPostalCode?: string) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.givenName = givenName;
    this.surname = surname;
    this.homeNumber = homeNumber;
    this.workNumber = workNumber;
    this.mobileNumber = mobileNumber;
    this.emailAddress = emailAddress;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.addressLine3 = addressLine3;
    this.addressLine4 = addressLine4;
    this.addressSuburb = addressSuburb
    this.addressCity = addressCity;
    this.addressRegion = addressRegion;
    this.addressCountry = addressCountry;
    this.addressPostalCode = addressPostalCode;
  }
}
