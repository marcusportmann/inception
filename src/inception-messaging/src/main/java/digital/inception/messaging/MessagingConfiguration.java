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

package digital.inception.messaging;

import digital.inception.persistence.PersistenceUtil;
import javax.sql.DataSource;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The <b>MessagingConfiguration</b> class provides the Spring configuration for the Messaging
 * module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "messagingEntityManagerFactory",
    basePackages = {"digital.inception.messaging"})
@EnableScheduling
public class MessagingConfiguration {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /**
   * Constructs a new <b>MessagingConfiguration</b>.
   *
   * @param applicationContext the Spring application context
   */
  public MessagingConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the messaging entity manager factory bean associated with the application data source.
   *
   * @return the messaging entity manager factory bean associated with the application data source
   */
  @Bean
  @DependsOn("applicationDataSource")
  public LocalContainerEntityManagerFactoryBean messagingEntityManagerFactory() {
    try {
      DataSource dataSource = applicationContext.getBean("applicationDataSource", DataSource.class);

      PlatformTransactionManager platformTransactionManager =
          applicationContext.getBean(PlatformTransactionManager.class);

      return PersistenceUtil.createEntityManager(
          "messaging", dataSource, platformTransactionManager, "digital.inception.messaging");

    } catch (Throwable e) {
      throw new FatalBeanException(
          "Failed to initialize the messaging entity manager factory bean", e);
    }
  }
}
