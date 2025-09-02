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
import digital.inception.operations.exception.DuplicateEventException;
import digital.inception.operations.exception.EventNotFoundException;
import digital.inception.operations.model.Event;
import digital.inception.operations.model.EventStatus;
import digital.inception.operations.model.EventType;
import digital.inception.operations.model.ObjectType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code EventService} interface defines the functionality provided by an Event Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface EventService {

  /**
   * Retrieve the events for the object.
   *
   * @param tenantId the ID for the tenant
   * @param objectType the object type for the object
   * @param objectId the ID for the object
   * @return the events for the object
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the events for the object could not be retrieved
   */
  List<Event> getEventsForObject(UUID tenantId, ObjectType objectType, UUID objectId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Returns the maximum number of processing attempts for an event.
   *
   * @return the maximum number of processing attempts for an event
   */
  int getMaximumEventProcessingAttempts();

  /**
   * Retrieve the next event queued for processing.
   *
   * <p>The event will be locked to prevent duplicate processing.
   *
   * @param tenantId the ID for the tenant
   * @return an Optional containing the next event queued for processing or an empty Optional if no
   *     events are currently queued for processing
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the next event queued for processing could not be
   *     retrieved
   */
  Optional<Event> getNextEventQueuedForProcessing(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Process the event.
   *
   * @param event the event
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the event could not be processed
   */
  void processEvent(Event event) throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Publish the event.
   *
   * @param tenantId the ID for the tenant
   * @param eventType the event type
   * @param objectType the type of object the event is associated with
   * @param objectId the ID for the object the event is associated with
   * @param actor the person or system who completed the action that led to the event
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateEventException if the event already exists
   * @throws ServiceUnavailableException if the event could not be published
   */
  void publishEvent(
      UUID tenantId, EventType eventType, ObjectType objectType, UUID objectId, String actor)
      throws InvalidArgumentException, DuplicateEventException, ServiceUnavailableException;

  /**
   * Publish the event.
   *
   * @param tenantId the ID for the tenant
   * @param event event
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateEventException if the event already exists
   * @throws ServiceUnavailableException if the event could not be published
   */
  void publishEvent(UUID tenantId, Event event)
      throws InvalidArgumentException, DuplicateEventException, ServiceUnavailableException;

  /**
   * Reset the event locks.
   *
   * @param tenantId the ID for the tenant
   * @param status the current status of the events that have been locked
   * @param newStatus the new status for the events that have been unlocked
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the event locks could not be reset
   */
  void resetEventLocks(UUID tenantId, EventStatus status, EventStatus newStatus)
      throws InvalidArgumentException, ServiceUnavailableException;

  /** Trigger the event processing. */
  void triggerEventProcessing();

  /**
   * Unlock a locked event.
   *
   * @param tenantId the ID for the tenant
   * @param eventId the ID for the event
   * @param status the new status for the unlocked event
   * @throws InvalidArgumentException if an argument is invalid
   * @throws EventNotFoundException if the event could not be found
   * @throws ServiceUnavailableException if the event could not be unlocked
   */
  void unlockEvent(UUID tenantId, UUID eventId, EventStatus status)
      throws InvalidArgumentException, EventNotFoundException, ServiceUnavailableException;

  /** The {@code TriggerEventProcessingEvent} record. */
  record TriggerEventProcessingEvent() {}
}
