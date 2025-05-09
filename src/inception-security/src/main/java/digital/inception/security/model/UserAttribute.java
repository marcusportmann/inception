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

package digital.inception.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.BinaryBuffer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

/**
 * The {@code UserAttribute} class stores a user attribute as a name-value pair.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A user attribute in the form of a name-value pair")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "value"})
@XmlRootElement(name = "Attribute", namespace = "https://inception.digital/security")
@XmlType(
    name = "UserAttribute",
    namespace = "https://inception.digital/security",
    propOrder = {"name", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class UserAttribute implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The name of the user attribute. */
  @Schema(
      description = "The name of the user attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The value for the user attribute. */
  @Schema(
      description = "The value for the user attribute",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(max = 2000)
  private String value;

  /** Constructs a new {@code UserAttribute}. */
  public UserAttribute() {}

  /**
   * Constructs a new {@code UserAttribute}.
   *
   * @param name the name of the user attribute
   * @param value the {@code BigDecimal} value for the user attribute
   */
  public UserAttribute(String name, BigDecimal value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new {@code UserAttribute}.
   *
   * @param name the name of the user attribute
   * @param value the binary value for the user attribute
   */
  public UserAttribute(String name, BinaryBuffer value) {
    this.name = name;
    this.value = Base64.getEncoder().encodeToString(value.getData());
  }

  /**
   * Constructs a new {@code UserAttribute}.
   *
   * @param name the name of the user attribute
   * @param value the binary value for the user attribute
   */
  public UserAttribute(String name, byte[] value) {
    this.name = name;
    this.value = Base64.getEncoder().encodeToString(value);
  }

  /**
   * Constructs a new {@code UserAttribute}.
   *
   * @param name the name of the user attribute
   * @param value the {@code double} value for the user attribute
   */
  public UserAttribute(String name, double value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new {@code UserAttribute}.
   *
   * @param name the name of the user attribute
   * @param value the {@code long} value for the user attribute
   */
  public UserAttribute(String name, long value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new {@code UserAttribute}.
   *
   * @param name the name of the user attribute
   * @param value the {@code String} value for the user attribute
   */
  public UserAttribute(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns whether the list of user attributes contains a user attribute whose name matches the
   * specified name.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @return {@code true} if the list of user attributes contains a user attribute whose name
   *     matches the specified name or {@code false} otherwise
   */
  public static boolean contains(List<UserAttribute> list, String name) {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the binary value for the user attribute with the specified name in the specified list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @return the binary value for the user attribute with the specified name in the specified list
   * @throws UserAttributeException if the binary value could not be retrieved for the user
   *     attribute
   */
  public static byte[] getBinaryValue(List<UserAttribute> list, String name)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        try {
          return Base64.getDecoder().decode(userAttribute.value);
        } catch (Throwable e) {
          throw new UserAttributeException(
              "Failed to retrieve the binary value for the user attribute ("
                  + userAttribute.name
                  + ")");
        }
      }
    }

    throw new UserAttributeException(
        "Failed to retrieve the binary value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Returns the {@code BigDecimal} value for the user attribute with the specified name in the
   * specified list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @return the {@code BigDecimal} value for the user attribute with the specified name in the
   *     specified list
   * @throws UserAttributeException if the decimal value could not be retrieved for the user
   *     attribute
   */
  public static BigDecimal getDecimalValue(List<UserAttribute> list, String name)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        try {
          return new BigDecimal(userAttribute.value);
        } catch (Throwable e) {
          throw new UserAttributeException(
              "Failed to retrieve the decimal value for the user attribute ("
                  + userAttribute.name
                  + ")");
        }
      }
    }

    throw new UserAttributeException(
        "Failed to retrieve the decimal value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Returns the {@code double} value for the user attribute with the specified name in the
   * specified list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @return the {@code double} value for the user attribute with the specified name in the
   *     specified list
   * @throws UserAttributeException if the double value could not be retrieved for the user
   *     attribute
   */
  public static double getDoubleValue(List<UserAttribute> list, String name)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        try {
          return Double.parseDouble(userAttribute.value);
        } catch (Throwable e) {
          throw new UserAttributeException(
              "Failed to retrieve the double value for the user attribute ("
                  + userAttribute.name
                  + ")");
        }
      }
    }

    throw new UserAttributeException(
        "Failed to retrieve the double value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Returns the {@code int} value for the user attribute with the specified name in the specified
   * list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @return the {@code int} value for the user attribute with the specified name in the specified
   *     list
   * @throws UserAttributeException if the integer value could not be retrieved for the user
   *     attribute
   */
  public static int getIntegerValue(List<UserAttribute> list, String name)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        try {
          return Integer.parseInt(userAttribute.value);
        } catch (Throwable e) {
          throw new UserAttributeException(
              "Failed to retrieve the integer value for the user attribute ("
                  + userAttribute.name
                  + ")");
        }
      }
    }

    throw new UserAttributeException(
        "Failed to retrieve the integer value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Returns the {@code long} value for the user attribute with the specified name in the specified
   * list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @return the {@code long} value for the user attribute with the specified name in the specified
   *     list
   * @throws UserAttributeException if the long value could not be retrieved for the user attribute
   */
  public static long getLongValue(List<UserAttribute> list, String name)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        try {
          return Long.parseLong(userAttribute.value);
        } catch (Throwable e) {
          throw new UserAttributeException(
              "Failed to retrieve the long value for the user attribute ("
                  + userAttribute.name
                  + ")");
        }
      }
    }

    throw new UserAttributeException(
        "Failed to retrieve the long value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Returns the {@code String} value for the user attribute with the specified name in the
   * specified list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @return the {@code String} value for the user attribute with the specified name in the
   *     specified list
   * @throws UserAttributeException if the string value could not be retrieved for the user
   *     attribute
   */
  public static String getStringValue(List<UserAttribute> list, String name)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        return userAttribute.value;
      }
    }

    throw new UserAttributeException(
        "Failed to retrieve the string value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Set the binary value for the user attribute with the specified name in the specified list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @param value the binary value for the user attribute
   * @throws UserAttributeException if the binary value could not be set for the user attribute
   */
  public static void setBinaryValue(List<UserAttribute> list, String name, BinaryBuffer value)
      throws UserAttributeException {
    setBinaryValue(list, name, value.getData());
  }

  /**
   * Set the binary value for the user attribute with the specified name in the specified list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @param value the binary value for the user attribute
   * @throws UserAttributeException if the binary value could not be set for the user attribute
   */
  public static void setBinaryValue(List<UserAttribute> list, String name, byte[] value)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        userAttribute.setBinaryValue(value);

        return;
      }
    }

    throw new UserAttributeException(
        "Failed to set the binary value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Set the {@code BigDecimal} value for the user attribute with the specified name in the
   * specified list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @param value the {@code BigDecimal} value for the user attribute
   * @throws UserAttributeException if the decimal value could not be set for the user attribute
   */
  public static void setDecimalValue(List<UserAttribute> list, String name, BigDecimal value)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        userAttribute.setDecimalValue(value);

        return;
      }
    }

    throw new UserAttributeException(
        "Failed to set the decimal value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Set the {@code double} value for the user attribute with the specified name in the specified
   * list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @param value the {@code double} value for the user attribute
   * @throws UserAttributeException if the double value could not be set for the user attribute
   */
  public static void setDoubleValue(List<UserAttribute> list, String name, double value)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        userAttribute.setDoubleValue(value);

        return;
      }
    }

    throw new UserAttributeException(
        "Failed to set the double value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Set the {@code int} value for the user attribute with the specified name in the specified list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @param value the {@code int} value for the user attribute
   * @throws UserAttributeException if the integer value could not be set for the user attribute
   */
  public static void setIntegerValue(List<UserAttribute> list, String name, int value)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        userAttribute.setIntegerValue(value);

        return;
      }
    }

    throw new UserAttributeException(
        "Failed to set the integer value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Set the {@code long} value for the user attribute with the specified name in the specified
   * list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @param value the {@code long} value for the user attribute
   * @throws UserAttributeException if the long value could not be set for the user attribute
   */
  public static void setLongValue(List<UserAttribute> list, String name, long value)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        userAttribute.setLongValue(value);

        return;
      }
    }

    throw new UserAttributeException(
        "Failed to set the long value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Set the {@code String} value for the user attribute with the specified name in the specified
   * list.
   *
   * @param list the user attributes to search
   * @param name the name of the attribute
   * @param value the {@code String} value for the user attribute
   * @throws UserAttributeException if the string value could not be set for the user attribute
   */
  public static void setStringValue(List<UserAttribute> list, String name, String value)
      throws UserAttributeException {
    for (UserAttribute userAttribute : list) {
      if (userAttribute.name.equalsIgnoreCase(name)) {
        userAttribute.setStringValue(value);

        return;
      }
    }

    throw new UserAttributeException(
        "Failed to set the string value for the user attribute ("
            + name
            + "): The attribute could not be found");
  }

  /**
   * Returns the binary value for the user attribute.
   *
   * @return the binary value for the user attribute
   * @throws UserAttributeException if the binary value could not be retrieved for the user
   *     attribute
   */
  public byte[] getBinaryValue() throws UserAttributeException {
    try {
      return Base64.getDecoder().decode(value);
    } catch (Throwable e) {
      throw new UserAttributeException(
          "Failed to retrieve the binary value for the user attribute (" + name + ")");
    }
  }

  /**
   * Returns the {@code BigDecimal} value for the user attribute.
   *
   * @return the {@code BigDecimal} value for the user attribute
   * @throws UserAttributeException if the decimal value could not be retrieved for the user
   *     attribute
   */
  public BigDecimal getDecimalValue() throws UserAttributeException {
    try {
      return new BigDecimal(value);
    } catch (Throwable e) {
      throw new UserAttributeException(
          "Failed to retrieve the decimal value for the user attribute (" + name + ")");
    }
  }

  /**
   * Returns the {@code double} value for the user attribute.
   *
   * @return the {@code double} value for the user attribute
   * @throws UserAttributeException if the double value could not be retrieved for the user
   *     attribute
   */
  public double getDoubleValue() throws UserAttributeException {
    try {
      return Double.parseDouble(value);
    } catch (Throwable e) {
      throw new UserAttributeException(
          "Failed to retrieve the double value for the user attribute (" + name + ")");
    }
  }

  /**
   * Returns the {@code int} value for the user attribute.
   *
   * @return the {@code int} value for the user attribute
   * @throws UserAttributeException if the integer value could not be retrieved for the user
   *     attribute
   */
  public int getIntegerValue() throws UserAttributeException {
    try {
      return Integer.parseInt(value);
    } catch (Throwable e) {
      throw new UserAttributeException(
          "Failed to retrieve the integer value for the user attribute (" + name + ")");
    }
  }

  /**
   * Returns the {@code long} value for the user attribute.
   *
   * @return the {@code long} value for the user attribute
   * @throws UserAttributeException if the long value could not be retrieved for the user attribute
   */
  public long getLongValue() throws UserAttributeException {
    try {
      return Long.parseLong(value);
    } catch (Throwable e) {
      throw new UserAttributeException(
          "Failed to retrieve the long value for the user attribute (" + name + ")");
    }
  }

  /**
   * Returns the name of the user attribute.
   *
   * @return the name of the user attribute
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the {@code String} value for the user attribute.
   *
   * @return the {@code String} value for the user attribute
   */
  public String getStringValue() {
    return value;
  }

  /**
   * Returns the {@code String} value for the user attribute.
   *
   * @return the {@code String} value for the user attribute
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the binary value for the user attribute.
   *
   * @param value the binary value for the user attribute
   */
  public void setBinaryValue(BinaryBuffer value) {
    this.value = Base64.getEncoder().encodeToString(value.getData());
  }

  /**
   * Set the binary value for the user attribute.
   *
   * @param value the binary value for the user attribute
   */
  public void setBinaryValue(byte[] value) {
    this.value = Base64.getEncoder().encodeToString(value);
  }

  /**
   * Set the {@code BigDecimal} value for the user attribute.
   *
   * @param value the {@code BigDecimal} value for the user attribute
   */
  public void setDecimalValue(BigDecimal value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the {@code double} value for the user attribute.
   *
   * @param value the {@code double} value for the user attribute
   */
  public void setDoubleValue(double value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the {@code int} value for the user attribute.
   *
   * @param value the {@code int} value for the user attribute
   */
  public void setIntegerValue(int value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the {@code long} value for the user attribute.
   *
   * @param value the {@code long} value for the user attribute
   */
  public void setLongValue(long value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the name of the user attribute.
   *
   * @param name the name of the user attribute
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the {@code String} value for the user attribute.
   *
   * @param value the {@code String} value for the user attribute
   */
  public void setStringValue(String value) {
    this.value = value;
  }
}
