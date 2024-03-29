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

/**
 * The TokenSortBy enumeration defines the possible methods used to sort a list of tokens.
 *
 * @author Marcus Portmann
 */
export enum TokenSortBy {

  /**
   * Sort by expires.
   */
  Expires = 'expires',

  /**
   * Sort by issued.
   */
  Issued = 'issued',

  /**
   * Sort by name.
   */
  Name = 'name',

  /**
   * Sort by revoked.
   */
  Revoked = 'revoked',

  /**
   * Sort by type.
   */
  Type = 'type'
}
