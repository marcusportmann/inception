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

package digital.inception.messaging.messages;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.wbxml.Element;
import digital.inception.security.Organization;
import java.io.Serializable;
import java.util.UUID;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>OrganizationData</code> class holds the information for an organization.
 *
 * @author Marcus Portmann
 */
public class OrganizationData implements Serializable {

  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the organization.
   */
  private UUID id;

  /**
   * The name of the organization.
   */
  private String name;

  /**
   * Constructs a new <code>OrganizationData</code>.
   *
   * @param element the WBXML element containing the organization data
   */
  OrganizationData(Element element) {
    try {
      this.id = UUID.fromString(element.getChildText("Id"));
      this.name =
          StringUtils.isEmpty(element.getChildText("Name")) ? "" : element.getChildText("Name");
    } catch (Throwable e) {
      throw new RuntimeException("Failed to extract the organization data from the WBXML", e);
    }
  }

  /**
   * Constructs a new <code>OrganizationData</code>.
   *
   * @param organization the <code>Organization</code> instance containing the organization data
   */
  OrganizationData(Organization organization) {
    this.id = organization.getId();
    this.name = organization.getName();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the organization.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the organization
   */
  public UUID getId() {
    return id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the organization.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the organization
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the name of the organization.
   *
   * @return the name of the organization
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the organization.
   *
   * @param name the name of the organization
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the WBXML element containing the organization data.
   *
   * @return the WBXML element containing the organization data
   */
  Element toElement() {
    Element organizationElement = new Element("Organization");

    organizationElement.addContent(new Element("Id", id.toString()));
    organizationElement.addContent(new Element("Name", StringUtils.isEmpty(name) ? "" : name));

    return organizationElement;
  }
}
