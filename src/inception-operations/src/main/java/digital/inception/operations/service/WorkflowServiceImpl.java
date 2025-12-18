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
import digital.inception.core.util.TokenReplacer;
import digital.inception.json.JsonClasspathResource;
import digital.inception.operations.connector.WorkflowEngineConnector;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.DuplicateWorkflowDocumentException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.DuplicateWorkflowException;
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
import digital.inception.operations.model.AttributeSearchCriteria;
import digital.inception.operations.model.CancelWorkflowRequest;
import digital.inception.operations.model.CreateWorkflowNoteRequest;
import digital.inception.operations.model.DeleteWorkflowStepRequest;
import digital.inception.operations.model.DelinkInteractionFromWorkflowRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.Event;
import digital.inception.operations.model.EventType;
import digital.inception.operations.model.ExternalReferenceSearchCriteria;
import digital.inception.operations.model.FinalizeWorkflowRequest;
import digital.inception.operations.model.FinalizeWorkflowStepRequest;
import digital.inception.operations.model.FormDefinition;
import digital.inception.operations.model.InitiateWorkflowInteractionLink;
import digital.inception.operations.model.InitiateWorkflowRequest;
import digital.inception.operations.model.InitiateWorkflowStepRequest;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.InteractionSummary;
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
import digital.inception.operations.model.VariableSearchCriteria;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.WaiveWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionAttribute;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionDocumentDefinition;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDefinitionPermission;
import digital.inception.operations.model.WorkflowDefinitionSummary;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocumentStatus;
import digital.inception.operations.model.WorkflowDocumentSummaries;
import digital.inception.operations.model.WorkflowDocumentSummary;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowEngineIds;
import digital.inception.operations.model.WorkflowExternalReference;
import digital.inception.operations.model.WorkflowFormType;
import digital.inception.operations.model.WorkflowInteractionLink;
import digital.inception.operations.model.WorkflowInteractionLinkId;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowStepDefinition;
import digital.inception.operations.model.WorkflowStepId;
import digital.inception.operations.model.WorkflowStepStatus;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.model.WorkflowSummary;
import digital.inception.operations.model.WorkflowVariable;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionCategoryRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionSummaryRepository;
import digital.inception.operations.persistence.jpa.WorkflowDocumentRepository;
import digital.inception.operations.persistence.jpa.WorkflowEngineRepository;
import digital.inception.operations.persistence.jpa.WorkflowInteractionLinkRepository;
import digital.inception.operations.persistence.jpa.WorkflowNoteRepository;
import digital.inception.operations.persistence.jpa.WorkflowRepository;
import digital.inception.operations.persistence.jpa.WorkflowStepRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  /** The Event Service. */
  private final EventService eventService;

  /** The Interaction Service. */
  private final InteractionService interactionService;

  /** The Validation Service. */
  private final ValidationService validationService;

  /** The Workflow Definition Category Repository. */
  private final WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository;

  /** The Workflow Definition Repository. */
  private final WorkflowDefinitionRepository workflowDefinitionRepository;

  /** The Workflow Definition Summary Repository. */
  private final WorkflowDefinitionSummaryRepository workflowDefinitionSummaryRepository;

  /** The Workflow Document Repository. */
  private final WorkflowDocumentRepository workflowDocumentRepository;

  /** The workflow engine connectors. */
  private final Map<String, WorkflowEngineConnector> workflowEngineConnectors =
      new ConcurrentHashMap<>();

  /** The Workflow Engine Repository. */
  private final WorkflowEngineRepository workflowEngineRepository;

  /** The Workflow Interaction Link Repository. */
  private final WorkflowInteractionLinkRepository workflowInteractionLinkRepository;

  /** The Workflow Note Repository. */
  private final WorkflowNoteRepository workflowNoteRepository;

  /** The Workflow Repository. */
  private final WorkflowRepository workflowRepository;

  /** The Workflow Step Repository. */
  private final WorkflowStepRepository workflowStepRepository;

  /* Entity Manager */
  @PersistenceContext(unitName = "operations")
  private EntityManager entityManager;

  /** The maximum number of filtered workflow documents that will be returned by the service. */
  @Value("${inception.operations.max-filtered-workflow-documents:#{100}}")
  private int maxFilteredWorkflowDocuments;

  /** The maximum number of filtered workflow notes that will be returned by the service. */
  @Value("${inception.operations.max-filtered-workflow-notes:#{100}}")
  private int maxFilteredWorkflowNotes;

  /** The maximum number of filtered workflows that will be returned by the service. */
  @Value("${inception.operations.max-filtered-workflows:#{100}}")
  private int maxFilteredWorkflows;

  /** Are we using Oracle? */
  private boolean usingOracle;

  /** The internal reference to the Workflow Service to enable caching. */
  private WorkflowService workflowService;

  /**
   * Constructs a new {@code WorkflowServiceImpl}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param applicationDataSource the application data source
   * @param workflowDefinitionCategoryRepository the Workflow Definition Category Repository
   * @param workflowDefinitionRepository the Workflow Definition Repository
   * @param workflowDefinitionSummaryRepository the Workflow Definition Summary Repository
   * @param workflowDocumentRepository the Workflow Document Repository
   * @param workflowEngineRepository the Workflow Engine Repository
   * @param workflowInteractionLinkRepository the Workflow Interaction Link Repository
   * @param workflowNoteRepository the Workflow Note Repository
   * @param workflowRepository the Workflow Repository
   * @param workflowStepRepository the Workflow Step Repository
   * @param documentService the Document Service
   * @param eventService the Event Service
   * @param interactionService the Interaction Service
   * @param validationService the Validation Service
   */
  public WorkflowServiceImpl(
      ApplicationContext applicationContext,
      @Qualifier("applicationDataSource") DataSource applicationDataSource,
      WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository,
      WorkflowDefinitionRepository workflowDefinitionRepository,
      WorkflowDefinitionSummaryRepository workflowDefinitionSummaryRepository,
      WorkflowDocumentRepository workflowDocumentRepository,
      WorkflowEngineRepository workflowEngineRepository,
      WorkflowInteractionLinkRepository workflowInteractionLinkRepository,
      WorkflowNoteRepository workflowNoteRepository,
      WorkflowRepository workflowRepository,
      WorkflowStepRepository workflowStepRepository,
      DocumentService documentService,
      EventService eventService,
      InteractionService interactionService,
      ValidationService validationService) {
    super(applicationContext);

    this.workflowDefinitionCategoryRepository = workflowDefinitionCategoryRepository;
    this.workflowDefinitionRepository = workflowDefinitionRepository;
    this.workflowDefinitionSummaryRepository = workflowDefinitionSummaryRepository;
    this.workflowDocumentRepository = workflowDocumentRepository;
    this.workflowEngineRepository = workflowEngineRepository;
    this.workflowInteractionLinkRepository = workflowInteractionLinkRepository;
    this.workflowNoteRepository = workflowNoteRepository;
    this.workflowRepository = workflowRepository;
    this.workflowStepRepository = workflowStepRepository;
    this.documentService = documentService;
    this.eventService = eventService;
    this.interactionService = interactionService;
    this.validationService = validationService;

    try {
      try (Connection connection = applicationDataSource.getConnection()) {
        DatabaseMetaData metaData = connection.getMetaData();

        String databaseProductName = metaData.getDatabaseProductName();
        String url = metaData.getURL();
        usingOracle =
            (databaseProductName != null && databaseProductName.toLowerCase().contains("oracle"))
                || (url != null && url.toLowerCase().startsWith("jdbc:oracle:"));
      }
    } catch (Throwable e) {
      log.warn("Failed to check if we are using Oracle", e);
      usingOracle = false;
    }
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
      if (!workflowRepository.existsByTenantIdAndId(
          tenantId, cancelWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, cancelWorkflowRequest.getWorkflowId());
      }

      WorkflowEngineIds workflowEngineIds =
          getWorkflowService()
              .getWorkflowEngineIdsForWorkflow(tenantId, cancelWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowDefinitionForWorkflow(tenantId, cancelWorkflowRequest.getWorkflowId());

      getWorkflowEngineConnector(workflowEngineIds.getEngineId())
          .cancelWorkflow(
              workflowDefinition,
              tenantId,
              cancelWorkflowRequest.getWorkflowId(),
              workflowEngineIds.getEngineInstanceId());

      OffsetDateTime now = OffsetDateTime.now();

      if (workflowRepository.cancelWorkflow(
              tenantId,
              cancelWorkflowRequest.getWorkflowId(),
              now,
              canceledBy,
              cancelWorkflowRequest.getCancellationReason())
          <= 0) {
        throw new WorkflowNotFoundException(tenantId, cancelWorkflowRequest.getWorkflowId());
      }

      // Cancel all the active workflow steps
      List<WorkflowStep> activeWorkflowSteps =
          workflowStepRepository.findByWorkflowIdAndStatus(
              cancelWorkflowRequest.getWorkflowId(), WorkflowStepStatus.ACTIVE);

      for (WorkflowStep activeWorkflowStep : activeWorkflowSteps) {
        workflowStepRepository.cancelWorkflowStep(
            cancelWorkflowRequest.getWorkflowId(), activeWorkflowStep.getCode(), now);
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
  }

  @Override
  public Workflow createWorkflow(UUID tenantId, Workflow workflow)
      throws DuplicateWorkflowException, ServiceUnavailableException {
    try {
      if (workflowRepository.existsById(workflow.getId())) {
        throw new DuplicateWorkflowException(workflow.getId());
      }

      return workflowRepository.saveAndFlush(workflow);
    } catch (DuplicateWorkflowException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow ("
              + workflow.getId()
              + ") with the definition ID ("
              + workflow.getDefinitionId()
              + ") and the definition version ("
              + workflow.getDefinitionVersion()
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
    } catch (InvalidArgumentException
        | DuplicateWorkflowDefinitionVersionException
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
  public WorkflowDocument createWorkflowDocument(UUID tenantId, WorkflowDocument workflowDocument)
      throws DuplicateWorkflowDocumentException, ServiceUnavailableException {
    try {
      if (workflowDocumentRepository.existsById(workflowDocument.getId())) {
        throw new DuplicateWorkflowDocumentException(workflowDocument.getId());
      }

      return workflowDocumentRepository.saveAndFlush(workflowDocument);
    } catch (DuplicateWorkflowDocumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow document ("
              + workflowDocument.getId()
              + ") for the tenant ("
              + tenantId
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

      return workflowNoteRepository.saveAndFlush(workflowNote);
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
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      workflowRepository.deleteById(workflowId);
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
      if (!workflowDocumentRepository.existsByTenantIdAndId(tenantId, workflowDocumentId)) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      }

      /*
       * NOTE: The search by both tenant ID and workflow document ID includes a security check to
       * ensure that the workflow document not only exists, but is also associated with the
       * specified tenant.
       */
      Optional<UUID> documentIdOptional =
          workflowDocumentRepository.findDocumentIdByTenantIdAndId(tenantId, workflowDocumentId);

      if (documentIdOptional.isPresent()) {
        UUID documentId = documentIdOptional.get();

        if (workflowDocumentRepository.countByDocumentId(documentId) == 1L) {
          try {
            documentService.deleteDocument(tenantId, documentId);
          } catch (DocumentNotFoundException ignored) {
          }
        }
      }

      workflowDocumentRepository.deleteById(workflowDocumentId);
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
      if (!workflowNoteRepository.existsByTenantIdAndId(tenantId, workflowNoteId)) {
        throw new WorkflowNoteNotFoundException(tenantId, workflowNoteId);
      }

      workflowNoteRepository.deleteById(workflowNoteId);
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
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowStepNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("deleteWorkflowStepRequest", deleteWorkflowStepRequest);

    try {
      if (!workflowRepository.existsByTenantIdAndId(
          tenantId, deleteWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, deleteWorkflowStepRequest.getWorkflowId());
      }

      WorkflowStepId workflowStepId =
          new WorkflowStepId(
              deleteWorkflowStepRequest.getWorkflowId(), deleteWorkflowStepRequest.getStep());

      if (!workflowStepRepository.existsById(workflowStepId)) {
        throw new WorkflowStepNotFoundException(
            tenantId,
            deleteWorkflowStepRequest.getWorkflowId(),
            deleteWorkflowStepRequest.getStep());
      }

      workflowStepRepository.deleteById(workflowStepId);
    } catch (WorkflowNotFoundException | WorkflowStepNotFoundException e) {
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
          WorkflowNotFoundException,
          WorkflowInteractionLinkNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("delinkInteractionFromWorkflowRequest", delinkInteractionFromWorkflowRequest);

    try {
      if (!workflowExists(tenantId, delinkInteractionFromWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(
            tenantId, delinkInteractionFromWorkflowRequest.getWorkflowId());
      }

      if (!workflowInteractionLinkRepository.existsById(
          new WorkflowInteractionLinkId(
              delinkInteractionFromWorkflowRequest.getWorkflowId(),
              delinkInteractionFromWorkflowRequest.getInteractionId()))) {
        throw new WorkflowInteractionLinkNotFoundException(
            tenantId,
            delinkInteractionFromWorkflowRequest.getWorkflowId(),
            delinkInteractionFromWorkflowRequest.getInteractionId());
      }

      workflowInteractionLinkRepository.deleteById(
          new WorkflowInteractionLinkId(
              delinkInteractionFromWorkflowRequest.getWorkflowId(),
              delinkInteractionFromWorkflowRequest.getInteractionId()));
    } catch (WorkflowNotFoundException | WorkflowInteractionLinkNotFoundException e) {
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
      if (workflowRepository.finalizeWorkflow(
              tenantId,
              finalizeWorkflowRequest.getWorkflowId(),
              finalizeWorkflowRequest.getStatus(),
              OffsetDateTime.now(),
              finalizedBy)
          <= 0) {
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
  }

  @Override
  public void finalizeWorkflowStep(
      UUID tenantId, FinalizeWorkflowStepRequest finalizeWorkflowStepRequest)
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowStepNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("finalizeWorkflowStepRequest", finalizeWorkflowStepRequest);

    try {
      if (!workflowRepository.existsByTenantIdAndId(
          tenantId, finalizeWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, finalizeWorkflowStepRequest.getWorkflowId());
      }

      if (StringUtils.hasText(finalizeWorkflowStepRequest.getNextStep())) {
        WorkflowDefinitionId workflowDefinitionId =
            getWorkflowDefinitionIdForWorkflow(
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

      if (workflowStepRepository.finalizeWorkflowStep(
              finalizeWorkflowStepRequest.getWorkflowId(),
              finalizeWorkflowStepRequest.getStep(),
              finalizeWorkflowStepRequest.getStatus(),
              OffsetDateTime.now())
          <= 0) {
        throw new WorkflowStepNotFoundException(
            tenantId,
            finalizeWorkflowStepRequest.getWorkflowId(),
            finalizeWorkflowStepRequest.getStep());
      }

      if (StringUtils.hasText(finalizeWorkflowStepRequest.getNextStep())) {
        WorkflowStep workflowStep =
            new WorkflowStep(
                finalizeWorkflowStepRequest.getWorkflowId(),
                finalizeWorkflowStepRequest.getNextStep(),
                WorkflowStepStatus.ACTIVE,
                OffsetDateTime.now());

        workflowStepRepository.saveAndFlush(workflowStep);
      }
    } catch (InvalidArgumentException
        | WorkflowNotFoundException
        | WorkflowStepNotFoundException e) {
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
      return workflowRepository.findActiveWorkflowIdsForTenantAndWorkflowEngine(
          tenantId, workflowEngineId);
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
  public String getDocumentDefinitionIdForWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws InvalidArgumentException,
          WorkflowDocumentNotFoundException,
          ServiceUnavailableException {
    if (workflowDocumentId == null) {
      throw new InvalidArgumentException("workflowDocumentId");
    }

    try {
      Optional<String> documentDefinitionIdOptional =
          workflowDocumentRepository.findDocumentDefinitionIdByTenantIdAndId(
              tenantId, workflowDocumentId);

      if (documentDefinitionIdOptional.isEmpty()) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      } else {
        return documentDefinitionIdOptional.get();
      }
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document definition ID for the workflow document ("
              + workflowDocumentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public InteractionSummaries getInteractionSummariesForWorkflow(
      UUID tenantId,
      UUID workflowId,
      String filter,
      InteractionSortBy sortBy,
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
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(workflowId);
      }

      // Build Specification-style predicate
      Specification<Interaction> specification =
          (root, query, criteriaBuilder) -> {
            // We may have joins / subqueries; avoid duplicates
            if (query != null) {
              query.distinct(true);
            }

            List<Predicate> andPredicates = new ArrayList<>();

            // Tenant filter
            // andPredicates.add(cb.equal(root.get("tenantId"), tenantId));

            // Filter: match sender OR subject (case-insensitive)
            if (filter != null && !filter.isBlank()) {
              String like = "%" + filter.toLowerCase() + "%";
              Predicate senderLike =
                  criteriaBuilder.like(criteriaBuilder.lower(root.get("sender")), like);
              Predicate subjectLike =
                  criteriaBuilder.like(criteriaBuilder.lower(root.get("subject")), like);

              andPredicates.add(criteriaBuilder.or(senderLike, subjectLike));
            }

            // Restrict to interactions linked to the given workflow
            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<WorkflowInteractionLink> wilRoot = subquery.from(WorkflowInteractionLink.class);

            subquery
                .select(wilRoot.get("interactionId"))
                .where(criteriaBuilder.equal(wilRoot.get("workflowId"), workflowId));

            andPredicates.add(root.get("id").in(subquery));

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
          };

      // Normalize paging parameters
      pageIndex = (pageIndex == null || pageIndex < 0) ? 0 : pageIndex;
      pageSize = (pageSize == null || pageSize < 1) ? 100 : pageSize;
      int firstResult = pageIndex * pageSize;

      // Normalize sort parameters
      sortBy = (sortBy != null) ? sortBy : InteractionSortBy.OCCURRED;
      sortDirection = (sortDirection != null) ? sortDirection : SortDirection.ASCENDING;

      String sortByPropertyName = InteractionSortBy.resolveSortByPropertyName(sortBy);

      // Retrieve the criteria builder
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

      // Data query (InteractionSummary projection, no counts)
      CriteriaQuery<InteractionSummary> dataCriteriaQuery =
          criteriaBuilder.createQuery(InteractionSummary.class);

      Root<Interaction> root = dataCriteriaQuery.from(Interaction.class);

      Predicate dataPredicate = specification.toPredicate(root, dataCriteriaQuery, criteriaBuilder);

      dataCriteriaQuery
          .select(
              criteriaBuilder.construct(
                  InteractionSummary.class,
                  root.get("id"),
                  root.get("tenantId"),
                  root.get("status"),
                  root.get("sourceId"),
                  root.get("conversationId"),
                  root.get("partyId"),
                  root.get("type"),
                  root.get("direction"),
                  root.get("sender"),
                  root.get("recipients"),
                  root.get("subject"),
                  root.get("mimeType"),
                  root.get("priority"),
                  root.get("occurred"),
                  root.get("assigned"),
                  root.get("assignedTo")))
          .where(dataPredicate);

      // Sorting
      if (sortDirection == SortDirection.ASCENDING) {
        dataCriteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortByPropertyName)));
      } else {
        dataCriteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortByPropertyName)));
      }

      TypedQuery<InteractionSummary> dataQuery = entityManager.createQuery(dataCriteriaQuery);
      dataQuery.setFirstResult(firstResult);
      dataQuery.setMaxResults(pageSize);

      List<InteractionSummary> interactionSummaries = dataQuery.getResultList();

      // Count query (for total)
      CriteriaQuery<Long> countCq = criteriaBuilder.createQuery(Long.class);
      Root<Interaction> countRoot = countCq.from(Interaction.class);

      Predicate countPredicate = specification.toPredicate(countRoot, countCq, criteriaBuilder);

      countCq.select(criteriaBuilder.countDistinct(countRoot.get("id"))).where(countPredicate);

      Long total = entityManager.createQuery(countCq).getSingleResult();

      return new InteractionSummaries(
          interactionSummaries, total, sortBy, sortDirection, pageIndex, pageSize);

    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the interaction summaries for the workflow ("
              + workflowId
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
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      return workflowDocumentRepository.findOutstandingWorkflowDocumentsForWorkflow(workflowId);
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
      /*
       * NOTE: The search by both tenant ID and workflow ID includes a security check to ensure
       * that the workflow not only exists, but is also associated with the specified tenant.
       */
      Optional<Workflow> workflowOptional =
          workflowRepository.findByTenantIdAndId(tenantId, workflowId);

      if (workflowOptional.isEmpty()) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      return workflowOptional.get();
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public Workflow getWorkflow(UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      Optional<Workflow> workflowOptional = workflowRepository.findById(workflowId);

      if (workflowOptional.isEmpty()) {
        throw new WorkflowNotFoundException(workflowId);
      }

      return workflowOptional.get();
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow (" + workflowId + ")", e);
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
      Optional<WorkflowDefinitionId> workflowDefinitionIdOptional =
          workflowRepository.findWorkflowDefinitionIdByWorkflowId(workflowId);

      if (workflowDefinitionIdOptional.isEmpty()) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      WorkflowDefinitionId workflowDefinitionId = workflowDefinitionIdOptional.get();

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
      Optional<WorkflowDefinitionId> workflowDefinitionIdOptional =
          workflowRepository.findWorkflowDefinitionIdByWorkflowId(workflowId);

      if (workflowDefinitionIdOptional.isEmpty()) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      } else {
        return workflowDefinitionIdOptional.get();
      }
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
      /*
       * NOTE: The search by both tenant ID and workflow document ID includes a security check to
       * ensure that the workflow document not only exists, but is also associated with the
       * specified tenant.
       */
      Optional<WorkflowDocument> workflowDocumentOptional =
          workflowDocumentRepository.findByTenantIdAndId(tenantId, workflowDocumentId);

      if (workflowDocumentOptional.isEmpty()) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      }

      return workflowDocumentOptional.get();
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
      if (!workflowDocumentRepository.existsByTenantIdAndId(tenantId, workflowDocumentId)) {
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
  public WorkflowDocumentSummaries getWorkflowDocumentSummariesForWorkflow(
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
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(workflowId);
      }

      // Build Specification-style predicate
      Specification<WorkflowDocument> specification =
          (root, query, criteriaBuilder) -> {
            // We may have joins; avoid duplicates
            if (query != null) {
              query.distinct(true);
            }

            List<Predicate> andPredicates = new ArrayList<>();

            // Tenant filter
            andPredicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

            // Restrict to the workflow
            andPredicates.add(criteriaBuilder.equal(root.get("workflowId"), workflowId));

            // Join to documentDefinition so we can filter and project
            Join<WorkflowDocument, DocumentDefinition> ddJoin =
                root.join("documentDefinition", JoinType.LEFT);

            // Optional filter (case-insensitive)
            if (StringUtils.hasText(filter)) {
              String like = "%" + filter.toLowerCase() + "%";

              andPredicates.add(
                  criteriaBuilder.or(
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("documentDefinitionId")),
                          "%" + filter.toLowerCase() + "%"),
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("requestedBy")),
                          "%" + filter.toLowerCase() + "%"),
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("providedBy")),
                          "%" + filter.toLowerCase() + "%"),
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("verifiedBy")),
                          "%" + filter.toLowerCase() + "%")));
            }

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
          };

      // Normalize paging parameters
      pageIndex = (pageIndex == null || pageIndex < 0) ? 0 : pageIndex;
      pageSize = (pageSize == null || pageSize < 1) ? 100 : pageSize;
      int firstResult = pageIndex * pageSize;

      // Normalize sort parameters
      sortBy = (sortBy != null) ? sortBy : WorkflowDocumentSortBy.REQUESTED;
      sortDirection = (sortDirection != null) ? sortDirection : SortDirection.ASCENDING;

      String sortByPropertyName = WorkflowDocumentSortBy.resolveSortByPropertyName(sortBy);

      // Retrieve the criteria builder
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

      // Data query (WorkflowDocumentSummary projection)
      CriteriaQuery<WorkflowDocumentSummary> dataCriteriaQuery =
          criteriaBuilder.createQuery(WorkflowDocumentSummary.class);

      Root<WorkflowDocument> root = dataCriteriaQuery.from(WorkflowDocument.class);
      Join<WorkflowDocument, DocumentDefinition> ddJoin =
          root.join("documentDefinition", JoinType.LEFT);

      Predicate dataPredicate = specification.toPredicate(root, dataCriteriaQuery, criteriaBuilder);

      dataCriteriaQuery
          .select(
              criteriaBuilder.construct(
                  WorkflowDocumentSummary.class,
                  root.get("id"),
                  root.get("tenantId"),
                  root.get("workflowId"),
                  root.get("documentDefinitionId"),
                  ddJoin.get("name"),
                  ddJoin.get("shortName"),
                  ddJoin.get("description"),
                  root.get("requested"),
                  root.get("requestedFromPartyId"),
                  root.get("requestedBy"),
                  root.get("provided"),
                  root.get("providedBy"),
                  root.get("rejected"),
                  root.get("rejectedBy"),
                  root.get("rejectionReason"),
                  root.get("verified"),
                  root.get("verifiedBy"),
                  root.get("waived"),
                  root.get("waivedBy"),
                  root.get("waiveReason"),
                  root.get("internal"),
                  root.get("status"),
                  root.get("documentId"),
                  root.get("description")))
          .where(dataPredicate);

      // Sorting (sortByPropertyName should resolve to a property on WorkflowDocument or its join)
      // Example: "requested" (on root) or "documentDefinition.name" (handle accordingly)
      if ("documentDefinition.name".equals(sortByPropertyName)) {
        if (sortDirection == SortDirection.ASCENDING) {
          dataCriteriaQuery.orderBy(criteriaBuilder.asc(ddJoin.get("name")));
        } else {
          dataCriteriaQuery.orderBy(criteriaBuilder.desc(ddJoin.get("name")));
        }
      } else {
        if (sortDirection == SortDirection.ASCENDING) {
          dataCriteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortByPropertyName)));
        } else {
          dataCriteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortByPropertyName)));
        }
      }

      TypedQuery<WorkflowDocumentSummary> dataQuery = entityManager.createQuery(dataCriteriaQuery);
      dataQuery.setFirstResult(firstResult);
      dataQuery.setMaxResults(pageSize);

      List<WorkflowDocumentSummary> workflowDocumentSummaries = dataQuery.getResultList();

      // Count query (for total)
      CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<WorkflowDocument> countRoot = countCriteriaQuery.from(WorkflowDocument.class);
      // If you need filters involving documentDefinition in count, join it again
      countRoot.join("documentDefinition", JoinType.LEFT);

      Predicate countPredicate =
          specification.toPredicate(countRoot, countCriteriaQuery, criteriaBuilder);

      countCriteriaQuery
          .select(criteriaBuilder.countDistinct(countRoot.get("id")))
          .where(countPredicate);

      Long total = entityManager.createQuery(countCriteriaQuery).getSingleResult();

      // Wrap in WorkflowDocumentSummaries
      return new WorkflowDocumentSummaries(
          workflowDocumentSummaries, total, sortBy, sortDirection, pageIndex, pageSize);

    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow document summaries for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public List<WorkflowDocument> getWorkflowDocuments(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    try {
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      return workflowDocumentRepository.findByWorkflowId(workflowId);
    } catch (WorkflowNotFoundException e) {
      throw e;

    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow documents for the workflow ("
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
      Optional<WorkflowEngineIds> workflowEngineIdsOptional =
          workflowRepository.findWorkflowEngineIdsForWorkflow(workflowId);

      if (workflowEngineIdsOptional.isEmpty()) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      return workflowEngineIdsOptional.get();
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
      Optional<UUID> workflowIdOptional =
          workflowDocumentRepository.findWorkflowIdByTenantIdAndId(tenantId, workflowDocumentId);

      if (workflowIdOptional.isEmpty()) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      } else {
        return workflowIdOptional.get();
      }
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
      /*
       * NOTE: The search by both tenant ID and workflow note ID includes a security check to ensure
       * that the workflow note not only exists, but is also associated with the specified tenant.
       */
      Optional<WorkflowNote> workflowNoteOptional =
          workflowNoteRepository.findByTenantIdAndId(tenantId, workflowNoteId);

      if (workflowNoteOptional.isEmpty()) {
        throw new WorkflowNoteNotFoundException(tenantId, workflowNoteId);
      }

      return workflowNoteOptional.get();
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
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      // Paging normalisation
      pageIndex = (pageIndex == null) ? 0 : Math.max(0, pageIndex);
      pageSize =
          (pageSize == null) ? 50 : Math.max(1, Math.min(pageSize, maxFilteredWorkflowNotes));

      // Sorting normalisation
      String sortProperty = WorkflowNoteSortBy.resolveSortByPropertyName(sortBy);
      Sort.Direction direction =
          (sortDirection == SortDirection.DESCENDING) ? Sort.Direction.DESC : Sort.Direction.ASC;

      PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, direction, sortProperty);

      // Specification
      Specification<WorkflowNote> spec =
          (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
            predicates.add(criteriaBuilder.equal(root.get("workflowId"), workflowId));

            if (StringUtils.hasText(filter)) {
              String f = filter.trim().toLowerCase();
              predicates.add(
                  criteriaBuilder.or(
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("createdBy")), "%" + f + "%"),
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("updatedBy")), "%" + f + "%")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
          };

      Page<WorkflowNote> workflowNotePage = workflowNoteRepository.findAll(spec, pageRequest);

      return new WorkflowNotes(
          workflowNotePage.getContent(),
          workflowNotePage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
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
      Optional<WorkflowStatus> workflowStatusOptional =
          workflowRepository.findWorkflowStatusByTenantIdAndWorkflowId(tenantId, workflowId);

      if (workflowStatusOptional.isPresent()) {
        return workflowStatusOptional.get();
      } else {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }
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
  @Transactional
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

      // Determine the name of the workflow
      String workflowName;

      if (StringUtils.hasText(initiateWorkflowRequest.getName())) {
        workflowName = initiateWorkflowRequest.getName();
      } else if (StringUtils.hasText(workflowDefinition.getNameTemplate())) {
        Map<String, String> templateParameters = new HashMap<>();

        for (WorkflowAttribute workflowAttribute : initiateWorkflowRequest.getAttributes()) {
          templateParameters.put(workflowAttribute.getName(), workflowAttribute.getValue());
        }

        TokenReplacer tokenReplacer = TokenReplacer.defaultStyle();

        workflowName =
            tokenReplacer.replace(workflowDefinition.getNameTemplate(), templateParameters);
      } else {
        workflowName = workflowDefinition.getName();
      }

      Workflow workflow =
          new Workflow(
              tenantId,
              initiateWorkflowRequest.getParentId(),
              workflowDefinition.getId(),
              workflowDefinition.getVersion(),
              workflowName,
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

      workflowRepository.saveAndFlush(workflow);

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

          workflowDocumentRepository.saveAndFlush(workflowDocument);
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
          getWorkflowDefinitionIdForWorkflow(tenantId, initiateWorkflowStepRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowService()
              .getWorkflowDefinitionVersion(
                  workflowDefinitionId.getId(), workflowDefinitionId.getVersion());

      boolean foundWorkflowStepDefinition = false;
      for (WorkflowStepDefinition stepDefinition : workflowDefinition.getStepDefinitions()) {
        if (stepDefinition.getCode().equals(initiateWorkflowStepRequest.getStep())) {
          foundWorkflowStepDefinition = true;
          break;
        }
      }

      if (!foundWorkflowStepDefinition) {
        throw new InvalidArgumentException("initiateWorkflowStepRequest.step");
      }

      WorkflowStep workflowStep =
          new WorkflowStep(
              initiateWorkflowStepRequest.getWorkflowId(),
              initiateWorkflowStepRequest.getStep(),
              WorkflowStepStatus.ACTIVE,
              OffsetDateTime.now());

      workflowStepRepository.saveAndFlush(workflowStep);

      return workflowStep;
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
      if (workflowInteractionLinkRepository.existsById(
          new WorkflowInteractionLinkId(
              linkInteractionToWorkflowRequest.getWorkflowId(),
              linkInteractionToWorkflowRequest.getInteractionId()))) {
        return;
      }

      if (!interactionService.interactionExists(
          tenantId, linkInteractionToWorkflowRequest.getInteractionId())) {
        throw new InteractionNotFoundException(
            tenantId, linkInteractionToWorkflowRequest.getInteractionId());
      }

      if (!workflowRepository.existsByTenantIdAndId(
          tenantId, linkInteractionToWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(
            tenantId, linkInteractionToWorkflowRequest.getWorkflowId());
      }

      workflowInteractionLinkRepository.saveAndFlush(
          new WorkflowInteractionLink(
              linkInteractionToWorkflowRequest.getWorkflowId(),
              linkInteractionToWorkflowRequest.getInteractionId(),
              linkInteractionToWorkflowRequest.getConversationId(),
              OffsetDateTime.now(),
              linkedBy));
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

    Document document = null;

    try {
      String documentDefinitionId =
          getDocumentDefinitionIdForWorkflowDocument(
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

      Optional<WorkflowDocument> workflowDocumentOptional =
          workflowDocumentRepository.findByTenantIdAndId(
              tenantId, provideWorkflowDocumentRequest.getWorkflowDocumentId());

      if (workflowDocumentOptional.isEmpty()) {
        throw new WorkflowDocumentNotFoundException(
            tenantId, provideWorkflowDocumentRequest.getWorkflowDocumentId());
      }

      WorkflowDocument workflowDocument = workflowDocumentOptional.get();

      /*
       * If we already have a document associated with this workflow document, and it is not
       * associated with any other workflow documents, then delete it.
       */
      if (workflowDocument.getDocumentId() != null) {
        if (workflowDocumentRepository.countByDocumentId(workflowDocument.getDocumentId()) == 1L) {
          documentService.deleteDocument(tenantId, workflowDocument.getDocumentId());
        }
      }

      // Create the new document
      document = new Document(workflowDocument.getDocumentDefinitionId());
      document.setCreated(OffsetDateTime.now());
      document.setCreatedBy(providedBy);
      document.setData(provideWorkflowDocumentRequest.getData());
      document.setFileType(provideWorkflowDocumentRequest.getFileType());
      document.setHash(documentService.calculateDataHash(provideWorkflowDocumentRequest.getData()));
      document.setName(provideWorkflowDocumentRequest.getName());
      document.setSourceDocumentId(provideWorkflowDocumentRequest.getSourceDocumentId());
      document.setTenantId(tenantId);

      if (provideWorkflowDocumentRequest.getAttributes() != null) {
        document.setAttributes(provideWorkflowDocumentRequest.getAttributes());
      }

      if (provideWorkflowDocumentRequest.getExternalReferences() != null) {
        document.setExternalReferences(provideWorkflowDocumentRequest.getExternalReferences());
      }

      documentService.createDocument(tenantId, document);

      /*
       * Associate the new document with the workflow document, set the workflow document status to
       * PROVIDED or VERIFIABLE depending on whether the workflow document is verifiable.
       */
      workflowDocument.setDocumentId(document.getId());
      workflowDocument.setProvided(OffsetDateTime.now());
      workflowDocument.setProvidedBy(providedBy);
      workflowDocument.setRejected(null);
      workflowDocument.setRejectedBy(null);
      workflowDocument.setRejectionReason(null);
      workflowDocument.setVerified(null);
      workflowDocument.setVerifiedBy(null);

      if (provideWorkflowDocumentRequest.getDescription() != null) {
        workflowDocument.setDescription(provideWorkflowDocumentRequest.getDescription());
      }

      boolean verifiable =
          workflowDocumentRepository.isWorkflowDocumentVerifiable(workflowDocument.getId());

      if (verifiable) {
        workflowDocument.setStatus(WorkflowDocumentStatus.VERIFIABLE);
      } else {
        workflowDocument.setStatus(WorkflowDocumentStatus.PROVIDED);
      }

      workflowDocumentRepository.saveAndFlush(workflowDocument);

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
      if (document != null) {
        try {
          documentService.deleteDocument(tenantId, document.getId());
        } catch (Throwable f) {
          log.error(
              "Failed to delete the orphaned document ("
                  + document.getId()
                  + ") for the tenant ("
                  + tenantId
                  + ") that was created when providing the workflow document",
              f);
        }
      }

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
      if (workflowDocumentRepository.rejectWorkflowDocument(
              rejectWorkflowDocumentRequest.getWorkflowDocumentId(),
              OffsetDateTime.now(),
              rejectedBy,
              rejectWorkflowDocumentRequest.getRejectionReason())
          == 0) {
        throw new WorkflowDocumentNotFoundException(
            tenantId, rejectWorkflowDocumentRequest.getWorkflowDocumentId());
      }

      if (rejectWorkflowDocumentRequest.getDescription() != null) {
        workflowDocumentRepository.updateWorkflowDocumentDescription(
            rejectWorkflowDocumentRequest.getWorkflowDocumentId(),
            rejectWorkflowDocumentRequest.getDescription());
      }

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

      WorkflowDefinitionDocumentDefinition workflowDefinitionDocumentDefinition =
          workflowDefinitionDocumentDefinitionOptional.get();

      if (!documentService.documentDefinitionExists(
          requestWorkflowDocumentRequest.getDocumentDefinitionId())) {
        throw new DocumentDefinitionNotFoundException(
            requestWorkflowDocumentRequest.getDocumentDefinitionId());
      }

      WorkflowDocument workflowDocument =
          new WorkflowDocument(
              tenantId,
              requestWorkflowDocumentRequest.getWorkflowId(),
              requestWorkflowDocumentRequest.getDocumentDefinitionId(),
              workflowDefinitionDocumentDefinition.isInternal(),
              OffsetDateTime.now(),
              requestedBy,
              requestWorkflowDocumentRequest.getRequestedFromPartyId());

      if (requestWorkflowDocumentRequest.getDescription() != null) {
        workflowDocument.setDescription(requestWorkflowDocumentRequest.getDescription());
      }

      workflowDocument = workflowDocumentRepository.saveAndFlush(workflowDocument);

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
  @Transactional
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
      if (!workflowDocumentRepository.existsByTenantIdAndId(
          tenantId, resetWorkflowDocumentRequest.getWorkflowDocumentId())) {
        throw new WorkflowDocumentNotFoundException(
            tenantId, resetWorkflowDocumentRequest.getWorkflowDocumentId());
      }

      /*
       * NOTE: The search by both tenant ID and workflow document ID includes a security check to
       * ensure that the workflow document not only exists, but is also associated with the
       * specified tenant.
       */
      Optional<UUID> documentIdOptional =
          workflowDocumentRepository.findDocumentIdByTenantIdAndId(
              tenantId, resetWorkflowDocumentRequest.getWorkflowDocumentId());

      if (documentIdOptional.isPresent()) {
        UUID documentId = documentIdOptional.get();

        if (workflowDocumentRepository.countByDocumentId(documentId) == 1L) {
          try {
            documentService.deleteDocument(tenantId, documentId);
          } catch (DocumentNotFoundException ignored) {
          }
        }
      }

      workflowDocumentRepository.resetWorkflowDocument(
          tenantId, resetWorkflowDocumentRequest.getWorkflowDocumentId());
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
      // Build Specification
      Specification<Workflow> specification =
          (root, query, criteriaBuilder) -> {
            // Avoid duplicates when joins or subqueries are involved
            query.distinct(true);

            // AND'ed top-level predicates
            List<Predicate> andPredicates = new ArrayList<>();

            // Tenant filter
            andPredicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

            // Top-level filters
            if (searchWorkflowsRequest.getId() != null) {
              andPredicates.add(
                  criteriaBuilder.equal(root.get("id"), searchWorkflowsRequest.getId()));
            }

            if (StringUtils.hasText(searchWorkflowsRequest.getDefinitionId())) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      criteriaBuilder.lower(root.get("definitionId")),
                      searchWorkflowsRequest.getDefinitionId().toLowerCase()));
            }

            if (searchWorkflowsRequest.getStatus() != null) {
              andPredicates.add(
                  criteriaBuilder.equal(root.get("status"), searchWorkflowsRequest.getStatus()));
            }

            if (searchWorkflowsRequest.getParentId() != null) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      root.get("parentId"), searchWorkflowsRequest.getParentId()));
            }

            if (searchWorkflowsRequest.getPartyId() != null) {
              andPredicates.add(
                  criteriaBuilder.equal(root.get("partyId"), searchWorkflowsRequest.getPartyId()));
            }

            if (StringUtils.hasText(searchWorkflowsRequest.getInitiatedBy())) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      criteriaBuilder.lower(root.get("initiatedBy")),
                      searchWorkflowsRequest.getInitiatedBy().toLowerCase()));
            }

            if (StringUtils.hasText(searchWorkflowsRequest.getUpdatedBy())) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      criteriaBuilder.lower(root.get("updatedBy")),
                      searchWorkflowsRequest.getUpdatedBy().toLowerCase()));
            }

            if (StringUtils.hasText(searchWorkflowsRequest.getFinalizedBy())) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      criteriaBuilder.lower(root.get("finalizedBy")),
                      searchWorkflowsRequest.getFinalizedBy().toLowerCase()));
            }

            if (StringUtils.hasText(searchWorkflowsRequest.getSuspendedBy())) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      criteriaBuilder.lower(root.get("suspendedBy")),
                      searchWorkflowsRequest.getSuspendedBy().toLowerCase()));
            }

            if (StringUtils.hasText(searchWorkflowsRequest.getCanceledBy())) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      criteriaBuilder.lower(root.get("canceledBy")),
                      searchWorkflowsRequest.getCanceledBy().toLowerCase()));
            }

            // Interaction ID criteria
            if (searchWorkflowsRequest.getInteractionId() != null) {
              var subQuery = query.subquery(Integer.class);
              var workflowInteractionLinkRoot = subQuery.from(WorkflowInteractionLink.class);
              Predicate subPredicate =
                  criteriaBuilder.equal(
                      workflowInteractionLinkRoot.get("workflowId"), root.get("id"));

              subPredicate =
                  criteriaBuilder.and(
                      subPredicate,
                      criteriaBuilder.equal(
                          workflowInteractionLinkRoot.get("interactionId"),
                          searchWorkflowsRequest.getInteractionId()));

              subQuery.select(criteriaBuilder.literal(1)).where(subPredicate);
              andPredicates.add(criteriaBuilder.exists(subQuery));
            }

            // Attribute criteria (OR all attribute pairs)
            if (searchWorkflowsRequest.getAttributes() != null
                && !searchWorkflowsRequest.getAttributes().isEmpty()) {
              List<Predicate> attributePredicates = new ArrayList<>();

              for (AttributeSearchCriteria attributeSearchCriteria :
                  searchWorkflowsRequest.getAttributes()) {
                if (attributeSearchCriteria == null) continue;

                var subQuery = query.subquery(Integer.class);
                var workflowAttributeRoot = subQuery.from(WorkflowAttribute.class);
                Predicate subPredicate =
                    criteriaBuilder.equal(workflowAttributeRoot.get("workflowId"), root.get("id"));

                if (StringUtils.hasText(attributeSearchCriteria.getName())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(workflowAttributeRoot.get("name")),
                              attributeSearchCriteria.getName().toLowerCase()));
                }
                if (StringUtils.hasText(attributeSearchCriteria.getValue())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(workflowAttributeRoot.get("value")),
                              attributeSearchCriteria.getValue().toLowerCase()));
                }

                subQuery.select(criteriaBuilder.literal(1)).where(subPredicate);
                attributePredicates.add(criteriaBuilder.exists(subQuery));
              }

              if (!attributePredicates.isEmpty()) {
                andPredicates.add(
                    criteriaBuilder.or(attributePredicates.toArray(new Predicate[0])));
              }
            }

            // External reference criteria (OR all external reference pairs)
            if (searchWorkflowsRequest.getExternalReferences() != null
                && !searchWorkflowsRequest.getExternalReferences().isEmpty()) {
              List<Predicate> externalReferencePredicates = new ArrayList<>();

              for (ExternalReferenceSearchCriteria externalReferenceSearchCriteria :
                  searchWorkflowsRequest.getExternalReferences()) {
                if (externalReferenceSearchCriteria == null) continue;

                var subQuery = query.subquery(Integer.class);
                var workflowExternalReferenceRoot = subQuery.from(WorkflowExternalReference.class);
                Predicate subPredicate =
                    criteriaBuilder.equal(
                        workflowExternalReferenceRoot.get("objectId"), root.get("id"));

                if (StringUtils.hasText(externalReferenceSearchCriteria.getType())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(workflowExternalReferenceRoot.get("type")),
                              externalReferenceSearchCriteria.getType().toLowerCase()));
                }
                if (StringUtils.hasText(externalReferenceSearchCriteria.getValue())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(workflowExternalReferenceRoot.get("value")),
                              externalReferenceSearchCriteria.getValue().toLowerCase()));
                }

                subQuery.select(criteriaBuilder.literal(1)).where(subPredicate);
                externalReferencePredicates.add(criteriaBuilder.exists(subQuery));
              }

              if (!externalReferencePredicates.isEmpty()) {
                andPredicates.add(
                    criteriaBuilder.or(externalReferencePredicates.toArray(new Predicate[0])));
              }
            }

            // Variable criteria (OR all variable pairs)
            if (searchWorkflowsRequest.getVariables() != null
                && !searchWorkflowsRequest.getVariables().isEmpty()) {
              List<Predicate> variablePredicates = new ArrayList<>();

              for (VariableSearchCriteria variableSearchCriteria :
                  searchWorkflowsRequest.getVariables()) {
                if (variableSearchCriteria == null) continue;

                var subQuery = query.subquery(Integer.class);
                var workflowVariableRoot = subQuery.from(WorkflowVariable.class);
                Predicate subPredicate =
                    criteriaBuilder.equal(workflowVariableRoot.get("workflowId"), root.get("id"));

                if (StringUtils.hasText(variableSearchCriteria.getName())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(workflowVariableRoot.get("name")),
                              variableSearchCriteria.getName().toLowerCase()));
                }
                if (StringUtils.hasText(variableSearchCriteria.getValue())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(workflowVariableRoot.get("value")),
                              variableSearchCriteria.getValue().toLowerCase()));
                }

                subQuery.select(criteriaBuilder.literal(1)).where(subPredicate);
                variablePredicates.add(criteriaBuilder.exists(subQuery));
              }

              if (!variablePredicates.isEmpty()) {
                andPredicates.add(criteriaBuilder.or(variablePredicates.toArray(new Predicate[0])));
              }
            }

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
          };

      // Paging
      int pageIndex =
          searchWorkflowsRequest.getPageIndex() == null
              ? 0
              : Math.max(0, searchWorkflowsRequest.getPageIndex());
      int pageSize =
          searchWorkflowsRequest.getPageSize() == null
              ? 50
              : Math.max(1, Math.min(searchWorkflowsRequest.getPageSize(), maxFilteredWorkflows));

      final int firstResult = pageIndex * pageSize;

      // Retrieve the criteria builder
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

      // Create a query using the WorkflowSummary projection
      CriteriaQuery<WorkflowSummary> dataCriteriaQuery =
          criteriaBuilder.createQuery(WorkflowSummary.class);

      // Create the root
      Root<Workflow> root = dataCriteriaQuery.from(Workflow.class);
      Root<WorkflowDefinition> definitionRoot = dataCriteriaQuery.from(WorkflowDefinition.class);

      // Apply the spec as predicate
      Predicate dataPredicate = specification.toPredicate(root, dataCriteriaQuery, criteriaBuilder);

      // Join Workflow -> WorkflowDefinition on (definitionId, definitionVersion)
      dataPredicate =
          criteriaBuilder.and(
              dataPredicate,
              criteriaBuilder.equal(definitionRoot.get("id"), root.get("definitionId")),
              criteriaBuilder.equal(definitionRoot.get("version"), root.get("definitionVersion")));

      // Build "effectiveDescription":
      // IF (workflow.description is not null AND length(trim(workflow.description)) > 0)
      //    THEN workflow.description
      //    ELSE workflowDefinition.description
      Expression<String> workflowDescription = root.get("description");
      Expression<Integer> workflowDescriptionTrimmed =
          criteriaBuilder.length(criteriaBuilder.trim(workflowDescription));

      Expression<String> effectiveDescription =
          criteriaBuilder
              .<String>selectCase()
              .when(
                  criteriaBuilder.and(
                      criteriaBuilder.isNotNull(workflowDescription),
                      criteriaBuilder.greaterThan(workflowDescriptionTrimmed, 0)),
                  workflowDescription)
              .otherwise(definitionRoot.get("description"));

      // Select only the fields required by WorkflowSummary
      dataCriteriaQuery
          .select(
              criteriaBuilder.construct(
                  WorkflowSummary.class,
                  root.get("id"),
                  root.get("tenantId"),
                  root.get("parentId"),
                  root.get("definitionId"),
                  root.get("definitionVersion"),
                  definitionRoot.get("name"),
                  root.get("name"),
                  root.get("status"),
                  root.get("initiated"),
                  root.get("initiatedBy"),
                  root.get("updated"),
                  root.get("updatedBy"),
                  root.get("finalized"),
                  root.get("finalizedBy"),
                  effectiveDescription))
          .where(dataPredicate);

      SortDirection sortDirection =
          (searchWorkflowsRequest.getSortDirection() != null)
              ? searchWorkflowsRequest.getSortDirection()
              : SortDirection.ASCENDING;

      if (sortDirection == SortDirection.ASCENDING) {
        dataCriteriaQuery.orderBy(
            criteriaBuilder.asc(
                root.get(
                    WorkflowSortBy.resolveSortByPropertyName(searchWorkflowsRequest.getSortBy()))));
      } else {
        dataCriteriaQuery.orderBy(
            criteriaBuilder.desc(
                root.get(
                    WorkflowSortBy.resolveSortByPropertyName(searchWorkflowsRequest.getSortBy()))));
      }

      TypedQuery<WorkflowSummary> dataQuery = entityManager.createQuery(dataCriteriaQuery);
      dataQuery.setFirstResult(firstResult);
      dataQuery.setMaxResults(pageSize);

      List<WorkflowSummary> workflowSummaries = dataQuery.getResultList();

      // Count query (for total elements)
      CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
      Root<Workflow> countRoot = countCriteriaQuery.from(Workflow.class);

      Predicate countPredicate =
          specification.toPredicate(countRoot, countCriteriaQuery, criteriaBuilder);

      // Because we used query.distinct(true) above due to joins, prefer countDistinct on the PK
      countCriteriaQuery
          .select(criteriaBuilder.countDistinct(countRoot.get("id")))
          .where(countPredicate);

      Long total = entityManager.createQuery(countCriteriaQuery).getSingleResult();

      return new WorkflowSummaries(
          workflowSummaries,
          total,
          searchWorkflowsRequest.getSortBy(),
          searchWorkflowsRequest.getSortDirection(),
          pageIndex,
          pageSize);
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
      OffsetDateTime now = OffsetDateTime.now();

      if (status == WorkflowStatus.COMPLETED) {
        if (workflowRepository.finalizeWorkflow(
                tenantId, workflowId, WorkflowStatus.COMPLETED, now, "SYSTEM")
            <= 0) {
          throw new WorkflowNotFoundException(tenantId, workflowId);
        }

        // Complete all the active workflow steps
        List<WorkflowStep> activeWorkflowSteps =
            workflowStepRepository.findByWorkflowIdAndStatus(workflowId, WorkflowStepStatus.ACTIVE);

        for (WorkflowStep activeWorkflowStep : activeWorkflowSteps) {
          workflowStepRepository.finalizeWorkflowStep(
              workflowId, activeWorkflowStep.getCode(), WorkflowStepStatus.COMPLETED, now);
        }
      } else if (status == WorkflowStatus.SUSPENDED) {
        if (workflowRepository.suspendWorkflow(tenantId, workflowId, now, "SYSTEM") <= 0) {
          throw new WorkflowNotFoundException(tenantId, workflowId);
        }

        // Suspend all the active workflow steps
        List<WorkflowStep> activeWorkflowSteps =
            workflowStepRepository.findByWorkflowIdAndStatus(workflowId, WorkflowStepStatus.ACTIVE);

        for (WorkflowStep activeWorkflowStep : activeWorkflowSteps) {
          workflowStepRepository.suspendWorkflowStep(workflowId, activeWorkflowStep.getCode(), now);
        }
      } else if (status == WorkflowStatus.TERMINATED) {
        if (workflowRepository.finalizeWorkflow(
                tenantId, workflowId, WorkflowStatus.TERMINATED, now, "SYSTEM")
            <= 0) {
          throw new WorkflowNotFoundException(tenantId, workflowId);
        }

        // Terminate all the active workflow steps
        List<WorkflowStep> activeWorkflowSteps =
            workflowStepRepository.findByWorkflowIdAndStatus(workflowId, WorkflowStepStatus.ACTIVE);

        for (WorkflowStep activeWorkflowStep : activeWorkflowSteps) {
          workflowStepRepository.finalizeWorkflowStep(
              workflowId, activeWorkflowStep.getCode(), WorkflowStepStatus.TERMINATED, now);
        }
      } else if (status == WorkflowStatus.FAILED) {
        if (workflowRepository.finalizeWorkflow(
                tenantId, workflowId, WorkflowStatus.FAILED, now, "SYSTEM")
            <= 0) {
          throw new WorkflowNotFoundException(tenantId, workflowId);
        }

        // Fail all the active workflow steps
        List<WorkflowStep> activeWorkflowSteps =
            workflowStepRepository.findByWorkflowIdAndStatus(workflowId, WorkflowStepStatus.ACTIVE);

        for (WorkflowStep activeWorkflowStep : activeWorkflowSteps) {
          workflowStepRepository.finalizeWorkflowStep(
              workflowId, activeWorkflowStep.getCode(), WorkflowStepStatus.FAILED, now);
        }
      }
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
      Workflow workflow = getWorkflow(tenantId, startWorkflowRequest.getWorkflowId());

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

      workflowRepository.save(workflow);
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
      if (!workflowRepository.existsByTenantIdAndId(
          tenantId, suspendWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, suspendWorkflowRequest.getWorkflowId());
      }

      if (getWorkflowStatus(tenantId, suspendWorkflowRequest.getWorkflowId())
          != WorkflowStatus.ACTIVE) {
        throw new InvalidWorkflowStatusException(suspendWorkflowRequest.getWorkflowId());
      }

      WorkflowEngineIds workflowEngineIds =
          getWorkflowEngineIdsForWorkflow(tenantId, suspendWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowDefinitionForWorkflow(tenantId, suspendWorkflowRequest.getWorkflowId());

      getWorkflowEngineConnector(workflowEngineIds.getEngineId())
          .suspendWorkflow(
              workflowDefinition,
              tenantId,
              suspendWorkflowRequest.getWorkflowId(),
              workflowEngineIds.getEngineInstanceId());

      OffsetDateTime now = OffsetDateTime.now();

      if (workflowRepository.suspendWorkflow(
              tenantId, suspendWorkflowRequest.getWorkflowId(), now, suspendedBy)
          <= 0) {
        throw new WorkflowNotFoundException(tenantId, suspendWorkflowRequest.getWorkflowId());
      }

      // Suspend all the active workflow steps
      List<WorkflowStep> activeWorkflowSteps =
          workflowStepRepository.findByWorkflowIdAndStatus(
              suspendWorkflowRequest.getWorkflowId(), WorkflowStepStatus.ACTIVE);

      for (WorkflowStep activeWorkflowStep : activeWorkflowSteps) {
        workflowStepRepository.suspendWorkflowStep(
            suspendWorkflowRequest.getWorkflowId(), activeWorkflowStep.getCode(), now);
      }
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
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowStepNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("suspendWorkflowStepRequest", suspendWorkflowStepRequest);

    try {
      if (!workflowRepository.existsByTenantIdAndId(
          tenantId, suspendWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, suspendWorkflowStepRequest.getWorkflowId());
      }

      if (workflowStepRepository.suspendWorkflowStep(
              suspendWorkflowStepRequest.getWorkflowId(),
              suspendWorkflowStepRequest.getStep(),
              OffsetDateTime.now())
          <= 0) {
        throw new WorkflowStepNotFoundException(
            tenantId,
            suspendWorkflowStepRequest.getWorkflowId(),
            suspendWorkflowStepRequest.getStep());
      }
    } catch (WorkflowNotFoundException | WorkflowStepNotFoundException e) {
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
      if (!workflowRepository.existsByTenantIdAndId(
          tenantId, unsuspendWorkflowRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, unsuspendWorkflowRequest.getWorkflowId());
      }

      if (getWorkflowStatus(tenantId, unsuspendWorkflowRequest.getWorkflowId())
          != WorkflowStatus.SUSPENDED) {
        throw new InvalidWorkflowStatusException(unsuspendWorkflowRequest.getWorkflowId());
      }

      WorkflowEngineIds workflowEngineIds =
          getWorkflowEngineIdsForWorkflow(tenantId, unsuspendWorkflowRequest.getWorkflowId());

      WorkflowDefinition workflowDefinition =
          getWorkflowDefinitionForWorkflow(tenantId, unsuspendWorkflowRequest.getWorkflowId());

      getWorkflowEngineConnector(workflowEngineIds.getEngineId())
          .unsuspendWorkflow(
              workflowDefinition,
              tenantId,
              unsuspendWorkflowRequest.getWorkflowId(),
              workflowEngineIds.getEngineInstanceId());

      if (workflowRepository.unsuspendWorkflow(tenantId, unsuspendWorkflowRequest.getWorkflowId())
          <= 0) {
        throw new WorkflowNotFoundException(tenantId, unsuspendWorkflowRequest.getWorkflowId());
      }

      // Unsuspend all the suspended workflow steps
      List<WorkflowStep> suspendedWorkflowSteps =
          workflowStepRepository.findByWorkflowIdAndStatus(
              unsuspendWorkflowRequest.getWorkflowId(), WorkflowStepStatus.SUSPENDED);

      for (WorkflowStep suspendedWorkflowStep : suspendedWorkflowSteps) {
        workflowStepRepository.unsuspendWorkflowStep(
            unsuspendWorkflowRequest.getWorkflowId(), suspendedWorkflowStep.getCode());
      }
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
      throws InvalidArgumentException,
          WorkflowNotFoundException,
          WorkflowStepNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("unsuspendWorkflowStepRequest", unsuspendWorkflowStepRequest);

    try {
      if (!workflowRepository.existsByTenantIdAndId(
          tenantId, unsuspendWorkflowStepRequest.getWorkflowId())) {
        throw new WorkflowNotFoundException(tenantId, unsuspendWorkflowStepRequest.getWorkflowId());
      }

      if (workflowStepRepository.unsuspendWorkflowStep(
              unsuspendWorkflowStepRequest.getWorkflowId(), unsuspendWorkflowStepRequest.getStep())
          <= 0) {
        throw new WorkflowStepNotFoundException(
            tenantId,
            unsuspendWorkflowStepRequest.getWorkflowId(),
            unsuspendWorkflowStepRequest.getStep());
      }
    } catch (WorkflowNotFoundException | WorkflowStepNotFoundException e) {
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
      Workflow workflow = getWorkflow(tenantId, updateWorkflowRequest.getWorkflowId());

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

      if (StringUtils.hasText(updateWorkflowRequest.getName())) {
        workflow.setName(updateWorkflowRequest.getName());
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

      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflow.getId())) {
        throw new WorkflowNotFoundException(tenantId, workflow.getId());
      }

      return workflowRepository.saveAndFlush(workflow);
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
          getWorkflowNote(tenantId, updateWorkflowNoteRequest.getWorkflowNoteId());

      workflowNote.setContent(updateWorkflowNoteRequest.getContent());
      workflowNote.setUpdated(OffsetDateTime.now());
      workflowNote.setUpdatedBy(updatedBy);

      if (!workflowNoteRepository.existsByTenantIdAndId(tenantId, workflowNote.getId())) {
        throw new WorkflowNoteNotFoundException(tenantId, workflowNote.getId());
      }

      return workflowNoteRepository.saveAndFlush(workflowNote);
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
      if (workflowDocumentRepository.verifyWorkflowDocument(
              verifyWorkflowDocumentRequest.getWorkflowDocumentId(),
              OffsetDateTime.now(),
              verifiedBy)
          == 0) {
        throw new WorkflowDocumentNotFoundException(
            tenantId, verifyWorkflowDocumentRequest.getWorkflowDocumentId());
      }

      if (verifyWorkflowDocumentRequest.getDescription() != null) {
        workflowDocumentRepository.updateWorkflowDocumentDescription(
            verifyWorkflowDocumentRequest.getWorkflowDocumentId(),
            verifyWorkflowDocumentRequest.getDescription());
      }

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
      if (workflowDocumentRepository.waiveWorkflowDocument(
              waiveWorkflowDocumentRequest.getWorkflowDocumentId(),
              OffsetDateTime.now(),
              waivedBy,
              waiveWorkflowDocumentRequest.getWaiveReason())
          == 0) {
        throw new WorkflowDocumentNotFoundException(
            tenantId, waiveWorkflowDocumentRequest.getWorkflowDocumentId());
      }

      if (waiveWorkflowDocumentRequest.getDescription() != null) {
        workflowDocumentRepository.updateWorkflowDocumentDescription(
            waiveWorkflowDocumentRequest.getWorkflowDocumentId(),
            waiveWorkflowDocumentRequest.getDescription());
      }

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
      return workflowDocumentRepository.existsByTenantIdAndWorkflowIdAndId(
          tenantId, workflowId, workflowDocumentId);
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
      return workflowRepository.existsByTenantIdAndId(tenantId, workflowId);
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
      return workflowNoteRepository.existsByTenantIdAndWorkflowIdAndId(
          tenantId, workflowId, workflowNoteId);
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

  private Sort.Direction resolveSortDirection(
      digital.inception.core.sorting.SortDirection sortDirection) {
    if (sortDirection == null) {
      return Sort.Direction.DESC;
    } else if (sortDirection == SortDirection.ASCENDING) {
      return Sort.Direction.ASC;
    } else {
      return Sort.Direction.DESC;
    }
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
            .map(WorkflowDefinitionAttribute::getName)
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
