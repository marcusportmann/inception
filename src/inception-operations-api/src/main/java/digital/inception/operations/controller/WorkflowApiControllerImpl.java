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
import digital.inception.operations.exception.DuplicateWorkflowAttributeDefinitionException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.FormDefinitionNotFoundException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InvalidWorkflowStatusException;
import digital.inception.operations.exception.WorkflowAttributeDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowInteractionLinkNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.exception.WorkflowStepNotFoundException;
import digital.inception.operations.model.CancelWorkflowRequest;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.DeleteWorkflowStepRequest;
import digital.inception.operations.model.DelinkInteractionFromWorkflowRequest;
import digital.inception.operations.model.Event;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.FormDefinition;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.LinkInteractionToWorkflowRequest;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.SearchWorkflowsRequest;
import digital.inception.operations.model.StartWorkflowRequest;
import digital.inception.operations.model.SuspendWorkflowRequest;
import digital.inception.operations.model.SuspendWorkflowStepRequest;
import digital.inception.operations.model.UnsuspendWorkflowRequest;
import digital.inception.operations.model.UnsuspendWorkflowStepRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttributeDefinition;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDefinitionPermission;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowPermissionType;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.service.BackgroundWorkflowStatusVerifier;
import digital.inception.operations.service.WorkflowService;
import java.util.ArrayList;
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
  public void cancelWorkflow(UUID tenantId, CancelWorkflowRequest cancelWorkflowRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, cancelWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, cancelWorkflowRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to cancel the workflow ("
              + cancelWorkflowRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.cancelWorkflow(tenantId, cancelWorkflowRequest, getAuthenticationName());
  }

  @Override
  public void createWorkflowAttributeDefinition(
      WorkflowAttributeDefinition workflowAttributeDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowAttributeDefinitionException,
          ServiceUnavailableException {
    workflowService.createWorkflowAttributeDefinition(workflowAttributeDefinition);
  }

  @Override
  public void createWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionVersionException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowEngineNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
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
  public void deleteWorkflowAttributeDefinition(String workflowAttributeDefinitionCode)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException {
    workflowService.deleteWorkflowAttributeDefinition(workflowAttributeDefinitionCode);
  }

  @Override
  public void deleteWorkflowDefinition(String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
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
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
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
  public void deleteWorkflowStep(UUID tenantId, DeleteWorkflowStepRequest deleteWorkflowStepRequest)
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
      if (!workflowService.workflowExists(tenantId, deleteWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, deleteWorkflowStepRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow step ("
              + deleteWorkflowStepRequest.getStep()
              + ") for the workflow ("
              + deleteWorkflowStepRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.deleteWorkflowStep(tenantId, deleteWorkflowStepRequest);
  }

  @Override
  public void delinkInteractionFromWorkflow(
      UUID tenantId, DelinkInteractionFromWorkflowRequest delinkInteractionFromWorkflowRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          WorkflowNotFoundException,
          WorkflowInteractionLinkNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    workflowService.delinkInteractionFromWorkflow(tenantId, delinkInteractionFromWorkflowRequest);
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
  public FormDefinition getStartFormDefinitionForWorkflowDefinition(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          FormDefinitionNotFoundException,
          ServiceUnavailableException {
    return workflowService.getStartFormDefinitionForWorkflowDefinition(
        workflowDefinitionId, workflowDefinitionVersion);
  }

  @Override
  public FormDefinition getWorkFormDefinitionForWorkflowDefinition(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          FormDefinitionNotFoundException,
          ServiceUnavailableException {
    return workflowService.getWorkFormDefinitionForWorkflowDefinition(
        workflowDefinitionId, workflowDefinitionVersion);
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
  public WorkflowAttributeDefinition getWorkflowAttributeDefinition(
      String workflowAttributeDefinitionCode)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException {
    return workflowService.getWorkflowAttributeDefinition(workflowAttributeDefinitionCode);
  }

  @Override
  public List<WorkflowAttributeDefinition> getWorkflowAttributeDefinitions(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.getWorkflowAttributeDefinitions(tenantId);
  }

  @Override
  public WorkflowDefinition getWorkflowDefinition(String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
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
  public List<WorkflowDefinitionSummary>
      getWorkflowDefinitionSummariesForWorkflowDefinitionCategory(
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

    List<WorkflowDefinitionSummary> filteredWorkflowDefinitionSummaries = new ArrayList<>();

    for (WorkflowDefinitionSummary workflowDefinitionSummary :
        workflowService.getWorkflowDefinitionSummaries(tenantId, workflowDefinitionCategoryId)) {

      if ((hasAccessToFunction("Operations.OperationsAdministration"))
          || (hasAccessToFunction("Operations.WorkflowAdministration"))
          || (hasWorkflowDefinitionPermission(
              workflowDefinitionSummary.getId(),
              workflowDefinitionSummary.getVersion(),
              WorkflowPermissionType.INITIATE_WORKFLOW))) {

        filteredWorkflowDefinitionSummaries.add(workflowDefinitionSummary);
      }
    }

    return filteredWorkflowDefinitionSummaries;
  }

  @Override
  public WorkflowDefinition getWorkflowDefinitionVersion(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
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
  public List<Event> getWorkflowDocumentEvents(
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
          "Failed to retrieve the events for the workflow document ("
              + workflowDocumentId
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    return workflowService.getWorkflowDocumentEvents(tenantId, workflowDocumentId);
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
  public UUID initiateWorkflow(UUID tenantId, InitiateWorkflowRequest initiateWorkflowRequest)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          InteractionNotFoundException,
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
  public void linkInteractionToWorkflow(
      UUID tenantId, LinkInteractionToWorkflowRequest linkInteractionToWorkflowRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          WorkflowNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    workflowService.linkInteractionToWorkflow(
        tenantId, linkInteractionToWorkflowRequest, getAuthenticationName());
  }

  @Override
  public UUID provideWorkflowDocument(
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

    return workflowService.provideWorkflowDocument(
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
  public UUID requestWorkflowDocument(
      UUID tenantId, RequestWorkflowDocumentRequest requestWorkflowDocumentRequest)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.requestWorkflowDocument(
        tenantId, requestWorkflowDocumentRequest, getAuthenticationName());
  }

  @Override
  public WorkflowSummaries searchWorkflows(
      UUID tenantId, SearchWorkflowsRequest searchWorkflowsRequest)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return workflowService.searchWorkflows(tenantId, searchWorkflowsRequest);
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
  public void suspendWorkflow(UUID tenantId, SuspendWorkflowRequest suspendWorkflowRequest)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          InvalidWorkflowStatusException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, suspendWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, suspendWorkflowRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to suspend the workflow ("
              + suspendWorkflowRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.suspendWorkflow(tenantId, suspendWorkflowRequest, getAuthenticationName());
  }

  @Override
  public void suspendWorkflowStep(
      UUID tenantId, SuspendWorkflowStepRequest suspendWorkflowStepRequest)
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
      if (!workflowService.workflowExists(tenantId, suspendWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, suspendWorkflowStepRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to suspend the workflow step ("
              + suspendWorkflowStepRequest.getStep()
              + ") for the workflow ("
              + suspendWorkflowStepRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.suspendWorkflowStep(tenantId, suspendWorkflowStepRequest);
  }

  @Override
  public void unsuspendWorkflow(UUID tenantId, UnsuspendWorkflowRequest unsuspendWorkflowRequest)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          InvalidWorkflowStatusException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!workflowService.workflowExists(tenantId, unsuspendWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, unsuspendWorkflowRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unsuspend the workflow ("
              + unsuspendWorkflowRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.unsuspendWorkflow(tenantId, unsuspendWorkflowRequest);
  }

  @Override
  public void unsuspendWorkflowStep(
      UUID tenantId, UnsuspendWorkflowStepRequest unsuspendWorkflowStepRequest)
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
      if (!workflowService.workflowExists(tenantId, unsuspendWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, unsuspendWorkflowStepRequest.getWorkflowId());
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unsuspend the workflow step ("
              + unsuspendWorkflowStepRequest.getStep()
              + ") for the workflow ("
              + unsuspendWorkflowStepRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    workflowService.unsuspendWorkflowStep(tenantId, unsuspendWorkflowStepRequest);
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
  public void updateWorkflowAttributeDefinition(
      String workflowAttributeDefinitionCode,
      WorkflowAttributeDefinition workflowAttributeDefinition)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowAttributeDefinitionCode)) {
      throw new InvalidArgumentException("workflowAttributeDefinitionCode");
    }

    if (!Objects.equals(workflowAttributeDefinitionCode, workflowAttributeDefinition.getCode())) {
      throw new InvalidArgumentException("workflowAttributeDefinition.code");
    }

    workflowService.updateWorkflowAttributeDefinition(workflowAttributeDefinition);
  }

  @Override
  public void updateWorkflowDefinition(
      String workflowDefinitionId,
      Integer workflowDefinitionVersion,
      WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          WorkflowEngineNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    if (workflowDefinitionVersion == null) {
      throw new InvalidArgumentException("workflowDefinitionVersion");
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

    //    UUID workflowId =
    //        workflowService.getWorkflowIdForWorkflowDocument(
    //            tenantId, verifyWorkflowDocumentRequest.getWorkflowDocumentId());
    //
    //    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
    //        && (!hasAccessToFunction("Operations.WorkflowAdministration"))
    //        && (!hasWorkflowDefinitionPermission(
    //            tenantId, workflowId, WorkflowPermissionType.INITIATE_WORKFLOW))) {
    //      throw new AccessDeniedException(
    //          "No permission ("
    //              + WorkflowPermissionType.INITIATE_WORKFLOW
    //              + ") for workflow ("
    //              + workflowId
    //              + ")");
    //    }

    workflowService.verifyWorkflowDocument(
        tenantId, verifyWorkflowDocumentRequest, getAuthenticationName());
  }

  @Override
  public void verifyWorkflowStatuses() throws ServiceUnavailableException {
    try {
      BackgroundWorkflowStatusVerifier backgroundWorkflowStatusVerifier =
          getApplicationContext().getBean(BackgroundWorkflowStatusVerifier.class);

      backgroundWorkflowStatusVerifier.verifyWorkflowStatuses();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to verify the workflow statuses", e);
    }
  }

  /**
   * Confirm that the user associated with the authenticated request has the specified permission
   * for the workflow definition with the specified ID and version.
   *
   * @param workflowDefinitionId the ID for the workflow definition
   * @param workflowDefinitionVersion the version of the workflow definition
   * @param permissionType the workflow permission type
   * @return {@code true} if the user associated with the authenticated request has the specified
   *     permission for the workflow definition with the specified ID and version or {@code false}
   *     otherwise
   */
  private boolean hasWorkflowDefinitionPermission(
      String workflowDefinitionId,
      int workflowDefinitionVersion,
      WorkflowPermissionType permissionType)
      throws ServiceUnavailableException {
    if (isSecurityDisabled()) {
      return true;
    }

    try {
      List<WorkflowDefinitionPermission> workflowDefinitionPermissions =
          workflowService.getWorkflowDefinitionPermissions(
              workflowDefinitionId, workflowDefinitionVersion);

      // If no permissions have been defined for the workflow definition, allow access by default
      // TODO: Verify if we should deny by default here instead -- MARCUS
      if (workflowDefinitionPermissions.isEmpty()) {
        return true;
      }

      for (WorkflowDefinitionPermission workflowDefinitionPermission :
          workflowDefinitionPermissions) {
        if (workflowDefinitionPermission.getType().equals(permissionType)) {
          if (hasRole(workflowDefinitionPermission.getRoleCode())) {
            return true;
          }
        }
      }
    } catch (WorkflowDefinitionVersionNotFoundException e) {
      // Do nothing, we will return false below
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to verify whether the user associated with the authenticated request has the permission ("
              + permissionType.code()
              + ") for the workflow definition ("
              + workflowDefinitionId
              + ")",
          e);
    }

    return false;
  }

  /**
   * Confirm that the user associated with the authenticated request has the specified permission
   * for the workflow with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param workflowId the ID for the workflow
   * @param permissionType the workflow permission type
   * @return {@code true} if the user associated with the authenticated request has the specified
   *     permission for the workflow with the specified ID or {@code false} otherwise
   */
  private boolean hasWorkflowDefinitionPermission(
      UUID tenantId, UUID workflowId, WorkflowPermissionType permissionType)
      throws ServiceUnavailableException {
    try {
      WorkflowDefinitionId workflowDefinitionId =
          workflowService.getWorkflowDefinitionIdForWorkflow(tenantId, workflowId);

      return hasWorkflowDefinitionPermission(
          workflowDefinitionId.getId(), workflowDefinitionId.getVersion(), permissionType);
    } catch (WorkflowNotFoundException e) {
      // Do nothing, we will return false below
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to verify whether the user associated with the authenticated request has the permission ("
              + permissionType.code()
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    return false;
  }
}
