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
 * The Consent class holds the information for a consent provided by an organization or
 * person.
 *
 * @author Marcus Portmann
 */
export class Consent {

  /**
   * The ISO 8601 format date the consent is effective from.
   */
  effectiveFrom?: string;

  /**
   * The ISO 8601 format date the consent is effective to.
   */
  effectiveTo?: string;

  /**
   * The code for the consent type.
   */
  type: string;

  /**
   * Constructs a new Consent.
   *
   * @param type          The code for the consent type.
   * @param effectiveFrom The ISO 8601 format date the consent is effective from.
   * @param effectiveTo   The ISO 8601 format date the consent is effective to.
   */
  constructor(type: string, effectiveFrom?: string, effectiveTo?: string) {
    this.type = type;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }
}






