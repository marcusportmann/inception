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
import {ContactMechanism} from './contact-mechanism';
import {ExternalReference} from './external-reference';
import {IdentityDocument} from './identity-document';
import {Lock} from './lock';
import {PhysicalAddress} from './physical-address';
import {Preference} from './preference';
import {Role} from './role';
import {SegmentAllocation} from './segment-allocation';
import {Status} from './status';
import {TaxNumber} from './tax-number';

/**
 * The Organization class holds the information for an organization, which is an organised group of
 * people with a particular purpose, such as a business or government department.
 *
 * @author Marcus Portmann
 */
export class Organization {

  /**
   * The attributes for the organization.
   */
  attributes: Attribute[] = [];

  /**
   * The contact mechanisms for the organization.
   */
  contactMechanisms: ContactMechanism[] = [];

  /**
   * The ISO 3166-1 alpha-2 codes for the countries of tax residence for the organization.
   */
  countriesOfTaxResidence: string[] = [];

  /**
   * The external references for the organization.
   */
  externalReferences: ExternalReference[] = [];

  /**
   * The ID for the organization.
   */
  id: string;

  /**
   * The identity documents for the organization.
   */
  identityDocuments: IdentityDocument[] = [];

  /**
   * The locks applied to the organization.
   */
  locks: Lock[] = [];

  /**
   * The name of the organization, e.g. the legal name of an organization, etc.
   */
  name: string;

  /**
   * The physical addresses for the organization.
   */
  physicalAddresses: PhysicalAddress[] = [];

  /**
   * The preferences for the organization.
   */
  preferences: Preference[] = [];

  /**
   * The roles assigned directly to the organization.
   */
  roles: Role[] = [];

  /**
   * The segment allocations for the organization.
   */
  segmentAllocations: SegmentAllocation[] = [];

  /**
   * The statuses assigned to the organization.
   */
  statuses: Status[] = [];

  /**
   * The tax numbers for the organization.
   */
  taxNumbers: TaxNumber[] = [];

  /**
   * The ID for the tenant the organization is associated with.
   */
  tenantId: string;

  /**
   * Constructs a new Organization.
   *
   * @param id       The ID for the organization.
   * @param tenantId The ID for the tenant the organization is associated with.
   * @param name     The name of the organization.
   */
  constructor(id: string, tenantId: string, name: string) {
    this.id = id;
    this.tenantId = tenantId;
    this.name = name;
  }
}
