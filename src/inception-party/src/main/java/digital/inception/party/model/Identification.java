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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * The <b>Identification</b> class holds the information for a legal form of identification that may
 * be used to verify aspects of a party's identity.
 *
 * <p>The primary key for the identification entity (ID) is a surrogate key to support the
 * management of related data in one or more external stores, e.g. an image of the identification
 * stored in an enterprise content management persistence. This approach allows an entity to be
 * modified without impacting the related data's referential integrity, for example, when correcting
 * an error that occurred during the initial capture of the information for an identification.
 *
 * <p>See: https://spec.edmcouncil.org/fibo/ontology/FND/AgentsAndPeople/People/Identification
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A legal form of identification that may be used to verify aspects of an organization's or person's identity")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "type",
  "number",
  "issueDate",
  "expiryDate",
  "countryOfIssue",
  "providedDate",
})
@XmlRootElement(name = "Identification", namespace = "https://inception.digital/party")
@XmlType(
    name = "Identification",
    namespace = "https://inception.digital/party",
    propOrder = {
      "id",
      "type",
      "number",
      "issueDate",
      "expiryDate",
      "countryOfIssue",
      "providedDate",
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_identifications")
public class Identification implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 alpha-2 code for the country of issue for the identification. */
  @Schema(
      description = "The ISO 3166-1 alpha-2 code for the country of issue for the identification",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CountryOfIssue", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  @Column(name = "country_of_issue", length = 2, nullable = false)
  private String countryOfIssue;

  /** The expiry date for the identification. */
  @Schema(description = "The ISO 8601 format expiry date for the identification")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "ExpiryDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "expiry_date")
  private LocalDate expiryDate;

  /** The ID for the identification. */
  @Schema(
      description = "The ID for the identification",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The issue date for the identification. */
  @Schema(
      description = "The ISO 8601 format issue date for the identification",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "IssueDate", required = true)
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @NotNull
  @Column(name = "issue_date", nullable = false)
  private LocalDate issueDate;

  /** The number for the identification. */
  @Schema(
      description = "The number for the identification",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Number", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "number", length = 50, nullable = false)
  private String number;

  /** The party the identification is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("identificationReference")
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The date the identification was provided. */
  @Schema(description = "The ISO 8601 format date the identification was provided")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "ProvidedDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "provided_date")
  private LocalDate providedDate;

  /** The code for the identification type. */
  @Schema(
      description = "The code for the identification type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** Constructs a new <b>Identification</b>. */
  public Identification() {}

  /**
   * Constructs a new <b>Identification</b>.
   *
   * @param type the code for the identification type
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the
   *     identification
   * @param issueDate the issue date for the identification
   * @param number the number for the identification
   */
  public Identification(String type, String countryOfIssue, LocalDate issueDate, String number) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.countryOfIssue = countryOfIssue;
    this.issueDate = issueDate;
    this.number = number;
  }

  /**
   * Constructs a new <b>Identification</b>.
   *
   * @param type the code for the identification type
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the
   *     identification
   * @param issueDate the issue date for the identification
   * @param expiryDate the expiry date for the identification
   * @param number the number for the identification
   */
  public Identification(
      String type,
      String countryOfIssue,
      LocalDate issueDate,
      LocalDate expiryDate,
      String number) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.countryOfIssue = countryOfIssue;
    this.issueDate = issueDate;
    this.expiryDate = expiryDate;
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

    Identification other = (Identification) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of issue for the identification.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of issue for the identification
   */
  public String getCountryOfIssue() {
    return countryOfIssue;
  }

  /**
   * Returns the expiry date for the identification.
   *
   * @return the expiry date for the identification
   */
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  /**
   * Returns the ID for the identification.
   *
   * @return the ID for the identification
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the issue date for the identification.
   *
   * @return the issue date for the identification
   */
  public LocalDate getIssueDate() {
    return issueDate;
  }

  /**
   * The number for the identification.
   *
   * @return the number for the identification
   */
  public String getNumber() {
    return number;
  }

  /**
   * Returns the party the identification is associated with.
   *
   * @return the party the identification is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the date the identification was provided.
   *
   * @return the date the identification was provided
   */
  public LocalDate getProvidedDate() {
    return providedDate;
  }

  /**
   * Returns the code for the identification type.
   *
   * @return the code for the identification type
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
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country of issue for the identification.
   *
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the
   *     identification
   */
  public void setCountryOfIssue(String countryOfIssue) {
    this.countryOfIssue = countryOfIssue;
  }

  /**
   * Set the expiry date for the identification.
   *
   * @param expiryDate the expiry date for the identification
   */
  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Set the ID for the identification.
   *
   * @param id the ID for the identification
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the issue date for the identification.
   *
   * @param issueDate the issue date for the identification
   */
  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  /**
   * Set the number for the identification.
   *
   * @param number the number for the identification
   */
  public void setNumber(String number) {
    this.number = number;
  }

  /**
   * Set the party the identification is associated with.
   *
   * @param party the party the identification is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the date the identification was provided.
   *
   * @param dateProvided the date the identification was provided
   */
  public void setProvidedDate(LocalDate dateProvided) {
    this.providedDate = dateProvided;
  }

  /**
   * Set the code for the identification type.
   *
   * @param type the code for the identification type
   */
  public void setType(String type) {
    this.type = type;
  }
}
