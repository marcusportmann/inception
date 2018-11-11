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
import digital.inception.security.ISecurityService;
import digital.inception.security.SecurityServiceAuthenticationManager;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration
  .EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration
  .AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration
  .EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers
  .AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers
  .AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AuthorizationServerConfiguration</code> class provides the OAuth2 Authorization Server
 * configuration.
 * </p>
 * Execute the following commands to generate the RSA key pair:
 * <ul>
 *   <li>
 *     openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048
 *   </li>
 *   <li>
 *     openssl rsa -in private.pem -outform PEM -pubout -out public.pem
 *   </li>
 *   <li>
 *     openssl rsa -in private.pem -out private_unencrypted.pem -outform PEM
 *   </li>
 * </ul>
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableAuthorizationServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
  /**
   * The access token validity in seconds.
   */
  public static final Integer ACCESS_TOKEN_VALIDITY = 1 * 60;

  /**
   * The refresh token validity in seconds.
   */
  public static final Integer REFRESH_TOKEN_VALIDITY = 2 * 365 * 24 * 60 * 60;

  /**
   * The private key used to sign OAuth2 tokens.
   */
  @Value("${security.oauth2.jwt.privateKey:#{null}}")
  private String jwtPrivateKey;

  /**
   * The public key used to verify OAuth2 tokens.
   */
  @Value("${security.oauth2.jwt.publicKey:#{null}}")
  private String jwtPublicKey;

  /* Security Service */
  @Autowired
  private ISecurityService securityService;

  /**
   * Returns the JWT access token converter for the authorization server.
   *
   * @return the JWT access token converter for the authorization server
   */
  public JwtAccessTokenConverter accessTokenConverter()
  {
    if (StringUtil.isNullOrEmpty(jwtPrivateKey))
    {
      throw new ConfigurationException(
          "Failed to initialize the JWT access token converter for the authorization server: "
          + "The JWT private key was not specified");
    }

    if (StringUtil.isNullOrEmpty(jwtPublicKey))
    {
      throw new ConfigurationException(
          "Failed to initialize the JWT access token converter for the authorization server: "
          + "The JWT public key was not specified");
    }

    try
    {
      JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

      converter.setSigningKey(jwtPrivateKey);

      converter.setVerifier(new RsaVerifier(jwtPublicKey));

      return converter;
    }
    catch (Throwable e)
    {
      throw new FatalBeanException(
          "Failed to initialize the JWT access token converter for the authorization server", e);
    }
  }

  /**
   * Configure the ClientDetailsService, e.g. declaring individual clients and their properties. Note that password
   * grant is not enabled (even if some clients are allowed it) unless an AuthenticationManager is supplied to the
   * configure(AuthorizationServerEndpointsConfigurer). At least one client, or a fully formed custom
   * ClientDetailsService must be declared or the server will not start.
   *
   * @param clients the client details service configurer
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception
  {
    clients.withClientDetails(new ClientDetailsService());
  }

  /**
   * Configure the non-security features of the Authorization Server endpoints, like token store,
   * token customizations, user approvals and grant types.
   *
   * @param endpoints the endpoints configurer
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints)
    throws Exception
  {
    // Set the token store
    endpoints.tokenStore(tokenStore());

    // Set the token enhancer chain
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
    endpoints.tokenEnhancer(tokenEnhancerChain);

    // Set the authentication manager
    SecurityServiceAuthenticationManager authenticationManager =
        new SecurityServiceAuthenticationManager(securityService);
    endpoints.authenticationManager(authenticationManager);

    // Set the access token converter
    endpoints.accessTokenConverter(accessTokenConverter());

    // Set the allowed token endpoint request methods
    endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

    // Set user details service
    endpoints.userDetailsService(userDetailsService());
  }

  /**
   * Configure the security of the Authorization Server, which means in practical terms the
   * /oauth/token endpoint. The /oauth/authorize endpoint also needs to be secure, but that is a
   * normal user-facing endpoint and should be secured the same way as the rest of your UI, so is
   * not covered here. The default settings cover the most common requirements, following
   * recommendations from the OAuth2 spec, so you don't need to do anything here to get a basic
   * server up and running.
   *
   * @param security a fluent configurer for security features
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security)
    throws Exception
  {
    super.configure(security);

    // Allow form authentication for clients
    security.allowFormAuthenticationForClients();

    // Set the password encoder
    security.passwordEncoder(new BCryptPasswordEncoder());

    // Set the access denied handler
    security.accessDeniedHandler(new AccessDeniedHandler());
  }

  /**
   * Returns the token enhancer for the authorization server.
   *
   * @return the token enhancer for the authorization server
   */
  @Bean
  @DependsOn("securityService")
  public TokenEnhancer tokenEnhancer()
  {
    return new TokenEnhancer(securityService);
  }

  /**
   * Returns the token services for the authorization server.
   *
   * @return the token services for the authorization server
   */
  public DefaultTokenServices tokenServices()
  {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    //defaultTokenServices.setClientDetailsService(clientDetailsService());
    defaultTokenServices.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY);
    defaultTokenServices.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY);

    return defaultTokenServices;
  }

  /**
   * Returns the OAuth2 token store for the authorization server.
   *
   * @return the OAuth2 token store for the authorization server
   */
  public TokenStore tokenStore()
  {
    return new JwtTokenStore(accessTokenConverter());
  }

  /**
   * Returns the user details service.
   *
   * @return the user details service
   */
  @Bean
  public UserDetailsService userDetailsService()
  {
    return new UserDetailsService();
  }
}
