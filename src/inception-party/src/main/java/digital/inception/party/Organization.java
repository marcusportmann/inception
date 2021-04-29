/*
 * Copyright 2021 Marcus Portmann
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
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.party.constraints.ValidOrganization;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.springframework.util.StringUtils;

/**
 * The <b>Organization</b> class holds the information for an organization, which is an organised
 * group of people with a particular purpose, such as a business or government department.
 *
 * <p>This class exposes the JSON and XML properties using a property-based approach rather than a
 * field-based approach to support the JPA inheritance model.
 *
 * <p>The following steps must be completed when adding a new attribute to the organization entity:
 *
 * <ol>
 *   <li>Add a new column for the new attribute to the definition of the <b>party.organizations</b>
 *       table in the <b>inception-party-h2.sql</b> file.
 *   <li>Add a description for the new column for the new attribute to the
 *       <b>inception-party-h2.sql</b> file.
 *   <li>Add a new property for the new attribute to the <b>Organization</b> class.
 *   <li>Add the appropriate validation for the new attribute to the
 *       <b>ValidOrganizationValidator</b> class.
 *   <li>Add a new column for the new attribute to the <b>inception-party.changelog.xml</b> file.
 *   <li>Add the name of the attribute to the <b>Attribute.RESERVED_ATTRIBUTE_TYPE_CODES</b> array.
 *   <li>Add support for applying validations described by <b>RoleTypeAttributeTypeConstraint</b>s
 *       to the <b>ValidOrganizationValidator</b>.
 * </ol>
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "An organised group of people with a particular purpose, such as a business or government department")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"created", "type", "updated"})
@JsonPropertyOrder({
  "id",
  "tenantId",
  "name",
  "attributes",
  "contactMechanisms",
  "externalReferences",
  "identityDocuments",
  "locks",
  "physicalAddresses",
  "preferences",
  "roles",
  "segmentAllocations",
  "statuses",
  "countriesOfTaxResidence",
  "taxNumbers",
})
@XmlRootElement(name = "Organization", namespace = "http://inception.digital/party")
@XmlType(
    name = "Organization",
    namespace = "http://inception.digital/party",
    propOrder = {
      "id",
      "tenantId",
      "name",
      "attributes",
      "contactMechanisms",
      "externalReferences",
      "identityDocuments",
      "locks",
      "physicalAddresses",
      "preferences",
      "roles",
      "segmentAllocations",
      "statuses",
      "countriesOfTaxResidence",
      "taxNumbers"
    })
@XmlAccessorType(XmlAccessType.PROPERTY)
@ValidOrganization
@Entity
@Table(schema = "party", name = "organizations")
public class Organization extends PartyBase implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The attributes for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Attribute> attributes = new HashSet<>();

  /** The contact mechanisms for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<ContactMechanism> contactMechanisms = new HashSet<>();

  /** The external references for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<ExternalReference> externalReferences = new HashSet<>();

  /** The identity documents for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<IdentityDocument> identityDocuments = new HashSet<>();

  /** The locks applied to the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Lock> locks = new HashSet<>();

  /** The physical addresses for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<PhysicalAddress> physicalAddresses = new HashSet<>();

  /** The preferences for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Preference> preferences = new HashSet<>();

  /** The roles assigned directly to the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Role> roles = new HashSet<>();

  /** The segment allocations for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<SegmentAllocation> segmentAllocations = new HashSet<>();

  /** The statuses assigned to the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Status> statuses = new HashSet<>();

  /** The tax numbers for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<TaxNumber> taxNumbers = new HashSet<>();

  /**
   * The comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for the
   * organization.
   */
  @JsonIgnore
  @XmlTransient
  @Size(max = 30)
  @Column(table = "organizations", name = "countries_of_tax_residence", length = 30)
  private String countriesOfTaxResidence;

  /** Constructs a new <b>Organization</b>. */
  public Organization() {
    super(PartyType.ORGANIZATION);
  }

  /**
   * Constructs a new <b>Organization</b>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the organization is
   *     associated with
   * @param name the name of the organization
   */
  public Organization(UUID tenantId, String name) {
    super(UuidCreator.getShortPrefixComb(), tenantId, PartyType.ORGANIZATION, name);
  }

  /**
   * Add the attribute for the organization.
   *
   * @param attribute the attribute
   */
  public void addAttribute(Attribute attribute) {
    attributes.removeIf(
        existingAttribute -> Objects.equals(existingAttribute.getType(), attribute.getType()));

    attribute.setParty(this);

    attributes.add(attribute);
  }

  /**
   * Add the contact mechanism for the organization.
   *
   * @param contactMechanism the contact mechanism
   */
  public void addContactMechanism(ContactMechanism contactMechanism) {
    contactMechanisms.removeIf(
        existingContactMechanism ->
            Objects.equals(existingContactMechanism.getType(), contactMechanism.getType())
                && Objects.equals(existingContactMechanism.getRole(), contactMechanism.getRole()));

    contactMechanism.setParty(this);

    contactMechanisms.add(contactMechanism);
  }

  /**
   * Add the external reference for the organization.
   *
   * @param externalReference the external reference
   */
  public void addExternalReference(ExternalReference externalReference) {
    externalReferences.removeIf(
        existingExternalReference ->
            Objects.equals(existingExternalReference.getId(), externalReference.getId()));

    externalReference.setParty(this);

    externalReferences.add(externalReference);
  }

  /**
   * Add the identity document for the organization.
   *
   * @param identityDocument the identity document
   */
  public void addIdentityDocument(IdentityDocument identityDocument) {
    identityDocuments.removeIf(
        existingIdentityDocument ->
            Objects.equals(existingIdentityDocument.getId(), identityDocument.getId()));

    identityDocuments.removeIf(
        existingIdentityDocument ->
            Objects.equals(existingIdentityDocument.getType(), identityDocument.getType())
                && Objects.equals(
                    existingIdentityDocument.getCountryOfIssue(),
                    identityDocument.getCountryOfIssue())
                && Objects.equals(
                    existingIdentityDocument.getDateOfIssue(), identityDocument.getDateOfIssue()));

    identityDocument.setParty(this);

    identityDocuments.add(identityDocument);
  }

  /**
   * Apply the lock to the organization.
   *
   * @param lock the lock
   */
  public void addLock(Lock lock) {
    locks.removeIf(existingLock -> Objects.equals(existingLock.getType(), lock.getType()));

    lock.setParty(this);

    locks.add(lock);
  }

  /**
   * Add the physical address for the organization.
   *
   * @param physicalAddress the physical address
   */
  public void addPhysicalAddress(PhysicalAddress physicalAddress) {
    physicalAddresses.removeIf(
        existingPhysicalAddress ->
            Objects.equals(existingPhysicalAddress.getId(), physicalAddress.getId()));

    physicalAddress.setParty(this);

    physicalAddresses.add(physicalAddress);
  }

  /**
   * Add the preference for the organization.
   *
   * @param preference the preference
   */
  public void addPreference(Preference preference) {
    preferences.removeIf(
        existingPreference -> Objects.equals(existingPreference.getType(), preference.getType()));

    preference.setParty(this);

    preferences.add(preference);
  }

  /**
   * Add the role to the organization independent of a party association.
   *
   * @param role the role
   */
  public void addRole(Role role) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), role.getType()));

    role.setParty(this);

    roles.add(role);
  }

  /**
   * Add the segment allocation to the organization.
   *
   * @param segmentAllocation the segment organization
   */
  public void addSegmentAllocation(SegmentAllocation segmentAllocation) {
    segmentAllocations.removeIf(
        existingSegmentAllocation ->
            Objects.equals(existingSegmentAllocation.getSegment(), segmentAllocation.getSegment()));

    segmentAllocation.setParty(this);

    segmentAllocations.add(segmentAllocation);
  }

  /**
   * Assign the status to the organization.
   *
   * @param status the status
   */
  public void addStatus(Status status) {
    statuses.removeIf(existingStatus -> Objects.equals(existingStatus.getType(), status.getType()));

    status.setParty(this);

    statuses.add(status);
  }

  /**
   * Add the tax number for the organization.
   *
   * @param taxNumber the tax number
   */
  public void addTaxNumber(TaxNumber taxNumber) {
    taxNumbers.removeIf(
        existingTaxNumber -> Objects.equals(existingTaxNumber.getType(), taxNumber.getType()));

    taxNumber.setParty(this);

    taxNumbers.add(taxNumber);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    Organization other = (Organization) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Retrieve the attribute with the specified type for the organization.
   *
   * @param type the code for the attribute type
   * @return an Optional containing the attribute with the specified type for the organization or an
   *     empty Optional if the attribute could not be found
   */
  public Optional<Attribute> getAttributeWithType(String type) {
    return attributes.stream()
        .filter(attribute -> Objects.equals(attribute.getType(), type))
        .findFirst();
  }

  /**
   * Returns the attributes for the organization.
   *
   * @return the attributes for the organization
   */
  @Schema(description = "The attributes for the organization")
  @JsonProperty
  @JsonManagedReference("attributeReference")
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  public Set<Attribute> getAttributes() {
    return attributes;
  }

  /**
   * Retrieve the contact mechanism with the specified role for the organization.
   *
   * @param role the code for the contact mechanism role
   * @return an Optional containing the contact mechanism with the specified role for the
   *     organization or an empty Optional if the contact mechanism could not be found
   */
  public Optional<ContactMechanism> getContactMechanismWithRole(String role) {
    return contactMechanisms.stream()
        .filter(contactMechanism -> Objects.equals(contactMechanism.getRole(), role))
        .findFirst();
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the organization.
   *
   * @param type the code for the contact mechanism type
   * @param purpose the code for the contact mechanism role
   * @return an Optional containing the contact mechanism with the specified type and purpose for
   *     the organization or an empty Optional if the contact mechanism could not be found
   */
  public Optional<ContactMechanism> getContactMechanismWithTypeAndPurpose(
      String type, String purpose) {
    return contactMechanisms.stream()
        .filter(
            contactMechanism ->
                Objects.equals(contactMechanism.getType(), type)
                    && contactMechanism.hasPurpose(purpose))
        .findFirst();
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
  public Set<ContactMechanism> getContactMechanisms() {
    return contactMechanisms;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 codes for the countries of tax residence for the organization.
   *
   * @return the ISO 3166-1 alpha-2 codes for the countries of tax residence for the organization
   */
  @Schema(
      description =
          "The ISO 3166-1 alpha-2 codes for the countries of tax residence for the organization")
  @JsonProperty
  @XmlElementWrapper(name = "CountriesOfTaxResidence")
  @XmlElement(name = "CountryOfTaxResidence")
  @Size(max = 10)
  public Set<String> getCountriesOfTaxResidence() {
    return Set.of(StringUtils.commaDelimitedListToStringArray(countriesOfTaxResidence));
  }

  /**
   * Returns the date and time the organization was created.
   *
   * @return the date and time the organization was created
   */
  @JsonIgnore
  @XmlTransient
  public LocalDateTime getCreated() {
    return super.getCreated();
  }

  /**
   * Retrieve the first external reference with the specified type for the organization.
   *
   * @param type the code for the external reference type
   * @return an Optional containing the first external reference with the specified type for the
   *     organization or an empty if an external reference could not be found
   */
  public Optional<ExternalReference> getExternalReferenceWithType(String type) {
    return externalReferences.stream()
        .filter(externalReference -> Objects.equals(externalReference.getType(), type))
        .findFirst();
  }

  /**
   * Returns the external references for the organization.
   *
   * @return the external references for the organization
   */
  @Schema(description = "The external references for the organization")
  @JsonProperty
  @JsonManagedReference("externalReferenceReference")
  @XmlElementWrapper(name = "ExternalReferences")
  @XmlElement(name = "ExternalReference")
  public Set<ExternalReference> getExternalReferences() {
    return externalReferences;
  }

  /**
   * Retrieve the external references with the specified type for the organization.
   *
   * @param type the code for the external reference type
   * @return the external references with the specified type for the organization
   */
  public Set<ExternalReference> getExternalReferencesWithType(String type) {
    return externalReferences.stream()
        .filter(externalReference -> Objects.equals(externalReference.getType(), type))
        .collect(Collectors.toSet());
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the organization.
   *
   * @return the Universally Unique Identifier (UUID) for the organization
   */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the organization",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Retrieve the identity document with the specified type for the organization.
   *
   * @param type the code for the identity document type
   * @return an Optional containing the identity document with the specified type for the
   *     organization or an empty if the identity document could not be found
   */
  public Optional<IdentityDocument> getIdentityDocumentWithType(String type) {
    return identityDocuments.stream()
        .filter(identityDocument -> Objects.equals(identityDocument.getType(), type))
        .findFirst();
  }

  /**
   * Returns the identity documents for the organization.
   *
   * @return the identity documents for the organization
   */
  @Schema(description = "The identity documents for the organization")
  @JsonProperty
  @JsonManagedReference("identityDocumentReference")
  @XmlElementWrapper(name = "IdentityDocuments")
  @XmlElement(name = "IdentityDocument")
  public Set<IdentityDocument> getIdentityDocuments() {
    return identityDocuments;
  }

  /**
   * Retrieve the lock with the specified type for the organization.
   *
   * @param type the code for the lock type
   * @return an Optional containing the lock with the specified type for the organization or an
   *     empty Optional if the lock could not be found
   */
  public Optional<Lock> getLockWithType(String type) {
    return locks.stream().filter(lock -> Objects.equals(lock.getType(), type)).findFirst();
  }

  /**
   * Returns the locks applied to the organization.
   *
   * @return the locks applied to the organization
   */
  @Schema(description = "The locks applied to the organization")
  @JsonProperty
  @JsonManagedReference("lockReference")
  @XmlElementWrapper(name = "Locks")
  @XmlElement(name = "Lock")
  public Set<Lock> getLocks() {
    return locks;
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
   * Retrieve the first physical address with the specified role for the organization.
   *
   * @param role the code for the physical address role
   * @return an Optional containing the first physical address with the specified role for the
   *     organization or an empty Optional if the physical address could not be found
   */
  public Optional<PhysicalAddress> getPhysicalAddressWithRole(String role) {
    return physicalAddresses.stream()
        .filter(physicalAddress -> Objects.equals(physicalAddress.getRole(), role))
        .findFirst();
  }

  /**
   * Retrieve the first physical address with the specified type and purpose for the organization.
   *
   * @param type the code for the physical address type
   * @param purpose the code for the physical address purpose
   * @return an Optional containing the first physical address with the specified type and purpose
   *     for the organization or an empty Optional if the physical address could not be found
   */
  public Optional<PhysicalAddress> getPhysicalAddressWithTypeAndPurpose(
      String type, String purpose) {
    return physicalAddresses.stream()
        .filter(
            physicalAddress ->
                Objects.equals(physicalAddress.getType(), type)
                    && physicalAddress.getPurposes().contains(purpose))
        .findFirst();
  }

  /**
   * Returns the physical addresses for the organization.
   *
   * @return the physical addresses for the organization
   */
  @Schema(description = "The physical addresses for the organization")
  @JsonProperty
  @JsonManagedReference("physicalAddressReference")
  @XmlElementWrapper(name = "PhysicalAddresses")
  @XmlElement(name = "PhysicalAddress")
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return physicalAddresses;
  }

  /**
   * Retrieve the preference with the specified type for the organization.
   *
   * @param type the code for the preference type
   * @return an Optional containing the preference with the specified type for the organization or
   *     an empty Optional if the preference could not be found
   */
  public Optional<Preference> getPreferenceWithType(String type) {
    return preferences.stream()
        .filter(preference -> Objects.equals(preference.getType(), type))
        .findFirst();
  }

  /**
   * Returns the preferences for the organization.
   *
   * @return the preferences for the organization
   */
  @Schema(description = "The preferences for the organization")
  @JsonProperty
  @JsonManagedReference("preferenceReference")
  @XmlElementWrapper(name = "Preferences")
  @XmlElement(name = "Preference")
  public Set<Preference> getPreferences() {
    return preferences;
  }

  /**
   * Retrieve the role with the specified type for the organization independent of a party
   * association.
   *
   * @param type the code for the role type
   * @return an Optional containing the role with the specified type for the organization
   *     independent of a party association or an empty Optional if the role could not be found
   */
  public Optional<Role> getRoleWithType(String type) {
    return roles.stream().filter(role -> Objects.equals(role.getType(), type)).findFirst();
  }

  /**
   * Returns the roles assigned directly to the organization.
   *
   * @return the roles assigned directly to the organization
   */
  @Schema(description = "The roles assigned directly to the organization")
  @JsonProperty
  @JsonManagedReference("roleReference")
  @XmlElementWrapper(name = "Roles")
  @XmlElement(name = "Role")
  public Set<Role> getRoles() {
    return roles;
  }

  /**
   * Retrieve the segment allocation with the specified segment for the organization.
   *
   * @param segment the code for the segment
   * @return an Optional containing the segment allocation with the specified segment for the
   *     organization or an empty Optional if the segment allocation could not be found
   */
  public Optional<SegmentAllocation> getSegmentAllocationWithSegment(String segment) {
    return segmentAllocations.stream()
        .filter(segmentAllocation -> Objects.equals(segmentAllocation.getSegment(), segment))
        .findFirst();
  }

  /**
   * Returns the segment allocations for the organization.
   *
   * @return the segment allocations for the organization
   */
  @Schema(description = "The segment allocations for the organization")
  @JsonProperty
  @JsonManagedReference("segmentAllocationReference")
  @XmlElementWrapper(name = "SegmentAllocations")
  @XmlElement(name = "SegmentAllocation")
  public Set<SegmentAllocation> getSegmentAllocations() {
    return segmentAllocations;
  }

  /**
   * Retrieve the status with the specified type for the organization.
   *
   * @param type the code for the status type
   * @return an Optional containing the status with the specified type for the organization or an
   *     empty Optional if the status could not be found
   */
  public Optional<Status> getStatusWithType(String type) {
    return statuses.stream().filter(status -> Objects.equals(status.getType(), type)).findFirst();
  }

  /**
   * Returns the statuses assigned to the organization.
   *
   * @return the statuses assigned to the organization
   */
  @Schema(description = "The statuses assigned to the organization")
  @JsonProperty
  @JsonManagedReference("statusReference")
  @XmlElementWrapper(name = "Statuses")
  @XmlElement(name = "Status")
  public Set<Status> getStatuses() {
    return statuses;
  }

  /**
   * Retrieve the tax number with the specified type for the organization.
   *
   * @param type the tax number type
   * @return an Optional containing the tax number with the specified type for the organization or
   *     an empty Optional if the tax number could not be found
   */
  public Optional<TaxNumber> getTaxNumberWithType(String type) {
    return taxNumbers.stream()
        .filter(taxNumber -> Objects.equals(taxNumber.getType(), type))
        .findFirst();
  }

  /**
   * Returns the tax numbers for the organization.
   *
   * @return the tax numbers for the organization
   */
  @Schema(description = "The tax numbers for the organization")
  @JsonProperty
  @JsonManagedReference("taxNumberReference")
  @XmlElementWrapper(name = "TaxNumbers")
  @XmlElement(name = "TaxNumber")
  public Set<TaxNumber> getTaxNumbers() {
    return taxNumbers;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the organization is associated
   * with.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the organization is associated
   *     with
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the tenant the organization is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  public UUID getTenantId() {
    return super.getTenantId();
  }

  /**
   * Returns the party type for the organization.
   *
   * @return the party type for the organization
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
  public LocalDateTime getUpdated() {
    return super.getUpdated();
  }

  /**
   * Returns whether the organization has an attribute with the specified type.
   *
   * @param type the code for the attribute type
   * @return <b>true</b> if the organization has an attribute with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasAttributeWithType(String type) {
    return attributes.stream().anyMatch(attribute -> Objects.equals(attribute.getType(), type));
  }

  /**
   * Returns whether the organization has a contact mechanism with the specified role.
   *
   * @param role the code for the contact mechanism role
   * @return <b>true</b> if the organization has a contact mechanism with the specified role or
   *     <b>false</b> otherwise
   */
  public boolean hasContactMechanismWithRole(String role) {
    return contactMechanisms.stream()
        .anyMatch(contactMechanism -> Objects.equals(contactMechanism.getRole(), role));
  }

  /**
   * Returns whether the organization has a contact mechanism with the specified type.
   *
   * @param type the code for the contact mechanism type
   * @return <b>true</b> if the organization has a contact mechanism with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasContactMechanismWithType(String type) {
    return contactMechanisms.stream()
        .anyMatch(contactMechanism -> Objects.equals(contactMechanism.getType(), type));
  }

  /**
   * Returns whether the organization has an external reference with the specified type.
   *
   * @param type the code for the external reference type
   * @return <b>true</b> if the organization has an external reference with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasExternalReferenceWithType(String type) {
    return externalReferences.stream()
        .anyMatch(externalReference -> Objects.equals(externalReference.getType(), type));
  }

  /**
   * Returns whether the organization has an identity document with the specified type.
   *
   * @param type the code for the identity document type
   * @return <b>true</b> if the organization has an identity document with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasIdentityDocumentWithType(String type) {
    return identityDocuments.stream()
        .anyMatch(identityDocument -> Objects.equals(identityDocument.getType(), type));
  }

  /**
   * Returns whether the organization has a lock with the specified type.
   *
   * @param type the code for the lock type
   * @return <b>true</b> if the organization has a lock with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasLockWithType(String type) {
    return locks.stream().anyMatch(lock -> Objects.equals(lock.getType(), type));
  }

  /**
   * Returns whether the organization has a physical address with the specified role.
   *
   * @param role the code for the physical address role
   * @return <b>true</b> if the organization has a physical address with the specified role or
   *     <b>false</b> otherwise
   */
  public boolean hasPhysicalAddressWithRole(String role) {
    return physicalAddresses.stream()
        .anyMatch(physicalAddress -> Objects.equals(physicalAddress.getRole(), role));
  }

  /**
   * Returns whether the organization has a physical address with the specified type.
   *
   * @param type the code for the physical address type
   * @return <b>true</b> if the organization has a physical address with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasPhysicalAddressWithType(String type) {
    return physicalAddresses.stream()
        .anyMatch(physicalAddress -> Objects.equals(physicalAddress.getType(), type));
  }

  /**
   * Returns whether the organization has a preference with the specified type.
   *
   * @param type the code for the preference type
   * @return <b>true</b> if the organization has a preference with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasPreferenceWithType(String type) {
    return preferences.stream().anyMatch(preference -> Objects.equals(preference.getType(), type));
  }

  /**
   * Returns whether the organization has a role with the specified type.
   *
   * @param type the code for the role type
   * @return <b>true</b> if the organization has a role with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasRoleWithType(String type) {
    return roles.stream().anyMatch(role -> Objects.equals(role.getType(), type));
  }

  /**
   * Returns whether the organization has a status with the specified type.
   *
   * @param type the code for the status type
   * @return <b>true</b> if the organization has a status with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasStatusWithType(String type) {
    return statuses.stream().anyMatch(status -> Objects.equals(status.getType(), type));
  }

  /**
   * Returns whether the organization has a tax number with the specified type.
   *
   * @param type the code for the tax number type
   * @return <b>true</b> if the organization has a tax number with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasTaxNumberWithType(String type) {
    return taxNumbers.stream().anyMatch(taxNumber -> Objects.equals(taxNumber.getType(), type));
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((getId() == null) ? 0 : getId().hashCode());
  }

  /**
   * Remove the attribute with the specified type for the organization.
   *
   * @param type the code for the attribute type
   */
  public void removeAttributeWithType(String type) {
    attributes.removeIf(existingAttribute -> Objects.equals(existingAttribute.getType(), type));
  }

  /**
   * Remove the contact mechanism with the specified role for the organization.
   *
   * @param role the code for the contact mechanism role
   */
  public void removeContactMechanismWithRole(String role) {
    contactMechanisms.removeIf(
        existingContactMechanism -> Objects.equals(existingContactMechanism.getRole(), role));
  }

  /**
   * Remove the external reference with the specified type for the organization.
   *
   * @param type the code for the external reference type
   */
  public void removeExternalReferenceWithType(String type) {
    externalReferences.removeIf(
        existingExternalReference -> Objects.equals(existingExternalReference.getType(), type));
  }

  /**
   * Remove the identity document with the specified type for the organization.
   *
   * @param type the code for the identity document type
   */
  public void removeIdentityDocumentWithType(String type) {
    identityDocuments.removeIf(
        existingIdentityDocument -> Objects.equals(existingIdentityDocument.getType(), type));
  }

  /**
   * Remove the lock with the specified type for the organization.
   *
   * @param type the code for the lock type
   */
  public void removeLockWithType(String type) {
    locks.removeIf(existingLock -> Objects.equals(existingLock.getType(), type));
  }

  /**
   * Remove any physical addresses with the specified role for the organization.
   *
   * @param role the code for the physical address role
   */
  public void removePhysicalAddressWithRole(String role) {
    physicalAddresses.removeIf(
        existingPhysicalAddress -> Objects.equals(existingPhysicalAddress.getRole(), role));
  }

  /**
   * Remove the preference with the specified type for the organization.
   *
   * @param type the code for the preference type
   */
  public void removePreferenceWithType(String type) {
    preferences.removeIf(existingPreference -> Objects.equals(existingPreference.getType(), type));
  }

  /**
   * Remove the role with the specified type for the organization.
   *
   * @param type the code for role type
   */
  public void removeRoleWithType(String type) {
    roles.removeIf(existingRole -> Objects.equals(existingRole.getType(), type));
  }

  /**
   * Remove the segment allocation with the specified segment for the organization.
   *
   * @param segment the code for the segment
   */
  public void removeSegmentAllocationWithSegment(String segment) {
    segmentAllocations.removeIf(
        segmentAllocation -> Objects.equals(segmentAllocation.getSegment(), segment));
  }

  /**
   * Remove the status with the specified type for the organization.
   *
   * @param type the code for the lock type
   */
  public void removeStatusWithType(String type) {
    statuses.removeIf(existingStatus -> Objects.equals(existingStatus.getType(), type));
  }

  /**
   * Remove the tax number with the specified type for the organization.
   *
   * @param type the tax number type
   */
  public void removeTaxNumberWithType(String type) {
    taxNumbers.removeIf(existingTaxNumber -> Objects.equals(existingTaxNumber.getType(), type));
  }

  /**
   * Set the attributes for the organization.
   *
   * @param attributes the attributes for the organization
   */
  public void setAttributes(Set<Attribute> attributes) {
    attributes.forEach(attribute -> attribute.setParty(this));
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the contact mechanisms for the organization.
   *
   * @param contactMechanisms the contact mechanisms for the organization
   */
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    contactMechanisms.forEach(contactMechanism -> contactMechanism.setParty(this));
    this.contactMechanisms.clear();
    this.contactMechanisms.addAll(contactMechanisms);
  }

  /**
   * Set the ISO 3166-1 alpha-2 codes for the countries of tax residence for the organization.
   *
   * @param countriesOfTaxResidence the ISO 3166-1 alpha-2 codes for the countries of tax residence
   *     for the organization
   */
  public void setCountriesOfTaxResidence(Set<String> countriesOfTaxResidence) {
    this.countriesOfTaxResidence =
        StringUtils.collectionToDelimitedString(countriesOfTaxResidence, ",");
  }

  /**
   * Set the code for the single country of tax residence for the organization.
   *
   * @param countryOfTaxResidence the code for the single country of tax residence for the
   *     organization
   */
  @JsonIgnore
  public void setCountryOfTaxResidence(String countryOfTaxResidence) {
    this.countriesOfTaxResidence = countryOfTaxResidence;
  }

  /**
   * Set the external references for the organization.
   *
   * @param externalReferences the external references for the organization
   */
  public void setExternalReferences(Set<ExternalReference> externalReferences) {
    externalReferences.forEach(externalReference -> externalReference.setParty(this));
    this.externalReferences.clear();
    this.externalReferences.addAll(externalReferences);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the organization.
   *
   * @param id the Universally Unique Identifier (UUID) for the organization
   */
  @Override
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the identity documents for the organization.
   *
   * @param identityDocuments the identity documents for the organization
   */
  public void setIdentityDocuments(Set<IdentityDocument> identityDocuments) {
    identityDocuments.forEach(identityDocument -> identityDocument.setParty(this));
    this.identityDocuments.clear();
    this.identityDocuments.addAll(identityDocuments);
  }

  /**
   * Set the locks for the organization.
   *
   * @param locks the locks for the organization
   */
  public void setLocks(Set<Lock> locks) {
    locks.forEach(lock -> lock.setParty(this));
    this.locks.clear();
    this.locks.addAll(locks);
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
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    physicalAddresses.forEach(physicalAddress -> physicalAddress.setParty(this));
    this.physicalAddresses.clear();
    this.physicalAddresses.addAll(physicalAddresses);
  }

  /**
   * Set the preferences for the organization.
   *
   * @param preferences the preferences for the organization
   */
  public void setPreferences(Set<Preference> preferences) {
    preferences.forEach(preference -> preference.setParty(this));
    this.preferences.clear();
    this.preferences.addAll(preferences);
  }

  /**
   * Set the roles assigned directly to the organization.
   *
   * @param roles the roles
   */
  public void setRoles(Set<Role> roles) {
    roles.forEach(role -> role.setParty(this));
    this.roles.clear();
    this.roles.addAll(roles);
  }

  /**
   * Set the segment allocations for the organization.
   *
   * @param segmentAllocations the segment allocations for the organization
   */
  public void setSegmentAllocations(Set<SegmentAllocation> segmentAllocations) {
    segmentAllocations.forEach(segmentAllocation -> segmentAllocation.setParty(this));
    this.segmentAllocations.clear();
    this.segmentAllocations.addAll(segmentAllocations);
  }

  /**
   * Set the statuses for the organization.
   *
   * @param statuses the statuses for the organization
   */
  public void setStatuses(Set<Status> statuses) {
    statuses.forEach(status -> status.setParty(this));
    this.statuses.clear();
    this.statuses.addAll(statuses);
  }

  /**
   * Set the tax numbers for the organization.
   *
   * @param taxNumbers the tax numbers for the organization
   */
  public void setTaxNumbers(Set<TaxNumber> taxNumbers) {
    taxNumbers.forEach(taxNumber -> taxNumber.setParty(this));
    this.taxNumbers.clear();
    this.taxNumbers.addAll(taxNumbers);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the organization is associated
   * with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the organization is
   *     associated with
   */
  public void setTenantId(UUID tenantId) {
    super.setTenantId(tenantId);
  }
}
