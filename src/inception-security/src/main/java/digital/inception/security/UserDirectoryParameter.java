/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.security;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.util.Base64Util;
import digital.inception.core.util.BinaryBuffer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserDirectoryParameter</code> class stores a parameter for a user directory as a
 * name-value pair.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A name-value pair parameter for a user directory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "value"})
@XmlRootElement(name = "UserDirectoryParameter", namespace = "http://security.inception.digital")
@XmlType(
    name = "UserDirectoryParameter",
    namespace = "http://security.inception.digital",
    propOrder = {"name", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class UserDirectoryParameter implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The name for the user directory parameter. */
  @Schema(description = "The name for the user directory parameter", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The value for the user directory parameter. */
  @Schema(description = "The value for the user directory parameter", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(max = 4000)
  private String value;

  /** Constructs a new <code>UserDirectoryParameter</code>. */
  public UserDirectoryParameter() {}

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name the name for the user directory parameter
   * @param value the <code>BigDecimal</code> value for the user directory parameter
   */
  public UserDirectoryParameter(String name, BigDecimal value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name the name for the user directory parameter
   * @param value the binary value for the user directory parameter
   */
  public UserDirectoryParameter(String name, BinaryBuffer value) {
    this.name = name;
    this.value = Base64Util.encodeBytes(value.getData());
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name the name for the user directory parameter
   * @param value the binary value for the user directory parameter
   */
  public UserDirectoryParameter(String name, byte[] value) {
    this.name = name;
    this.value = Base64Util.encodeBytes(value);
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name the name for the user directory parameter
   * @param value the <code>double</code> value for the user directory parameter
   */
  public UserDirectoryParameter(String name, double value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name the name for the user directory parameter
   * @param value the <code>long</code> value for the user directory parameter
   */
  public UserDirectoryParameter(String name, long value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name the name for the user directory parameter
   * @param value the <code>String</code> value for the user directory parameter
   */
  public UserDirectoryParameter(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns whether the list of <code>UserDirectoryParameter</code> instances contains an instance
   * whose name matches the specified name.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @return <code>true</code> if the list of <code>UserDirectoryParameter</code> instances contains
   *     an instance whose name matches the specified name or <code>false</code> otherwise
   */
  public static boolean contains(List<UserDirectoryParameter> parameters, String name) {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the binary value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @return the binary value for the <code>UserDirectoryParameter</code> instance with the
   *     specified name in the specified list
   */
  public static byte[] getBinaryValue(List<UserDirectoryParameter> parameters, String name)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        try {
          return Base64Util.decode(parameter.value);
        } catch (Throwable e) {
          throw new UserDirectoryParameterException(
              String.format(
                  "Failed to retrieve the binary value for the user directory parameter (%s)",
                  parameter.name));
        }
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to retrieve the binary value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Returns the <code>boolean</code> value for the <code>UserDirectoryParameter</code> instance
   * with the specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @return the <code>boolean</code> value for the <code>UserDirectoryParameter</code> instance
   *     with the specified name in the specified list
   */
  public static boolean getBooleanValue(List<UserDirectoryParameter> parameters, String name)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        try {
          return Boolean.parseBoolean(parameter.value);
        } catch (Throwable e) {
          throw new UserDirectoryParameterException(
              String.format(
                  "Failed to retrieve the boolean value for the user directory parameter (%s)",
                  parameter.name));
        }
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to retrieve the boolean value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance
   * with the specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @return the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance
   *     with the specified name in the specified list
   */
  public static BigDecimal getDecimalValue(List<UserDirectoryParameter> parameters, String name)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        try {
          return new BigDecimal(parameter.value);
        } catch (Throwable e) {
          throw new UserDirectoryParameterException(
              String.format(
                  "Failed to retrieve the decimal value for the user directory parameter (%s)",
                  parameter.name));
        }
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to retrieve the decimal value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Returns the <code>double</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @return the <code>double</code> value for the <code>UserDirectoryParameter</code> instance with
   *     the specified name in the specified list
   */
  public static double getDoubleValue(List<UserDirectoryParameter> parameters, String name)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        try {
          return Double.parseDouble(parameter.value);
        } catch (Throwable e) {
          throw new UserDirectoryParameterException(
              String.format(
                  "Failed to retrieve the double value for the user directory parameter (%s)",
                  parameter.name));
        }
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to retrieve the double value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Returns the <code>int</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @return the <code>int</code> value for the <code>UserDirectoryParameter</code> instance with
   *     the specified name in the specified list
   */
  public static int getIntegerValue(List<UserDirectoryParameter> parameters, String name)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        try {
          return Integer.parseInt(parameter.value);
        } catch (Throwable e) {
          throw new UserDirectoryParameterException(
              String.format(
                  "Failed to retrieve the integer value for the user directory parameter (%s)",
                  parameter.name));
        }
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to retrieve the integer value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Returns the <code>long</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @return the <code>long</code> value for the <code>UserDirectoryParameter</code> instance with
   *     the specified name in the specified list
   */
  public static long getLongValue(List<UserDirectoryParameter> parameters, String name)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        try {
          return Long.parseLong(parameter.value);
        } catch (Throwable e) {
          throw new UserDirectoryParameterException(
              String.format(
                  "Failed to retrieve the long value for the user directory parameter (%s)",
                  parameter.name));
        }
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to retrieve the long value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Returns the <code>String</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @return the <code>String</code> value for the <code>UserDirectoryParameter</code> instance with
   *     the specified name in the specified list
   */
  public static String getStringValue(List<UserDirectoryParameter> parameters, String name)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        return parameter.value;
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to retrieve the string value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Set the binary value for the <code>UserDirectoryParameter</code> instance with the specified
   * name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @param value the binary value for the user directory parameter
   */
  public static void setBinaryValue(
      List<UserDirectoryParameter> parameters, String name, BinaryBuffer value)
      throws UserDirectoryParameterException {
    setBinaryValue(parameters, name, value.getData());
  }

  /**
   * Set the binary value for the <code>UserDirectoryParameter</code> instance with the specified
   * name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @param value the binary value for the user directory parameter
   */
  public static void setBinaryValue(
      List<UserDirectoryParameter> parameters, String name, byte[] value)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        parameter.setBinaryValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to set the binary value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Set the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @param value the <code>BigDecimal</code> value for the user directory parameter
   */
  public static void setDecimalValue(
      List<UserDirectoryParameter> parameters, String name, BigDecimal value)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        parameter.setDecimalValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to set the decimal value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Set the <code>double</code> value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @param value the <code>double</code> value for the user directory parameter
   */
  public static void setDoubleValue(
      List<UserDirectoryParameter> parameters, String name, double value)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        parameter.setDoubleValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to set the double value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Set the <code>int</code> value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @param value the <code>int</code> value for the user directory parameter
   */
  public static void setIntegerValue(
      List<UserDirectoryParameter> parameters, String name, int value)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        parameter.setIntegerValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to set the long value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Set the <code>long</code> value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @param value the <code>long</code> value for the user directory parameter
   */
  public static void setLongValue(List<UserDirectoryParameter> parameters, String name, long value)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        parameter.setLongValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to set the long value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Set the <code>String</code> value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param parameters the <code>UserDirectoryParameter</code> instances to search for the <code>
   *                   UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   * @param value the <code>String</code> value for the user directory parameter
   */
  public static void setStringValue(
      List<UserDirectoryParameter> parameters, String name, String value)
      throws UserDirectoryParameterException {
    for (UserDirectoryParameter parameter : parameters) {
      if (parameter.name.equalsIgnoreCase(name)) {
        parameter.setStringValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(
        String.format(
            "Failed to set the string value for the user directory parameter (%s): "
                + "The user directory parameter could not be found",
            name));
  }

  /**
   * Returns the binary value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the binary value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  public byte[] getBinaryValue() throws UserDirectoryParameterException {
    try {
      return Base64Util.decode(value);
    } catch (Throwable e) {
      throw new UserDirectoryParameterException(
          String.format(
              "Failed to retrieve the binary value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  public BigDecimal getDecimalValue() throws UserDirectoryParameterException {
    try {
      return new BigDecimal(value);
    } catch (Throwable e) {
      throw new UserDirectoryParameterException(
          String.format(
              "Failed to retrieve the decimal value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the <code>double</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>double</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  public double getDoubleValue() throws UserDirectoryParameterException {
    try {
      return Double.parseDouble(value);
    } catch (Throwable e) {
      throw new UserDirectoryParameterException(
          String.format(
              "Failed to retrieve the double value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the <code>int</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>int</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  public int getIntegerValue() throws UserDirectoryParameterException {
    try {
      return Integer.parseInt(value);
    } catch (Throwable e) {
      throw new UserDirectoryParameterException(
          String.format(
              "Failed to retrieve the integer value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the <code>long</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>long</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  public long getLongValue() throws UserDirectoryParameterException {
    try {
      return Long.parseLong(value);
    } catch (Throwable e) {
      throw new UserDirectoryParameterException(
          String.format(
              "Failed to retrieve the long value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the name for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the name for the <code>UserDirectoryParameter</code> instance
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the <code>String</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>String</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  public String getStringValue() {
    return value;
  }

  /**
   * Returns the <code>String</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>String</code> value for the <code>UserDirectoryParameter</code> instance
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the binary value for the user directory parameter.
   *
   * @param value the binary value for the user directory parameter
   */
  @JsonIgnore
  public void setBinaryValue(BinaryBuffer value) {
    this.value = Base64Util.encodeBytes(value.getData());
  }

  /**
   * Set the binary value for the user directory parameter.
   *
   * @param value the binary value for the user directory parameter
   */
  @JsonIgnore
  public void setBinaryValue(byte[] value) {
    this.value = Base64Util.encodeBytes(value);
  }

  /**
   * Set the <code>boolean</code> value for the user directory parameter.
   *
   * @param value the <code>boolean</code> value for the user directory parameter
   */
  @JsonIgnore
  public void setBooleanValue(boolean value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>BigDecimal</code> value for the user directory parameter.
   *
   * @param value the <code>BigDecimal</code> value for the user directory parameter
   */
  @JsonIgnore
  public void setDecimalValue(BigDecimal value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>double</code> value for the user directory parameter.
   *
   * @param value the <code>double</code> value for the user directory parameter
   */
  @JsonIgnore
  public void setDoubleValue(double value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>int</code> value for the user directory parameter.
   *
   * @param value the <code>int</code> value for the user directory parameter
   */
  @JsonIgnore
  public void setIntegerValue(int value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>long</code> value for the user directory parameter.
   *
   * @param value the <code>long</code> value for the user directory parameter
   */
  @JsonIgnore
  public void setLongValue(long value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the name for the user directory parameter.
   *
   * @param name the name for the user directory parameter
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the <code>String</code> value for the user directory parameter.
   *
   * @param value the <code>String</code> value for the user directory parameter
   */
  @JsonIgnore
  public void setStringValue(String value) {
    this.value = value;
  }

  /**
   * Set the <code>String</code> value for the user directory parameter.
   *
   * @param value the <code>String</code> value for the user directory parameter
   */
  public void setValue(String value) {
    this.value = value;
  }
}
