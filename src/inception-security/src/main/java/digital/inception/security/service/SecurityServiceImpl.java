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

package digital.inception.security.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.PasswordUtil;
import digital.inception.core.util.RandomStringGenerator;
import digital.inception.core.util.ResourceException;
import digital.inception.core.util.ResourceUtil;
import digital.inception.core.xml.XmlSchemaClasspathInputSource;
import digital.inception.mail.model.MailTemplate;
import digital.inception.mail.model.MailTemplateContentType;
import digital.inception.mail.service.MailService;
import digital.inception.security.model.AuthenticationFailedException;
import digital.inception.security.model.DuplicateFunctionException;
import digital.inception.security.model.DuplicateGroupException;
import digital.inception.security.model.DuplicatePolicyException;
import digital.inception.security.model.DuplicateTenantException;
import digital.inception.security.model.DuplicateUserDirectoryException;
import digital.inception.security.model.DuplicateUserException;
import digital.inception.security.model.ExistingGroupMembersException;
import digital.inception.security.model.ExistingGroupsException;
import digital.inception.security.model.ExistingPasswordException;
import digital.inception.security.model.ExistingUsersException;
import digital.inception.security.model.ExpiredPasswordException;
import digital.inception.security.model.Function;
import digital.inception.security.model.FunctionNotFoundException;
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
import digital.inception.security.model.InternalUserDirectoryProvider;
import digital.inception.security.model.InvalidAttributeException;
import digital.inception.security.model.InvalidPolicyDataException;
import digital.inception.security.model.InvalidSecurityCodeException;
import digital.inception.security.model.PasswordChangeReason;
import digital.inception.security.model.PasswordReset;
import digital.inception.security.model.PasswordResetStatus;
import digital.inception.security.model.Policy;
import digital.inception.security.model.PolicyDataMismatchException;
import digital.inception.security.model.PolicyNotFoundException;
import digital.inception.security.model.PolicySortBy;
import digital.inception.security.model.PolicySummaries;
import digital.inception.security.model.PolicyType;
import digital.inception.security.model.RevokedToken;
import digital.inception.security.model.Role;
import digital.inception.security.model.RoleNotFoundException;
import digital.inception.security.model.Tenant;
import digital.inception.security.model.TenantNotFoundException;
import digital.inception.security.model.TenantUserDirectoryNotFoundException;
import digital.inception.security.model.Tenants;
import digital.inception.security.model.Token;
import digital.inception.security.model.TokenClaim;
import digital.inception.security.model.TokenNotFoundException;
import digital.inception.security.model.TokenSortBy;
import digital.inception.security.model.TokenStatus;
import digital.inception.security.model.TokenSummaries;
import digital.inception.security.model.TokenSummary;
import digital.inception.security.model.TokenType;
import digital.inception.security.model.User;
import digital.inception.security.model.UserAttribute;
import digital.inception.security.model.UserDirectories;
import digital.inception.security.model.UserDirectory;
import digital.inception.security.model.UserDirectoryCapabilities;
import digital.inception.security.model.UserDirectoryNotFoundException;
import digital.inception.security.model.UserDirectoryProvider;
import digital.inception.security.model.UserDirectorySummaries;
import digital.inception.security.model.UserDirectorySummary;
import digital.inception.security.model.UserDirectoryType;
import digital.inception.security.model.UserDirectoryTypeNotFoundException;
import digital.inception.security.model.UserLockedException;
import digital.inception.security.model.UserNotFoundException;
import digital.inception.security.model.UserSortBy;
import digital.inception.security.model.Users;
import digital.inception.security.persistence.FunctionRepository;
import digital.inception.security.persistence.GroupRepository;
import digital.inception.security.persistence.PasswordResetRepository;
import digital.inception.security.persistence.RoleRepository;
import digital.inception.security.persistence.TenantRepository;
import digital.inception.security.persistence.TokenRepository;
import digital.inception.security.persistence.TokenSummaryRepository;
import digital.inception.security.persistence.UserDirectoryRepository;
import digital.inception.security.persistence.UserDirectorySummaryRepository;
import digital.inception.security.persistence.UserDirectoryTypeRepository;
import digital.inception.security.persistence.UserRepository;
import digital.inception.security.store.PolicyStore;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Predicate;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The <b>SecurityServiceImpl</b> class provides the Security Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess", "DuplicatedCode"})
public class SecurityServiceImpl extends AbstractServiceBase implements SecurityService {

  /** The maximum number of filtered tenants. */
  private static final int MAX_FILTERED_TENANTS = 100;

  /** The maximum number of filtered tokens. */
  private static final int MAX_FILTERED_TOKENS = 100;

  /** The maximum number of filtered user directories. */
  private static final int MAX_FILTERED_USER_DIRECTORIES = 100;

  /** The code for the password reset mail template. */
  private static final String PASSWORD_RESET_MAIL_TEMPLATE_ID =
      "Inception.Security.PasswordResetMail";

  /** The Function Repository. */
  private final FunctionRepository functionRepository;

  /** The Group Repository. */
  private final GroupRepository groupRepository;

  /* The ID of the RSA key used to sign JWTs. */
  private final String jwtRsaKeyId;

  /** The Mail Service. */
  private final MailService mailService;

  /** The Password Reset Repository. */
  private final PasswordResetRepository passwordResetRepository;

  /** The Policy Data Store. */
  private final PolicyStore policyDataStore;

  /** The Role Repository. */
  private final RoleRepository roleRepository;

  /**
   * The random alphanumeric string generator that will be used to generate security codes for
   * password resets.
   */
  private final RandomStringGenerator securityCodeGenerator =
      new RandomStringGenerator(
          20, new SecureRandom(), "1234567890ACEFGHJKLMNPQRUVWXYabcdefhijkprstuvwx");

  /** The Tenant Repository. */
  private final TenantRepository tenantRepository;

  /** The Token Repository. */
  private final TokenRepository tokenRepository;

  /** The Token Summary Repository. */
  private final TokenSummaryRepository tokenSummaryRepository;

  /** The User Directory Repository. */
  private final UserDirectoryRepository userDirectoryRepository;

  /** The User Directory Summary Repository. */
  private final UserDirectorySummaryRepository userDirectorySummaryRepository;

  /** The User Directory Type Repository. */
  private final UserDirectoryTypeRepository userDirectoryTypeRepository;

  /** The User Repository. */
  private final UserRepository userRepository;

  /* The RSA private key used to sign JWTs. */
  private RSAPrivateKey jwtRsaPrivateKey;

  /* The RSA public key used to verify JWTs. */
  private RSAPublicKey jwtRsaPublicKey;

  /** The user directories. */
  private Map<UUID, UserDirectoryProvider> userDirectories = new ConcurrentHashMap<>();

  /**
   * Constructs a new <b>SecurityServiceImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param resourceLoader the Spring resource loader
   * @param mailService the Mail Service
   * @param policyDataStore the Policy Data Store
   * @param functionRepository the Function Repository
   * @param groupRepository the Group Repository
   * @param passwordResetRepository the Password Reset Repository
   * @param roleRepository the Role Repository
   * @param tenantRepository the Tenant Repository
   * @param tokenRepository the Token Repository
   * @param tokenSummaryRepository the Token Summary Repository
   * @param userDirectoryRepository the User Directory Repository
   * @param userDirectorySummaryRepository the User Directory Summary Repository
   * @param userDirectoryTypeRepository the User Directory Type Repository
   * @param userRepository the User Repository
   */
  public SecurityServiceImpl(
      ApplicationContext applicationContext,
      ResourceLoader resourceLoader,
      MailService mailService,
      PolicyStore policyDataStore,
      FunctionRepository functionRepository,
      GroupRepository groupRepository,
      PasswordResetRepository passwordResetRepository,
      RoleRepository roleRepository,
      TenantRepository tenantRepository,
      TokenRepository tokenRepository,
      TokenSummaryRepository tokenSummaryRepository,
      UserDirectoryRepository userDirectoryRepository,
      UserDirectorySummaryRepository userDirectorySummaryRepository,
      UserDirectoryTypeRepository userDirectoryTypeRepository,
      UserRepository userRepository) {
    super(applicationContext);

    this.mailService = mailService;
    this.policyDataStore = policyDataStore;
    this.functionRepository = functionRepository;
    this.groupRepository = groupRepository;
    this.passwordResetRepository = passwordResetRepository;
    this.roleRepository = roleRepository;
    this.tenantRepository = tenantRepository;
    this.tokenRepository = tokenRepository;
    this.tokenSummaryRepository = tokenSummaryRepository;
    this.userDirectoryRepository = userDirectoryRepository;
    this.userDirectorySummaryRepository = userDirectorySummaryRepository;
    this.userDirectoryTypeRepository = userDirectoryTypeRepository;
    this.userRepository = userRepository;

    if (StringUtils.hasText(
        applicationContext.getEnvironment().getProperty("inception.security.jwt.rsa-key-id"))) {
      this.jwtRsaKeyId =
          applicationContext.getEnvironment().getProperty("inception.security.jwt.rsa-key-id");
    } else if (StringUtils.hasText(
        applicationContext
            .getEnvironment()
            .getProperty("inception.authorization-server.jwt.rsa-key-id"))) {
      this.jwtRsaKeyId =
          applicationContext
              .getEnvironment()
              .getProperty("inception.authorization-server.jwt.rsa-key-id");
    } else {
      this.jwtRsaKeyId = "inception";
    }

    log.info("Using the JWT RSA key ID (" + jwtRsaKeyId + ") when issuing JWT tokens");

    String jwtRsaPrivateKeyLocation =
        applicationContext.getEnvironment().getProperty("inception.security.jwt.rsa-private-key");

    if (!StringUtils.hasText(jwtRsaPrivateKeyLocation)) {
      jwtRsaPrivateKeyLocation =
          applicationContext
              .getEnvironment()
              .getProperty("inception.authorization-server.jwt.rsa-private-key");
    }

    if (StringUtils.hasText(jwtRsaPrivateKeyLocation)) {
      try {
        this.jwtRsaPrivateKey =
            ResourceUtil.getRSAPrivateKeyResource(resourceLoader, jwtRsaPrivateKeyLocation);
      } catch (ResourceException e) {
        log.error(
            "Failed to initialize the JWT RSA private key (" + jwtRsaPrivateKeyLocation + ")", e);
      }
    }

    String jwtRsaPublicKeyLocation =
        applicationContext.getEnvironment().getProperty("inception.security.jwt.rsa-public-key");

    if (!StringUtils.hasText(jwtRsaPublicKeyLocation)) {
      jwtRsaPublicKeyLocation =
          applicationContext
              .getEnvironment()
              .getProperty("inception.authorization-server.jwt.rsa-public-key");
    }

    if (StringUtils.hasText(jwtRsaPublicKeyLocation)) {
      try {
        this.jwtRsaPublicKey =
            ResourceUtil.getRSAPublicKeyResource(resourceLoader, jwtRsaPublicKeyLocation);
      } catch (Throwable e) {
        log.error(
            "Failed to initialize the JWT RSA public key (" + jwtRsaPublicKeyLocation + ")", e);
      }
    }
  }

  @Override
  public void addMemberToGroup(
      UUID userDirectoryId, String groupName, GroupMemberType memberType, String memberName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (memberType == null) {
      throw new InvalidArgumentException("memberType");
    }

    if (!StringUtils.hasText(memberName)) {
      throw new InvalidArgumentException("memberName");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.addMemberToGroup(groupName, memberType, memberName);
  }

  @Override
  public void addRoleToGroup(UUID userDirectoryId, String groupName, String roleCode)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          RoleNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (!StringUtils.hasText(roleCode)) {
      throw new InvalidArgumentException("roleCode");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.addRoleToGroup(groupName, roleCode);
  }

  @Override
  public void addUserDirectoryToTenant(UUID tenantId, UUID userDirectoryId)
      throws InvalidArgumentException,
          TenantNotFoundException,
          UserDirectoryNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    try {
      if (!tenantRepository.existsById(tenantId)) {
        throw new TenantNotFoundException(tenantId);
      }

      if (!userDirectoryRepository.existsById(userDirectoryId)) {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      if (tenantRepository.userDirectoryToTenantMappingExists(tenantId, userDirectoryId) == 1) {
        return;
      }

      tenantRepository.addUserDirectoryToTenant(tenantId, userDirectoryId);
    } catch (TenantNotFoundException | UserDirectoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to add the user directory ("
              + userDirectoryId
              + ") to the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void addUserToGroup(UUID userDirectoryId, String groupName, String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.addUserToGroup(groupName, username);
  }

  @Override
  public void adminChangePassword(
      UUID userDirectoryId,
      String username,
      String newPassword,
      boolean expirePassword,
      boolean lockUser,
      boolean resetPasswordHistory,
      PasswordChangeReason reason)
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

    if (!StringUtils.hasText(newPassword)) {
      throw new InvalidArgumentException("newPassword");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.adminChangePassword(
        username, newPassword, expirePassword, lockUser, resetPasswordHistory, reason);
  }

  @Override
  public UUID authenticate(String username, String password)
      throws InvalidArgumentException,
          AuthenticationFailedException,
          UserLockedException,
          ExpiredPasswordException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    if (!StringUtils.hasText(password)) {
      throw new InvalidArgumentException("password");
    }

    try {
      // First check if this is an internal user and if so determine the user directory ID
      Optional<UUID> internalUserDirectoryIdOptional = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryIdOptional.isPresent()) {
        UUID internalUserDirectoryId = internalUserDirectoryIdOptional.get();

        UserDirectoryProvider internalUserDirectory = userDirectories.get(internalUserDirectoryId);

        if (internalUserDirectory == null) {
          throw new ServiceUnavailableException(
              "The user directory ID ("
                  + internalUserDirectoryId
                  + ") for the internal user ("
                  + username
                  + ") is invalid");
        } else {
          internalUserDirectory.authenticate(username, password);

          return internalUserDirectoryId;
        }
      } else {
        /*
         * Check the "external" user directories to see if one of them can authenticate this user.
         */
        for (UUID userDirectoryId : userDirectories.keySet()) {
          UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null) {
            if (!(userDirectory instanceof InternalUserDirectoryProvider)) {
              if (userDirectory.isExistingUser(username)) {
                userDirectory.authenticate(username, password);

                return userDirectoryId;
              }
            }
          }
        }

        throw new UserNotFoundException(username);
      }
    } catch (AuthenticationFailedException
        | UserNotFoundException
        | UserLockedException
        | ExpiredPasswordException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to authenticate the user (" + username + ")", e);
    }
  }

  @Override
  public UUID changePassword(String username, String password, String newPassword)
      throws InvalidArgumentException,
          AuthenticationFailedException,
          UserLockedException,
          ExistingPasswordException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    if (!StringUtils.hasText(password)) {
      throw new InvalidArgumentException("password");
    }

    if (!StringUtils.hasText(newPassword)) {
      throw new InvalidArgumentException("newPassword");
    }

    try {
      // First check if this is an internal user and if so determine the user directory ID
      Optional<UUID> internalUserDirectoryIdOptional = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryIdOptional.isPresent()) {
        UUID internalUserDirectoryId = internalUserDirectoryIdOptional.get();

        UserDirectoryProvider internalUserDirectory = userDirectories.get(internalUserDirectoryId);

        if (internalUserDirectory == null) {
          throw new ServiceUnavailableException(
              "The user directory ID ("
                  + internalUserDirectoryId
                  + ") for the internal user ("
                  + username
                  + ") is invalid");
        } else {
          internalUserDirectory.changePassword(username, password, newPassword);

          return internalUserDirectoryId;
        }
      } else {
        /*
         * Check the "external" user directories to see if one of them can change the password for this user.
         */
        for (UUID userDirectoryId : userDirectories.keySet()) {
          UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null) {
            if (!(userDirectory instanceof InternalUserDirectoryProvider)) {
              if (userDirectory.isExistingUser(username)) {
                userDirectory.changePassword(username, password, newPassword);

                return userDirectoryId;
              }
            }
          }
        }

        throw new AuthenticationFailedException(
            "Authentication failed while attempting to change the password for the user ("
                + username
                + ")");
      }
    } catch (AuthenticationFailedException | UserLockedException | ExistingPasswordException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to change the password for the user (" + username + ")", e);
    }
  }

  @Override
  public void createFunction(Function function)
      throws InvalidArgumentException, DuplicateFunctionException, ServiceUnavailableException {
    validateArgument("function", function);

    try {
      if (functionRepository.existsById(function.getCode())) {
        throw new DuplicateFunctionException(function.getCode());
      }

      functionRepository.saveAndFlush(function);
    } catch (DuplicateFunctionException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the function (" + function.getCode() + ")", e);
    }
  }

  @Override
  public void createGroup(Group group)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          DuplicateGroupException,
          ServiceUnavailableException {
    validateArgument("group", group);

    UserDirectoryProvider userDirectory = userDirectories.get(group.getUserDirectoryId());

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(group.getUserDirectoryId());
    }

    userDirectory.createGroup(group);
  }

  @Override
  public void createPolicy(Policy policy)
      throws InvalidArgumentException,
          InvalidPolicyDataException,
          DuplicatePolicyException,
          PolicyDataMismatchException,
          ServiceUnavailableException {
    validatePolicy(policy);

    policyDataStore.createPolicy(policy);
  }

  @Override
  public Optional<UserDirectory> createTenant(Tenant tenant, boolean createUserDirectory)
      throws InvalidArgumentException, DuplicateTenantException, ServiceUnavailableException {
    validateArgument("tenant", tenant);

    UserDirectory userDirectory = null;

    try {
      if ((tenant.getId() != null) && tenantRepository.existsById(tenant.getId())) {
        throw new DuplicateTenantException(tenant.getId());
      }

      if (tenantRepository.existsByNameIgnoreCase(tenant.getName())) {
        throw new DuplicateTenantException(tenant.getName());
      }

      if (createUserDirectory) {
        userDirectory = newInternalUserDirectoryForTenant(tenant);

        tenant.linkUserDirectory(userDirectory);
      }

      tenantRepository.saveAndFlush(tenant);

      try {
        reloadUserDirectories();
      } catch (Throwable e) {
        log.error("Failed to reload the user directories", e);
      }

      return Optional.ofNullable(userDirectory);
    } catch (DuplicateTenantException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the tenant (" + tenant.getId() + ")", e);
    }
  }

  @Override
  public void createUser(User user, boolean expiredPassword, boolean userLocked)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          DuplicateUserException,
          ServiceUnavailableException {
    validateArgument("user", user);

    UserDirectoryProvider userDirectory = userDirectories.get(user.getUserDirectoryId());

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(user.getUserDirectoryId());
    }

    if (getUserDirectoryIdForUser(user.getUsername()).isPresent()) {
      throw new DuplicateUserException(user.getUsername());
    }

    userDirectory.createUser(user, expiredPassword, userLocked);
  }

  @Override
  public void createUserDirectory(UserDirectory userDirectory)
      throws InvalidArgumentException,
          DuplicateUserDirectoryException,
          ServiceUnavailableException {
    validateArgument("userDirectory", userDirectory);

    try {
      if ((userDirectory.getId() != null)
          && userDirectoryRepository.existsById(userDirectory.getId())) {
        throw new DuplicateUserDirectoryException(userDirectory.getId());
      }

      if (userDirectoryRepository.existsByNameIgnoreCase(userDirectory.getName())) {
        throw new DuplicateUserDirectoryException(userDirectory.getName());
      }

      userDirectoryRepository.saveAndFlush(userDirectory);

      try {
        reloadUserDirectories();
      } catch (Throwable e) {
        log.error("Failed to reload the user directories", e);
      }
    } catch (DuplicateUserDirectoryException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the user directory (" + userDirectory.getName() + ")", e);
    }
  }

  @Override
  public void deleteFunction(String functionCode)
      throws InvalidArgumentException, FunctionNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(functionCode)) {
      throw new InvalidArgumentException("functionCode");
    }

    try {
      if (!functionRepository.existsById(functionCode)) {
        throw new FunctionNotFoundException(functionCode);
      }

      functionRepository.deleteById(functionCode);
    } catch (FunctionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the function (" + functionCode + ")", e);
    }
  }

  @Override
  public void deleteGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ExistingGroupMembersException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.deleteGroup(groupName);
  }

  @Override
  public void deletePolicy(String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(policyId)) {
      throw new InvalidArgumentException("policyId");
    }

    policyDataStore.deletePolicy(policyId);
  }

  @Override
  public void deleteTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      if (!tenantRepository.existsById(tenantId)) {
        throw new TenantNotFoundException(tenantId);
      }

      tenantRepository.deleteById(tenantId);
    } catch (TenantNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delete the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  public void deleteToken(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(tokenId)) {
      throw new InvalidArgumentException("tokenId");
    }

    try {
      if (!tokenRepository.existsById(tokenId)) {
        throw new TokenNotFoundException(tokenId);
      }

      tokenRepository.deleteById(tokenId);
    } catch (TokenNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delete the token (" + tokenId + ")", e);
    }
  }

  @Override
  public void deleteUser(UUID userDirectoryId, String username)
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

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.deleteUser(username);
  }

  @Override
  public void deleteUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException,
          ExistingGroupsException,
          ExistingUsersException,
          UserDirectoryNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    try {
      if (!userDirectoryRepository.existsById(userDirectoryId)) {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      if (groupRepository.existsByUserDirectoryId(userDirectoryId)) {
        throw new ExistingGroupsException(userDirectoryId);
      }

      if (userRepository.existsByUserDirectoryId(userDirectoryId)) {
        throw new ExistingUsersException(userDirectoryId);
      }

      userDirectoryRepository.deleteById(userDirectoryId);

      try {
        reloadUserDirectories();
      } catch (Throwable e) {
        log.error("Failed to reload the user directories", e);
      }
    } catch (ExistingGroupsException | ExistingUsersException | UserDirectoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the user directory (" + userDirectoryId + ")", e);
    }
  }

  @Override
  public List<User> findUsers(UUID userDirectoryId, List<UserAttribute> userAttributes)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          InvalidAttributeException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (userAttributes == null) {
      throw new InvalidArgumentException("attributes");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.findUsers(userAttributes);
  }

  @Override
  public Token generateToken(GenerateTokenRequest generateTokenRequest)
      throws InvalidArgumentException, ServiceUnavailableException {
    validateArgument("generateTokenRequest", generateTokenRequest);

    if (generateTokenRequest.getType() == TokenType.JWT) {

      if (!StringUtils.hasText(jwtRsaKeyId)) {
        throw new ServiceUnavailableException(
            "Failed to generate the token ("
                + generateTokenRequest.getName()
                + ") with type ("
                + generateTokenRequest.getType()
                + "): No inception.security.jwt.rsa-key-id property specified");
      }

      if (jwtRsaPrivateKey == null) {
        throw new ServiceUnavailableException(
            "Failed to generate the token ("
                + generateTokenRequest.getName()
                + ") with type ("
                + generateTokenRequest.getType()
                + "): No inception.security.jwt.rsa-private-key property specified");
      }

      try {
        String jwtId = UuidCreator.getTimeOrderedEpoch().toString().replace("-", "");

        OffsetDateTime issuedAt = OffsetDateTime.now();

        JWSSigner signer = new RSASSASigner(jwtRsaPrivateKey);

        JWTClaimsSet.Builder jwtClaimsSetBuilder = new Builder();

        jwtClaimsSetBuilder.issueTime(Date.from(issuedAt.toInstant()));

        jwtClaimsSetBuilder.issuer(jwtRsaKeyId);

        jwtClaimsSetBuilder.subject(generateTokenRequest.getName());

        jwtClaimsSetBuilder.claim("jti", jwtId);

        if (generateTokenRequest.getValidFromDate() != null) {
          jwtClaimsSetBuilder.notBeforeTime(
              Date.from(
                  generateTokenRequest
                      .getValidFromDate()
                      .atStartOfDay(ZoneId.systemDefault())
                      .toInstant()));
        }

        if (generateTokenRequest.getExpiryDate() != null) {
          jwtClaimsSetBuilder.expirationTime(
              Date.from(
                  generateTokenRequest
                      .getExpiryDate()
                      .atStartOfDay(ZoneId.systemDefault())
                      .toInstant()));
        }

        for (TokenClaim tokenClaim : generateTokenRequest.getClaims()) {
          jwtClaimsSetBuilder.claim(tokenClaim.getName(), tokenClaim.getValues());
        }

        SignedJWT signedJWT =
            new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(jwtRsaKeyId).build(),
                jwtClaimsSetBuilder.build());

        signedJWT.sign(signer);

        Token token =
            new Token(
                jwtId,
                generateTokenRequest.getType(),
                generateTokenRequest.getName(),
                generateTokenRequest.getDescription(),
                issuedAt,
                generateTokenRequest.getValidFromDate(),
                generateTokenRequest.getExpiryDate(),
                generateTokenRequest.getClaims(),
                signedJWT.serialize());

        return tokenRepository.saveAndFlush(token);
      } catch (Throwable e) {
        throw new ServiceUnavailableException(
            "Failed to generate the token ("
                + generateTokenRequest.getName()
                + ") with type ("
                + generateTokenRequest.getType()
                + ")",
            e);
      }
    } else {
      throw new ServiceUnavailableException(
          "Failed to generate the token ("
              + generateTokenRequest.getName()
              + ") with type ("
              + generateTokenRequest.getType()
              + "): Unsupported token type ("
              + generateTokenRequest.getType()
              + ")");
    }
  }

  @Override
  public Function getFunction(String functionCode)
      throws InvalidArgumentException, FunctionNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(functionCode)) {
      throw new InvalidArgumentException("functionCode");
    }

    try {
      Optional<Function> functionOptional = functionRepository.findById(functionCode);

      if (functionOptional.isPresent()) {
        return functionOptional.get();
      } else {
        throw new FunctionNotFoundException(functionCode);
      }
    } catch (FunctionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the function (" + functionCode + ")", e);
    }
  }

  @Override
  public List<String> getFunctionCodesForUser(UUID userDirectoryId, String username)
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

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getFunctionCodesForUser(username);
  }

  @Override
  public List<Function> getFunctions() throws ServiceUnavailableException {
    try {
      return functionRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the functions", e);
    }
  }

  @Override
  public Group getGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroup(groupName);
  }

  @Override
  public List<String> getGroupNames(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroupNames();
  }

  @Override
  public List<String> getGroupNamesForUser(UUID userDirectoryId, String username)
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

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroupNamesForUser(username);
  }

  @Override
  public List<Group> getGroups(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroups();
  }

  @Override
  public Groups getGroups(
      UUID userDirectoryId,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroups(filter, sortDirection, pageIndex, pageSize);
  }

  @Override
  public List<Group> getGroupsForUser(UUID userDirectoryId, String username)
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

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getGroupsForUser(username);
  }

  @Override
  public List<GroupMember> getMembersForGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getMembersForGroup(groupName);
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
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getMembersForGroup(groupName, filter, sortDirection, pageIndex, pageSize);
  }

  @Override
  public List<Policy> getPolicies() throws ServiceUnavailableException {
    return policyDataStore.getPolicies();
  }

  @Override
  public Policy getPolicy(String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(policyId)) {
      throw new InvalidArgumentException("policyId");
    }

    return policyDataStore.getPolicy(policyId);
  }

  @Override
  public String getPolicyName(String policyId)
      throws InvalidArgumentException, PolicyNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(policyId)) {
      throw new InvalidArgumentException("policyId");
    }

    return policyDataStore.getPolicyName(policyId);
  }

  @Override
  public PolicySummaries getPolicySummaries(
      String filter,
      PolicySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    return policyDataStore.getPolicySummaries(filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public List<RevokedToken> getRevokedTokens() throws ServiceUnavailableException {
    try {
      return tokenRepository.getRevokedTokens();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the revoked tokens", e);
    }
  }

  @Override
  public List<String> getRoleCodesForGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getRoleCodesForGroup(groupName);
  }

  @Override
  public List<String> getRoleCodesForUser(UUID userDirectoryId, String username)
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

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getRoleCodesForUser(username);
  }

  @Override
  public List<Role> getRoles() throws ServiceUnavailableException {
    try {
      return roleRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the roles", e);
    }
  }

  @Override
  public List<GroupRole> getRolesForGroup(UUID userDirectoryId, String groupName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getRolesForGroup(groupName);
  }

  @Override
  public Tenant getTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      Optional<Tenant> tenantOptional = tenantRepository.findById(tenantId);

      if (tenantOptional.isPresent()) {
        return tenantOptional.get();
      } else {
        throw new TenantNotFoundException(tenantId);
      }
    } catch (TenantNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  public List<UUID> getTenantIdsForUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    try {
      if (!userDirectoryRepository.existsById(userDirectoryId)) {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      return userDirectoryRepository.getTenantIdsById(userDirectoryId);
    } catch (UserDirectoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the IDs for the tenants for the user directory ("
              + userDirectoryId
              + ")",
          e);
    }
  }

  @Override
  public String getTenantName(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      Optional<String> nameOptional = tenantRepository.getNameById(tenantId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      } else {
        throw new TenantNotFoundException(tenantId);
      }
    } catch (TenantNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the name of the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  public List<Tenant> getTenants() throws ServiceUnavailableException {
    try {
      return tenantRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the tenants", e);
    }
  }

  @Override
  public Tenants getTenants(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = MAX_FILTERED_TENANTS;
    }

    try {
      PageRequest pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_TENANTS),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              "name");

      Page<Tenant> tenantPage;

      if (StringUtils.hasText(filter)) {
        tenantPage =
            tenantRepository.findAll(
                (Specification<Tenant>)
                    (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + filter.toLowerCase() + "%"),
                pageRequest);
      } else {
        tenantPage = tenantRepository.findAll(pageRequest);
      }

      return new Tenants(
          tenantPage.toList(),
          tenantPage.getTotalElements(),
          filter,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      String message = "Failed to retrieve the tenants";

      if (StringUtils.hasText(filter)) {
        message += " matching the filter \"%s\"".formatted(filter);
      }

      message += " for the page " + pageIndex + " using the page size " + pageSize;

      message += ": ";

      message += e.getMessage();

      throw new ServiceUnavailableException(message, e);
    }
  }

  @Override
  public List<Tenant> getTenantsForUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    try {
      if (!userDirectoryRepository.existsById(userDirectoryId)) {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      return tenantRepository.findAllByUserDirectoryId(userDirectoryId);
    } catch (UserDirectoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the tenants associated with the user directory ("
              + userDirectoryId
              + ")",
          e);
    }
  }

  @Override
  public Token getToken(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(tokenId)) {
      throw new InvalidArgumentException("tokenId");
    }

    try {
      Optional<Token> tokenOptional = tokenRepository.findById(tokenId);

      if (tokenOptional.isPresent()) {
        return tokenOptional.get();
      } else {
        throw new TokenNotFoundException(tokenId);
      }
    } catch (TokenNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the token (" + tokenId + ")", e);
    }
  }

  @Override
  public String getTokenName(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(tokenId)) {
      throw new InvalidArgumentException("tokenId");
    }

    try {
      Optional<String> nameOptional = tokenRepository.getNameById(tokenId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      } else {
        throw new TokenNotFoundException(tokenId);
      }
    } catch (TokenNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the name of the token (" + tokenId + ")", e);
    }
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
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = TokenSortBy.NAME;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.DESCENDING;
    }

    try {
      PageRequest pageRequest;

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = MAX_FILTERED_TOKENS;
      }

      String sortProperty;
      if (sortBy == TokenSortBy.EXPIRES) {
        sortProperty = "expires";
      } else if (sortBy == TokenSortBy.ISSUED) {
        sortProperty = "issued";
      } else if (sortBy == TokenSortBy.REVOKED) {
        sortProperty = "revoked";
      } else if (sortBy == TokenSortBy.TYPE) {
        sortProperty = "type";
      } else {
        sortProperty = "name";
      }

      pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_TOKENS),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              sortProperty);

      Page<TokenSummary> tokenSummaryPage =
          tokenSummaryRepository.findAll(
              (Specification<TokenSummary>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    if (status != null) {
                      if (status == TokenStatus.ACTIVE) {
                        predicates.add(
                            criteriaBuilder.or(
                                criteriaBuilder.isNull(root.get("validFromDate")),
                                criteriaBuilder.lessThanOrEqualTo(
                                    root.get("validFromDate"), LocalDate.now())));
                        predicates.add(
                            criteriaBuilder.or(
                                criteriaBuilder.isNull(root.get("expiryDate")),
                                criteriaBuilder.greaterThan(
                                    root.get("expiryDate"), LocalDate.now())));
                      } else if (status == TokenStatus.EXPIRED) {
                        predicates.add(
                            criteriaBuilder.and(
                                criteriaBuilder.isNotNull(root.get("expiryDate")),
                                criteriaBuilder.lessThanOrEqualTo(
                                    root.get("expiryDate"), LocalDate.now())));
                      } else if (status == TokenStatus.PENDING) {
                        predicates.add(
                            criteriaBuilder.and(
                                criteriaBuilder.isNotNull(root.get("validFromDate")),
                                criteriaBuilder.greaterThan(
                                    root.get("validFromDate"), LocalDate.now())));
                      }

                      if (status == TokenStatus.REVOKED) {
                        predicates.add(criteriaBuilder.isNotNull(root.get("revocationDate")));
                      } else {
                        predicates.add(criteriaBuilder.isNull(root.get("revocationDate")));
                      }
                    }

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.like(
                              criteriaBuilder.lower(root.get("name")),
                              "%" + filter.toLowerCase() + "%"));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new TokenSummaries(
          tokenSummaryPage.toList(),
          tokenSummaryPage.getTotalElements(),
          status,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered token summaries", e);
    }
  }

  @Override
  public List<Token> getTokens() throws ServiceUnavailableException {
    try {
      return tokenRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the tokens", e);
    }
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

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUser(username);
  }

  @Override
  public List<UserDirectory> getUserDirectories() throws ServiceUnavailableException {
    try {
      return userDirectoryRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the user directories", e);
    }
  }

  @Override
  public UserDirectories getUserDirectories(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = MAX_FILTERED_USER_DIRECTORIES;
    }

    try {
      Page<UserDirectory> userDirectoryPage;

      PageRequest pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_USER_DIRECTORIES),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              "name");

      if (StringUtils.hasText(filter)) {
        userDirectoryPage =
            userDirectoryRepository.findAll(
                (Specification<UserDirectory>)
                    (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + filter.toLowerCase() + "%"),
                pageRequest);
      } else {
        userDirectoryPage = userDirectoryRepository.findAll(pageRequest);
      }

      return new UserDirectories(
          userDirectoryPage.toList(),
          userDirectoryPage.getTotalElements(),
          filter,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered user directories", e);
    }
  }

  @Override
  public List<UserDirectory> getUserDirectoriesForTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      if (!tenantRepository.existsById(tenantId)) {
        throw new TenantNotFoundException(tenantId);
      }

      return userDirectoryRepository.findAllByTenantId(tenantId);
    } catch (TenantNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the user directories associated with the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public UserDirectory getUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    try {
      Optional<UserDirectory> userDirectoryOptional =
          userDirectoryRepository.findById(userDirectoryId);

      if (userDirectoryOptional.isPresent()) {
        return userDirectoryOptional.get();
      } else {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }
    } catch (UserDirectoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the user directory (" + userDirectoryId + ")", e);
    }
  }

  @Override
  public UserDirectoryCapabilities getUserDirectoryCapabilities(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getCapabilities();
  }

  @Override
  public Optional<UUID> getUserDirectoryIdForUser(String username)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    try {
      // First check if this is an internal user and if so determine the user directory ID
      Optional<UUID> internalUserDirectoryIdOptional = getInternalUserDirectoryIdForUser(username);

      if (internalUserDirectoryIdOptional.isPresent()) {
        return internalUserDirectoryIdOptional;
      } else {
        /*
         * Check the "external" user directories to see if the user is associated with one of them.
         */
        for (UUID userDirectoryId : userDirectories.keySet()) {
          UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

          if (userDirectory != null) {
            if (!(userDirectory instanceof InternalUserDirectoryProvider)) {
              if (userDirectory.isExistingUser(username)) {
                return Optional.of(userDirectoryId);
              }
            }
          }
        }

        return Optional.empty();
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the user directory ID for the user (" + username + ")", e);
    }
  }

  @Override
  public List<UUID> getUserDirectoryIdsForTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      if (!tenantRepository.existsById(tenantId)) {
        throw new TenantNotFoundException(tenantId);
      }

      return tenantRepository.getUserDirectoryIdsById(tenantId);
    } catch (TenantNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the IDs for the user directories associated with the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public List<UUID> getUserDirectoryIdsForUser(String username)
      throws InvalidArgumentException, UserNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    try {
      List<UUID> userDirectoryIdsForUser = new ArrayList<>();

      Optional<UUID> userDirectoryIdOptional = getUserDirectoryIdForUser(username);

      if (userDirectoryIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      /*
       * Retrieve the list of IDs for the tenants the user is associated with as a result
       * of their user directory being associated with these tenants.
       */
      List<UUID> tenantIds = getTenantIdsForUserDirectory(userDirectoryIdOptional.get());

      /*
       * Retrieve the list of IDs for the user directories the user is associated with as a result
       * of being associated with one or more tenants.
       */
      for (var tenantId : tenantIds) {
        // Retrieve the list of user directories associated with the tenant
        var userDirectoryIdsForTenant = getUserDirectoryIdsForTenant(tenantId);

        userDirectoryIdsForUser.addAll(userDirectoryIdsForTenant);
      }

      return userDirectoryIdsForUser;
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the IDs for the user directories the user ("
              + username
              + ") is associated with",
          e);
    }
  }

  @Override
  public String getUserDirectoryName(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    try {
      Optional<String> nameOptional = userDirectoryRepository.getNameById(userDirectoryId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      } else {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }
    } catch (UserDirectoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the name of the user directory (" + userDirectoryId + ")", e);
    }
  }

  @Override
  public UserDirectorySummaries getUserDirectorySummaries(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = MAX_FILTERED_USER_DIRECTORIES;
    }

    try {
      Page<UserDirectorySummary> userDirectorySummaryPage;

      PageRequest pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_USER_DIRECTORIES),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              "name");

      if (StringUtils.hasText(filter)) {
        userDirectorySummaryPage =
            userDirectorySummaryRepository.findAll(
                (Specification<UserDirectorySummary>)
                    (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + filter.toLowerCase() + "%"),
                pageRequest);
      } else {
        userDirectorySummaryPage = userDirectorySummaryRepository.findAll(pageRequest);
      }

      return new UserDirectorySummaries(
          userDirectorySummaryPage.toList(),
          userDirectorySummaryPage.getTotalElements(),
          filter,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered summaries for the user directories", e);
    }
  }

  @Override
  public List<UserDirectorySummary> getUserDirectorySummariesForTenant(UUID tenantId)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      if (!tenantRepository.existsById(tenantId)) {
        throw new TenantNotFoundException(tenantId);
      }

      return userDirectorySummaryRepository.findAllByTenantId(tenantId);
    } catch (TenantNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the user "
              + "directories associated with the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public UserDirectoryType getUserDirectoryTypeForUserDirectory(UUID userDirectoryId)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserDirectoryTypeNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    try {
      Optional<String> typeOptional =
          userDirectoryRepository.getTypeForUserDirectoryById(userDirectoryId);

      if (typeOptional.isEmpty()) {
        throw new UserDirectoryNotFoundException(userDirectoryId);
      }

      Optional<UserDirectoryType> userDirectoryTypeOptional =
          userDirectoryTypeRepository.findById(typeOptional.get());

      if (userDirectoryTypeOptional.isPresent()) {
        return userDirectoryTypeOptional.get();
      } else {
        throw new UserDirectoryTypeNotFoundException(typeOptional.get());
      }
    } catch (UserDirectoryNotFoundException | UserDirectoryTypeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the user directory type for the user directory ("
              + userDirectoryId
              + ")",
          e);
    }
  }

  @Override
  public List<UserDirectoryType> getUserDirectoryTypes() throws ServiceUnavailableException {
    try {
      return userDirectoryTypeRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the user directory types", e);
    }
  }

  @Override
  public String getUserName(UUID userDirectoryId, String username)
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

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUserName(username);
  }

  @Override
  public List<User> getUsers(UUID userDirectoryId)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUsers();
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
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (sortBy == null) {
      sortBy = UserSortBy.NAME;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.ASCENDING;
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.getUsers(filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  /** Initialize the Security Service. */
  @PostConstruct
  public void init() {
    try {
      // Load the default password reset mail template
      if (!mailService.mailTemplateExists(PASSWORD_RESET_MAIL_TEMPLATE_ID)) {
        byte[] passwordResetMailTemplate =
            ResourceUtil.getClasspathResource("digital/inception/security/PasswordReset.ftl");

        MailTemplate mailTemplate =
            new MailTemplate(
                PASSWORD_RESET_MAIL_TEMPLATE_ID,
                "Password Reset",
                MailTemplateContentType.HTML,
                passwordResetMailTemplate);

        mailService.createMailTemplate(mailTemplate);
      }

      // Load the user directories
      reloadUserDirectories();
    } catch (Throwable e) {
      throw new RuntimeException("Failed to initialize the Security Service", e);
    }
  }

  @Override
  public void initiatePasswordReset(String username, String resetPasswordUrl, boolean sendEmail)
      throws InvalidArgumentException, UserNotFoundException, ServiceUnavailableException {
    initiatePasswordReset(username, resetPasswordUrl, sendEmail, null);
  }

  @Override
  public void initiatePasswordReset(
      String username, String resetPasswordUrl, boolean sendEmail, String securityCode)
      throws InvalidArgumentException, UserNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    if (!StringUtils.hasText(resetPasswordUrl)) {
      throw new InvalidArgumentException("resetPasswordUrl");
    }

    try {
      Optional<UUID> userDirectoryIdOptional = getUserDirectoryIdForUser(username);

      if (userDirectoryIdOptional.isEmpty()) {
        throw new UserNotFoundException(username);
      }

      UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryIdOptional.get());

      User user = userDirectory.getUser(username);

      if (StringUtils.hasText(user.getEmail())) {
        if (!StringUtils.hasText(securityCode)) {
          securityCode = securityCodeGenerator.nextString();
        }

        String securityCodeHash = PasswordUtil.createPasswordHash(securityCode);

        PasswordReset passwordReset = new PasswordReset(username, securityCodeHash);

        if (sendEmail) {
          sendPasswordResetEmail(user, resetPasswordUrl, securityCode);
        }

        passwordResetRepository.saveAndFlush(passwordReset);
      } else {
        log.warn(
            "Failed to send the password reset communication to the user ("
                + username
                + ") who does not have a valid email address");
      }
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to initiate the password reset process for the user (" + username + ")", e);
    }
  }

  @Override
  public boolean isExistingUser(UUID userDirectoryId, String username)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.isExistingUser(username);
  }

  @Override
  public boolean isUserInGroup(UUID userDirectoryId, String groupName, String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    return userDirectory.isUserInGroup(groupName, username);
  }

  @Override
  public void reinstateToken(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(tokenId)) {
      throw new InvalidArgumentException("tokenId");
    }

    try {
      if (tokenRepository.reinstateToken(tokenId) == 0) {
        throw new TokenNotFoundException(tokenId);
      }
    } catch (TokenNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to reinstate the token (" + tokenId + ")", e);
    }
  }

  @Override
  public void reloadUserDirectories() throws ServiceUnavailableException {
    try {
      Map<UUID, UserDirectoryProvider> reloadedUserDirectories = new ConcurrentHashMap<>();

      List<UserDirectoryType> userDirectoryTypes = getUserDirectoryTypes();

      for (UserDirectory userDirectory : getUserDirectories()) {
        UserDirectoryType userDirectoryType;

        userDirectoryType =
            userDirectoryTypes.stream()
                .filter(
                    possibleUserDirectoryType ->
                        possibleUserDirectoryType.getCode().equals(userDirectory.getType()))
                .findFirst()
                .orElse(null);

        if (userDirectoryType == null) {
          log.error(
              "Failed to load the user directory ("
                  + userDirectory.getId()
                  + "): The user directory type ("
                  + userDirectory.getType()
                  + ") was not loaded");

          continue;
        }

        try {
          Class<?> clazz =
              Thread.currentThread()
                  .getContextClassLoader()
                  .loadClass(userDirectoryType.getProviderClassName());

          if (!UserDirectoryProvider.class.isAssignableFrom(clazz)) {
            throw new ServiceUnavailableException(
                "The user directory class ("
                    + userDirectoryType.getProviderClassName()
                    + ") does not implement the IUserDirectory interface");
          }

          Class<? extends UserDirectoryProvider> userDirectoryProviderClass =
              clazz.asSubclass(UserDirectoryProvider.class);

          Constructor<? extends UserDirectoryProvider> userDirectoryProviderClassConstructor;

          try {
            userDirectoryProviderClassConstructor =
                userDirectoryProviderClass.getConstructor(
                    UUID.class,
                    List.class,
                    GroupRepository.class,
                    UserRepository.class,
                    RoleRepository.class);
          } catch (NoSuchMethodException e) {
            throw new ServiceUnavailableException(
                "The user directory provider class ("
                    + userDirectoryType.getProviderClassName()
                    + ") does not provide a valid constructor (long, Map<String,String>)");
          }

          UserDirectoryProvider userDirectoryProviderInstance =
              userDirectoryProviderClassConstructor.newInstance(
                  userDirectory.getId(),
                  userDirectory.getParameters(),
                  groupRepository,
                  userRepository,
                  roleRepository);

          getApplicationContext()
              .getAutowireCapableBeanFactory()
              .autowireBean(userDirectoryProviderInstance);

          reloadedUserDirectories.put(userDirectory.getId(), userDirectoryProviderInstance);
        } catch (Throwable e) {
          throw new ServiceUnavailableException(
              "Failed to initialize the user directory ("
                  + userDirectory.getId()
                  + ")("
                  + userDirectory.getName()
                  + ")",
              e);
        }
      }

      this.userDirectories = reloadedUserDirectories;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to reload the user directories", e);
    }
  }

  @Override
  public void removeMemberFromGroup(
      UUID userDirectoryId, String groupName, GroupMemberType memberType, String memberName)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          GroupMemberNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (memberType == null) {
      throw new InvalidArgumentException("memberType");
    }

    if (!StringUtils.hasText(memberName)) {
      throw new InvalidArgumentException("memberName");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.removeMemberFromGroup(groupName, memberType, memberName);
  }

  @Override
  public void removeRoleFromGroup(UUID userDirectoryId, String groupName, String roleCode)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          GroupRoleNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (!StringUtils.hasText(roleCode)) {
      throw new InvalidArgumentException("roleCode");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.removeRoleFromGroup(groupName, roleCode);
  }

  @Override
  public void removeUserDirectoryFromTenant(UUID tenantId, UUID userDirectoryId)
      throws InvalidArgumentException,
          TenantNotFoundException,
          TenantUserDirectoryNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    try {
      if (!tenantRepository.existsById(tenantId)) {
        throw new TenantNotFoundException(tenantId);
      }

      if (tenantRepository.userDirectoryToTenantMappingExists(tenantId, userDirectoryId) == 0) {
        throw new TenantUserDirectoryNotFoundException(tenantId, userDirectoryId);
      }

      tenantRepository.removeUserDirectoryFromTenant(tenantId, userDirectoryId);
    } catch (TenantNotFoundException | TenantUserDirectoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to add the user directory ("
              + userDirectoryId
              + ") to the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void removeUserFromGroup(UUID userDirectoryId, String groupName, String username)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    if (userDirectoryId == null) {
      throw new InvalidArgumentException("userDirectoryId");
    }

    if (!StringUtils.hasText(groupName)) {
      throw new InvalidArgumentException("groupName");
    }

    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryId);

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(userDirectoryId);
    }

    userDirectory.removeUserFromGroup(groupName, username);
  }

  @Override
  public void resetPassword(String username, String newPassword, String securityCode)
      throws InvalidArgumentException,
          InvalidSecurityCodeException,
          UserLockedException,
          ExistingPasswordException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(username)) {
      throw new InvalidArgumentException("username");
    }

    if (!StringUtils.hasText(newPassword)) {
      throw new InvalidArgumentException("newPassword");
    }

    if (!StringUtils.hasText(securityCode)) {
      throw new InvalidArgumentException("securityCode");
    }

    try {
      Optional<UUID> userDirectoryIdOptional = getUserDirectoryIdForUser(username);

      if (userDirectoryIdOptional.isEmpty()) {
        throw new InvalidSecurityCodeException(username);
      }

      List<PasswordReset> passwordResets =
          passwordResetRepository.findAllByUsernameAndStatus(
              username, PasswordResetStatus.REQUESTED);

      String securityCodeHash = PasswordUtil.createPasswordHash(securityCode);

      for (PasswordReset passwordReset : passwordResets) {
        if (passwordReset.getSecurityCodeHash().equals(securityCodeHash)) {
          UserDirectoryProvider userDirectory = userDirectories.get(userDirectoryIdOptional.get());

          userDirectory.resetPassword(username, newPassword);

          return;
        }
      }

      throw new InvalidSecurityCodeException(username);
    } catch (UserLockedException | InvalidSecurityCodeException | ExistingPasswordException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to reset the password for the user (" + username + ")", e);
    }
  }

  @Override
  public void revokeToken(String tokenId)
      throws InvalidArgumentException, TokenNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(tokenId)) {
      throw new InvalidArgumentException("tokenId");
    }

    try {
      if (tokenRepository.revokeToken(tokenId, LocalDate.now()) == 0) {
        throw new TokenNotFoundException(tokenId);
      }
    } catch (TokenNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to revoke the token (" + tokenId + ")", e);
    }
  }

  @Override
  public void updateFunction(Function function)
      throws InvalidArgumentException, FunctionNotFoundException, ServiceUnavailableException {
    validateArgument("function", function);

    try {
      if (!functionRepository.existsById(function.getCode())) {
        throw new FunctionNotFoundException(function.getCode());
      }

      functionRepository.saveAndFlush(function);
    } catch (FunctionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the function (" + function.getCode() + ")", e);
    }
  }

  @Override
  public void updateGroup(Group group)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          GroupNotFoundException,
          ServiceUnavailableException {
    validateArgument("group", group);

    UserDirectoryProvider userDirectory = userDirectories.get(group.getUserDirectoryId());

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(group.getUserDirectoryId());
    }

    userDirectory.updateGroup(group);
  }

  @Override
  public void updatePolicy(Policy policy)
      throws InvalidArgumentException,
          InvalidPolicyDataException,
          PolicyDataMismatchException,
          PolicyNotFoundException,
          ServiceUnavailableException {
    validatePolicy(policy);

    policyDataStore.updatePolicy(policy);
  }

  @Override
  public void updateTenant(Tenant tenant)
      throws InvalidArgumentException, TenantNotFoundException, ServiceUnavailableException {
    validateArgument("tenant", tenant);

    try {
      Optional<Tenant> tenantOptional = tenantRepository.findById(tenant.getId());

      if (tenantOptional.isPresent()) {
        Tenant existingTenant = tenantOptional.get();

        existingTenant.setName(tenant.getName());
        existingTenant.setStatus(tenant.getStatus());

        tenantRepository.saveAndFlush(existingTenant);
      } else {
        throw new TenantNotFoundException(tenant.getId());
      }
    } catch (TenantNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the tenant (" + tenant.getId() + ")", e);
    }
  }

  @Override
  public void updateUser(User user, boolean expirePassword, boolean lockUser)
      throws InvalidArgumentException,
          UserDirectoryNotFoundException,
          UserNotFoundException,
          ServiceUnavailableException {
    validateArgument("user", user);

    UserDirectoryProvider userDirectory = userDirectories.get(user.getUserDirectoryId());

    if (userDirectory == null) {
      throw new UserDirectoryNotFoundException(user.getUserDirectoryId());
    }

    userDirectory.updateUser(user, expirePassword, lockUser);
  }

  @Override
  public void updateUserDirectory(UserDirectory userDirectory)
      throws InvalidArgumentException, UserDirectoryNotFoundException, ServiceUnavailableException {
    validateArgument("userDirectory", userDirectory);

    try {
      if (!userDirectoryRepository.existsById(userDirectory.getId())) {
        throw new UserDirectoryNotFoundException(userDirectory.getId());
      }

      userDirectoryRepository.saveAndFlush(userDirectory);

      reloadUserDirectories();
    } catch (UserDirectoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the user directory (" + userDirectory.getName() + ")", e);
    }
  }

  /**
   * Returns the ID for the internal user directory the internal user with the specified username is
   * associated with.
   *
   * @param username the username for the internal user
   * @return an Optional containing the ID for the internal user directory the internal user with
   *     the specified username is associated with or an empty Optional if an internal user with the
   *     specified username could not be found
   * @throws ServiceUnavailableException if the internal user directory ID could not be retrieved
   *     for the user
   */
  private Optional<UUID> getInternalUserDirectoryIdForUser(String username)
      throws ServiceUnavailableException {
    try {
      return userRepository.getUserDirectoryIdByUsernameIgnoreCase(username);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the ID for the internal user directory for the internal user ("
              + username
              + ")",
          e);
    }
  }

  /**
   * Checks whether the specified value is <b>null</b> or blank.
   *
   * @param value the value to check
   * @return true if the value is <b>null</b> or blank
   */
  private boolean isNullOrEmpty(Object value) {
    if (value == null) {
      return true;
    }

    if (value instanceof String) {
      return ((String) value).isEmpty();
    }

    return false;
  }

  private UserDirectory newInternalUserDirectoryForTenant(Tenant tenant)
      throws ServiceUnavailableException {
    UserDirectory userDirectory = new UserDirectory();

    if (tenant.getId() != null) {
      userDirectory.setId(tenant.getId());
    }

    userDirectory.setType("InternalUserDirectory");
    userDirectory.setName(tenant.getName() + " Internal User Directory");

    String buffer =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE userDirectory "
            + "SYSTEM \"UserDirectoryConfiguration.dtd\"><userDirectory>"
            + "<parameter><name>MaxPasswordAttempts</name><value>5</value></parameter>"
            + "<parameter><name>PasswordExpiryMonths</name><value>12</value></parameter>"
            + "<parameter><name>PasswordHistoryMonths</name><value>24</value></parameter>"
            + "<parameter><name>MaxFilteredUsers</name><value>100</value></parameter>"
            + "</userDirectory>";

    try {
      userDirectory.setConfiguration(buffer);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to set the configuration for the user directory", e);
    }

    return userDirectory;
  }

  private void sendPasswordResetEmail(User user, String resetPasswordUrl, String securityCode)
      throws ServiceUnavailableException {
    try {
      if (StringUtils.hasText(user.getEmail())) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("preferredName", user.getPreferredName().toUpperCase());
        parameters.put("securityCode", securityCode);
        parameters.put(
            "resetPasswordUrl",
            resetPasswordUrl
                + "?username="
                + URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8)
                + "&securityCode="
                + URLEncoder.encode(securityCode, StandardCharsets.UTF_8));

        mailService.sendMail(
            Collections.singletonList(user.getEmail()),
            "Password Reset",
            "no-reply@inception.digital",
            "Inception",
            PASSWORD_RESET_MAIL_TEMPLATE_ID,
            parameters);
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to send the password reset email", e);
    }
  }

  private void validatePolicy(Policy policy)
      throws InvalidArgumentException,
          InvalidPolicyDataException,
          PolicyDataMismatchException,
          ServiceUnavailableException {
    validateArgument("policy", policy);

    if ((policy.getType() == PolicyType.XACML_POLICY_SET)
        || (policy.getType() == PolicyType.XACML_POLICY)) {

      try {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        schemaFactory.setResourceResolver(
            (type, namespaceURI, publicId, systemId, baseURI) -> {
              if (systemId.equals("http://www.w3.org/2001/xml.xsd")) {
                return new XmlSchemaClasspathInputSource(
                    namespaceURI, publicId, systemId, baseURI, "META-INF/xacml/xml.xsd");
              }

              throw new RuntimeException("Failed to resolve the resource (" + systemId + ")");
            });

        Schema schema =
            schemaFactory.newSchema(
                new StreamSource[] {
                  new StreamSource(
                      new ByteArrayInputStream(
                          ResourceUtil.getClasspathResource(
                              "META-INF/xacml/xacml-core-v3-schema-wd-17.xsd")))
                });

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setSchema(schema);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.setErrorHandler(
            new ErrorHandler() {
              @Override
              public void error(SAXParseException exception) throws SAXException {
                throw new SAXException("Failed to process the XACML XML data", exception);
              }

              @Override
              public void fatalError(SAXParseException exception) throws SAXException {
                throw new SAXException("Failed to process the XACML XML data", exception);
              }

              @Override
              public void warning(SAXParseException exception) throws SAXException {
                throw new SAXException("Failed to process the XACML XML data", exception);
              }
            });

        Document document =
            documentBuilder.parse(
                new InputSource(new ByteArrayInputStream(policy.getData().getBytes())));

        Element documentElement = document.getDocumentElement();

        String documentElementTagName = documentElement.getTagName();

        if (documentElementTagName.equals("PolicySet")) {
          if (policy.getType() != PolicyType.XACML_POLICY_SET) {
            throw new PolicyDataMismatchException(
                "The type for the policy and the XML document element ("
                    + documentElementTagName
                    + ") do not match");
          }

          String policySetId = documentElement.getAttribute("PolicySetId");
          String version = documentElement.getAttribute("Version");

          if (!policy.getId().equals(policySetId)) {
            throw new PolicyDataMismatchException(
                "The ID for the policy and the PolicySetId attribute on the PolicySet XML element do not match");
          }

          if (!policy.getVersion().equals(version)) {
            throw new PolicyDataMismatchException(
                "The version for the policy and the Version attribute on the PolicySet XML element do not match");
          }
        } else if (documentElementTagName.equals("Policy")) {
          if (policy.getType() != PolicyType.XACML_POLICY) {
            throw new PolicyDataMismatchException(
                "The type for the policy and the XML document element ("
                    + documentElementTagName
                    + ") do not match");
          }

          String policyId = documentElement.getAttribute("PolicyId");
          String version = documentElement.getAttribute("Version");

          if (!policy.getId().equals(policyId)) {
            throw new PolicyDataMismatchException(
                "The ID for the policy and the PolicyId attribute on the Policy XML element do not match");
          }

          if (!policy.getVersion().equals(version)) {
            throw new PolicyDataMismatchException(
                "The version for the policy and the Version attribute on the Policy XML element do not match");
          }
        }
      } catch (PolicyDataMismatchException e) {
        throw e;
      } catch (SAXException e) {
        throw new InvalidPolicyDataException(e);
      } catch (Throwable e) {
        throw new ServiceUnavailableException("Failed to validate the XACML XML data", e);
      }
    }
  }
}
