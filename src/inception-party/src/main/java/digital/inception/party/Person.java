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
@JsonPropertyOrder({"id", "fullName"})
@XmlRootElement(name = "Person", namespace = "http://party.inception.digital")
@XmlType(
    name = "Person",
    namespace = "http://party.inception.digital",
    propOrder = {"id", "fullName"})
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

  /** The full name of the person. */
  @Schema(description = "The full name of the person", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "FullName", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "name", nullable = false, length = 100)
  private String fullName;

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

  /** Constructs a new <code>Person</code>. */
  public Person() {}

  /**
   * Returns the full name of the person.
   *
   * @return the full name of the person
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Set the full name of the person.
   *
   * @param fullName the full name of the person
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
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
   * Set the Universally Unique Identifier (UUID) uniquely identifying the person.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the person
   */
  public void setId(UUID id) {
    this.id = id;
  }
}
