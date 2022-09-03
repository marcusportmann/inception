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
 * The Segment class holds the information for a segment.
 *
 * @author Marcus Portmann
 */
export class Segment {

  /**
   * The code for the segment.
   */
  code: string;

  /**
   * The description for the segment.
   */
  description: string;

  /**
   * The Unicode locale identifier for the segment.
   */
  localeId: string;

  /**
   * The name of the segment.
   */
  name: string;

  /**
   * The codes for the party types the segment is associated with.
   */
  partyTypes: string[];

  /**
   * The sort index for the segment.
   */
  sortIndex: number;

  /**
   * The ID for the tenant the segment is specific to.
   */
  tenantId?: string;

  /**
   * The code for the segmentation type for the segment.
   */
  type: string;

  /**
   * Constructs a new Segment.
   *
   * @param code        The code for the segment.
   * @param type        The code for the segmentation type for the segment
   * @param localeId    The Unicode locale identifier for the segment.
   * @param sortIndex   The sort index for the segment.
   * @param name        The name of the segment.
   * @param description The description for the segment.
   * @param partyTypes  The codes for the party types the segment is associated with.
   * @param tenantId    The ID for the tenant the segment is specific to.
   */
  constructor(code: string, type: string, localeId: string, sortIndex: number, name: string,
              description: string, partyTypes: string[], tenantId?: string) {
    this.code = code;
    this.type = type;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.partyTypes = partyTypes;
    this.tenantId = tenantId;
  }
}
