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

import {TokenClaim} from './token-claim';
import {TokenType} from './token-type';

/**
 * The GenerateTokenRequest class holds the information for a request to generate a token.
 *
 * @author Marcus Portmann
 */
export class GenerateTokenRequest {

  /**
   * The claims for the token.
   */
  claims: TokenClaim[];

  /**
   * The description for the token.
   */
  description: string | null = null;

  /**
   * The date the token expires.
   */
  expires: Date | null;

  /**
   * The name of the token.
   */
  name: string;

  /**
   * The token type.
   */
  type: TokenType;

  /**
   * The date the token is valid from.
   */
  validFrom: Date | null;

  /**
   * Constructs a new GenerateTokenRequest.
   *
   * @param type        The token type.
   * @param name        The name of the token.
   * @param description The description for the token.
   * @param claims      The claims for the token.
   * @param expires     The date the token expires.
   * @param validFrom   The date the token is valid from.
   */
  constructor(type: TokenType, name: string, description: string, claims: TokenClaim[],
              expires?: Date, validFrom?: Date) {
    this.type = type;
    this.name = name;
    this.description = description;
    this.claims = claims;
    this.expires = !!expires ? expires : null;
    this.validFrom = !!validFrom ? validFrom : null;
  }
}
