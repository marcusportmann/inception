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
import digital.inception.core.util.StringUtil;
import digital.inception.operations.connector.WorkflowEngineConnector;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DuplicateWorkflowAttributeDefinitionException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
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
import digital.inception.operations.model.DelinkInteractionFromWorkflowRequest;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.InitiateWorkflowInteractionLink;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.LinkInteractionToWorkflowRequest;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.RequiredDocumentAttribute;
import digital.inception.operations.model.StartWorkflowRequest;
import digital.inception.operations.model.SuspendWorkflowRequest;
import digital.inception.operations.model.SuspendWorkflowStepRequest;
import digital.inception.operations.model.UnsuspendWorkflowRequest;
import digital.inception.operations.model.UnsuspendWorkflowStepRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowAttributeDefinition;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionDocumentDefinition;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDefinitionPermission;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowInteractionLink;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowStepDefinition;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.persistence.jpa.WorkflowAttributeDefinitionRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionCategoryRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionSummaryRepository;
import digital.inception.operations.persistence.jpa.WorkflowEngineRepository;
import digital.inception.operations.store.WorkflowStore;
import java.lang.reflect.Constructor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

  /** The Document Service. */
  private final DocumentService documentService;

  private final InteractionService interactionService;

  /** The Workflow Attribute Definition Repository. */
  private final WorkflowAttributeDefinitionRepository workflowAttributeDefinitionRepository;

  /** The Workflow Data Validation Service. */
  private final WorkflowDataValidationService workflowDataValidationService;

  /** The Workflow Definition Category Repository. */
  private final WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository;

  /** The Workflow Definition Repository. */
  private final WorkflowDefinitionRepository workflowDefinitionRepository;

  /** The Workflow Definition Summary Repository. */
  private final WorkflowDefinitionSummaryRepository workflowDefinitionSummaryRepository;

  /** The workflow engine connectors. */
  private final Map<String, WorkflowEngineConnector> workflowEngineConnectors =
      new ConcurrentHashMap<>();

  /** The Workflow Engine Repository. */
  private final WorkflowEngineRepository workflowEngineRepository;

  /** The Workflow Store. */
  private final WorkflowStore workflowStore;

  /** The maximum number of filtered workflow documents that will be returned by the service. */
  @Value("${inception.operations.max-filtered-workflow-documents:#{100}}")
  private int maxFilteredWorkflowDocuments;

  /** The maximum number of filtered workflow notes that will be returned by the service. */
  @Value("${inception.operations.max-filtered-workflow-notes:#{100}}")
  private int maxFilteredWorkflowNotes;

  /** The maximum number of filtered workflows that will be returned by the service. */
  @Value("${inception.operations.max-filtered-workflows:#{100}}")
  private int maxFilteredWorkflows;

  /** The internal reference to the Workflow Service to enable caching. */
  private WorkflowService workflowService;

  /**
   * Constructs a new {@code WorkflowServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param workflowStore the Workflow Store
   * @param workflowAttributeDefinitionRepository the Workflow Attribute Definition Repository
   * @param workflowDefinitionCategoryRepository the Workflow Definition Category Repository
   * @param workflowDefinitionRepository the Workflow Definition Repository
   * @param workflowDefinitionSummaryRepository the Workflow Definition Summary Repository
   * @param workflowEngineRepository the Workflow Engine Repository
   * @param documentService the Document Service
   * @param workflowDataValidationService the Workflow Data Validation Service
   * @param interactionService the Interaction Service
   */
  public WorkflowServiceImpl(
      ApplicationContext applicationContext,
      WorkflowStore workflowStore,
      WorkflowAttributeDefinitionRepository workflowAttributeDefinitionRepository,
      WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository,
      WorkflowDefinitionRepository workflowDefinitionRepository,
      WorkflowDefinitionSummaryRepository workflowDefinitionSummaryRepository,
      WorkflowEngineRepository workflowEngineRepository,
      DocumentService documentService,
      WorkflowDataValidationService workflowDataValidationService,
      InteractionService interactionService) {
    super(applicationContext);

    this.workflowStore = workflowStore;
    this.workflowAttributeDefinitionRepository = workflowAttributeDefinitionRepository;
    this.workflowDefinitionCategoryRepository = workflowDefinitionCategoryRepository;
    this.workflowDefinitionRepository = workflowDefinitionRepository;
    this.workflowDefinitionSummaryRepository = workflowDefinitionSummaryRepository;
    this.workflowEngineRepository = workflowEngineRepository;
    this.documentService = documentService;
    this.workflowDataValidationService = workflowDataValidationService;
    this.interactionService = interactionService;
  }

  @Override
  public void cancelWorkflow(
      UUID tenantId, CancelWorkflowRequest cancelWorkflowRequest, String canceledBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("cancelWorkflowRequest", cancelWorkflowRequest);

    try {
      Workflow workflow = getWorkflow(tenantId, cancelWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(
                  workflow.getDefinitionId(), workflow.getDefinitionVersion());

      getWorkflowEngineConnector(workflowDefinition.getEngineId())
          .cancelWorkflow(tenantId, workflowDefinition, workflow);

      workflowStore.cancelWorkflow(
          tenantId,
          cancelWorkflowRequest.getWorkflowId(),
          canceledBy,
          cancelWorkflowRequest.getCancellationReason());
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
  }

  @Override
  @CacheEvict(cacheNames = "workflowAttributeDefinitions", allEntries = true)
  public void createWorkflowAttributeDefinition(
      WorkflowAttributeDefinition workflowAttributeDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowAttributeDefinitionException,
          ServiceUnavailableException {
    validateArgument("workflowAttributeDefinition", workflowAttributeDefinition);

    try {
      if (workflowAttributeDefinitionRepository.existsById(workflowAttributeDefinition.getCode())) {
        throw new DuplicateWorkflowAttributeDefinitionException(
            workflowAttributeDefinition.getCode());
      }

      workflowAttributeDefinitionRepository.saveAndFlush(workflowAttributeDefinition);
    } catch (DuplicateWorkflowAttributeDefinitionException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow attribute definition ("
              + workflowAttributeDefinition.getCode()
              + ")",
          e);
    }
  }

  @Override
  @Caching(
      put = {
        @CachePut(
            cacheNames = "workflowDefinition",
            key = "#workflowDefinition.id + '-' + #workflowDefinition.version")
      },
      evict = {
        @CacheEvict(cacheNames = "latestWorkflowDefinition", key = "#workflowDefinition.id"),
        @CacheEvict(
            cacheNames = "workflowDefinitionPermissions",
            key = "#workflowDefinition.id + '-' + #workflowDefinition.version"),
        @CacheEvict(
            cacheNames = {"workflowDefinitions", "workflowDefinitionSummaries"},
            allEntries = true)
      })
  public void createWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionVersionException,
          WorkflowDefinitionCategoryNotFoundException,
          DocumentDefinitionNotFoundException,
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

      for (WorkflowDefinitionDocumentDefinition workflowDefinitionDocumentDefinition :
          workflowDefinition.getDocumentDefinitions()) {
        if (!documentService.documentDefinitionExists(
            workflowDefinitionDocumentDefinition.getDocumentDefinitionId())) {
          throw new DocumentDefinitionNotFoundException(
              workflowDefinitionDocumentDefinition.getDocumentDefinitionId());
        }
      }

      workflowDefinitionRepository.saveAndFlush(workflowDefinition);
    } catch (DuplicateWorkflowDefinitionVersionException
        | WorkflowDefinitionCategoryNotFoundException
        | DocumentDefinitionNotFoundException e) {
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
  @CacheEvict(cacheNames = "workflowDefinitionCategories", allEntries = true)
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
        throw new WorkflowNotFoundException(tenantId, createWorkflowNoteRequest.getWorkflowId());
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

    try {
      workflowStore.deleteWorkflow(tenantId, workflowId);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  @CacheEvict(cacheNames = "workflowAttributeDefinitions", allEntries = true)
  public void deleteWorkflowAttributeDefinition(String workflowAttributeDefinitionCode)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowAttributeDefinitionCode)) {
      throw new InvalidArgumentException("workflowAttributeDefinitionCode");
    }

    try {
      if (!workflowAttributeDefinitionRepository.existsById(workflowAttributeDefinitionCode)) {
        throw new WorkflowAttributeDefinitionNotFoundException(workflowAttributeDefinitionCode);
      }

      workflowAttributeDefinitionRepository.deleteById(workflowAttributeDefinitionCode);
    } catch (WorkflowAttributeDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow attribute definition ("
              + workflowAttributeDefinitionCode
              + ")",
          e);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(
            cacheNames = {
              "latestWorkflowDefinition",
              "workflowDefinition",
              "workflowDefinitions",
              "workflowDefinitionSummaries"
            },
            allEntries = true)
      })
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
  @CacheEvict(cacheNames = "workflowDefinitionCategories", allEntries = true)
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

  @Caching(
      evict = {
        @CacheEvict(cacheNames = "latestWorkflowDefinition", key = "#workflowDefinitionId"),
        @CacheEvict(
            cacheNames = {"workflowDefinition", "workflowDefinitionPermissions"},
            key = "#workflowDefinitionId + '-' + #workflowDefinitionVersion"),
        @CacheEvict(
            cacheNames = {"workflowDefinitions", "workflowDefinitionSummaries"},
            allEntries = true)
      })
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
  public void deleteWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowDocumentId == null) {
      throw new InvalidArgumentException("workflowDocumentId");
    }

    try {
      workflowStore.deleteWorkflowDocument(tenantId, workflowDocumentId);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow document ("
              + workflowDocumentId
              + ") for the tenant ("
              + tenantId
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
  public void delinkInteractionFromWorkflow(
      UUID tenantId, DelinkInteractionFromWorkflowRequest delinkInteractionFromWorkflowRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          WorkflowNotFoundException,
          WorkflowInteractionLinkNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("delinkInteractionFromWorkflowRequest", delinkInteractionFromWorkflowRequest);

    try {
      workflowStore.delinkInteractionFromWorkflow(
          tenantId,
          delinkInteractionFromWorkflowRequest.getWorkflowId(),
          delinkInteractionFromWorkflowRequest.getInteractionId());
    } catch (InteractionNotFoundException
        | WorkflowNotFoundException
        | WorkflowInteractionLinkNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delink the interaction ("
              + delinkInteractionFromWorkflowRequest.getInteractionId()
              + ") from the workflow ("
              + delinkInteractionFromWorkflowRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void finalizeWorkflow(
      UUID tenantId, FinalizeWorkflowRequest finalizeWorkflowRequest, String finalizedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("finalizeWorkflowRequest", finalizeWorkflowRequest);

    try {
      workflowStore.finalizeWorkflow(
          tenantId,
          finalizeWorkflowRequest.getWorkflowId(),
          finalizeWorkflowRequest.getStatus(),
          finalizedBy);
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
  }

  @Override
  public void finalizeWorkflowStep(
      UUID tenantId, FinalizeWorkflowStepRequest finalizeWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowStepNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("finalizeWorkflowStepRequest", finalizeWorkflowStepRequest);

    try {
      workflowStore.finalizeWorkflowStep(
          tenantId,
          finalizeWorkflowStepRequest.getWorkflowId(),
          finalizeWorkflowStepRequest.getStep(),
          finalizeWorkflowStepRequest.getStatus());
    } catch (WorkflowStepNotFoundException e) {
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
  }

  @Override
  public List<OutstandingWorkflowDocument> getOutstandingWorkflowDocuments(
      UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    try {
      return workflowStore.getOutstandingWorkflowDocuments(tenantId, workflowId);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the outstanding workflow documents for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  @Cacheable(cacheNames = "workflowAttributeDefinitions", key = "#tenantId.toString + '-REQUIRED'")
  public List<WorkflowAttributeDefinition> getRequiredWorkflowAttributeDefinitions(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      List<WorkflowAttributeDefinition> workflowAttributeDefinitions =
          getWorkflowService().getWorkflowAttributeDefinitions(tenantId);

      return workflowAttributeDefinitions.stream()
          .filter(WorkflowAttributeDefinition::isRequired)
          .toList();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the required workflow attribute definitions for the tenant ("
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

    try {
      return workflowStore.getWorkflow(tenantId, workflowId);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public WorkflowAttributeDefinition getWorkflowAttributeDefinition(
      String workflowAttributeDefinitionCode)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowAttributeDefinitionCode)) {
      throw new InvalidArgumentException("workflowAttributeDefinitionCode");
    }

    try {
      Optional<WorkflowAttributeDefinition> workflowAttributeDefinitionOptional =
          workflowAttributeDefinitionRepository.findById(workflowAttributeDefinitionCode);

      if (workflowAttributeDefinitionOptional.isEmpty()) {
        throw new WorkflowAttributeDefinitionNotFoundException(workflowAttributeDefinitionCode);
      }

      return workflowAttributeDefinitionOptional.get();
    } catch (WorkflowAttributeDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow attribute definition ("
              + workflowAttributeDefinitionCode
              + ")",
          e);
    }
  }

  @Override
  @Cacheable(cacheNames = "workflowAttributeDefinitions", key = "#tenantId")
  public List<WorkflowAttributeDefinition> getWorkflowAttributeDefinitions(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      return workflowAttributeDefinitionRepository.findForTenantOrGlobal(tenantId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow attribute definitions for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  @Cacheable(cacheNames = "latestWorkflowDefinition", key = "#workflowDefinitionId")
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
  @Cacheable(cacheNames = "workflowDefinitionCategories", key = "#tenantId")
  public List<WorkflowDefinitionCategory> getWorkflowDefinitionCategories(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

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
  public WorkflowDefinitionId getWorkflowDefinitionIdForWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    try {
      return workflowStore.getWorkflowDefinitionIdForWorkflow(tenantId, workflowId);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow definition ID and version for the workflow ("
              + workflowId
              + ")",
          e);
    }
  }

  @Override
  @Cacheable(
      cacheNames = "workflowDefinitionPermissions",
      key = "#workflowDefinitionId + '-' + #workflowDefinitionVersion")
  public List<WorkflowDefinitionPermission> getWorkflowDefinitionPermissions(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(workflowDefinitionId)) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    try {
      if (!workflowDefinitionRepository.existsById(
          new WorkflowDefinitionId(workflowDefinitionId, workflowDefinitionVersion))) {
        throw new WorkflowDefinitionVersionNotFoundException(
            workflowDefinitionId, workflowDefinitionVersion);
      }

      return workflowDefinitionRepository.findPermissionsByIdAndVersion(
          workflowDefinitionId, workflowDefinitionVersion);
    } catch (WorkflowDefinitionVersionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow definition permissions for the workflow definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
              + ")",
          e);
    }
  }

  @Override
  @Cacheable(cacheNames = "workflowDefinitionSummaries", key = "#workflowDefinitionCategoryId")
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
  @Cacheable(
      cacheNames = "workflowDefinition",
      key = "#workflowDefinitionId + '-' + #workflowDefinitionVersion")
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
  public WorkflowDocument getWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowDocumentId == null) {
      throw new InvalidArgumentException("workflowDocumentId");
    }

    try {
      return workflowStore.getWorkflowDocument(tenantId, workflowDocumentId);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow document ("
              + workflowDocumentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
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
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    try {
      return workflowStore.getWorkflowDocuments(
          tenantId,
          workflowId,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize,
          maxFilteredWorkflowDocuments);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered workflow documents for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
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
  public UUID getWorkflowIdForWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowDocumentId == null) {
      throw new InvalidArgumentException("workflowDocumentId");
    }

    try {
      return workflowStore.getWorkflowIdForWorkflowDocument(tenantId, workflowDocumentId);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow ID for the workflow document ("
              + workflowDocumentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
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
      String workflowDefinitionId,
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
          workflowDefinitionId,
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
      UUID tenantId, InitiateWorkflowRequest initiateWorkflowRequest, String initiatedBy)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          InteractionNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("initiateWorkflowRequest", initiateWorkflowRequest);

    try {
      OffsetDateTime now = OffsetDateTime.now();

      WorkflowDefinition workflowDefinition =
          getWorkflowService().getWorkflowDefinition(initiateWorkflowRequest.getDefinitionId());

      // Validate the workflow attributes
      for (WorkflowAttribute workflowAttribute : initiateWorkflowRequest.getAttributes()) {
        if (!isValidWorkflowAttribute(
            tenantId, workflowDefinition.getId(), workflowAttribute.getCode())) {
          log.warn(
              "Invalid workflow attribute ("
                  + workflowAttribute.getCode()
                  + ") provided when initiating a workflow with the workflow definition ("
                  + workflowDefinition.getId()
                  + ") version ("
                  + workflowDefinition.getVersion()
                  + ") for the tenant ("
                  + tenantId
                  + ")");

          throw new InvalidArgumentException(
              "initiateWorkflowRequest.attributes.code",
              "the workflow attribute (" + workflowAttribute.getCode() + ") is invalid");
        }
      }

      // Validate the required workflow attributes
      validateRequiredWorkflowAttributes(
          tenantId,
          "initiateWorkflowRequest.attributes",
          initiateWorkflowRequest.getDefinitionId(),
          initiateWorkflowRequest.getAttributes());

      // Validate the workflow data if we have a validation schema
      if ((workflowDefinition.getValidationSchemaType() != null)
          && (StringUtils.hasText(workflowDefinition.getValidationSchema()))) {
        if (!workflowDataValidationService.validateWorkflowData(
            workflowDefinition.getValidationSchemaType(),
            workflowDefinition.getValidationSchema(),
            initiateWorkflowRequest.getData())) {
          throw new InvalidArgumentException("initiateWorkflowRequest.data");
        }
      }

      // Validate the interaction links
      for (InitiateWorkflowInteractionLink initiateWorkflowInteractionLink :
          initiateWorkflowRequest.getInteractionLinks()) {
        if (!interactionService.interactionExists(
            tenantId, initiateWorkflowInteractionLink.getInteractionId())) {
          throw new InteractionNotFoundException(
              tenantId, initiateWorkflowInteractionLink.getInteractionId());
        }
      }

      Workflow workflow =
          new Workflow(
              tenantId,
              initiateWorkflowRequest.getParentId(),
              workflowDefinition.getId(),
              workflowDefinition.getVersion(),
              initiateWorkflowRequest.getPendWorkflow()
                  ? WorkflowStatus.PENDING
                  : WorkflowStatus.ACTIVE,
              initiateWorkflowRequest.getAttributes(),
              initiateWorkflowRequest.getData(),
              now,
              initiatedBy);

      for (InitiateWorkflowInteractionLink initiateWorkflowInteractionLink :
          initiateWorkflowRequest.getInteractionLinks()) {
        workflow.addInteractionLink(
            new WorkflowInteractionLink(
                initiateWorkflowInteractionLink.getInteractionId(), now, initiatedBy));
      }

      workflow.setExternalReference(initiateWorkflowRequest.getExternalReference());
      workflow.setPartyId(initiateWorkflowRequest.getPartyId());

      if (!initiateWorkflowRequest.getPendWorkflow()) {
        String engineInstanceId =
            getWorkflowEngineConnector(workflowDefinition.getEngineId())
                .initiateWorkflow(
                    tenantId,
                    workflowDefinition,
                    initiateWorkflowRequest.getAttributes(),
                    initiateWorkflowRequest.getData());

        workflow.setEngineInstanceId(engineInstanceId);
      }

      workflow = workflowStore.createWorkflow(tenantId, workflow);

      for (WorkflowDefinitionDocumentDefinition documentDefinition :
          workflowDefinition.getDocumentDefinitions()) {
        if (documentDefinition.isRequired()) {
          WorkflowDocument workflowDocument =
              new WorkflowDocument(
                  tenantId,
                  workflow.getId(),
                  documentDefinition.getDocumentDefinitionId(),
                  now,
                  initiatedBy);

          workflowStore.createWorkflowDocument(tenantId, workflowDocument);
        }
      }

      return workflow;
    } catch (InvalidArgumentException
        | WorkflowDefinitionNotFoundException
        | InteractionNotFoundException e) {
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
      WorkflowDefinitionId workflowDefinitionId =
          workflowStore.getWorkflowDefinitionIdForWorkflow(
              tenantId, initiateWorkflowStepRequest.getWorkflowId());

      List<WorkflowStepDefinition> stepDefinitions =
          workflowDefinitionRepository.findStepDefinitionsByWorkflowDefinitionIdAndVersion(
              workflowDefinitionId.getId(), workflowDefinitionId.getVersion());

      boolean foundWorkflowStepDefinition = false;
      for (WorkflowStepDefinition stepDefinition : stepDefinitions) {
        if (StringUtil.equalsIgnoreCase(
            initiateWorkflowStepRequest.getStep(), stepDefinition.getCode())) {
          foundWorkflowStepDefinition = true;
          break;
        }
      }

      if (!foundWorkflowStepDefinition) {
        throw new InvalidArgumentException("initiateWorkflowStepRequest.step");
      }

      return workflowStore.initiateWorkflowStep(
          tenantId,
          initiateWorkflowStepRequest.getWorkflowId(),
          initiateWorkflowStepRequest.getStep());
    } catch (InvalidArgumentException | WorkflowNotFoundException e) {
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
  public boolean isValidWorkflowAttribute(
      UUID tenantId, String workflowDefinitionId, String attributeCode)
      throws ServiceUnavailableException {
    try {
      List<WorkflowAttributeDefinition> workflowAttributeDefinitions =
          getWorkflowService().getWorkflowAttributeDefinitions(tenantId);

      for (WorkflowAttributeDefinition workflowAttributeDefinition : workflowAttributeDefinitions) {
        if ((workflowAttributeDefinition.getWorkflowDefinitionId() == null)
            || (StringUtil.equalsIgnoreCase(
                workflowAttributeDefinition.getWorkflowDefinitionId(), workflowDefinitionId))) {
          if ((workflowAttributeDefinition.getTenantId() == null)
              || (workflowAttributeDefinition.getTenantId().equals(tenantId))) {
            if (workflowAttributeDefinition.getCode().equals(attributeCode)) {
              return true;
            }
          }
        }
      }

      return false;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the workflow attribute ("
              + attributeCode
              + ") for the workflow with the workflow definition ("
              + workflowDefinitionId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void linkInteractionToWorkflow(
      UUID tenantId,
      LinkInteractionToWorkflowRequest linkInteractionToWorkflowRequest,
      String linkedBy)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          WorkflowNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (!StringUtils.hasText(linkedBy)) {
      throw new InvalidArgumentException("linkedBy");
    }

    validateArgument("linkInteractionToWorkflowRequest", linkInteractionToWorkflowRequest);

    try {
      workflowStore.linkInteractionToWorkflow(
          tenantId,
          linkInteractionToWorkflowRequest.getWorkflowId(),
          linkInteractionToWorkflowRequest.getInteractionId(),
          linkedBy);
    } catch (InteractionNotFoundException | WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to link the interaction ("
              + linkInteractionToWorkflowRequest.getInteractionId()
              + ") to the workflow ("
              + linkInteractionToWorkflowRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void provideWorkflowDocument(
      UUID tenantId,
      ProvideWorkflowDocumentRequest provideWorkflowDocumentRequest,
      String providedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (!StringUtils.hasText(providedBy)) {
      throw new InvalidArgumentException("providedBy");
    }

    validateArgument("provideWorkflowDocumentRequest", provideWorkflowDocumentRequest);

    try {
      String documentDefinitionId =
          workflowStore.getDocumentDefinitionIdForWorkflowDocument(
              tenantId, provideWorkflowDocumentRequest.getWorkflowDocumentId());

      // Validate the document attributes
      for (DocumentAttribute documentAttribute : provideWorkflowDocumentRequest.getAttributes()) {
        if (!documentService.isValidDocumentAttribute(
            tenantId, documentDefinitionId, documentAttribute.getCode())) {
          log.warn(
              "Invalid document attribute ("
                  + documentAttribute.getCode()
                  + ") provided for workflow document ("
                  + provideWorkflowDocumentRequest.getWorkflowDocumentId()
                  + ") for the tenant ("
                  + tenantId
                  + ")");

          throw new InvalidArgumentException(
              "provideWorkflowDocumentRequest.attributes.code",
              "the document attribute (" + documentAttribute.getCode() + ") is invalid");
        }
      }

      // Validate the required document attributes
      documentService.validateRequiredDocumentAttributes(
          tenantId,
          "provideWorkflowDocumentRequest.attributes",
          documentDefinitionId,
          provideWorkflowDocumentRequest.getAttributes());

      DocumentDefinition documentDefinition =
          documentService.getDocumentDefinition(documentDefinitionId);

      if (documentDefinition.getRequiredDocumentAttributes() != null) {
        for (RequiredDocumentAttribute requiredDocumentAttribute :
            documentDefinition.getRequiredDocumentAttributes()) {
          switch (requiredDocumentAttribute) {
            case EXPIRY_DATE -> {
              if (provideWorkflowDocumentRequest.getExpiryDate() == null) {
                throw new InvalidArgumentException("provideWorkflowDocumentRequest.expiryDate");
              }
            }
            case EXTERNAL_REFERENCE -> {
              if (provideWorkflowDocumentRequest.getExternalReference() == null) {
                throw new InvalidArgumentException(
                    "provideWorkflowDocumentRequest.externalReference");
              }
            }
            case ISSUE_DATE -> {
              if (provideWorkflowDocumentRequest.getIssueDate() == null) {
                throw new InvalidArgumentException("provideWorkflowDocumentRequest.issueDate");
              }
            }
          }
        }
      }

      workflowStore.provideWorkflowDocument(tenantId, provideWorkflowDocumentRequest, providedBy);
    } catch (InvalidArgumentException | WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to provide the workflow document", e);
    }
  }

  @Override
  public void rejectWorkflowDocument(
      UUID tenantId, RejectWorkflowDocumentRequest rejectWorkflowDocumentRequest, String rejectedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (!StringUtils.hasText(rejectedBy)) {
      throw new InvalidArgumentException("rejectedBy");
    }

    validateArgument("rejectWorkflowDocumentRequest", rejectWorkflowDocumentRequest);

    try {
      workflowStore.rejectWorkflowDocument(tenantId, rejectWorkflowDocumentRequest, rejectedBy);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to reject the workflow document ("
              + rejectWorkflowDocumentRequest.getWorkflowDocumentId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public UUID requestWorkflowDocument(
      UUID tenantId,
      RequestWorkflowDocumentRequest requestWorkflowDocumentRequest,
      String requestedBy)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (!StringUtils.hasText(requestedBy)) {
      throw new InvalidArgumentException("requestedBy");
    }

    validateArgument("requestWorkflowDocumentRequest", requestWorkflowDocumentRequest);

    try {
      if (!documentService.documentDefinitionExists(
          requestWorkflowDocumentRequest.getDocumentDefinitionId())) {
        throw new DocumentDefinitionNotFoundException(
            requestWorkflowDocumentRequest.getDocumentDefinitionId());
      }

      return workflowStore.requestWorkflowDocument(
          tenantId, requestWorkflowDocumentRequest, requestedBy);
    } catch (DocumentDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to request the workflow document with the document definition ("
              + requestWorkflowDocumentRequest.getDocumentDefinitionId()
              + ") for the workflow ("
              + requestWorkflowDocumentRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void startWorkflow(
      UUID tenantId, StartWorkflowRequest startWorkflowRequest, String startedBy)
      throws InvalidArgumentException,
          InvalidWorkflowStatusException,
          WorkflowNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("startWorkflowRequest", startWorkflowRequest);

    try {
      Workflow workflow = workflowStore.getWorkflow(tenantId, startWorkflowRequest.getWorkflowId());

      if (workflow.getStatus() != WorkflowStatus.PENDING) {
        throw new InvalidWorkflowStatusException(startWorkflowRequest.getWorkflowId());
      }

      // TODO: Start the workflow

    } catch (InvalidWorkflowStatusException | WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to start the workflow ("
              + startWorkflowRequest.getWorkflowId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void suspendWorkflow(
      UUID tenantId, SuspendWorkflowRequest suspendWorkflowRequest, String suspendedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("suspendWorkflowRequest", suspendWorkflowRequest);

    try {
      Workflow workflow = getWorkflow(tenantId, suspendWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(
                  workflow.getDefinitionId(), workflow.getDefinitionVersion());

      getWorkflowEngineConnector(workflowDefinition.getEngineId())
          .suspendWorkflow(tenantId, workflowDefinition, workflow);

      workflowStore.suspendWorkflow(tenantId, suspendWorkflowRequest.getWorkflowId(), suspendedBy);
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
  }

  @Override
  public void suspendWorkflowStep(
      UUID tenantId, SuspendWorkflowStepRequest suspendWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowStepNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("suspendWorkflowStepRequest", suspendWorkflowStepRequest);

    try {
      workflowStore.suspendWorkflowStep(
          tenantId,
          suspendWorkflowStepRequest.getWorkflowId(),
          suspendWorkflowStepRequest.getStep());
    } catch (WorkflowStepNotFoundException e) {
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
  }

  @Override
  public void unsuspendWorkflow(UUID tenantId, UnsuspendWorkflowRequest unsuspendWorkflowRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("unsuspendWorkflowRequest", unsuspendWorkflowRequest);

    try {
      Workflow workflow = getWorkflow(tenantId, unsuspendWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(
                  workflow.getDefinitionId(), workflow.getDefinitionVersion());

      getWorkflowEngineConnector(workflowDefinition.getEngineId())
          .suspendWorkflow(tenantId, workflowDefinition, workflow);

      workflowStore.unsuspendWorkflow(tenantId, unsuspendWorkflowRequest.getWorkflowId());
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
  }

  @Override
  public void unsuspendWorkflowStep(
      UUID tenantId, UnsuspendWorkflowStepRequest unsuspendWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowStepNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("unsuspendWorkflowStepRequest", unsuspendWorkflowStepRequest);

    try {
      workflowStore.unsuspendWorkflowStep(
          tenantId,
          unsuspendWorkflowStepRequest.getWorkflowId(),
          unsuspendWorkflowStepRequest.getStep());
    } catch (WorkflowStepNotFoundException e) {
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

      WorkflowDefinition workflowDefinition =
          getWorkflowDefinitionVersion(workflow.getDefinitionId(), workflow.getDefinitionVersion());

      // Validate the workflow data if we have a validation schema
      if ((workflowDefinition.getValidationSchemaType() != null)
          && (StringUtils.hasText(workflowDefinition.getValidationSchema()))) {
        if (!workflowDataValidationService.validateWorkflowData(
            workflowDefinition.getValidationSchemaType(),
            workflowDefinition.getValidationSchema(),
            updateWorkflowRequest.getData())) {
          throw new InvalidArgumentException("updateWorkflowRequest.data");
        }
      }

      if (updateWorkflowRequest.getAttributes() != null) {
        // Validate the required workflow attributes
        validateRequiredWorkflowAttributes(
            tenantId,
            "updateWorkflowRequest.attributes",
            workflowDefinition.getId(),
            updateWorkflowRequest.getAttributes());

        workflow.setAttributes(updateWorkflowRequest.getAttributes());
      }

      if (StringUtils.hasText(updateWorkflowRequest.getData())) {
        workflow.setData(updateWorkflowRequest.getData());

        getWorkflowEngineConnector(workflowDefinition.getEngineId())
            .updateWorkflowData(
                tenantId, workflowDefinition, workflow, updateWorkflowRequest.getData());
      }

      if (updateWorkflowRequest.getStatus() != null) {
        workflow.setStatus(updateWorkflowRequest.getStatus());
      }

      workflow.setUpdated(OffsetDateTime.now());
      workflow.setUpdatedBy(updatedBy);

      return workflowStore.updateWorkflow(tenantId, workflow);
    } catch (InvalidArgumentException | WorkflowNotFoundException e) {
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
  @CacheEvict(cacheNames = "workflowAttributeDefinitions", allEntries = true)
  public void updateWorkflowAttributeDefinition(
      WorkflowAttributeDefinition workflowAttributeDefinition)
      throws InvalidArgumentException,
          WorkflowAttributeDefinitionNotFoundException,
          ServiceUnavailableException {
    validateArgument("workflowAttributeDefinition", workflowAttributeDefinition);

    try {
      if (!workflowAttributeDefinitionRepository.existsById(
          workflowAttributeDefinition.getCode())) {
        throw new WorkflowAttributeDefinitionNotFoundException(
            workflowAttributeDefinition.getCode());
      }

      workflowAttributeDefinitionRepository.saveAndFlush(workflowAttributeDefinition);
    } catch (WorkflowAttributeDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow attribute definition ("
              + workflowAttributeDefinition.getCode()
              + ")",
          e);
    }
  }

  @Override
  @Caching(
      put = {
        @CachePut(
            cacheNames = "workflowDefinition",
            key = "#workflowDefinition.id + '-' + #workflowDefinition.version")
      },
      evict = {
        @CacheEvict(cacheNames = "latestWorkflowDefinition", key = "#workflowDefinition.id"),
        @CacheEvict(
            cacheNames = "workflowDefinitionPermissions",
            key = "#workflowDefinition.id + '-' + #workflowDefinition.version"),
        @CacheEvict(
            cacheNames = {"workflowDefinitions", "workflowDefinitionSummaries"},
            allEntries = true)
      })
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
  @CacheEvict(cacheNames = "workflowDefinitionCategories", allEntries = true)
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
  public void validateRequiredWorkflowAttributes(
      UUID tenantId,
      String parameter,
      String workflowDefinitionId,
      List<WorkflowAttribute> workflowAttributes)
      throws InvalidArgumentException, ServiceUnavailableException {
    try {
      List<WorkflowAttributeDefinition> requiredWorkflowAttributeDefinitions =
          getWorkflowService().getRequiredWorkflowAttributeDefinitions(tenantId);

      for (WorkflowAttributeDefinition requiredWorkflowAttributeDefinition :
          requiredWorkflowAttributeDefinitions) {
        if ((requiredWorkflowAttributeDefinition.getWorkflowDefinitionId() == null)
            || (StringUtil.equalsIgnoreCase(
                requiredWorkflowAttributeDefinition.getWorkflowDefinitionId(),
                workflowDefinitionId))) {
          if ((requiredWorkflowAttributeDefinition.getTenantId() == null)
              || (requiredWorkflowAttributeDefinition.getTenantId().equals(tenantId))) {
            if (workflowAttributes.stream()
                .noneMatch(
                    workflowAttribute ->
                        StringUtil.equalsIgnoreCase(
                            requiredWorkflowAttributeDefinition.getCode(),
                            workflowAttribute.getCode()))) {
              throw new InvalidArgumentException(
                  parameter,
                  "the workflow attribute ("
                      + requiredWorkflowAttributeDefinition.getCode()
                      + ") is required");
            }
          }
        }
      }
    } catch (InvalidArgumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to validate the required workflow attributes for the workflow definition ("
              + workflowDefinitionId
              + ")",
          e);
    }
  }

  @Override
  public void verifyWorkflowDocument(
      UUID tenantId, VerifyWorkflowDocumentRequest verifyWorkflowDocumentRequest, String verifiedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (!StringUtils.hasText(verifiedBy)) {
      throw new InvalidArgumentException("verifiedBy");
    }

    validateArgument("verifyWorkflowDocumentRequest", verifyWorkflowDocumentRequest);

    try {
      workflowStore.verifyWorkflowDocument(tenantId, verifyWorkflowDocumentRequest, verifiedBy);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to verify the workflow document ("
              + verifyWorkflowDocumentRequest.getWorkflowDocumentId()
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
  public boolean workflowDocumentExists(UUID tenantId, UUID workflowId, UUID workflowDocumentId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    if (workflowDocumentId == null) {
      throw new InvalidArgumentException("workflowDocumentId");
    }

    try {
      return workflowStore.workflowDocumentExists(tenantId, workflowId, workflowDocumentId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow document ("
              + workflowDocumentId
              + ") exists for the workflow ("
              + workflowId
              + ") and tenant ("
              + tenantId
              + ")",
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
  public boolean workflowExists(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

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
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    if (workflowNoteId == null) {
      throw new InvalidArgumentException("workflowNoteId");
    }

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

  private WorkflowEngineConnector getWorkflowEngineConnector(String engineId)
      throws ServiceUnavailableException {
    WorkflowEngineConnector workflowEngineConnector = workflowEngineConnectors.get(engineId);

    if (workflowEngineConnector == null) {
      try {
        WorkflowEngine workflowEngine = getWorkflowEngine(engineId);

        Class<?> clazz =
            Thread.currentThread()
                .getContextClassLoader()
                .loadClass(workflowEngine.getConnectorClassName());

        Constructor<?> constructor;

        try {
          constructor = clazz.getConstructor(ApplicationContext.class, WorkflowEngine.class);
        } catch (NoSuchMethodException e) {
          constructor = null;
        }

        if (constructor != null) {
          // Create an instance of the workflow engine connector
          workflowEngineConnector =
              (WorkflowEngineConnector)
                  constructor.newInstance(getApplicationContext(), workflowEngine);

          // Perform dependency injection on the workflow engine connector
          getApplicationContext()
              .getAutowireCapableBeanFactory()
              .autowireBean(workflowEngineConnector);

          // Cache the workflow engine connector
          workflowEngineConnectors.put(engineId, workflowEngineConnector);
        } else {
          throw new RuntimeException(
              "Failed to initialize the workflow engine connector for the workflow engine ("
                  + engineId
                  + "): The workflow engine connector class does not provide a constructor with the required signature");
        }
      } catch (Throwable e) {
        throw new ServiceUnavailableException(
            "Failed to retrieve the workflow engine connector for the workflow engine ("
                + engineId
                + ")",
            e);
      }
    }

    return workflowEngineConnector;
  }

  /**
   * Returns the internal reference to the Workflow Service to enable caching.
   *
   * @return the internal reference to the Workflow Service to enable caching.
   */
  private WorkflowService getWorkflowService() {
    if (workflowService == null) {
      workflowService = getApplicationContext().getBean(WorkflowService.class);
    }

    return workflowService;
  }
}
