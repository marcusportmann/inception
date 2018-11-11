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

import digital.inception.core.configuration.ConfigurationException;
import digital.inception.core.util.StringUtil;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * The <code>ResourceServerConfiguration</code> class provides the OAuth2 Resource Server
 * configuration.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{
  /**
   * The public key used to verify OAuth2 tokens.
   */
  @Value("${security.oauth2.jwt.publicKey:#{null}}")
  private String jwtPublicKey;

  /**
   * Returns the OAuth2 access token converter for the resource server.
   *
   * @return the OAuth2 access token converter for the resource server
   */
  public JwtAccessTokenConverter accessTokenConverter()
  {
    if (StringUtil.isNullOrEmpty(jwtPublicKey))
    {
      throw new ConfigurationException("Failed to initialize the JWT access token converter for the resource server: "
          + "The JWT public key was not specified");
    }

    try
    {
      JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

      converter.setVerifier(new RsaVerifier(jwtPublicKey));

      return converter;
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialize the JWT access token converter for the resource server", e);
    }
  }

  @Override
  public void configure(HttpSecurity http)
    throws Exception
  {
    // TODO: Enable secure access based on configuration -- MARCUS
    // http.requiresChannel().anyRequest().requiresSecure();

    // Enable anonymous access to the SpringFox Swagger resources
    http.authorizeRequests().antMatchers("/**").permitAll();
    http.authorizeRequests().antMatchers("/api/**").authenticated();

    // All other requests need to be authenticated
    http.authorizeRequests().anyRequest().authenticated();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer config)
  {
    config.tokenServices(tokenServices());
  }

  /**
   * Returns the OAuth2 token services for the resource server.
   *
   * @return the OAuth2 token services for the resource server
   */
  public DefaultTokenServices tokenServices()
  {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());

    return defaultTokenServices;
  }

  /**
   * Returns the OAuth2 token store for the resource server.
   *
   * @return the OAuth2 token store for the resource server
   */
  public TokenStore tokenStore()
  {
    return new JwtTokenStore(accessTokenConverter());
  }
}
