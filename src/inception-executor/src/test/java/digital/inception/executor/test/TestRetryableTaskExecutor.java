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

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.executor.model.SimpleTaskExecutor;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskExecutionDelayedException;
import digital.inception.executor.model.TaskExecutionFailedException;
import digital.inception.executor.model.TaskExecutionRetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <b>TestRetryableTaskExecutor</b> class.
 *
 * @author Marcus Portmann
 */
public class TestRetryableTaskExecutor extends SimpleTaskExecutor<TestRetryableTaskData> {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TestRetryableTaskExecutor.class);

  /**
   * Constructs a new <b>TestRetryableTaskExecutor</b>
   *
   * @param objectMapper the Jackson ObjectMapper instance
   */
  public TestRetryableTaskExecutor(ObjectMapper objectMapper) {
    super(objectMapper, TestRetryableTaskData.class);
  }

  @Override
  public boolean executeTask(Task task, TestRetryableTaskData taskData)
      throws TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException {
    logger.info(
        "Executing the retryable task (" + task.getId() + ") with type (" + task.getType() + ")");

    if (task.getExecutionAttempts() <= 5) {
      // TODO: Throw TaskExecutionDelayedException if the number of execution attempts is 2

      throw new TaskExecutionRetryableException(
          task.getId(),
          "Simulating the temporary failure of a task with type (" + task.getType() + ")");
    }

    return false;
  }
}
