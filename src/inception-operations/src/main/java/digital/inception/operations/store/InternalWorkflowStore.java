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
import digital.inception.operations.exception.DuplicateWorkflowException;
import digital.inception.operations.exception.DuplicateWorkflowNoteException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.exception.WorkflowNoteNotFoundException;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.model.WorkflowNoteSortBy;
import digital.inception.operations.model.WorkflowNotes;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowStep;
import digital.inception.operations.model.WorkflowStepStatus;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.model.WorkflowSummary;
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
   * @param workflowNoteRepository the Workflow Note Repository
   * @param workflowRepository the Workflow Repository
   * @param workflowStepRepository the Workflow Step Repository
   * @param workflowSummaryRepository the Workflow Summary Repository
   */
  public InternalWorkflowStore(
      WorkflowNoteRepository workflowNoteRepository,
      WorkflowRepository workflowRepository,
      WorkflowStepRepository workflowStepRepository,
      WorkflowSummaryRepository workflowSummaryRepository) {
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
        throw new WorkflowNotFoundException(workflowId);
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
        throw new WorkflowNoteNotFoundException(workflowNoteId);
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
        throw new WorkflowNotFoundException(workflowId);
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
        throw new WorkflowNoteNotFoundException(workflowNoteId);
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
        throw new WorkflowNotFoundException(workflowId);
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
      }
      if (sortBy == WorkflowNoteSortBy.UPDATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updated");
      }
      if (sortBy == WorkflowNoteSortBy.UPDATED_BY) {
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
                      //                      predicates.add(
                      //                          criteriaBuilder.or(
                      //                              criteriaBuilder.like(
                      //
                      // criteriaBuilder.lower(root.get("definitionId")),
                      //                                  "%" + filter.toLowerCase() + "%")));
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
        throw new WorkflowNotFoundException(workflowId);
      }

      // Obtain a proxy to the Workflow without fetching it
      Workflow workflowRef = entityManager.getReference(Workflow.class, workflowId);

      WorkflowStep workflowStep =
          new WorkflowStep(workflowRef, step, WorkflowStepStatus.ACTIVE, OffsetDateTime.now());

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
        throw new WorkflowNotFoundException(workflow.getId());
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
  public WorkflowNote updateWorkflowNote(UUID tenantId, WorkflowNote workflowNote)
      throws WorkflowNoteNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowNoteRepository.existsByTenantIdAndId(tenantId, workflowNote.getId())) {
        throw new WorkflowNoteNotFoundException(workflowNote.getId());
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
