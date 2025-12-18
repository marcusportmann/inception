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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * The {@code WorkflowDocumentSummary} class holds the information for a workflow document summary.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow document summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "workflowId",
  "documentDefinitionId",
  "documentDefinitionName",
  "documentDefinitionShortName",
  "documentDefinitionDescription",
  "requested",
  "requestedFromPartyId",
  "requestedBy",
  "provided",
  "providedBy",
  "rejected",
  "rejectedBy",
  "rejectionReason",
  "verified",
  "verifiedBy",
  "waived",
  "waivedBy",
  "waiveReason",
  "internal",
  "status",
  "documentId",
  "description",
})
@XmlRootElement(
    name = "WorkflowDocumentSummary",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDocumentSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "workflowId",
      "documentDefinitionId",
      "documentDefinitionName",
      "documentDefinitionShortName",
      "documentDefinitionDescription",
      "requested",
      "requestedFromPartyId",
      "requestedBy",
      "provided",
      "providedBy",
      "rejected",
      "rejectedBy",
      "rejectionReason",
      "verified",
      "verifiedBy",
      "waived",
      "waivedBy",
      "waiveReason",
      "internal",
      "status",
      "documentId",
      "description"
    })
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowDocumentSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The description for the workflow document. */
  @Schema(description = "The description for the workflow document")
  @JsonProperty
  @XmlElement(name = "Description")
  @Size(max = 500)
  private String description;

  /** The description for the document definition the workflow document is associated with. */
  @Schema(
      description =
          "The description for the document definition the workflow document is associated with")
  @JsonProperty
  @XmlElement(name = "DocumentDefinitionDescription")
  @Size(min = 1, max = 500)
  private String documentDefinitionDescription;

  /** The ID for the document definition the workflow document is associated with. */
  @Schema(
      description = "The ID for the document definition the workflow document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentDefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String documentDefinitionId;

  /** The name for the document definition the workflow document is associated with. */
  @Schema(
      description = "The name for the document definition the workflow document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentDefinitionName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String documentDefinitionName;

  /** The short name for the document definition the workflow document is associated with. */
  @Schema(
      description =
          "The short name for the document definition the workflow document is associated with")
  @JsonProperty
  @XmlElement(name = "DocumentDefinitionShortName")
  @Size(min = 1, max = 50)
  private String documentDefinitionShortName;

  /** The ID for the document. */
  @Schema(description = "The ID for the document")
  @JsonProperty
  @XmlElement(name = "DocumentId")
  private UUID documentId;

  /** The ID for the workflow document. */
  @Schema(
      description = "The ID for the workflow document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /** Is the workflow document internal-only and excluded for external users. */
  @Schema(
      description = "Is the workflow document internal-only and excluded for external users",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Internal", required = true)
  @NotNull
  private boolean internal;

  /** The date and time the workflow document was provided. */
  @Schema(description = "The date and time the workflow document was provided")
  @JsonProperty
  @XmlElement(name = "Provided")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime provided;

  /** The person or system that provided the workflow document. */
  @Schema(description = "The person or system that provided the workflow document")
  @JsonProperty
  @XmlElement(name = "ProvidedBy")
  @Size(min = 1, max = 100)
  private String providedBy;

  /** The date and time the workflow document was rejected. */
  @Schema(description = "The date and time the workflow document was rejected")
  @JsonProperty
  @XmlElement(name = "Rejected")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime rejected;

  /** The person or system that rejected the workflow document. */
  @Schema(description = "The person or system that rejected the workflow document")
  @JsonProperty
  @XmlElement(name = "RejectedBy")
  @Size(min = 1, max = 100)
  private String rejectedBy;

  /** The reason the workflow document was rejected. */
  @Schema(description = "The reason the workflow document was rejected")
  @JsonProperty
  @XmlElement(name = "RejectionReason")
  @Size(min = 1, max = 500)
  private String rejectionReason;

  /** The date and time the workflow document was requested. */
  @Schema(
      description = "The date and time the workflow document was requested",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Requested", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  private OffsetDateTime requested;

  /** The person or system that requested the workflow document. */
  @Schema(
      description = "The person or system that requested the workflow document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "RequestedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String requestedBy;

  /** The ID for the party the workflow document was requested from. */
  @Schema(description = "The ID for the party the workflow document was requested from")
  @JsonProperty
  @XmlElement(name = "RequestedFromPartyId")
  private UUID requestedFromPartyId;

  /** The status of the workflow document. */
  @Schema(
      description = "The status of the workflow document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  private WorkflowDocumentStatus status;

  /** The ID for the tenant the workflow document is associated with. */
  @Schema(
      description = "The ID for the tenant the workflow document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  private UUID tenantId;

  /** The date and time the workflow document was verified. */
  @Schema(description = "The date and time the workflow document was verified")
  @JsonProperty
  @XmlElement(name = "Verified")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime verified;

  /** The person or system that verified the workflow document. */
  @Schema(description = "The person or system that verified the workflow document")
  @JsonProperty
  @XmlElement(name = "VerifiedBy")
  @Size(min = 1, max = 100)
  private String verifiedBy;

  /** The reason the workflow document was waived. */
  @Schema(description = "The reason the workflow document was waived")
  @JsonProperty
  @XmlElement(name = "WaiveReason")
  @Size(min = 1, max = 500)
  private String waiveReason;

  /** The date and time the workflow document was waived. */
  @Schema(description = "The date and time the workflow document was waived")
  @JsonProperty
  @XmlElement(name = "Waived")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private OffsetDateTime waived;

  /** The person or system that waived the workflow document. */
  @Schema(description = "The person or system that waived the workflow document")
  @JsonProperty
  @XmlElement(name = "WaivedBy")
  @Size(min = 1, max = 100)
  private String waivedBy;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  private UUID workflowId;

  /** Constructs a new {@code WorkflowDocumentSummary}. */
  public WorkflowDocumentSummary() {}

  /**
   * Constructs a new {@code WorkflowDocumentSummary}.
   *
   * @param id the ID for the workflow document
   * @param tenantId the ID for the tenant the workflow document is associated with
   * @param workflowId the ID for the workflow
   * @param documentDefinitionId the ID for the document definition the workflow document is
   *     associated with
   * @param documentDefinitionName the name for the document definition the workflow document is
   *     associated with
   * @param documentDefinitionShortName the short name for the document definition the workflow
   *     document is associated with
   * @param documentDefinitionDescription the description for the document definition the workflow
   *     document is associated with
   * @param requested the date and time the workflow document was requested
   * @param requestedFromPartyId the ID for the party the workflow document was requested from
   * @param requestedBy the person or system that requested the workflow document
   * @param provided the date and time the workflow document was provided
   * @param providedBy the person or system that provided the workflow document
   * @param rejected the date and time the workflow document was rejected
   * @param rejectedBy the person or system that rejected the workflow document
   * @param rejectionReason the reason the workflow document was rejected
   * @param verified the date and time the workflow document was verified
   * @param verifiedBy the person or system that verified the workflow document
   * @param waived the date and time the workflow document was waived
   * @param waivedBy the person or system that waived the workflow document
   * @param waiveReason the reason the workflow document was waived
   * @param internal is the workflow document internal-only and excluded for external users
   * @param status the status of the workflow document
   * @param documentId the ID for the document
   * @param description the description for the workflow document
   */
  public WorkflowDocumentSummary(
      UUID id,
      UUID tenantId,
      UUID workflowId,
      String documentDefinitionId,
      String documentDefinitionName,
      String documentDefinitionShortName,
      String documentDefinitionDescription,
      OffsetDateTime requested,
      UUID requestedFromPartyId,
      String requestedBy,
      OffsetDateTime provided,
      String providedBy,
      OffsetDateTime rejected,
      String rejectedBy,
      String rejectionReason,
      OffsetDateTime verified,
      String verifiedBy,
      OffsetDateTime waived,
      String waivedBy,
      String waiveReason,
      boolean internal,
      WorkflowDocumentStatus status,
      UUID documentId,
      String description) {
    this.id = id;
    this.tenantId = tenantId;
    this.workflowId = workflowId;
    this.documentDefinitionId = documentDefinitionId;
    this.documentDefinitionName = documentDefinitionName;
    this.documentDefinitionShortName = documentDefinitionShortName;
    this.documentDefinitionDescription = documentDefinitionDescription;
    this.requested = requested;
    this.requestedFromPartyId = requestedFromPartyId;
    this.requestedBy = requestedBy;
    this.provided = provided;
    this.providedBy = providedBy;
    this.rejected = rejected;
    this.rejectedBy = rejectedBy;
    this.rejectionReason = rejectionReason;

    this.verified = verified;
    this.verifiedBy = verifiedBy;

    this.waived = waived;
    this.waivedBy = waivedBy;
    this.waiveReason = waiveReason;

    this.internal = internal;
    this.status = status;
    this.documentId = documentId;
    this.description = description;
  }

  /**
   * Returns the description for the workflow document.
   *
   * @return the description for the workflow document
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the description for the document definition the workflow document is associated with.
   *
   * @return the description for the document definition the workflow document is associated with
   */
  public String getDocumentDefinitionDescription() {
    return documentDefinitionDescription;
  }

  /**
   * Returns the ID for the document definition the workflow document is associated with.
   *
   * @return the ID for the document definition the workflow document is associated with
   */
  public String getDocumentDefinitionId() {
    return documentDefinitionId;
  }

  /**
   * Returns the name for the document definition the workflow document is associated with.
   *
   * @return the name for the document definition the workflow document is associated with
   */
  public String getDocumentDefinitionName() {
    return documentDefinitionName;
  }

  /**
   * Returns the short name for the document definition the workflow document is associated with.
   *
   * @return the short name for the document definition the workflow document is associated with
   */
  public String getDocumentDefinitionShortName() {
    return documentDefinitionShortName;
  }

  /**
   * Returns the ID for the document.
   *
   * @return the ID for the document
   */
  public UUID getDocumentId() {
    return documentId;
  }

  /**
   * Returns the ID for the workflow document.
   *
   * @return the ID for the workflow document
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the date and time the workflow document was provided.
   *
   * @return the date and time the workflow document was provided
   */
  public OffsetDateTime getProvided() {
    return provided;
  }

  /**
   * Returns the person or system that provided the workflow document.
   *
   * @return the person or system that provided the workflow document
   */
  public String getProvidedBy() {
    return providedBy;
  }

  /**
   * Returns the date and time the workflow document was rejected.
   *
   * @return the date and time the workflow document was rejected
   */
  public OffsetDateTime getRejected() {
    return rejected;
  }

  /**
   * Returns the person or system that rejected the workflow document.
   *
   * @return the person or system that rejected the workflow document
   */
  public String getRejectedBy() {
    return rejectedBy;
  }

  /**
   * Returns the reason the workflow document was rejected.
   *
   * @return the reason the workflow document was rejected
   */
  public String getRejectionReason() {
    return rejectionReason;
  }

  /**
   * Returns the date and time the workflow document was requested.
   *
   * @return the date and time the workflow document was requested
   */
  public OffsetDateTime getRequested() {
    return requested;
  }

  /**
   * Returns the person or system that requested the workflow document.
   *
   * @return the person or system that requested the workflow document
   */
  public String getRequestedBy() {
    return requestedBy;
  }

  /**
   * Returns the ID for the party the workflow document was requested from.
   *
   * @return the ID for the party the workflow document was requested from
   */
  public UUID getRequestedFromPartyId() {
    return requestedFromPartyId;
  }

  /**
   * Returns the status of the workflow document.
   *
   * @return the status of the workflow document
   */
  public WorkflowDocumentStatus getStatus() {
    return status;
  }

  /**
   * Returns the ID for the tenant the workflow document is associated with.
   *
   * @return the ID for the tenant the workflow document is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date and time the workflow document was verified.
   *
   * @return the date and time the workflow document was verified
   */
  public OffsetDateTime getVerified() {
    return verified;
  }

  /**
   * Returns the person or system that verified the workflow document.
   *
   * @return the person or system that verified the workflow document
   */
  public String getVerifiedBy() {
    return verifiedBy;
  }

  /**
   * Returns the reason the workflow document was waived.
   *
   * @return the reason the workflow document was waived
   */
  public String getWaiveReason() {
    return waiveReason;
  }

  /**
   * Returns the date and time the workflow document was waived.
   *
   * @return the date and time the workflow document was waived
   */
  public OffsetDateTime getWaived() {
    return waived;
  }

  /**
   * Returns the person or system that waived the workflow document.
   *
   * @return the person or system that waived the workflow document
   */
  public String getWaivedBy() {
    return waivedBy;
  }

  /**
   * Returns the ID for the workflow.
   *
   * @return the ID for the workflow
   */
  public UUID getWorkflowId() {
    return workflowId;
  }

  /**
   * Returns whether the workflow document is internal-only and excluded for external users.
   *
   * @return {@code true} if the workflow document is internal-only and excluded for external users
   *     or {@code false} otherwise
   */
  public boolean isInternal() {
    return internal;
  }
}
