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
import com.github.f4b6a3.uuid.UuidCreator;
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
 * The {@code TaskEvent} class holds the information for a task event.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A task event")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "timestamp", "taskId", "taskType", "taskStep", "taskData"})
@XmlRootElement(name = "TaskEvent", namespace = "https://inception.digital/executor")
@XmlType(
    name = "TaskEvent",
    namespace = "https://inception.digital/executor",
    propOrder = {"id", "type", "timestamp", "taskId", "taskType", "taskStep", "taskData"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "executor_task_events")
@SuppressWarnings({"unused", "WeakerAccess"})
public class TaskEvent implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the task event. */
  @Schema(description = "The ID for the task event", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The task data. */
  @Schema(description = "The task data")
  @JsonProperty
  @XmlElement(name = "TaskData")
  @Size(min = 1, max = 10485760)
  @Column(name = "task_data")
  private String taskData;

  /** The ID for the task. */
  @Schema(description = "The ID for the task", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TaskId", required = true)
  @NotNull
  @Column(name = "task_id", nullable = false)
  private UUID taskId;

  /** The code for the task step for a multistep task. */
  @Schema(description = "The code for the task step for a multistep task")
  @JsonProperty
  @XmlElement(name = "TaskStep")
  @Size(min = 1, max = 50)
  @Column(name = "task_step", length = 50)
  private String taskStep;

  /** The code for the task type. */
  @Schema(description = "The code for the task type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TaskType", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "task_type", length = 50, nullable = false)
  private String taskType;

  /** The date and time the task event occurred. */
  @Schema(description = "The date and time the task event occurred")
  @JsonProperty
  @XmlElement(name = "Timestamp")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "timestamp")
  private OffsetDateTime timestamp;

  /** The task event type. */
  @Schema(description = "The task event type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", length = 50, nullable = false)
  private TaskEventType type;

  /** Constructs a new {@code TaskEvent}. */
  public TaskEvent() {}

  /**
   * Constructs a new {@code TaskEvent}.
   *
   * @param type the task event type
   * @param task the task
   * @param withData {@code true} if the task data should be captured as part of the task event or
   *     {@code false} otherwise
   */
  public TaskEvent(TaskEventType type, Task task, boolean withData) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.type = type;
    this.timestamp = OffsetDateTime.now();
    this.taskId = task.getId();
    this.taskType = task.getType();
    this.taskStep = task.getStep();

    if (withData) {
      this.taskData = task.getData();
    }
  }

  /**
   * Constructs a new {@code TaskEvent}.
   *
   * @param type the task event type
   * @param taskId the ID for the task
   * @param taskType the code for the task type
   * @param taskStep optional code for the task step for a multistep task
   */
  public TaskEvent(TaskEventType type, UUID taskId, String taskType, String taskStep) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.type = type;
    this.timestamp = OffsetDateTime.now();
    this.taskId = taskId;
    this.taskType = taskType;
    this.taskStep = taskStep;
  }

  /**
   * Constructs a new {@code TaskEvent}.
   *
   * @param type the task event type
   * @param taskId the ID for the task
   * @param taskType the code for the task type
   * @param taskStep optional code for the task step for a multistep task
   * @param taskData the task data
   */
  public TaskEvent(
      TaskEventType type, UUID taskId, String taskType, String taskStep, String taskData) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.type = type;
    this.timestamp = OffsetDateTime.now();
    this.taskId = taskId;
    this.taskType = taskType;
    this.taskStep = taskStep;
    this.taskData = taskData;
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

    TaskEvent other = (TaskEvent) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the task event.
   *
   * @return the ID for the task event
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the task data.
   *
   * @return the task data
   */
  public String getTaskData() {
    return taskData;
  }

  /**
   * Returns the ID for the task.
   *
   * @return the ID for the task
   */
  public UUID getTaskId() {
    return taskId;
  }

  /**
   * Returns the code for the task step for a multistep task.
   *
   * @return the code for the task step for a multistep task
   */
  public String getTaskStep() {
    return taskStep;
  }

  /**
   * Returns the code for the task type.
   *
   * @return the code for the task type
   */
  public String getTaskType() {
    return taskType;
  }

  /**
   * Returns the date and time the task event occurred.
   *
   * @return the date and time the task event occurred
   */
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * Returns the task event type.
   *
   * @return the task event type
   */
  public TaskEventType getType() {
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
   * Set the ID for the task event.
   *
   * @param id the ID for the task event
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the task data.
   *
   * @param taskData the task data
   */
  public void setTaskData(String taskData) {
    this.taskData = taskData;
  }

  /**
   * Set the ID for the task.
   *
   * @param taskId the ID for the task
   */
  public void setTaskId(UUID taskId) {
    this.taskId = taskId;
  }

  /**
   * Set the code for the task step for a multistep task.
   *
   * @param taskStep the code for the task step for a multistep task
   */
  public void setTaskStep(String taskStep) {
    this.taskStep = taskStep;
  }

  /**
   * Set the code for the task type.
   *
   * @param taskType the code for the task type
   */
  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  /**
   * Set the date and time the task event occurred.
   *
   * @param timestamp the date and time the task event occurred
   */
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Set the task event type.
   *
   * @param type the task event type
   */
  public void setType(TaskEventType type) {
    this.type = type;
  }
}
