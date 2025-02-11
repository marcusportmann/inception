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

package digital.inception.executor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

/**
 * The <b>QueueTaskRequest</b> class holds the information for a request to queue a task for
 * execution.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to queue a task for execution")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "batchId", "externalReference", "suspended", "data"})
@XmlRootElement(name = "QueueTaskRequest", namespace = "https://inception.digital/executor")
@XmlType(
    name = "QueueTaskRequest",
    namespace = "https://inception.digital/executor",
    propOrder = {"type", "batchId", "externalReference", "suspended", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class QueueTaskRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the task batch. */
  @Schema(description = "The ID for the task batch")
  @JsonProperty
  @XmlElement(name = "BatchId")
  @Size(min = 1, max = 50)
  private String batchId;

  /** The task data. */
  @Schema(description = "The task data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Size(min = 1, max = 10485760)
  private String data;

  /** The external reference for the task. */
  @Schema(description = "The external reference for the task")
  @JsonProperty
  @XmlElement(name = "ExternalReference")
  @Size(min = 1, max = 50)
  private String externalReference;

  /** The flag indicating that the task must be suspended. */
  @Schema(description = "The flag indicating that the task must be suspended")
  @JsonProperty
  @XmlElement(name = "Suspended")
  private Boolean suspended;

  /** The code for the task type. */
  @Schema(description = "The code for the task type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  private String type;

  /** Constructs a new <b>QueueTaskRequest</b>. */
  public QueueTaskRequest() {}

  /**
   * Constructs a new <b>QueueTaskRequest</b>.
   *
   * @param type the code for the task type
   * @param data the task data
   */
  public QueueTaskRequest(String type, String data) {
    this.type = type;
    this.data = data;
  }

  /**
   * Constructs a new <b>QueueTaskRequest</b>.
   *
   * @param type the code for the task type
   * @param batchId the ID for the task batch
   * @param externalReference the external reference for the task
   * @param data the task data
   * @param suspended the flag indicating that the task must be suspended
   */
  public QueueTaskRequest(
      String type, String batchId, String externalReference, String data, boolean suspended) {
    this.batchId = batchId;
    this.externalReference = externalReference;
    this.type = type;
    this.data = data;
    this.suspended = suspended;
  }

  /**
   * Returns the ID for the task batch.
   *
   * @return the ID for the task batch
   */
  public String getBatchId() {
    return batchId;
  }

  /**
   * Returns the task data.
   *
   * @return the task data
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the external reference for the task.
   *
   * @return the external reference for the task
   */
  public String getExternalReference() {
    return externalReference;
  }

  /**
   * Returns the flag indicating that the task must be suspended.
   *
   * @return the flag indicating that the task must be suspended
   */
  public Boolean getSuspended() {
    return suspended;
  }

  /**
   * Returns the code for the task type.
   *
   * @return the code for the task type
   */
  public String getType() {
    return type;
  }

  /**
   * Set the ID for the task batch.
   *
   * @param batchId the ID for the task batch
   */
  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  /**
   * Set the task data.
   *
   * @param data the task data
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the external reference for the task.
   *
   * @param externalReference the external reference for the task
   */
  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  /**
   * Set the flag indicating that the task must be suspended.
   *
   * @param suspended the flag indicating that the task must be suspended
   */
  public void setSuspended(Boolean suspended) {
    this.suspended = suspended;
  }

  /**
   * Set the code for the task type.
   *
   * @param type the code for the task type
   */
  public void setType(String type) {
    this.type = type;
  }
}
