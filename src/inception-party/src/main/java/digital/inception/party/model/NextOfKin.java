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

package digital.inception.party.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code NextOfKin} class holds the information for a next of kin for a person.
 *
 * <p>The primary key for the next of kin entity (ID) is a surrogate key to support the management
 * of related data in one or more external stores, e.g. a copy of an identification for the next of
 * kin stored in an enterprise content management persistence. This approach allows an entity to be
 * modified without impacting the related data's referential integrity, for example, when correcting
 * an error that occurred during the initial capture of the information for a next of kin.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A next of kin for a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "type",
  "name",
  "givenName",
  "surname",
  "homeNumber",
  "workNumber",
  "mobileNumber",
  "emailAddress",
  "addressLine1",
  "addressLine2",
  "addressLine3",
  "addressLine4",
  "addressSuburb",
  "addressCity",
  "addressRegion",
  "addressCountry",
  "addressPostalCode"
})
@XmlRootElement(name = "NextOfKin", namespace = "https://inception.digital/party")
@XmlType(
    name = "NextOfKin",
    namespace = "https://inception.digital/party",
    propOrder = {
      "id",
      "type",
      "name",
      "givenName",
      "surname",
      "homeNumber",
      "workNumber",
      "mobileNumber",
      "emailAddress",
      "addressLine1",
      "addressLine2",
      "addressLine3",
      "addressLine4",
      "addressSuburb",
      "addressCity",
      "addressRegion",
      "addressCountry",
      "addressPostalCode"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_next_of_kin")
public class NextOfKin implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The next of kin address city. */
  @Schema(description = "The next of kin address city")
  @JsonProperty
  @XmlElement(name = "AddressCity")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.AddressCity.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}-., ]*(?!\\s+)$")
  @Column(name = "address_city", length = 50)
  private String addressCity;

  /** The ISO 3166-1 alpha-2 code for the next of kin address country. */
  @Schema(description = "The ISO 3166-1 alpha-2 code for the next of kin address country")
  @JsonProperty
  @XmlElement(name = "AddressCountry")
  @Size(min = 2, max = 2)
  @Column(name = "address_country", length = 2)
  private String addressCountry;

  /** The next of kin address line 1. */
  @Schema(description = "The next of kin address line 1")
  @JsonProperty
  @XmlElement(name = "AddressLine1")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.AddressLine1.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "address_line1", length = 100)
  private String addressLine1;

  /** The next of kin address line 2. */
  @Schema(description = "The next of kin address line 2")
  @JsonProperty
  @XmlElement(name = "AddressLine2")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.AddressLine2.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "address_line2", length = 100)
  private String addressLine2;

  /** The next of kin address line 3. */
  @Schema(description = "The next of kin address line 3")
  @JsonProperty
  @XmlElement(name = "AddressLine3")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.AddressLine3.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "address_line3", length = 100)
  private String addressLine3;

  /** The next of kin address line 4. */
  @Schema(description = "The next of kin address line 4")
  @JsonProperty
  @XmlElement(name = "AddressLine4")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.AddressLine4.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "address_line4", length = 100)
  private String addressLine4;

  /** The next of kin address postal code. */
  @Schema(description = "The next of kin address postal code")
  @JsonProperty
  @XmlElement(name = "AddressPostalCode")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.AddressPostalCode.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,# ]*(?!\\s+)$")
  @Column(name = "address_postal_code", length = 50)
  private String addressPostalCode;

  /** The ISO 3166-2 subdivision code for the next of kin address region. */
  @Schema(description = "The ISO 3166-2 subdivision code for the next of kin address region")
  @JsonProperty
  @XmlElement(name = "AddressRegion")
  @Size(min = 4, max = 6)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.AddressRegion.Pattern.message}",
      regexp = "[A-Z]{2}-[A-Z0-9]{1,3}")
  @Column(name = "address_region", length = 6)
  private String addressRegion;

  /** The next of kin address suburb. */
  @Schema(description = "The next of kin address suburb")
  @JsonProperty
  @XmlElement(name = "AddressSuburb")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.AddressSuburb.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}-., ]*(?!\\s+)$")
  @Column(name = "address_suburb", length = 50)
  private String addressSuburb;

  /** The email address for the next of kin. */
  @Schema(description = "The email address for the next of kin")
  @JsonProperty
  @XmlElement(name = "EmailAddress")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.EmailAddress.Pattern.message}",
      regexp =
          "^$|(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
  @Column(name = "email_address", length = 100)
  private String emailAddress;

  /** The given name, firstname, forename, or Christian name for the next of kin. */
  @Schema(
      description = "The given name, firstname, forename, or Christian name for the next of kin")
  @JsonProperty
  @XmlElement(name = "GivenName")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.GivenName.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}- ]*(?!\\s+)$")
  @Column(name = "given_name", length = 100)
  private String givenName;

  /** The home phone number for the next of kin. */
  @Schema(description = "The home phone number for the next of kin")
  @JsonProperty
  @XmlElement(name = "HomeNumber")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.HomeNumber.Pattern.message}",
      regexp = "^(?!\\s+)[0-9|+-., ()]*(?!\\s+)$")
  @Column(name = "home_number", length = 50)
  private String homeNumber;

  /** The ID for the next of kin. */
  @Schema(description = "The ID for the next of kin", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The mobile number for the next of kin. */
  @Schema(description = "The mobile number for the next of kin")
  @JsonProperty
  @XmlElement(name = "MobileNumber")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.MobileNumber.Pattern.message}",
      regexp = "^(?!\\s+)[0-9|+-., ()]*(?!\\s+)$")
  @Column(name = "mobile_number", length = 50)
  private String mobileNumber;

  /** The name of the next of kin. */
  @Schema(description = "The name of the next of kin", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.Name.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}- ]*(?!\\s+)$")
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The ID for the person the next of kin is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Column(name = "person_id", nullable = false)
  private UUID personId;

  /** The surname, last name, or family name for the next of kin. */
  @Schema(description = "The surname, last name, or family name for the next of kin")
  @JsonProperty
  @XmlElement(name = "Surname")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.Surname.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}- ]*(?!\\s+)$")
  @Column(name = "surname", length = 100)
  private String surname;

  /** The code for the next of kin type for the next of kin. */
  @Schema(description = "The code for the next of kin type for the next of kin")
  @JsonProperty
  @XmlElement(name = "Type")
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "type", length = 50)
  private String type;

  /** The work phone number for the next of kin. */
  @Schema(description = "The work phone number for the next of kin")
  @JsonProperty
  @XmlElement(name = "WorkNumber")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.NextOfKin.WorkNumber.Pattern.message}",
      regexp = "^(?!\\s+)[0-9|+-., ()]*(?!\\s+)$")
  @Column(name = "work_number", length = 50)
  private String workNumber;

  /** Constructs a new {@code NextOfKin}. */
  public NextOfKin() {}

  /**
   * Constructs a new {@code NextOfKin}.
   *
   * @param type the code for the next of kin type for the next of kin
   * @param name the name of the next of kin
   * @param givenName the given name, firstname, forename, or Christian name for the next of kin
   * @param surname the surname, last name, or family name for the next of kin
   * @param homeNumber the home phone number for the next of kin
   * @param workNumber the work phone number for the next of kin
   * @param mobileNumber the mobile number for the next of kin
   * @param emailAddress the email address for the next of kin
   * @param addressLine1 the next of kin address line 1
   * @param addressLine2 the next of kin address line 2
   * @param addressLine3 the next of kin address line 3
   * @param addressLine4 the next of kin address line 4
   * @param addressSuburb the next of kin address suburb
   * @param addressCity the next of kin address city
   * @param addressRegion the ISO 3166-2 subdivision code for the next of kin address region
   * @param addressCountry the ISO 3166-1 alpha-2 code for the next of kin address country
   * @param addressPostalCode the next of kin address postal code
   */
  public NextOfKin(
      String type,
      String name,
      String givenName,
      String surname,
      String homeNumber,
      String workNumber,
      String mobileNumber,
      String emailAddress,
      String addressLine1,
      String addressLine2,
      String addressLine3,
      String addressLine4,
      String addressSuburb,
      String addressCity,
      String addressRegion,
      String addressCountry,
      String addressPostalCode) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.type = type;
    this.name = name;
    this.givenName = givenName;
    this.surname = surname;
    this.homeNumber = homeNumber;
    this.workNumber = workNumber;
    this.mobileNumber = mobileNumber;
    this.emailAddress = emailAddress;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.addressLine3 = addressLine3;
    this.addressLine4 = addressLine4;
    this.addressSuburb = addressSuburb;
    this.addressCity = addressCity;
    this.addressRegion = addressRegion;
    this.addressCountry = addressCountry;
    this.addressPostalCode = addressPostalCode;
  }

  /**
   * Constructs a new {@code NextOfKin}.
   *
   * @param type the code for the next of kin type for the next of kin
   * @param name the name of the next of kin
   * @param homeNumber the home phone number for the next of kin
   * @param mobileNumber the mobile number for the next of kin
   * @param emailAddress the email address for the next of kin
   */
  public NextOfKin(
      String type, String name, String homeNumber, String mobileNumber, String emailAddress) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.type = type;
    this.name = name;
    this.homeNumber = homeNumber;
    this.mobileNumber = mobileNumber;
    this.emailAddress = emailAddress;
  }

  /**
   * Constructs a new {@code NextOfKin}.
   *
   * @param type the code for the next of kin type for the next of kin
   * @param name the name of the next of kin
   */
  public NextOfKin(String type, String name) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.type = type;
    this.name = name;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    NextOfKin other = (NextOfKin) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the next of kin address city.
   *
   * @return the next of kin address city
   */
  public String getAddressCity() {
    return addressCity;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the next of kin address country.
   *
   * @return the ISO 3166-1 alpha-2 code for the next of kin address country
   */
  public String getAddressCountry() {
    return addressCountry;
  }

  /**
   * Returns the next of kin address line 1.
   *
   * @return the next of kin address line 1
   */
  public String getAddressLine1() {
    return addressLine1;
  }

  /**
   * Returns the next of kin address line 2.
   *
   * @return the next of kin address line 2
   */
  public String getAddressLine2() {
    return addressLine2;
  }

  /**
   * Returns the next of kin address line 3.
   *
   * @return the next of kin address line 3
   */
  public String getAddressLine3() {
    return addressLine3;
  }

  /**
   * Returns the next of kin address line 4.
   *
   * @return the next of kin address line 4
   */
  public String getAddressLine4() {
    return addressLine4;
  }

  /**
   * Returns the next of kin address postal code.
   *
   * @return the next of kin address postal code
   */
  public String getAddressPostalCode() {
    return addressPostalCode;
  }

  /**
   * Returns the ISO 3166-2 subdivision code for the next of kin address region.
   *
   * @return the ISO 3166-2 subdivision code for the next of kin address region
   */
  public String getAddressRegion() {
    return addressRegion;
  }

  /**
   * Returns the next of kin address suburb.
   *
   * @return the next of kin address suburb
   */
  public String getAddressSuburb() {
    return addressSuburb;
  }

  /**
   * Returns the email address for the next of kin.
   *
   * @return the email address for the next of kin
   */
  public String getEmailAddress() {
    return emailAddress;
  }

  /**
   * Returns the given name, firstname, forename, or Christian name for the next of kin.
   *
   * @return the given name, firstname, forename, or Christian name for the next of kin
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * Returns the home phone number for the next of kin.
   *
   * @return the home phone number for the next of kin
   */
  public String getHomeNumber() {
    return homeNumber;
  }

  /**
   * Returns the ID for the next of kin.
   *
   * @return the ID for the next of kin
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the mobile number for the next of kin.
   *
   * @return the mobile number for the next of kin
   */
  public String getMobileNumber() {
    return mobileNumber;
  }

  /**
   * Returns the name of the next of kin.
   *
   * @return the name of the next of kin
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the surname, last name, or family name for the next of kin.
   *
   * @return the surname, last name, or family name for the next of kin
   */
  public String getSurname() {
    return surname;
  }

  /**
   * Returns the code for the next of kin type for the next of kin.
   *
   * @return the code for the next of kin type for the next of kin
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the work phone number for the next of kin.
   *
   * @return the work phone number for the next of kin
   */
  public String getWorkNumber() {
    return workNumber;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Set the next of kin address city.
   *
   * @param addressCity the next of kin address city
   */
  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the next of kin address country.
   *
   * @param addressCountry the ISO 3166-1 alpha-2 code for the next of kin address country
   */
  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  /**
   * Set the next of kin address line 1.
   *
   * @param addressLine1 the next of kin address line 1
   */
  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  /**
   * Set the next of kin address line 2
   *
   * @param addressLine2 the next of kin address line 2
   */
  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  /**
   * Set the next of kin address line 3.
   *
   * @param addressLine3 the next of kin address line 3
   */
  public void setAddressLine3(String addressLine3) {
    this.addressLine3 = addressLine3;
  }

  /**
   * Set the next of kin address line 4.
   *
   * @param addressLine4 the next of kin address line 4
   */
  public void setAddressLine4(String addressLine4) {
    this.addressLine4 = addressLine4;
  }

  /**
   * Set the next of kin address postal code.
   *
   * @param addressPostalCode the next of kin address postal code
   */
  public void setAddressPostalCode(String addressPostalCode) {
    this.addressPostalCode = addressPostalCode;
  }

  /**
   * Set the ISO 3166-2 subdivision code for the next of kin address region.
   *
   * @param addressRegion the ISO 3166-2 subdivision code for the next of kin address region
   */
  public void setAddressRegion(String addressRegion) {
    this.addressRegion = addressRegion;
  }

  /**
   * Set the next of kin address suburb.
   *
   * @param addressSuburb the next of kin address suburb
   */
  public void setAddressSuburb(String addressSuburb) {
    this.addressSuburb = addressSuburb;
  }

  /**
   * Set the email address for the next of kin.
   *
   * @param emailAddress the email address for the next of kin
   */
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  /**
   * Set the given name, firstname, forename, or Christian name for the next of kin.
   *
   * @param givenName the given name, firstname, forename, or Christian name for the next of kin
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   * Set the home number for the next of kin.
   *
   * @param homeNumber the home number for the next of kin
   */
  public void setHomeNumber(String homeNumber) {
    this.homeNumber = homeNumber;
  }

  /**
   * Get the ID for the next of kin.
   *
   * @param id the ID for the next of kin
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the mobile number for the next of kin.
   *
   * @param mobileNumber the mobile number for the next of kin
   */
  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  /**
   * Set the name of the next of kin.
   *
   * @param name the name of the next of kin
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the person the next of kin is associated with.
   *
   * @param person the person the next of kin is associated with
   */
  @JsonBackReference("nextOfKinReference")
  @Schema(hidden = true)
  public void setPerson(Person person) {
    if (person != null) {
      this.personId = person.getId();
    } else {
      this.personId = null;
    }
  }

  /**
   * Set the surname, last name, or family name for the next of kin.
   *
   * @param surname the surname, last name, or family name for the next of kin
   */
  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   * Set the code for the next of kin type for the next of kin.
   *
   * @param type the code for the next of kin type for the next of kin
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Set the work number for the next of kin.
   *
   * @param workNumber the work number for the next of kin
   */
  public void setWorkNumber(String workNumber) {
    this.workNumber = workNumber;
  }

  /**
   * Called by the JAXB runtime an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof Person parent) {
      setPerson(parent);
    }
  }
}
