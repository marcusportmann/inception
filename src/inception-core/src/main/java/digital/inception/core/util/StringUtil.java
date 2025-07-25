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

/**
 * The {@code StringUtil} class is a utility class which provides methods for performing operations
 * on strings.
 *
 * @author Marcus Portmann
 */
public final class StringUtil {

  /** Private constructor to prevent instantiation. */
  private StringUtil() {}

  /**
   * Compares two strings for equality, ignoring case considerations.
   *
   * <p>This method is null-safe. If both input strings are {@code null}, the method returns {@code
   * true}. If only one of the strings is {@code null}, the method returns {@code false}. Otherwise,
   * the method returns the result of {@code a.equalsIgnoreCase(b)}.
   *
   * @param a the first string to compare, may be {@code null}
   * @param b the second string to compare, may be {@code null}
   * @return {@code true} if the strings are equal (ignoring case), or both are {@code null}; {@code
   *     false} otherwise
   */
  public static boolean equalsIgnoreCase(String a, String b) {
    return a == null ? b == null : a.equalsIgnoreCase(b);
  }
}
