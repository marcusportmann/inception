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
import digital.inception.operations.exception.DocumentAttributeDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentAttributeDefinitionException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentAttributeDefinition;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.DocumentDefinitionSummary;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.SearchDocumentsRequest;
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
   * @param createdBy the person or system creating the document
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
   * Create the document attribute definition.
   *
   * @param documentAttributeDefinition the document attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateDocumentAttributeDefinitionException if the document attribute definition
   *     already exists
   * @throws ServiceUnavailableException if the document attribute definition could not be created
   */
  void createDocumentAttributeDefinition(DocumentAttributeDefinition documentAttributeDefinition)
      throws InvalidArgumentException,
          DuplicateDocumentAttributeDefinitionException,
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
   * @param createdBy the person or system creating the document note
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
   * Delete the document attribute definition.
   *
   * @param documentAttributeDefinitionCode the code for the document attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentAttributeDefinitionNotFoundException if the document attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the document attribute definition could not be deleted
   */
  void deleteDocumentAttributeDefinition(String documentAttributeDefinitionCode)
      throws InvalidArgumentException,
          DocumentAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

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
   * Check whether the document definition category exists.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @return {@code true} if the document definition category exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the document definition category failed
   */
  boolean documentDefinitionCategoryExists(String documentDefinitionCategoryId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
   * Check whether the document definition exists and is associated with the document definition
   * category with the specified ID.
   *
   * @param documentDefinitionCategoryId the ID for the document definition category
   * @param documentDefinitionId the ID for the document definition
   * @return {@code true} if the document definition exists or {@code false} otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the document definition failed
   */
  boolean documentDefinitionExists(String documentDefinitionCategoryId, String documentDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Check whether the document exists.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @return {@code true} if the document exists or {@code false} otherwise
   * @throws ServiceUnavailableException if the existence of the document could not be determined
   */
  boolean documentExists(UUID tenantId, UUID documentId) throws ServiceUnavailableException;

  /**
   * Check whether the document note exists.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document the document note is associated with
   * @param documentNoteId the ID for the document note
   * @return {@code true} if the document note exists or {@code false} otherwise
   * @throws ServiceUnavailableException if the existence of the document note could not be
   *     determined
   */
  boolean documentNoteExists(UUID tenantId, UUID documentId, UUID documentNoteId)
      throws ServiceUnavailableException;

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
   * Retrieve the document attribute definition.
   *
   * @param documentAttributeDefinitionCode the code for the document attribute definition
   * @return the document attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentAttributeDefinitionNotFoundException if the document attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the document attribute definition could not be retrieved
   */
  DocumentAttributeDefinition getDocumentAttributeDefinition(String documentAttributeDefinitionCode)
      throws InvalidArgumentException,
          DocumentAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Retrieve the document attribute definitions.
   *
   * @param tenantId the ID for the tenant
   * @return the document attribute definitions
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the document attribute definitions could not be
   *     retrieved
   */
  List<DocumentAttributeDefinition> getDocumentAttributeDefinitions(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
   * Retrieve the summaries for the document definitions associated with the document definition
   * category with the specified ID.
   *
   * @param tenantId the ID for the tenant
   * @param documentDefinitionCategoryId the ID for the document definition category the document
   *     definitions are associated with
   * @return the summaries for the document definitions associated with the document definition
   *     category with the specified ID
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws ServiceUnavailableException if the document definition summaries could not be retrieved
   */
  List<DocumentDefinitionSummary> getDocumentDefinitionSummaries(
      UUID tenantId, String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException;

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
      Integer pageSize)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the required document attribute definitions.
   *
   * @param tenantId the ID for the tenant
   * @return the required document attribute definitions
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the required document attribute definitions could not be
   *     retrieved
   */
  List<DocumentAttributeDefinition> getRequiredDocumentAttributeDefinitions(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Search for documents.
   *
   * @param tenantId the ID for the tenant
   * @param searchDocumentsRequest the request to search for documents matching specific criteria
   * @return the summaries for the documents matching the search criteria
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the document search failed
   */
  DocumentSummaries searchDocuments(UUID tenantId, SearchDocumentsRequest searchDocumentsRequest)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Update the document.
   *
   * @param tenantId the ID for the tenant
   * @param updateDocumentRequest the request to update the document
   * @param updatedBy the person or system updating the document
   * @return the updated document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be updated
   */
  Document updateDocument(
      UUID tenantId, UpdateDocumentRequest updateDocumentRequest, String updatedBy)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Update the document attribute definition.
   *
   * @param documentAttributeDefinition the document attribute definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentAttributeDefinitionNotFoundException if the document attribute definition could
   *     not be found
   * @throws ServiceUnavailableException if the document attribute definition could not be updated
   */
  void updateDocumentAttributeDefinition(DocumentAttributeDefinition documentAttributeDefinition)
      throws InvalidArgumentException,
          DocumentAttributeDefinitionNotFoundException,
          ServiceUnavailableException;

  /**
   * Update the document definition.
   *
   * @param documentDefinition the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentDefinitionCategoryNotFoundException if the document definition category could
   *     not be found
   * @throws DocumentDefinitionNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be updated
   */
  void updateDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
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
   * @param updatedBy the person or system updating the document note
   * @return the updated document note
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be updated
   */
  DocumentNote updateDocumentNote(
      UUID tenantId, UpdateDocumentNoteRequest updateDocumentNoteRequest, String updatedBy)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException;
}
