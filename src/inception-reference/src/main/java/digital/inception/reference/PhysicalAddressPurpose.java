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
import java.util.Collection;
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
import org.springframework.util.StringUtils;

/**
 * The <code>PhysicalAddressPurpose</code> class holds the information for a possible physical
 * address purpose.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A physical address purpose")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "localeId",
  "partyTypes",
  "sortIndex",
  "name",
  "description"
})
@XmlRootElement(name = "PhysicalAddressPurpose", namespace = "http://reference.inception.digital")
@XmlType(
    name = "PhysicalAddressPurpose",
    namespace = "http://reference.inception.digital",
    propOrder = {
      "code",
      "localeId",
      "partyTypes",
      "sortIndex",
      "name",
      "description"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "reference", name = "physical_address_purposes")
@IdClass(PhysicalAddressPurposeId.class)
public class PhysicalAddressPurpose implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the physical address purpose. */
  @Schema(description = "The code for the physical address purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "code", nullable = false)
  private String code;

  /** The description for the physical address purpose. */
  @Schema(description = "The description for the physical address purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", nullable = false)
  private String description;

  /** The Unicode locale identifier for the physical address purpose. */
  @Schema(
      description = "The Unicode locale identifier for the physical address purpose",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", nullable = false)
  private String localeId;

  /** The name of the physical address purpose. */
  @Schema(description = "The name of the physical address purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * The comma-delimited codes for the party types the physical address purpose is associated with.
   */
  @NotNull
  @Size(min = 1, max = 300)
  @Column(name = "party_types", nullable = false)
  private String partyTypes;

  /** The sort index for the physical address purpose. */
  @Schema(description = "The sort index for the physical address purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** Constructs a new <code>PhysicalAddressPurpose</code>. */
  public PhysicalAddressPurpose() {}

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

    PhysicalAddressPurpose other = (PhysicalAddressPurpose) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the physical address purpose.
   *
   * @return the code for the physical address purpose
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the physical address purpose.
   *
   * @return the description for the physical address purpose
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the physical address purpose.
   *
   * @return the Unicode locale identifier for the physical address purpose
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the physical address purpose.
   *
   * @return the name of the physical address purpose
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the codes for the party types the physical address purpose is associated with.
   *
   * @return the codes for the party types the physical address purpose is associated with
   */
  @Schema(
      description = "The codes for the party types the physical address purpose is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyTypes", required = true)
  public String[] getPartyTypes() {
    return StringUtils.commaDelimitedListToStringArray(partyTypes);
  }

  /**
   * Returns the sort index for the physical address purpose.
   *
   * @return the sort index for the physical address purpose
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
   * Set the code for the physical address purpose.
   *
   * @param code the code for the physical address purpose
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the physical address purpose.
   *
   * @param description the description for the physical address purpose
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the physical address purpose.
   *
   * @param localeId the Unicode locale identifier for the physical address purpose
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the physical address purpose.
   *
   * @param name the name of the physical address purpose
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the codes for the party types the physical address purpose is associated with.
   *
   * @param partyTypes the codes for the party types the physical address purpose is associated with
   */
  public void setPartyTypes(String[] partyTypes) {
    this.partyTypes = StringUtils.arrayToCommaDelimitedString(partyTypes);
  }

  /**
   * Set the codes for the party types the physical address purpose is associated with.
   *
   * @param partyTypes the codes for the party types the physical address purpose is associated with
   */
  public void setPartyTypes(Collection<String> partyTypes) {
    this.partyTypes = StringUtils.collectionToDelimitedString(partyTypes, ",");
  }

  /**
   * Set the sort index for the physical address purpose.
   *
   * @param sortIndex the sort index for the physical address purpose
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }
}
