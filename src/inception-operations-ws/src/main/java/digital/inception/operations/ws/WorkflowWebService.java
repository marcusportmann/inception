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

package digital.inception.operations.ws;

import digital.inception.operations.service.WorkflowService;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import org.springframework.context.ApplicationContext;

/**
 * The {@code WorkflowWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "WorkflowService",
    name = "IWorkflowService",
    targetNamespace = "https://inception.digital/operations")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class WorkflowWebService extends AbstractWebServiceBase {

  /** The Workflow Service. */
  private final WorkflowService workflowService;

  /**
   * Constructs a new {@code WorkflowWebService}.
   *
   * @param applicationContext the Spring application context
   * @param workflowService the Workflow Service
   */
  public WorkflowWebService(
      ApplicationContext applicationContext, WorkflowService workflowService) {
    super(applicationContext);

    this.workflowService = workflowService;
  }
}
