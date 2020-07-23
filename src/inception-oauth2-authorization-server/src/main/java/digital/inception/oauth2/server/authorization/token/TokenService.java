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

package digital.inception.oauth2.server.authorization.token;

import digital.inception.security.ISecurityService;
import digital.inception.security.User;
import java.security.interfaces.RSAPrivateKey;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The <code>TokenService</code> class provides the Token Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class TokenService implements ITokenService {

  /** The access token validity in seconds (24 hours). */
  public static final int ACCESS_TOKEN_VALIDITY = 86400;

  /** The refresh token validity in seconds. */
  public static final int REFRESH_TOKEN_VALIDITY = 2 * 365 * 24 * 60 * 60;

  /* The application name. */
  @Value("${spring.application.name:#{null}}")
  private String applicationName;

  /* The RSA private key used to sign the JWTs. */
  @Value("${inception.oauth2.authorizationserver.jwt.private-key-value}")
  private RSAPrivateKey rsaPrivateKey;

  /* Security Service */
  private final ISecurityService securityService;

  /**
   * Constructs a new <code>TokenService</code>.
   *
   * @param securityService the Security Service
   */
  public TokenService(ISecurityService securityService) {
    this.securityService = securityService;
  }

  /**
   * Issue an OAuth2 access token for the specified user.
   *
   * @param username the username identifying the user
   * @return the OAuth2 access token
   */
  public OAuth2AccessToken issueOAuth2AccessToken(String username) throws TokenServiceException {
    try {
      UUID userDirectoryId = securityService.getUserDirectoryIdForUser(username);

      // Retrieve the details for the user
      User user = securityService.getUser(userDirectoryId, username);

      // Retrieve the function codes for the user
      List<String> functionCodes =
          securityService.getFunctionCodesForUser(userDirectoryId, username);

      // Retrieve the list of IDs for the organizations the user is associated with
      List<UUID> organizationIds =
          securityService.getOrganizationIdsForUserDirectory(userDirectoryId);

      // Retrieve the list of roles for the user
      List<String> roleCodes = securityService.getRoleCodesForUser(userDirectoryId, username);

      // Build the OAuth2 access token
      return OAuth2AccessToken.build(
          user,
          roleCodes,
          functionCodes,
          organizationIds,
          applicationName,
          ACCESS_TOKEN_VALIDITY,
          rsaPrivateKey);

    } catch (Throwable e) {
      throw new TokenServiceException(
          "Failed to create the access token for the user (" + username + ")", e);
    }
  }
}
