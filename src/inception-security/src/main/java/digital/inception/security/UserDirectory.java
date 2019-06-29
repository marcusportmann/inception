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

import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.springframework.util.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The <code>UserDirectory</code> class holds the information for a user directory.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "UserDirectory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "typeId", "name", "parameters" })
@XmlRootElement(name = "UserDirectory", namespace = "http://security.inception.digital")
@XmlType(name = "UserDirectory", namespace = "http://security.inception.digital",
    propOrder = { "id", "typeId", "name", "parameters" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class UserDirectory
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The parameters for the user directory.
   */
  @ApiModelProperty(value = "The parameters for the user directory", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Parameters", required = true)
  @XmlElement(name = "Parameter", required = true)
  @Valid
  private List<UserDirectoryParameter> parameters = new ArrayList<>();

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /**
   * The name of the user directory.
   */
  @ApiModelProperty(value = "The name of the user directory", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 4000)
  private String name;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the user directory type.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the user directory type",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TypeId", required = true)
  @NotNull
  private UUID typeId;

  /**
   * Constructs a new <code>UserDirectory</code>.
   */
  public UserDirectory() {}

  /**
   * Returns the XML configuration data for the user directory.
   *
   * @return the XML configuration data for the user directory
   */
  public String getConfiguration()
  {
    StringBuilder buffer = new StringBuilder();

    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    buffer.append(
        "<!DOCTYPE userDirectory SYSTEM \"UserDirectoryConfiguration.dtd\"><userDirectory>");

    for (UserDirectoryParameter parameter : parameters)
    {
      buffer.append("<parameter>");
      buffer.append("<name>").append(parameter.getName()).append("</name>");
      buffer.append("<value>").append(StringUtils.isEmpty(parameter.getValue())
          ? ""
          : parameter.getValue()).append("</value>");
      buffer.append("</parameter>");
    }

    buffer.append("</userDirectory>");

    return buffer.toString();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the user directory.
   *
   * @return the name of the user directory
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the parameters for the user directory.
   *
   * @return the parameters for the user directory
   */
  public List<UserDirectoryParameter> getParameters()
  {
    return parameters;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * type.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   *         type
   */
  public UUID getTypeId()
  {
    return typeId;
  }

  /**
   * Set the XML configuration data for the user directory.
   *
   * @param configuration the XML configuration data for the user directory
   */
  public void setConfiguration(String configuration)
    throws SecurityServiceException
  {
    try
    {
      // Parse the XML configuration data
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

      builderFactory.setValidating(true);

      DocumentBuilder builder = builderFactory.newDocumentBuilder();

      builder.setEntityResolver(new DtdJarResolver("UserDirectoryConfiguration.dtd",
          "META-INF/UserDirectoryConfiguration.dtd"));
      builder.setErrorHandler(new XmlParserErrorHandler());

      InputSource inputSource = new InputSource(new ByteArrayInputStream(configuration.getBytes()));
      Document document = builder.parse(inputSource);

      Element rootElement = document.getDocumentElement();

      // Read the user directory parameters configuration
      parameters = new ArrayList<>();

      NodeList parameterElements = rootElement.getElementsByTagName("parameter");

      for (int i = 0; i < parameterElements.getLength(); i++)
      {
        Element parameterElement = (Element) parameterElements.item(i);

        parameters.add(new UserDirectoryParameter(XmlUtil.getChildElementText(parameterElement,
            "name"), XmlUtil.getChildElementText(parameterElement, "value")));
      }
    }
    catch (Throwable e)
    {
      throw new SecurityServiceException(
          "Failed to parse the XML configuration data for the user directory", e);
    }
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the user directory.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the user directory.
   *
   * @param name the name of the user directory
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the parameters for the user directory.
   *
   * @param parameters the parameters for the user directory
   */
  public void setParameters(List<UserDirectoryParameter> parameters)
  {
    this.parameters = parameters;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the user directory type.
   *
   * @param typeId the Universally Unique Identifier (UUID) used to uniquely identify the user
   *               directory type
   */
  public void setTypeId(UUID typeId)
  {
    this.typeId = typeId;
  }
}
