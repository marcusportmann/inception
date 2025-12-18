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

package digital.inception.reference.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Region} class holds the information for a region.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "An area, especially part of a country or the world having definable characteristics but not always fixed boundaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"country", "code", "localeId", "sortOrder", "name", "description"})
@XmlRootElement(name = "Region", namespace = "https://inception.digital/reference")
@XmlType(
    name = "Region",
    namespace = "https://inception.digital/reference",
    propOrder = {"country", "code", "localeId", "sortOrder", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "reference_regions")
@IdClass(RegionId.class)
public class Region implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 3166-2 subdivision code for the region. */
  @Schema(
      description = "The ISO 3166-2 subdivision code for the region",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 4, max = 6)
  @Id
  @Column(name = "code", length = 6, nullable = false)
  private String code;

  /** The ISO 3166-1 alpha-2 code for the country the region is associated with. */
  @Schema(
      description = "The ISO 3166-1 alpha-2 code for the country the region is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Country", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  @Id
  @Column(name = "country", length = 2, nullable = false)
  private String country;

  /** The description for the region. */
  @Schema(
      description = "The description for the region",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the region. */
  @Schema(
      description = "The Unicode locale identifier for the region",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the region. */
  @Schema(description = "The name of the region", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The sort order for the region. */
  @Schema(description = "The sort order for the region")
  @JsonProperty
  @XmlElement(name = "SortOrder")
  @Column(name = "sort_index")
  private Integer sortOrder;

  /** Constructs a new {@code Region}. */
  public Region() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    Region other = (Region) object;

    return Objects.equals(country, other.country)
        && Objects.equals(code, other.code)
        && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the ISO 3166-2 subdivision code for the region.
   *
   * @return the ISO 3166-2 subdivision code for the region
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country the region is associated with.
   *
   * @return the ISO 3166-1 alpha-2 code for the country the region is associated with
   */
  public String getCountry() {
    return country;
  }

  /**
   * Returns the description for the region.
   *
   * @return the description for the region
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the region.
   *
   * @return the Unicode locale identifier for the region
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the region.
   *
   * @return the name of the region
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort order for the region.
   *
   * @return the sort order for the region
   */
  public Integer getSortOrder() {
    return sortOrder;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((country == null) ? 0 : country.hashCode())
        + ((code == null) ? 0 : code.hashCode())
        + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Sets the ISO 3166-2 subdivision code for the region.
   *
   * @param code the ISO 3166-2 subdivision code for the region
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Sets the ISO 3166-1 alpha-2 code for the country the region is associated with.
   *
   * @param country the ISO 3166-1 alpha-2 code for the country the region is associated with
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Sets the description for the region.
   *
   * @param description the description for the region
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the Unicode locale identifier for the region.
   *
   * @param localeId the Unicode locale identifier for the region
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Sets the name of the region.
   *
   * @param name the name of the region
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the sort order for the region.
   *
   * @param sortOrder the sort order for the region
   */
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
}
