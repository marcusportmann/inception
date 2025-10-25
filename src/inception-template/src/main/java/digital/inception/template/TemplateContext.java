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

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code TemplateContext} class represents the execution context for a template being rendered
 * by a {@link TemplateRenderer}.
 *
 * <p>It encapsulates the data source used by the template renderer (for example, an XML {@code
 * Document}, a JSON string, or a {@code Map}), and provides a mechanism to manage global and local
 * variables accessible during template evaluation.
 *
 * <p>A {@code TemplateContext} instance provides:
 *
 * <ul>
 *   <li>A reference to the data source from which expressions such as {@code {{ json:$.title }}} or
 *       {@code {{ xpath:/document/title }}} are resolved.
 *   <li>A <em>global variable scope</em> that persists throughout the entire template rendering
 *       process.
 *   <li>A <em>local scope</em> used for transient or block-specific variables during evaluation.
 * </ul>
 *
 * <p>When rendering templates with a {@link TemplateRenderer} implementation, a {@code
 * TemplateContext} is automatically created and managed by the templateRenderer to:
 *
 * <ul>
 *   <li>Resolve JSONPath or XPath expressions against the data source.
 *   <li>Store intermediate variables such as loop counters or formatted values.
 *   <li>Provide user-defined variables for use across multiple templates.
 * </ul>
 *
 * @author Marcus Portmann
 */
public final class TemplateContext {

  /**
   * The data source used to resolve expressions in the template.
   *
   * <p>This can be an XML {@code Document}, a JSON object, or a {@code Map}.
   */
  private final Object dataSource;

  /**
   * The local variable scope for storing temporary variables during template rendering.
   *
   * <p>These values are typically cleared after a template block (such as a loop or conditional) is
   * processed.
   */
  private final Map<String, Object> localScope;

  /**
   * The global variable scope for storing variables that persist across the entire template
   * rendering process.
   */
  private final Map<String, Object> variables;

  /**
   * Constructs a new {@code TemplateContext} with the specified data source.
   *
   * @param dataSource the data source used for resolving template expressions, such as an XML
   *     {@code Document}, JSON data, or a {@code Map}
   */
  public TemplateContext(Object dataSource) {
    this.dataSource = dataSource;
    this.variables = new HashMap<>();
    this.localScope = new HashMap<>();
  }

  /**
   * Clears all variables from the local scope.
   *
   * <p>This is typically used between template sections to ensure that temporary variables do not
   * leak into subsequent evaluations.
   */
  public void clearLocalScope() {
    localScope.clear();
  }

  /**
   * Returns the data source used for expression evaluation.
   *
   * @return the data source object (for example, XML {@code Document}, JSON string, or {@code Map})
   */
  public Object getDataSource() {
    return dataSource;
  }

  /**
   * Returns the value of a local variable with the specified name.
   *
   * @param name the name of the local variable
   * @return the local variable value, or {@code null} if not defined
   */
  public Object getLocal(String name) {
    return localScope.get(name);
  }

  /**
   * Returns the value of a global variable with the specified name.
   *
   * @param name the name of the global variable
   * @return the variable value, or {@code null} if not defined
   */
  public Object getVariable(String name) {
    return variables.get(name);
  }

  /**
   * Sets or updates a local variable in the current rendering scope.
   *
   * @param name the name of the local variable
   * @param value the value to assign to the variable
   */
  public void setLocal(String name, Object value) {
    localScope.put(name, value);
  }

  /**
   * Sets or updates a global variable that will be available throughout the entire template
   * rendering process.
   *
   * @param name the name of the variable
   * @param value the value to assign to the variable
   */
  public void setVariable(String name, Object value) {
    variables.put(name, value);
  }
}
