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

package digital.inception.executor.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The <b>BackgroundTaskResetter</b> class implements the Background Task Resetter, which resets
 * "hung" tasks that have been locked and executing longer than a global or task-type-specific
 * timeout.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class BackgroundTaskResetter {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(BackgroundTaskResetter.class);

  /** The Executor Service. */
  private final IExecutorService executorService;

  /**
   * Constructs a new <b>BackgroundTaskResetter</b>.
   *
   * @param executorService the Executor Service
   */
  public BackgroundTaskResetter(IExecutorService executorService) {
    this.executorService = executorService;
  }

  /**
   * Reset "hung" tasks, which have been locked and executing longer than a global or
   * task-type-specific timeout.
   */
  @Scheduled(cron = "0 0/10 * * * ?")
  public void resetHungTasks() {
    try {
      executorService.resetHungTasks();
    } catch (Throwable e) {
      logger.error("Failed to reset the hung tasks", e);
    }
  }

  /** Initialize the Background Task Resetter. */
  @PostConstruct
  public void init() {
    logger.info("Initializing the Background Task Resetter");
  }
}
