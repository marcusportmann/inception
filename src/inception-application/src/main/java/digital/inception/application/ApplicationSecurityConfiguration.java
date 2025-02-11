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

package digital.inception.application;

import digital.inception.core.configuration.ConfigurationException;
import digital.inception.core.util.CryptoUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.netty.http.client.HttpClient;

/**
 * The <b>ApplicationSecurityConfiguration</b> class provides access to the application security
 * configuration and initialises the application key store and trust store.
 *
 * @author Marcus Portmann
 */
@Configuration
@SuppressWarnings("unused")
public class ApplicationSecurityConfiguration implements WebClientCustomizer {

  /** The persistent application JWT. */
  @Value("${inception.application.security.jwt:#{null}}")
  private String jwt;

  /** The application key password. */
  @Value("${inception.application.security.key-password:#{null}}")
  private String keyPassword;

  /** The application key store alias. */
  @Value("${inception.application.security.key-alias:#{null}}")
  private String keyStoreAlias;

  /** The application key store password. */
  @Value("${inception.application.security.key-store-password:#{null}}")
  private String keyStorePassword;

  /** The application key store path. */
  @Value("${inception.application.security.key-store:#{null}}")
  private String keyStorePath;

  /** The application key store type. */
  @Value("${inception.application.security.key-store-type:#{null}}")
  private String keyStoreType;

  /** The application trust store password. */
  @Value("${inception.application.security.trust-store-password:#{null}}")
  private String trustStorePassword;

  /** The application trust store path. */
  @Value("${inception.application.security.trust-store:#{null}}")
  private String trustStorePath;

  /** The application trust store type. */
  @Value("${inception.application.security.trust-store-type:#{null}}")
  private String trustStoreType;

  /** Constructs a new <b>ApplicationSecurityConfiguration</b>. */
  public ApplicationSecurityConfiguration() {}

  @Override
  public void customize(Builder webClientBuilder) {
    try {
      KeyManagerFactory keyManagerFactory = null;

      if (hasKeyStoreConfiguration()) {
        keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        keyManagerFactory.init(keyStore(), keyStorePassword().toCharArray());
      }

      TrustManagerFactory trustManagerFactory = null;

      if (hasTrustStoreConfiguration()) {
        trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        trustManagerFactory.init(trustStore());
      }

      HttpClient httpClient;

      if ((keyManagerFactory != null) || (trustManagerFactory != null)) {
        SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

        if (keyManagerFactory != null) {
          sslContextBuilder.keyManager(keyManagerFactory);
        }

        if (trustManagerFactory != null) {
          sslContextBuilder.trustManager(trustManagerFactory);
        }

        SslContext sslContext = sslContextBuilder.build();

        httpClient =
            HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
      } else {
        httpClient = HttpClient.create();
      }

      ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
      webClientBuilder.clientConnector(connector);

      // Add the persistent application JWT to all API calls if configured
      if (jwt != null) {
        webClientBuilder.defaultHeader("Authorization", "Bearer " + jwt);
      }
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to apply the security configuration to the web client builder", e);
    }
  }

  /**
   * Returns whether the key store configuration has been specified.
   *
   * @return <b>true</b> if the key store configuration has been specified or <b>false</b> otherwise
   */
  public boolean hasKeyStoreConfiguration() {
    return ((StringUtils.hasText(keyStoreType))
        && (StringUtils.hasText(keyStorePath))
        && (StringUtils.hasText(keyStorePassword)));
  }

  /**
   * Returns whether the trust store configuration has been specified.
   *
   * @return <b>true</b> if the trust store configuration has been specified or <b>false</b>
   *     otherwise
   */
  public boolean hasTrustStoreConfiguration() {
    return ((StringUtils.hasText(trustStoreType)) && (StringUtils.hasText(trustStorePath)));
  }

  /**
   * Returns the application key store.
   *
   * @return the application key store
   */
  @Bean(name = "applicationKeyStore")
  @ConditionalOnProperty(
      value = {
        "inception.application.security.key-store-type",
        "inception.application.security.key-store",
        "inception.application.security.key-store-password"
      })
  public KeyStore keyStore() {
    try {
      if (!StringUtils.hasText(keyStoreType)) {
        throw new ConfigurationException(
            "The type was not specified for the application key store");
      }

      if (!StringUtils.hasText(keyStorePath)) {
        throw new ConfigurationException(
            "The path was not specified for the application key store");
      }

      if (!StringUtils.hasText(keyStorePassword)) {
        throw new ConfigurationException(
            "The password was not specified for the application key store");
      }

      return CryptoUtil.loadKeyStore(keyStoreType, keyStorePath, keyStorePassword);
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialize the application key store", e);
    }
  }

  /**
   * Returns the application key store password.
   *
   * @return the application key store password
   */
  @Bean(name = "applicationKeyStorePassword")
  public String keyStorePassword() {
    return keyStorePassword;
  }

  /**
   * Returns the application trust store.
   *
   * @return the application trust store
   */
  @Bean(name = "applicationTrustStore")
  @ConditionalOnProperty(
      value = {
        "inception.application.security.trust-store-type",
        "inception.application.security.trust-store",
        "inception.application.security.trust-store-password"
      })
  public KeyStore trustStore() {
    try {
      if (!StringUtils.hasText(trustStoreType)) {
        throw new ConfigurationException(
            "The type was not specified for the application trust store");
      }

      if (!StringUtils.hasText(trustStorePath)) {
        throw new ConfigurationException(
            "The path was not specified for the application trust store");
      }

      trustStorePassword = StringUtils.hasText(trustStorePassword) ? trustStorePassword : "";

      return CryptoUtil.loadTrustStore(trustStoreType, trustStorePath, trustStorePassword);
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialize the application trust store", e);
    }
  }
}
