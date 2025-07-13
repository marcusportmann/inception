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
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.util.TenantUtil;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.service.WorkflowService;
import java.util.Objects;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code WorkflowApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class WorkflowApiControllerImpl extends SecureApiController
    implements WorkflowApiController {

  /** The Workflow Service. */
  private final WorkflowService workflowService;

  /**
   * Constructs a new {@code WorkflowApiControllerImpl}.
   *
   * @param applicationContext the Spring application context
   * @param workflowService the Workflow Service
   */
  public WorkflowApiControllerImpl(
      ApplicationContext applicationContext, WorkflowService workflowService) {
    super(applicationContext);

    this.workflowService = workflowService;
  }

  @Override
  public UUID createWorkflow(UUID tenantId, CreateWorkflowRequest createWorkflowRequest)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    Workflow workflow =
        workflowService.createWorkflow(tenantId, createWorkflowRequest, getAuthenticationName());

    return workflow.getId();
  }

  @Override
  public void createWorkflowDefinition(
      UUID tenantId, String workflowDefinitionCategoryId, WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionVersionException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getWorkflowDefinitions() method where
     *       we want to retrieve the "global" and "tenant-specific" workflow definitions.
     *       The ability to create or update workflow definitions is an administrative function and
     *       is not assigned to a user for a particular tenant.
     */

    if (!StringUtils.hasText(workflowDefinitionCategoryId)) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    if (!Objects.equals(workflowDefinitionCategoryId, workflowDefinition.getCategoryId())) {
      throw new InvalidArgumentException("workflowDefinition.categoryId");
    }

    workflowService.createWorkflowDefinition(workflowDefinition);
  }

  @Override
  public void createWorkflowDefinitionCategory(
      UUID tenantId, WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionCategoryException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getWorkflowDefinitionCategories() method where
     *       we want to retrieve the "global" and "tenant-specific" workflow definition categories.
     *       The ability to create or update workflow definition categories is an administrative
     *       function and is not assigned to a user for a particular tenant.
     */

    workflowService.createWorkflowDefinitionCategory(workflowDefinitionCategory);
  }

  @Override
  public UUID createWorkflowNote(UUID tenantId, CreateWorkflowNoteRequest createWorkflowNoteRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    WorkflowNote workflowNote =
        workflowService.createWorkflowNote(
            tenantId, createWorkflowNoteRequest, getAuthenticationName());

    return workflowNote.getId();
  }

  @Override
  public void updateWorkflow(UUID tenantId, UpdateWorkflowRequest updateWorkflowRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    Workflow workflow =
        workflowService.updateWorkflow(tenantId, updateWorkflowRequest, getAuthenticationName());
  }

  @Override
  public void updateWorkflowNote(UUID tenantId, UpdateWorkflowNoteRequest updateWorkflowNoteRequest)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    WorkflowNote workflowNote =
        workflowService.updateWorkflowNote(
            tenantId, updateWorkflowNoteRequest, getAuthenticationName());
  }
}
