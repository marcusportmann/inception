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

import {TokenStatus} from './token-status';
import {TokenType} from './token-type';

/**
 * The TokenSummary class holds the summary information for a token.
 *
 * @author Marcus Portmann
 */
export class TokenSummary {

  /**
   * The description for the token.
   */
  description: string | null = null;

  /**
   * The ISO 8601 format date value for the date the token expires.
   */
  expiryDate: string | null = null;

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
   * The ISO 8601 format date value for the date the token was revoked.
   */
  revocationDate: string | null = null;

  /**
   * The token status.
   */
  status: TokenStatus;

  /**
   * The token type.
   */
  type: TokenType;

  /**
   * The ISO 8601 format date value for the date the token is valid from.
   */
  validFromDate: string | null = null;

  /**
   * Constructs a new TokenSummary.
   *
   * @param id          The ID for the token.
   * @param type        The token type.
   * @param name        The name of the token.
   * @param description The description for the token.
   * @param issued      The date and time the token was issued.
   * @param status      The token status.
   */
  constructor(id: string, type: TokenType, name: string, description: string, issued: Date,
              status: TokenStatus) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.description = description;
    this.issued = issued;
    this.status = status;
  }
}
