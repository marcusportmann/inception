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
 * The SourceOfWealth class holds the information for a source of wealth for a person.
 *
 * @author Marcus Portmann
 */
export class SourceOfWealth {

  /**
   * The description for the source of wealth.
   */
  description?: string;

  /**
   * The date the source of wealth is effective from.
   */
  effectiveFrom?: Date;

  /**
   * The date the source of wealth is effective to.
   */
  effectiveTo?: Date;

  /**
   * The code for the source of wealth type.
   */
  type: string;

  /**
   * Constructs a new SourceOfWealth.
   *
   * @param type          The code for the source of wealth type.
   * @param description   The description for the source of wealth.
   * @param effectiveFrom The date the source of wealth is effective from.
   * @param effectiveTo   The date the source of wealth is effective to.
   */
  constructor(type: string, description?: string, effectiveFrom?: Date, effectiveTo?: Date) {
    this.type = type;
    this.description = description;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }
}
