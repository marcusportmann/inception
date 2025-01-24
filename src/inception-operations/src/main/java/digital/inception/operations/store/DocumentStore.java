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
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentNotFoundException;
import digital.inception.operations.model.DuplicateDocumentException;
import java.util.UUID;

/**
 * The <b>DocumentStore</b> interface defines the functionality provided by a case store, which
 * manages the persistence of documents.
 *
 * @author Marcus Portmann
 */
public interface DocumentStore {

  /**
   * Create the new document.
   *
   * @param document the document
   * @return the document
   * @throws DuplicateDocumentException if the document already exists
   * @throws ServiceUnavailableException if the document could not be created
   */
  Document createDocument(Document document)
      throws DuplicateDocumentException, ServiceUnavailableException;

  /**
   * Delete the document.
   *
   * @param documentId the ID for the document
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be deleted
   */
  void deleteDocument(UUID documentId)
      throws DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the document.
   *
   * @param documentId the ID for the document
   * @return the document
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be retrieved
   */
  Document getDocument(UUID documentId)
      throws DocumentNotFoundException, ServiceUnavailableException;

  /**
   * Update the document.
   *
   * @param document the document
   * @return the document
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be updated
   */
  Document updateDocument(Document document)
      throws DocumentNotFoundException, ServiceUnavailableException;
}
