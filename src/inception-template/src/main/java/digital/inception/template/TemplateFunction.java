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

/**
 * A functional interface representing a callable function that can be executed within a template
 * rendering context.
 *
 * <p>Implementations of {@code TemplateFunction} define custom operations that may be invoked from
 * within a template rendered by a {@link TemplateRenderer} implementation. Functions provide
 * reusable logic that operates on data available in the {@link TemplateContext}, such as text
 * transformation, formatting, or custom lookups.
 *
 * @author Marcus Portmann
 */
@FunctionalInterface
public interface TemplateFunction {

  /**
   * Executes the function using the provided {@link TemplateContext} and arguments.
   *
   * @param templateContext the {@link TemplateContext} containing the data source, variables, and
   *     local scope for the current rendering operation
   * @param args the evaluated arguments passed to the function from the template expression; may be
   *     empty but never {@code null}
   * @return the string result of the function to be substituted into the rendered template
   * @throws TemplateRenderException if an error occurs during function execution, such as invalid
   *     arguments or evaluation errors
   */
  String execute(TemplateContext templateContext, String... args) throws TemplateRenderException;
}
