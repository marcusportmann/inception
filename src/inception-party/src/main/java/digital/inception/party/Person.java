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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Person</code> class holds the information for a person.
 *
 * <p>This entity may be used under different circumstances when more or less information is
 * available for a person. As a result, most of the attributes are optional and the particular
 * application making use of this entity should enforce which attributes are mandatory.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "name",
  "preferredName",
  "title",
  "givenName",
  "middleNames",
  "initials",
  "surname",
  "maidenName",
  "gender",
  "race",
  "maritalStatus",
  "marriageType",
  "dateOfBirth",
  "dateOfDeath",
  "countryOfBirth",
  "identityDocuments"
})
@XmlRootElement(name = "Person", namespace = "http://party.inception.digital")
@XmlType(
    name = "Person",
    namespace = "http://party.inception.digital",
    propOrder = {
      "preferredName",
      "title",
      "givenName",
      "middleNames",
      "initials",
      "surname",
      "maidenName",
      "gender",
      "race",
      "maritalStatus",
      "marriageType",
      "dateOfBirth",
      "dateOfDeath",
      "countryOfBirth",
      "identityDocuments"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "parties")
@SecondaryTable(
    schema = "party",
    name = "persons",
    pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")})
public class Person {

  /** The identity documents for the person. */
  @Schema(description = "The identity documents for the person")
  @JsonProperty
  @JsonManagedReference
  @XmlElementWrapper(name = "IdentityDocuments")
  @XmlElement(name = "IdentityDocument")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(
      name = "person_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false,
      nullable = false)
  private final Set<IdentityDocument> identityDocuments = new HashSet<>();

  /** The type of party for the person. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Column(table = "parties", name = "type", nullable = false)
  private final PartyType partyType = PartyType.PERSON;

  /** The optional code identifying the country of birth for the person. */
  @Schema(description = "The optional code identifying the country of birth for the person")
  @JsonProperty
  @XmlElement(name = "CountryOfBirth")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "country_of_birth", length = 10)
  private String countryOfBirth;

  /** The date and time the person was created. */
  @JsonIgnore
  @XmlTransient
  @Column(table = "persons", name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The optional date of birth for the person. */
  @Schema(description = "The optional date of birth for the person")
  @JsonProperty
  @XmlElement(name = "DateOfBirth")
  @Column(table = "persons", name = "date_of_birth")
  private LocalDate dateOfBirth;

  /** The optional date of death for the person. */
  @Schema(description = "The optional date of death for the person")
  @JsonProperty
  @XmlElement(name = "DateOfDeath")
  @Column(table = "persons", name = "date_of_death")
  private LocalDate dateOfDeath;

  /** The optional code identifying the gender for the person. */
  @Schema(description = "The optional code identifying the gender for the person")
  @JsonProperty
  @XmlElement(name = "Gender")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "gender")
  private String gender;

  /** The optional given name, firstname, forename, or Christian name for the person. */
  @Schema(
      description =
          "The optional given name, firstname, forename, or Christian name for the person")
  @JsonProperty
  @XmlElement(name = "GivenName")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "given_name", length = 100)
  private String givenName;

  /** The Universally Unique Identifier (UUID) uniquely identifying the person. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) uniquely identifying the person",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The optional initials for the person. */
  @Schema(description = "The optional initials for the person")
  @JsonProperty
  @XmlElement(name = "Initials")
  @Size(min = 1, max = 20)
  @Column(table = "persons", name = "initials", length = 20)
  private String initials;

  /** The optional maiden name for the person. */
  @Schema(description = "The optional maiden name for the person")
  @JsonProperty
  @XmlElement(name = "MaidenName")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "maiden_name", length = 100)
  private String maidenName;

  /** The optional code identifying the marital status for the person. */
  @Schema(description = "The optional code identifying the marital status for the person")
  @JsonProperty
  @XmlElement(name = "MaritalStatus")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "marital_status")
  private String maritalStatus;

  /** The optional code identifying the marriage type for the person if the person is married. */
  @Schema(
      description =
          "The optional code identifying the marriage type for the person if the person is married")
  @JsonProperty
  @XmlElement(name = "MarriageType")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "marriage_type")
  private String marriageType;

  /** The optional middle names for the person. */
  @Schema(description = "The optional middle names for the person")
  @JsonProperty
  @XmlElement(name = "MiddleNames")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "middle_names", length = 100)
  private String middleNames;

  /**
   * The personal name or full name of the person.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the person. This name should match the full name on the identity document(s)
   * associated with the person.
   *
   * <p>See https://en.wikipedia.org/wiki/Personal_name
   */
  @Schema(description = "The personal name or full name of the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(table = "parties", name = "name", nullable = false, length = 100)
  private String name;

  /** The date and time the party associated with the person was created. */
  @JsonIgnore
  @XmlTransient
  @Column(table = "parties", name = "created", nullable = false, updatable = false)
  private LocalDateTime partyCreated;

  /** The date and time the party associated with the person was last updated. */
  @JsonIgnore
  @XmlTransient
  @Column(table = "parties", name = "updated", insertable = false)
  private LocalDateTime partyUpdated;

  /**
   * The optional preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   */
  @Schema(description = "The optional preferred name for the person")
  @JsonProperty
  @XmlElement(name = "PreferredName")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "preferred_name", length = 100)
  private String preferredName;

  /** The optional code identifying the race for the person. */
  @Schema(description = "The optional code identifying the race for the person")
  @JsonProperty
  @XmlElement(name = "Race")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "race")
  private String race;

  /** The optional surname, last name, or family name for the person. */
  @Schema(description = "The optional surname, last name, or family name for the person")
  @JsonProperty
  @XmlElement(name = "Surname")
  @Size(min = 1, max = 100)
  @Column(table = "persons", name = "surname", length = 100)
  private String surname;

  /** The optional code identifying the title for the person. */
  @Schema(description = "The optional code identifying the title for the person")
  @JsonProperty
  @XmlElement(name = "Title")
  @Size(min = 1, max = 10)
  @Column(table = "persons", name = "title")
  private String title;

  /** The date and time the person was last updated. */
  @JsonIgnore
  @XmlTransient
  @Column(table = "persons", name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <code>Person</code>. */
  public Person() {}

  /**
   * Add the identity document for the person
   *
   * @param identityDocument the identity document
   */
  public void addIdentityDocument(IdentityDocument identityDocument) {
    identityDocument.setPerson(this);

    this.identityDocuments.add(identityDocument);
  }

  /**
   * Returns the optional code identifying the country of birth for the person.
   *
   * @return the optional code identifying the country of birth for the person
   */
  public String getCountryOfBirth() {
    return countryOfBirth;
  }

  /**
   * Returns the date and time the person was created.
   *
   * @return the date and time the person was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the optional date of birth for the person.
   *
   * @return the optional date of birth for the person
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Returns the optional date of death for the person.
   *
   * @return the optional date of death for the person
   */
  public LocalDate getDateOfDeath() {
    return dateOfDeath;
  }

  /**
   * Returns the optional code identifying the gender for the person.
   *
   * @return the optional code identifying the gender for the person
   */
  public String getGender() {
    return gender;
  }

  /**
   * Returns the optional given name, firstname, forename, or Christian name for the person.
   *
   * @return the optional given name, firstname, forename, or Christian name for the person
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the person.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the person
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the identity documents for the person.
   *
   * @return the identity documents for the person
   */
  public Set<IdentityDocument> getIdentityDocuments() {
    return identityDocuments;
  }

  /**
   * Returns the optional initials for the person.
   *
   * @return the optional initials for the person
   */
  public String getInitials() {
    return initials;
  }

  /**
   * Returns the optional maiden name for the person.
   *
   * @return the optional maiden name for the person
   */
  public String getMaidenName() {
    return maidenName;
  }

  /**
   * Returns the optional code identifying the marital status for the person.
   *
   * @return the optional code identifying the marital status for the person
   */
  public String getMaritalStatus() {
    return maritalStatus;
  }

  /**
   * Returns the optional code identifying the marriage type for the person if the person is
   * married.
   *
   * @return the optional code identifying the marriage type for the person if the person is married
   */
  public String getMarriageType() {
    return marriageType;
  }

  /**
   * Returns the optional middle names for the person.
   *
   * @return the optional middle names for the person
   */
  public String getMiddleNames() {
    return middleNames;
  }

  /**
   * Returns the personal name or full name of the person.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the person. This name should match the full name on the identity document(s)
   * associated with the person.
   *
   * @return the personal name or full name of the person
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the optional preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @return the optional preferred name for the person
   */
  public String getPreferredName() {
    return preferredName;
  }

  /**
   * The optional code identifying the race for the person.
   *
   * @return the optional code identifying the race for the person
   */
  public String getRace() {
    return race;
  }

  /**
   * Returns the optional surname, last name, or family name for the person.
   *
   * @return the optional surname, last name, or family name for the person
   */
  public String getSurname() {
    return surname;
  }

  /**
   * Returns the optional code identifying the title for the person.
   *
   * @return the optional code identifying the title for the person
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the date and time the person was last updated.
   *
   * @return the date and time the person was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Set the optional code identifying the country of birth for the person.
   *
   * @param countryOfBirth the optional code identifying the country of birth for the person
   */
  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  /**
   * Set the optional date of birth for the person.
   *
   * @param dateOfBirth the optional date of birth for the person
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Set the optional date of death for the person.
   *
   * @param dateOfDeath the optional date of death for the person
   */
  public void setDateOfDeath(LocalDate dateOfDeath) {
    this.dateOfDeath = dateOfDeath;
  }

  /**
   * Set the optional code identifying the gender for the person.
   *
   * @param gender the optional code identifying the gender for the person
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Set the optional given name, firstname, forename, or Christian name for the person.
   *
   * @param givenName the optional given name, firstname, forename, or Christian name for the person
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the person.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the person
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the identity documents for the person.
   *
   * @param identityDocuments the identity documents for the person
   */
  public void setIdentityDocuments(Set<IdentityDocument> identityDocuments) {
    this.identityDocuments.clear();
    this.identityDocuments.addAll(identityDocuments);
  }

  /**
   * Set the optional initials for the person.
   *
   * @param initials the optional initials for the person
   */
  public void setInitials(String initials) {
    this.initials = initials;
  }

  /**
   * Set the optional maiden name for the person.
   *
   * @param maidenName the optional maiden name for the person
   */
  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }

  /**
   * Set the optional code identifying the marital status for the person.
   *
   * @param maritalStatus the optional code identifying the marital status for the person
   */
  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  /**
   * Set the optional code identifying the marriage type for the person if the person is married.
   *
   * @param marriageType the optional code identifying the marriage type for the person if the
   *     person is married
   */
  public void setMarriageType(String marriageType) {
    this.marriageType = marriageType;
  }

  /**
   * Set the optional middle names for the person.
   *
   * @param middleNames the optional middle names for the person
   */
  public void setMiddleNames(String middleNames) {
    this.middleNames = middleNames;
  }

  /**
   * Set the personal name or full name of the person.
   *
   * <p>In Western culture, this is constructed from a combination of the given name (also known as
   * the first name, forename, or Christian name), and the surname (also known as the last name or
   * family name) of the person. This name should match the full name on the identity document(s)
   * associated with the person.
   *
   * @param name the personal name or full name of the person
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the optional preferred name for the person.
   *
   * <p>In Western culture, this is usually the given name, which is also known as the first name,
   * forename, or Christian name.
   *
   * @param preferredName the optional preferred name for the person
   */
  public void setPreferredName(String preferredName) {
    this.preferredName = preferredName;
  }

  /**
   * Set the optional code identifying the race for the person.
   *
   * @param race the optional code identifying the race for the person
   */
  public void setRace(String race) {
    this.race = race;
  }

  /**
   * Set the optional surname, last name, or family name for the person.
   *
   * @param surname the optional surname, last name, or family name for the person
   */
  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   * Set the optional code identifying the title for the person.
   *
   * @param title the optional code identifying the title for the person
   */
  public void setTitle(String title) {
    this.title = title;
  }

  @PrePersist
  protected void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    created = now;
    partyCreated = now;
  }

  @PreUpdate
  protected void preUpdate() {
    LocalDateTime now = LocalDateTime.now();
    updated = now;
    partyUpdated = now;
  }
}
