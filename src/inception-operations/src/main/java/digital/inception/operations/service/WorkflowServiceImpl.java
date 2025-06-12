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
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionException;
import digital.inception.operations.exception.DuplicateWorkflowEngineException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionNotFoundException;
import digital.inception.operations.exception.WorkflowDefinitionVersionNotFoundException;
import digital.inception.operations.exception.WorkflowEngineNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.model.CreateWorkflowRequest;
import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.model.Workflow;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionCategory;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowStatus;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionCategoryRepository;
import digital.inception.operations.persistence.jpa.WorkflowDefinitionRepository;
import digital.inception.operations.persistence.jpa.WorkflowEngineRepository;
import digital.inception.operations.store.WorkflowStore;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code WorkflowServiceImpl} class provides the Workflow Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class WorkflowServiceImpl extends AbstractServiceBase implements WorkflowService {

  /** The Workflow Definition Category Repository. */
  private final WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository;

  /** The Workflow Definition Repository. */
  private final WorkflowDefinitionRepository workflowDefinitionRepository;

  /** The Workflow Engine Repository. */
  private final WorkflowEngineRepository workflowEngineRepository;

  /** The Workflow Store. */
  private final WorkflowStore workflowStore;

  /**
   * Constructs a new {@code WorkflowServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param workflowStore the Workflow Store
   * @param workflowDefinitionCategoryRepository the Workflow Definition Category Repository
   * @param workflowDefinitionRepository the Workflow Definition Repository
   * @param workflowEngineRepository the Workflow Engine Repository
   */
  public WorkflowServiceImpl(
      ApplicationContext applicationContext,
      WorkflowStore workflowStore,
      WorkflowDefinitionCategoryRepository workflowDefinitionCategoryRepository,
      WorkflowDefinitionRepository workflowDefinitionRepository,
      WorkflowEngineRepository workflowEngineRepository) {
    super(applicationContext);

    this.workflowStore = workflowStore;
    this.workflowDefinitionCategoryRepository = workflowDefinitionCategoryRepository;
    this.workflowDefinitionRepository = workflowDefinitionRepository;
    this.workflowEngineRepository = workflowEngineRepository;
  }

  @Override
  public Workflow createWorkflow(UUID tenantId, CreateWorkflowRequest createWorkflowRequest)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("createWorkflowRequest", createWorkflowRequest);

    try {
      Optional<WorkflowDefinition> workflowDefinitionOptional =
          workflowDefinitionRepository.findLatestVersionById(
              createWorkflowRequest.getDefinitionId());

      if (workflowDefinitionOptional.isEmpty()) {
        throw new WorkflowDefinitionNotFoundException(createWorkflowRequest.getDefinitionId());
      }

      WorkflowDefinition workflowDefinition = workflowDefinitionOptional.get();

      Workflow workflow =
          new Workflow(
              UuidCreator.getTimeOrderedEpoch(),
              tenantId,
              createWorkflowRequest.getParentId(),
              workflowDefinition.getId(),
              workflowDefinition.getVersion(),
              WorkflowStatus.IN_PROGRESS,
              createWorkflowRequest.getData());

      return workflowStore.createWorkflow(tenantId, workflow);
    } catch (WorkflowDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the workflow ("
              + createWorkflowRequest.getDefinitionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void createWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          DuplicateWorkflowDefinitionException,
          ServiceUnavailableException {
    validateArgument("workflowDefinition", workflowDefinition);

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
          "Failed to create the workflow definition (" + workflowDefinition.getId() + ")", e);
    }
  }

  @Override
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
  public void deleteWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    workflowStore.deleteWorkflow(tenantId, workflowId);
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
          "Failed to delete the workflow definition (" + workflowDefinitionId + ")", e);
    }
  }

  @Override
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
  public Workflow getWorkflow(UUID tenantId, UUID workflowId)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (workflowId == null) {
      throw new InvalidArgumentException("workflowId");
    }

    return workflowStore.getWorkflow(tenantId, workflowId);
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
          "Failed to retrieve the latest version of the workflow definition ("
              + workflowDefinitionId
              + ")",
          e);
    }
  }

  @Override
  public List<WorkflowDefinitionCategory> getWorkflowDefinitionCategories(UUID tenantId)
      throws ServiceUnavailableException {
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
  public List<WorkflowDefinition> getWorkflowDefinitions(
      UUID tenantId, String workflowDefinitionCategoryId)
      throws WorkflowDefinitionCategoryNotFoundException, ServiceUnavailableException {
    try {
      if (!workflowDefinitionCategoryRepository.existsById(workflowDefinitionCategoryId)) {
        throw new WorkflowDefinitionCategoryNotFoundException(workflowDefinitionCategoryId);
      }

      return workflowDefinitionRepository.findForCategoryAndTenantOrGlobal(
          workflowDefinitionCategoryId, tenantId);
    } catch (WorkflowDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the workflow definitions associated with the workflow definition category ("
              + workflowDefinitionCategoryId
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
  public List<WorkflowEngine> getWorkflowEngines() throws ServiceUnavailableException {
    try {
      return workflowEngineRepository.findAll();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the workflow engines", e);
    }
  }

  @Override
  public Workflow updateWorkflow(UUID tenantId, UpdateWorkflowRequest updateWorkflowRequest)
      throws InvalidArgumentException, WorkflowNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("updateWorkflowRequest", updateWorkflowRequest);

    try {
      Workflow workflow = workflowStore.getWorkflow(tenantId, updateWorkflowRequest.getId());

      if (StringUtils.hasText(updateWorkflowRequest.getData())) {
        workflow.setData(updateWorkflowRequest.getData());
      }

      if (updateWorkflowRequest.getStatus() != null) {
        workflow.setStatus(updateWorkflowRequest.getStatus());
      }

      return workflowStore.updateWorkflow(tenantId, workflow);
    } catch (WorkflowNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the workflow ("
              + updateWorkflowRequest.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void updateWorkflowDefinition(WorkflowDefinition workflowDefinition)
      throws InvalidArgumentException,
          WorkflowDefinitionNotFoundException,
          ServiceUnavailableException {
    validateArgument("workflowDefinition", workflowDefinition);

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
          "Failed to update the workflow definition (" + workflowDefinition.getId() + ")", e);
    }
  }

  @Override
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
}
