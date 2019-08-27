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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>UserDirectoryType</code> class holds the information for a user directory type.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "UserDirectoryType")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "code", "name", "userDirectoryClassName" })
@XmlRootElement(name = "UserDirectoryType", namespace = "http://security.inception.digital")
@XmlType(name = "UserDirectoryType", namespace = "http://security.inception.digital",
    propOrder = { "code", "name", "userDirectoryClassName" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class UserDirectoryType
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The code used to uniquely identify the user directory type.
   */
  @ApiModelProperty(value = "The code used to uniquely identify the user directory type",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String code;

  /**
   * The name of the user directory type.
   */
  @ApiModelProperty(value = "The name of the user directory type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String name;

  /**
   * The Java class that implements the user directory type.
   */
  private transient Class userDirectoryClass;

  /**
   * The fully qualified name of the Java class that implements the user directory type.
   */
  @ApiModelProperty(
      value = "The fully qualified name of the Java class that implements the user directory type",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryClassName", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String userDirectoryClassName;

  /**
   * Constructs a new <code>UserDirectoryType</code>.
   */
  public UserDirectoryType() {}

  /**
   * Constructs a new <code>UserDirectoryType</code>.
   *
   * @param code                    the code used to uniquely identify the user directory type
   * @param name                    the name of the user directory type
   * @param userDirectoryClassName  the fully qualified name of the Java class that implements the
   *                                user directory type
   */
  UserDirectoryType(String code, String name, String userDirectoryClassName)
  {
    this.code = code;
    this.name = name;
    this.userDirectoryClassName = userDirectoryClassName;
  }

  /**
   * Returns the code used to uniquely identify the user directory type.
   *
   * @return the code used to uniquely identify the user directory type
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the name of the user directory type.
   *
   * @return the name of the user directory type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the Java class that implements the user directory type.
   *
   * @return the Java class that implements the user directory type
   */
  Class getUserDirectoryClass()
    throws SecurityServiceException
  {
    if (userDirectoryClass == null)
    {
      try
      {
        userDirectoryClass = Thread.currentThread().getContextClassLoader().loadClass(
            userDirectoryClassName);

        if (!IUserDirectory.class.isAssignableFrom(userDirectoryClass))
        {
          throw new SecurityServiceException(
              "The user directory class does not implement the IUserDirectory interface");
        }
      }
      catch (Throwable e)
      {
        throw new SecurityServiceException(String.format(
            "Failed to load the user directory class (%s) for the user directory (%s)",
            userDirectoryClassName, name), e);
      }
    }

    return userDirectoryClass;
  }

  /**
   * Returns the fully qualified name of the Java class that implements the user directory type.
   *
   * @return the fully qualified name of the Java class that implements the user directory type
   */
  String getUserDirectoryClassName()
  {
    return userDirectoryClassName;
  }
}
