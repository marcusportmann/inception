/*
 * Copyright 2018 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationTransactionManager</code> class implements a Spring component that
 * configures Atomikos as the Spring JTA Transaction Manager.
 *
 * @author Marcus Portmann
 */
@Component
public class TransactionManagementConfigurer
  implements org.springframework.transaction.annotation.TransactionManagementConfigurer
{
  private static final Object lock = new Object();
  private static UserTransactionManager userTransactionManager;
  private static UserTransactionImp userTransaction;
  private static Class<?> enhancerClass;
  private static Class<?> userTransactionTrackerClass;
  private static Class<?> transactionManagerTransactionTrackerClass;
  private static Method enhancerClassSetSuperclassMethod;
  private static Method enhancerClassSetCallbackMethod;
  private static Method enhancerClassCreateMethod;

  static
  {
    try
    {
      // Attempt to load the CGLib net.sf.cglib.proxy.Enhancer class
      enhancerClass = Thread.currentThread().getContextClassLoader().loadClass(
          "net.sf.cglib.proxy.Enhancer");

      // Attempt to load the CGLib net.sf.cglib.proxy.Callback class
      Class<?> callbackClass = Thread.currentThread().getContextClassLoader().loadClass(
          "net.sf.cglib.proxy.Callback");

      // Retrieve the setSuperClass method for the net.sf.cglib.proxy.Enhancer class
      enhancerClassSetSuperclassMethod = enhancerClass.getMethod("setSuperclass", Class.class);

      // Retrieve the setCallback method for the net.sf.cglib.proxy.Enhancer class
      enhancerClassSetCallbackMethod = enhancerClass.getMethod("setCallback", callbackClass);

      // Retrieve the create method for the net.sf.cglib.proxy.Enhancer class
      enhancerClassCreateMethod = enhancerClass.getMethod("create");

      // Attempt to load the digital.inception.test.UserTransactionTracker class
      userTransactionTrackerClass = Thread.currentThread().getContextClassLoader().loadClass(
          "digital.inception.test.UserTransactionTracker");

      // Attempt to load the digital.inception.test.TransactionManagerTransactionTracker class
      transactionManagerTransactionTrackerClass = Thread.currentThread().getContextClassLoader()
          .loadClass("digital.inception.test.TransactionManagerTransactionTracker");
    }
    catch (ClassNotFoundException | NoSuchMethodException ignored) {}
  }

  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager()
  {
    return transactionManager();
  }

  /**
   * Returns the Spring Transaction Manager.
   *
   * @return the Spring Transaction Manager
   */
  @Bean
  @DependsOn({ "userTransactionManager", "userTransaction" })
  public PlatformTransactionManager transactionManager()
  {
    return new JtaTransactionManager(userTransaction(), userTransactionManager());
  }

  /**
   * Returns the Atomikos JTA User Transaction.
   *
   * @return the Atomikos JTA User Transaction
   */
  @Bean
  @DependsOn({ "userTransactionManager" })
  public UserTransaction userTransaction()
  {
    synchronized (lock)
    {
      if (userTransaction == null)
      {
        try
        {
          if ((enhancerClass != null) && (userTransactionTrackerClass != null))
          {
            Object userTransactionEnhancer = enhancerClass.newInstance();

            enhancerClassSetSuperclassMethod.invoke(userTransactionEnhancer,
                UserTransactionImp.class);

            Constructor<?> userTransactionTrackerConstructor =
                userTransactionTrackerClass.getDeclaredConstructor(javax.transaction
                .TransactionManager.class);

            userTransactionTrackerConstructor.setAccessible(true);

            enhancerClassSetCallbackMethod.invoke(userTransactionEnhancer,
                userTransactionTrackerConstructor.newInstance(userTransactionManager()));

            userTransaction = (UserTransactionImp) enhancerClassCreateMethod.invoke(
                userTransactionEnhancer);

            userTransaction.setTransactionTimeout(300);
          }
          else
          {
            userTransaction = new UserTransactionImp();

            userTransaction.setTransactionTimeout(300);
          }
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialise the Atomikos User Transaction", e);
        }
      }

      return userTransaction;
    }
  }

  /**
   * Returns the Atomikos JTA User Transaction Manager.
   *
   * @return the Atomikos JTA User Transaction Manager
   */
  @Bean(initMethod = "init", destroyMethod = "close")
  public UserTransactionManager userTransactionManager()
  {
    synchronized (lock)
    {
      if (userTransactionManager == null)
      {
        try
        {
          if ((enhancerClass != null) && (transactionManagerTransactionTrackerClass != null))
          {
            Object transactionManagerEnhancer = enhancerClass.newInstance();

            enhancerClassSetSuperclassMethod.invoke(transactionManagerEnhancer,
                UserTransactionManager.class);

            Constructor<?> transactionManagerTransactionTrackerConstructor =
                transactionManagerTransactionTrackerClass.getDeclaredConstructor();

            transactionManagerTransactionTrackerConstructor.setAccessible(true);

            enhancerClassSetCallbackMethod.invoke(transactionManagerEnhancer,
                transactionManagerTransactionTrackerConstructor.newInstance());

            userTransactionManager = (UserTransactionManager) enhancerClassCreateMethod.invoke(
                transactionManagerEnhancer);

            userTransactionManager.setForceShutdown(false);
          }
          else
          {
            userTransactionManager = new UserTransactionManager();

            userTransactionManager.setForceShutdown(false);
          }
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialise the Atomikos Transaction Manager", e);
        }
      }

      return userTransactionManager;
    }
  }
}
