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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * Abstract base class for domain-specific row-mapper utility classes.
 *
 * <p>A row-mapper utility class is a non-instantiable class that groups static {@code
 * org.springframework.jdbc.core.RowMapper RowMapper} implementations for a particular domain area,
 * such as members, schemes, employers, or benefits. Repository classes can then reuse those row
 * mappers when converting JDBC query results to application model objects.
 *
 * <p>This base class provides common helper methods that static row mapper implementations can use
 * while converting {@link ResultSet} values. It is especially useful for nullable columns, blank
 * string handling, date/time conversion, boolean flag conversion, and numeric columns read through
 * JDBC primitive getters, where JDBC returns a primitive default value and requires {@link
 * ResultSet#wasNull()} to distinguish an actual database value from {@code NULL}.
 *
 * <p>Concrete row-mapper utility classes should extend this class to inherit the shared mapping
 * helpers, while still declaring a private constructor to prevent instantiation.
 *
 * @author Marcus Portmann
 */
public abstract class AbstractRowMappers {

  /** Protected constructor to prevent instantiation. */
  protected AbstractRowMappers() {}

  /**
   * Returns the value of the specified result set column as a nullable {@link String}.
   *
   * <p>Blank strings are returned unchanged. Use {@link #getNullableTrimmedString(ResultSet,
   * String)} when leading and trailing whitespace should be removed, or {@link
   * #getNullableNonBlankString(ResultSet, String)} when blank strings should be treated as {@code
   * null}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the column value as a {@link String}, or {@code null} when the database value is {@code
   *     NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static String getNullableString(ResultSet resultSet, String columnName)
      throws SQLException {

    return resultSet.getString(columnName);
  }

  /**
   * Returns the value of the specified result set column as a nullable, trimmed {@link String}.
   *
   * <p>Database {@code NULL} values are returned as {@code null}. Non-null values are returned
   * after applying {@link String#trim()}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the trimmed column value, or {@code null} when the database value is {@code NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static String getNullableTrimmedString(ResultSet resultSet, String columnName)
      throws SQLException {

    String value = resultSet.getString(columnName);
    return value != null ? value.trim() : null;
  }

  /**
   * Returns the value of the specified result set column as a nullable, non-blank {@link String}.
   *
   * <p>Database {@code NULL} values, empty strings, and strings containing only whitespace are
   * returned as {@code null}. Non-blank values are returned after applying {@link String#trim()}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the trimmed column value, or {@code null} when the database value is {@code NULL} or
   *     blank
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static String getNullableNonBlankString(ResultSet resultSet, String columnName)
      throws SQLException {

    String value = getNullableTrimmedString(resultSet, columnName);
    return value != null && !value.isBlank() ? value : null;
  }

  /**
   * Returns the value of the specified result set column as a nullable {@link Integer}.
   *
   * <p>This method wraps {@link ResultSet#getInt(String)} and checks {@link ResultSet#wasNull()} so
   * that a database {@code NULL} value is returned as {@code null} instead of the JDBC primitive
   * default value {@code 0}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the column value as an {@link Integer}, or {@code null} when the database value is
   *     {@code NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static Integer getNullableInteger(ResultSet resultSet, String columnName)
      throws SQLException {

    int value = resultSet.getInt(columnName);
    return resultSet.wasNull() ? null : value;
  }

  /**
   * Returns the value of the specified result set column as a nullable {@link Long}.
   *
   * <p>This method wraps {@link ResultSet#getLong(String)} and checks {@link ResultSet#wasNull()}
   * so that a database {@code NULL} value is returned as {@code null} instead of the JDBC primitive
   * default value {@code 0L}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the column value as a {@link Long}, or {@code null} when the database value is {@code
   *     NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static Long getNullableLong(ResultSet resultSet, String columnName)
      throws SQLException {

    long value = resultSet.getLong(columnName);
    return resultSet.wasNull() ? null : value;
  }

  /**
   * Returns the value of the specified result set column as a nullable {@link Boolean}.
   *
   * <p>This method wraps {@link ResultSet#getBoolean(String)} and checks {@link
   * ResultSet#wasNull()} so that a database {@code NULL} value is returned as {@code null} instead
   * of the JDBC primitive default value {@code false}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the column value as a {@link Boolean}, or {@code null} when the database value is
   *     {@code NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static Boolean getNullableBoolean(ResultSet resultSet, String columnName)
      throws SQLException {

    boolean value = resultSet.getBoolean(columnName);
    return resultSet.wasNull() ? null : value;
  }

  /**
   * Returns the value of the specified result set column as a nullable {@link BigDecimal}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the column value as a {@link BigDecimal}, or {@code null} when the database value is
   *     {@code NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static BigDecimal getNullableBigDecimal(ResultSet resultSet, String columnName)
      throws SQLException {

    return resultSet.getBigDecimal(columnName);
  }

  /**
   * Returns the value of the specified SQL {@link Date} column as a nullable {@link LocalDate}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the column value as a {@link LocalDate}, or {@code null} when the database value is
   *     {@code NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static LocalDate getNullableLocalDate(ResultSet resultSet, String columnName)
      throws SQLException {

    Date value = resultSet.getDate(columnName);
    return value != null ? value.toLocalDate() : null;
  }

  /**
   * Returns the value of the specified SQL {@link Timestamp} column as a nullable {@link
   * OffsetDateTime}.
   *
   * <p>The timestamp is converted using {@link ZoneId#systemDefault()}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return the column value as an {@link OffsetDateTime}, or {@code null} when the database value
   *     is {@code NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static OffsetDateTime getNullableOffsetDateTime(ResultSet resultSet, String columnName)
      throws SQLException {

    return getNullableOffsetDateTime(resultSet, columnName, ZoneId.systemDefault());
  }

  /**
   * Returns the value of the specified SQL {@link Timestamp} column as a nullable {@link
   * OffsetDateTime}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @param zoneId the time zone to use when converting the timestamp
   * @return the column value as an {@link OffsetDateTime}, or {@code null} when the database value
   *     is {@code NULL}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static OffsetDateTime getNullableOffsetDateTime(
      ResultSet resultSet, String columnName, ZoneId zoneId) throws SQLException {

    Timestamp value = resultSet.getTimestamp(columnName);

    return value != null ? value.toInstant().atZone(zoneId).toOffsetDateTime() : null;
  }

  /**
   * Returns {@code true} when the specified result set column contains {@code "Y"}.
   *
   * <p>This is useful for mapping database flags represented as {@code Y}/{@code N} values. The
   * comparison is case-insensitive. Database {@code NULL} values, blank values, and any value other
   * than {@code "Y"} return {@code false}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return {@code true} when the column value is {@code "Y"}; otherwise {@code false}
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static boolean getYesNoBoolean(ResultSet resultSet, String columnName)
      throws SQLException {

    String value = getNullableTrimmedString(resultSet, columnName);
    return "Y".equalsIgnoreCase(value);
  }

  /**
   * Returns the nullable boolean value represented by a {@code Y}/{@code N} result set column.
   *
   * <p>The comparison is case-insensitive. Database {@code NULL} and blank values are returned as
   * {@code null}. Values of {@code "Y"} return {@code true}; values of {@code "N"} return {@code
   * false}. Any other value is returned as {@code null}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return {@code true} for {@code "Y"}, {@code false} for {@code "N"}, or {@code null} when no
   *     boolean value can be derived
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static Boolean getNullableYesNoBoolean(ResultSet resultSet, String columnName)
      throws SQLException {

    String value = getNullableTrimmedString(resultSet, columnName);

    if (value == null || value.isBlank()) {
      return null;
    }

    if ("Y".equalsIgnoreCase(value)) {
      return true;
    }

    if ("N".equalsIgnoreCase(value)) {
      return false;
    }

    return null;
  }

  /**
   * Returns the nullable boolean value represented by a {@code 1}/{@code 0} result set column.
   *
   * <p>Database {@code NULL} values are returned as {@code null}. A value of {@code 1} returns
   * {@code true}; a value of {@code 0} returns {@code false}. Other values are returned as {@code
   * null}.
   *
   * @param resultSet the result set currently positioned on the row being mapped
   * @param columnName the name of the column to read
   * @return {@code true} for {@code 1}, {@code false} for {@code 0}, or {@code null} when no
   *     boolean value can be derived
   * @throws SQLException if the column cannot be read from the result set
   */
  protected static Boolean getNullableOneZeroBoolean(ResultSet resultSet, String columnName)
      throws SQLException {

    Integer value = getNullableInteger(resultSet, columnName);

    if (value == null) {
      return null;
    }

    if (value == 1) {
      return true;
    }

    if (value == 0) {
      return false;
    }

    return null;
  }
}
