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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import digital.inception.operations.persistence.jpa.RecipientsAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code InteractionSummary} class holds the information for an interaction summary.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An interaction summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "status",
  "sourceId",
  "conversationId",
  "partyId",
  "type",
  "direction",
  "sender",
  "recipients",
  "subject",
  "mimeType",
  "occurred",
  "assigned",
  "assignedTo"
})
@XmlRootElement(name = "InteractionSummary", namespace = "https://inception.digital/operations")
@XmlType(
    name = "InteractionSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "status",
      "sourceId",
      "conversationId",
      "partyId",
      "type",
      "direction",
      "sender",
      "recipients",
      "subject",
      "mimeType",
      "occurred",
      "assigned",
      "assignedTo"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interactions")
@SuppressWarnings({"unused", "WeakerAccess"})
public class InteractionSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date and time the interaction was assigned. */
  @Schema(description = "The date and time the interaction was assigned")
  @JsonProperty
  @XmlElement(name = "Timestamp")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "assigned")
  private OffsetDateTime assigned;

  /** The username for the user the interaction is assigned to. */
  @Schema(description = "The username for the user the interaction is assigned to")
  @JsonProperty
  @XmlElement(name = "AssignedTo")
  @Size(min = 1, max = 100)
  @Column(name = "assigned_to", length = 100)
  private String assignedTo;

  /** The ID for the conversation the interaction is associated with. */
  @Schema(description = "The ID for the conversation the interaction is associated with")
  @JsonProperty
  @XmlElement(name = "ConversationId")
  @Size(min = 1, max = 30)
  @Column(name = "conversation_id", length = 30)
  private String conversationId;

  /** The direction for the interaction, i.e., inbound or outbound. */
  @Schema(
      description = "The direction for the interaction, i.e., inbound or outbound",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Direction", required = true)
  @NotNull
  @Column(name = "direction", nullable = false)
  private InteractionDirection direction;

  /** The ID for the interaction. */
  @Schema(description = "The ID for the interaction", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The mime type for the content for the interaction. */
  @Schema(
      description = "The mime type for the content for the interaction",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "MimeType", required = true)
  @NotNull
  @Column(name = "mime_type", nullable = false)
  private InteractionMimeType mimeType;

  /** The date and time the interaction occurred (received if inbound, sent if outbound). */
  @Schema(
      description =
          "The date and time the interaction occurred (received if inbound, sent if outbound)",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Occurred", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "occurred", nullable = false)
  private OffsetDateTime occurred;

  /** The ID for the party the interaction is associated with. */
  @Schema(description = "The ID for the party the interaction is associated with")
  @JsonProperty
  @XmlElement(name = "PartyId")
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /**
   * The identifiers representing the recipients for the interaction, e.g. email addresses, mobile
   * numbers, etc. Stored as a comma-delimited list in the database.
   */
  @Schema(
      description =
          "The identifiers representing the recipients for the interaction, e.g. email addresses, mobile numbers, etc",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Recipients", required = true)
  @XmlElement(name = "Recipient", required = true)
  @Convert(converter = RecipientsAttributeConverter.class)
  @NotNull
  @Column(name = "recipients", length = 2000, nullable = false)
  private List<String> recipients;

  /**
   * The identifier representing who the interaction is from, e.g. an email address, a mobile
   * number, etc.
   */
  @Schema(
      description =
          "The identifier representing who the interaction is from, e.g. an email address, a mobile number, etc",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Sender", required = true)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "sender", length = 255, nullable = false)
  private String sender;

  /** The ID for the interaction source the interaction is associated with. */
  @Schema(
      description = "The ID for the interaction source the interaction is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SourceId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "source_id", length = 50, nullable = false)
  private UUID sourceId;

  /** The status of the interaction. */
  @Schema(
      description = "The status of the interaction",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private InteractionStatus status;

  /** The subject for the interaction. */
  @Schema(
      description = "The subject for the interaction",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Subject", required = true)
  @Size(max = 2000)
  @Column(name = "subject", length = 2000, nullable = false)
  private String subject;

  /** The ID for the tenant the interaction is associated with. */
  @Schema(
      description = "The ID for the tenant the interaction is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The interaction type. */
  @Schema(description = "The interaction type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", nullable = false)
  private InteractionType type;

  /** Constructs a new {@code InteractionSummary}. */
  public InteractionSummary() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument otherwise {@code false}
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

    InteractionSummary other = (InteractionSummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the interaction was assigned.
   *
   * @return the date and time the interaction was assigned
   */
  public OffsetDateTime getAssigned() {
    return assigned;
  }

  /**
   * Returns the username for the user the interaction is assigned to.
   *
   * @return the username for the user the interaction is assigned to
   */
  public String getAssignedTo() {
    return assignedTo;
  }

  /**
   * Returns the ID for the conversation the interaction is associated with.
   *
   * @return the ID for the conversation the interaction is associated with
   */
  public String getConversationId() {
    return conversationId;
  }

  /**
   * Returns the direction for the interaction, i.e., inbound or outbound.
   *
   * @return the direction for the interaction, i.e., inbound or outbound
   */
  public InteractionDirection getDirection() {
    return direction;
  }

  /**
   * Returns the ID for the interaction.
   *
   * @return the ID for the interaction
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the mime type for the content for the interaction.
   *
   * @return the mime type for the content for the interaction
   */
  public InteractionMimeType getMimeType() {
    return mimeType;
  }

  /**
   * Returns the date and time the interaction occurred (received if inbound, sent if outbound).
   *
   * @return the date and time the interaction occurred (received if inbound, sent if outbound)
   */
  public OffsetDateTime getOccurred() {
    return occurred;
  }

  /**
   * Returns the ID for the party the interaction is associated with.
   *
   * @return the ID for the party the interaction is associated with
   */
  public UUID getPartyId() {
    return partyId;
  }

  /**
   * Returns the identifiers representing the recipients for the interaction, e.g. email addresses,
   * mobile numbers, etc.
   *
   * @return the identifiers representing the recipients for the interaction, e.g. email addresses,
   *     mobile numbers, etc
   */
  public List<String> getRecipients() {
    return recipients;
  }

  /**
   * Returns the identifier representing who the interaction is from, e.g. an email address, a
   * mobile number, etc.
   *
   * @return the identifier representing who the interaction is from, e.g. an email address, a
   *     mobile number, etc
   */
  public String getSender() {
    return sender;
  }

  /**
   * Returns the ID for the interaction source the interaction is associated with.
   *
   * @return the ID for the interaction source the interaction is associated with
   */
  public UUID getSourceId() {
    return sourceId;
  }

  /**
   * Returns the status of the interaction.
   *
   * @return the status of the interaction
   */
  public InteractionStatus getStatus() {
    return status;
  }

  /**
   * Returns the subject for the interaction.
   *
   * @return the subject for the interaction
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Returns the ID for the tenant the interaction is associated with.
   *
   * @return the ID for the tenant the interaction is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the interaction type.
   *
   * @return the interaction type
   */
  public InteractionType getType() {
    return type;
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
   * Set the direction for the interaction, i.e., inbound or outbound.
   *
   * @param direction the direction for the interaction, i.e., inbound or outbound
   */
  public void setDirection(InteractionDirection direction) {
    this.direction = direction;
  }
}
