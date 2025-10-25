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
 * Defines the contract for evaluating expressions within a template against a specific type of data
 * source, such as XML, JSON, or Java objects.
 *
 * <p>Implementations of {@code ExpressionEvaluator} provide a mechanism for resolving template
 * expressions like {@code {{ xpath:/document/title }}} or {@code {{ json:$.author }}} by reading
 * data from the given {@code dataSource}.
 *
 * <p>The template renderer delegates the resolution of expressions to registered {@code
 * ExpressionEvaluator} instances. Each evaluator is responsible for handling a specific type of
 * data source format. For example:
 *
 * <ul>
 *   <li><b>XPathExpressionEvaluator</b> — evaluates XPath expressions against XML documents.
 *   <li><b>JsonPathExpressionEvaluator</b> — evaluates JSONPath expressions against JSON data.
 *   <li><b>MapExpressionEvaluator</b> — resolves expressions against Java {@code Map} instances.
 * </ul>
 *
 * <p>The template renderer typically maintains a registry of evaluators. During template parsing,
 * it determines which evaluator supports the current data source by invoking {@link
 * #supports(Object)} and delegates expression resolution to the matching evaluator’s {@link
 * #evaluate(String, Object)} method.
 *
 * <p>Implementations should throw an {@link ExpressionEvaluationException} when an expression is
 * syntactically invalid, references missing data, or cannot be evaluated successfully.
 *
 * @author Marcus Portmann
 */
public interface ExpressionEvaluator {

  /**
   * Evaluates the specified expression against the given data source and returns the resulting
   * string value.
   *
   * <p>The expression syntax and evaluation semantics depend on the specific implementation (for
   * example, XPath, JSONPath, or property resolution).
   *
   * @param expression the expression to evaluate (for example, an XPath or JSONPath string)
   * @param dataSource the data source to evaluate the expression against (for example, an XML
   *     {@code Document}, JSON object, or {@code Map})
   * @return the result of evaluating the expression as a string (never {@code null})
   * @throws ExpressionEvaluationException if the expression cannot be evaluated due to syntax
   *     errors, missing data, or unsupported data source
   */
  String evaluate(String expression, Object dataSource) throws ExpressionEvaluationException;

  /**
   * Determines whether this evaluator supports evaluating expressions against the specified data
   * source type.
   *
   * <p>For example, an {@code XPathExpressionEvaluator} would return {@code true} for XML {@code
   * Document} objects, while a {@code JsonPathExpressionEvaluator} would support JSON objects or
   * strings.
   *
   * @param dataSource the data source to check
   * @return {@code true} if this evaluator can handle the specified data source or {@code false}
   *     otherwise
   */
  boolean supports(Object dataSource);
}
