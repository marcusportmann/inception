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
import java.util.Optional;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.util.StringUtils;

/**
 * The {@code ApplicationR2dbcConfiguration} class initialises the R2DBC connection factory, R2DBC
 * dialect, mapping context, converter and entity operations for the application database using
 * Spring Data RDBC.
 *
 * <p>This implementation configures Spring Data R2DBC to use plain (unquoted) identifiers by
 * creating the {@link R2dbcMappingContext} using {@link R2dbcMappingContext#forPlainIdentifiers()}
 * or {@link R2dbcMappingContext#forPlainIdentifiers(NamingStrategy)}. This aligns the identifier
 * handling more closely with Spring JPA / Hibernate when used with H2 and avoids case-sensitive
 * quoted identifier mismatches for database objects created using unquoted Liquibase DDL.
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
   * The maximum size of the R2DBC connection pool holding R2DBC connections to the application
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
   * Constructs a new {@code ApplicationR2dbcConfiguration}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
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
   * The {@code R2dbcEntityOperations} instance enabling reactive R2DBC operations with the
   * application database using entities.
   *
   * @param connectionFactory the R2DBC connection factory for the application database
   * @param dialect the R2DBC dialect for the application database
   * @param converter the R2DBC converter for the application database
   * @return the {@code R2dbcEntityOperations} instance enabling reactive R2DBC operations with the
   *     application database using entities
   */
  @Bean("applicationEntityOperations")
  @Primary
  @Lazy(false)
  public R2dbcEntityOperations applicationEntityOperations(
      @Qualifier("applicationConnectionFactory") ConnectionFactory connectionFactory,
      @Qualifier("applicationR2dbcDialect") R2dbcDialect dialect,
      @Qualifier("applicationR2dbcConverter") R2dbcConverter converter) {

    DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);

    return new R2dbcEntityTemplate(databaseClient, dialect, converter);
  }

  /**
   * Returns the R2DBC converter for the application database.
   *
   * <p>The converter uses the application {@link R2dbcMappingContext} to map between Java objects
   * and relational rows.
   *
   * @param mappingContext the R2DBC mapping context for the application database
   * @return the R2DBC converter for the application database
   */
  @Bean("applicationR2dbcConverter")
  @Primary
  public R2dbcConverter applicationR2dbcConverter(
      @Qualifier("applicationR2dbcMappingContext") R2dbcMappingContext mappingContext) {
    return new MappingR2dbcConverter(mappingContext);
  }

  /**
   * Returns the R2DBC dialect for the application R2DBC connection factory.
   *
   * @param connectionFactory the R2DBC connection factory for the application database
   * @return the R2DBC dialect for the application R2DBC connection factory
   */
  @Bean("applicationR2dbcDialect")
  @Primary
  public R2dbcDialect applicationR2dbcDialect(
      @Qualifier("applicationConnectionFactory") ConnectionFactory connectionFactory) {
    return DialectResolver.getDialect(connectionFactory);
  }

  /**
   * Returns the R2DBC mapping context for the application database.
   *
   * <p>This Spring Data RDBC implementation configures the mapping context to use plain (unquoted)
   * identifiers using the dedicated plain-identifier factory methods.
   *
   * <p>If the managed entity types are available in the Spring application context, they are
   * applied to the mapping context. If they are not available, the mapping context is returned
   * without explicitly setting the managed types.
   *
   * @param namingStrategy the optional naming strategy used to derive table and column names
   * @param relationalManagedTypesProvider the optional provider for the managed entity types for
   *     the relational mapping context
   * @return the R2DBC mapping context for the application database
   */
  @Bean("applicationR2dbcMappingContext")
  @Primary
  public R2dbcMappingContext applicationR2dbcMappingContext(
      Optional<NamingStrategy> namingStrategy,
      ObjectProvider<RelationalManagedTypes> relationalManagedTypesProvider) {

    R2dbcMappingContext context =
        namingStrategy
            .map(R2dbcMappingContext::forPlainIdentifiers)
            .orElseGet(R2dbcMappingContext::forPlainIdentifiers);

    RelationalManagedTypes relationalManagedTypes = relationalManagedTypesProvider.getIfAvailable();
    if (relationalManagedTypes != null) {
      context.setManagedTypes(relationalManagedTypes);
    }

    return context;
  }

  /**
   * Returns the reactive transaction manager for the application R2DBC connection factory.
   *
   * @param connectionFactory the application R2DBC connection factory
   * @return the reactive transaction manager for the application R2DBC connection factory
   */
  @Bean("applicationReactiveTransactionManager")
  public ReactiveTransactionManager applicationReactiveTransactionManager(
      @Qualifier("applicationConnectionFactory") ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }
}
