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

import java.sql.Connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>DataSourceTracker</code> class implements a cglib method interceptor that tracks the
 * usage of JDBC connections for Data Sources.
 *
 * @author Marcus Portmann
 */
public class DataSourceTracker
  implements MethodInterceptor, Serializable
{
  private static final long serialVersionUID = 1000000;
  private static ThreadLocal<Map<Connection, StackTraceElement[]>> activeDatabaseConnections =
      ThreadLocal.withInitial(ConcurrentHashMap::new);

  /**
   * Constructs a new <code>DataSourceTracker</code>.
   */
  DataSourceTracker() {}

  /**
   * Returns the active database connections for all Data Sources associated with the tracker.
   *
   * @return the active database connections for all Data Sources associated with the tracker
   */
  public static Map<Connection, StackTraceElement[]> getActiveDatabaseConnections()
  {
    return activeDatabaseConnections.get();
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
        case "getConnection":
        {
          boolean include = true;

          Connection connection = new ConnectionProxy((Connection) proxy.invokeSuper(obj, args));

          StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

          for (StackTraceElement stackTraceElement : stackTrace)
          {
            if (stackTraceElement.getClassName().contains("NonContextualJdbcConnectionAccess"))
            {
              include = false;
            }
          }

          if (include)
          {
            getActiveDatabaseConnections().put(connection, stackTrace);
          }

          return connection;
        }

        default:
          return proxy.invokeSuper(obj, args);
      }
    }
    catch (Throwable e)
    {
      LoggerFactory.getLogger(DataSourceTracker.class).error(
          "Failed to invoke the DataSource method (" + method.getName() + ")", e);

      throw e;
    }
  }
}
