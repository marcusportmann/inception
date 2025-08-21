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

package digital.inception.operations.connector;

import digital.inception.operations.model.WorkflowEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * The {@code AbstractWorkflowEngineConnectorBase} class provides an abstract base class that
 * workflow engine connector implementations can extend to access common functionality.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused"})
public abstract class AbstractWorkflowEngineConnectorBase {

  /** The logger for the derived class. */
  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The workflow engine the workflow engine connector is associated with. */
  private final WorkflowEngine workflowEngine;

  /**
   * Constructs a {@code AbstractServiceBase}.
   *
   * @param applicationContext the Spring application context
   * @param workflowEngine the workflow engine the workflow engine connector is associated with
   */
  public AbstractWorkflowEngineConnectorBase(
      ApplicationContext applicationContext, WorkflowEngine workflowEngine) {
    this.applicationContext = applicationContext;
    this.workflowEngine = workflowEngine;
  }

  /**
   * Returns the Spring application context.
   *
   * @return the Spring application context
   */
  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * Returns the workflow engine the workflow engine connector is associated with.
   *
   * @return the workflow engine the workflow engine connector is associated with
   */
  public WorkflowEngine getWorkflowEngine() {
    return workflowEngine;
  }
}
