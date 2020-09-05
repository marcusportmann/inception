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

package digital.inception.reference;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Country</code> class holds the information for a possible country.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A geopolitical entity representing a country or dependent territory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "locale",
  "sortIndex",
  "name",
  "shortName",
  "description",
  "sovereignState",
  "nationality"
})
@XmlRootElement(name = "Country", namespace = "http://reference.inception.digital")
@XmlType(
    name = "Country",
    namespace = "http://reference.inception.digital",
    propOrder = {
      "code",
      "localeId",
      "sortIndex",
      "name",
      "shortName",
      "description",
      "sovereignState",
      "nationality"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "reference", name = "countries")
@IdClass(CountryId.class)
public class Country {

  /** The code for the country. */
  @Schema(description = "The code for the country", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 10)
  @Id
  @Column(name = "code", nullable = false)
  private String code;

  /** The description for the country. */
  @Schema(description = "The description for the country", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", nullable = false)
  private String description;

  /** The Unicode locale identifier for the country. */
  @Schema(description = "The Unicode locale identifier for the country", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", nullable = false)
  private String localeId;

  /** The name of the country. */
  @Schema(description = "The name of the country", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", nullable = false)
  private String name;

  /** The nationality for the country. */
  @Schema(description = "The nationality for the country", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Nationality", required = true)
  @NotNull
  @Size(max = 50)
  @Column(name = "nationality", nullable = false)
  private String nationality;

  /** The short name for the country. */
  @Schema(description = "The short name for the country", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ShortName", required = true)
  @NotNull
  @Size(max = 50)
  @Column(name = "short_name", nullable = false)
  private String shortName;

  /** The sort index for the country. */
  @Schema(description = "The sort index for the country", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The code identifying the sovereign state for the country. */
  @Schema(description = "The code identifying the sovereign state for the country", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SovereignState", required = true)
  @NotNull
  @Size(min = 1, max = 10)
  @Column(name = "sovereign_state", nullable = false)
  private String sovereignState;

  /** Constructs a new <code>Country</code>. */
  public Country() {}

  /**
   * Returns the code for the country.
   *
   * @return the code for the country
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the country.
   *
   * @return the description for the country
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the country.
   *
   * @return the Unicode locale identifier for the country
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the country.
   *
   * @return the name of the country
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the nationality for the country.
   *
   * @return the nationality for the country
   */
  public String getNationality() {
    return nationality;
  }

  /**
   * Returns the short name for the country.
   *
   * @return the short name for the country
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Returns the sort index for the country.
   *
   * @return the sort index for the country
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the code identifying the sovereign state for the country.
   *
   * @return the code identifying the sovereign state for the country
   */
  public String getSovereignState() {
    return sovereignState;
  }

  /**
   * Set the code for the country.
   *
   * @param code the code for the country
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the country.
   *
   * @param description the description for the country
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the country.
   *
   * @param localeId the Unicode locale identifier for the country
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the country.
   *
   * @param name the name of the country
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the nationality for the country.
   *
   * @param nationality the nationality for the country
   */
  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  /**
   * Set the short name for the country.
   *
   * @param shortDescription the short name for the country
   */
  public void setShortName(String shortDescription) {
    this.shortName = shortDescription;
  }

  /**
   * Set the sort index for the country.
   *
   * @param sortIndex the sort index for the country
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the code identifying the sovereign state for the country.
   *
   * @param sovereignState the code identifying the sovereign state for the country
   */
  public void setSovereignState(String sovereignState) {
    this.sovereignState = sovereignState;
  }
}
