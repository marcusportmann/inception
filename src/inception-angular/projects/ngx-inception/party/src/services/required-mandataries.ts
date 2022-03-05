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
 * The RequiredMandataries enumeration defining the possible numbers of mandataries required to
 * execute mandates.
 *
 * @author Marcus Portmann
 */
export enum RequiredMandataries {

  /**
   * All mandataries.
   */
  All = 'all',

  /**
   * Any mandatary.
   */
  Any = 'any',

  /**
   * Any two mandataries.
   */
  AnyTwo = 'any_two',

  /**
   * Any three mandataries.
   */
  AnyThree = 'any_three',

  /**
   * Any four mandataries.
   */
  AnyFour = 'any_four',

  /**
   * Any five mandataries.
   */
  AnyFive = 'any_five'
}
