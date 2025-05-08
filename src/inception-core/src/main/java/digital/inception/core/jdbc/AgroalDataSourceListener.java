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

import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The {@code AgroalDataSourceListener} class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class AgroalDataSourceListener implements io.agroal.api.AgroalDataSourceListener {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(AgroalDataSourceListener.class);

  private final DataSourceConfiguration dataSourceConfiguration;

  /**
   * Creates a new {@code AgroalDataSourceListener} instance.
   *
   * @param dataSourceConfiguration the data source configuration
   */
  public AgroalDataSourceListener(DataSourceConfiguration dataSourceConfiguration) {
    this.dataSourceConfiguration = dataSourceConfiguration;
  }

  @Override
  public void beforeConnectionAcquire() {
    io.agroal.api.AgroalDataSourceListener.super.beforeConnectionAcquire();
  }

  @Override
  public void beforeConnectionCreation() {
    io.agroal.api.AgroalDataSourceListener.super.beforeConnectionCreation();
  }

  @Override
  public void beforeConnectionDestroy(Connection connection) {
    io.agroal.api.AgroalDataSourceListener.super.beforeConnectionDestroy(connection);
  }

  @Override
  public void beforeConnectionFlush(Connection connection) {
    io.agroal.api.AgroalDataSourceListener.super.beforeConnectionFlush(connection);
  }

  @Override
  public void beforeConnectionLeak(Connection connection) {
    io.agroal.api.AgroalDataSourceListener.super.beforeConnectionLeak(connection);
  }

  @Override
  public void beforeConnectionReap(Connection connection) {
    io.agroal.api.AgroalDataSourceListener.super.beforeConnectionReap(connection);
  }

  @Override
  public void beforeConnectionReturn(Connection connection) {
    io.agroal.api.AgroalDataSourceListener.super.beforeConnectionReturn(connection);
  }

  @Override
  public void beforeConnectionValidation(Connection connection) {
    io.agroal.api.AgroalDataSourceListener.super.beforeConnectionValidation(connection);
  }

  @Override
  public void onConnectionAcquire(Connection connection) {
    if (log.isTraceEnabled()) {
      String databaseName =
          DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

      log.trace(
          "Connection ({}) to database ({}) acquired by thread ({})",
          connection,
          databaseName,
          Thread.currentThread().getName());
    }
  }

  @Override
  public void onConnectionCreation(Connection connection) {
    if (log.isTraceEnabled()) {
      String databaseName =
          DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

      log.trace(
          "Connection ({}) to database ({}) created by thread ({})",
          connection,
          databaseName,
          Thread.currentThread().getName());
    }
  }

  @Override
  public void onConnectionDestroy(Connection connection) {
    if (log.isTraceEnabled()) {
      String databaseName =
          DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

      log.trace(
          "Connection ({}) to database ({}) destroyed by thread ({})",
          connection,
          databaseName,
          Thread.currentThread().getName());
    }
  }

  @Override
  public void onConnectionFlush(Connection connection) {
    if (log.isTraceEnabled()) {
      String databaseName =
          DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

      log.trace(
          "Connection ({}) to database ({}) flushed by thread ({})",
          connection,
          databaseName,
          Thread.currentThread().getName());
    }
  }

  @Override
  public void onConnectionInvalid(Connection connection) {
    io.agroal.api.AgroalDataSourceListener.super.onConnectionInvalid(connection);
  }

  @Override
  public void onConnectionLeak(Connection connection, Thread thread) {
    if (log.isWarnEnabled()) {
      String databaseName =
          DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

      StringBuilder stackTraceBuilder = new StringBuilder();
      for (StackTraceElement stackTraceElement : thread.getStackTrace()) {
        stackTraceBuilder.append("    at ").append(stackTraceElement.toString()).append("\n");
      }

      MDC.put("stackTrace", stackTraceBuilder.toString());

      log.warn(
          "Potential connection leak detected for connection ({}) to database ({}) acquired by thread ({})",
          connection,
          databaseName,
          thread.getName());
    }
  }

  @Override
  public void onConnectionPooled(Connection connection) {
    if (log.isTraceEnabled()) {
      String databaseName =
          DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

      log.trace(
          "Connection ({}) to database ({}) pooled by thread ({})",
          connection,
          databaseName,
          Thread.currentThread().getName());
    }
  }

  @Override
  public void onConnectionReap(Connection connection) {
    if (log.isTraceEnabled()) {
      String databaseName =
          DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

      log.trace(
          "Connection ({}) to database ({}) reaped by thread ({})",
          connection,
          databaseName,
          Thread.currentThread().getName());
    }
  }

  @Override
  public void onConnectionReturn(Connection connection) {
    if (log.isTraceEnabled()) {
      String databaseName =
          DatabaseNameExtractor.getDatabaseNameFromJdbcUrl(dataSourceConfiguration.getUrl());

      log.trace(
          "Connection ({}) to database ({}) returned by thread ({})",
          connection,
          databaseName,
          Thread.currentThread().getName());
    }
  }

  @Override
  public void onConnectionValid(Connection connection) {
    io.agroal.api.AgroalDataSourceListener.super.onConnectionValid(connection);
  }

  @Override
  public void onInfo(String message) {
    log.info(message);
  }

  @Override
  public void onWarning(String message) {
    log.warn(message);
  }

  @Override
  public void onWarning(Throwable throwable) {
    log.warn(throwable.getMessage(), throwable);
  }
}
