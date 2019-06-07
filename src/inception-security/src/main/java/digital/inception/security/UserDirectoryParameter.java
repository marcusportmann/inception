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

package digital.inception.security;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import digital.inception.core.util.Base64Util;
import digital.inception.core.util.BinaryBuffer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>UserDirectoryParameter</code> class stores a configuration parameter for a user
 * directory as a name-value pair.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "UserDirectoryParameter")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "value" })
@XmlRootElement(name = "UserDirectoryParameter", namespace = "http://security.inception.digital")
@XmlType(name = "UserDirectoryParameter", namespace = "http://security.inception.digital",
    propOrder = { "name", "value" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class UserDirectoryParameter
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The name for the user directory parameter.
   */
  @ApiModelProperty(value = "The name for the user directory parameter", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String name;

  /**
   * The value for the user directory parameter.
   */
  @ApiModelProperty(value = "The value for the user directory parameter", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(max = 4000)
  private String value;

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   */
  public UserDirectoryParameter() {}

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name  the name for the user directory parameter
   * @param value the <code>BigDecimal</code> value for the user directory parameter
   */
  public UserDirectoryParameter(String name, BigDecimal value)
  {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name  the name for the user directory parameter
   * @param value the binary value for the user directory parameter
   */
  public UserDirectoryParameter(String name, BinaryBuffer value)
  {
    this.name = name;
    this.value = Base64Util.encodeBytes(value.getData());
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name  the name for the user directory parameter
   * @param value the binary value for the user directory parameter
   */
  public UserDirectoryParameter(String name, byte[] value)
  {
    this.name = name;
    this.value = Base64Util.encodeBytes(value);
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name  the name for the user directory parameter
   * @param value the <code>double</code> value for the user directory parameter
   */
  public UserDirectoryParameter(String name, double value)
  {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name  the name for the user directory parameter
   * @param value the <code>long</code> value for the user directory parameter
   */
  public UserDirectoryParameter(String name, long value)
  {
    this.name = name;
    this.value = String.valueOf(value);
  }

  /**
   * Constructs a new <code>UserDirectoryParameter</code>.
   *
   * @param name  the name for the user directory parameter
   * @param value the <code>String</code> value for the user directory parameter
   */
  public UserDirectoryParameter(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Checks whether the list contains a <code>UserDirectoryParameter</code> instance with the
   * specified name.
   *
   * @param list the <code>UserDirectoryParameter</code> instances to search for the
   *             <code>UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   *
   * @return <code>true</code> if the list contains a <code>UserDirectoryParameter</code> instance
   *         with the specified name or <code>false</code> otherwise
   */
  public static boolean contains(List<UserDirectoryParameter> list, String name)
  {
    return list.stream().anyMatch(userDirectoryParameter -> userDirectoryParameter.getName()
        .equalsIgnoreCase(name));
  }

  /**
   * Returns the binary value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param list the <code>UserDirectoryParameter</code> instances to search for the
   *             <code>UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   *
   * @return the binary value for the <code>UserDirectoryParameter</code> instance with the
   *         specified name in the specified list
   */
  public static byte[] getBinaryValue(List<UserDirectoryParameter> list, String name)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        try
        {
          return Base64Util.decode(attribute.value);
        }
        catch (Throwable e)
        {
          throw new UserDirectoryParameterException(String.format(
              "Failed to retrieve the binary value for the user directory parameter (%s)", attribute
              .name));
        }
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to retrieve the binary value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Returns the <code>boolean</code> value for the <code>UserDirectoryParameter</code> instance
   * with the specified name in the specified list.
   *
   * @param list the <code>UserDirectoryParameter</code> instances to search for the
   *             <code>UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   *
   * @return the <code>boolean</code> value for the <code>UserDirectoryParameter</code> instance
   *         with the specified name in the specified list
   */
  public static boolean getBooleanValue(List<UserDirectoryParameter> list, String name)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        try
        {
          return Boolean.parseBoolean(attribute.value);
        }
        catch (Throwable e)
        {
          throw new UserDirectoryParameterException(String.format(
              "Failed to retrieve the boolean value for the user directory parameter (%s)",
              attribute.name));
        }
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to retrieve the boolean value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance
   * with the specified name in the specified list.
   *
   * @param list the <code>UserDirectoryParameter</code> instances to search for the
   *             <code>UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   *
   * @return the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance
   *         with the specified name in the specified list
   */
  public static BigDecimal getDecimalValue(List<UserDirectoryParameter> list, String name)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        try
        {
          return new BigDecimal(attribute.value);
        }
        catch (Throwable e)
        {
          throw new UserDirectoryParameterException(String.format(
              "Failed to retrieve the decimal value for the user directory parameter (%s)",
              attribute.name));
        }
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to retrieve the decimal value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Returns the <code>double</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param list the <code>UserDirectoryParameter</code> instances to search for the
   *             <code>UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   *
   * @return the <code>double</code> value for the <code>UserDirectoryParameter</code> instance with
   *         the specified name in the specified list
   */
  public static double getDoubleValue(List<UserDirectoryParameter> list, String name)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        try
        {
          return Double.parseDouble(attribute.value);
        }
        catch (Throwable e)
        {
          throw new UserDirectoryParameterException(String.format(
              "Failed to retrieve the double value for the user directory parameter (%s)", attribute
              .name));
        }
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to retrieve the double value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Returns the <code>int</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param list the <code>UserDirectoryParameter</code> instances to search for the
   *             <code>UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   *
   * @return the <code>int</code> value for the <code>UserDirectoryParameter</code> instance with
   *         the specified name in the specified list
   */
  public static int getIntegerValue(List<UserDirectoryParameter> list, String name)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        try
        {
          return Integer.parseInt(attribute.value);
        }
        catch (Throwable e)
        {
          throw new UserDirectoryParameterException(String.format(
              "Failed to retrieve the integer value for the user directory parameter (%s)",
              attribute.name));
        }
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to retrieve the integer value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Returns the <code>long</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param list the <code>UserDirectoryParameter</code> instances to search for the
   *             <code>UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   *
   * @return the <code>long</code> value for the <code>UserDirectoryParameter</code> instance with
   *         the specified name in the specified list
   */
  public static long getLongValue(List<UserDirectoryParameter> list, String name)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        try
        {
          return Long.parseLong(attribute.value);
        }
        catch (Throwable e)
        {
          throw new UserDirectoryParameterException(String.format(
              "Failed to retrieve the long value for the user directory parameter (%s)", attribute
              .name));
        }
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to retrieve the long value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Returns the <code>String</code> value for the <code>UserDirectoryParameter</code> instance with
   * the* specified name in the specified list.
   *
   * @param list the <code>UserDirectoryParameter</code> instances to search for the
   *             <code>UserDirectoryParameter</code> with the specified name
   * @param name the name for the user directory parameter
   *
   * @return the <code>String</code> value for the <code>UserDirectoryParameter</code> instance with
   *         the specified name in the specified list
   */
  public static String getStringValue(List<UserDirectoryParameter> list, String name)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        return attribute.value;
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to retrieve the string value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Set the binary value for the <code>UserDirectoryParameter</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the <code>UserDirectoryParameter</code> instances to search for the
   *              <code>UserDirectoryParameter</code> with the specified name
   * @param name  the name for the user directory parameter
   * @param value the binary value for the user directory parameter
   */
  public static void setBinaryValue(List<UserDirectoryParameter> list, String name,
      BinaryBuffer value)
    throws UserDirectoryParameterException
  {
    setBinaryValue(list, name, value.getData());
  }

  /**
   * Set the binary value for the <code>UserDirectoryParameter</code> instance with the specified
   * name in the specified list.
   *
   * @param list  the <code>UserDirectoryParameter</code> instances to search for the
   *              <code>UserDirectoryParameter</code> with the specified name
   * @param name  the name for the user directory parameter
   * @param value the binary value for the user directory parameter
   */
  public static void setBinaryValue(List<UserDirectoryParameter> list, String name, byte[] value)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setBinaryValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to set the binary value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Set the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance with
   * the specified name in the specified list.
   *
   * @param list  the <code>UserDirectoryParameter</code> instances to search for the
   *              <code>UserDirectoryParameter</code> with the specified name
   * @param name  the name for the user directory parameter
   * @param value the <code>BigDecimal</code> value for the user directory parameter
   */
  public static void setDecimalValue(List<UserDirectoryParameter> list, String name,
      BigDecimal value)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setDecimalValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to set the decimal value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Set the <code>double</code> value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param list  the <code>UserDirectoryParameter</code> instances to search for the
   *              <code>UserDirectoryParameter</code> with the specified name
   * @param name  the name for the user directory parameter
   * @param value the <code>double</code> value for the user directory parameter
   */
  public static void setDoubleValue(List<UserDirectoryParameter> list, String name, double value)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setDoubleValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to set the double value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Set the <code>long</code> value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param list  the <code>UserDirectoryParameter</code> instances to search for the
   *              <code>UserDirectoryParameter</code> with the specified name
   * @param name  the name for the user directory parameter
   * @param value the <code>long</code> value for the user directory parameter
   */
  public static void setLongValue(List<UserDirectoryParameter> list, String name, long value)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setLongValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to set the long value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Set the <code>String</code> value for the <code>UserDirectoryParameter</code> instance with the
   * specified name in the specified list.
   *
   * @param list  the <code>UserDirectoryParameter</code> instances to search for the
   *              <code>UserDirectoryParameter</code> with the specified name
   * @param name  the name for the user directory parameter
   * @param value the <code>String</code> value for the user directory parameter
   */
  public static void setStringValue(List<UserDirectoryParameter> list, String name, String value)
    throws UserDirectoryParameterException
  {
    for (UserDirectoryParameter attribute : list)
    {
      if (attribute.name.equalsIgnoreCase(name))
      {
        attribute.setStringValue(value);

        return;
      }
    }

    throw new UserDirectoryParameterException(String.format(
        "Failed to set the string value for the user directory parameter (%s): "
        + "The attribute could not be found", name));
  }

  /**
   * Returns the binary value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the binary value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  @XmlTransient
  public byte[] getBinaryValue()
    throws UserDirectoryParameterException
  {
    try
    {
      return Base64Util.decode(value);
    }
    catch (Throwable e)
    {
      throw new UserDirectoryParameterException(String.format(
          "Failed to retrieve the binary value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>BigDecimal</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  @XmlTransient
  public BigDecimal getDecimalValue()
    throws UserDirectoryParameterException
  {
    try
    {
      return new BigDecimal(value);
    }
    catch (Throwable e)
    {
      throw new UserDirectoryParameterException(String.format(
          "Failed to retrieve the decimal value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the <code>double</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>double</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  @XmlTransient
  public double getDoubleValue()
    throws UserDirectoryParameterException
  {
    try
    {
      return Double.parseDouble(value);
    }
    catch (Throwable e)
    {
      throw new UserDirectoryParameterException(String.format(
          "Failed to retrieve the double value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the <code>long</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>long</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  @XmlTransient
  public long getLongValue()
    throws UserDirectoryParameterException
  {
    try
    {
      return Long.parseLong(value);
    }
    catch (Throwable e)
    {
      throw new UserDirectoryParameterException(String.format(
          "Failed to retrieve the long value for the user directory parameter (%s)", name));
    }
  }

  /**
   * Returns the name for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the name for the <code>UserDirectoryParameter</code> instance
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the <code>String</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>String</code> value for the <code>UserDirectoryParameter</code> instance
   */
  @JsonIgnore
  @XmlTransient
  public String getStringValue()
  {
    return value;
  }

  /**
   * Returns the <code>String</code> value for the <code>UserDirectoryParameter</code> instance.
   *
   * @return the <code>String</code> value for the <code>UserDirectoryParameter</code> instance
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the binary value for the user directory parameter.
   *
   * @param value the binary value for the user directory parameter
   */
  public void setBinaryValue(BinaryBuffer value)
  {
    this.value = Base64Util.encodeBytes(value.getData());
  }

  /**
   * Set the binary value for the user directory parameter.
   *
   * @param value the binary value for the user directory parameter
   */
  public void setBinaryValue(byte[] value)
  {
    this.value = Base64Util.encodeBytes(value);
  }

  /**
   * Set the <code>BigDecimal</code> value for the user directory parameter.
   *
   * @param value the <code>BigDecimal</code> value for the user directory parameter
   */
  public void setDecimalValue(BigDecimal value)
  {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>double</code> value for the user directory parameter.
   *
   * @param value the <code>double</code> value for the user directory parameter
   */
  public void setDoubleValue(double value)
  {
    this.value = String.valueOf(value);
  }

  /**
   * Set the <code>Long</code> value for the user directory parameter.
   *
   * @param value the <code>long</code> value for the user directory parameter
   */
  public void setLongValue(long value)
  {
    this.value = String.valueOf(value);
  }

  /**
   * Set the name for the user directory parameter.
   *
   * @param name the name for the user directory parameter
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the <code>String</code> value for the user directory parameter.
   *
   * @param value the <code>String</code> value for the user directory parameter
   */
  public void setStringValue(String value)
  {
    this.value = value;
  }
}
