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
import digital.inception.operations.model.Event;
import digital.inception.operations.model.EventType;
import digital.inception.operations.model.ObjectType;
import java.util.List;
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
}
