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

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * Evaluates JSONPath expressions against JSON {@link String} data sources.
 *
 * <p>Supports evaluating expressions such as {@code $.author} or {@code $.items[0].title} within
 * JSON-based templates rendered by the template renderer.
 *
 * <p><b>Supported Data Sources:</b> JSON strings.
 *
 * @author Marcus Portmann
 */
public class JsonPathEvaluator implements ExpressionEvaluator {

  /** Constructs a new {@code JsonPathEvaluator}. */
  public JsonPathEvaluator() {}

  @Override
  public String evaluate(String expression, Object dataSource)
      throws ExpressionEvaluationException {
    if (!supports(dataSource)) {
      throw new ExpressionEvaluationException(
          "DataSource must be a JSON string for JSONPath evaluation");
    }

    try {
      Object document =
          Configuration.defaultConfiguration().jsonProvider().parse((String) dataSource);

      Object result = JsonPath.read(document, expression);
      return result != null ? result.toString() : "";
    } catch (PathNotFoundException e) {
      return ""; // Graceful fallback for missing paths
    } catch (Throwable e) {
      throw new ExpressionEvaluationException(
          "JSONPath evaluation failed for the expression: " + expression, e);
    }
  }

  @Override
  public boolean supports(Object dataSource) {
    return dataSource instanceof String;
  }
}
