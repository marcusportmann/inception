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

package digital.inception.reference;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
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
 * The <code>ResidencePermitType</code> class holds the information for a possible residence permit
 * type, which is a type of official document giving a foreign national authorization to reside in a
 * country.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A type of official document giving a foreign national authorization to reside in a country")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "localeId", "sortIndex", "name", "description", "countryOfIssue"})
@XmlRootElement(name = "ResidencePermitType", namespace = "http://reference.inception.digital")
@XmlType(
    name = "ResidencePermitType",
    namespace = "http://reference.inception.digital",
    propOrder = {"code", "localeId", "sortIndex", "name", "description", "countryOfIssue"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "reference", name = "residence_permit_types")
@IdClass(ResidencePermitTypeId.class)
public class ResidencePermitType implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the residence permit type. */
  @Schema(description = "The code for the residence permit type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "code", length = 30, nullable = false)
  private String code;

  /** The code for the country of issue for the residence permit type. */
  @Schema(
      description = "The code for the country of issue for the residence permit type",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "CountryOfIssue", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "country_of_issue", length = 30, nullable = false)
  private String countryOfIssue;

  /** The description for the residence permit type. */
  @Schema(description = "The description for the residence permit type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the residence permit type. */
  @Schema(
      description = "The Unicode locale identifier for the residence permit type",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the residence permit type. */
  @Schema(description = "The name of the residence permit type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The sort index for the residence permit type. */
  @Schema(description = "The sort index for the residence permit type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** Constructs a new <code>ResidencePermitType</code>. */
  public ResidencePermitType() {}

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

    ResidencePermitType other = (ResidencePermitType) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the residence permit type.
   *
   * @return the code for the residence permit type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the code for the country of issue for the residence permit type.
   *
   * @return the code for the country of issue for the residence permit type
   */
  public String getCountryOfIssue() {
    return countryOfIssue;
  }

  /**
   * Returns the description for the residence permit type.
   *
   * @return the description for the residence permit type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the residence permit type.
   *
   * @return the Unicode locale identifier for the residence permit type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the residence permit type.
   *
   * @return the name of the residence permit type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort index for the residence permit type.
   *
   * @return the sort index for the residence permit type
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((code == null) ? 0 : code.hashCode()) + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Set the code for the residence permit type.
   *
   * @param code the code for the residence permit type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the code for the country of issue for the residence permit type.
   *
   * @param countryOfIssue the code for the country of issue for the identity document type
   */
  public void setCountryOfIssue(String countryOfIssue) {
    this.countryOfIssue = countryOfIssue;
  }

  /**
   * Set the description for the residence permit type.
   *
   * @param description the description for the residence permit type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the residence permit type.
   *
   * @param localeId the Unicode locale identifier for the residence permit type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the residence permit type.
   *
   * @param name the name of the residence permit type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the sort index for the residence permit type.
   *
   * @param sortIndex the sort index for the residence permit type
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }
}
