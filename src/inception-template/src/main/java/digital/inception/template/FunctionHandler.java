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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and executes function calls found inside template expressions.
 *
 * <p>{@code FunctionHandler} is responsible for recognizing function-call syntax within {@code {{
 * ... }}} placeholders, parsing arguments (including nested expressions and quoted string
 * literals), evaluating argument expressions through the {@link TemplateRenderer}, and invoking the
 * target {@link TemplateFunction}.
 *
 * @param templateRenderer the {@link TemplateRenderer} to use for evaluating expressions
 * @author Marcus Portmann
 */
public record FunctionHandler(TemplateRenderer templateRenderer) {

  /**
   * Parses a function invocation from the given expression and executes it.
   *
   * <p>Function syntax is {@code <name>(arg1, arg2, ...)} where each argument may be:
   *
   * <ul>
   *   <li>A quoted string literal (single or double quotes), e.g. {@code 'Hello'}, {@code "World"}.
   *   <li>An evaluator-prefixed expression (e.g., {@code json:$.title}, {@code xpath:/doc/title}).
   *   <li>A nested function call (parentheses are tracked during parsing).
   * </ul>
   *
   * <p>Expression arguments are evaluated using {@link
   * TemplateRenderer#evaluateSimpleExpression(String, TemplateContext)} before the target {@link
   * TemplateFunction} is invoked. String literals have their surrounding quotes removed.
   *
   * @param expression the raw function expression (e.g., {@code uppercase(json:$.title)})
   * @param context the current rendering {@link TemplateContext}
   * @return the string result produced by the invoked {@link TemplateFunction}, or an empty string
   *     if no function call is detected
   * @throws TemplateRenderException if the function is unknown or a rendering error occurs
   */
  public String handleFunctionCall(String expression, TemplateContext context)
      throws TemplateRenderException {
    // Pattern pattern = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*)\\(([^)]*)\\)");
    Pattern pattern = Pattern.compile("^\\s*([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\((.*)\\)\\s*$");

    Matcher matcher = pattern.matcher(expression);

    if (matcher.find()) {
      String functionName = matcher.group(1);
      String argsString = matcher.group(2);
      String[] args = parseArguments(argsString);

      // Evaluate each argument (they might be expressions themselves)
      for (int i = 0; i < args.length; i++) {
        if (isExpression(args[i])) {
          args[i] = templateRenderer.evaluateSimpleExpression(args[i].trim(), context);
        } else {
          // Remove quotes from string literals
          args[i] = stripQuotes(args[i].trim());
        }
      }

      TemplateFunction function = templateRenderer.getFunction(functionName);
      if (function != null) {
        return function.execute(context, args);
      } else {
        throw new TemplateRenderException("Unknown function: " + functionName);
      }
    }

    return "";
  }

  /**
   * Determines whether the provided token should be treated as an expression that must be evaluated
   * by an {@link ExpressionEvaluator} (via {@link TemplateRenderer}).
   *
   * <p>Currently recognizes prefixes {@code xpath:} and {@code json:}.
   *
   * @param value the token to inspect
   * @return {@code true} if the token begins with a known evaluator prefix; {@code false} otherwise
   */
  private boolean isExpression(String value) {
    return templateRenderer.hasEvaluatorPrefix(value);
  }

  /**
   * Splits an argument list into individual arguments, preserving quoted strings and tracking
   * nested parentheses to avoid premature splitting on commas inside nested calls.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>{@code " 'A, B', json:$.title "} → {@code ["'A, B'", "json:$.title"]}
   *   <li>{@code " substring(json:$.name, 0, 3), 'X' "} → {@code ["substring(json:$.name, 0, 3)",
   *       "'X'"]}
   * </ul>
   *
   * @param argsString the raw argument list between parentheses; may be {@code null} or empty
   * @return an array of raw argument tokens (whitespace trimmed but quotes retained), never {@code
   *     null}
   */
  private String[] parseArguments(String argsString) {
    if (argsString == null || argsString.trim().isEmpty()) {
      return new String[0];
    }

    List<String> args = new ArrayList<>();
    StringBuilder currentArg = new StringBuilder();
    boolean inQuotes = false;
    int parenDepth = 0;

    for (char c : argsString.toCharArray()) {
      if (c == '\''
          && (currentArg.isEmpty() || currentArg.charAt(currentArg.length() - 1) != '\\')) {
        inQuotes = !inQuotes;
        currentArg.append(c); // Keep quotes for now; we'll strip them later.
      } else if (c == '(' && !inQuotes) {
        parenDepth++;
        currentArg.append(c);
      } else if (c == ')' && !inQuotes) {
        parenDepth--;
        currentArg.append(c);
      } else if (c == ',' && !inQuotes && parenDepth == 0) {
        args.add(currentArg.toString().trim());
        currentArg = new StringBuilder();
        continue;
      } else {
        currentArg.append(c);
      }
    }

    if (!currentArg.isEmpty()) {
      args.add(currentArg.toString().trim());
    }

    return args.toArray(new String[0]);
  }

  /**
   * Removes surrounding single or double quotes from a string literal, if present. Escaped quotes
   * within the string are not modified.
   *
   * @param value the literal value (possibly quoted)
   * @return the unquoted value if quotes were present; otherwise the original value
   */
  private String stripQuotes(String value) {
    if (value != null && value.length() >= 2) {
      if ((value.startsWith("'") && value.endsWith("'"))
          || (value.startsWith("\"") && value.endsWith("\""))) {
        return value.substring(1, value.length() - 1);
      }
    }
    return value;
  }
}
