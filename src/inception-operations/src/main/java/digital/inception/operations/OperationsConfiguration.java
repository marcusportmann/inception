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

package digital.inception.operations;

import digital.inception.jpa.JpaUtil;
import java.util.concurrent.Executor;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * The {@code OperationsConfiguration} class provides the Spring configuration for the Operations
 * module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "operationsEntityManagerFactory",
    basePackages = {"digital.inception.operations.persistence.jpa"})
public class OperationsConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /**
   * Constructs a new {@code OperationsConfiguration}.
   *
   * @param applicationContext the Spring application context
   */
  public OperationsConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the operations entity manager factory bean associated with the application data source.
   *
   * @param applicationContext the Spring application context
   * @param dataSource the application data source
   * @return the operations entity manager factory bean associated with the application data source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean operationsEntityManagerFactory(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource) {
    return JpaUtil.createEntityManager(
        applicationContext, "operations", dataSource, "digital.inception.operations");
  }

  /**
   * Returns the dedicated {@code ThreadPoolTaskExecutor} used to trigger interaction processing
   * asynchronously.
   *
   * @return the dedicated {@code ThreadPoolTaskExecutor} used to trigger interaction processing
   *     asynchronously
   */
  @Bean
  public Executor triggerInteractionProcessingExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(1);
    threadPoolTaskExecutor.setMaxPoolSize(2);
    threadPoolTaskExecutor.setQueueCapacity(100);
    threadPoolTaskExecutor.setThreadNamePrefix("trigger-interaction-processing-task-");
    threadPoolTaskExecutor.initialize();
    return threadPoolTaskExecutor;
  }

  /**
   * Returns the dedicated {@code ThreadPoolTaskExecutor} used to trigger interaction source
   * synchronization asynchronously.
   *
   * @return the dedicated {@code ThreadPoolTaskExecutor} used to trigger interaction source
   *     synchronization asynchronously
   */
  @Bean
  public Executor triggerInteractionSourceSynchronizationExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(1);
    threadPoolTaskExecutor.setMaxPoolSize(2);
    threadPoolTaskExecutor.setQueueCapacity(100);
    threadPoolTaskExecutor.setThreadNamePrefix("trigger-interaction-source-synchronization-task-");
    threadPoolTaskExecutor.initialize();
    return threadPoolTaskExecutor;
  }
}
