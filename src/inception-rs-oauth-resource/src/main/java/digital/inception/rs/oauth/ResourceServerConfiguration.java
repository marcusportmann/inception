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

import digital.inception.core.configuration.ConfigurationException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.StringUtils;

/**
 * The <code>ResourceServerConfiguration</code> class provides the OAuth2 Resource Server
 * configuration.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The public key used to verify OAuth2 tokens.
   */
  @Value("${security.oauth2.jwt.publicKey:#{null}}")
  private String jwtPublicKey;

  /**
   * The fully qualified class name for the custom <code>UserAuthenticationConverter</code>
   * implementation that should be used to create Spring <code>Authentication</code> instances from
   * OAuth2 access tokens. This allows additional security information, e.g. additional authorities,
   * to be added to the <code>Authentication</code> context beyond what is included in the OAuth2
   * access tokens.
   */
  @Value("${security.userAuthenticationConverter:#{null}}")
  private String userAuthenticationConverter;

  /**
   * Constructs a new <code>ResourceServerConfiguration</code>.
   *
   * @param applicationContext the Spring application context
   */
  public ResourceServerConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the OAuth2 access token converter for the resource server.
   *
   * @return the OAuth2 access token converter for the resource server
   */
  public JwtAccessTokenConverter accessTokenConverter() {
    if (StringUtils.isEmpty(jwtPublicKey)) {
      throw new ConfigurationException(
          "Failed to initialize the JWT access token converter for the resource server: "
              + "The JWT public key was not specified");
    }

    try {
      JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

      if (!StringUtils.isEmpty(userAuthenticationConverter)) {
        try {
          Class<?> userAuthenticationConverterClass = Thread.currentThread().getContextClassLoader()
              .loadClass(userAuthenticationConverter);

          if (!UserAuthenticationConverter.class
              .isAssignableFrom(userAuthenticationConverterClass)) {
            throw new RuntimeException("The user authentication converter class ("
                + userAuthenticationConverter
                + ") does not implement the UserAuthenticationConverter interface");
          }

          UserAuthenticationConverter userAuthenticationConverter =
              userAuthenticationConverterClass.asSubclass(UserAuthenticationConverter.class)
                  .getConstructor().newInstance();

          applicationContext.getAutowireCapableBeanFactory().autowireBean(
              userAuthenticationConverter);

          DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

          accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);

          converter.setAccessTokenConverter(accessTokenConverter);
        } catch (Throwable e) {
          throw new RuntimeException(
              "Failed to initialize the custom UserAuthenticationConverter implementation ("
                  + userAuthenticationConverter + ")", e);
        }
      }

      converter.setVerifier(new RsaVerifier(jwtPublicKey));

      return converter;
    } catch (Throwable e) {
      throw new FatalBeanException(
          "Failed to initialize the JWT access token converter for the resource server", e);
    }
  }

  @Override
  public void configure(HttpSecurity http)
      throws Exception {
    // TODO: Enable secure access based on configuration -- MARCUS
    // http.requiresChannel().anyRequest().requiresSecure();

    // Enable anonymous access to the SpringFox Swagger resources
    http.authorizeRequests().antMatchers("/**").permitAll();
    http.authorizeRequests().antMatchers("/api/**").authenticated();

    // All other requests need to be authenticated
    http.authorizeRequests().anyRequest().authenticated();
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer config) {
    config.tokenServices(tokenServices());
  }

  /**
   * Returns the OAuth2 token services for the resource server.
   *
   * @return the OAuth2 token services for the resource server
   */
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());

    return defaultTokenServices;
  }

  /**
   * Returns the OAuth2 token store for the resource server.
   *
   * @return the OAuth2 token store for the resource server
   */
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }
}
