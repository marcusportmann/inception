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
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.exception.DuplicateInteractionAttachmentException;
import digital.inception.operations.exception.DuplicateInteractionException;
import digital.inception.operations.exception.DuplicateInteractionSourceException;
import digital.inception.operations.exception.InteractionAttachmentNotFoundException;
import digital.inception.operations.exception.InteractionNotFoundException;
import digital.inception.operations.exception.InteractionNoteNotFoundException;
import digital.inception.operations.exception.InteractionSourceNotFoundException;
import digital.inception.operations.exception.PartyNotFoundException;
import digital.inception.operations.model.AssignInteractionRequest;
import digital.inception.operations.model.CreateInteractionNoteRequest;
import digital.inception.operations.model.DelinkPartyFromInteractionRequest;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentSortBy;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionDirection;
import digital.inception.operations.model.InteractionNote;
import digital.inception.operations.model.InteractionNoteSortBy;
import digital.inception.operations.model.InteractionNotes;
import digital.inception.operations.model.InteractionProcessingResult;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourcePermission;
import digital.inception.operations.model.InteractionSourceSummary;
import digital.inception.operations.model.InteractionSourceType;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.LinkPartyToInteractionRequest;
import digital.inception.operations.model.TransferInteractionRequest;
import digital.inception.operations.model.UpdateInteractionNoteRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code InteractionService} interface defines the functionality provided by an Interaction
 * Service implementation.
 *
 * @author Marcus Portmann
 */
public interface InteractionService {

  /**
   * Assign an interaction to a user.
   *
   * @param tenantId the ID for the tenant
   * @param assignInteractionRequest the request to assign an interaction to a user
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be assigned to the user
   */
  void assignInteraction(UUID tenantId, AssignInteractionRequest assignInteractionRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Create the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interaction the interaction
   * @return the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateInteractionException if the interaction already exists
   * @throws ServiceUnavailableException if the interaction could not be created
   */
  Interaction createInteraction(UUID tenantId, Interaction interaction)
      throws InvalidArgumentException, DuplicateInteractionException, ServiceUnavailableException;

  /**
   * Create the interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachment the interaction attachment
   * @return the interaction attachment
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateInteractionAttachmentException if the interaction attachment already exists
   * @throws ServiceUnavailableException if the interaction attachment could not be created
   */
  InteractionAttachment createInteractionAttachment(
      UUID tenantId, InteractionAttachment interactionAttachment)
      throws InvalidArgumentException,
          DuplicateInteractionAttachmentException,
          ServiceUnavailableException;

  /**
   * Create the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param createInteractionNoteRequest the request to create an interaction note
   * @param createdBy the person or system that created the interaction note
   * @return the interaction note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction note could not be created
   */
  InteractionNote createInteractionNote(
      UUID tenantId, CreateInteractionNoteRequest createInteractionNoteRequest, String createdBy)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Create the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSource the interaction source
   * @return the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateInteractionSourceException if the interaction source already exists
   * @throws ServiceUnavailableException if the interaction source could not be created
   */
  InteractionSource createInteractionSource(UUID tenantId, InteractionSource interactionSource)
      throws InvalidArgumentException,
          DuplicateInteractionSourceException,
          ServiceUnavailableException;

  /**
   * Delete the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be deleted
   */
  void deleteInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Delete the interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachmentId the ID for the interaction attachment
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionAttachmentNotFoundException if the interaction attachment could not be found
   * @throws ServiceUnavailableException if the interaction attachment could not be deleted
   */
  void deleteInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param interactionNoteId the ID for the interaction note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the interaction note could not be deleted
   */
  void deleteInteractionNote(UUID tenantId, UUID interactionNoteId)
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction source could not be deleted
   */
  void deleteInteractionSource(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Delink the party from the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param delinkPartyFromInteractionRequest the request to delink the party from the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the party could not be delinked from the interaction
   */
  void delinkPartyFromInteraction(
      UUID tenantId, DelinkPartyFromInteractionRequest delinkPartyFromInteractionRequest)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be retrieved
   */
  Interaction getInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachmentId the ID for the interaction attachment
   * @return the interaction attachment
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionAttachmentNotFoundException if the interaction attachment could not be found
   * @throws ServiceUnavailableException if the interaction attachment could not be retrieved
   */
  InteractionAttachment getInteractionAttachment(UUID tenantId, UUID interactionAttachmentId)
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException;

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
   * @return the summaries for the interaction attachments for the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
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
      Integer pageSize)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param interactionNoteId the ID for the interaction note
   * @return the interaction note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the interaction note could not be retrieved
   */
  InteractionNote getInteractionNote(UUID tenantId, UUID interactionNoteId)
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the interaction notes for the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction the interaction notes are associated with
   * @param filter the filter to apply to the interaction notes
   * @param sortBy the method used to sort the interaction notes e.g. by created at
   * @param sortDirection the sort direction to apply to the interaction notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the interaction notes
   * @throws InvalidArgumentException if an argument is invalid
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
      Integer pageSize)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @return the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction source could not be retrieved
   */
  InteractionSource getInteractionSource(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the ID for the interaction source the interaction is associated with.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return the ID for the interaction source the interaction is associated with
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the ID for the interaction source could not be retrieved
   *     for the interaction
   */
  UUID getInteractionSourceIdForInteraction(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the ID for the interaction source the interaction note is associated with.
   *
   * @param tenantId the ID for the tenant
   * @param interactionNoteId the ID for the interaction note
   * @return the ID for the interaction source the interaction note is associated with
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the ID for the interaction source could not be retrieved
   *     for the interaction note
   */
  UUID getInteractionSourceIdForInteractionNote(UUID tenantId, UUID interactionNoteId)
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the permissions for the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @return the permissions for the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the permissions for the interaction source could not be
   *     retrieved
   */
  List<InteractionSourcePermission> getInteractionSourcePermissions(
      UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the summaries for the interaction sources.
   *
   * @param tenantId the ID for the tenant
   * @return the summaries for the interaction sources
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction source summaries could not be retrieved
   */
  List<InteractionSourceSummary> getInteractionSourceSummaries(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve all the interaction sources.
   *
   * @param tenantId the ID for the tenant
   * @return the interaction sources
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction sources could not be retrieved
   */
  List<InteractionSource> getInteractionSources(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve all the interaction sources.
   *
   * @return the interaction sources
   * @throws ServiceUnavailableException if the interaction sources could not be retrieved
   */
  List<InteractionSource> getInteractionSources() throws ServiceUnavailableException;

  /**
   * Retrieve the interaction sources with the specified type.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceType the interaction source type for the interaction sources
   * @return the interaction sources
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction sources with the specified type could
   *     not be retrieved
   */
  List<InteractionSource> getInteractionSources(
      UUID tenantId, InteractionSourceType interactionSourceType)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the interactions for the interaction source with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source the interactions are associated
   *     with
   * @param status the status filter to apply to the interaction summaries
   * @param direction the direction filter to apply to the interaction summaries, i.e., inbound or
   *     outbound
   * @param filter the filter to apply to the interaction summaries
   * @param sortBy the method used to sort the interaction summaries e.g. by occurred
   * @param sortDirection the sort direction to apply to the interaction summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the interactions
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction summaries could not be retrieved
   */
  InteractionSummaries getInteractionSummaries(
      UUID tenantId,
      UUID interactionSourceId,
      InteractionStatus status,
      InteractionDirection direction,
      String filter,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Returns the maximum number of processing attempts for an interaction.
   *
   * @return the maximum number of processing attempts for an interaction
   */
  int getMaximumInteractionProcessingAttempts();

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
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the interaction attachment failed
   */
  boolean interactionAttachmentExistsWithId(UUID tenantId, UUID interactionAttachmentId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Returns whether an interaction attachment with the specified hash exists for the interaction
   * with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction that the interaction attachment is associated
   *     with
   * @param hash the hash for the interaction attachment
   * @return {@code true} if an interaction attachment with the specified hash exists for the
   *     interaction with the specified ID or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the interaction attachment failed
   */
  boolean interactionAttachmentExistsWithInteractionIdAndHash(
      UUID tenantId, UUID interactionId, String hash)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the interaction exists.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return {@code true} if the interaction exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the existence of the interaction could not be determined
   */
  boolean interactionExists(UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Returns whether an interaction with the specified source reference for the interaction source
   * with the specified ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source-specific reference for the interaction
   * @return {@code true} if an interaction with the specified source reference for the interaction
   *     source with the specified ID exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the interaction failed
   */
  boolean interactionExistsWithSourceIdAndSourceReference(
      UUID tenantId, UUID sourceId, String sourceReference)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the interaction note exists.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction the interaction note is associated with
   * @param interactionNoteId the ID for the interaction note
   * @return {@code true} if the interaction note exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the existence of the interaction note could not be
   *     determined
   */
  boolean interactionNoteExists(UUID tenantId, UUID interactionId, UUID interactionNoteId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the interaction source exists.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @return {@code true} if the interaction source exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the existence of the interaction source could not be
   *     determined
   */
  boolean interactionSourceExists(UUID tenantId, UUID interactionSourceId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Link a party to an interaction.
   *
   * @param tenantId the ID for the tenant
   * @param linkPartyToInteractionRequest the request to link a party to an interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws PartyNotFoundException if the party could not be found
   * @throws ServiceUnavailableException if the party could not be linked to the interaction
   */
  void linkPartyToInteraction(
      UUID tenantId, LinkPartyToInteractionRequest linkPartyToInteractionRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          PartyNotFoundException,
          ServiceUnavailableException;

  /**
   * Process the interaction.
   *
   * @param interaction the interaction
   * @return the result of processing the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction could not be processed
   */
  InteractionProcessingResult processInteraction(Interaction interaction)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Reset the interaction locks.
   *
   * @param status the current status of the interactions that have been locked
   * @param newStatus the new status for the interactions that have been unlocked
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction locks could not be reset
   */
  void resetInteractionLocks(InteractionStatus status, InteractionStatus newStatus)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Synchronize the interaction source.
   *
   * <p>In the case of a mailbox interaction source, this will retrieve the latest email messages
   * from the associated mailbox and create the corresponding interactions.
   *
   * @param interactionSource the interaction source
   * @return the number of new interactions
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the interaction source could not be synchronized
   */
  int synchronizeInteractionSource(InteractionSource interactionSource)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Transfer an interaction to an interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param transferInteractionRequest the request to transfer an interaction to an interaction
   *     source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction could not be transferred to the
   *     interaction source
   */
  void transferInteraction(UUID tenantId, TransferInteractionRequest transferInteractionRequest)
      throws InvalidArgumentException,
          InteractionNotFoundException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

  /** Trigger the interaction processing. */
  void triggerInteractionProcessing();

  /** Trigger the interaction source synchronization. */
  void triggerInteractionSourceSynchronization();

  /**
   * Unlock a locked interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @param status the new status for the unlocked interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be unlocked
   */
  void unlockInteraction(UUID tenantId, UUID interactionId, InteractionStatus status)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Update the interaction.
   *
   * @param tenantId the ID for the tenant
   * @param interaction the interaction
   * @return the interaction
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNotFoundException if the interaction could not be found
   * @throws ServiceUnavailableException if the interaction could not be updated
   */
  Interaction updateInteraction(UUID tenantId, Interaction interaction)
      throws InvalidArgumentException, InteractionNotFoundException, ServiceUnavailableException;

  /**
   * Update the interaction attachment.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachment the interaction attachment
   * @return the interaction attachment
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionAttachmentNotFoundException if the interaction attachment could not be found
   * @throws ServiceUnavailableException if the interaction attachment could not be updated
   */
  InteractionAttachment updateInteractionAttachment(
      UUID tenantId, InteractionAttachment interactionAttachment)
      throws InvalidArgumentException,
          InteractionAttachmentNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the interaction note.
   *
   * @param tenantId the ID for the tenant
   * @param updateInteractionNoteRequest the request to update an interaction note
   * @param updatedBy the person or system updating the interaction note
   * @return the updated interaction note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionNoteNotFoundException if the interaction note could not be found
   * @throws ServiceUnavailableException if the interaction note could not be updated
   */
  InteractionNote updateInteractionNote(
      UUID tenantId, UpdateInteractionNoteRequest updateInteractionNoteRequest, String updatedBy)
      throws InvalidArgumentException,
          InteractionNoteNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSource the interaction source
   * @return the interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws InteractionSourceNotFoundException if the interaction source could not be found
   * @throws ServiceUnavailableException if the interaction source could not be updated
   */
  InteractionSource updateInteractionSource(UUID tenantId, InteractionSource interactionSource)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;
}
