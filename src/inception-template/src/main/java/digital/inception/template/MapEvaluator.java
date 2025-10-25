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

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import java.util.Map;

/**
 * Evaluates JSONPath expressions against {@link Map}-based data sources.
 *
 * <p>This evaluator allows template expressions such as {@code $.metadata.created} or {@code
 * $.items[0].title} to be resolved directly from a {@link Map} representing parsed JSON data.
 *
 * <p><b>Supported Data Sources:</b> {@link Map} instances.
 *
 * @author Marcus Portmann
 */
public class MapEvaluator implements ExpressionEvaluator {

  /** Constructs a new {@code MapEvaluator}. */
  public MapEvaluator() {}

  @Override
  public String evaluate(String expression, Object dataSource)
      throws ExpressionEvaluationException {
    if (!supports(dataSource)) {
      throw new ExpressionEvaluationException("DataSource must be a Map for MapEvaluator");
    }

    try {
      Object result = JsonPath.read(dataSource, expression);
      return result != null ? result.toString() : "";
    } catch (PathNotFoundException e) {
      return "";
    } catch (Throwable e) {
      throw new ExpressionEvaluationException(
          "Map JSONPath evaluation failed for expression: " + expression, e);
    }
  }

  @Override
  public boolean supports(Object dataSource) {
    return dataSource instanceof Map;
  }
}
