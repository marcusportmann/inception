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

import {PartyType} from "./party-type";

/**
 * The Party class holds the information for a party.
 *
 * @author Marcus Portmann
 */
export class Party {

  /**
   * The ID for the party.
   */
  id: string;

  /**
   * The name of the party, e.g. the full legal name of a person, the legal name of an organization,
   * the name of a family, etc.
   */
  name: string;

  /**
   * The ID for the tenant the party is associated with.
   */
  tenantId: string;

  /**
   * The party type.
   */
  type: PartyType;

  /**
   * Constructs a new Party.
   *
   * @param id       The ID for the party.
   * @param tenantId The ID for the tenant the party is associated with.
   * @param type     The party type.
   * @param name     The name of the party, e.g. the full legal name of a person, the legal name of
   *                 an organization, the name of a family, etc.
   */
  constructor(id: string, tenantId: string, type: PartyType, name: string) {
    this.id = id;
    this.tenantId = tenantId;
    this.type = type;
    this.name = name;
  }
}
