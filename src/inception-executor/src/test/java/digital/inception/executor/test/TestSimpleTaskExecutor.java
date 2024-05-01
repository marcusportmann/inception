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
 * The <b>TestSimpleTaskExecutor</b> class.
 *
 * @author Marcus Portmann
 */
public class TestSimpleTaskExecutor extends SimpleTaskExecutor<TestSimpleTaskData> {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TestSimpleTaskExecutor.class);

  /**
   * Constructs a new <b>SimpleTaskExecutor</b>
   *
   * @param objectMapper the Jackson ObjectMapper instance
   */
  public TestSimpleTaskExecutor(ObjectMapper objectMapper) {
    super(objectMapper, TestSimpleTaskData.class);
  }

  @Override
  public boolean executeTask(Task task, TestSimpleTaskData taskData)
      throws TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException {
    logger.info("Executing the task (" + task.getId() + ") with type (" + task.getType() + ")");

    if (taskData.getFailTask()) {
      throw new TaskExecutionFailedException(
          task.getId(), "Simulating the failure of a task with type (" + task.getType() + ")");
    }

    if (taskData.getRetryTask()) {
      throw new TaskExecutionRetryableException(
          task.getId(),
          "Simulating the temporary failure of a task with type (" + task.getType() + ")");
    }

    if (taskData.getDelayTask()) {
      throw new TaskExecutionDelayedException(task.getId(), 600000);
    }

    if (taskData.getUpdateMessage()) {
      taskData.setMessage("This is the updated message");
      return true;
    } else {
      return false;
    }
  }
}
