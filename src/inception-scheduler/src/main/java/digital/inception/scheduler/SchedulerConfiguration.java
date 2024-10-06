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

package digital.inception.scheduler;

import digital.inception.jpa.JpaUtil;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The <b>SchedulerConfiguration</b> class provides the Spring configuration for the Scheduler
 * module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "schedulerEntityManagerFactory",
    basePackages = {"digital.inception.scheduler"})
@EnableScheduling
public class SchedulerConfiguration {

  /** Constructs a new <b>SchedulerConfiguration</b>. */
  public SchedulerConfiguration() {}

  /**
   * Returns the scheduler entity manager factory bean associated with the application data source.
   *
   * @param dataSource the application data source
   * @param platformTransactionManager the platform transaction manager
   * @return the scheduler entity manager factory bean associated with the application data source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean schedulerEntityManagerFactory(
      @Qualifier("applicationDataSource") DataSource dataSource,
      PlatformTransactionManager platformTransactionManager) {
    return JpaUtil.createEntityManager(
        "scheduler", dataSource, platformTransactionManager, "digital.inception.scheduler");
  }
}
