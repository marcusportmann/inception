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

package digital.inception;

/**
 * The <code>Debug</code> class provides debugging capabilities.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class Debug
{
  /**
   * Returns the root directory for the JEE application server the application is running under or
   * <code>null</code> if the root directory cannot be determined.
   *
   * @return the root directory for the JEE application server the application is running under or
   *         <code>null</code> if the root directory cannot be determined
   */
  public static String getApplicationServerRootDirectory()
  {
    String wasInstallRoot = System.getProperty("was.install.root");

    if (wasInstallRoot != null)
    {
      return wasInstallRoot;
    }

    String jbossServerBaseDir = System.getProperty("jboss.home.dir");

    if (jbossServerBaseDir != null)
    {
      return jbossServerBaseDir;
    }

    String catalinaHome = System.getProperty("catalina.home");

    if (catalinaHome != null)
    {
      return catalinaHome;
    }

    String geronimoServerDir = System.getProperty("org.apache.geronimo.server.dir");

    if (geronimoServerDir != null)
    {
      return geronimoServerDir;
    }

    String wlpUserDir = System.getProperty("wlp.user.dir");

    if (wlpUserDir != null)
    {
      return wlpUserDir;
    }

    return null;
  }

  /**
   * Returns <code>true</code> if the application infrastructure is running in debug mode or
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if the application infrastructure is running in debug mode or
   *         <code>false</code> otherwise
   */
  public static boolean inDebugMode()
  {
    if (System.getProperty("digital.inception.debugMode") != null)
    {
      try
      {
        return Boolean.parseBoolean(System.getProperty("digital.inception.debugMode"));
      }
      catch (Throwable ignored) {}
    }

    return false;
  }
}
