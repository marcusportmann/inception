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
      WorkflowDefinitionRepository caseDefinitionRepository) {
    this.validator = validator;
    this.caseStore = caseStore;
    this.caseDefinitionRepository = caseDefinitionRepository;
  }

  @Override
  public boolean caseDefinitionExists(String caseDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(caseDefinitionId)) {
      throw new InvalidArgumentException("caseDefinitionId");
    }

    try {
      return caseDefinitionRepository.existsById(caseDefinitionId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the case definition (" + caseDefinitionId + ") exists", e);
    }
  }

  @Override
  public boolean caseDefinitionVersionExists(String caseDefinitionId, int caseDefinitionVersion)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(caseDefinitionId)) {
      throw new InvalidArgumentException("caseDefinitionId");
    }

    if (caseDefinitionVersion <= 0) {
      throw new InvalidArgumentException("caseDefinitionVersion");
    }

    try {
      return caseDefinitionRepository.existsById(
          new WorkflowDefinitionId(caseDefinitionId, caseDefinitionVersion));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the case definition ("
              + caseDefinitionId
              + ") version ("
              + caseDefinitionVersion
              + ") exists",
          e);
    }
  }

  @Override
  public void createCaseDefinition(WorkflowDefinition caseDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionException,
          ServiceUnavailableException {
    validateCaseDefinition(caseDefinition);

    try {
      if (caseDefinitionRepository.existsById(caseDefinition.getId())) {
        throw new DuplicateWorkflowDefinitionException(caseDefinition.getId());
      }

      caseDefinition.setVersion(1);

      caseDefinitionRepository.saveAndFlush(caseDefinition);
    } catch (DuplicateWorkflowDefinitionException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the case definition (" + caseDefinition.getId() + ")", e);
    }
  }

  @Override
  public Workflow createWorkflow(CreateWorkflowRequest createWorkflowRequest, String createdBy)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    validateCreateCaseRequest(createCaseRequest);

    try {
      Optional<WorkflowDefinition> caseDefinitionOptional =
          caseDefinitionRepository.findLatestVersionById(createCaseRequest.getDefinitionId());

      if (caseDefinitionOptional.isEmpty()) {
        throw new WorkflowDefinitionNotFoundException(createCaseRequest.getDefinitionId());
      }

      WorkflowDefinition caseDefinition = caseDefinitionOptional.get();

      Workflow theCase =
          new Workflow(
              UuidCreator.getTimeOrderedEpoch(),
              createCaseRequest.getParentId(),
              caseDefinition.getId(),
              caseDefinition.getVersion(),
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
  public void deleteCaseDefinition(String caseDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(caseDefinitionId)) {
      throw new InvalidArgumentException("caseDefinitionId");
    }

    try {
      if (!caseDefinitionRepository.existsById(caseDefinitionId)) {
        throw new WorkflowDefinitionNotFoundException(caseDefinitionId);
      }

      caseDefinitionRepository.deleteById(caseDefinitionId);
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the case definition (" + caseDefinitionId + ")", e);
    }
  }

  @Override
  public void deleteCaseDefinitionVersion(String caseDefinitionId, int caseDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(caseDefinitionId)) {
      throw new InvalidArgumentException("caseDefinitionId");
    }

    if (caseDefinitionVersion <= 0) {
      throw new InvalidArgumentException("caseDefinitionVersion");
    }

    try {
      WorkflowDefinitionId id = new WorkflowDefinitionId(caseDefinitionId, caseDefinitionVersion);

      if (!caseDefinitionRepository.existsById(id)) {
        throw new WorkflowDefinitionVersionNotFoundException(
            caseDefinitionId, caseDefinitionVersion);
      }

      caseDefinitionRepository.deleteById(id);
    } catch (WorkflowDefinitionVersionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the case definition ("
              + caseDefinitionId
              + ") version ("
              + caseDefinitionVersion
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
  public WorkflowDefinition getCaseDefinition(String caseDefinitionId)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(caseDefinitionId)) {
      throw new InvalidArgumentException("caseDefinitionId");
    }

    try {
      Optional<WorkflowDefinition> caseDefinitionOptional =
          caseDefinitionRepository.findLatestVersionById(caseDefinitionId);

      if (caseDefinitionOptional.isEmpty()) {
        throw new WorkflowDefinitionNotFoundException(caseDefinitionId);
      }

      return caseDefinitionOptional.get();
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the latest version of the case definition (" + caseDefinitionId + ")",
          e);
    }
  }

  @Override
  public WorkflowDefinition getCaseDefinitionVersion(
      String caseDefinitionId, int caseDefinitionVersion)
      throws InvalidArgumentException,
          WorkflowDefinitionVersionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(caseDefinitionId)) {
      throw new InvalidArgumentException("caseDefinitionId");
    }

    if (caseDefinitionVersion <= 0) {
      throw new InvalidArgumentException("caseDefinitionVersion");
    }

    try {
      Optional<WorkflowDefinition> caseDefinitionOptional =
          caseDefinitionRepository.findById(
              new WorkflowDefinitionId(caseDefinitionId, caseDefinitionVersion));

      if (caseDefinitionOptional.isEmpty()) {
        throw new WorkflowDefinitionVersionNotFoundException(
            caseDefinitionId, caseDefinitionVersion);
      }

      return caseDefinitionOptional.get();
    } catch (WorkflowDefinitionVersionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the case definition ("
              + caseDefinitionId
              + ") version ("
              + caseDefinitionVersion
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
  public void updateCaseDefinition(WorkflowDefinition caseDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    validateCaseDefinition(caseDefinition);

    try {
      if (!caseDefinitionRepository.existsById(caseDefinition.getId())) {
        throw new WorkflowDefinitionNotFoundException(caseDefinition.getId());
      }

      caseDefinition.setVersion(
          caseDefinitionRepository.getNextVersionById(caseDefinition.getId()));

      caseDefinitionRepository.saveAndFlush(caseDefinition);
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the case definition (" + caseDefinition.getId() + ")", e);
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

  private void validateCaseDefinition(WorkflowDefinition caseDefinition)
      throws InvalidArgumentException {
    if (caseDefinition == null) {
      throw new InvalidArgumentException("caseDefinition");
    }

    Set<ConstraintViolation<WorkflowDefinition>> constraintViolations =
        validator.validate(caseDefinition);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "caseDefinition", ValidationError.toValidationErrors(constraintViolations));
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
