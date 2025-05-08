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

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.model.DuplicateWorkflowException;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowNotFoundException;
import digital.inception.operations.model.WorkflowSortBy;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.model.WorkflowSummaries;
import digital.inception.operations.model.WorkflowSummary;
import digital.inception.operations.persistence.jpa.WorkflowRepository;
import digital.inception.operations.persistence.jpa.WorkflowSummaryRepository;
import jakarta.persistence.criteria.Predicate;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code InternalWorkflowStore} class provides the internal workflow store implementation.
 *
 * @author Marcus Portmann
 */
@Service
@Conditional(InternalWorkflowStoreEnabledCondition.class)
@SuppressWarnings("unused")
public class InternalWorkflowStore implements WorkflowStore {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(InternalWorkflowStore.class);

  /** The Workflow Repository. */
  private final WorkflowRepository workflowRepository;

  /** The Workflow Summary Repository. */
  private final WorkflowSummaryRepository workflowSummaryRepository;

  /**
   * Constructs a new {@code InternalWorkflowStore}.
   *
   * @param workflowRepository the Workflow Repository
   * @param workflowSummaryRepository the Workflow Summary Repository
   */
  public InternalWorkflowStore(
      WorkflowRepository workflowRepository, WorkflowSummaryRepository workflowSummaryRepository) {
    this.workflowRepository = workflowRepository;
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
              + ") with definition ID ("
              + workflow.getDefinitionId()
              + ") and definition version ("
              + workflow.getDefinitionVersion()
              + ")",
          e);
    }
  }

  @Override
  public void deleteWorkflow(UUID tenantId, UUID workflowId)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowRepository.existsById(workflowId)) {
        throw new WorkflowNotFoundException(workflowId);
      }

      workflowRepository.deleteById(workflowId);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the workflow (" + workflowId + ")", e);
    }
  }

  @Override
  public Workflow getWorkflow(UUID tenantId, UUID workflowId)
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
  public WorkflowSummaries getWorkflowSummaries(
      UUID tenantId,
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

                    if (status != null) {
                      predicates.add(criteriaBuilder.equal(root.get("status"), status));
                    }

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("definitionId")),
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
          "Failed to retrieve the filtered workflow summaries", e);
    }
  }

  @Override
  public Workflow updateWorkflow(UUID tenantId, Workflow workflow)
      throws WorkflowNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowRepository.existsById(workflow.getId())) {
        throw new WorkflowNotFoundException(workflow.getId());
      }

      return workflowRepository.saveAndFlush(workflow);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow ("
              + workflow.getId()
              + ") with definition ID ("
              + workflow.getDefinitionId()
              + ") and definition version ("
              + workflow.getDefinitionVersion()
              + ")",
          e);
    }
  }
}
