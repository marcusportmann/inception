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
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.TenantUtil;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.InvalidWorkflowStatusException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.exception.WorkflowStepNotFoundException;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.StartWorkflowRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.service.WorkflowService;
import java.util.List;
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
  public void createWorkflowDefinition(
      String workflowDefinitionCategoryId, WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionVersionException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
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
      WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionCategoryException,
          ServiceUnavailableException {
    workflowService.createWorkflowDefinitionCategory(workflowDefinitionCategory);
  }

  @Override
  public void createWorkflowEngine(WorkflowEngine workflowEngine)
      throws InvalidArgumentException,
          DuplicateWorkflowEngineException,
          ServiceUnavailableException {
    workflowService.createWorkflowEngine(workflowEngine);
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
  public void deleteWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    workflowService.deleteWorkflow(tenantId, workflowId);
  }

  @Override
  public void deleteWorkflowDefinition(
      String workflowDefinitionCategoryId, String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    if (workflowDefinitionCategoryId == null) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    try {
      if (!workflowService.workflowDefinitionCategoryExists(workflowDefinitionCategoryId)) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategoryId);
      }

      if (!workflowService.workflowDefinitionExists(
          workflowDefinitionCategoryId, workflowDefinitionId)) {
        throw new WorkflowDefinitionNotFoundException(workflowDefinitionId);
      }
    } catch (WorkflowDefinitionCategoryNotFoundException | WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow definition ("
              + workflowDefinitionId
              + ") for the workflow definition category ("
              + workflowDefinitionCategoryId
              + ")",
          e);
    }

    workflowService.deleteWorkflowDefinition(workflowDefinitionId);
  }

  @Override
  public void deleteWorkflowDefinitionCategory(String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    workflowService.deleteWorkflowDefinitionCategory(workflowDefinitionCategoryId);
  }

  @Override
  public void deleteWorkflowDefinitionVersion(
      String workflowDefinitionCategoryId,
      String workflowDefinitionId,
      int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    try {
      if (!workflowService.workflowDefinitionCategoryExists(workflowDefinitionCategoryId)) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategoryId);
      }

      if (!workflowService.workflowDefinitionExists(
          workflowDefinitionCategoryId, workflowDefinitionId)) {
        throw new WorkflowDefinitionNotFoundException(workflowDefinitionId);
      }
    } catch (WorkflowDefinitionCategoryNotFoundException | WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the version ("
              + workflowDefinitionVersion
              + ") of the workflow definition ("
              + workflowDefinitionId
              + ") under the workflow definition category ("
              + workflowDefinitionCategoryId
              + ")",
          e);
    }

    workflowService.deleteWorkflowDefinitionVersion(
        workflowDefinitionId, workflowDefinitionVersion);
  }

  @Override
  public void deleteWorkflowDocument(UUID tenantId, UUID workflowId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      if (!workflowService.workflowDocumentExists(tenantId, workflowId, workflowDocumentId)) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      }
    } catch (WorkflowNotFoundException | WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow document ("
              + workflowDocumentId
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.deleteWorkflowDocument(tenantId, workflowDocumentId);
  }

  @Override
  public void deleteWorkflowEngine(String workflowEngineId)
      throws InvalidArgumentException,
          WorkflowEngineNotFoundException,
          ServiceUnavailableException {
    workflowService.deleteWorkflowEngine(workflowEngineId);
  }

  @Override
  public void deleteWorkflowNote(UUID tenantId, UUID workflowId, UUID workflowNoteId)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowNoteNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    try {
      if (!workflowService.workflowExists(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      if (!workflowService.workflowNoteExists(tenantId, workflowId, workflowNoteId)) {
        throw new WorkflowNoteNotFoundException(tenantId, workflowId);
      }
    } catch (WorkflowNotFoundException | WorkflowNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow note ("
              + workflowNoteId
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.deleteWorkflowNote(tenantId, workflowNoteId);
  }

  @Override
  public void finalizeWorkflow(UUID tenantId, FinalizeWorkflowRequest finalizeWorkflowRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, finalizeWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, finalizeWorkflowRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to finalize the workflow ("
              + finalizeWorkflowRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.finalizeWorkflow(tenantId, finalizeWorkflowRequest, getAuthenticationName());
  }

  @Override
  public void finalizeWorkflowStep(
      UUID tenantId, FinalizeWorkflowStepRequest finalizeWorkflowStepRequest)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowStepNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, finalizeWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, finalizeWorkflowStepRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to finalize the workflow step ("
              + finalizeWorkflowStepRequest.getStep()
              + ") for the workflow ("
              + finalizeWorkflowStepRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.finalizeWorkflowStep(tenantId, finalizeWorkflowStepRequest);
  }

  @Override
  public List<OutstandingWorkflowDocument> getOutstandingWorkflowDocuments(
      UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.getOutstandingWorkflowDocuments(tenantId, workflowId);
  }

  @Override
  public Workflow getWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.getWorkflow(tenantId, workflowId);
  }

  @Override
  public WorkflowDefinition getWorkflowDefinition(
      String workflowDefinitionCategoryId, String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    try {
      if (!workflowService.workflowDefinitionCategoryExists(workflowDefinitionCategoryId)) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategoryId);
      }

      if (!workflowService.workflowDefinitionExists(
          workflowDefinitionCategoryId, workflowDefinitionId)) {
        throw new WorkflowDefinitionNotFoundException(workflowDefinitionId);
      }
    } catch (WorkflowDefinitionCategoryNotFoundException | WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow definition ("
              + workflowDefinitionId
              + ") under the workflow definition category ("
              + workflowDefinitionCategoryId
              + ")",
          e);
    }

    return workflowService.getWorkflowDefinition(workflowDefinitionId);
  }

  @Override
  public List<WorkflowDefinitionCategory> getWorkflowDefinitionCategories(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.getWorkflowDefinitionCategories(tenantId);
  }

  @Override
  public WorkflowDefinitionCategory getWorkflowDefinitionCategory(
      String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    return workflowService.getWorkflowDefinitionCategory(workflowDefinitionCategoryId);
  }

  @Override
  public List<WorkflowDefinitionSummary> getWorkflowDefinitionSummaries(
      UUID tenantId, String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.getWorkflowDefinitionSummaries(tenantId, workflowDefinitionCategoryId);
  }

  @Override
  public WorkflowDefinition getWorkflowDefinitionVersion(
      String workflowDefinitionCategoryId,
      String workflowDefinitionId,
      int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    try {
      if (!workflowService.workflowDefinitionCategoryExists(workflowDefinitionCategoryId)) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategoryId);
      }

      if (!workflowService.workflowDefinitionExists(
          workflowDefinitionCategoryId, workflowDefinitionId)) {
        throw new WorkflowDefinitionNotFoundException(workflowDefinitionId);
      }
    } catch (WorkflowDefinitionCategoryNotFoundException | WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the version ("
              + workflowDefinitionVersion
              + ") of the workflow definition ("
              + workflowDefinitionId
              + ") under the workflow definition category ("
              + workflowDefinitionCategoryId
              + ")",
          e);
    }

    return workflowService.getWorkflowDefinitionVersion(
        workflowDefinitionId, workflowDefinitionVersion);
  }

  @Override
  public WorkflowDocument getWorkflowDocument(
      UUID tenantId, UUID workflowId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      if (!workflowService.workflowDocumentExists(tenantId, workflowId, workflowDocumentId)) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      }
    } catch (WorkflowNotFoundException | WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow document ("
              + workflowDocumentId
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    return workflowService.getWorkflowDocument(tenantId, workflowDocumentId);
  }

  @Override
  public WorkflowDocuments getWorkflowDocuments(
      UUID tenantId,
      UUID workflowId,
      String filter,
      WorkflowDocumentSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.getWorkflowDocuments(
        tenantId, workflowId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public WorkflowEngine getWorkflowEngine(String workflowEngineId)
      throws InvalidArgumentException,
          WorkflowEngineNotFoundException,
          ServiceUnavailableException {
    return workflowService.getWorkflowEngine(workflowEngineId);
  }

  @Override
  public List<WorkflowEngine> getWorkflowEngines()
      throws InvalidArgumentException, ServiceUnavailableException {

    return workflowService.getWorkflowEngines();
  }

  @Override
  public WorkflowNote getWorkflowNote(UUID tenantId, UUID workflowId, UUID workflowNoteId)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowNoteNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      if (!workflowService.workflowNoteExists(tenantId, workflowId, workflowNoteId)) {
        throw new WorkflowNoteNotFoundException(tenantId, workflowNoteId);
      }
    } catch (WorkflowNotFoundException | WorkflowNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow note ("
              + workflowNoteId
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    return workflowService.getWorkflowNote(tenantId, workflowNoteId);
  }

  @Override
  public WorkflowNotes getWorkflowNotes(
      UUID tenantId,
      UUID workflowId,
      String filter,
      WorkflowNoteSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.getWorkflowNotes(
        tenantId, workflowId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public WorkflowSummaries getWorkflowSummaries(
      UUID tenantId,
      String definitionId,
      WorkflowStatus status,
      String filter,
      WorkflowSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.getWorkflowSummaries(
        tenantId, definitionId, status, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public UUID initiateWorkflow(UUID tenantId, InitiateWorkflowRequest initiateWorkflowRequest)
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
        workflowService.initiateWorkflow(
            tenantId, initiateWorkflowRequest, getAuthenticationName());

    return workflow.getId();
  }

  @Override
  public void startWorkflow(UUID tenantId, StartWorkflowRequest startWorkflowRequest)
      throws InvalidArgumentException,
      InvalidWorkflowStatusException,
      WorkflowNotFoundException,
      ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    workflowService.startWorkflow(tenantId, startWorkflowRequest, getAuthenticationName());
  }


  @Override
  public void initiateWorkflowStep(
      UUID tenantId, InitiateWorkflowStepRequest initiateWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, initiateWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, initiateWorkflowStepRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to initiate the workflow step ("
              + initiateWorkflowStepRequest.getStep()
              + ") for the workflow ("
              + initiateWorkflowStepRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    WorkflowStep workflowStep =
        workflowService.initiateWorkflowStep(tenantId, initiateWorkflowStepRequest);
  }

  @Override
  public void provideWorkflowDocument(
      UUID tenantId, ProvideWorkflowDocumentRequest provideWorkflowDocumentRequest)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    workflowService.provideWorkflowDocument(
        tenantId, provideWorkflowDocumentRequest, getAuthenticationName());
  }

  @Override
  public void rejectWorkflowDocument(
      UUID tenantId, RejectWorkflowDocumentRequest rejectWorkflowDocumentRequest)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    workflowService.rejectWorkflowDocument(
        tenantId, rejectWorkflowDocumentRequest, getAuthenticationName());
  }

  @Override
  public void requestWorkflowDocument(
      UUID tenantId, RequestWorkflowDocumentRequest requestWorkflowDocumentRequest)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    workflowService.requestWorkflowDocument(
        tenantId, requestWorkflowDocumentRequest, getAuthenticationName());
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
  public void updateWorkflowDefinition(
      String workflowDefinitionCategoryId,
      String workflowDefinitionId,
      Integer workflowDefinitionVersion,
      WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionCategoryId)) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    if (workflowDefinitionVersion == null) {
      throw new InvalidArgumentException("workflowDefinitionVersion");
    }

    if (!Objects.equals(workflowDefinitionCategoryId, workflowDefinition.getCategoryId())) {
      throw new InvalidArgumentException("workflowDefinition.categoryId");
    }

    if (!Objects.equals(workflowDefinitionId, workflowDefinition.getId())) {
      throw new InvalidArgumentException("workflowDefinition.id");
    }

    if (workflowDefinitionVersion != workflowDefinition.getVersion()) {
      throw new InvalidArgumentException("workflowDefinition.version");
    }

    workflowService.updateWorkflowDefinition(workflowDefinition);
  }

  @Override
  public void updateWorkflowDefinitionCategory(
      String workflowDefinitionCategoryId, WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionCategoryId)) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    if (!Objects.equals(workflowDefinitionCategoryId, workflowDefinitionCategory.getId())) {
      throw new InvalidArgumentException("workflowDefinitionCategory.id");
    }

    workflowService.updateWorkflowDefinitionCategory(workflowDefinitionCategory);
  }

  @Override
  public void updateWorkflowEngine(String workflowEngineId, WorkflowEngine workflowEngine)
      throws InvalidArgumentException,
          WorkflowEngineNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowEngineId)) {
      throw new InvalidArgumentException("workflowEngineId");
    }

    if (!Objects.equals(workflowEngineId, workflowEngine.getId())) {
      throw new InvalidArgumentException("workflowEngine.id");
    }

    workflowService.updateWorkflowEngine(workflowEngine);
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

  @Override
  public void verifyWorkflowDocument(
      UUID tenantId, VerifyWorkflowDocumentRequest verifyWorkflowDocumentRequest)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    workflowService.verifyWorkflowDocument(
        tenantId, verifyWorkflowDocumentRequest, getAuthenticationName());
  }
}
