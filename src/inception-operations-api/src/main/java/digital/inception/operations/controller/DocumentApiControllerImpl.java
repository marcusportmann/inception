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
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.service.DocumentService;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
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
      ApplicationContext applicationContext,
      DocumentService documentService) {
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
        && (!hasAccessToTenant(tenantId))) {
      throw new AccessDeniedException("Access denied to the tenant (" + tenantId + ")");
    }

    Document document = documentService.createDocument(tenantId, createDocumentRequest);

    return document.getId();
  }
}
