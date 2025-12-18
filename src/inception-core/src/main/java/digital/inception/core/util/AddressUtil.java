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

package digital.inception.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/**
 * The {@code AddressUtil} class is a utility class that provides methods for working with address
 * strings.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class AddressUtil {

  private static final Pattern STREET_NUMBER_PATTERN = Pattern.compile("^\\s*(\\d+[A-Za-z]*)\\b");

  /** Private constructor to prevent instantiation. */
  private AddressUtil() {}

  /**
   * Extracts the street name from an address line.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>{@code "16 Baker Street"} → {@code "Baker Street"}
   *   <li>{@code "16B Baker Street"} → {@code "Baker Street"}
   *   <li>{@code " 101A Main Road"} → {@code "Main Road"}
   * </ul>
   *
   * @param addressLine1 the full address line 1 (street number and street name)
   * @return the street name part, or {@code null} if none found
   */
  public static String extractStreetName(String addressLine1) {
    if (!StringUtils.hasText(addressLine1)) {
      return null;
    }

    Matcher matcher = STREET_NUMBER_PATTERN.matcher(addressLine1);
    if (matcher.find()) {
      // Everything after the matched street number is the street name
      int endOfNumber = matcher.end();
      String remainder = addressLine1.substring(endOfNumber).trim();
      return remainder.isEmpty() ? null : remainder;
    }

    return null;
  }

  /**
   * Extracts the street number from an address line.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>{@code "16 Baker Street"} → {@code "16"}
   *   <li>{@code "16B Baker Street"} → {@code "16B"}
   *   <li>{@code " 101A Main Road"} → {@code "101A"}
   * </ul>
   *
   * @param addressLine1 the full address line 1 (street number and street name)
   * @return the street number part, or {@code null} if none found
   */
  public static String extractStreetNumber(String addressLine1) {
    if (!StringUtils.hasText(addressLine1)) {
      return null;
    }

    Matcher matcher = STREET_NUMBER_PATTERN.matcher(addressLine1);
    if (matcher.find()) {
      return matcher.group(1);
    }

    return null;
  }
}
