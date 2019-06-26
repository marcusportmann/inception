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

package digital.inception.rs.oauth;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.security.ISecurityService;
import digital.inception.security.Organization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The <code>TokenEnhancer</code> class implements a token enhancer that adds additional context
 * to the OAuth2 access token.
 *
 * @author Marcus Portmann
 */
public class TokenEnhancer
  implements org.springframework.security.oauth2.provider.token.TokenEnhancer
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TokenEnhancer.class);

  /* Security Service */
  private ISecurityService securityService;

  /**
   * Constructs a new <code>TokenEnhancer</code>.
   *
   * @param securityService the Security Service
   */
  public TokenEnhancer(ISecurityService securityService)
  {
    this.securityService = securityService;
  }

  /**
   * Provides an opportunity for customization of an access token (e.g. through its additional
   * information map) during the process of creating a new token for use by a client.
   *
   * @param accessToken    the current access token with its expiration and refresh token
   * @param authentication the current authentication including client and user details
   *
   * @return a new token enhanced with additional information
   */
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
      OAuth2Authentication authentication)
  {
    Map<String, Object> additionalInfo = new HashMap<>();

    try
    {
      Object principal = authentication.getPrincipal();

      String username;

      if (principal instanceof String)
      {
        username = principal.toString();
      }
      else if (principal instanceof UserDetails)
      {
        username = ((UserDetails) principal).getUsername();
      }
      else
      {
        throw new RuntimeException("Unexpected principal type (" + principal.getClass().getName()
            + ")");
      }

      UUID userDirectoryId = securityService.getUserDirectoryIdForUser(username);

      additionalInfo.put("userDirectoryId", userDirectoryId.toString());
    }
    catch (Throwable e)
    {
      throw new TokenEnhancerException("Failed to retrieve the organizations for the user ("
          + authentication.getPrincipal() + "): " + e.getMessage(), e);
    }

    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

    return accessToken;
  }
}
