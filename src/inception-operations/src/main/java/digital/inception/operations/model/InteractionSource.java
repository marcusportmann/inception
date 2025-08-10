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
import digital.inception.core.util.StringUtil;
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
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code InteractionSource} class holds the information for an interaction source.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An interaction source")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "tenantId", "name", "type", "permissions", "attributes"})
@XmlRootElement(name = "InteractionSource", namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionSource",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "tenantId", "name", "type", "permissions", "attributes"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interaction_sources")
public class InteractionSource implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attributes for the interaction source. */
  @Schema(description = "The attributes for the interaction source")
  @JsonProperty
  @JsonManagedReference("interactionSourceAttributeReference")
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderBy("code")
  @JoinColumn(name = "source_id", insertable = false, updatable = false)
  private final List<InteractionSourceAttribute> attributes = new ArrayList<>();

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

  /** Constructs a new {@code InteractionSource}. */
  public InteractionSource() {}

  // TODO: ADD AUDIT CAPABILIGTIES For all interaction-related operations

  /**
   * Constructs a new {@code InteractionSource}.
   *
   * @param id the ID for the interaction source
   * @param tenantId the ID for the tenant the interaction source is associated with
   * @param name the name of the interaction source
   * @param type the interaction source type
   * @param attributes the attributes for the interaction source
   */
  public InteractionSource(
      UUID id,
      UUID tenantId,
      String name,
      InteractionSourceType type,
      List<InteractionSourceAttribute> attributes) {
    this.id = id;
    this.tenantId = tenantId;
    this.name = name;
    this.type = type;
    setAttributes(attributes);
  }

  /**
   * Constructs a new mailbox {@code InteractionSource}.
   *
   * @param id the ID for the mailbox interaction source
   * @param tenantId the ID for the tenant the mailbox interaction source is associated with
   * @param name the name of the mailbox interaction source
   * @param protocol the service provider and email protocol for the service hosting the mailbox
   * @param host the hostname or IP address for the service hosting the mailbox
   * @param port the network port for the service hosting the mailbox
   * @param principal the principal identifying the entity that is attempting to authenticate and
   *     gain access to the mailbox
   * @param credential the credential used to authenticate the entity attempting to access the
   *     mailbox
   * @param emailAddress the email address for the mailbox
   * @param archiveMail should mail retrieved from the mailbox be archived
   * @param deleteMail should mail retrieved from the mailbox be deleted
   * @param debug is debugging enabled for the mailbox interaction source
   * @return the mailbox {@code InteractionSource}
   */
  public static InteractionSource createMailboxInteractionSource(
      UUID id,
      UUID tenantId,
      String name,
      MailboxProtocol protocol,
      String host,
      int port,
      String principal,
      String credential,
      String emailAddress,
      boolean archiveMail,
      boolean deleteMail,
      boolean debug) {
    return new InteractionSource(
        id,
        tenantId,
        name,
        InteractionSourceType.MAILBOX,
        List.of(
            new InteractionSourceAttribute(
                MailboxInteractionSourceAttributeName.PROTOCOL.code(), protocol.code()),
            new InteractionSourceAttribute(MailboxInteractionSourceAttributeName.HOST.code(), host),
            new InteractionSourceAttribute(
                MailboxInteractionSourceAttributeName.PORT.code(), String.valueOf(port)),
            new InteractionSourceAttribute(
                MailboxInteractionSourceAttributeName.PRINCIPAL.code(), principal),
            new InteractionSourceAttribute(
                MailboxInteractionSourceAttributeName.CREDENTIAL.code(), credential),
            new InteractionSourceAttribute(
                MailboxInteractionSourceAttributeName.EMAIL_ADDRESS.code(), emailAddress),
            new InteractionSourceAttribute(
                MailboxInteractionSourceAttributeName.ARCHIVE_MAIL.code(),
                String.valueOf(archiveMail)),
            new InteractionSourceAttribute(
                MailboxInteractionSourceAttributeName.DELETE_MAIL.code(),
                String.valueOf(deleteMail)),
            new InteractionSourceAttribute(
                MailboxInteractionSourceAttributeName.DEBUG.code(), String.valueOf(debug))));
  }

  /**
   * Constructs a new virtual {@code InteractionSource}.
   *
   * @param id the ID for the virtual interaction source
   * @param tenantId the ID for the tenant the virtual interaction source is associated with
   * @param name the name of the virtual interaction source
   * @return the virtual {@code InteractionSource}
   */
  public static InteractionSource createVirtualInteractionSource(
      UUID id, UUID tenantId, String name) {
    return new InteractionSource(id, tenantId, name, InteractionSourceType.VIRTUAL, List.of());
  }

  /**
   * Constructs a new WhatsApp {@code InteractionSource}.
   *
   * @param id the ID for the WhatsApp interaction source
   * @param tenantId the ID for the tenant the WhatsApp interaction source is associated with
   * @param name the name of the WhatsApp interaction source
   * @param debug is debugging enabled for the WhatsApp interaction source
   * @return the WhatsApp {@code InteractionSource}
   */
  public static InteractionSource createWhatsAppInteractionSource(
      UUID id, UUID tenantId, String name, boolean debug) {
    return new InteractionSource(
        id,
        tenantId,
        name,
        InteractionSourceType.WHATSAPP,
        List.of(
            new InteractionSourceAttribute(
                WhatsAppInteractionSourceAttributeName.DEBUG.code(), String.valueOf(debug))));
  }

  /**
   * Add the attribute for the interaction source.
   *
   * @param attribute the attribute
   */
  public void addAttribute(InteractionSourceAttribute attribute) {
    attributes.removeIf(
        existingAttribute ->
            StringUtil.equalsIgnoreCase(existingAttribute.getCode(), attribute.getCode()));

    attribute.setInteractionSource(this);

    attributes.add(attribute);
  }

  /**
   * Add the permission for the interaction source.
   *
   * @param permission the permission
   */
  public void addPermission(InteractionSourcePermission permission) {
    permissions.removeIf(
        existingPermission ->
            (StringUtil.equalsIgnoreCase(existingPermission.getRoleCode(), permission.getRoleCode())
                && existingPermission.getType() == permission.getType()));

    permission.setInteractionSource(this);

    permissions.add(permission);
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

    InteractionSource other = (InteractionSource) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Retrieve the attribute with the specified code for the interaction source.
   *
   * @param code the code for the attribute
   * @return an Optional containing the attribute with the specified code for the interaction source
   *     or an empty Optional if the attribute could not be found
   */
  public Optional<InteractionSourceAttribute> getAttributeWithCode(String code) {
    return attributes.stream()
        .filter(attribute -> StringUtil.equalsIgnoreCase(attribute.getCode(), code))
        .findFirst();
  }

  /**
   * Returns the attributes for the interaction source.
   *
   * @return the attributes for the interaction source
   */
  public List<InteractionSourceAttribute> getAttributes() {
    return attributes;
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
   * Returns whether the interaction source has an attribute with the specified code.
   *
   * @param code the code for the attribute
   * @return {@code true} if the interaction source has an attribute with the specified code or
   *     {@code false} otherwise
   */
  public boolean hasAttributeWithCode(String code) {
    return attributes.stream()
        .anyMatch(attribute -> StringUtil.equalsIgnoreCase(attribute.getCode(), code));
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
   * Remove the attribute with the specified code for the interaction source.
   *
   * @param code the code for the attribute
   */
  public void removeAttributeWithCode(String code) {
    attributes.removeIf(
        existingAttribute -> StringUtil.equalsIgnoreCase(existingAttribute.getCode(), code));
  }

  /**
   * Remove the permission with the specified role code and interaction source permission type for
   * the interaction source.
   *
   * @param roleCode the role code for the permission
   * @param type the interaction source permission type
   */
  public void removePermission(String roleCode, InteractionSourcePermissionType type) {
    permissions.removeIf(
        existingPermission ->
            (StringUtil.equalsIgnoreCase(existingPermission.getRoleCode(), roleCode)
                && (existingPermission.getType() == type)));
  }

  /**
   * Set the attributes for the interaction source.
   *
   * @param attributes the attributes for the interaction source
   */
  public void setAttributes(List<InteractionSourceAttribute> attributes) {
    attributes.forEach(attribute -> attribute.setInteractionSource(this));
    this.attributes.clear();
    this.attributes.addAll(attributes);
  }

  /**
   * Set the ID for the interaction source.
   *
   * @param id the ID for the interaction source
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the interaction source.
   *
   * @param name the name of the interaction source
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the permissions for the interaction source.
   *
   * @param permissions the permissions for the interaction source
   */
  public void setPermissions(List<InteractionSourcePermission> permissions) {
    permissions.forEach(permission -> permission.setInteractionSource(this));
    this.permissions.clear();
    this.permissions.addAll(permissions);
  }

  /**
   * Set the ID for the tenant the interaction source is associated with.
   *
   * @param tenantId the ID for the tenant the interaction source is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the interaction source type.
   *
   * @param type the interaction source type
   */
  public void setType(InteractionSourceType type) {
    this.type = type;
  }
}
