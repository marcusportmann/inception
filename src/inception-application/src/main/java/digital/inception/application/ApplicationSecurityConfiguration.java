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

package digital.inception.application;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.configuration.ConfigurationException;
import digital.inception.core.util.CryptoUtil;
import java.security.KeyStore;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationSecurityConfiguration</code> class provides access to the application
 * security configuration and initialises the application key store and trust store.
 *
 * @author Marcus Portmann
 */
@Configuration
@ConditionalOnProperty(value = "application.security.keyStore.path")
@SuppressWarnings("unused")
public class ApplicationSecurityConfiguration {

  /** The application key store alias. */
  @Value("${application.security.keyStore.alias:#{null}}")
  private String keyStoreAlias;

  /** The application key store password. */
  @Value("${application.security.keyStore.password:#{null}}")
  private String keyStorePassword;

  /** The application key store path. */
  @Value("${application.security.keyStore.path:#{null}}")
  private String keyStorePath;

  /** The application key store type. */
  @Value("${application.security.keyStore.type:#{null}}")
  private String keyStoreType;

  /** The optional application trust store password. */
  @Value("${application.security.trustStore.password:#{null}}")
  private String trustStorePassword;

  /** The optional application trust store path. */
  @Value("${application.security.trustStore.path:#{null}}")
  private String trustStorePath;

  /** The optional application trust store type. */
  @Value("${application.security.trustStore.type:#{null}}")
  private String trustStoreType;

  /**
   * Returns the application key store.
   *
   * @return the application key store
   */
  @Bean(name = "applicationKeyStore")
  public KeyStore keyStore() {
    try {
      if (StringUtils.isEmpty(keyStoreType)) {
        throw new ConfigurationException(
            "The type was not specified for the application key store");
      }

      if (StringUtils.isEmpty(keyStorePath)) {
        throw new ConfigurationException(
            "The path was not specified for the application key store");
      }

      if (StringUtils.isEmpty(keyStorePassword)) {
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
  public KeyStore trustStore() {
    try {
      if (StringUtils.isEmpty(trustStoreType)) {
        throw new ConfigurationException(
            "The type was not specified for the application trust store");
      }

      if (StringUtils.isEmpty(trustStorePath)) {
        throw new ConfigurationException(
            "The path was not specified for the application trust store");
      }

      trustStorePassword = StringUtils.isEmpty(trustStorePassword) ? "" : trustStorePassword;

      return CryptoUtil.loadTrustStore(trustStoreType, trustStorePath, trustStorePassword);
    } catch (Throwable e) {
      throw new FatalBeanException("Failed to initialize the application trust store", e);
    }
  }
}
