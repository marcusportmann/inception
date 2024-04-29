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
 * The <b>TestMultistepTaskData</b> class holds the data for the test multistep task.
 *
 * @author Marcus Portmann
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"message", "startTimestamp", "finishTimestamp", "failTask", "retryTask"})
@SuppressWarnings({"unused", "WeakerAccess"})
public class TestMultistepTaskData implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** Should the task be failed. */
  @JsonProperty(required = true)
  private boolean failTask;

  /** The finish timestamp. */
  @JsonProperty(required = true)
  private long finishTimestamp;

  /** The message. */
  @JsonProperty(required = true)
  private String message;

  /** Should the task be retried. */
  @JsonProperty(required = true)
  private boolean retryTask;

  /** The start timestamp. */
  @JsonProperty(required = true)
  private long startTimestamp;

  /** Constructs a new <b>TestMultistepTaskData</b>. */
  public TestMultistepTaskData() {
    this.startTimestamp = System.currentTimeMillis();
  }

  /**
   * Constructs a new <b>TestMultistepTaskData</b>.
   *
   * @param message the message
   */
  public TestMultistepTaskData(String message) {
    this.message = message;
    this.startTimestamp = System.currentTimeMillis();
  }

  /**
   * Returns whether the task should be failed.
   *
   * @return <b>true</b> if the task should be failed or false otherwise
   */
  public boolean getFailTask() {
    return failTask;
  }

  /**
   * Returns the finish timestamp.
   *
   * @return the finish timestamp
   */
  public long getFinishTimestamp() {
    return finishTimestamp;
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
   * Returns the start timestamp.
   *
   * @return the start timestamp
   */
  public long getStartTimestamp() {
    return startTimestamp;
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
   * Set the finish timestamp.
   *
   * @param finishTimestamp the finish timestamp
   */
  public void setFinishTimestamp(long finishTimestamp) {
    this.finishTimestamp = finishTimestamp;
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
   * Set the start timestamp.
   *
   * @param startTimestamp the start timestamp
   */
  public void setStartTimestamp(long startTimestamp) {
    this.startTimestamp = startTimestamp;
  }
}
