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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code InteractionSourceSummary} class holds the summary information for an interaction
 * source.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An interaction source summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "tenantId", "name", "type", "enabled", "permissions"})
@XmlRootElement(
    name = "InteractionSourceSummary",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionSourceSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "tenantId", "name", "type", "enabled", "permissions"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interaction_sources")
public class InteractionSourceSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The permissions for the interaction source. */
  @Schema(description = "The permissions for the interaction source")
  @JsonProperty
  @JsonManagedReference("interactionSourcePermissionReference")
  @XmlElementWrapper(name = "Permissions")
  @XmlElement(name = "Permission")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("roleCode")
  @JoinColumn(name = "source_id", insertable = false, updatable = false)
  private final List<InteractionSourcePermission> permissions = new ArrayList<>();

  /**
   * Is the interaction source enabled?
   *
   * <p>If the interaction source is not enabled, it will not be synchronized.
   */
  @Schema(
      description = "Is the interaction source enabled",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Enabled", required = true)
  @NotNull
  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  /** The ID for the interaction source. */
  @Schema(
      description = "The ID for the interaction source",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name of the interaction source. */
  @Schema(
      description = "The name of the interaction source",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  /** The ID for the tenant the interaction source is associated with. */
  @Schema(
      description = "The ID for the tenant the interaction source is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The interaction source type. */
  @Schema(description = "The interaction source type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", length = 50, nullable = false)
  private InteractionSourceType type;

  /** Constructs a new {@code InteractionSourceSummary}. */
  public InteractionSourceSummary() {}

  /**
   * Constructs a new {@code InteractionSourceSummary}.
   *
   * @param id the ID for the interaction source
   * @param tenantId the ID for the tenant the interaction source is associated with
   * @param type the interaction source type
   * @param name the name of the interaction source
   * @param enabled is the interaction source enabled
   */
  public InteractionSourceSummary(
      UUID id, UUID tenantId, InteractionSourceType type, String name, boolean enabled) {
    this.id = id;
    this.tenantId = tenantId;
    this.type = type;
    this.name = name;
    this.enabled = enabled;
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

    InteractionSourceSummary other = (InteractionSourceSummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the interaction source.
   *
   * @return the ID for the interaction source
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the interaction source.
   *
   * @return the name of the interaction source
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the permissions for the interaction source.
   *
   * @return the permissions for the interaction source
   */
  public List<InteractionSourcePermission> getPermissions() {
    return permissions;
  }

  /**
   * Returns the ID for the tenant the interaction source is associated with.
   *
   * @return the ID for the tenant the interaction source is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the interaction source type.
   *
   * @return the interaction source type
   */
  public InteractionSourceType getType() {
    return type;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Returns whether the interaction source is enabled.
   *
   * @return {@code true} if the interaction source is enabled or {@code} false otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }
}
