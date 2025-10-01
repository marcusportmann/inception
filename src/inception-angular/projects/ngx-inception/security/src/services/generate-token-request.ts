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

import { TokenClaim } from './token-claim';
import { TokenType } from './token-type';

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
   * The ISO 8601 format date value for the date the token expires.
   */
  expiryDate: string | null;

  /**
   * The name of the token.
   */
  name: string;

  /**
   * The token type.
   */
  type: TokenType;

  /**
   * The ISO 8601 format date value for the date the token is valid from.
   */
  validFromDate: string | null;

  /**
   * Constructs a new GenerateTokenRequest.
   *
   * @param type          The token type.
   * @param name          The name of the token.
   * @param description   The description for the token.
   * @param claims        The claims for the token.
   * @param expiryDate    The ISO 8601 format date value for the date the token expires.
   * @param validFromDate The ISO 8601 format date value for the date the token is valid from.
   */
  constructor(
    type: TokenType,
    name: string,
    description: string,
    claims: TokenClaim[],
    expiryDate?: string,
    validFromDate?: string
  ) {
    this.type = type;
    this.name = name;
    this.description = description;
    this.claims = claims;
    this.expiryDate = !!expiryDate ? expiryDate : null;
    this.validFromDate = !!validFromDate ? validFromDate : null;
  }
}
