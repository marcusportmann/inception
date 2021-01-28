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

package digital.inception.transaction;



import com.arjuna.ats.arjuna.coordinator.TxControl;
import com.arjuna.ats.arjuna.recovery.RecoveryManager;
import java.lang.reflect.Constructor;
import javax.naming.InitialContext;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;



/**
 * The <b>TransactionConfiguration</b> class.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfiguration {

  private static Class<?> transactionManagerProxyClass;

  private static Constructor<?> transactionManagerProxyConstructor;

  private static Class<?> userTransactionProxyClass;

  private static Constructor<?> userTransactionProxyConstructor;

  static {
    TxControl.setXANodeName(nodeName());

    try {
      transactionManagerProxyClass =
          Class.forName("digital.inception.test.TransactionManagerProxy");

      transactionManagerProxyConstructor =
          transactionManagerProxyClass.getConstructor(TransactionManager.class);
    } catch (ClassNotFoundException | NoSuchMethodException ignored) {
    }

    try {
      userTransactionProxyClass = Class.forName("digital.inception.test.UserTransactionProxy");

      userTransactionProxyConstructor =
          userTransactionProxyClass.getConstructor(UserTransaction.class, TransactionManager.class);
    } catch (ClassNotFoundException | NoSuchMethodException ignored) {
    }
  }

  /**
   * Retrieve the XA node name.
   *
   * @return the XA node name
   */
  private static String nodeName() {
    String applicationName = null;

    try {
      applicationName = InitialContext.doLookup("java:app/AppName");
    } catch (Throwable ignored) {
    }

    if (applicationName == null) {
      try {
        applicationName = InitialContext.doLookup("java:comp/env/ApplicationName");
      } catch (Throwable ignored) {
      }
    }

    String instanceName = (applicationName == null) ? "" : applicationName + "::";

    try {
      java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();

      instanceName += localMachine.getHostName().toLowerCase();
    } catch (Throwable e) {
      instanceName = "Unknown";
    }

    // Check if we are running under JBoss and if so retrieve the server name
    if (System.getProperty("jboss.server.name") != null) {
      instanceName = instanceName + "::" + System.getProperty("jboss.server.name");
    }

    // Check if we are running under Glassfish and if so retrieve the server name
    else if (System.getProperty("glassfish.version") != null) {
      instanceName = instanceName + "::" + System.getProperty("com.sun.aas.instanceName");
    }

    // Check if we are running under WebSphere Application Server Community Edition (Geronimo)
    else if (System.getProperty("org.apache.geronimo.server.dir") != null) {
      instanceName = instanceName + "::Geronimo";
    }

    // Check if we are running under WebSphere Application Server Liberty Profile
    else if (System.getProperty("wlp.user.dir") != null) {
      instanceName = instanceName + "::WLP";
    }

    /*
     * Check if we are running under WebSphere and if so execute the code below to retrieve the
     * server name.
     */
    else {
      Class<?> clazz = null;

      try {
        clazz =
            Thread.currentThread()
                .getContextClassLoader()
                .loadClass("com.ibm.websphere.management.configservice.ConfigurationService");
      } catch (Throwable ignored) {
      }

      if (clazz != null) {
        try {
          instanceName = instanceName + "::" + InitialContext.doLookup("servername").toString();
        } catch (Throwable e) {
          instanceName = instanceName + "::Unknown";
        }
      }
    }

    return instanceName;
  }

  /**
   * Returns the Narayana recovery manager.
   *
   * @return the Narayana recovery manager
   */
  @Bean
  public RecoveryManager narayanaRecoveryManager() {
    com.arjuna.ats.arjuna.recovery.RecoveryManager recoveryManager =
        com.arjuna.ats.arjuna.recovery.RecoveryManager.manager();
    recoveryManager.initialize();

    return recoveryManager;
  }

  /**
   * Returns the Narayana recovery manager service.
   *
   * @return the Narayana recovery manager service
   */
  @Bean
  public com.arjuna.ats.jbossatx.jta.RecoveryManagerService narayanaRecoveryManagerService() {
    com.arjuna.ats.jbossatx.jta.RecoveryManagerService recoveryManagerService =
        new com.arjuna.ats.jbossatx.jta.RecoveryManagerService();
    recoveryManagerService.create();

    return recoveryManagerService;
  }

  /**
   * Returns the Narayana JTA transaction manager.
   *
   * @return the Narayana JTA transaction manager
   */
  @Bean
  @DependsOn({"narayanaTransactionSynchronizationRegistry"})
  public TransactionManager narayanaTransactionManager() {
    TransactionManager transactionManager =
        com.arjuna.ats.jta.TransactionManager.transactionManager();

    if (transactionManagerProxyConstructor != null) {
      try {
        return (TransactionManager)
            transactionManagerProxyConstructor.newInstance(transactionManager);
      } catch (Throwable e) {
        return transactionManager;
      }
    } else {
      return transactionManager;
    }
  }

  /**
   * Returns the Narayana JTA transaction synchronization registry.
   *
   * @return the Narayana JTA transaction synchronization registry
   */
  @Bean
  @DependsOn({"narayanaRecoveryManager", "narayanaRecoveryManagerService"})
  public TransactionSynchronizationRegistry narayanaTransactionSynchronizationRegistry() {
    return new com.arjuna.ats.internal.jta.transaction.arjunacore
        .TransactionSynchronizationRegistryImple();
  }

  /**
   * Returns the Narayana JTA user transaction.
   *
   * @return the Narayana JTA user transaction
   */
  @Bean
  @DependsOn({"narayanaTransactionSynchronizationRegistry", "narayanaTransactionManager"})
  public UserTransaction narayanaUserTransaction() {
    UserTransaction userTransaction = com.arjuna.ats.jta.UserTransaction.userTransaction();

    if (transactionManagerProxyConstructor != null) {
      try {
        return (UserTransaction)
            userTransactionProxyConstructor.newInstance(
                userTransaction, narayanaTransactionManager());
      } catch (Throwable e) {
        return userTransaction;
      }
    } else {
      return userTransaction;
    }
  }

  /**
   * Returns the Spring JTA platform transaction manager.
   *
   * @return the Spring JTA platform transaction manager
   */
  @Bean
  @DependsOn({"narayanaTransactionManager", "narayanaUserTransaction"})
  public PlatformTransactionManager transactionManager() {
    try {
      return new JtaTransactionManager(narayanaUserTransaction(), narayanaTransactionManager());
    } catch (Throwable e) {
      throw new RuntimeException("Failed to initialize the JTA transaction manager");
    }
  }
}
