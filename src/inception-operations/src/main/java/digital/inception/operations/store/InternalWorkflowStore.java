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

package digital.inception.operations.store;

import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DuplicateWorkflowDocumentException;
import digital.inception.operations.exception.DuplicateWorkflowException;
import digital.inception.operations.exception.DuplicateWorkflowNoteException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
import digital.inception.operations.exception.WorkflowInteractionLinkNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.exception.WorkflowStepNotFoundException;
import digital.inception.operations.model.AttributeSearchCriteria;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.ExternalReferenceSearchCriteria;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.SearchWorkflowsRequest;
import digital.inception.operations.model.VariableSearchCriteria;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowDefinitionDocumentDefinition;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocumentStatus;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowEngineIds;
import digital.inception.operations.model.WorkflowExternalReference;
import digital.inception.operations.model.WorkflowInteractionLink;
import digital.inception.operations.model.WorkflowInteractionLinkId;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowStepId;
import digital.inception.operations.model.WorkflowStepStatus;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.model.WorkflowSummary;
import digital.inception.operations.model.WorkflowVariable;
import digital.inception.operations.persistence.jpa.WorkflowDocumentRepository;
import digital.inception.operations.persistence.jpa.WorkflowInteractionLinkRepository;
import digital.inception.operations.persistence.jpa.WorkflowNoteRepository;
import digital.inception.operations.persistence.jpa.WorkflowRepository;
import digital.inception.operations.persistence.jpa.WorkflowStepRepository;
import digital.inception.operations.persistence.jpa.WorkflowSummaryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The {@code InternalWorkflowStore} class provides the internal workflow store implementation.
 *
 * @author Marcus Portmann
 */
@Component
@Conditional(InternalWorkflowStoreEnabledCondition.class)
@SuppressWarnings("unused")
public class InternalWorkflowStore implements WorkflowStore {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(InternalWorkflowStore.class);

  /** The Document Store. */
  private final DocumentStore documentStore;

  /** The Interaction Store. */
  private final InteractionStore interactionStore;

  /** The Workflow Document Repository. */
  private final WorkflowDocumentRepository workflowDocumentRepository;

  /** The Workflow Interaction Link Repository. */
  private final WorkflowInteractionLinkRepository workflowInteractionLinkRepository;

  /** The Workflow Note Repository. */
  private final WorkflowNoteRepository workflowNoteRepository;

  /** The Workflow Repository. */
  private final WorkflowRepository workflowRepository;

  /** The Workflow Step Repository. */
  private final WorkflowStepRepository workflowStepRepository;

  /** The Workflow Summary Repository. */
  private final WorkflowSummaryRepository workflowSummaryRepository;

  /* Entity Manager */
  @PersistenceContext(unitName = "operations")
  private EntityManager entityManager;

  /** Are we using Oracle? */
  private boolean usingOracle;

  /**
   * Constructs a new {@code InternalWorkflowStore}.
   *
   * @param dataSource the application data source
   * @param workflowDocumentRepository the Workflow Document Repository
   * @param workflowInteractionLinkRepository the Workflow Interaction Link Repository
   * @param workflowNoteRepository the Workflow Note Repository
   * @param workflowRepository the Workflow Repository
   * @param workflowStepRepository the Workflow Step Repository
   * @param workflowSummaryRepository the Workflow Summary Repository
   * @param documentStore the Document Store
   * @param interactionStore the Interaction Store
   */
  public InternalWorkflowStore(
      @Qualifier("applicationDataSource") DataSource dataSource,
      WorkflowDocumentRepository workflowDocumentRepository,
      WorkflowInteractionLinkRepository workflowInteractionLinkRepository,
      WorkflowNoteRepository workflowNoteRepository,
      WorkflowRepository workflowRepository,
      WorkflowStepRepository workflowStepRepository,
      WorkflowSummaryRepository workflowSummaryRepository,
      DocumentStore documentStore,
      InteractionStore interactionStore) {
    this.workflowDocumentRepository = workflowDocumentRepository;
    this.workflowInteractionLinkRepository = workflowInteractionLinkRepository;
    this.workflowNoteRepository = workflowNoteRepository;
    this.workflowRepository = workflowRepository;
    this.workflowStepRepository = workflowStepRepository;
    this.workflowSummaryRepository = workflowSummaryRepository;
    this.documentStore = documentStore;
    this.interactionStore = interactionStore;

    try {
      try (Connection connection = dataSource.getConnection()) {
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
      UUID tenantId, UUID workflowId, String canceledBy, String cancellationReason)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      OffsetDateTime now = OffsetDateTime.now();

      if (workflowRepository.cancelWorkflow(
              tenantId, workflowId, now, canceledBy, cancellationReason)
          <= 0) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      // Cancel all the active workflow steps
      List<WorkflowStep> activeWorkflowSteps =
          workflowStepRepository.findByWorkflowIdAndStatus(workflowId, WorkflowStepStatus.ACTIVE);

      for (WorkflowStep activeWorkflowStep : activeWorkflowSteps) {
        workflowStepRepository.cancelWorkflowStep(workflowId, activeWorkflowStep.getCode(), now);
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to cancel the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
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
  public WorkflowNote createWorkflowNote(UUID tenantId, WorkflowNote workflowNote)
      throws DuplicateWorkflowNoteException, ServiceUnavailableException {
    try {
      if (workflowNoteRepository.existsById(workflowNote.getId())) {
        throw new DuplicateWorkflowNoteException(workflowNote.getId());
      }

      return workflowNoteRepository.saveAndFlush(workflowNote);
    } catch (DuplicateWorkflowNoteException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow note ("
              + workflowNote.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException {
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
  public void deleteWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {
    try {
      /*
       * NOTE: The search by both tenant ID and workflow document ID includes a security check to
       * ensure that the workflow document not only exists, but is also associated with the
       * specified tenant.
       */
      Optional<UUID> documentIdOptional =
          workflowDocumentRepository.findDocumentIdByTenantIdAndId(tenantId, workflowDocumentId);

      if (documentIdOptional.isEmpty()) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      }
      UUID documentId = documentIdOptional.get();

      if (workflowDocumentRepository.countByDocumentId(documentId) == 1L) {
        try {
          documentStore.deleteDocument(tenantId, documentId);
        } catch (DocumentNotFoundException ignored) {
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
  public void deleteWorkflowNote(UUID tenantId, UUID workflowNoteId)
      throws WorkflowNoteNotFoundException, ServiceUnavailableException {
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
  public void deleteWorkflowStep(UUID tenantId, UUID workflowId, String step)
      throws WorkflowStepNotFoundException, ServiceUnavailableException {
    try {
      WorkflowStepId workflowStepId = new WorkflowStepId(workflowId, step);

      if (!workflowStepRepository.existsById(workflowStepId)) {
        throw new WorkflowStepNotFoundException(tenantId, workflowId, step);
      }

      workflowStepRepository.deleteById(workflowStepId);
    } catch (WorkflowStepNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow step ("
              + step
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void delinkInteractionFromWorkflow(UUID tenantId, UUID workflowId, UUID interactionId)
      throws InteractionNotFoundException,
          WorkflowNotFoundException,
          WorkflowInteractionLinkNotFoundException,
          ServiceUnavailableException {
    try {
      if (!interactionStore.interactionExists(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      if (!workflowExists(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      if (!workflowInteractionLinkRepository.existsById(
          new WorkflowInteractionLinkId(workflowId, interactionId))) {
        throw new WorkflowInteractionLinkNotFoundException(tenantId, workflowId, interactionId);
      }

      workflowInteractionLinkRepository.deleteById(
          new WorkflowInteractionLinkId(workflowId, interactionId));
    } catch (InteractionNotFoundException
        | WorkflowNotFoundException
        | WorkflowInteractionLinkNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delink the interaction ("
              + interactionId
              + ") from the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void finalizeWorkflow(
      UUID tenantId, UUID workflowId, WorkflowStatus status, String finalizedBy)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (workflowRepository.finalizeWorkflow(
              tenantId, workflowId, status, OffsetDateTime.now(), finalizedBy)
          <= 0) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to finalize the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public void finalizeWorkflowStep(
      UUID tenantId, UUID workflowId, String step, WorkflowStepStatus status, String nextStep)
      throws WorkflowStepNotFoundException, ServiceUnavailableException {
    try {
      if (workflowStepRepository.finalizeWorkflowStep(
              workflowId, step, status, OffsetDateTime.now())
          <= 0) {
        throw new WorkflowStepNotFoundException(tenantId, workflowId, step);
      }

      if (StringUtils.hasText(nextStep)) {
        WorkflowStep workflowStep =
            new WorkflowStep(workflowId, nextStep, WorkflowStepStatus.ACTIVE, OffsetDateTime.now());

        workflowStepRepository.saveAndFlush(workflowStep);
      }
    } catch (WorkflowStepNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to finalize the workflow step ("
              + step
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public List<UUID> getActiveWorkflowIdsForWorkflowEngine(UUID tenantId, String workflowEngineId)
      throws ServiceUnavailableException {
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
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {
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
              + ")",
          e);
    }
  }

  @Override
  public List<OutstandingWorkflowDocument> getOutstandingWorkflowDocuments(
      UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException {
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
  public Workflow getWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException {
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
  public WorkflowDefinitionId getWorkflowDefinitionIdForWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException {
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
  public WorkflowDocument getWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {
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
  public WorkflowDocuments getWorkflowDocuments(
      UUID tenantId,
      UUID workflowId,
      String filter,
      WorkflowDocumentSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      PageRequest pageRequest;

      if (sortBy == WorkflowDocumentSortBy.REQUESTED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "requested");
      } else if (sortBy == WorkflowDocumentSortBy.REQUESTED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "requestedBy");
      } else if (sortBy == WorkflowDocumentSortBy.PROVIDED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "provided");
      } else if (sortBy == WorkflowDocumentSortBy.PROVIDED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "providedBy");
      } else if (sortBy == WorkflowDocumentSortBy.VERIFIED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "verified");
      } else if (sortBy == WorkflowDocumentSortBy.VERIFIED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "verifiedBy");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "requested");
      }

      Page<WorkflowDocument> workflowDocumentPage =
          workflowDocumentRepository.findAll(
              (Specification<WorkflowDocument>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
                    predicates.add(criteriaBuilder.equal(root.get("workflowId"), workflowId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
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

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new WorkflowDocuments(
          workflowDocumentPage.toList(),
          workflowDocumentPage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
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
  public UUID getWorkflowIdForWorkflowDocument(UUID tenantId, UUID workflowDocumentId)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {
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
      throws WorkflowNoteNotFoundException, ServiceUnavailableException {
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
      Integer pageSize,
      int maxResults)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      PageRequest pageRequest;

      if (sortBy == WorkflowNoteSortBy.CREATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "created");
      } else if (sortBy == WorkflowNoteSortBy.CREATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "createdBy");
      } else if (sortBy == WorkflowNoteSortBy.UPDATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updated");
      } else if (sortBy == WorkflowNoteSortBy.UPDATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updatedBy");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "created");
      }

      Page<WorkflowNote> workflowNotePage =
          workflowNoteRepository.findAll(
              (Specification<WorkflowNote>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
                    predicates.add(criteriaBuilder.equal(root.get("workflowId"), workflowId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("createdBy")),
                                  "%" + filter.toLowerCase() + "%"),
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("updatedBy")),
                                  "%" + filter.toLowerCase() + "%")));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new WorkflowNotes(
          workflowNotePage.toList(),
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
  public WorkflowStep initiateWorkflowStep(UUID tenantId, UUID workflowId, String step)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      WorkflowStep workflowStep =
          new WorkflowStep(workflowId, step, WorkflowStepStatus.ACTIVE, OffsetDateTime.now());

      workflowStepRepository.saveAndFlush(workflowStep);

      return workflowStep;
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to initiate the workflow step ("
              + step
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void linkInteractionToWorkflow(
      UUID tenantId, UUID workflowId, UUID interactionId, String conversationId, String linkedBy)
      throws InteractionNotFoundException, WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (workflowInteractionLinkRepository.existsById(
          new WorkflowInteractionLinkId(workflowId, interactionId))) {
        return;
      }

      if (!interactionStore.interactionExists(tenantId, interactionId)) {
        throw new InteractionNotFoundException(tenantId, interactionId);
      }

      if (!workflowExists(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      workflowInteractionLinkRepository.saveAndFlush(
          new WorkflowInteractionLink(
              workflowId, interactionId, conversationId, OffsetDateTime.now(), linkedBy));
    } catch (InteractionNotFoundException | WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to link the interaction ("
              + interactionId
              + ") to the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public WorkflowDocument provideWorkflowDocument(
      UUID tenantId,
      ProvideWorkflowDocumentRequest provideWorkflowDocumentRequest,
      String providedBy)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {

    Document document = null;

    try {
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
          documentStore.deleteDocument(tenantId, workflowDocument.getDocumentId());
        }
      }

      // Create the new document
      document = new Document(workflowDocument.getDocumentDefinitionId());
      document.setCreated(OffsetDateTime.now());
      document.setCreatedBy(providedBy);
      document.setData(provideWorkflowDocumentRequest.getData());
      document.setExpiryDate(provideWorkflowDocumentRequest.getExpiryDate());
      document.setFileType(provideWorkflowDocumentRequest.getFileType());
      document.setHash(
          documentStore.calculateDocumentDataHash(provideWorkflowDocumentRequest.getData()));
      document.setIssueDate(provideWorkflowDocumentRequest.getIssueDate());
      document.setName(provideWorkflowDocumentRequest.getName());
      document.setSourceDocumentId(provideWorkflowDocumentRequest.getSourceDocumentId());
      document.setTenantId(tenantId);

      if (provideWorkflowDocumentRequest.getAttributes() != null) {
        document.setAttributes(provideWorkflowDocumentRequest.getAttributes());
      }

      if (provideWorkflowDocumentRequest.getExternalReferences() != null) {
        document.setExternalReferences(provideWorkflowDocumentRequest.getExternalReferences());
      }

      documentStore.createDocument(tenantId, document);

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

      return workflowDocument;
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      if (document != null) {
        try {
          documentStore.deleteDocument(tenantId, document.getId());
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

      throw new ServiceUnavailableException(
          "Failed to provide the workflow document ("
              + provideWorkflowDocumentRequest.getWorkflowDocumentId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void rejectWorkflowDocument(
      UUID tenantId, RejectWorkflowDocumentRequest rejectWorkflowDocumentRequest, String rejectedBy)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {
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
  public WorkflowDocument requestWorkflowDocument(
      UUID tenantId,
      RequestWorkflowDocumentRequest requestWorkflowDocumentRequest,
      WorkflowDefinitionDocumentDefinition workflowDefinitionDocumentDefinition,
      String requestedBy)
      throws DocumentDefinitionNotFoundException, ServiceUnavailableException {

    try {
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

      workflowDocumentRepository.saveAndFlush(workflowDocument);

      return workflowDocument;
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
  public WorkflowSummaries searchWorkflows(
      UUID tenantId, SearchWorkflowsRequest searchWorkflowsRequest, int maxResults)
      throws ServiceUnavailableException {
    try {
      // Build Specification
      Specification<WorkflowSummary> specification =
          (root, query, criteriaBuilder) -> {
            // Avoid duplicates when joins or subqueries are involved
            query.distinct(true);

            // AND'ed top-level predicates
            List<Predicate> andPredicates = new ArrayList<>();

            // Tenant filter
            if (tenantId != null) {
              andPredicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
            }

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

                if (StringUtils.hasText(attributeSearchCriteria.getCode())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(workflowAttributeRoot.get("code")),
                              attributeSearchCriteria.getCode().toLowerCase()));
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

      // Sorting
      String sortByPropertyName =
          resolveWorkflowSortByPropertyName(searchWorkflowsRequest.getSortBy());
      Sort.Direction dir = resolveSortDirection(searchWorkflowsRequest.getSortDirection());
      Sort sort = Sort.by(dir, sortByPropertyName);

      // Paging
      int pageIndex =
          searchWorkflowsRequest.getPageIndex() == null
              ? 0
              : Math.max(0, searchWorkflowsRequest.getPageIndex());
      int pageSize =
          searchWorkflowsRequest.getPageSize() == null
              ? 50
              : Math.max(1, Math.min(searchWorkflowsRequest.getPageSize(), maxResults));
      Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

      Page<WorkflowSummary> workflowSummaryPage =
          workflowSummaryRepository.findAll(specification, pageable);

      return new WorkflowSummaries(
          workflowSummaryPage.toList(),
          workflowSummaryPage.getTotalElements(),
          searchWorkflowsRequest.getSortBy(),
          searchWorkflowsRequest.getSortDirection(),
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to search for the workflows for the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  public void setWorkflowStatus(UUID tenantId, UUID workflowId, WorkflowStatus status)
      throws WorkflowNotFoundException, ServiceUnavailableException {
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
  public void suspendWorkflow(UUID tenantId, UUID workflowId, String suspendedBy)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      OffsetDateTime now = OffsetDateTime.now();

      if (workflowRepository.suspendWorkflow(tenantId, workflowId, now, suspendedBy) <= 0) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      // Suspend all the active workflow steps
      List<WorkflowStep> activeWorkflowSteps =
          workflowStepRepository.findByWorkflowIdAndStatus(workflowId, WorkflowStepStatus.ACTIVE);

      for (WorkflowStep activeWorkflowStep : activeWorkflowSteps) {
        workflowStepRepository.suspendWorkflowStep(workflowId, activeWorkflowStep.getCode(), now);
      }
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to suspend the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public void suspendWorkflowStep(UUID tenantId, UUID workflowId, String step)
      throws WorkflowStepNotFoundException, ServiceUnavailableException {
    try {
      if (workflowStepRepository.suspendWorkflowStep(workflowId, step, OffsetDateTime.now()) <= 0) {
        throw new WorkflowStepNotFoundException(tenantId, workflowId, step);
      }
    } catch (WorkflowStepNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to suspend the workflow step ("
              + step
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void unsuspendWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (workflowRepository.unsuspendWorkflow(tenantId, workflowId) <= 0) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      // Unsuspend all the suspended workflow steps
      List<WorkflowStep> suspendedWorkflowSteps =
          workflowStepRepository.findByWorkflowIdAndStatus(
              workflowId, WorkflowStepStatus.SUSPENDED);

      for (WorkflowStep suspendedWorkflowStep : suspendedWorkflowSteps) {
        workflowStepRepository.unsuspendWorkflowStep(workflowId, suspendedWorkflowStep.getCode());
      }

    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unsuspend the workflow (" + workflowId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public void unsuspendWorkflowStep(UUID tenantId, UUID workflowId, String step)
      throws WorkflowStepNotFoundException, ServiceUnavailableException {
    try {
      if (workflowStepRepository.unsuspendWorkflowStep(workflowId, step) <= 0) {
        throw new WorkflowStepNotFoundException(tenantId, workflowId, step);
      }
    } catch (WorkflowStepNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unsuspend the workflow step ("
              + step
              + ") for the workflow ("
              + workflowId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Workflow updateWorkflow(UUID tenantId, Workflow workflow)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflow.getId())) {
        throw new WorkflowNotFoundException(tenantId, workflow.getId());
      }

      return workflowRepository.saveAndFlush(workflow);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow ("
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
  public WorkflowDocument updateWorkflowDocument(UUID tenantId, WorkflowDocument workflowDocument)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowDocumentRepository.existsByTenantIdAndId(tenantId, workflowDocument.getId())) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocument.getId());
      }

      return workflowDocumentRepository.saveAndFlush(workflowDocument);
    } catch (WorkflowDocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow document ("
              + workflowDocument.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public WorkflowNote updateWorkflowNote(UUID tenantId, WorkflowNote workflowNote)
      throws WorkflowNoteNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowNoteRepository.existsByTenantIdAndId(tenantId, workflowNote.getId())) {
        throw new WorkflowNoteNotFoundException(tenantId, workflowNote.getId());
      }

      return workflowNoteRepository.saveAndFlush(workflowNote);
    } catch (WorkflowNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow note ("
              + workflowNote.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void verifyWorkflowDocument(
      UUID tenantId, VerifyWorkflowDocumentRequest verifyWorkflowDocumentRequest, String verifiedBy)
      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {
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
  public boolean workflowDocumentExists(UUID tenantId, UUID workflowId, UUID workflowDocumentId)
      throws ServiceUnavailableException {
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
  public boolean workflowDocumentExists(UUID tenantId, UUID workflowDocumentId)
      throws ServiceUnavailableException {
    try {
      return workflowDocumentRepository.existsByTenantIdAndId(tenantId, workflowDocumentId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the workflow document ("
              + workflowDocumentId
              + ") exists for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean workflowExists(UUID tenantId, UUID workflowId) throws ServiceUnavailableException {
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
      throws ServiceUnavailableException {
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

  private String resolveWorkflowSortByPropertyName(WorkflowSortBy sortBy) {
    if (sortBy == null) return "initiated";
    return switch (sortBy) {
      case WorkflowSortBy.DEFINITION_ID -> "definitionId";
      case WorkflowSortBy.FINALIZED -> "finalized";
      case WorkflowSortBy.FINALIZED_BY -> "finalizedBy";
      case WorkflowSortBy.INITIATED -> "initiated";
      case WorkflowSortBy.INITIATED_BY -> "initiatedBy";
      case WorkflowSortBy.UPDATED -> "updated";
      case WorkflowSortBy.UPDATED_BY -> "updatedBy";
      default -> "initiated";
    };
  }
}
