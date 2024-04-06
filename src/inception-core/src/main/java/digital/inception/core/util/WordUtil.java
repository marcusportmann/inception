/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.core.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/**
 * The <b>WordUtil</b> class is a utility class which provides methods for performing operations on
 * strings that contain words.
 *
 * <p>Derived from the org.apache.commons.text.WordUtil class in the commons-text library.
 */
public class WordUtil {

  /**
   * {@code WordUtil} instances should NOT be constructed in standard programming. Instead, the
   * class should be used as {@code WordUtil.wrap("foo bar", 20);}.
   */
  private WordUtil() {}

  /**
   * Capitalizes all the whitespace separated words in a String. Only the first character of each
   * word is changed. To convert the rest of each word to lowercase at the same time, use {@link
   * #capitalizeFully(String)}.
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input String
   * returns {@code null}. Capitalization uses the Unicode title case, normally equivalent to upper
   * case.
   *
   * <pre>
   * WordUtil.capitalize(null)        = null
   * WordUtil.capitalize("")          = ""
   * WordUtil.capitalize("i am FINE") = "I Am FINE"
   * </pre>
   *
   * @param str the String to capitalize, may be null
   * @return capitalized String, {@code null} if null String input
   * @see #uncapitalize(String)
   * @see #capitalizeFully(String)
   */
  public static String capitalize(final String str) {
    return capitalize(str, null);
  }

  /**
   * Capitalizes all the delimiter separated words in a String. Only the first character of each
   * word is changed. To convert the rest of each word to lowercase at the same time, use {@link
   * #capitalizeFully(String, char[])}.
   *
   * <p>The delimiters represent a set of characters understood to separate words. The first string
   * character and the first non-delimiter character after a delimiter will be capitalized.
   *
   * <p>A {@code null} input String returns {@code null}. Capitalization uses the Unicode title
   * case, normally equivalent to upper case.
   *
   * <pre>
   * WordUtil.capitalize(null, *)            = null
   * WordUtil.capitalize("", *)              = ""
   * WordUtil.capitalize(*, new char[0])     = *
   * WordUtil.capitalize("i am fine", null)  = "I Am Fine"
   * WordUtil.capitalize("i aM.fine", {'.'}) = "I aM.Fine"
   * WordUtil.capitalize("i am fine", new char[]{}) = "I am fine"
   * </pre>
   *
   * @param str the String to capitalize, may be null
   * @param delimiters set of characters to determine capitalization, null means whitespace
   * @return capitalized String, {@code null} if null String input
   * @see #uncapitalize(String)
   * @see #capitalizeFully(String)
   */
  public static String capitalize(final String str, final char... delimiters) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    final Predicate<Integer> isDelimiter = generateIsDelimiterFunction(delimiters);
    final int strLen = str.length();
    final int[] newCodePoints = new int[strLen];
    int outOffset = 0;

    boolean capitalizeNext = true;
    for (int index = 0; index < strLen; ) {
      final int codePoint = str.codePointAt(index);

      if (isDelimiter.test(codePoint)) {
        capitalizeNext = true;
        newCodePoints[outOffset++] = codePoint;
        index += Character.charCount(codePoint);
      } else if (capitalizeNext) {
        final int titleCaseCodePoint = Character.toTitleCase(codePoint);
        newCodePoints[outOffset++] = titleCaseCodePoint;
        index += Character.charCount(titleCaseCodePoint);
        capitalizeNext = false;
      } else {
        newCodePoints[outOffset++] = codePoint;
        index += Character.charCount(codePoint);
      }
    }
    return new String(newCodePoints, 0, outOffset);
  }

  /**
   * Converts all the whitespace separated words in a String into capitalized words, that is each
   * word is made up of a titlecase character and then a series of lowercase characters.
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input String
   * returns {@code null}. Capitalization uses the Unicode title case, normally equivalent to upper
   * case.
   *
   * <pre>
   * WordUtil.capitalizeFully(null)        = null
   * WordUtil.capitalizeFully("")          = ""
   * WordUtil.capitalizeFully("i am FINE") = "I Am Fine"
   * </pre>
   *
   * @param str the String to capitalize, may be null
   * @return capitalized String, {@code null} if null String input
   */
  public static String capitalizeFully(final String str) {
    return capitalizeFully(str, null);
  }

  /**
   * Converts all the delimiter separated words in a String into capitalized words, that is each
   * word is made up of a titlecase character and then a series of lowercase characters.
   *
   * <p>The delimiters represent a set of characters understood to separate words. The first string
   * character and the first non-delimiter character after a delimiter will be capitalized.
   *
   * <p>A {@code null} input String returns {@code null}. Capitalization uses the Unicode title
   * case, normally equivalent to upper case.
   *
   * <pre>
   * WordUtil.capitalizeFully(null, *)            = null
   * WordUtil.capitalizeFully("", *)              = ""
   * WordUtil.capitalizeFully(*, null)            = *
   * WordUtil.capitalizeFully(*, new char[0])     = *
   * WordUtil.capitalizeFully("i aM.fine", {'.'}) = "I am.Fine"
   * </pre>
   *
   * @param str the String to capitalize, may be null
   * @param delimiters set of characters to determine capitalization, null means whitespace
   * @return capitalized String, {@code null} if null String input
   */
  public static String capitalizeFully(String str, final char... delimiters) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    str = str.toLowerCase();
    return capitalize(str, delimiters);
  }

  /**
   * Checks if the String contains all words in the given array.
   *
   * <p>A {@code null} String will return {@code false}. A {@code null}, zero length search array or
   * if one element of array is null will return {@code false}.
   *
   * <pre>
   * WordUtil.containsAllWords(null, *)            = false
   * WordUtil.containsAllWords("", *)              = false
   * WordUtil.containsAllWords(*, null)            = false
   * WordUtil.containsAllWords(*, [])              = false
   * WordUtil.containsAllWords("abcd", "ab", "cd") = false
   * WordUtil.containsAllWords("abc def", "def", "abc") = true
   * </pre>
   *
   * @param word The CharSequence to check, may be null
   * @param words The array of String words to search for, may be null
   * @return {@code true} if all search words are found, {@code false} otherwise
   */
  public static boolean containsAllWords(final String word, final String... words) {

    if (StringUtils.isEmpty(word) || ((words == null) || (words.length == 0))) {
      return false;
    }
    for (final String w : words) {
      if (!StringUtils.hasText(w)) {
        return false;
      }
      final Pattern p = Pattern.compile(".*\\b" + Pattern.quote(w) + "\\b.*");
      if (!p.matcher(word).matches()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Extracts the initial characters from each word in the String.
   *
   * <p>All first characters after whitespace are returned as a new string. Their case is not
   * changed.
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input String
   * returns {@code null}.
   *
   * <pre>
   * WordUtil.initials(null)             = null
   * WordUtil.initials("")               = ""
   * WordUtil.initials("Ben John Lee")   = "BJL"
   * WordUtil.initials("Ben J.Lee")      = "BJ"
   * </pre>
   *
   * @param str the String to get initials from, may be null
   * @return String of initial letters, {@code null} if null String input
   * @see #initials(String,char[])
   */
  public static String initials(final String str) {
    return initials(str, null);
  }

  /**
   * Extracts the initial characters from each word in the String.
   *
   * <p>All first characters after the defined delimiters are returned as a new string. Their case
   * is not changed.
   *
   * <p>If the delimiters array is null, then Whitespace is used. Whitespace is defined by {@link
   * Character#isWhitespace(char)}. A {@code null} input String returns {@code null}. An empty
   * delimiter array returns an empty String.
   *
   * <pre>
   * WordUtil.initials(null, *)                = null
   * WordUtil.initials("", *)                  = ""
   * WordUtil.initials("Ben John Lee", null)   = "BJL"
   * WordUtil.initials("Ben J.Lee", null)      = "BJ"
   * WordUtil.initials("Ben J.Lee", [' ','.']) = "BJL"
   * WordUtil.initials(*, new char[0])         = ""
   * </pre>
   *
   * @param str the String to get initials from, may be null
   * @param delimiters set of characters to determine words, null means whitespace
   * @return String of initial characters, {@code null} if null String input
   * @see #initials(String)
   */
  public static String initials(final String str, final char... delimiters) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    if (delimiters != null && delimiters.length == 0) {
      return "";
    }
    final Predicate<Integer> isDelimiter = generateIsDelimiterFunction(delimiters);
    final int strLen = str.length();
    final int[] newCodePoints = new int[strLen / 2 + 1];
    int count = 0;
    boolean lastWasGap = true;
    for (int i = 0; i < strLen; ) {
      final int codePoint = str.codePointAt(i);

      if (isDelimiter.test(codePoint)) {
        lastWasGap = true;
      } else if (lastWasGap) {
        newCodePoints[count++] = codePoint;
        lastWasGap = false;
      }

      i += Character.charCount(codePoint);
    }
    return new String(newCodePoints, 0, count);
  }

  /**
   * Is the character a delimiter.
   *
   * @param ch the character to check
   * @param delimiters the delimiters
   * @return true if it is a delimiter
   * @deprecated as of 1.2 and will be removed in 2.0
   */
  @Deprecated
  public static boolean isDelimiter(final char ch, final char[] delimiters) {
    if (delimiters == null) {
      return Character.isWhitespace(ch);
    }
    for (final char delimiter : delimiters) {
      if (ch == delimiter) {
        return true;
      }
    }
    return false;
  }

  /**
   * Is the codePoint a delimiter.
   *
   * @param codePoint the codePint to check
   * @param delimiters the delimiters
   * @return true if it is a delimiter
   * @deprecated as of 1.2 and will be removed in 2.0
   */
  @Deprecated
  public static boolean isDelimiter(final int codePoint, final char[] delimiters) {
    if (delimiters == null) {
      return Character.isWhitespace(codePoint);
    }
    for (int index = 0; index < delimiters.length; index++) {
      final int delimiterCodePoint = Character.codePointAt(delimiters, index);
      if (delimiterCodePoint == codePoint) {
        return true;
      }
    }
    return false;
  }

  /**
   * Swaps the case of a String using a word based algorithm.
   *
   * <ul>
   *   <li>Upper case character converts to Lower case
   *   <li>Title case character converts to Lower case
   *   <li>Lower case character after Whitespace or at start converts to Title case
   *   <li>Other Lower case character converts to Upper case
   * </ul>
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input String
   * returns {@code null}.
   *
   * <pre>
   * StringUtils.swapCase(null)                 = null
   * StringUtils.swapCase("")                   = ""
   * StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
   * </pre>
   *
   * @param str the String to swap case, may be null
   * @return The changed String, {@code null} if null String input
   */
  public static String swapCase(final String str) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    final int strLen = str.length();
    final int[] newCodePoints = new int[strLen];
    int outOffset = 0;
    boolean whitespace = true;
    for (int index = 0; index < strLen; ) {
      final int oldCodepoint = str.codePointAt(index);
      final int newCodePoint;
      if (Character.isUpperCase(oldCodepoint) || Character.isTitleCase(oldCodepoint)) {
        newCodePoint = Character.toLowerCase(oldCodepoint);
        whitespace = false;
      } else if (Character.isLowerCase(oldCodepoint)) {
        if (whitespace) {
          newCodePoint = Character.toTitleCase(oldCodepoint);
          whitespace = false;
        } else {
          newCodePoint = Character.toUpperCase(oldCodepoint);
        }
      } else {
        whitespace = Character.isWhitespace(oldCodepoint);
        newCodePoint = oldCodepoint;
      }
      newCodePoints[outOffset++] = newCodePoint;
      index += Character.charCount(newCodePoint);
    }
    return new String(newCodePoints, 0, outOffset);
  }

  /**
   * Uncapitalizes all the whitespace separated words in a String. Only the first character of each
   * word is changed.
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input String
   * returns {@code null}.
   *
   * <pre>
   * WordUtil.uncapitalize(null)        = null
   * WordUtil.uncapitalize("")          = ""
   * WordUtil.uncapitalize("I Am FINE") = "i am fINE"
   * </pre>
   *
   * @param str the String to uncapitalize, may be null
   * @return uncapitalized String, {@code null} if null String input
   * @see #capitalize(String)
   */
  public static String uncapitalize(final String str) {
    return uncapitalize(str, null);
  }

  /**
   * Uncapitalizes all the whitespace separated words in a String. Only the first character of each
   * word is changed.
   *
   * <p>The delimiters represent a set of characters understood to separate words. The first string
   * character and the first non-delimiter character after a delimiter will be uncapitalized.
   *
   * <p>Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input String
   * returns {@code null}.
   *
   * <pre>
   * WordUtil.uncapitalize(null, *)            = null
   * WordUtil.uncapitalize("", *)              = ""
   * WordUtil.uncapitalize(*, null)            = *
   * WordUtil.uncapitalize(*, new char[0])     = *
   * WordUtil.uncapitalize("I AM.FINE", {'.'}) = "i AM.fINE"
   * WordUtil.uncapitalize("I am fine", new char[]{}) = "i am fine"
   * </pre>
   *
   * @param str the String to uncapitalize, may be null
   * @param delimiters set of characters to determine uncapitalization, null means whitespace
   * @return uncapitalized String, {@code null} if null String input
   * @see #capitalize(String)
   */
  public static String uncapitalize(final String str, final char... delimiters) {
    if (StringUtils.isEmpty(str)) {
      return str;
    }
    final Predicate<Integer> isDelimiter = generateIsDelimiterFunction(delimiters);
    final int strLen = str.length();
    final int[] newCodePoints = new int[strLen];
    int outOffset = 0;

    boolean uncapitalizeNext = true;
    for (int index = 0; index < strLen; ) {
      final int codePoint = str.codePointAt(index);

      if (isDelimiter.test(codePoint)) {
        uncapitalizeNext = true;
        newCodePoints[outOffset++] = codePoint;
        index += Character.charCount(codePoint);
      } else if (uncapitalizeNext) {
        final int titleCaseCodePoint = Character.toLowerCase(codePoint);
        newCodePoints[outOffset++] = titleCaseCodePoint;
        index += Character.charCount(titleCaseCodePoint);
        uncapitalizeNext = false;
      } else {
        newCodePoints[outOffset++] = codePoint;
        index += Character.charCount(codePoint);
      }
    }
    return new String(newCodePoints, 0, outOffset);
  }

  /**
   * Wraps a single line of text, identifying words by {@code ' '}.
   *
   * <p>New lines will be separated by the system property line separator. Very long words, such as
   * URLs will <i>not</i> be wrapped.
   *
   * <p>Leading spaces on a new line are stripped. Trailing spaces are not stripped.
   *
   * <table border="1">
   *  <caption>Examples</caption>
   *  <tr>
   *   <th>input</th>
   *   <th>wrapLength</th>
   *   <th>result</th>
   *  </tr>
   *  <tr>
   *   <td>null</td>
   *   <td>*</td>
   *   <td>null</td>
   *  </tr>
   *  <tr>
   *   <td>""</td>
   *   <td>*</td>
   *   <td>""</td>
   *  </tr>
   *  <tr>
   *   <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
   *   <td>20</td>
   *   <td>"Here is one line of\ntext that is going\nto be wrapped after\n20 columns."</td>
   *  </tr>
   *  <tr>
   *   <td>"Click here to jump to the commons website - https://commons.apache.org"</td>
   *   <td>20</td>
   *   <td>"Click here to jump\nto the commons\nwebsite -\nhttps://commons.apache.org"</td>
   *  </tr>
   *  <tr>
   *   <td>"Click here, https://commons.apache.org, to jump to the commons website"</td>
   *   <td>20</td>
   *   <td>"Click here,\nhttps://commons.apache.org,\nto jump to the\ncommons website"</td>
   *  </tr>
   * </table>
   *
   * (assuming that '\n' is the systems line separator)
   *
   * @param str the String to be word wrapped, may be null
   * @param wrapLength the column to wrap the words at, less than 1 is treated as 1
   * @return a line with newlines inserted, {@code null} if null input
   */
  public static String wrap(final String str, final int wrapLength) {
    return wrap(str, wrapLength, null, false);
  }

  /**
   * Wraps a single line of text, identifying words by {@code ' '}.
   *
   * <p>Leading spaces on a new line are stripped. Trailing spaces are not stripped.
   *
   * <table border="1">
   *  <caption>Examples</caption>
   *  <tr>
   *   <th>input</th>
   *   <th>wrapLength</th>
   *   <th>newLineString</th>
   *   <th>wrapLongWords</th>
   *   <th>result</th>
   *  </tr>
   *  <tr>
   *   <td>null</td>
   *   <td>*</td>
   *   <td>*</td>
   *   <td>true/false</td>
   *   <td>null</td>
   *  </tr>
   *  <tr>
   *   <td>""</td>
   *   <td>*</td>
   *   <td>*</td>
   *   <td>true/false</td>
   *   <td>""</td>
   *  </tr>
   *  <tr>
   *   <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
   *   <td>20</td>
   *   <td>"\n"</td>
   *   <td>true/false</td>
   *   <td>"Here is one line of\ntext that is going\nto be wrapped after\n20 columns."</td>
   *  </tr>
   *  <tr>
   *   <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
   *   <td>20</td>
   *   <td>"&lt;br /&gt;"</td>
   *   <td>true/false</td>
   *   <td>"Here is one line of&lt;br /&gt;text that is going&lt;
   *   br /&gt;to be wrapped after&lt;br /&gt;20 columns."</td>
   *  </tr>
   *  <tr>
   *   <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
   *   <td>20</td>
   *   <td>null</td>
   *   <td>true/false</td>
   *   <td>"Here is one line of" + systemNewLine + "text that is going"
   *   + systemNewLine + "to be wrapped after" + systemNewLine + "20 columns."</td>
   *  </tr>
   *  <tr>
   *   <td>"Click here to jump to the commons website - https://commons.apache.org"</td>
   *   <td>20</td>
   *   <td>"\n"</td>
   *   <td>false</td>
   *   <td>"Click here to jump\nto the commons\nwebsite -\nhttps://commons.apache.org"</td>
   *  </tr>
   *  <tr>
   *   <td>"Click here to jump to the commons website - https://commons.apache.org"</td>
   *   <td>20</td>
   *   <td>"\n"</td>
   *   <td>true</td>
   *   <td>"Click here to jump\nto the commons\nwebsite -\nhttps://commons.apach\ne.org"</td>
   *  </tr>
   * </table>
   *
   * @param str the String to be word wrapped, may be null
   * @param wrapLength the column to wrap the words at, less than 1 is treated as 1
   * @param newLineStr the string to insert for a new line, {@code null} uses the system property
   *     line separator
   * @param wrapLongWords true if long words (such as URLs) should be wrapped
   * @return a line with newlines inserted, {@code null} if null input
   */
  public static String wrap(
      final String str,
      final int wrapLength,
      final String newLineStr,
      final boolean wrapLongWords) {
    return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
  }

  /**
   * Wraps a single line of text, identifying words by {@code wrapOn}.
   *
   * <p>Leading spaces on a new line are stripped. Trailing spaces are not stripped.
   *
   * <table border="1">
   *  <caption>Examples</caption>
   *  <tr>
   *   <th>input</th>
   *   <th>wrapLength</th>
   *   <th>newLineString</th>
   *   <th>wrapLongWords</th>
   *   <th>wrapOn</th>
   *   <th>result</th>
   *  </tr>
   *  <tr>
   *   <td>null</td>
   *   <td>*</td>
   *   <td>*</td>
   *   <td>true/false</td>
   *   <td>*</td>
   *   <td>null</td>
   *  </tr>
   *  <tr>
   *   <td>""</td>
   *   <td>*</td>
   *   <td>*</td>
   *   <td>true/false</td>
   *   <td>*</td>
   *   <td>""</td>
   *  </tr>
   *  <tr>
   *   <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
   *   <td>20</td>
   *   <td>"\n"</td>
   *   <td>true/false</td>
   *   <td>" "</td>
   *   <td>"Here is one line of\ntext that is going\nto be wrapped after\n20 columns."</td>
   *  </tr>
   *  <tr>
   *   <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
   *   <td>20</td>
   *   <td>"&lt;br /&gt;"</td>
   *   <td>true/false</td>
   *   <td>" "</td>
   *   <td>"Here is one line of&lt;br /&gt;text that is going&lt;br /&gt;
   *   to be wrapped after&lt;br /&gt;20 columns."</td>
   *  </tr>
   *  <tr>
   *   <td>"Here is one line of text that is going to be wrapped after 20 columns."</td>
   *   <td>20</td>
   *   <td>null</td>
   *   <td>true/false</td>
   *   <td>" "</td>
   *   <td>"Here is one line of" + systemNewLine + "text that is going"
   *   + systemNewLine + "to be wrapped after" + systemNewLine + "20 columns."</td>
   *  </tr>
   *  <tr>
   *   <td>"Click here to jump to the commons website - https://commons.apache.org"</td>
   *   <td>20</td>
   *   <td>"\n"</td>
   *   <td>false</td>
   *   <td>" "</td>
   *   <td>"Click here to jump\nto the commons\nwebsite -\nhttps://commons.apache.org"</td>
   *  </tr>
   *  <tr>
   *   <td>"Click here to jump to the commons website - https://commons.apache.org"</td>
   *   <td>20</td>
   *   <td>"\n"</td>
   *   <td>true</td>
   *   <td>" "</td>
   *   <td>"Click here to jump\nto the commons\nwebsite -\nhttps://commons.apach\ne.org"</td>
   *  </tr>
   *  <tr>
   *   <td>"flammable/inflammable"</td>
   *   <td>20</td>
   *   <td>"\n"</td>
   *   <td>true</td>
   *   <td>"/"</td>
   *   <td>"flammable\ninflammable"</td>
   *  </tr>
   * </table>
   *
   * @param str the String to be word wrapped, may be null
   * @param wrapLength the column to wrap the words at, less than 1 is treated as 1
   * @param newLineStr the string to insert for a new line, {@code null} uses the system property
   *     line separator
   * @param wrapLongWords true if long words (such as URLs) should be wrapped
   * @param wrapOn regex expression to be used as a breakable characters, if blank string is
   *     provided a space character will be used
   * @return a line with newlines inserted, {@code null} if null input
   */
  public static String wrap(
      final String str,
      int wrapLength,
      String newLineStr,
      final boolean wrapLongWords,
      String wrapOn) {
    if (str == null) {
      return null;
    }
    if (newLineStr == null) {
      newLineStr = System.lineSeparator();
    }
    if (wrapLength < 1) {
      wrapLength = 1;
    }

    if (!StringUtils.hasText(wrapOn)) {
      wrapOn = " ";
    }
    final Pattern patternToWrapOn = Pattern.compile(wrapOn);
    final int inputLineLength = str.length();
    int offset = 0;
    final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
    int matcherSize = -1;

    while (offset < inputLineLength) {
      int spaceToWrapAt = -1;
      Matcher matcher =
          patternToWrapOn.matcher(
              str.substring(
                  offset,
                  Math.min(
                      (int) Math.min(Integer.MAX_VALUE, offset + wrapLength + 1L),
                      inputLineLength)));
      if (matcher.find()) {
        if (matcher.start() == 0) {
          matcherSize = matcher.end();
          if (matcherSize != 0) {
            offset += matcher.end();
            continue;
          }
          offset += 1;
        }
        spaceToWrapAt = matcher.start() + offset;
      }

      // only last line without leading spaces is left
      if (inputLineLength - offset <= wrapLength) {
        break;
      }

      while (matcher.find()) {
        spaceToWrapAt = matcher.start() + offset;
      }

      if (spaceToWrapAt >= offset) {
        // normal case
        wrappedLine.append(str, offset, spaceToWrapAt);
        wrappedLine.append(newLineStr);
        offset = spaceToWrapAt + 1;

      } else // really long word or URL
      if (wrapLongWords) {
        if (matcherSize == 0) {
          offset--;
        }
        // wrap really long word one line at a time
        wrappedLine.append(str, offset, wrapLength + offset);
        wrappedLine.append(newLineStr);
        offset += wrapLength;
        matcherSize = -1;
      } else {
        // do not wrap really long word, just extend beyond limit
        matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
        if (matcher.find()) {
          matcherSize = matcher.end() - matcher.start();
          spaceToWrapAt = matcher.start() + offset + wrapLength;
        }

        if (spaceToWrapAt >= 0) {
          if (matcherSize == 0 && offset != 0) {
            offset--;
          }
          wrappedLine.append(str, offset, spaceToWrapAt);
          wrappedLine.append(newLineStr);
          offset = spaceToWrapAt + 1;
        } else {
          if (matcherSize == 0 && offset != 0) {
            offset--;
          }
          wrappedLine.append(str, offset, str.length());
          offset = inputLineLength;
          matcherSize = -1;
        }
      }
    }

    if (matcherSize == 0 && offset < inputLineLength) {
      offset--;
    }

    // Whatever is left in line is short enough to just pass through
    wrappedLine.append(str, offset, str.length());

    return wrappedLine.toString();
  }

  /**
   * Given the array of delimiters supplied; returns a function determining whether a character code
   * point is a delimiter. The function provides O(1) lookup time. Whitespace is defined by {@link
   * Character#isWhitespace(char)} and is used as the defaultvalue if delimiters is null.
   *
   * @param delimiters set of characters to determine delimiters, null means whitespace
   * @return Predicate<Integer> taking a code point value as an argument and returning true if a
   *     delimiter.
   */
  private static Predicate<Integer> generateIsDelimiterFunction(final char[] delimiters) {
    final Predicate<Integer> isDelimiter;
    if (delimiters == null || delimiters.length == 0) {
      isDelimiter = delimiters == null ? Character::isWhitespace : c -> false;
    } else {
      Set<Integer> delimiterSet = new HashSet<>();
      for (int index = 0; index < delimiters.length; index++) {
        delimiterSet.add(Character.codePointAt(delimiters, index));
      }
      isDelimiter = delimiterSet::contains;
    }

    return isDelimiter;
  }
}
