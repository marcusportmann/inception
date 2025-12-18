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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.executor.exception.InvalidTaskStatusException;
import digital.inception.executor.exception.TaskExecutionFailedException;
import digital.inception.executor.exception.TaskNotFoundException;
import digital.inception.executor.exception.TaskTypeNotFoundException;
import digital.inception.executor.model.ArchivedTask;
import digital.inception.executor.model.QueueTaskRequest;
import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskEvent;
import digital.inception.executor.model.TaskEventType;
import digital.inception.executor.model.TaskPriority;
import digital.inception.executor.model.TaskSortBy;
import digital.inception.executor.model.TaskStatus;
import digital.inception.executor.model.TaskSummaries;
import digital.inception.executor.model.TaskSummary;
import digital.inception.executor.model.TaskType;
import digital.inception.executor.service.ExecutorService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The {@code ExecutorServiceTests} class contains the JUnit tests for the {@code ExecutorService}
 * class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ExecutorServiceTests {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(ExecutorServiceTests.class);

  /** The secure random number generator. */
  private static final SecureRandom secureRandom = new SecureRandom();

  /** The Executor Service. */
  @Autowired private ExecutorService executorService;

  /** The Jackson ObjectMapper. */
  @Autowired private ObjectMapper objectMapper;

  /** Test the cancel batch functionality. */
  @Test
  public void cancelBatchTest() throws Exception {
    String batchId = "batch_id_" + generateSuffix();

    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");
    testSimpleTaskData.setDelayTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(),
                batchId,
                null,
                objectMapper.writeValueAsString(testSimpleTaskData),
                false));

    waitForTaskToDelay(taskId);

    executorService.cancelBatch(batchId);

    // Confirm that cancelling a task is idempotent
    executorService.cancelBatch(batchId);

    Task retrievedTask = executorService.getTask(taskId);

    TestSimpleTaskData retrievedTestSimpleTaskData =
        deserializeTaskData(retrievedTask.getData(), TestSimpleTaskData.class);

    assertEquals(TaskStatus.CANCELED, retrievedTask.getStatus());
  }

  /** Test the cancel task functionality. */
  @Test
  public void cancelTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");
    testSimpleTaskData.setDelayTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(), objectMapper.writeValueAsString(testSimpleTaskData)));

    waitForTaskToDelay(taskId);

    Task retrievedTask = executorService.getTask(taskId);

    executorService.cancelTask(taskId);

    // Confirm that cancelling a task is idempotent
    executorService.cancelTask(taskId);

    retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.CANCELED, retrievedTask.getStatus());
  }

  /** Test the delayed task functionality. */
  @Test
  public void delayedTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");
    testSimpleTaskData.setDelayTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(), objectMapper.writeValueAsString(testSimpleTaskData)));

    assertTrue(executorService.isTaskWithTaskTypeQueuedOrExecuting(testSimpleTaskType.getCode()));

    waitForTaskToDelay(taskId);

    Task retrievedTask = executorService.getTask(taskId);

    assertEquals(taskId, retrievedTask.getId());
  }

  /** Test the get task summaries functionality. */
  @Test
  public void getTaskSummariesTest() throws Exception {
    String batchId = "batch_id_" + generateSuffix();
    String externalReference = "external_reference_" + generateSuffix();

    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(),
                batchId,
                externalReference,
                objectMapper.writeValueAsString(new TestSimpleTaskData("This is a test message")),
                false));

    waitForTaskToComplete(taskId, 20);

    // Find by batch ID
    TaskSummaries taskSummaries =
        executorService.getTaskSummaries(
            null, null, batchId, TaskSortBy.QUEUED, SortDirection.ASCENDING, 0, 10);

    assertEquals(1, taskSummaries.getTaskSummaries().size());

    // Find by external reference
    taskSummaries =
        executorService.getTaskSummaries(
            null, null, externalReference, TaskSortBy.QUEUED, SortDirection.ASCENDING, 0, 10);

    assertEquals(1, taskSummaries.getTaskSummaries().size());

    // Find by batch ID and type
    taskSummaries =
        executorService.getTaskSummaries(
            testSimpleTaskType.getCode(),
            null,
            batchId,
            TaskSortBy.QUEUED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, taskSummaries.getTaskSummaries().size());

    // Find by batch ID and status
    taskSummaries =
        executorService.getTaskSummaries(
            null, TaskStatus.COMPLETED, batchId, TaskSortBy.QUEUED, SortDirection.ASCENDING, 0, 10);

    assertEquals(1, taskSummaries.getTaskSummaries().size());

    // Find by batch ID, type and status
    taskSummaries =
        executorService.getTaskSummaries(
            testSimpleTaskType.getCode(),
            TaskStatus.COMPLETED,
            batchId,
            TaskSortBy.QUEUED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, taskSummaries.getTaskSummaries().size());

    // Find by type
    taskSummaries =
        executorService.getTaskSummaries(
            testSimpleTaskType.getCode(),
            null,
            null,
            TaskSortBy.QUEUED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, taskSummaries.getTaskSummaries().size());

    // Find by status
    taskSummaries =
        executorService.getTaskSummaries(
            null, TaskStatus.COMPLETED, null, TaskSortBy.QUEUED, SortDirection.ASCENDING, 0, 10);

    for (TaskSummary taskSummary : taskSummaries.getTaskSummaries()) {
      assertEquals(TaskStatus.COMPLETED, taskSummary.getStatus());
    }

    // Find by type and status
    taskSummaries =
        executorService.getTaskSummaries(
            testSimpleTaskType.getCode(),
            TaskStatus.COMPLETED,
            null,
            TaskSortBy.QUEUED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, taskSummaries.getTaskSummaries().size());

    // Invalid type
    taskSummaries =
        executorService.getTaskSummaries(
            "invalid_type", null, null, TaskSortBy.QUEUED, SortDirection.ASCENDING, 0, 10);

    assertEquals(0, taskSummaries.getTaskSummaries().size());

    // Invalid filter
    taskSummaries =
        executorService.getTaskSummaries(
            null, null, "invalid filter", TaskSortBy.QUEUED, SortDirection.ASCENDING, 0, 10);

    assertEquals(0, taskSummaries.getTaskSummaries().size());
  }

  /** Test the invalid task status functionality. */
  @Test
  public void invalidTaskStatusTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(), objectMapper.writeValueAsString(testSimpleTaskData)));

    waitForTaskToComplete(taskId, 20);

    assertThrows(InvalidTaskStatusException.class, () -> executorService.cancelTask(taskId));

    assertThrows(InvalidTaskStatusException.class, () -> executorService.suspendTask(taskId));

    assertThrows(InvalidTaskStatusException.class, () -> executorService.unsuspendTask(taskId));
  }

  /** Test the multistep task failure functionality. */
  @Test
  public void multistepTaskFailureTest() throws Exception {
    TaskType testMultistepTaskType =
        new TaskType(
            "test_multistep_" + generateSuffix(),
            "Test Multistep",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestMultistepTaskExecutor",
            1);

    executorService.createTaskType(testMultistepTaskType);

    TestMultistepTaskData testMultistepTaskData =
        new TestMultistepTaskData("This is a test message");
    testMultistepTaskData.setFailTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testMultistepTaskType.getCode(),
                objectMapper.writeValueAsString(testMultistepTaskData)));

    waitForTaskToFail(taskId);

    Task retrievedTask = executorService.getTask(taskId);

    TestMultistepTaskData retrievedTestMultistepTaskData =
        deserializeTaskData(retrievedTask.getData(), TestMultistepTaskData.class);

    assertEquals("This is a test message", retrievedTestMultistepTaskData.getMessage());

    assertEquals(TaskStatus.FAILED, retrievedTask.getStatus());
  }

  /** Multi-step task load test. */
  @Test
  @Disabled
  public void multistepTaskLoadTest() throws Exception {

    TaskType testMultistepTaskType =
        new TaskType(
            "test_multistep_" + generateSuffix(),
            "Test Multistep",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestMultistepTaskExecutor",
            10);

    executorService.createTaskType(testMultistepTaskType);

    List<UUID> taskIds = new ArrayList<>();

    for (int i = 0; i < 1000; i++) {
      TestMultistepTaskData testMultistepTaskData =
          new TestMultistepTaskData("This is test message " + randomId());

      testMultistepTaskData.setRetryTask(true);

      UUID taskId =
          executorService.queueTask(
              new QueueTaskRequest(
                  testMultistepTaskType.getCode(),
                  objectMapper.writeValueAsString(testMultistepTaskData)));

      taskIds.add(taskId);
    }

    for (UUID taskId : taskIds) {
      waitForTaskToComplete(taskId, 600);
    }
  }

  /** Test the multistep task retry functionality. */
  @Test
  public void multistepTaskRetryTest() throws Exception {
    TaskType testMultistepTaskType =
        new TaskType(
            "test_multistep_" + generateSuffix(),
            "Test Multistep",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestMultistepTaskExecutor",
            10);

    executorService.createTaskType(testMultistepTaskType);

    TestMultistepTaskData testMultistepTaskData =
        new TestMultistepTaskData("This is a test message");
    testMultistepTaskData.setRetryTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testMultistepTaskType.getCode(),
                objectMapper.writeValueAsString(testMultistepTaskData)));

    Thread.sleep(1000);

    waitForTaskToRetry(taskId);

    Task retrievedTask = executorService.getTask(taskId);

    TestMultistepTaskData retrievedTestMultistepTaskData =
        deserializeTaskData(retrievedTask.getData(), TestMultistepTaskData.class);

    assertEquals("This is a test message", retrievedTestMultistepTaskData.getMessage());

    assertEquals(TaskStatus.QUEUED, retrievedTask.getStatus());
    assertEquals(1, retrievedTask.getExecutionAttempts());
  }

  /** Test the multistep task functionality. */
  @Test
  public void multistepTaskTest() throws Exception {
    TaskType testMultistepTaskType =
        new TaskType(
            "test_multistep_" + generateSuffix(),
            "Test Multistep",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestMultistepTaskExecutor",
            1);

    executorService.createTaskType(testMultistepTaskType);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testMultistepTaskType.getCode(),
                objectMapper.writeValueAsString(
                    new TestMultistepTaskData("This message will be updated"))));

    waitForTaskToComplete(taskId, 20);

    Task retrievedTask = executorService.getTask(taskId);

    TestMultistepTaskData retrievedTestMultistepTaskData =
        deserializeTaskData(retrievedTask.getData(), TestMultistepTaskData.class);

    assertEquals("Message updated by step 3", retrievedTestMultistepTaskData.getMessage());

    log.info(
        "Time taken to complete multistep task is "
            + (retrievedTestMultistepTaskData.getFinishTimestamp()
                - retrievedTestMultistepTaskData.getStartTimestamp())
            + " milliseconds");
  }

  /** Test the multistep with delay task functionality. */
  @Test
  public void multistepWithDelayTaskTest() throws Exception {
    TaskType testMultistepWithDelayTaskType =
        new TaskType(
            "test_multistep_with_delay_" + generateSuffix(),
            "Test Multistep With Delay",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestMultistepWithDelayTaskExecutor",
            1);

    executorService.createTaskType(testMultistepWithDelayTaskType);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testMultistepWithDelayTaskType.getCode(),
                objectMapper.writeValueAsString(
                    new TestMultistepTaskData("This is a test message"))));

    waitForTaskToMoveToStep(taskId, "step_2", 10);

    Task retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.QUEUED, retrievedTask.getStatus());
    assertTrue(retrievedTask.getNextExecution().isAfter(OffsetDateTime.now().plusSeconds(120)));
  }

  /** Test the parameter-based queue task functionality. */
  @Test
  public void parameterBasedQueueTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    UUID taskId =
        executorService.queueTask(
            testSimpleTaskType.getCode(),
            "Batch ID",
            "External Reference",
            false,
            objectMapper.writeValueAsString(new TestMultistepTaskData("This is a test message")));

    waitForTaskToComplete(taskId, 20);
  }

  /** Test the process completed multistep task functionality. */
  @Test
  public void processCompletedMultistepTaskTest() throws Exception {
    TaskType testMultistepTaskType =
        new TaskType(
            "test_multistep_" + generateSuffix(),
            "Test Multistep",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestMultistepTaskExecutor",
            true,
            true,
            1);

    executorService.createTaskType(testMultistepTaskType);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testMultistepTaskType.getCode(),
                objectMapper.writeValueAsString(
                    new TestMultistepTaskData("This is a test message"))));

    waitForTaskToComplete(taskId, 20);

    Task retrievedTask = executorService.getTask(taskId);

    executorService.setHistoricalTaskRetentionDays(0);

    executorService.archiveAndDeleteHistoricalTasks();

    ArchivedTask archivedTask = executorService.getArchivedTask(taskId);

    assertEquals(retrievedTask.getId(), archivedTask.getId());
    assertEquals(retrievedTask.getType(), archivedTask.getType());
    assertEquals(retrievedTask.getStep(), archivedTask.getStep());
    assertEquals(retrievedTask.getStatus(), archivedTask.getStatus());
    assertEquals(retrievedTask.getQueued(), archivedTask.getQueued());
    assertEquals(retrievedTask.getExecuted(), archivedTask.getExecuted());
    assertEquals(retrievedTask.getData(), archivedTask.getData());
  }

  /** Test the process completed simple single-step task functionality. */
  @Test
  public void processCompletedSimpleTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(),
                objectMapper.writeValueAsString(new TestSimpleTaskData("This is a test message"))));

    waitForTaskToComplete(taskId, 20);

    executorService.setHistoricalTaskRetentionDays(0);

    executorService.archiveAndDeleteHistoricalTasks();

    assertThrows(TaskNotFoundException.class, () -> executorService.getTask(taskId));
  }

  /** Test the queue suspended task functionality. */
  @Test
  public void queueSuspendedTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    QueueTaskRequest queueTaskRequest =
        new QueueTaskRequest(
            testSimpleTaskType.getCode(),
            objectMapper.writeValueAsString(new TestSimpleTaskData("This is a test message")));

    queueTaskRequest.setSuspended(true);

    UUID taskId = executorService.queueTask(queueTaskRequest);

    Task retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.SUSPENDED, retrievedTask.getStatus());
  }

  /** Test the queue task functionality. */
  @Test
  public void queueTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    QueueTaskRequest queueTaskRequest =
        new QueueTaskRequest(
            testSimpleTaskType.getCode(),
            "Batch ID",
            "External Reference",
            objectMapper.writeValueAsString(new TestMultistepTaskData("This is a test message")),
            false);

    UUID taskId = executorService.queueTask(queueTaskRequest);

    waitForTaskToComplete(taskId, 20);

    Task retrievedTask = executorService.getTask(taskId);

    assertEquals(queueTaskRequest.getBatchId(), retrievedTask.getBatchId());
    assertEquals(queueTaskRequest.getExternalReference(), retrievedTask.getExternalReference());
    assertEquals(queueTaskRequest.getExternalReference(), retrievedTask.getExternalReference());
    assertEquals(queueTaskRequest.getData(), retrievedTask.getData());
  }

  /** Test the hung task reset functionality. */
  @Test
  public void resetHungTasksTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    testSimpleTaskType.setExecutionTimeout(5 * 60 * 1000);

    executorService.createTaskType(testSimpleTaskType);

    executorService.resetHungTasks();
  }

  /** Test the retryable task functionality. */
  @Test
  public void retryableTaskTest() throws Exception {
    TaskType retryableTaskType =
        new TaskType(
            "test_retryable_" + generateSuffix(),
            "Test Retryable",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestRetryableTaskExecutor",
            10,
            0);

    executorService.createTaskType(retryableTaskType);

    QueueTaskRequest queueTaskRequest =
        new QueueTaskRequest(
            retryableTaskType.getCode(),
            objectMapper.writeValueAsString(new TestSimpleTaskData("This is a test message")));

    UUID taskId = executorService.queueTask(queueTaskRequest);

    waitForTaskToComplete(taskId, 20);
  }

  /** Test the simple single-step task failure functionality. */
  @Test
  public void simpleTaskFailureTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");
    testSimpleTaskData.setFailTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(), objectMapper.writeValueAsString(testSimpleTaskData)));

    waitForTaskToFail(taskId);

    Task retrievedTask = executorService.getTask(taskId);

    TestSimpleTaskData retrievedTestSimpleTaskData =
        deserializeTaskData(retrievedTask.getData(), TestSimpleTaskData.class);

    assertEquals("This is a test message", retrievedTestSimpleTaskData.getMessage());

    assertEquals(TaskStatus.FAILED, retrievedTask.getStatus());
  }

  /** Test the simple single-step task retry functionality. */
  @Test
  public void simpleTaskRetryTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            10);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");
    testSimpleTaskData.setRetryTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(), objectMapper.writeValueAsString(testSimpleTaskData)));

    waitForTaskToRetry(taskId);

    Task retrievedTask = executorService.getTask(taskId);

    TestSimpleTaskData retrievedTestSimpleTaskData =
        deserializeTaskData(retrievedTask.getData(), TestSimpleTaskData.class);

    assertEquals("This is a test message", retrievedTestSimpleTaskData.getMessage());

    assertEquals(TaskStatus.QUEUED, retrievedTask.getStatus());
    assertEquals(1, retrievedTask.getExecutionAttempts());
  }

  /** Test the simple single-step task functionality. */
  @Test
  public void simpleTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(),
                objectMapper.writeValueAsString(
                    new TestSimpleTaskData("This message will not be updated"))));

    waitForTaskToComplete(taskId, 20);

    Task retrievedTask = executorService.getTask(taskId);

    TestSimpleTaskData retrievedTestSimpleTaskData =
        deserializeTaskData(retrievedTask.getData(), TestSimpleTaskData.class);

    assertEquals("This message will not be updated", retrievedTestSimpleTaskData.getMessage());

    taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(),
                objectMapper.writeValueAsString(
                    new TestSimpleTaskData("This message will be updated", true))));

    waitForTaskToComplete(taskId, 20);

    retrievedTask = executorService.getTask(taskId);

    retrievedTestSimpleTaskData =
        deserializeTaskData(retrievedTask.getData(), TestSimpleTaskData.class);

    assertEquals("This is the updated message", retrievedTestSimpleTaskData.getMessage());

    assertEquals(TaskStatus.COMPLETED, executorService.getTaskStatus(taskId));
  }

  /** Test the suspend batch functionality. */
  @Test
  public void suspendBatchTest() throws Exception {
    String batchId = "batch_id_" + generateSuffix();

    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");
    testSimpleTaskData.setDelayTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(),
                batchId,
                null,
                objectMapper.writeValueAsString(testSimpleTaskData),
                false));

    waitForTaskToDelay(taskId);

    executorService.suspendBatch(batchId);

    Task retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.SUSPENDED, retrievedTask.getStatus());
  }

  /** Test the suspend task functionality. */
  @Test
  public void suspendTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");
    testSimpleTaskData.setDelayTask(true);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(), objectMapper.writeValueAsString(testSimpleTaskData)));

    waitForTaskToDelay(taskId);

    Task retrievedTask = executorService.getTask(taskId);

    executorService.suspendTask(taskId);

    retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.SUSPENDED, retrievedTask.getStatus());
  }

  /** Test the task event functionality. */
  @Test
  public void taskEventTest() throws Exception {
    TaskType testMultistepTaskType =
        new TaskType(
            "test_multistep_" + generateSuffix(),
            "Test Multistep",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestMultistepTaskExecutor",
            1);

    testMultistepTaskType.setEventTypes(
        List.of(TaskEventType.STEP_COMPLETED, TaskEventType.TASK_COMPLETED));

    executorService.createTaskType(testMultistepTaskType);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testMultistepTaskType.getCode(),
                objectMapper.writeValueAsString(
                    new TestMultistepTaskData("This message will be updated"))));

    waitForTaskToComplete(taskId, 20);

    List<TaskEvent> taskEvents = executorService.getTaskEventsForTask(taskId);

    assertEquals(4, taskEvents.size());
  }

  /** Test the task event with data functionality. */
  @Test
  public void taskEventWithDataTest() throws Exception {
    TaskType testMultistepTaskType =
        new TaskType(
            "test_multistep_" + generateSuffix(),
            "Test Multistep",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestMultistepTaskExecutor",
            1);

    testMultistepTaskType.setEventTypesWithTaskData(
        List.of(TaskEventType.STEP_COMPLETED, TaskEventType.TASK_COMPLETED));

    executorService.createTaskType(testMultistepTaskType);

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testMultistepTaskType.getCode(),
                objectMapper.writeValueAsString(
                    new TestMultistepTaskData("This message will be updated"))));

    waitForTaskToComplete(taskId, 20);

    List<TaskEvent> taskEvents = executorService.getTaskEventsForTask(taskId);

    assertEquals(4, taskEvents.size());

    for (TaskEvent taskEvent : taskEvents) {
      assertNotNull(taskEvent.getTaskData());
    }

    assertEquals("step_3", taskEvents.get(2).getTaskStep());

    TestMultistepTaskData retrievedTestMultistepTaskData =
        deserializeTaskData(taskEvents.get(2).getTaskData(), TestMultistepTaskData.class);

    assertEquals("Message updated by step 3", retrievedTestMultistepTaskData.getMessage());
  }

  /** Test the task retrieval functionality. */
  @Test
  public void taskRetrievalTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    String batchId = "batch_id_" + generateSuffix();

    String externalReference = "external_reference_" + generateSuffix();

    QueueTaskRequest queueTaskRequest =
        new QueueTaskRequest(
            testSimpleTaskType.getCode(),
            batchId,
            externalReference,
            objectMapper.writeValueAsString(new TestSimpleTaskData("This is a test message")),
            false);

    UUID taskId = executorService.queueTask(queueTaskRequest);

    waitForTaskToComplete(taskId, 20);

    Task retrievedTask = executorService.getTask(taskId);

    assertEquals(taskId, retrievedTask.getId());

    retrievedTask = executorService.getTaskByExternalReference(externalReference);

    assertEquals(taskId, retrievedTask.getId());
  }

  /** Test the task type functionality. */
  @Test
  public void taskTypeTest() throws Exception {
    TaskType taskType = new TaskType();

    taskType.setCode("test_task_" + generateSuffix());
    taskType.setName("Test Task Name");
    taskType.setPriority(TaskPriority.HIGH);
    taskType.setEnabled(false);
    taskType.setExecutorClass("digital.inception.executor.test.TestSimpleTaskExecutor");
    taskType.setArchiveCompleted(true);
    taskType.setArchiveFailed(true);
    taskType.setMaximumExecutionAttempts(7);
    taskType.setRetryDelay(777);
    taskType.setEventTypes(List.of(TaskEventType.TASK_COMPLETED, TaskEventType.TASK_FAILED));

    executorService.createTaskType(taskType);

    TaskType retreivedTaskType = executorService.getTaskType(taskType.getCode());

    assertEquals(taskType.getCode(), retreivedTaskType.getCode());
    assertEquals(taskType.getName(), retreivedTaskType.getName());
    assertEquals(taskType.getPriority(), retreivedTaskType.getPriority());
    assertEquals(taskType.getEnabled(), retreivedTaskType.getEnabled());
    assertEquals(taskType.getExecutorClass(), retreivedTaskType.getExecutorClass());
    assertEquals(taskType.getArchiveCompleted(), retreivedTaskType.getArchiveCompleted());
    assertEquals(taskType.getArchiveFailed(), retreivedTaskType.getArchiveFailed());
    assertEquals(
        taskType.getMaximumExecutionAttempts(), retreivedTaskType.getMaximumExecutionAttempts());
    assertEquals(taskType.getRetryDelay(), retreivedTaskType.getRetryDelay());
    assertEquals(taskType.getEventTypes().size(), retreivedTaskType.getEventTypes().size());
  }

  /** Test the task type validation functionality. */
  @Test
  public void taskTypeValidationTest() throws Exception {

    InvalidArgumentException invalidArgumentException =
        assertThrows(
            InvalidArgumentException.class, () -> executorService.createTaskType(new TaskType()));

    assertEquals(4, invalidArgumentException.getValidationErrors().size());

    TaskType invalidExecutorClassTaskType =
        new TaskType(
            "invalid_executor_class_task_type",
            "Invalid Executor Class Task Type",
            TaskPriority.NORMAL,
            "invalid.class.name");

    invalidArgumentException =
        assertThrows(
            InvalidArgumentException.class,
            () -> executorService.createTaskType(invalidExecutorClassTaskType));

    assertEquals(1, invalidArgumentException.getValidationErrors().size());
    assertEquals(
        "invalid executor class (invalid.class.name)",
        invalidArgumentException.getValidationErrors().getFirst().getMessage());
  }

  /** Test the task validation functionality. */
  @Test
  @SuppressWarnings("unused")
  public void taskValidationTest() throws Exception {
    InvalidArgumentException invalidArgumentException =
        assertThrows(
            InvalidArgumentException.class,
            () -> executorService.queueTask(new QueueTaskRequest()));

    assertEquals(2, invalidArgumentException.getValidationErrors().size());

    invalidArgumentException =
        assertThrows(InvalidArgumentException.class, () -> executorService.executeTask(new Task()));

    assertEquals(5, invalidArgumentException.getValidationErrors().size());

    TaskExecutionFailedException taskExecutionFailedException =
        assertThrows(
            TaskExecutionFailedException.class,
            () -> executorService.executeTask(new Task("invalid_task_type", "invalid task data")));

    TaskTypeNotFoundException taskTypeNotFoundException =
        assertThrows(
            TaskTypeNotFoundException.class,
            () ->
                executorService.queueTask(new QueueTaskRequest("invalid_task_type", "task_data")));

    assertEquals(
        "The task type (invalid_task_type) could not be found",
        taskTypeNotFoundException.getMessage());
  }

  /** Test the unsuspend batch functionality. */
  @Test
  public void unsuspendBatchTest() throws Exception {
    String batchId = "batch_id_" + generateSuffix();

    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(),
                batchId,
                null,
                objectMapper.writeValueAsString(testSimpleTaskData),
                true));

    Task retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.SUSPENDED, retrievedTask.getStatus());

    executorService.unsuspendTask(taskId);

    waitForTaskToComplete(taskId, 120);

    retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.COMPLETED, retrievedTask.getStatus());
  }

  /** Test the unsuspend task functionality. */
  @Test
  public void unsuspendTaskTest() throws Exception {
    TaskType testSimpleTaskType =
        new TaskType(
            "test_simple_" + generateSuffix(),
            "Test Simple",
            TaskPriority.NORMAL,
            "digital.inception.executor.test.TestSimpleTaskExecutor",
            1);

    executorService.createTaskType(testSimpleTaskType);

    TestSimpleTaskData testSimpleTaskData = new TestSimpleTaskData("This is a test message");

    UUID taskId =
        executorService.queueTask(
            new QueueTaskRequest(
                testSimpleTaskType.getCode(),
                null,
                null,
                objectMapper.writeValueAsString(testSimpleTaskData),
                true));

    Task retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.SUSPENDED, retrievedTask.getStatus());

    executorService.unsuspendTask(taskId);

    waitForTaskToComplete(taskId, 120);

    retrievedTask = executorService.getTask(taskId);

    assertEquals(TaskStatus.COMPLETED, retrievedTask.getStatus());
  }

  private static String generateSuffix() {
    StringBuilder suffix = new StringBuilder(6);
    Random random = new Random();

    for (int i = 0; i < 6; i++) {
      char randomChar = (char) ('a' + random.nextInt(26));
      suffix.append(randomChar);
    }

    return suffix.toString();
  }

  private <TaskDataType> TaskDataType deserializeTaskData(
      String json, Class<TaskDataType> taskDataTypeClass) throws JsonProcessingException {
    return objectMapper.readValue(json, taskDataTypeClass);
  }

  private String randomId() {
    return String.format("%04X", secureRandom.nextInt(0x10000));
  }

  private void waitForTaskToComplete(UUID taskId, int seconds) throws Exception {
    for (int i = 0; i < ((seconds * 1000) / 250); i++) {
      Task task = executorService.getTask(taskId);

      if (task.getStatus() == TaskStatus.COMPLETED) {
        return;
      }

      Thread.sleep(250);
    }

    throw new RuntimeException("Timed out waiting for the task (" + taskId + ") to complete");
  }

  private void waitForTaskToDelay(UUID taskId) throws Exception {
    for (int i = 0; i < 50; i++) {
      Task task = executorService.getTask(taskId);

      if ((task.getStatus() == TaskStatus.QUEUED) && (task.getNextExecution() != null)) {
        return;
      }

      Thread.sleep(250);
    }

    throw new RuntimeException("Timed out waiting for the task (" + taskId + ") to delay");
  }

  private void waitForTaskToFail(UUID taskId) throws Exception {
    for (int i = 0; i < 50; i++) {
      Task task = executorService.getTask(taskId);

      if (task.getStatus() == TaskStatus.FAILED) {
        return;
      }

      Thread.sleep(250);
    }

    throw new RuntimeException("Timed out waiting for the task (" + taskId + ") to fail");
  }

  private void waitForTaskToMoveToStep(UUID taskId, String step, int seconds) throws Exception {
    for (int i = 0; i < ((seconds * 1000) / 250); i++) {
      Task task = executorService.getTask(taskId);

      if (step.equals(task.getStep())) {
        return;
      }

      Thread.sleep(250);
    }

    throw new RuntimeException(
        "Timed out waiting for the task (" + taskId + ") to move to step (" + step + ")");
  }

  private void waitForTaskToRetry(UUID taskId) throws Exception {
    for (int i = 0; i < 50; i++) {
      Task task = executorService.getTask(taskId);

      if ((task.getStatus() == TaskStatus.QUEUED) && (task.getExecutionAttempts() == 1)) {
        return;
      }

      Thread.sleep(250);
    }

    throw new RuntimeException("Timed out waiting for the task (" + taskId + ") to retry");
  }
}
