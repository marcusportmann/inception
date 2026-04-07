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

package digital.inception.core.reference;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Currency} class holds the information for a currency.
 *
 * <p>See: https://en.wikipedia.org/wiki/ISO_4217
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class Currency implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 4217 alphabetic code for the currency. */
  private String code;

  /** The description for the currency. */
  private String description;

  /** The name of the currency. */
  private String name;

  /** The ISO 4217 numeric code for the currency. */
  private String numericCode;

  /** The short name for the currency. */
  private String shortName;

  /** The sort order for the currency. */
  private Integer sortOrder;

  /** Constructs a new {@code Currency}. */
  public Currency() {}

  /**
   * Constructs a new {@code Currency}.
   *
   * @param code the ISO 4217 alphabetic code for the currency
   * @param numericCode the ISO 4217 numeric code for the currency
   * @param sortOrder the sort order for the currency
   * @param name the name of the currency
   * @param shortName the short name for the currency
   * @param description the description for the currency
   */
  public Currency(
      String code,
      String numericCode,
      Integer sortOrder,
      String name,
      String shortName,
      String description) {
    this.code = code;
    this.numericCode = numericCode;
    this.sortOrder = sortOrder;
    this.name = name;
    this.shortName = shortName;
    this.description = description;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    Currency other = (Currency) object;

    return Objects.equals(code, other.code);
  }

  /**
   * Returns the ISO 4217 alphabetic code for the currency.
   *
   * @return the ISO 4217 alphabetic code for the currency
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the currency.
   *
   * @return the description for the currency
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the name of the currency.
   *
   * @return the name of the currency
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the ISO 4217 numeric code for the currency.
   *
   * @return the ISO 4217 numeric code for the currency
   */
  public String getNumericCode() {
    return numericCode;
  }

  /**
   * Returns the short name for the currency.
   *
   * @return the short name for the currency
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Returns the sort order for the currency.
   *
   * @return the sort order for the currency
   */
  public Integer getSortOrder() {
    return sortOrder;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((code == null) ? 0 : code.hashCode());
  }

  /**
   * Sets the ISO 4217 alphabetic code for the currency.
   *
   * @param code the ISO 4217 alphabetic code for the currency
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Sets the description for the currency.
   *
   * @param description the description for the currency
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the name of the currency.
   *
   * @param name the name of the currency
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the ISO 4217 numeric code for the currency.
   *
   * @param numericCode the ISO 4217 numeric code for the currency
   */
  public void setNumericCode(String numericCode) {
    this.numericCode = numericCode;
  }

  /**
   * Sets the short name for the currency.
   *
   * @param shortName the short name for the currency
   */
  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  /**
   * Sets the sort order for the currency.
   *
   * @param sortOrder the sort order for the currency
   */
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
}
