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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Organization</code> class holds the information for an organization.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name"})
@XmlRootElement(name = "Organization", namespace = "http://party.inception.digital")
@XmlType(
    name = "Organization",
    namespace = "http://party.inception.digital",
    propOrder = {})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "organizations")
public class Organization extends Party {

  /** Constructs a new <code>Organization</code>. */
  public Organization() {
    super(PartyType.ORGANIZATION);
  }
}
