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

import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.exception.DuplicateInteractionAttachmentException;
import digital.inception.operations.exception.DuplicateInteractionException;
import digital.inception.operations.exception.DuplicateInteractionNoteException;
import digital.inception.operations.exception.InteractionAttachmentNotFoundException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InteractionNoteNotFoundException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentSortBy;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
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
   * Create the interaction.
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
   * Create the interaction attachment.
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
   * Create the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param interactionNote the interaction note
   * @return the interaction note
   * @throws DuplicateInteractionNoteException if the interaction note already exists
   * @throws ServiceUnavailableException if the interaction note could not be created
   */
  InteractionNote createInteractionNote(UUID tenantId, InteractionNote interactionNote)
      throws DuplicateInteractionNoteException, ServiceUnavailableException;

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
   * Delete the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param interactionNoteId the ID for the interaction note
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the interaction note could not be deleted
   */
  void deleteInteractionNote(UUID tenantId, UUID interactionNoteId)
      throws InteractionNoteNotFoundException, ServiceUnavailableException;

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
   * Retrieve the summaries for the interaction attachments for the interaction with the specified
   * ID.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction the interaction attachments are associated with
   * @param filter the filter to apply to the interaction attachment summaries
   * @param sortBy the method used to sort the interaction attachment summaries e.g. by name
   * @param sortDirection the sort direction to apply to the interaction attachment summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of interaction attachment summaries that should be
   *     retrieved
   * @return the summaries for the interaction attachments for the interaction
   * @throws ServiceUnavailableException if the interaction attachment summaries could not be
   *     retrieved
   */
  InteractionAttachmentSummaries getInteractionAttachmentSummaries(
      UUID tenantId,
      UUID interactionId,
      String filter,
      InteractionAttachmentSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException;

  /**
   * Retrieve the ID for the interaction with the specified source reference and source ID.
   *
   * @param tenantId the ID for the tenant
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source-specific reference
   * @return an Optional containing the ID for the interaction with the specified source reference
   *     and source ID or an empty optional if the interaction could not be found
   * @throws ServiceUnavailableException if the ID for the interaction with the specified source
   *     reference and source ID could not be retrieved
   */
  Optional<UUID> getInteractionIdBySourceIdAndSourceReference(
      UUID tenantId, UUID sourceId, String sourceReference) throws ServiceUnavailableException;

  /**
   * Retrieve the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param interactionNoteId the ID for the interaction note
   * @return the interaction note
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the interaction note could not be retrieved
   */
  InteractionNote getInteractionNote(UUID tenantId, UUID interactionNoteId)
      throws InteractionNoteNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction notes for the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction the interaction notes are associated with
   * @param filter the filter to apply to the interaction notes
   * @param sortBy the method used to sort the interaction notes e.g. by created
   * @param sortDirection the sort direction to apply to the interaction notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of interaction notes that should be retrieved
   * @return the interaction notes
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction notes could not be retrieved
   */
  InteractionNotes getInteractionNotes(
      UUID tenantId,
      UUID interactionId,
      String filter,
      InteractionNoteSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws InteractionNotFoundException, ServiceUnavailableException;

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
      UUID sourceId,
      InteractionStatus status,
      String filter,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException;

  /**
   * Retrieve the next interaction queued for processing.
   *
   * <p>The interaction will be locked to prevent duplicate processing.
   *
   * @return an Optional containing the next interaction queued for processing or an empty Optional
   *     if no interactions are currently queued for processing
   * @throws ServiceUnavailableException if the next interaction queued for processing could not be
   *     retrieved
   */
  Optional<Interaction> getNextInteractionQueuedForProcessing() throws ServiceUnavailableException;

  /**
   * Returns whether an interaction attachment with the specified ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachmentId the ID for the interaction attachment
   * @return {@code true} if an interaction attachment with the specified ID exists or {@code false}
   *     otherwise
   * @throws ServiceUnavailableException if the check for the interaction attachment failed
   */
  boolean interactionAttachmentExistsWithId(UUID tenantId, UUID interactionAttachmentId)
      throws ServiceUnavailableException;

  /**
   * Check whether the interaction with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the interaction is associated with
   * @param interactionId the ID for the interaction
   * @return {@code true} if the interaction with the specified tenant ID and ID exists or {@code
   *     false} otherwise
   * @throws ServiceUnavailableException if the existence of the interaction could not be determined
   */
  boolean interactionExists(UUID tenantId, UUID interactionId) throws ServiceUnavailableException;

  /**
   * Check whether the interaction note with the specified tenant ID, interaction ID and ID exists.
   *
   * @param tenantId the ID for the tenant the interaction note is associated with
   * @param interactionId the ID for the interaction the interaction note is associated with
   * @param interactionNoteId the ID for the interaction note
   * @return {@code true} if the interaction note with the specified tenant ID, interaction ID and
   *     ID exists or {@code false} otherwise
   * @throws ServiceUnavailableException if the existence of the interaction note could not be
   *     determined
   */
  boolean interactionNoteExists(UUID tenantId, UUID interactionId, UUID interactionNoteId)
      throws ServiceUnavailableException;

  /**
   * Reset the interaction locks.
   *
   * @param status the current status of the interactions that have been locked
   * @param newStatus the new status for the interactions that have been unlocked
   * @throws ServiceUnavailableException if the interaction locks could not be reset
   */
  void resetInteractionLocks(InteractionStatus status, InteractionStatus newStatus)
      throws ServiceUnavailableException;

  /**
   * Unlock a locked interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @param status the new status for the unlocked interaction
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be unlocked
   */
  void unlockInteraction(UUID tenantId, UUID interactionId, InteractionStatus status)
      throws InteractionNotFoundException, ServiceUnavailableException;

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

  /**
   * Update the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param interactionNote the interaction note
   * @return the updated interaction note
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the interaction note could not be updated
   */
  InteractionNote updateInteractionNote(UUID tenantId, InteractionNote interactionNote)
      throws InteractionNoteNotFoundException, ServiceUnavailableException;
}
