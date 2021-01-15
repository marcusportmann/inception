package digital.inception.banking.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.banking.customer.constraints.ValidIndividualCustomer;
import digital.inception.party.ContactMechanism;
import digital.inception.party.ContactMechanismPurpose;
import digital.inception.party.ContactMechanismType;
import digital.inception.party.IdentityDocument;
import digital.inception.party.PartyType;
import digital.inception.party.Person;
import digital.inception.party.PhysicalAddress;
import digital.inception.party.PhysicalAddressPurpose;
import digital.inception.party.PhysicalAddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>IndividualCustomer</code> class holds the information for an individual customer.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A individual customer")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "correspondenceLanguage",
  "countryOfBirth",
  "countryOfResidence",
  "dateOfBirth",
  "dateOfDeath",
  "emancipatedMinor",
  "employmentStatus",
  "employmentType",
  "gender",
  "givenName",
  "homeLanguage",
  "id",
  "initials",
  "maidenName",
  "maritalStatus",
  "marriageType",
  "middleNames",
  "name",
  "occupation",
  "preferredName",
  "race",
  "residencyStatus",
  "residentialType",
  "surname",
  "tenantId",
  "title",
  "countryOfTaxResidence",
  "taxNumberType",
  "taxNumber",
  "contactMechanisms",
  "identityDocuments",
  "physicalAddresses"
})
@XmlRootElement(
    name = "IndividualCustomer",
    namespace = "http://customer.banking.inception.digital")
@XmlType(
    name = "IndividualCustomer",
    namespace = "http://customer.banking.inception.digital",
    propOrder = {"emancipatedMinor"})
@XmlAccessorType(XmlAccessType.FIELD)
@ValidIndividualCustomer
@Entity
@Table(schema = "customer", name = "individual_customers")
public class IndividualCustomer extends Person implements Serializable {

  private static final long serialVersionUID = 1000000;

  /**
   * The optional boolean flag indicating whether the individual customer is an emancipated minor.
   */
  @Schema(
      description =
          "The optional boolean flag indicating whether the individual customer is an emancipated minor")
  @JsonProperty
  @XmlElement(name = "EmancipatedMinor")
  @Column(table = "individual_customers", name = "emancipated_minor")
  private Boolean emancipatedMinor = false;

  /** Constructs a new <code>IndividualCustomer</code>. */
  public IndividualCustomer() {}

  /**
   * Add the contact mechanism for the individual customer.
   *
   * @param contactMechanism the contact mechanism
   */
  @Override
  public void addContactMechanism(ContactMechanism contactMechanism) {
    super.addContactMechanism(contactMechanism);
  }

  /**
   * Add the identity document for the individual customer.
   *
   * @param identityDocument the identity document
   */
  @Override
  public void addIdentityDocument(IdentityDocument identityDocument) {
    super.addIdentityDocument(identityDocument);
  }

  /**
   * Add the physical address for the individual customer.
   *
   * @param physicalAddress the physical address
   */
  @Override
  public void addPhysicalAddress(PhysicalAddress physicalAddress) {
    super.addPhysicalAddress(physicalAddress);
  }

  /**
   * Retrieve the contact mechanism with the specified type and purpose for the individual customer.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   * @return the contact mechanism with the specified type and purpose for the individual customer
   *     or <code>null
   *     </code> if the contact mechanism could not be found
   */
  @Override
  public ContactMechanism getContactMechanism(
      ContactMechanismType type, ContactMechanismPurpose purpose) {
    return super.getContactMechanism(type, purpose);
  }

  /**
   * Returns the contact mechanisms for the individual customer.
   *
   * @return the contact mechanisms for the individual customer
   */
  @Schema(description = "The contact mechanisms for the individual customer")
  @Override
  public Set<ContactMechanism> getContactMechanisms() {
    return super.getContactMechanisms();
  }

  /**
   * Returns the code for the correspondence language for the individual customer.
   *
   * @return the code for the correspondence language for the individual customer
   */
  @Schema(
      description = "The code for the correspondence language for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getCorrespondenceLanguage() {
    return super.getCorrespondenceLanguage();
  }

  /**
   * Returns the code for the country of birth for the individual customer.
   *
   * @return the code for the country of birth for the individual customer
   */
  @Schema(
      description = "The code for the country of birth for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getCountryOfBirth() {
    return super.getCountryOfBirth();
  }

  /**
   * Returns the code for the country of residence for the individual customer.
   *
   * @return the code for the country of residence for the individual customer
   */
  @Schema(
      description = "The code for the country of residence for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getCountryOfResidence() {
    return super.getCountryOfResidence();
  }

  /**
   * Returns the optional code for the country of tax residence for the individual customer.
   *
   * @return the optional code for the country of tax residence for the individual customer
   */
  @Schema(
      description =
          "The optional code for the country of tax residence for the individual customer")
  @Override
  public String getCountryOfTaxResidence() {
    return super.getCountryOfTaxResidence();
  }

  /**
   * Returns the date of birth for the individual customer.
   *
   * @return the date of birth for the individual customer
   */
  @Schema(description = "The date of birth for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public LocalDate getDateOfBirth() {
    return super.getDateOfBirth();
  }

  /**
   * Returns the optional date of death for the individual customer.
   *
   * @return the optional date of death for the individual customer
   */
  @Schema(description = "The optional date of death for the individual customer")
  @JsonProperty
  @Override
  public LocalDate getDateOfDeath() {
    return super.getDateOfDeath();
  }

  /**
   * Returns the optional boolean flag indicating whether the individual customer is an emancipated
   * minor.
   *
   * @return the optional boolean flag indicating whether the individual customer is an emancipated
   *     minor
   */
  public Boolean getEmancipatedMinor() {
    return emancipatedMinor;
  }

  /**
   * Returns the code for the employment status for the individual customer.
   *
   * @return the code for the employment status for the individual customer
   */
  @Schema(
      description = "The code for the employment status for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getEmploymentStatus() {
    return super.getEmploymentStatus();
  }

  /**
   * Returns the code for the employment type for the individual customer.
   *
   * @return the code for the employment type for the individual customer
   */
  @Schema(
      description = "The code for the employment type for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getEmploymentType() {
    return super.getEmploymentType();
  }

  /**
   * Returns the code for the gender for the individual customer.
   *
   * @return the code for the gender for the individual customer
   */
  @Schema(description = "The code for the gender for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getGender() {
    return super.getGender();
  }

  /**
   * Returns the given name, firstname, forename, or Christian name for the individual customer.
   *
   * @return the given name, firstname, forename, or Christian name for the individual customer
   */
  @Schema(
      description =
          "The given name, firstname, forename, or Christian name for the individual customer.\n",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getGivenName() {
    return super.getGivenName();
  }

  /**
   * Returns the code for the home language for the individual customer.
   *
   * @return the code for the home language for the individual customer
   */
  @Schema(
      description = "The code for the home language for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getHomeLanguage() {
    return super.getHomeLanguage();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the individual customer.
   *
   * @return the Universally Unique Identifier (UUID) for the individual customer
   */
  @Schema(description = "The Universally Unique Identifier (UUID) for the individual customer")
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the identity documents for the individual customer.
   *
   * @return the identity documents for the individual customer
   */
  @Schema(description = "The identity documents for the individual customer")
  @Override
  public Set<IdentityDocument> getIdentityDocuments() {
    return super.getIdentityDocuments();
  }

  /**
   * Returns the initials for the individual customer.
   *
   * @return the initials for the individual customer
   */
  @Schema(description = "The initials for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getInitials() {
    return super.getInitials();
  }

  /**
   * Returns the optional maiden name for the individual customer.
   *
   * @return the optional maiden name for the individual customer
   */
  @Schema(description = "The optional maiden name for the individual customer")
  @Override
  public String getMaidenName() {
    return super.getMaidenName();
  }

  /**
   * Returns the code for the marital status for the individual customer.
   *
   * @return the code for the marital status for the individual customer
   */
  @Schema(
      description = "The code for the marital status for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getMaritalStatus() {
    return super.getMaritalStatus();
  }

  /**
   * Returns the code for the marriage type for the individual customer if the individual customer
   * is married.
   *
   * @return the code for the marriage type for the individual customer if the individual customer
   *     is married
   */
  @Schema(
      description =
          "The code for the marriage type for the individual customer if the person is married")
  @Override
  public String getMarriageType() {
    return super.getMarriageType();
  }

  /**
   * Returns the optional middle names for the individual customer.
   *
   * @return the optional middle names for the individual customer
   */
  @Schema(description = "The optional middle names for the individual customer")
  @Override
  public String getMiddleNames() {
    return super.getMiddleNames();
  }

  /**
   * Returns the personal name or full name of the individual customer.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the individual customer. This name should match the full name on the identity
   * document(s) associated with the individual customer.
   *
   * <p>See https://en.wikipedia.org/wiki/Personal_name
   *
   * @return the personal name or full name of the individual customer
   */
  @Schema(description = "The personal name or full name of the individual customer")
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the code for the occupation for the individual customer.
   *
   * @return the code for the occupation for the individual customer
   */
  @Schema(description = "The code for the occupation for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getOccupation() {
    return super.getOccupation();
  }

  /**
   * Retrieve the physical address with the specified type and purpose for the individual customer.
   *
   * @param type the physical address type
   * @param purpose the physical address purpose
   * @return the physical address with the specified type and purpose for the individual customer or
   *     <code>null
   *     </code> if the physical address could not be found
   */
  @Override
  public PhysicalAddress getPhysicalAddress(
      PhysicalAddressType type, PhysicalAddressPurpose purpose) {
    return super.getPhysicalAddress(type, purpose);
  }

  /**
   * Returns the physical addresses for the individual customer.
   *
   * @return the physical addresses for the individual customer
   */
  @Schema(description = "The physical addresses for the individual customer")
  @Override
  public Set<PhysicalAddress> getPhysicalAddresses() {
    return super.getPhysicalAddresses();
  }

  /**
   * Returns the optional preferred name for the individual customer.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @return the optional preferred name for the individual customer
   */
  @Schema(description = "The optional preferred name for the individual customer")
  @Override
  public String getPreferredName() {
    return super.getPreferredName();
  }

  /**
   * The code for the race for the individual customer.
   *
   * @return the code for the race for the individual customer
   */
  @Schema(description = "The code for the race for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getRace() {
    return super.getRace();
  }

  /**
   * Returns the code for the residency status for the individual customer.
   *
   * @return the code for the residency status for the individual customer
   */
  @Schema(
      description = "The code for the residency status for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getResidencyStatus() {
    return super.getResidencyStatus();
  }

  /**
   * Returns the code for the residential type for the individual customer.
   *
   * @return the code for the residential type for the individual customer
   */
  @Schema(
      description = "The code for the residential type for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getResidentialType() {
    return super.getResidentialType();
  }

  /**
   * Returns the surname, last name, or family name for the individual customer.
   *
   * @return the surname, last name, or family name for the individual customer
   */
  @Schema(
      description = "The surname, last name, or family name for the individual customer",
      required = true)
  @JsonProperty(required = true)
  @Override
  public String getSurname() {
    return super.getSurname();
  }

  /**
   * Returns the optional tax number for the individual customer.
   *
   * @return the optional tax number for the individual customer
   */
  @Schema(description = "The optional tax number for the individual customer")
  @Override
  public String getTaxNumber() {
    return super.getTaxNumber();
  }

  /**
   * Returns the optional code for the tax number type for the individual customer.
   *
   * @return the optional code for the tax number type for the individual customer
   */
  @Schema(description = "The optional tax number type for the individual customer")
  @Override
  public String getTaxNumberType() {
    return super.getTaxNumberType();
  }

  /**
   * Returns the code for the title for the individual customer.
   *
   * @return the code for the title for the individual customer
   */
  @Schema(description = "The code for the title for the individual customer", required = true)
  @JsonProperty(required = true)
  @Override
  public String getTitle() {
    return super.getTitle();
  }

  /**
   * Returns the type of party for the individual customer.
   *
   * @return the type of party for the individual customer
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public PartyType getType() {
    return super.getType();
  }

  /**
   * Returns the date and time the individual customer was last updated.
   *
   * @return the date and time the individual customer was last updated
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getUpdated() {
    return super.getUpdated();
  }

  /**
   * Remove the contact mechanism with the specified type and purpose for the individual customer.
   *
   * @param type the contact mechanism type
   * @param purpose the contact mechanism purpose
   */
  @Override
  public void removeContactMechanism(ContactMechanismType type, ContactMechanismPurpose purpose) {
    super.removeContactMechanism(type, purpose);
  }

  /**
   * Remove the identity document with the specified type for the individual customer.
   *
   * @param type the code for the type of identity document
   */
  @Override
  public void removeIdentityDocumentByType(String type) {
    super.removeIdentityDocumentByType(type);
  }

  /**
   * Remove the physical address with the specified type and purpose for the individual customer.
   *
   * @param type the physical address type
   * @param purpose the physical address purpose
   */
  @Override
  public void removePhysicalAddress(PhysicalAddressType type, PhysicalAddressPurpose purpose) {
    super.removePhysicalAddress(type, purpose);
  }

  /**
   * Set the contact mechanisms for the individual customer.
   *
   * @param contactMechanisms the contact mechanisms for the individual customer
   */
  @Override
  public void setContactMechanisms(Set<ContactMechanism> contactMechanisms) {
    super.setContactMechanisms(contactMechanisms);
  }

  /**
   * Set the code for the correspondence language for the individual customer.
   *
   * @param correspondenceLanguage the code for the correspondence language for the private
   *     individual customer
   */
  public void setCorrespondenceLanguage(String correspondenceLanguage) {
    super.setCorrespondenceLanguage(correspondenceLanguage);
  }

  /**
   * Set the code for the country of birth for the individual customer.
   *
   * @param countryOfBirth the code for the country of birth for the individual customer
   */
  @Override
  public void setCountryOfBirth(String countryOfBirth) {
    super.setCountryOfBirth(countryOfBirth);
  }

  /**
   * Set the code for the country of residence for the individual customer.
   *
   * @param countryOfResidence the code for the country of residence for the private individual
   *     customer
   */
  @Override
  public void setCountryOfResidence(String countryOfResidence) {
    super.setCountryOfResidence(countryOfResidence);
  }

  /**
   * Set the optional code for the country of tax residence for the individual customer.
   *
   * @param countryOfTaxResidence the optional code for the country of tax residence for the
   *     individual customer
   */
  @Override
  public void setCountryOfTaxResidence(String countryOfTaxResidence) {
    super.setCountryOfTaxResidence(countryOfTaxResidence);
  }

  /**
   * Set the date of birth for the individual customer.
   *
   * @param dateOfBirth the date of birth for the individual customer
   */
  @Override
  public void setDateOfBirth(LocalDate dateOfBirth) {
    super.setDateOfBirth(dateOfBirth);
  }

  /**
   * Set the optional date of death for the individual customer.
   *
   * @param dateOfDeath the optional date of death for the individual customer
   */
  @Override
  public void setDateOfDeath(LocalDate dateOfDeath) {
    super.setDateOfDeath(dateOfDeath);
  }

  /**
   * Set the optional boolean flag indicating whether the individual customer is an emancipated
   * minor.
   *
   * @param emancipatedMinor the optional boolean flag indicating whether the individual customer is
   *     an emancipated minor
   */
  public void setEmancipatedMinor(Boolean emancipatedMinor) {
    this.emancipatedMinor = emancipatedMinor;
  }

  /**
   * Set the code for the employment status for the individual customer.
   *
   * @param employmentStatus the code for the employment status for the individual customer
   */
  @Override
  public void setEmploymentStatus(String employmentStatus) {
    super.setEmploymentStatus(employmentStatus);
  }

  /**
   * Set the code for the employment type for the individual customer.
   *
   * @param employmentType the code for the employment type for the individual customer
   */
  @Override
  public void setEmploymentType(String employmentType) {
    super.setEmploymentType(employmentType);
  }

  /**
   * Set the code for the gender for the individual customer.
   *
   * @param gender the code for the gender for the individual customer
   */
  @Override
  public void setGender(String gender) {
    super.setGender(gender);
  }

  /**
   * Set the given name, firstname, forename, or Christian name for the individual customer.
   *
   * @param givenName the given name, firstname, forename, or Christian name for the private
   *     individual customer
   */
  @Override
  public void setGivenName(String givenName) {
    super.setGivenName(givenName);
  }

  /**
   * Set the code for the home language for the individual customer.
   *
   * @param homeLanguage the code for the home language for the individual customer
   */
  @Override
  public void setHomeLanguage(String homeLanguage) {
    super.setHomeLanguage(homeLanguage);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the individual customer.
   *
   * @param id the Universally Unique Identifier (UUID) for the individual customer
   */
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the identity documents for the individual customer.
   *
   * @param identityDocuments the identity documents for the individual customer
   */
  @Override
  public void setIdentityDocuments(Set<IdentityDocument> identityDocuments) {
    super.setIdentityDocuments(identityDocuments);
  }

  /**
   * Set the initials for the individual customer.
   *
   * @param initials the initials for the individual customer
   */
  @Override
  public void setInitials(String initials) {
    super.setInitials(initials);
  }

  /**
   * Set the optional maiden name for the individual customer.
   *
   * @param maidenName the optional maiden name for the individual customer
   */
  @Override
  public void setMaidenName(String maidenName) {
    super.setMaidenName(maidenName);
  }

  /**
   * Set the code for the marital status for the individual customer.
   *
   * @param maritalStatus the code for the marital status for the individual customer
   */
  @Override
  public void setMaritalStatus(String maritalStatus) {
    super.setMaritalStatus(maritalStatus);
  }

  /**
   * Set the code for the marriage type for the individual customer if the private individual
   * customer is married.
   *
   * @param marriageType the code for the marriage type for the individual customer if the
   *     individual customer is married
   */
  @Override
  public void setMarriageType(String marriageType) {
    super.setMarriageType(marriageType);
  }

  /**
   * Set the optional middle names for the individual customer.
   *
   * @param middleNames the optional middle names for the individual customer
   */
  @Override
  public void setMiddleNames(String middleNames) {
    super.setMiddleNames(middleNames);
  }

  /**
   * Set the individual customeral name or full name of the individual customer.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the individual customer. This name should match the full name on the identity
   * document(s) associated with the individual customer.
   *
   * <p>See https://en.wikipedia.org/wiki/Personal_name
   *
   * @param name
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the code for the occupation for the individual customer.
   *
   * @param occupation the code for the occupation for the individual customer
   */
  @Override
  public void setOccupation(String occupation) {
    super.setOccupation(occupation);
  }

  /**
   * Set the physical addresses for the individual customer.
   *
   * @param physicalAddresses the physical addresses for the individual customer
   */
  @Override
  public void setPhysicalAddresses(Set<PhysicalAddress> physicalAddresses) {
    super.setPhysicalAddresses(physicalAddresses);
  }

  /**
   * Set the optional preferred name for the individual customer.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @param preferredName the optional preferred name for the individual customer
   */
  @Override
  public void setPreferredName(String preferredName) {
    super.setPreferredName(preferredName);
  }

  /**
   * Set the code for the race for the individual customer.
   *
   * @param race the code for the race for the individual customer
   */
  @Override
  public void setRace(String race) {
    super.setRace(race);
  }

  /**
   * Set the code for the residency status for the individual customer.
   *
   * @param residencyStatus the code for the residency status for the individual customer
   */
  @Override
  public void setResidencyStatus(String residencyStatus) {
    super.setResidencyStatus(residencyStatus);
  }

  /**
   * Set the code for the residential type for the individual customer.
   *
   * @param residentialType the code for the residential type for the individual customer
   */
  @Override
  public void setResidentialType(String residentialType) {
    super.setResidentialType(residentialType);
  }

  /**
   * Set the surname, last name, or family name for the individual customer.
   *
   * @param surname the surname, last name, or family name for the individual customer
   */
  @Override
  public void setSurname(String surname) {
    super.setSurname(surname);
  }

  /**
   * Set the optional tax number for the individual customer.
   *
   * @param taxNumber the optional tax number for the individual customer
   */
  @Override
  public void setTaxNumber(String taxNumber) {
    super.setTaxNumber(taxNumber);
  }

  /**
   * Set the optional code for the tax number type for the individual customer.
   *
   * @param taxNumberType the optional code for the tax number type for the private individual
   *     customer
   */
  @Override
  public void setTaxNumberType(String taxNumberType) {
    super.setTaxNumberType(taxNumberType);
  }

  /**
   * Set the code for the title for the individual customer.
   *
   * @param title the code for the title for the individual customer
   */
  @Override
  public void setTitle(String title) {
    super.setTitle(title);
  }
}
