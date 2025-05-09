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

import digital.inception.api.SecureApiController;
import digital.inception.core.api.ApiUtil;
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
import digital.inception.security.model.PasswordChangeReason;
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
import digital.inception.security.service.SecurityService;
import digital.inception.security.service.SecurityServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code SecurityApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class SecurityApiControllerImpl extends SecureApiController
    implements SecurityApiController {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(SecurityApiControllerImpl.class);

  /** The Security Service. */
  private final SecurityService securityService;

  /**
   * Constructs a new {@code SecurityApiControllerImpl}.
   *
   * @param applicationContext the Spring application context
   * @param securityService the Security Service
   */
  public SecurityApiControllerImpl(
      ApplicationContext applicationContext, SecurityService securityService) {
    super(applicationContext);

    this.securityService = securityService;
  }

  @Override
  public void addMemberToGroup(UUID userDirectoryId, String groupName, GroupMember groupMember)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (groupMember == null) {
      throw new InvalidArgumentException("groupMember");
    }

    if (!userDirectoryId.equals(groupMember.getUserDirectoryId())) {
      throw new InvalidArgumentException("groupMember");
    }

    if (!groupName.equals(groupMember.getGroupName())) {
      throw new InvalidArgumentException("groupMember");
    }

    validateArgument("groupMember", groupMember);

    securityService.addMemberToGroup(
        userDirectoryId, groupName, groupMember.getMemberType(), groupMember.getMemberName());
  }

  @Override
  public void addRoleToGroup(UUID userDirectoryId, String groupName, GroupRole groupRole)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          RoleNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (groupRole == null) {
      throw new InvalidArgumentException("groupRole");
    }

    if (!userDirectoryId.equals(groupRole.getUserDirectoryId())) {
      throw new InvalidArgumentException("groupRole");
    }

    if (!groupName.equals(groupRole.getGroupName())) {
      throw new InvalidArgumentException("groupRole");
    }

    validateArgument("groupRole", groupRole);

    if (groupRole.getRoleCode().equalsIgnoreCase(SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE)
        && (!hasRole(SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE))) {
      throw new AccessDeniedException(
          "Insufficient authority to add the "
              + SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE
              + " role to the group ("
              + groupName
              + ") for the user directory ("
              + userDirectoryId
              + ")");
    }

    securityService.addRoleToGroup(userDirectoryId, groupName, groupRole.getRoleCode());
  }

  @Override
  public void addUserDirectoryToTenant(UUID tenantId, TenantUserDirectory tenantUserDirectory)
      throws InvalidArgumentException,
          TenantNotFoundException,
          UserDirectoryNotFoundException,
          ServiceUnavailableException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (tenantUserDirectory == null) {
      throw new InvalidArgumentException("tenantUserDirectory");
    }

    if (!tenantId.equals(tenantUserDirectory.getTenantId())) {
      throw new InvalidArgumentException("tenantUserDirectory");
    }

    validateArgument("tenantUserDirectory", tenantUserDirectory);

    if (!tenantUserDirectory.getTenantId().equals(tenantId)) {
      throw new InvalidArgumentException("tenantId");
    }

    securityService.addUserDirectoryToTenant(tenantId, tenantUserDirectory.getUserDirectoryId());
  }

  @Override
  public void adminChangePassword(
      UUID userDirectoryId, String username, PasswordChange passwordChange)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    if (passwordChange == null) {
      throw new InvalidArgumentException("passwordChange");
    }

    if (!StringUtils.hasText(passwordChange.getNewPassword())) {
      throw new InvalidArgumentException("passwordChange");
    }

    validateArgument("passwordChange", passwordChange);

    securityService.adminChangePassword(
        userDirectoryId,
        username,
        passwordChange.getNewPassword(),
        passwordChange.getExpirePassword() != null && passwordChange.getExpirePassword(),
        passwordChange.getLockUser() != null && passwordChange.getLockUser(),
        passwordChange.getResetPasswordHistory() != null
            && passwordChange.getResetPasswordHistory(),
        passwordChange.getReason());
  }

  @Override
  public void changePassword(String username, PasswordChange passwordChange)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          AuthenticationFailedException,
          InvalidSecurityCodeException,
          ExistingPasswordException,
          UserLockedException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    if (passwordChange == null) {
      throw new InvalidArgumentException("passwordChange");
    }

    if (!StringUtils.hasText(passwordChange.getNewPassword())) {
      throw new InvalidArgumentException("passwordChange");
    }

    validateArgument("passwordChange", passwordChange);

    if (passwordChange.getReason() == PasswordChangeReason.ADMINISTRATIVE) {
      if (isSecurityDisabled()
          || hasRole(SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE)
          || hasAccessToFunction("Security.TenantAdministration")
          || hasAccessToFunction("Security.UserAdministration")
          || hasAccessToFunction("Security.ResetUserPassword")) {

        Optional<UUID> userDirectoryIdOptional =
            securityService.getUserDirectoryIdForUser(username);

        if (userDirectoryIdOptional.isEmpty()) {
          throw new UserNotFoundException(username);
        }

        UUID userDirectoryId = userDirectoryIdOptional.get();

        if (isSecurityEnabled() && (!hasAccessToUserDirectory(userDirectoryId))) {
          throw new AccessDeniedException(
              "Access denied to the user directory (" + userDirectoryId + ")");
        }

        securityService.adminChangePassword(
            userDirectoryId,
            username,
            passwordChange.getNewPassword(),
            passwordChange.getExpirePassword() != null && passwordChange.getExpirePassword(),
            passwordChange.getLockUser() != null && passwordChange.getLockUser(),
            passwordChange.getResetPasswordHistory() != null
                && passwordChange.getResetPasswordHistory(),
            passwordChange.getReason());
      } else {
        throw new AccessDeniedException(
            "Insufficient access to change the password for the user (" + username + ")");
      }
    } else if (passwordChange.getReason() == PasswordChangeReason.USER) {
      if (!StringUtils.hasText(passwordChange.getPassword())) {
        throw new InvalidArgumentException("passwordChange");
      }

      securityService.changePassword(
          username, passwordChange.getPassword(), passwordChange.getNewPassword());
    } else if (passwordChange.getReason() == PasswordChangeReason.RESET) {
      if (!StringUtils.hasText(passwordChange.getSecurityCode())) {
        throw new InvalidArgumentException("passwordChange");
      }

      securityService.resetPassword(
          username, passwordChange.getNewPassword(), passwordChange.getSecurityCode());
    }
  }

  @Override
  public void createGroup(UUID userDirectoryId, Group group)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          DuplicateGroupException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    if (group == null) {
      throw new InvalidArgumentException("group");
    }

    if (!userDirectoryId.equals(group.getUserDirectoryId())) {
      throw new InvalidArgumentException("group");
    }

    securityService.createGroup(group);
  }

  @Override
  public void createPolicy(Policy policy)
      throws InvalidArgumentException,
          InvalidPolicyDataException,
          DuplicatePolicyException,
          PolicyDataMismatchException,
          ServiceUnavailableException {
    securityService.createPolicy(policy);
  }

  @Override
  public void createTenant(Tenant tenant, Boolean createUserDirectory)
      throws InvalidArgumentException, DuplicateTenantException, ServiceUnavailableException {
    securityService.createTenant(tenant, (createUserDirectory != null) && createUserDirectory);
  }

  @Override
  public void createUser(
      UUID userDirectoryId, User user, Boolean expiredPassword, Boolean userLocked)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          DuplicateUserException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    if (user == null) {
      throw new InvalidArgumentException("user");
    }

    if (!userDirectoryId.equals(user.getUserDirectoryId())) {
      throw new InvalidArgumentException("user");
    }

    securityService.createUser(
        user, (expiredPassword != null) && expiredPassword, (userLocked != null) && userLocked);
  }

  @Override
  public void createUserDirectory(UserDirectory userDirectory)
      throws InvalidArgumentException,
          DuplicateUserDirectoryException,
          ServiceUnavailableException {
    securityService.createUserDirectory(userDirectory);
  }

  @Override
  public void deleteGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ExistingGroupMembersException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    securityService.deleteGroup(userDirectoryId, groupName);
  }

  @Override
  public void deletePolicy(String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException {
    securityService.deletePolicy(policyId);
  }

  @Override
  public void deleteTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    securityService.deleteTenant(tenantId);
  }

  @Override
  public void deleteToken(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    securityService.deleteToken(tokenId);
  }

  @Override
  public void deleteUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    securityService.deleteUser(userDirectoryId, username);
  }

  @Override
  public void deleteUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException,
          ExistingGroupsException,
          ExistingUsersException,
          UserDirectoryNotFoundException,
          ServiceUnavailableException {
    securityService.deleteUserDirectory(userDirectoryId);
  }

  @Override
  public Token generateToken(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The request to generate the token",
              required = true)
          @RequestBody
          GenerateTokenRequest generateTokenRequest)
      throws InvalidArgumentException, ServiceUnavailableException {
    return securityService.generateToken(generateTokenRequest);
  }

  @Override
  public Group getGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getGroup(userDirectoryId, groupName);
  }

  @Override
  public List<String> getGroupNames(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getGroupNames(userDirectoryId);
  }

  @Override
  public List<String> getGroupNamesForUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getGroupNamesForUser(userDirectoryId, username);
  }

  @Override
  public Groups getGroups(
      UUID userDirectoryId,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getGroups(userDirectoryId, filter, sortDirection, pageIndex, pageSize);
  }

  @Override
  public GroupMembers getMembersForGroup(
      UUID userDirectoryId,
      String groupName,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getMembersForGroup(
        userDirectoryId, groupName, filter, sortDirection, pageIndex, pageSize);
  }

  @Override
  public List<Policy> getPolicies() throws ServiceUnavailableException {
    return securityService.getPolicies();
  }

  @Override
  public Policy getPolicy(String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(policyId)) {
      throw new InvalidArgumentException("policyId");
    }

    return securityService.getPolicy(policyId);
  }

  @Override
  public String getPolicyName(String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(securityService.getPolicyName(policyId));
  }

  @Override
  public PolicySummaries getPolicySummaries(
      String filter,
      PolicySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return securityService.getPolicySummaries(filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public List<RevokedToken> getRevokedTokens() throws ServiceUnavailableException {
    return securityService.getRevokedTokens();
  }

  @Override
  public List<String> getRoleCodesForGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getRoleCodesForGroup(userDirectoryId, groupName);
  }

  @Override
  public List<Role> getRoles() throws ServiceUnavailableException {
    return securityService.getRoles();
  }

  @Override
  public List<GroupRole> getRolesForGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getRolesForGroup(userDirectoryId, groupName);
  }

  @Override
  public Tenant getTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    return securityService.getTenant(tenantId);
  }

  @Override
  public String getTenantName(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(securityService.getTenantName(tenantId));
  }

  @Override
  public Tenants getTenants(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return securityService.getTenants(filter, sortDirection, pageIndex, pageSize);
  }

  @Override
  public ResponseEntity<List<Tenant>> getTenantsForUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (isSecurityEnabled() && (!hasAccessToUserDirectory(userDirectoryId))) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    List<Tenant> tenants = securityService.getTenantsForUserDirectory(userDirectoryId);

    return new ResponseEntity<>(tenants, HttpStatus.OK);
  }

  @Override
  public Token getToken(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    return securityService.getToken(tokenId);
  }

  @Override
  public String getTokenName(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(securityService.getTokenName(tokenId));
  }

  @Override
  public TokenSummaries getTokenSummaries(
      TokenStatus status,
      String filter,
      TokenSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (status == null) {
      status = TokenStatus.ALL;
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return securityService.getTokenSummaries(
        status, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public List<Token> getTokens() throws ServiceUnavailableException {
    return securityService.getTokens();
  }

  @Override
  public User getUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    //noinspection StatementWithEmptyBody
    if (hasRole("Administrator")) {
      // Administrators always have access to retrieve a user's details
    } else if (isSecurityDisabled()
        || hasAccessToFunction("Security.TenantAdministration")
        || hasAccessToFunction("Security.UserAdministration")
        || hasAccessToFunction("Security.ResetUserPassword")) {
      if (isSecurityEnabled() && (!hasAccessToUserDirectory(userDirectoryId))) {
        throw new AccessDeniedException(
            "Access denied to the user directory (" + userDirectoryId + ")");
      }
    } else //noinspection StatementWithEmptyBody
    if ((authentication != null) && (authentication.getName().equalsIgnoreCase(username))) {
      // Users can retrieve their own details
    } else {
      throw new AccessDeniedException("Access denied to the user (" + username + ")");
    }

    User user = securityService.getUser(userDirectoryId, username);

    // Remove the password information
    user.setPassword(null);
    user.setPasswordAttempts(0);
    user.setPasswordExpiry(null);

    return user;
  }

  @Override
  public UserDirectories getUserDirectories(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return securityService.getUserDirectories(filter, sortDirection, pageIndex, pageSize);
  }

  @Override
  public ResponseEntity<List<UserDirectory>> getUserDirectoriesForTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    List<UserDirectory> userDirectories = securityService.getUserDirectoriesForTenant(tenantId);

    if (hasRole(SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE)
        || hasAccessToFunction("Security.TenantAdministration")) {
      return new ResponseEntity<>(userDirectories, HttpStatus.OK);
    } else {
      List<UserDirectory> filteredUserDirectories = new ArrayList<>();

      for (UserDirectory userDirectory : userDirectories) {
        if (hasAccessToUserDirectory(userDirectory.getId())) {
          filteredUserDirectories.add(userDirectory);
        }
      }

      return new ResponseEntity<>(filteredUserDirectories, HttpStatus.OK);
    }
  }

  @Override
  public UserDirectory getUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return securityService.getUserDirectory(userDirectoryId);
  }

  @Override
  public UserDirectoryCapabilities getUserDirectoryCapabilities(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getUserDirectoryCapabilities(userDirectoryId);
  }

  @Override
  public String getUserDirectoryName(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(securityService.getUserDirectoryName(userDirectoryId));
  }

  @Override
  public UserDirectorySummaries getUserDirectorySummaries(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return securityService.getUserDirectorySummaries(filter, sortDirection, pageIndex, pageSize);
  }

  @Override
  public ResponseEntity<List<UserDirectorySummary>> getUserDirectorySummariesForTenant(
      UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    List<UserDirectorySummary> userDirectorySummaries =
        securityService.getUserDirectorySummariesForTenant(tenantId);

    if (hasRole("Administrator") || hasAccessToFunction("Security.TenantAdministration")) {
      return new ResponseEntity<>(userDirectorySummaries, HttpStatus.OK);
    } else {
      List<UserDirectorySummary> filteredUserDirectorySummaries = new ArrayList<>();

      for (UserDirectorySummary userDirectorySummary : userDirectorySummaries) {
        if (hasAccessToUserDirectory(userDirectorySummary.getId())) {
          filteredUserDirectorySummaries.add(userDirectorySummary);
        }
      }

      return new ResponseEntity<>(filteredUserDirectorySummaries, HttpStatus.OK);
    }
  }

  @Override
  public UserDirectoryType getUserDirectoryTypeForUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserDirectoryTypeNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getUserDirectoryTypeForUserDirectory(userDirectoryId);
  }

  @Override
  public List<UserDirectoryType> getUserDirectoryTypes() throws ServiceUnavailableException {
    return securityService.getUserDirectoryTypes();
  }

  @Override
  public String getUserName(UUID userDirectoryId, String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return ApiUtil.quote(securityService.getUserName(userDirectoryId, username));
  }

  @Override
  public Users getUsers(
      UUID userDirectoryId,
      String filter,
      UserSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    return securityService.getUsers(
        userDirectoryId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public void reinstateToken(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    securityService.reinstateToken(tokenId);
  }

  @Override
  public void removeMemberFromGroup(
      UUID userDirectoryId, String groupName, GroupMemberType memberType, String memberName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          GroupMemberNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    securityService.removeMemberFromGroup(userDirectoryId, groupName, memberType, memberName);
  }

  @Override
  public void removeRoleFromGroup(UUID userDirectoryId, String groupName, String roleCode)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          GroupRoleNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    if (roleCode.equalsIgnoreCase(SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE)
        && (!hasRole(SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE))) {
      throw new AccessDeniedException(
          "Insufficient authority to remove the "
              + SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE
              + " role from the group ("
              + groupName
              + ") for the user directory ("
              + userDirectoryId
              + ")");
    }

    securityService.removeRoleFromGroup(userDirectoryId, groupName, roleCode);
  }

  @Override
  public void removeUserDirectoryFromTenant(UUID tenantId, UUID userDirectoryId)
      throws InvalidArgumentException,
          TenantNotFoundException,
          TenantUserDirectoryNotFoundException,
          ServiceUnavailableException {
    securityService.removeUserDirectoryFromTenant(tenantId, userDirectoryId);
  }

  @Override
  public void resetPassword(String username, String resetPasswordUrl)
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      securityService.initiatePasswordReset(username, resetPasswordUrl, true);
    } catch (UserNotFoundException ignored) {
      // Swallow exception to prevent disclosing whether a user exists
    }
  }

  @Override
  public void revokeToken(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    securityService.revokeToken(tokenId);
  }

  @Override
  public void updateGroup(UUID userDirectoryId, String groupName, Group group)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (!hasAccessToUserDirectory(userDirectoryId)) {
      throw new AccessDeniedException(
          "Access denied to the user directory (" + userDirectoryId + ")");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (group == null) {
      throw new InvalidArgumentException("group");
    }

    if (!groupName.equals(group.getName())) {
      throw new InvalidArgumentException("group");
    }

    securityService.updateGroup(group);
  }

  @Override
  public void updatePolicy(String policyId, Policy policy)
      throws InvalidArgumentException,
          InvalidPolicyDataException,
          PolicyDataMismatchException,
          PolicyNotFoundException,
          ServiceUnavailableException {
    if (policyId == null) {
      throw new InvalidArgumentException("policyId");
    }

    if (policy == null) {
      throw new InvalidArgumentException("policy");
    }

    if (!policyId.equals(policy.getId())) {
      throw new InvalidArgumentException("policy");
    }

    securityService.updatePolicy(policy);
  }

  @Override
  public void updateTenant(UUID tenantId, Tenant tenant)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (tenant == null) {
      throw new InvalidArgumentException("tenant");
    }

    if (!tenantId.equals(tenant.getId())) {
      throw new InvalidArgumentException("tenant");
    }

    securityService.updateTenant(tenant);
  }

  @Override
  public void updateUser(
      UUID userDirectoryId, String username, User user, Boolean expirePassword, Boolean lockUser)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    // Apply access control
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (isSecurityEnabled()) {
      //noinspection StatementWithEmptyBody
      if (hasRole("Administrator")) {
        // Administrators always have access to retrieve a user's details
      } else if (hasAccessToFunction("Security.TenantAdministration")
          || hasAccessToFunction("Security.UserAdministration")) {
        if (!hasAccessToUserDirectory(userDirectoryId)) {
          throw new AccessDeniedException(
              "Access denied to the user directory (" + userDirectoryId + ")");
        }
      } else //noinspection StatementWithEmptyBody
      if ((authentication != null) && (authentication.getName().equalsIgnoreCase(username))) {
        // Users can retrieve their own details
      } else {
        throw new AccessDeniedException("Access denied to the user (" + username + ")");
      }
    }

    if (user == null) {
      throw new InvalidArgumentException("user");
    }

    if (!username.equals(user.getUsername())) {
      throw new InvalidArgumentException("user");
    }

    securityService.updateUser(
        user, (expirePassword != null) && expirePassword, (lockUser != null) && lockUser);
  }

  @Override
  public void updateUserDirectory(UUID userDirectoryId, UserDirectory userDirectory)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (userDirectory == null) {
      throw new InvalidArgumentException("userDirectory");
    }

    if (!userDirectoryId.equals(userDirectory.getId())) {
      throw new InvalidArgumentException("userDirectory");
    }

    securityService.updateUserDirectory(userDirectory);
  }

  /**
   * Confirm that the user associated with the authenticated request has access to the user
   * directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return {@code true} if the user associated with the authenticated request has access to the
   *     user directory or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   */
  protected boolean hasAccessToUserDirectory(UUID userDirectoryId) throws InvalidArgumentException {
    if (isSecurityEnabled()) {
      if (userDirectoryId == null) {
        throw new InvalidArgumentException("userDirectoryId");
      }

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      // Could not retrieve the currently authenticated principal
      if (authentication == null) {
        return false;
      }

      try {
        // If the user is not authenticated then they cannot have access
        if (!authentication.isAuthenticated()) {
          return false;
        }

        // If the user has the "Administrator" role they always have access
        if (hasRole(SecurityServiceImpl.ADMINISTRATOR_ROLE_CODE)) {
          return true;
        }

        List<UUID> userDirectoryIdsForUser = new ArrayList<>();

        List<UUID> tenantIds = getUUIDValuesForAuthoritiesWithPrefix(authentication, "TENANT_");

        for (UUID tenantId : tenantIds) {
          var userDirectoryIdsForTenant = securityService.getUserDirectoryIdsForTenant(tenantId);

          userDirectoryIdsForUser.addAll(userDirectoryIdsForTenant);
        }

        return userDirectoryIdsForUser.stream().anyMatch(userDirectoryId::equals);
      } catch (Throwable e) {
        log.error(
            "Failed to check if the user ("
                + authentication.getName()
                + ") has access to the user directory ("
                + userDirectoryId
                + ")",
            e);
        return false;
      }
    } else {
      return true;
    }
  }
}
