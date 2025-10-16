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

package digital.inception.operations.controller;

import digital.inception.api.SecureApiController;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.TenantUtil;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DocumentTemplateCategoryNotFoundException;
import digital.inception.operations.exception.DocumentTemplateNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.exception.DuplicateDocumentTemplateCategoryException;
import digital.inception.operations.exception.DuplicateDocumentTemplateException;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.CreateDocumentTemplateRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.DocumentTemplate;
import digital.inception.operations.model.DocumentTemplateCategory;
import digital.inception.operations.model.DocumentTemplateSortBy;
import digital.inception.operations.model.DocumentTemplateSummaries;
import digital.inception.operations.model.DocumentTemplateSummary;
import digital.inception.operations.model.SearchDocumentsRequest;
import digital.inception.operations.model.UpdateDocumentNoteRequest;
import digital.inception.operations.model.UpdateDocumentRequest;
import digital.inception.operations.model.UpdateDocumentTemplateRequest;
import digital.inception.operations.service.DocumentService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The {@code DocumentApiControllerImpl} class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class DocumentApiControllerImpl extends SecureApiController
    implements DocumentApiController {

  /** The Document Service. */
  private final DocumentService documentService;

  /**
   * Constructs a new {@code DocumentApiControllerImpl}.
   *
   * @param applicationContext the Spring application context
   * @param documentService the Document Service
   */
  public DocumentApiControllerImpl(
      ApplicationContext applicationContext, DocumentService documentService) {
    super(applicationContext);

    this.documentService = documentService;
  }

  @Override
  public UUID createDocument(UUID tenantId, CreateDocumentRequest createDocumentRequest)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    Document document =
        documentService.createDocument(tenantId, createDocumentRequest, getAuthenticationName());

    return document.getId();
  }

  @Override
  public void createDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    documentService.createDocumentDefinition(documentDefinition);
  }

  @Override
  public void createDocumentDefinitionCategory(
      DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionCategoryException,
          ServiceUnavailableException {
    documentService.createDocumentDefinitionCategory(documentDefinitionCategory);
  }

  @Override
  public UUID createDocumentNote(UUID tenantId, CreateDocumentNoteRequest createDocumentNoteRequest)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    DocumentNote documentNote =
        documentService.createDocumentNote(
            tenantId, createDocumentNoteRequest, getAuthenticationName());

    return documentNote.getId();
  }

  @Override
  public void createDocumentTemplate(CreateDocumentTemplateRequest createDocumentTemplateRequest)
      throws InvalidArgumentException,
          DuplicateDocumentTemplateException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    documentService.createDocumentTemplate(createDocumentTemplateRequest, getAuthenticationName());
  }

  @Override
  public void createDocumentTemplateCategory(DocumentTemplateCategory documentTemplateCategory)
      throws InvalidArgumentException,
          DuplicateDocumentTemplateCategoryException,
          ServiceUnavailableException {
    documentService.createDocumentTemplateCategory(documentTemplateCategory);
  }

  @Override
  public void deleteDocument(UUID tenantId, UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    documentService.deleteDocument(tenantId, documentId);
  }

  @Override
  public void deleteDocumentDefinition(String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    documentService.deleteDocumentDefinition(documentDefinitionId);
  }

  @Override
  public void deleteDocumentDefinitionCategory(String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    documentService.deleteDocumentDefinitionCategory(documentDefinitionCategoryId);
  }

  @Override
  public void deleteDocumentNote(UUID tenantId, UUID documentId, UUID documentNoteId)
      throws InvalidArgumentException,
          DocumentNotFoundException,
          DocumentNoteNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    if (documentId == null) {
      throw new InvalidArgumentException("documentId");
    }

    try {
      if (!documentService.documentExists(tenantId, documentId)) {
        throw new DocumentNotFoundException(tenantId, documentId);
      }

      if (!documentService.documentNoteExists(tenantId, documentId, documentNoteId)) {
        throw new DocumentNoteNotFoundException(tenantId, documentId);
      }
    } catch (DocumentNotFoundException | DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document note ("
              + documentNoteId
              + ") for the document ("
              + documentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    documentService.deleteDocumentNote(tenantId, documentNoteId);
  }

  @Override
  public void deleteDocumentTemplate(String documentTemplateId)
      throws InvalidArgumentException,
          DocumentTemplateNotFoundException,
          ServiceUnavailableException {
    documentService.deleteDocumentTemplate(documentTemplateId);
  }

  @Override
  public void deleteDocumentTemplateCategory(String documentTemplateCategoryId)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    documentService.deleteDocumentTemplateCategory(documentTemplateCategoryId);
  }

  @Override
  public Document getDocument(UUID tenantId, UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.getDocument(tenantId, documentId);
  }

  @Override
  public DocumentDefinition getDocumentDefinition(String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    return documentService.getDocumentDefinition(documentDefinitionId);
  }

  @Override
  public List<DocumentDefinitionCategory> getDocumentDefinitionCategories(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.getDocumentDefinitionCategories(tenantId);
  }

  @Override
  public DocumentDefinitionCategory getDocumentDefinitionCategory(
      String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    return documentService.getDocumentDefinitionCategory(documentDefinitionCategoryId);
  }

  @Override
  public List<DocumentDefinition> getDocumentDefinitionsForDocumentDefinitionCategory(
      UUID tenantId, String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.getDocumentDefinitions(tenantId, documentDefinitionCategoryId);
  }

  @Override
  public DocumentNote getDocumentNote(UUID tenantId, UUID documentId, UUID documentNoteId)
      throws InvalidArgumentException,
          DocumentNotFoundException,
          DocumentNoteNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    try {
      if (!documentService.documentExists(tenantId, documentId)) {
        throw new DocumentNotFoundException(tenantId, documentId);
      }

      if (!documentService.documentNoteExists(tenantId, documentId, documentNoteId)) {
        throw new DocumentNoteNotFoundException(tenantId, documentNoteId);
      }
    } catch (DocumentNotFoundException | DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document note ("
              + documentNoteId
              + ") for the document ("
              + documentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }

    return documentService.getDocumentNote(tenantId, documentNoteId);
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
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.getDocumentNotes(
        tenantId, documentId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public DocumentTemplate getDocumentTemplate(String documentTemplateId)
      throws InvalidArgumentException,
          DocumentTemplateNotFoundException,
          ServiceUnavailableException {
    return documentService.getDocumentTemplate(documentTemplateId);
  }

  @Override
  public List<DocumentTemplateCategory> getDocumentTemplateCategories(UUID tenantId)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.getDocumentTemplateCategories(tenantId);
  }

  @Override
  public DocumentTemplateCategory getDocumentTemplateCategory(String documentTemplateCategoryId)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    return documentService.getDocumentTemplateCategory(documentTemplateCategoryId);
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
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.getDocumentTemplateSummaries(
        tenantId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public List<DocumentTemplateSummary> getDocumentTemplateSummaries(
      UUID tenantId, String documentTemplateCategoryId)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.getDocumentTemplateSummaries(tenantId, documentTemplateCategoryId);
  }

  @Override
  public DocumentSummaries searchDocuments(
      UUID tenantId, SearchDocumentsRequest searchDocumentsRequest)
      throws InvalidArgumentException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.searchDocuments(tenantId, searchDocumentsRequest);
  }

  @Override
  public void updateDocument(UUID tenantId, UpdateDocumentRequest updateDocumentRequest)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    Document document =
        documentService.updateDocument(tenantId, updateDocumentRequest, getAuthenticationName());
  }

  @Override
  public void updateDocumentDefinition(
      String documentDefinitionId, DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionId)) {
      throw new InvalidArgumentException("documentDefinitionId");
    }

    if (!Objects.equals(documentDefinitionId, documentDefinition.getId())) {
      throw new InvalidArgumentException("documentDefinition.id");
    }

    documentService.updateDocumentDefinition(documentDefinition);
  }

  @Override
  public void updateDocumentDefinitionCategory(
      String documentDefinitionCategoryId, DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentDefinitionCategoryId)) {
      throw new InvalidArgumentException("documentDefinitionCategoryId");
    }

    if (!Objects.equals(documentDefinitionCategoryId, documentDefinitionCategory.getId())) {
      throw new InvalidArgumentException("documentDefinitionCategory.id");
    }

    documentService.updateDocumentDefinitionCategory(documentDefinitionCategory);
  }

  @Override
  public void updateDocumentNote(UUID tenantId, UpdateDocumentNoteRequest updateDocumentNoteRequest)
      throws InvalidArgumentException, DocumentNoteNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    DocumentNote documentNote =
        documentService.updateDocumentNote(
            tenantId, updateDocumentNoteRequest, getAuthenticationName());
  }

  @Override
  public void updateDocumentTemplate(UpdateDocumentTemplateRequest updateDocumentTemplateRequest)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          DocumentTemplateNotFoundException,
          ServiceUnavailableException {
    documentService.updateDocumentTemplate(updateDocumentTemplateRequest, getAuthenticationName());
  }

  @Override
  public void updateDocumentTemplateCategory(
      String documentTemplateCategoryId, DocumentTemplateCategory documentTemplateCategory)
      throws InvalidArgumentException,
          DocumentTemplateCategoryNotFoundException,
          ServiceUnavailableException {
    if (!StringUtils.hasText(documentTemplateCategoryId)) {
      throw new InvalidArgumentException("documentTemplateCategoryId");
    }

    if (!Objects.equals(documentTemplateCategoryId, documentTemplateCategory.getId())) {
      throw new InvalidArgumentException("documentTemplateCategory.id");
    }

    documentService.updateDocumentTemplateCategory(documentTemplateCategory);
  }
}
