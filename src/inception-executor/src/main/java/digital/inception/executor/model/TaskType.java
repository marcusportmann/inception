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
import digital.inception.executor.constraint.ValidTaskType;
import digital.inception.executor.persistence.TaskEventTypeListConverter;
import io.swagger.v3.oas.annotations.media.Schema;
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
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * The <b>TaskType</b> class holds the information for a task type.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A task type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "code",
  "name",
  "priority",
  "enabled",
  "executorClass",
  "archiveCompleted",
  "archiveFailed",
  "maximumExecutionAttempts",
  "executionTimeout",
  "retryDelay",
  "eventTypes",
  "eventTypesWithTaskData"
})
@XmlRootElement(name = "TaskType", namespace = "https://inception.digital/executor")
@XmlType(
    name = "TaskType",
    namespace = "https://inception.digital/executor",
    propOrder = {
      "code",
      "name",
      "priority",
      "enabled",
      "executorClass",
      "archiveCompleted",
      "archiveFailed",
      "maximumExecutionAttempts",
      "executionTimeout",
      "retryDelay",
      "eventTypes",
      "eventTypesWithTaskData"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidTaskType
@Entity
@Table(name = "executor_task_types")
@SuppressWarnings({"unused", "WeakerAccess"})
public class TaskType implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** Archive completed tasks of this type. */
  @Schema(
      description = "Archive completed tasks of this type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ArchiveCompleted", required = true)
  @NotNull
  @Column(name = "archive_completed", nullable = false)
  private boolean archiveCompleted;

  /** Archive failed tasks of this type. */
  @Schema(
      description = "Archive failed tasks of this type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ArchiveFailed", required = true)
  @NotNull
  @Column(name = "archive_failed", nullable = false)
  private boolean archiveFailed;

  /** The code for the task type. */
  @Schema(description = "The code for the task type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "code", length = 50, nullable = false)
  private String code;

  /**
   * Is the task type enabled?
   *
   * <p>If the task type is not enabled, tasks of this type will not be executed.
   */
  @Schema(description = "Is the task type enabled", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Enabled", required = true)
  @NotNull
  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  /** The event types that should be tracked for tasks of this type. */
  @Schema(description = "The event types that should be tracked for tasks of this type")
  @JsonProperty
  @XmlElementWrapper(name = "EventTypes")
  @XmlElement(name = "EventType")
  @Column(name = "event_types", length = 1000)
  @Convert(converter = TaskEventTypeListConverter.class)
  private List<TaskEventType> eventTypes;

  /** The event types that should be tracked for tasks of this type. */
  @Schema(
      description = "The event types that should be tracked for tasks of this type with task data")
  @JsonProperty
  @XmlElementWrapper(name = "EventTypesWithTaskData")
  @XmlElement(name = "EventTypeWithTaskData")
  @Column(name = "event_types_with_task_data", length = 1000)
  @Convert(converter = TaskEventTypeListConverter.class)
  private List<TaskEventType> eventTypesWithTaskData;

  /**
   * The amount of time in milliseconds after which a locked and executing task of this type will be
   * considered hung and will be reset.
   */
  @Schema(
      description =
          "The amount of time in milliseconds after which a locked and executing task of this type will be considered hung and will be reset")
  @JsonProperty
  @XmlElement(name = "ExecutionTimeout")
  @Column(name = "execution_timeout")
  private Integer executionTimeout;

  /** The fully qualified name of the Java class that executes tasks of this type. */
  @Schema(
      description = "The fully qualified name of the Java class that executes tasks of this type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ExecutorClass", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "executor_class", length = 1000, nullable = false)
  private String executorClass;

  /** The maximum execution attempts for tasks of this type. */
  @Schema(description = "The maximum execution attempts for tasks of this type")
  @JsonProperty
  @XmlElement(name = "MaximumExecutionAttempts")
  @Column(name = "maximum_execution_attempts")
  private Integer maximumExecutionAttempts;

  /** The name of the task type. */
  @Schema(description = "The name of the task type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The priority for tasks of this type. */
  @Schema(
      description = "The priority for tasks of this type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Priority", required = true)
  @NotNull
  @Column(name = "priority", length = 50, nullable = false)
  private TaskPriority priority;

  /** The retry delay for tasks of this type. */
  @Schema(description = "The retry delay for tasks of this type")
  @JsonProperty
  @XmlElement(name = "RetryDelay")
  @Column(name = "retry_delay")
  private Integer retryDelay;

  /**
   * Constructs a new <b>TaskType</b>.
   *
   * @param code the code for the task type
   * @param name the name of the task type
   * @param priority the priority for tasks of this type
   * @param executorClass the fully qualified name of the Java class that executes tasks of this
   *     type
   * @param archiveCompleted archive completed tasks of this type
   * @param archiveFailed archive failed tasks of this type
   * @param maximumExecutionAttempts the maximum execution attempts for tasks of this type
   * @param retryDelay the retry delay for tasks of this type
   * @param executionTimeout the amount of time in milliseconds after which a locked and executing
   *     task of this type will be considered hung and will be reset
   */
  public TaskType(
      String code,
      String name,
      TaskPriority priority,
      String executorClass,
      boolean archiveCompleted,
      boolean archiveFailed,
      int maximumExecutionAttempts,
      int retryDelay,
      int executionTimeout) {
    this.code = code;
    this.name = name;
    this.priority = priority;
    this.executorClass = executorClass;
    this.archiveCompleted = archiveCompleted;
    this.archiveFailed = archiveFailed;
    this.maximumExecutionAttempts = maximumExecutionAttempts;
    this.retryDelay = retryDelay;
    this.executionTimeout = executionTimeout;
    this.enabled = true;
  }

  /**
   * Constructs a new <b>TaskType</b>.
   *
   * @param code the code for the task type
   * @param name the name of the task type
   * @param priority the priority for tasks of this type
   * @param executorClass the fully qualified name of the Java class that executes tasks of this
   *     type
   * @param archiveCompleted archive completed tasks of this type
   * @param archiveFailed archive failed tasks of this type
   */
  public TaskType(
      String code,
      String name,
      TaskPriority priority,
      String executorClass,
      boolean archiveCompleted,
      boolean archiveFailed) {
    this.code = code;
    this.name = name;
    this.priority = priority;
    this.executorClass = executorClass;
    this.archiveCompleted = archiveCompleted;
    this.archiveFailed = archiveFailed;
    this.enabled = true;
  }

  /**
   * Constructs a new <b>TaskType</b>.
   *
   * @param code the code for the task type
   * @param name the name of the task type
   * @param priority the priority for tasks of this type
   * @param executorClass the fully qualified name of the Java class that executes tasks of this
   *     type
   * @param archiveCompleted archive completed tasks of this type
   * @param archiveFailed archive failed tasks of this type
   * @param maximumExecutionAttempts the maximum execution attempts for tasks of this type
   */
  public TaskType(
      String code,
      String name,
      TaskPriority priority,
      String executorClass,
      boolean archiveCompleted,
      boolean archiveFailed,
      int maximumExecutionAttempts) {
    this.code = code;
    this.name = name;
    this.priority = priority;
    this.executorClass = executorClass;
    this.archiveCompleted = archiveCompleted;
    this.archiveFailed = archiveFailed;
    this.maximumExecutionAttempts = maximumExecutionAttempts;
    this.enabled = true;
  }

  /**
   * Constructs a new <b>TaskType</b>.
   *
   * @param code the code for the task type
   * @param name the name of the task type
   * @param priority the priority for tasks of this type
   * @param executorClass the fully qualified name of the Java class that executes tasks of this
   *     type
   * @param archiveCompleted archive completed tasks of this type
   * @param archiveFailed archive failed tasks of this type
   * @param maximumExecutionAttempts the maximum execution attempts for tasks of this type
   * @param retryDelay the retry delay for tasks of this type
   */
  public TaskType(
      String code,
      String name,
      TaskPriority priority,
      String executorClass,
      boolean archiveCompleted,
      boolean archiveFailed,
      int maximumExecutionAttempts,
      int retryDelay) {
    this.code = code;
    this.name = name;
    this.priority = priority;
    this.executorClass = executorClass;
    this.archiveCompleted = archiveCompleted;
    this.archiveFailed = archiveFailed;
    this.maximumExecutionAttempts = maximumExecutionAttempts;
    this.retryDelay = retryDelay;
    this.enabled = true;
  }

  /**
   * Constructs a new <b>TaskType</b>.
   *
   * @param code the code for the task type
   * @param name the name of the task type
   * @param priority the priority for tasks of this type
   * @param executorClass the fully qualified name of the Java class that executes tasks of this
   *     type
   */
  public TaskType(String code, String name, TaskPriority priority, String executorClass) {
    this.code = code;
    this.name = name;
    this.priority = priority;
    this.executorClass = executorClass;
    this.archiveCompleted = false;
    this.archiveFailed = false;
    this.enabled = true;
  }

  /**
   * Constructs a new <b>TaskType</b>.
   *
   * @param code the code for the task type
   * @param name the name of the task type
   * @param priority the priority for tasks of this type
   * @param executorClass the fully qualified name of the Java class that executes tasks of this
   *     type
   * @param maximumExecutionAttempts the maximum execution attempts for tasks of this type
   */
  public TaskType(
      String code,
      String name,
      TaskPriority priority,
      String executorClass,
      int maximumExecutionAttempts) {
    this.code = code;
    this.name = name;
    this.priority = priority;
    this.executorClass = executorClass;
    this.archiveCompleted = false;
    this.archiveFailed = false;
    this.maximumExecutionAttempts = maximumExecutionAttempts;
    this.enabled = true;
  }

  /**
   * Constructs a new <b>TaskType</b>.
   *
   * @param code the code for the task type
   * @param name the name of the task type
   * @param priority the priority for tasks of this type
   * @param executorClass the fully qualified name of the Java class that executes tasks of this
   *     type
   * @param maximumExecutionAttempts the maximum execution attempts for tasks of this type
   * @param retryDelay the retry delay for tasks of this type
   */
  public TaskType(
      String code,
      String name,
      TaskPriority priority,
      String executorClass,
      int maximumExecutionAttempts,
      int retryDelay) {
    this.code = code;
    this.name = name;
    this.priority = priority;
    this.executorClass = executorClass;
    this.archiveCompleted = false;
    this.archiveFailed = false;
    this.maximumExecutionAttempts = maximumExecutionAttempts;
    this.retryDelay = retryDelay;
    this.enabled = true;
  }

  /** Constructs a new <b>TaskType</b>. */
  public TaskType() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
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

    TaskType other = (TaskType) object;

    return Objects.equals(code, other.code);
  }

  /**
   * Returns whether to archive completed tasks of this type.
   *
   * @return <b>true</b> if completed tasks of this type should be archived or <b>false</b>
   *     otherwise
   */
  public boolean getArchiveCompleted() {
    return archiveCompleted;
  }

  /**
   * Returns whether to archive failed tasks of this type.
   *
   * @return <b>true</b> if failed tasks of this type should be archived or <b>false</b> otherwise
   */
  public boolean getArchiveFailed() {
    return archiveFailed;
  }

  /**
   * Returns the code for the task type.
   *
   * @return the code for the task type
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns whether the task type is enabled.
   *
   * @return <b>true</b> if the task type is enabled <b>false</b> otherwise
   */
  public boolean getEnabled() {
    return enabled;
  }

  /**
   * Returns the event types that should be tracked for tasks of this type.
   *
   * @return the event types that should be tracked for tasks of this type
   */
  public List<TaskEventType> getEventTypes() {
    return eventTypes;
  }

  /**
   * Returns the event types that should be tracked for tasks of this type with task data.
   *
   * @return the event types that should be tracked for tasks of this type with task data
   */
  public List<TaskEventType> getEventTypesWithTaskData() {
    return eventTypesWithTaskData;
  }

  /**
   * Returns the amount of time in milliseconds after which a locked and executing task of this type
   * will be considered hung and will be reset.
   *
   * @return the amount of time in milliseconds after which a locked and executing task of this type
   *     will be considered hung and will be reset
   */
  public Integer getExecutionTimeout() {
    return executionTimeout;
  }

  /**
   * Returns the fully qualified name of the Java class that executes tasks of this type.
   *
   * @return the fully qualified name of the Java class that executes tasks of this type
   */
  public String getExecutorClass() {
    return executorClass;
  }

  /**
   * Returns the maximum execution attempts for tasks of this type.
   *
   * @return the maximum execution attempts for tasks of this type
   */
  public Integer getMaximumExecutionAttempts() {
    return maximumExecutionAttempts;
  }

  /**
   * Returns the name of the task type.
   *
   * @return the name of the task type
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the priority for tasks of this type.
   *
   * @return the priority for tasks of this type
   */
  public TaskPriority getPriority() {
    return priority;
  }

  /**
   * Returns the retry delay for tasks of this type.
   *
   * @return the retry delay for tasks of this type
   */
  public Integer getRetryDelay() {
    return retryDelay;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (code == null) ? 0 : code.hashCode();
  }

  /**
   * Returns whether the event type is enabled for the task type.
   *
   * @param eventType the task event type
   * @return <b>true</b> if the event type is enabled for the task type or <b>false</b> otherwise
   */
  public boolean isEventTypeEnabled(TaskEventType eventType) {
    if (eventTypes != null) {
      return getEventTypes().contains(eventType);
    }

    return false;
  }

  /**
   * Returns whether the event type is enabled for the task type with task data.
   *
   * @param eventType the task event type
   * @return <b>true</b> if the event type is enabled for the task type with task data or
   *     <b>false</b> otherwise
   */
  public boolean isEventTypeEnabledWithTaskData(TaskEventType eventType) {
    if (eventTypesWithTaskData != null) {
      return getEventTypesWithTaskData().contains(eventType);
    }

    return false;
  }

  /**
   * Set whether to archive completed tasks of this type.
   *
   * @param archiveCompleted <b>true</b> if completed tasks of this type should be archived or
   *     <b>false</b> otherwise
   */
  public void setArchiveCompleted(boolean archiveCompleted) {
    this.archiveCompleted = archiveCompleted;
  }

  /**
   * Set whether to archive failed tasks of this type.
   *
   * @param archiveFailed <b>true</b> if failed tasks of this type should be archived or
   *     <b>false</b> otherwise
   */
  public void setArchiveFailed(boolean archiveFailed) {
    this.archiveFailed = archiveFailed;
  }

  /**
   * Set the code for the task type.
   *
   * @param code the code for the task type
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set whether the task type is enabled.
   *
   * @param enabled <b>true</b> if the task type is enabled or <b>false</b> otherwise
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Set the event types that should be tracked for tasks of this type.
   *
   * @param eventTypes the event types that should be tracked for tasks of this type
   */
  public void setEventTypes(List<TaskEventType> eventTypes) {
    this.eventTypes = eventTypes;
  }

  /**
   * Set the event types that should be tracked for tasks of this type with task data.
   *
   * @param eventTypesWithTaskData the event types that should be tracked for tasks of this type
   *     with task data
   */
  public void setEventTypesWithTaskData(List<TaskEventType> eventTypesWithTaskData) {
    this.eventTypesWithTaskData = eventTypesWithTaskData;
  }

  /**
   * Set the amount of time in milliseconds after which a locked and executing task of this type
   * will be considered hung and will be reset.
   *
   * @param executionTimeout the amount of time in milliseconds after which a locked and executing
   *     task of this type will be considered hung and will be reset
   */
  public void setExecutionTimeout(Integer executionTimeout) {
    this.executionTimeout = executionTimeout;
  }

  /**
   * Set the fully qualified name of the Java class that executes tasks of this type.
   *
   * @param executorClass the fully qualified name of the Java class that executes tasks of this
   *     type
   */
  public void setExecutorClass(String executorClass) {
    this.executorClass = executorClass;
  }

  /**
   * Set the maximum execution attempts for tasks of this type.
   *
   * @param maximumExecutionAttempts the maximum execution attempts for tasks of this type
   */
  public void setMaximumExecutionAttempts(Integer maximumExecutionAttempts) {
    this.maximumExecutionAttempts = maximumExecutionAttempts;
  }

  /**
   * Set the name of the task type.
   *
   * @param name the name of the task type
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the priority for tasks of this type.
   *
   * @param priority the priority for tasks of this type
   */
  public void setPriority(TaskPriority priority) {
    this.priority = priority;
  }

  /**
   * Set the retry delay for tasks of this type.
   *
   * @param retryDelay the retry delay for tasks of this type
   */
  public void setRetryDelay(Integer retryDelay) {
    this.retryDelay = retryDelay;
  }
}
