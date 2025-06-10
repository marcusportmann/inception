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
import digital.inception.operations.exception.InteractionSourceNotFoundException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentSortBy;
import digital.inception.operations.model.InteractionAttachmentSummaries;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourceType;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import java.util.List;
import java.util.UUID;

/**
 * The {@code InteractionService} interface defines the functionality provided by an Interaction
 * Service implementation.
 *
 * @author Marcus Portmann
 */
public interface InteractionService {

  /** The ID for the default tenant. */
  UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

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
   * @param sourceId the ID for the interaction source the interactions are associated with
   * @param status the status filter to apply to the interaction summaries
   * @param filter the filter to apply to the interaction summaries
   * @param sortBy the method used to sort the interaction summaries e.g. by timestamp
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
      UUID sourceId,
      InteractionStatus status,
      String filter,
      InteractionSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException,
          InteractionSourceNotFoundException,
          ServiceUnavailableException;

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
   * Returns whether an interaction with the specified source reference for the interaction source
   * with the specified ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source specific reference for the interaction
   * @return {@code true} if an interaction with the specified source reference for the interaction
   *     source with the specified ID exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the interaction failed
   */
  boolean interactionExistsWithSourceIdAndSourceReference(
      UUID tenantId, UUID sourceId, String sourceReference)
      throws InvalidArgumentException, ServiceUnavailableException;


  /**
   * Returns whether an interaction with the specified ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction
   * @return {@code true} if an interaction with the specified ID exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the interaction failed
   */
  boolean interactionExistsWithId(
      UUID tenantId, UUID interactionId)
      throws InvalidArgumentException, ServiceUnavailableException;


  /**
   * Returns whether an interaction attachment with the specified ID exists.
   *
   * @param tenantId the ID for the tenant
   * @param interactionAttachmentId the ID for the interaction attachment
   * @return {@code true} if an interaction attachment with the specified ID exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the interaction attachment failed
   */
  boolean interactionAttachmentExistsWithId(
      UUID tenantId, UUID interactionAttachmentId)
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
