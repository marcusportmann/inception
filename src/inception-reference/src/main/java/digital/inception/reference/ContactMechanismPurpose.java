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
 * The <code>ContactMechanismPurpose</code> class holds the information for a possible contact
 * mechanism purpose.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A type of person under a certain age, usually the age of majority in a given jurisdiction, which legally demarcates childhood from adulthood")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "type",
  "code",
  "localeId",
  "numericCode",
  "partyType",
  "sortIndex",
  "name",
  "description"
})
@XmlRootElement(name = "ContactMechanismPurpose", namespace = "http://reference.inception.digital")
@XmlType(
    name = "ContactMechanismPurpose",
    namespace = "http://reference.inception.digital",
    propOrder = {
      "type",
      "code",
      "localeId",
      "numericCode",
      "partyType",
      "sortIndex",
      "name",
      "description"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "reference", name = "contact_mechanism_purposes")
@IdClass(ContactMechanismPurposeId.class)
public class ContactMechanismPurpose implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the contact mechanism purpose. */
  @Schema(description = "The code for the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "code", nullable = false)
  private String code;

  /** The description for the contact mechanism purpose. */
  @Schema(description = "The description for the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", nullable = false)
  private String description;

  /** The Unicode locale identifier for the contact mechanism purpose. */
  @Schema(
      description = "The Unicode locale identifier for the contact mechanism purpose",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", nullable = false)
  private String localeId;

  /** The name of the contact mechanism purpose. */
  @Schema(description = "The name of the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", nullable = false)
  private String name;

  /** The numeric code for the contact mechanism purpose. */
  @Schema(description = "The numeric code for the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "NumericCode", required = true)
  @NotNull
  @Column(name = "numeric_code", nullable = false)
  private Integer numericCode;

  /** The code for the party type the contact mechanism purpose is associated with. */
  @Schema(
      description = "The code for the party type the contact mechanism purpose is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyType", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "party_type", nullable = false)
  private String partyType;

  /** The sort index for the contact mechanism purpose. */
  @Schema(description = "The sort index for the contact mechanism purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The code for the contact mechanism type the contact mechanism purpose is associated with. */
  @Schema(
      description =
          "The code for the contact mechanism type the contact mechanism purpose is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", nullable = false)
  private String type;

  /** Constructs a new <code>ContactMechanismPurpose</code>. */
  public ContactMechanismPurpose() {}

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

    ContactMechanismPurpose other = (ContactMechanismPurpose) object;

    return Objects.equals(type, other.type)
        && Objects.equals(code, other.code)
        && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the contact mechanism purpose.
   *
   * @return the code for the contact mechanism purpose
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the contact mechanism purpose.
   *
   * @return the description for the contact mechanism purpose
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the contact mechanism purpose.
   *
   * @return the Unicode locale identifier for the contact mechanism purpose
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the contact mechanism purpose.
   *
   * @return the name of the contact mechanism purpose
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the numeric code for the contact mechanism purpose.
   *
   * @return the numeric code for the contact mechanism purpose
   */
  public Integer getNumericCode() {
    return numericCode;
  }

  /**
   * Returns the code for the party type the contact mechanism purpose is associated with.
   *
   * @return the code for the party type the contact mechanism purpose is associated with
   */
  public String getPartyType() {
    return partyType;
  }

  /**
   * Returns the sort index for the contact mechanism purpose.
   *
   * @return the sort index for the contact mechanism purpose
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the code for the contact mechanism type the contact mechanism purpose is associated
   * with.
   *
   * @return the code for the contact mechanism type the contact mechanism purpose is associated
   *     with
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
    return ((type == null) ? 0 : type.hashCode())
        + ((code == null) ? 0 : code.hashCode())
        + ((localeId == null) ? 0 : localeId.hashCode());
  }

  /**
   * Set the code for the contact mechanism purpose.
   *
   * @param code the code for the contact mechanism purpose
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the contact mechanism purpose.
   *
   * @param description the description for the contact mechanism purpose
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the contact mechanism purpose.
   *
   * @param localeId the Unicode locale identifier for the contact mechanism purpose
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the contact mechanism purpose.
   *
   * @param name the name of the contact mechanism purpose
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the numeric code for the contact mechanism purpose.
   *
   * @param numericCode the numeric code for the contact mechanism purpose
   */
  public void setNumericCode(Integer numericCode) {
    this.numericCode = numericCode;
  }

  /**
   * Set the code for the party type the contact mechanism purpose is associated with.
   *
   * @param partyType the code for the party type the contact mechanism purpose is associated with
   */
  public void setPartyType(String partyType) {
    this.partyType = partyType;
  }

  /**
   * Set the sort index for the contact mechanism purpose.
   *
   * @param sortIndex the sort index for the contact mechanism purpose
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the code for the contact mechanism type the contact mechanism purpose is associated with.
   *
   * @param type the code for the contact mechanism type the contact mechanism purpose is
   *     associated with
   */
  public void setType(String type) {
    this.type = type;
  }
}
