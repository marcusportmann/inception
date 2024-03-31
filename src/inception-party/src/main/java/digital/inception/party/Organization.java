/*
 * Copyright Marcus Portmann
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.LocalDateAdapter;
import digital.inception.party.constraints.ValidOrganization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
  "identificationType",
  "identificationNumber",
  "identificationIssueDate",
  "identificationExpiryDate",
  "identificationCountryOfIssue",
  "attributes",
  "contactMechanisms",
  "externalReferences",
  "identifications",
  "industryAllocations",
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
      "identificationType",
      "identificationNumber",
      "identificationIssueDate",
      "identificationExpiryDate",
      "identificationCountryOfIssue",
      "attributes",
      "contactMechanisms",
      "externalReferences",
      "identifications",
      "industryAllocations",
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
@Table(name = "party_organizations")
public class Organization extends PartyBase implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Attribute> attributes = new HashSet<>();

  /** The contact mechanisms for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<ContactMechanism> contactMechanisms = new HashSet<>();

  /** The external references for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<ExternalReference> externalReferences = new HashSet<>();

  /** The identifications for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Identification> identifications = new HashSet<>();

  /** The industry allocations for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "organization",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("system, industry")
  private final Set<IndustryAllocation> industryAllocations = new HashSet<>();

  /** The locks applied to the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
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
  @OrderBy("type")
  private final Set<Preference> preferences = new HashSet<>();

  /** The roles assigned directly to the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Role> roles = new HashSet<>();

  /** The segment allocations for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("segment")
  private final Set<SegmentAllocation> segmentAllocations = new HashSet<>();

  /** The statuses assigned to the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<Status> statuses = new HashSet<>();

  /** The tax numbers for the organization. */
  @Valid
  @OneToMany(
      mappedBy = "party",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @OrderBy("type")
  private final Set<TaxNumber> taxNumbers = new HashSet<>();

  /**
   * The comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for the
   * organization.
   */
  @JsonIgnore
  @XmlTransient
  @Size(max = 50)
  @Column(name = "countries_of_tax_residence", length = 50)
  private String countriesOfTaxResidence;

  /**
   * The ISO 3166-1 alpha-2 code for the country of issue for the identification for the
   * organization.
   */
  @Size(min = 2, max = 2)
  @Column(name = "identification_country_of_issue", length = 2)
  private String identificationCountryOfIssue;

  /** The expiry date for the identification for the organization. */
  @Column(name = "identification_expiry_date")
  private LocalDate identificationExpiryDate;

  /** The issue date for the identification for the organization. */
  @Column(name = "identification_issue_date")
  private LocalDate identificationIssueDate;

  /** The identification number for the organization. */
  @Size(min = 1, max = 30)
  @Column(name = "identification_number", length = 30)
  private String identificationNumber;

  /** The code for the identification type for the organization. */
  @Size(min = 1, max = 50)
  @Column(name = "identification_type", length = 50)
  private String identificationType;

  /** Constructs a new <b>Organization</b>. */
  public Organization() {
    super(PartyType.ORGANIZATION);
  }

  /**
   * Constructs a new <b>Organization</b>.
   *
   * @param tenantId the ID for the tenant the organization is associated with
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
            Objects.equals(existingExternalReference.getType(), externalReference.getType()));

    externalReference.setParty(this);

    externalReferences.add(externalReference);
  }

  /**
   * Add the identification for the organization.
   *
   * @param identification the identification
   */
  public void addIdentification(Identification identification) {
    identifications.removeIf(
        existingIdentification ->
            Objects.equals(existingIdentification.getId(), identification.getId()));

    identifications.removeIf(
        existingIdentification ->
            Objects.equals(existingIdentification.getType(), identification.getType())
                && Objects.equals(
                    existingIdentification.getCountryOfIssue(), identification.getCountryOfIssue())
                && Objects.equals(
                    existingIdentification.getIssueDate(), identification.getIssueDate()));

    identification.setParty(this);

    identifications.add(identification);
  }

  /**
   * Add the industry allocation for the organization.
   *
   * @param industryAllocation the industry allocation
   */
  public void addIndustryAllocation(IndustryAllocation industryAllocation) {
    industryAllocations.removeIf(
        existingIndustryAllocation ->
            (Objects.equals(existingIndustryAllocation.getSystem(), industryAllocation.getSystem())
                && Objects.equals(
                    existingIndustryAllocation.getIndustry(), industryAllocation.getIndustry())));

    industryAllocation.setOrganization(this);

    industryAllocations.add(industryAllocation);
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
   * Returns the ID for the organization.
   *
   * @return the ID for the organization
   */
  @Schema(description = "The ID for the organization", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of issue for the identification for the
   * organization.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of issue for the identification for the
   *     organization
   */
  @Schema(
      description =
          "The ISO 3166-1 alpha-2 code for the country of issue for the identification for the organization")
  @JsonProperty
  @XmlElement(name = "IdentificationCountryOfIssue")
  public String getIdentificationCountryOfIssue() {
    return identificationCountryOfIssue;
  }

  /**
   * Returns the expiry date for the identification for the organization.
   *
   * @return the expiry date for the identification for the organization
   */
  @Schema(
      description = "The ISO 8601 format expiry date for the identification for the organization")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "IdentificationExpiryDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  public LocalDate getIdentificationExpiryDate() {
    return identificationExpiryDate;
  }

  /**
   * Returns the issue date for the identification for the organization.
   *
   * @return the issue date for the identification for the organization
   */
  @Schema(
      description = "The ISO 8601 format issue date for the identification for the organization")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "IdentificationIssueDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  public LocalDate getIdentificationIssueDate() {
    return identificationIssueDate;
  }

  /**
   * Returns the identification number for the organization.
   *
   * @return the identification number for the organization
   */
  @Schema(description = "The identification number for the organization")
  @JsonProperty
  @XmlElement(name = "IdentificationNumber")
  public String getIdentificationNumber() {
    return identificationNumber;
  }

  /**
   * Returns the code for the identification type for the organization.
   *
   * @return the code for the identification type for the organization
   */
  @Schema(description = "The code for the identification type for the organization")
  @JsonProperty
  @XmlElement(name = "IdentificationType")
  public String getIdentificationType() {
    return identificationType;
  }

  /**
   * Retrieve the identification with the specified type for the organization.
   *
   * @param type the code for the identification type
   * @return an Optional containing the identification with the specified type for the organization
   *     or an empty if the identification could not be found
   */
  public Optional<Identification> getIdentificationWithType(String type) {
    return identifications.stream()
        .filter(identification -> Objects.equals(identification.getType(), type))
        .findFirst();
  }

  /**
   * Returns the identifications for the organization.
   *
   * @return the identifications for the organization
   */
  @Schema(description = "The identifications for the organization")
  @JsonProperty
  @JsonManagedReference("identificationReference")
  @XmlElementWrapper(name = "Identifications")
  @XmlElement(name = "Identification")
  public Set<Identification> getIdentifications() {
    return identifications;
  }

  /**
   * Retrieve the industry allocation with the specified industry classification system and industry
   * classification for the organization.
   *
   * @param system the code for the industry classification system
   * @param industry the code for the industry classification
   * @return an Optional containing the industry allocation with the specified industry
   *     classification system and industry classification for the organization or an empty Optional
   *     if the industry allocation could not be found
   */
  public Optional<IndustryAllocation> getIndustryAllocationWithSystemAndIndustry(
      String system, String industry) {
    return industryAllocations.stream()
        .filter(
            industryAllocation ->
                Objects.equals(industryAllocation.getSystem(), system)
                    && Objects.equals(industryAllocation.getIndustry(), industry))
        .findFirst();
  }

  /**
   * Returns the industry allocations for the organization.
   *
   * @return the industry allocations for the organization
   */
  @Schema(description = "The industry allocations for the organization")
  @JsonProperty
  @JsonManagedReference("industryAllocationReference")
  @XmlElementWrapper(name = "IndustryAllocations")
  @XmlElement(name = "IndustryAllocation")
  public Set<IndustryAllocation> getIndustryAllocations() {
    return industryAllocations;
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
   * Returns the ID for the tenant the organization is associated with.
   *
   * @return the ID for the tenant the organization is associated with
   */
  @Schema(
      description = "The ID for the tenant the organization is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
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
   * Returns whether the organization has an identification with the specified type.
   *
   * @param type the code for the identification type
   * @return <b>true</b> if the organization has an identification with the specified type or
   *     <b>false</b> otherwise
   */
  public boolean hasIdentificationWithType(String type) {
    return identifications.stream()
        .anyMatch(identification -> Objects.equals(identification.getType(), type));
  }

  /**
   * Returns whether the organization has an industry allocation with the specified industry
   * classification system and industry classification.
   *
   * @param system the code for the industry classification system
   * @param industry the code for the industry classification
   * @return <b>true</b> if the organization has an industry allocation with the specified industry
   *     classification system and industry classification or <b>false</b> otherwise
   */
  public boolean hasIndustryAllocationWithSystemAndIndustry(String system, String industry) {
    return industryAllocations.stream()
        .anyMatch(
            industryAllocation ->
                (Objects.equals(industryAllocation.getSystem(), system)
                    && Objects.equals(industryAllocation.getIndustry(), industry)));
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
   * Remove the industry allocation with the specified industry classification system and industry
   * classification for the organization.
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
   * Remove the identification with the specified type for the organization.
   *
   * @param type the code for the identification type
   */
  public void removeIdentificationWithType(String type) {
    identifications.removeIf(
        existingIdentification -> Objects.equals(existingIdentification.getType(), type));
  }

  /**
   * Remove the attribute with the specified type for the organization.
   *
   * @param system the code for the industry classification system
   * @param industry the code for the industry classification
   */
  public void removeIndustryAllocationWithSystemAndIndustry(String system, String industry) {
    industryAllocations.removeIf(
        industryAllocation ->
            (Objects.equals(industryAllocation.getSystem(), system)
                && Objects.equals(industryAllocation.getIndustry(), industry)));
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
   * Set the ID for the organization.
   *
   * @param id the ID for the organization
   */
  @Override
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country of issue for the identification for the
   * organization.
   *
   * @param identificationCountryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for
   *     the identification for the organization
   */
  public void setIdentificationCountryOfIssue(String identificationCountryOfIssue) {
    this.identificationCountryOfIssue = identificationCountryOfIssue;
  }

  /**
   * Set the expiry date for the identification for the organization.
   *
   * @param identificationExpiryDate the expiry date for the identification for the organization
   */
  public void setIdentificationExpiryDate(LocalDate identificationExpiryDate) {
    this.identificationExpiryDate = identificationExpiryDate;
  }

  /**
   * Set the issue date for the identification for the organization.
   *
   * @param identificationIssueDate the issue date for the identification for the organization
   */
  public void setIdentificationIssueDate(LocalDate identificationIssueDate) {
    this.identificationIssueDate = identificationIssueDate;
  }

  /**
   * Set the identification number for the organization.
   *
   * @param identificationNumber the identification number for the organization
   */
  public void setIdentificationNumber(String identificationNumber) {
    this.identificationNumber = identificationNumber;
  }

  /**
   * Set the code for the identification type for the organization.
   *
   * @param identificationType the code for the identification type for the organization
   */
  public void setIdentificationType(String identificationType) {
    this.identificationType = identificationType;
  }

  /**
   * Set the identifications for the organization.
   *
   * @param identifications the identifications for the organization
   */
  public void setIdentifications(Set<Identification> identifications) {
    identifications.forEach(identification -> identification.setParty(this));
    this.identifications.clear();
    this.identifications.addAll(identifications);
  }

  /**
   * Set the industry allocations for the organization.
   *
   * @param industryAllocations the industry allocations for the organization
   */
  public void setIndustryAllocations(Set<IndustryAllocation> industryAllocations) {
    industryAllocations.forEach(industryAllocation -> industryAllocation.setOrganization(this));
    this.industryAllocations.clear();
    this.industryAllocations.addAll(industryAllocations);
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
   * Set the ID for the tenant the organization is associated with.
   *
   * @param tenantId the ID for the tenant the organization is associated with
   */
  public void setTenantId(UUID tenantId) {
    super.setTenantId(tenantId);
  }
}
