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
 * The SourceOfFunds class holds the information for a source of funds for a person.
 *
 * @author Marcus Portmann
 */
export class SourceOfFunds {

  /**
   * The description for the source of funds.
   */
  description?: string;

  /**
   * The ISO 8601 format date the source of funds is effective from.
   */
  effectiveFrom?: string;

  /**
   * The ISO 8601 format date the source of funds is effective to.
   */
  effectiveTo?: string;

  /**
   * The percentage of the total of all sources of funds attributed to the source of funds.
   */
  percentage?: number;

  /**
   * The code for the source of funds type.
   */
  type: string;

  /**
   * Constructs a new SourceOfFunds.
   *
   * @param type          The code for the source of funds type.
   * @param percentage    The percentage of the total of all sources of funds attributed to the
   *                      source of funds.
   * @param description   The description for the source of funds.
   * @param effectiveFrom The ISO 8601 format date the source of funds is effective from.
   * @param effectiveTo   The ISO 8601 format date the source of funds is effective to.
   */
  constructor(type: string, percentage?: number, description?: string, effectiveFrom?: string,
              effectiveTo?: string) {
    this.type = type;
    this.percentage = percentage;
    this.description = description;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }
}
