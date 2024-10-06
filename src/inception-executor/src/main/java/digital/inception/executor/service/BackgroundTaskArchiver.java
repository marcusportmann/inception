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
 * The <b>BackgroundTaskArchiver</b> class implements the Background Task Archiver, which archives
 * and purges historical tasks.
 *
 * @author Marcus Portmann
 */

@Service
@SuppressWarnings("unused")
public class BackgroundTaskArchiver {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(BackgroundTaskArchiver.class);

  /** The Executor Service. */
  private final IExecutorService executorService;

  /**
   * Constructs a new <b>BackgroundTaskArchiver</b>.
   *
   * @param executorService the Executor Service
   */
  public BackgroundTaskArchiver(IExecutorService executorService) {
    this.executorService = executorService;
  }

  /** Archive and purge the historical tasks. */
  @Scheduled(cron = "0 0 22 * * ?")
  public void archiveAndPurgeTasks() {
    try {
      executorService.archiveAndDeleteHistoricalTasks();
    } catch (Throwable e) {
      log.error("Failed to archive and delete the historical tasks", e);
    }
  }

  /** Initialize the Background Task Archiver. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Background Task Archiver");
  }
}
