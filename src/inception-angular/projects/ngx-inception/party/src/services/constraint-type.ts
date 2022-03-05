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
 * The ConstraintType enumeration defines the types of constraints that can be applied when
 * validating an attribute or preference for a party.
 *
 * @author Marcus Portmann
 */
export enum ConstraintType {

  /**
   * Maximum size.
   */
  MaxSize = 'max_size',

  /**
   * Minimum size.
   */
  MinSize = 'min_size',

  /**
   * Pattern.
   */
  Pattern = 'pattern',

  /**
   * Reference.
   */
  Reference = 'reference',

  /**
   * Required.
   */
  Required = 'required',

  /**
   * Size.
   */
  Size = 'size'
}

