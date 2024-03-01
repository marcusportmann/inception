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

package digital.inception.scheduler;

import digital.inception.api.ApiUtil;
import digital.inception.api.SecureApiController;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>SchedulerApiController</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused", "WeakerAccess"})
public class SchedulerApiController extends SecureApiController implements ISchedulerApiController {

  /** The Scheduler Service. */
  private final ISchedulerService schedulerService;

  /**
   * Constructs a new <b>SchedulerApiController</b>.
   *
   * @param applicationContext the Spring application context
   * @param schedulerService the Scheduler Service
   */
  public SchedulerApiController(
      ApplicationContext applicationContext, ISchedulerService schedulerService) {
    super(applicationContext);

    this.schedulerService = schedulerService;
  }

  @Override
  public void createJob(Job job)
      throws InvalidArgumentException, DuplicateJobException, ServiceUnavailableException {
    schedulerService.createJob(job);
  }

  @Override
  public void deleteJob(String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    schedulerService.deleteJob(jobId);
  }

  @Override
  public Job getJob(String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    return schedulerService.getJob(jobId);
  }

  @Override
  public String getJobName(String jobId)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(schedulerService.getJobName(jobId));
  }

  @Override
  public List<Job> getJobs() throws ServiceUnavailableException {
    return schedulerService.getJobs();
  }

  @Override
  public void updateJob(String jobId, Job job)
      throws InvalidArgumentException, JobNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(jobId)) {
      throw new InvalidArgumentException("jobId");
    }

    if (job == null) {
      throw new InvalidArgumentException("job");
    }

    if (!jobId.equals(job.getId())) {
      throw new InvalidArgumentException("job");
    }

    schedulerService.updateJob(job);
  }
}
