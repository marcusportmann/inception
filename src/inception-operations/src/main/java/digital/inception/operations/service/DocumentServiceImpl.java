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
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.UpdateDocumentRequest;
import digital.inception.operations.persistence.jpa.DocumentDefinitionCategoryRepository;
import digital.inception.operations.persistence.jpa.DocumentDefinitionRepository;
import digital.inception.operations.store.DocumentStore;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

// TODO: Add methods to link and unlink documents from a workflow, i.e. manage workflow documents
// (WorkflowDocument).

/**
 * The {@code DocumentServiceImpl} class provides the Document Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class DocumentServiceImpl extends AbstractServiceBase implements DocumentService {

  /** The Document Definition Category Repository. */
  private final DocumentDefinitionCategoryRepository documentDefinitionCategoryRepository;

  /** The Document Definition Repository. */
  private final DocumentDefinitionRepository documentDefinitionRepository;

  /** The Document Store. */
  private final DocumentStore documentStore;

  /**
   * Constructs a new {@code DocumentServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param documentStore the Document Store
   * @param documentDefinitionCategoryRepository the Document Definition Category Repository
   * @param documentDefinitionRepository the Document Definition Repository
   */
  public DocumentServiceImpl(
      ApplicationContext applicationContext,
      DocumentStore documentStore,
      DocumentDefinitionCategoryRepository documentDefinitionCategoryRepository,
      DocumentDefinitionRepository documentDefinitionRepository) {
    super(applicationContext);

    this.documentStore = documentStore;
    this.documentDefinitionCategoryRepository = documentDefinitionCategoryRepository;
    this.documentDefinitionRepository = documentDefinitionRepository;
  }

  @Override
  public Document createDocument(UUID tenantId, CreateDocumentRequest createDocumentRequest)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("createDocumentRequest", createDocumentRequest);

    try {
      if (documentDefinitionRepository.existsById(createDocumentRequest.getDefinitionId())) {
        throw new DocumentDefinitionNotFoundException(createDocumentRequest.getDefinitionId());
      }

      Document document = new Document(createDocumentRequest.getDefinitionId());
      document.setData(createDocumentRequest.getData());
      document.setExpiryDate(createDocumentRequest.getExpiryDate());
      document.setExternalReference(createDocumentRequest.getExternalReference());
      document.setFileType(createDocumentRequest.getFileType());
      document.setHash(calculateDocumentHash(createDocumentRequest.getData()));
      document.setIssueDate(createDocumentRequest.getIssueDate());
      document.setName(createDocumentRequest.getName());
      document.setSourceDocumentId(createDocumentRequest.getSourceDocumentId());
      document.setTenantId(tenantId);

      return documentStore.createDocument(tenantId, document);
    } catch (DocumentDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document with the document definition ID ("
              + createDocumentRequest.getDefinitionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void createDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionException,
          ServiceUnavailableException {
    validateArgument("documentDefinition", documentDefinition);

    try {
      if (documentDefinitionRepository.existsById(documentDefinition.getId())) {
        throw new DuplicateDocumentDefinitionException(documentDefinition.getId());
      }

      documentDefinitionRepository.saveAndFlush(documentDefinition);
    } catch (DuplicateDocumentDefinitionException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document definition (" + documentDefinition.getId() + ")", e);
    }
  }

  @Override
  public void createDocumentDefinitionCategory(
      DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionCategoryException,
          ServiceUnavailableException {
    validateArgument("documentDefinitionCategory", documentDefinitionCategory);

    try {
      if (documentDefinitionCategoryRepository.existsById(documentDefinitionCategory.getId())) {
        throw new DuplicateDocumentDefinitionCategoryException(documentDefinitionCategory.getId());
      }

      documentDefinitionCategoryRepository.saveAndFlush(documentDefinitionCategory);
    } catch (DuplicateDocumentDefinitionCategoryException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document definition category ("
              + documentDefinitionCategory.getId()
              + ")",
          e);
    }
  }

  @Override
  public void deleteDocument(UUID tenantId, UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (documentId == null) {
      throw new InvalidArgumentException("documentId");
    }

    documentStore.deleteDocument(tenantId, documentId);
  }

  @Override
  public void deleteDocumentDefinition(String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionId)) {
      throw new InvalidArgumentException("documentDefinitionId");
    }

    try {
      if (!documentDefinitionRepository.existsById(documentDefinitionId)) {
        throw new DocumentDefinitionNotFoundException(documentDefinitionId);
      }

      documentDefinitionRepository.deleteById(documentDefinitionId);
    } catch (DocumentDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document definition (" + documentDefinitionId + ")", e);
    }
  }

  @Override
  public void deleteDocumentDefinitionCategory(String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionCategoryId)) {
      throw new InvalidArgumentException("documentDefinitionCategoryId");
    }

    try {
      if (!documentDefinitionCategoryRepository.existsById(documentDefinitionCategoryId)) {
        throw new DocumentDefinitionCategoryNotFoundException(documentDefinitionCategoryId);
      }

      documentDefinitionCategoryRepository.deleteById(documentDefinitionCategoryId);
    } catch (DocumentDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document definition category ("
              + documentDefinitionCategoryId
              + ")",
          e);
    }
  }

  @Override
  public boolean documentDefinitionExists(String documentDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionId)) {
      throw new InvalidArgumentException("documentDefinitionId");
    }

    try {
      return documentDefinitionRepository.existsById(documentDefinitionId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the document definition (" + documentDefinitionId + ") exists",
          e);
    }
  }

  @Override
  public Document getDocument(UUID tenantId, UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (documentId == null) {
      throw new InvalidArgumentException("documentId");
    }

    return documentStore.getDocument(tenantId, documentId);
  }

  @Override
  public DocumentDefinition getDocumentDefinition(String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionId)) {
      throw new InvalidArgumentException("documentDefinitionId");
    }

    try {
      Optional<DocumentDefinition> documentDefinitionOptional =
          documentDefinitionRepository.findById(documentDefinitionId);

      if (documentDefinitionOptional.isEmpty()) {
        throw new DocumentDefinitionNotFoundException(documentDefinitionId);
      }

      return documentDefinitionOptional.get();
    } catch (DocumentDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document definition (" + documentDefinitionId + ")", e);
    }
  }

  @Override
  public DocumentDefinitionCategory getDocumentDefinitionCategory(
      String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionCategoryId)) {
      throw new InvalidArgumentException("documentDefinitionCategoryId");
    }

    try {
      Optional<DocumentDefinitionCategory> documentDefinitionCategoryOptional =
          documentDefinitionCategoryRepository.findById(documentDefinitionCategoryId);

      if (documentDefinitionCategoryOptional.isEmpty()) {
        throw new DocumentDefinitionCategoryNotFoundException(documentDefinitionCategoryId);
      }

      return documentDefinitionCategoryOptional.get();
    } catch (DocumentDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document definition category ("
              + documentDefinitionCategoryId
              + ")",
          e);
    }
  }

  @Override
  public Document updateDocument(UUID tenantId, UpdateDocumentRequest updateDocumentRequest)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("updateDocumentRequest", updateDocumentRequest);

    try {
      Document document = documentStore.getDocument(tenantId, updateDocumentRequest.getId());

      document.setData(updateDocumentRequest.getData());
      document.setExpiryDate(updateDocumentRequest.getExpiryDate());
      document.setExternalReference(updateDocumentRequest.getExternalReference());
      document.setFileType(updateDocumentRequest.getFileType());
      document.setIssueDate(updateDocumentRequest.getIssueDate());
      document.setName(updateDocumentRequest.getName());
      document.setSourceDocumentId(updateDocumentRequest.getSourceDocumentId());

      return documentStore.updateDocument(tenantId, document);
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document ("
              + updateDocumentRequest.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void updateDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    validateArgument("documentDefinition", documentDefinition);

    try {
      if (!documentDefinitionRepository.existsById(documentDefinition.getId())) {
        throw new DocumentDefinitionNotFoundException(documentDefinition.getId());
      }

      documentDefinitionRepository.saveAndFlush(documentDefinition);
    } catch (DocumentDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document definition (" + documentDefinition.getId() + ")", e);
    }
  }

  @Override
  public void updateDocumentDefinitionCategory(
      DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    validateArgument("documentDefinitionCategory", documentDefinitionCategory);

    try {
      if (!documentDefinitionCategoryRepository.existsById(documentDefinitionCategory.getId())) {
        throw new DocumentDefinitionCategoryNotFoundException(documentDefinitionCategory.getId());
      }

      documentDefinitionCategoryRepository.saveAndFlush(documentDefinitionCategory);
    } catch (DocumentDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document definition category ("
              + documentDefinitionCategory.getId()
              + ")",
          e);
    }
  }

  private String calculateDocumentHash(byte[] documentData) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      digest.update(documentData);

      return Base64.getEncoder().encodeToString(digest.digest());
    } catch (Throwable e) {
      throw new RuntimeException("Failed to calculate the SHA-256 hash for the document data", e);
    }
  }
}
