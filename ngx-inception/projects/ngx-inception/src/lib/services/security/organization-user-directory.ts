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

/**
 * The OrganizationUserDirectory class holds the information for an organization user directory.
 *
 * @author Marcus Portmann
 */
export class OrganizationUserDirectory {

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  organizationId: string;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the organization.
   */
  userDirectoryId: string;

  /**
   * Constructs a new OrganizationUserDirectory.
   *
   * @param organizationId  The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        organization.
   * @param userDirectoryId The Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory.
   */
  constructor(organizationId: string, userDirectoryId: string) {
    this.organizationId = organizationId;
    this.userDirectoryId = userDirectoryId;
  }
}
