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
import {I18n} from '@ngx-translate/i18n-polyfill';

/**
 * The AuthenticationFailedError class holds the information for an authentication failed error.
 *
 * @author Marcus Portmann
 */
export class AuthenticationFailedError extends Error {

  /**
   * Constructs a new AuthenticationFailedError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_authentication_failed_error',
      value: 'Authentication failed.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_duplicate_group_error',
      value: 'A group with the specified name already exists.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_duplicate_organization_error',
      value: 'An organization with the specified ID or name already exists.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_duplicate_user_directory_error',
      value: 'A user directory with the specified ID or name already exists.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_duplicate_user_error',
      value: 'A user with the specified username already exists.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_existing_group_member_error',
      value: 'The group member already exists.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_existing_group_members_error',
      value: 'The group has existing members.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_existing_group_role_error',
      value: 'The group role already exists.'
    }), cause);
  }
}

/**
 * The ExistingOrganizationUserDirectoryError class holds the information for an existing
 * organization user directory error.
 *
 * @author Marcus Portmann
 */
export class ExistingOrganizationUserDirectoryError extends Error {

  /**
   * Constructs a new ExistingOrganizationUserDirectoryError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_existing_organization_user_directory_error',
      value: 'The organization user directory already exists.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_existing_password_error',
      value: 'The new password has been used recently and is not valid.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_group_member_not_found_error',
      value: 'The group member could not be found.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_group_not_found_error',
      value: 'The group could not be found.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_group_role_not_found_error',
      value: 'The group role could not be found.'
    }), cause);
  }
}

/**
 * The InvalidSecurityCodeError class holds the information for an invalid security code error.
 *
 * @author Marcus Portmann
 */
export class InvalidSecurityCodeError extends Error {

  /**
   * Constructs a new InvalidSecurityCodeError.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_invalid_security_code_error',
      value: 'The security code is invalid.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_organization_not_found_error',
      value: 'The organization could not be found.'
    }), cause);
  }
}

/**
 * The OrganizationUserDirectoryNotFound class holds the information for an organization user
 * directory not found error.
 *
 * @author Marcus Portmann
 */
export class OrganizationUserDirectoryNotFound extends Error {

  /**
   * Constructs a new OrganizationUserDirectoryNotFound.
   *
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_organization_user_directory_not_found_error',
      value: 'The organization user directory could not be found.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_user_directory_not_found_error',
      value: 'The user directory could not be found.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_user_locked_error',
      value: 'The user has exceeded the maximum number of failed password attempts and has been locked.'
    }), cause);
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
   * @param i18n  The internationalization service.
   * @param cause The optional cause of the error.
   */
  constructor(i18n: I18n, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(i18n({
      id: '@@security_user_not_found_error',
      value: 'The user could not be found.'
    }), cause);
  }
}


