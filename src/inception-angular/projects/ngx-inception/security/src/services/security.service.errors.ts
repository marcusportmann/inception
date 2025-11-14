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

import { HttpErrorResponse } from '@angular/common/http';
import { Error, HttpError, ProblemDetails } from 'ngx-inception/core';

/**
 * The AuthenticationFailedError class holds the information for an authentication failed error.
 *
 * @author Marcus Portmann
 */
export class AuthenticationFailedError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/authentication-failed';

  /**
   * Constructs a new AuthenticationFailedError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_authentication_failed_error:Authentication failed.`,
      cause
    );
  }
}

/**
 * The DuplicateGroupError class holds the information for a duplicate group error.
 *
 * @author Marcus Portmann
 */
export class DuplicateGroupError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/duplicate-group';

  /**
   * Constructs a new DuplicateGroupError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_duplicate_group_error:A group with the specified name already exists.`,
      cause
    );
  }
}

/**
 * The DuplicatePolicyError class holds the information for a duplicate policy error.
 *
 * @author Marcus Portmann
 */
export class DuplicatePolicyError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/duplicate-policy';

  /**
   * Constructs a new DuplicatePolicyError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_duplicate_policy_error:A policy with the specified ID already exists.`,
      cause
    );
  }
}

/**
 * The DuplicateTenantError class holds the information for a duplicate tenant error.
 *
 * @author Marcus Portmann
 */
export class DuplicateTenantError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/duplicate-tenant';

  /**
   * Constructs a new DuplicateTenantError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_duplicate_tenant_error:A tenant with the specified ID or name already exists.`,
      cause
    );
  }
}

/**
 * The DuplicateUserDirectoryError class holds the information for a duplicate user directory error.
 *
 * @author Marcus Portmann
 */
export class DuplicateUserDirectoryError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/duplicate-user-directory';

  /**
   * Constructs a new DuplicateUserDirectoryError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_duplicate_user_directory_error:A user directory with the specified ID or name already exists.`,
      cause
    );
  }
}

/**
 * The DuplicateUserError class holds the information for a duplicate user error.
 *
 * @author Marcus Portmann
 */
export class DuplicateUserError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/duplicate-user';

  /**
   * Constructs a new DuplicateUserError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_duplicate_user_error:A user with the specified username already exists.`,
      cause
    );
  }
}

/**
 * The ExistingGroupMembersError class holds the information for an existing group members error.
 *
 * @author Marcus Portmann
 */
export class ExistingGroupMembersError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/existing-group-members';

  /**
   * Constructs a new ExistingGroupMembersError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_existing_group_members_error:The group has existing members.`,
      cause
    );
  }
}

/**
 * The ExistingGroupsError class holds the information for an existing groups error.
 *
 * @author Marcus Portmann
 */
export class ExistingGroupsError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/existing-groups';

  /**
   * Constructs a new ExistingGroupsError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_existing_groups_error:The user directory has existing groups.`,
      cause
    );
  }
}

/**
 * The ExistingPasswordError class holds the information for an existing password error.
 *
 * @author Marcus Portmann
 */
export class ExistingPasswordError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/existing-password';

  /**
   * Constructs a new ExistingPasswordError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_existing_password_error:The new password has been used recently and is not valid.`,
      cause
    );
  }
}

/**
 * The ExistingUsersError class holds the information for an existing-users error.
 *
 * @author Marcus Portmann
 */
export class ExistingUsersError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/existing-users';

  /**
   * Constructs a new ExistingUsersError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_existing_users_error:The user directory has existing users.`,
      cause
    );
  }
}

/**
 * The GroupMemberNotFoundError class holds the information for a group member not found error.
 *
 * @author Marcus Portmann
 */
export class GroupMemberNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/group-member-not-found';

  /**
   * Constructs a new GroupMemberNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_group_member_not_found_error:The group member could not be found.`,
      cause
    );
  }
}

/**
 * The GroupNotFoundError class holds the information for a group not found error.
 *
 * @author Marcus Portmann
 */
export class GroupNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/group-not-found';

  /**
   * Constructs a new GroupNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_group_not_found_error:The group could not be found.`,
      cause
    );
  }
}

/**
 * The GroupRoleNotFoundError class holds the information for a group role not found error.
 *
 * @author Marcus Portmann
 */
export class GroupRoleNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/group-role-not-found';

  /**
   * Constructs a new GroupRoleNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_group_role_not_found_error:The group role could not be found.`,
      cause
    );
  }
}

/**
 * The InvalidPolicyDataError class holds the information for an invalid policy data error.
 *
 * @author Marcus Portmann
 */
export class InvalidPolicyDataError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/invalid-policy-data';

  /**
   * Constructs a new InvalidPolicyDataError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_invalid_policy_data_error:The policy data is invalid.`,
      cause
    );
  }
}

/**
 * The InvalidSecurityCodeError class holds the information for an invalid security code error.
 *
 * @author Marcus Portmann
 */
export class InvalidSecurityCodeError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/invalid-security-code';

  /**
   * Constructs a new InvalidSecurityCodeError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_invalid_security_code_error:The security code is invalid.`,
      cause
    );
  }
}

/**
 * The PolicyDataMismatchError class holds the information for a policy data mismatch error.
 *
 * @author Marcus Portmann
 */
export class PolicyDataMismatchError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/policy-data-mismatch';

  /**
   * Constructs a new PolicyDataMismatchError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_policy_data_mismatch_error:The policy type, policy ID or policy version does not match the policy data.`,
      cause
    );
  }
}

/**
 * The PolicyNotFoundError class holds the information for a policy not found error.
 *
 * @author Marcus Portmann
 */
export class PolicyNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/policy-not-found';

  /**
   * Constructs a new PolicyNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_policy_not_found_error:The policy could not be found.`,
      cause
    );
  }
}

/**
 * The RoleNotFoundError class holds the information for a role not found error.
 *
 * @author Marcus Portmann
 */
export class RoleNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/role-not-found';

  /**
   * Constructs a new RoleNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_role_not_found_error:The role could not be found.`,
      cause
    );
  }
}

/**
 * The SessionError class holds the information for a session error.
 *
 * @author Marcus Portmann
 */
export class SessionError extends Error {
  /**
   * Constructs a new SessionError.
   *
   * @param message The error message.
   * @param cause   The cause of the error.
   */
  constructor(
    message: string,
    cause?: ProblemDetails | HttpErrorResponse | HttpError
  ) {
    super(message, cause);
  }
}

/**
 * The TenantNotFoundError class holds the information for a tenant not found error.
 *
 * @author Marcus Portmann
 */
export class TenantNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/tenant-not-found';

  /**
   * Constructs a new TenantNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_tenant_not_found_error:The tenant could not be found.`,
      cause
    );
  }
}

/**
 * The TenantUserDirectoryNotFoundError class holds the information for a tenant user
 * directory not found error.
 *
 * @author Marcus Portmann
 */
export class TenantUserDirectoryNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/tenant-user-directory-not-found';

  /**
   * Constructs a new TenantUserDirectoryNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_tenant_user_directory_not_found_error:The tenant user directory could not be found.`,
      cause
    );
  }
}

/**
 * The TokenNotFoundError class holds the information for a token not found error.
 *
 * @author Marcus Portmann
 */
export class TokenNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/token-not-found';

  /**
   * Constructs a new TokenNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_token_not_found_error:The token could not be found.`,
      cause
    );
  }
}

/**
 * The UserDirectoryNotFoundError class holds the information for a user directory not found error.
 *
 * @author Marcus Portmann
 */
export class UserDirectoryNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/user-directory-not-found';

  /**
   * Constructs a new UserDirectoryNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_user_directory_not_found_error:The user directory could not be found.`,
      cause
    );
  }
}

/**
 * The UserLockedError class holds the information for a user-locked error.
 *
 * @author Marcus Portmann
 */
export class UserLockedError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/user-locked';

  /**
   * Constructs a new UserLockedError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_user_locked_error:The user has exceeded the maximum number of failed password attempts and has been locked.`,
      cause
    );
  }
}

/**
 * The UserNotFoundError class holds the information for a user not found error.
 *
 * @author Marcus Portmann
 */
export class UserNotFoundError extends Error {
  static readonly TYPE =
    'https://inception.digital/problems/security/user-not-found';

  /**
   * Constructs a new UserNotFoundError.
   *
   * @param cause The cause of the error.
   */
  constructor(cause?: ProblemDetails | HttpErrorResponse | HttpError) {
    super(
      $localize`:@@security_user_not_found_error:The user could not be found.`,
      cause
    );
  }
}
