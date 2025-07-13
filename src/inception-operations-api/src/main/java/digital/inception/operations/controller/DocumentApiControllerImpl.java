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
import digital.inception.core.util.TenantUtil;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.exception.DuplicateWorkflowDefinitionVersionException;
import digital.inception.operations.exception.WorkflowDefinitionCategoryNotFoundException;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.UpdateDocumentNoteRequest;
import digital.inception.operations.model.UpdateDocumentRequest;
import digital.inception.operations.model.WorkflowDefinition;
import digital.inception.operations.service.DocumentService;
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
