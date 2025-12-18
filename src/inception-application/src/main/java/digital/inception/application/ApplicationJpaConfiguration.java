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

import digital.inception.jpa.JpaUtil;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.StringUtils;

/**
 * The {@code ApplicationJpaConfiguration} class initialises the JPA EntityManagerFactory for the
 * application.
 *
 * @author Marcus Portmann
 */
@Configuration
@ConditionalOnClass(
    name = {
      "org.springframework.orm.jpa.JpaVendorAdapter",
      "org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean",
      "org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter",
      "org.springframework.transaction.PlatformTransactionManager"
    })
@ConditionalOnBean(name = "applicationDataSource")
public class ApplicationJpaConfiguration {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(ApplicationJpaConfiguration.class);

  private final ApplicationContext applicationContext;

  /**
   * Constructs a new {@code ApplicationJpaConfiguration}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   */
  ApplicationJpaConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the application entity manager factory bean associated with the application data
   * source.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param applicationDataSource the application data source
   * @return the application entity manager factory bean associated with the application data source
   */
  @Bean("applicationEntityManagerFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource applicationDataSource) {
    List<String> packagesToScanForEntities = JpaUtil.packagesToScanForEntities(applicationContext);

    log.info(
        "Scanning the following packages for JPA entities: "
            + StringUtils.collectionToDelimitedString(packagesToScanForEntities, ","));

    return JpaUtil.createEntityManager(
        applicationContext,
        "application",
        applicationDataSource,
        StringUtils.toStringArray(packagesToScanForEntities));
  }
}
