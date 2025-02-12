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

package digital.inception.operations.controller;

import digital.inception.api.SecureApiController;
import digital.inception.operations.service.IOperationsService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>OperationsApiControllerImpl</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class OperationsApiControllerImpl extends SecureApiController
    implements OperationsApiController {

  /** The Operations Service. */
  private final IOperationsService operationsService;

  /**
   * Constructs a new <b>OperationsApiControllerImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param operationsService the Operations Service
   */
  public OperationsApiControllerImpl(
      ApplicationContext applicationContext, IOperationsService operationsService) {
    super(applicationContext);

    this.operationsService = operationsService;
  }
}
