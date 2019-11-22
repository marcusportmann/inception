/*
 * Copyright 2019 Marcus Portmann
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

import { ValidatorFn, AbstractControl } from '@angular/forms';

/**
 * The FileValidator class.
 *
 * @author Marcus Portmann
 */
export class FileValidator {

  /**
   * Validator function to validate that the total length of one or more files does not exceed the
   * maximum number of bytes allowed.
   *
   * @param bytes The maximum number of bytes allowed.
   *
   * @returns The validator function.
   */
  public static maxSize(bytes: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const size = control && control.value ? (control.value as File[]).map(f => f.size).reduce((acc, i) => acc + i, 0) : 0;
      const condition = bytes >= size;
      return condition
        ? null
        : {
          maxSize: {
            actualSize: size,
            allowedSize: bytes
          }
        };
    };
  }

  /**
   * Validator function to validate that the total length of one or more files is greater than the
   * minimum number of bytes required.
   *
   * @param bytes The minimum number of bytes required.
   *
   * @returns The validator function.
   */
  public static minSize(bytes: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      if (!(control && control.value)) {
        return null;
      }

      const size =  (control.value as File[]).map(f => f.size).reduce((acc, i) => acc + i, 0);
      const condition = bytes < size;
      return condition
        ? null : {
          minSize: {
            actualSize: size,
            requiredSize: bytes
          }
        };
    };
  }
}
