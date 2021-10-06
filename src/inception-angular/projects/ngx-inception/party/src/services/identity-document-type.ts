/*
 * Copyright 2021 Marcus Portmann
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
 * The IdentityDocumentType class holds the information for an identity document type.
 *
 * @author Marcus Portmann
 */
export class IdentityDocumentType {

  /**
   * The code for the identity document type.
   */
  code: string;

  /**
   * The optional ISO 3166-1 alpha-2 code for the country of issue for the identity document type.
   */
  countryOfIssue: string;

  /**
   * The description for the identity document type.
   */
  description: string;

  /**
   * The Unicode locale identifier for the identity document type.
   */
  localeId: string;

  /**
   * The name of the identity document type.
   */
  name: string;

  /**
   * The sort index for the identity document type.
   */
  sortIndex: number;

  /**
   * The Universally Unique Identifier (UUID) for the tenant the identity document type is specific
   * to.
   */
  tenantId?: string;

  /**
   * Constructs a new IdentityDocumentType.
   *
   * @param code           The code for the identity document type.
   * @param localeId       The Unicode locale identifier for the identity document type.
   * @param sortIndex      The sort index for the identity document type.
   * @param name           The name of the identity document type.
   * @param description    The description for the identity document type.
   * @param countryOfIssue The optional ISO 3166-1 alpha-2 code for the country of issue for the
   *                       identity document type.
   * @param tenantId       The Universally Unique Identifier (UUID) for the tenant the identity
   *                       document type is specific to.
   */
  constructor(code: string, localeId: string, sortIndex: number, name: string, description: string,
              countryOfIssue: string, tenantId?: string) {
    this.code = code;
    this.localeId = localeId;
    this.sortIndex = sortIndex;
    this.name = name;
    this.description = description;
    this.countryOfIssue = countryOfIssue;
    this.tenantId = tenantId;
  }
}
