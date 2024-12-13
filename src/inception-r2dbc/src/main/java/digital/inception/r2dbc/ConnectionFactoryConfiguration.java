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

package digital.inception.r2dbc;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

/**
 * The <b>ConnectionFactoryConfiguration</b> class provides access to the configuration properties
 * for a R2DBC connection factory.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused"})
public class ConnectionFactoryConfiguration implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The initial size of the R2DBC connection pool holding R2DBC connections to the database. */
  private int initialPoolSize = 1;

  /**
   * The maximum amount of time a R2DBC connection to the database can remain idle before it is
   * closed.
   */
  private Duration maxIdleTime = Duration.ofMinutes(30);

  /**
   * The maximum size of the R2DBC connection pool holding R2DBC connects to the application
   * database.
   */
  private int maxPoolSize = 5;

  /** The password used to create R2DBC connections to the database. */
  private String password;

  /** The URL used to create R2DBC connections to the database. */
  private String url;

  /** The username used to create R2DBC connections to the database. */
  private String username;

  /** Constructs a new <b>ConnectionFactoryConfiguration</b>. */
  public ConnectionFactoryConfiguration() {}

  /**
   * Constructs a new <b>ConnectionFactoryConfiguration</b>.
   *
   * @param url the URL used to create R2DBC connections to the database
   * @param username the username used to create R2DBC connections to the database
   * @param password the password used to create R2DBC connections to the database
   * @param initialPoolSize the initial size of the R2DBC connection pool holding R2DBC connections
   *     to the database
   * @param maxPoolSize the maximum size of the R2DBC connection pool holding R2DBC connects to the
   *     application database
   * @param maxIdleTime the maximum amount of time a R2DBC connection to the database can remain
   *     idle before it is closed
   */
  public ConnectionFactoryConfiguration(
      String url,
      String username,
      String password,
      int initialPoolSize,
      int maxPoolSize,
      Duration maxIdleTime) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.initialPoolSize = initialPoolSize;
    this.maxPoolSize = maxPoolSize;
    this.maxIdleTime = maxIdleTime;
  }

  /**
   * Constructs a new <b>ConnectionFactoryConfiguration</b>.
   *
   * @param url the URL used to create R2DBC connections to the database
   * @param initialPoolSize the initial size of the R2DBC connection pool holding R2DBC connections
   *     to the database
   * @param maxPoolSize the maximum size of the R2DBC connection pool holding R2DBC connects to the
   *     application database
   * @param maxIdleTime the maximum amount of time a R2DBC connection to the database can remain
   *     idle before it is closed
   */
  public ConnectionFactoryConfiguration(
      String url, int initialPoolSize, int maxPoolSize, Duration maxIdleTime) {
    this.url = url;
    this.initialPoolSize = initialPoolSize;
    this.maxPoolSize = maxPoolSize;
    this.maxIdleTime = maxIdleTime;
  }

  /**
   * Constructs a new <b>ConnectionFactoryConfiguration</b>.
   *
   * @param url the URL used to create R2DBC connections to the database
   * @param username the username used to create R2DBC connections to the database
   * @param password the password used to create R2DBC connections to the database
   */
  public ConnectionFactoryConfiguration(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  /**
   * Returns the initial size of the R2DBC connection pool holding R2DBC connections to the
   * database.
   *
   * @return the initial size of the R2DBC connection pool holding R2DBC connections to the database
   */
  public int getInitialPoolSize() {
    return initialPoolSize;
  }

  /**
   * Returns the maximum amount of time a R2DBC connection to the database can remain idle before it
   * is closed.
   *
   * @return the maximum amount of time a R2DBC connection to the database can remain idle before it
   *     is closed
   */
  public Duration getMaxIdleTime() {
    return maxIdleTime;
  }

  /**
   * Returns the maximum amount of time a R2DBC connection to the database can remain idle before it
   * is closed.
   *
   * @return the maximum amount of time a R2DBC connection to the database can remain idle before it
   *     is closed
   */
  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  /**
   * Returns the password used to create R2DBC connections to the database.
   *
   * @return the password used to create R2DBC connections to the database
   */
  public String getPassword() {
    return password;
  }

  /**
   * Returns the URL used to create R2DBC connections to the database.
   *
   * @return the URL used to create R2DBC connections to the database
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns the username used to create R2DBC connections to the database.
   *
   * @return the username used to create R2DBC connections to the database
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the initial size of the R2DBC connection pool holding R2DBC connections to the database.
   *
   * @param initialPoolSize the initial size of the R2DBC connection pool holding R2DBC connections
   *     to the database
   */
  public void setInitialPoolSize(int initialPoolSize) {
    this.initialPoolSize = initialPoolSize;
  }

  /**
   * Set the maximum amount of time a R2DBC connection to the database can remain idle before it is
   * closed.
   *
   * @param maxIdleTime the maximum amount of time a R2DBC connection to the database can remain
   *     idle before it is closed
   */
  public void setMaxIdleTime(Duration maxIdleTime) {
    this.maxIdleTime = maxIdleTime;
  }

  /**
   * Set the maximum amount of time a R2DBC connection to the database can remain idle before it is
   * closed.
   *
   * @param maxPoolSize the maximum amount of time a R2DBC connection to the database can remain
   *     idle before it is closed
   */
  public void setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }

  /**
   * Set the password used to create R2DBC connections to the database.
   *
   * @param password the password used to create R2DBC connections to the database
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Set the URL used to create R2DBC connections to the database.
   *
   * @param url the URL used to create R2DBC connections to the database
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Set the username used to create R2DBC connections to the database.
   *
   * @param username the username used to create R2DBC connections to the database
   */
  public void setUsername(String username) {
    this.username = username;
  }
}
