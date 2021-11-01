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

import {RequiredMandataries} from "./required-mandataries";

/**
 * The Mandate class holds the information for a mandate, which is an assignment of authority to act
 * on behalf of a party (the mandator), which is given to another party (the mandatary).
 *
 * @author Marcus Portmann
 */
export class Mandate {

  /**
   * The date the mandate is effective from.
   */
  effectiveFrom?: Date;

  /**
   * The date the mandate is effective to.
   */
  effectiveTo?: Date;

  /**
   * The ID for the mandate.
   */
  id: string;

  /**
   * The number of mandataries required to execute the mandate.
   */
  requiredMandataries?: RequiredMandataries;

  /**
   * The ID for the tenant the mandate is associated with.
   */
  tenantId: string;

  /**
   * The code for the mandate type.
   */
  type: string;

  /**
   * Constructs a new Mandate.
   *
   * @param id                  The ID for the mandate.
   * @param tenantId            The ID for the tenant the mandate is associated with.
   * @param type                The code for the mandate type.
   * @param requiredMandataries The number of mandataries required to execute the mandate.
   * @param effectiveFrom       The date the mandate is effective from.
   * @param effectiveTo         The date the mandate is effective to.
   */
  constructor(id: string, tenantId: string, type: string, requiredMandataries?: RequiredMandataries,
              effectiveFrom?: Date, effectiveTo?: Date) {
    this.id = id;
    this.tenantId = tenantId;
    this.type = type;
    this.requiredMandataries = requiredMandataries;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }
}
