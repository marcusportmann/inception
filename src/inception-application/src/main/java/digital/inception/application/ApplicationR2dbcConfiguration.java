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

import digital.inception.r2dbc.ConnectionFactoryConfiguration;
import digital.inception.r2dbc.ConnectionFactoryUtil;
import io.r2dbc.spi.ConnectionFactory;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.util.StringUtils;

/**
 * The <b>ApplicationR2dbcConfiguration</b> class initialises the R2DBC connection factory and R2DBC
 * entity operations for the application database.
 *
 * @author Marcus Portmann
 */
@Configuration
@ConditionalOnClass(
    name = {
      "io.r2dbc.spi.ConnectionFactory",
      "org.springframework.data.r2dbc.core.R2dbcEntityOperations"
    })
@ConditionalOnProperty({"inception.application.r2dbc-connection-factory.url"})
public class ApplicationR2dbcConfiguration {

  private final ApplicationContext applicationContext;

  /**
   * The initial size of the R2DBC connection pool holding R2DBC connections to the application
   * database.
   */
  @Value("${inception.application.r2dbc-connection-factory.initial-pool-size:1}")
  private int initialPoolSize;

  /**
   * The maximum amount of time a R2DBC connection to the application database can remain idle
   * before it is closed.
   */
  @Value("${inception.application.r2dbc-connection-factory.max-idle-time:50m}")
  private Duration maxIdleTime;

  /**
   * The maximum size of the R2DBC connection pool holding R2DBC connects to the application
   * database.
   */
  @Value("${inception.application.r2dbc-connection-factory.max-pool-size:5}")
  private int maxPoolSize;

  /** The password used to create R2DBC connections to the application database. */
  @Value("${inception.application.r2dbc-connection-factory.password:#{null}}")
  private String password;

  /** The URL used to create R2DBC connections to the application database. */
  @Value("${inception.application.r2dbc-connection-factory.url:#{null}}")
  private String url;

  /** The username used to create R2DBC connections to the application database. */
  @Value("${inception.application.r2dbc-connection-factory.username:#{null}}")
  private String username;

  /**
   * Constructs a new <b>ApplicationR2dbcConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   */
  ApplicationR2dbcConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the R2DBC connection factory that can be used to create reactive connections to the
   * application database.
   *
   * @return the R2DBC connection factory that can be used to create reactive connections to the
   *     application database
   */
  @Bean("applicationConnectionFactory")
  @Primary
  public ConnectionFactory applicationConnectionFactory() {
    ConnectionFactoryConfiguration connectionFactoryConfiguration =
        new ConnectionFactoryConfiguration(
            url,
            StringUtils.hasText(username) ? username : "",
            StringUtils.hasText(password) ? password : "",
            initialPoolSize,
            maxPoolSize,
            maxIdleTime);

    return ConnectionFactoryUtil.initConnectionFactory(
        applicationContext, connectionFactoryConfiguration);
  }

  /**
   * The <b>R2dbcEntityOperations</b> instance enabling reactive R2DBC operations with the
   * application database using entities.
   *
   * @param applicationConnectionFactory the R2DBC connection factory for the application database
   * @return the <b>R2dbcEntityOperations</b> instance enabling reactive R2DBC operations with the
   *     application database using entities
   */
  @Bean("applicationEntityOperations")
  @Primary
  public R2dbcEntityOperations applicationEntityOperations(
      @Qualifier("applicationConnectionFactory") ConnectionFactory applicationConnectionFactory) {
    return new R2dbcEntityTemplate(applicationConnectionFactory);
  }

  /**
   * Returns the reactive transaction manager for the application R2DBC connection factory.
   *
   * @param applicationConnectionFactory the application R2DBC connection factory
   * @return the reactive transaction manager for the application R2DBC connection factory
   */
  @Bean("applicationReactiveTransactionManager")
  public ReactiveTransactionManager applicationReactiveTransactionManager(
      @Qualifier("applicationConnectionFactory") ConnectionFactory applicationConnectionFactory) {
    return new R2dbcTransactionManager(applicationConnectionFactory);
  }
}
