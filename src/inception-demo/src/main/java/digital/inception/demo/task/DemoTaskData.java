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

package digital.inception.demo.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serial;
import java.io.Serializable;

/**
 * The <b>DemoTaskData</b> class holds the data for the demo task.
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

  /** Constructs a new <b>DemoTaskData</b>. */
  public DemoTaskData() {}

  /**
   * Constructs a new <b>DemoTaskData</b>.
   *
   * @param message the message
   */
  public DemoTaskData(String message) {
    this.message = message;
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
   * Returns whether the slow processing of the task should be simulated.
   *
   * @return <b>true</b> if the slow processing of the task should be simulated or <b>false</b>
   *     otherwise
   */
  public boolean getSlowTask() {
    return slowTask;
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
   * Set whether the slow processing of the task should be simulated.
   *
   * @param slowTask <b>true</b> if the slow processing of the task should be simulated or
   *     <b>false</b> otherwise
   */
  public void setSlowTask(boolean slowTask) {
    this.slowTask = slowTask;
  }
}
