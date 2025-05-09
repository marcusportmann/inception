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

package digital.inception.core.jdbc;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code DataSourceConfiguration} class provides access to the configuration properties for a
 * data source.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class DataSourceConfiguration implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The fully qualified name of the data source class used to connect to the database. */
  private String className;

  /** The maximum size of the database connection pool used to connect to the database. */
  private int maxPoolSize = 5;

  /** The minimum size of the database connection pool used to connect to the database. */
  private int minPoolSize = 1;

  /** The password for the database. */
  private String password;

  /** The URL used to connect to the database. */
  private String url;

  /** The username for the database. */
  private String username;

  /** The timeout in seconds when validating a connection in the database connection pool. */
  private int validationTimeout = 30;

  /** Constructs a new {@code DataSourceConfiguration}. */
  public DataSourceConfiguration() {}

  /**
   * Constructs a new {@code DataSourceConfiguration}.
   *
   * @param className the fully qualified name of the data source class used to connect to the
   *     database
   * @param url the URL used to connect to the database
   * @param username the username for the database
   * @param password the password for the database
   * @param minPoolSize the minimum size of the database connection pool used to connect to the
   *     database
   * @param maxPoolSize the maximum size of the database connection pool used to connect to the
   *     database
   */
  public DataSourceConfiguration(
      String className,
      String url,
      String username,
      String password,
      int minPoolSize,
      int maxPoolSize) {
    this.className = className;
    this.url = url;
    this.username = username;
    this.password = password;
    this.minPoolSize = minPoolSize;
    this.maxPoolSize = maxPoolSize;
  }

  /**
   * Constructs a new {@code DataSourceConfiguration}.
   *
   * @param className the fully qualified name of the data source class used to connect to the
   *     database
   * @param url the URL used to connect to the database
   * @param username the username for the database
   * @param password the password for the database
   * @param minPoolSize the minimum size of the database connection pool used to connect to the
   *     database
   * @param maxPoolSize the maximum size of the database connection pool used to connect to the
   *     database
   * @param validationTimeout the timeout in seconds when validating a connection in the database
   *     connection pool
   */
  public DataSourceConfiguration(
      String className,
      String url,
      String username,
      String password,
      int minPoolSize,
      int maxPoolSize,
      int validationTimeout) {
    this.className = className;
    this.url = url;
    this.username = username;
    this.password = password;
    this.minPoolSize = minPoolSize;
    this.maxPoolSize = maxPoolSize;
    this.validationTimeout = validationTimeout;
  }

  /**
   * Returns the fully qualified name of the data source class used to connect to the database.
   *
   * @return the fully qualified name of the data source class used to connect to the database
   */
  public String getClassName() {
    return className;
  }

  /**
   * Returns the maximum size of the database connection pool used to connect to the database.
   *
   * @return the maximum size of the database connection pool used to connect to the database
   */
  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  /**
   * Returns the minimum size of the database connection pool used to connect to the database.
   *
   * @return the minimum size of the database connection pool used to connect to the database
   */
  public int getMinPoolSize() {
    return minPoolSize;
  }

  /**
   * Returns the password for the database.
   *
   * @return the password for the database
   */
  public String getPassword() {
    return password;
  }

  /**
   * Returns the URL used to connect to the database.
   *
   * @return the URL used to connect to the database
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns the username for the database.
   *
   * @return the username for the database
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the timeout in seconds when validating a connection in the database connection pool.
   *
   * @return the timeout in seconds when validating a connection in the database connection pool
   */
  public int getValidationTimeout() {
    return validationTimeout;
  }

  /**
   * Set the fully qualified name of the data source class used to connect to the database.
   *
   * @param className the fully qualified name of the data source class used to connect to the
   *     database
   */
  public void setClassName(String className) {
    this.className = className;
  }

  /**
   * Set the maximum size of the database connection pool used to connect to the database.
   *
   * @param maxPoolSize the maximum size of the database connection pool used to connect to the
   *     database
   */
  public void setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }

  /**
   * Set the minimum size of the database connection pool used to connect to the database.
   *
   * @param minPoolSize the minimum size of the database connection pool used to connect to the
   *     database
   */
  public void setMinPoolSize(int minPoolSize) {
    this.minPoolSize = minPoolSize;
  }

  /**
   * Set the password for the database.
   *
   * @param password the password for the database
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Set the URL used to connect to the database.
   *
   * @param url the URL used to connect to the database
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Set the username for the database.
   *
   * @param username the username for the database
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Set the timeout in seconds when validating a connection in the database connection pool.
   *
   * @param validationTimeout the timeout in seconds when validating a connection in the database
   *     connection pool
   */
  public void setValidationTimeout(int validationTimeout) {
    this.validationTimeout = validationTimeout;
  }
}
