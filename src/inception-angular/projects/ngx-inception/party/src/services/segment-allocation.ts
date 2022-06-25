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
 * The SegmentAllocation class holds the information for the allocation of an organization or person
 * to a segment.
 *
 * @author Marcus Portmann
 */
export class SegmentAllocation {

  /**
   * The ISO 8601 format date the segment allocation is effective from.
   */
  effectiveFrom?: string;

  /**
   * The ISO 8601 format date the segment allocation is effective to.
   */
  effectiveTo?: string;

  /**
   * The code for the segment.
   */
  segment: string;

  /**
   * Constructs a new SegmentAllocation.
   *
   * @param segment       The code for the segment.
   * @param effectiveFrom The ISO 8601 format date the segment allocation is effective from.
   * @param effectiveTo   The ISO 8601 format date the segment allocation is effective to.
   */
  constructor(segment: string, effectiveFrom?: string, effectiveTo?: string) {
    this.segment = segment;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }
}






