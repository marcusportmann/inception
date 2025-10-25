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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles and expands control structures (e.g., <code>{{#if}}</code>, <code>{{#each}}</code>,
 * <code>{{#with}}</code>) within templates processed by {@link TemplateRenderer}.
 *
 * <p>This implementation currently supports:
 *
 * <ul>
 *   <li><b>If</b> blocks:
 *       <pre>{@code
 * {{#if condition}}
 *   ... true branch ...
 * {{/if}}
 *
 * }</pre>
 *   <li><b>If–Else</b> blocks:
 *       <pre>{@code
 * {{#if condition}}
 *   ... true branch ...
 * {{else}}
 *   ... false branch ...
 * {{/if}}
 *
 * }</pre>
 *   <li>Nested control structures of any supported type (e.g., nested <code>#if</code> blocks).
 * </ul>
 *
 * <p>The <b>#each</b> and <b>#with</b> directives are recognized but not yet fully implemented.
 * They currently render as empty strings to avoid leaving unprocessed control tags in the output.
 *
 * <h2>Implementation Details</h2>
 *
 * <ul>
 *   <li>The handler performs repeated single-pass scans of the template text to locate the next
 *       outermost control block (e.g., <code>{{#if ...}}</code> through <code>{{/if}}</code>).
 *   <li>When a block is found, it is evaluated and replaced with its rendered output. The process
 *       continues until no more control structures remain.
 *   <li>Nested content is re-rendered via {@link TemplateRenderer#renderFragment(String,
 *       TemplateContext)} to support recursive placeholder evaluation and nested control logic.
 * </ul>
 *
 * @param templateRenderer the {@link TemplateRenderer} to use for evaluating expressions
 * @author Marcus Portmann
 */
public record ControlStructureHandler(TemplateRenderer templateRenderer) {

  private static final String CLOSE_FMT = "\\{\\{\\/(%s)\\s*\\}\\}";

  private static final Pattern ELSE_TAG = Pattern.compile("\\{\\{\\s*else\\s*\\}\\}");

  // {{#if cond}}, {{/if}}, {{else}}
  private static final Pattern OPEN_TAG = Pattern.compile("\\{\\{#(if|each|with)\\s+([^}]*)\\}\\}");

  /**
   * Expands and evaluates all supported control structures (such as {@code #if}, {@code #if ...
   * {{else}}}, {@code #each}, and {@code #with}) within the given template text.
   *
   * <p>This method scans the input template for control-structure blocks using Handlebars-like
   * syntax (for example, <code>{{#if ...}}</code>, <code>{{else}}</code>, <code>{{/if}}</code>) and
   * evaluates them in the context of the provided {@link TemplateContext}.
   *
   * @param template the raw template text containing control-structure directives; must not be
   *     {@code null}
   * @param context the current rendering {@link TemplateContext} that provides variable resolution
   *     and evaluator access
   * @return the template with all recognized control structures expanded and rendered
   * @throws NullPointerException if {@code template} is {@code null}
   */
  public String render(String template, TemplateContext context) {
    Objects.requireNonNull(template, "template");
    StringBuilder working = new StringBuilder(template);

    while (true) {
      Block block = findNextOutermostBlock(working);
      if (block == null) {
        break; // no more control blocks
      }

      String replacement;
      try {
        replacement =
            switch (block.kind) {
              case IF -> renderIf(block, context);
              case EACH -> renderEach(block, context); // currently stub
              case WITH -> renderWith(block, context); // currently stub
            };
      } catch (TemplateRenderException e) {
        // Fail-safe: replace the block with empty string on error
        replacement = "";
      }

      // Splice in the rendered content
      working.replace(block.start, block.end, replacement);
    }

    return working.toString();
  }

  private Block findNextOutermostBlock(CharSequence src) {
    Matcher open = OPEN_TAG.matcher(src);
    if (!open.find()) {
      return null;
    }

    // First open tag
    int openStart = open.start();
    String directive = open.group(1); // if | each | with
    String argument = open.group(2).trim(); // expression after directive

    Kind kind =
        switch (directive) {
          case "if" -> Kind.IF;
          case "each" -> Kind.EACH;
          case "with" -> Kind.WITH;
          default -> null;
        };
    if (kind == null) return null;

    // Find its matching close, respecting nesting for the *same* directive
    Pattern closePattern = Pattern.compile(String.format(CLOSE_FMT, directive));
    Matcher rest =
        Pattern.compile(
                "(?:"
                    + OPEN_TAG.pattern()
                    + ")|(?:"
                    + closePattern.pattern()
                    + ")|(?:"
                    + ELSE_TAG.pattern()
                    + ")",
                Pattern.DOTALL)
            .matcher(src);

    // Position the matcher to start scanning *after* this opening tag
    rest.region(open.end(), src.length());

    int depth = 1;
    int elsePos = -1;
    int trueStart = open.end();
    int closePos = -1;

    while (rest.find()) {
      String m = rest.group();

      // Another open tag?
      if (m.startsWith("{{#")) {
        String innerDirective = rest.group(1); // from OPEN_TAG's first group
        if (directive.equals(innerDirective)) {
          depth++; // nested same-kind; must be closed before we can close this one
        }
        continue;
      }

      // Else tag?
      if (m.matches(ELSE_TAG.pattern())) {
        // Only capture the else that belongs to our current depth
        if (depth == 1 && elsePos < 0) {
          elsePos = rest.start();
        }
        continue;
      }

      // A close tag for this directive?
      if (m.startsWith("{{/")) {
        depth--;
        if (depth == 0) {
          closePos = rest.start();
          break;
        }
      }
    }

    if (closePos < 0) {
      // Malformed: no closing tag; treat as plain text (skip this open and search again)
      // Move the search past this open tag by recursion on substring.
      // Simpler: return null to avoid infinite loop; caller will stop.
      return null;
    }

    String full = src.subSequence(openStart, rest.end()).toString();
    String between = src.subSequence(trueStart, closePos).toString();

    String truePart, falsePart;
    if (elsePos >= 0) {
      truePart = src.subSequence(trueStart, elsePos).toString();
      // else token ends at rest.start() of that match; we need to know where it ended,
      // but since we didn’t store it, recompute for safety:
      Matcher elseHere = ELSE_TAG.matcher(src);
      elseHere.region(elsePos, closePos);
      if (elseHere.find()) {
        int elseEnd = elseHere.end();
        falsePart = src.subSequence(elseEnd, closePos).toString();
      } else {
        falsePart = "";
      }
    } else {
      truePart = between;
      falsePart = "";
    }

    Block b = new Block();
    b.kind = kind;
    b.argument = argument;
    b.truePart = truePart;
    b.falsePart = falsePart;
    b.start = openStart;
    b.end = rest.end();
    b.full = full;
    return b;
  }

  private String renderEach(Block block, TemplateContext context) {
    // TODO: Implement when TemplateContext exposes safe iteration & scoping.
    // For now return empty string to avoid leaking block markers.
    return "";
  }

  private String renderIf(Block block, TemplateContext context) throws TemplateRenderException {
    // Evaluate truthiness: non-empty string => true
    String condValue = templateRenderer.evaluateSimpleExpression(block.argument.trim(), context);
    boolean truthy = condValue != null && !condValue.isEmpty();

    String chosen = truthy ? block.truePart : block.falsePart;

    // Render nested placeholders / nested control blocks
    return templateRenderer.renderFragment(chosen, context);
  }

  private String renderWith(Block block, TemplateContext context) {
    // TODO: Implement when TemplateContext exposes a way to push/pop a scoped object.
    // For now, render block with current context (no scope change) to be minimally useful.
    try {
      return templateRenderer.renderFragment(block.truePart, context);
    } catch (TemplateRenderException e) {
      return "";
    }
  }

  private enum Kind {
    IF,
    EACH,
    WITH
  }

  private static final class Block {
    String argument;

    int end; // end index (exclusive)

    String falsePart; // empty when no {{else}}

    String full;

    Kind kind;

    int start; // start index in source

    String truePart;
  }
}
