/*
 * Copyright 2019 Marcus Portmann
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

import {OrganizationStatus} from './organization-status';

/**
 * The Organization class holds the information for an organization.
 *
 * @author Marcus Portmann
 */
export class Organization {

  /**
   * The ID used to uniquely identify the organization.
   */
  id: number;

  /**
   * The name of the organization.
   */
  name: string;

  /**
   * The status for the organization.
   */
  status: OrganizationStatus;

  /**
   * Constructs a new Organization.
   *
   * @param id     The ID used to uniquely identify the organization.
   * @param name   The name of the organization.
   * @param status The status for the organization.
   */
  constructor(id: number, name: string, status: OrganizationStatus) {
    this.id = id;
    this.name = name;
    this.status = status;
  }
}
