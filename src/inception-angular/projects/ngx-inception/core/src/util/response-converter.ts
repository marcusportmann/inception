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

import {map} from 'rxjs/operators';
import {ISO8601Util} from './iso-8601-util';

function convertStringValuesToTypes(value: any): any {
  if (!value) return value;

  if (typeof value === 'string') {
    const trimmedValue = value.trim();

    // Check if it's a valid ISO date string (example for length 23 or 29 or similar future formats)
    const iso8601Regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d+)?(?:Z|[\+\-]\d{2}:\d{2})?$/;
    if (iso8601Regex.test(trimmedValue)) {
      const parsedDate: Date = ISO8601Util.toDate(trimmedValue);
      return isNaN(parsedDate.getTime()) ? value : parsedDate;
    }
  }

  if (Array.isArray(value)) {
    return value.map(convertStringValuesToTypes);
  }

  if (typeof value === 'object') {
    for (const [key, val] of Object.entries(value)) {
      value[key] = convertStringValuesToTypes(val);
    }
  }

  return value;
}

export function ResponseConverter(target: any, propertyKey: string,
                                  descriptor: PropertyDescriptor): PropertyDescriptor {
  const originalMethod = descriptor.value;

  descriptor.value = function (...args: any) {
    return originalMethod.call(this, ...args).pipe(map((result: any) => {

      //let originalResult = Object.assign({}, result);
      //console.log('originalResult = ', originalResult);

      let convertedResult = convertStringValuesToTypes(result)

      // console.log('convertedResult = ', convertedResult);

      return convertedResult;
    }));
  }

  return descriptor;
}
