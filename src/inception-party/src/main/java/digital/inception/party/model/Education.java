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
import com.github.f4b6a3.uuid.UuidCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
import java.util.UUID;

/**
 * The <b>Education</b> class holds the information for an education obtained by a person.
 *
 * <p>The primary key for the education entity (ID) is a surrogate key to support the management of
 * related data in one or more external stores, e.g. an image of a person's higher certificate
 * stored in an enterprise content management persistence. This approach allows an entity to be
 * modified without impacting the related data's referential integrity, for example, when correcting
 * an error that occurred during the initial capture of the information for an education.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An education obtained by a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "institutionCountry",
  "institutionName",
  "qualificationType",
  "qualificationName",
  "qualificationYear",
  "fieldOfStudy",
  "firstYearAttended",
  "lastYearAttended"
})
@XmlRootElement(name = "Education", namespace = "https://inception.digital/party")
@XmlType(
    name = "Education",
    namespace = "https://inception.digital/party",
    propOrder = {
      "id",
      "institutionCountry",
      "institutionName",
      "qualificationType",
      "qualificationName",
      "qualificationYear",
      "fieldOfStudy",
      "firstYearAttended",
      "lastYearAttended"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_educations")
public class Education implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the field of study. */
  @Schema(description = "The code for the field of study")
  @JsonProperty
  @XmlElement(name = "FieldOfStudy")
  @Size(min = 1, max = 50)
  @Column(name = "field_of_study", length = 50)
  private String fieldOfStudy;

  /** The first year attended. */
  @Schema(description = "The first year attended")
  @JsonProperty
  @XmlElement(name = "FirstYearAttended")
  @Column(name = "first_year_attended")
  private Integer firstYearAttended;

  /** The ID for the education. */
  @Schema(description = "The ID for the education", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The ISO 3166-1 alpha-2 code for the country the educational institution is located in. */
  @Schema(
      description =
          "The ISO 3166-1 alpha-2 code for the country the educational institution is located in")
  @JsonProperty
  @XmlElement(name = "InstitutionCountry")
  @Size(min = 2, max = 2)
  @Column(name = "institution_country", length = 2)
  private String institutionCountry;

  /** The name of the educational institution. */
  @Schema(
      description = "The name of the educational institution",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "InstitutionName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Education.InstitutionName.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,' ]*(?!\\s+)$")
  @Column(name = "institution_name", length = 100, nullable = false)
  private String institutionName;

  /** The last year attended. */
  @Schema(description = "The last year attended")
  @JsonProperty
  @XmlElement(name = "LastYearAttended")
  @Column(name = "last_year_attended")
  private Integer lastYearAttended;

  /** The person the education is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("educationReference")
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id")
  private Person person;

  /** The name of the qualification. */
  @Schema(description = "The name of the qualification")
  @JsonProperty
  @XmlElement(name = "QualificationName")
  @Size(min = 1, max = 100)
  @Pattern(
      message = "{digital.inception.party.Education.QualificationName.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,' ]*(?!\\s+)$")
  @Column(name = "qualification_name", length = 100)
  private String qualificationName;

  /** The code for the qualification type. */
  @Schema(
      description = "The code for the qualification type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "QualificationType", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "qualification_type", length = 50, nullable = false)
  private String qualificationType;

  /** The year the qualification was obtained. */
  @Schema(
      description = "The year the qualification was obtained",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "QualificationYear", required = true)
  @NotNull
  @Column(name = "qualification_year", nullable = false)
  private Integer qualificationYear;

  /** Constructs a new <b>Education</b>. */
  public Education() {}

  /**
   * Constructs a new <b>Education</b>.
   *
   * @param institutionCountry the ISO 3166-1 alpha-2 code for the country the educational
   *     institution is located in
   * @param institutionName the name of the educational institution
   * @param qualificationType the code for the qualification type
   * @param qualificationName the name of the qualification
   * @param qualificationYear the year the qualification was obtained
   * @param fieldOfStudy the code for the field of study
   * @param firstYearAttended the first year attended
   * @param lastYearAttended the last year attended
   */
  public Education(
      String institutionCountry,
      String institutionName,
      String qualificationType,
      String qualificationName,
      Integer qualificationYear,
      String fieldOfStudy,
      Integer firstYearAttended,
      Integer lastYearAttended) {
    this.id = UuidCreator.getShortPrefixComb();
    this.institutionCountry = institutionCountry;
    this.institutionName = institutionName;
    this.qualificationType = qualificationType;
    this.qualificationName = qualificationName;
    this.qualificationYear = qualificationYear;
    this.fieldOfStudy = fieldOfStudy;
    this.firstYearAttended = firstYearAttended;
    this.lastYearAttended = lastYearAttended;
  }

  /**
   * Constructs a new <b>Education</b>.
   *
   * @param institutionName the name of the educational institution
   * @param qualificationType the code for the qualification type
   * @param qualificationYear the year the qualification was obtained
   */
  public Education(String institutionName, String qualificationType, Integer qualificationYear) {
    this.id = UuidCreator.getShortPrefixComb();
    this.institutionName = institutionName;
    this.qualificationType = qualificationType;
    this.qualificationYear = qualificationYear;
    this.fieldOfStudy = fieldOfStudy;
  }

  /**
   * Constructs a new <b>Education</b>.
   *
   * @param institutionName the name of the educational institution
   * @param qualificationType the code for the qualification type
   * @param qualificationName the name of the qualification
   * @param qualificationYear the year the qualification was obtained
   * @param fieldOfStudy the code for the field of study
   */
  public Education(
      String institutionName,
      String qualificationType,
      String qualificationName,
      Integer qualificationYear,
      String fieldOfStudy) {
    this.id = UuidCreator.getShortPrefixComb();
    this.institutionName = institutionName;
    this.qualificationType = qualificationType;
    this.qualificationName = qualificationName;
    this.qualificationYear = qualificationYear;
    this.fieldOfStudy = fieldOfStudy;
  }

  /**
   * Constructs a new <b>Education</b>.
   *
   * @param institutionCountry the ISO 3166-1 alpha-2 code for the country the educational
   *     institution is located in
   * @param institutionName the name of the educational institution
   * @param qualificationType the code for the qualification type
   * @param qualificationName the name of the qualification
   * @param qualificationYear the year the qualification was obtained
   * @param fieldOfStudy the code for the field of study
   */
  public Education(
      String institutionCountry,
      String institutionName,
      String qualificationType,
      String qualificationName,
      Integer qualificationYear,
      String fieldOfStudy) {
    this.id = UuidCreator.getShortPrefixComb();
    this.institutionCountry = institutionCountry;
    this.institutionName = institutionName;
    this.qualificationType = qualificationType;
    this.qualificationName = qualificationName;
    this.qualificationYear = qualificationYear;
    this.fieldOfStudy = fieldOfStudy;
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

    Education other = (Education) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the code for the field of study.
   *
   * @return the code for the field of study
   */
  public String getFieldOfStudy() {
    return fieldOfStudy;
  }

  /**
   * Returns the first year attended.
   *
   * @return the first year attended
   */
  public Integer getFirstYearAttended() {
    return firstYearAttended;
  }

  /**
   * Returns the ID for the education.
   *
   * @return the ID for the education
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country the educational institution is located in.
   *
   * @return the ISO 3166-1 alpha-2 code for the country the educational institution is located in
   */
  public String getInstitutionCountry() {
    return institutionCountry;
  }

  /**
   * Returns the name of the educational institution.
   *
   * @return the name of the educational institution
   */
  public String getInstitutionName() {
    return institutionName;
  }

  /**
   * Returns the last year attended.
   *
   * @return the last year attended
   */
  public Integer getLastYearAttended() {
    return lastYearAttended;
  }

  /**
   * Returns the person the education is associated with.
   *
   * @return the person the education is associated with
   */
  @Schema(hidden = true)
  public Person getPerson() {
    return person;
  }

  /**
   * Returns the name of the qualification.
   *
   * @return the name of the qualification
   */
  public String getQualificationName() {
    return qualificationName;
  }

  /**
   * Returns the code for the qualification type.
   *
   * @return the code for the qualification type
   */
  public String getQualificationType() {
    return qualificationType;
  }

  /**
   * Returns the year the qualification was obtained.
   *
   * @return the year the qualification was obtained
   */
  public Integer getQualificationYear() {
    return qualificationYear;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Set the code for the field of study.
   *
   * @param fieldOfStudy the code for the field of study
   */
  public void setFieldOfStudy(String fieldOfStudy) {
    this.fieldOfStudy = fieldOfStudy;
  }

  /**
   * Set the first year attended.
   *
   * @param firstYearAttended the first year attended
   */
  public void setFirstYearAttended(Integer firstYearAttended) {
    this.firstYearAttended = firstYearAttended;
  }

  /**
   * Set the ID for the education.
   *
   * @param id the ID for the education
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country the educational institution is located in
   *
   * @param institutionCountry the ISO 3166-1 alpha-2 code for the country the educational
   *     institution is located in
   */
  public void setInstitutionCountry(String institutionCountry) {
    this.institutionCountry = institutionCountry;
  }

  /**
   * Set the name of the educational institution.
   *
   * @param institutionName name of the educational institution
   */
  public void setInstitutionName(String institutionName) {
    this.institutionName = institutionName;
  }

  /**
   * Set the last year attended.
   *
   * @param lastYearAttended the last year attended
   */
  public void setLastYearAttended(Integer lastYearAttended) {
    this.lastYearAttended = lastYearAttended;
  }

  /**
   * Set the person the education is associated with.
   *
   * @param person the person the education is associated with
   */
  @Schema(hidden = true)
  public void setPerson(Person person) {
    this.person = person;
  }

  /**
   * Set the name of the qualification.
   *
   * @param qualificationName the name of the qualification
   */
  public void setQualificationName(String qualificationName) {
    this.qualificationName = qualificationName;
  }

  /**
   * Set the code for the qualification type.
   *
   * @param qualificationType the code for the qualification type
   */
  public void setQualificationType(String qualificationType) {
    this.qualificationType = qualificationType;
  }

  /**
   * Set the year the qualification was obtained.
   *
   * @param qualificationYear the year the qualification was obtained
   */
  public void setQualificationYear(Integer qualificationYear) {
    this.qualificationYear = qualificationYear;
  }
}
