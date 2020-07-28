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

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import digital.inception.security.ISecurityService;
import digital.inception.security.User;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;
import java.util.Set;
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

  /** The access token validity in seconds. */
  public static final int ACCESS_TOKEN_VALIDITY = 5 * 60;

  /**
   * The period in seconds prior to expiry of the refresh token during which it will be
   * automatically re-issued.
   */
  public static final int REFRESH_TOKEN_REISSUE_INTERVAL = 90 * 24 * 60 * 60;

  /** The refresh token validity in seconds. */
  public static final int REFRESH_TOKEN_VALIDITY = 365 * 24 * 60 * 60;

  /* Security Service */
  private final ISecurityService securityService;

  /* The application name. */
  @Value("${spring.application.name:#{null}}")
  private String applicationName;

  /* The RSA private key used to sign the JWTs. */
  @Value("${inception.oauth2.authorizationserver.jwt.private-key-location}")
  private RSAPrivateKey rsaPrivateKey;

  /* The RSA public key used to verify the JWTs. */
  @Value("${inception.oauth2.authorizationserver.jwt.public-key-location}")
  private RSAPublicKey rsaPublicKey;

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
   * @param scopes the optional scope(s) associated to the access token
   * @return the OAuth2 access token
   */
  public OAuth2AccessToken issueOAuth2AccessToken(String username, Set<String> scopes)
      throws TokenServiceException {
    try {
      return createOAuth2AccessToken(username, scopes);
    } catch (Throwable e) {
      throw new TokenServiceException(
          "Failed to issue the OAuth2 access token for the user (" + username + ")", e);
    }
  }

  /**
   * Issue an OAuth2 refresh token for the specified user.
   *
   * @param username the username identifying the user
   * @param scopes the optional scope(s) associated to the refresh token
   * @return the OAuth2 refresh token
   */
  public OAuth2RefreshToken issueOAuth2RefreshToken(String username, Set<String> scopes)
      throws TokenServiceException {
    try {
      return createOAuth2RefreshToken(username, scopes);
    } catch (Throwable e) {
      throw new TokenServiceException(
          "Failed to issue the OAuth2 refresh token for the user (" + username + ")", e);
    }
  }

  /**
   * Refresh an OAuth2 access token and if required the OAuth2 refresh token.
   *
   * @param encodedOAuth2RefreshToken the encoded OAuth2 refresh token
   * @return the refreshed tokens
   */
  public RefreshedOAuth2Tokens refreshOAuth2Tokens(String encodedOAuth2RefreshToken)
      throws InvalidOAuth2RefreshTokenException, TokenServiceException {
    try {
      SignedJWT signedJWT = SignedJWT.parse(encodedOAuth2RefreshToken);

      RSASSAVerifier verifier = new RSASSAVerifier(rsaPublicKey);

      if (!signedJWT.verify(verifier)) {
        throw new InvalidOAuth2RefreshTokenException();
      }

      JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

      String username = claimsSet.getSubject();
      String scopeClaim = claimsSet.getStringClaim(OAuth2AccessToken.SCOPE_CLAIM);
      Instant issuedAt = claimsSet.getIssueTime().toInstant();
      Instant expiresAt = claimsSet.getExpirationTime().toInstant();

      if (Instant.now().isAfter(expiresAt)) {
        throw new InvalidOAuth2RefreshTokenException();
      }

      OAuth2AccessToken accessToken =
          createOAuth2AccessToken(
              username, (scopeClaim != null) ? Set.of(scopeClaim.split(" ")) : null);

      if (Instant.now().isBefore(expiresAt.minusSeconds(REFRESH_TOKEN_REISSUE_INTERVAL))) {
        return new RefreshedOAuth2Tokens(accessToken, null);
      } else {
        OAuth2RefreshToken refreshToken =
            createOAuth2RefreshToken(
                username, (scopeClaim != null) ? Set.of(scopeClaim.split(" ")) : null);

        return new RefreshedOAuth2Tokens(accessToken, refreshToken);
      }
    } catch (InvalidOAuth2RefreshTokenException e) {
      throw e;
    } catch (Throwable e) {
      throw new TokenServiceException("Failed to refresh the OAuth tokens", e);
    }
  }

  private OAuth2AccessToken createOAuth2AccessToken(String username, Set<String> scopes)
      throws TokenCreationException {
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
          scopes,
          applicationName,
          ACCESS_TOKEN_VALIDITY,
          rsaPrivateKey);
    } catch (Throwable e) {
      throw new TokenCreationException("Failed to create the OAuth2 access token", e);
    }
  }

  private OAuth2RefreshToken createOAuth2RefreshToken(String username, Set<String> scopes)
      throws TokenCreationException {
    try {
      return OAuth2RefreshToken.build(username, scopes, REFRESH_TOKEN_VALIDITY, rsaPrivateKey);
    } catch (Throwable e) {
      throw new TokenCreationException("Failed to create the OAuth2 refresh token", e);
    }
  }
}
