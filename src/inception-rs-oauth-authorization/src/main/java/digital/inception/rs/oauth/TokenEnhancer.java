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

package digital.inception.rs.oauth;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.security.ISecurityService;
import digital.inception.security.User;
import digital.inception.security.UserDetails;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TokenEnhancer</code> class implements a token enhancer that adds additional context to
 * the OAuth2 access token.
 *
 * @author Marcus Portmann
 */
public class TokenEnhancer
    implements org.springframework.security.oauth2.provider.token.TokenEnhancer {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TokenEnhancer.class);

  /* Security Service */
  private ISecurityService securityService;

  /**
   * Constructs a new <code>TokenEnhancer</code>.
   *
   * @param securityService the Security Service
   */
  public TokenEnhancer(ISecurityService securityService) {
    this.securityService = securityService;
  }

  /**
   * Provides an opportunity for customization of an access token (e.g. through its additional
   * information map) during the process of creating a new token for use by a client.
   *
   * @param accessToken the current access token with its expiration and refresh token
   * @param authentication the current authentication including client and user details
   * @return a new token enhanced with additional information
   */
  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    Map<String, Object> additionalInfo = new HashMap<>();

    try {
      UserDetails userDetails =
          UserDetails.class.isInstance(authentication.getPrincipal())
              ? UserDetails.class.cast(authentication.getPrincipal())
              : null;

      if (userDetails != null) {
        User user = userDetails.getUser();

        additionalInfo.put("user_directory_id", user.getUserDirectoryId().toString());

        if ((!StringUtils.isEmpty(user.getFirstName()))
            && (!StringUtils.isEmpty(user.getFirstName()))) {
          additionalInfo.put("user_full_name", user.getFirstName() + " " + user.getLastName());
        } else if (!StringUtils.isEmpty(user.getFirstName())) {
          additionalInfo.put("user_full_name", user.getFirstName());
        } else if (!StringUtils.isEmpty(user.getEmail())) {
          additionalInfo.put("user_full_name", user.getEmail());
        } else {
          additionalInfo.put("user_full_name", user.getUsername());
        }
      }
    } catch (Throwable e) {
      throw new TokenEnhancerException(
          "Failed to retrieve the organizations for the user ("
              + authentication.getPrincipal()
              + "): "
              + e.getMessage(),
          e);
    }

    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

    return accessToken;
  }
}
