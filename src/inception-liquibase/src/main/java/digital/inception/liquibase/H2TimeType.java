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

package digital.inception.liquibase;

import liquibase.database.Database;
import liquibase.database.core.H2Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.TimeType;
import org.springframework.util.StringUtils;

/**
 * The <b>H2TimeType</b> class overrides the default time mapping for Liquibase when working with an
 * H2 database to increase the timestamp precision to include nanoseconds. This is required when
 * using Java 15+ on Linux and Windows where nanosecond date-time values are used.
 *
 * @author Marcus Portmann
 */
@DataTypeInfo(
    name = "time",
    aliases = {
      "java.sql.Types.TIME",
      "java.sql.Types.TIMES_WITH_TIMEZONE",
      "java.sql.Time",
      "timetz"
    },
    minParameters = 0,
    maxParameters = 1,
    priority = LiquibaseDataType.PRIORITY_DEFAULT + 1)
public class H2TimeType extends TimeType {

  /** Constructs a new <b>H2TimeType</b>. */
  public H2TimeType() {}

  @Override
  public DatabaseDataType toDatabaseDataType(Database database) {
    String originalDefinition =
        StringUtils.hasText(getRawDefinition())
            ? StringUtils.trimAllWhitespace(getRawDefinition())
            : "";

    if (database instanceof H2Database) {
      if ("timewithtimezone".equalsIgnoreCase(originalDefinition)) {
        return new DatabaseDataType("TIME(9) WITH TIME ZONE");
      } else if ("time with time zone".equalsIgnoreCase(originalDefinition)) {
        return new DatabaseDataType("TIME(9) WITH TIME ZONE");
      } else if ("time".equalsIgnoreCase(originalDefinition)) {
        return new DatabaseDataType("TIME(9)");
      }
    }

    return super.toDatabaseDataType(database);
  }
}
