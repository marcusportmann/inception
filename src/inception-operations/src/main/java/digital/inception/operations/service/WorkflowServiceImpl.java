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

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.ValidationError;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.DuplicateWorkflowDefinitionException;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowDefinitionNotFoundException;
import digital.inception.operations.model.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.model.WorkflowNotFoundException;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionRepository;
import digital.inception.operations.store.WorkflowStore;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code WorkflowServiceImpl} class provides the Workflow Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class WorkflowServiceImpl implements WorkflowService {

  /** The JSR-303 validator. */
  private final Validator validator;

  /** The Workflow Definition Repository. */
  private final WorkflowDefinitionRepository workflowDefinitionRepository;

  /** The Workflow Store. */
  private final WorkflowStore workflowStore;

  /**
   * Constructs a new {@code WorkflowServiceImpl}.
   *
   * @param validator the JSR-380 validator
   * @param workflowStore the Workflow Store
   * @param workflowDefinitionRepository the Workflow Definition Repository
   */
  public WorkflowServiceImpl(
      Validator validator,
      WorkflowStore caseStore,
      WorkflowDefinitionRepository workflowDefinitionRepository) {
    this.validator = validator;
    this.caseStore = caseStore;
    this.workflowDefinitionRepository = workflowDefinitionRepository;
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
          "Failed to check whether the case definition (" + workflowDefinitionId + ") exists", e);
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
          "Failed to check whether the case definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
              + ") exists",
          e);
    }
  }

  @Override
  public void createWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateWorkflowDefinition(workflowDefinition);

    if (!Objects.equals(tenantId, association.getTenantId())) {
      throw new InvalidArgumentException("association.tenantId");
    }

    try {
      if (workflowDefinitionRepository.existsById(workflowDefinition.getId())) {
        throw new DuplicateWorkflowDefinitionException(workflowDefinition.getId());
      }

      workflowDefinition.setVersion(1);

      workflowDefinitionRepository.saveAndFlush(workflowDefinition);
    } catch (DuplicateWorkflowDefinitionException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the case definition (" + workflowDefinition.getId() + ")", e);
    }
  }

  @Override
  public Workflow createWorkflow(CreateWorkflowRequest createWorkflowRequest, String createdBy)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    validateCreateCaseRequest(createCaseRequest);

    try {
      Optional<WorkflowDefinition> workflowDefinitionOptional =
          workflowDefinitionRepository.findLatestVersionById(createCaseRequest.getDefinitionId());

      if (workflowDefinitionOptional.isEmpty()) {
        throw new WorkflowDefinitionNotFoundException(createCaseRequest.getDefinitionId());
      }

      WorkflowDefinition workflowDefinition = workflowDefinitionOptional.get();

      Workflow theCase =
          new Workflow(
              UuidCreator.getTimeOrderedEpoch(),
              createCaseRequest.getParentId(),
              workflowDefinition.getId(),
              workflowDefinition.getVersion(),
              WorkflowStatus.IN_PROGRESS,
              createCaseRequest.getData(),
              createdBy);

      return caseStore.createCase(theCase);
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the case (" + createCaseRequest.getDefinitionId() + ")", e);
    }
  }

  @Override
  public void deleteCase(UUID caseId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (caseId == null) {
      throw new InvalidArgumentException("caseId");
    }

    caseStore.deleteCase(caseId);
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
          "Failed to delete the case definition (" + workflowDefinitionId + ")", e);
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
          "Failed to delete the case definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
              + ")",
          e);
    }
  }

  @Override
  public Workflow getCase(UUID caseId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (caseId == null) {
      throw new InvalidArgumentException("caseId");
    }

    return caseStore.getCase(caseId);
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
          "Failed to retrieve the latest version of the case definition ("
              + workflowDefinitionId
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
          "Failed to retrieve the case definition ("
              + workflowDefinitionId
              + ") version ("
              + workflowDefinitionVersion
              + ")",
          e);
    }
  }

  @Override
  public Workflow updateCase(UpdateWorkflowRequest updateCaseRequest, String updatedBy)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    validateUpdateCaseRequest(updateCaseRequest);

    try {
      Workflow theCase = caseStore.getCase(updateCaseRequest.getId());

      if (StringUtils.hasText(updateCaseRequest.getData())) {
        theCase.setData(updateCaseRequest.getData());
      }

      if (updateCaseRequest.getStatus() != null) {
        theCase.setStatus(updateCaseRequest.getStatus());
      }

      theCase.setUpdated(OffsetDateTime.now());
      theCase.setUpdatedBy(updatedBy);

      return caseStore.updateCase(theCase);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the case (" + updateCaseRequest.getId() + ")", e);
    }
  }

  @Override
  public void updateWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    validateWorkflowDefinition(workflowDefinition);

    try {
      if (!workflowDefinitionRepository.existsById(workflowDefinition.getId())) {
        throw new WorkflowDefinitionNotFoundException(workflowDefinition.getId());
      }

      workflowDefinition.setVersion(
          workflowDefinitionRepository.getNextVersionById(workflowDefinition.getId()));

      workflowDefinitionRepository.saveAndFlush(workflowDefinition);
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the case definition (" + workflowDefinition.getId() + ")", e);
    }
  }

  private void validateCase(Workflow theCase) throws InvalidArgumentException {
    if (theCase == null) {
      throw new InvalidArgumentException("case");
    }

    Set<ConstraintViolation<Workflow>> constraintViolations = validator.validate(theCase);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "case", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException {
    if (workflowDefinition == null) {
      throw new InvalidArgumentException("workflowDefinition");
    }

    Set<ConstraintViolation<WorkflowDefinition>> constraintViolations =
        validator.validate(workflowDefinition);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "workflowDefinition", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateCreateCaseRequest(CreateWorkflowRequest createCaseRequest)
      throws InvalidArgumentException {
    if (createCaseRequest == null) {
      throw new InvalidArgumentException("createCaseRequest");
    }

    Set<ConstraintViolation<CreateWorkflowRequest>> constraintViolations =
        validator.validate(createCaseRequest);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "createCaseRequest", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateUpdateCaseRequest(UpdateWorkflowRequest updateCaseRequest)
      throws InvalidArgumentException {
    if (updateCaseRequest == null) {
      throw new InvalidArgumentException("updateCaseRequest");
    }

    Set<ConstraintViolation<UpdateWorkflowRequest>> constraintViolations =
        validator.validate(updateCaseRequest);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "updateCaseRequest", ValidationError.toValidationErrors(constraintViolations));
    }
  }
}
