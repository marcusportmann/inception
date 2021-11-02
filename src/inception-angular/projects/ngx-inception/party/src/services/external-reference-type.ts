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
 * The ExternalReferenceType class holds the information for an external reference type.
 *
 * @author Marcus Portmann
 */
export class ExternalReferenceType {

  /**
   * The code for the external reference type.
   */
  code: string;

  /**
   * The description for the external reference type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the external reference type.
   */
  localeId: string;

  /**
   * The name of the external reference type.
   */
  name: string;

  /**
   * The codes for the party types the external reference type is associated with..
   */
  partyTypes: string[];

  /**
   * The sort index for the external reference type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the external reference type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new ExternalReferenceType.
   *
   * @param code        The code for the external reference type.
   * @param localeId    The Unicode locale identifier for the external reference type.
   * @param sortIndex   The sort index for the external reference type.
   * @param name        The name of the external reference type.
   * @param description The description for the external reference type.
   * @param partyTypes  The codes for the party types the external reference type is associated with.
   * @param tenantId    The ID for the tenant the external reference type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              partyTypes: string[], tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.partyTypes = partyTypes;
    this.tenantId = tenantId;
  }
}
