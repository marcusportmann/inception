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
 * The {@code MeasurementUnit} class holds the information for a measurement unit.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of unit of measurement")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "localeId", "sortOrder", "name", "description", "system", "type"})
@XmlRootElement(name = "MeasurementUnit", namespace = "https://inception.digital/reference")
@XmlType(
    name = "MeasurementUnit",
    namespace = "https://inception.digital/reference",
    propOrder = {"code", "localeId", "sortOrder", "name", "description", "system", "type"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "reference_measurement_units")
@IdClass(MeasurementUnitId.class)
public class MeasurementUnit implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the measurement unit. */
  @Schema(
      description = "The code for the measurement unit",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the measurement unit. */
  @Schema(
      description = "The description for the measurement unit",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the measurement unit. */
  @Schema(
      description = "The Unicode locale identifier for the measurement unit",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the measurement unit. */
  @Schema(
      description = "The name of the measurement unit",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The sort order for the measurement unit. */
  @Schema(description = "The sort order for the measurement unit")
  @JsonProperty
  @XmlElement(name = "SortOrder")
  @Column(name = "sort_index")
  private Integer sortOrder;

  /** The code for the measurement system. */
  @Schema(
      description = "The code for the measurement system",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "System", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "system", length = 50, nullable = false)
  private String system;

  /** The code for the measurement unit type. */
  @Schema(
      description = "The code for the measurement unit type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** Constructs a new {@code MeasurementUnit}. */
  public MeasurementUnit() {}

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

    MeasurementUnit other = (MeasurementUnit) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the measurement unit.
   *
   * @return the code for the measurement unit
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the measurement unit.
   *
   * @return the description for the measurement unit
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the measurement unit.
   *
   * @return the Unicode locale identifier for the measurement unit
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the measurement unit.
   *
   * @return the name of the measurement unit
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort order for the measurement unit.
   *
   * @return the sort order for the measurement unit
   */
  public Integer getSortOrder() {
    return sortOrder;
  }

  /**
   * Returns the code for the measurement system.
   *
   * @return the code for the measurement system
   */
  public String getSystem() {
    return system;
  }

  /**
   * Returns the code for the measurement unit type.
   *
   * @return the code for the measurement unit type
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
    return ((code == null) ? 0 : code.hashCode()) + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Sets the code for the measurement unit.
   *
   * @param code the code for the measurement unit
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Sets the description for the measurement unit.
   *
   * @param description the description for the measurement unit
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the Unicode locale identifier for the measurement unit.
   *
   * @param localeId the Unicode locale identifier for the measurement unit
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Sets the name of the measurement unit.
   *
   * @param name the name of the measurement unit
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the sort order for the measurement unit.
   *
   * @param sortOrder the sort order for the measurement unit
   */
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  /**
   * Sets the code for the measurement system.
   *
   * @param system the code for the measurement system
   */
  public void setSystem(String system) {
    this.system = system;
  }

  /**
   * Sets the code for the measurement unit type.
   *
   * @param type the code for the measurement unit type
   */
  public void setType(String type) {
    this.type = type;
  }
}
