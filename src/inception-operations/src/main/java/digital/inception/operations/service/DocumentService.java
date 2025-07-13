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
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSortBy;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.UpdateDocumentNoteRequest;
import digital.inception.operations.model.UpdateDocumentRequest;
import java.util.List;
import java.util.UUID;

/**
 * The {@code DocumentService} interface defines the functionality provided by a Document Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface DocumentService {

  /**
   * Calculate the hash for the document data.
   *
   * @param documentData the document data
   * @return the hash for the document data
   */
  String calculateDocumentDataHash(byte[] documentData);

  /**
   * Create a new document.
   *
   * @param tenantId the ID for the tenant
   * @param createDocumentRequest the request to create a document
   * @param createdBy the username for the user creating the document
   * @return the document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the document could not be created
   */
  Document createDocument(
      UUID tenantId, CreateDocumentRequest createDocumentRequest, String createdBy)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the document definition.
   *
   * @param documentDefinition the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateDocumentDefinitionException if the document definition already exists
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition could not be created
   */
  void createDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Create the document definition category.
   *
   * @param documentDefinitionCategory the document definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateDocumentDefinitionCategoryException if the document definition category
   *     already exists
   * @throws ServiceUnavailableException if the document definition category could not be created
   */
  void createDocumentDefinitionCategory(DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionCategoryException,
          ServiceUnavailableException;

  /**
   * Create the document note.
   *
   * @param tenantId the ID for the tenant
   * @param createDocumentNoteRequest the request to create a document note
   * @param createdBy the username for the user creating the document note
   * @return the document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document note could not be created
   */
  DocumentNote createDocumentNote(
      UUID tenantId, CreateDocumentNoteRequest createDocumentNoteRequest, String createdBy)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Delete the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be deleted
   */
  void deleteDocument(UUID tenantId, UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Delete the document definition.
   *
   * @param documentDefinitionId the ID for the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the document definition could not be deleted
   */
  void deleteDocumentDefinition(String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the document definition category.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition category could not be deleted
   */
  void deleteDocumentDefinitionCategory(String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Delete the document note.
   *
   * @param tenantId the ID for the tenant
   * @param documentNoteId the ID for the document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be deleted
   */
  void deleteDocumentNote(UUID tenantId, UUID documentNoteId)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException;

  /**
   * Check whether the document definition exists.
   *
   * @param documentDefinitionId the ID for the document definition
   * @return {@code true} if the document definition exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the document definition failed
   */
  boolean documentDefinitionExists(String documentDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the document with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the document is associated with
   * @param documentId the ID for the document
   * @return {@code true} if the document with the specified tenant ID and ID exists or {@code
   *     false} otherwise
   * @throws ServiceUnavailableException if the existence of the document could not be determined
   */
  boolean documentExists(UUID tenantId, UUID documentId) throws ServiceUnavailableException;

  /**
   * Retrieve the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @return the document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be retrieved
   */
  Document getDocument(UUID tenantId, UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the document definition.
   *
   * @param documentDefinitionId the ID for the document definition
   * @return the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionNotFoundException if the document definition could not be found
   * @throws ServiceUnavailableException if the document definition could not be retrieved
   */
  DocumentDefinition getDocumentDefinition(String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the document definition categories.
   *
   * @param tenantId the ID for the tenant
   * @return the document definition categories
   * @throws ServiceUnavailableException if the document definition categories could not be
   *     retrieved
   */
  List<DocumentDefinitionCategory> getDocumentDefinitionCategories(UUID tenantId)
      throws ServiceUnavailableException;

  /**
   * Retrieve the document definition category.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @return the document definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition category could not be retrieved
   */
  DocumentDefinitionCategory getDocumentDefinitionCategory(String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the document definitions associated with the document definition category with the
   * specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param documentDefinitionCategoryId the ID for the document definition category the document
   *     definitions are associated with
   * @return the document definitions associated with the document definition category with the
   *     specified ID
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition categories could not be
   *     retrieved
   */
  List<DocumentDefinition> getDocumentDefinitions(
      UUID tenantId, String documentDefinitionCategoryId)
      throws DocumentDefinitionCategoryNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the document note.
   *
   * @param tenantId the ID for the tenant
   * @param documentNoteId the ID for the document note
   * @return the document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be retrieved
   */
  DocumentNote getDocumentNote(UUID tenantId, UUID documentNoteId)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the document notes for the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document the document notes are associated with
   * @param filter the filter to apply to the document notes
   * @param sortBy the method used to sort the document notes, e.g. by created
   * @param sortDirection the sort direction to apply to the document notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of document notes that should be retrieved
   * @return the document notes
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document notes could not be retrieved
   */
  DocumentNotes getDocumentNotes(
      UUID tenantId,
      UUID documentId,
      String filter,
      DocumentNoteSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the documents.
   *
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the document summaries
   * @param sortBy the method used to sort the document summaries e.g. by definition ID
   * @param sortDirection the sort direction to apply to the document summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of document summaries that should be retrieved
   * @return the summaries for the documents
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the document summaries could not be retrieved
   */
  DocumentSummaries getDocumentSummaries(
      UUID tenantId,
      String filter,
      DocumentSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Update the document.
   *
   * @param tenantId the ID for the tenant
   * @param updateDocumentRequest the request to update the document
   * @param updatedBy the username for the user updating the document
   * @return the updated document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be updated
   */
  Document updateDocument(
      UUID tenantId, UpdateDocumentRequest updateDocumentRequest, String updatedBy)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Update the document definition.
   *
   * @param documentDefinition the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be updated
   */
  void updateDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the document definition category.
   *
   * @param documentDefinitionCategory the document definition category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition category could not be updated
   */
  void updateDocumentDefinitionCategory(DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the document note.
   *
   * @param tenantId the ID for the tenant
   * @param updateDocumentNoteRequest the request to update a document note
   * @param updatedBy the username for the user updating the document note
   * @return the updated document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be updated
   */
  DocumentNote updateDocumentNote(
      UUID tenantId, UpdateDocumentNoteRequest updateDocumentNoteRequest, String updatedBy)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException;
}
