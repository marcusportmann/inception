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
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.DocumentDefinitionSummary;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSortBy;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.UpdateDocumentNoteRequest;
import digital.inception.operations.model.UpdateDocumentRequest;
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
  public void createDocumentDefinition(
      UUID tenantId, String documentDefinitionCategoryId, DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getDocumentDefinitions() method where
     *       we want to retrieve the "global" and "tenant-specific" document definitions.
     *       The ability to create or update document definitions is an administrative function and
     *       is not assigned to a user for a particular tenant.
     */

    if (!StringUtils.hasText(documentDefinitionCategoryId)) {
      throw new InvalidArgumentException("documentDefinitionCategoryId");
    }

    if (!Objects.equals(documentDefinitionCategoryId, documentDefinition.getCategoryId())) {
      throw new InvalidArgumentException("documentDefinition.categoryId");
    }

    documentService.createDocumentDefinition(documentDefinition);
  }

  @Override
  public void createDocumentDefinitionCategory(
      UUID tenantId, DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException,
          DuplicateDocumentDefinitionCategoryException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getDocumentDefinitionCategories() method where
     *       we want to retrieve the "global" and "tenant-specific" document definition categories.
     *       The ability to create or update document definition categories is an administrative
     *       function and is not assigned to a user for a particular tenant.
     */

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
  public void deleteDocumentDefinition(
      UUID tenantId, String documentDefinitionCategoryId, String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getDocumentDefinitions() method where
     *       we want to retrieve the "global" and "tenant-specific" document definitions.
     *       The ability to create or update document definitions is an administrative function and
     *       is not assigned to a user for a particular tenant.
     */

    if (documentDefinitionCategoryId == null) {
      throw new InvalidArgumentException("documentDefinitionCategoryId");
    }

    try {
      if (!documentService.documentDefinitionCategoryExists(documentDefinitionCategoryId)) {
        throw new DocumentDefinitionCategoryNotFoundException(documentDefinitionCategoryId);
      }

      if (!documentService.documentDefinitionExists(
          documentDefinitionCategoryId, documentDefinitionId)) {
        throw new DocumentDefinitionNotFoundException(documentDefinitionId);
      }
    } catch (DocumentDefinitionCategoryNotFoundException | DocumentDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document definition ("
              + documentDefinitionId
              + ") for the document definition category ("
              + documentDefinitionCategoryId
              + ")",
          e);
    }

    documentService.deleteDocumentDefinition(documentDefinitionId);
  }

  @Override
  public void deleteDocumentDefinitionCategory(UUID tenantId, String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getDocumentDefinitionCategories() method where
     *       we want to retrieve the "global" and "tenant-specific" document definition categories.
     *       The ability to create or update document definition categories is an administrative
     *       function and is not assigned to a user for a particular tenant.
     */

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
        throw new DocumentNotFoundException(documentId);
      }

      if (!documentService.documentNoteExists(tenantId, documentId, documentNoteId)) {
        throw new DocumentNoteNotFoundException(documentId);
      }
    } catch (DocumentNotFoundException | DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document note ("
              + documentNoteId
              + ") for the document ("
              + documentId
              + ")",
          e);
    }

    documentService.deleteDocumentNote(tenantId, documentNoteId);
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
  public DocumentDefinition getDocumentDefinition(
      UUID tenantId, String documentDefinitionCategoryId, String documentDefinitionId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          DocumentDefinitionNotFoundException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getDocumentDefinitions() method where
     *       we want to retrieve the "global" and "tenant-specific" document definitions.
     *       The ability to create or update document definitions is an administrative function and
     *       is not assigned to a user for a particular tenant.
     */

    try {
      if (!documentService.documentDefinitionCategoryExists(documentDefinitionCategoryId)) {
        throw new DocumentDefinitionCategoryNotFoundException(documentDefinitionCategoryId);
      }

      if (!documentService.documentDefinitionExists(
          documentDefinitionCategoryId, documentDefinitionId)) {
        throw new DocumentDefinitionNotFoundException(documentDefinitionId);
      }
    } catch (DocumentDefinitionCategoryNotFoundException | DocumentDefinitionNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document definition ("
              + documentDefinitionId
              + ") under the document definition category ("
              + documentDefinitionCategoryId
              + ")",
          e);
    }

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
      UUID tenantId, String documentDefinitionCategoryId)
      throws InvalidArgumentException,
          DocumentDefinitionCategoryNotFoundException,
          ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getDocumentDefinitionCategories() method where
     *       we want to retrieve the "global" and "tenant-specific" document definition categories.
     *       The ability to create or update document definition categories is an administrative
     *       function and is not assigned to a user for a particular tenant.
     */

    DocumentDefinitionCategory documentDefinitionCategory =
        documentService.getDocumentDefinitionCategory(documentDefinitionCategoryId);

    return documentDefinitionCategory;
  }

  @Override
  public List<DocumentDefinitionSummary> getDocumentDefinitionSummaries(
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

    return documentService.getDocumentDefinitionSummaries(tenantId, documentDefinitionCategoryId);
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
        throw new DocumentNotFoundException(documentId);
      }

      if (!documentService.documentNoteExists(tenantId, documentId, documentNoteId)) {
        throw new DocumentNoteNotFoundException(documentNoteId);
      }
    } catch (DocumentNotFoundException | DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document note ("
              + documentNoteId
              + ") for the document ("
              + documentId
              + ")",
          e);
    }

    return documentService.getDocumentNote(tenantId, documentNoteId);
  }

  @Override
  public DocumentNotes getDocumentNotes(UUID tenantId, UUID documentId, String filter,
      DocumentNoteSortBy sortBy, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    tenantId = (tenantId == null) ? TenantUtil.DEFAULT_TENANT_ID : tenantId;

    if ((!hasAccessToFunction("Operations.OperationsAdministration"))
        && (!hasAccessToFunction("Operations.DocumentAdministration"))
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    return documentService.getDocumentNotes(tenantId, documentId, filter, sortBy, sortDirection, pageIndex, pageSize);
  }

  @Override
  public DocumentSummaries getDocumentSummaries(
      UUID tenantId,
      String definitionId,
      String filter,
      DocumentSortBy sortBy,
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

    return documentService.getDocumentSummaries(
        tenantId, definitionId, filter, sortBy, sortDirection, pageIndex, pageSize);
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
  public void updateDocumentDefinition(String documentDefinitionCategoryId,
      String documentDefinitionId, DocumentDefinition documentDefinition)
      throws InvalidArgumentException, DocumentDefinitionCategoryNotFoundException, DocumentDefinitionNotFoundException, ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getDocumentDefinitions() method where
     *       we want to retrieve the "global" and "tenant-specific" document definitions.
     *       The ability to create or update document definitions is an administrative function and
     *       is not assigned to a user for a particular tenant.
     */

    if (!StringUtils.hasText(documentDefinitionCategoryId)) {
      throw new InvalidArgumentException("documentDefinitionCategoryId");
    }

    if (!StringUtils.hasText(documentDefinitionId)) {
      throw new InvalidArgumentException("documentDefinitionId");
    }

    if (!Objects.equals(documentDefinitionCategoryId, documentDefinition.getCategoryId())) {
      throw new InvalidArgumentException("documentDefinition.categoryId");
    }

    if (!Objects.equals(documentDefinitionId, documentDefinition.getId())) {
      throw new InvalidArgumentException("documentDefinition.id");
    }

    documentService.updateDocumentDefinition(documentDefinition);
  }

  @Override
  public void updateDocumentDefinitionCategory(String documentDefinitionCategoryId,
      DocumentDefinitionCategory documentDefinitionCategory)
      throws InvalidArgumentException, DocumentDefinitionCategoryNotFoundException, ServiceUnavailableException {
    /*
     * NOTE: We do not reference the tenantId in this method. It is included to ensure consistency
     *       in the API. It is actually used in the getDocumentDefinitionCategories() method where
     *       we want to retrieve the "global" and "tenant-specific" document definition categories.
     *       The ability to create or update document definition categories is an administrative
     *       function and is not assigned to a user for a particular tenant.
     */

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
}
