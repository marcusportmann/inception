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
import digital.inception.json.JsonClasspathResource;
import digital.inception.operations.connector.WorkflowEngineConnector;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.FormDefinitionNotFoundException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InvalidWorkflowStatusException;
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
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.Event;
import digital.inception.operations.model.EventType;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.FormDefinition;
import digital.inception.operations.model.InitiateWorkflowInteractionLink;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.LinkInteractionToWorkflowRequest;
import digital.inception.operations.model.ObjectType;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.ResetWorkflowDocumentRequest;
import digital.inception.operations.model.SearchWorkflowsRequest;
import digital.inception.operations.model.StartWorkflowRequest;
import digital.inception.operations.model.SuspendWorkflowRequest;
import digital.inception.operations.model.SuspendWorkflowStepRequest;
import digital.inception.operations.model.UnsuspendWorkflowRequest;
import digital.inception.operations.model.UnsuspendWorkflowStepRequest;
import digital.inception.operations.model.UpdateWorkflowNoteRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.ValidWorkflowDefinitionAttribute;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.WaiveWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionAttribute;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionDocumentDefinition;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDefinitionPermission;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowEngineIds;
import digital.inception.operations.model.WorkflowFormType;
import digital.inception.operations.model.WorkflowInteractionLink;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowStepDefinition;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionCategoryRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionSummaryRepository;
import digital.inception.operations.persistence.jpa.WorkflowEngineRepository;
import digital.inception.operations.store.WorkflowStore;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
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

  /** The Application Event Publisher. */
  private final ApplicationEventPublisher applicationEventPublisher;

  /** The Document Service. */
  private final DocumentService documentService;

  /** The Event Service. */
  private final EventService eventService;

  private final InteractionService interactionService;

  /** The Validation Service. */
  private final ValidationService validationService;

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
   * @param applicationEventPublisher the application event publisher
   * @param workflowStore the Workflow Store
   * @param workflowDefinitionCategoryRepository the Workflow Definition Category Repository
   * @param workflowDefinitionRepository the Workflow Definition Repository
   * @param workflowDefinitionSummaryRepository the Workflow Definition Summary Repository
   * @param workflowEngineRepository the Workflow Engine Repository
   * @param documentService the Document Service
   * @param eventService the Event Service
   * @param interactionService the Interaction Service
   * @param validationService the Validation Service
   */
  public WorkflowServiceImpl(
      ApplicationContext applicationContext,
      ApplicationEventPublisher applicationEventPublisher,
      WorkflowStore workflowStore,
      WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository,
      WorkflowDefinitionRepository workflowDefinitionRepository,
      WorkflowDefinitionSummaryRepository workflowDefinitionSummaryRepository,
      WorkflowEngineRepository workflowEngineRepository,
      DocumentService documentService,
      EventService eventService,
      InteractionService interactionService,
      ValidationService validationService) {
    super(applicationContext);

    this.applicationEventPublisher = applicationEventPublisher;
    this.workflowStore = workflowStore;
    this.workflowDefinitionCategoryRepository = workflowDefinitionCategoryRepository;
    this.workflowDefinitionRepository = workflowDefinitionRepository;
    this.workflowDefinitionSummaryRepository = workflowDefinitionSummaryRepository;
    this.workflowEngineRepository = workflowEngineRepository;
    this.documentService = documentService;
    this.eventService = eventService;
    this.interactionService = interactionService;
    this.validationService = validationService;
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
      WorkflowEngineIds workflowEngineIds =
          workflowStore.getWorkflowEngineIdsForWorkflow(
              tenantId, cancelWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowDefinitionForWorkflow(tenantId, cancelWorkflowRequest.getWorkflowId());

      getWorkflowEngineConnector(workflowEngineIds.getEngineId())
          .cancelWorkflow(
              workflowDefinition,
              tenantId,
              cancelWorkflowRequest.getWorkflowId(),
              workflowEngineIds.getEngineInstanceId());

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
  @Caching(
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
          WorkflowEngineNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    validateArgument("workflowDefinition", workflowDefinition);

    try {
      if (workflowDefinitionRepository.existsByIdAndVersion(
          workflowDefinition.getId(), workflowDefinition.getVersion())) {
        throw new DuplicateWorkflowDefinitionVersionException(
            workflowDefinition.getId(), workflowDefinition.getVersion());
      }

      validateWorkflowDefinition(workflowDefinition);

      workflowDefinitionRepository.saveAndFlush(workflowDefinition);
    } catch (DuplicateWorkflowDefinitionVersionException
        | WorkflowDefinitionCategoryNotFoundException
        | WorkflowEngineNotFoundException
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
  public void deleteWorkflowStep(UUID tenantId, DeleteWorkflowStepRequest deleteWorkflowStepRequest)
      throws InvalidArgumentException, WorkflowStepNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("deleteWorkflowStepRequest", deleteWorkflowStepRequest);

    try {
      workflowStore.deleteWorkflowStep(
          tenantId, deleteWorkflowStepRequest.getWorkflowId(), deleteWorkflowStepRequest.getStep());
    } catch (WorkflowStepNotFoundException e) {
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

    if (!StringUtils.hasText(finalizedBy)) {
      throw new InvalidArgumentException("finalizedBy");
    }

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
      if (StringUtils.hasText(finalizeWorkflowStepRequest.getNextStep())) {
        WorkflowDefinitionId workflowDefinitionId =
            workflowStore.getWorkflowDefinitionIdForWorkflow(
                tenantId, finalizeWorkflowStepRequest.getWorkflowId());

        List<WorkflowStepDefinition> stepDefinitions =
            workflowDefinitionRepository.findStepDefinitionsByWorkflowDefinitionIdAndVersion(
                workflowDefinitionId.getId(), workflowDefinitionId.getVersion());

        boolean foundWorkflowStepDefinition = false;
        for (WorkflowStepDefinition stepDefinition : stepDefinitions) {
          if (stepDefinition.getCode().equals(finalizeWorkflowStepRequest.getNextStep())) {
            foundWorkflowStepDefinition = true;
            break;
          }
        }

        if (!foundWorkflowStepDefinition) {
          throw new InvalidArgumentException("finalizeWorkflowStepRequest.nextStep");
        }
      }

      workflowStore.finalizeWorkflowStep(
          tenantId,
          finalizeWorkflowStepRequest.getWorkflowId(),
          finalizeWorkflowStepRequest.getStep(),
          finalizeWorkflowStepRequest.getStatus(),
          finalizeWorkflowStepRequest.getNextStep());
    } catch (InvalidArgumentException | WorkflowStepNotFoundException e) {
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
  public List<UUID> getActiveWorkflowIdsForWorkflowEngine(UUID tenantId, String workflowEngineId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (!StringUtils.hasText(workflowEngineId)) {
      throw new InvalidArgumentException("workflowEngineId");
    }

    try {
      return workflowStore.getActiveWorkflowIdsForWorkflowEngine(tenantId, workflowEngineId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the IDs for the active workflows for the workflow engine ("
              + workflowEngineId
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
  public FormDefinition getStartFormDefinitionForWorkflowDefinition(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          FormDefinitionNotFoundException,
          ServiceUnavailableException {
    if (workflowDefinitionId == null) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    if (workflowDefinitionVersion <= 0) {
      throw new InvalidArgumentException("workflowDefinitionVersion");
    }

    try {
      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(workflowDefinitionId, workflowDefinitionVersion);

      WorkflowEngineConnector workflowEngineConnector =
          getWorkflowEngineConnector(workflowDefinition.getEngineId());

      Optional<FormDefinition> formDefinitionOptional =
          workflowEngineConnector.getFormDefinition(
              workflowDefinition, WorkflowFormType.START_FORM);

      if (formDefinitionOptional.isEmpty()) {
        throw new FormDefinitionNotFoundException(
            workflowDefinitionId, workflowDefinitionVersion, WorkflowFormType.START_FORM);
      }

      return formDefinitionOptional.get();

    } catch (WorkflowDefinitionVersionNotFoundException | FormDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the start form definition for the workflow definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
              + ")",
          e);
    }
  }

  @Override
  public FormDefinition getWorkFormDefinitionForWorkflowDefinition(
      String workflowDefinitionId, int workflowDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          FormDefinitionNotFoundException,
          ServiceUnavailableException {
    if (workflowDefinitionId == null) {
      throw new InvalidArgumentException("workflowDefinitionId");
    }

    if (workflowDefinitionVersion <= 0) {
      throw new InvalidArgumentException("workflowDefinitionVersion");
    }

    try {
      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(workflowDefinitionId, workflowDefinitionVersion);

      WorkflowEngineConnector workflowEngineConnector =
          getWorkflowEngineConnector(workflowDefinition.getEngineId());

      Optional<FormDefinition> formDefinitionOptional =
          workflowEngineConnector.getFormDefinition(workflowDefinition, WorkflowFormType.WORK_FORM);

      if (formDefinitionOptional.isEmpty()) {
        throw new FormDefinitionNotFoundException(
            workflowDefinitionId, workflowDefinitionVersion, WorkflowFormType.WORK_FORM);
      }

      return formDefinitionOptional.get();

    } catch (WorkflowDefinitionVersionNotFoundException | FormDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the work form definition for the workflow definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
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
  public WorkflowDefinition getWorkflowDefinitionForWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    try {
      WorkflowDefinitionId workflowDefinitionId =
          workflowStore.getWorkflowDefinitionIdForWorkflow(tenantId, workflowId);

      return getWorkflowService()
          .getWorkflowDefinitionVersion(
              workflowDefinitionId.getId(), workflowDefinitionId.getVersion());
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow definition for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
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
              + ") for the tenant ("
              + tenantId
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

    if (workflowDefinitionVersion <= 0) {
      throw new InvalidArgumentException("workflowDefinitionVersion");
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
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

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
  public List<Event> getWorkflowDocumentEvents(UUID tenantId, UUID workflowDocumentId)
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
      if (!workflowStore.workflowDocumentExists(tenantId, workflowDocumentId)) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      }

      return eventService.getEventsForObject(
          tenantId, ObjectType.WORKFLOW_DOCUMENT, workflowDocumentId);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the events for the workflow document ("
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
  public WorkflowEngineConnector getWorkflowEngineConnector(String workflowEngineId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(workflowEngineId)) {
      throw new InvalidArgumentException("workflowEngineId");
    }

    WorkflowEngineConnector workflowEngineConnector =
        workflowEngineConnectors.get(workflowEngineId);

    if (workflowEngineConnector == null) {
      try {
        WorkflowEngine workflowEngine = getWorkflowEngine(workflowEngineId);

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
          workflowEngineConnectors.put(workflowEngineId, workflowEngineConnector);
        } else {
          throw new RuntimeException(
              "Failed to initialize the workflow engine connector for the workflow engine ("
                  + workflowEngineId
                  + "): The workflow engine connector class does not provide a constructor with the required signature");
        }
      } catch (Throwable e) {
        throw new ServiceUnavailableException(
            "Failed to retrieve the workflow engine connector for the workflow engine ("
                + workflowEngineId
                + ")",
            e);
      }
    }

    return workflowEngineConnector;
  }

  @Override
  public List<String> getWorkflowEngineIds() throws ServiceUnavailableException {
    try {
      return workflowEngineRepository.findAllIds();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the workflow engine IDs", e);
    }
  }

  @Override
  public WorkflowEngineIds getWorkflowEngineIdsForWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      return workflowStore.getWorkflowEngineIdsForWorkflow(tenantId, workflowId);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow engine IDs for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
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
  public WorkflowStatus getWorkflowStatus(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    try {
      return workflowStore.getWorkflowStatus(tenantId, workflowId);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the status for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  /** Initialize the Workflow Service. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Workflow Service");

    // Load the workflow definitions from the classpath
    try {
      log.info("Loading the workflow definitions from the classpath");

      List<JsonClasspathResource<WorkflowDefinition>> jsonClasspathResources =
          JsonClasspathResource.loadFromClasspath(
              "workflow-definitions", getObjectMapper(), WorkflowDefinition.class);

      for (JsonClasspathResource<WorkflowDefinition> jsonClasspathResource :
          jsonClasspathResources) {
        WorkflowDefinition workflowDefinition = jsonClasspathResource.value();

        if (workflowDefinitionVersionExists(
            workflowDefinition.getId(), workflowDefinition.getVersion())) {
          updateWorkflowDefinition(workflowDefinition);

          log.info(
              "Updated the workflow definition ("
                  + workflowDefinition.getId()
                  + ") version ("
                  + workflowDefinition.getVersion()
                  + ")");
        } else {
          createWorkflowDefinition(workflowDefinition);

          log.info(
              "Created the workflow definition ("
                  + workflowDefinition.getId()
                  + ") version ("
                  + workflowDefinition.getVersion()
                  + ")");
        }
      }
    } catch (Throwable e) {
      throw new BeanInitializationException(
          "Failed to load the workflow definitions from the classpath", e);
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

    if (!StringUtils.hasText(initiatedBy)) {
      throw new InvalidArgumentException("initiatedBy");
    }

    try {
      OffsetDateTime now = OffsetDateTime.now();

      WorkflowDefinition workflowDefinition =
          getWorkflowService().getWorkflowDefinition(initiateWorkflowRequest.getDefinitionId());

      if (initiateWorkflowRequest.getExternalReferences() != null) {
        // Validate the external references
        validationService.validateExternalReferences(
            tenantId,
            "initiateWorkflowRequest.externalReferences",
            ObjectType.WORKFLOW,
            initiateWorkflowRequest.getExternalReferences());
      }

      if (initiateWorkflowRequest.getAttributes() != null) {
        // Validate the allowed workflow attributes
        validationService.validateAllowedWorkflowAttributes(
            "initiateWorkflowRequest.attributes",
            workflowDefinition,
            initiateWorkflowRequest.getAttributes());

        // Validate the required workflow attributes
        validationService.validateRequiredWorkflowAttributes(
            "initiateWorkflowRequest.attributes",
            workflowDefinition,
            initiateWorkflowRequest.getAttributes());
      }

      if (initiateWorkflowRequest.getVariables() != null) {
        // Validate the allowed workflow variables
        validationService.validateAllowedWorkflowVariables(
            "initiateWorkflowRequest.variables",
            workflowDefinition,
            initiateWorkflowRequest.getVariables());

        // Validate the required workflow variables
        validationService.validateRequiredWorkflowVariables(
            "initiateWorkflowRequest.variables",
            workflowDefinition,
            initiateWorkflowRequest.getVariables());
      }

      // Validate the workflow data if we have a validation schema
      if ((workflowDefinition.getValidationSchemaType() != null)
          && (StringUtils.hasText(workflowDefinition.getValidationSchema()))) {
        if (!validationService.validateWorkflowData(
            workflowDefinition.getValidationSchemaType(),
            workflowDefinition.getValidationSchema(),
            initiateWorkflowRequest.getData())) {
          throw new InvalidArgumentException("initiateWorkflowRequest.data");
        }
      }

      if (initiateWorkflowRequest.getInteractionLinks() != null) {
        // Validate the interaction links
        for (InitiateWorkflowInteractionLink initiateWorkflowInteractionLink :
            initiateWorkflowRequest.getInteractionLinks()) {
          if (!interactionService.interactionExists(
              tenantId, initiateWorkflowInteractionLink.getInteractionId())) {
            throw new InteractionNotFoundException(
                tenantId, initiateWorkflowInteractionLink.getInteractionId());
          }
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
              initiateWorkflowRequest.getDescription(),
              initiateWorkflowRequest.getExternalReferences(),
              initiateWorkflowRequest.getAttributes(),
              initiateWorkflowRequest.getVariables(),
              initiateWorkflowRequest.getData(),
              now,
              initiatedBy);

      if (initiateWorkflowRequest.getInteractionLinks() != null) {
        for (InitiateWorkflowInteractionLink initiateWorkflowInteractionLink :
            initiateWorkflowRequest.getInteractionLinks()) {
          workflow.addInteractionLink(
              new WorkflowInteractionLink(
                  initiateWorkflowInteractionLink.getInteractionId(),
                  initiateWorkflowInteractionLink.getConversationId(),
                  now,
                  initiatedBy));
        }
      }

      workflow.setPartyId(initiateWorkflowRequest.getPartyId());

      if (!initiateWorkflowRequest.getPendWorkflow()) {
        String engineInstanceId =
            getWorkflowEngineConnector(workflowDefinition.getEngineId())
                .startWorkflow(
                    workflowDefinition,
                    tenantId,
                    workflow.getId(),
                    initiateWorkflowRequest.getAttributes(),
                    initiateWorkflowRequest.getVariables(),
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
                  documentDefinition.isInternal(),
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
        if (stepDefinition.getCode().equals(initiateWorkflowStepRequest.getStep())) {
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

    validateArgument("linkInteractionToWorkflowRequest", linkInteractionToWorkflowRequest);

    if (!StringUtils.hasText(linkedBy)) {
      throw new InvalidArgumentException("linkedBy");
    }

    try {
      workflowStore.linkInteractionToWorkflow(
          tenantId,
          linkInteractionToWorkflowRequest.getWorkflowId(),
          linkInteractionToWorkflowRequest.getInteractionId(),
          linkInteractionToWorkflowRequest.getConversationId(),
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
  public UUID provideWorkflowDocument(
      UUID tenantId,
      ProvideWorkflowDocumentRequest provideWorkflowDocumentRequest,
      String providedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("provideWorkflowDocumentRequest", provideWorkflowDocumentRequest);

    if (!StringUtils.hasText(providedBy)) {
      throw new InvalidArgumentException("providedBy");
    }

    try {
      String documentDefinitionId =
          workflowStore.getDocumentDefinitionIdForWorkflowDocument(
              tenantId, provideWorkflowDocumentRequest.getWorkflowDocumentId());

      DocumentDefinition documentDefinition =
          documentService.getDocumentDefinition(documentDefinitionId);

      if (provideWorkflowDocumentRequest.getExternalReferences() != null) {
        // Validate the external references
        validationService.validateExternalReferences(
            tenantId,
            "provideWorkflowDocumentRequest.externalReferences",
            ObjectType.DOCUMENT,
            provideWorkflowDocumentRequest.getExternalReferences());
      }

      if (provideWorkflowDocumentRequest.getAttributes() != null) {
        // Validate the allowed document attributes
        validationService.validateAllowedDocumentAttributes(
            "provideWorkflowDocumentRequest.attributes",
            documentDefinition,
            provideWorkflowDocumentRequest.getAttributes());

        // Validate the required document attributes
        validationService.validateRequiredDocumentAttributes(
            "provideWorkflowDocumentRequest.attributes",
            documentDefinition,
            provideWorkflowDocumentRequest.getAttributes());
      }

      WorkflowDocument workflowDocument =
          workflowStore.provideWorkflowDocument(
              tenantId, provideWorkflowDocumentRequest, providedBy);

      eventService.publishEvent(
          tenantId,
          EventType.WORKFLOW_DOCUMENT_PROVIDED,
          ObjectType.WORKFLOW_DOCUMENT,
          workflowDocument.getId(),
          providedBy);

      return workflowDocument.getDocumentId();
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

    validateArgument("rejectWorkflowDocumentRequest", rejectWorkflowDocumentRequest);

    if (!StringUtils.hasText(rejectedBy)) {
      throw new InvalidArgumentException("rejectedBy");
    }

    try {
      workflowStore.rejectWorkflowDocument(tenantId, rejectWorkflowDocumentRequest, rejectedBy);

      eventService.publishEvent(
          tenantId,
          EventType.WORKFLOW_DOCUMENT_REJECTED,
          ObjectType.WORKFLOW_DOCUMENT,
          rejectWorkflowDocumentRequest.getWorkflowDocumentId(),
          rejectedBy);
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
          WorkflowNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("requestWorkflowDocumentRequest", requestWorkflowDocumentRequest);

    if (!StringUtils.hasText(requestedBy)) {
      throw new InvalidArgumentException("requestedBy");
    }

    try {
      WorkflowDefinitionId workflowDefinitionId =
          getWorkflowDefinitionIdForWorkflow(
              tenantId, requestWorkflowDocumentRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(
                  workflowDefinitionId.getId(), workflowDefinitionId.getVersion());

      Optional<WorkflowDefinitionDocumentDefinition> workflowDefinitionDocumentDefinitionOptional =
          workflowDefinition.getDocumentDefinition(
              requestWorkflowDocumentRequest.getDocumentDefinitionId());

      if (workflowDefinitionDocumentDefinitionOptional.isEmpty()) {
        throw new DocumentDefinitionNotFoundException(
            requestWorkflowDocumentRequest.getDocumentDefinitionId());
      }

      if (!documentService.documentDefinitionExists(
          requestWorkflowDocumentRequest.getDocumentDefinitionId())) {
        throw new DocumentDefinitionNotFoundException(
            requestWorkflowDocumentRequest.getDocumentDefinitionId());
      }

      WorkflowDocument workflowDocument =
          workflowStore.requestWorkflowDocument(
              tenantId,
              requestWorkflowDocumentRequest,
              workflowDefinitionDocumentDefinitionOptional.get(),
              requestedBy);

      eventService.publishEvent(
          tenantId,
          EventType.WORKFLOW_DOCUMENT_REQUESTED,
          ObjectType.WORKFLOW_DOCUMENT,
          workflowDocument.getId(),
          requestedBy);

      return workflowDocument.getId();
    } catch (WorkflowNotFoundException | DocumentDefinitionNotFoundException e) {
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
  public void resetWorkflowDocument(
      UUID tenantId, ResetWorkflowDocumentRequest resetWorkflowDocumentRequest)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("resetWorkflowDocumentRequest", resetWorkflowDocumentRequest);

    try {
      workflowStore.resetWorkflowDocument(tenantId, resetWorkflowDocumentRequest);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to reset the workflow document ("
              + resetWorkflowDocumentRequest.getWorkflowDocumentId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public WorkflowSummaries searchWorkflows(
      UUID tenantId, SearchWorkflowsRequest searchWorkflowsRequest)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("searchWorkflowsRequest", searchWorkflowsRequest);

    try {
      return workflowStore.searchWorkflows(tenantId, searchWorkflowsRequest, maxFilteredWorkflows);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to search for workflows for the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  public void setWorkflowStatus(UUID tenantId, UUID workflowId, WorkflowStatus status)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    if (status == null) {
      throw new InvalidArgumentException("status");
    }

    try {
      workflowStore.setWorkflowStatus(tenantId, workflowId, status);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to set the status for the workflow ("
              + workflowId
              + ") to ("
              + status
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

    if (!StringUtils.hasText(startedBy)) {
      throw new InvalidArgumentException("startedBy");
    }

    try {
      Workflow workflow = workflowStore.getWorkflow(tenantId, startWorkflowRequest.getWorkflowId());

      if (workflow.getStatus() != WorkflowStatus.PENDING) {
        throw new InvalidWorkflowStatusException(startWorkflowRequest.getWorkflowId());
      }

      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(
                  workflow.getDefinitionId(), workflow.getDefinitionVersion());

      // Start the workflow
      String engineInstanceId =
          getWorkflowEngineConnector(workflowDefinition.getEngineId())
              .startWorkflow(
                  workflowDefinition,
                  tenantId,
                  workflow.getId(),
                  workflow.getAttributes(),
                  workflow.getVariables(),
                  workflow.getData());

      workflow.setEngineInstanceId(engineInstanceId);
      workflow.setStatus(WorkflowStatus.ACTIVE);

      workflowStore.updateWorkflow(tenantId, workflow);
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
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          InvalidWorkflowStatusException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("suspendWorkflowRequest", suspendWorkflowRequest);

    if (!StringUtils.hasText(suspendedBy)) {
      throw new InvalidArgumentException("suspendedBy");
    }

    try {
      if (workflowStore.getWorkflowStatus(tenantId, suspendWorkflowRequest.getWorkflowId())
          != WorkflowStatus.ACTIVE) {
        throw new InvalidWorkflowStatusException(suspendWorkflowRequest.getWorkflowId());
      }

      WorkflowEngineIds workflowEngineIds =
          workflowStore.getWorkflowEngineIdsForWorkflow(
              tenantId, suspendWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowDefinitionForWorkflow(tenantId, suspendWorkflowRequest.getWorkflowId());

      getWorkflowEngineConnector(workflowEngineIds.getEngineId())
          .suspendWorkflow(
              workflowDefinition,
              tenantId,
              suspendWorkflowRequest.getWorkflowId(),
              workflowEngineIds.getEngineInstanceId());

      workflowStore.suspendWorkflow(tenantId, suspendWorkflowRequest.getWorkflowId(), suspendedBy);
    } catch (WorkflowNotFoundException | InvalidWorkflowStatusException e) {
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
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          InvalidWorkflowStatusException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("unsuspendWorkflowRequest", unsuspendWorkflowRequest);

    try {
      if (workflowStore.getWorkflowStatus(tenantId, unsuspendWorkflowRequest.getWorkflowId())
          != WorkflowStatus.SUSPENDED) {
        throw new InvalidWorkflowStatusException(unsuspendWorkflowRequest.getWorkflowId());
      }

      WorkflowEngineIds workflowEngineIds =
          workflowStore.getWorkflowEngineIdsForWorkflow(
              tenantId, unsuspendWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowDefinitionForWorkflow(tenantId, unsuspendWorkflowRequest.getWorkflowId());

      getWorkflowEngineConnector(workflowEngineIds.getEngineId())
          .unsuspendWorkflow(
              workflowDefinition,
              tenantId,
              unsuspendWorkflowRequest.getWorkflowId(),
              workflowEngineIds.getEngineInstanceId());

      workflowStore.unsuspendWorkflow(tenantId, unsuspendWorkflowRequest.getWorkflowId());
    } catch (WorkflowNotFoundException | InvalidWorkflowStatusException e) {
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

    if (!StringUtils.hasText(updatedBy)) {
      throw new InvalidArgumentException("updatedBy");
    }

    try {
      Workflow workflow =
          workflowStore.getWorkflow(tenantId, updateWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(
                  workflow.getDefinitionId(), workflow.getDefinitionVersion());

      // Validate the workflow data if we have a validation schema
      if ((workflowDefinition.getValidationSchemaType() != null)
          && (StringUtils.hasText(workflowDefinition.getValidationSchema()))) {
        if (!validationService.validateWorkflowData(
            workflowDefinition.getValidationSchemaType(),
            workflowDefinition.getValidationSchema(),
            updateWorkflowRequest.getData())) {
          throw new InvalidArgumentException("updateWorkflowRequest.data");
        }
      }

      if (updateWorkflowRequest.getExternalReferences() != null) {
        // Validate the external references
        validationService.validateExternalReferences(
            tenantId,
            "updateWorkflowRequest.externalReferences",
            ObjectType.WORKFLOW,
            updateWorkflowRequest.getExternalReferences());

        workflow.setExternalReferences(updateWorkflowRequest.getExternalReferences());
      }

      if (updateWorkflowRequest.getAttributes() != null) {
        // Validate the allowed workflow attributes
        validationService.validateAllowedWorkflowAttributes(
            "updateWorkflowRequest.attributes",
            workflowDefinition,
            updateWorkflowRequest.getAttributes());

        // Validate the required workflow attributes
        validationService.validateRequiredWorkflowAttributes(
            "updateWorkflowRequest.attributes",
            workflowDefinition,
            updateWorkflowRequest.getAttributes());

        workflow.setAttributes(updateWorkflowRequest.getAttributes());
      }

      if (updateWorkflowRequest.getVariables() != null) {
        // Validate the allowed workflow variables
        validationService.validateAllowedWorkflowVariables(
            "initiateWorkflowRequest.variables",
            workflowDefinition,
            updateWorkflowRequest.getVariables());

        // Validate the required workflow variables
        validationService.validateRequiredWorkflowVariables(
            "initiateWorkflowRequest.variables",
            workflowDefinition,
            updateWorkflowRequest.getVariables());

        workflow.setVariables(updateWorkflowRequest.getVariables());
      }

      if (StringUtils.hasText(updateWorkflowRequest.getData())) {
        workflow.setData(updateWorkflowRequest.getData());

        getWorkflowEngineConnector(workflowDefinition.getEngineId())
            .updateWorkflowData(
                workflowDefinition,
                tenantId,
                workflow.getId(),
                workflow.getEngineInstanceId(),
                updateWorkflowRequest.getData());
      }

      if (updateWorkflowRequest.getStatus() != null) {
        workflow.setStatus(updateWorkflowRequest.getStatus());
      }

      if (updateWorkflowRequest.getDescription() != null) {
        workflow.setDescription(updateWorkflowRequest.getDescription());
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
  @Caching(
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
          WorkflowEngineNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    validateArgument("workflowDefinition", workflowDefinition);

    try {
      if (!workflowDefinitionRepository.existsByIdAndVersion(
          workflowDefinition.getId(), workflowDefinition.getVersion())) {
        throw new WorkflowDefinitionVersionNotFoundException(
            workflowDefinition.getId(), workflowDefinition.getVersion());
      }

      validateWorkflowDefinition(workflowDefinition);

      workflowDefinitionRepository.saveAndFlush(workflowDefinition);
    } catch (WorkflowDefinitionCategoryNotFoundException
        | WorkflowDefinitionVersionNotFoundException
        | WorkflowEngineNotFoundException
        | DocumentDefinitionNotFoundException e) {
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

    if (!StringUtils.hasText(updatedBy)) {
      throw new InvalidArgumentException("updatedBy");
    }

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
  public void verifyWorkflowDocument(
      UUID tenantId, VerifyWorkflowDocumentRequest verifyWorkflowDocumentRequest, String verifiedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("verifyWorkflowDocumentRequest", verifyWorkflowDocumentRequest);

    if (!StringUtils.hasText(verifiedBy)) {
      throw new InvalidArgumentException("verifiedBy");
    }

    try {
      workflowStore.verifyWorkflowDocument(tenantId, verifyWorkflowDocumentRequest, verifiedBy);

      eventService.publishEvent(
          tenantId,
          EventType.WORKFLOW_DOCUMENT_VERIFIED,
          ObjectType.WORKFLOW_DOCUMENT,
          verifyWorkflowDocumentRequest.getWorkflowDocumentId(),
          verifiedBy);
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
  public void waiveWorkflowDocument(
      UUID tenantId, WaiveWorkflowDocumentRequest waiveWorkflowDocumentRequest, String waivedBy)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("waiveWorkflowDocumentRequest", waiveWorkflowDocumentRequest);

    if (!StringUtils.hasText(waivedBy)) {
      throw new InvalidArgumentException("waivedBy");
    }

    try {
      workflowStore.waiveWorkflowDocument(tenantId, waiveWorkflowDocumentRequest, waivedBy);

      eventService.publishEvent(
          tenantId,
          EventType.WORKFLOW_DOCUMENT_WAIVED,
          ObjectType.WORKFLOW_DOCUMENT,
          waiveWorkflowDocumentRequest.getWorkflowDocumentId(),
          waivedBy);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to waive the workflow document ("
              + waiveWorkflowDocumentRequest.getWorkflowDocumentId()
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

  private void validateWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionCategoryNotFoundException,
          WorkflowEngineNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!workflowDefinitionCategoryRepository.existsById(workflowDefinition.getCategoryId())) {
      throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinition.getCategoryId());
    }

    if (!workflowEngineRepository.existsById(workflowDefinition.getEngineId())) {
      throw new WorkflowEngineNotFoundException(workflowDefinition.getEngineId());
    }

    List<ValidWorkflowDefinitionAttribute> validWorkflowDefinitionAttributes =
        getWorkflowEngineConnector(workflowDefinition.getEngineId())
            .getValidWorkflowDefinitionAttributes();

    // Create lookup sets for efficient comparison
    Set<String> validWorkflowDefinitionAttributeCodes =
        validWorkflowDefinitionAttributes.stream()
            .map(ValidWorkflowDefinitionAttribute::getCode)
            .collect(Collectors.toSet());

    Set<String> providedWorkflowDefinitionAttributeCodes =
        workflowDefinition.getAttributes().stream()
            .map(WorkflowDefinitionAttribute::getCode)
            .collect(Collectors.toSet());

    // Check for missing required workflow definition attributes
    String missingRequiredAttribute =
        validWorkflowDefinitionAttributes.stream()
            .filter(ValidWorkflowDefinitionAttribute::isRequired)
            .map(ValidWorkflowDefinitionAttribute::getCode)
            .filter(code -> !providedWorkflowDefinitionAttributeCodes.contains(code))
            .findFirst()
            .orElse(null);

    if (missingRequiredAttribute != null) {
      throw new InvalidArgumentException(
          "workflowDefinition",
          "the workflow definition attribute (" + missingRequiredAttribute + ") is required");
    }

    // Check for invalid workflow definition attributes
    String invalidAttribute =
        providedWorkflowDefinitionAttributeCodes.stream()
            .filter(code -> !validWorkflowDefinitionAttributeCodes.contains(code))
            .findFirst()
            .orElse(null);

    if (invalidAttribute != null) {
      throw new InvalidArgumentException(
          "workflowDefinition",
          "the workflow definition attribute (" + invalidAttribute + ") is invalid");
    }

    // Validate document definitions if they exist
    for (WorkflowDefinitionDocumentDefinition documentDefinition :
        workflowDefinition.getDocumentDefinitions()) {
      if (!documentService.documentDefinitionExists(documentDefinition.getDocumentDefinitionId())) {
        throw new DocumentDefinitionNotFoundException(documentDefinition.getDocumentDefinitionId());
      }
    }
  }
}
