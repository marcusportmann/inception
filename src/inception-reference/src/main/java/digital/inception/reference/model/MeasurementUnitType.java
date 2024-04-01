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
 * The <b>MeasurementUnitType</b> class holds the information for a measurement unit type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of unit of measurement")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "localeId", "sortIndex", "name", "description"})
@XmlRootElement(name = "MeasurementUnitType", namespace = "http://inception.digital/reference")
@XmlType(
    name = "MeasurementUnitType",
    namespace = "http://inception.digital/reference",
    propOrder = {"code", "localeId", "sortIndex", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "reference_measurement_unit_types")
@IdClass(MeasurementUnitTypeId.class)
public class MeasurementUnitType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the measurement unit type. */
  @Schema(
      description = "The code for the measurement unit type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the measurement unit type. */
  @Schema(
      description = "The description for the measurement unit type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the measurement unit type. */
  @Schema(
      description = "The Unicode locale identifier for the measurement unit type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the measurement unit type. */
  @Schema(
      description = "The name of the measurement unit type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The sort index for the measurement unit type. */
  @Schema(description = "The sort index for the measurement unit type")
  @JsonProperty
  @XmlElement(name = "SortIndex")
  @Column(name = "sort_index")
  private Integer sortIndex;

  /** Constructs a new <b>MeasurementUnitType</b>. */
  public MeasurementUnitType() {}

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

    MeasurementUnitType other = (MeasurementUnitType) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the measurement unit type.
   *
   * @return the code for the measurement unit type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the measurement unit type.
   *
   * @return the description for the measurement unit type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the measurement unit type.
   *
   * @return the Unicode locale identifier for the measurement unit type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the measurement unit type.
   *
   * @return the name of the measurement unit type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort index for the measurement unit type.
   *
   * @return the sort index for the measurement unit type
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
   * Set the code for the measurement unit type.
   *
   * @param code the code for the measurement unit type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the measurement unit type.
   *
   * @param description the description for the measurement unit type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the measurement unit type.
   *
   * @param localeId the Unicode locale identifier for the measurement unit type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the measurement unit type.
   *
   * @param name the name of the measurement unit type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the sort index for the measurement unit type.
   *
   * @param sortIndex the sort index for the measurement unit type
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }
}
