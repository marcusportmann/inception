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

package digital.inception.sms;

import digital.inception.core.CoreConfiguration;
import digital.inception.jpa.JpaUtil;
import java.util.concurrent.Executor;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * The {@code SMSConfiguration} class provides the Spring configuration for the SMS module.
 *
 * @author Marcus Portmann
 */
@Configuration
@Import(CoreConfiguration.class)
@EnableJpaRepositories(
    basePackages = {"digital.inception.sms.persistence.jpa"},
    entityManagerFactoryRef = "smsEntityManagerFactory")
@EnableScheduling
public class SMSConfiguration {

  /** Constructs a new {@code SMSConfiguration}. */
  public SMSConfiguration() {}

  /**
   * Returns the sms entity manager factory bean associated with the application data source.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param dataSource the application data source
   * @return the sms entity manager factory bean associated with the application data source
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean smsEntityManagerFactory(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource dataSource) {
    return JpaUtil.createEntityManager(
        applicationContext, "sms", dataSource, "digital.inception.sms");
  }

  /**
   * Returns the dedicated {@code ThreadPoolTaskExecutor} used to trigger sms sending
   * asynchronously.
   *
   * @return the dedicated {@code ThreadPoolTaskExecutor} used to trigger sms sending asynchronously
   */
  @Bean
  public Executor triggerSMSSendingExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(1);
    threadPoolTaskExecutor.setMaxPoolSize(2);
    threadPoolTaskExecutor.setQueueCapacity(100);
    threadPoolTaskExecutor.setThreadNamePrefix("trigger-sms-sending-task-");
    threadPoolTaskExecutor.initialize();
    return threadPoolTaskExecutor;
  }
}
