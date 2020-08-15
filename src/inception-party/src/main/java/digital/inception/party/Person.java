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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Person</code> class holds the information for a person.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name"})
@XmlRootElement(name = "Person", namespace = "http://party.inception.digital")
@XmlType(
    name = "Person",
    namespace = "http://party.inception.digital",
    propOrder = {"id", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "party")
@SecondaryTable(
    schema = "party",
    name = "person",
    pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")})
public class Person {

  /** The type of party for the person. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Column(name = "type", nullable = false)
  private final PartyType type = PartyType.PERSON;

  /** The date of birth for the person. */
  @Schema(description = "The date of birth for the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "DateOfBirth", required = true)
  @NotNull
  @Column(table = "person", name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;

  /** The code identifying the gender for the person. */
  @Schema(description = "The code identifying the gender for the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Gender", required = true)
  @NotNull
  @Column(table = "person", name = "gender", nullable = false)
  private String gender;

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

  /** The name of the person. */
  @Schema(description = "The name of the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** Constructs a new <code>Person</code>. */
  public Person() {}

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
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the person.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the person
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the person.
   *
   * @return the name of the person
   */
  public String getName() {
    return name;
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
   * Set the Universally Unique Identifier (UUID) uniquely identifying the person.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the person
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the person.
   *
   * @param name the name of the person
   */
  public void setName(String name) {
    this.name = name;
  }
}
