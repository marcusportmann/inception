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

import {Attribute} from './attribute';
import {Consent} from "./consent";
import {ContactMechanism} from './contact-mechanism';
import {Education} from "./education";
import {Employment} from "./employment";
import {ExternalReference} from './external-reference';
import {IdentityDocument} from './identity-document';
import {LanguageProficiency} from "./language-proficiency";
import {Lock} from './lock';
import {MeasurementSystem} from "./measurement-system";
import {NextOfKin} from "./next-of-kin";
import {PhysicalAddress} from './physical-address';
import {Preference} from './preference';
import {ResidencePermit} from "./residence-permit";
import {Role} from './role';
import {SegmentAllocation} from './segment-allocation';
import {Skill} from './skill';
import {SourceOfFunds} from "./source-of-funds";
import {SourceOfWealth} from "./source-of-wealth";
import {Status} from './status';
import {TaxNumber} from './tax-number';

/**
 * The Person class holds the information for a person.
 *
 * @author Marcus Portmann
 */
export class Person {

  /**
   * The attributes for the person.
   */
  attributes: Attribute[] = [];

  /**
   * The consents provided by the person.
   */
  consents: Consent[] = [];

  /**
   * The contact mechanisms for the person.
   */
  contactMechanisms: ContactMechanism[] = [];

  /**
   * The ISO 3166-1 alpha-2 codes for the countries of citizenship for the person.
   */
  countriesOfCitizenship: string[] = [];

  /**
   * The ISO 3166-1 alpha-2 codes for the countries of tax residence for the person.
   */
  countriesOfTaxResidence: string[] = [];

  /**
   * The ISO 3166-1 alpha-2 code for the country of birth for the person.
   */
  countryOfBirth?: string;

  /**
   * The ISO 3166-1 alpha-2 code for the country of residence for the person.
   */
  countryOfResidence?: string;

  /**
   * The date of birth for the person.
   */
  dateOfBirth?: Date;

  /**
   * The date of death for the person.
   */
  dateOfDeath?: Date;

  /**
   * The educations for the person.
   */
  educations: Education[] = [];

  /**
   * The code for the employment status for the person.
   */
  employmentStatus?: string;

  /**
   * The code for the employment type for the person.
   */
  employmentType?: string;

  /**
   * The employments for the person.
   */
  employments: Employment[] = [];

  /**
   * The external references for the person.
   */
  externalReferences: ExternalReference[] = [];

  /**
   * The code for the gender for the person.
   */
  gender?: string;

  /**
   * The given name, firstname, forename, or Christian name for the person.
   */
  givenName?: string;

  /**
   * The code for the highest qualification type for the person.
   */
  highestQualificationType?: string;

  /**
   * The ID for the person.
   */
  id: string;

  /**
   * The identity documents for the person.
   */
  identityDocuments: IdentityDocument[] = [];

  /**
   * The initials for the person.
   */
  initials?: string;

  /**
   * The ISO 639-1 alpha-2 code for the language for the person.
   */
  language?: string;

  /**
   * The language proficiencies for the person.
   */
  languageProficiencies: LanguageProficiency[] = [];

  /**
   * The locks applied to the person.
   */
  locks: Lock[] = [];

  /**
   * The maiden name for the person.
   */
  maidenName?: string;

  /**
   * The code for the marital status for the person.
   */
  maritalStatus?: string;

  /**
   * The date for the marital status for the person.
   */
  maritalStatusDate?: Date;

  /**
   * The code for the marriage type for the person if the person is married.
   */
  marriageType?: string;

  /**
   * The measurement system for the person.
   */
  measurementSystem?: MeasurementSystem;

  /**
   * The middle names for the person.
   */
  middleNames?: string;

  /**
   * The personal name or full name of the person.
   */
  name: string;

  /**
   * The next of kin for the person.
   */
  nextOfKin: NextOfKin[] = [];

  /**
   * The code for the occupation for the person.
   */
  occupation?: string;

  /**
   * The physical addresses for the person.
   */
  physicalAddresses: PhysicalAddress[] = [];

  /**
   * The preferences for the person.
   */
  preferences: Preference[] = [];

  /**
   * The preferred name for the person.
   */
  preferredName?: string;

  /**
   * The code for the race for the person.
   */
  race?: string;

  /**
   * The residence permits for the person.
   */
  residencePermits: ResidencePermit[] = [];

  /**
   * The code for the residency status for the person.
   */
  residencyStatus?: string;

  /**
   * The code for the residential type for the person.
   */
  residentialType?: string;

  /**
   * The roles assigned directly to the person.
   */
  roles: Role[] = [];

  /**
   * The segment allocations for the person.
   */
  segmentAllocations: SegmentAllocation[] = [];

  /**
   * The skills for the person.
   */
  skills: Skill[] = [];

  /**
   * The sources of funds for the person.
   */
  sourcesOfFunds: SourceOfFunds[] = [];

  /**
   * The sources of wealth for the person.
   */
  sourcesOfWealth: SourceOfWealth[] = [];

  /**
   *  The statuses assigned to the person.
   */
  statuses: Status[] = [];

  /**
   * The surname, last name, or family name for the person.
   */
  surname?: string;

  /**
   * The tax numbers for the person.
   */
  taxNumbers: TaxNumber[] = [];

  /**
   * The ID for the tenant the person is associated with.
   */
  tenantId: string;

  /**
   * The time zone ID for the person.
   */
  timeZone?: string;

  /**
   * The code for the title for the person.
   */
  title?: string;

  /**
   * Constructs a new Person.
   *
   * @param id       The ID for the person.
   * @param tenantId The ID for the tenant the person is associated with.
   * @param name     The personal name or full name of the person.
   */
  constructor(id: string, tenantId: string, name: string) {
    this.id = id;
    this.tenantId = tenantId;
    this.name = name;
  }
}
