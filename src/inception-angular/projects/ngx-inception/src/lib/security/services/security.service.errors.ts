/*
 * Copyright 2020 Marcus Portmann
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
import {HttpError} from '../../core/errors/http-error';
import {Error} from '../../core/errors/error';
import {ApiError} from '../../core/errors/api-error';

/**
 * The AuthenticationFailedError class holds the information for an authentication failed error.
 *
 * @author Marcus Portmann
 */
export class AuthenticationFailedError extends Error {

  /**
   * Constructs a new AuthenticationFailedError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_authentication_failed_error:Authentication failed.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_duplicate_group_error:A group with the specified name already exists.`, cause);
  }
}

/**
 * The DuplicateTenantError class holds the information for a duplicate tenant error.
 *
 * @author Marcus Portmann
 */
export class DuplicateTenantError extends Error {

  /**
   * Constructs a new DuplicateTenantError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_duplicate_tenant_error:An tenant with the specified ID or name already exists.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_duplicate_user_directory_error:A user directory with the specified ID or name already exists.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_duplicate_user_error:A user with the specified username already exists.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_existing_group_member_error:The group member already exists.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_existing_group_members_error:The group has existing members.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_existing_group_role_error:The group role already exists.`, cause);
  }
}

/**
 * The ExistingTenantUserDirectoryError class holds the information for an existing
 * tenant user directory error.
 *
 * @author Marcus Portmann
 */
export class ExistingTenantUserDirectoryError extends Error {

  /**
   * Constructs a new ExistingTenantUserDirectoryError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_existing_tenant_user_directory_error:The tenant user directory already exists.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_existing_password_error:The new password has been used recently and is not valid.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_group_member_not_found_error:The group member could not be found.`, cause);
  }
}

/**
 * The GroupNotFoundError class holds the information for a group not found error.
 *
 * @author Marcus Portmann
 */
export class GroupNotFoundError extends Error {

  /**
   * Constructs a new GroupNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_group_not_found_error:The group could not be found.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_group_role_not_found_error:The group role could not be found.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_invalid_security_code_error:The security code is invalid.`, cause);
  }
}

/**
 * The LoginError class holds the information for a login error.
 *
 * @author Marcus Portmann
 */
export class LoginError extends Error {

  /**
   * Constructs a new LoginError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_login_error:Incorrect username or password.`, cause);
  }
}

/**
 * The PasswordExpiredError class holds the information for a password expired error.
 *
 * @author Marcus Portmann
 */
export class PasswordExpiredError extends Error {

  /**
   * Constructs a new PasswordExpiredError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_password_expired_error:The password has expired.`, cause);
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
 * The SessionError class holds the information for a session error.
 *
 * @author Marcus Portmann
 */
export class SessionError extends Error {

  /**
   * Constructs a new SecurityServiceError.
   *
   * @param message The error SessionError.
   * @param cause   The optional cause of the error.
   */
  constructor(message: string, cause?: ApiError | HttpErrorResponse | HttpError) {
    super(message, cause);
  }
}

/**
 * The TenantNotFoundError class holds the information for a tenant not found error.
 *
 * @author Marcus Portmann
 */
export class TenantNotFoundError extends Error {

  /**
   * Constructs a new TenantNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_tenant_not_found_error:The tenant could not be found.`, cause);
  }
}

/**
 * The TenantUserDirectoryNotFoundError class holds the information for a tenant user
 * directory not found error.
 *
 * @author Marcus Portmann
 */
export class TenantUserDirectoryNotFoundError extends Error {

  /**
   * Constructs a new TenantUserDirectoryNotFoundError.
   *
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_tenant_user_directory_not_found_error:The tenant user directory could not be found.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_user_directory_not_found_error:The user directory could not be found.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_user_locked_error:The user has exceeded the maximum number of failed password attempts and has been locked.`, cause);
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
   * @param cause The optional cause of the error.
   */
  constructor(cause?: ApiError | HttpErrorResponse | HttpError) {
    super($localize`:@@security_user_not_found_error:The user could not be found.`, cause);
  }
}


