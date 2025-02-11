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
import digital.inception.operations.model.Process;
import digital.inception.operations.model.ProcessDefinition;
import digital.inception.operations.model.ProcessDefinitionId;
import digital.inception.operations.model.ProcessDefinitionNotFoundException;
import digital.inception.operations.model.ProcessDefinitionVersionNotFoundException;
import digital.inception.operations.model.ProcessNotFoundException;
import digital.inception.operations.model.ProcessStatus;
import digital.inception.operations.model.CreateProcessRequest;
import digital.inception.operations.model.DuplicateProcessDefinitionException;
import digital.inception.operations.model.UpdateProcessRequest;
import digital.inception.operations.persistence.ProcessDefinitionRepository;
import digital.inception.operations.store.IProcessStore;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The <b>ProcessService</b> class provides the Process Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class ProcessService implements IProcessService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

  /** The Process Definition Repository. */
  private final ProcessDefinitionRepository processDefinitionRepository;

  /** The Process Store. */
  private final IProcessStore processStore;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <b>ProcessService</b>.
   *
   * @param validator the JSR-380 validator
   * @param processStore the Process Store
   * @param processDefinitionRepository the Process Definition Repository
   */
  public ProcessService(
      Validator validator,
      IProcessStore processStore,
      ProcessDefinitionRepository processDefinitionRepository) {
    this.validator = validator;
    this.processStore = processStore;
    this.processDefinitionRepository = processDefinitionRepository;
  }

  @Override
  public boolean processDefinitionExists(String processDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(processDefinitionId)) {
      throw new InvalidArgumentException("processDefinitionId");
    }

    try {
      return processDefinitionRepository.existsById(processDefinitionId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the process definition (" + processDefinitionId + ") exists", e);
    }
  }

  @Override
  public boolean processDefinitionVersionExists(String processDefinitionId, int processDefinitionVersion)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(processDefinitionId)) {
      throw new InvalidArgumentException("processDefinitionId");
    }

    if (processDefinitionVersion <= 0) {
      throw new InvalidArgumentException("processDefinitionVersion");
    }

    try {
      return processDefinitionRepository.existsById(
          new ProcessDefinitionId(processDefinitionId, processDefinitionVersion));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the process definition ("
              + processDefinitionId
              + ") version ("
              + processDefinitionVersion
              + ") exists",
          e);
    }
  }

  @Override
  public Process createProcess(CreateProcessRequest createProcessRequest, String createdBy)
      throws InvalidArgumentException,
      ProcessDefinitionNotFoundException,
      ServiceUnavailableException {
    validateCreateProcessRequest(createProcessRequest);

    try {
      Optional<ProcessDefinition> processDefinitionOptional =
          processDefinitionRepository.findLatestVersionById(createProcessRequest.getDefinitionId());

      if (processDefinitionOptional.isEmpty()) {
        throw new ProcessDefinitionNotFoundException(createProcessRequest.getDefinitionId());
      }

      ProcessDefinition processDefinition = processDefinitionOptional.get();

      Process process =
          new Process(
              UuidCreator.getTimeOrderedEpoch(),
              createProcessRequest.getParentId(),
              processDefinition.getId(),
              processDefinition.getVersion(),
              ProcessStatus.IN_PROGRESS,
              createProcessRequest.getData(),
              createdBy);

      return processStore.createProcess(process);
    } catch (ProcessDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the process (" + createProcessRequest.getDefinitionId() + ")", e);
    }
  }

  @Override
  public void createProcessDefinition(ProcessDefinition processDefinition)
      throws InvalidArgumentException,
      DuplicateProcessDefinitionException,
      ServiceUnavailableException {
    validateProcessDefinition(processDefinition);

    try {
      if (processDefinitionRepository.existsById(processDefinition.getId())) {
        throw new DuplicateProcessDefinitionException(processDefinition.getId());
      }

      processDefinition.setVersion(1);

      processDefinitionRepository.saveAndFlush(processDefinition);
    } catch (DuplicateProcessDefinitionException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the process definition (" + processDefinition.getId() + ")", e);
    }
  }

  @Override
  public void deleteProcess(UUID processId)
      throws InvalidArgumentException, ProcessNotFoundException, ServiceUnavailableException {
    if (processId == null) {
      throw new InvalidArgumentException("processId");
    }

    processStore.deleteProcess(processId);
  }

  @Override
  public void deleteProcessDefinition(String processDefinitionId)
      throws InvalidArgumentException,
      ProcessDefinitionNotFoundException,
      ServiceUnavailableException {
    if (!StringUtils.hasText(processDefinitionId)) {
      throw new InvalidArgumentException("processDefinitionId");
    }

    try {
      if (!processDefinitionRepository.existsById(processDefinitionId)) {
        throw new ProcessDefinitionNotFoundException(processDefinitionId);
      }

      processDefinitionRepository.deleteById(processDefinitionId);
    } catch (ProcessDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the process definition (" + processDefinitionId + ")", e);
    }
  }

  @Override
  public void deleteProcessDefinitionVersion(String processDefinitionId, int processDefinitionVersion)
      throws InvalidArgumentException,
      ProcessDefinitionVersionNotFoundException,
      ServiceUnavailableException {
    if (!StringUtils.hasText(processDefinitionId)) {
      throw new InvalidArgumentException("processDefinitionId");
    }

    if (processDefinitionVersion <= 0) {
      throw new InvalidArgumentException("processDefinitionVersion");
    }

    try {
      ProcessDefinitionId id = new ProcessDefinitionId(processDefinitionId, processDefinitionVersion);

      if (!processDefinitionRepository.existsById(id)) {
        throw new ProcessDefinitionVersionNotFoundException(processDefinitionId, processDefinitionVersion);
      }

      processDefinitionRepository.deleteById(id);
    } catch (ProcessDefinitionVersionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the process definition ("
              + processDefinitionId
              + ") version ("
              + processDefinitionVersion
              + ")",
          e);
    }
  }

  @Override
  public Process getProcess(UUID processId)
      throws InvalidArgumentException, ProcessNotFoundException, ServiceUnavailableException {
    if (processId == null) {
      throw new InvalidArgumentException("processId");
    }

    return processStore.getProcess(processId);
  }

  @Override
  public ProcessDefinition getProcessDefinition(String processDefinitionId)
      throws InvalidArgumentException,
      ProcessDefinitionNotFoundException,
      ServiceUnavailableException {
    if (!StringUtils.hasText(processDefinitionId)) {
      throw new InvalidArgumentException("processDefinitionId");
    }

    try {
      Optional<ProcessDefinition> processDefinitionOptional =
          processDefinitionRepository.findLatestVersionById(processDefinitionId);

      if (processDefinitionOptional.isEmpty()) {
        throw new ProcessDefinitionNotFoundException(processDefinitionId);
      }

      return processDefinitionOptional.get();
    } catch (ProcessDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the latest version of the process definition (" + processDefinitionId + ")",
          e);
    }
  }

  @Override
  public ProcessDefinition getProcessDefinitionVersion(String processDefinitionId, int processDefinitionVersion)
      throws InvalidArgumentException,
      ProcessDefinitionVersionNotFoundException,
      ServiceUnavailableException {
    if (!StringUtils.hasText(processDefinitionId)) {
      throw new InvalidArgumentException("processDefinitionId");
    }

    if (processDefinitionVersion <= 0) {
      throw new InvalidArgumentException("processDefinitionVersion");
    }

    try {
      Optional<ProcessDefinition> processDefinitionOptional =
          processDefinitionRepository.findById(
              new ProcessDefinitionId(processDefinitionId, processDefinitionVersion));

      if (processDefinitionOptional.isEmpty()) {
        throw new ProcessDefinitionVersionNotFoundException(processDefinitionId, processDefinitionVersion);
      }

      return processDefinitionOptional.get();
    } catch (ProcessDefinitionVersionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the process definition ("
              + processDefinitionId
              + ") version ("
              + processDefinitionVersion
              + ")",
          e);
    }
  }

  @Override
  public Process updateProcess(UpdateProcessRequest updateProcessRequest, String updatedBy)
      throws InvalidArgumentException, ProcessNotFoundException, ServiceUnavailableException {
    validateUpdateProcessRequest(updateProcessRequest);

    try {
      Process process = processStore.getProcess(updateProcessRequest.getId());

      if (StringUtils.hasText(updateProcessRequest.getData())) {
        process.setData(updateProcessRequest.getData());
      }

      if (updateProcessRequest.getStatus() != null) {
        process.setStatus(updateProcessRequest.getStatus());
      }

      process.setUpdated(OffsetDateTime.now());
      process.setUpdatedBy(updatedBy);

      return processStore.updateProcess(process);
    } catch (ProcessNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the process (" + updateProcessRequest.getId() + ")", e);
    }
  }

  @Override
  public void updateProcessDefinition(ProcessDefinition processDefinition)
      throws InvalidArgumentException,
      ProcessDefinitionNotFoundException,
      ServiceUnavailableException {
    validateProcessDefinition(processDefinition);

    try {
      if (!processDefinitionRepository.existsById(processDefinition.getId())) {
        throw new ProcessDefinitionNotFoundException(processDefinition.getId());
      }

      processDefinition.setVersion(
          processDefinitionRepository.getNextVersionById(processDefinition.getId()));

      processDefinitionRepository.saveAndFlush(processDefinition);
    } catch (ProcessDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the process definition (" + processDefinition.getId() + ")", e);
    }
  }

  private void validateProcess(Process process)
      throws InvalidArgumentException {
    if (process == null) {
      throw new InvalidArgumentException("process");
    }

    Set<ConstraintViolation<Process>> constraintViolations = validator.validate(process);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "process", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateProcessDefinition(ProcessDefinition processDefinition)
      throws InvalidArgumentException {
    if (processDefinition == null) {
      throw new InvalidArgumentException("processDefinition");
    }

    Set<ConstraintViolation<ProcessDefinition>> constraintViolations =
        validator.validate(processDefinition);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "processDefinition", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateCreateProcessRequest(CreateProcessRequest createProcessRequest)
      throws InvalidArgumentException {
    if (createProcessRequest == null) {
      throw new InvalidArgumentException("createProcessRequest");
    }

    Set<ConstraintViolation<CreateProcessRequest>> constraintViolations =
        validator.validate(createProcessRequest);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "createProcessRequest", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateUpdateProcessRequest(UpdateProcessRequest updateProcessRequest)
      throws InvalidArgumentException {
    if (updateProcessRequest == null) {
      throw new InvalidArgumentException("updateProcessRequest");
    }

    Set<ConstraintViolation<UpdateProcessRequest>> constraintViolations =
        validator.validate(updateProcessRequest);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "updateProcessRequest", ValidationError.toValidationErrors(constraintViolations));
    }
  }
}
