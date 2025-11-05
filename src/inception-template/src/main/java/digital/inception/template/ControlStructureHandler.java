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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles and expands control structures (e.g., {{#if}}, {{#each}}) within templates.
 *
 * <p>Supported:
 *
 * <ul>
 *   <li>{{#if ...}}...{{/if}} with optional {{else}}
 *   <li>{{#each ...}}...{{/each}} with optional {{else}}
 *   <li>Nested control structures of any supported type
 * </ul>
 *
 * @param templateRenderer the {@link TemplateRenderer} to use for evaluating expressions
 * @author Marcus Portmann
 */
public record ControlStructureHandler(TemplateRenderer templateRenderer) {

  private static final Pattern ALIAS_SPEC =
      Pattern.compile("(?i)^(.*?)\\s+as\\s+(\\$?[A-Za-z_][A-Za-z0-9_]*)\\s*$");

  private static final String CLOSE_FMT = "\\{\\{\\/(%s)\\s*\\}\\}";

  private static final Pattern CLOSE_TAG = Pattern.compile("\\{\\{\\/(if|each)\\s*\\}\\}");

  private static final String DIR_EACH = "each";

  private static final String DIR_IF = "if";

  private static final Pattern ELSE_TAG = Pattern.compile("\\{\\{\\s*else\\s*\\}\\}");

  private static final String L_FIRST = "first";

  // Loop-local variable names
  private static final String L_INDEX = "index";

  private static final String L_LAST = "last";

  private static final String L_THIS = "this";

  private static final int MAX_EACH_ITERATIONS = 10_000;

  private static final Pattern OPEN_TAG = Pattern.compile("\\{\\{#(if|each)\\s+([^}]*)\\}\\}");

  private static final Pattern TAG_UNION =
      Pattern.compile(
          "(?:"
              + OPEN_TAG.pattern()
              + ")|(?:"
              + CLOSE_TAG.pattern()
              + ")|(?:"
              + ELSE_TAG.pattern()
              + ")",
          Pattern.DOTALL);

  /**
   * Expands and evaluates all supported control structures in the given template.
   *
   * <p>This method repeatedly scans the input for the next outermost control block, evaluates it in
   * the provided {@link TemplateContext}, replaces the block with its rendered output, and
   * continues until no more control structures remain. Nested blocks are supported: the chosen body
   * of each block is re-rendered via {@link TemplateRenderer#renderFragment(String,
   * TemplateContext)} so that nested placeholders and inner control structures are processed.
   *
   * <p><b>Supported directives</b>
   *
   * <ul>
   *   <li>{@code {{#if <expr>}}} <i>then-body</i> [{@code {{else}}} <i>else-body</i>] {@code
   *       {{/if}}}
   *   <li>{@code {{#each <expr> [as <alias>]}}} <i>item-body</i> [{@code {{else}}}
   *       <i>empty-body</i>] {@code {{/each}}}
   * </ul>
   *
   * <p><b>#each loop variables</b> (available as locals during each iteration):
   *
   * <ul>
   *   <li>{@code $index} — zero-based index
   *   <li>{@code $first} — {@code "true"} for the first item, else empty
   *   <li>{@code $last} — {@code "true"} for the last item, else empty
   *   <li>{@code $this} — current item’s string value
   *   <li>Optional alias via {@code as <alias>} (e.g., {@code {{$item}}})
   * </ul>
   *
   * <p><b>Error handling:</b> if a block fails to render, it is replaced with an empty string.
   * Unmatched or malformed block tags are left unexpanded.
   *
   * <p><b>Example</b>
   *
   * <pre>{@code
   * {{#if json:$.title}}
   *   <h1>{{ json:$.title }}</h1>
   * {{else}}
   *   <h1>Untitled</h1>
   * {{/if}}
   *
   * <ul>
   * {{#each json:$.items[*] as item}}
   *   <li>{{$index}}: {{$item}}</li>
   * {{else}}
   *   <li>No items</li>
   * {{/each}}
   * </ul>
   * }</pre>
   *
   * @param template the raw template text containing control-structure directives; must not be
   *     {@code null}
   * @param context the current rendering context providing the data source and variable scopes;
   *     must not be {@code null}
   * @return the template with all recognized control structures expanded and rendered
   * @throws NullPointerException if {@code template} or {@code context} is {@code null}
   */
  public String render(String template, TemplateContext context) {
    Objects.requireNonNull(template, "template");
    Objects.requireNonNull(context, "context");

    StringBuilder working = new StringBuilder(template);

    // Repeatedly find the next outermost block and replace it with its rendering.
    while (true) {
      Block block = findNextOutermostBlock(working);
      if (block == null) {
        break; // nothing left to expand
      }

      String replacement;
      try {
        replacement =
            switch (block.kind) {
              case IF -> renderIf(block, context);
              case EACH -> renderEach(block, context);
            };
      } catch (TemplateRenderException e) {
        // Fail-safe: if rendering fails, drop the block.
        replacement = "";
      }

      working.replace(block.start, block.end, replacement);
    }

    return working.toString();
  }

  /**
   * Expand all occurrences of {{ prefix:@... }} by replacing '@' with the concrete per-iteration
   * path. Uses a precompiled pattern for the given prefix to avoid per-iteration compilation.
   */
  private static String expandAnchorsForPrefix(
      String src, Pattern anchorPattern, String prefix, String itemPath) {
    // Quick bail-outs to skip matcher work in the common case
    if (src.indexOf('@') < 0) return src;
    if (src.indexOf('{') < 0) return src;

    Matcher m = anchorPattern.matcher(src);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      String suffix = m.group(1); // may be empty
      String replacement = "{{ " + prefix + ":" + itemPath + suffix + " }}";
      m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
    }
    m.appendTail(sb);
    return sb.toString();
  }

  /** Split plain text into list items. */
  private static List<Item> splitItems(String text) {
    List<Item> items = new ArrayList<>();
    if (text == null || text.isEmpty()) {
      return items;
    }

    String[] parts;
    if (text.indexOf('\n') >= 0) {
      parts = text.split("\\r?\\n");
    } else if (text.indexOf(',') >= 0) {
      parts = text.split("\\s*,\\s*");
    } else {
      parts = text.trim().split("\\s+");
    }

    for (String p : parts) {
      String v = p.trim();
      if (!v.isEmpty()) {
        items.add(Item.scalar(v));
      }
    }
    return items;
  }

  /**
   * Finds the next outermost control block ({{#if}} or {{#each}}) and returns its boundaries and
   * parts. This implementation uses a small directive stack, so {{else}} is only recognized for the
   * active block.
   */
  private Block findNextOutermostBlock(CharSequence src) {
    // Keep searching if we encounter a malformed open-without-close
    int searchFrom = 0;
    while (true) {
      Matcher open = OPEN_TAG.matcher(src);
      if (!open.find(searchFrom)) {
        return null; // none found
      }

      int openStart = open.start();
      int openEnd = open.end();
      String directive = open.group(1); // "if" | "each"
      String argument = open.group(2).trim();

      Kind kind =
          switch (directive) {
            case DIR_IF -> Kind.IF;
            case DIR_EACH -> Kind.EACH;
            default -> null;
          };
      if (kind == null) {
        searchFrom = openEnd;
        continue;
      }

      // Scan forward with a token union and a directive stack
      Deque<String> stack = new ArrayDeque<>();
      stack.push(directive);

      Matcher tok = TAG_UNION.matcher(src);
      tok.region(openEnd, src.length());

      int trueStart = openEnd;
      int elsePos = -1;
      int closePos = -1;
      int closeEnd = -1;

      while (tok.find()) {
        String token = tok.group(); // exact text of the matched token

        if (token.startsWith("{{#")) {
          // Opening tag: push its directive
          Matcher innerOpen = OPEN_TAG.matcher(token);
          if (innerOpen.matches()) {
            stack.push(innerOpen.group(1));
          }
          continue;
        }

        if (token.startsWith("{{/")) {
          // Closing tag: pop if it matches the stack top
          Matcher close = CLOSE_TAG.matcher(token);
          if (close.matches()) {
            String closing = close.group(1);
            if (!stack.isEmpty() && stack.peek().equals(closing)) {
              stack.pop();
              if (stack.isEmpty()) {
                closePos = tok.start();
                closeEnd = tok.end();
                break; // matched our outermost block
              }
            }
          }
          continue;
        }

        // Else tag belongs to current outer block only at depth == 1
        if (ELSE_TAG.matcher(token).matches() && stack.size() == 1) {
          elsePos = tok.start();
        }
      }

      if (closePos < 0) {
        // Malformed: no closing tag for this open; continue searching after this open
        searchFrom = openEnd;
        continue;
      }

      // Slice true/false parts
      String truePart, falsePart;
      if (elsePos >= 0) {
        // locate the end of that else token (we have tok stopped at close; need a local match)
        Matcher elseHere = ELSE_TAG.matcher(src);
        elseHere.region(elsePos, closePos);
        int elseEnd = elseHere.find() ? elseHere.end() : elsePos;
        truePart = src.subSequence(trueStart, elsePos).toString();
        falsePart = src.subSequence(elseEnd, closePos).toString();
      } else {
        truePart = src.subSequence(trueStart, closePos).toString();
        falsePart = "";
      }

      Block b = new Block();
      b.kind = kind;
      b.argument = argument;
      b.truePart = truePart;
      b.falsePart = falsePart;
      b.start = openStart;
      b.end = closeEnd;
      b.full = src.subSequence(openStart, closeEnd).toString();
      return b;
    }
  }

  /**
   * Implements:
   *
   * <pre>
   * {{#each <expr> [as <alias>]}}
   *   ... {{$index}} {{$first}} {{$last}} {{$this}} {{$alias}} ...
   *   ... and {{ json:@... }} / {{ xpath:@... }} relative selectors ...
   * {{else}} ... {{/each}}
   * </pre>
   */
  private String renderEach(Block block, TemplateContext context) throws TemplateRenderException {
    String rawArg = block.argument.trim();

    // Parse optional "as <alias>"
    String expr = rawArg;
    String alias = null;
    Matcher aliasMatcher = ALIAS_SPEC.matcher(rawArg);
    if (aliasMatcher.matches()) {
      expr = aliasMatcher.group(1).trim();
      alias = aliasMatcher.group(2);
      if (alias.startsWith("$")) alias = alias.substring(1);
    }

    // Determine evaluator prefix
    String prefix = null;
    int colon = expr.indexOf(':');
    if (colon > 0) {
      prefix = expr.substring(0, colon).trim().toLowerCase();
    }

    List<Item> items;
    if (DIR_IF.equals(prefix)) {
      // unreachable; just here to prevent accidental collision if "if:" ever existed
      items = List.of();
    } else if ("json".equals(prefix)) {
      String pathExpr = expr.substring(colon + 1).trim();
      items = resolveJsonItems(pathExpr, context);
    } else if ("xpath".equals(prefix)) {
      String pathExpr = expr.substring(colon + 1).trim();
      items = resolveXPathItems(pathExpr, context);
    } else if (colon > 0 && templateRenderer.hasEvaluatorPrefix(expr)) {
      String value = templateRenderer.evaluateSimpleExpression(expr, context);
      items = splitItems(value);
    } else {
      String value = templateRenderer.evaluateSimpleExpression(expr, context);
      items = splitItems(value);
    }

    if (items.isEmpty()) {
      return block.falsePart.isEmpty()
          ? ""
          : templateRenderer.renderFragment(block.falsePart, context);
    }

    // Precompile anchor pattern if this loop uses anchors (json/xpath only)
    Pattern anchorPattern = null;
    String anchorPrefix = items.get(0).anchorPrefix;
    if (anchorPrefix != null) {
      String regex = "\\{\\{\\s*" + Pattern.quote(anchorPrefix) + "\\s*:\\s*@([^}]*)\\}\\}";
      anchorPattern = Pattern.compile(regex, Pattern.DOTALL);
    }

    // Save previous locals
    Object prevIndex = context.getLocal(L_INDEX);
    Object prevFirst = context.getLocal(L_FIRST);
    Object prevLast = context.getLocal(L_LAST);
    Object prevThis = context.getLocal(L_THIS);
    Object prevAlias = (alias != null) ? context.getLocal(alias) : null;

    StringBuilder out = new StringBuilder(block.truePart.length() * items.size());

    for (int i = 0; i < items.size(); i++) {
      Item it = items.get(i);

      context.setLocal(L_INDEX, i);
      context.setLocal(L_FIRST, i == 0 ? "true" : "");
      context.setLocal(L_LAST, i == items.size() - 1 ? "true" : "");
      context.setLocal(L_THIS, it.value);
      if (alias != null) context.setLocal(alias, it.value);

      String iterationTemplate = block.truePart;
      if (anchorPattern != null && it.anchorPath != null) {
        iterationTemplate =
            expandAnchorsForPrefix(iterationTemplate, anchorPattern, anchorPrefix, it.anchorPath);
      }

      out.append(templateRenderer.renderFragment(iterationTemplate, context));
    }

    // Restore locals
    context.setLocal(L_INDEX, prevIndex);
    context.setLocal(L_FIRST, prevFirst);
    context.setLocal(L_LAST, prevLast);
    context.setLocal(L_THIS, prevThis);
    if (alias != null) context.setLocal(alias, prevAlias);

    return out.toString();
  }

  private String renderIf(Block block, TemplateContext context) throws TemplateRenderException {
    String condValue = templateRenderer.evaluateSimpleExpression(block.argument.trim(), context);
    boolean truthy = condValue != null && !condValue.isEmpty();
    String chosen = truthy ? block.truePart : block.falsePart;
    return templateRenderer.renderFragment(chosen, context);
  }

  /** Resolve JSON items by index: supports both "...[*]" and "..." (array) and single value. */
  private List<Item> resolveJsonItems(String pathExpr, TemplateContext context)
      throws TemplateRenderException {
    List<Item> items = new ArrayList<>();

    if (pathExpr.contains("[*]")) {
      for (int i = 0; i < MAX_EACH_ITERATIONS; i++) {
        String itemPath = pathExpr.replace("[*]", "[" + i + "]");
        String val = templateRenderer.evaluateSimpleExpression("json:" + itemPath, context);
        if (val == null || val.isEmpty()) {
          if (i == 0) {
            /* nothing at all */
          }
          break;
        }
        items.add(Item.json(itemPath, val));
      }
      return items;
    }

    // Try "array indexing" [0..] first; fall back to single value if index 0 is empty.
    boolean any = false;
    for (int i = 0; i < MAX_EACH_ITERATIONS; i++) {
      String itemPath = pathExpr + "[" + i + "]";
      String val = templateRenderer.evaluateSimpleExpression("json:" + itemPath, context);
      if (val == null || val.isEmpty()) {
        if (!any) {
          String single = templateRenderer.evaluateSimpleExpression("json:" + pathExpr, context);
          if (single != null && !single.isEmpty()) {
            items.add(Item.json(pathExpr, single));
          }
        }
        break;
      }
      any = true;
      items.add(Item.json(itemPath, val));
    }
    return items;
  }

  /** Resolve XPath items using 1-based positional predicates; fall back to single node/value. */
  private List<Item> resolveXPathItems(String pathExpr, TemplateContext context)
      throws TemplateRenderException {
    List<Item> items = new ArrayList<>();
    String base = "(" + pathExpr + ")";

    boolean any = false;
    for (int pos = 1; pos < MAX_EACH_ITERATIONS; pos++) {
      String itemPath = base + "[" + pos + "]";
      String val = templateRenderer.evaluateSimpleExpression("xpath:" + itemPath, context);
      if (val == null || val.isEmpty()) {
        if (!any) {
          String single = templateRenderer.evaluateSimpleExpression("xpath:" + pathExpr, context);
          if (single != null && !single.isEmpty()) {
            items.add(Item.xpath(pathExpr, single));
          }
        }
        break;
      }
      any = true;
      items.add(Item.xpath(itemPath, val));
    }
    return items;
  }

  // ---- Model ----------------------------------------------------------------

  private enum Kind {
    IF,
    EACH
  }

  private static final class Block {
    String argument;

    int end; // end index (exclusive)

    String falsePart; // content after  {{else}} (empty when no else)

    String full; // full text of the block including tags (for debugging)

    Kind kind;

    int start; // start index in source

    String truePart; // content before {{else}} (or entire body if no else)
  }

  /**
   * Represents one iteration item (value plus anchor info for @-expansion).
   *
   * @param anchorPath per-iteration path for '@' or null
   * @param anchorPrefix "json" | "xpath" | null
   */
  private record Item(String value, String anchorPrefix, String anchorPath) {

    static Item json(String itemPath, String value) {
      return new Item(value, "json", itemPath);
    }

    static Item scalar(String value) {
      return new Item(value, null, null);
    }

    static Item xpath(String itemPath, String value) {
      return new Item(value, "xpath", itemPath);
    }
  }
}
