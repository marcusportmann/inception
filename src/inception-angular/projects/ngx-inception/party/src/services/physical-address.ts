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
 * The PhysicalAddress class holds the information for a physical address for an organization or
 * person.
 *
 * @author Marcus Portmann
 */
export class PhysicalAddress {

  /**
   * The building floor for the physical address.
   */
  buildingFloor?: string;

  /**
   * The building name for the physical address that is required for a building address.
   */
  buildingName?: string;

  /**
   * The building room for the physical address.
   */
  buildingRoom?: string;

  /**
   * The town or city for the physical address that is required for a building, complex, postal,
   * site or street address.
   */
  city?: string;

  /**
   * The complex name for the physical address that is required for a complex address.
   */
  complexName?: string;

  /**
   * The complex unit number for the physical address that is required for a complex address.
   */
  complexUnitNumber?: string;

  /**
   * The ISO 3166-1 alpha-2 code for the country for the physical address.
   */
  country?: string;

  /**
   * The farm description for the physical address.
   */
  farmDescription?: string;

  /**
   * The farm name for the physical address.
   */
  farmName?: string;

  /**
   * The farm number for the physical address that is required for a farm address.
   */
  farmNumber?: string;

  /**
   * The ID for the physical address.
   */
  id: string;

  /**
   * The GPS latitude for the physical address.
   */
  latitude?: string;

  /**
   * The address line 1 for the physical address that is required for an international, postal or
   * unstructured address.
   */
  line1?: string;

  /**
   * The address line 2 for the physical address.
   */
  line2?: string;

  /**
   * The address line 3 for the physical address.
   */
  line3?: string;

  /**
   * The address line 4 for the physical address.
   */
  line4?: string;

  /**
   * The GPS longitude for the physical address.
   */
  longitude?: string;

  /**
   * The postal code for the physical address.
   */
  postalCode?: string;

  /**
   * The codes for the physical address purposes.
   */
  purposes: string[];

  /**
   * The ISO 3166-2 subdivision code for the region for the physical address.
   */
  region?: string;

  /**
   * The code for the physical address role.
   */
  role: string;

  /**
   * The site block for the physical address that is required for a site address.
   */
  siteBlock?: string;

  /**
   * The site number for the physical address that is required for a site address.
   */
  siteNumber?: string;

  /**
   * The street name for the physical address that is required for a street address.
   */
  streetName?: string;

  /**
   * The street number for the physical address that is required for a street address.
   */
  streetNumber?: string;

  /**
   * The suburb for the physical address.
   */
  suburb?: string;

  /**
   * The code for the physical address type.
   */
  type: string;

  /**
   * Constructs a new PhysicalAddress.
   *
   * @param id       The ID for the physical address.
   * @param type     The code for the physical address type.
   * @param role     The code for the physical address role.
   * @param purposes The codes for the physical address purposes.
   */
  constructor(id: string, type: string, role: string, purposes?: string[]) {
    this.id = id;
    this.type = type;
    this.role = role;

    if (purposes == null) {
      this.purposes = [];
    } else {
      this.purposes = purposes;
    }
  }
}
