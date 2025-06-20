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

package digital.inception.operations.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.file.FileType;
import digital.inception.core.util.TenantUtil;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.RequiredDocumentAttribute;
import digital.inception.operations.service.DocumentService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The {@code DocumentServiceTests} class contains the JUnit tests for the {@code DocumentService}
 * class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, OperationsConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class DocumentServiceTests {

  /** The secure random number generator. */
  private static final SecureRandom secureRandom = new SecureRandom();

  /** The Document Service. */
  @Autowired private DocumentService documentService;

  /** The Jackson Object Mapper. */
  @Autowired private ObjectMapper objectMapper;

  /** Test the document service functionality. */
  @Test
  public void documentServiceTest() throws Exception {
    DocumentDefinitionCategory sharedDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_shared_document_definition_category_" + randomId(),
            "Test Shared Document Definition Category");

    documentService.createDocumentDefinitionCategory(sharedDocumentDefinitionCategory);

    DocumentDefinitionCategory retrievedDocumentDefinitionCategory =
        documentService.getDocumentDefinitionCategory(sharedDocumentDefinitionCategory.getId());

    compareDocumentDefinitionCategories(
        sharedDocumentDefinitionCategory, retrievedDocumentDefinitionCategory);

    DocumentDefinitionCategory tenantDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_tenant_document_definition_category_" + randomId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition Category");

    documentService.createDocumentDefinitionCategory(tenantDocumentDefinitionCategory);

    assertThrows(
        DuplicateDocumentDefinitionCategoryException.class,
        () -> {
          documentService.createDocumentDefinitionCategory(tenantDocumentDefinitionCategory);
        });

    retrievedDocumentDefinitionCategory =
        documentService.getDocumentDefinitionCategory(tenantDocumentDefinitionCategory.getId());

    compareDocumentDefinitionCategories(
        tenantDocumentDefinitionCategory, retrievedDocumentDefinitionCategory);

    List<DocumentDefinitionCategory> documentDefinitionCategories =
        documentService.getDocumentDefinitionCategories(TenantUtil.DEFAULT_TENANT_ID);

    assertEquals(2, documentDefinitionCategories.size());

    documentDefinitionCategories =
        documentService.getDocumentDefinitionCategories(UUID.randomUUID());

    assertEquals(1, documentDefinitionCategories.size());

    compareDocumentDefinitionCategories(
        sharedDocumentDefinitionCategory, documentDefinitionCategories.getFirst());

    DocumentDefinition sharedDocumentDefinition =
        new DocumentDefinition(
            "test_shared_document_definition_" + randomId(),
            sharedDocumentDefinitionCategory.getId(),
            "Test Shared Document Definition",
            List.of(
                RequiredDocumentAttribute.EXPIRY_DATE,
                RequiredDocumentAttribute.EXTERNAL_REFERENCE,
                RequiredDocumentAttribute.ISSUE_DATE));

    documentService.createDocumentDefinition(sharedDocumentDefinition);

    DocumentDefinition retrievedDocumentDefinition =
        documentService.getDocumentDefinition(sharedDocumentDefinition.getId());

    assertNotNull(retrievedDocumentDefinition.getRequiredDocumentAttributes());

    assertTrue(
        retrievedDocumentDefinition
            .getRequiredDocumentAttributes()
            .contains(RequiredDocumentAttribute.EXTERNAL_REFERENCE));

    compareDocumentDefinitions(sharedDocumentDefinition, retrievedDocumentDefinition);

    DocumentDefinition tenantDocumentDefinition =
        new DocumentDefinition(
            "test_tenant_document_definition_" + randomId(),
            tenantDocumentDefinitionCategory.getId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition");

    documentService.createDocumentDefinition(tenantDocumentDefinition);

    assertThrows(
        DuplicateDocumentDefinitionException.class,
        () -> {
          documentService.createDocumentDefinition(tenantDocumentDefinition);
        });

    retrievedDocumentDefinition =
        documentService.getDocumentDefinition(tenantDocumentDefinition.getId());

    compareDocumentDefinitions(tenantDocumentDefinition, retrievedDocumentDefinition);

    List<DocumentDefinition> documentDefinitions =
        documentService.getDocumentDefinitions(
            TenantUtil.DEFAULT_TENANT_ID, sharedDocumentDefinitionCategory.getId());

    assertEquals(1, documentDefinitions.size());
    assertEquals(sharedDocumentDefinition.getId(), documentDefinitions.getFirst().getId());

    documentDefinitions =
        documentService.getDocumentDefinitions(
            UUID.randomUUID(), sharedDocumentDefinitionCategory.getId());

    assertEquals(1, documentDefinitions.size());
    assertEquals(sharedDocumentDefinition.getId(), documentDefinitions.getFirst().getId());

    documentDefinitions =
        documentService.getDocumentDefinitions(
            TenantUtil.DEFAULT_TENANT_ID, tenantDocumentDefinitionCategory.getId());

    assertEquals(1, documentDefinitions.size());
    assertEquals(tenantDocumentDefinition.getId(), documentDefinitions.getFirst().getId());

    documentDefinitions =
        documentService.getDocumentDefinitions(
            UUID.randomUUID(), tenantDocumentDefinitionCategory.getId());

    assertEquals(0, documentDefinitions.size());

    tenantDocumentDefinition.setName("Updated Test Tenant Document Definition");
    tenantDocumentDefinition.setCategoryId(sharedDocumentDefinitionCategory.getId());
    tenantDocumentDefinition.setRequiredDocumentAttributes(
        List.of(RequiredDocumentAttribute.EXPIRY_DATE));

    documentService.updateDocumentDefinition(tenantDocumentDefinition);

    retrievedDocumentDefinition =
        documentService.getDocumentDefinition(tenantDocumentDefinition.getId());

    compareDocumentDefinitions(tenantDocumentDefinition, retrievedDocumentDefinition);

    Document document =
        documentService.createDocument(
            TenantUtil.DEFAULT_TENANT_ID,
            getCreateDocumentRequest(sharedDocumentDefinition.getId()));

    Document retrievedDocument =
        documentService.getDocument(TenantUtil.DEFAULT_TENANT_ID, document.getId());

    compareDocuments(document, retrievedDocument);

    documentService.deleteDocument(TenantUtil.DEFAULT_TENANT_ID, document.getId());

    assertThrows(
        DocumentNotFoundException.class,
        () -> {
          documentService.getDocument(TenantUtil.DEFAULT_TENANT_ID, document.getId());
        });

    documentService.deleteDocumentDefinition(tenantDocumentDefinition.getId());

    documentService.deleteDocumentDefinition(sharedDocumentDefinition.getId());

    assertThrows(
        DocumentDefinitionNotFoundException.class,
        () -> {
          documentService.getDocumentDefinition(sharedDocumentDefinition.getId());
        });

    sharedDocumentDefinitionCategory.setName("Updated Test Shared Document Definition Category");
    sharedDocumentDefinitionCategory.setTenantId(TenantUtil.DEFAULT_TENANT_ID);

    documentService.updateDocumentDefinitionCategory(sharedDocumentDefinitionCategory);

    retrievedDocumentDefinitionCategory =
        documentService.getDocumentDefinitionCategory(sharedDocumentDefinitionCategory.getId());

    compareDocumentDefinitionCategories(
        sharedDocumentDefinitionCategory, retrievedDocumentDefinitionCategory);

    documentService.deleteDocumentDefinitionCategory(tenantDocumentDefinitionCategory.getId());

    documentService.deleteDocumentDefinitionCategory(sharedDocumentDefinitionCategory.getId());

    assertThrows(
        DocumentDefinitionCategoryNotFoundException.class,
        () -> {
          documentService.getDocumentDefinitionCategory(sharedDocumentDefinitionCategory.getId());
        });
  }

  private void compareDocumentDefinitionCategories(
      DocumentDefinitionCategory documentDefinitionCategory1,
      DocumentDefinitionCategory documentDefinitionCategory2) {
    assertEquals(
        documentDefinitionCategory1.getId(),
        documentDefinitionCategory2.getId(),
        "The ID values for the document definition categories do not match");
    assertEquals(
        documentDefinitionCategory1.getTenantId(),
        documentDefinitionCategory2.getTenantId(),
        "The tenant ID values for the document definition categories do not match");
    assertEquals(
        documentDefinitionCategory1.getName(),
        documentDefinitionCategory2.getName(),
        "The name values for the document definition categories do not match");
  }

  private void compareDocumentDefinitions(
      DocumentDefinition documentDefinition1, DocumentDefinition documentDefinition2) {
    assertEquals(
        documentDefinition1.getId(),
        documentDefinition2.getId(),
        "The ID values for the document definitions do not match");
    assertEquals(
        documentDefinition1.getCategoryId(),
        documentDefinition2.getCategoryId(),
        "The category ID values for the document definitions do not match");
    assertEquals(
        documentDefinition1.getTenantId(),
        documentDefinition2.getTenantId(),
        "The tenant ID values for the document definitions do not match");
    assertEquals(
        documentDefinition1.getName(),
        documentDefinition2.getName(),
        "The name values for the document definitions do not match");

    assertEquals(
        (documentDefinition1.getRequiredDocumentAttributes() != null)
            ? documentDefinition1.getRequiredDocumentAttributes()
            : List.of(),
        (documentDefinition2.getRequiredDocumentAttributes() != null)
            ? documentDefinition2.getRequiredDocumentAttributes()
            : List.of(),
        "The required document attributes values for the document definitions do not match");
  }

  private void compareDocuments(Document document1, Document document2) {
    assertArrayEquals(
        document1.getData(), document2.getData(), "The data values for the documents do not match");
    assertEquals(
        document1.getDefinitionId(),
        document2.getDefinitionId(),
        "The definition ID values for the documents do not match");
    assertEquals(
        document1.getExpiryDate(),
        document2.getExpiryDate(),
        "The expiry date values for the documents do not match");
    assertEquals(
        document1.getExternalReference(),
        document2.getExternalReference(),
        "The external reference values for the documents do not match");
    assertEquals(
        document1.getFileType(),
        document2.getFileType(),
        "The file type values for the documents do not match");
    assertEquals(
        document1.getHash(), document2.getHash(), "The hash values for the documents do not match");
    assertEquals(
        document1.getId(), document2.getId(), "The ID values for the documents do not match");
    assertEquals(
        document1.getIssueDate(),
        document2.getIssueDate(),
        "The issue date values for the documents do not match");
    assertEquals(
        document1.getName(), document2.getName(), "The name values for the documents do not match");
    assertEquals(
        document1.getSourceDocumentId(),
        document2.getSourceDocumentId(),
        "The source document ID values for the documents do not match");
    assertEquals(
        document1.getTenantId(),
        document2.getTenantId(),
        "The tenant ID values for the documents do not match");
  }

  private CreateDocumentRequest getCreateDocumentRequest(String documentDefinitionId) {
    byte[] data = "This is some test data.".getBytes(StandardCharsets.UTF_8);

    CreateDocumentRequest createDocumentRequest = new CreateDocumentRequest();

    createDocumentRequest.setData(data);
    createDocumentRequest.setDefinitionId(documentDefinitionId);
    createDocumentRequest.setExpiryDate(LocalDate.now().plusMonths(6));
    createDocumentRequest.setExternalReference(UUID.randomUUID().toString());
    createDocumentRequest.setFileType(FileType.TEXT);
    createDocumentRequest.setIssueDate(LocalDate.of(2016, 7, 16));
    createDocumentRequest.setName("test.txt");
    createDocumentRequest.setSourceDocumentId(null);

    return createDocumentRequest;
  }

  private String randomId() {
    return String.format("%04X", secureRandom.nextInt(0x10000));
  }
}
