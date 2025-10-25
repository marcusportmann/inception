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

package digital.inception.template;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides a collection of built-in {@link TemplateFunction} implementations that perform common
 * string manipulation and date formatting operations.
 *
 * <p>These functions are intended for use with a template renderer implementation to simplify
 * common transformation and formatting tasks when rendering templates. Each function can be invoked
 * directly from a template expression, for example:
 *
 * <pre>{@code
 * <p>{{ uppercase(json:$.title) }}</p>
 * <p>{{ now('yyyy-MM-dd') }}</p>
 * <p>{{ default(json:$.author, 'Unknown') }}</p>
 * }</pre>
 *
 * <p>The {@code BuiltInTemplateFunctions} class defines several reusable {@link TemplateFunction}
 * constants that can be registered with a {@link TemplateRenderer} implementation. These functions
 * operate on text and date values, returning a {@link String} result to be inserted into the
 * rendered output.
 *
 * <table>
 *   <caption>Summary of Built-In Functions</caption>
 *   <tr><th>Function</th><th>Description</th><th>Example Usage</th></tr>
 *   <tr>
 *     <td>{@link #UPPERCASE}</td>
 *     <td>Converts the input string to uppercase.</td>
 *     <td><code>{{ uppercase('hello') }}</code> → <code>HELLO</code></td>
 *   </tr>
 *   <tr>
 *     <td>{@link #LOWERCASE}</td>
 *     <td>Converts the input string to lowercase.</td>
 *     <td><code>{{ lowercase('HELLO') }}</code> → <code>hello</code></td>
 *   </tr>
 *   <tr>
 *     <td>{@link #TRIM}</td>
 *     <td>Removes leading and trailing whitespace.</td>
 *     <td><code>{{ trim('  padded text  ') }}</code> → <code>padded text</code></td>
 *   </tr>
 *   <tr>
 *     <td>{@link #NOW}</td>
 *     <td>Returns the current date and time formatted using the provided pattern (default <code>yyyy-MM-dd HH:mm:ss</code>).</td>
 *     <td><code>{{ now('yyyy-MM-dd') }}</code> → <code>2025-10-24</code></td>
 *   </tr>
 *   <tr>
 *     <td>{@link #SUBSTRING}</td>
 *     <td>Extracts a substring from the input string using start and optional end indices.</td>
 *     <td><code>{{ substring('Template', 0, 4) }}</code> → <code>Temp</code></td>
 *   </tr>
 *   <tr>
 *     <td>{@link #LENGTH}</td>
 *     <td>Returns the number of characters in the input string.</td>
 *     <td><code>{{ length('Template') }}</code> → <code>8</code></td>
 *   </tr>
 *   <tr>
 *     <td>{@link #DEFAULT}</td>
 *     <td>Returns the first argument if non-empty; otherwise returns the specified default value.</td>
 *     <td><code>{{ default('', 'N/A') }}</code> → <code>N/A</code></td>
 *   </tr>
 *   <tr>
 *     <td>{@link #REPLACE}</td>
 *     <td>Replaces all occurrences of a substring with another string.</td>
 *     <td><code>{{ replace('Hello World', 'World', 'Universe') }}</code> → <code>Hello Universe</code></td>
 *   </tr>
 *   <tr>
 *     <td>{@link #CONCAT}</td>
 *     <td>Concatenates all provided arguments into a single string.</td>
 *     <td><code>{{ concat('A', '-', 'B', '-', 'C') }}</code> → <code>A-B-C</code></td>
 *   </tr>
 * </table>
 *
 * <p>Each built-in function is designed to handle null or invalid arguments gracefully by returning
 * a sensible default value (such as an empty string). Functions that perform numeric parsing or
 * date formatting attempt to recover gracefully by using default behavior when invalid input is
 * encountered.
 *
 * @author Marcus Portmann
 */
public class BuiltInTemplateFunctions {

  /**
   * Concatenates all provided arguments into a single string.
   *
   * <p>Example:
   *
   * <pre>{@code
   * {{ concat('A', '-', 'B', '-', 'C') }} → A-B-C
   * }</pre>
   */
  public static final TemplateFunction CONCAT =
      (context, args) -> {
        if (args == null || args.length == 0) return "";
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
          result.append(arg);
        }
        return result.toString();
      };

  /**
   * Returns the first argument if non-empty; otherwise returns the specified default value.
   *
   * <p>Example:
   *
   * <pre>{@code
   * {{ default('', 'Unknown') }} → Unknown
   * }</pre>
   */
  public static final TemplateFunction DEFAULT =
      (context, args) -> {
        if (args == null || args.length == 0) return "";
        String value = args[0];
        String defaultValue = args.length > 1 ? args[1] : "";
        return value.isEmpty() ? defaultValue : value;
      };

  /**
   * Returns the number of characters in the input string. Example: <code>{{ length('Hello') }}
   * </code> → <code>5</code>
   */
  public static final TemplateFunction LENGTH =
      (context, args) -> {
        if (args == null || args.length == 0) return "0";
        return String.valueOf(args[0].length());
      };

  /**
   * Converts the input string to lowercase. Example: <code>{{ lowercase('HELLO') }}</code> → <code>
   * hello</code>
   */
  public static final TemplateFunction LOWERCASE =
      (context, args) -> {
        if (args == null || args.length == 0) return "";
        return args[0].toLowerCase();
      };

  /**
   * Returns the current date and time formatted according to the specified pattern.
   *
   * <p>If no format is provided, defaults to <code>yyyy-MM-dd HH:mm:ss</code>. If an invalid
   * pattern is supplied, the ISO local date-time format is used instead. Example:
   *
   * <pre>{@code
   * {{ now('yyyy-MM-dd') }} → 2025-10-24
   * }</pre>
   */
  public static final TemplateFunction NOW =
      (context, args) -> {
        String format = "yyyy-MM-dd HH:mm:ss";
        if (args != null && args.length > 0 && !args[0].isEmpty()) {
          format = args[0];
        }
        try {
          return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
          return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
      };

  /**
   * Replaces all occurrences of a substring within the input string with another string.
   *
   * <p>Example:
   *
   * <pre>{@code
   * {{ replace('Hello World', 'World', 'Universe') }} → Hello Universe
   * }</pre>
   */
  public static final TemplateFunction REPLACE =
      (context, args) -> {
        if (args == null || args.length < 3) return "";
        return args[0].replace(args[1], args[2]);
      };

  /**
   * Extracts a substring from the input string.
   *
   * <ul>
   *   <li>If two arguments are provided, extracts from {@code startIndex} to the end.
   *   <li>If three arguments are provided, extracts from {@code startIndex} to {@code endIndex}.
   * </ul>
   *
   * Example:
   *
   * <pre>{@code
   * {{ substring('Template', 0, 4) }} → Temp
   * }</pre>
   */
  public static final TemplateFunction SUBSTRING =
      (context, args) -> {
        if (args == null || args.length < 2) return "";
        try {
          String input = args[0];
          int start = Integer.parseInt(args[1]);
          if (args.length == 2) {
            return input.substring(start);
          } else {
            int end = Integer.parseInt(args[2]);
            return input.substring(start, end);
          }
        } catch (Exception e) {
          return "";
        }
      };

  /**
   * Trims leading and trailing whitespace from the input string. Example: <code>
   * {{ trim('  text  ') }}</code> → <code>text</code>
   */
  public static final TemplateFunction TRIM =
      (context, args) -> {
        if (args == null || args.length == 0) return "";
        return args[0].trim();
      };

  /**
   * Converts the input string to uppercase. Example: <code>{{ uppercase('hello') }}</code> → <code>
   * HELLO</code>
   */
  public static final TemplateFunction UPPERCASE =
      (context, args) -> {
        if (args == null || args.length == 0) return "";
        return args[0].toUpperCase();
      };

  /** Constructs a new {@code BuiltInTemplateFunctions}. */
  public BuiltInTemplateFunctions() {}
}
