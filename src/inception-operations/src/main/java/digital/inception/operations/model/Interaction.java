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
import digital.inception.operations.persistence.jpa.RecipientsAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.mail.Address;
import jakarta.mail.internet.InternetAddress;
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
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

/**
 * The {@code Interaction} class holds the information for an interaction.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An interaction")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "status",
  "sourceId",
  "sourceReference",
  "conversationId",
  "partyId",
  "type",
  "direction",
  "sender",
  "recipients",
  "subject",
  "mimeType",
  "content",
  "occurred",
  "assigned",
  "assignedTo",
  "processed",
  "processingAttempts",
  "processingTime",
  "locked",
  "lockName",
  "lastProcessed"
})
@XmlRootElement(name = "Interaction", namespace = "https://inception.digital/operations")
@XmlType(
    name = "Interaction",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "status",
      "sourceId",
      "sourceReference",
      "conversationId",
      "partyId",
      "type",
      "direction",
      "sender",
      "recipients",
      "subject",
      "mimeType",
      "content",
      "occurred",
      "assigned",
      "assignedTo",
      "processed",
      "processingAttempts",
      "processingTime",
      "locked",
      "lockName",
      "lastProcessed"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_interactions")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Interaction implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date and time the interaction was assigned. */
  @Schema(description = "The date and time the interaction was assigned")
  @JsonProperty
  @XmlElement(name = "Assigned")
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

  /** The content for the interaction. */
  @Schema(
      description = "The content for the interaction",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Content", required = true)
  @NotNull
  @Size(min = 1, max = 10485760)
  @Column(name = "content", length = 10485760, nullable = false)
  private String content;

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

  /** The date and time the last attempt was made to process the interaction. */
  @Schema(description = "The date and time the last attempt was made to process the interaction")
  @JsonProperty
  @XmlElement(name = "LastProcessed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "last_processed")
  private OffsetDateTime lastProcessed;

  /** The name of the entity that has locked the interaction for processing. */
  @Schema(description = "The name of the entity that has locked the interaction for processing")
  @XmlElement(name = "LockName")
  @Size(min = 1, max = 100)
  @Column(name = "lock_name", length = 100)
  private String lockName;

  /** The date and time the interaction was locked for processing. */
  @Schema(description = "The date and time the interaction was locked for processing")
  @JsonProperty
  @XmlElement(name = "Locked")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "locked")
  private OffsetDateTime locked;

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
  @Column(name = "party_id")
  private UUID partyId;

  /** The date and time the interaction was processed. */
  @Schema(description = "The date and time the interaction was processed")
  @JsonProperty
  @XmlElement(name = "Processed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "processed")
  private OffsetDateTime processed;

  /** The number of times the processing of the interaction has been attempted. */
  @Schema(description = "The number of times the processing of the interaction has been attempted")
  @JsonProperty
  @XmlElement(name = "ProcessingAttempts")
  @Column(name = "processing_attempts", nullable = false)
  private Integer processingAttempts = 0;

  /** The time taken to process the interaction in milliseconds. */
  @Schema(
      description = "The time taken to process the interaction in milliseconds",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ProcessingTime", required = true)
  @NotNull
  @Column(name = "processing_time", nullable = false)
  private long processingTime;

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
  @Column(name = "source_id", nullable = false)
  private UUID sourceId;

  /** The interaction source specific reference for the interaction. */
  @Schema(description = "The interaction source specific reference for the interaction")
  @JsonProperty
  @XmlElement(name = "SourceReference")
  @Size(min = 1, max = 400)
  @Column(name = "source_reference", length = 400)
  private String sourceReference;

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
  @Schema(description = "The subject for the interaction")
  @JsonProperty
  @XmlElement(name = "Subject")
  @Size(max = 400)
  @Column(name = "subject", length = 400)
  private String subject;

  /** The ID for the tenant the interaction is associated with. */
  @Schema(description = "The ID for the tenant the interaction is associated with")
  @JsonProperty
  @XmlElement(name = "TenantId")
  @Column(name = "tenant_id")
  private UUID tenantId;

  /** The type of interaction. */
  @Schema(description = "The type of interaction", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", nullable = false)
  private InteractionType type;

  /** Constructs a new {@code Interaction}. */
  public Interaction() {}

  /**
   * Constructs a new {@code Interaction}.
   *
   * @param id the ID for the interaction
   * @param tenantId the ID for the tenant the interaction is associated with
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param type the type of interaction
   * @param direction the direction for the interaction, i.e., inbound or outbound
   * @param sender the identifier representing who the interaction is from, e.g. an email address, a
   *     mobile number, etc
   * @param recipients the identifiers representing the recipients for the interaction, e.g. email
   *     addresses, mobile numbers, etc
   * @param subject the subject for the interaction
   * @param content the content for the interaction
   * @param mimeType the mime type for the content for the interaction
   * @param status the status of the interaction
   */
  public Interaction(
      UUID id,
      UUID tenantId,
      UUID sourceId,
      InteractionType type,
      InteractionDirection direction,
      String sender,
      List<String> recipients,
      String subject,
      String content,
      InteractionMimeType mimeType,
      InteractionStatus status) {
    this.id = id;
    this.tenantId = tenantId;
    this.sourceId = sourceId;
    this.type = type;
    this.direction = direction;
    this.sender = sender;
    this.recipients = recipients;
    this.subject = subject;
    this.content = content;
    this.mimeType = mimeType;
    this.status = status;
    this.occurred = OffsetDateTime.now();
  }

  /**
   * Constructs a new {@code Interaction}.
   *
   * @param id the ID for the interaction
   * @param tenantId the ID for the tenant the interaction is associated with
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source specific reference for the interaction
   * @param type the type of interaction
   * @param direction the direction for the interaction, i.e., inbound or outbound
   * @param sender the identifier representing who the interaction is from, e.g. an email address, a
   *     mobile number, etc
   * @param recipients the identifiers representing the recipients for the interaction, e.g. email
   *     addresses, mobile numbers, etc
   * @param subject the subject for the interaction
   * @param content the content for the interaction
   * @param mimeType the mime type for the content for the interaction
   * @param status the status of the interaction
   */
  public Interaction(
      UUID id,
      UUID tenantId,
      UUID sourceId,
      String sourceReference,
      InteractionType type,
      InteractionDirection direction,
      String sender,
      List<String> recipients,
      String subject,
      String content,
      InteractionMimeType mimeType,
      InteractionStatus status) {
    this.id = id;
    this.tenantId = tenantId;
    this.sourceId = sourceId;
    this.sourceReference = sourceReference;
    this.type = type;
    this.direction = direction;
    this.sender = sender;
    this.recipients = recipients;
    this.subject = subject;
    this.content = content;
    this.mimeType = mimeType;
    this.status = status;
    this.occurred = OffsetDateTime.now();
  }

  /**
   * Returns whether this object is equal to the specified object.
   *
   * @param object the object to compare this object to
   * @return <code>true</code> if this object is equal to the specified object or <code>false</code>
   *     otherwise
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

    Interaction other = (Interaction) object;

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
   * Returns the content for the interaction.
   *
   * @return the content for the interaction
   */
  public String getContent() {
    return content;
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
   * Returns the date and time the last attempt was made to process the interaction.
   *
   * @return the date and time the last attempt was made to process the interaction
   */
  public OffsetDateTime getLastProcessed() {
    return lastProcessed;
  }

  /**
   * Returns the name of the entity that has locked the interaction for processing.
   *
   * @return the name of the entity that has locked the interaction for processing
   */
  public String getLockName() {
    return lockName;
  }

  /**
   * Returns the date and time the interaction was locked for processing.
   *
   * @return the date and time the interaction was locked for processing
   */
  public OffsetDateTime getLocked() {
    return locked;
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
   * Returns the date and time the interaction was processed.
   *
   * @return the date and time the interaction was processed
   */
  public OffsetDateTime getProcessed() {
    return processed;
  }

  /**
   * Returns the number of times the processing of the interaction has been attempted.
   *
   * @return the number of times the processing of the interaction has been attempted
   */
  public Integer getProcessingAttempts() {
    return processingAttempts;
  }

  /**
   * Returns the time taken to process the interaction in milliseconds.
   *
   * @return the time taken to process the interaction in milliseconds
   */
  public long getProcessingTime() {
    return processingTime;
  }

  /**
   * Returns the address objects for the identifiers representing the recipients for the
   * interaction, e.g. email addresses, mobile numbers, etc.
   *
   * @return the address objects for the identifiers representing the recipients for the
   *     interaction, e.g. email addresses, mobile numbers, etc
   */
  @JsonIgnore
  @XmlTransient
  public List<Address> getRecipientAddresses() {
    if (recipients == null) {
      return List.of();
    }

    try {
      return recipients.stream()
          .map(String::trim)
          .filter(StringUtils::hasText)
          .map(
              emailAddress -> {
                try {
                  return (Address) new InternetAddress(emailAddress);
                } catch (Throwable e) {
                  throw new RuntimeException(
                      "Failed to create the internet address (" + emailAddress + ")", e);
                }
              })
          .toList();
    } catch (Throwable e) {
      throw new RuntimeException("Failed to retrieve the recipient addresses", e);
    }
  }

  /**
   * Returns the comma-separated names for the identifiers representing the recipients for the
   * interaction, e.g. email addresses, mobile numbers, etc.
   *
   * @return the comma-separated names for the identifiers representing the recipients for the
   *     interaction, e.g. email addresses, mobile numbers, etc
   */
  @JsonIgnore
  @XmlTransient
  public String getRecipientNames() {
    try {
      return getRecipientAddresses().stream()
          .map(
              address -> {
                if (address instanceof InternetAddress internetAddress) {
                  String personal = internetAddress.getPersonal();
                  return StringUtils.hasText(personal) ? personal : internetAddress.getAddress();
                }
                return address.toString();
              })
          .filter(StringUtils::hasText)
          .collect(Collectors.joining(", "));
    } catch (Throwable e) {
      throw new RuntimeException("Failed to retrieve the recipient names", e);
    }
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
   * Returns the identifier representing who the interaction is from.
   *
   * @return the identifier representing who the interaction is from
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
   * Returns the interaction source specific reference for the interaction.
   *
   * @return the interaction source specific reference for the interaction
   */
  public String getSourceReference() {
    return sourceReference;
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
   * Returns the type of interaction.
   *
   * @return the type of interaction
   */
  public InteractionType getType() {
    return type;
  }

  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /** Increment the number of times that the processing of the interaction was attempted. */
  public void incrementProcessingAttempts() {
    if (processingAttempts == null) {
      processingAttempts = 1;
    } else {
      processingAttempts++;
    }
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
   * Set the content for the interaction.
   *
   * @param content the content for the interaction
   */
  public void setContent(String content) {
    this.content = content;
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
   * Set the direction for the interaction, i.e., inbound or outbound.
   *
   * @param direction the direction for the interaction, i.e., inbound or outbound
   */
  public void setDirection(InteractionDirection direction) {
    this.direction = direction;
  }

  /**
   * Set the ID for the interaction.
   *
   * @param id the ID for the interaction
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the date and time the last attempt was made to process the interaction.
   *
   * @param lastProcessed the date and time the last attempt was made to process the interaction
   */
  public void setLastProcessed(OffsetDateTime lastProcessed) {
    this.lastProcessed = lastProcessed;
  }

  /**
   * Set the name of the entity that has locked the interaction for processing.
   *
   * @param lockName the name of the entity that has locked the interaction for processing
   */
  public void setLockName(String lockName) {
    this.lockName = lockName;
  }

  /**
   * Set the date and time the interaction was locked for processing.
   *
   * @param locked the date and time the interaction was locked for processing
   */
  public void setLocked(OffsetDateTime locked) {
    this.locked = locked;
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
   * Set the date and time the interaction occurred (received if inbound, sent if outbound).
   *
   * @param occurred the date and time the interaction occurred (received if inbound, sent if
   *     outbound)
   */
  public void setOccurred(OffsetDateTime occurred) {
    this.occurred = occurred;
  }

  /**
   * Set the ID for the party the interaction is associated with.
   *
   * @param partyId the ID for the party the interaction is associated with
   */
  public void setPartyId(UUID partyId) {
    this.partyId = partyId;
  }

  /**
   * Set the date and time the interaction was processed.
   *
   * @param processed the date and time the interaction was processed
   */
  public void setProcessed(OffsetDateTime processed) {
    this.processed = processed;
  }

  /**
   * Set the number of times the processing of the interaction has been attempted.
   *
   * @param processingAttempts the number of times the processing of the interaction has been
   *     attempted
   */
  public void setProcessingAttempts(Integer processingAttempts) {
    this.processingAttempts = processingAttempts;
  }

  /**
   * Set the time taken to process the interaction in milliseconds.
   *
   * @param processingTime the time taken to process the interaction in milliseconds
   */
  public void setProcessingTime(@NotNull long processingTime) {
    this.processingTime = processingTime;
  }

  /**
   * Set the identifiers representing the recipients for the interaction, e.g. email addresses,
   * mobile numbers, etc.
   *
   * @param recipients the identifiers representing the recipients for the interaction, e.g. email
   *     addresses, mobile numbers, etc
   */
  public void setRecipients(List<String> recipients) {
    this.recipients = recipients;
  }

  /**
   * Set the identifier representing who the interaction is from.
   *
   * @param from the identifier representing who the interaction is from
   */
  public void setSender(String from) {
    this.sender = from;
  }

  /**
   * Set the ID for the interaction source the interaction is associated with.
   *
   * @param sourceId the ID for the interaction source the interaction is associated with
   */
  public void setSourceId(UUID sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * Set the interaction source specific reference for the interaction.
   *
   * @param sourceReference the interaction source specific reference for the interaction
   */
  public void setSourceReference(String sourceReference) {
    this.sourceReference = sourceReference;
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
   * Set the ID for the tenant the interaction is associated with.
   *
   * @param tenantId the ID for the tenant the interaction is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the type of interaction.
   *
   * @param type the type of interaction
   */
  public void setType(InteractionType type) {
    this.type = type;
  }
}
