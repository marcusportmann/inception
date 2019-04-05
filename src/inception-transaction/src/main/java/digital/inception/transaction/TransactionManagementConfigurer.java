/*
 * Copyright 2019 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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
  private static Class<?> enhancerClass;
  private static Method enhancerClassCreateMethod;
  private static Method enhancerClassSetCallbackMethod;
  private static Method enhancerClassSetSuperclassMethod;
  private static Class<?> transactionManagerClass;
  private static Class<?> transactionManagerTransactionTrackerClass;
  private static UserTransactionImp userTransaction;
  private static UserTransactionManager userTransactionManager;
  private static Class<?> userTransactionTrackerClass;

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

      // Attempt to load the javax.transaction.TransactionManager
      transactionManagerClass = Thread.currentThread().getContextClassLoader().loadClass(
          "javax.transaction.TransactionManager");

      // Attempt to load the digital.inception.test.TransactionManagerTransactionTracker class
      transactionManagerTransactionTrackerClass = Thread.currentThread().getContextClassLoader()
          .loadClass("digital.inception.test.TransactionManagerTransactionTracker");

      // Attempt to load the digital.inception.test.UserTransactionTracker class
      userTransactionTrackerClass = Thread.currentThread().getContextClassLoader().loadClass(
          "digital.inception.test.UserTransactionTracker");

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
    try
    {
      for (Constructor<?> constructor : JtaTransactionManager.class.getConstructors())
      {
        if (constructor.getParameterCount() == 2)
        {
          return PlatformTransactionManager.class.cast(constructor.newInstance(userTransaction(),
              userTransactionManager()));
        }
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException(
          "Failed to invoke the required constructor on the org.springframework.transaction.jta.JtaTransactionManager class");
    }

    throw new RuntimeException(
        "Failed to find the required constructor on the org.springframework.transaction.jta.JtaTransactionManager class");
  }

  /**
   * Returns the Atomikos JTA User Transaction.
   *
   * @return the Atomikos JTA User Transaction
   */
  @Bean
  @DependsOn({ "userTransactionManager" })
  public UserTransactionImp userTransaction()
  {
    synchronized (lock)
    {
      if (userTransaction == null)
      {
        try
        {
          if ((enhancerClass != null) && (userTransactionTrackerClass != null))
          {
            Object userTransactionEnhancer = enhancerClass.getConstructor().newInstance();

            enhancerClassSetSuperclassMethod.invoke(userTransactionEnhancer,
                UserTransactionImp.class);

            Constructor<?> userTransactionTrackerConstructor =
                userTransactionTrackerClass.getDeclaredConstructor(transactionManagerClass);

            userTransactionTrackerConstructor.setAccessible(true);

            enhancerClassSetCallbackMethod.invoke(userTransactionEnhancer,
                userTransactionTrackerConstructor.newInstance(userTransactionManager()));

            userTransaction = (UserTransactionImp) enhancerClassCreateMethod.invoke(
                userTransactionEnhancer);
          }
          else
          {
            userTransaction = new UserTransactionImp();
          }
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialize the Atomikos User Transaction", e);
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
            Object transactionManagerEnhancer = enhancerClass.getConstructor().newInstance();

            enhancerClassSetSuperclassMethod.invoke(transactionManagerEnhancer,
                UserTransactionManager.class);

            Constructor<?> transactionManagerTransactionTrackerConstructor =
                transactionManagerTransactionTrackerClass.getDeclaredConstructor();

            transactionManagerTransactionTrackerConstructor.setAccessible(true);

            enhancerClassSetCallbackMethod.invoke(transactionManagerEnhancer,
                transactionManagerTransactionTrackerConstructor.newInstance());

            userTransactionManager = (UserTransactionManager) enhancerClassCreateMethod.invoke(
                transactionManagerEnhancer);

            Method setForceShutdownMethod = UserTransactionManager.class.getMethod(
                "setForceShutdown", Boolean.TYPE);

            setForceShutdownMethod.invoke(userTransactionManager, false);
          }
          else
          {
            userTransactionManager = new UserTransactionManager();

            Method setForceShutdownMethod = UserTransactionManager.class.getMethod(
                "setForceShutdown", Boolean.TYPE);

            setForceShutdownMethod.invoke(userTransactionManager, false);
          }
        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to initialize the Atomikos Transaction Manager", e);
        }
      }

      return userTransactionManager;
    }
  }
}
