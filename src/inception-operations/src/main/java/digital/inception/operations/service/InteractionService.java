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

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.model.DuplicateInteractionAttachmentException;
import digital.inception.operations.model.DuplicateInteractionException;
import digital.inception.operations.model.DuplicateMailboxInteractionSourceException;
import digital.inception.operations.model.DuplicateWhatsAppInteractionSourceException;
import digital.inception.operations.model.Interaction;
import digital.inception.operations.model.InteractionAttachment;
import digital.inception.operations.model.InteractionAttachmentNotFoundException;
import digital.inception.operations.model.InteractionNotFoundException;
import digital.inception.operations.model.InteractionSortBy;
import digital.inception.operations.model.InteractionStatus;
import digital.inception.operations.model.InteractionSummaries;
import digital.inception.operations.model.MailboxInteractionSource;
import digital.inception.operations.model.MailboxInteractionSourceNotFoundException;
import digital.inception.operations.model.WhatsAppInteractionSource;
import digital.inception.operations.model.WhatsAppInteractionSourceNotFoundException;
import java.util.List;
import java.util.UUID;

/**
 * The <b>InteractionService</b> interface defines the functionality provided by an Interaction
 * Service implementation.
 *
 * @author Marcus Portmann
 */
public interface InteractionService {

  /** The ID for the default tenant. */
  UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  /**
   * Create the new interaction.
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
   * Create the new interaction attachment.
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
   * Create a mailbox interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param mailboxInteractionSource the mailbox interaction source
   * @return the mailbox interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateMailboxInteractionSourceException if the mailbox interaction source already
   *     exists
   * @throws ServiceUnavailableException if the mailbox interaction source could not be created
   */
  MailboxInteractionSource createMailboxInteractionSource(
      UUID tenantId, MailboxInteractionSource mailboxInteractionSource)
      throws InvalidArgumentException,
          DuplicateMailboxInteractionSourceException,
          ServiceUnavailableException;

  /**
   * Create a WhatsApp interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param whatsAppInteractionSource the WhatsApp interaction source
   * @return the WhatsApp interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateWhatsAppInteractionSourceException if the WhatsApp interaction source already
   *     exists
   * @throws ServiceUnavailableException if the WhatsApp interaction source could not be created
   */
  WhatsAppInteractionSource createWhatsAppInteractionSource(
      UUID tenantId, WhatsAppInteractionSource whatsAppInteractionSource)
      throws InvalidArgumentException,
          DuplicateWhatsAppInteractionSourceException,
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
   * Delete the mailbox interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param mailboxInteractionSourceId the ID for the mailbox interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailboxInteractionSourceNotFoundException if the mailbox interaction source could not
   *     be found
   * @throws ServiceUnavailableException if the mailbox interaction source could not be deleted
   */
  void deleteMailboxInteractionSource(UUID tenantId, String mailboxInteractionSourceId)
      throws InvalidArgumentException,
          MailboxInteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the WhatsApp interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param whatsAppInteractionSourceId the ID for the WhatsApp interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WhatsAppInteractionSourceNotFoundException if the WhatsApp interaction source could not
   *     be found
   * @throws ServiceUnavailableException if the WhatsApp interaction source could not be deleted
   */
  void deleteWhatsAppInteractionSource(UUID tenantId, String whatsAppInteractionSourceId)
      throws InvalidArgumentException,
          WhatsAppInteractionSourceNotFoundException,
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
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the mailbox interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param mailboxInteractionSourceId the ID for the mailbox interaction source
   * @return the mailbox interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws MailboxInteractionSourceNotFoundException if the mailbox interaction source could not
   *     be found
   * @throws ServiceUnavailableException if the mailbox interaction source could not be retrieved
   */
  MailboxInteractionSource getMailboxInteractionSource(
      UUID tenantId, String mailboxInteractionSourceId)
      throws InvalidArgumentException,
          MailboxInteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve all the mailbox interaction sources.
   *
   * @param tenantId the ID for the tenant
   * @return the mailbox interaction sources
   * @throws ServiceUnavailableException if the mailbox interaction sources could not be retrieved
   */
  List<MailboxInteractionSource> getMailboxInteractionSources(UUID tenantId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the WhatsApp interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param whatsAppInteractionSourceId the ID for the WhatsApp interaction source
   * @return the WhatsApp interaction source
   * @throws InvalidArgumentException if an argument is invalid
   * @throws WhatsAppInteractionSourceNotFoundException if the WhatsApp interaction source could not
   *     be found
   * @throws ServiceUnavailableException if the WhatsApp interaction source could not be retrieved
   */
  WhatsAppInteractionSource getWhatsAppInteractionSource(
      UUID tenantId, String whatsAppInteractionSourceId)
      throws InvalidArgumentException,
          WhatsAppInteractionSourceNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve all the WhatsApp interaction sources.
   *
   * @param tenantId the ID for the tenant
   * @return the WhatsApp interaction sources
   * @throws ServiceUnavailableException if the WhatsApp interaction sources could not be retrieved
   */
  List<WhatsAppInteractionSource> getWhatsAppInteractionSources(UUID tenantId)
      throws ServiceUnavailableException;

  /**
   * Returns whether an interaction attachment with the specified hash exists for the interaction
   * with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param interactionId the ID for the interaction that the interaction attachment is associated
   *     with
   * @param hash the hash for the interaction attachment
   * @return <b>true</b> if an interaction attachment with the specified hash exists for the
   *     interaction with the specified ID or <b>false</b> otherwise
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
   * @return <b>true</b> if an interaction with the specified source reference for the interaction
   *     source with the specified ID exists or <b>false</b> otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the interaction failed
   */
  boolean interactionExistsWithSourceIdAndSourceReference(
      UUID tenantId, String sourceId, String sourceReference)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Synchronize the mailbox interaction source.
   *
   * @param tenantId the ID for the tenant
   *     <p>This will retrieve the latest email messages from the associated mailbox and create the
   *     corresponding interactions.
   * @param mailboxInteractionSource the mailbox interaction source
   * @return the number of new interactions
   * @throws ServiceUnavailableException if the mailbox interaction source could not be synchronized
   */
  int synchronizeMailboxInteractionSource(
      UUID tenantId, MailboxInteractionSource mailboxInteractionSource)
      throws ServiceUnavailableException;

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
}
