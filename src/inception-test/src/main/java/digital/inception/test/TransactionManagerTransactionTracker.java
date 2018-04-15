///*
// * Copyright 2018 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package digital.inception.test;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//import org.slf4j.LoggerFactory;
//
//import javax.transaction.Transaction;
//import javax.transaction.TransactionManager;
//import java.io.Serializable;
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
////~--- JDK imports ------------------------------------------------------------
//
///**
// * The <code>TransactionManagerTransactionTracker</code> class implements a cglib method
// * interceptor that tracks the Java Transaction (JTA) API transactions associated with the current
// * thread and managed by a <code>javax.transaction.TransactionManager</code> implementation.
// *
// * @author Marcus Portmann
// */
//public class TransactionManagerTransactionTracker
//  implements MethodInterceptor, Serializable
//{
//  private static final long serialVersionUID = 1000000;
//  private static ThreadLocal<Map<Transaction, StackTraceElement[]>> activeTransactionStackTraces =
//      ThreadLocal.withInitial(ConcurrentHashMap::new);
//
//  /**
//   * Constructs a new <code>TransactionManagerTransactionTracker</code>.
//   */
//  TransactionManagerTransactionTracker() {}
//
//  /**
//   * Intercept the method invocation.
//   *
//   * @param obj    the object the method was invoked on
//   * @param method the method that was invoked
//   * @param args   the method arguments
//   * @param proxy  the proxy
//   *
//   * @return the results of invoking the method
//   */
//  @Override
//  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
//    throws Throwable
//  {
//    try
//    {
//      Method getTransactionMethod = TransactionManager.class.getMethod("getTransaction");
//
//      switch (method.getName())
//      {
//        case "begin":
//        {
//          /*
//           * TODO: Should we throw an exception if there is an existing transaction when calling
//           *       the begin method on the UserTransaction implementation?
//           */
//
//          try
//          {
//            return proxy.invokeSuper(obj, args);
//          }
//          finally
//          {
//            Transaction afterTransaction = (Transaction) getTransactionMethod.invoke(obj);
//
//            if (afterTransaction != null)
//            {
//              getActiveTransactionStackTraces().put(afterTransaction, Thread.currentThread()
//                  .getStackTrace());
//            }
//          }
//        }
//
//        case "commit":
//        case "rollback":
//        {
//          Transaction beforeTransaction = (Transaction) getTransactionMethod.invoke(obj);
//
//          try
//          {
//            return proxy.invokeSuper(obj, args);
//          }
//          finally
//          {
//            Transaction afterTransaction = (Transaction) getTransactionMethod.invoke(obj);
//
//            if ((beforeTransaction != null) && (afterTransaction == null))
//            {
//              if (getActiveTransactionStackTraces().containsKey(beforeTransaction))
//              {
//                getActiveTransactionStackTraces().remove(beforeTransaction);
//              }
//            }
//          }
//        }
//
//        case "resume":
//        {
//          return proxy.invokeSuper(obj, args);
//        }
//
//        case "setRollbackOnly":
//        {
//          /*
//           * This check to confirm that we have a valid transaction was added to handle the issue
//           * where the Hibernate JPA implementation would try to rollback a transaction even if one
//           * didn't exist when a non-hibernate exception was thrown.
//           */
//          Transaction transaction = (Transaction) getTransactionMethod.invoke(obj);
//
//          if (transaction != null)
//          {
//            return proxy.invokeSuper(obj, args);
//          }
//          else
//          {
//            return null;
//          }
//        }
//
//        case "suspend":
//        {
//          return proxy.invokeSuper(obj, args);
//        }
//
//        default:
//          return proxy.invokeSuper(obj, args);
//      }
//    }
//    catch (Throwable e)
//    {
//      LoggerFactory.getLogger(TransactionManagerTransactionTracker.class).error(
//          "Failed to invoke the TransactionManager method (" + method.getName() + ")", e);
//
//      throw e;
//    }
//  }
//
//  /**
//   * Returns the active transaction stack traces for the current thread.
//   *
//   * @return the active transaction stack traces for the current thread
//   */
//  static Map<Transaction, StackTraceElement[]> getActiveTransactionStackTraces()
//  {
//    return activeTransactionStackTraces.get();
//  }
//}
