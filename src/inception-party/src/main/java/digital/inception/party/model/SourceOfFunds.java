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
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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

/**
 * The <b>SourceOfFunds</b> class holds the information for a source of funds for a person.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A source of funds for a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "effectiveFrom", "effectiveTo", "percentage", "description"})
@XmlRootElement(name = "SourceOfFunds", namespace = "http://inception.digital/party")
@XmlType(
    name = "SourceOfFunds",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "effectiveFrom", "effectiveTo", "percentage", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_sources_of_funds")
@IdClass(SourceOfFundsId.class)
public class SourceOfFunds implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The description for the source of funds. */
  @Schema(description = "The description for the source of funds")
  @JsonProperty
  @XmlElement(name = "Description")
  @Column(name = "description", length = 100)
  private String description;

  /** The date the source of funds is effective from. */
  @Schema(description = "The ISO 8601 format date the source of funds is effective from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the source of funds is effective to. */
  @Schema(description = "The ISO 8601 format date the source of funds is effective to")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The percentage of the total of all sources of funds attributed to the source of funds. */
  @Schema(
      description =
          "The percentage of the total of all sources of funds attributed to the source of funds")
  @JsonProperty
  @XmlElement(name = "Percentage")
  @Column(name = "percentage")
  private Integer percentage;

  /** The person the source of funds is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("sourceOfFundsReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id")
  private Person person;

  /** The code for the source of funds type. */
  @Schema(
      description = "The code for the source of funds type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** Constructs a new <b>SourceOfFunds</b>. */
  public SourceOfFunds() {}

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   */
  public SourceOfFunds(String type) {
    this.type = type;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param percentage the percentage of the total of all sources of funds attributed to this source
   *     of funds
   */
  public SourceOfFunds(String type, int percentage) {
    this.type = type;
    this.percentage = percentage;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param effectiveFrom the date the source of funds is effective from
   */
  public SourceOfFunds(String type, LocalDate effectiveFrom) {
    this.type = type;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param effectiveFrom the date the source of funds is effective from
   * @param percentage the percentage of the total of all sources of funds attributed to this source
   *     of funds
   */
  public SourceOfFunds(String type, LocalDate effectiveFrom, int percentage) {
    this.type = type;
    this.effectiveFrom = effectiveFrom;
    this.percentage = percentage;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param effectiveFrom the date the source of funds is effective from
   * @param effectiveTo the date the source of funds is effective to
   */
  public SourceOfFunds(String type, LocalDate effectiveFrom, LocalDate effectiveTo) {
    this.type = type;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param effectiveFrom the date the source of funds is effective from
   * @param effectiveTo the date the source of funds is effective to
   * @param percentage the percentage of the total of all sources of funds attributed to this source
   *     of funds
   */
  public SourceOfFunds(
      String type, LocalDate effectiveFrom, LocalDate effectiveTo, int percentage) {
    this.type = type;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
    this.percentage = percentage;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param description the description for the source of funds
   */
  public SourceOfFunds(String type, String description) {
    this.type = type;
    this.description = description;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param description the description for the source of funds
   * @param percentage the percentage of the total of all sources of funds attributed to this source
   *     of funds
   */
  public SourceOfFunds(String type, String description, int percentage) {
    this.type = type;
    this.description = description;
    this.percentage = percentage;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param description the description for the source of funds
   * @param effectiveFrom the date the source of funds is effective from
   */
  public SourceOfFunds(String type, String description, LocalDate effectiveFrom) {
    this.type = type;
    this.description = description;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param description the description for the source of funds
   * @param effectiveFrom the date the source of funds is effective from
   * @param percentage the percentage of the total of all sources of funds attributed to this source
   *     of funds
   */
  public SourceOfFunds(String type, String description, LocalDate effectiveFrom, int percentage) {
    this.type = type;
    this.description = description;
    this.effectiveFrom = effectiveFrom;
    this.percentage = percentage;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param description the description for the source of funds
   * @param effectiveFrom the date the source of funds is effective from
   * @param effectiveTo the date the source of funds is effective to
   */
  public SourceOfFunds(
      String type, String description, LocalDate effectiveFrom, LocalDate effectiveTo) {
    this.type = type;
    this.description = description;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }

  /**
   * Constructs a new <b>SourceOfFunds</b>.
   *
   * @param type the source of funds type
   * @param description the description for the source of funds
   * @param effectiveFrom the date the source of funds is effective from
   * @param effectiveTo the date the source of funds is effective to
   * @param percentage the percentage of the total of all sources of funds attributed to this source
   *     of funds
   */
  public SourceOfFunds(
      String type,
      String description,
      LocalDate effectiveFrom,
      LocalDate effectiveTo,
      int percentage) {
    this.type = type;
    this.description = description;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
    this.percentage = percentage;
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

    SourceOfFunds other = (SourceOfFunds) object;

    return Objects.equals(person, other.person) && Objects.equals(type, other.type);
  }

  /**
   * Returns the description for the source of funds.
   *
   * @return the description for the source of funds
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the date the source of funds is effective from.
   *
   * @return the date the source of funds is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the date the source of funds is effective to.
   *
   * @return the date the source of funds is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the percentage of the total of all sources of funds attributed to the source of funds.
   *
   * @return the percentage of the total of all sources of funds attributed to the source of funds
   */
  public Integer getPercentage() {
    return percentage;
  }

  /**
   * Returns the person the source of funds is associated with.
   *
   * @return the person the source of funds is associated with
   */
  @Schema(hidden = true)
  public Person getPerson() {
    return person;
  }

  /**
   * Returns the code for the source of funds type.
   *
   * @return the code for the source of funds type
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
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the description for the source of funds.
   *
   * @param description the description for the source of funds
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the date the source of funds is effective from.
   *
   * @param effectiveFrom the date the source of funds is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the date the source of funds is effective to.
   *
   * @param effectiveTo the date the source of funds is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the percentage of the total of all sources of funds attributed to the source of funds.
   *
   * @param percentage the percentage of the total of all sources of funds attributed to the source
   *     of funds
   */
  public void setPercentage(Integer percentage) {
    this.percentage = percentage;
  }

  /**
   * Set the person the source of funds is associated with.
   *
   * @param person the person the source of funds is associated with
   */
  @Schema(hidden = true)
  public void setPerson(Person person) {
    this.person = person;
  }

  /**
   * Set the code for the source of funds type.
   *
   * @param type the code for the source of funds type
   */
  public void setType(String type) {
    this.type = type;
  }
}
