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
 * The <b>IdentityDocument</b> class holds the information for a legal document which may be used to
 * verify aspects of a party's identity.
 *
 * <p>The primary key for the identity document entity (ID) is a surrogate key to support the
 * management of related data in one or more external stores, e.g. an image of the identity document
 * stored in an enterprise content management repository. This approach allows an entity to be
 * modified without impacting the related data's referential integrity, for example, when correcting
 * an error that occurred during the initial capture of the information for an identity document.
 *
 * <p>See: https://spec.edmcouncil.org/fibo/ontology/FND/AgentsAndPeople/People/IdentityDocument
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A legal document which may be used to verify aspects of an organization's or person's identity")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "type",
  "countryOfIssue",
  "dateOfIssue",
  "dateOfExpiry",
  "dateProvided",
  "number"
})
@XmlRootElement(name = "IdentityDocument", namespace = "http://inception.digital/party")
@XmlType(
    name = "IdentityDocument",
    namespace = "http://inception.digital/party",
    propOrder = {
      "id",
      "type",
      "countryOfIssue",
      "dateOfIssue",
      "dateOfExpiry",
      "dateProvided",
      "number"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "identity_documents")
public class IdentityDocument implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 alpha-2 code for the country of issue for the identity document. */
  @Schema(
      description =
          "The ISO 3166-1 alpha-2 code for the country of issue for the identity document",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "CountryOfIssue", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  @Column(name = "country_of_issue", length = 2, nullable = false)
  private String countryOfIssue;

  /** The date and time the identity document was created. */
  @JsonIgnore
  @XmlTransient
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The date of expiry for the identity document. */
  @Schema(description = "The date of expiry for the identity document")
  @JsonProperty
  @XmlElement(name = "DateOfExpiry")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "date_of_expiry")
  private LocalDate dateOfExpiry;

  /** The date of issue for the identity document. */
  @Schema(description = "The date of issue for the identity document", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "DateOfIssue", required = true)
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @NotNull
  @Column(name = "date_of_issue", nullable = false)
  private LocalDate dateOfIssue;

  /** The date the identity document was provided. */
  @Schema(description = "The date the identity document was provided")
  @JsonProperty
  @XmlElement(name = "DateProvided")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "date_provided")
  private LocalDate dateProvided;

  /** The ID for the identity document. */
  @Schema(description = "The ID for the identity document", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The number for the identity document. */
  @Schema(description = "The number for the identity document", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Number", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "number", length = 30, nullable = false)
  private String number;

  /** The party the identity document is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("identityDocumentReference")
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The code for the identity document type. */
  @Schema(description = "The code for the identity document type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the identity document was last updated. */
  @JsonIgnore
  @XmlTransient
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>IdentityDocument</b>. */
  public IdentityDocument() {}

  /**
   * Constructs a new <b>IdentityDocument</b>.
   *
   * @param type the code for the identity document type
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the identity
   *     document
   * @param dateOfIssue the date of issue for the identity document
   * @param number the number for the identity document
   */
  public IdentityDocument(
      String type, String countryOfIssue, LocalDate dateOfIssue, String number) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.countryOfIssue = countryOfIssue;
    this.dateOfIssue = dateOfIssue;
    this.number = number;
  }

  /**
   * Constructs a new <b>IdentityDocument</b>.
   *
   * @param type the code for the identity document type
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the identity
   *     document
   * @param dateOfIssue the date of issue for the identity document
   * @param dateOfExpiry the date of expiry for the identity document
   * @param number the number for the identity document
   */
  public IdentityDocument(
      String type,
      String countryOfIssue,
      LocalDate dateOfIssue,
      LocalDate dateOfExpiry,
      String number) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.countryOfIssue = countryOfIssue;
    this.dateOfIssue = dateOfIssue;
    this.dateOfExpiry = dateOfExpiry;
    this.number = number;
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

    IdentityDocument other = (IdentityDocument) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of issue for the identity document.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of issue for the identity document
   */
  public String getCountryOfIssue() {
    return countryOfIssue;
  }

  /**
   * Returns the date and time the identity document was created.
   *
   * @return the date and time the identity document was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the date of expiry for the identity document.
   *
   * @return the date of expiry for the identity document
   */
  public LocalDate getDateOfExpiry() {
    return dateOfExpiry;
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
   * Returns the date the identity document was provided.
   *
   * @return the date the identity document was provided
   */
  public LocalDate getDateProvided() {
    return dateProvided;
  }

  /**
   * Returns the ID for the identity document.
   *
   * @return the ID for the identity document
   */
  public UUID getId() {
    return id;
  }

  /**
   * The number for the identity document.
   *
   * @return the number for the identity document
   */
  public String getNumber() {
    return number;
  }

  /**
   * Returns the party the identity document is associated with.
   *
   * @return the party the identity document is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the code for the identity document type.
   *
   * @return the code for the identity document type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the identity document was last updated.
   *
   * @return the date and time the identity document was last updated
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
   * Set the ISO 3166-1 alpha-2 code for the country of issue for the identity document.
   *
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the identity
   *     document
   */
  public void setCountryOfIssue(String countryOfIssue) {
    this.countryOfIssue = countryOfIssue;
  }

  /**
   * Set the date of expiry for the identity document.
   *
   * @param dateOfExpiry the date of expiry for the identity document
   */
  public void setDateOfExpiry(LocalDate dateOfExpiry) {
    this.dateOfExpiry = dateOfExpiry;
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
   * Set the date the identity document was provided.
   *
   * @param dateProvided the date the identity document was provided
   */
  public void setDateProvided(LocalDate dateProvided) {
    this.dateProvided = dateProvided;
  }

  /**
   * Set the ID for the identity document.
   *
   * @param id the ID for the identity document
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the number for the identity document.
   *
   * @param number the number for the identity document
   */
  public void setNumber(String number) {
    this.number = number;
  }

  /**
   * Set the party the identity document is associated with.
   *
   * @param party the party the identity document is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the code for the identity document type.
   *
   * @param type the code for the identity document type
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
