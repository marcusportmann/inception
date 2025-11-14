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

import { TokenType } from './token-type';

/**
 * The RevokedToken class holds the information for a revoked token.
 *
 * @author Marcus Portmann
 */
export class RevokedToken {
  /**
   * The date the token expires.
   */
  expires: Date | null = null;

  /**
   * The ID for the token.
   */
  id: string;

  /**
   * The date and time the token was issued.
   */
  issued: Date;

  /**
   * The name of the token.
   */
  name: string;

  /**
   * The date the token was revoked.
   */
  revoked: Date | null = null;

  /**
   * The token type.
   */
  type: TokenType;

  /**
   * The date the token is valid from.
   */
  validFrom: Date | null = null;

  /**
   * Constructs a new RevokedToken.
   *
   * @param id        The ID for the token.
   * @param type      The token type.
   * @param name      The name of the token.
   * @param issued    The date and time the token was issued.
   * @param expires   The date the token expires.
   * @param validFrom The date the token is valid from.
   * @param revoked   The date the token was revoked.
   */
  constructor(
    id: string,
    type: TokenType,
    name: string,
    issued: Date,
    expires?: Date,
    validFrom?: Date,
    revoked?: Date
  ) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.issued = issued;
    this.expires = expires ? expires : null;
    this.validFrom = validFrom ? validFrom : null;
    this.revoked = revoked ? revoked : null;
  }
}
