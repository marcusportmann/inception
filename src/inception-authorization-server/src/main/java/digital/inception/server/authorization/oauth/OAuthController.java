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

package digital.inception.server.authorization.oauth;

import digital.inception.security.model.AuthenticationFailedException;
import digital.inception.security.model.ExpiredPasswordException;
import digital.inception.security.model.UserLockedException;
import digital.inception.security.model.UserNotFoundException;
import digital.inception.security.service.ISecurityService;
import digital.inception.server.authorization.token.ITokenService;
import digital.inception.server.authorization.token.InvalidOAuth2RefreshTokenException;
import digital.inception.server.authorization.token.OAuth2AccessToken;
import digital.inception.server.authorization.token.OAuth2RefreshToken;
import digital.inception.server.authorization.token.RefreshedOAuth2Tokens;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The <b>OAuthController</b> class implements a simple OAuth authorization server that provides
 * support for the Resource Owner Password Grant.
 */
@Controller
@RequestMapping("oauth")
@CrossOrigin
public class OAuthController {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(OAuthController.class);

  /* Security Service */
  private final ISecurityService securityService;

  /* Token Service */
  private final ITokenService tokenService;

  /**
   * Constructs a new <b>OAuthController</b>.
   *
   * @param securityService the Security Service
   * @param tokenService the Token Service
   */
  public OAuthController(ISecurityService securityService, ITokenService tokenService) {
    this.securityService = securityService;
    this.tokenService = tokenService;
  }

  /**
   * The "token" endpoint that supports the Resource Owner Password Grant.
   *
   * @param request the HTTP servlet request
   * @param parameters the request parameter s
   * @return the token response
   */
  @PostMapping(value = "/token", produces = "application/json")
  public ResponseEntity<String> token(
      HttpServletRequest request, @RequestParam Map<String, String> parameters) {

    // TODO: Implement OAuth2 client authentication -- MARCUS

    Response response = processAccessTokenRequest(request, parameters);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
    httpHeaders.set("Pragma", "no-cache");

    return new ResponseEntity<>(response.getBody(), httpHeaders, response.getStatus());
  }

  private boolean authenticateClient(HttpServletRequest request) {

    /* TODO: Authenticate the client using the HTTP basic authentication scheme or the client ID
     *       and client secret request body parameters for the x-www-form-urlencoded request.
     *
     *       See: https://tools.ietf.org/html/rfc6749#section-2.3.1
     */

    return true;
  }

  private Response processAccessTokenRequest(
      HttpServletRequest request, Map<String, String> parameters) {

    // Retrieve the grant type associated with the request
    String grantType = parameters.get(GrantRequest.GRANT_TYPE_PARAMETER);

    // If no grant type is specified return an error
    if (!StringUtils.hasText(grantType)) {
      return new InvalidRequestErrorResponse(
          "Missing '" + GrantRequest.GRANT_TYPE_PARAMETER + "' parameter");
    }

    if (grantType.equals(ResourceOwnerPasswordCredentialsGrantRequest.GRANT_TYPE)) {
      if (!ResourceOwnerPasswordCredentialsGrantRequest.isValid(parameters)) {
        return new InvalidRequestErrorResponse(
            "Invalid 'Resource Owner Password Credentials Grant' request");
      }

      ResourceOwnerPasswordCredentialsGrantRequest grantRequest =
          new ResourceOwnerPasswordCredentialsGrantRequest(parameters);

      return processResourceOwnerPasswordCredentialsGrantRequest(grantRequest);
    } else if (grantType.equals(RefreshAccessTokenGrantRequest.GRANT_TYPE)) {
      if (!RefreshAccessTokenGrantRequest.isValid(parameters)) {
        return new InvalidRequestErrorResponse("Invalid 'Refresh Access Token Grant' request");
      }

      RefreshAccessTokenGrantRequest grantRequest = new RefreshAccessTokenGrantRequest(parameters);

      return processRefreshAccessTokenGrantRequest(grantRequest);
    } else {
      return new UnsupportedGrantTypeErrorResponse(grantType);
    }
  }

  private Response processRefreshAccessTokenGrantRequest(RefreshAccessTokenGrantRequest request) {
    try {
      RefreshedOAuth2Tokens refreshedOAuth2Tokens =
          tokenService.refreshOAuth2Tokens(request.getRefreshToken());

      if (refreshedOAuth2Tokens.getRefreshToken() == null) {
        return new ResourceOwnerPasswordCredentialsGrantResponse(
            refreshedOAuth2Tokens.getAccessToken().getTokenValue(),
            refreshedOAuth2Tokens.getAccessToken().getExpiresIn());
      } else {
        return new ResourceOwnerPasswordCredentialsGrantResponse(
            refreshedOAuth2Tokens.getAccessToken().getTokenValue(),
            refreshedOAuth2Tokens.getAccessToken().getExpiresIn(),
            refreshedOAuth2Tokens.getRefreshToken().getTokenValue());
      }
    } catch (InvalidOAuth2RefreshTokenException e) {
      return new InvalidGrantErrorResponse("Invalid refresh token");
    } catch (Throwable e) {
      log.error("Failed to process the 'Refresh Access Token Grant' request", e);
      return new SystemUnavailableResponse(
          "Failed to process the 'Refresh Access Token Grant' request", e);
    }
  }

  private Response processResourceOwnerPasswordCredentialsGrantRequest(
      ResourceOwnerPasswordCredentialsGrantRequest request) {

    try {
      UUID userDirectoryId =
          securityService.authenticate(request.getUsername(), request.getPassword());

      OAuth2AccessToken oAuth2AccessToken =
          tokenService.issueOAuth2AccessToken(
              request.getUsername(),
              (request.getScope() != null) ? Set.of(request.getScope().split(" ")) : null);

      OAuth2RefreshToken oAuth2RefreshToken =
          tokenService.issueOAuth2RefreshToken(
              oAuth2AccessToken.getSubject(),
              (request.getScope() != null) ? Set.of(request.getScope().split(" ")) : null);

      return new ResourceOwnerPasswordCredentialsGrantResponse(
          oAuth2AccessToken.getTokenValue(),
          oAuth2AccessToken.getExpiresIn(),
          oAuth2RefreshToken.getTokenValue());
    } catch (AuthenticationFailedException | UserNotFoundException e) {
      return new InvalidGrantErrorResponse("Bad credentials");
    } catch (UserLockedException e) {
      return new InvalidGrantErrorResponse("User locked");
    } catch (ExpiredPasswordException e) {
      return new InvalidGrantErrorResponse("Credentials expired");
    } catch (Throwable e) {
      log.error(
          "Failed to process the 'Resource Owner Password Credentials Grant' request for the user ("
              + request.getUsername()
              + ")",
          e);
      return new SystemUnavailableResponse(
          "Failed to process the 'Resource Owner Password Credentials Grant' request for the user '"
              + request.getUsername()
              + "'",
          e);
    }
  }
}
