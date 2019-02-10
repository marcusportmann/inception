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

import digital.inception.core.util.StringUtil;
import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserDirectory</code> class stores the information for a user directory.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public class UserDirectory
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private Map<String, String> parameters = new HashMap<>();
  private UUID id;
  private String name;
  private UserDirectoryType type;
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

    for (String parameterName : parameters.keySet())
    {
      buffer.append("<parameter>");
      buffer.append("<name>").append(parameterName).append("</name>");
      buffer.append("<value>").append(StringUtil.notNull(parameters.get(parameterName))).append(
          "</value>");
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
  public Map<String, String> getParameters()
  {
    return parameters;
  }

  /**
   * Returns the user directory type.
   *
   * @return the user directory type
   */
  public UserDirectoryType getType()
  {
    return type;
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
      parameters = new HashMap<>();

      NodeList parameterElements = rootElement.getElementsByTagName("parameter");

      for (int i = 0; i < parameterElements.getLength(); i++)
      {
        Element parameterElement = (Element) parameterElements.item(i);

        parameters.put(XmlUtil.getChildElementText(parameterElement, "name"),
            XmlUtil.getChildElementText(parameterElement, "value"));
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
  public void setParameters(Map<String, String> parameters)
  {
    this.parameters = parameters;
  }

  /**
   * Set the user directory type.
   *
   * @param type the user directory type
   */
  public void setType(UserDirectoryType type)
  {
    this.type = type;
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
