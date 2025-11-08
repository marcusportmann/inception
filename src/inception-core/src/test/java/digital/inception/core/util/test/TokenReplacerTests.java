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

package digital.inception.core.util.test;

import static org.junit.jupiter.api.Assertions.*;

import digital.inception.core.util.TokenReplacer;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The {@code TokenReplacerTests} class.
 *
 * @author Marcus Portmann
 */
public class TokenReplacerTests {

  @Test
  @DisplayName("Custom prefix/suffix are honored")
  void customDelimitersWork() {
    TokenReplacer r = TokenReplacer.builder().withPrefix("{{").withSuffix("}}").build();
    assertEquals("Hello Marcus", r.replace("Hello {{name}}", mapOf("name", "Marcus")));
  }

  @Test
  @DisplayName("Multi-char default separator splits on first unescaped occurrence")
  void defaultSeparatorMultiChar() {
    TokenReplacer r = TokenReplacer.builder().withDefaultSeparator("::").build();
    // Default is "def:ault" (extra ':' allowed in default)
    assertEquals("def:ault", r.replace("${x::def:ault}", Map.of()));
  }

  @Test
  @DisplayName("Disabling escape char means backslash is literal and token still resolves")
  void disableEscapeChar() {
    TokenReplacer r = TokenReplacer.builder().withEscapeChar(null).build();
    // Backslash is literal, token is recognized and replaced
    assertEquals("\\Alice", r.replace("\\${name}", Map.of("name", "Alice")));
  }

  @Test
  @DisplayName("Escapes inside token body: default separator can be literal in name")
  void escapeInsideTokenBodyDefaultSeparatorInName() {
    TokenReplacer r = TokenReplacer.builder().build(); // default separator ":"
    // Name part is "key:part" (':' escaped), then default "DEFAULT"
    String out = r.replace("${key\\:part:DEFAULT}", mapOf("key:part", "OK"));
    assertEquals("OK", out);
  }

  @Test
  @DisplayName("Escapes inside token body: suffix char '}' can appear in name")
  void escapeInsideTokenBodySuffixCharInName() {
    TokenReplacer r = TokenReplacer.builder().build();
    // Name is "key}part" because '}' is escaped in the token body
    String out = r.replace("${key\\}part}", mapOf("key}part", "ok"));
    assertEquals("ok", out);
  }

  @Test
  @DisplayName("Literal prefix is rendered when escaped in template")
  void escapeLiteralPrefix() {
    TokenReplacer r = TokenReplacer.builder().build(); // default escape '\'
    assertEquals(
        "Total cost ${amount}", r.replace("Total cost \\${amount}", Map.of("amount", "5")));
  }

  @Test
  @DisplayName("ignoreCase=true performs case-insensitive lookup")
  void ignoreCaseLookup() {
    TokenReplacer r = TokenReplacer.builder().ignoreCase(true).build();
    assertEquals("Chris", r.replace("${user}", mapOf("USER", "Chris")));
    assertEquals("Chris", r.replace("${UsEr}", mapOf("user", "Chris")));
  }

  @Test
  @DisplayName("Missing: LEAVE_AS_IS (default) preserves original token")
  void missingLeaveAsIs() {
    TokenReplacer r = TokenReplacer.builder().build();
    assertEquals("Hello ${name}", r.replace("Hello ${name}", Map.of()));
  }

  @Test
  @DisplayName("Missing: REPLACE_WITH_EMPTY removes the token")
  void missingReplaceWithEmpty() {
    TokenReplacer r =
        TokenReplacer.builder()
            .withMissingKeyStrategy(TokenReplacer.MissingKeyStrategy.REPLACE_WITH_EMPTY)
            .build();
    assertEquals("Hello !", r.replace("Hello ${name}!", Map.of()));
  }

  @Test
  @DisplayName("Missing: REPLACE_WITH_FALLBACK uses provided fallback")
  void missingReplaceWithFallback() {
    TokenReplacer r =
        TokenReplacer.builder()
            .withMissingKeyStrategy(TokenReplacer.MissingKeyStrategy.REPLACE_WITH_FALLBACK)
            .withMissingReplacement("<missing>")
            .build();
    assertEquals("Hello <missing>", r.replace("Hello ${name}", Map.of()));
  }

  @Test
  @DisplayName("Missing: REPLACE_WITH_FALLBACK uses empty string when fallback is null")
  void missingReplaceWithFallback_nullFallbackBecomesEmpty() {
    TokenReplacer r =
        TokenReplacer.builder()
            .withMissingKeyStrategy(TokenReplacer.MissingKeyStrategy.REPLACE_WITH_FALLBACK)
            .withMissingReplacement(null)
            .build();
    assertEquals("Hello ", r.replace("Hello ${name}", Map.of()));
  }

  @Test
  @DisplayName("Missing: THROW_EXCEPTION throws for missing token without default")
  void missingThrowException() {
    TokenReplacer r =
        TokenReplacer.builder()
            .withMissingKeyStrategy(TokenReplacer.MissingKeyStrategy.THROW_EXCEPTION)
            .build();
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> r.replace("Hello ${name}", Map.of()));
    assertTrue(ex.getMessage().contains("No value for token"));
  }

  @Test
  @DisplayName("Multiple tokens across a template")
  void multipleTokens() {
    TokenReplacer r = TokenReplacer.builder().build();
    String tpl = "A=${a}, B=${b:2}, C=\\${c}, D=${d}";
    Map<String, String> vals = mapOf("a", "1", "d", "4");
    // \${c} -> literal ${c}; others replaced (b uses default 2; d uses "4")
    assertEquals("A=1, B=2, C=${c}, D=4", r.replace(tpl, vals));
  }

  @Test
  @DisplayName("No recursion: replacements are single-pass")
  void recursionDisabledSinglePassOnly() {
    TokenReplacer r = TokenReplacer.builder().resolveRecursively(false).build();
    assertEquals("${b}", r.replace("${a}", mapOf("a", "${b}", "b", "B")));
  }

  @Test
  @DisplayName("Recursion resolves tokens introduced by values")
  void recursionEnabledEesolvesValues() {
    TokenReplacer r = TokenReplacer.builder().resolveRecursively(true).build();
    assertEquals("B", r.replace("${a}", mapOf("a", "${b}", "b", "B")));
  }

  @Test
  @DisplayName("Recursion resolves tokens introduced by inline defaults across passes")
  void recursionEnabledResolvesInlineDefault() {
    TokenReplacer r = TokenReplacer.builder().resolveRecursively(true).build();
    assertEquals("there", r.replace("${x:${y}}", mapOf("y", "there")));
  }

  @Test
  @DisplayName("Recursion with nested phrase")
  void recursionNestedPhrase() {
    TokenReplacer r = TokenReplacer.builder().resolveRecursively(true).withMaxDepth(10).build();
    assertEquals("Hi Sam!", r.replace("${greet}!", mapOf("greet", "Hi ${name}", "name", "Sam")));
  }

  @Test
  @DisplayName("replace: null template returns null; null values treated as empty map")
  void replaceNullHandling() {
    TokenReplacer r = TokenReplacer.defaultStyle();
    assertNull(r.replace(null, Map.of()));
    assertEquals("Hello world", r.replace("Hello ${x:world}", null));
  }

  @Test
  @DisplayName("replaceWithDefaults: empty map value is NOT treated as missing by default")
  void replaceWithDefaultsEmptyValueIsNotMissing() {
    String out = TokenReplacer.replaceWithDefaults("Port: ${port:8080}", Map.of("port", ""));
    assertEquals("Port: ", out);
  }

  @Test
  @DisplayName("replaceWithDefaults: inline default is used when key is absent")
  void replaceWithDefaultsUsesInlineDefault() {
    String out = TokenReplacer.replaceWithDefaults("Hi ${user:there}!", Map.of());
    assertEquals("Hi there!", out);
  }

  @Test
  @DisplayName("replaceWithDefaults: value present wins over inline default")
  void replaceWithDefaultsValuePresentWins() {
    String out = TokenReplacer.replaceWithDefaults("Hi ${user:there}!", Map.of("user", "Marcus"));
    assertEquals("Hi Marcus!", out);
  }

  @Test
  @DisplayName("treatBlankAsMissing=true triggers inline default")
  void treatBlankAsMissingAppliesDefault() {
    TokenReplacer r = TokenReplacer.builder().treatBlankAsMissing(true).build();
    assertEquals("X=1", r.replace("X=${x:1}", Map.of("x", "   ")));
  }

  @Test
  @DisplayName("Blank/empty not treated as missing by default")
  void treatBlankEmptyDefaultsDoNotTriggerDefault() {
    TokenReplacer r = TokenReplacer.builder().build();
    assertEquals("X=", r.replace("X=${x:1}", Map.of("x", "")));
    assertEquals("X=   ", r.replace("X=${x:1}", Map.of("x", "   ")));
  }

  @Test
  @DisplayName("treatEmptyAsMissing=true triggers inline default")
  void treatEmptyAsMissingAppliesDefault() {
    TokenReplacer r = TokenReplacer.builder().treatEmptyAsMissing(true).build();
    assertEquals("Port: 8080", r.replace("Port: ${port:8080}", Map.of("port", "")));
  }

  @Test
  @DisplayName("trimTokenParts=false preserves whitespace in name")
  void trimTokenPartsFalse() {
    TokenReplacer r = TokenReplacer.builder().trimTokenParts(false).build();
    // Name is " name " including spaces
    assertEquals("X", r.replace("${ name }", mapOf(" name ", "X")));
  }

  @Test
  @DisplayName("trimTokenParts=true (default) trims name and default")
  void trimTokenPartsTrue() {
    TokenReplacer r = TokenReplacer.builder().build(); // trim=true by default
    assertEquals("default", r.replace("${  name   :   default   }", Map.of()));
    assertEquals("X", r.replace("${   name   }", mapOf("name", "X")));
  }

  @Test
  @DisplayName("Unterminated token: remainder is preserved unchanged (lenient)")
  void unterminatedTokenPreserved() {
    TokenReplacer r = TokenReplacer.builder().build();
    assertEquals("Start ${oops", r.replace("Start ${oops", Map.of("oops", "X")));
  }

  private static Map<String, String> mapOf(Object... kv) {
    Map<String, String> m = new HashMap<>();
    for (int i = 0; i < kv.length; i += 2) {
      m.put((String) kv[i], (String) kv[i + 1]);
    }
    return m;
  }
}
