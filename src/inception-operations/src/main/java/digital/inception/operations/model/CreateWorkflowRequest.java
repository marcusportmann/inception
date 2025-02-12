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
import digital.inception.operations.constraint.ValidCreateWorkflowRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * The <b>CreateWorkflowRequest</b> class holds the information for a request to create a workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to create a workflow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"tenantId", "parentId", "definitionId", "externalReference", "data"})
@XmlRootElement(name = "CreateWorkflowRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "CreateWorkflowRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"tenantId", "parentId", "definitionId", "externalReference", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@ValidCreateWorkflowRequest
@SuppressWarnings({"unused", "WeakerAccess"})
public class CreateWorkflowRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The data for the workflow. */
  @Schema(description = "The data for the workflow")
  @JsonProperty
  @XmlElement(name = "Data")
  @Size(min = 1, max = 10485760)
  private String data;

  /** The ID for the workflow definition the workflow is associated with. */
  @Schema(
      description = "The ID for the workflow definition the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String definitionId;

  /** The external reference for the workflow. */
  @Schema(description = "The external reference for the workflow")
  @JsonProperty
  @XmlElement(name = "ExternalReference")
  @Size(max = 100)
  private String externalReference;

  /** The ID for the parent workflow. */
  @Schema(description = "The ID for the parent workflow")
  @JsonProperty
  @XmlElement(name = "ParentId")
  private UUID parentId;

  /** The ID for the tenant the workflow is associated with. */
  @Schema(
      description = "The ID for the tenant the workflow is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  private UUID tenantId;

  /** Constructs a new <b>CreateWorkflowRequest</b>. */
  public CreateWorkflowRequest() {}

  /**
   * Constructs a new <b>CreateWorkflowRequest</b>.
   *
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param data the data for the workflow
   */
  public CreateWorkflowRequest(String definitionId, String data) {
    this.definitionId = definitionId;
    this.data = data;
  }

  /**
   * Constructs a new <b>CreateWorkflowRequest</b>.
   *
   * @param parentId the ID for the parent workflow
   * @param definitionId the ID for the workflow definition the workflow is associated with
   * @param data the data for the workflow
   */
  public CreateWorkflowRequest(UUID parentId, String definitionId, String data) {
    this.parentId = parentId;
    this.definitionId = definitionId;
    this.data = data;
  }

  /**
   * Returns the data for the workflow.
   *
   * @return the data for the workflow
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the ID for the workflow definition the workflow is associated with.
   *
   * @return the ID for the workflow definition the workflow is associated with
   */
  public String getDefinitionId() {
    return definitionId;
  }

  /**
   * Returns the external reference for the workflow.
   *
   * @return the external reference for the workflow
   */
  public String getExternalReference() {
    return externalReference;
  }

  /**
   * Returns the ID for the parent workflow.
   *
   * @return the ID for the parent workflow
   */
  public UUID getParentId() {
    return parentId;
  }

  /**
   * Returns the ID for the tenant the workflow is associated with.
   *
   * @return the ID for the tenant the workflow is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Set the data for the workflow.
   *
   * @param data the data for the workflow
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the workflow definition the workflow is associated with.
   *
   * @param definitionId the ID for the workflow definition the workflow is associated with
   */
  public void setDefinitionId(String definitionId) {
    this.definitionId = definitionId;
  }

  /**
   * Set the external reference for the workflow.
   *
   * @param externalReference the external reference for the workflow
   */
  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  /**
   * Set the ID for the parent workflow.
   *
   * @param parentId the ID for the parent workflow
   */
  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  /**
   * Set the ID for the tenant the workflow is associated with.
   *
   * @param tenantId the ID for the tenant the workflow is associated with
   */
  public void setTenantId(@NotNull UUID tenantId) {
    this.tenantId = tenantId;
  }
}
