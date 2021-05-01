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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.springframework.util.StringUtils;

/**
 * The <b>Segment</b> class holds the information for a segment.
 *
 * <p>A segment is a subdivision of the total population of organizations or persons based on a
 * specific attribute or attributes.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A segment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "localeId",
  "tenantId",
  "sortIndex",
  "name",
  "description",
  "partyTypes"
})
@XmlRootElement(name = "Segment", namespace = "http://inception.digital/party")
@XmlType(
    name = "Segment",
    namespace = "http://inception.digital/party",
    propOrder = {"code", "localeId", "tenantId", "sortIndex", "name", "description", "partyTypes"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "segments")
@IdClass(SegmentId.class)
public class Segment implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the segment. */
  @Schema(description = "The code for the segment", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "code", length = 30, nullable = false)
  private String code;

  /** The description for the segment. */
  @Schema(description = "The description for the segment", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the segment. */
  @Schema(description = "The Unicode locale identifier for the segment", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the segment. */
  @Schema(description = "The name of the segment", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The comma-delimited codes for the party types the segment is associated with. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Size(min = 1, max = 310)
  @Column(name = "party_types", length = 310, nullable = false)
  private String partyTypes;

  /** The sort index for the segment. */
  @Schema(description = "The sort index for the segment", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** The Universally Unique Identifier (UUID) for the tenant the segment is specific to. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the tenant the segment is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new <b>Segment</b>. */
  public Segment() {}

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

    Segment other = (Segment) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the segment.
   *
   * @return the code for the segment
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the segment.
   *
   * @return the description for the segment
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the segment.
   *
   * @return the Unicode locale identifier for the segment
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the segment.
   *
   * @return the name of the segment
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the codes for the party types the segment is associated with.
   *
   * @return the codes for the party types the segment is associated with
   */
  @Schema(
      description = "The codes for the party types the segment is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyTypes", required = true)
  public String[] getPartyTypes() {
    return StringUtils.commaDelimitedListToStringArray(partyTypes);
  }

  /**
   * Returns the sort index for the segment.
   *
   * @return the sort index for the segment
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the segment is specific to.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the segment is specific to
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
   * Returns whether the segment is valid for the party type.
   *
   * @param partyTypeCode the code for the party type
   * @return <b>true</b> if the segment is valid for the party type or <b>false</b> otherwise
   */
  public boolean isValidForPartyType(String partyTypeCode) {
    return Arrays.stream(getPartyTypes())
        .anyMatch(validPartyType -> validPartyType.equals(partyTypeCode));
  }

  /**
   * Set the code for the segment.
   *
   * @param code the code for the segment
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the segment.
   *
   * @param description the description for the segment
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the segment.
   *
   * @param localeId the Unicode locale identifier for the segment
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the segment.
   *
   * @param name the name of the segment
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the codes for the party types the segment is associated with.
   *
   * @param partyTypes the codes for the party types the segment is associated with
   */
  public void setPartyTypes(String[] partyTypes) {
    this.partyTypes = StringUtils.arrayToCommaDelimitedString(partyTypes);
  }

  /**
   * Set the codes for the party types the segment is associated with.
   *
   * @param partyTypes the codes for the party types the segment is associated with
   */
  @JsonIgnore
  public void setPartyTypes(Collection<String> partyTypes) {
    this.partyTypes = StringUtils.collectionToDelimitedString(partyTypes, ",");
  }

  /**
   * Set the sort index for the segment.
   *
   * @param sortIndex the sort index for the segment
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the segment is specific to.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the segment is specific
   *     to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}
