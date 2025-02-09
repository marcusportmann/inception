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

package digital.inception.jpa;

import io.agroal.api.AgroalDataSource;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

/**
 * The <b>JpaUtil</b> class provides JPA-related utility methods.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class JpaUtil {

  /** Private constructor to prevent instantiation. */
  private JpaUtil() {}

  /**
   * Create a new entity manager factory bean.
   *
   * @param applicationContext the Spring application context
   * @param persistenceUnitName the name of the persistence unit
   * @param dataSource the data source
   * @param packagesToScan the packages to scan for entities
   * @return the entity manager factory bean
   */
  public static LocalContainerEntityManagerFactoryBean createEntityManager(
      ApplicationContext applicationContext,
      String persistenceUnitName,
      DataSource dataSource,
      String... packagesToScan) {
    try {
      PlatformTransactionManager platformTransactionManager =
          applicationContext.getBean(PlatformTransactionManager.class);

      LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
          new LocalContainerEntityManagerFactoryBean();

      entityManagerFactoryBean.setPersistenceUnitName(persistenceUnitName);

      entityManagerFactoryBean.setJtaDataSource(dataSource);

      // Merge the provided packages with the additional package.
      String[] additionalPackagesToScan = {"digital.inception.jpa"};
      String[] mergedPackagesToScan =
          Stream.concat(Arrays.stream(packagesToScan), Stream.of(additionalPackagesToScan))
              .toArray(String[]::new);

      entityManagerFactoryBean.setPackagesToScan(mergedPackagesToScan);

      // TODO: Detect the JPA vendor and create the correct adapter -- MARCUS
      HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
      jpaVendorAdapter.setGenerateDdl(false);

      jpaVendorAdapter.setShowSql(
          applicationContext
              .getEnvironment()
              .getProperty("inception.debug.jpa-show-sql", Boolean.class, false));

      try (Connection connection = dataSource.getConnection()) {
        DatabaseMetaData metaData = connection.getMetaData();

        switch (metaData.getDatabaseProductName()) {
          case "H2":
            break;

          case "Microsoft SQL Server":
            jpaVendorAdapter.setDatabase(Database.SQL_SERVER);
            jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLServer2012Dialect");

            break;

          case "Oracle":
            jpaVendorAdapter.setDatabase(Database.ORACLE);
            // NOTE: DO NOT REMOVE THE LINE BELOW!
            //       IT WILL RESULT IN DATA NOT BEING RETRIEVED CORRECTLY FROM ORACLE IN JPA QUERIES
            jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.OracleDialect");
            break;

          case "PostgreSQL":
            jpaVendorAdapter.setDatabase(Database.POSTGRESQL);
            break;

          default:
            jpaVendorAdapter.setDatabase(Database.DEFAULT);
            break;
        }
      }

      entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

      Map<String, Object> jpaPropertyMap = entityManagerFactoryBean.getJpaPropertyMap();

      if (dataSource.isWrapperFor(AgroalDataSource.class)) {
        AgroalDataSource agroalDataSource = dataSource.unwrap(AgroalDataSource.class);

        jpaPropertyMap.put(
            AvailableSettings.CONNECTION_PROVIDER, new AgroalConnectionProvider(agroalDataSource));
      }

      if (platformTransactionManager instanceof JtaTransactionManager) {
        jpaPropertyMap.put(AvailableSettings.TRANSACTION_COORDINATOR_STRATEGY, "jta");
        jpaPropertyMap.put(AvailableSettings.JTA_PLATFORM, "JBossTS");
      }

      return entityManagerFactoryBean;
    } catch (Throwable e) {
      throw new FatalBeanException(
          "Failed to create the entity manager factory bean for the persistence unit ("
              + persistenceUnitName
              + ")",
          e);
    }
  }

  /**
   * Walk the specified object recursively linking the JPA entities.
   *
   * @param object the object
   */
  public static void linkEntities(Object object) {
    try {
      Class<?> clazz = object.getClass();

      while (clazz != null) {
        // Process each field
        for (Field field : clazz.getDeclaredFields()) {
          /*
           * If the OneToMany annotation is present on the field, the field contains an
           * object, array of objects, or collection of objects that needs to be linked.
           */
          OneToMany oneToMany = field.getAnnotation(OneToMany.class);

          if (oneToMany != null) {
            // Retrieve the object for the field
            field.setAccessible(true);
            Object fieldObject = field.get(object);

            linkOneToManyEntities(oneToMany, object, fieldObject);
          }
        }

        // Process each method
        for (Method method : clazz.getDeclaredMethods()) {
          /*
           * If the OneToMany annotation is present on the method, the method may return an array or
           * collection of objects that needs to be linked.
           */
          OneToMany oneToMany = method.getAnnotation(OneToMany.class);

          if (oneToMany != null) {
            // Retrieve the object returned by the method
            if ((method.getParameterCount() == 0)
                && (!method.getReturnType().isPrimitive())
                && (method.getReturnType() != void.class)) {
              method.setAccessible(true);
              Object methodObject = method.invoke(object);

              linkOneToManyEntities(oneToMany, object, methodObject);
            }
          }
        }

        clazz = clazz.getSuperclass();
      }
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to link the JPA entities in the object graph ("
              + object.getClass().getName()
              + ")",
          e);
    }
  }

  /**
   * Returns the names of the packages to scan for JPA entity classes.
   *
   * @param applicationContext the Spring application context
   * @return the names of the packages to scan for JPA entity classes
   */
  public static List<String> packagesToScanForEntities(ApplicationContext applicationContext) {
    List<String> packagesToScan = new ArrayList<>();

    // Add the base packages specified using the EnableJpaRepositories annotation
    Map<String, Object> annotatedBeans =
        applicationContext.getBeansWithAnnotation(EnableJpaRepositories.class);

    for (String beanName : annotatedBeans.keySet()) {
      Class<?> beanClass = annotatedBeans.get(beanName).getClass();

      EnableJpaRepositories enableJpaRepositories =
          AnnotationUtils.findAnnotation(beanClass, EnableJpaRepositories.class);

      if (enableJpaRepositories != null) {
        for (String basePackage : enableJpaRepositories.basePackages()) {
          if ((!basePackage.startsWith("digital.inception"))
              || (basePackage.startsWith("digital.inception.demo"))) {
            // Replace any existing packages to scan with the higher level package
            packagesToScan.removeIf(packageToScan -> packageToScan.startsWith(basePackage));

            // Check if there is a higher level package already being scanned
            if (packagesToScan.stream().noneMatch(basePackage::startsWith)) {
              packagesToScan.add(basePackage);
            }
          }
        }
      }
    }

    return packagesToScan;
  }

  private static void linkOneToManyEntities(
      OneToMany oneToMany, Object parentObject, Object childObject) throws Exception {
    // If the child object is an array, iterate through each object in the array
    if (childObject.getClass().isArray()) {
      int length = Array.getLength(childObject);

      for (int i = 0; i < length; i++) {
        linkOneToManyEntities(oneToMany, parentObject, Array.get(childObject, i));
      }
    }
    // If the child object is a collection, iterate through each object in the collection
    else if (childObject instanceof Collection<?> collection) {
      for (Object collectionObject : collection) {
        linkOneToManyEntities(oneToMany, parentObject, collectionObject);
      }
    } else {
      boolean linkedOneToManyEntities = false;

      Class<?> childClass = childObject.getClass();

      while (childClass != null) {
        for (Field childField : childClass.getDeclaredFields()) {
          /*
           * If the ManyToOne annotation is present on the field, the field contains an
           * object that needs to be linked.
           */
          ManyToOne manyToOne = childField.getAnnotation(ManyToOne.class);

          if (manyToOne != null) {
            if (Objects.equals(oneToMany.mappedBy(), childField.getName())) {
              childField.setAccessible(true);
              childField.set(childObject, parentObject);
              linkedOneToManyEntities = true;
              break;
            } else if (Objects.equals(parentObject.getClass(), childField.getType())) {
              childField.setAccessible(true);
              childField.set(childObject, parentObject);
              linkedOneToManyEntities = true;
              break;
            }
          }
        }

        childClass = childClass.getSuperclass();
      }

      if ((!linkedOneToManyEntities) && StringUtils.hasText(oneToMany.mappedBy())) {
        childClass = childObject.getClass();

        while (childClass != null) {
          try {
            String getterName = "get" + StringUtils.capitalize(oneToMany.mappedBy());

            Method getterMethod = childClass.getDeclaredMethod(getterName);

            ManyToOne manyToOne = getterMethod.getAnnotation(ManyToOne.class);

            if (manyToOne != null) {
              String setterName = "set" + StringUtils.capitalize(oneToMany.mappedBy());

              Method setterMethod =
                  childClass.getDeclaredMethod(setterName, parentObject.getClass());

              setterMethod.setAccessible(true);
              setterMethod.invoke(childObject, parentObject);
              break;
            }
          } catch (NoSuchMethodException ignored) {
          }

          childClass = childClass.getSuperclass();
        }
      }

      linkEntities(childObject);
    }
  }
}
