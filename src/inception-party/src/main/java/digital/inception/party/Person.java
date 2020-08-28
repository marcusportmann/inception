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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Person</code> class holds the information for a person.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "dateOfBirth", "gender", "identityDocuments"})
@XmlRootElement(name = "Person", namespace = "http://party.inception.digital")
@XmlType(
    name = "Person",
    namespace = "http://party.inception.digital",
    propOrder = {"dateOfBirth", "gender", "identityDocuments"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "persons")
public class Person extends Party {

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

  /** The date of birth for the person. */
  @Schema(description = "The date of birth for the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "DateOfBirth", required = true)
  @NotNull
  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;

  /** The code identifying the gender for the person. */
  @Schema(description = "The code identifying the gender for the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Gender", required = true)
  @NotNull
  @Column(name = "gender", nullable = false)
  private String gender;

  /** Constructs a new <code>Person</code>. */
  public Person() {
    super(PartyType.PERSON);
  }

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
   * Returns the date of birth for the person.
   *
   * @return the date of birth for the person
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Returns the code identifying the gender for the person.
   *
   * @return the code identifying the gender for the person
   */
  public String getGender() {
    return gender;
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
   * Set the date of birth for the person.
   *
   * @param dateOfBirth the date of birth for the person
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Set the code identifying the gender for the person.
   *
   * @param gender the code identifying the gender for the person
   */
  public void setGender(String gender) {
    this.gender = gender;
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
}
