/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.application.test;

import digital.inception.jpa.JpaUtil;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The <b>ApplicationTestConfiguration</b> class.
 *
 * @author Marcus Portmann
 */
@Configuration
@ComponentScan(basePackages = {"digital.inception.application.test"})
@EnableJpaRepositories(
    entityManagerFactoryRef = "applicationTestEntityManagerFactory",
    basePackages = {"digital.inception.application.test"})
public class ApplicationTestConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /**
   * Constructs a new <b>ApplicationTestConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   */
  public ApplicationTestConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the application test entity manager factory bean associated with the application data
   * source.
   *
   * @param dataSource the application data source
   * @param platformTransactionManager the platform transaction manager
   * @return the application test entity manager factory bean associated with the application data
   *     source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean applicationTestEntityManagerFactory(
      @Qualifier("applicationDataSource") DataSource dataSource,
      PlatformTransactionManager platformTransactionManager) {
    return JpaUtil.createEntityManager(
        "applicationTest",
        dataSource,
        platformTransactionManager,
        "digital.inception.application.test");
  }
}
