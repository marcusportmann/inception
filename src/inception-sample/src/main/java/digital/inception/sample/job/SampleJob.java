/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.sample.job;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.scheduler.IJob;
import digital.inception.scheduler.JobExecutionContext;
import digital.inception.scheduler.JobExecutionFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>SampleJob</code> class.
 *
 * @author Marcus Portmann
 */
public class SampleJob implements IJob {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleJob.class);

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionFailedException {
    logger.info("Sample job execution started");

    try {
      Thread.sleep(20000L);
    } catch (Throwable e) {
    }

    logger.info("Sample job execution finished");
  }
}
