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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
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
 * The <b>QualificationType</b> class holds the information for a qualification type.
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
  "sortIndex",
  "name",
  "description",
  "fieldOfStudy"
})
@XmlRootElement(name = "QualificationType", namespace = "http://inception.digital/party")
@XmlType(
    name = "QualificationType",
    namespace = "http://inception.digital/party",
    propOrder = {
      "code",
      "localeId",
      "tenantId",
      "sortIndex",
      "name",
      "description",
      "fieldOfStudy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "qualification_types")
@IdClass(QualificationTypeId.class)
public class QualificationType implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the qualification type. */
  @Schema(description = "The code for the qualification type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "code", length = 30, nullable = false)
  private String code;

  /** The description for the qualification type. */
  @Schema(description = "The description for the qualification type", required = true)
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
  @Size(min = 1, max = 30)
  @Column(name = "field_of_study", length = 30)
  private String fieldOfStudy;

  /** The Unicode locale identifier for the qualification type. */
  @Schema(description = "The Unicode locale identifier for the qualification type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the qualification type. */
  @Schema(description = "The name of the qualification type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The sort index for the qualification type. */
  @Schema(description = "The sort index for the qualification type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The ID for the tenant the qualification type is specific to. */
  @Schema(description = "The ID for the tenant the qualification type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new <b>QualificationType</b>. */
  public QualificationType() {}

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
   * Returns the sort index for the qualification type.
   *
   * @return the sort index for the qualification type
   */
  public Integer getSortIndex() {
    return sortIndex;
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
   * Set the sort index for the qualification type.
   *
   * @param sortIndex the sort index for the qualification type
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
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
