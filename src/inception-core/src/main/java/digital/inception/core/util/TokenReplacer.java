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

package digital.inception.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code TokenReplacer} replaces tokens in a string with values from a map.
 *
 * <p>Features include:
 *
 * <ul>
 *   <li>Configurable token delimiters (default: {@code ${name}})
 *   <li>Optional escape character for literal prefixes (default: {@code '\\'})
 *   <li>Case-insensitive lookup (opt-in)
 *   <li>Missing-key strategies (leave, empty, fallback, throw)
 *   <li>Recursive resolution (opt-in, with max depth)
 *   <li>Default values inside tokens via {@code ${name:defaultValue}}:
 *       <ul>
 *         <li>Example: {@code ${dataSourceClassName:org.h2.jdbcx.JdbcDataSource}}
 *         <li>The substring after the first {@code ':'} is the default (additional {@code ':'}
 *             characters are allowed in the default).
 *         <li>Default text can itself contain tokens, which will be resolved on subsequent passes
 *             when recursive resolution is enabled.
 *       </ul>
 * </ul>
 *
 * @author Marcus Portmann
 */
public final class TokenReplacer {

  // Defaults support
  private final String defaultSeparator; // default ":"

  private final Character escapeChar; // e.g. '\\' or null for none

  private final boolean ignoreCase;

  private final int maxDepth;

  private final MissingKeyStrategy missingKeyStrategy;

  private final String missingReplacement;

  private final String prefix;

  private final boolean resolveRecursively;

  private final String suffix;

  private final boolean treatBlankAsMissing; // if true, blank (trim().isEmpty()) counts as missing

  private final boolean treatEmptyAsMissing; // if true, "" counts as missing

  private final boolean trimTokenParts;

  private TokenReplacer(Builder b) {
    this.prefix = Objects.requireNonNull(b.prefix, "prefix");
    this.suffix = Objects.requireNonNull(b.suffix, "suffix");
    if (prefix.isEmpty() || suffix.isEmpty()) {
      throw new IllegalArgumentException("prefix/suffix must not be empty");
    }
    this.escapeChar = b.escapeChar;
    this.ignoreCase = b.ignoreCase;
    this.trimTokenParts = b.trimTokenParts;
    this.resolveRecursively = b.resolveRecursively;
    this.maxDepth = b.maxDepth;
    this.missingKeyStrategy = Objects.requireNonNull(b.missingKeyStrategy, "missingKeyStrategy");
    this.missingReplacement = b.missingReplacement;
    this.defaultSeparator = Objects.requireNonNull(b.defaultSeparator, "defaultSeparator");
    if (defaultSeparator.isEmpty()) {
      throw new IllegalArgumentException("defaultSeparator must not be empty");
    }
    this.treatEmptyAsMissing = b.treatEmptyAsMissing;
    this.treatBlankAsMissing = b.treatBlankAsMissing;
  }

  /**
   * Creates a new {@link Builder} for configuring a {@code TokenReplacer}.
   *
   * <p>The returned builder is initialized with sensible defaults:
   *
   * <ul>
   *   <li>Token delimiters: {@code ${name}} (prefix {@code "${"}, suffix {@code "}"})
   *   <li>Escape character: {@code '\\'}
   *   <li>Missing-key strategy: {@link MissingKeyStrategy#LEAVE_AS_IS}
   *   <li>Default-value separator: {@code ":"} (e.g., {@code ${name:default}})
   * </ul>
   *
   * <p>Use the builder's fluent methods to customize delimiters, escaping, case handling, recursion
   * behavior, and missing-key handling, then call {@link Builder#build() build()} to create an
   * immutable {@code TokenReplacer}.
   *
   * @return a new {@link Builder} preconfigured with the default {@code TokenReplacer} options
   * @see #defaultStyle()
   * @see Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Convenient default: ${name} tokens, backslash escape, leave-missing-as-is, default separator
   * ":" enabled.
   *
   * @return the {@code TokenReplacer} configured using the defaults
   */
  public static TokenReplacer defaultStyle() {
    return builder().build();
  }

  /**
   * Convenience method that replaces tokens in {@code template} using {@code values}, with support
   * for inline default values.
   *
   * <p>Tokens use the form {@code ${name}} and may specify a default after a colon: {@code
   * ${name:defaultValue}}. If {@code name} is not present in {@code values}, the text after the
   * <em>first</em> {@code ':'} is used as the replacement (additional colons are allowed inside the
   * default). By default, empty or blank values in the map are treated as present and will
   * <em>not</em> trigger the default; to change this behavior, use {@link #builder()} with {@link
   * Builder#treatEmptyAsMissing(boolean)} or {@link Builder#treatBlankAsMissing(boolean)}.
   *
   * <p>This method uses the standard configuration:
   *
   * <ul>
   *   <li>Delimiters: {@code ${...}}
   *   <li>Escape the prefix literally with a backslash: {@code \${name}}
   *   <li>Case-sensitive key lookup
   *   <li>No recursive resolution
   *   <li>Missing keys without a default are left as-is
   *   <li>Default-value separator: {@code ":"} (e.g., {@code ${name:default}})
   * </ul>
   *
   * <p>Examples:
   *
   * <pre>{@code
   * TokenReplacer.replaceWithDefaults("Hi ${user:there}!", Map.of());            // "Hi there!"
   * TokenReplacer.replaceWithDefaults("Port: ${port:8080}", Map.of("port",""));  // "Port: "
   * TokenReplacer.replaceWithDefaults("X=${x:1}", Map.of("x","0"));              // "X=0"
   * }</pre>
   *
   * @param template the input text; may be {@code null} (returns {@code null})
   * @param values map of token names to replacement values; may be {@code null} (treated as an
   *     empty map)
   * @return the processed text with tokens (and inline defaults) applied
   * @see #builder()
   * @see #defaultStyle()
   */
  public static String replaceWithDefaults(String template, Map<String, String> values) {
    return TokenReplacer.defaultStyle().replace(template, values);
  }

  /**
   * Replace tokens in {@code template} using {@code values}.
   *
   * @param template the input text (null returns null)
   * @param values token name -> replacement string
   * @return text with tokens replaced
   */
  public String replace(String template, Map<String, String> values) {
    if (template == null) return null;
    Map<String, String> lookup = (values == null) ? Collections.emptyMap() : values;
    if (ignoreCase) {
      Map<String, String> m = new HashMap<>(lookup.size());
      for (Map.Entry<String, String> e : lookup.entrySet()) {
        String k = e.getKey();
        if (k != null) {
          m.put(k.toLowerCase(Locale.ROOT), e.getValue());
        }
      }
      lookup = m;
    }

    if (!resolveRecursively) {
      return replacePass(template, lookup);
    }

    String current = template;
    for (int i = 0; i < maxDepth; i++) {
      String next = replacePass(current, lookup);
      if (next.equals(current)) break; // no more changes
      current = next;
    }
    return current;
  }

  private String[] parseInner(String raw) {
    StringBuilder name = new StringBuilder(raw.length());
    StringBuilder def = null;
    final String sep = defaultSeparator;
    final int sepLen = sep.length();
    int i = 0;
    while (i < raw.length()) {
      char c = raw.charAt(i);
      if (escapeChar != null && c == escapeChar && i + 1 < raw.length()) {
        // Skip escape and copy the next char literally
        if (def == null) name.append(raw.charAt(i + 1));
        else def.append(raw.charAt(i + 1));
        i += 2;
        continue;
      }
      // Check for unescaped separator (multi-char supported)
      if (def == null
          && sepLen > 0
          && i + sepLen <= raw.length()
          && raw.regionMatches(false, i, sep, 0, sepLen)) {
        def = new StringBuilder(raw.length() - i);
        i += sepLen;
        continue;
      }
      // Otherwise, copy char
      if (def == null) name.append(c);
      else def.append(c);
      i++;
    }
    String namePart = trimTokenParts ? name.toString().trim() : name.toString();
    String defaultPart =
        (def == null) ? null : (trimTokenParts ? def.toString().trim() : def.toString());
    return new String[] {namePart, defaultPart};
  }

  /**
   * Performs a single, non-recursive replacement pass over the given {@code template}.
   *
   * <p>This method scans for the configured token {@link #prefix} and {@link #suffix}, replaces each
   * token once using the provided {@code lookup}, and returns the resulting text. It does <em>not</em>
   * iterate to resolve tokens introduced by replacements; multi-pass/recursive behavior is handled
   * by the caller (see {@link #replace(String, Map)}).
   *
   * <h3>Token syntax</h3>
   * <ul>
   *   <li>Basic form: {@code <prefix>name<suffix>} (e.g., {@code ${user}})</li>
   *   <li>Inline default: {@code <prefix>name<defaultSeparator>default<suffix>}
   *       (e.g., {@code ${port:8080}}). The {@link #defaultSeparator} may be multi-character.</li>
   * </ul>
   *
   * <h3>Escaping</h3>
   * <ul>
   *   <li>Literal prefix in the template: if {@link #escapeChar} is non-null, placing it immediately
   *       before the prefix renders the prefix literally (e.g., {@code \${} → ${}).</li>
   *   <li>Inside the token body (between prefix and suffix), splitting and unescaping are delegated
   *       to {@code parseInner(String)}. That parser:
   *       <ul>
   *         <li>Splits {@code name} and {@code default} on the first <em>unescaped</em>
   *             {@link #defaultSeparator}.</li>
   *         <li>Treats {@link #escapeChar} (when non-null) as an escape for any following character,
   *             allowing literal default separators and suffix characters within the token body.</li>
   *         <li>Optionally trims {@code name} and {@code default} when {@link #trimTokenParts} is true.</li>
   *       </ul>
   *   </li>
   *   <li>The closing suffix in the template is found as the first occurrence of {@link #suffix}
   *       that is <em>not</em> immediately preceded by {@link #escapeChar} (when non-null).</li>
   * </ul>
   *
   * <h3>Lookup &amp; missing values</h3>
   * <ul>
   *   <li>Keys are normalized according to {@link #ignoreCase} (ROOT locale).</li>
   *   <li>A value is considered missing if it is {@code null}, or if
   *       {@link #treatEmptyAsMissing} and/or {@link #treatBlankAsMissing} deem it missing.</li>
   *   <li>If missing and an inline default is present, the default text is emitted as parsed.</li>
   *   <li>If missing and no inline default is present, behavior follows {@link #missingKeyStrategy}:
   *     <ul>
   *       <li>{@code LEAVE_AS_IS}: the original token text (including its inner contents) is
   *           preserved in the output.</li>
   *       <li>{@code REPLACE_WITH_EMPTY}: nothing is appended.</li>
   *       <li>{@code REPLACE_WITH_FALLBACK}: {@link #missingReplacement} (or {@code ""} if {@code null}) is appended.</li>
   *       <li>{@code THROW_EXCEPTION}: an {@link IllegalArgumentException} is thrown.</li>
   *     </ul>
   *   </li>
   * </ul>
   *
   * <h3>Unterminated tokens</h3>
   * If no (unescaped) closing suffix is found, the method appends the remainder of the template
   * starting at the token prefix unchanged and terminates the pass (lenient behavior).
   *
   * <h3>Complexity &amp; thread-safety</h3>
   * Runs in time linear to the template length. The enclosing class is immutable; this method
   * relies only on {@code final} configuration fields and is thread-safe.
   * <p>
   * @param template non-null input text to process
   * @param lookup non-null map used for token resolution (values may be {@code null})
   * @return the template with one replacement pass applied
   * @throws IllegalArgumentException if {@link #missingKeyStrategy} is {@code THROW_EXCEPTION} and a
   *         missing token without an inline default is encountered
   * @see #replace(String, Map)
   * @see #parseInner(String)
   */
  private String replacePass(String template, Map<String, String> lookup) {
    final int pLen = prefix.length();
    final int sLen = suffix.length();

    StringBuilder out = new StringBuilder(template.length() + 32);
    int i = 0;

    while (i < template.length()) {
      int start = template.indexOf(prefix, i);
      if (start < 0) {
        out.append(template, i, template.length());
        break;
      }

      // Handle escaped prefix (e.g., "\${")
      if (escapeChar != null && start > 0 && template.charAt(start - 1) == escapeChar) {
        out.append(template, i, start - 1).append(prefix);
        i = start + pLen;
        continue;
      }

      // Append text up to the token
      out.append(template, i, start);
      final int tokenStart = start + pLen;

      // Find the first *unescaped* closing suffix
      int searchFrom = tokenStart;
      int end = -1;
      while (true) {
        int pos = template.indexOf(suffix, searchFrom);
        if (pos < 0) break;

        // If the suffix is immediately preceded by the escape char, treat it as literal and
        // continue
        if (escapeChar != null && pos > tokenStart && template.charAt(pos - 1) == escapeChar) {
          // Skip past the first character of the (escaped) suffix to avoid infinite loops
          searchFrom = pos + 1;
          continue;
        }

        end = pos;
        break;
      }

      if (end < 0) {
        // No closing suffix; leave the rest as-is
        out.append(template, start, template.length());
        break;
      }

      // Raw inner token text (may contain escapes); let parseInner handle splitting & unescaping
      String rawInner = template.substring(tokenStart, end);
      String[] parts = parseInner(rawInner); // returns [namePart, defaultPartOrNull]
      String namePart = parts[0];
      String defaultPart = parts[1];

      String key = ignoreCase ? namePart.toLowerCase(Locale.ROOT) : namePart;
      String value = lookup.get(key);

      boolean isMissing =
          value == null
              || treatEmptyAsMissing && value.isEmpty()
              || treatBlankAsMissing && value.trim().isEmpty();

      if (isMissing) {
        if (defaultPart != null) {
          // Use the default as parsed; if recursion is enabled, later passes may resolve tokens
          // within it
          out.append(defaultPart);
        } else {
          switch (missingKeyStrategy) {
            case LEAVE_AS_IS:
              // Preserve original token text exactly as it appeared
              out.append(prefix).append(rawInner).append(suffix);
              break;
            case REPLACE_WITH_EMPTY:
              // append nothing
              break;
            case REPLACE_WITH_FALLBACK:
              out.append(missingReplacement == null ? "" : missingReplacement);
              break;
            case THROW_EXCEPTION:
              throw new IllegalArgumentException("No value for token: " + namePart);
          }
        }
      } else {
        out.append(value);
      }

      // Move past the closing suffix
      i = end + sLen;
    }

    return out.toString();
  }

  /**
   * Defines how {@link TokenReplacer} handles tokens whose values are absent.
   *
   * <p>A token is considered <em>missing</em> when the lookup map does not contain the key, or when
   * its value is treated as missing via {@link TokenReplacer.Builder#treatEmptyAsMissing(boolean)}
   * / {@link TokenReplacer.Builder#treatBlankAsMissing(boolean)}. If a token specifies an inline
   * default (e.g., {@code ${name:default}}), that default is used and the {@code
   * MissingKeyStrategy} is not consulted.
   *
   * <p>The chosen strategy is applied during replacement; when recursive resolution is enabled, the
   * result of one pass may be further processed in subsequent passes.
   *
   * <p>Example (no inline default present):
   *
   * <pre>{@code
   * // With LEAVE_AS_IS:
   * // "Hello ${user}"  -> "Hello ${user}"
   * }</pre>
   *
   * @see TokenReplacer.Builder#withMissingKeyStrategy(MissingKeyStrategy)
   * @see TokenReplacer.Builder#withMissingReplacement(String)
   */
  public enum MissingKeyStrategy {
    /**
     * Leave the original token text intact in the output (e.g., {@code ${missing}}). Useful for
     * multi-pass rendering or diagnostics.
     */
    LEAVE_AS_IS,

    /**
     * Replace the missing token with an empty string. Example: {@code "Hello ${user}!"} → {@code
     * "Hello !"}.
     */
    REPLACE_WITH_EMPTY,

    /**
     * Replace the missing token with a configured fallback string as set by {@link
     * TokenReplacer.Builder#withMissingReplacement(String)}. If no fallback is configured, this
     * behaves like replacing with an empty string.
     */
    REPLACE_WITH_FALLBACK,

    /**
     * Throw an {@link IllegalArgumentException} upon encountering the first missing token. Use this
     * to enforce that all tokens must be provided or specify inline defaults.
     */
    THROW_EXCEPTION
  }

  /**
   * Fluent builder for creating immutable {@link TokenReplacer} instances.
   *
   * <p><strong>Defaults</strong> (unless overridden):
   *
   * <ul>
   *   <li>Delimiters: {@code ${...}} (prefix {@code "${"}, suffix {@code "}"})
   *   <li>Escape character: {@code '\\'} (use {@code null} to disable)
   *   <li>Case-sensitive key lookup
   *   <li>Whitespace trimming inside token parts enabled (e.g., {@code ${ name : default }})
   *   <li>No recursive resolution; max depth {@code 10} when enabled
   *   <li>Missing-key strategy: {@link TokenReplacer.MissingKeyStrategy#LEAVE_AS_IS}
   *   <li>Default-value separator: {@code ":"} (e.g., {@code ${name:default}})
   *   <li>{@code treatEmptyAsMissing=false}, {@code treatBlankAsMissing=false}
   * </ul>
   *
   * <p><strong>Configuration options</strong>:
   *
   * <ul>
   *   <li>{@link #withPrefix(String)} / {@link #withSuffix(String)} — token delimiters
   *   <li>{@link #withEscapeChar(Character)} — escape the prefix literally (or disable)
   *   <li>{@link #ignoreCase(boolean)} — case-insensitive map key lookup
   *   <li>{@link #trimTokenParts(boolean)} — trim whitespace around token name/default
   *   <li>{@link #resolveRecursively(boolean)} / {@link #withMaxDepth(int)} — resolve nested tokens
   *   <li>{@link #withMissingKeyStrategy(TokenReplacer.MissingKeyStrategy)} / {@link
   *       #withMissingReplacement(String)} — behavior for absent keys
   *   <li>{@link #withDefaultSeparator(String)} — separator between name and inline default
   *   <li>{@link #treatEmptyAsMissing(boolean)} / {@link #treatBlankAsMissing(boolean)} — control
   *       whether empty/blank values trigger the inline default
   * </ul>
   *
   * <p><strong>Usage</strong>:
   *
   * <pre>{@code
   * TokenReplacer replacer = TokenReplacer.builder()
   *     .withPrefix("${").withSuffix("}")
   *     .withEscapeChar('\\')
   *     .ignoreCase(true)
   *     .trimTokenParts(true)
   *     .resolveRecursively(true)
   *     .withMaxDepth(10)
   *     .withDefaultSeparator(":")
   *     .withMissingKeyStrategy(TokenReplacer.MissingKeyStrategy.REPLACE_WITH_FALLBACK)
   *     .withMissingReplacement("<missing>")
   *     .treatEmptyAsMissing(true)
   *     .build();
   * }</pre>
   *
   * <p><strong>Validation and thread-safety</strong>: {@link #withMaxDepth(int)} requires {@code
   * maxDepth >= 1}. Construction of {@link TokenReplacer} will reject empty prefix, suffix, or
   * default separator. This builder is not thread-safe; the built {@code TokenReplacer} is
   * immutable and thread-safe.
   *
   * @see TokenReplacer
   */
  public static final class Builder {
    // Defaults
    private String defaultSeparator = ":"; // ${name:default}

    private Character escapeChar = '\\';

    private boolean ignoreCase = false;

    private int maxDepth = 10;

    private MissingKeyStrategy missingKeyStrategy = MissingKeyStrategy.LEAVE_AS_IS;

    private String missingReplacement = "";

    private String prefix = "${";

    private boolean resolveRecursively = false;

    private String suffix = "}";

    private boolean treatBlankAsMissing = false;

    private boolean treatEmptyAsMissing = false;

    private boolean trimTokenParts = true;

    /** Constructs a new {@code Builder}. */
    public Builder() {}

    /**
     * Build the {@code TokenReplacer}.
     *
     * @return the {@code TokenReplacer}
     */
    public TokenReplacer build() {
      return new TokenReplacer(this);
    }

    /**
     * Case-insensitive token lookup (keys lower-cased with {@link java.util.Locale#ROOT}).
     *
     * @param ignoreCase whether to perform case-insensitive key lookup
     * @return this builder for chaining
     */
    public Builder ignoreCase(boolean ignoreCase) {
      this.ignoreCase = ignoreCase;
      return this;
    }

    /**
     * Resolve tokens recursively if replacement values (or defaults) contain tokens.
     *
     * @param resolveRecursively {@code true} to enable recursive resolution
     * @return this builder for chaining
     */
    public Builder resolveRecursively(boolean resolveRecursively) {
      this.resolveRecursively = resolveRecursively;
      return this;
    }

    /**
     * Treat blank (whitespace-only) values as missing. Default is {@code false}.
     *
     * @param value {@code true} to treat blank values as missing
     * @return this builder for chaining
     */
    public Builder treatBlankAsMissing(boolean value) {
      this.treatBlankAsMissing = value;
      return this;
    }

    /**
     * Treat empty string values as missing (so inline defaults apply). Default is {@code false}.
     *
     * @param value {@code true} to treat empty strings as missing
     * @return this builder for chaining
     */
    public Builder treatEmptyAsMissing(boolean value) {
      this.treatEmptyAsMissing = value;
      return this;
    }

    /**
     * Trim whitespace inside token parts, e.g., {@code ${ name : default }}. Default is {@code
     * true}.
     *
     * @param trimTokenParts {@code true} to trim token name/default parts
     * @return this builder for chaining
     */
    public Builder trimTokenParts(boolean trimTokenParts) {
      this.trimTokenParts = trimTokenParts;
      return this;
    }

    /**
     * Change the inline default-value separator (default {@code ":"}). May be multi-character.
     *
     * @param separator the separator string; must not be empty
     * @return this builder for chaining
     */
    public Builder withDefaultSeparator(String separator) {
      this.defaultSeparator = separator;
      return this;
    }

    /**
     * Set the escape character used in templates and inside tokens; pass {@code null} to disable.
     * Default is {@code '\\'}.
     *
     * @param escapeChar the escape character, or {@code null} to disable escaping
     * @return this builder for chaining
     */
    public Builder withEscapeChar(Character escapeChar) {
      this.escapeChar = escapeChar;
      return this;
    }

    /**
     * Maximum recursion depth (only used if {@code resolveRecursively == true}).
     *
     * @param maxDepth the maximum number of passes; must be {@code >= 1}
     * @return this builder for chaining
     * @throws IllegalArgumentException if {@code maxDepth < 1}
     */
    public Builder withMaxDepth(int maxDepth) {
      if (maxDepth < 1) throw new IllegalArgumentException("maxDepth must be >= 1");
      this.maxDepth = maxDepth;
      return this;
    }

    /**
     * Strategy to apply when a token is missing and no inline default is provided.
     *
     * @param strategy the missing-key strategy
     * @return this builder for chaining
     */
    public Builder withMissingKeyStrategy(MissingKeyStrategy strategy) {
      this.missingKeyStrategy = strategy;
      return this;
    }

    /**
     * Fallback text used when the strategy is {@link
     * TokenReplacer.MissingKeyStrategy#REPLACE_WITH_FALLBACK}.
     *
     * @param missingReplacement the fallback text; may be {@code null} to treat as empty
     * @return this builder for chaining
     */
    public Builder withMissingReplacement(String missingReplacement) {
      this.missingReplacement = missingReplacement;
      return this;
    }

    /**
     * Set the token prefix delimiter.
     *
     * @param prefix the prefix; must not be empty
     * @return this builder for chaining
     */
    public Builder withPrefix(String prefix) {
      this.prefix = prefix;
      return this;
    }

    /**
     * Set the token suffix delimiter.
     *
     * @param suffix the suffix; must not be empty
     * @return this builder for chaining
     */
    public Builder withSuffix(String suffix) {
      this.suffix = suffix;
      return this;
    }
  }
}
