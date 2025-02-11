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
import digital.inception.operations.model.DuplicateProcessException;
import digital.inception.operations.model.Process;
import digital.inception.operations.model.ProcessNotFoundException;
import digital.inception.operations.model.ProcessSortBy;
import digital.inception.operations.model.ProcessStatus;
import digital.inception.operations.model.ProcessSummaries;
import digital.inception.operations.model.ProcessSummary;
import digital.inception.operations.persistence.ProcessRepository;
import digital.inception.operations.persistence.ProcessSummaryRepository;
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
 * The <b>InternalProcessStore</b> class provides the internal process store implementation.
 *
 * @author Marcus Portmann
 */
@Service
@Conditional(InternalProcessStoreEnabledCondition.class)
@SuppressWarnings("unused")
public class InternalProcessStore implements IProcessStore {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(InternalProcessStore.class);

  /** The Process Repository. */
  private final ProcessRepository processRepository;

  /** The Process Summary Repository. */
  private final ProcessSummaryRepository processSummaryRepository;

  /**
   * Constructs a new <b>InternalProcessStore</b>.
   *
   * @param processRepository the Process Repository
   * @param processSummaryRepository the Process Summary Repository
   */
  public InternalProcessStore(
      ProcessRepository processRepository, ProcessSummaryRepository processSummaryRepository) {
    this.processRepository = processRepository;
    this.processSummaryRepository = processSummaryRepository;
  }

  @Override
  public Process createProcess(Process theProcess)
      throws DuplicateProcessException, ServiceUnavailableException {
    try {
      if (processRepository.existsById(theProcess.getId())) {
        throw new DuplicateProcessException(theProcess.getId());
      }

      return processRepository.saveAndFlush(theProcess);
    } catch (DuplicateProcessException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the process ("
              + theProcess.getId()
              + ") with definition ID ("
              + theProcess.getDefinitionId()
              + ") and definition version ("
              + theProcess.getDefinitionVersion()
              + ")",
          e);
    }
  }

  @Override
  public void deleteProcess(UUID processId)
      throws ProcessNotFoundException, ServiceUnavailableException {
    try {
      if (!processRepository.existsById(processId)) {
        throw new ProcessNotFoundException(processId);
      }

      processRepository.deleteById(processId);
    } catch (ProcessNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delete the process (" + processId + ")", e);
    }
  }

  @Override
  public Process getProcess(UUID processId)
      throws ProcessNotFoundException, ServiceUnavailableException {
    try {
      Optional<Process> processOptional = processRepository.findById(processId);

      if (processOptional.isEmpty()) {
        throw new ProcessNotFoundException(processId);
      }

      return processOptional.get();
    } catch (ProcessNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the process (" + processId + ")", e);
    }
  }

  @Override
  public ProcessSummaries getProcessSummaries(
      ProcessStatus status,
      String filter,
      ProcessSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (sortBy == ProcessSortBy.DEFINITION_ID) {
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

      Page<ProcessSummary> processSummaryPage =
          processSummaryRepository.findAll(
              (Specification<ProcessSummary>)
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

      return new ProcessSummaries(
          processSummaryPage.toList(),
          processSummaryPage.getTotalElements(),
          status,
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered process summaries", e);
    }
  }

  @Override
  public Process updateProcess(Process theProcess)
      throws ProcessNotFoundException, ServiceUnavailableException {
    try {
      if (!processRepository.existsById(theProcess.getId())) {
        throw new ProcessNotFoundException(theProcess.getId());
      }

      return processRepository.saveAndFlush(theProcess);
    } catch (ProcessNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the process ("
              + theProcess.getId()
              + ") with definition ID ("
              + theProcess.getDefinitionId()
              + ") and definition version ("
              + theProcess.getDefinitionVersion()
              + ")",
          e);
    }
  }
}
