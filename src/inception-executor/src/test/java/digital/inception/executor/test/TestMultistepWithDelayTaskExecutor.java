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
import digital.inception.executor.model.MultistepTaskExecutor;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskExecutionDelayedException;
import digital.inception.executor.model.TaskExecutionFailedException;
import digital.inception.executor.model.TaskExecutionRetryableException;
import digital.inception.executor.model.TaskStep;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <b>TestMultistepWithDelayTaskExecutor</b> class.
 *
 * @author Marcus Portmann
 */
public class TestMultistepWithDelayTaskExecutor
    extends MultistepTaskExecutor<TestMultistepTaskData> {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(TestMultistepWithDelayTaskExecutor.class);

  /**
   * Constructs a new <b>TestMultistepWithDelayTaskExecutor</b>
   *
   * @param objectMapper the Jackson ObjectMapper instance
   */
  public TestMultistepWithDelayTaskExecutor(ObjectMapper objectMapper) {
    super(
        objectMapper,
        TestMultistepTaskData.class,
        List.of(new TaskStep("step_1", "Step 1", 5 * 60 * 1000), new TaskStep("step_2", "Step 2")));
  }

  @Override
  public boolean executeTaskStep(Task task, TaskStep taskStep, TestMultistepTaskData taskData)
      throws TaskExecutionFailedException,
          TaskExecutionRetryableException,
          TaskExecutionDelayedException {
    log.info(
        "Executing the step ("
            + taskStep.getName()
            + ") for the task ("
            + task.getId()
            + ") with type ("
            + task.getType()
            + ")");

    return false;
  }
}
