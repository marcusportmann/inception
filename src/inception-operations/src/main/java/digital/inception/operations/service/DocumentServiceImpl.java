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
import digital.inception.core.sorting.SortDirection;
import digital.inception.json.JsonClasspathResource;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DocumentTemplateCategoryNotFoundException;
import digital.inception.operations.exception.DocumentTemplateNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.exception.DuplicateDocumentException;
import digital.inception.operations.exception.DuplicateDocumentNoteException;
import digital.inception.operations.exception.DuplicateDocumentTemplateCategoryException;
import digital.inception.operations.exception.DuplicateDocumentTemplateException;
import digital.inception.operations.model.AttributeSearchCriteria;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.CreateDocumentTemplateRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.DocumentExternalReference;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSortBy;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.DocumentSummary;
import digital.inception.operations.model.DocumentTemplate;
import digital.inception.operations.model.DocumentTemplateCategory;
import digital.inception.operations.model.DocumentTemplateSortBy;
import digital.inception.operations.model.DocumentTemplateSummaries;
import digital.inception.operations.model.DocumentTemplateSummary;
import digital.inception.operations.model.ExternalReferenceSearchCriteria;
import digital.inception.operations.model.ObjectType;
import digital.inception.operations.model.SearchDocumentsRequest;
import digital.inception.operations.model.UpdateDocumentNoteRequest;
import digital.inception.operations.model.UpdateDocumentRequest;
import digital.inception.operations.model.UpdateDocumentTemplateRequest;
import digital.inception.operations.persistence.jpa.DocumentDefinitionCategoryRepository;
import digital.inception.operations.persistence.jpa.DocumentDefinitionRepository;
import digital.inception.operations.persistence.jpa.DocumentNoteRepository;
import digital.inception.operations.persistence.jpa.DocumentRepository;
import digital.inception.operations.persistence.jpa.DocumentSummaryRepository;
import digital.inception.operations.persistence.jpa.DocumentTemplateCategoryRepository;
import digital.inception.operations.persistence.jpa.DocumentTemplateRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Predicate;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

  /** The Document Note Repository. */
  private final DocumentNoteRepository documentNoteRepository;

  /** The Document Repository. */
  private final DocumentRepository documentRepository;

  /** The Document Summary Repository. */
  private final DocumentSummaryRepository documentSummaryRepository;

  /** The Document Template Category Repository. */
  private final DocumentTemplateCategoryRepository documentTemplateCategoryRepository;

  /** The Document Template Repository. */
  private final DocumentTemplateRepository documentTemplateRepository;

  /** The Validation Service. */
  private final ValidationService validationService;

  /** The internal reference to the Document Service to enable caching. */
  private DocumentService documentService;

  /** The maximum number of filtered document notes that will be returned by the service. */
  @Value("${inception.operations.max-filtered-document-notes:#{100}}")
  private int maxFilteredDocumentNotes;

  /** The maximum number of filtered document templates that will be returned by the service. */
  @Value("${inception.operations.max-filtered-document-templates:#{100}}")
  private int maxFilteredDocumentTemplates;

  /** The maximum number of filtered documents that will be returned by the service. */
  @Value("${inception.operations.max-filtered-documents:#{100}}")
  private int maxFilteredDocuments;

  /**
   * Constructs a new {@code DocumentServiceImpl}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param documentDefinitionCategoryRepository the Document Definition Category Repository
   * @param documentDefinitionRepository the Document Definition Repository
   * @param documentNoteRepository the Document Note Repository
   * @param documentRepository the Document Repository
   * @param documentTemplateRepository the Document Template Repository
   * @param documentSummaryRepository the Document Summary Repository
   * @param documentTemplateCategoryRepository the Document Template Category Repository
   * @param validationService the Validation Service
   */
  public DocumentServiceImpl(
      ApplicationContext applicationContext,
      DocumentDefinitionCategoryRepository documentDefinitionCategoryRepository,
      DocumentDefinitionRepository documentDefinitionRepository,
      DocumentNoteRepository documentNoteRepository,
      DocumentRepository documentRepository,
      DocumentSummaryRepository documentSummaryRepository,
      DocumentTemplateCategoryRepository documentTemplateCategoryRepository,
      DocumentTemplateRepository documentTemplateRepository,
      ValidationService validationService) {
    super(applicationContext);

    this.documentDefinitionCategoryRepository = documentDefinitionCategoryRepository;
    this.documentDefinitionRepository = documentDefinitionRepository;
    this.documentNoteRepository = documentNoteRepository;
    this.documentRepository = documentRepository;
    this.documentSummaryRepository = documentSummaryRepository;
    this.documentTemplateCategoryRepository = documentTemplateCategoryRepository;
    this.documentTemplateRepository = documentTemplateRepository;
    this.validationService = validationService;
  }

  @Override
  public String calculateDataHash(byte[] data) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      digest.update(data);

      return Base64.getEncoder().encodeToString(digest.digest());
    } catch (Throwable e) {
      throw new RuntimeException("Failed to calculate the hash for the data", e);
    }
  }

  @Override
  public Document createDocument(UUID tenantId, Document document)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          DuplicateDocumentException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("document", document);

    try {
      DocumentDefinition documentDefinition =
          getDocumentService().getDocumentDefinition(document.getDefinitionId());

      if (documentRepository.existsById(document.getId())) {
        throw new DuplicateDocumentException(document.getId());
      }

      return documentRepository.saveAndFlush(document);
    } catch (InvalidArgumentException
        | DocumentDefinitionNotFoundException
        | DuplicateDocumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document ("
              + document.getId()
              + ") with the document definition ID ("
              + document.getDefinitionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Document createDocument(
      UUID tenantId, CreateDocumentRequest createDocumentRequest, String createdBy)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("createDocumentRequest", createDocumentRequest);

    try {
      DocumentDefinition documentDefinition =
          getDocumentService().getDocumentDefinition(createDocumentRequest.getDefinitionId());

      Document document = new Document(createDocumentRequest.getDefinitionId());

      if (createDocumentRequest.getExternalReferences() != null) {
        // Validate the external references
        validationService.validateExternalReferences(
            tenantId,
            "createDocumentRequest.externalReferences",
            ObjectType.DOCUMENT,
            createDocumentRequest.getExternalReferences());

        document.setExternalReferences(createDocumentRequest.getExternalReferences());
      }

      if (createDocumentRequest.getAttributes() != null) {
        // Validate the allowed document attributes
        validationService.validateAllowedDocumentAttributes(
            "createDocumentRequest.attributes",
            documentDefinition,
            createDocumentRequest.getAttributes());

        // Validate the required document attributes
        validationService.validateRequiredDocumentAttributes(
            "createDocumentRequest.attributes",
            documentDefinition,
            createDocumentRequest.getAttributes());

        document.setAttributes(createDocumentRequest.getAttributes());
      }

      document.setCreated(OffsetDateTime.now());
      document.setCreatedBy(createdBy);
      document.setData(createDocumentRequest.getData());
      document.setFileType(createDocumentRequest.getFileType());
      document.setHash(calculateDataHash(createDocumentRequest.getData()));
      document.setName(createDocumentRequest.getName());
      document.setSourceDocumentId(createDocumentRequest.getSourceDocumentId());
      document.setTenantId(tenantId);

      if (documentRepository.existsById(document.getId())) {
        throw new DuplicateDocumentException(document.getId());
      }

      return documentRepository.saveAndFlush(document);
    } catch (InvalidArgumentException | DocumentDefinitionNotFoundException e) {
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
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    validateArgument("documentDefinition", documentDefinition);

    try {
      if (!documentDefinitionCategoryRepository.existsById(documentDefinition.getCategoryId())) {
        throw new DocumentDefinitionCategoryNotFoundException(documentDefinition.getCategoryId());
      }

      if (documentDefinitionRepository.existsById(documentDefinition.getId())) {
        throw new DuplicateDocumentDefinitionException(documentDefinition.getId());
      }

      documentDefinitionRepository.saveAndFlush(documentDefinition);
    } catch (DuplicateDocumentDefinitionException | DocumentDefinitionCategoryNotFoundException e) {
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
  public DocumentNote createDocumentNote(
      UUID tenantId, CreateDocumentNoteRequest createDocumentNoteRequest, String createdBy)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("createDocumentNoteRequest", createDocumentNoteRequest);

    if (!StringUtils.hasText(createdBy)) {
      throw new InvalidArgumentException("createdBy");
    }

    try {
      if (!documentExists(tenantId, createDocumentNoteRequest.getDocumentId())) {
        throw new DocumentNotFoundException(tenantId, createDocumentNoteRequest.getDocumentId());
      }

      DocumentNote documentNote =
          new DocumentNote(
              tenantId,
              createDocumentNoteRequest.getDocumentId(),
              createDocumentNoteRequest.getContent(),
              OffsetDateTime.now(),
              createdBy);

      if (documentNoteRepository.existsById(documentNote.getId())) {
        throw new DuplicateDocumentNoteException(documentNote.getId());
      }

      return documentNoteRepository.saveAndFlush(documentNote);
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document note for the document ("
              + createDocumentNoteRequest.getDocumentId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void createDocumentTemplate(
      CreateDocumentTemplateRequest createDocumentTemplateRequest, String createdBy)
      throws InvalidArgumentException,
          DuplicateDocumentTemplateException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    validateArgument("createDocumentTemplateRequest", createDocumentTemplateRequest);

    try {
      if (!documentTemplateCategoryRepository.existsById(
          createDocumentTemplateRequest.getCategoryId())) {
        throw new DocumentTemplateCategoryNotFoundException(
            createDocumentTemplateRequest.getCategoryId());
      }

      if (documentTemplateRepository.existsById(createDocumentTemplateRequest.getId())) {
        throw new DuplicateDocumentTemplateException(createDocumentTemplateRequest.getId());
      }

      DocumentTemplate documentTemplate = new DocumentTemplate();
      documentTemplate.setCategoryId(createDocumentTemplateRequest.getCategoryId());
      documentTemplate.setCreated(OffsetDateTime.now());
      documentTemplate.setCreatedBy(createdBy);
      documentTemplate.setData(createDocumentTemplateRequest.getData());
      documentTemplate.setDescription(createDocumentTemplateRequest.getDescription());
      documentTemplate.setHash(calculateDataHash(createDocumentTemplateRequest.getData()));
      documentTemplate.setId(createDocumentTemplateRequest.getId());
      documentTemplate.setName(createDocumentTemplateRequest.getName());
      documentTemplate.setTenantId(createDocumentTemplateRequest.getTenantId());

      documentTemplateRepository.saveAndFlush(documentTemplate);
    } catch (DuplicateDocumentTemplateException | DocumentTemplateCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document template (" + createDocumentTemplateRequest.getId() + ")",
          e);
    }
  }

  @Override
  public void createDocumentTemplateCategory(DocumentTemplateCategory documentTemplateCategory)
      throws InvalidArgumentException,
          DuplicateDocumentTemplateCategoryException,
          ServiceUnavailableException {
    validateArgument("documentTemplateCategory", documentTemplateCategory);

    try {
      if (documentTemplateCategoryRepository.existsById(documentTemplateCategory.getId())) {
        throw new DuplicateDocumentTemplateCategoryException(documentTemplateCategory.getId());
      }

      documentTemplateCategoryRepository.saveAndFlush(documentTemplateCategory);
    } catch (DuplicateDocumentTemplateCategoryException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document template category ("
              + documentTemplateCategory.getId()
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

    try {
      if (!documentRepository.existsByTenantIdAndId(tenantId, documentId)) {
        throw new DocumentNotFoundException(tenantId, documentId);
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
  public void deleteDocumentNote(UUID tenantId, UUID documentNoteId)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (documentNoteId == null) {
      throw new InvalidArgumentException("documentNoteId");
    }

    try {
      if (!documentNoteRepository.existsByTenantIdAndId(tenantId, documentNoteId)) {
        throw new DocumentNoteNotFoundException(tenantId, documentNoteId);
      }

      documentNoteRepository.deleteById(documentNoteId);
    } catch (DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document note ("
              + documentNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteDocumentTemplate(String documentTemplateId)
      throws InvalidArgumentException,
          DocumentTemplateNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentTemplateId)) {
      throw new InvalidArgumentException("documentTemplateId");
    }

    try {
      if (!documentTemplateRepository.existsById(documentTemplateId)) {
        throw new DocumentTemplateNotFoundException(documentTemplateId);
      }

      documentTemplateRepository.deleteById(documentTemplateId);
    } catch (DocumentTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document template (" + documentTemplateId + ")", e);
    }
  }

  @Override
  public void deleteDocumentTemplateCategory(String documentTemplateCategoryId)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentTemplateCategoryId)) {
      throw new InvalidArgumentException("documentTemplateCategoryId");
    }

    try {
      if (!documentTemplateCategoryRepository.existsById(documentTemplateCategoryId)) {
        throw new DocumentTemplateCategoryNotFoundException(documentTemplateCategoryId);
      }

      documentTemplateCategoryRepository.deleteById(documentTemplateCategoryId);
    } catch (DocumentTemplateCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document template category (" + documentTemplateCategoryId + ")",
          e);
    }
  }

  @Override
  public boolean documentDefinitionCategoryExists(String documentDefinitionCategoryId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionCategoryId)) {
      throw new InvalidArgumentException("documentDefinitionCategoryId");
    }

    try {
      return documentDefinitionCategoryRepository.existsById(documentDefinitionCategoryId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the document definition category ("
              + documentDefinitionCategoryId
              + ") exists",
          e);
    }
  }

  @Override
  public boolean documentDefinitionExists(
      String documentDefinitionCategoryId, String documentDefinitionId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionCategoryId)) {
      throw new InvalidArgumentException("documentDefinitionCategoryId");
    }

    if (!StringUtils.hasText(documentDefinitionId)) {
      throw new InvalidArgumentException("documentDefinitionId");
    }

    try {
      return documentDefinitionRepository.existsByCategoryIdAndId(
          documentDefinitionCategoryId, documentDefinitionId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the document definition ("
              + documentDefinitionId
              + ") exists and is associated with the document definition category ("
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
  public boolean documentExists(UUID tenantId, UUID documentId) throws ServiceUnavailableException {
    try {
      return documentRepository.existsByTenantIdAndId(tenantId, documentId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the document ("
              + documentId
              + ") exists for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean documentNoteExists(UUID tenantId, UUID documentId, UUID documentNoteId)
      throws ServiceUnavailableException {
    try {
      return documentNoteRepository.existsByTenantIdAndDocumentIdAndId(
          tenantId, documentId, documentNoteId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the document note ("
              + documentNoteId
              + ") exists for the document ("
              + documentId
              + ") and tenant ("
              + tenantId
              + ")",
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

    try {
      /*
       * NOTE: The search by both tenant ID and document ID includes a security check to ensure
       * that the document not only exists, but is also associated with the specified tenant.
       */
      Optional<Document> documentOptional =
          documentRepository.findByTenantIdAndId(tenantId, documentId);

      if (documentOptional.isEmpty()) {
        throw new DocumentNotFoundException(tenantId, documentId);
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
  public List<DocumentDefinitionCategory> getDocumentDefinitionCategories(UUID tenantId)
      throws ServiceUnavailableException {
    try {
      return documentDefinitionCategoryRepository.findForTenantOrGlobal(tenantId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document definition categories for the tenant (" + tenantId + ")",
          e);
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
  public List<DocumentDefinition> getDocumentDefinitions(
      UUID tenantId, String documentDefinitionCategoryId)
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

      return documentDefinitionRepository.findForCategoryAndTenantOrGlobal(
          documentDefinitionCategoryId, tenantId);
    } catch (DocumentDefinitionCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document definitions associated with the document definition category ("
              + documentDefinitionCategoryId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public DocumentNote getDocumentNote(UUID tenantId, UUID documentNoteId)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (documentNoteId == null) {
      throw new InvalidArgumentException("documentNoteId");
    }

    try {
      /*
       * NOTE: The search by both tenant ID and document note ID includes a security check to ensure
       * that the document note not only exists, but is also associated with the specified tenant.
       */
      Optional<DocumentNote> documentNoteOptional =
          documentNoteRepository.findByTenantIdAndId(tenantId, documentNoteId);

      if (documentNoteOptional.isEmpty()) {
        throw new DocumentNoteNotFoundException(tenantId, documentNoteId);
      }

      return documentNoteOptional.get();
    } catch (DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document note ("
              + documentNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public DocumentNotes getDocumentNotes(
      UUID tenantId,
      UUID documentId,
      String filter,
      DocumentNoteSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if (documentId == null) {
      throw new InvalidArgumentException("documentId");
    }

    try {
      if (!documentRepository.existsByTenantIdAndId(tenantId, documentId)) {
        throw new DocumentNotFoundException(tenantId, documentId);
      }

      // Paging normalisation
      pageIndex = (pageIndex == null) ? 0 : Math.max(0, pageIndex);
      pageSize =
          (pageSize == null) ? 50 : Math.max(1, Math.min(pageSize, maxFilteredDocumentNotes));

      // Sorting normalisation
      String sortProperty = DocumentNoteSortBy.resolveSortByPropertyName(sortBy);
      Sort.Direction direction =
          (sortDirection == SortDirection.DESCENDING) ? Sort.Direction.DESC : Sort.Direction.ASC;

      PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, direction, sortProperty);

      // Specification
      Specification<DocumentNote> spec =
          (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
            predicates.add(criteriaBuilder.equal(root.get("documentId"), documentId));

            if (StringUtils.hasText(filter)) {
              String f = filter.trim().toLowerCase();
              predicates.add(
                  criteriaBuilder.or(
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("createdBy")), "%" + f + "%"),
                      criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("updatedBy")), "%" + f + "%")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
          };

      Page<DocumentNote> documentNotePage = documentNoteRepository.findAll(spec, pageRequest);

      return new DocumentNotes(
          documentNotePage.getContent(), // getContent() avoids extra copying
          documentNotePage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered document notes for the document ("
              + documentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public DocumentTemplate getDocumentTemplate(String documentTemplateId)
      throws InvalidArgumentException,
          DocumentTemplateNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentTemplateId)) {
      throw new InvalidArgumentException("documentTemplateId");
    }

    try {
      Optional<DocumentTemplate> documentTemplateOptional =
          documentTemplateRepository.findById(documentTemplateId);

      if (documentTemplateOptional.isEmpty()) {
        throw new DocumentTemplateNotFoundException(documentTemplateId);
      }

      return documentTemplateOptional.get();
    } catch (DocumentTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document template (" + documentTemplateId + ")", e);
    }
  }

  @Override
  public List<DocumentTemplateCategory> getDocumentTemplateCategories(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    try {
      return documentTemplateCategoryRepository.findForTenantOrGlobal(tenantId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document template categories for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public DocumentTemplateCategory getDocumentTemplateCategory(String documentTemplateCategoryId)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentTemplateCategoryId)) {
      throw new InvalidArgumentException("documentTemplateCategoryId");
    }

    try {
      Optional<DocumentTemplateCategory> documentTemplateCategoryOptional =
          documentTemplateCategoryRepository.findById(documentTemplateCategoryId);

      if (documentTemplateCategoryOptional.isEmpty()) {
        throw new DocumentTemplateCategoryNotFoundException(documentTemplateCategoryId);
      }

      return documentTemplateCategoryOptional.get();
    } catch (DocumentTemplateCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document template category (" + documentTemplateCategoryId + ")",
          e);
    }
  }

  @Override
  public List<DocumentTemplateSummary> getDocumentTemplateSummaries(
      UUID tenantId, String documentTemplateCategoryId)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentTemplateCategoryId)) {
      throw new InvalidArgumentException("documentTemplateCategoryId");
    }

    try {
      if (!documentTemplateCategoryRepository.existsById(documentTemplateCategoryId)) {
        throw new DocumentTemplateCategoryNotFoundException(documentTemplateCategoryId);
      }

      return documentTemplateRepository.findDocumentTemplateSummariesForCategoryAndTenantOrGlobal(
          documentTemplateCategoryId, tenantId);
    } catch (DocumentTemplateCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the document templates associated with the document template category ("
              + documentTemplateCategoryId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public DocumentTemplateSummaries getDocumentTemplateSummaries(
      UUID tenantId,
      String filter,
      DocumentTemplateSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    if (sortBy == null) {
      sortBy = DocumentTemplateSortBy.ID;
    }

    if (sortDirection == null) {
      sortDirection = SortDirection.DESCENDING;
    }

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = maxFilteredDocumentTemplates;
    }

    String filterLike = (filter == null) ? null : "%" + filter.toLowerCase() + "%";

    PageRequest pageRequest;

    if (sortBy == DocumentTemplateSortBy.NAME) {
      pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, maxFilteredDocumentTemplates),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              "name");
    } else {
      pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, maxFilteredDocumentTemplates),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              "id");
    }

    try {
      Page<DocumentTemplateSummary> documentTemplateSummaryPage =
          documentTemplateRepository.findDocumentTemplateSummaries(
              tenantId, filterLike, pageRequest);

      return new DocumentTemplateSummaries(
          documentTemplateSummaryPage.toList(),
          documentTemplateSummaryPage.getTotalElements(),
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the document templates for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  /** Initialize the Document Service. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Document Service");

    // Load the document definitions from the classpath
    try {
      log.info("Loading the document definitions from the classpath");

      List<JsonClasspathResource<DocumentDefinition>> jsonClasspathResources =
          JsonClasspathResource.loadFromClasspath(
              "document-definitions", getObjectMapper(), DocumentDefinition.class);

      for (JsonClasspathResource<DocumentDefinition> jsonClasspathResource :
          jsonClasspathResources) {
        DocumentDefinition documentDefinition = jsonClasspathResource.value();

        if (documentDefinitionExists(documentDefinition.getId())) {
          updateDocumentDefinition(documentDefinition);

          log.info("Updated the document definition (" + documentDefinition.getId() + ")");
        } else {
          createDocumentDefinition(documentDefinition);

          log.info("Created the document definition (" + documentDefinition.getId() + ")");
        }
      }
    } catch (Throwable e) {
      throw new BeanInitializationException(
          "Failed to load the document definitions from the classpath", e);
    }
  }

  @Override
  public DocumentSummaries searchDocuments(
      UUID tenantId, SearchDocumentsRequest searchDocumentsRequest)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("searchDocumentsRequest", searchDocumentsRequest);

    try {
      // Build Specification
      Specification<DocumentSummary> specification =
          (root, query, criteriaBuilder) -> {
            // Avoid duplicates when joins or subqueries are involved
            if (query != null) {
              query.distinct(true);
            }

            // AND'ed top-level predicates
            List<Predicate> andPredicates = new ArrayList<>();

            // Tenant filter
            andPredicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

            // Top-level filters
            if (StringUtils.hasText(searchDocumentsRequest.getDefinitionId())) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      criteriaBuilder.lower(root.get("definitionId")),
                      searchDocumentsRequest.getDefinitionId().toLowerCase()));
            }

            // Attribute criteria (OR all attribute pairs)
            if (searchDocumentsRequest.getAttributes() != null
                && !searchDocumentsRequest.getAttributes().isEmpty()) {
              List<Predicate> attributePredicates = new ArrayList<>();

              for (AttributeSearchCriteria attributeSearchCriteria :
                  searchDocumentsRequest.getAttributes()) {
                if (attributeSearchCriteria == null) continue;

                var subQuery = query.subquery(Integer.class);
                var documentAttributeRoot = subQuery.from(DocumentAttribute.class);
                Predicate subPredicate =
                    criteriaBuilder.equal(documentAttributeRoot.get("documentId"), root.get("id"));

                if (StringUtils.hasText(attributeSearchCriteria.getName())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(documentAttributeRoot.get("name")),
                              attributeSearchCriteria.getName().toLowerCase()));
                }
                if (StringUtils.hasText(attributeSearchCriteria.getValue())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(documentAttributeRoot.get("value")),
                              attributeSearchCriteria.getValue().toLowerCase()));
                }

                subQuery.select(criteriaBuilder.literal(1)).where(subPredicate);
                attributePredicates.add(criteriaBuilder.exists(subQuery));
              }

              if (!attributePredicates.isEmpty()) {
                andPredicates.add(
                    criteriaBuilder.or(attributePredicates.toArray(new Predicate[0])));
              }
            }

            // External reference criteria (OR all external reference pairs)
            if (searchDocumentsRequest.getExternalReferences() != null
                && !searchDocumentsRequest.getExternalReferences().isEmpty()) {
              List<Predicate> externalReferencePredicates = new ArrayList<>();

              for (ExternalReferenceSearchCriteria externalReferenceSearchCriteria :
                  searchDocumentsRequest.getExternalReferences()) {
                if (externalReferenceSearchCriteria == null) continue;

                var subQuery = query.subquery(Integer.class);
                var documentExternalReferenceRoot = subQuery.from(DocumentExternalReference.class);
                Predicate subPredicate =
                    criteriaBuilder.equal(
                        documentExternalReferenceRoot.get("objectId"), root.get("id"));

                if (StringUtils.hasText(externalReferenceSearchCriteria.getType())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(documentExternalReferenceRoot.get("type")),
                              externalReferenceSearchCriteria.getType().toLowerCase()));
                }
                if (StringUtils.hasText(externalReferenceSearchCriteria.getValue())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(documentExternalReferenceRoot.get("value")),
                              externalReferenceSearchCriteria.getValue().toLowerCase()));
                }

                subQuery.select(criteriaBuilder.literal(1)).where(subPredicate);
                externalReferencePredicates.add(criteriaBuilder.exists(subQuery));
              }

              if (!externalReferencePredicates.isEmpty()) {
                andPredicates.add(
                    criteriaBuilder.or(externalReferencePredicates.toArray(new Predicate[0])));
              }
            }

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
          };

      // Sorting
      String sortByPropertyName =
          DocumentSortBy.resolveSortByPropertyName(searchDocumentsRequest.getSortBy());
      Sort.Direction dir = resolveSortDirection(searchDocumentsRequest.getSortDirection());
      Sort sort = Sort.by(dir, sortByPropertyName);

      // Paging
      int pageIndex =
          searchDocumentsRequest.getPageIndex() == null
              ? 0
              : Math.max(0, searchDocumentsRequest.getPageIndex());
      int pageSize =
          searchDocumentsRequest.getPageSize() == null
              ? 50
              : Math.max(1, Math.min(searchDocumentsRequest.getPageSize(), maxFilteredDocuments));
      Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

      Page<DocumentSummary> documentSummaryPage =
          documentSummaryRepository.findAll(specification, pageable);

      return new DocumentSummaries(
          documentSummaryPage.toList(),
          documentSummaryPage.getTotalElements(),
          searchDocumentsRequest.getSortBy(),
          searchDocumentsRequest.getSortDirection(),
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to search for documents for the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  public Document updateDocument(
      UUID tenantId, UpdateDocumentRequest updateDocumentRequest, String updatedBy)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("updateDocumentRequest", updateDocumentRequest);

    try {
      Optional<Document> documentOptional =
          documentRepository.findByTenantIdAndId(tenantId, updateDocumentRequest.getDocumentId());

      if (documentOptional.isEmpty()) {
        throw new DocumentNotFoundException(tenantId, updateDocumentRequest.getDocumentId());
      }

      Document document = documentOptional.get();

      if (updateDocumentRequest.getExternalReferences() != null) {
        // Validate the external references
        validationService.validateExternalReferences(
            tenantId,
            "updateDocumentRequest.externalReferences",
            ObjectType.DOCUMENT,
            updateDocumentRequest.getExternalReferences());

        document.setExternalReferences(updateDocumentRequest.getExternalReferences());
      }

      if (updateDocumentRequest.getAttributes() != null) {
        DocumentDefinition documentDefinition =
            getDocumentService().getDocumentDefinition(document.getDefinitionId());

        // Validate the allowed document attributes
        validationService.validateAllowedDocumentAttributes(
            "updateDocumentRequest.attributes",
            documentDefinition,
            updateDocumentRequest.getAttributes());

        // Validate the required document attributes
        validationService.validateRequiredDocumentAttributes(
            "updateDocumentRequest.attributes",
            documentDefinition,
            updateDocumentRequest.getAttributes());

        document.setAttributes(updateDocumentRequest.getAttributes());
      }

      document.setData(updateDocumentRequest.getData());
      document.setFileType(updateDocumentRequest.getFileType());
      document.setName(updateDocumentRequest.getName());
      document.setSourceDocumentId(updateDocumentRequest.getSourceDocumentId());
      document.setUpdated(OffsetDateTime.now());
      document.setUpdatedBy(updatedBy);

      return documentRepository.saveAndFlush(document);
    } catch (InvalidArgumentException | DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document ("
              + updateDocumentRequest.getDocumentId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void updateDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    validateArgument("documentDefinition", documentDefinition);

    try {
      if (!documentDefinitionCategoryRepository.existsById(documentDefinition.getCategoryId())) {
        throw new DocumentDefinitionCategoryNotFoundException(documentDefinition.getCategoryId());
      }

      if (!documentDefinitionRepository.existsById(documentDefinition.getId())) {
        throw new DocumentDefinitionNotFoundException(documentDefinition.getId());
      }

      documentDefinitionRepository.saveAndFlush(documentDefinition);
    } catch (DocumentDefinitionCategoryNotFoundException | DocumentDefinitionNotFoundException e) {
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

  @Override
  public DocumentNote updateDocumentNote(
      UUID tenantId, UpdateDocumentNoteRequest updateDocumentNoteRequest, String updatedBy)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException {
    if (tenantId == null) {
      throw new InvalidArgumentException("tenantId");
    }

    validateArgument("updateDocumentNoteRequest", updateDocumentNoteRequest);

    try {
      Optional<DocumentNote> documentNoteOptional =
          documentNoteRepository.findByTenantIdAndId(
              tenantId, updateDocumentNoteRequest.getDocumentNoteId());

      if (documentNoteOptional.isEmpty()) {
        throw new DocumentNoteNotFoundException(
            tenantId, updateDocumentNoteRequest.getDocumentNoteId());
      }

      DocumentNote documentNote = documentNoteOptional.get();

      documentNote.setContent(updateDocumentNoteRequest.getContent());
      documentNote.setUpdated(OffsetDateTime.now());
      documentNote.setUpdatedBy(updatedBy);

      return documentNoteRepository.saveAndFlush(documentNote);
    } catch (DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document note ("
              + updateDocumentNoteRequest.getDocumentNoteId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void updateDocumentTemplate(
      UpdateDocumentTemplateRequest updateDocumentTemplateRequest, String updatedBy)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          DocumentTemplateNotFoundException,
          ServiceUnavailableException {
    validateArgument("updateDocumentTemplateRequest", updateDocumentTemplateRequest);

    try {
      if (!documentTemplateCategoryRepository.existsById(
          updateDocumentTemplateRequest.getCategoryId())) {
        throw new DocumentTemplateCategoryNotFoundException(
            updateDocumentTemplateRequest.getCategoryId());
      }

      Optional<DocumentTemplate> documentTemplateOptional =
          documentTemplateRepository.findById(updateDocumentTemplateRequest.getId());

      if (documentTemplateOptional.isEmpty()) {
        throw new DocumentTemplateNotFoundException(updateDocumentTemplateRequest.getId());
      }

      DocumentTemplate documentTemplate = documentTemplateOptional.get();

      documentTemplate.setCategoryId(updateDocumentTemplateRequest.getCategoryId());
      documentTemplate.setData(updateDocumentTemplateRequest.getData());
      documentTemplate.setDescription(updateDocumentTemplateRequest.getDescription());
      documentTemplate.setHash(calculateDataHash(updateDocumentTemplateRequest.getData()));
      documentTemplate.setId(updateDocumentTemplateRequest.getId());
      documentTemplate.setName(updateDocumentTemplateRequest.getName());
      documentTemplate.setTenantId(updateDocumentTemplateRequest.getTenantId());
      documentTemplate.setUpdated(OffsetDateTime.now());
      documentTemplate.setUpdatedBy(updatedBy);

      documentTemplateRepository.saveAndFlush(documentTemplate);
    } catch (DocumentTemplateCategoryNotFoundException | DocumentTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document template (" + updateDocumentTemplateRequest.getId() + ")",
          e);
    }
  }

  @Override
  public void updateDocumentTemplateCategory(DocumentTemplateCategory documentTemplateCategory)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    validateArgument("documentTemplateCategory", documentTemplateCategory);

    try {
      if (!documentTemplateCategoryRepository.existsById(documentTemplateCategory.getId())) {
        throw new DocumentTemplateCategoryNotFoundException(documentTemplateCategory.getId());
      }

      documentTemplateCategoryRepository.saveAndFlush(documentTemplateCategory);
    } catch (DocumentTemplateCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document template category ("
              + documentTemplateCategory.getId()
              + ")",
          e);
    }
  }

  /**
   * Returns the internal reference to the Document Service to enable caching.
   *
   * @return the internal reference to the Document Service to enable caching.
   */
  private DocumentService getDocumentService() {
    if (documentService == null) {
      documentService = getApplicationContext().getBean(DocumentService.class);
    }

    return documentService;
  }

  private Sort.Direction resolveSortDirection(SortDirection sortDirection) {
    if (sortDirection == null) {
      return Sort.Direction.DESC;
    } else if (sortDirection == SortDirection.ASCENDING) {
      return Sort.Direction.ASC;
    } else {
      return Sort.Direction.DESC;
    }
  }
}
