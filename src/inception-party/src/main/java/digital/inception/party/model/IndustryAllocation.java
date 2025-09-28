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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
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
 * The {@code IndustryAllocation} class holds the information for the allocation of an organization
 * to an industry.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An allocation of an organization to an industry")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"system", "industry", "effectiveFrom", "effectiveTo"})
@XmlRootElement(name = "IndustryAllocation", namespace = "https://inception.digital/party")
@XmlType(
    name = "IndustryAllocation",
    namespace = "https://inception.digital/party",
    propOrder = {"system", "industry", "effectiveFrom", "effectiveTo"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_industry_allocations")
@IdClass(IndustryAllocationId.class)
@SuppressWarnings("unused")
public class IndustryAllocation implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date the industry allocation is effective from. */
  @Schema(description = "The date the industry allocation is effective from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the industry allocation is effective to. */
  @Schema(description = "The date the industry allocation is effective to")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The code for the industry classification. */
  @Schema(
      description = "The code for the industry classification",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Industry", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "industry", length = 50, nullable = false)
  private String industry;

  /** The ID for the organization the industry allocation is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "organization_id", nullable = false)
  private UUID organizationId;

  /** The code for the industry classification system. */
  @Schema(
      description = "The code for the industry classification system",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "System", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "system", length = 50, nullable = false)
  private String system;

  /** Constructs a new {@code IndustryAllocation}. */
  public IndustryAllocation() {}

  /**
   * Constructs a new {@code IndustryAllocation}.
   *
   * @param system the code for the industry classification system
   * @param industry the code for the industry classification
   */
  public IndustryAllocation(String system, String industry) {
    this.system = system;
    this.industry = industry;
  }

  /**
   * Constructs a new {@code IndustryAllocation}.
   *
   * @param system the code for the industry classification system
   * @param industry the code for the industry classification
   * @param effectiveFrom the date the industry allocation is effective from
   */
  public IndustryAllocation(String system, String industry, LocalDate effectiveFrom) {
    this.system = system;
    this.industry = industry;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new {@code IndustryAllocation}.
   *
   * @param system the code for the industry classification system
   * @param industry the code for the industry classification
   * @param effectiveFrom the date the industry allocation is effective from
   * @param effectiveTo the date the industry allocation is effective to
   */
  public IndustryAllocation(
      String system, String industry, LocalDate effectiveFrom, LocalDate effectiveTo) {
    this.system = system;
    this.industry = industry;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }

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

    IndustryAllocation other = (IndustryAllocation) object;

    return Objects.equals(organizationId, other.organizationId)
        && Objects.equals(system, other.system)
        && Objects.equals(industry, other.industry);
  }

  /**
   * Returns the date the industry allocation is effective from.
   *
   * @return the date the industry allocation is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the date the industry allocation is effective to.
   *
   * @return the date the industry allocation is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the code for the industry classification.
   *
   * @return the code for the industry classification
   */
  public String getIndustry() {
    return industry;
  }

  /**
   * Returns the code for the industry classification system.
   *
   * @return the code for the industry classification system
   */
  public String getSystem() {
    return system;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((organizationId == null) ? 0 : organizationId.hashCode())
        + ((system == null) ? 0 : system.hashCode())
        + ((industry == null) ? 0 : industry.hashCode());
  }

  /**
   * Set the date the industry allocation is effective from.
   *
   * @param effectiveFrom the date the industry allocation is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the date the industry allocation is effective to.
   *
   * @param effectiveTo the date the industry allocation is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the code for the industry classification.
   *
   * @param industry the code for the industry classification
   */
  public void setIndustry(String industry) {
    this.industry = industry;
  }

  /**
   * Set the organization the industry allocation is associated with.
   *
   * @param organization the organization the industry allocation is associated with
   */
  @JsonBackReference("industryAllocationReference")
  @Schema(hidden = true)
  public void setOrganization(Organization organization) {
    if (organization != null) {
      this.organizationId = organization.getId();
    } else {
      this.organizationId = null;
    }
  }

  /**
   * Set the code for the industry classification system.
   *
   * @param system the code for the industry classification system
   */
  public void setSystem(String system) {
    this.system = system;
  }

  /**
   * Called by the JAXB runtime when an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof Organization parent) {
      setOrganization(parent);
    }
  }
}
