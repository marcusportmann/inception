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
import digital.inception.core.xml.OffsetDateTimeAdapter;
import digital.inception.executor.constraint.ValidTask;
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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The <b>Task</b> class holds the information for a task.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A task")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "batchId",
  "type",
  "step",
  "status",
  "priority",
  "queued",
  "executed",
  "executionTime",
  "externalReference",
  "executionAttempts",
  "locked",
  "lockName",
  "nextExecution",
  "data"
})
@XmlRootElement(name = "Task", namespace = "https://inception.digital/executor")
@XmlType(
    name = "Task",
    namespace = "https://inception.digital/executor",
    propOrder = {
      "id",
      "batchId",
      "type",
      "step",
      "status",
      "priority",
      "queued",
      "executed",
      "executionTime",
      "externalReference",
      "executionAttempts",
      "locked",
      "lockName",
      "nextExecution",
      "data"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidTask
@Entity
@Table(name = "executor_tasks")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Task implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The optional ID for the task batch. */
  @Schema(description = "The optional ID for the task batch")
  @JsonProperty
  @XmlElement(name = "BatchId")
  @Size(min = 1, max = 50)
  @Column(name = "batch_id", length = 50)
  private String batchId;

  /** The task data. */
  @Schema(description = "The task data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Size(min = 1, max = 10485760)
  @Column(name = "data", nullable = false)
  private String data;

  /** The date and time the task was executed. */
  @Schema(description = "The date and time the task was executed")
  @JsonProperty
  @XmlElement(name = "Executed")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "executed")
  private OffsetDateTime executed;

  /** The number of times the execution of the task has been attempted. */
  @Schema(description = "The number of times the execution of the task has been attempted")
  @JsonProperty
  @XmlElement(name = "ExecutionAttempts")
  @Column(name = "execution_attempts", nullable = false)
  private Integer executionAttempts = 0;

  /** The time taken to execute the task in milliseconds. */
  @Schema(
      description = "The time taken to execute the task in milliseconds",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ExecutionTime", required = true)
  @NotNull
  @Column(name = "execution_time", nullable = false)
  private long executionTime;

  /** The optional external reference for the task. */
  @Schema(description = "The optional external reference for the task")
  @JsonProperty
  @XmlElement(name = "ExternalReference")
  @Size(min = 1, max = 50)
  @Column(name = "external_reference", length = 50)
  private String externalReference;

  /** The ID for the task. */
  @Schema(description = "The ID for the task", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name of the entity that has locked the task for execution. */
  @Schema(description = "The name of the entity that has locked the task for execution")
  @XmlElement(name = "LockName")
  @Size(min = 1, max = 100)
  @Column(name = "lock_name", length = 100)
  private String lockName;

  /** The date and time the task was locked for execution. */
  @Schema(description = "The date and time the task was locked for execution")
  @JsonProperty
  @XmlElement(name = "Locked")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "locked")
  private OffsetDateTime locked;

  /** The date and time the task will be executed. */
  @Schema(description = "The date and time the task will be executed")
  @JsonProperty
  @XmlElement(name = "NextExecution")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "next_execution")
  private OffsetDateTime nextExecution;

  /** The priority of the task. */
  @Schema(description = "The priority of the task", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Priority", required = true)
  @NotNull
  @Column(name = "priority", nullable = false)
  private TaskPriority priority;

  /** The date and time the task was queued for execution. */
  @Schema(
      description = "The date and time the task was queued for execution",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty
  @XmlElement(name = "Queued")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "queued", nullable = false)
  private OffsetDateTime queued;

  /** The status of the task. */
  @Schema(description = "The status of the task", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private TaskStatus status;

  /** The optional code for the current task step for a multistep task. */
  @Schema(description = "The optional code for the current task step for a multistep task")
  @JsonProperty
  @XmlElement(name = "Step")
  @Size(min = 1, max = 50)
  @Column(name = "step", length = 50)
  private String step;

  /** The code for the task type. */
  @Schema(description = "The code for the task type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** Constructs a new <b>Task</b>. */
  public Task() {}

  /**
   * Constructs a new <b>Task</b>.
   *
   * @param type the code for the task type
   * @param data the task data
   */
  public Task(String type, String data) {
    this.id = UUID.randomUUID();
    this.type = type;
    this.data = data;
    this.status = TaskStatus.QUEUED;
    this.priority = TaskPriority.NORMAL;
    this.queued = OffsetDateTime.now();
    this.executionAttempts = 0;
  }

  /**
   * Constructs a new <b>Task</b>.
   *
   * @param type the code for the task type
   * @param step the optional code for the current task step for a multistep task
   * @param data the task data
   */
  public Task(String type, String step, String data) {
    this.id = UUID.randomUUID();
    this.type = type;
    this.step = step;
    this.data = data;
    this.status = TaskStatus.QUEUED;
    this.priority = TaskPriority.NORMAL;
    this.queued = OffsetDateTime.now();
    this.executionAttempts = 0;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
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

    Task other = (Task) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the optional ID for the task batch.
   *
   * @return the optional ID for the task batch
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
   * Returns the date and time the task was executed.
   *
   * @return the date and time the task was executed
   */
  public OffsetDateTime getExecuted() {
    return executed;
  }

  /**
   * Returns the number of times the execution of the task has been attempted.
   *
   * @return the number of times the execution of the task has been attempted
   */
  public Integer getExecutionAttempts() {
    return executionAttempts;
  }

  /**
   * Returns the time taken to execute the task in milliseconds.
   *
   * @return the time taken to execute the task in milliseconds
   */
  public long getExecutionTime() {
    return executionTime;
  }

  /**
   * Returns the optional external reference for the task.
   *
   * @return the optional external reference for the task
   */
  public String getExternalReference() {
    return externalReference;
  }

  /**
   * Returns the ID for the task.
   *
   * @return the ID for the task
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the entity that has locked the task for execution.
   *
   * @return the name of the entity that has locked the task for execution
   */
  public String getLockName() {
    return lockName;
  }

  /**
   * Returns the date and time the task was locked for execution.
   *
   * @return the date and time the task was locked for execution
   */
  public OffsetDateTime getLocked() {
    return locked;
  }

  /**
   * Returns the date and time the task will be executed.
   *
   * @return the date and time the task will be executed
   */
  public OffsetDateTime getNextExecution() {
    return nextExecution;
  }

  /**
   * Returns the priority of the task.
   *
   * @return the priority of the task
   */
  public TaskPriority getPriority() {
    return priority;
  }

  /**
   * Returns the date and time the task was queued for execution.
   *
   * @return the date and time the task was queued for execution
   */
  public OffsetDateTime getQueued() {
    return queued;
  }

  /**
   * Returns the status of the task.
   *
   * @return the status of the task
   */
  public TaskStatus getStatus() {
    return status;
  }

  /**
   * Returns the optional code for the current task step for a multistep task.
   *
   * @return the optional code for the current task step for a multistep task
   */
  public String getStep() {
    return step;
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
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /** Increment the number of execution attempts for the task. */
  public void incrementExecutionAttempts() {
    if (executionAttempts == null) {
      executionAttempts = 1;
    } else {
      executionAttempts++;
    }
  }

  /**
   * Set the optional ID for the task batch.
   *
   * @param batchId the optional ID for the task batch
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
   * Set the date and time the task was executed.
   *
   * @param executed the date and time the task was executed
   */
  public void setExecuted(OffsetDateTime executed) {
    this.executed = executed;
  }

  /**
   * Set the number of times the execution of the task has been attempted.
   *
   * @param executionAttempts the number of times the execution of the task has been attempted
   */
  public void setExecutionAttempts(Integer executionAttempts) {
    this.executionAttempts = executionAttempts;
  }

  /**
   * Set the time taken to execute the task in milliseconds.
   *
   * @param executionTime the time taken to execute the task in milliseconds
   */
  public void setExecutionTime(long executionTime) {
    this.executionTime = executionTime;
  }

  /**
   * Set the optional external reference for the task.
   *
   * @param externalReference the optional external reference for the task
   */
  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  /**
   * Set the ID for the task.
   *
   * @param id the ID for the task
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the entity that has locked the task for execution.
   *
   * @param lockName the name of the entity that has locked the task for execution
   */
  public void setLockName(String lockName) {
    this.lockName = lockName;
  }

  /**
   * Set the date and time the task was locked for execution.
   *
   * @param locked the date and time the task was locked for execution
   */
  public void setLocked(OffsetDateTime locked) {
    this.locked = locked;
  }

  /**
   * Set the date and time the task will be executed.
   *
   * @param nextExecution the date and time the task will be executed
   */
  public void setNextExecution(OffsetDateTime nextExecution) {
    this.nextExecution = nextExecution;
  }

  /**
   * Set the priority of the task.
   *
   * @param priority the priority of the task
   */
  public void setPriority(TaskPriority priority) {
    this.priority = priority;
  }

  /**
   * Set the date and time the task was queued for execution.
   *
   * @param queued the date and time the task was queued for execution
   */
  public void setQueued(OffsetDateTime queued) {
    this.queued = queued;
  }

  /**
   * Set the status of the task.
   *
   * @param status the status of the task
   */
  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  /**
   * Set the optional code for the current task step for a multistep task.
   *
   * @param step the optional code for the current task step for a multistep task
   */
  public void setStep(String step) {
    this.step = step;
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
