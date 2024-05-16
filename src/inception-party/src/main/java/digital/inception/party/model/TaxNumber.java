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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The <b>TaxNumber</b> class holds the information for a tax number associated with an organization
 * or person.
 *
 * <p>Even though the country of issue is specified as part of the reference data for a tax number
 * type, it is included here to eliminate the additional join. The additional join will also be
 * problematic if the reference data is stored in a separate database.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A tax number for an organization or person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "number", "countryOfIssue"})
@XmlRootElement(name = "TaxNumber", namespace = "https://inception.digital/party")
@XmlType(
    name = "TaxNumber",
    namespace = "https://inception.digital/party",
    propOrder = {"type", "number", "countryOfIssue"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_tax_numbers")
@IdClass(TaxNumberId.class)
public class TaxNumber implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 alpha-2 code for the country of issue for the tax number. */
  @Schema(
      description = "The ISO 3166-1 alpha-2 code for the country of issue for the tax number",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "CountryOfIssue", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  @Column(name = "country_of_issue", length = 2, nullable = false)
  private String countryOfIssue;

  /** The tax number. */
  @Schema(description = "The tax number")
  @JsonProperty
  @XmlElement(name = "Number")
  @Size(min = 1, max = 50)
  @Column(name = "number", length = 50)
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
  @Schema(
      description = "The code for the tax number type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "type", length = 50, nullable = false)
  private String type;

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
    this.number = number;
    this.countryOfIssue = countryOfIssue;
  }

  /**
   * Constructs a new <b>TaxNumber</b>.
   *
   * @param type the code for the tax number type
   * @param countryOfIssue the ISO 3166-1 alpha-2 code for the country of issue for the tax number
   */
  public TaxNumber(String type, String countryOfIssue) {
    this.type = type;
    this.countryOfIssue = countryOfIssue;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
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
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((party == null) ? 0 : party.hashCode()) + ((type == null) ? 0 : type.hashCode());
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
