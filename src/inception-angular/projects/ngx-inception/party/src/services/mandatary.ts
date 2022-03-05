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
 * The Mandatary class holds the information for a mandatary for a mandate.
 *
 * @author Marcus Portmann
 */
export class Mandatary {

  /**
   * The ID for the party who is the recipient of the mandate.
   */
  partyId: string;

  /**
   * The code for the mandatary role.
   */
  role: string;

  /**
   * Constructs a new Mandatary.
   *
   * @param partyId The ID for the party who is the recipient of the mandate.
   * @param role    The code for the mandatary role.
   */
  constructor(partyId: string, role: string) {
    this.partyId = partyId;
    this.role = role;
  }
}






