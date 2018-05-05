/*
 * Copyright 2018 Marcus Portmann
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ClientDetails</code> class holds the information for an OAuth2 client.
 *
 * @author Marcus Portmann
 */
public class ClientDetails
  implements org.springframework.security.oauth2.provider.ClientDetails
{
  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the client.
   */
  private String clientId;

  /**
   * Constructs a new <code>ClientDetails</code>.
   *
   * @param clientId the Universally Unique Identifier (UUID) used to uniquely identify the client
   */
  public ClientDetails(String clientId)
  {
    this.clientId = clientId;
  }

  /**
   * Returns the access token validity period for this client.
   *
   * @return the access token validity period for this client
   */
  @Override
  public Integer getAccessTokenValiditySeconds()
  {
    return AuthorizationServerConfiguration.ACCESS_TOKEN_VALIDITY;
  }

  /**
   * Returns the information for this client, not needed by the vanilla OAuth protocol but might
   * be useful, for example, for storing descriptive information.
   *
   * @return the information for this client, not needed by the vanilla OAuth protocol but might be
   *         useful, for example, for storing descriptive information
   */
  @Override
  public Map<String, Object> getAdditionalInformation()
  {
    return null;
  }

  /**
   * Returns the authorities that are granted to the OAuth client.
   *
   * @return the authorities that are granted to the OAuth client
   */
  @Override
  public Collection<GrantedAuthority> getAuthorities()
  {
    List<GrantedAuthority> authorities = new ArrayList<>();

    authorities.add(new SimpleGrantedAuthority("read:codeCategories"));

    return authorities;
  }

  /**
   * Returns the grant types for which this client is authorized.
   *
   * @return the grant types for which this client is authorized
   */
  @Override
  public Set<String> getAuthorizedGrantTypes()
  {
    Set<String> grantTypes = new HashSet<>();

    grantTypes.add("password");
    grantTypes.add("refresh_token");

    return grantTypes;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the client.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the client
   */
  @Override
  public String getClientId()
  {
    return clientId;
  }

  /**
   * Returns the client secret.
   *
   * @return the client secret
   */
  @Override
  public String getClientSecret()
  {
    return "";
  }

  /**
   * Returns the refresh token validity period for this client.
   *
   * @return the refresh token validity period for this client
   */
  @Override
  public Integer getRefreshTokenValiditySeconds()
  {
    return AuthorizationServerConfiguration.REFRESH_TOKEN_VALIDITY;
  }

  /**
   * Returns the pre-defined redirect URI for this client to use during the "authorization_code"
   * access grant.
   *
   * @return the pre-defined redirect URI for this client to use during the "authorization_code"
   *         access grant
   */
  @Override
  public Set<String> getRegisteredRedirectUri()
  {
    return new HashSet<>();
  }

  /**
   * Returns the resources that this client can access.
   *
   * @return the resources that this client can access
   */
  @Override
  public Set<String> getResourceIds()
  {
    return new HashSet<>();
  }

  /**
   * Returns the scope of this client.
   *
   * @return the scope of this client
   */
  @Override
  public Set<String> getScope()
  {
    return new HashSet<>();
  }

  /**
   * Returns whether the client needs user approval for a particular scope.
   *
   * @param scope the scope
   *
   * @return <code>true</code> if the client does not need user approval for a particular scope or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean isAutoApprove(String scope)
  {
    return false;
  }

  /**
   * Returns whether this client is limited to a specific scope.
   *
   * @return <code>true</code> if this client is limited to a specific scope or <code>false</code>
   *         otherwise
   */
  @Override
  public boolean isScoped()
  {
    return false;
  }

  /**
   * Returns whether a secret is required to authenticate this client.
   *
   * @return <code>true</code> if a secret is required to authenticate this client or
   *         <code>false</code> otherwise
   */
  @Override
  public boolean isSecretRequired()
  {
    return false;
  }
}
