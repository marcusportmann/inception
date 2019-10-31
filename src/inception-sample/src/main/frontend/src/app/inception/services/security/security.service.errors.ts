/*
 * Copyright 2019 Marcus Portmann
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
import {HttpError} from '../../errors/http-error';
import {Error} from '../../errors/error';
import {ApiError} from '../../errors/api-error';

/**
 * The AuthenticationFailedError class holds the information for an authentication failed error.
 *
 * @author Marcus Portmann
 */
export class AuthenticationFailedError extends Error {

  /**
   * Constructs a new AuthenticationFailedError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The DuplicateGroupError class holds the information for a duplicate group error.
 *
 * @author Marcus Portmann
 */
export class DuplicateGroupError extends Error {

  /**
   * Constructs a new DuplicateGroupError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The DuplicateOrganizationError class holds the information for a duplicate organization error.
 *
 * @author Marcus Portmann
 */
export class DuplicateOrganizationError extends Error {

  /**
   * Constructs a new DuplicateOrganizationError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The DuplicateUserDirectoryError class holds the information for a duplicate user directory error.
 *
 * @author Marcus Portmann
 */
export class DuplicateUserDirectoryError extends Error {

  /**
   * Constructs a new DuplicateUserDirectoryError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The DuplicateUserError class holds the information for a duplicate user error.
 *
 * @author Marcus Portmann
 */
export class DuplicateUserError extends Error {

  /**
   * Constructs a new DuplicateUserError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The ExistingGroupMemberError class holds the information for an existing group member error.
 *
 * @author Marcus Portmann
 */
export class ExistingGroupMemberError extends Error {

  /**
   * Constructs a new ExistingGroupMemberError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The ExistingGroupRoleError class holds the information for an existing group role error.
 *
 * @author Marcus Portmann
 */
export class ExistingGroupRoleError extends Error {

  /**
   * Constructs a new ExistingGroupRoleError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The ExistingGroupMembersError class holds the information for an existing group members error.
 *
 * @author Marcus Portmann
 */
export class ExistingGroupMembersError extends Error {

  /**
   * Constructs a new ExistingGroupMembersError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The ExistingPasswordError class holds the information for an existing password error.
 *
 * @author Marcus Portmann
 */
export class ExistingPasswordError extends Error {

  /**
   * Constructs a new ExistingPasswordError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The GroupMemberNotFoundError class holds the information for a group member not found error.
 *
 * @author Marcus Portmann
 */
export class GroupMemberNotFoundError extends Error {

  /**
   * Constructs a new GroupMemberNotFoundError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The GroupNotFoundError class holds the information for a group not found error.
 *
 * @author Marcus Portmann
 */
export class GroupNotFoundError extends Error {

  /**
   * Constructs a new UserNotFoundError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The GroupRoleNotFoundError class holds the information for a group role not found error.
 *
 * @author Marcus Portmann
 */
export class GroupRoleNotFoundError extends Error {

  /**
   * Constructs a new GroupRoleNotFoundError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The OrganizationNotFoundError class holds the information for an organization not found error.
 *
 * @author Marcus Portmann
 */
export class OrganizationNotFoundError extends Error {

  /**
   * Constructs a new OrganizationNotFoundError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The SecurityServiceError class holds the information for a Security Service error.
 *
 * @author Marcus Portmann
 */
export class SecurityServiceError extends Error {

  /**
   * Constructs a new SecurityServiceError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The UserDirectoryNotFoundError class holds the information for a user directory not found error.
 *
 * @author Marcus Portmann
 */
export class UserDirectoryNotFoundError extends Error {

  /**
   * Constructs a new UserDirectoryNotFoundError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The UserLockedError class holds the information for a user locked error.
 *
 * @author Marcus Portmann
 */
export class UserLockedError extends Error {

  /**
   * Constructs a new UserLockedError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The UserNotFoundError class holds the information for a user not found error.
 *
 * @author Marcus Portmann
 */
export class UserNotFoundError extends Error {

  /**
   * Constructs a new UserNotFoundError.
   *
   * @param message The error message.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}


