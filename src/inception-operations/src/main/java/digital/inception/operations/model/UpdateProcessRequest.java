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
 * The <b>UpdateProcessRequest</b> class holds the information for request to update a process.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to update a process")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "status", "data"})
@XmlRootElement(name = "UpdateProcessRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "UpdateProcessRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "status", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class UpdateProcessRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The updated data for the process. */
  @Schema(description = "The updated data for the process")
  @JsonProperty
  @XmlElement(name = "Data")
  @Size(min = 1, max = 10485760)
  private String data;

  /** The ID for the process. */
  @Schema(description = "The ID for the process", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /** The updated status of the process. */
  @Schema(description = "The updated status of the process")
  @JsonProperty
  @XmlElement(name = "Status")
  private ProcessStatus status;

  /** Constructs a new <b>UpdateProcessRequest</b>. */
  public UpdateProcessRequest() {}

  /**
   * Constructs a new <b>UpdateProcessRequest</b>.
   *
   * @param id the ID for the process
   * @param data the updated data for the process
   */
  public UpdateProcessRequest(UUID id, String data) {
    this.id = id;
    this.data = data;
  }

  /**
   * Constructs a new <b>UpdateProcessRequest</b>.
   *
   * @param id the ID for the process
   * @param status the updated status of the process
   */
  public UpdateProcessRequest(UUID id, ProcessStatus status) {
    this.id = id;
    this.status = status;
  }

  /**
   * Constructs a new <b>UpdateProcessRequest</b>.
   *
   * @param id the ID for the process
   * @param status the updated status of the process
   * @param data the updated data for the process
   */
  public UpdateProcessRequest(UUID id, ProcessStatus status, String data) {
    this.id = id;
    this.status = status;
    this.data = data;
  }

  /**
   * Returns the updated data for the process.
   *
   * @return the updated data for the process
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the ID for the process.
   *
   * @return the ID for the process
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the updated status of the process.
   *
   * @return the updated status of the process
   */
  public ProcessStatus getStatus() {
    return status;
  }

  /**
   * Set the updated data for the process.
   *
   * @param data the updated data for the process
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the ID for the process.
   *
   * @param id the ID for the process
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the updated status of the process.
   *
   * @param status the updated status of the process
   */
  public void setStatus(ProcessStatus status) {
    this.status = status;
  }
}
