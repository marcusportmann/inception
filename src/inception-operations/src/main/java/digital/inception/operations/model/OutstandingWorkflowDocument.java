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
 * The {@code OutstandingWorkflowDocument} class holds the information for an outstanding workflow
 * document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An outstanding workflow document")
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
  "status",
  "description"
})
@XmlRootElement(
    name = "OutstandingWorkflowDocument",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "OutstandingWorkflowDocument",
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
      "status",
      "description"
    })
@XmlAccessorType(XmlAccessType.FIELD)
public class OutstandingWorkflowDocument implements Serializable {

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

  /** The ID for the workflow. */
  @Schema(description = "The ID for the workflow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "WorkflowId", required = true)
  @NotNull
  private UUID workflowId;

  /** Constructs a new {@code OutstandingWorkflowDocument}. */
  public OutstandingWorkflowDocument() {}

  /**
   * Constructs a new {@code OutstandingWorkflowDocument}.
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
   * @param status the status of the workflow document
   * @param requested the date and time the workflow document was requested
   * @param requestedFromPartyId the ID for the party the workflow document was requested from
   * @param internal is the workflow document internal-only and excluded for external users
   * @param description the description for the workflow document
   */
  public OutstandingWorkflowDocument(
      UUID id,
      UUID tenantId,
      UUID workflowId,
      String documentDefinitionId,
      String documentDefinitionName,
      String documentDefinitionShortName,
      String documentDefinitionDescription,
      WorkflowDocumentStatus status,
      OffsetDateTime requested,
      UUID requestedFromPartyId,
      boolean internal,
      String description) {
    this.id = id;
    this.tenantId = tenantId;
    this.workflowId = workflowId;
    this.documentDefinitionId = documentDefinitionId;
    this.documentDefinitionName = documentDefinitionName;
    this.documentDefinitionShortName = documentDefinitionShortName;
    this.documentDefinitionDescription = documentDefinitionDescription;
    this.status = status;
    this.requested = requested;
    this.requestedFromPartyId = requestedFromPartyId;
    this.internal = internal;
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
   * Returns the ID for the workflow document.
   *
   * @return the ID for the workflow document
   */
  public UUID getId() {
    return id;
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

  /**
   * Sets the description for the workflow document.
   *
   * @param description the description for the workflow document
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the description for the document definition the workflow document is associated with.
   *
   * @param documentDefinitionDescription the description for the document definition the workflow
   *     document is associated with
   */
  public void setDocumentDefinitionDescription(String documentDefinitionDescription) {
    this.documentDefinitionDescription = documentDefinitionDescription;
  }

  /**
   * Sets the short name for the document definition the workflow document is associated with.
   *
   * @param documentDefinitionShortName the short name for the document definition the workflow
   *     document is associated with
   */
  public void setDocumentDefinitionShortName(String documentDefinitionShortName) {
    this.documentDefinitionShortName = documentDefinitionShortName;
  }

  /**
   * Set whether the the workflow document is internal-only and excluded for external users.
   *
   * @param internal {@code true} if the workflow document is internal-only and excluded for
   *     external users or {@code false} otherwise
   */
  public void setInternal(boolean internal) {
    this.internal = internal;
  }

  /**
   * Sets the ID for the party the workflow document was requested from.
   *
   * @param requestedFromPartyId the ID for the party the workflow document was requested from
   */
  public void setRequestedFromPartyId(UUID requestedFromPartyId) {
    this.requestedFromPartyId = requestedFromPartyId;
  }

  /**
   * Sets the status of the workflow document.
   *
   * @param status the status of the workflow document
   */
  public void setStatus(WorkflowDocumentStatus status) {
    this.status = status;
  }
}
