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
package digital.inception.oauth2.server.authorization.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import digital.inception.oauth2.server.authorization.token.ITokenService;
import digital.inception.oauth2.server.authorization.token.OAuth2AccessToken;
import digital.inception.security.AuthenticationFailedException;
import digital.inception.security.ExpiredPasswordException;
import digital.inception.security.ISecurityService;
import digital.inception.security.UserLockedException;
import digital.inception.security.UserNotFoundException;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerController.class);

  /* Security Service */
  private ISecurityService securityService;

  /* Token Service */
  private ITokenService tokenService;


  /**
   * Constructs a new <code>AuthorizationServerController</code>.
   *
   * @param securityService the Security Service
   * @param tokenService the Token Service
   */
  public AuthorizationServerController(ISecurityService securityService, ITokenService tokenService) {
    this.securityService = securityService;
    this.tokenService = tokenService;
  }

//  @PostConstruct
//  protected void initialiseRSAJWK() {
//    try {
//      rsaJWK = new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).build();
//    }
//    catch (Throwable e) {
//      throw new FatalBeanException("Failed to initialise the RSA JWK", e);
//    }
//  }



  private Response processResourceOwnerPasswordCredentialsGrantRequest(ResourceOwnerPasswordCredentialsGrantRequest request) {

    try {
      UUID userDirectoryId = securityService.authenticate(request.getUsername(), request.getPassword());

      OAuth2AccessToken oAuth2AccessToken = tokenService.issueOAuth2AccessToken(request.getUsername());

      if (false) {
        throw new RuntimeException("Testing 1.. 2.. 3..");
      }



      return new ResourceOwnerPasswordCredentialsGrantResponse(oAuth2AccessToken.getTokenValue(), oAuth2AccessToken.getExpiresIn());


    }
    catch (AuthenticationFailedException | UserNotFoundException e) {
      return new InvalidGrantErrorResponse("Bad credentials");
    }
    catch (UserLockedException e) {
      return new InvalidGrantErrorResponse("User locked");
    }
    catch (ExpiredPasswordException e) {
      return new InvalidGrantErrorResponse("Credentials expired");
    }
    catch (Throwable e) {
      logger.error("Failed to authenticate the user (" + request.getUsername() + ")", e);
      return new SystemUnavailableResponse("Failed to authenticate the user '" + request.getUsername() + "'", e);
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

    // TODO: Implement client authentication -- MARCUS

    Response response = processAccessTokenRequest(request, parameters);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
    httpHeaders.set("Pragma", "no-cache");

    return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatus());
  }

  private Response processAccessTokenRequest(HttpServletRequest request, Map<String, String> parameters) {

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
