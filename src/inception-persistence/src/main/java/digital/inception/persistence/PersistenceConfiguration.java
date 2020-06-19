/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.persistence;

// ~--- non-JDK imports --------------------------------------------------------

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>PersistenceConfiguration</code> class provides the Spring configuration for the
 * Persistence module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories
public class PersistenceConfiguration {

  /** The Spring application context. */
  private ApplicationContext applicationContext;

  /** The optional comma-delimited packages on the classpath to scan for JPA entities. */
  @Value("${application.database.packagesToScanForEntities:#{null}}")
  private String packagesToScanForEntities;

  /**
   * Constructs a new <code>PersistenceConfiguration</code>.
   *
   * @param applicationContext the Spring application context
   */
  public PersistenceConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the application entity manager factory bean associated with the application data
   * source.
   *
   * @return the application entity manager factory bean associated with the application data source
   */
  @Bean(name = "applicationPersistenceUnit")
  @DependsOn("applicationDataSource")
  @Primary
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
    try {
      DataSource dataSource = applicationContext.getBean("applicationDataSource", DataSource.class);

      LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
          new LocalContainerEntityManagerFactoryBean();

      HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
      jpaVendorAdapter.setGenerateDdl(false);

      try (Connection connection = dataSource.getConnection()) {
        DatabaseMetaData metaData = connection.getMetaData();

        switch (metaData.getDatabaseProductName()) {
          case "H2":
            jpaVendorAdapter.setDatabase(Database.H2);
            jpaVendorAdapter.setShowSql(true);

            break;

          case "Microsoft SQL Server":
            jpaVendorAdapter.setDatabase(Database.SQL_SERVER);
            jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLServer2012Dialect");
            jpaVendorAdapter.setShowSql(false);

            break;

          default:
            jpaVendorAdapter.setDatabase(Database.DEFAULT);
            jpaVendorAdapter.setShowSql(false);

            break;
        }
      }

      entityManagerFactoryBean.setPersistenceUnitName("applicationPersistenceUnit");
      entityManagerFactoryBean.setJtaDataSource(dataSource);

      // TODO: REMOVE THIS -- MARCUS
      entityManagerFactoryBean.setPackagesToScan(
          StringUtils.toStringArray(packagesToScanForEntities()));
      entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

      PlatformTransactionManager platformTransactionManager =
          applicationContext.getBean(PlatformTransactionManager.class);

      if (platformTransactionManager instanceof JtaTransactionManager) {
        Map<String, Object> jpaPropertyMap = entityManagerFactoryBean.getJpaPropertyMap();

        jpaPropertyMap.put(
            "hibernate.transaction.jta.platform",
            new JtaPlatform(((JtaTransactionManager) platformTransactionManager)));
      }

      return entityManagerFactoryBean;
    } catch (Throwable e) {
      throw new FatalBeanException(
          "Failed to initialize the application entity manager factory bean", e);
    }
  }

  /**
   * Returns the names of the packages to scan for JPA entities.
   *
   * @return the names of the packages to scan for JPA entities
   */
  private List<String> packagesToScanForEntities() {
    List<String> packagesToScanForEntities = new ArrayList<>();

    packagesToScanForEntities.add("digital.inception");

    if (!StringUtils.isEmpty(this.packagesToScanForEntities)) {
      String[] packagesToScan = this.packagesToScanForEntities.split(",");

      Collections.addAll(packagesToScanForEntities, StringUtils.trimArrayElements(packagesToScan));
    }

    return packagesToScanForEntities;
  }
}
