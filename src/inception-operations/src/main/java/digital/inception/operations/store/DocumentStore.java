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

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentException;
import digital.inception.operations.exception.DuplicateDocumentNoteException;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSortBy;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.UpdateDocumentNoteRequest;
import java.util.UUID;

/**
 * The {@code DocumentStore} interface defines the functionality provided by a case store, which
 * manages the persistence of documents.
 *
 * @author Marcus Portmann
 */
public interface DocumentStore {

  /**
   * Create the document.
   *
   * @param tenantId the ID for the tenant
   * @param document the document
   * @return the document
   * @throws DuplicateDocumentException if the document already exists
   * @throws ServiceUnavailableException if the document could not be created
   */
  Document createDocument(UUID tenantId, Document document)
      throws DuplicateDocumentException, ServiceUnavailableException;

  /**
   * Create the document note.
   *
   * @param tenantId the ID for the tenant
   * @param documentNote the document note
   * @throws DuplicateDocumentNoteException if the document note already exists
   * @throws ServiceUnavailableException if the document note could not be created
   */
  DocumentNote createDocumentNote(UUID tenantId, DocumentNote documentNote)
      throws DuplicateDocumentNoteException, ServiceUnavailableException;

  /**
   * Delete the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be deleted
   */
  void deleteDocument(UUID tenantId, UUID documentId)
      throws DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Delete the document note.
   *
   * @param tenantId the ID for the tenant
   * @param documentNoteId the ID for the document note
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be deleted
   */
  void deleteDocumentNote(UUID tenantId, UUID documentNoteId)
      throws DocumentNoteNotFoundException, ServiceUnavailableException;

  /**
   * Returns whether a document with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the document is associated with
   * @param documentId the ID for the document
   * @return {@code true} if a document with the specified tenant ID and ID exists or {@code false}
   *     otherwise
   * @throws ServiceUnavailableException if the existence of the document could not be determined
   */
  boolean documentExists(UUID tenantId, UUID documentId) throws ServiceUnavailableException;

  /**
   * Retrieve the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document
   * @return the document
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be retrieved
   */
  Document getDocument(UUID tenantId, UUID documentId)
      throws DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the document note.
   *
   * @param tenantId the ID for the tenant
   * @param documentNoteId the ID for the document note
   * @return the document note
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be retrieved
   */
  DocumentNote getDocumentNote(UUID tenantId, UUID documentNoteId)
      throws DocumentNoteNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the document notes for the document.
   *
   * @param tenantId the ID for the tenant
   * @param documentId the ID for the document the document notes are associated with
   * @param filter the filter to apply to the document notes
   * @param sortBy the method used to sort the document notes e.g. by created
   * @param sortDirection the sort direction to apply to the document notes
   * @param pageIndex the page index
   * @param pageSize the page size
   * @param maxResults the maximum number of document notes that should be retrieved
   * @return the document notes
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
      throws DocumentNotFoundException, ServiceUnavailableException;

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
      throws ServiceUnavailableException;

  /**
   * Update the document.
   *
   * @param tenantId the ID for the tenant
   * @param document the document
   * @return the document
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be updated
   */
  Document updateDocument(UUID tenantId, Document document)
      throws DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Update the document note.
   *
   * @param tenantId the ID for the tenant
   * @param documentNote the document note
   * @throws DocumentNoteNotFoundException if the document note could not be found
   * @throws ServiceUnavailableException if the document note could not be updated
   */
  DocumentNote updateDocumentNote(UUID tenantId, DocumentNote documentNote)
      throws DocumentNoteNotFoundException, ServiceUnavailableException;
}
