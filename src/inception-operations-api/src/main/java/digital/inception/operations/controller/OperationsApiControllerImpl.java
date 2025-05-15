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
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinitionNotFoundException;
import digital.inception.operations.model.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionNotFoundException;
import digital.inception.operations.service.DocumentService;
import digital.inception.operations.service.WorkflowService;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code OperationsApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class OperationsApiControllerImpl extends SecureApiController
    implements OperationsApiController {

  private final DocumentService documentService;

  /** The Workflow Service. */
  private final WorkflowService workflowService;

  /**
   * Constructs a new {@code OperationsApiControllerImpl}.
   *
   * @param applicationContext the Spring application context
   * @param workflowService the Workflow Service
   */
  public OperationsApiControllerImpl(
      ApplicationContext applicationContext,
      WorkflowService workflowService,
      DocumentService documentService) {
    super(applicationContext);

    this.workflowService = workflowService;
    this.documentService = documentService;
  }

  @Override
  public UUID createDocument(UUID tenantId, CreateDocumentRequest createDocumentRequest)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? DEFAULT_TENANT_ID : tenantId;

    if  ((!hasAccessToFunction("Operations.OperationsAdministration")) &&   (!hasAccessToTenant(createDocumentRequest.getTenantId()))) {
      throw new AccessDeniedException(
          "Access denied to the tenant (" + createDocumentRequest.getTenantId() + ")");
    }

    Document document =
        documentService.createDocument(tenantId, createDocumentRequest, getAuthenticationName());

    return document.getId();
  }

  @Override
  public UUID createWorkflow(UUID tenantId, CreateWorkflowRequest createWorkflowRequest)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? DEFAULT_TENANT_ID : tenantId;

    if  ((!hasAccessToFunction("Operations.OperationsAdministration")) &&   (!hasAccessToTenant(createWorkflowRequest.getTenantId()))) {
      throw new AccessDeniedException(
          "Access denied to the tenant (" + createWorkflowRequest.getTenantId() + ")");
    }

    Workflow workflow =
        workflowService.createWorkflow(tenantId, createWorkflowRequest, getAuthenticationName());

    return workflow.getId();
  }

  @Override
  public void createWorkflowDefinitionCategory(
      UUID tenantId,
      WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionCategoryException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getWorkflowDefinitionCategories() method where
     *       we want to retrieve the "global" and "tenant specific" workflow definition categories.
     *       The ability to create or update workflow definition categories is an administrative
     *       function and is not assigned to a user for a particular tenant.
     */

    workflowService.createWorkflowDefinitionCategory(tenantId, workflowDefinitionCategory);
  }
}
