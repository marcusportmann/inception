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
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentException;
import digital.inception.operations.model.Document;
import digital.inception.operations.persistence.jpa.DocumentRepository;
import digital.inception.operations.persistence.jpa.DocumentSummaryRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

/**
 * The {@code InternalDocumentStore} class provides the internal document store implementation.
 *
 * @author Marcus Portmann
 */
@Service
@Conditional(InternalDocumentStoreEnabledCondition.class)
@SuppressWarnings("unused")
public class InternalDocumentStore implements DocumentStore {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(InternalDocumentStore.class);

  /** The Document Repository. */
  private final DocumentRepository documentRepository;

  /** The Document Summary Repository. */
  private final DocumentSummaryRepository documentSummaryRepository;

  /**
   * Constructs a new {@code InternalDocumentStore}.
   *
   * @param documentRepository the Document Repository
   * @param documentSummaryRepository the Document Summary Repository
   */
  public InternalDocumentStore(
      DocumentRepository documentRepository, DocumentSummaryRepository documentSummaryRepository) {
    this.documentRepository = documentRepository;
    this.documentSummaryRepository = documentSummaryRepository;
  }

  @Override
  public Document createDocument(UUID tenantId, Document document)
      throws DuplicateDocumentException, ServiceUnavailableException {
    try {
      if (documentRepository.existsById(document.getId())) {
        throw new DuplicateDocumentException(document.getId());
      }

      return documentRepository.saveAndFlush(document);
    } catch (DuplicateDocumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document ("
              + document.getId()
              + ") with the definition ID ("
              + document.getDefinitionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteDocument(UUID tenantId, UUID documentId)
      throws DocumentNotFoundException, ServiceUnavailableException {
    try {
      if (!documentRepository.existsByTenantIdAndId(tenantId, documentId)) {
        throw new DocumentNotFoundException(documentId);
      }

      documentRepository.deleteById(documentId);
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document (" + documentId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public Document getDocument(UUID tenantId, UUID documentId)
      throws DocumentNotFoundException, ServiceUnavailableException {
    try {
      /*
       * NOTE: The search by both tenant ID and document ID includes a security check to ensure
       * that the document not only exists, but is also associated with the specified tenant.
       */
      Optional<Document> documentOptional =
          documentRepository.findByTenantIdAndId(tenantId, documentId);

      if (documentOptional.isEmpty()) {
        throw new DocumentNotFoundException(documentId);
      }

      return documentOptional.get();
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document (" + documentId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public Document updateDocument(UUID tenantId, Document document)
      throws DocumentNotFoundException, ServiceUnavailableException {
    try {
      if (!documentRepository.existsByTenantIdAndId(tenantId, document.getId())) {
        throw new DocumentNotFoundException(document.getId());
      }

      return documentRepository.saveAndFlush(document);
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document ("
              + document.getId()
              + ") with the definition ID ("
              + document.getDefinitionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }
}
