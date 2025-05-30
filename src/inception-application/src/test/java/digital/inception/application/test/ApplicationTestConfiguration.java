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

/**
 * The {@code ApplicationTestConfiguration} class.
 *
 * @author Marcus Portmann
 */
@Configuration
@ComponentScan(basePackages = {"digital.inception.application.test"})
@EnableJpaRepositories(
    basePackages = {"digital.inception.application.test"},
    entityManagerFactoryRef = "applicationTestEntityManagerFactory")
public class ApplicationTestConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /**
   * Constructs a new {@code ApplicationTestConfiguration}.
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
   * @param applicationContext the Spring application context
   * @param dataSource the application data source
   * @return the application test entity manager factory bean associated with the application data
   *     source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean applicationTestEntityManagerFactory(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource) {
    return JpaUtil.createEntityManager(
        applicationContext, "applicationTest", dataSource, "digital.inception.application.test");
  }
}
