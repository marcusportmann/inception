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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <b>Employment</b> class holds the information for an employment obtained by a person.
 *
 * <p>The primary key for the employment entity (ID) is a surrogate key to support the management of
 * related data in one or more external stores, e.g. a record of the person's employment stored in
 * an enterprise content management repository. This approach allows an entity to be modified
 * without impacting the related data's referential integrity, for example, when correcting an error
 * that occurred during the initial capture of the information for an employment.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An employment for a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "employerName",
  "employerPhoneNumber",
  "employerEmailAddress",
  "employerContactPerson",
  "employerAddressLine1",
  "employerAddressLine2",
  "employerAddressLine3",
  "employerAddressLine4",
  "employerAddressSuburb",
  "employerAddressCity",
  "employerAddressRegion",
  "employerAddressCountry",
  "employerAddressPostalCode",
  "startDate",
  "endDate",
  "type",
  "occupation"
})
@XmlRootElement(name = "Employment", namespace = "http://inception.digital/party")
@XmlType(
    name = "Employment",
    namespace = "http://inception.digital/party",
    propOrder = {
      "id",
      "employerName",
      "employerPhoneNumber",
      "employerEmailAddress",
      "employerContactPerson",
      "employerAddressLine1",
      "employerAddressLine2",
      "employerAddressLine3",
      "employerAddressLine4",
      "employerAddressSuburb",
      "employerAddressCity",
      "employerAddressRegion",
      "employerAddressCountry",
      "employerAddressPostalCode",
      "startDate",
      "endDate",
      "type",
      "occupation"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "employments")
public class Employment implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the employment was created. */
  @JsonIgnore
  @XmlTransient
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The employer address city. */
  @Schema(description = "The employer address city")
  @JsonProperty
  @XmlElement(name = "EmployerAddressCity")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerAddressCity.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}-., ]*(?!\\s+)$")
  @Column(name = "employer_address_city", length = 50)
  private String employerAddressCity;

  /** The ISO 3166-1 alpha-2 code for the employer address country. */
  @Schema(description = "The ISO 3166-1 alpha-2 code for the employer address country")
  @JsonProperty
  @XmlElement(name = "EmployerAddressCountry")
  @Size(min = 2, max = 2)
  @Column(name = "employer_address_country", length = 2)
  private String employerAddressCountry;

  /** The employer address line 1. */
  @Schema(description = "The employer address line 1")
  @JsonProperty
  @XmlElement(name = "EmployerAddressLine1")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerAddressLine1.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "employer_address_line1", length = 100)
  private String employerAddressLine1;

  /** The employer address line 2. */
  @Schema(description = "The employer address line 2")
  @JsonProperty
  @XmlElement(name = "EmployerAddressLine2")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerAddressLine2.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "employer_address_line2", length = 100)
  private String employerAddressLine2;

  /** The employer address line 3. */
  @Schema(description = "The employer address line 3")
  @JsonProperty
  @XmlElement(name = "EmployerAddressLine3")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerAddressLine3.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "employer_address_line3", length = 100)
  private String employerAddressLine3;

  /** The employer address line 4. */
  @Schema(description = "The employer address line 4")
  @JsonProperty
  @XmlElement(name = "EmployerAddressLine4")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerAddressLine4.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "employer_address_line4", length = 100)
  private String employerAddressLine4;

  /** The employer address postal code. */
  @Schema(description = "The employer address postal code")
  @JsonProperty
  @XmlElement(name = "EmployerAddressPostalCode")
  @Size(min = 1, max = 30)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerAddressPostalCode.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,# ]*(?!\\s+)$")
  @Column(name = "employer_address_postal_code", length = 30)
  private String employerAddressPostalCode;

  /** The ISO 3166-2 subdivision code for the employer address region. */
  @Schema(description = "The ISO 3166-2 subdivision code for the employer address region")
  @JsonProperty
  @XmlElement(name = "EmployerAddressRegion")
  @Size(min = 4, max = 6)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerAddressRegion.Pattern.message}",
      regexp = "[A-Z]{2}-[A-Z0-9]{1,3}")
  @Column(name = "employer_address_region", length = 6)
  private String employerAddressRegion;

  /** The employer address suburb. */
  @Schema(description = "The employer address suburb")
  @JsonProperty
  @XmlElement(name = "EmployerAddressSuburb")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerAddressSuburb.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}-., ]*(?!\\s+)$")
  @Column(name = "employer_address_suburb", length = 50)
  private String employerAddressSuburb;

  /** The employer contact person. */
  @Schema(description = "The employer contact person")
  @JsonProperty
  @XmlElement(name = "EmployerContactPerson")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerContactPerson.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}- ]*(?!\\s+)$")
  @Column(name = "employer_contact_person", length = 100)
  private String employerContactPerson;

  /** The employer e-mail address. */
  @Schema(description = "The employer e-mail address")
  @JsonProperty
  @XmlElement(name = "EmployerEmailAddress")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerEmailAddress.Pattern.message}",
      regexp =
          "^$|(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
  @Column(name = "employer_email_address", length = 100)
  private String employerEmailAddress;

  /** The employer name. */
  @Schema(description = "The employer name", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "EmployerName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerName.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-., ]*(?!\\s+)$")
  @Column(name = "employer_name", length = 100, nullable = false)
  private String employerName;

  /** The employer phone number. */
  @Schema(description = "The employer phone number")
  @JsonProperty
  @XmlElement(name = "EmployerPhoneNumber")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.Employment.EmployerPhoneNumber.Pattern.message}",
      regexp = "^(?!\\s+)[0-9|+-., ()]*(?!\\s+)$")
  @Column(name = "employer_phone_number", length = 50)
  private String employerPhoneNumber;

  /** The end date for the employment. */
  @Schema(description = "The end date for the employment")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EndDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "end_date")
  private LocalDate endDate;

  /** The ID for the employment. */
  @Schema(description = "The ID for the employment", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The code for the occupation for the employment. */
  @Schema(description = "The code for the occupation for the employment")
  @JsonProperty
  @XmlElement(name = "Occupation")
  @Size(min = 1, max = 30)
  @Column(name = "occupation", length = 30)
  private String occupation;

  /** The person the employment is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("employmentReference")
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id")
  private Person person;

  /** The start date for the employment. */
  @Schema(description = "The start date for the employment", required = true)
  @JsonProperty(required = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "StartDate", required = true)
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  /** The code for the employment type for the employment. */
  @Schema(description = "The code for the employment type for the employment")
  @JsonProperty
  @XmlElement(name = "Type")
  @Size(min = 1, max = 30)
  @Column(name = "type", length = 30)
  private String type;

  /** The date and time the employment was last updated. */
  @JsonIgnore
  @XmlTransient
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>Employment</b>. */
  public Employment() {}

  /**
   * Constructs a new <b>Employment</b>.
   *
   * @param employerName the employer name
   * @param employerPhoneNumber the employer phone number
   * @param employerEmailAddress the employer e-mail address
   * @param employerContactPerson the employer contact person
   * @param employerAddressLine1 the employer address line 1
   * @param employerAddressLine2 the employer address line 2
   * @param employerAddressLine3 the employer address line 3
   * @param employerAddressLine4 the employer address line 4
   * @param employerAddressSuburb the employer address suburb
   * @param employerAddressCity the employer address city
   * @param employerAddressRegion the ISO 3166-2 subdivision code for the employer address region
   * @param employerAddressCountry the ISO 3166-1 alpha-2 code for the employer address country
   * @param employerAddressPostalCode the employer address postal code
   * @param startDate the start date for the employment
   * @param endDate the end date for the employment
   * @param type the code for the employment type for the employment
   * @param occupation the code for the occupation for the employment
   */
  public Employment(
      String employerName,
      String employerPhoneNumber,
      String employerEmailAddress,
      String employerContactPerson,
      String employerAddressLine1,
      String employerAddressLine2,
      String employerAddressLine3,
      String employerAddressLine4,
      String employerAddressSuburb,
      String employerAddressCity,
      String employerAddressRegion,
      String employerAddressCountry,
      String employerAddressPostalCode,
      LocalDate startDate,
      LocalDate endDate,
      String type,
      String occupation) {
    this.id = UuidCreator.getShortPrefixComb();
    this.employerName = employerName;
    this.employerPhoneNumber = employerPhoneNumber;
    this.employerEmailAddress = employerEmailAddress;
    this.employerContactPerson = employerContactPerson;
    this.employerAddressLine1 = employerAddressLine1;
    this.employerAddressLine2 = employerAddressLine2;
    this.employerAddressLine3 = employerAddressLine3;
    this.employerAddressLine4 = employerAddressLine4;
    this.employerAddressSuburb = employerAddressSuburb;
    this.employerAddressCity = employerAddressCity;
    this.employerAddressRegion = employerAddressRegion;
    this.employerAddressCountry = employerAddressCountry;
    this.employerAddressPostalCode = employerAddressPostalCode;
    this.startDate = startDate;
    this.endDate = endDate;
    this.type = type;
    this.occupation = occupation;
  }

  /**
   * Constructs a new <b>Employment</b>.
   *
   * @param employerName the employer name
   * @param employerPhoneNumber the employer phone number
   * @param employerEmailAddress the employer e-mail address
   * @param employerContactPerson the employer contact person
   * @param employerAddressLine1 the employer address line 1
   * @param employerAddressLine2 the employer address line 2
   * @param employerAddressLine3 the employer address line 3
   * @param employerAddressLine4 the employer address line 4
   * @param employerAddressSuburb the employer address suburb
   * @param employerAddressCity the employer address city
   * @param employerAddressRegion the ISO 3166-2 subdivision code for the employer address region
   * @param employerAddressCountry the ISO 3166-1 alpha-2 code for the employer address country
   * @param employerAddressPostalCode the employer address postal code
   * @param startDate the start date for the employment
   * @param type the code for the employment type for the employment
   * @param occupation the code for the occupation for the employment
   */
  public Employment(
      String employerName,
      String employerPhoneNumber,
      String employerEmailAddress,
      String employerContactPerson,
      String employerAddressLine1,
      String employerAddressLine2,
      String employerAddressLine3,
      String employerAddressLine4,
      String employerAddressSuburb,
      String employerAddressCity,
      String employerAddressRegion,
      String employerAddressCountry,
      String employerAddressPostalCode,
      LocalDate startDate,
      String type,
      String occupation) {
    this.id = UuidCreator.getShortPrefixComb();
    this.employerName = employerName;
    this.employerPhoneNumber = employerPhoneNumber;
    this.employerEmailAddress = employerEmailAddress;
    this.employerContactPerson = employerContactPerson;
    this.employerAddressLine1 = employerAddressLine1;
    this.employerAddressLine2 = employerAddressLine2;
    this.employerAddressLine3 = employerAddressLine3;
    this.employerAddressLine4 = employerAddressLine4;
    this.employerAddressSuburb = employerAddressSuburb;
    this.employerAddressCity = employerAddressCity;
    this.employerAddressRegion = employerAddressRegion;
    this.employerAddressCountry = employerAddressCountry;
    this.employerAddressPostalCode = employerAddressPostalCode;
    this.startDate = startDate;
    this.type = type;
    this.occupation = occupation;
  }

  /**
   * Constructs a new <b>Employment</b>.
   *
   * @param employerName the employer name
   * @param startDate the start date for the employment
   * @param endDate the end date for the employment
   */
  public Employment(String employerName, LocalDate startDate, LocalDate endDate) {
    this.id = UuidCreator.getShortPrefixComb();
    this.employerName = employerName;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * Constructs a new <b>Employment</b>.
   *
   * @param employerName the employer name
   * @param startDate the start date for the employment
   */
  public Employment(String employerName, LocalDate startDate) {
    this.id = UuidCreator.getShortPrefixComb();
    this.employerName = employerName;
    this.startDate = startDate;
  }

  /**
   * Constructs a new <b>Employment</b>.
   *
   * @param employerName the employer name
   * @param startDate the start date for the employment
   * @param endDate the end date for the employment
   * @param type the code for the employment type for the employment
   * @param occupation the code for the occupation for the employment
   */
  public Employment(
      String employerName, LocalDate startDate, LocalDate endDate, String type, String occupation) {
    this.id = UuidCreator.getShortPrefixComb();
    this.employerName = employerName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.type = type;
    this.occupation = occupation;
  }

  /**
   * Constructs a new <b>Employment</b>.
   *
   * @param employerName the employer name
   * @param startDate the start date for the employment
   * @param type the code for the employment type for the employment
   * @param occupation the code for the occupation for the employment
   */
  public Employment(String employerName, LocalDate startDate, String type, String occupation) {
    this.id = UuidCreator.getShortPrefixComb();
    this.employerName = employerName;
    this.startDate = startDate;
    this.type = type;
    this.occupation = occupation;
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

    Employment other = (Employment) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the employment was created.
   *
   * @return the date and time the employment was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the employer address city.
   *
   * @return the employer address city
   */
  public String getEmployerAddressCity() {
    return employerAddressCity;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the employer address country.
   *
   * @return the ISO 3166-1 alpha-2 code for the employer address country
   */
  public String getEmployerAddressCountry() {
    return employerAddressCountry;
  }

  /**
   * Returns the employer address line 1.
   *
   * @return the employer address line 1
   */
  public String getEmployerAddressLine1() {
    return employerAddressLine1;
  }

  /**
   * Returns the employer address line 2.
   *
   * @return the employer address line 2
   */
  public String getEmployerAddressLine2() {
    return employerAddressLine2;
  }

  /**
   * Returns the employer address line 3.
   *
   * @return the employer address line 3
   */
  public String getEmployerAddressLine3() {
    return employerAddressLine3;
  }

  /**
   * Set the employer address line 4.
   *
   * @return the employer address line 4
   */
  public String getEmployerAddressLine4() {
    return employerAddressLine4;
  }

  /**
   * Returns the employer address postal code.
   *
   * @return the employer address postal code
   */
  public String getEmployerAddressPostalCode() {
    return employerAddressPostalCode;
  }

  /**
   * Returns the ISO 3166-2 subdivision code for the employer address region.
   *
   * @return the ISO 3166-2 subdivision code for the employer address region
   */
  public String getEmployerAddressRegion() {
    return employerAddressRegion;
  }

  /**
   * Returns the employer address suburb.
   *
   * @return the employer address suburb
   */
  public String getEmployerAddressSuburb() {
    return employerAddressSuburb;
  }

  /**
   * Returns the employer contact person.
   *
   * @return the employer contact person
   */
  public String getEmployerContactPerson() {
    return employerContactPerson;
  }

  /**
   * Returns the employer e-mail address.
   *
   * @return the employer e-mail address
   */
  public String getEmployerEmailAddress() {
    return employerEmailAddress;
  }

  /**
   * Returns the employer name.
   *
   * @return the employer name
   */
  public String getEmployerName() {
    return employerName;
  }

  /**
   * Returns the employer phone number.
   *
   * @return the employer phone number
   */
  public String getEmployerPhoneNumber() {
    return employerPhoneNumber;
  }

  /**
   * Returns the end date for the employment.
   *
   * @return the end date for the employment
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  /**
   * Returns the ID for the employment.
   *
   * @return the ID for the employment
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the code for the occupation for the employment.
   *
   * @return the code for the occupation for the employment
   */
  public String getOccupation() {
    return occupation;
  }

  /**
   * Returns the person the employment is associated with.
   *
   * @return the person the employment is associated with
   */
  @Schema(hidden = true)
  public Person getPerson() {
    return person;
  }

  /**
   * Returns the start date for the employment.
   *
   * @return the start date for the employment
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * Returns the code for the employment type for the employment.
   *
   * @return the code for the employment type for the employment
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the employment was last updated.
   *
   * @return the date and time the employment was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
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
   * Set the employer address city.
   *
   * @param employerAddressCity the employer address city
   */
  public void setEmployerAddressCity(String employerAddressCity) {
    this.employerAddressCity = employerAddressCity;
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the employer address country.
   *
   * @param employerAddressCountry the ISO 3166-1 alpha-2 code for the employer address country
   */
  public void setEmployerAddressCountry(String employerAddressCountry) {
    this.employerAddressCountry = employerAddressCountry;
  }

  /**
   * Set the employer address line 1.
   *
   * @param employerAddressLine1 the employer address line 1
   */
  public void setEmployerAddressLine1(String employerAddressLine1) {
    this.employerAddressLine1 = employerAddressLine1;
  }

  /**
   * Set the employer address line 2.
   *
   * @param employerAddressLine2 the employer address line 2
   */
  public void setEmployerAddressLine2(String employerAddressLine2) {
    this.employerAddressLine2 = employerAddressLine2;
  }

  /**
   * Set the employer address line 3.
   *
   * @param employerAddressLine3 the employer address line 3
   */
  public void setEmployerAddressLine3(String employerAddressLine3) {
    this.employerAddressLine3 = employerAddressLine3;
  }

  /**
   * Returns the employer address line 4.
   *
   * @param employerAddressLine4 the employer address line 4
   */
  public void setEmployerAddressLine4(String employerAddressLine4) {
    this.employerAddressLine4 = employerAddressLine4;
  }

  /**
   * Set the employer address postal code.
   *
   * @param employerAddressPostalCode the employer address postal code
   */
  public void setEmployerAddressPostalCode(String employerAddressPostalCode) {
    this.employerAddressPostalCode = employerAddressPostalCode;
  }

  /**
   * Set the ISO 3166-2 subdivision code for the employer address region.
   *
   * @param employerAddressRegion the ISO 3166-2 subdivision code for the employer address region
   */
  public void setEmployerAddressRegion(String employerAddressRegion) {
    this.employerAddressRegion = employerAddressRegion;
  }

  /**
   * Set the employer address suburb.
   *
   * @param employerAddressSuburb the employer address suburb
   */
  public void setEmployerAddressSuburb(String employerAddressSuburb) {
    this.employerAddressSuburb = employerAddressSuburb;
  }

  /**
   * Set the employer contact person.
   *
   * @param employerContactPerson the employer contact person
   */
  public void setEmployerContactPerson(String employerContactPerson) {
    this.employerContactPerson = employerContactPerson;
  }

  /**
   * Set the employer e-mail address.
   *
   * @param employerEmailAddress the employer e-mail address
   */
  public void setEmployerEmailAddress(String employerEmailAddress) {
    this.employerEmailAddress = employerEmailAddress;
  }

  /**
   * Set the employer name.
   *
   * @param employerName the employer name
   */
  public void setEmployerName(String employerName) {
    this.employerName = employerName;
  }

  /**
   * Set the employer phone number.
   *
   * @param employerPhoneNumber the employer phone number
   */
  public void setEmployerPhoneNumber(String employerPhoneNumber) {
    this.employerPhoneNumber = employerPhoneNumber;
  }

  /**
   * Set the end date for the employment.
   *
   * @param endDate the end date for the employment
   */
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  /**
   * Set the ID for the employment.
   *
   * @param id the ID for the employment
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the code for the occupation for the employment.
   *
   * @param occupation the code for the occupation for the employment
   */
  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  /**
   * Set the person the employment is associated with.
   *
   * @param person the person the employment is associated with
   */
  @Schema(hidden = true)
  public void setPerson(Person person) {
    this.person = person;
  }

  /**
   * Set the start date for the employment.
   *
   * @param startDate the start date for the employment
   */
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   * Set the code for the employment type for the employment.
   *
   * @param type the code for the employment type for the employment
   */
  public void setType(String type) {
    this.type = type;
  }

  /** The Java Persistence callback method invoked before the entity is created in the database. */
  @PrePersist
  protected void onCreate() {
    created = LocalDateTime.now();
  }

  /** The Java Persistence callback method invoked before the entity is updated in the database. */
  @PreUpdate
  protected void onUpdate() {
    updated = LocalDateTime.now();
  }
}
