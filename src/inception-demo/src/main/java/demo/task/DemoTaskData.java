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

package demo.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code DemoTaskData} class holds the data for the demo task.
 *
 * @author Marcus Portmann
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"message", "failTask", "retryTask", "slowTask"})
@SuppressWarnings({"unused", "WeakerAccess"})
public class DemoTaskData implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** Should the task be failed. */
  @JsonProperty(required = true)
  private boolean failTask;

  /** The message. */
  @JsonProperty(required = true)
  private String message;

  /** Should the task be retried. */
  @JsonProperty(required = true)
  private boolean retryTask;

  /** Should the slow processing of the task be simulated. */
  @JsonProperty(required = true)
  private boolean slowTask;

  /** Creates a new {@code DemoTaskData} instance. */
  public DemoTaskData() {}

  /**
   * Creates a new {@code DemoTaskData} instance.
   *
   * @param message the message
   */
  public DemoTaskData(String message) {
    this.message = message;
  }

  /**
   * Returns whether the task should be failed.
   *
   * @return {@code true} if the task should be failed or {@code false} otherwise
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
   * @return {@code true} if the task should be retried or {@code false} otherwise
   */
  public boolean getRetryTask() {
    return retryTask;
  }

  /**
   * Returns whether the slow processing of the task should be simulated.
   *
   * @return {@code true} if the slow processing of the task should be simulated or {@code false}
   *     otherwise
   */
  public boolean getSlowTask() {
    return slowTask;
  }

  /**
   * Set whether the task should be failed.
   *
   * @param failTask {@code true} if the task should be failed or {@code false} otherwise
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
   * @param retryTask {@code true} if the task should be retried or {@code false} otherwise
   */
  public void setRetryTask(boolean retryTask) {
    this.retryTask = retryTask;
  }

  /**
   * Set whether the slow processing of the task should be simulated.
   *
   * @param slowTask {@code true} if the slow processing of the task should be simulated or {@code
   *     false} otherwise
   */
  public void setSlowTask(boolean slowTask) {
    this.slowTask = slowTask;
  }
}
