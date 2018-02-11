/*
 * Copyright 2018 Marcus Portmann
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

package digital.inception.messaging.messages;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.StringUtil;
import digital.inception.core.wbxml.Element;
import digital.inception.security.Organisation;

import java.io.Serializable;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>OrganisationData</code> class stores the information for an organisation.
 *
 * @author Marcus Portmann
 */
public class OrganisationData
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   */
  private UUID id;

  /**
   * The name of the organisation.
   */
  private String name;

  /**
   * Constructs a new <code>OrganisationData</code>.
   *
   * @param element the WBXML element containing the organisation data
   */
  OrganisationData(Element element)
  {
    try
    {
      this.id = UUID.fromString(element.getChildText("Id"));
      this.name = StringUtil.notNull(element.getChildText("Name"));
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to extract the organisation data from the WBXML", e);
    }
  }

  /**
   * Constructs a new <code>OrganisationData</code>.
   *
   * @param organisation the <code>Organisation</code> instance containing the organisation data
   */
  OrganisationData(Organisation organisation)
  {
    this.id = organisation.getId();
    this.name = organisation.getName();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the organisation.
   *
   * @return the name of the organisation
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the organisation.
   *
   * @param name the name of the organisation
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Returns the WBXML element containing the organisation data.
   *
   * @return the WBXML element containing the organisation data
   */
  Element toElement()
  {
    Element organisationElement = new Element("Organisation");

    organisationElement.addContent(new Element("Id", id.toString()));
    organisationElement.addContent(new Element("Name", StringUtil.notNull(name)));

    return organisationElement;
  }
}
