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
import digital.inception.operations.model.DuplicateInteractionAttachmentException;
import digital.inception.operations.model.DuplicateInteractionException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentNotFoundException;
import digital.inception.operations.model.InteractionNotFoundException;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code InteractionStore} interface defines the functionality provided by an interaction
 * store, which manages the persistence of interactions and interaction attachments.
 *
 * @author Marcus Portmann
 */
public interface InteractionStore {

  /**
   * Create the new interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interaction the interaction
   * @return the interaction
   * @throws DuplicateInteractionException if the interaction already exists
   * @throws ServiceUnavailableException if the interaction could not be created
   */
  Interaction createInteraction(UUID tenantId, Interaction interaction)
      throws DuplicateInteractionException, ServiceUnavailableException;

  /**
   * Create the new interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachment the interaction attachment
   * @return the interaction attachment
   * @throws DuplicateInteractionAttachmentException if the interaction attachment already exists
   * @throws ServiceUnavailableException if the interaction attachment could not be created
   */
  InteractionAttachment createInteractionAttachment(
      UUID tenantId, InteractionAttachment interactionAttachment)
      throws DuplicateInteractionAttachmentException, ServiceUnavailableException;

  /**
   * Delete the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be deleted
   */
  void deleteInteraction(UUID tenantId, UUID interactionId)
      throws InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Delete the interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachmentId the ID for the interaction attachment
   * @throws InteractionAttachmentNotFoundException if the interaction attachment could not be found
   * @throws ServiceUnavailableException if the interaction attachment could not be deleted
   */
  void deleteInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return the interaction
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be retrieved
   */
  Interaction getInteraction(UUID tenantId, UUID interactionId)
      throws InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachmentId the ID for the interaction attachment
   * @return the interaction attachment
   * @throws InteractionAttachmentNotFoundException if the interaction attachment could not be found
   * @throws ServiceUnavailableException if the interaction attachment could not be retrieved
   */
  InteractionAttachment getInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the ID for the interaction attachment with the specified interaction ID and hash.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction that the interaction attachment is associated
   *     with
   * @param hash the hash for interaction attachment
   * @return an Optional containing the ID for the interaction attachment with the specified
   *     interaction ID and hash or an empty optional if the interaction attachment could not be
   *     found
   * @throws ServiceUnavailableException if the ID for the interaction attachment with the specified
   *     interaction ID and hash could not be retrieved
   */
  Optional<UUID> getInteractionAttachmentIdByInteractionIdAndHash(
      UUID tenantId, UUID interactionId, String hash) throws ServiceUnavailableException;

  /**
   * Retrieve the ID for the interaction with the specified source reference and source ID.
   *
   * @param tenantId the ID for the tenant
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source specific reference
   * @return an Optional containing the ID for the interaction with the specified source reference
   *     and source ID or an empty optional if the interaction could not be found
   * @throws ServiceUnavailableException if the ID for the interaction with the specified source
   *     reference and source ID could not be retrieved
   */
  Optional<UUID> getInteractionIdBySourceIdAndSourceReference(
      UUID tenantId, String sourceId, String sourceReference) throws ServiceUnavailableException;

  /**
   * Retrieve the summaries for the interactions for the interaction source with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param sourceId the ID for the interaction source the interactions are associated with
   * @param status the status filter to apply to the interaction summaries
   * @param filter the filter to apply to the interaction summaries
   * @param sortBy the method used to sort the interaction summaries e.g. by timestamp
   * @param sortDirection the sort direction to apply to the interaction summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of interaction summaries that should be retrieved
   * @return the summaries for the interactions
   * @throws ServiceUnavailableException if the interaction summaries could not be retrieved
   */
  InteractionSummaries getInteractionSummaries(
      UUID tenantId,
      String sourceId,
      InteractionStatus status,
      String filter,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException;

  /**
   * Update the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interaction the interaction
   * @return the interaction
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be updated
   */
  Interaction updateInteraction(UUID tenantId, Interaction interaction)
      throws InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Update the interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachment the interaction attachment
   * @return the interaction attachment
   * @throws InteractionAttachmentNotFoundException if the interaction attachment could not be found
   * @throws ServiceUnavailableException if the interaction attachment could not be updated
   */
  InteractionAttachment updateInteractionAttachment(
      UUID tenantId, InteractionAttachment interactionAttachment)
      throws InteractionAttachmentNotFoundException, ServiceUnavailableException;
}
