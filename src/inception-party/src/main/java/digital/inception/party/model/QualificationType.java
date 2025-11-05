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
import java.util.UUID;

/**
 * The {@code QualificationType} class holds the information for a qualification type.
 *
 * @see <a
 *     href="https://www.mastersportal.com/articles/1953/should-i-study-a-postgraduate-certificate-or-a-graduate-diploma-abroad.html">Should
 *     I Study a Postgraduate Certificate or a Graduate Diploma Abroad?</a>
 * @see <a href="https://desbt.qld.gov.au/training/training-careers/about/qualifications">Australian
 *     Qualification Types</a>
 * @see <a
 *     href="https://www.studylink.govt.nz/about-studylink/glossary/qualification-types.html#null">New
 *     Zealand Qualification Types</a>
 * @see <a
 *     href="https://www.skillsportal.co.za/content/6-academic-qualifications-you-need-know">South
 *     African Academic Qualification Types</a>
 * @author Marcus Portmann
 */
@Schema(description = "A qualification type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "localeId",
  "tenantId",
  "sortOrder",
  "name",
  "description",
  "fieldOfStudy"
})
@XmlRootElement(name = "QualificationType", namespace = "https://inception.digital/party")
@XmlType(
    name = "QualificationType",
    namespace = "https://inception.digital/party",
    propOrder = {
      "code",
      "localeId",
      "tenantId",
      "sortOrder",
      "name",
      "description",
      "fieldOfStudy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_qualification_types")
@IdClass(QualificationTypeId.class)
@SuppressWarnings("unused")
public class QualificationType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the qualification type. */
  @Schema(
      description = "The code for the qualification type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The description for the qualification type. */
  @Schema(
      description = "The description for the qualification type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The code for the field of study for the qualification type. */
  @Schema(description = "The code for the field of study for the qualification type")
  @JsonProperty
  @XmlElement(name = "FieldOfStudy")
  @Size(min = 1, max = 50)
  @Column(name = "field_of_study", length = 50)
  private String fieldOfStudy;

  /** The Unicode locale identifier for the qualification type. */
  @Schema(
      description = "The Unicode locale identifier for the qualification type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the qualification type. */
  @Schema(
      description = "The name of the qualification type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The sort order for the qualification type. */
  @Schema(
      description = "The sort order for the qualification type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortOrder", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortOrder;

  /** The ID for the tenant the qualification type is specific to. */
  @Schema(description = "The ID for the tenant the qualification type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new {@code QualificationType}. */
  public QualificationType() {}

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

    QualificationType other = (QualificationType) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the qualification type.
   *
   * @return the code for the qualification type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the qualification type.
   *
   * @return the description for the qualification type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the code for the field of study for the qualification type.
   *
   * @return the code for the field of study for the qualification type
   */
  public String getFieldOfStudy() {
    return fieldOfStudy;
  }

  /**
   * Returns the Unicode locale identifier for the qualification type.
   *
   * @return the Unicode locale identifier for the qualification type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the qualification type.
   *
   * @return the name of the qualification type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort order for the qualification type.
   *
   * @return the sort order for the qualification type
   */
  public Integer getSortOrder() {
    return sortOrder;
  }

  /**
   * Returns the ID for the tenant the qualification type is specific to.
   *
   * @return the ID for the tenant the qualification type is specific to
   */
  public UUID getTenantId() {
    return tenantId;
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
   * Set the code for the qualification type.
   *
   * @param code the code for the qualification type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the qualification type.
   *
   * @param description the description for the qualification type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the code for the field of study for the qualification type.
   *
   * @param fieldOfStudy the code for the field of study for the qualification type
   */
  public void setFieldOfStudy(String fieldOfStudy) {
    this.fieldOfStudy = fieldOfStudy;
  }

  /**
   * Set the Unicode locale identifier for the qualification type.
   *
   * @param localeId the Unicode locale identifier for the qualification type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the qualification type.
   *
   * @param name the name of the qualification type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the sort order for the qualification type.
   *
   * @param sortOrder the sort order for the qualification type
   */
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  /**
   * Set the ID for the tenant the qualification type is specific to.
   *
   * @param tenantId the ID for the tenant the qualification type is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}
