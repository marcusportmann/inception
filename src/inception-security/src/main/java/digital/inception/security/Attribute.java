/*
 * Copyright 2020 Marcus Portmann
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
 * The <code>Attribute</code> class stores an attribute for a security entity as a name-value pair.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Attribute")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "value"})
@XmlRootElement(name = "Attribute", namespace = "http://security.inception.digital")
@XmlType(
    name = "Attribute",
    namespace = "http://security.inception.digital",
    propOrder = {"name", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class Attribute implements Serializable {

  private static final long serialVersionUID = 1000000;

  /**
   * The name for the attribute.
   */
  @Schema(description = "The name for the attribute", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /**
   * The value for the attribute.
   */
  @Schema(description = "The value for the attribute", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(max = 4000)
  private String value;

  /**
   * Constructs a new <code>Attribute</code>.
   */
  public Attribute() {
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the <code>BigDecimal</code> value for the attribute
   */
  public Attribute(String name, BigDecimal value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the binary value for the attribute
   */
  public Attribute(String name, BinaryBuffer value) {
    this.name = name;
    this.value = Base64Util.encodeBytes(value.getData());
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the binary value for the attribute
   */
  public Attribute(String name, byte[] value) {
    this.name = name;
    this.value = Base64Util.encodeBytes(value);
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the <code>double</code> value for the attribute
   */
  public Attribute(String name, double value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the <code>long</code> value for the attribute
   */
  public Attribute(String name, long value) {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>Attribute</code>.
   *
   * @param name  the name for the attribute
   * @param value the <code>String</code> value for the attribute
   */
  public Attribute(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns whether the list of <code>Attribute</code> instances contains an instance whose name
   * matches the specified name.
   *
   * @param list the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *             the specified name
   * @param name the name for the attribute
   *
   * @return <code>true</code> if the list of <code>Attribute</code> instances contains an instance
   * whose name matches the specified name or <code>false</code> otherwise
   */
  public static boolean contains(List<Attribute> list, String name) {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the binary value for the <code>Attribute</code> instance with the specified name in the
   * specified list.
   *
   * @param list the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *             the specified name
   * @param name the name for the attribute
   *
   * @return the binary value for the <code>Attribute</code> instance with the specified name in the
   * specified list
   */
  public static byte[] getBinaryValue(List<Attribute> list, String name) throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        try {
          return Base64Util.decode(attribute.value);
        } catch (Throwable e) {
          throw new AttributeException(
              String.format(
                  "Failed to retrieve the binary value for the attribute (%s)", attribute.name));
        }
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to retrieve the binary value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list.
   *
   * @param list the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *             the specified name
   * @param name the name for the attribute
   *
   * @return the <code>BigDecimal</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list
   */
  public static BigDecimal getDecimalValue(List<Attribute> list, String name)
      throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        try {
          return new BigDecimal(attribute.value);
        } catch (Throwable e) {
          throw new AttributeException(
              String.format(
                  "Failed to retrieve the decimal value for the attribute (%s)", attribute.name));
        }
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to retrieve the decimal value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Returns the <code>double</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list.
   *
   * @param list the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *             the specified name
   * @param name the name for the attribute
   *
   * @return the <code>double</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list
   */
  public static double getDoubleValue(List<Attribute> list, String name) throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        try {
          return Double.parseDouble(attribute.value);
        } catch (Throwable e) {
          throw new AttributeException(
              String.format(
                  "Failed to retrieve the double value for the attribute (%s)", attribute.name));
        }
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to retrieve the double value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Returns the <int>long</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list.
   *
   * @param list the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *             the specified name
   * @param name the name for the attribute
   *
   * @return the <code>int</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list
   */
  public static int getIntegerValue(List<Attribute> list, String name) throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        try {
          return Integer.parseInt(attribute.value);
        } catch (Throwable e) {
          throw new AttributeException(
              String.format(
                  "Failed to retrieve the integer value for the attribute (%s)", attribute.name));
        }
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to retrieve the integer value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Returns the <code>long</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list.
   *
   * @param list the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *             the specified name
   * @param name the name for the attribute
   *
   * @return the <code>long</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list
   */
  public static long getLongValue(List<Attribute> list, String name) throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        try {
          return Long.parseLong(attribute.value);
        } catch (Throwable e) {
          throw new AttributeException(
              String.format(
                  "Failed to retrieve the long value for the attribute (%s)", attribute.name));
        }
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to retrieve the long value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Returns the <code>String</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list.
   *
   * @param list the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *             the specified name
   * @param name the name for the attribute
   *
   * @return the <code>String</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list
   */
  public static String getStringValue(List<Attribute> list, String name) throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        return attribute.value;
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to retrieve the string value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Set the binary value for the <code>Attribute</code> instance with the specified name in the
   * specified list.
   *
   * @param list  the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *              the specified name
   * @param name  the name for the attribute
   * @param value the binary value for the attribute
   */
  public static void setBinaryValue(List<Attribute> list, String name, BinaryBuffer value)
      throws AttributeException {
    setBinaryValue(list, name, value.getData());
  }

  /**
   * Set the binary value for the <code>Attribute</code> instance with the specified name in the
   * specified list.
   *
   * @param list  the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *              the specified name
   * @param name  the name for the attribute
   * @param value the binary value for the attribute
   */
  public static void setBinaryValue(List<Attribute> list, String name, byte[] value)
      throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        attribute.setBinaryValue(value);

        return;
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to set the binary value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Set the <code>BigDecimal</code> value for the <code>Attribute</code> instance with the
   * specified name in the specified list.
   *
   * @param list  the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *              the specified name
   * @param name  the name for the attribute
   * @param value the <code>BigDecimal</code> value for the attribute
   */
  public static void setDecimalValue(List<Attribute> list, String name, BigDecimal value)
      throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        attribute.setDecimalValue(value);

        return;
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to set the decimal value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Set the <code>double</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *              the specified name
   * @param name  the name for the attribute
   * @param value the <code>double</code> value for the attribute
   */
  public static void setDoubleValue(List<Attribute> list, String name, double value)
      throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        attribute.setDoubleValue(value);

        return;
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to set the double value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Set the <code>int</code> value for the <code>Attribute</code> instance with the specified name
   * in the specified list.
   *
   * @param list  the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *              the specified name
   * @param name  the name for the attribute
   * @param value the <code>int</code> value for the attribute
   */
  public static void setIntegerValue(List<Attribute> list, String name, int value)
      throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        attribute.setIntegerValue(value);

        return;
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to set the integer value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Set the <code>long</code> value for the <code>Attribute</code> instance with the specified name
   * in the specified list.
   *
   * @param list  the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *              the specified name
   * @param name  the name for the attribute
   * @param value the <code>long</code> value for the attribute
   */
  public static void setLongValue(List<Attribute> list, String name, long value)
      throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        attribute.setLongValue(value);

        return;
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to set the long value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Set the <code>String</code> value for the <code>Attribute</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the <code>Attribute</code> instances to search for the <code>Attribute</code> with
   *              the specified name
   * @param name  the name for the attribute
   * @param value the <code>String</code> value for the attribute
   */
  public static void setStringValue(List<Attribute> list, String name, String value)
      throws AttributeException {
    for (Attribute attribute : list) {
      if (attribute.name.equalsIgnoreCase(name)) {
        attribute.setStringValue(value);

        return;
      }
    }

    throw new AttributeException(
        String.format(
            "Failed to set the string value for the attribute (%s): "
                + "The attribute could not be found",
            name));
  }

  /**
   * Returns the binary value for the <code>Attribute</code> instance.
   *
   * @return the binary value for the <code>Attribute</code> instance
   */
  public byte[] getBinaryValue() throws AttributeException {
    try {
      return Base64Util.decode(value);
    } catch (Throwable e) {
      throw new AttributeException(
          String.format("Failed to retrieve the binary value for the attribute (%s)", name));
    }
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>BigDecimal</code> value for the <code>Attribute</code> instance
   */
  public BigDecimal getDecimalValue() throws AttributeException {
    try {
      return new BigDecimal(value);
    } catch (Throwable e) {
      throw new AttributeException(
          String.format("Failed to retrieve the decimal value for the attribute (%s)", name));
    }
  }

  /**
   * Returns the <code>double</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>double</code> value for the <code>Attribute</code> instance
   */
  public double getDoubleValue() throws AttributeException {
    try {
      return Double.parseDouble(value);
    } catch (Throwable e) {
      throw new AttributeException(
          String.format("Failed to retrieve the double value for the attribute (%s)", name));
    }
  }

  /**
   * Returns the <code>int</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>int</code> value for the <code>Attribute</code> instance
   */
  public int getIntegerValue() throws AttributeException {
    try {
      return Integer.parseInt(value);
    } catch (Throwable e) {
      throw new AttributeException(
          String.format("Failed to retrieve the integer value for the attribute (%s)", name));
    }
  }

  /**
   * Returns the <code>long</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>long</code> value for the <code>Attribute</code> instance
   */
  public long getLongValue() throws AttributeException {
    try {
      return Long.parseLong(value);
    } catch (Throwable e) {
      throw new AttributeException(
          String.format("Failed to retrieve the long value for the attribute (%s)", name));
    }
  }

  /**
   * Returns the name for the <code>Attribute</code> instance.
   *
   * @return the name for the <code>Attribute</code> instance
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the <code>String</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>String</code> value for the <code>Attribute</code> instance
   */
  public String getStringValue() {
    return value;
  }

  /**
   * Returns the <code>String</code> value for the <code>Attribute</code> instance.
   *
   * @return the <code>String</code> value for the <code>Attribute</code> instance
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the binary value for the attribute.
   *
   * @param value the binary value for the attribute
   */
  public void setBinaryValue(BinaryBuffer value) {
    this.value = Base64Util.encodeBytes(value.getData());
  }

  /**
   * Set the binary value for the attribute.
   *
   * @param value the binary value for the attribute
   */
  public void setBinaryValue(byte[] value) {
    this.value = Base64Util.encodeBytes(value);
  }

  /**
   * Set the <code>BigDecimal</code> value for the attribute.
   *
   * @param value the <code>BigDecimal</code> value for the attribute
   */
  public void setDecimalValue(BigDecimal value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>double</code> value for the attribute.
   *
   * @param value the <code>double</code> value for the attribute
   */
  public void setDoubleValue(double value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>int</code> value for the attribute.
   *
   * @param value the <code>int</code> value for the attribute
   */
  public void setIntegerValue(int value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>long</code> value for the attribute.
   *
   * @param value the <code>long</code> value for the attribute
   */
  public void setLongValue(long value) {
    this.value = String.valueOf(value);
  }

  /**
   * Set the name for the attribute.
   *
   * @param name the name for the attribute
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the <code>String</code> value for the attribute.
   *
   * @param value the <code>String</code> value for the attribute
   */
  public void setStringValue(String value) {
    this.value = value;
  }
}
