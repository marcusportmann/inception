/*
 * Copyright 2019 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.rs.SecureRestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//~--- JDK imports ------------------------------------------------------------

import javax.validation.Validator;

/**
 * The <code>SchedulerRestController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/api/scheduler")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SchedulerRestController extends SecureRestController
{
  /**
   * The Scheduler Service.
   */
  private ISchedulerService schedulerService;

  /**
   * The JSR-303 validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>SchedulerRestController</code>.
   *
   * @param schedulerService the Scheduler Service
   * @param validator        the JSR-303 validator
   */
  public SchedulerRestController(ISchedulerService schedulerService, Validator validator)
  {
    this.schedulerService = schedulerService;
    this.validator = validator;
  }
}
