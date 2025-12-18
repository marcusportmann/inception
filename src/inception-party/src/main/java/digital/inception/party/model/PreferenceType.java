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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.jpa.StringListAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/**
 * The {@code PreferenceType} class holds the information for a preference type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A type of preference")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "category",
  "localeId",
  "tenantId",
  "sortOrder",
  "name",
  "description",
  "partyTypes",
  "pattern"
})
@XmlRootElement(name = "PreferenceType", namespace = "https://inception.digital/party")
@XmlType(
    name = "PreferenceType",
    namespace = "https://inception.digital/party",
    propOrder = {
      "code",
      "category",
      "localeId",
      "tenantId",
      "sortOrder",
      "name",
      "description",
      "partyTypes",
      "pattern"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_preference_types")
@IdClass(PreferenceTypeId.class)
public class PreferenceType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the preference type category the preference type is associated with. */
  @Schema(
      description =
          "The code for the preference type category the preference type is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Category", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "category", length = 50, nullable = false)
  private String category;

  /** The code for the preference type. */
  @Schema(
      description = "The code for the preference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /** The compiled pattern. */
  @JsonIgnore private transient Pattern compiledPattern;

  /** The description for the preference type. */
  @Schema(
      description = "The description for the preference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", length = 200, nullable = false)
  private String description;

  /** The Unicode locale identifier for the preference type. */
  @Schema(
      description = "The Unicode locale identifier for the preference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", length = 10, nullable = false)
  private String localeId;

  /** The name of the preference type. */
  @Schema(
      description = "The name of the preference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The codes for the party types the preference type is associated with. */
  @Schema(
      description = "The codes for the party types the preference type is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "PartyTypes", required = true)
  @XmlElement(name = "PartyType", required = true)
  @NotNull
  @Size(min = 1, max = 10)
  @Convert(converter = StringListAttributeConverter.class)
  @Column(name = "party_types", length = 510, nullable = false)
  private List<String> partyTypes;

  /** The regular expression pattern used to validate a string value for the preference type. */
  @Schema(
      description =
          "The regular expression pattern used to validate a string value for the preference type")
  @JsonProperty
  @XmlElement(name = "Pattern")
  @Size(min = 1, max = 1000)
  @Column(name = "pattern", length = 1000)
  private String pattern;

  /** The sort order for the preference type. */
  @Schema(
      description = "The sort order for the preference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortOrder", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortOrder;

  /** The ID for the tenant the preference type is specific to. */
  @Schema(description = "The ID for the tenant the preference type is specific to")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** Constructs a new {@code PreferenceType}. */
  public PreferenceType() {}

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

    PreferenceType other = (PreferenceType) object;

    return Objects.equals(code, other.code) && Objects.equals(localeId, other.localeId);
  }

  /**
   * Returns the code for the preference type category the preference type is associated with.
   *
   * @return the code for the preference type category the preference type is associated with
   */
  public String getCategory() {
    return category;
  }

  /**
   * Returns the code for the preference type.
   *
   * @return the code for the preference type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the compiled pattern.
   *
   * @return the compiled pattern
   */
  @JsonIgnore
  public Pattern getCompiledPattern() {
    if (compiledPattern == null) {
      compiledPattern = Pattern.compile(pattern);
    }

    return compiledPattern;
  }

  /**
   * Returns the description for the preference type.
   *
   * @return the description for the preference type
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the preference type.
   *
   * @return the Unicode locale identifier for the preference type
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the preference type.
   *
   * @return the name of the preference type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the codes for the party types the preference type is associated with.
   *
   * @return the codes for the party types the preference type is associated with
   */
  public List<String> getPartyTypes() {
    return partyTypes;
  }

  /**
   * Returns the regular expression pattern used to validate a string value for the preference type.
   *
   * @return the regular expression pattern used to validate a string value for the preference type
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * Returns the sort order for the preference type.
   *
   * @return the sort order for the preference type
   */
  public Integer getSortOrder() {
    return sortOrder;
  }

  /**
   * Returns the ID for the tenant the preference type is specific to.
   *
   * @return the ID for the tenant the preference type is specific to
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
   * Returns whether the preference type is valid for the party type.
   *
   * @param partyTypeCode the code for the party type
   * @return {@code true} if the preference type is valid for the party type or {@code false}
   *     otherwise
   */
  public boolean isValidForPartyType(String partyTypeCode) {
    if (!StringUtils.hasText(partyTypeCode)) {
      return false;
    }

    return partyTypes.contains(partyTypeCode);
  }

  /**
   * Sets the code for the preference type category the preference type is associated with.
   *
   * @param category the code for the preference type category the preference type is associated
   *     with
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Sets the code for the preference type.
   *
   * @param code the code for the preference type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Sets the description for the preference type.
   *
   * @param description the description for the preference type
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the Unicode locale identifier for the preference type.
   *
   * @param localeId the Unicode locale identifier for the preference type
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Sets the name of the preference type.
   *
   * @param name the name of the preference type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the codes for the party types the preference type is associated with.
   *
   * @param partyTypes the codes for the party types the preference type is associated with
   */
  public void setPartyTypes(List<String> partyTypes) {
    this.partyTypes = partyTypes;
  }

  /**
   * Sets the regular expression pattern used to validate a string value for the preference type.
   *
   * @param pattern the regular expression pattern used to validate a string value for the
   *     preference type
   */
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  /**
   * Sets the sort order for the preference type.
   *
   * @param sortOrder the sort order for the preference type
   */
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  /**
   * Sets the ID for the tenant the preference type is specific to.
   *
   * @param tenantId the ID for the tenant the preference type is specific to
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }
}
