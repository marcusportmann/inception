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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Organization</code> class holds the information for an organization, which is an
 * organised group of people with a particular purpose, such as a business or government department.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "An organised group of people with a particular purpose, such as a business or government department")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"type"})
@JsonPropertyOrder({"id", "name", "contactMechanisms", "physicalAddresses"})
@XmlRootElement(name = "Organization", namespace = "http://party.inception.digital")
@XmlType(
    name = "Organization",
    namespace = "http://party.inception.digital",
    propOrder = {"id", "name", "contactMechanisms", "physicalAddresses"})
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@DiscriminatorValue("1")
@Table(schema = "party", name = "organizations")
public class Organization extends Party implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** Constructs a new <code>Organization</code>. */
  public Organization() {
    super(PartyType.ORGANIZATION);
  }

  /**
   * Add the contact mechanism for the organization.
   *
   * @param contactMechanism the contact mechanism
   */
  @Override
  public void addContactMechanism(ContactMechanism contactMechanism) {
    super.addContactMechanism(contactMechanism);
  }

  /**
   * Add the physical address for the organization.
   *
   * @param physicalAddress the physical address
   */
  @Override
  public void addPhysicalAddress(PhysicalAddress physicalAddress) {
    super.addPhysicalAddress(physicalAddress);
  }

  /**
   * Returns the contact mechanisms for the organization.
   *
   * @return the contact mechanisms for the organization
   */
  @Schema(description = "The contact mechanisms for the organization")
  @JsonProperty
  @JsonManagedReference("contactMechanismReference")
  @XmlElementWrapper(name = "ContactMechanisms")
  @XmlElement(name = "ContactMechanism")
  @Override
  public Set<ContactMechanism> getContactMechanisms() {
    return super.getContactMechanisms();
  }

  /**
   * Returns the date and time the organization was created.
   *
   * @return the date and time the organization was created
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the organization.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the organization
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the organization")
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the name of the organization.
   *
   * @return the name of the organization
   */
  @Schema(description = "The name of the organization")
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the physical addresses for the organization.
   *
   * @return the physical addresses for the organization
   */
  @Schema(description = "The physical addresses for the organization")
  @JsonProperty
  @JsonManagedReference
  @XmlElementWrapper(name = "PhysicalAddresses")
  @XmlElement(name = "PhysicalAddress")
  @Override
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return super.getPhysicalAddresses();
  }

  /**
   * Returns the type of party for the organization.
   *
   * @return the type of party for the organization
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public PartyType getType() {
    return super.getType();
  }

  /**
   * Returns the date and time the organization was last updated.
   *
   * @return the date and time the organization was last updated
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getUpdated() {
    return super.getUpdated();
  }

  /**
   * Remove the contact mechanism with the specified type and purpose for the organization.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   */
  @Override
  public void removeContactMechanism(ContactMechanismType type, ContactMechanismPurpose purpose) {
    super.removeContactMechanism(type, purpose);
  }

  /**
   * Remove the physical address with the specified type and purpose for the organization.
   *
   * @param type the physical address type
   * @param purpose the physical address purpose
   */
  @Override
  public void removePhysicalAddress(PhysicalAddressType type, PhysicalAddressPurpose purpose) {
    super.removePhysicalAddress(type, purpose);
  }

  /**
   * Set the contact mechanisms for the organization.
   *
   * @param contactMechanisms the contact mechanisms for the organization
   */
  @Override
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    super.setContactMechanisms(contactMechanisms);
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the organization.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the organization
   */
  @Override
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the name of the organization.
   *
   * @param name the name of the organization
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the physical addresses for the organization.
   *
   * @param physicalAddresses the physical addresses for the organization
   */
  @Override
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    super.setPhysicalAddresses(physicalAddresses);
  }
}
