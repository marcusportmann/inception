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
 * The ValueChange class holds the information for a value change.
 */
export class ValueChange<ValueType> {

  /**
   * The old value.
   */
  oldValue: ValueType | null;

  /**
   * The new value.
   */
  newValue: ValueType | null;

  /**
   * Constructs a new ValueChange.
   *
   * @param oldValue The old value.
   * @param newValue The new value.
   */
  constructor(oldValue: ValueType | null, newValue: ValueType | null) {
    this.oldValue = oldValue;
    this.newValue = newValue;
  }
}
