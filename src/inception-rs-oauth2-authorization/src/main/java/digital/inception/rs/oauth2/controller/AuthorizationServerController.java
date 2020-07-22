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
package digital.inception.rs.oauth2.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;
import digital.inception.security.AuthenticationFailedException;
import digital.inception.security.ExpiredPasswordException;
import digital.inception.security.ISecurityService;
import digital.inception.security.User;
import digital.inception.security.UserLockedException;
import digital.inception.security.UserNotFoundException;
import java.io.StringWriter;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The <code>AuthorizationServerController</code> class implements a simple OAuth authorization
 * server that provides support for the Resource Owner Password Grant.
 */
@Controller
@RequestMapping("oauth")
public class AuthorizationServerController  {

  /** The access token validity in seconds (24 hours). */
  public static final int ACCESS_TOKEN_VALIDITY = 86400;

  /** The refresh token validity in seconds. */
  public static final int REFRESH_TOKEN_VALIDITY = 2 * 365 * 24 * 60 * 60;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerController.class);

  /* Security Service */
  private ISecurityService securityService;

  /* The application name. */
  @Value("${spring.application.name:#{null}}")
  private String applicationName;

  /* The RSA private key used to sign the JWTs. */
  @Value("${security.oauth2.jwt.privateKey}")
  private RSAPrivateKey rsaPrivateKey;

  /* The RSA public key used to sign the JWTs. */
  @Value("${security.oauth2.jwt.publicKey}")
  private RSAPublicKey rsaPublicKey;

  /* The RSA JWK used to sign the JWTs. */
  private RSAKey rsaJWK;

  /**
   * Constructs a new <code>AuthorizationServerController</code>.
   *
   * @param securityService the Security Service
   */
  public AuthorizationServerController(ISecurityService securityService) {
    this.securityService = securityService;
  }

  @PostConstruct
  protected void initialiseRSAJWK() {
    try {
      rsaJWK = new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).build();
    }
    catch (Throwable e) {
      throw new FatalBeanException("Failed to initialise the RSA JWK", e);
    }
  }

  private Response processResourceOwnerPasswordCredentialsGrantRequest(ResourceOwnerPasswordCredentialsGrantRequest request) {

    try {
      UUID userDirectoryId = securityService.authenticate(username, password);

      // Retrieve the details for the user
      User user = securityService.getUser(userDirectoryId, username);

      // Retrieve the function codes for the user
      List<String> functionCodes =
          securityService.getFunctionCodesForUser(userDirectoryId, username);

      // Retrieve the list of IDs for the organizations the user is associated with
      List<UUID> organizationIds =
          securityService.getOrganizationIdsForUserDirectory(userDirectoryId);

      /*
       * Retrieve the list of IDs for the user directories the user is associated with as a result
       * of being associated with one or more organizations.
       */
      List<UUID> userDirectoryIdsForOrganizations = new ArrayList<>();

      for (var organizationId : organizationIds) {
        // Retrieve the list of user directories associated with the organization
        var userDirectoryIdsForOrganization =
            securityService.getUserDirectoryIdsForOrganization(organizationId);

        userDirectoryIdsForOrganizations.addAll(userDirectoryIdsForOrganization);
      }

      // Retrieve the list of roles for the user
      List<String> roleCodes = securityService.getRoleCodesForUser(userDirectoryId, username);

      JWSSigner signer = new RSASSASigner(rsaPrivateKey);

      JWTClaimsSet.Builder jwtClaimsSetBuilder = new Builder();

      jwtClaimsSetBuilder.subject(user.getUsername());

      if (!StringUtils.isEmpty(applicationName)) {
        jwtClaimsSetBuilder.issuer(applicationName);
      }

      if ((!StringUtils.isEmpty(user.getFirstName()))
          && (!StringUtils.isEmpty(user.getFirstName()))) {
        jwtClaimsSetBuilder.claim("user_full_name", user.getFirstName() + " " + user.getLastName());
      } else if (!StringUtils.isEmpty(user.getFirstName())) {
        jwtClaimsSetBuilder.claim("user_full_name", user.getFirstName());
      } else if (!StringUtils.isEmpty(user.getEmail())) {
        jwtClaimsSetBuilder.claim("user_full_name", user.getEmail());
      } else {
        jwtClaimsSetBuilder.claim("user_full_name", user.getUsername());
      }

      jwtClaimsSetBuilder.claim("user_directory_id", userDirectoryId.toString());

      jwtClaimsSetBuilder.expirationTime(new Date(System.currentTimeMillis() + (ACCESS_TOKEN_VALIDITY * 1000L)));

      SignedJWT signedJWT = new SignedJWT(
          new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
          jwtClaimsSetBuilder.build());

      signedJWT.sign(signer);



      return new ResourceOwnerPasswordCredentialsGrantResponse(signedJWT.serialize());







      ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(stringWriter.getBuffer().toString());
    }
    catch (AuthenticationFailedException | UserNotFoundException e) {
      return new

      return createErrorResponse("invalid_grant", "Bad credentials");
    }
    catch (UserLockedException e) {
      return createErrorResponse("invalid_grant", "User locked");
    }
    catch (ExpiredPasswordException e) {
      return createErrorResponse("invalid_grant", "Credentials expired");
    }
    catch (Throwable e) {
      logger.error("Failed to authenticate the user (" + username + ")", e);
      return new ResponseEntity<>("Failed to authenticate the user '" + username + "': " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  /**
   * The "token" endpoint that supports the Resource Owner Password Grant.
   *
   * @param parameters the request parameters
   * @return the token response
   */
  @PostMapping(value = "/token", produces = "application/json")
  public ResponseEntity<String> token(HttpServletRequest request, @RequestParam Map<String, String> parameters) {
    Response response = processAccessTokenRequest(request, parameters);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
    httpHeaders.set("Pragma", "no-cache");

    return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatus());
  }

  private Response processAccessTokenRequest(HttpServletRequest request, Map<String, String> parameters) {
    // Authenticate the client associated with the request
    if (!authenticateClient(request)) {
      return new InvalidClientErrorResponse()
    }

    // Retrieve the grant type associated with the request
    String grantType = parameters.get(GrantRequest.GRANT_TYPE_PARAMETER);

    // If no grant type is specified return an error
    if (StringUtils.isEmpty(grantType)) {
      return new InvalidRequestErrorResponse("Missing '" + GrantRequest.GRANT_TYPE_PARAMETER+ "' parameter");
    }

    if (grantType.equals(ResourceOwnerPasswordCredentialsGrantRequest.GRANT_TYPE)) {
      if (!ResourceOwnerPasswordCredentialsGrantRequest.isValid(parameters)) {
        return new InvalidRequestErrorResponse("Invalid 'Resource Owner Password Credentials Grant' request");
      }

      ResourceOwnerPasswordCredentialsGrantRequest grantRequest = new ResourceOwnerPasswordCredentialsGrantRequest(parameters);

      return processResourceOwnerPasswordCredentialsGrantRequest(grantRequest);
    }
//    else if (grantType.equals("refresh_token")) {
//
//    }
    else {
      return new UnsupportedGrantTypeErrorResponse(grantType);
    }

  }

  private boolean authenticateClient(HttpServletRequest request) {

    /* TODO: Authenticate the client using the HTTP basic authentication scheme or the client ID
     *       and client secret request body parameters for the x-www-form-urlencoded request.
     *
     *       See: https://tools.ietf.org/html/rfc6749#section-2.3.1
     */

    return true;
  }

}
