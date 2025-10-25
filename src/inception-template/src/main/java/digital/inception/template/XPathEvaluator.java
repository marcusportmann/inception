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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

/**
 * An {@link ExpressionEvaluator} implementation that evaluates XPath expressions against XML {@link
 * Document} data sources.
 *
 * <p>This evaluator enables template expressions prefixed with <code>xpath:</code> to extract data
 * from XML documents during template rendering. It uses the standard Java {@link javax.xml.xpath}
 * API to compile and evaluate XPath expressions.
 *
 * <p>The {@code XPathEvaluator} allows templates to retrieve values from structured XML data by
 * evaluating XPath expressions. It is used in conjunction with the template renderer, which
 * automatically delegates expression evaluation to this class when encountering <code>xpath:</code>
 * expressions within a template.
 *
 * <p><b>Supported Data Sources</b>
 *
 * <ul>
 *   <li>An XML {@link Document} parsed using the DOM API.
 * </ul>
 *
 * <p><b>Error Handling</b>
 *
 * <ul>
 *   <li>If the provided data source is not an XML {@link Document}, an {@link
 *       ExpressionEvaluationException} is thrown.
 *   <li>If the XPath expression is invalid or cannot be evaluated, an {@link
 *       ExpressionEvaluationException} is thrown with a descriptive message.
 *   <li>Null results from the XPath expression are converted to empty strings.
 * </ul>
 *
 * @author Marcus Portmann
 */
public class XPathEvaluator implements ExpressionEvaluator {

  /** Constructs a new {@code XPathEvaluator}. */
  public XPathEvaluator() {}

  /**
   * Evaluates an XPath expression against the provided XML {@link Document} data source.
   *
   * <p>The expression is compiled and evaluated using the standard {@link javax.xml.xpath} API. If
   * the expression produces a {@code null} result, an empty string is returned.
   *
   * <p>If the data source is not an instance of {@link Document}, or if the expression cannot be
   * evaluated due to syntax or runtime errors, an {@link ExpressionEvaluationException} is thrown.
   *
   * @param expression the XPath expression to evaluate (for example, <code>/book/title</code>)
   * @param dataSource the XML {@link Document} serving as the data source
   * @return the evaluated expression result as a {@link String}, or an empty string if the
   *     expression resolves to {@code null}
   * @throws ExpressionEvaluationException if the data source is unsupported or evaluation fails due
   *     to invalid XPath syntax or runtime errors
   */
  @Override
  public String evaluate(String expression, Object dataSource)
      throws ExpressionEvaluationException {
    if (!supports(dataSource)) {
      throw new ExpressionEvaluationException(
          "DataSource must be a XML Document for XPath evaluation");
    }

    try {
      Document doc = (Document) dataSource;
      XPath xpath = XPathFactory.newInstance().newXPath();
      XPathExpression expr = xpath.compile(expression);
      String result = expr.evaluate(doc);
      return result != null ? result : "";
    } catch (Throwable e) {
      throw new ExpressionEvaluationException(
          "XPath evaluation failed for the expression: " + expression, e);
    }
  }

  /**
   * Determines whether this evaluator supports evaluating expressions against the specified data
   * source type.
   *
   * <p>This evaluator supports XML data sources represented as {@link Document} objects.
   *
   * @param dataSource the data source to check
   * @return {@code true} if the data source is an instance of {@link Document}; {@code false}
   *     otherwise
   */
  @Override
  public boolean supports(Object dataSource) {
    return dataSource instanceof Document;
  }
}
