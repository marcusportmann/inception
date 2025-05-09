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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/**
 * The {@code DatabaseNameExtractor} class provides the ability to extract the database name or SID
 * from a JDBC URL for H2, SQL Server, PostgreSQL, or Oracle.
 *
 * @author Marcus Portmann
 */
public final class DatabaseNameExtractor {

  /** Private default constructor to prevent instantiation. */
  private DatabaseNameExtractor() {}

  /**
   * Extracts the database name or SID from a JDBC URL for H2, SQL Server, PostgreSQL, or Oracle.
   *
   * @param jdbcUrl the JDBC URL
   * @return the name of the database, or SID for Oracle, or {@code Unknown} if the database type is
   *     not recognized
   */
  public static String getDatabaseNameFromJdbcUrl(String jdbcUrl) {
    try {
      if (jdbcUrl == null || jdbcUrl.isEmpty()) {
        return "Unknown";
      }

      // H2 Database URL: jdbc:h2:~/test
      if (jdbcUrl.startsWith("jdbc:h2:")) {
        return removeParametersFromDatabaseName(extractH2DatabaseName(jdbcUrl));
      }

      // Microsoft SQL Server URL: jdbc:sqlserver://localhost:1433;databaseName=mydb
      if (jdbcUrl.startsWith("jdbc:sqlserver:")) {
        return removeParametersFromDatabaseName(extractSqlServerDatabaseName(jdbcUrl));
      }

      // PostgreSQL URL: jdbc:postgresql://localhost:5432/mydb
      if (jdbcUrl.startsWith("jdbc:postgresql:")) {
        return removeParametersFromDatabaseName(extractPostgresDatabaseName(jdbcUrl));
      }

      // Oracle URL: jdbc:oracle:thin:@//localhost:1521/SID or jdbc:oracle:thin:@localhost:1521:SID
      if (jdbcUrl.startsWith("jdbc:oracle:thin:")) {
        return removeParametersFromDatabaseName(extractOracleSid(jdbcUrl));
      }

      // Unknown database type
      return "Unknown";
    } catch (Throwable e) {
      return "Unknown";
    }
  }

  // Extract database name for H2
  private static String extractH2DatabaseName(String jdbcUrl) {
    String pattern = "jdbc:h2:(.+)";
    String databaseName = matchPattern(jdbcUrl, pattern, 1);

    if (StringUtils.hasText(databaseName)
        && databaseName.startsWith("mem:")
        && (databaseName.length() > 4)) {
      return databaseName.substring(databaseName.indexOf(":") + 1);
    } else {
      return databaseName;
    }
  }

  // Extract SID for Oracle
  private static String extractOracleSid(String jdbcUrl) {
    // Match URL formats: jdbc:oracle:thin:@localhost:1521:SID or
    // jdbc:oracle:thin:@//localhost:1521/SID
    String pattern =
        "jdbc:oracle:thin:@(?:[^:]+:)?[^:]+:(\\w+)|jdbc:oracle:thin:@//[^:]+:[^/]+/([^\\?]+)";
    return matchPattern(jdbcUrl, pattern, 1, 2);
  }

  // Extract database name for PostgreSQL
  private static String extractPostgresDatabaseName(String jdbcUrl) {
    String pattern = "jdbc:postgresql://[^/]+/([^\\?]+)";
    return matchPattern(jdbcUrl, pattern, 1);
  }

  // Extract database name for SQL Server
  private static String extractSqlServerDatabaseName(String jdbcUrl) {
    String pattern = ".*;databaseName=([^;]+).*";
    return matchPattern(jdbcUrl, pattern, 1);
  }

  // Utility method for pattern matching with optional groups
  private static String matchPattern(String jdbcUrl, String regex, int... groupIndices) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(jdbcUrl);
    if (matcher.matches()) {
      for (int groupIndex : groupIndices) {
        String match = matcher.group(groupIndex);
        if (match != null) {
          return match;
        }
      }
    }
    return null;
  }

  private static String removeParametersFromDatabaseName(String databaseName) {
    if (databaseName.contains(";")) {
      return databaseName.substring(0, databaseName.indexOf(";"));
    } else {
      return databaseName;
    }
  }
}
