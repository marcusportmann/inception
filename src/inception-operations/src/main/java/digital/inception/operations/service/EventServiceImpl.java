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

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.util.ServiceUtil;
import digital.inception.operations.connector.WorkflowEngineConnector;
import digital.inception.operations.exception.DuplicateEventException;
import digital.inception.operations.exception.EventNotFoundException;
import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
import digital.inception.operations.exception.WorkflowNotFoundException;
import digital.inception.operations.model.Event;
import digital.inception.operations.model.EventStatus;
import digital.inception.operations.model.EventType;
import digital.inception.operations.model.ObjectType;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.model.WorkflowDefinitionId;
import digital.inception.operations.model.WorkflowEngineIds;
import digital.inception.operations.persistence.jpa.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * The {@code EventServiceImpl} class provides the Workflow Document Event Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class EventServiceImpl extends AbstractServiceBase implements EventService {

  /** The Spring application event publisher. */
  private final ApplicationEventPublisher applicationEventPublisher;

  /** The Event Repository. */
  private final EventRepository eventRepository;

  /* The name of the Event Service instance. */
  private final String instanceName = ServiceUtil.getServiceInstanceName("EventService");

  /* Entity Manager */
  @PersistenceContext(unitName = "operations")
  private EntityManager entityManager;

  /** The maximum number of processing attempts for an event. */
  @Value("${inception.operations.maximum-event-processing-attempts:#{100}}")
  private int maximumEventProcessingAttempts;

  /** The Workflow Service. */
  private WorkflowService workflowService;

  /**
   * Constructs a new {@code EventServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param applicationEventPublisher the Spring application event publisher
   * @param eventRepository the Event Repository
   */
  public EventServiceImpl(
      ApplicationContext applicationContext,
      ApplicationEventPublisher applicationEventPublisher,
      EventRepository eventRepository) {
    super(applicationContext);

    this.applicationEventPublisher = applicationEventPublisher;
    this.eventRepository = eventRepository;
  }

  @Override
  public List<Event> getEventsForObject(UUID tenantId, ObjectType objectType, UUID objectId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (objectType == null) {
      throw new InvalidArgumentException("objectType");
    }

    if (objectId == null) {
      throw new InvalidArgumentException("objectId");
    }

    try {
      return eventRepository.findByTenantIdAndObjectTypeAndObjectId(tenantId, objectType, objectId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the events for the object ("
              + objectId
              + ") of type ("
              + objectType
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public int getMaximumEventProcessingAttempts() {
    return maximumEventProcessingAttempts;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Optional<Event> getNextEventQueuedForProcessing(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      // Handle the situation where different time precisions are used in the database
      OffsetDateTime now = OffsetDateTime.now().plusSeconds(1);

      PageRequest pageRequest = PageRequest.of(0, 1);

      List<Event> events =
          eventRepository.findEventsQueuedForProcessingForWrite(tenantId, now, pageRequest);

      if (!events.isEmpty()) {
        Event event = events.getFirst();

        OffsetDateTime locked = OffsetDateTime.now();

        eventRepository.lockEventForProcessing(tenantId, event.getId(), instanceName, locked);

        entityManager.detach(event);

        event.setStatus(EventStatus.PROCESSING);
        event.setLocked(locked);
        event.setLockName(instanceName);
        event.incrementProcessingAttempts();

        return Optional.of(event);
      } else {
        return Optional.empty();
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the next event that has been queued for processing for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void processEvent(Event event)
      throws InvalidArgumentException, ServiceUnavailableException {
    log.info(
        "Processing the event ("
            + event.getId()
            + ") for the tenant ("
            + event.getTenantId()
            + ") with type ("
            + event.getType()
            + ")");

    if ((event.getType() == EventType.WORKFLOW_DOCUMENT_REQUESTED)
        || (event.getType() == EventType.WORKFLOW_DOCUMENT_PROVIDED)
        || (event.getType() == EventType.WORKFLOW_DOCUMENT_REJECTED)
        || (event.getType() == EventType.WORKFLOW_DOCUMENT_VERIFIED)
        || (event.getType() == EventType.WORKFLOW_DOCUMENT_WAIVED)) {
      processWorkflowDocumentEvent(event);
    }
  }

  @Override
  @Transactional
  public void publishEvent(UUID tenantId, Event event)
      throws InvalidArgumentException, DuplicateEventException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("event", event);

    try {
      if (eventRepository.existsById(event.getId())) {
        throw new DuplicateEventException(event.getId());
      }

      eventRepository.saveAndFlush(event);

      triggerEventProcessing();
    } catch (DuplicateEventException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to publish the event (" + event.getId() + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  @Transactional
  public void publishEvent(
      UUID tenantId, EventType eventType, ObjectType objectType, UUID objectId, String actor)
      throws InvalidArgumentException, DuplicateEventException, ServiceUnavailableException {
    publishEvent(
        tenantId,
        new Event(tenantId, objectType, objectId, eventType, OffsetDateTime.now(), actor));
  }

  @Override
  public void resetEventLocks(UUID tenantId, EventStatus status, EventStatus newStatus)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (status == null) {
      throw new InvalidArgumentException("status");
    }

    if (newStatus == null) {
      throw new InvalidArgumentException("newStatus");
    }

    try {
      eventRepository.resetEventLocks(tenantId, status, newStatus, instanceName);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to reset the locks for the events with status ("
              + status
              + ") that have been locked using the lock name ("
              + instanceName
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void triggerEventProcessing() {
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            // Fire-and-forget trigger *after* the TX is really committed
            applicationEventPublisher.publishEvent(new TriggerEventProcessingEvent());
          }
        });
  }

  @Override
  public void unlockEvent(UUID tenantId, UUID eventId, EventStatus status)
      throws InvalidArgumentException, EventNotFoundException, ServiceUnavailableException {
    if (eventId == null) {
      throw new InvalidArgumentException("eventId");
    }

    if (status == null) {
      throw new InvalidArgumentException("status");
    }

    try {
      if (!eventRepository.existsByTenantIdAndId(tenantId, eventId)) {
        throw new EventNotFoundException(tenantId, eventId);
      }

      eventRepository.unlockEvent(tenantId, eventId, status);
    } catch (EventNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unlock and set the status for the event ("
              + eventId
              + ") for the tenant ("
              + tenantId
              + ") to ("
              + status
              + ")",
          e);
    }
  }

  /**
   * Returns the lazily evaluated Workflow Service to avoid circular references.
   *
   * @return the lazily evaluated Workflow Service to avoid circular references.
   */
  private WorkflowService getWorkflowService() {
    if (workflowService == null) {
      workflowService = getApplicationContext().getBean(WorkflowService.class);
    }

    return workflowService;
  }

  private void processWorkflowDocumentEvent(Event event) throws ServiceUnavailableException {
    try {
      UUID workflowId =
          getWorkflowService()
              .getWorkflowIdForWorkflowDocument(event.getTenantId(), event.getObjectId());

      WorkflowDefinition workflowDefinition =
          getWorkflowService().getWorkflowDefinitionForWorkflow(event.getTenantId(), workflowId);

      WorkflowEngineIds workflowEngineIds =
          getWorkflowService().getWorkflowEngineIdsForWorkflow(event.getTenantId(), workflowId);

      WorkflowEngineConnector workflowEngineConnector =
          getWorkflowService().getWorkflowEngineConnector(workflowEngineIds.getEngineId());

      workflowEngineConnector.processWorkflowDocumentEvent(
          workflowDefinition,
          event.getTenantId(),
          workflowId,
          workflowEngineIds.getEngineInstanceId(),
          event.getObjectId(),
          event.getType());
    } catch (WorkflowNotFoundException | WorkflowDocumentNotFoundException ignored) {
      // Ignore the workflow and/or workflow document that has been deleted
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to process the workflow document event ("
              + event.getType()
              + ") for the workflow document ("
              + event.getObjectId()
              + ") for the tenant ("
              + event.getTenantId()
              + ")",
          e);
    }
  }
}
