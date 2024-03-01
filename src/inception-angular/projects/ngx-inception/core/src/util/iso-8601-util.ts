/*
 * Copyright Marcus Portmann
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

import * as moment from 'moment';

/**
 * The ISO8601Util class.
 *
 * @author Marcus Portmann
 */
export class ISO8601Util {

  /**
   * Transform an ISO 8601 format string into a <b>Date</b> instance.
   *
   * @param iso8601string the ISO 8601 format string to transform
   * @return the <b>Date</b> instance for the ISO 8601 format string
   */
  static toDate(iso8601string: string): Date {
    let m = moment.parseZone(iso8601string, moment.ISO_8601, true);

    if (m.isValid()) {
      return m.local(true).toDate();
    } else {
      throw new Error(
        'Failed to convert the invalid ISO 8601 format string (' + iso8601string + ')');
    }
  }
}







