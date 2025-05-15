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

package digital.inception.security.controller;

import digital.inception.core.api.ProblemDetails;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.security.model.AuthenticationFailedException;
import digital.inception.security.model.DuplicateGroupException;
import digital.inception.security.model.DuplicatePolicyException;
import digital.inception.security.model.DuplicateTenantException;
import digital.inception.security.model.DuplicateUserDirectoryException;
import digital.inception.security.model.DuplicateUserException;
import digital.inception.security.model.ExistingGroupMembersException;
import digital.inception.security.model.ExistingGroupsException;
import digital.inception.security.model.ExistingPasswordException;
import digital.inception.security.model.ExistingUsersException;
import digital.inception.security.model.GenerateTokenRequest;
import digital.inception.security.model.Group;
import digital.inception.security.model.GroupMember;
import digital.inception.security.model.GroupMemberNotFoundException;
import digital.inception.security.model.GroupMemberType;
import digital.inception.security.model.GroupMembers;
import digital.inception.security.model.GroupNotFoundException;
import digital.inception.security.model.GroupRole;
import digital.inception.security.model.GroupRoleNotFoundException;
import digital.inception.security.model.Groups;
import digital.inception.security.model.InvalidPolicyDataException;
import digital.inception.security.model.InvalidSecurityCodeException;
import digital.inception.security.model.PasswordChange;
import digital.inception.security.model.Policy;
import digital.inception.security.model.PolicyDataMismatchException;
import digital.inception.security.model.PolicyNotFoundException;
import digital.inception.security.model.PolicySortBy;
import digital.inception.security.model.PolicySummaries;
import digital.inception.security.model.RevokedToken;
import digital.inception.security.model.Role;
import digital.inception.security.model.RoleNotFoundException;
import digital.inception.security.model.Tenant;
import digital.inception.security.model.TenantNotFoundException;
import digital.inception.security.model.TenantUserDirectory;
import digital.inception.security.model.TenantUserDirectoryNotFoundException;
import digital.inception.security.model.Tenants;
import digital.inception.security.model.Token;
import digital.inception.security.model.TokenNotFoundException;
import digital.inception.security.model.TokenSortBy;
import digital.inception.security.model.TokenStatus;
import digital.inception.security.model.TokenSummaries;
import digital.inception.security.model.User;
import digital.inception.security.model.UserDirectories;
import digital.inception.security.model.UserDirectory;
import digital.inception.security.model.UserDirectoryCapabilities;
import digital.inception.security.model.UserDirectoryNotFoundException;
import digital.inception.security.model.UserDirectorySummaries;
import digital.inception.security.model.UserDirectorySummary;
import digital.inception.security.model.UserDirectoryType;
import digital.inception.security.model.UserDirectoryTypeNotFoundException;
import digital.inception.security.model.UserLockedException;
import digital.inception.security.model.UserNotFoundException;
import digital.inception.security.model.UserSortBy;
import digital.inception.security.model.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code SecurityApiController} interface.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Security")
@RequestMapping(value = "/api/security")
// @el (isSecurityDisabled: digital.inception.api.SecureApiSecurityExpressionRoot.isSecurityEnabled)
public interface SecurityApiController {

  /**
   * Add the group member to the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param groupMember the group member
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the group member could not be added to the group
   */
  @Operation(
      summary = "Add the group member to the group",
      description = "Add the group member to the group")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The group member was successfully added to the group"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group or user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The group member already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}/members",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  void addMemberToGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The group member association to add",
              required = true)
          @RequestBody
          GroupMember groupMember)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException;

  /**
   * Add the role to the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param groupRole the group role
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws RoleNotFoundException if the role could not be found
   * @throws ServiceUnavailableException if the role could not be added to the group
   */
  @Operation(summary = "Add the role to the group", description = "Add the role to the group")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The role was successfully added to the group"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group or role could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The group role already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}/roles",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  void addRoleToGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The group role association to add",
              required = true)
          @RequestBody
          GroupRole groupRole)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          RoleNotFoundException,
          ServiceUnavailableException;

  /**
   * Add the user directory to the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param tenantUserDirectory the tenant user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be added to the tenant
   */
  @Operation(
      summary = "Add the user directory to the tenant",
      description = "Add the user directory to the tenant")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The user directory was successfully added to the tenant"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The tenant or user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The tenant user directory already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants/{tenantId}/user-directories",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  void addUserDirectoryToTenant(
      @Parameter(name = "tenantId", description = "The ID for the tenant", required = true)
          @PathVariable
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The tenant user directory association to add",
              required = true)
          @RequestBody
          TenantUserDirectory tenantUserDirectory)
      throws InvalidArgumentException,
          TenantNotFoundException,
          UserDirectoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Administratively change the password for the user.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username for the user
   * @param passwordChange the password change
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the password could not be administratively changed
   */
  @Operation(
      summary = "Administratively change the password for the user",
      description = "Administratively change the password for the user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The password for the user was changed successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/users/{username}/password",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.UserAdministration') or hasAccessToFunction('Security.ResetUserPassword')")
  void adminChangePassword(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "username", description = "The username for the user", required = true)
          @PathVariable
          String username,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The password change to apply",
              required = true)
          @RequestBody
          PasswordChange passwordChange)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException;

  /**
   * Change the password for the user.
   *
   * @param username the username for the user
   * @param passwordChange the password change
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws AuthenticationFailedException if the authentication failed
   * @throws InvalidSecurityCodeException if the security code is invalid
   * @throws ExistingPasswordException if the user has previously used the new password
   * @throws UserLockedException if the user is locked
   * @throws ServiceUnavailableException if the password could not be changed
   */
  @Operation(
      summary = "Change the password for the user",
      description = "Change the password for the user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The password for the user was changed successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication failed or invalid security code",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description =
                "The user has exceeded the maximum number of failed password attempts and has been locked",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "The new password for the user has been used recently and is not valid",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/users/{username}/password",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void changePassword(
      @Parameter(name = "username", description = "The username for the user", required = true)
          @PathVariable
          String username,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The password change to apply",
              required = true)
          @RequestBody
          PasswordChange passwordChange)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          AuthenticationFailedException,
          InvalidSecurityCodeException,
          ExistingPasswordException,
          UserLockedException,
          ServiceUnavailableException;

  /**
   * Create the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param group the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws DuplicateGroupException if the group already exists
   * @throws ServiceUnavailableException if the group could not be created
   */
  @Operation(summary = "Create the group", description = "Create the group")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The group was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A group with the specified name already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  void createGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The group to create",
              required = true)
          @RequestBody
          Group group)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          DuplicateGroupException,
          ServiceUnavailableException;

  /**
   * Create the policy.
   *
   * @param policy the policy
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InvalidPolicyDataException the policy data is invalid
   * @throws DuplicatePolicyException if the policy already exists
   * @throws PolicyDataMismatchException if the policy attributes do not match the policy data
   * @throws ServiceUnavailableException if the policy could not be created
   */
  @Operation(summary = "Create the policy", description = "Create the policy")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The policy was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument or invalid policy data",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A policy with the specified already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/policies",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.PolicyAdministration')")
  void createPolicy(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The policy to create",
              required = true)
          @RequestBody
          Policy policy)
      throws InvalidArgumentException,
          InvalidPolicyDataException,
          DuplicatePolicyException,
          PolicyDataMismatchException,
          ServiceUnavailableException;

  /**
   * Create the tenant.
   *
   * @param tenant the tenant
   * @param createUserDirectory should a new internal user directory be created for the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateTenantException if the tenant already exists
   * @throws ServiceUnavailableException if the tenant could not be created
   */
  @Operation(summary = "Create the tenant", description = "Create the tenant")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The tenant was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A tenant with the specified ID or name already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  void createTenant(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The tenant to create",
              required = true)
          @RequestBody
          Tenant tenant,
      @Parameter(
              name = "createUserDirectory",
              description = "Should a new internal user directory be created for the tenant")
          @RequestParam(value = "createUserDirectory", required = false)
          Boolean createUserDirectory)
      throws InvalidArgumentException, DuplicateTenantException, ServiceUnavailableException;

  /**
   * Create the user.
   *
   * @param userDirectoryId the ID for the user directory
   * @param user the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked create the user locked
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws DuplicateUserException if the user already exists
   * @throws ServiceUnavailableException if the user could not be created
   */
  @Operation(summary = "Create the user", description = "Create the user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The user was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A user with the specified username already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/users",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.UserAdministration')")
  void createUser(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The user to create",
              required = true)
          @RequestBody
          User user,
      @Parameter(
              name = "expiredPassword",
              description = "Create the user with its password expired")
          @RequestParam(value = "expiredPassword", required = false)
          Boolean expiredPassword,
      @Parameter(name = "userLocked", description = "Create the user locked")
          @RequestParam(value = "userLocked", required = false)
          Boolean userLocked)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          DuplicateUserException,
          ServiceUnavailableException;

  /**
   * Create the user directory.
   *
   * @param userDirectory the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateUserDirectoryException if the user directory already exists
   * @throws ServiceUnavailableException if the user directory could not be created
   */
  @Operation(summary = "Create the user directory", description = "Create the user directory")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The user directory was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description = "A user directory with the specified ID or name already exists",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration')")
  void createUserDirectory(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The user directory to create",
              required = true)
          @RequestBody
          UserDirectory userDirectory)
      throws InvalidArgumentException, DuplicateUserDirectoryException, ServiceUnavailableException;

  /**
   * Delete the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws ExistingGroupMembersException if the group has existing members
   * @throws ServiceUnavailableException if the group could not be deleted
   */
  @Operation(summary = "Delete the group", description = "Delete the group")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The group was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description =
                "The group could not be deleted since it is still associated with one or more members",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  void deleteGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ExistingGroupMembersException,
          ServiceUnavailableException;

  /**
   * Delete the policy.
   *
   * @param policyId the ID for the policy
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PolicyNotFoundException if the policy could not be found
   * @throws ServiceUnavailableException if the policy could not be deleted
   */
  @Operation(summary = "Delete the policy", description = "Delete the policy")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The policy was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The policy could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/policies/{policyId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.PolicyAdministration')")
  void deletePolicy(
      @Parameter(name = "policyId", description = "The ID for the policy", required = true)
          @PathVariable
          String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException;

  /**
   * Delete the tenant.
   *
   * @param tenantId the ID for the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the tenant could not be deleted
   */
  @Operation(summary = "Delete the tenant", description = "Delete the tenant")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The tenant was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The tenant could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants/{tenantId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  void deleteTenant(
      @Parameter(name = "tenantId", description = "The ID for the tenant", required = true)
          @PathVariable
          UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException;

  /**
   * Delete the token.
   *
   * @param tokenId the ID for the token
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TokenNotFoundException if the token could not be found
   * @throws ServiceUnavailableException if the token could not be deleted
   */
  @Operation(summary = "Delete the token", description = "Delete the token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The token was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The token could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tokens/{tokenId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TokenAdministration')")
  void deleteToken(
      @Parameter(name = "tokenId", description = "The ID for the token", required = true)
          @PathVariable
          String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException;

  /**
   * Delete the user.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username for the user
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the user could not be deleted
   */
  @Operation(summary = "Delete the user", description = "Delete the user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The user was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/users/{username}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.UserAdministration')")
  void deleteUser(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "username", description = "The username for the user", required = true)
          @PathVariable
          String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ExistingGroupsException if the user directory has existing groups
   * @throws ExistingUsersException if the user directory has existing users
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be deleted
   */
  @Operation(summary = "Delete the user directory", description = "Delete the user directory")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The user directory was deleted successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "409",
            description =
                "The user directory could not be deleted since it is still associated with one or more groups or users",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration')")
  void deleteUserDirectory(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId)
      throws InvalidArgumentException,
          ExistingGroupsException,
          ExistingUsersException,
          UserDirectoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Generate a token.
   *
   * @param generateTokenRequest the request to generate the token
   * @return the token
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the token could not be generated
   */
  @Operation(summary = "Generate a token", description = "Generate a token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The token was generated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/generate-token",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TokenAdministration')")
  Token generateToken(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to generate the token",
              required = true)
          @RequestBody
          GenerateTokenRequest generateTokenRequest)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @return the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws ServiceUnavailableException if the group could not be retrieved
   */
  @Operation(summary = "Retrieve the group", description = "Retrieve the group")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  Group getGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve all the group names.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the group names
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the group names could not be retrieved
   */
  @Operation(summary = "Retrieve all the group names", description = "Retrieve all the group names")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/group-names",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  List<String> getGroupNames(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the names of the groups the user is a member of.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username for the user
   * @return the names of the groups the user is a member of
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the names of the groups the user is a member of could
   *     not be retrieved
   */
  @Operation(
      summary = "Retrieve the names of the groups the user is a member of",
      description = "Retrieve the names of the groups the user is a member of")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/users/{username}/group-names",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.UserAdministration')")
  List<String> getGroupNamesForUser(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "username", description = "The username for the user", required = true)
          @PathVariable
          String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the groups.
   *
   * @param userDirectoryId the ID for the user directory
   * @param filter the filter to apply to the groups
   * @param sortDirection the sort direction to apply to the groups
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the groups
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the groups could not be retrieved
   */
  @Operation(summary = "Retrieve the groups", description = "Retrieve the groups")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  Groups getGroups(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "filter", description = "The filter to apply to the groups")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(name = "sortDirection", description = "The sort direction to apply to the groups")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the group members for the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param filter the filter to apply to the group members
   * @param sortDirection the sort direction to apply to the group members
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the group members for the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws ServiceUnavailableException if the group members could not be retrieved for the group
   */
  @Operation(summary = "Retrieve the group members", description = "Retrieve the group members")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}/members",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  GroupMembers getMembersForGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName,
      @Parameter(name = "filter", description = "The filter to apply to the group members")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the group members")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve all the policies.
   *
   * @return the policies
   * @throws ServiceUnavailableException if the policies could not be retrieved
   */
  @Operation(summary = "Retrieve all the policies", description = "Retrieve all the policies")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/policies",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<Policy> getPolicies() throws ServiceUnavailableException;

  /**
   * Retrieve the policy.
   *
   * @param policyId the ID for the policy
   * @return the policy
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PolicyNotFoundException if the policy could not be found
   * @throws ServiceUnavailableException if the policy could not be retrieved
   */
  @Operation(summary = "Retrieve the policy", description = "Retrieve the policy")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The policy could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/policies/{policyId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.PolicyAdministration')")
  Policy getPolicy(
      @Parameter(name = "policyId", description = "The ID for the policy", required = true)
          @PathVariable
          String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the policy.
   *
   * @param policyId the ID for the policy
   * @return the name of the policy
   * @throws InvalidArgumentException if an argument is invalid
   * @throws PolicyNotFoundException if the policy could not be found
   * @throws ServiceUnavailableException if the name of the policy could not be retrieved
   */
  @Operation(
      summary = "Retrieve the name of policy",
      description = "Retrieve the name of the policy")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The policy could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/policies/{policyId}/name",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.PolicyAdministration')")
  String getPolicyName(
      @Parameter(name = "policyId", description = "The ID for the policy", required = true)
          @PathVariable
          String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the policies.
   *
   * @param filter the filter to apply to the policy summaries
   * @param sortBy the method used to sort the policy summaries e.g. by name
   * @param sortDirection the sort direction to apply to the policy summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the policies
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the policy summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the policy summaries",
      description = "Retrieve the policy summaries")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/policy-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.PolicyAdministration')")
  PolicySummaries getPolicySummaries(
      @Parameter(name = "filter", description = "The filter to apply to the policy summaries")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the policy summaries e.g. by name")
          @RequestParam(value = "sortBy", required = false)
          PolicySortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the policy summaries")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve all the revoked tokens.
   *
   * @return the revoked tokens
   * @throws ServiceUnavailableException if the revoked tokens could not be retrieved
   */
  @Operation(
      summary = "Retrieve all the revoked tokens",
      description = "Retrieve all the revoked tokens")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/revoked-tokens",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  List<RevokedToken> getRevokedTokens() throws ServiceUnavailableException;

  /**
   * Retrieve the codes for the roles that have been assigned to the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @return the codes for the roles that have been assigned to the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws ServiceUnavailableException if the codes for the roles assigned to the group could not
   *     be retrieved
   */
  @Operation(
      summary = "Retrieve the codes for the roles that have been assigned to the group",
      description = "Retrieve the codes for the roles that have been assigned to the group")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}/role-codes",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  List<String> getRoleCodesForGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve all the roles.
   *
   * @return the roles
   * @throws ServiceUnavailableException if the roles could not be retrieved
   */
  @Operation(summary = "Retrieve all the roles", description = "Retrieve all the roles")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/roles",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  List<Role> getRoles() throws ServiceUnavailableException;

  /**
   * Retrieve the roles that have been assigned to the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @return the roles that have been assigned to the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws ServiceUnavailableException if the codes for the roles assigned to the group could not
   *     be retrieved
   */
  @Operation(
      summary = "Retrieve the roles that have been assigned to the group",
      description = "Retrieve the roles that have been assigned to the group")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}/roles",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  List<GroupRole> getRolesForGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the tenant could not be retrieved
   */
  @Operation(summary = "Retrieve the tenant", description = "Retrieve the tenant")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The tenant could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants/{tenantId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  Tenant getTenant(
      @Parameter(name = "tenantId", description = "The ID for the tenant", required = true)
          @PathVariable
          UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return the name of the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the name of the tenant could not be retrieved
   */
  @Operation(
      summary = "Retrieve the name of tenant",
      description = "Retrieve the name of the tenant")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The tenant could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants/{tenantId}/name",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  String getTenantName(
      @Parameter(name = "tenantId", description = "The ID for the tenant", required = true)
          @PathVariable
          UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the tenants.
   *
   * @param filter the filter to apply to the tenants
   * @param sortDirection the sort direction to apply to the tenants
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the tenants
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tenants could not be retrieved
   */
  @Operation(summary = "Retrieve the tenants", description = "Retrieve the tenants")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  Tenants getTenants(
      @Parameter(name = "filter", description = "The filter to apply to the tenants")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(name = "sortDirection", description = "The sort direction to apply to the tenants")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the tenants the user directory is associated with.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the tenants the user directory is associated with
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the tenants could not be retrieved for the user
   *     directory
   */
  @Operation(
      summary = "Retrieve the tenants the user directory is associated with",
      description = "Retrieve the tenants the user directory is associated with")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/tenants",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  ResponseEntity<List<Tenant>> getTenantsForUserDirectory(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the token.
   *
   * @param tokenId the ID for the token
   * @return the token
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TokenNotFoundException if the token could not be found
   * @throws ServiceUnavailableException if the token could not be retrieved
   */
  @Operation(summary = "Retrieve the token", description = "Retrieve the token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The token could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tokens/{tokenId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TokenAdministration')")
  Token getToken(
      @Parameter(name = "tokenId", description = "The ID for the token", required = true)
          @PathVariable
          String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the token.
   *
   * @param tokenId the ID for the token
   * @return the name of the token
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TokenNotFoundException if the token could not be found
   * @throws ServiceUnavailableException if the name of the token could not be retrieved
   */
  @Operation(summary = "Retrieve the name of token", description = "Retrieve the name of the token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The token could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tokens/{tokenId}/name",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TokenAdministration')")
  String getTokenName(
      @Parameter(name = "tokenId", description = "The ID for the token", required = true)
          @PathVariable
          String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the tokens.
   *
   * @param status the status filter to apply to the token summaries
   * @param filter the filter to apply to the token summaries
   * @param sortBy the method used to sort the token summaries e.g. by name
   * @param sortDirection the sort direction to apply to the token summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the tokens
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the token summaries could not be retrieved
   */
  @Operation(summary = "Retrieve the token summaries", description = "Retrieve the token summaries")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/token-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TokenAdministration')")
  TokenSummaries getTokenSummaries(
      @Parameter(name = "status", description = "The status filter to apply to the token summaries")
          @RequestParam(value = "status", required = false)
          TokenStatus status,
      @Parameter(name = "filter", description = "The filter to apply to the token summaries")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortBy",
              description = "The method used to sort the token summaries e.g. by name")
          @RequestParam(value = "sortBy", required = false)
          TokenSortBy sortBy,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the token summaries")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve all the tokens.
   *
   * @return the tokens
   * @throws ServiceUnavailableException if the tokens could not be retrieved
   */
  @Operation(summary = "Retrieve all the tokens", description = "Retrieve all the tokens")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tokens",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TokenAdministration')")
  List<Token> getTokens() throws ServiceUnavailableException;

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username for the user
   * @return the user
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the user could not be retrieved
   */
  @Operation(summary = "Retrieve the user", description = "Retrieve the user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/users/{username}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  User getUser(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "username", description = "The username for the user", required = true)
          @PathVariable
          String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the user directories.
   *
   * @param filter the filter to apply to the user directories
   * @param sortDirection the sort direction to apply to the user directories
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the user directories
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the user directories could not be retrieved
   */
  @Operation(
      summary = "Retrieve the user directories",
      description = "Retrieve the user directories")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration')")
  UserDirectories getUserDirectories(
      @Parameter(name = "filter", description = "The filter to apply to the user directories")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the user directories")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the user directories the tenant is associated with.
   *
   * @param tenantId the ID for the tenant
   * @return the user directories the tenant is associated with
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the user directories could not be retrieved for the
   *     tenant
   */
  @Operation(
      summary = "Retrieve the user directories the tenant is associated with",
      description = "Retrieve the user directories the tenant is associated with")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The tenant could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants/{tenantId}/user-directories",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.UserAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  ResponseEntity<List<UserDirectory>> getUserDirectoriesForTenant(
      @Parameter(name = "tenantId", description = "The ID for the tenant", required = true)
          @PathVariable
          UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be retrieved
   */
  @Operation(summary = "Retrieve the user directory", description = "Retrieve the user directory")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration')")
  UserDirectory getUserDirectory(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the capabilities the user directory supports.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the capabilities the user directory supports
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory capabilities could not be retrieved
   */
  @Operation(
      summary = "Retrieve the capabilities the user directory supports",
      description = "Retrieve the capabilities the user directory supports")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/capabilities",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration') or hasAccessToFunction('Security.UserAdministration') or hasAccessToFunction('Security.GroupAdministration') or hasAccessToFunction('Security.ResetUserPassword')")
  UserDirectoryCapabilities getUserDirectoryCapabilities(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the name of user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the name of the user directory could not be retrieved
   */
  @Operation(
      summary = "Retrieve the name of the user directory",
      description = "Retrieve the name of the user directory")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/name",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration')")
  String getUserDirectoryName(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the user directories.
   *
   * @param filter the filter to apply to the user directory summaries
   * @param sortDirection the sort direction to apply to the user directory summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the user directories
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the user directory summaries could not be retrieved
   */
  @Operation(
      summary = "Retrieve the summaries for the user directories",
      description = "Retrieve the summaries for the user directories")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directory-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration')")
  UserDirectorySummaries getUserDirectorySummaries(
      @Parameter(name = "filter", description = "The filter to apply to the user directories")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(
              name = "sortDirection",
              description = "The sort direction to apply to the user directories")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the user directories the tenant is associated with.
   *
   * @param tenantId the ID for the tenant
   * @return the summaries for the user directories the tenant is associated with
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the user directory summaries could not be retrieved for
   *     the tenant
   */
  @Operation(
      summary = "Retrieve the summaries for the user directories the tenant is associated with",
      description = "Retrieve the summaries for the user directories the tenant is associated with")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The tenant could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants/{tenantId}/user-directory-summaries",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.ResetUserPassword') or hasAccessToFunction('Security.UserAdministration') or hasAccessToFunction('Security.UserGroups')")
  ResponseEntity<List<UserDirectorySummary>> getUserDirectorySummariesForTenant(
      @Parameter(name = "tenantId", description = "The ID for the tenant", required = true)
          @PathVariable
          UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the user directory type for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the user directory type for the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserDirectoryTypeNotFoundException if the user directory type could not be found
   * @throws ServiceUnavailableException if the user directory type could not be retrieved for the
   *     user directory
   */
  @Operation(
      summary = "Retrieve the user directory type for the user directory",
      description = "Retrieve the user directory type for the user directory")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or user directory type could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/user-directory-type",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration') or hasAccessToFunction('Security.UserAdministration')")
  UserDirectoryType getUserDirectoryTypeForUserDirectory(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserDirectoryTypeNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the user directory types.
   *
   * @return the user directory types
   * @throws ServiceUnavailableException if the user directory types could not be retrieved
   */
  @Operation(
      summary = "Retrieve the user directory types",
      description = "Retrieve the user directory types")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directory-types",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration')")
  List<UserDirectoryType> getUserDirectoryTypes() throws ServiceUnavailableException;

  /**
   * Retrieve the name of the user.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username for the user
   * @return the name of the user
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the name of the user could not be retrieved
   */
  @Operation(
      summary = "Retrieve the name of the user",
      description = "Retrieve the name of the user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/users/{username}/name",
      method = RequestMethod.GET,
      produces = "text/plain")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.UserAdministration') or hasAccessToFunction('Security.ResetUserPassword')")
  String getUserName(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "username", description = "The username for the user", required = true)
          @PathVariable
          String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId the ID for the user directory
   * @param filter the filter to apply to the users
   * @param sortBy the method used to sort the users e.g. by name.
   * @param sortDirection the sort direction to apply to the users
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the users
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the users could not be retrieved
   */
  @Operation(summary = "Retrieve the users", description = "Retrieve the users")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/users",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.UserAdministration') or hasAccessToFunction('Security.ResetUserPassword')")
  Users getUsers(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "filter", description = "The filter to apply to the users")
          @RequestParam(value = "filter", required = false)
          String filter,
      @Parameter(name = "sortBy", description = "The method used to sort the users e.g. by name")
          @RequestParam(value = "sortBy", required = false)
          UserSortBy sortBy,
      @Parameter(name = "sortDirection", description = "The sort direction to apply to the users")
          @RequestParam(value = "sortDirection", required = false)
          SortDirection sortDirection,
      @Parameter(name = "pageIndex", description = "The page index", example = "0")
          @RequestParam(value = "pageIndex", required = false, defaultValue = "0")
          Integer pageIndex,
      @Parameter(name = "pageSize", description = "The page size", example = "10")
          @RequestParam(value = "pageSize", required = false, defaultValue = "10")
          Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException;

  /**
   * Reinstate the token.
   *
   * @param tokenId the ID for the token
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TokenNotFoundException if the token could not be found
   * @throws ServiceUnavailableException if the token could not be reinstated
   */
  @Operation(summary = "Reinstate the token", description = "Reinstate the token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The token was reinstated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The token could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tokens/{tokenId}/reinstate",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TokenAdministration')")
  void reinstateToken(
      @Parameter(name = "tokenId", description = "The ID for the token", required = true)
          @PathVariable
          String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException;

  /**
   * Remove the group member from the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param memberType the group member type
   * @param memberName the name of the group member
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws GroupMemberNotFoundException if the group member could not be found
   * @throws ServiceUnavailableException if the group member could not be removed from the group
   */
  @Operation(
      summary = "Remove the group member from the group",
      description = "Remove the group member from the group")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The group member was successfully removed from the group"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group or group member could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value =
          "/user-directories/{userDirectoryId}/groups/{groupName}/members/{memberType}/{memberName}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  void removeMemberFromGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName,
      @Parameter(name = "memberType", description = "The group member type", required = true)
          @PathVariable
          GroupMemberType memberType,
      @Parameter(name = "memberName", description = "The name of the group member", required = true)
          @PathVariable
          String memberName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          GroupMemberNotFoundException,
          ServiceUnavailableException;

  /**
   * Remove the role from the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param roleCode the code for the role
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws GroupRoleNotFoundException if the group role could not be found
   * @throws ServiceUnavailableException if the role could not be removed from the group
   */
  @Operation(
      summary = "Remove the role from the group",
      description = "Remove the role from the group")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The role was successfully removed from the group"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group or group role could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}/roles/{roleCode}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  void removeRoleFromGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName,
      @Parameter(name = "roleCode", description = "The code for the role", required = true)
          @PathVariable
          String roleCode)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          GroupRoleNotFoundException,
          ServiceUnavailableException;

  /**
   * Remove the user directory from the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param userDirectoryId the ID for the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws TenantUserDirectoryNotFoundException if the tenant user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be removed from the tenant
   */
  @Operation(
      summary = "Remove the user directory from the tenant",
      description = "Remove the user directory from the tenant")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The user directory was successfully removed from the tenant"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The tenant or tenant user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants/{tenantId}/user-directories/{userDirectoryId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  void removeUserDirectoryFromTenant(
      @Parameter(name = "tenantId", description = "The ID for the tenant", required = true)
          @PathVariable
          UUID tenantId,
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId)
      throws InvalidArgumentException,
          TenantNotFoundException,
          TenantUserDirectoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Initiate the password reset process for the user.
   *
   * @param username the username for the user
   * @param resetPasswordUrl the reset password URL
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the password reset could not be initiated
   */
  @Operation(
      summary = "Initiate the password reset process for the user",
      description = "Initiate the password reset process for the user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The password reset process was initiated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/users/{username}/reset-password",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void resetPassword(
      @Parameter(name = "username", description = "The username for the user", required = true)
          @PathVariable
          String username,
      @Parameter(name = "resetPasswordUrl", description = "The reset password URL")
          @RequestParam(value = "resetPasswordUrl")
          String resetPasswordUrl)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Revoke the token.
   *
   * @param tokenId the ID for the token
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TokenNotFoundException if the token could not be found
   * @throws ServiceUnavailableException if the token could not be revoked
   */
  @Operation(summary = "Revoke the token", description = "Revoke the token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The token was revoked successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The token could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tokens/{tokenId}/revoke",
      method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TokenAdministration')")
  void revokeToken(
      @Parameter(name = "tokenId", description = "The ID for the token", required = true)
          @PathVariable
          String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException;

  /**
   * Update the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param group the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws ServiceUnavailableException if the group could not be updated
   */
  @Operation(summary = "Update the group", description = "Update the group")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The group was updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or group could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/groups/{groupName}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration') or hasAccessToFunction('Security.GroupAdministration')")
  void updateGroup(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "groupName", description = "The name of the group", required = true)
          @PathVariable
          String groupName,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The group to update",
              required = true)
          @RequestBody
          Group group)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the policy.
   *
   * @param policyId the ID for the policy
   * @param policy the policy
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InvalidPolicyDataException the policy data is invalid
   * @throws PolicyDataMismatchException if the policy attributes do not match the policy data
   * @throws PolicyNotFoundException if the policy could not be found
   * @throws ServiceUnavailableException if the policy could not be updated
   */
  @Operation(summary = "Update the policy", description = "Update the policy")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The policy was updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument or policy data",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The policy could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/policies/{policyId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.PolicyAdministration')")
  void updatePolicy(
      @Parameter(name = "policyId", description = "The ID for the policy", required = true)
          @PathVariable
          String policyId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The policy to update",
              required = true)
          @RequestBody
          Policy policy)
      throws InvalidArgumentException,
          InvalidPolicyDataException,
          PolicyDataMismatchException,
          PolicyNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param tenant the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the tenant could not be updated
   */
  @Operation(summary = "Update the tenant", description = "Update the tenant")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The tenant was updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The tenant could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/tenants/{tenantId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.TenantAdministration')")
  void updateTenant(
      @Parameter(name = "tenantId", description = "The ID for the tenant", required = true)
          @PathVariable
          UUID tenantId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The tenant to update",
              required = true)
          @RequestBody
          Tenant tenant)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException;

  /**
   * Update the user.
   *
   * @param userDirectoryId the ID for the user directory
   * @param username the username for the user
   * @param user the user
   * @param expirePassword expire the user's password
   * @param lockUser lock the user
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the user could not be updated
   */
  @Operation(summary = "Update the user", description = "Update the user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The user was updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory or user could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}/users/{username}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void updateUser(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @Parameter(name = "username", description = "The username for the user", required = true)
          @PathVariable
          String username,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The user to update",
              required = true)
          @RequestBody
          User user,
      @Parameter(name = "expirePassword", description = "Expire the user's password")
          @RequestParam(value = "expirePassword", required = false)
          Boolean expirePassword,
      @Parameter(name = "lockUser", description = "Lock the user")
          @RequestParam(value = "lockUser", required = false)
          Boolean lockUser)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @param userDirectory the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be updated
   */
  @Operation(summary = "Update the user directory", description = "Update the user directory")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "The user directory was updated successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid argument",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "404",
            description = "The user directory could not be found",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class))),
        @ApiResponse(
            responseCode = "500",
            description =
                "An error has occurred and the request could not be processed at this time",
            content =
                @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetails.class)))
      })
  @RequestMapping(
      value = "/user-directories/{userDirectoryId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(
      "isSecurityDisabled() or hasRole('Administrator') or hasAccessToFunction('Security.UserDirectoryAdministration')")
  void updateUserDirectory(
      @Parameter(
              name = "userDirectoryId",
              description = "The ID for the user directory",
              required = true)
          @PathVariable
          UUID userDirectoryId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The user directory to update",
              required = true)
          @RequestBody
          UserDirectory userDirectory)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException;
}
