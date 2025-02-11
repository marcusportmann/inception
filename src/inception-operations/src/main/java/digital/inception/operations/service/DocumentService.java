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
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionNotFoundException;
import digital.inception.operations.model.DocumentNotFoundException;
import digital.inception.operations.model.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.UpdateDocumentRequest;
import java.util.UUID;

/**
 * The <b>DocumentService</b> interface defines the functionality provided by a Document Service
 * implementation.
 *
 * @author Marcus Portmann
 */
public interface DocumentService {

  /** The ID for the default tenant. */
  UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

  /**
   * Create a new document.
   *
   * @param tenantId the ID for the tenant
   * @param createDocumentRequest the request to create a document
   * @param createdBy the person or system requesting the document creation
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
   * Create the new document definition.
   *
   * @param documentDefinition the document definition
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateDocumentDefinitionException if the document definition already exists
   * @throws ServiceUnavailableException if the document definition could not be created
   */
  void createDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionException,
          ServiceUnavailableException;

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
   * Check whether the document definition exists.
   *
   * @param documentDefinitionId the ID for the document definition
   * @return <b>true</b> if the document definition exists or <b>false</b> otherwise
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the check for the document definition failed
   */
  boolean documentDefinitionExists(String documentDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException;

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
   * Update the document.
   *
   * @param tenantId the ID for the tenant
   * @param updateDocumentRequest the request to update the document
   * @param updatedBy the person or system requesting the document update
   * @return the updated document
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DocumentNotFoundException if the document could not be found
   * @throws ServiceUnavailableException if the document could not be created
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
}
