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

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * The {@code TemplateRenderer} renders text templates that contain dynamic expressions and function
 * calls.
 *
 * <p>During the rendering the {@code TemplateRenderer} parses placeholders such as {@code {{
 * json:$.title }}} and {@code {{ uppercase(xpath:/document/title) }}}, delegates expression
 * evaluation to registered {@link ExpressionEvaluator evaluators} (e.g., JSONPath/XPath), invokes
 * registered {@link TemplateFunction functions}, and produces the final rendered output.
 *
 * <h2>Overview</h2>
 *
 * <ol>
 *   <li>Parse template placeholders delimited by <code>{{ ... }}</code>.
 *   <li>Resolve expressions using the appropriate {@link ExpressionEvaluator} based on a prefix
 *       (for example, {@code json:} or {@code xpath:}).
 *   <li>Execute registered {@link TemplateFunction}s (for example, {@code uppercase}, {@code now}).
 *   <li>Substitute results back into the template to yield the rendered string.
 * </ol>
 *
 * <h3>Extensibility</h3>
 *
 * <ul>
 *   <li>Register custom {@link TemplateFunction}s to extend transformation/formatting behaviors.
 *   <li>Register custom {@link ExpressionEvaluator}s to support additional data source types or
 *       syntaxes.
 * </ul>
 *
 * <h3>Example</h3>
 *
 * <pre>{@code
 * TemplateRenderer renderer = new TemplateRenderer();
 * renderer.registerEvaluator("json", new JsonPathEvaluator());
 * renderer.registerEvaluator("xpath", new XPathEvaluator());
 * renderer.registerFunction("uppercase", BuiltInTemplateFunctions.UPPERCASE);
 *
 * String template = "<h1>{{ uppercase(json:$.title) }}</h1>";
 * String json = "{ \"title\": \"Hello\" }";
 *
 * String result = renderer.render(template, json);
 * // result: "<h1>HELLO</h1>"
 * }</pre>
 *
 * @author Marcus Portmann
 */
@Component
@SuppressWarnings("unused")
public class TemplateRenderer {

  private final ControlStructureHandler controlHandler;

  private final Map<String, ExpressionEvaluator> evaluators;

  private final boolean failOnMissingPath = false;

  private final FunctionHandler functionHandler;

  private final Map<String, TemplateFunction> functions;

  /** Creates a new {@code TemplateRenderer}. */
  public TemplateRenderer() {
    this.evaluators = new HashMap<>();
    this.functions = new HashMap<>();
    this.controlHandler = new ControlStructureHandler(this);
    this.functionHandler = new FunctionHandler(this);

    // Register default evaluators
    registerEvaluator("xpath", new XPathEvaluator());
    registerEvaluator("json", new JsonPathEvaluator());
    registerEvaluator("map", new MapEvaluator());

    // Register built-in functions
    registerBuiltInFunctions();
  }

  /**
   * Parses an XML string into a DOM {@link Document}.
   *
   * @param xmlString the XML content
   * @return a namespace-aware {@link Document}
   * @throws TemplateRenderException if parsing fails
   */
  public static Document parseXml(String xmlString) throws TemplateRenderException {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.parse(new InputSource(new StringReader(xmlString)));
    } catch (Exception e) {
      throw new TemplateRenderException("Failed to parse XML", e);
    }
  }

  /**
   * Registers an {@link ExpressionEvaluator} that evaluates expressions with the given prefix.
   *
   * <p>Examples:
   *
   * <ul>
   *   <li>{@code "json"} → {@link JsonPathEvaluator} to enable {@code {{ json:$.title }}}
   *   <li>{@code "xpath"} → {@link XPathEvaluator} to enable {@code {{ xpath:/document/title }}}
   * </ul>
   *
   * @param prefix the evaluator prefix (e.g., {@code "json"}, {@code "xpath"})
   * @param evaluator the evaluator instance to register
   */
  public void registerEvaluator(String prefix, ExpressionEvaluator evaluator) {
    evaluators.put(prefix.toLowerCase(), evaluator);
  }

  /**
   * Registers a {@link TemplateFunction} that can be invoked within templates by name.
   *
   * <p>Example:
   *
   * <pre>{@code
   * renderer.registerFunction("uppercase", BuiltInTemplateFunctions.UPPERCASE);
   * // Usage in template: {{ uppercase(json:$.title) }}
   * }</pre>
   *
   * @param name the name used to invoke the function in template expressions
   * @param function the function implementation to register
   */
  public void registerFunction(String name, TemplateFunction function) {
    functions.put(name.toLowerCase(), function);
  }

  /**
   * Renders the specified template using the provided data source.
   *
   * <p>The rendering process evaluates expressions and functions contained in the template and
   * substitutes their results into the output.
   *
   * @param template the template content
   * @param dataSource the data source (e.g., XML {@link Document}, JSON string, or {@link
   *     java.util.Map})
   * @return the rendered output
   * @throws TemplateRenderException if parsing or evaluation fails
   */
  public String render(String template, Object dataSource) throws TemplateRenderException {
    TemplateContext context = new TemplateContext(dataSource);
    return processTemplate(template, context);
  }

  String evaluateExpression(String expression, TemplateContext context)
      throws TemplateRenderException {
    expression = expression.trim();

    // 1) Evaluator-prefixed expressions should be evaluated first
    int colon = expression.indexOf(':');
    if (colon > 0) {
      String prefix = expression.substring(0, colon).toLowerCase();
      ExpressionEvaluator evaluator = evaluators.get(prefix);
      if (evaluator != null && evaluator.supports(context.getDataSource())) {
        return evaluateSimpleExpression(expression, context); // lets XPath handle count(), etc.
      }
    }

    // 2) Function calls (template functions like uppercase(...))
    if (expression.contains("(") && expression.contains(")")) {
      return functionHandler.handleFunctionCall(expression, context);
    }

    // 3) Control structures
    if (expression.startsWith("#")) {
      return controlHandler.render(expression, context);
    }

    // 4) Fallback
    return evaluateSimpleExpression(expression, context);
  }

  String evaluateSimpleExpression(String expression, TemplateContext context)
      throws TemplateRenderException {
    if (expression.isEmpty()) {
      return "";
    }

    // Variable reference: {{$varName}}
    if (expression.startsWith("$")) {
      Object variable = context.getVariable(expression.substring(1));
      return variable != null ? variable.toString() : "";
    }

    // Evaluator prefix: prefix:expression
    int colonIndex = expression.indexOf(':');
    if (colonIndex > 0) {
      String prefix = expression.substring(0, colonIndex).toLowerCase();
      String pathExpression = expression.substring(colonIndex + 1);

      ExpressionEvaluator evaluator = evaluators.get(prefix);
      if (evaluator != null && evaluator.supports(context.getDataSource())) {
        try {
          String value = evaluator.evaluate(pathExpression, context.getDataSource());
          if (value.isEmpty() && failOnMissingPath) {
            throw new TemplateRenderException("Path not found: " + expression);
          }
          return value;
        } catch (ExpressionEvaluationException e) {
          if (failOnMissingPath) {
            throw new TemplateRenderException("Failed to evaluate expression: " + expression, e);
          }
          // Gracefully degrade on missing paths/errors if not failing hard
          return "";
        }
      }
    }

    // No evaluator matched — treat as literal text
    return expression;
  }

  // Helper for internal use by handlers
  TemplateFunction getFunction(String name) {
    return functions.get(name.toLowerCase());
  }

  boolean hasEvaluatorPrefix(String value) {
    int colon = value.indexOf(':');
    if (colon <= 0) return false;
    String prefix = value.substring(0, colon).toLowerCase();
    return evaluators.containsKey(prefix);
  }

  String renderFragment(String fragment, TemplateContext context) throws TemplateRenderException {
    return processTemplate(fragment, context);
  }

  private String handleControlStructures(String template, TemplateContext context) {
    // Placeholder behavior: strip control tags.
    // A complete implementation would evaluate and render the block logic.
    return template.replaceAll("\\{\\{#.*?\\}\\}", "").replaceAll("\\{\\{/.*?\\}\\}", "");
  }

  private String processTemplate(String template, TemplateContext context)
      throws TemplateRenderException {
    // First, handle control structures (e.g., if/each/with) — placeholder implementation
    template = handleControlStructures(template, context);

    // Then process simple expressions {{ ... }}
    Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
    Matcher matcher = pattern.matcher(template);
    StringBuffer result = new StringBuffer();

    while (matcher.find()) {
      String expression = matcher.group(1).trim();
      String replacement;

      if (expression.startsWith("#") || expression.startsWith("/")) {
        // Control-structure tags were handled already
        replacement = "";
      } else {
        replacement = evaluateExpression(expression, context);
      }

      matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
    }
    matcher.appendTail(result);

    return result.toString();
  }

  private void registerBuiltInFunctions() {
    registerFunction("uppercase", BuiltInTemplateFunctions.UPPERCASE);
    registerFunction("lowercase", BuiltInTemplateFunctions.LOWERCASE);
    registerFunction("trim", BuiltInTemplateFunctions.TRIM);
    registerFunction("now", BuiltInTemplateFunctions.NOW);
    registerFunction("substring", BuiltInTemplateFunctions.SUBSTRING);
    registerFunction("length", BuiltInTemplateFunctions.LENGTH);
    registerFunction("default", BuiltInTemplateFunctions.DEFAULT);
    registerFunction("replace", BuiltInTemplateFunctions.REPLACE);
    registerFunction("concat", BuiltInTemplateFunctions.CONCAT);
  }
}
