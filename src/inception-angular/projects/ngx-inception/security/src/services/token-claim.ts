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
 * The TokenClaim class holds the information for a token claim.
 *
 * @author Marcus Portmann
 */
export class TokenClaim {

  /**
   * The name of the token claim.
   */
  name: string;

  /**
   * The values for the token claim.
   */
  values: string[];

  /**
   * Constructs a new TokenClaim.
   *
   * @param name   The name of the token claim.
   * @param values The values for the token claim.
   */
  constructor(name: string, values: string[]) {
    this.name = name;
    this.values = values;
  }
}
