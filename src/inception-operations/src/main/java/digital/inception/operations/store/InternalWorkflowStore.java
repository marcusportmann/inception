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
import digital.inception.operations.exception.DuplicateWorkflowDocumentException;
import digital.inception.operations.exception.DuplicateWorkflowException;
import digital.inception.operations.exception.DuplicateWorkflowNoteException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
import digital.inception.operations.exception.WorkflowInteractionLinkNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.exception.WorkflowStepNotFoundException;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
import digital.inception.operations.model.RejectWorkflowDocumentRequest;
import digital.inception.operations.model.RequestWorkflowDocumentRequest;
import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowAttribute;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocumentStatus;
import digital.inception.operations.model.WorkflowDocuments;
import digital.inception.operations.model.WorkflowInteractionLink;
import digital.inception.operations.model.WorkflowInteractionLinkId;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowStepStatus;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.model.WorkflowSummary;
import digital.inception.operations.persistence.jpa.WorkflowDocumentRepository;
import digital.inception.operations.persistence.jpa.WorkflowInteractionLinkRepository;
import digital.inception.operations.persistence.jpa.WorkflowNoteRepository;
import digital.inception.operations.persistence.jpa.WorkflowRepository;
import digital.inception.operations.persistence.jpa.WorkflowStepRepository;
import digital.inception.operations.persistence.jpa.WorkflowSummaryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
      if (workflowDocumentRepository.deleteByTenantIdAndId(tenantId, workflowDocumentId) == 0) {
        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocumentId);
      }
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
        throw new WorkflowInteractionLinkNotFoundException(workflowId, interactionId);
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
      UUID tenantId, UUID workflowId, String step, WorkflowStepStatus status)
      throws WorkflowStepNotFoundException, ServiceUnavailableException {
    try {
      if (workflowStepRepository.finalizeWorkflowStep(
              workflowId, step, status, OffsetDateTime.now())
          <= 0) {
        throw new WorkflowStepNotFoundException(workflowId, step);
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
          tenantId,
          workflowId,
          workflowDocumentPage.toList(),
          workflowDocumentPage.getTotalElements(),
          filter,
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
          tenantId,
          workflowId,
          workflowNotePage.toList(),
          workflowNotePage.getTotalElements(),
          filter,
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
  public WorkflowSummaries getWorkflowSummaries(
      UUID tenantId,
      String workflowDefinitionId,
      WorkflowStatus status,
      String filter,
      WorkflowSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (sortBy == WorkflowSortBy.FINALIZED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "finalized");
      } else if (sortBy == WorkflowSortBy.FINALIZED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "finalizedBy");
      } else if (sortBy == WorkflowSortBy.INITIATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "initiated");
      } else if (sortBy == WorkflowSortBy.INITIATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "initiatedBy");
      } else if (sortBy == WorkflowSortBy.UPDATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updated");
      } else if (sortBy == WorkflowSortBy.UPDATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updatedBy");
      } else if (sortBy == WorkflowSortBy.DEFINITION_ID) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "definitionId");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "initiated");
      }

      Page<WorkflowSummary> workflowSummaryPage =
          workflowSummaryRepository.findAll(
              (Specification<WorkflowSummary>)
                  (root, query, criteriaBuilder) -> {
                    // LEFT join attributes for filtering
                    Join<Workflow, WorkflowAttribute> attributesJoin =
                        root.join("attributes", JoinType.LEFT);

                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                    if (StringUtils.hasText(workflowDefinitionId)) {
                      predicates.add(
                          criteriaBuilder.equal(root.get("definitionId"), workflowDefinitionId));
                    }

                    if (status != null) {
                      predicates.add(criteriaBuilder.equal(root.get("status"), status));
                    }

                    if (StringUtils.hasText(filter)) {
                      String likeValue = "%" + filter.toLowerCase() + "%";

                      // Common LIKEs for non-text-indexed columns
                      Predicate initiatedByLike =
                          criteriaBuilder.like(
                              criteriaBuilder.lower(root.get("initiatedBy")), likeValue);
                      Predicate updatedByLike =
                          criteriaBuilder.like(
                              criteriaBuilder.lower(root.get("updatedBy")), likeValue);
                      Predicate finalizedByLike =
                          criteriaBuilder.like(
                              criteriaBuilder.lower(root.get("finalizedBy")), likeValue);

                      Predicate attributeValuePredicate;
                      if (usingOracle) {
                        // Oracle Text: CONTAINS(value, '*term*', 1) > 0
                        // Render contains(value, '*foo*', 1)
                        Expression<Integer> containsExpr =
                            criteriaBuilder.function(
                                "contains",
                                Integer.class,
                                attributesJoin.get("value"),
                                criteriaBuilder.literal("*" + filter + "*"),
                                criteriaBuilder.literal(1));
                        attributeValuePredicate = criteriaBuilder.greaterThan(containsExpr, 0);
                      } else {
                        attributeValuePredicate =
                            criteriaBuilder.like(
                                criteriaBuilder.lower(attributesJoin.get("value")), likeValue);
                      }

                      predicates.add(
                          criteriaBuilder.or(
                              attributeValuePredicate,
                              initiatedByLike,
                              updatedByLike,
                              finalizedByLike));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new WorkflowSummaries(
          tenantId,
          workflowSummaryPage.toList(),
          workflowSummaryPage.getTotalElements(),
          workflowDefinitionId,
          status,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered workflow summaries for the tenant (" + tenantId + ")",
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
      UUID tenantId, UUID workflowId, UUID interactionId, String linkedBy)
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
          new WorkflowInteractionLink(workflowId, interactionId, OffsetDateTime.now(), linkedBy));
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
  public void provideWorkflowDocument(
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

      List<DocumentAttribute> documentAttributes =
          provideWorkflowDocumentRequest.getAttributes().stream()
              .map(attr -> new DocumentAttribute(attr.getCode(), attr.getValue()))
              .toList();

      // Create the new document
      document = new Document(workflowDocument.getDocumentDefinitionId());
      document.setAttributes(documentAttributes);
      document.setCreated(OffsetDateTime.now());
      document.setCreatedBy(providedBy);
      document.setData(provideWorkflowDocumentRequest.getData());
      document.setExpiryDate(provideWorkflowDocumentRequest.getExpiryDate());
      document.setExternalReference(provideWorkflowDocumentRequest.getExternalReference());
      document.setFileType(provideWorkflowDocumentRequest.getFileType());
      document.setHash(
          documentStore.calculateDocumentDataHash(provideWorkflowDocumentRequest.getData()));
      document.setIssueDate(provideWorkflowDocumentRequest.getIssueDate());
      document.setName(provideWorkflowDocumentRequest.getName());
      document.setSourceDocumentId(provideWorkflowDocumentRequest.getSourceDocumentId());
      document.setTenantId(tenantId);

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

      boolean verifiable =
          workflowDocumentRepository.isWorkflowDocumentVerifiable(workflowDocument.getId());

      if (verifiable) {
        workflowDocument.setStatus(WorkflowDocumentStatus.VERIFIABLE);
      } else {
        workflowDocument.setStatus(WorkflowDocumentStatus.PROVIDED);
      }

      workflowDocumentRepository.saveAndFlush(workflowDocument);
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
      throws DocumentDefinitionNotFoundException, ServiceUnavailableException {

    try {
      WorkflowDocument workflowDocument =
          new WorkflowDocument(
              tenantId,
              requestWorkflowDocumentRequest.getWorkflowId(),
              requestWorkflowDocumentRequest.getDocumentDefinitionId(),
              OffsetDateTime.now(),
              requestedBy,
              requestWorkflowDocumentRequest.getRequestedFromPartyId());

      workflowDocumentRepository.saveAndFlush(workflowDocument);

      return workflowDocument.getWorkflowId();
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
        throw new WorkflowStepNotFoundException(workflowId, step);
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
        throw new WorkflowStepNotFoundException(workflowId, step);
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
}
