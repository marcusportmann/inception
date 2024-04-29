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

package digital.inception.executor;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import digital.inception.jpa.JpaUtil;

/**
 * The <b>ExecutorConfiguration</b> class provides the Spring configuration for the Executor module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "executorEntityManagerFactory",
    basePackages = {"digital.inception.executor"})
public class ExecutorConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /**
   * Constructs a new <b>ExecutorConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   */
  public ExecutorConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the executor entity manager factory bean associated with the application data source.
   *
   * @param dataSource the application data source
   * @param platformTransactionManager the platform transaction manager
   * @return the executor entity manager factory bean associated with the application data source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean executorEntityManagerFactory(
      @Qualifier("applicationDataSource") DataSource dataSource,
      PlatformTransactionManager platformTransactionManager) {
    return JpaUtil.createEntityManager(
        "executor", dataSource, platformTransactionManager, "digital.inception.executor");
  }
}
