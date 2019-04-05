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

import org.springframework.beans.factory.annotation.Autowired;

//~--- JDK imports ------------------------------------------------------------

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import javax.validation.Validator;

/**
 * The <code>SchedulerWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(serviceName = "SchedulerService", name = "ISchedulerService",
    targetNamespace = "http://scheduler.inception.digital")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SchedulerWebService
{
  /* Scheduler Service */
  @Autowired
  private ISchedulerService schedulerService;

  /* Validator */
  @Autowired
  private Validator validator;
}
