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

import {HttpErrorResponse} from '@angular/common/http';
import {Error, HttpError, ProblemDetails} from 'ngx-inception/core';

/**
 * The AssociationNotFoundError class holds the information for an association not found error.
 *
 * @author Marcus Portmann
 */
export class AssociationNotFoundError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/association-not-found';

  /**
   * Constructs a new AssociationNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_association_not_found_error:The association could not be found.`, cause);
  }
}

/**
 * The DuplicateAssociationError class holds the information for a duplicate association error.
 *
 * @author Marcus Portmann
 */
export class DuplicateAssociationError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/duplicate-association';

  /**
   * Constructs a new DuplicateAssociationError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_duplicate_association_error:The association already exists.`, cause);
  }
}

/**
 * The DuplicateMandateError class holds the information for a duplicate mandate error.
 *
 * @author Marcus Portmann
 */
export class DuplicateMandateError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/duplicate-mandate';

  /**
   * Constructs a new DuplicateMandateError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_duplicate_mandate_error:The mandate already exists.`, cause);
  }
}

/**
 * The DuplicateOrganizationError class holds the information for a duplicate organization error.
 *
 * @author Marcus Portmann
 */
export class DuplicateOrganizationError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/duplicate-organization';

  /**
   * Constructs a new DuplicateOrganizationError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_duplicate_organization_error:The organization already exists.`, cause);
  }
}

/**
 * The DuplicatePartyError class holds the information for a duplicate party error.
 *
 * @author Marcus Portmann
 */
export class DuplicatePartyError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/duplicate-party';

  /**
   * Constructs a new DuplicatePartyError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_duplicate_party_error:The party already exists.`, cause);
  }
}

/**
 * The DuplicatePersonError class holds the information for a duplicate person error.
 *
 * @author Marcus Portmann
 */
export class DuplicatePersonError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/duplicate-person';

  /**
   * Constructs a new DuplicatePersonError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_duplicate_person_error:The person already exists.`, cause);
  }
}

/**
 * The MandateNotFoundError class holds the information for a mandate not found error.
 *
 * @author Marcus Portmann
 */
export class MandateNotFoundError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/mandate-not-found';

  /**
   * Constructs a new MandateNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_mandate_not_found_error:The mandate could not be found.`, cause);
  }
}

/**
 * The OrganizationNotFoundError class holds the information for an organization not found error.
 *
 * @author Marcus Portmann
 */
export class OrganizationNotFoundError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/organization-not-found';

  /**
   * Constructs a new OrganizationNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_organization_not_found_error:The organization could not be found.`, cause);
  }
}

/**
 * The PartyNotFoundError class holds the information for a party not found error.
 *
 * @author Marcus Portmann
 */
export class PartyNotFoundError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/party-not-found';

  /**
   * Constructs a new PartyNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_party_not_found_error:The party could not be found.`, cause);
  }
}

/**
 * The PersonNotFoundError class holds the information for a person not found error.
 *
 * @author Marcus Portmann
 */
export class PersonNotFoundError extends Error {

  static readonly TYPE = 'http://inception.digital/problems/party/person-not-found';

  /**
   * Constructs a new PersonNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super($localize`:@@party_person_not_found_error:The person could not be found.`, cause);
  }
}



