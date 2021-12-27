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
 * The AssociationType class holds the information for an association type.
 *
 * @author Marcus Portmann
 */
export class AssociationType {

  /**
   * The code for the association type.
   */
  code: string;

  /**
   * The description for the association type.
   */
  description: string;

  /**
   * The code for the role type for the first party in the association.
   */
  firstPartyRole: string;

  /**
   * The codes for the party types for the first party in the association.
   */
  firstPartyTypes: string[];

  /**
   * The Unicode locale identifier for the association type.
   */
  localeId: string;

  /**
   * The name of the association type.
   */
  name: string;

  /**
   * The code for the role type for the second party in the association.
   */
  secondPartyRole: string;

  /**
   * The codes for the party types for the second party in the association.
   */
  secondPartyTypes: string[];

  /**
   * The sort index for the association type.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the association type is specific to.
   */
  tenantId?: string;

  /**
   * Constructs a new AssociationType.
   *
   * @param code             The code for the association type.
   * @param localeId         The Unicode locale identifier for the association type.
   * @param sortIndex        The sort index for the association type.
   * @param name             The name of the association type.
   * @param description      The description for the association type.
   * @param firstPartyTypes  The codes for the party types for the first party in the association.
   * @param firstPartyRole   The code for the role type for the first party in the association.
   * @param secondPartyTypes The codes for the party types for the second party in the association.
   * @param secondPartyRole  The code for the role type for the second party in the association.
   * @param tenantId         The ID for the tenant the association type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              firstPartyTypes: string[], firstPartyRole: string, secondPartyTypes: string[],
              secondPartyRole: string, tenantId?: string,) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.firstPartyTypes = firstPartyTypes;
    this.firstPartyRole = firstPartyRole;
    this.secondPartyTypes = secondPartyTypes;
    this.secondPartyRole = secondPartyRole;
    this.tenantId = tenantId;
  }
}
