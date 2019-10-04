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

package digital.inception.security;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.persistence.IDGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>SecurityService</code> class provides the Security Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SecurityService
  implements ISecurityService, InitializingBean
{
  /**
   * The name of the Administrators group.
   */
  public static final String ADMINISTRATORS_GROUP_NAME = "Administrators";

  /**
   * The name of the Administrator role.
   */
  public static final String ADMINISTRATOR_ROLE_NAME = "Administrator";

  /**
   * The ID used to uniquely identify the Administrators group.
   */
  public static final UUID ADMINISTRATORS_GROUP_ID = UUID.fromString(
      "a9e01fa2-f017-46e2-8187-424bf50a4f33");

  /**
   * The ID used to uniquely identify the Administration user directory.
   */
  public static final UUID ADMINISTRATION_USER_DIRECTORY_ID = UUID.fromString(
      "4ef18395-423a-4df6-b7d7-6bcdd85956e4");

  /**
   * The ID used to uniquely identify the internal user directory type.
   */
  public static final String INTERNAL_USER_DIRECTORY_TYPE = "InternalUserDirectory";

  /**
   * The ID used to uniquely identify the LDAP user directory type.
   */
  public static final String LDAP_USER_DIRECTORY_TYPE = "LDAPUserDirectory";

  /**
   * The maximum number of filtered organizations.
   */
  private static final int MAX_FILTERED_ORGANISATIONS = 100;

  /**
   * The maximum number of filtered user directories.
   */
  private static final int MAX_FILTERED_USER_DIRECTORIES = 100;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
  private Map<UUID, IUserDirectory> userDirectories = new ConcurrentHashMap<>();
  private Map<String, UserDirectoryType> userDirectoryTypes = new ConcurrentHashMap<>();

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The ID generator.
   */
  private IDGenerator idGenerator;

  /**
   * The User Directory Repository.
   */
  private UserDirectoryRepository userDirectoryRepository;

  /**
   * The User Repository.
   */
  private UserRepository userRepository;

  /**
   * The Function Repository.
   */
  private FunctionRepository functionRepository;

  /**
   * The Organization Repository.
   */
  private OrganizationRepository organizationRepository;

  /**
   * The User Directory Type Repository.
   */
  private UserDirectoryTypeRepository userDirectoryTypeRepository;

  /**
   * The Group repository.
   */
  private GroupRepository groupRepository;

  /**
   * The User Directory Summary Repository.
   */
  private UserDirectorySummaryRepository userDirectorySummaryRepository;

  /**
   * Constructs a new <code>SecurityService</code>.
   *
   * @param applicationContext             the Spring application context
   * @param idGenerator                    the ID generator
   * @param functionRepository             the Function Repository
   * @param groupRepository                the Group Repository
   * @param organizationRepository         the Organization Repository
   * @param userDirectoryRepository        the User Directory Repository
   * @param userDirectorySummaryRepository the User Directory Summary Repository
   * @param userDirectoryTypeRepository    the User Directory Type Repository
   * @param userRepository                 the User Repository
   */
  public SecurityService(ApplicationContext applicationContext, IDGenerator idGenerator,
      FunctionRepository functionRepository, GroupRepository groupRepository,
      OrganizationRepository organizationRepository,
      UserDirectoryRepository userDirectoryRepository,
      UserDirectorySummaryRepository userDirectorySummaryRepository,
      UserDirectoryTypeRepository userDirectoryTypeRepository, UserRepository userRepository)
  {
    this.applicationContext = applicationContext;
    this.idGenerator = idGenerator;

    this.functionRepository = functionRepository;
    this.groupRepository = groupRepository;
    this.organizationRepository = organizationRepository;
    this.userDirectoryRepository = userDirectoryRepository;
    this.userDirectorySummaryRepository = userDirectorySummaryRepository;
    this.userDirectoryTypeRepository = userDirectoryTypeRepository;
    this.userRepository = userRepository;
  }

  /**
   * Add the user to the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the security group uniquely identifying the security group
   */
  @Override
  @Transactional
  public void addUserToGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.addUserToGroup(username, groupName);
  }

  /**
   * Administratively change the password for the user.
   *
   * @param userDirectoryId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                             the user directory
   * @param username             the username identifying the user
   * @param newPassword          the new password
   * @param expirePassword       expire the user's password
   * @param lockUser             lock the user
   * @param resetPasswordHistory reset the user's password history
   * @param reason               the reason for changing the password
   */
  @Override
  @Transactional
  public void adminChangePassword(UUID userDirectoryId, String username, String newPassword,
      boolean expirePassword, boolean lockUser, boolean resetPasswordHistory,
      PasswordChangeReason reason)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.adminChangePassword(username, newPassword, expirePassword, lockUser,
        resetPasswordHistory, reason);
  }

  /**
   * Initialize the Security Service.
   */
  @Override
  public void afterPropertiesSet()
  {
    try
    {
      // Load the user directory types
      reloadUserDirectoryTypes();

      // Load the user directories
      reloadUserDirectories();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialize the Security Service", e);
    }
  }

  /**
   * Authenticate the user.
   *
   * @param username the username identifying the user
   * @param password the password being used to authenticate
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  @Override
  @Transactional
  public UUID authenticate(String username, String password)
    throws AuthenticationFailedException, UserLockedException, ExpiredPasswordException,
        UserNotFoundException, SecurityServiceException
  {
    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        IUserDirectory internalUserDirectory = userDirectories.get(internalUserDirectoryId);

        if (internalUserDirectory == null)
        {
          throw new SecurityServiceException("The user directory ID (" + internalUserDirectoryId
              + ") for the internal user (" + username + ") is invalid");
        }
        else
        {
          internalUserDirectory.authenticate(username, password);

          return internalUserDirectoryId;
        }
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if one of them can authenticate this
         * user.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                userDirectory.authenticate(username, password);

                return userDirectoryId;
              }
            }
          }
        }

        throw new UserNotFoundException(username);
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExpiredPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to authenticate the user (" + username + ")", e);
    }
  }

  /**
   * Change the password for the user.
   *
   * @param username    the username identifying the user
   * @param password    the password for the user that is used to authorise the operation
   * @param newPassword the new password
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  @Override
  @Transactional
  public UUID changePassword(String username, String password, String newPassword)
    throws AuthenticationFailedException, UserLockedException, UserNotFoundException,
        ExistingPasswordException, SecurityServiceException
  {
    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        IUserDirectory internalUserDirectory = userDirectories.get(internalUserDirectoryId);

        if (internalUserDirectory == null)
        {
          throw new SecurityServiceException("The user directory ID (" + internalUserDirectoryId
              + ") for the internal user (" + username + ") is invalid");
        }
        else
        {
          internalUserDirectory.changePassword(username, password, newPassword);

          return internalUserDirectoryId;
        }
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if one of them can change the
         * password for this user.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                userDirectory.changePassword(username, password, newPassword);

                return userDirectoryId;
              }
            }
          }
        }

        throw new UserNotFoundException(username);
      }
    }
    catch (AuthenticationFailedException | UserNotFoundException | UserLockedException
        | ExistingPasswordException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to change the password for the user (" + username
          + ")", e);
    }
  }

  /**
   * Create the new authorised function.
   *
   * @param function the function
   */
  @Override
  @Transactional
  public void createFunction(Function function)
    throws DuplicateFunctionException, SecurityServiceException

  {
    try
    {
      if (functionRepository.existsById(function.getCode()))
      {
        throw new DuplicateFunctionException(function.getCode());
      }

      functionRepository.saveAndFlush(function);
    }
    catch (DuplicateFunctionException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to create the function (" + function.getCode()
          + ")", e);
    }
  }

  /**
   * Create the new security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the security group
   */
  @Override
  @Transactional
  public void createGroup(UUID userDirectoryId, Group group)
    throws UserDirectoryNotFoundException, DuplicateGroupException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.createGroup(group);
  }

  /**
   * Create the new organization.
   *
   * @param organization        the organization
   * @param createUserDirectory should a new internal user directory be created for the organization
   *
   * @return the new internal user directory that was created for the organization or
   *         <code>null</code> if no user directory was created
   */
  @Override
  @Transactional
  public UserDirectory createOrganization(Organization organization, boolean createUserDirectory)
    throws DuplicateOrganizationException, SecurityServiceException
  {
    UserDirectory userDirectory = null;

    try
    {
      if (organizationRepository.existsById(organization.getId()))
      {
        throw new DuplicateOrganizationException(organization.getId());
      }

      if (organizationRepository.existsByNameIgnoreCase(organization.getName()))
      {
        throw new DuplicateOrganizationException(organization.getName());
      }

      if (createUserDirectory)
      {
        userDirectory = newInternalUserDirectoryForOrganization(organization);

        organization.linkUserDirectory(userDirectory);
      }

      organizationRepository.saveAndFlush(organization);

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }

      return userDirectory;
    }
    catch (DuplicateOrganizationException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to create the organization ("
          + organization.getId() + ")", e);
    }
  }

  /**
   * Create the new user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expiredPassword create the user with its password expired
   * @param userLocked      create the user locked
   */
  @Override
  @Transactional
  public void createUser(UUID userDirectoryId, User user, boolean expiredPassword,
      boolean userLocked)
    throws UserDirectoryNotFoundException, DuplicateUserException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.createUser(user, expiredPassword, userLocked);
  }

  /**
   * Create the new user directory.
   *
   * @param userDirectory the user directory
   */
  @Override
  @Transactional
  public void createUserDirectory(UserDirectory userDirectory)
    throws DuplicateUserDirectoryException, SecurityServiceException
  {
    try
    {
      if (userDirectoryRepository.existsById(userDirectory.getId()))
      {
        throw new DuplicateUserDirectoryException(userDirectory.getId());
      }

      if (userDirectoryRepository.existsByNameIgnoreCase(userDirectory.getName()))
      {
        throw new DuplicateUserDirectoryException(userDirectory.getName());
      }

      userDirectoryRepository.saveAndFlush(userDirectory);

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }
    }
    catch (DuplicateUserDirectoryException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to create the user directory ("
          + userDirectory.getName() + ")", e);
    }
  }

  /**
   * Delete the authorised function.
   *
   * @param functionCode the code used to uniquely identify the function
   */
  @Override
  @Transactional
  public void deleteFunction(String functionCode)
    throws FunctionNotFoundException, SecurityServiceException
  {
    try
    {
      if (!functionRepository.existsById(functionCode))
      {
        throw new FunctionNotFoundException(functionCode);
      }

      functionRepository.deleteById(functionCode);
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to delete the function (" + functionCode + ")", e);
    }
  }

  /**
   * Delete the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the security group uniquely identifying the security group
   */
  @Override
  @Transactional
  public void deleteGroup(UUID userDirectoryId, String groupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, ExistingGroupMembersException,
        SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.deleteGroup(groupName);
  }

  /**
   * Delete the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   */
  @Override
  @Transactional
  public void deleteOrganization(UUID organizationId)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    try
    {
      if (!organizationRepository.existsById(organizationId))
      {
        throw new OrganizationNotFoundException(organizationId);
      }

      organizationRepository.deleteById(organizationId);
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to delete the organization (" + organizationId
          + ")", e);
    }
  }

  /**
   * Delete the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   */
  @Override
  @Transactional
  public void deleteUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.deleteUser(username);
  }

  /**
   * Delete the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   */
  @Override
  @Transactional
  public void deleteUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    try
    {
      if (!userDirectoryRepository.existsById(userDirectoryId))
      {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      userDirectoryRepository.deleteById(userDirectoryId);

      try
      {
        reloadUserDirectories();
      }
      catch (Throwable e)
      {
        logger.error("Failed to reload the user directories", e);
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to delete the user directory (" + userDirectoryId
          + ")", e);
    }
  }

  /**
   * Retrieve the users matching the attribute criteria.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param attributes      the attribute criteria used to select the users
   *
   * @return the users whose attributes match the attribute criteria
   */
  @Override
  public List<User> findUsers(UUID userDirectoryId, List<Attribute> attributes)
    throws UserDirectoryNotFoundException, InvalidAttributeException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.findUsers(attributes);
  }

  /**
   * Retrieve the authorised function.
   *
   * @param functionCode the code used to uniquely identify the function
   *
   * @return the authorised function
   */
  @Override
  public Function getFunction(String functionCode)
    throws FunctionNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Function> functionOptional = functionRepository.findById(functionCode);

      if (functionOptional.isPresent())
      {
        return functionOptional.get();
      }
      else
      {
        throw new FunctionNotFoundException(functionCode);
      }
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the function (" + functionCode + ")",
          e);
    }
  }

  /**
   * Retrieve the authorised function codes for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the authorised function codes for the user
   */
  @Override
  public List<String> getFunctionCodesForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getFunctionCodesForUser(username);
  }

  /**
   * Retrieve all the authorised functions.
   *
   * @return the authorised functions
   */
  @Override
  public List<Function> getFunctions()
    throws SecurityServiceException
  {
    try
    {
      return functionRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the functions", e);
    }
  }

  /**
   * Retrieve the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param groupName       the name of the security group uniquely identifying the security group
   *
   * @return the security group
   */
  @Override
  public Group getGroup(UUID userDirectoryId, String groupName)
    throws UserDirectoryNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroup(groupName);
  }

  /**
   * Retrieve the security group names for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the security group names for the user
   */
  @Override
  public List<String> getGroupNamesForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroupNamesForUser(username);
  }

  /**
   * Retrieve all the security groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the security groups
   */
  @Override
  public List<Group> getGroups(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroups();
  }

  /**
   * Retrieve the security groups for the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   *
   * @return the security groups for the user
   */
  @Override
  public List<Group> getGroupsForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroupsForUser(username);
  }

  /**
   * Retrieve the number of security groups
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the number of security groups
   */
  @Override
  public long getNumberOfGroups(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getNumberOfGroups();
  }

  /**
   * Retrieve the number of organizations
   *
   * @return the number of organizations
   */
  @Override
  public long getNumberOfOrganizations()
    throws SecurityServiceException
  {
    return getNumberOfOrganizations(null);
  }

  /**
   * Retrieve the number of organizations
   *
   * @param filter the optional filter to apply to the organizations
   *
   * @return the number of organizations
   */
  @Override
  public long getNumberOfOrganizations(String filter)
    throws SecurityServiceException
  {
    try
    {
      if (StringUtils.isEmpty(filter))
      {
        return organizationRepository.count();
      }
      else
      {
        return organizationRepository.countByNameContainingIgnoreCase("%" + filter.toUpperCase()
            + "%");
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the number of organizations", e);
    }
  }

  /**
   * Retrieve the number of user directories
   *
   * @return the number of user directories
   */
  @Override
  public long getNumberOfUserDirectories()
    throws SecurityServiceException
  {
    return getNumberOfUserDirectories(null);
  }

  /**
   * Retrieve the number of user directories
   *
   * @param filter the optional filter to apply to the user directories
   *
   * @return the number of user directories
   */
  @Override
  public long getNumberOfUserDirectories(String filter)
    throws SecurityServiceException
  {
    try
    {
      if (StringUtils.isEmpty(filter))
      {
        return userDirectoryRepository.count();
      }
      else
      {
        return userDirectoryRepository.countByNameContainingIgnoreCase("%" + filter.toUpperCase()
            + "%");
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the number of user directories", e);
    }
  }

  /**
   * Retrieve the number of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @return the number of users
   */
  @Override
  public long getNumberOfUsers(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    return getNumberOfUsers(userDirectoryId, null);
  }

  /**
   * Retrieve the number of users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * @param filter          the optional filter to apply to the users
   *
   * @return the number of users
   */
  @Override
  public long getNumberOfUsers(UUID userDirectoryId, String filter)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getNumberOfUsers(filter);
  }

  /**
   * Retrieve the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return the organization
   */
  @Override
  public Organization getOrganization(UUID organizationId)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);

      if (organizationOptional.isPresent())
      {
        return organizationOptional.get();
      }
      else
      {
        throw new OrganizationNotFoundException(organizationId);
      }
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the organization (" + organizationId
          + ")", e);
    }
  }

  /**
   * Retrieve the IDs used to uniquely identify the organizations the user directory is associated
   * with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @return the IDs used to uniquely identify the organizations the user directory is associated
   *         with
   */
  @Override
  public List<UUID> getOrganizationIdsForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    try
    {
      if (!userDirectoryRepository.existsById(userDirectoryId))
      {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      return userDirectoryRepository.getOrganizationIdsById(userDirectoryId);
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the IDs for the organizations for the user directory ("
          + userDirectoryId + ")", e);
    }
  }

  /**
   * Retrieve the organizations.
   *
   * @return the organizations
   */
  @Override
  public List<Organization> getOrganizations()
    throws SecurityServiceException
  {
    try
    {
      return organizationRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the organizations", e);
    }
  }

  /**
   * Retrieve the organizations.
   *
   * @param filter        the optional filter to apply to the organizations
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the organizations
   */
  @Override
  public List<Organization> getOrganizations(String filter, SortDirection sortDirection,
      Integer pageIndex, Integer pageSize)
    throws SecurityServiceException
  {
    PageRequest pageRequest;

    if ((pageIndex != null) && (pageSize != null))
    {
      pageRequest = PageRequest.of(pageIndex, pageSize);
    }
    else
    {
      pageRequest = PageRequest.of(0, MAX_FILTERED_ORGANISATIONS);
    }

    try
    {
      if (StringUtils.isEmpty(filter))
      {
        if (sortDirection == SortDirection.ASCENDING)
        {
          return organizationRepository.findAllByOrderByNameAsc(pageRequest);
        }
        else
        {
          return organizationRepository.findAllByOrderByNameDesc(pageRequest);
        }
      }
      else
      {
        if (sortDirection == SortDirection.ASCENDING)
        {
          return organizationRepository.findByNameContainingIgnoreCaseOrderByNameAsc(filter,
              pageRequest);
        }
        else
        {
          return organizationRepository.findByNameContainingIgnoreCaseOrderByNameDesc(filter,
              pageRequest);
        }
      }
    }
    catch (Throwable e)
    {
      String message = "Failed to retrieve the organizations";

      if (!StringUtils.isEmpty(filter))
      {
        message += String.format(" matching the filter \"%s\"", filter);
      }

      if ((pageIndex != null) && (pageSize != null))
      {
        message += " for the page " + pageIndex + " using the page size " + pageSize;
      }

      message += ": ";

      message += e.getMessage();

      throw new SecurityServiceException(message, e);
    }
  }

  /**
   * Retrieve the organizations the user directory is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @return the organizations the user directory is associated with
   */
  @Override
  public List<Organization> getOrganizationsForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    try
    {
      if (!userDirectoryRepository.existsById(userDirectoryId))
      {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      return organizationRepository.findAllByUserDirectoryId(userDirectoryId);
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the organizations associated with the user directory ("
          + userDirectoryId + ")", e);
    }
  }

  /**
   * Retrieve the codes for the roles that the user has been assigned.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * @param username        the username identifying the user
   *
   * @return the codes for the roles that the user has been assigned
   */
  @Override
  public List<String> getRoleCodesForUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getRoleCodesForUser(username);
  }

  /**
   * Retrieve the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * @param username        the username identifying the user
   *
   * @return the user
   */
  @Override
  public User getUser(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUser(username);
  }

  /**
   * Retrieve the user directories.
   *
   * @return the user directories
   */
  @Override
  public List<UserDirectory> getUserDirectories()
    throws SecurityServiceException
  {
    try
    {
      return userDirectoryRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the user directories", e);
    }
  }

  /**
   * Retrieve the user directories.
   *
   * @param filter        the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the user directories
   */
  @Override
  public List<UserDirectory> getUserDirectories(String filter, SortDirection sortDirection,
      Integer pageIndex, Integer pageSize)
    throws SecurityServiceException
  {
    PageRequest pageRequest;

    if ((pageIndex != null) && (pageSize != null))
    {
      pageRequest = PageRequest.of(pageIndex, pageSize);
    }
    else
    {
      pageRequest = PageRequest.of(0, MAX_FILTERED_USER_DIRECTORIES);
    }

    try
    {
      if (StringUtils.isEmpty(filter))
      {
        if (sortDirection == SortDirection.ASCENDING)
        {
          return userDirectoryRepository.findAllByOrderByNameAsc(pageRequest);
        }
        else
        {
          return userDirectoryRepository.findAllByOrderByNameDesc(pageRequest);
        }
      }
      else
      {
        if (sortDirection == SortDirection.ASCENDING)
        {
          return userDirectoryRepository.findByNameContainingIgnoreCaseOrderByNameAsc(filter,
              pageRequest);
        }
        else
        {
          return userDirectoryRepository.findByNameContainingIgnoreCaseOrderByNameDesc(filter,
              pageRequest);
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the filtered user directories", e);
    }
  }

  /**
   * Retrieve the user directories the organization is associated with.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return the user directories the organization is associated with
   */
  @Override
  public List<UserDirectory> getUserDirectoriesForOrganization(UUID organizationId)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    try
    {
      if (!organizationRepository.existsById(organizationId))
      {
        throw new OrganizationNotFoundException(organizationId);
      }

      return userDirectoryRepository.findAllByOrganizationId(organizationId);
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the user directories associated with the organization ("
          + organizationId + ")", e);
    }
  }

  /**
   * Retrieve the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the user directory
   */
  @Override
  public UserDirectory getUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    try
    {
      Optional<UserDirectory> userDirectoryOptional = userDirectoryRepository.findById(
          userDirectoryId);

      if (userDirectoryOptional.isPresent())
      {
        return userDirectoryOptional.get();
      }
      else
      {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the user directory ("
          + userDirectoryId + ")", e);
    }
  }

  /**
   * Retrieve the Universally Unique Identifier (UUID) used to uniquely identify the user directory that the user with the specified
   * username is associated with.
   *
   * @param username the username identifying the user
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory that the user with the specified
   *         username is associated with or <code>null</code> if the user cannot be found
   */
  @Override
  public UUID getUserDirectoryIdForUser(String username)
    throws SecurityServiceException
  {
    try
    {
      // First check if this is an internal user and if so determine the user directory ID
      UUID internalUserDirectoryId = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryId != null)
      {
        return internalUserDirectoryId;
      }
      else
      {
        /*
         * Check all of the "external" user directories to see if the user is associated with one
         * of them.
         */
        for (UUID userDirectoryId : userDirectories.keySet())
        {
          IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null)
          {
            if (!(userDirectory instanceof InternalUserDirectory))
            {
              if (userDirectory.isExistingUser(username))
              {
                return userDirectoryId;
              }
            }
          }
        }

        return null;
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the user directory ID for the user ("
          + username + ")", e);
    }
  }

  /**
   * Retrieve the IDs used to uniquely identify the user directories the organization is associated
   * with.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the organization
   *
   * @return the IDs used to uniquely identify the user directories the organization is associated
   *         with
   */
  @Override
  public List<UUID> getUserDirectoryIdsForOrganization(UUID organizationId)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    try
    {
      if (!organizationRepository.existsById(organizationId))
      {
        throw new OrganizationNotFoundException(organizationId);
      }

      return organizationRepository.getUserDirectoryIdsById(organizationId);
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the IDs for the user directories associated with the organization ("
          + organizationId + ")", e);
    }
  }

  /**
   * Retrieve the summaries for the user directories.
   *
   * @param filter        the optional filter to apply to the user directories
   * @param sortDirection the optional sort direction to apply to the user directories
   * @param pageIndex     the optional page index
   * @param pageSize      the optional page size
   *
   * @return the summaries for the user directories
   */
  @Override
  public List<UserDirectorySummary> getUserDirectorySummaries(String filter,
      SortDirection sortDirection, Integer pageIndex, Integer pageSize)
    throws SecurityServiceException
  {
    PageRequest pageRequest;

    if ((pageIndex != null) && (pageSize != null))
    {
      pageRequest = PageRequest.of(pageIndex, pageSize);
    }
    else
    {
      pageRequest = PageRequest.of(0, MAX_FILTERED_USER_DIRECTORIES);
    }

    try
    {
      if (StringUtils.isEmpty(filter))
      {
        if (sortDirection == SortDirection.ASCENDING)
        {
          return userDirectorySummaryRepository.findAllByOrderByNameAsc(pageRequest);
        }
        else
        {
          return userDirectorySummaryRepository.findAllByOrderByNameDesc(pageRequest);
        }
      }
      else
      {
        if (sortDirection == SortDirection.ASCENDING)
        {
          return userDirectorySummaryRepository.findByNameContainingIgnoreCaseOrderByNameAsc(
              filter, pageRequest);
        }
        else
        {
          return userDirectorySummaryRepository.findByNameContainingIgnoreCaseOrderByNameDesc(
              filter, pageRequest);
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the filtered summaries for the user directories", e);
    }
  }

  /**
   * Retrieve the summaries for the user directories the organization is associated with.
   *
   * @param organizationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organization
   *
   * @return the summaries for the user directories the organization is associated with
   */
  @Override
  public List<UserDirectorySummary> getUserDirectorySummariesForOrganization(UUID organizationId)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    try
    {
      if (!organizationRepository.existsById(organizationId))
      {
        throw new OrganizationNotFoundException(organizationId);
      }

      return userDirectorySummaryRepository.findAllByOrganizationId(organizationId);
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the summaries for the user "
          + "directories associated with the organization (" + organizationId + ")", e);
    }
  }

  /**
   * Retrieve the user directory type for the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   *
   * @return the user directory type for the user directory
   */
  @Override
  public UserDirectoryType getUserDirectoryTypeForUserDirectory(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, UserDirectoryTypeNotFoundException,
        SecurityServiceException
  {
    try
    {
      Optional<String> typeOptional = userDirectoryRepository.getTypeForUserDirectoryById(
          userDirectoryId);

      if (typeOptional.isEmpty())
      {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      Optional<UserDirectoryType> userDirectoryTypeOptional = userDirectoryTypeRepository.findById(
          typeOptional.get());

      if (userDirectoryTypeOptional.isPresent())
      {
        return userDirectoryTypeOptional.get();
      }
      else
      {
        throw new UserDirectoryTypeNotFoundException(typeOptional.get());
      }
    }
    catch (UserDirectoryNotFoundException | UserDirectoryTypeNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
        "Failed to retrieve the user directory type for the user directory (" + userDirectoryId +
          ")", e);
    }
  }

  /**
   * Retrieve the user directory types.
   *
   * @return the user directory types
   */
  @Override
  public List<UserDirectoryType> getUserDirectoryTypes()
    throws SecurityServiceException
  {
    try
    {
      return userDirectoryTypeRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to retrieve the user directory types", e);
    }
  }

  /**
   * Retrieve all the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @return the users
   */
  @Override
  public List<User> getUsers(UUID userDirectoryId)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUsers();
  }

  /**
   * Retrieve the users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * @param filter          the optional filter to apply to the users
   * @param sortBy          the optional method used to sort the users e.g. by last name
   * @param sortDirection   the optional sort direction to apply to the users
   * @param pageIndex       the optional page index
   * @param pageSize        the optional page size
   *
   * @return the users
   */
  @Override
  public List<User> getUsers(UUID userDirectoryId, String filter, UserSortBy sortBy,
      SortDirection sortDirection, Integer pageIndex, Integer pageSize)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUsers(filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  /**
   * Is the user in the security group?
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * @param username        the username identifying the user
   * @param groupName       the name of the security group uniquely identifying the security group
   *
   * @return <code>true</code> if the user is a member of the security group or <code>false</code>
   *         otherwise
   */
  @Override
  public boolean isUserInGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.isUserInGroup(username, groupName);
  }

  /**
   * Reload the user directories.
   */
  @Override
  public void reloadUserDirectories()
    throws SecurityServiceException
  {
    try
    {
      Map<UUID, IUserDirectory> reloadedUserDirectories = new ConcurrentHashMap<>();

      List<UserDirectoryType> userDirectoryTypes = getUserDirectoryTypes();

      for (UserDirectory userDirectory : getUserDirectories())
      {
        UserDirectoryType userDirectoryType;

        userDirectoryType = userDirectoryTypes.stream().filter(
            possibleUserDirectoryType -> possibleUserDirectoryType.getCode().equals(
            userDirectory.getType())).findFirst().orElse(null);

        if (userDirectoryType == null)
        {
          logger.error("Failed to load the user directory (" + userDirectory.getId()
              + "): The user directory type (" + userDirectory.getType() + ") was not loaded");

          continue;
        }

        try
        {
          Class<?> clazz = userDirectoryType.getUserDirectoryClass();

          if (!IUserDirectory.class.isAssignableFrom(clazz))
          {
            throw new SecurityServiceException("The user directory class ("
                + userDirectoryType.getUserDirectoryClassName()
                + ") does not implement the IUserDirectory interface");
          }

          Class<? extends IUserDirectory> userDirectoryClass = clazz.asSubclass(
              IUserDirectory.class);

          Constructor<? extends IUserDirectory> userDirectoryClassConstructor =
              userDirectoryClass.getConstructor(UUID.class, List.class, GroupRepository.class,
              UserRepository.class);

          if (userDirectoryClassConstructor == null)
          {
            throw new SecurityServiceException("The user directory class ("
                + userDirectoryType.getUserDirectoryClassName()
                + ") does not provide a valid constructor (long, Map<String,String>)");
          }

          IUserDirectory userDirectoryInstance = userDirectoryClassConstructor.newInstance(
              userDirectory.getId(), userDirectory.getParameters(), groupRepository,
              userRepository);

          applicationContext.getAutowireCapableBeanFactory().autowireBean(userDirectoryInstance);

          reloadedUserDirectories.put(userDirectory.getId(), userDirectoryInstance);
        }
        catch (Throwable e)
        {
          throw new SecurityServiceException("Failed to initialize the user directory ("
              + userDirectory.getId() + ")(" + userDirectory.getName() + ")", e);
        }
      }

      this.userDirectories = reloadedUserDirectories;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to reload the user directories", e);
    }
  }

  /**
   * Remove the user from the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * @param username        the username identifying the user
   * @param groupName       the security group name
   */
  @Override
  @Transactional
  public void removeUserFromGroup(UUID userDirectoryId, String username, String groupName)
    throws UserDirectoryNotFoundException, UserNotFoundException, GroupNotFoundException,
        SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.removeUserFromGroup(username, groupName);
  }

  /**
   * Does the user directory support administering security groups.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @return <code>true</code> if the user directory supports administering security groups or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean supportsGroupAdministration(UUID userDirectoryId)
    throws UserDirectoryNotFoundException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.supportsGroupAdministration();
  }

  /**
   * Does the user directory support administering users.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *
   * @return <code>true</code> if the user directory supports administering users or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean supportsUserAdministration(UUID userDirectoryId)
    throws UserDirectoryNotFoundException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.supportsUserAdministration();
  }

  /**
   * Update the authorised function.
   *
   * @param function the function
   */
  @Override
  @Transactional
  public void updateFunction(Function function)
    throws FunctionNotFoundException, SecurityServiceException
  {
    try
    {
      if (!functionRepository.existsById(function.getCode()))
      {
        throw new FunctionNotFoundException(function.getCode());
      }

      functionRepository.saveAndFlush(function);
    }
    catch (FunctionNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to update the function (" + function.getCode()
          + ")", e);
    }
  }

  /**
   * Update the security group.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param group           the security group
   */
  @Override
  @Transactional
  public void updateGroup(UUID userDirectoryId, Group group)
    throws UserDirectoryNotFoundException, GroupNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.updateGroup(group);
  }

  /**
   * Update the organization.
   *
   * @param organization the organization
   */
  @Override
  @Transactional
  public void updateOrganization(Organization organization)
    throws OrganizationNotFoundException, SecurityServiceException
  {
    try
    {
      if (!organizationRepository.existsById(organization.getId()))
      {
        throw new OrganizationNotFoundException(organization.getId());
      }

      organizationRepository.saveAndFlush(organization);
    }
    catch (OrganizationNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to update the organization ("
          + organization.getId() + ")", e);
    }
  }

  /**
   * Update the user.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param user            the user
   * @param expirePassword  expire the user's password as part of the update
   * @param lockUser        lock the user as part of the update
   */
  @Override
  @Transactional
  public void updateUser(UUID userDirectoryId, User user, boolean expirePassword, boolean lockUser)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityServiceException
  {
    IUserDirectory userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null)
    {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.updateUser(user, expirePassword, lockUser);
  }

  /**
   * Update the user directory.
   *
   * @param userDirectory the user directory
   */
  @Override
  @Transactional
  public void updateUserDirectory(UserDirectory userDirectory)
    throws UserDirectoryNotFoundException, SecurityServiceException
  {
    try
    {
      if (!userDirectoryRepository.existsById(userDirectory.getId()))
      {
        throw new UserDirectoryNotFoundException(userDirectory.getId());
      }

      userDirectoryRepository.saveAndFlush(userDirectory);
    }
    catch (UserDirectoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to update the user directory ("
          + userDirectory.getName() + ")", e);
    }
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the internal user
   * directory the internal user with the specified username is associated with.
   *
   * @param username the username uniquely identifying the internal user
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the internal user
   *         directory the internal user with the specified username is associated with or
   *         <code>null</code> if an internal user with the specified username could not be found
   */
  private UUID getInternalUserDirectoryIdForUser(String username)
    throws SecurityServiceException
  {
    try
    {
      Optional<UUID> userDirectoryIdOptional =
          userRepository.getUserDirectoryIdByUsernameIgnoreCase(username);

      return userDirectoryIdOptional.orElse(null);
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to retrieve the ID for the internal user directory for the internal user ("
          + username + ")", e);
    }
  }

  /**
   * Checks whether the specified value is <code>null</code> or blank.
   *
   * @param value the value to check
   *
   * @return true if the value is <code>null</code> or blank
   */
  private boolean isNullOrEmpty(Object value)
  {
    if (value == null)
    {
      return true;
    }

    if (value instanceof String)
    {
      return ((String) value).length() == 0;
    }

    return false;
  }

  private UserDirectory newInternalUserDirectoryForOrganization(Organization organization)
    throws SecurityServiceException
  {
    UserDirectory userDirectory = new UserDirectory();

    userDirectory.setId(idGenerator.nextUUID());
    userDirectory.setType("InternalUserDirectory");
    userDirectory.setName(organization.getName() + " Internal User Directory");

    String buffer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE userDirectory "
        + "SYSTEM \"UserDirectoryConfiguration.dtd\"><userDirectory>"
        + "<parameter><name>MaxPasswordAttempts</name><value>5</value></parameter>"
        + "<parameter><name>PasswordExpiryMonths</name><value>12</value></parameter>"
        + "<parameter><name>PasswordHistoryMonths</name><value>24</value></parameter>"
        + "<parameter><name>MaxFilteredUsers</name><value>100</value></parameter>"
        + "</userDirectory>";

    userDirectory.setConfiguration(buffer);

    return userDirectory;
  }

  /**
   * Reload the user directory types.
   */
  private void reloadUserDirectoryTypes()
    throws SecurityServiceException
  {
    try
    {
      Map<String, UserDirectoryType> reloadedUserDirectoryTypes = new ConcurrentHashMap<>();

      for (UserDirectoryType userDirectoryType : getUserDirectoryTypes())
      {
        try
        {
          userDirectoryType.getUserDirectoryClass();
        }
        catch (Throwable e)
        {
          logger.error("Failed to load the user directory type (" + userDirectoryType.getCode()
              + "): Failed to retrieve the user directory class for the user directory type", e);

          continue;
        }

        reloadedUserDirectoryTypes.put(userDirectoryType.getCode(), userDirectoryType);
      }

      this.userDirectoryTypes = reloadedUserDirectoryTypes;
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException("Failed to reload the user directory types", e);
    }
  }
}
