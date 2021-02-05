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
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <b>TaxNumber</b> class holds the information for a tax number.
 *
 * <p>Even though the country of issue is specified as part of the reference data for a tax number
 * type, it is included here to eliminate the additional join. The additional join will also be
 * problematic if the reference data is stored in a separate database.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A tax number for a party")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"countryOfIssue", "type", "number"})
@XmlRootElement(name = "TaxNumber", namespace = "http://party.inception.digital")
@XmlType(
    name = "TaxNumber",
    namespace = "http://party.inception.digital",
    propOrder = {"countryOfIssue", "type", "number"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "tax_numbers")
@IdClass(TaxNumberId.class)
public class TaxNumber implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 alpha-2 code for the country of issue for the tax number. */
  @Schema(
      description = "The ISO 3166-1 alpha-2 code for the country of issue for the tax number",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "CountryOfIssue", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  @Column(name = "country_of_issue", length = 2, nullable = false)
  private String countryOfIssue;

  /** The date and time the tax number was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The tax number. */
  @Schema(description = "The tax number", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Number", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "number", length = 30, nullable = false)
  private String number;

  /** The party the tax number is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("taxNumberReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The code for the tax number type. */
  @Schema(description = "The code for the tax number type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the tax number was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>TaxNumber</b>. */
  public TaxNumber() {}

  /**
   * Constructs a new <b>TaxNumber</b>.
   *
   * @param type the code for the tax number type
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the tax number
   * @param number the tax number
   */
  public TaxNumber(String type, String countryOfIssue, String number) {
    this.type = type;
    this.countryOfIssue = countryOfIssue;
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

    TaxNumber other = (TaxNumber) object;

    return Objects.equals(party, other.party) && Objects.equals(type, other.type);
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country of issue for the tax number.
   *
   * @return the ISO 3166-1 alpha-2 code for the country of issue for the tax number
   */
  public String getCountryOfIssue() {
    return countryOfIssue;
  }

  /**
   * Returns the date and time the tax number was created.
   *
   * @return the date and time the tax number was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the tax number.
   *
   * @return the tax number
   */
  public String getNumber() {
    return number;
  }

  /**
   * Returns the party the tax number is associated with.
   *
   * @return the party the tax number is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the code for the tax number type.
   *
   * @return the code for the tax number type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the tax number was last updated.
   *
   * @return the date and time the tax number was last updated
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
    return (((party == null) || (party.getId() == null)) ? 0 : party.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country of issue for the tax number.
   *
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the tax number
   */
  public void setCountryOfIssue(String countryOfIssue) {
    this.countryOfIssue = countryOfIssue;
  }

  /**
   * Set the tax number.
   *
   * @param number the tax number
   */
  public void setNumber(String number) {
    this.number = number;
  }

  /**
   * Set the party the tax number is associated with.
   *
   * @param party the party the tax number is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the code for the tax number type.
   *
   * @param type the code for the tax number type
   */
  public void setType(String type) {
    this.type = type;
  }
}
