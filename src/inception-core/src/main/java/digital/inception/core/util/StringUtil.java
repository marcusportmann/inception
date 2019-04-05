/*
 * Copyright 2019 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import java.text.SimpleDateFormat;

import java.util.*;

/**
 * The <code>StringUtil</code> class is a utility class which provides methods for manipulating
 * strings and arrays of strings.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public final class StringUtil
{
  /**
   * <code>AE_TAB</code>
   */
  private static final int AE_TAB[] =
  {
    0x000, 0x001, 0x002, 0x003, 0x037, 0x02D, 0x02E, 0x02F, 0x016, 0x005, 0x025, 0x00B, 0x00C,
    0x00D, 0x00E, 0x00F, 0x010, 0x011, 0x012, 0x013, 0x03C, 0x03D, 0x032, 0x026, 0x018, 0x019,
    0x03F, 0x027, 0x01C, 0x01D, 0x01E, 0x01F, 0x040, 0x05A, 0x07F, 0x07B, 0x05B, 0x06C, 0x050,
    0x07D, 0x04D, 0x05D, 0x05C, 0x04E, 0x06B, 0x060, 0x04B, 0x061, 0x0F0, 0x0F1, 0x0F2, 0x0F3,
    0x0F4, 0x0F5, 0x0F6, 0x0F7, 0x0F8, 0x0F9, 0x07A, 0x05E, 0x04C, 0x07E, 0x06E, 0x06F, 0x07C,
    0x0C1, 0x0C2, 0x0C3, 0x0C4, 0x0C5, 0x0C6, 0x0C7, 0x0C8, 0x0C9, 0x0D1, 0x0D2, 0x0D3, 0x0D4,
    0x0D5, 0x0D6, 0x0D7, 0x0D8, 0x0D9, 0x0E2, 0x0E3, 0x0E4, 0x0E5, 0x0E6, 0x0E7, 0x0E8, 0x0E9,
    0x0AD, 0x0E0, 0x0BD, 0x05F, 0x06D, 0x079, 0x081, 0x082, 0x083, 0x084, 0x085, 0x086, 0x087,
    0x088, 0x089, 0x091, 0x092, 0x093, 0x094, 0x095, 0x096, 0x097, 0x098, 0x099, 0x0A2, 0x0A3,
    0x0A4, 0x0A5, 0x0A6, 0x0A7, 0x0A8, 0x0A9, 0x0C0, 0x04F, 0x0D0, 0x0A1, 0x007, 0x020, 0x021,
    0x022, 0x023, 0x024, 0x015, 0x006, 0x017, 0x028, 0x029, 0x02A, 0x02B, 0x02C, 0x009, 0x00A,
    0x01B, 0x030, 0x031, 0x01A, 0x033, 0x034, 0x035, 0x036, 0x008, 0x038, 0x039, 0x03A, 0x03B,
    0x004, 0x014, 0x03E, 0x0FF, 0x041, 0x0AA, 0x04A, 0x0B1, 0x09F, 0x0B2, 0x06A, 0x0B5, 0x0BB,
    0x0B4, 0x09A, 0x08A, 0x0B0, 0x0CA, 0x0AF, 0x0BC, 0x090, 0x08F, 0x0EA, 0x0FA, 0x0BE, 0x0A0,
    0x0B6, 0x0B3, 0x09D, 0x0DA, 0x09B, 0x08B, 0x0B7, 0x0B8, 0x0B9, 0x0AB, 0x064, 0x065, 0x062,
    0x066, 0x063, 0x067, 0x09E, 0x068, 0x074, 0x071, 0x072, 0x073, 0x078, 0x075, 0x076, 0x077,
    0x0AC, 0x069, 0x0ED, 0x0EE, 0x0EB, 0x0EF, 0x0EC, 0x0BF, 0x080, 0x0FD, 0x0FE, 0x0FB, 0x0FC,
    0x0BA, 0x0AE, 0x059, 0x044, 0x045, 0x042, 0x046, 0x043, 0x047, 0x09C, 0x048, 0x054, 0x051,
    0x052, 0x053, 0x058, 0x055, 0x056, 0x057, 0x08C, 0x049, 0x0CD, 0x0CE, 0x0CB, 0x0CF, 0x0CC,
    0x0E1, 0x070, 0x0DD, 0x0DE, 0x0DB, 0x0DC, 0x08D, 0x08E, 0x0DF
  };
  private static final char[] CHARACTERS_THAT_MUST_BE_QUOTED_FOR_CSV = { ',', '"', '\n' };

  /**
   * <code>EA_TAB</code>
   */
  private static final int EA_TAB[] =
  {
    0x000, 0x001, 0x002, 0x003, 0x09C, 0x009, 0x086, 0x07F, 0x097, 0x08D, 0x08E, 0x00B, 0x00C,
    0x00D, 0x00E, 0x00F, 0x010, 0x011, 0x012, 0x013, 0x09D, 0x085, 0x008, 0x087, 0x018, 0x019,
    0x092, 0x08F, 0x01C, 0x01D, 0x01E, 0x01F, 0x080, 0x081, 0x082, 0x083, 0x084, 0x00A, 0x017,
    0x01B, 0x088, 0x089, 0x08A, 0x08B, 0x08C, 0x005, 0x006, 0x007, 0x090, 0x091, 0x016, 0x093,
    0x094, 0x095, 0x096, 0x004, 0x098, 0x099, 0x09A, 0x09B, 0x014, 0x015, 0x09E, 0x01A, 0x020,
    0x0A0, 0x0E2, 0x0E4, 0x0E0, 0x0E1, 0x0E3, 0x0E5, 0x0E7, 0x0F1, 0x0A2, 0x02E, 0x03C, 0x028,
    0x02B, 0x07C, 0x026, 0x0E9, 0x0EA, 0x0EB, 0x0E8, 0x0ED, 0x0EE, 0x0EF, 0x0EC, 0x0DF, 0x021,
    0x024, 0x02A, 0x029, 0x03B, 0x05E, 0x02D, 0x02F, 0x0C2, 0x0C4, 0x0C0, 0x0C1, 0x0C3, 0x0C5,
    0x0C7, 0x0D1, 0x0A6, 0x02C, 0x025, 0x05F, 0x03E, 0x03F, 0x0F8, 0x0C9, 0x0CA, 0x0CB, 0x0C8,
    0x0CD, 0x0CE, 0x0CF, 0x0CC, 0x060, 0x03A, 0x023, 0x040, 0x027, 0x03D, 0x022, 0x0D8, 0x061,
    0x062, 0x063, 0x064, 0x065, 0x066, 0x067, 0x068, 0x069, 0x0AB, 0x0BB, 0x0F0, 0x0FD, 0x0FE,
    0x0B1, 0x0B0, 0x06A, 0x06B, 0x06C, 0x06D, 0x06E, 0x06F, 0x070, 0x071, 0x072, 0x0AA, 0x0BA,
    0x0E6, 0x0B8, 0x0C6, 0x0A4, 0x0B5, 0x07E, 0x073, 0x074, 0x075, 0x076, 0x077, 0x078, 0x079,
    0x07A, 0x0A1, 0x0BF, 0x0D0, 0x05B, 0x0DE, 0x0AE, 0x0AC, 0x0A3, 0x0A5, 0x0B7, 0x0A9, 0x0A7,
    0x0B6, 0x0BC, 0x0BD, 0x0BE, 0x0DD, 0x0A8, 0x0AF, 0x05D, 0x0B4, 0x0D7, 0x07B, 0x041, 0x042,
    0x043, 0x044, 0x045, 0x046, 0x047, 0x048, 0x049, 0x0AD, 0x0F4, 0x0F6, 0x0F2, 0x0F3, 0x0F5,
    0x07D, 0x04A, 0x04B, 0x04C, 0x04D, 0x04E, 0x04F, 0x050, 0x051, 0x052, 0x0B9, 0x0FB, 0x0FC,
    0x0F9, 0x0FA, 0x0FF, 0x05C, 0x0F7, 0x053, 0x054, 0x055, 0x056, 0x057, 0x058, 0x059, 0x05A,
    0x0B2, 0x0D4, 0x0D6, 0x0D2, 0x0D3, 0x0D5, 0x030, 0x031, 0x032, 0x033, 0x034, 0x035, 0x036,
    0x037, 0x038, 0x039, 0x0B3, 0x0DB, 0x0DC, 0x0D9, 0x0DA, 0x09F
  };
  private static final String ESCAPED_QUOTE = "\"\"";
  private static final String QUOTE = "\"";
  private static ThreadLocal<SimpleDateFormat> threadLocalSimpleDateFormat =
      ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd hh:mm a"));

  /**
   * Private default constructor to enforce utility pattern.
   */
  private StringUtil() {}

  /**
   * Convert the ASCII string to an array of bytes in EBCDIC.
   *
   * @param source the ASCII string to convert
   *
   * @return the byte array containing the results of converting the ASCII string to the EBCDIC
   * encoding
   */
  public static byte[] asciiEbcdic(String source)
  {
    int len;
    byte[] pBuf = source.getBytes();

    len = source.length();

    for (int i = 0; i < len; i++)
    {
      pBuf[i] = (byte) translateByte((pBuf[i] & 0xFF), "EBCDIC");
    }

    return pBuf;
  }

  /**
   * Capitalize each word in the string.
   *
   * @param str the string
   *
   * @return the capitalized string
   */
  public static String capitalize(String str)
  {
    if (StringUtil.isNullOrEmpty(str))
    {
      return str;
    }

    StringReader reader = new StringReader(str);

    try
    {
      StringBuilder buffer = new StringBuilder();

      boolean capitilizeNextCharacter = true;

      int c;
      while ((c = reader.read()) != -1)
      {
        char character = (char) c;

        if (capitilizeNextCharacter)
        {
          buffer.append(Character.toUpperCase(character));
        }
        else
        {
          buffer.append(character);
        }

        capitilizeNextCharacter = (character == '.') || (character == ' ');
      }

      return buffer.toString();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to capitalize the string (" + str + ")", e);
    }
  }

  /**
   * Concatenate the arrays of bytes.
   *
   * @param inputs the arrays of bytes to concatenate
   *
   * @return the byte array containing the concatenated arrays of bytes
   */
  public static byte[] concat(byte[][] inputs)
  {
    int total = 0;

    for (byte[] input : inputs)
    {
      total += input.length;
    }

    byte[] ret = new byte[total];
    int pos = 0;

    for (byte[] input : inputs)
    {
      System.arraycopy(input, 0, ret, pos, input.length);
      pos += input.length;
    }

    return ret;
  }

  /**
   * Convert a date to the standard <code>String</code> representation <b>yyyy-MM-dd hh:mm a</b>.
   *
   * @param date the date to convert
   *
   * @return the date in the standard <code>String</code> representation
   */
  public static String convertDateToString(Date date)
  {
    return threadLocalSimpleDateFormat.get().format(date);
  }

  /**
   * Convert a list of strings into a single string delimited by the specified delimiter.
   *
   * @param values    the list of strings to convert
   * @param delimiter the delimiter to use
   *
   * @return a single delimited string
   */
  public static String delimit(Collection<String> values, String delimiter)
  {
    StringBuilder buffer = new StringBuilder();

    for (String value : values)
    {
      if (buffer.length() > 0)
      {
        buffer.append(delimiter);
      }

      buffer.append(value);
    }

    return buffer.toString();
  }

  /**
   * Convert a list of strings into a single string delimited by the specified delimiter.
   *
   * @param values    the list of strings to convert
   * @param delimiter the delimiter to use
   *
   * @return a single delimited string
   */
  public static String delimit(List<String> values, String delimiter)
  {
    StringBuilder buffer = new StringBuilder();

    for (int i = 0; i < values.size(); i++)
    {
      buffer.append(values.get(i));

      if (i < (values.size() - 1))
      {
        buffer.append(delimiter);
      }
    }

    return buffer.toString();
  }

  /**
   * Convert an array of strings into a single string delimited by the specified delimiter.
   *
   * @param values    the array of strings to convert
   * @param delimiter the delimiter to use
   *
   * @return a single delimited string
   */
  public static String delimit(String[] values, String delimiter)
  {
    StringBuilder buffer = new StringBuilder();

    for (int i = 0; i < values.length; i++)
    {
      buffer.append(values[i]);

      if (i < (values.length - 1))
      {
        buffer.append(delimiter);
      }
    }

    return buffer.toString();
  }

  /**
   * Convert the array of bytes from the EBCDIC to ASCII encoding.
   *
   * @param source the byte array containing the ASCII-encoded bytes to convert
   *
   * @return a byte array containing the EBCDIC-encoded bytes
   */
  public static byte[] ebcdicAscii(byte[] source)
  {
    byte[] pBuf = new byte[source.length];

    for (int i = 0; i < source.length; i++)
    {
      pBuf[i] = (byte) translateByte((source[i] & 0xFF), "ASCII");
    }

    return pBuf;
  }

  /**
   * Convert a subset of the array of bytes from the EBCDIC to ASCII encoding.
   *
   * @param source the byte array containing the ASCII-encoded bytes to convert
   * @param from   the index into the array of bytes where the conversion should start
   * @param to     the index into the array of bytes where the conversion should stop
   *
   * @return a byte array containing the EBCDIC-encoded bytes
   */
  public static byte[] ebcdicAscii(byte[] source, int from, int to)
  {
    byte[] pBuf = new byte[source.length];

    for (int i = from; i < to; i++)
    {
      pBuf[i] = (byte) translateByte((source[i] & 0xFF), "ASCII");
    }

    return pBuf;
  }

  /**
   * Escape the CSV value if required.
   *
   * @param value the CSV value to escape
   *
   * @return the escaped CSV value
   */
  public static String escapeCSVValue(String value)
  {
    if (value.contains(QUOTE))
    {
      value = value.replace(QUOTE, ESCAPED_QUOTE);
    }

    for (char characterThatMustBeQuotedForCSV : CHARACTERS_THAT_MUST_BE_QUOTED_FOR_CSV)
    {
      if (value.indexOf(characterThatMustBeQuotedForCSV) != -1)
      {
        return QUOTE + value + QUOTE;
      }
    }

    return value;
  }

  /**
   * Returns <code>true</code> if the string is null or empty or <code>false</code> otherwise.
   *
   * @param in the string to check
   *
   * @return <code>true</code> if the string is null or empty or <code>false</code> otherwise
   */
  public static boolean isNullOrEmpty(String in)
  {
    return (in == null) || (in.length() == 0);
  }

  /**
   * Ensure that a string is not <code>null</code> by converting the <code>null</code> string to an
   * empty string if required.
   *
   * @param in the string to convert
   *
   * @return the specified string if it is not <code>null</code> else an empty string
   */
  public static String notNull(String in)
  {
    if (in == null)
    {
      return "";
    }
    else
    {
      return in;
    }
  }

  /**
   * Perform a string-based search and replace.
   *
   * @param str         the string to search
   * @param searchFor   the string to search for
   * @param replaceWith the string to replace with
   *
   * @return the string containing the result of the search and replace operation
   */
  public static String replace(String str, String searchFor, String replaceWith)
  {
    int c = 0;
    int i = str.indexOf(searchFor, c);

    if (i == -1)
    {
      return str;
    }

    StringBuilder buf = new StringBuilder(str.length() + replaceWith.length());

    do
    {
      buf.append(str.substring(c, i));
      buf.append(replaceWith);
      c = i + searchFor.length();
    }
    while ((i = str.indexOf(searchFor, c)) != -1);

    if (c < str.length())
    {
      buf.append(str.substring(c, str.length()));
    }

    return buf.toString();
  }

  /**
   * Swap the endian-ness of two bytes.
   *
   * @param sEndian the short containing the two bytes
   *
   * @return a short containing the two bytes with the endian-ness swapped
   */
  public static byte[] swapEndian2Bytes(short sEndian)
  {
    byte[] b = new byte[2];

    b[0] = (byte) (sEndian >>> 8);
    b[1] = (byte) sEndian;  /* cast implies & 0xff */

    return b;
  }

  /**
   * Convert a hex encoded string to its byte representation.
   *
   * @param hexString the hex encoded string to convert
   *
   * @return the byte representation of the hex encoded string
   *
   * @throws IllegalArgumentException if the number of digits in the parameter hexString is not even
   *                                  or if one of the digits is not a valid hex digit
   */
  public static byte[] toByteArray(String hexString)
    throws IllegalArgumentException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    char c1;
    char c2;
    byte b;

    if ((hexString.length() & 1) == 1)
    {
      throw new IllegalArgumentException("A Hex String Must Have a Even Number Of Digits");
    }

    for (int i = 0; i < hexString.length(); i += 2)
    {
      c1 = hexString.charAt(i);
      c2 = hexString.charAt(i + 1);
      b = 0;

      if ((c1 >= '0') && (c1 <= '9'))
      {
        b += ((c1 - '0') * 16);
      }
      else if ((c1 >= 'a') && (c1 <= 'f'))
      {
        b += ((c1 - 'a' + 10) * 16);
      }
      else if ((c1 >= 'A') && (c1 <= 'F'))
      {
        b += ((c1 - 'A' + 10) * 16);
      }
      else
      {
        throw new IllegalArgumentException("Hex String Containes invalid character" + b);
      }

      if ((c2 >= '0') && (c2 <= '9'))
      {
        b += (c2 - '0');
      }
      else if ((c2 >= 'a') && (c2 <= 'f'))
      {
        b += (c2 - 'a' + 10);
      }
      else if ((c2 >= 'A') && (c2 <= 'F'))
      {
        b += (c2 - 'A' + 10);
      }
      else
      {
        throw new IllegalArgumentException("Hex String Containes invalid character" + b);
      }

      baos.write(b);
    }

    return baos.toByteArray();
  }

  /**
   * Returns the comma-delimited String containing the specified values.
   *
   * @param values the values
   *
   * @return the comma-delimited String containing the specified values
   */
  public static String toCommaDelimited(List<String> values)
  {
    StringBuilder buffer = new StringBuilder();

    for (String value : values)
    {
      if (buffer.length() > 0)
      {
        buffer.append(",");
      }

      buffer.append(value);
    }

    return buffer.toString();
  }

  /**
   * Convert a byte array to its hex encoded representation.
   *
   * @param bytes the byte array to convert
   *
   * @return the hex encoded representation of the byte array
   */
  public static String toHexString(byte bytes[])
  {
    StringBuilder sb = new StringBuilder(bytes.length * 2);

    for (byte aByte : bytes)
    {
      sb.append(cd(aByte >> 4));
      sb.append(cd(aByte & 0x0f));
    }

    return (sb.toString());
  }

  /**
   * Truncates the specified string if it is longer than the specified maximum length and appends
   * an ellipsis (...) on the end.
   *
   * @param str       the string to truncate
   * @param maxLength the maximum length of the text
   *
   * @return the truncated string
   */
  public static String truncate(String str, int maxLength)
  {
    if (str.length() > maxLength)
    {
      return str.substring(0, maxLength).trim() + "...";
    }
    else
    {
      return str;
    }
  }

  /**
   * Convert a delimited string with the specified delimiter into an array of strings.
   *
   * @param values    the delimited string
   * @param delimiter the delimiter
   *
   * @return an array of strings
   */
  public static String[] undelimit(String values, String delimiter)
  {
    if (values == null)
    {
      return new String[0];
    }

    StringTokenizer tokens = new StringTokenizer(values, delimiter);
    List<String> list = new ArrayList<>();

    while (tokens.hasMoreTokens())
    {
      list.add(tokens.nextToken().trim());
    }

    return list.toArray(new String[list.size()]);
  }

  /**
   * Unpack the binary-coded decimal.
   *
   * @param source the byte array containing the binary-coded decimal to unpack
   *
   * @return the byte array containing the unpacked binary-coded decimal
   */
  public static byte[] unpackBCD(byte[] source)
  {
    byte[] ret = new byte[source.length * 2];
    int index = 0;

    for (byte aSource : source)
    {
      ret[index + 1] = (byte) (aSource & 0x0F);
      ret[index] = (byte) ((aSource >> 4) & 0x0F);
      index += 2;
    }

    return ret;
  }

  /**
   * Convers a byte to a Hex character, it is assumed that the byte only has the lower 4 bits used
   * {LSN}
   *
   * @param value the value to convert to a hex character
   *
   * @return the Hex character
   */
  private static char cd(int value)
  {
    value &= 0x0f;

    if (value >= 10)
    {
      return ((char) (value - 10 + 'a'));
    }
    else
    {
      return ((char) (value + '0'));
    }
  }

  private static int translateByte(int i, String thisCharSet)
  {
    byte tByte;

    if (thisCharSet.equals("EBCDIC"))
    {
      tByte = (byte) (AE_TAB[i]);
    }
    else
    {
      tByte = (byte) (EA_TAB[i]);
    }

    return tByte & 0xFF;
  }
}
