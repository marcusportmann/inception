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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
import java.util.Objects;
import java.util.UUID;
import org.springframework.util.StringUtils;

/** The {@code InteractionSummary} class holds the information for an interaction summary. */
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
  "from",
  "to",
  "subject",
  "mimeType",
  "timestamp",
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
      "from",
      "to",
      "subject",
      "mimeType",
      "timestamp",
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

  /**
   * The identifier representing who the interaction is from, e.g. an email address, a mobile
   * number, etc.
   */
  @Schema(
      description =
          "The identifier representing who the interaction is from, e.g. an email address, a mobile number, etc",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "From", required = true)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "from", length = 255, nullable = false)
  private String from;

  /** The ID for the interaction. */
  @Schema(
      description = "The unique identifier for the interaction",
      requiredMode = Schema.RequiredMode.REQUIRED)
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

  /** The unique identifier for the party the interaction is associated with. */
  @Schema(description = "The unique identifier for the party the interaction is associated with")
  @JsonProperty
  @XmlElement(name = "PartyId")
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /** The unique identifier for the interaction source the interaction is associated with. */
  @Schema(
      description =
          "The unique identifier for the interaction source the interaction is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SourceId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "source_id", length = 50, nullable = false)
  private String sourceId;

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

  /** The unique identifier for the tenant the interaction is associated with. */
  @Schema(
      description = "The unique identifier for the tenant the interaction is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The date and time the interaction was received or sent. */
  @Schema(
      description = "The date and time the interaction was received or sent",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Timestamp", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "timestamp", nullable = false)
  private OffsetDateTime timestamp;

  /**
   * The colon-seperated list of identifiers representing the recipients for the interaction, e.g.
   * email addresses, mobile numbers, etc.
   */
  @JsonIgnore
  @Column(name = "to", length = 2000)
  private String to;

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
   * Returns the identifier representing who the interaction is from, e.g. an email address, a
   * mobile number, etc.
   *
   * @return the identifier representing who the interaction is from, e.g. an email address, a
   *     mobile number, etc
   */
  public String getFrom() {
    return from;
  }

  /**
   * Returns the unique identifier for the interaction.
   *
   * @return the unique identifier for the interaction
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
   * Returns the unique identifier for the party the interaction is associated with.
   *
   * @return the unique identifier for the party the interaction is associated with
   */
  public UUID getPartyId() {
    return partyId;
  }

  /**
   * Returns the unique identifier for the interaction source the interaction is associated with.
   *
   * @return the unique identifier for the interaction source the interaction is associated with
   */
  public String getSourceId() {
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
   * Returns the unique identifier for the tenant the interaction is associated with.
   *
   * @return the unique identifier for the tenant the interaction is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the interaction was received or sent.
   *
   * @return the date and time the interaction was received or sent
   */
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * Returns the identifiers representing the recipients for the interaction, e.g. email addresses,
   * mobile numbers, etc.
   *
   * @return the identifiers representing the recipients for the interaction, e.g. email addresses,
   *     mobile numbers, etc
   */
  @Schema(
      description =
          "The identifiers representing the recipients for the interaction, e.g. email addresses, mobile numbers, etc",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "To", required = true)
  @XmlElement(name = "To", required = true)
  public String[] getTo() {
    return StringUtils.removeDuplicateStrings(StringUtils.delimitedListToStringArray(to, ";"));
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
   * Set the date and time the interaction was assigned.
   *
   * @param assigned the date and time the interaction was assigned
   */
  public void setAssigned(OffsetDateTime assigned) {
    this.assigned = assigned;
  }

  /**
   * Set the username for the user the interaction is assigned to.
   *
   * @param assignedTo the username for the user the interaction is assigned to
   */
  public void setAssignedTo(String assignedTo) {
    this.assignedTo = assignedTo;
  }

  /**
   * Set the ID for the conversation the interaction is associated with.
   *
   * @param conversationId the ID for the conversation the interaction is associated with
   */
  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  /**
   * Set the identifier representing who the interaction is from, e.g. an email address, a mobile
   * number, etc.
   *
   * @param from the identifier representing who the interaction is from, e.g. an email address, a
   *     mobile number, etc
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * Set the unique identifier for the interaction.
   *
   * @param id the unique identifier for the interaction
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the mime type for the content for the interaction.
   *
   * @param mimeType the mime type for the content for the interaction
   */
  public void setMimeType(InteractionMimeType mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Set the unique identifier for the party the interaction is associated with.
   *
   * @param partyId the unique identifier for the party the interaction is associated with
   */
  public void setPartyId(UUID partyId) {
    this.partyId = partyId;
  }

  /**
   * Set the ID for the interaction source the interaction is associated with.
   *
   * @param sourceId the ID for the interaction source the interaction is associated with
   */
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * Set the status of the interaction.
   *
   * @param status the status of the interaction
   */
  public void setStatus(InteractionStatus status) {
    this.status = status;
  }

  /**
   * Set the subject for the interaction.
   *
   * @param subject the subject for the interaction
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Set the date and time the interaction was received or sent.
   *
   * @param timestamp the date and time the interaction was received or sent
   */
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Set the identifiers representing the recipients for the interaction, e.g. email addresses,
   * mobile numbers, etc.
   *
   * @param to the identifiers representing the recipients for the interaction, e.g. email
   *     addresses, mobile numbers, etc
   */
  public void setTo(String[] to) {
    this.to = StringUtils.arrayToDelimitedString(to, ";");
  }

  /**
   * Set the interaction type.
   *
   * @param type the interaction type
   */
  public void setType(InteractionType type) {
    this.type = type;
  }
}
