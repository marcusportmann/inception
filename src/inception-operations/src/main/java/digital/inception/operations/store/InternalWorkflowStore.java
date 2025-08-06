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
import digital.inception.operations.exception.DuplicateWorkflowDocumentException;
import digital.inception.operations.exception.DuplicateWorkflowException;
import digital.inception.operations.exception.DuplicateWorkflowNoteException;
import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.exception.WorkflowStepNotFoundException;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDocument;
import digital.inception.operations.model.WorkflowDocumentSortBy;
import digital.inception.operations.model.WorkflowDocuments;
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
import digital.inception.operations.persistence.jpa.WorkflowDocumentRepository;
import digital.inception.operations.persistence.jpa.WorkflowNoteRepository;
import digital.inception.operations.persistence.jpa.WorkflowRepository;
import digital.inception.operations.persistence.jpa.WorkflowStepRepository;
import digital.inception.operations.persistence.jpa.WorkflowSummaryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  /** The Workflow Document Repository. */
  private final WorkflowDocumentRepository workflowDocumentRepository;

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

  /**
   * Constructs a new {@code InternalWorkflowStore}.
   *
   * @param workflowDocumentRepository the Workflow Document Repository
   * @param workflowNoteRepository the Workflow Note Repository
   * @param workflowRepository the Workflow Repository
   * @param workflowStepRepository the Workflow Step Repository
   * @param workflowSummaryRepository the Workflow Summary Repository
   */
  public InternalWorkflowStore(
      WorkflowDocumentRepository workflowDocumentRepository,
      WorkflowNoteRepository workflowNoteRepository,
      WorkflowRepository workflowRepository,
      WorkflowStepRepository workflowStepRepository,
      WorkflowSummaryRepository workflowSummaryRepository) {
    this.workflowDocumentRepository = workflowDocumentRepository;
    this.workflowNoteRepository = workflowNoteRepository;
    this.workflowRepository = workflowRepository;
    this.workflowStepRepository = workflowStepRepository;
    this.workflowSummaryRepository = workflowSummaryRepository;
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
      Optional<WorkflowStep> workflowStepOptional =
          workflowStepRepository.findById(new WorkflowStepId(workflowId, step));

      if (!workflowStepOptional.isPresent()) {
        throw new WorkflowStepNotFoundException(workflowId, step);
      }

      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
        throw new WorkflowNotFoundException(tenantId, workflowId);
      }

      WorkflowStep workflowStep = workflowStepOptional.get();

      workflowStep.setFinalized(OffsetDateTime.now());
      workflowStep.setStatus(status);

      workflowStepRepository.saveAndFlush(workflowStep);
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
      String definitionId,
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

      if (sortBy == WorkflowSortBy.DEFINITION_ID) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "definitionId");
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
      } else if (sortBy == WorkflowSortBy.FINALIZED) {
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

      Page<WorkflowSummary> workflowSummaryPage =
          workflowSummaryRepository.findAll(
              (Specification<WorkflowSummary>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                    if (StringUtils.hasText(definitionId)) {
                      predicates.add(criteriaBuilder.equal(root.get("definitionId"), definitionId));
                    }

                    if (status != null) {
                      predicates.add(criteriaBuilder.equal(root.get("status"), status));
                    }

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("initiatedBy")),
                                  "%" + filter.toLowerCase() + "%"),
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("updatedBy")),
                                  "%" + filter.toLowerCase() + "%"),
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("finalizedBy")),
                                  "%" + filter.toLowerCase() + "%")));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new WorkflowSummaries(
          tenantId,
          workflowSummaryPage.toList(),
          workflowSummaryPage.getTotalElements(),
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
        throw new WorkflowDocumentNotFoundException(workflowDocument.getId());
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
