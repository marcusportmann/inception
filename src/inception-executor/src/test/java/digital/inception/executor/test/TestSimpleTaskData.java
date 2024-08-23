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

package digital.inception.executor.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serial;
import java.io.Serializable;

/**
 * The <b>TestSimpleTaskData</b> class holds the data for the test simple task.
 *
 * @author Marcus Portmann
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"message", "updateMessage", "failTask", "retryTask", "delayTask"})
@SuppressWarnings({"unused", "WeakerAccess"})
public class TestSimpleTaskData implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** Should the task be delayed. */
  @JsonProperty(required = true)
  private boolean delayTask;

  /** Should the task be failed. */
  @JsonProperty(required = true)
  private boolean failTask;

  /** The message. */
  @JsonProperty(required = true)
  private String message;

  /** Should the task be retried. */
  @JsonProperty(required = true)
  private boolean retryTask;

  /** Should the message be updated. */
  @JsonProperty(required = true)
  private boolean updateMessage;

  /** Constructs a new <b>TestSimpleTaskData</b>. */
  public TestSimpleTaskData() {}

  /**
   * Constructs a new <b>TestSimpleTaskData</b>.
   *
   * @param message the message
   */
  public TestSimpleTaskData(String message) {
    this.message = message;
    this.updateMessage = false;
  }

  /**
   * Constructs a new <b>TestSimpleTaskData</b>.
   *
   * @param message the message
   * @param updateMessage <b>true</b> if the message should be updated or <b>false</b> otherwise
   */
  public TestSimpleTaskData(String message, boolean updateMessage) {
    this.message = message;
    this.updateMessage = updateMessage;
  }

  /**
   * Returns whether the task should be delayed.
   *
   * @return <b>true</b> if the task should be delayed or <b>false</b> otherwise
   */
  public boolean getDelayTask() {
    return delayTask;
  }

  /**
   * Returns whether the task should be failed.
   *
   * @return <b>true</b> if the task should be failed or <b>false</b> otherwise
   */
  public boolean getFailTask() {
    return failTask;
  }

  /**
   * Returns the message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns whether the task should be retried.
   *
   * @return <b>true</b> if the task should be retried or <b>false</b> otherwise
   */
  public boolean getRetryTask() {
    return retryTask;
  }

  /**
   * Returns whether the message should be updated.
   *
   * @return <b>true</b> if the message should be updated or <b>false</b> otherwise
   */
  public boolean getUpdateMessage() {
    return updateMessage;
  }

  /**
   * Set whether the task should be delayed.
   *
   * @param delayTask <b>true</b> if the task should be delayed or <b>false</b> otherwise
   */
  public void setDelayTask(boolean delayTask) {
    this.delayTask = delayTask;
  }

  /**
   * Set whether the task should be failed.
   *
   * @param failTask <b>true</b> if the task should be failed or <b>false</b> otherwise
   */
  public void setFailTask(boolean failTask) {
    this.failTask = failTask;
  }

  /**
   * Set the message.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Set whether the task should be retried.
   *
   * @param retryTask <b>true</b> if the task should be retried or <b>false</b> otherwise
   */
  public void setRetryTask(boolean retryTask) {
    this.retryTask = retryTask;
  }

  /**
   * Set whether the message should be updated.
   *
   * @param updateMessage <b>true</b> if the message should be updated or <b>false</b> otherwise
   */
  public void setUpdateMessage(boolean updateMessage) {
    this.updateMessage = updateMessage;
  }
}
