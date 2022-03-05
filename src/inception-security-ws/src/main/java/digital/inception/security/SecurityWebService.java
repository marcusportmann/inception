/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.security;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.ValidationError;
import digital.inception.core.sorting.SortDirection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.annotation.XmlElement;
import org.springframework.util.StringUtils;

//  For get functions use classes with this naming convention...
//
//      Users, Groups, Tenants, etc

/**
 * The <b>SecurityWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "SecurityService",
    name = "ISecurityService",
    targetNamespace = "http://inception.digital/security")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class SecurityWebService {

  /** The Security Service. */
  private final ISecurityService securityService;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <b>SecurityWebService</b>.
   *
   * @param securityService the Security Service
   * @param validator the JSR-303 validator
   */
  public SecurityWebService(ISecurityService securityService, Validator validator) {
    this.securityService = securityService;
    this.validator = validator;
  }

  /**
   * Add the group member to the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param memberType the group member type
   * @param memberName the name of the group member
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the group member could not be added to the group
   */
  @WebMethod(operationName = "AddMemberToGroup")
  public void addMemberToGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName,
      @WebParam(name = "MemberType") @XmlElement(required = true) GroupMemberType memberType,
      @WebParam(name = "MemberName") @XmlElement(required = true) String memberName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          UserNotFoundException, ServiceUnavailableException {
    securityService.addMemberToGroup(userDirectoryId, groupName, memberType, memberName);
  }

  /**
   * Add the role to the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param roleCode the code for the role
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws RoleNotFoundException if the role could not be found
   * @throws ServiceUnavailableException if the role could not be added to the group
   */
  @WebMethod(operationName = "AddRoleToGroup")
  public void addRoleToGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName,
      @WebParam(name = "RoleCode") @XmlElement(required = true) String roleCode)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          RoleNotFoundException, ServiceUnavailableException {
    securityService.addRoleToGroup(userDirectoryId, groupName, roleCode);
  }

  /**
   * Add the user directory to the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param userDirectoryId the ID for the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be added to the tenant
   */
  @WebMethod(operationName = "AddUserDirectoryToTenant")
  public void addUserDirectoryToTenant(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, TenantNotFoundException, UserDirectoryNotFoundException,
          ServiceUnavailableException {
    securityService.addUserDirectoryToTenant(tenantId, userDirectoryId);
  }

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
  @WebMethod(operationName = "AdminChangePassword")
  public void adminChangePassword(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "Username") @XmlElement(required = true) String username,
      @WebParam(name = "PasswordChange") @XmlElement(required = true) PasswordChange passwordChange)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          ServiceUnavailableException {
    if (passwordChange == null) {
      throw new InvalidArgumentException("passwordChange");
    }

    if (!StringUtils.hasText(passwordChange.getNewPassword())) {
      throw new InvalidArgumentException("passwordChange");
    }

    Set<ConstraintViolation<PasswordChange>> constraintViolations =
        validator.validate(passwordChange);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "passwordChange", ValidationError.toValidationErrors(constraintViolations));
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
  }

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
  @WebMethod(operationName = "ChangePassword")
  public void changePassword(
      @WebParam(name = "Username") @XmlElement(required = true) String username,
      @WebParam(name = "PasswordChange") @XmlElement(required = true) PasswordChange passwordChange)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          AuthenticationFailedException, InvalidSecurityCodeException, ExistingPasswordException,
          UserLockedException, ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    if (passwordChange == null) {
      throw new InvalidArgumentException("passwordChange");
    }

    Set<ConstraintViolation<PasswordChange>> constraintViolations =
        validator.validate(passwordChange);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "passwordChange", ValidationError.toValidationErrors(constraintViolations));
    }

    if (passwordChange.getReason() == PasswordChangeReason.ADMINISTRATIVE) {
      Optional<UUID> userDirectoryIdOptional = securityService.getUserDirectoryIdForUser(username);

      if (userDirectoryIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      } else {
        UUID userDirectoryId = userDirectoryIdOptional.get();

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

  /**
   * Create the new group.
   *
   * @param group the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws DuplicateGroupException if the group already exists
   * @throws ServiceUnavailableException if the group could not be created
   */
  @WebMethod(operationName = "CreateGroup")
  public void createGroup(@WebParam(name = "Group") @XmlElement(required = true) Group group)
      throws InvalidArgumentException, UserDirectoryNotFoundException, DuplicateGroupException,
          ServiceUnavailableException {
    securityService.createGroup(group);
  }

  /**
   * Create the new tenant.
   *
   * @param tenant the tenant to create
   * @param createUserDirectory should a new internal user directory be created for the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateTenantException if the tenant already exists
   * @throws ServiceUnavailableException if the tenant could not be created
   */
  @WebMethod(operationName = "CreateTenant")
  public void createTenant(
      @WebParam(name = "Tenant") @XmlElement(required = true) Tenant tenant,
      @WebParam(name = "CreateUserDirectory") @XmlElement(required = true)
          Boolean createUserDirectory)
      throws InvalidArgumentException, DuplicateTenantException, ServiceUnavailableException {
    securityService.createTenant(tenant, (createUserDirectory != null) && createUserDirectory);
  }

  /**
   * Create the new user.
   *
   * @param user the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked create the user locked
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws DuplicateUserException if the user already exists
   * @throws ServiceUnavailableException if the user could not be created
   */
  @WebMethod(operationName = "CreateUser")
  public void createUser(
      @WebParam(name = "User") @XmlElement(required = true) User user,
      @WebParam(name = "ExpiredPassword") @XmlElement(required = true) Boolean expiredPassword,
      @WebParam(name = "UserLocked") @XmlElement(required = true) Boolean userLocked)
      throws InvalidArgumentException, UserDirectoryNotFoundException, DuplicateUserException,
          ServiceUnavailableException {
    securityService.createUser(
        user, (expiredPassword != null) && expiredPassword, (userLocked != null) && userLocked);
  }

  /**
   * Create the new user directory.
   *
   * @param userDirectory the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateUserDirectoryException if the user directory already exists
   * @throws ServiceUnavailableException if the user directory could not be created
   */
  @WebMethod(operationName = "CreateUserDirectory")
  public void createUserDirectory(
      @WebParam(name = "UserDirectory") @XmlElement(required = true) UserDirectory userDirectory)
      throws InvalidArgumentException, DuplicateUserDirectoryException,
          ServiceUnavailableException {
    securityService.createUserDirectory(userDirectory);
  }

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
  @WebMethod(operationName = "DeleteGroup")
  public void deleteGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          ExistingGroupMembersException, ServiceUnavailableException {
    securityService.deleteGroup(userDirectoryId, groupName);
  }

  /**
   * Delete the tenant.
   *
   * @param tenantId the ID for the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the tenant could not be deleted
   */
  @WebMethod(operationName = "DeleteTenant")
  public void deleteTenant(@WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    securityService.deleteTenant(tenantId);
  }

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
  @WebMethod(operationName = "DeleteUser")
  public void deleteUser(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "Username") @XmlElement(required = true) String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          ServiceUnavailableException {
    securityService.deleteUser(userDirectoryId, username);
  }

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be deleted
   */
  @WebMethod(operationName = "DeleteUserDirectory")
  public void deleteUserDirectory(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    securityService.deleteUserDirectory(userDirectoryId);
  }

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
  @WebMethod(operationName = "GetGroup")
  @WebResult(name = "Group")
  public Group getGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          ServiceUnavailableException {
    return securityService.getGroup(userDirectoryId, groupName);
  }

  /**
   * Retrieve all the group names.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the group names
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the group names could not be retrieved
   */
  @WebMethod(operationName = "GetGroupNames")
  @WebResult(name = "GroupName")
  public List<String> getGroupNames(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return securityService.getGroupNames(userDirectoryId);
  }

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
  @WebMethod(operationName = "GetGroupNamesForUser")
  @WebResult(name = "GroupName")
  public List<String> getGroupNamesForUser(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "Username") @XmlElement(required = true) String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          ServiceUnavailableException {
    return securityService.getGroupNamesForUser(userDirectoryId, username);
  }

  /**
   * Retrieve the groups.
   *
   * @param userDirectoryId the ID for the user directory
   * @param filter the optional filter to apply to the groups
   * @param sortDirection the optional sort direction to apply to the groups
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the groups
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the groups could not be retrieved
   */
  @WebMethod(operationName = "GetGroups")
  @WebResult(name = "Groups")
  public Groups getGroups(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return securityService.getGroups(userDirectoryId, filter, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the group members for the group.
   *
   * @param userDirectoryId the ID for the user directory
   * @param groupName the name of the group
   * @param filter the optional filter to apply to the group members
   * @param sortDirection the optional sort direction to apply to the group members
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the group members for the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws ServiceUnavailableException if the group members could not be retrieved for the group
   */
  @WebMethod(operationName = "GetMembersForGroup")
  @WebResult(name = "GroupMembers")
  public GroupMembers getMembersForGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName,
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          ServiceUnavailableException {
    return securityService.getMembersForGroup(
        userDirectoryId, groupName, filter, sortDirection, pageIndex, pageSize);
  }

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
  @WebMethod(operationName = "GetRoleCodesForGroup")
  @WebResult(name = "RoleCode")
  public List<String> getRoleCodesForGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          ServiceUnavailableException {
    return securityService.getRoleCodesForGroup(userDirectoryId, groupName);
  }

  /**
   * Retrieve all the roles.
   *
   * @return the roles
   * @throws ServiceUnavailableException if the roles could not be retrieved
   */
  @WebMethod(operationName = "GetRoles")
  @WebResult(name = "Role")
  public List<Role> getRoles() throws ServiceUnavailableException {
    return securityService.getRoles();
  }

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
  @WebMethod(operationName = "GetRolesForGroup")
  @WebResult(name = "GroupRole")
  public List<GroupRole> getRolesForGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    return securityService.getRolesForGroup(userDirectoryId, groupName);
  }

  /**
   * Retrieve the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the tenant could not be retrieved
   */
  @WebMethod(operationName = "GetTenant")
  @WebResult(name = "Tenant")
  public Tenant getTenant(@WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    return securityService.getTenant(tenantId);
  }

  /**
   * Retrieve the name of the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return the name of the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the name of the tenant could not be retrieved
   */
  @WebMethod(operationName = "GetTenantName")
  @WebResult(name = "TenantName")
  public String getTenantName(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    return securityService.getTenantName(tenantId);
  }

  /**
   * Retrieve the tenants.
   *
   * @param filter the optional filter to apply to the tenants
   * @param sortDirection the optional sort direction to apply to the tenants
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the tenants
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the tenants could not be retrieved
   */
  @WebMethod(operationName = "GetTenants")
  @WebResult(name = "Tenant")
  public Tenants getTenants(
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return securityService.getTenants(filter, sortDirection, pageIndex, pageSize);
  }

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
  @WebMethod(operationName = "GetTenantsForUserDirectory")
  @WebResult(name = "Tenant")
  public List<Tenant> getTenantsForUserDirectory(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return securityService.getTenantsForUserDirectory(userDirectoryId);
  }

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
  @WebMethod(operationName = "GetUser")
  @WebResult(name = "User")
  public User getUser(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "Username") @XmlElement(required = true) String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          ServiceUnavailableException {
    User user = securityService.getUser(userDirectoryId, username);

    // Remove the password information
    user.setPassword(null);
    user.setPasswordAttempts(0);
    user.setPasswordExpiry(null);

    return user;
  }

  /**
   * Retrieve the user directories.
   *
   * @param filter the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the user directories
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the user directories could not be retrieved
   */
  @WebMethod(operationName = "GetUserDirectories")
  @WebResult(name = "UserDirectories")
  public UserDirectories getUserDirectories(
      @WebParam(name = "Filter") @XmlElement(required = true) String filter,
      @WebParam(name = "SortDirection") @XmlElement(required = true) SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement(required = true) Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement(required = true) Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return securityService.getUserDirectories(filter, sortDirection, pageIndex, pageSize);
  }

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
  @WebMethod(operationName = "GetUserDirectoriesForTenant")
  @WebResult(name = "UserDirectory")
  public List<UserDirectory> getUserDirectoriesForTenant(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    return securityService.getUserDirectoriesForTenant(tenantId);
  }

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be retrieved
   */
  @WebMethod(operationName = "GetUserDirectory")
  @WebResult(name = "UserDirectory")
  public UserDirectory getUserDirectory(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return securityService.getUserDirectory(userDirectoryId);
  }

  /**
   * Retrieve the capabilities the user directory supports.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the capabilities the user directory supports
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory capabilities could not be retrieved
   */
  @WebMethod(operationName = "GetUserDirectoryCapabilities")
  @WebResult(name = "UserDirectoryCapabilities")
  public UserDirectoryCapabilities getUserDirectoryCapabilities(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return securityService.getUserDirectoryCapabilities(userDirectoryId);
  }

  /**
   * Retrieve the name of the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   * @return the name of user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the name of the user directory could not be retrieved
   */
  @WebMethod(operationName = "GetUserDirectoryName")
  @WebResult(name = "UserDirectoryName")
  public String getUserDirectoryName(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return securityService.getUserDirectoryName(userDirectoryId);
  }

  /**
   * Retrieve the summaries for the user directories.
   *
   * @param filter the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the summaries for the user directories
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the user directory summaries could not be retrieved
   */
  @WebMethod(operationName = "GetUserDirectorySummaries")
  @WebResult(name = "UserDirectorySummaries")
  public UserDirectorySummaries getUserDirectorySummaries(
      @WebParam(name = "Filter") @XmlElement(required = true) String filter,
      @WebParam(name = "SortDirection") @XmlElement(required = true) SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement(required = true) Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement(required = true) Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return securityService.getUserDirectorySummaries(filter, sortDirection, pageIndex, pageSize);
  }

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
  @WebMethod(operationName = "GetUserDirectorySummariesForTenant")
  @WebResult(name = "UserDirectorySummary")
  public List<UserDirectorySummary> getUserDirectorySummariesForTenant(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    return securityService.getUserDirectorySummariesForTenant(tenantId);
  }

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
  @WebMethod(operationName = "GetUserDirectoryTypeForUserDirectory")
  @WebResult(name = "UserDirectoryType")
  public UserDirectoryType getUserDirectoryTypeForUserDirectory(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException,
          UserDirectoryTypeNotFoundException, ServiceUnavailableException {
    return securityService.getUserDirectoryTypeForUserDirectory(userDirectoryId);
  }

  /**
   * Retrieve the user directory types.
   *
   * @return the user directory types
   * @throws ServiceUnavailableException if the user directory types could not be retrieved
   */
  @WebMethod(operationName = "GetUserDirectoryTypes")
  @WebResult(name = "UserDirectoryType")
  public List<UserDirectoryType> getUserDirectoryTypes() throws ServiceUnavailableException {
    return securityService.getUserDirectoryTypes();
  }

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
  @WebMethod(operationName = "GetUserName")
  @WebResult(name = "UserName")
  public String getUserName(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "Username") @XmlElement(required = true) String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          ServiceUnavailableException {
    return securityService.getUserName(userDirectoryId, username);
  }

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId the ID for the user directory
   * @param filter the optional filter to apply to the users
   * @param sortBy The optional method used to sort the users e.g. by name.
   * @param sortDirection the optional sort direction to apply to the users
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the users
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the users could not be retrieved
   */
  @WebMethod(operationName = "GetUsers")
  @WebResult(name = "Users")
  public Users getUsers(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortBy") @XmlElement UserSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    return securityService.getUsers(
        userDirectoryId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

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
  @WebMethod(operationName = "RemoveMemberFromGroup")
  public void removeMemberFromGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName,
      @WebParam(name = "MemberType") @XmlElement(required = true) GroupMemberType memberType,
      @WebParam(name = "MemberName") @XmlElement(required = true) String memberName)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          GroupMemberNotFoundException, ServiceUnavailableException {
    securityService.removeMemberFromGroup(userDirectoryId, groupName, memberType, memberName);
  }

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
  @WebMethod(operationName = "RemoveRoleFromGroup")
  public void removeRoleFromGroup(
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId,
      @WebParam(name = "GroupName") @XmlElement(required = true) String groupName,
      @WebParam(name = "RoleCode") @XmlElement(required = true) String roleCode)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          GroupRoleNotFoundException, ServiceUnavailableException {
    securityService.removeRoleFromGroup(userDirectoryId, groupName, roleCode);
  }

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
  @WebMethod(operationName = "RemoveUserDirectoryFromTenant")
  public void removeUserDirectoryFromTenant(
      @WebParam(name = "TenantId") @XmlElement(required = true) UUID tenantId,
      @WebParam(name = "UserDirectoryId") @XmlElement(required = true) UUID userDirectoryId)
      throws InvalidArgumentException, TenantNotFoundException,
          TenantUserDirectoryNotFoundException, ServiceUnavailableException {
    securityService.removeUserDirectoryFromTenant(tenantId, userDirectoryId);
  }

  /**
   * Initiate the password reset process for the user.
   *
   * @param username the username for the user
   * @param resetPasswordUrl the reset password URL
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the password reset could not be initiated
   */
  @WebMethod(operationName = "ResetPassword")
  public void resetPassword(
      @WebParam(name = "Username") @XmlElement(required = true) String username,
      @WebParam(name = "ResetPasswordUrl") @XmlElement(required = true) String resetPasswordUrl)
      throws InvalidArgumentException, UserNotFoundException, ServiceUnavailableException {
    securityService.initiatePasswordReset(username, resetPasswordUrl, true);
  }

  /**
   * Update the group.
   *
   * @param group the group
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws GroupNotFoundException if the group could not be found
   * @throws ServiceUnavailableException if the group could not be updated
   */
  @WebMethod(operationName = "UpdateGroup")
  public void updateGroup(@WebParam(name = "Group") @XmlElement(required = true) Group group)
      throws InvalidArgumentException, UserDirectoryNotFoundException, GroupNotFoundException,
          ServiceUnavailableException {
    securityService.updateGroup(group);
  }

  /**
   * Update the tenant.
   *
   * @param tenant the tenant
   * @throws InvalidArgumentException if an argument is invalid
   * @throws TenantNotFoundException if the tenant could not be found
   * @throws ServiceUnavailableException if the tenant could not be updated
   */
  @WebMethod(operationName = "UpdateTenant")
  public void updateTenant(@WebParam(name = "Tenant") @XmlElement(required = true) Tenant tenant)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    securityService.updateTenant(tenant);
  }

  /**
   * Update the user.
   *
   * @param user the user
   * @param expirePassword expire the user's password
   * @param lockUser lock the user
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws UserNotFoundException if the user could not be found
   * @throws ServiceUnavailableException if the user could not be updated
   */
  @WebMethod(operationName = "UpdateUser")
  public void updateUser(
      @WebParam(name = "User") @XmlElement(required = true) User user,
      @WebParam(name = "ExpirePassword") @XmlElement(required = true) boolean expirePassword,
      @WebParam(name = "LockUser") @XmlElement(required = true) boolean lockUser)
      throws InvalidArgumentException, UserDirectoryNotFoundException, UserNotFoundException,
          ServiceUnavailableException {
    securityService.updateUser(user, expirePassword, lockUser);
  }

  /**
   * Update the user directory.
   *
   * @param userDirectory the user directory
   * @throws InvalidArgumentException if an argument is invalid
   * @throws UserDirectoryNotFoundException if the user directory could not be found
   * @throws ServiceUnavailableException if the user directory could not be updated
   */
  @WebMethod(operationName = "UpdateUserDirectory")
  public void updateUserDirectory(
      @WebParam(name = "UserDirectory") @XmlElement(required = true) UserDirectory userDirectory)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    securityService.updateUserDirectory(userDirectory);
  }
}
