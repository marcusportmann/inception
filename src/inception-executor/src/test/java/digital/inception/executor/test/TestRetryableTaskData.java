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
 * The <b>TestRetryableTaskData</b> class holds the data for the test retryable task.
 *
 * @author Marcus Portmann
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"message"})
@SuppressWarnings({"unused", "WeakerAccess"})
public class TestRetryableTaskData implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The message. */
  @JsonProperty(required = true)
  private String message;

  /** Constructs a new <b>TestRetryableTaskData</b>. */
  public TestRetryableTaskData() {}

  /**
   * Constructs a new <b>TestRetryableTaskData</b>.
   *
   * @param message the message
   */
  public TestRetryableTaskData(String message) {
    this.message = message;
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
   * Set the message.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }
}
