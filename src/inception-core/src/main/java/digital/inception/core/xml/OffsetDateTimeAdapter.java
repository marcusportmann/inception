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

package digital.inception.core.xml;

import digital.inception.core.util.ISO8601Util;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.OffsetDateTime;

/**
 * The {@code OffsetDateTimeAdapter} class implements a JAXB 2.0 adapter used to convert between
 * {@code String} and {@code OffsetDateTime} types. <br>
 * Can be used when customizing XML Schema to Java Representation Binding (XJC).
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

  /** Constructs a new {@code OffsetDateTimeAdapter}. */
  public OffsetDateTimeAdapter() {}

  /**
   * Marshals the {@code java.time.OffsetDateTime} value as an ISO8601 string.
   *
   * @param value the value to marshal
   * @return the {@code java.time.OffsetDateTime} value as an ISO8601 string
   */
  public static String toISO8601(OffsetDateTime value) {
    if (value == null) {
      return null;
    }

    return ISO8601Util.fromOffsetDateTime(value);
  }

  /**
   * Unmarshals the ISO8601 string value as a {@code java.time.OffsetDateTime}.
   *
   * @param value the ISO8601 string value
   * @return the ISO8601 string value as a {@code java.time.OffsetDateTime}
   */
  public static OffsetDateTime toOffsetDateTime(String value) {
    if (value == null) {
      return null;
    }

    try {
      return ISO8601Util.toOffsetDateTime(value);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to parse the xs:dateTime value (" + value + ")");
    }
  }

  /**
   * Marshals the {@code java.time.OffsetDateTime} value as an ISO8601 string.
   *
   * @param value the value to marshal
   * @return the {@code java.time.OffsetDateTime} value as an ISO8601 string
   */
  @Override
  public String marshal(OffsetDateTime value) {
    if (value == null) {
      return null;
    }

    return ISO8601Util.fromOffsetDateTime(value);
  }

  /**
   * Unmarshals the ISO8601 string value as a {@code java.time.OffsetDateTime}.
   *
   * @param value the ISO8601 string value
   * @return the ISO8601 string value as a {@code java.time.OffsetDateTime}
   */
  @Override
  public OffsetDateTime unmarshal(String value) {
    if (value == null) {
      return null;
    }

    try {
      return ISO8601Util.toOffsetDateTime(value);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to parse the xs:dateTime value (" + value + ")");
    }
  }
}
