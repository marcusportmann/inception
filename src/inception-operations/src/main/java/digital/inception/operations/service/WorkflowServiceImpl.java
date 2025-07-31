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

package digital.inception.operations.service;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionCategoryRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionSummaryRepository;
import digital.inception.operations.persistence.jpa.WorkflowEngineRepository;
import digital.inception.operations.store.WorkflowStore;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code WorkflowServiceImpl} class provides the Workflow Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class WorkflowServiceImpl extends AbstractServiceBase implements WorkflowService {

  /** The Workflow Definition Category Repository. */
  private final WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository;

  /** The Workflow Definition Repository. */
  private final WorkflowDefinitionRepository workflowDefinitionRepository;

  /** The Workflow Definition Summary Repository. */
  private final WorkflowDefinitionSummaryRepository workflowDefinitionSummaryRepository;

  /** The Workflow Engine Repository. */
  private final WorkflowEngineRepository workflowEngineRepository;

  /** The Workflow Store. */
  private final WorkflowStore workflowStore;

  /** The maximum number of filtered workflow notes that will be returned by the service. */
  @Value("${inception.operations.max-filtered-workflow-notes:#{100}}")
  private int maxFilteredWorkflowNotes;

  /** The maximum number of filtered workflows that will be returned by the service. */
  @Value("${inception.operations.max-filtered-workflows:#{100}}")
  private int maxFilteredWorkflows;

  /**
   * Constructs a new {@code WorkflowServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param workflowStore the Workflow Store
   * @param workflowDefinitionCategoryRepository the Workflow Definition Category Repository
   * @param workflowDefinitionRepository the Workflow Definition Repository
   * @param workflowDefinitionSummaryRepository the Workflow Definition Summary Repository
   * @param workflowEngineRepository the Workflow Engine Repository
   */
  public WorkflowServiceImpl(
      ApplicationContext applicationContext,
      WorkflowStore workflowStore,
      WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository,
      WorkflowDefinitionRepository workflowDefinitionRepository,
      WorkflowDefinitionSummaryRepository workflowDefinitionSummaryRepository,
      WorkflowEngineRepository workflowEngineRepository) {
    super(applicationContext);

    this.workflowStore = workflowStore;
    this.workflowDefinitionCategoryRepository = workflowDefinitionCategoryRepository;
    this.workflowDefinitionRepository = workflowDefinitionRepository;
    this.workflowDefinitionSummaryRepository = workflowDefinitionSummaryRepository;
    this.workflowEngineRepository = workflowEngineRepository;
  }

  @Override
  public void createWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionVersionException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    validateArgument("workflowDefinition", workflowDefinition);

    try {
      if (!workflowDefinitionCategoryRepository.existsById(workflowDefinition.getCategoryId())) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinition.getCategoryId());
      }

      if (workflowDefinitionRepository.existsByIdAndVersion(
          workflowDefinition.getId(), workflowDefinition.getVersion())) {
        throw new DuplicateWorkflowDefinitionVersionException(
            workflowDefinition.getId(), workflowDefinition.getVersion());
      }

      workflowDefinitionRepository.saveAndFlush(workflowDefinition);
    } catch (DuplicateWorkflowDefinitionVersionException
        | WorkflowDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow definition ("
              + workflowDefinition.getId()
              + ") version ("
              + workflowDefinition.getVersion()
              + ")",
          e);
    }
  }

  @Override
  public void createWorkflowDefinitionCategory(
      WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionCategoryException,
          ServiceUnavailableException {
    validateArgument("workflowDefinitionCategory", workflowDefinitionCategory);

    try {
      if (workflowDefinitionCategoryRepository.existsById(workflowDefinitionCategory.getId())) {
        throw new DuplicateWorkflowDefinitionCategoryException(workflowDefinitionCategory.getId());
      }

      workflowDefinitionCategoryRepository.saveAndFlush(workflowDefinitionCategory);
    } catch (DuplicateWorkflowDefinitionCategoryException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow definition category ("
              + workflowDefinitionCategory.getId()
              + ")",
          e);
    }
  }

  @Override
  public void createWorkflowEngine(WorkflowEngine workflowEngine)
      throws InvalidArgumentException,
          DuplicateWorkflowEngineException,
          ServiceUnavailableException {
    validateArgument("workflowEngine", workflowEngine);

    try {
      if (workflowEngineRepository.existsById(workflowEngine.getId())) {
        throw new DuplicateWorkflowEngineException(workflowEngine.getId());
      }

      workflowEngineRepository.saveAndFlush(workflowEngine);
    } catch (DuplicateWorkflowEngineException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow engine (" + workflowEngine.getId() + ")", e);
    }
  }

  @Override
  public WorkflowNote createWorkflowNote(
      UUID tenantId, CreateWorkflowNoteRequest createWorkflowNoteRequest, String createdBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("createWorkflowNoteRequest", createWorkflowNoteRequest);

    if (!StringUtils.hasText(createdBy)) {
      throw new InvalidArgumentException("createdBy");
    }

    try {
      if (!workflowExists(tenantId, createWorkflowNoteRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(createWorkflowNoteRequest.getWorkflowId());
      }

      WorkflowNote workflowNote =
          new WorkflowNote(
              tenantId,
              createWorkflowNoteRequest.getWorkflowId(),
              createWorkflowNoteRequest.getContent(),
              OffsetDateTime.now(),
              createdBy);

      return workflowStore.createWorkflowNote(tenantId, workflowNote);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow note for the workflow ("
              + createWorkflowNoteRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    workflowStore.deleteWorkflow(tenantId, workflowId);
  }

  @Override
  public void deleteWorkflowDefinition(String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    try {
      if (!workflowDefinitionRepository.existsById(workflowDefinitionId)) {
        throw new WorkflowDefinitionNotFoundException(workflowDefinitionId);
      }

      workflowDefinitionRepository.deleteById(workflowDefinitionId);
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow definition (" + workflowDefinitionId + ")", e);
    }
  }

  @Override
  public void deleteWorkflowDefinitionCategory(String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionCategoryId)) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    try {
      if (!workflowDefinitionCategoryRepository.existsById(workflowDefinitionCategoryId)) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategoryId);
      }

      workflowDefinitionCategoryRepository.deleteById(workflowDefinitionCategoryId);
    } catch (WorkflowDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow definition category ("
              + workflowDefinitionCategoryId
              + ")",
          e);
    }
  }

  @Override
  public void deleteWorkflowDefinitionVersion(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    if (workflowDefinitionVersion <= 0) {
      throw new InvalidArgumentException("workflowDefinitionVersion");
    }

    try {
      WorkflowDefinitionId id =
          new WorkflowDefinitionId(workflowDefinitionId, workflowDefinitionVersion);

      if (!workflowDefinitionRepository.existsById(id)) {
        throw new WorkflowDefinitionVersionNotFoundException(
            workflowDefinitionId, workflowDefinitionVersion);
      }

      workflowDefinitionRepository.deleteById(id);
    } catch (WorkflowDefinitionVersionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
              + ")",
          e);
    }
  }

  @Override
  public void deleteWorkflowEngine(String workflowEngineId)
      throws InvalidArgumentException,
          WorkflowEngineNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowEngineId)) {
      throw new InvalidArgumentException("workflowEngineId");
    }

    try {
      if (!workflowEngineRepository.existsById(workflowEngineId)) {
        throw new WorkflowEngineNotFoundException(workflowEngineId);
      }

      workflowEngineRepository.deleteById(workflowEngineId);
    } catch (WorkflowEngineNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow engine (" + workflowEngineId + ")", e);
    }
  }

  @Override
  public void deleteWorkflowNote(UUID tenantId, UUID workflowNoteId)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowNoteId == null) {
      throw new InvalidArgumentException("workflowNoteId");
    }

    try {
      workflowStore.deleteWorkflowNote(tenantId, workflowNoteId);
    } catch (WorkflowNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow note ("
              + workflowNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Workflow getWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    return workflowStore.getWorkflow(tenantId, workflowId);
  }

  @Override
  public WorkflowDefinition getWorkflowDefinition(String workflowDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    try {
      Optional<WorkflowDefinition> workflowDefinitionOptional =
          workflowDefinitionRepository.findLatestVersionById(workflowDefinitionId);

      if (workflowDefinitionOptional.isEmpty()) {
        throw new WorkflowDefinitionNotFoundException(workflowDefinitionId);
      }

      return workflowDefinitionOptional.get();
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the latest version of the workflow definition ("
              + workflowDefinitionId
              + ")",
          e);
    }
  }

  @Override
  public List<WorkflowDefinitionCategory> getWorkflowDefinitionCategories(UUID tenantId)
      throws ServiceUnavailableException {
    try {
      return workflowDefinitionCategoryRepository.findForTenantOrGlobal(tenantId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow definition categories for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public WorkflowDefinitionCategory getWorkflowDefinitionCategory(
      String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionCategoryId)) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    try {
      Optional<WorkflowDefinitionCategory> workflowDefinitionCategoryOptional =
          workflowDefinitionCategoryRepository.findById(workflowDefinitionCategoryId);

      if (workflowDefinitionCategoryOptional.isEmpty()) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategoryId);
      }

      return workflowDefinitionCategoryOptional.get();
    } catch (WorkflowDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow definition category ("
              + workflowDefinitionCategoryId
              + ")",
          e);
    }
  }

  @Override
  public List<WorkflowDefinitionSummary> getWorkflowDefinitionSummaries(
      UUID tenantId, String workflowDefinitionCategoryId)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionCategoryId)) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    try {
      if (!workflowDefinitionCategoryRepository.existsById(workflowDefinitionCategoryId)) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategoryId);
      }

      return workflowDefinitionSummaryRepository.findForCategoryAndTenantOrGlobal(
          workflowDefinitionCategoryId, tenantId);
    } catch (WorkflowDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the workflow definitions associated with the workflow definition category ("
              + workflowDefinitionCategoryId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public WorkflowDefinition getWorkflowDefinitionVersion(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    if (workflowDefinitionVersion <= 0) {
      throw new InvalidArgumentException("workflowDefinitionVersion");
    }

    try {
      Optional<WorkflowDefinition> workflowDefinitionOptional =
          workflowDefinitionRepository.findById(
              new WorkflowDefinitionId(workflowDefinitionId, workflowDefinitionVersion));

      if (workflowDefinitionOptional.isEmpty()) {
        throw new WorkflowDefinitionVersionNotFoundException(
            workflowDefinitionId, workflowDefinitionVersion);
      }

      return workflowDefinitionOptional.get();
    } catch (WorkflowDefinitionVersionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
              + ")",
          e);
    }
  }

  @Override
  public WorkflowEngine getWorkflowEngine(String workflowEngineId)
      throws InvalidArgumentException,
          WorkflowEngineNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowEngineId)) {
      throw new InvalidArgumentException("workflowEngineId");
    }

    try {
      Optional<WorkflowEngine> workflowEngineOptional =
          workflowEngineRepository.findById(workflowEngineId);

      if (workflowEngineOptional.isEmpty()) {
        throw new WorkflowEngineNotFoundException(workflowEngineId);
      }

      return workflowEngineOptional.get();
    } catch (WorkflowEngineNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow engine (" + workflowEngineId + ")", e);
    }
  }

  @Override
  public List<WorkflowEngine> getWorkflowEngines() throws ServiceUnavailableException {
    try {
      return workflowEngineRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the workflow engines", e);
    }
  }

  @Override
  public WorkflowNote getWorkflowNote(UUID tenantId, UUID workflowNoteId)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowNoteId == null) {
      throw new InvalidArgumentException("workflowNoteId");
    }

    try {
      return workflowStore.getWorkflowNote(tenantId, workflowNoteId);
    } catch (WorkflowNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow note ("
              + workflowNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
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
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    try {
      return workflowStore.getWorkflowNotes(
          tenantId,
          workflowId,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize,
          maxFilteredWorkflowNotes);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered workflow notes for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
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
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = WorkflowSortBy.DEFINITION_ID;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.DESCENDING;
    }

    try {
      return workflowStore.getWorkflowSummaries(
          tenantId,
          definitionId,
          status,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize,
          maxFilteredWorkflows);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered workflow summaries for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public Workflow initiateWorkflow(
      UUID tenantId, InitiateWorkflowRequest initiateWorkflowRequest, String createdBy)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("initiateWorkflowRequest", initiateWorkflowRequest);

    try {
      Optional<WorkflowDefinition> workflowDefinitionOptional =
          workflowDefinitionRepository.findLatestVersionById(
              initiateWorkflowRequest.getDefinitionId());

      if (workflowDefinitionOptional.isEmpty()) {
        throw new WorkflowDefinitionNotFoundException(initiateWorkflowRequest.getDefinitionId());
      }

      WorkflowDefinition workflowDefinition = workflowDefinitionOptional.get();

      Workflow workflow =
          new Workflow(
              tenantId,
              initiateWorkflowRequest.getParentId(),
              workflowDefinition.getId(),
              workflowDefinition.getVersion(),
              WorkflowStatus.ACTIVE,
              initiateWorkflowRequest.getData(),
              OffsetDateTime.now(),
              createdBy);

      return workflowStore.createWorkflow(tenantId, workflow);
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to initiate the workflow ("
              + initiateWorkflowRequest.getDefinitionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public WorkflowStep initiateWorkflowStep(
      UUID tenantId, InitiateWorkflowStepRequest initiateWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("initiateWorkflowStepRequest", initiateWorkflowStepRequest);

    try {
      return workflowStore.initiateWorkflowStep(
          tenantId,
          initiateWorkflowStepRequest.getWorkflowId(),
          initiateWorkflowStepRequest.getStep());
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
  }

  @Override
  public Workflow updateWorkflow(
      UUID tenantId, UpdateWorkflowRequest updateWorkflowRequest, String updatedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("updateWorkflowRequest", updateWorkflowRequest);

    try {
      Workflow workflow =
          workflowStore.getWorkflow(tenantId, updateWorkflowRequest.getWorkflowId());

      if (StringUtils.hasText(updateWorkflowRequest.getData())) {
        workflow.setData(updateWorkflowRequest.getData());
      }

      if (updateWorkflowRequest.getStatus() != null) {
        workflow.setStatus(updateWorkflowRequest.getStatus());
      }

      workflow.setUpdated(OffsetDateTime.now());
      workflow.setUpdatedBy(updatedBy);

      return workflowStore.updateWorkflow(tenantId, workflow);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow ("
              + updateWorkflowRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void updateWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    validateArgument("workflowDefinition", workflowDefinition);

    try {
      if (!workflowDefinitionCategoryRepository.existsById(workflowDefinition.getCategoryId())) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinition.getCategoryId());
      }

      if (!workflowDefinitionRepository.existsByIdAndVersion(
          workflowDefinition.getId(), workflowDefinition.getVersion())) {
        throw new WorkflowDefinitionVersionNotFoundException(
            workflowDefinition.getId(), workflowDefinition.getVersion());
      }

      workflowDefinitionRepository.saveAndFlush(workflowDefinition);
    } catch (WorkflowDefinitionCategoryNotFoundException
        | WorkflowDefinitionVersionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow definition ("
              + workflowDefinition.getId()
              + ") version ("
              + workflowDefinition.getVersion()
              + ")",
          e);
    }
  }

  @Override
  public void updateWorkflowDefinitionCategory(
      WorkflowDefinitionCategory workflowDefinitionCategory)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    validateArgument("workflowDefinitionCategory", workflowDefinitionCategory);

    try {
      if (!workflowDefinitionCategoryRepository.existsById(workflowDefinitionCategory.getId())) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategory.getId());
      }

      workflowDefinitionCategoryRepository.saveAndFlush(workflowDefinitionCategory);
    } catch (WorkflowDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow definition category ("
              + workflowDefinitionCategory.getId()
              + ")",
          e);
    }
  }

  @Override
  public void updateWorkflowEngine(WorkflowEngine workflowEngine)
      throws InvalidArgumentException,
          WorkflowEngineNotFoundException,
          ServiceUnavailableException {
    validateArgument("workflowEngine", workflowEngine);

    try {
      if (!workflowEngineRepository.existsById(workflowEngine.getId())) {
        throw new WorkflowEngineNotFoundException(workflowEngine.getId());
      }

      workflowEngineRepository.saveAndFlush(workflowEngine);
    } catch (WorkflowEngineNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow engine (" + workflowEngine.getId() + ")", e);
    }
  }

  @Override
  public WorkflowNote updateWorkflowNote(
      UUID tenantId, UpdateWorkflowNoteRequest updateWorkflowNoteRequest, String updatedBy)
      throws InvalidArgumentException, WorkflowNoteNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("updateWorkflowNoteRequest", updateWorkflowNoteRequest);

    try {
      WorkflowNote workflowNote =
          workflowStore.getWorkflowNote(tenantId, updateWorkflowNoteRequest.getWorkflowNoteId());

      workflowNote.setContent(updateWorkflowNoteRequest.getContent());
      workflowNote.setUpdated(OffsetDateTime.now());
      workflowNote.setUpdatedBy(updatedBy);

      return workflowStore.updateWorkflowNote(tenantId, workflowNote);
    } catch (WorkflowNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow note ("
              + updateWorkflowNoteRequest.getWorkflowNoteId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean workflowDefinitionCategoryExists(String workflowDefinitionCategoryId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionCategoryId)) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    try {
      return workflowDefinitionCategoryRepository.existsById(workflowDefinitionCategoryId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow definition category ("
              + workflowDefinitionCategoryId
              + ") exists",
          e);
    }
  }

  @Override
  public boolean workflowDefinitionExists(
      String workflowDefinitionCategoryId, String workflowDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionCategoryId)) {
      throw new InvalidArgumentException("workflowDefinitionCategoryId");
    }

    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    try {
      return workflowDefinitionRepository.existsByCategoryIdAndId(
          workflowDefinitionCategoryId, workflowDefinitionId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow definition ("
              + workflowDefinitionId
              + ") exists and is associated with the workflow definition category ("
              + workflowDefinitionCategoryId
              + ")",
          e);
    }
  }

  @Override
  public boolean workflowDefinitionExists(String workflowDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    try {
      return workflowDefinitionRepository.existsById(workflowDefinitionId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow definition (" + workflowDefinitionId + ") exists",
          e);
    }
  }

  @Override
  public boolean workflowDefinitionVersionExists(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    if (workflowDefinitionVersion <= 0) {
      throw new InvalidArgumentException("workflowDefinitionVersion");
    }

    try {
      return workflowDefinitionRepository.existsById(
          new WorkflowDefinitionId(workflowDefinitionId, workflowDefinitionVersion));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
              + ") exists",
          e);
    }
  }

  @Override
  public boolean workflowEngineExists(String workflowEngineId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(workflowEngineId)) {
      throw new InvalidArgumentException("workflowEngineId");
    }

    try {
      return workflowEngineRepository.existsById(workflowEngineId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow engine (" + workflowEngineId + ") exists", e);
    }
  }

  @Override
  public boolean workflowExists(UUID tenantId, UUID workflowId) throws ServiceUnavailableException {
    try {
      return workflowStore.workflowExists(tenantId, workflowId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow ("
              + workflowId
              + ") exists for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean workflowNoteExists(UUID tenantId, UUID workflowId, UUID workflowNoteId)
      throws ServiceUnavailableException {
    try {
      return workflowStore.workflowNoteExists(tenantId, workflowId, workflowNoteId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow note ("
              + workflowNoteId
              + ") exists for the workflow ("
              + workflowId
              + ") and tenant ("
              + tenantId
              + ")",
          e);
    }
  }
}
