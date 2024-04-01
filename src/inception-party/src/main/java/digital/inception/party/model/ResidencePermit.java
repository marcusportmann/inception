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
 * The <b>ResidencePermit</b> class holds the information for a residence permit issued to a person.
 *
 * <p>The primary key for the residence permit entity (ID) is a surrogate key to support the
 * management of related data in one or more external stores, e.g. an image of the residence permit
 * stored in an enterprise content management persistence. This approach allows an entity to be
 * modified without impacting the related data's referential integrity, for example, when correcting
 * an error that occurred during the initial capture of the information for a residence permit.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A residence permit issued to a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "countryOfIssue", "issueDate", "expiryDate", "number"})
@XmlRootElement(name = "ResidencePermit", namespace = "http://inception.digital/party")
@XmlType(
    name = "ResidencePermit",
    namespace = "http://inception.digital/party",
    propOrder = {"id", "type", "countryOfIssue", "issueDate", "expiryDate", "number"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_residence_permits")
public class ResidencePermit implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 alpha-2 code for the country of issue for the residence permit. */
  @Schema(
      description = "The ISO 3166-1 alpha-2 code for the country of issue for the residence permit",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CountryOfIssue", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  @Column(name = "country_of_issue", length = 2, nullable = false)
  private String countryOfIssue;

  /** The expiry date for the residence permit. */
  @Schema(description = "The ISO 8601 format expiry date for the residence permit")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "ExpiryDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "expiry_date")
  private LocalDate expiryDate;

  /** The ID for the residence permit. */
  @Schema(
      description = "The ID for the residence permit",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The issue date for the residence permit. */
  @Schema(description = "The ISO 8601 format issue date for the residence permit")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "IssueDate")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "issue_date")
  private LocalDate issueDate;

  /** The number for the residence permit. */
  @Schema(
      description = "The number for the residence permit",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Number", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "number", length = 50, nullable = false)
  private String number;

  /** The person the residence permit is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("residencePermitReference")
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id")
  private Person person;

  /** The code for the residence permit type. */
  @Schema(
      description = "The code for the residence permit type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** Constructs a new <b>ResidencePermit</b>. */
  public ResidencePermit() {}

  /**
   * Constructs a new <b>ResidencePermit</b>.
   *
   * @param type the code for the residence permit type
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the residence
   *     permit
   * @param issueDate the issue date for the residence permit
   * @param number the number for the residence permit
   */
  public ResidencePermit(String type, String countryOfIssue, LocalDate issueDate, String number) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.countryOfIssue = countryOfIssue;
    this.issueDate = issueDate;
    this.number = number;
  }

  /**
   * Constructs a new <b>ResidencePermit</b>.
   *
   * @param type the code for the residence permit type
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the residence
   *     permit
   * @param issueDate the issue date for the residence permit
   * @param expiryDate the expiry date for the residence permit
   * @param number thge number for the residence permit
   */
  public ResidencePermit(
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

    ResidencePermit other = (ResidencePermit) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of issue for the residence permit.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of issue for the residence permit
   */
  public String getCountryOfIssue() {
    return countryOfIssue;
  }

  /**
   * Returns the expiry date for the residence permit.
   *
   * @return the expiry date for the residence permit
   */
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  /**
   * Returns the ID for the residence permit.
   *
   * @return the ID for the residence permit
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the issue date for the residence permit.
   *
   * @return the issue date for the residence permit
   */
  public LocalDate getIssueDate() {
    return issueDate;
  }

  /**
   * The number for the residence permit.
   *
   * @return the number for the residence permit
   */
  public String getNumber() {
    return number;
  }

  /**
   * Returns the person the residence permit is associated with.
   *
   * @return the person the residence permit is associated with
   */
  @Schema(hidden = true)
  public Person getPerson() {
    return person;
  }

  /**
   * Returns the code for the residence permit type.
   *
   * @return the code for the residence permit type
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
   * Set the ISO 3166-1 alpha-2 code for the country of issue for the residence permit.
   *
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the residence
   *     permit
   */
  public void setCountryOfIssue(String countryOfIssue) {
    this.countryOfIssue = countryOfIssue;
  }

  /**
   * Set the expiry date for the residence permit.
   *
   * @param expiryDate the expiry date for the residence permit
   */
  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Set the ID for the residence permit.
   *
   * @param id the ID for the residence permit
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the issue date for the residence permit.
   *
   * @param issueDate the issue date for the residence permit
   */
  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  /**
   * Set the number for the residence permit.
   *
   * @param number the number for the residence permit
   */
  public void setNumber(String number) {
    this.number = number;
  }

  /**
   * Set the person the residence permit is associated with.
   *
   * @param person the person the residence permit is associated with
   */
  @Schema(hidden = true)
  public void setPerson(Person person) {
    this.person = person;
  }

  /**
   * Set the code for the residence permit type.
   *
   * @param type the code for the residence permit type
   */
  public void setType(String type) {
    this.type = type;
  }
}
