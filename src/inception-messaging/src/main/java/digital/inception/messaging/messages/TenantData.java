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
import digital.inception.security.Tenant;
import java.io.Serializable;
import java.util.UUID;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TenantData</code> class holds the information for a tenant.
 *
 * @author Marcus Portmann
 */
public class TenantData implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The Universally Unique Identifier (UUID) uniquely identifying the tenant. */
  private UUID id;

  /** The name of the tenant. */
  private String name;

  /**
   * Constructs a new <code>TenantData</code>.
   *
   * @param element the WBXML element containing the tenant data
   */
  TenantData(Element element) {
    try {
      this.id = UUID.fromString(element.getChildText("Id"));
      this.name =
          StringUtils.hasText(element.getChildText("Name")) ? element.getChildText("Name") : "";
    } catch (Throwable e) {
      throw new RuntimeException("Failed to extract the tenant data from the WBXML", e);
    }
  }

  /**
   * Constructs a new <code>TenantData</code>.
   *
   * @param tenant the <code>Tenant</code> instance containing the tenant data
   */
  TenantData(Tenant tenant) {
    this.id = tenant.getId();
    this.name = tenant.getName();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the tenant.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the tenant
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the tenant.
   *
   * @return the name of the tenant
   */
  public String getName() {
    return name;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the tenant.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the tenant
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the tenant.
   *
   * @param name the name of the tenant
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the WBXML element containing the tenant data.
   *
   * @return the WBXML element containing the tenant data
   */
  Element toElement() {
    Element tenantElement = new Element("Tenant");

    tenantElement.addContent(new Element("Id", id.toString()));
    tenantElement.addContent(new Element("Name", StringUtils.hasText(name) ? name : ""));

    return tenantElement;
  }
}
