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

package digital.inception.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digital.inception.core.CoreConfiguration;
import digital.inception.core.jdbc.DataSourceConfiguration;
import digital.inception.core.jdbc.DataSourceUtil;
import digital.inception.jpa.JpaUtil;
import digital.inception.json.InceptionModule;
import digital.inception.r2dbc.ConnectionFactoryConfiguration;
import digital.inception.r2dbc.ConnectionFactoryUtil;
import io.r2dbc.spi.ConnectionFactory;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import liquibase.command.CommandScope;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The {@code TestConfiguration} class provides the base Spring configuration for the JUnit test
 * classes that test the capabilities provided by the Inception Framework.
 *
 * <p>This configuration class disables the default application data source and Camunda Process
 * Engine bootstrapping using the component scan filters.
 *
 * @author Marcus Portmann
 */
@Configuration
@Import(CoreConfiguration.class)
@EnableAsync
@EnableConfigurationProperties
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(
    basePackages = {"digital.inception"},
    lazyInit = true,
    excludeFilters = {
      @ComponentScan.Filter(value = SpringBootApplication.class, type = FilterType.ANNOTATION)
    })
@SuppressWarnings("WeakerAccess")
public class TestConfiguration {

  private static final Object applicationDataSourceLock = new Object();

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(TestConfiguration.class);

  private static DataSource applicationDataSource;

  /** The active Spring profiles. */
  private final String[] activeSpringProfiles;

  private final ApplicationContext applicationContext;

  /**
   * The Liquibase changelog resources on the classpath used to initialize the application database.
   */
  @Value("classpath*:**/db/*.changelog.xml")
  private Resource[] liquibaseChangelogResources;

  /**
   * Constructs a new {@code TestConfiguration}.
   *
   * @param applicationContext the Spring application context
   */
  public TestConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.activeSpringProfiles = this.applicationContext.getEnvironment().getActiveProfiles();
  }

  /**
   * Initialize the in-memory application database and return the R2DBC connection factory that can
   * be used to create reactive connections to the database.
   *
   * @return the R2DBC connection factory that can be used to create reactive connections to the
   *     in-memory application database
   */
  @Bean("applicationConnectionFactory")
  @DependsOn("applicationDataSource")
  @Primary
  public ConnectionFactory applicationConnectionFactory() {
    try {
      ConnectionFactoryConfiguration connectionFactoryConfiguration =
          new ConnectionFactoryConfiguration(
              "r2dbc:h2:mem:///"
                  + Thread.currentThread().getName()
                  + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;NON_KEYWORDS=VALUE",
              "sa",
              "",
              1,
              5,
              Duration.ofMinutes(5));

      return ConnectionFactoryUtil.initConnectionFactory(
          applicationContext, connectionFactoryConfiguration);
    } catch (Throwable e) {
      throw new BeanCreationException(
          "Failed to initialize the R2DBC application connection factory using the in-memory application database",
          e);
    }
  }

  /**
   * Initialize the in-memory application database and return a data source that can be used to
   * interact with the database.
   *
   * <p>This data source returned by this method must be closed after use with the {@code close()}
   * method.
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  @Bean("applicationDataSource")
  @Primary
  public DataSource applicationDataSource() {
    try {
      synchronized (applicationDataSourceLock) {
        if (applicationDataSource == null) {
          try {
            DataSourceConfiguration dataSourceConfiguration =
                new DataSourceConfiguration(
                    "org.h2.jdbcx.JdbcDataSource",
                    "jdbc:h2:mem:"
                        + Thread.currentThread().getName()
                        + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;NON_KEYWORDS=KEY,VALUE",
                    "sa",
                    "",
                    1,
                    5,
                    30);

            applicationDataSource =
                DataSourceUtil.initAgroalDataSource(applicationContext, dataSourceConfiguration);

            // Initialize the in-memory database using Liquibase changeSets
            try (Connection connection = applicationDataSource.getConnection()) {
              liquibase.database.Database database =
                  DatabaseFactory.getInstance()
                      .findCorrectDatabaseImplementation(new JdbcConnection(connection));

              for (Resource changelogResource : liquibaseChangelogResources) {
                if (!Objects.requireNonNull(changelogResource.getFilename())
                    .toLowerCase()
                    .endsWith("-data.changelog.xml")) {
                  String changelogFile = "db/" + changelogResource.getFilename();

                  log.info("Applying Liquibase changelog: " + changelogResource.getFilename());

                  new CommandScope("update")
                      .addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database)
                      .addArgumentValue(
                          "labels", StringUtils.arrayToCommaDelimitedString(activeSpringProfiles))
                      .addArgumentValue("changeLogFile", changelogFile)
                      .execute();
                }
              }

              for (Resource changelogResource : liquibaseChangelogResources) {
                if (Objects.requireNonNull(changelogResource.getFilename())
                    .toLowerCase()
                    .endsWith("-data.changelog.xml")) {
                  String changelogFile = "db/" + changelogResource.getFilename();

                  log.info("Applying Liquibase data changelog: " + changelogResource.getFilename());

                  new CommandScope("update")
                      .addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG, database)
                      .addArgumentValue(
                          "labels", StringUtils.arrayToCommaDelimitedString(activeSpringProfiles))
                      .addArgumentValue("changeLogFile", changelogFile)
                      .execute();
                }
              }

              DatabaseMetaData databaseMetaData = connection.getMetaData();

              try (ResultSet catalogsResultSet = databaseMetaData.getCatalogs()) {
                while (catalogsResultSet.next()) {
                  String catalogName = catalogsResultSet.getString(1);
                }
              }
            }
          } catch (Throwable e) {
            throw new RuntimeException(
                "Failed to initialize the in-memory application database", e);
          }
        }

        return applicationDataSource;
      }
    } catch (Throwable e) {
      throw new BeanCreationException(
          "Failed to initialize the application data source using the in-memory application database",
          e);
    }
  }

  /**
   * Returns the application entity manager factory bean associated with the application data
   * source.
   *
   * @param applicationContext the Spring application context
   * @param dataSource the application data source
   * @return the application entity manager factory bean associated with the application data source
   */
  @Bean("applicationEntityManagerFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource) {
    List<String> packagesToScanForEntities = JpaUtil.packagesToScanForEntities(applicationContext);

    log.info(
        "Scanning the following packages for JPA entities: "
            + StringUtils.collectionToDelimitedString(packagesToScanForEntities, ","));

    try {
      return JpaUtil.createEntityManager(
          applicationContext,
          "application",
          dataSource,
          StringUtils.toStringArray(packagesToScanForEntities));
    } catch (Throwable e) {
      throw new BeanCreationException(
          "Failed to initialize the application entity manager factory using the in-memory application database",
          e);
    }
  }

  /**
   * The {@code R2dbcEntityOperations} instance enabling reactive R2DBC operations with the
   * application database using entities.
   *
   * @param applicationConnectionFactory the R2DBC connection factory for the application database
   * @return the {@code R2dbcEntityOperations} instance enabling reactive R2DBC operations with the
   *     application database using entities
   */
  @Bean("applicationEntityOperations")
  @Primary
  public R2dbcEntityOperations applicationEntityOperations(
      @Qualifier("applicationConnectionFactory") ConnectionFactory applicationConnectionFactory) {
    return new R2dbcEntityTemplate(applicationConnectionFactory);
  }

  /**
   * Returns the cache manager.
   *
   * @return the cache manager
   */
  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager();
  }

  /**
   * Returns the Jackson2 object mapper.
   *
   * @return the Jackson2 object mapper
   */
  @Bean
  public ObjectMapper objectMapper() {
    return jackson2ObjectMapperBuilder()
        .build()
        .disable(SerializationFeature.INDENT_OUTPUT)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  /**
   * Returns the Spring task executor to use for @Async method invocations.
   *
   * @return the Spring task executor to use for @Async method invocations
   */
  @Bean
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(5);
    threadPoolTaskExecutor.setMaxPoolSize(10);
    threadPoolTaskExecutor.setQueueCapacity(100);
    threadPoolTaskExecutor.setThreadNamePrefix("inception-async-");
    threadPoolTaskExecutor.initialize();
    return threadPoolTaskExecutor;
  }

  /**
   * Returns the Spring task scheduler.
   *
   * @return the Spring task scheduler
   */
  @Bean
  public TaskScheduler taskScheduler() {
    return new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
  }

  /**
   * Returns the WebClient.Builder.
   *
   * @return the default WebClient.Builder
   */
  @Bean
  public WebClient.Builder webClientBuilder() {

    // TODO: Configure with security if available -- MARCUS

    return WebClient.builder();
  }

  /**
   * Returns the {@code Jackson2ObjectMapperBuilder} bean, which configures the Jackson JSON
   * processor package.
   *
   * @return the {@code Jackson2ObjectMapperBuilder} bean, which configures the Jackson JSON
   *     processor package
   */
  protected Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();

    /*
     * Install the custom Jackson module that supports serializing and de-serializing ISO 8601 date
     * and date/time values. The jackson-datatype-jsr310 module provided by Jackson was not used as
     * it does not handle timezones correctly for LocalDateTime, OffsetDateTime or Instant objects.
     *
     * This module also supports serializing and deserializing Enum types that implement the
     * CodeEnum interface.
     */
    jackson2ObjectMapperBuilder.modulesToInstall(new InceptionModule());

    return jackson2ObjectMapperBuilder;
  }
}
