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
import {map} from 'rxjs/operators';

function convertStringValuesToTypes(value: any): any {
  if (value) {
    if ((typeof (value) === 'string') && ((value.length == 23) || (value.length == 29))) {
      let m = moment.parseZone(value, moment.ISO_8601, true);

      if (m.isValid()) {
        return m.local(true).toDate();
      } else {
        return value;
      }
    } else if (Array.isArray(value)) {
      for (let i = 0; i < value.length; i++) {
        value[i] = convertStringValuesToTypes(value[i]);
      }
    } else if (typeof (value) === 'object') {
      Object.entries(value).forEach(
        ([entryKey, entryValue]) => {
          if (entryValue) {
            // @ts-ignore
            value[entryKey] = convertStringValuesToTypes(entryValue);
          }
        });
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


//
//
// Object.entries(object).forEach(
//   ([key, value]) => {
//     if (value) {
//       if (Array.isArray(value)) {
//         value.forEach((item: any) => {
//           if (typeof (item) === 'string') {
//             let m = moment.parseZone(value, moment.ISO_8601, true);
//
//             if (m.isValid()) {
//               // @ts-ignore
//               object[key] = m.toDate();
//             }
//           } else if (typeof (value) === 'object') {
//             convertObject(value);
//           }
//         });
//       } else if (typeof (value) === 'string') {
//         let m = moment.parseZone(value, moment.ISO_8601, true);
//
//         if (m.isValid()) {
//           // @ts-ignore
//           object[key] = m.toDate();
//         }
//       } else if (typeof (value) === 'object') {
//         convertObject(value);
//       }
//     }
//   }
// );
//
// return object;
