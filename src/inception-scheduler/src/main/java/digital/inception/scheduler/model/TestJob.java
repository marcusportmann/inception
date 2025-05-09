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

package digital.inception.scheduler.model;

import digital.inception.scheduler.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The {@code TestJob} class implements the test job.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "SpringJavaAutowiredMembersInspection"})
public class TestJob implements JobImplementation {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(TestJob.class);

  /* Scheduler Service */
  @Autowired private SchedulerService schedulerService;

  /** Constructs a new {@code TestJob}. */
  public TestJob() {}

  /**
   * Execute the job.
   *
   * @param context the job execution context
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionFailedException {
    try {
      log.info("Executing the test job (" + schedulerService + ")");
    } catch (Throwable e) {
      throw new JobExecutionFailedException("Failed to execute the test job", e);
    }
  }
}
