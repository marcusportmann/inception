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
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code WorkflowDocument} class holds the information for a workflow document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A workflow document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "workflowId",
  "documentDefinitionId",
  "documentId",
  "status",
  "requested",
  "requestedBy",
  "provided",
  "providedBy",
  "rejected",
  "rejectedBy",
  "rejectionReason",
  "verified",
  "verifiedBy"
})
@XmlRootElement(name = "WorkflowDocument", namespace = "https://inception.digital/operations")
@XmlType(
    name = "WorkflowDocument",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "tenantId",
      "workflowId",
      "documentDefinitionId",
      "documentId",
      "status",
      "requested",
      "requestedBy",
      "provided",
      "providedBy",
      "rejected",
      "rejectedBy",
      "rejectionReason",
      "verified",
      "verifiedBy"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_workflow_documents")
public class WorkflowDocument implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The document definition the workflow document is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "document_definition_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  private DocumentDefinition documentDefinition;

  /** The ID for the document definition the workflow document is associated with. */
  @Schema(
      description = "The ID for the document definition the workflow document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentDefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "document_definition_id", length = 50, nullable = false)
  private String documentDefinitionId;

  /** The ID for the document. */
  @Schema(description = "The ID for the document")
  @JsonProperty
  @XmlElement(name = "DocumentId")
  @Column(name = "document_id")
  private UUID documentId;

  /** The ID for the workflow document. */
  @Schema(
      description = "The ID for the workflow document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The date and time the workflow document was provided. */
  @Schema(description = "The date and time the workflow document was provided")
  @JsonProperty
  @XmlElement(name = "Provided")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "provided")
  private OffsetDateTime provided;

  /** The person or system that provided the workflow document. */
  @Schema(description = "The person or system that provided the workflow document")
  @JsonProperty
  @XmlElement(name = "ProvidedBy")
  @Size(min = 1, max = 100)
  @Column(name = "provided_by", length = 100)
  private String providedBy;

  /** The date and time the workflow document was rejected. */
  @Schema(description = "The date and time the workflow document was rejected")
  @JsonProperty
  @XmlElement(name = "Rejected")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "rejected")
  private OffsetDateTime rejected;

  /** The person or system that rejected the workflow document. */
  @Schema(description = "The person or system that rejected the workflow document")
  @JsonProperty
  @XmlElement(name = "RejectedBy")
  @Size(min = 1, max = 100)
  @Column(name = "rejected_by", length = 100)
  private String rejectedBy;

  /** The reason the workflow document was rejected. */
  @Schema(description = "The reason the workflow document was rejected")
  @JsonProperty
  @XmlElement(name = "RejectionReason")
  @Size(min = 1, max = 100)
  @Column(name = "rejection_reason", length = 100)
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
  @Column(name = "requested", nullable = false)
  private OffsetDateTime requested;

  /** The person or system that requested the workflow document. */
  @Schema(
      description = "The person or system that requested the workflow document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "RequestedBy", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "requested_by", length = 100, nullable = false)
  private String requestedBy;

  /** The status of the workflow document. */
  @Schema(
      description = "The status of the workflow document",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private WorkflowDocumentStatus status;

  /** The ID for the tenant the workflow document is associated with. */
  @Schema(
      description = "The ID for the tenant the workflow document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The date and time the workflow document was verified. */
  @Schema(description = "The date and time the workflow document was verified")
  @JsonProperty
  @XmlElement(name = "Verified")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "verified")
  private OffsetDateTime verified;

  /** The person or system that verified the workflow document. */
  @Schema(description = "The person or system that verified the workflow document")
  @JsonProperty
  @XmlElement(name = "VerifiedBy")
  @Size(min = 1, max = 100)
  @Column(name = "verified_by", length = 100)
  private String verifiedBy;

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  @Column(name = "workflow_id", nullable = false)
  private UUID workflowId;

  /** Constructs a new {@code WorkflowDocument}. */
  public WorkflowDocument() {}

  /**
   * Constructs a new {@code WorkflowDocument}.
   *
   * @param tenantId the ID for the tenant the workflow document is associated with
   * @param workflowId the ID for the workflow
   * @param documentDefinitionId the ID for the document definition the workflow document is
   *     associated with
   * @param requestedBy the person or system that requested the workflow document
   */
  public WorkflowDocument(
      UUID tenantId, UUID workflowId, String documentDefinitionId, String requestedBy) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.tenantId = tenantId;
    this.workflowId = workflowId;
    this.documentDefinitionId = documentDefinitionId;
    this.status = WorkflowDocumentStatus.REQUESTED;
    this.requested = OffsetDateTime.now();
    this.requestedBy = requestedBy;
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

    WorkflowDocument other = (WorkflowDocument) object;

    return Objects.equals(workflowId, other.workflowId)
        && Objects.equals(documentId, other.documentId);
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
   * Returns the ID for the workflow.
   *
   * @return the ID for the workflow
   */
  public UUID getWorkflowId() {
    return workflowId;
  }

  /**
   * Set the ID for the document definition the workflow document is associated with.
   *
   * @param documentDefinitionId the ID for the document definition the workflow document is
   *     associated with
   */
  public void setDocumentDefinitionId(String documentDefinitionId) {
    this.documentDefinitionId = documentDefinitionId;
  }

  /**
   * Set the ID for the document.
   *
   * @param documentId the ID for the document
   */
  public void setDocumentId(UUID documentId) {
    this.documentId = documentId;
  }

  /**
   * Set the ID for the workflow document.
   *
   * @param id the ID for the workflow document
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the date and time the workflow document was provided.
   *
   * @param provided the date and time the workflow document was provided
   */
  public void setProvided(OffsetDateTime provided) {
    this.provided = provided;
  }

  /**
   * Set the person or system that provided the workflow document.
   *
   * @param providedBy the person or system that provided the workflow document
   */
  public void setProvidedBy(String providedBy) {
    this.providedBy = providedBy;
  }

  /**
   * Set the date and time the workflow document was rejected.
   *
   * @param rejected the date and time the workflow document was rejected
   */
  public void setRejected(OffsetDateTime rejected) {
    this.rejected = rejected;
  }

  /**
   * Set the person or system that rejected the workflow document.
   *
   * @param rejectedBy the person or system that rejected the workflow document
   */
  public void setRejectedBy(String rejectedBy) {
    this.rejectedBy = rejectedBy;
  }

  /**
   * Set the reason the workflow document was rejected.
   *
   * @param rejectionReason the reason the workflow document was rejected
   */
  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  /**
   * Set the date and time the workflow document was requested.
   *
   * @param requested the date and time the workflow document was requested
   */
  public void setRequested(OffsetDateTime requested) {
    this.requested = requested;
  }

  /**
   * Set the person or system that requested the workflow document.
   *
   * @param requestedBy the person or system that requested the workflow document
   */
  public void setRequestedBy(String requestedBy) {
    this.requestedBy = requestedBy;
  }

  /**
   * Set the status of the workflow document.
   *
   * @param status the status of the workflow document
   */
  public void setStatus(WorkflowDocumentStatus status) {
    this.status = status;
  }

  /**
   * Set the ID for the tenant the workflow document is associated with.
   *
   * @param tenantId the ID for the tenant the workflow document is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the date and time the workflow document was verified.
   *
   * @param verified the date and time the workflow document was verified
   */
  public void setVerified(OffsetDateTime verified) {
    this.verified = verified;
  }

  /**
   * Set the person or system that verified the workflow document.
   *
   * @param verifiedBy the person or system that verified the workflow document
   */
  public void setVerifiedBy(String verifiedBy) {
    this.verifiedBy = verifiedBy;
  }

  /**
   * Set the ID for the workflow.
   *
   * @param workflowId the ID for the workflow
   */
  public void setWorkflowId(UUID workflowId) {
    this.workflowId = workflowId;
  }
}
