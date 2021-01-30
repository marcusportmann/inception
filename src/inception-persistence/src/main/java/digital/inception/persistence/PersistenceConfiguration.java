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

package digital.inception.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

/**
 * The <b>PersistenceConfiguration</b> class provides the Spring configuration for the persistence
 * module and initializes the application entity manager factory bean associated with the
 * application data source.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class PersistenceConfiguration {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(PersistenceConfiguration.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The optional comma-delimited packages on the classpath to scan for JPA entities. */
  @Value("${inception.persistence.entity-packages:#{null}}")
  private String packagesToScanForEntities;

  /**
   * Constructs a new <b>PersistenceConfiguration</b>.
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
      // EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
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

      entityManagerFactoryBean.setPackagesToScan(
          StringUtils.toStringArray(packagesToScanForEntities()));
      entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

      Map<String, Object> jpaPropertyMap = entityManagerFactoryBean.getJpaPropertyMap();

      PlatformTransactionManager platformTransactionManager =
          applicationContext.getBean(PlatformTransactionManager.class);

      if (platformTransactionManager instanceof JtaTransactionManager) {
        jpaPropertyMap.put(
            "hibernate.transaction.jta.platform",
            new JtaPlatform(((JtaTransactionManager) platformTransactionManager)));
      }

      // EclipseLink
      //      jpaPropertyMap.put(
      //          "eclipselink.target-server",
      //          "digital.inception.persistence.EclipseLinkJtaTransactionController");
      //      jpaPropertyMap.put("eclipselink.weaving", "false");

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
    List<String> packagesToScan = new ArrayList<>();

    packagesToScan.add("digital.inception");

    // Add the packages to scan for entities explicitly specified in the configuration property
    if (StringUtils.hasText(this.packagesToScanForEntities)) {
      for (String packageToScanForEntities : this.packagesToScanForEntities.split(",")) {
        // Replace any existing packages to scan with the higher level package
        packagesToScan.removeIf(
            packageToScan -> packageToScan.startsWith(packageToScanForEntities));

        // Check if there is a higher level package already being scanned
        if (packagesToScan.stream().noneMatch(packageToScanForEntities::startsWith)) {
          packagesToScan.add(packageToScanForEntities);
        }
      }
    }

    // Add the base packages specified using the EnableJpaRepositories annotation
    Map<String, Object> annotatedBeans =
        applicationContext.getBeansWithAnnotation(EnableJpaRepositories.class);

    for (String beanName : annotatedBeans.keySet()) {
      Class<?> beanClass = annotatedBeans.get(beanName).getClass();

      EnableJpaRepositories enableJpaRepositories =
          beanClass.getAnnotation(EnableJpaRepositories.class);

      if (enableJpaRepositories != null) {
        for (String basePackage : enableJpaRepositories.basePackages()) {
          // Replace any existing packages to scan with the higher level package
          packagesToScan.removeIf(packageToScan -> packageToScan.startsWith(basePackage));

          // Check if there is a higher level package already being scanned
          if (packagesToScan.stream().noneMatch(basePackage::startsWith)) {
            packagesToScan.add(basePackage);
          }
        }
      }
    }

    logger.info(
        "Scanning the following packages for JPA entities: "
            + StringUtils.collectionToDelimitedString(packagesToScan, ","));

    return packagesToScan;
  }
}
