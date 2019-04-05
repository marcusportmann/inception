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

package digital.inception.test;

//~--- non-JDK imports --------------------------------------------------------

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.lang.reflect.Method;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * The <code>UserTransactionTracker</code> class implements a cglib method interceptor that tracks
 * the Java Transaction (JTA) API transactions associated with the current thread and managed by a
 * <code>javax.transaction.UserTransaction</code> implementation.
 *
 * @author Marcus Portmann
 */
public class UserTransactionTracker
  implements MethodInterceptor, Serializable
{
  private static final long serialVersionUID = 1000000;
  private static ThreadLocal<Map<Transaction, StackTraceElement[]>> activeTransactionStackTraces =
      ThreadLocal.withInitial(ConcurrentHashMap::new);
  private TransactionManager transactionManager;

  /**
   * Constructs a new <code>UserTransactionTracker</code>.
   *
   * @param transactionManager the transaction manager
   */
  UserTransactionTracker(TransactionManager transactionManager)
  {
    this.transactionManager = transactionManager;
  }

  /**
   * Intercept the method invocation.
   *
   * @param obj    the object the method was invoked on
   * @param method the method that was invoked
   * @param args   the method arguments
   * @param proxy  the proxy
   *
   * @return the results of invoking the method
   */
  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
    throws Throwable
  {
    try
    {
      switch (method.getName())
      {
        case "begin":
        {
          /*
           * TODO: Should we throw an exception if there is an existing transaction when calling
           *       the begin method on the UserTransaction implementation?
           */

          try
          {
            return proxy.invokeSuper(obj, args);
          }
          finally
          {
            Transaction afterTransaction = getCurrentTransaction();

            if (afterTransaction != null)
            {
              getActiveTransactionStackTraces().put(afterTransaction, Thread.currentThread()
                  .getStackTrace());
            }
          }
        }

        case "commit":
        case "rollback":
        {
          Transaction beforeTransaction = getCurrentTransaction();

          try
          {
            return proxy.invokeSuper(obj, args);
          }
          finally
          {
            Transaction afterTransaction = getCurrentTransaction();

            if ((beforeTransaction != null) && (afterTransaction == null))
            {
              if (getActiveTransactionStackTraces().containsKey(beforeTransaction))
              {
                getActiveTransactionStackTraces().remove(beforeTransaction);
              }
            }
          }
        }

        default:
          return proxy.invokeSuper(obj, args);
      }
    }
    catch (Throwable e)
    {
      LoggerFactory.getLogger(UserTransactionTracker.class).error(
          "Failed to invoke the UserTransaction method", e);

      throw e;
    }
  }

  /**
   * Returns the active transaction stack traces for the current thread.
   *
   * @return the active transaction stack traces for the current thread
   */
  static Map<Transaction, StackTraceElement[]> getActiveTransactionStackTraces()
  {
    return activeTransactionStackTraces.get();
  }

  /**
   * Returns the current transaction.
   *
   * @return the current transaction or <code>null</code> if there is no current transaction
   */
  private Transaction getCurrentTransaction()
  {
    try
    {
      return transactionManager.getTransaction();
    }
    catch (Throwable e)
    {
      LoggerFactory.getLogger(UserTransactionTracker.class).error(
          "Failed to retrieve the current transaction", e);

      return null;
    }
  }
}
