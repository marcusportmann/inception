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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
 * The <code>IdentityDocument</code> class holds the information for an identity document.
 *
 * <p>See: https://spec.edmcouncil.org/fibo/ontology/FND/AgentsAndPeople/People/IdentityDocument
 *
 * @author Marcus Portmann
 */
@Schema(description = "IdentityDocument")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "dateOfIssue"})
@XmlRootElement(name = "IdentityDocument", namespace = "http://party.inception.digital")
@XmlType(
    name = "IdentityDocument",
    namespace = "http://party.inception.digital",
    propOrder = {"id", "type", "dateOfIssue"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "identity_documents")
// @IdClass(IdentityDocumentId.class)
public class IdentityDocument {

  /** The date of issue for the identity document. */
  @Schema(description = "The date of issue for the identity document", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "DateOfIssue", required = true)
  @NotNull
  @Column(name = "date_of_issue", nullable = false)
  private LocalDate dateOfIssue;

  /** The Universally Unique Identifier (UUID) uniquely identifying the identity document. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the identity document",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The person the identity document is associated with. */
  @JsonBackReference
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY)
  private Person person;

  /** The code identifying the type of identity document. */
  @Schema(description = "The code identifying the type of identity document", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 10)
  @Column(name = "type", nullable = false)
  private String type;

  /** Constructs a new <code>IdentityDocument</code>. */
  public IdentityDocument() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
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

    IdentityDocument other = (IdentityDocument) object;

    return Objects.equals(person, other.person) && Objects.equals(id, other.id);
  }

  /**
   * Returns the date of issue for the identity document.
   *
   * @return the date of issue for the identity document
   */
  public LocalDate getDateOfIssue() {
    return dateOfIssue;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the identity document.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the identity document
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the person the identity document is associated with.
   *
   * @return the person the identity document is associated with
   */
  public Person getPerson() {
    return person;
  }

  /**
   * Returns the code identifying the type of identity document.
   *
   * @return the code identifying the type of identity document
   */
  public String getType() {
    return type;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((person == null) || (person.getId() == null)) ? 0 : person.getId().hashCode())
        + ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Set the date of issue for the identity document.
   *
   * @param dateOfIssue the date of issue for the identity document
   */
  public void setDateOfIssue(LocalDate dateOfIssue) {
    this.dateOfIssue = dateOfIssue;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the identity document.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the identity document
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the person the identity document is associated with.
   *
   * @param person the person the identity document is associated with
   */
  public void setPerson(Person person) {
    this.person = person;
  }

  /**
   * Set the code identifying the type of identity document.
   *
   * @param type the code identifying the type of identity document
   */
  public void setType(String type) {
    this.type = type;
  }
}
