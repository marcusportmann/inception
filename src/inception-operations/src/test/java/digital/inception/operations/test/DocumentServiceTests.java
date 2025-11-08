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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.file.FileType;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.util.TenantUtil;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DocumentTemplateCategoryNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.AttributeSearchCriteria;
import digital.inception.operations.model.AttributeType;
import digital.inception.operations.model.CreateDocumentNoteRequest;
import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.model.CreateDocumentTemplateRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.DocumentAttributeDefinition;
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
import digital.inception.operations.model.ExternalReferenceType;
import digital.inception.operations.model.ObjectType;
import digital.inception.operations.model.SearchDocumentsRequest;
import digital.inception.operations.model.UpdateDocumentNoteRequest;
import digital.inception.operations.model.UpdateDocumentRequest;
import digital.inception.operations.model.UpdateDocumentTemplateRequest;
import digital.inception.operations.service.DocumentService;
import digital.inception.operations.service.OperationsReferenceService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

  /** The Operations Reference Service. */
  @Autowired private OperationsReferenceService operationsReferenceService;

  /** Test the document service functionality. */
  @Test
  public void documentServiceTest() throws Exception {

    ExternalReferenceType externalReferenceType =
        new ExternalReferenceType(
            "test_document_external_reference_code",
            "Test Document External Reference",
            "Test Document External Reference Description",
            ObjectType.DOCUMENT,
            TenantUtil.DEFAULT_TENANT_ID);

    operationsReferenceService.createExternalReferenceType(externalReferenceType);

    List<ExternalReferenceType> retrievedExternalReferenceTypes =
        operationsReferenceService.getExternalReferenceTypes();

    assertEquals(1, retrievedExternalReferenceTypes.size());

    retrievedExternalReferenceTypes =
        operationsReferenceService.getExternalReferenceTypes(TenantUtil.DEFAULT_TENANT_ID);

    assertEquals(1, retrievedExternalReferenceTypes.size());

    retrievedExternalReferenceTypes =
        operationsReferenceService.getExternalReferenceTypes(UUID.randomUUID());

    assertEquals(0, retrievedExternalReferenceTypes.size());

    ExternalReferenceType retrievedExternalReferenceType =
        operationsReferenceService.getExternalReferenceType(
            "test_document_external_reference_code");

    assertEquals("test_document_external_reference_code", retrievedExternalReferenceType.getCode());
    assertEquals("Test Document External Reference", retrievedExternalReferenceType.getName());
    assertEquals(
        "Test Document External Reference Description",
        retrievedExternalReferenceType.getDescription());
    assertEquals(ObjectType.DOCUMENT, retrievedExternalReferenceType.getObjectType());
    assertEquals(TenantUtil.DEFAULT_TENANT_ID, retrievedExternalReferenceType.getTenantId());

    DocumentDefinitionCategory sharedDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_shared_document_definition_category_" + randomId(),
            "Test Shared Document Definition Category");

    documentService.createDocumentDefinitionCategory(sharedDocumentDefinitionCategory);

    assertTrue(
        documentService.documentDefinitionCategoryExists(sharedDocumentDefinitionCategory.getId()));

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
            null,
            "Test Shared Document Definition",
            "The description for the test shared document definition",
            null,
            List.of(
                new DocumentAttributeDefinition(
                    "testDocumentAttribute",
                    AttributeType.STRING,
                    "Test Document Attribute",
                    "Test Document Attribute Description",
                    true,
                    "(?i).*value.*")));

    documentService.createDocumentDefinition(sharedDocumentDefinition);

    assertTrue(documentService.documentDefinitionExists(sharedDocumentDefinition.getId()));

    assertTrue(
        documentService.documentDefinitionExists(
            sharedDocumentDefinition.getCategoryId(), sharedDocumentDefinition.getId()));

    DocumentDefinition retrievedDocumentDefinition =
        documentService.getDocumentDefinition(sharedDocumentDefinition.getId());

    compareDocumentDefinitions(sharedDocumentDefinition, retrievedDocumentDefinition);

    DocumentDefinition tenantDocumentDefinition =
        new DocumentDefinition(
            "test_tenant_document_definition_" + randomId(),
            tenantDocumentDefinitionCategory.getId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition",
            "The description for the test tenant document definition",
            null,
            null);

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

    documentService.updateDocumentDefinition(tenantDocumentDefinition);

    retrievedDocumentDefinition =
        documentService.getDocumentDefinition(tenantDocumentDefinition.getId());

    compareDocumentDefinitions(tenantDocumentDefinition, retrievedDocumentDefinition);

    Document document =
        documentService.createDocument(
            TenantUtil.DEFAULT_TENANT_ID,
            getCreateDocumentRequest(sharedDocumentDefinition.getId()),
            "TEST1");

    assertTrue(documentService.documentExists(TenantUtil.DEFAULT_TENANT_ID, document.getId()));

    Document retrievedDocument =
        documentService.getDocument(TenantUtil.DEFAULT_TENANT_ID, document.getId());

    compareDocuments(document, retrievedDocument);

    UpdateDocumentRequest updateDocumentRequest = getUpdateDocumentRequest(document);

    Document updatedDocument =
        documentService.updateDocument(
            TenantUtil.DEFAULT_TENANT_ID, updateDocumentRequest, "TEST2");

    retrievedDocument = documentService.getDocument(TenantUtil.DEFAULT_TENANT_ID, document.getId());

    assertArrayEquals(
        updateDocumentRequest.getData(),
        retrievedDocument.getData(),
        "Invalid value for the \"data\" document property");
    assertEquals(
        updateDocumentRequest.getFileType(),
        retrievedDocument.getFileType(),
        "Invalid value for the \"fileType\" document property");
    assertEquals(
        updateDocumentRequest.getDocumentId(),
        retrievedDocument.getId(),
        "Invalid value for the \"id\" document property");
    assertEquals(
        updateDocumentRequest.getName(),
        retrievedDocument.getName(),
        "Invalid value for the \"name\" document property");
    assertEquals(
        updateDocumentRequest.getSourceDocumentId(),
        retrievedDocument.getSourceDocumentId(),
        "Invalid value for the \"sourceDocumentId\" document property");

    List<AttributeSearchCriteria> documentAttributeSearchCriteria = new ArrayList<>();
    documentAttributeSearchCriteria.add(
        new AttributeSearchCriteria(
            "testDocumentAttribute", "test_document_attribute_updated_value"));

    List<ExternalReferenceSearchCriteria> documentExternalReferenceSearchCriteria =
        new ArrayList<>();
    documentExternalReferenceSearchCriteria.add(
        new ExternalReferenceSearchCriteria(
            "test_document_external_reference_code",
            "test_document_external_reference_updated_value"));

    SearchDocumentsRequest searchDocumentsRequest =
        new SearchDocumentsRequest(
            retrievedDocument.getDefinitionId(),
            documentAttributeSearchCriteria,
            documentExternalReferenceSearchCriteria,
            DocumentSortBy.DEFINITION_ID,
            SortDirection.ASCENDING,
            0,
            10);

    DocumentSummaries documentSummaries =
        documentService.searchDocuments(TenantUtil.DEFAULT_TENANT_ID, searchDocumentsRequest);

    assertEquals(1, documentSummaries.getTotal());

    DocumentSummary documentSummary = documentSummaries.getDocumentSummaries().getFirst();

    assertEquals(
        retrievedDocument.getFileType(),
        documentSummary.getFileType(),
        "Invalid value for the \"fileType\" document summary property");
    assertEquals(
        retrievedDocument.getId(),
        documentSummary.getId(),
        "Invalid value for the \"id\" document summary property");
    assertEquals(
        retrievedDocument.getName(),
        documentSummary.getName(),
        "Invalid value for the \"name\" document summary property");
    assertEquals(
        retrievedDocument.getSourceDocumentId(),
        documentSummary.getSourceDocumentId(),
        "Invalid value for the \"sourceDocumentId\" document summary property");

    CreateDocumentNoteRequest createDocumentNoteRequest =
        new CreateDocumentNoteRequest(document.getId(), "This is the document note content.");

    DocumentNote documentNote =
        documentService.createDocumentNote(
            TenantUtil.DEFAULT_TENANT_ID, createDocumentNoteRequest, "TEST1");

    assertTrue(
        documentService.documentNoteExists(
            TenantUtil.DEFAULT_TENANT_ID, documentNote.getDocumentId(), documentNote.getId()));

    DocumentNote retrievedDocumentNote =
        documentService.getDocumentNote(TenantUtil.DEFAULT_TENANT_ID, documentNote.getId());

    compareDocumentNotes(documentNote, retrievedDocumentNote);

    UpdateDocumentNoteRequest updateDocumentNoteRequest =
        new UpdateDocumentNoteRequest(documentNote.getId(), "This is the document note content.");

    DocumentNote updatedDocumentNote =
        documentService.updateDocumentNote(
            TenantUtil.DEFAULT_TENANT_ID, updateDocumentNoteRequest, "TEST2");

    retrievedDocumentNote =
        documentService.getDocumentNote(TenantUtil.DEFAULT_TENANT_ID, updatedDocumentNote.getId());

    compareDocumentNotes(updatedDocumentNote, retrievedDocumentNote);

    DocumentNotes documentNotes =
        documentService.getDocumentNotes(
            TenantUtil.DEFAULT_TENANT_ID,
            document.getId(),
            "TEST2",
            DocumentNoteSortBy.CREATED,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(1, documentNotes.getTotal());

    compareDocumentNotes(updatedDocumentNote, documentNotes.getDocumentNotes().getFirst());

    documentService.deleteDocumentNote(TenantUtil.DEFAULT_TENANT_ID, documentNote.getId());

    assertThrows(
        DocumentNoteNotFoundException.class,
        () -> {
          documentService.getDocumentNote(TenantUtil.DEFAULT_TENANT_ID, documentNote.getId());
        });

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

    sharedDocumentDefinitionCategory.setName(
        "Updated " + sharedDocumentDefinitionCategory.getName());
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

    operationsReferenceService.deleteExternalReferenceType("test_document_external_reference_code");
  }

  /** Test the document template functionality. */
  @Test
  void documentTemplateTest() throws Exception {
    DocumentTemplateCategory sharedDocumentTemplateCategory =
        new DocumentTemplateCategory(
            "test_shared_document_template_category_" + randomId(),
            null,
            "Test Shared Document Template Category");

    documentService.createDocumentTemplateCategory(sharedDocumentTemplateCategory);

    DocumentTemplateCategory retrievedDocumentTemplateCategory =
        documentService.getDocumentTemplateCategory(sharedDocumentTemplateCategory.getId());

    compareDocumentTemplateCategories(
        sharedDocumentTemplateCategory, retrievedDocumentTemplateCategory);

    DocumentTemplateCategory tenantDocumentTemplateCategory =
        new DocumentTemplateCategory(
            "test_tenant_document_template_category_" + randomId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Template Category");

    documentService.createDocumentTemplateCategory(tenantDocumentTemplateCategory);

    retrievedDocumentTemplateCategory =
        documentService.getDocumentTemplateCategory(tenantDocumentTemplateCategory.getId());

    compareDocumentTemplateCategories(
        tenantDocumentTemplateCategory, retrievedDocumentTemplateCategory);

    List<DocumentTemplateCategory> retrievedDocumentTemplateCategories =
        documentService.getDocumentTemplateCategories(TenantUtil.DEFAULT_TENANT_ID);

    assertEquals(2, retrievedDocumentTemplateCategories.size());

    retrievedDocumentTemplateCategories =
        documentService.getDocumentTemplateCategories(UUID.randomUUID());

    assertEquals(1, retrievedDocumentTemplateCategories.size());

    compareDocumentTemplateCategories(
        sharedDocumentTemplateCategory, retrievedDocumentTemplateCategories.getFirst());

    CreateDocumentTemplateRequest createSharedDocumentTemplateRequest =
        getCreateDocumentTemplateRequest(
            "test_shared_document_template_" + randomId(),
            sharedDocumentTemplateCategory.getId(),
            null,
            "Test Shared Document Template",
            "Test Shared Document Template Description");

    documentService.createDocumentTemplate(createSharedDocumentTemplateRequest, "TEST1");

    DocumentTemplate retrievedDocumentTemplate =
        documentService.getDocumentTemplate(createSharedDocumentTemplateRequest.getId());

    CreateDocumentTemplateRequest createTenantDocumentTemplateRequest =
        getCreateDocumentTemplateRequest(
            "test_tenant_document_template_" + randomId(),
            sharedDocumentTemplateCategory.getId(),
            TenantUtil.DEFAULT_TENANT_ID,
            "Test Tenant Document Template",
            "Test Tenant Document Template Description");

    documentService.createDocumentTemplate(createTenantDocumentTemplateRequest, "TEST1");

    retrievedDocumentTemplate =
        documentService.getDocumentTemplate(createTenantDocumentTemplateRequest.getId());

    assertEquals(createTenantDocumentTemplateRequest.getId(), retrievedDocumentTemplate.getId());
    assertEquals(
        createTenantDocumentTemplateRequest.getCategoryId(),
        retrievedDocumentTemplate.getCategoryId());
    assertEquals(
        createTenantDocumentTemplateRequest.getTenantId(), retrievedDocumentTemplate.getTenantId());
    assertEquals(
        createTenantDocumentTemplateRequest.getName(), retrievedDocumentTemplate.getName());
    assertEquals(
        createTenantDocumentTemplateRequest.getDescription(),
        retrievedDocumentTemplate.getDescription());
    assertArrayEquals(
        createTenantDocumentTemplateRequest.getData(), retrievedDocumentTemplate.getData());
    assertNotNull(retrievedDocumentTemplate.getCreated());
    assertEquals("TEST1", retrievedDocumentTemplate.getCreatedBy());
    assertNull(retrievedDocumentTemplate.getUpdated());
    assertNull(retrievedDocumentTemplate.getUpdatedBy());

    UpdateDocumentTemplateRequest updateTenantDocumentTemplateRequest =
        getUpdateDocumentTemplateRequest(createTenantDocumentTemplateRequest);

    documentService.updateDocumentTemplate(updateTenantDocumentTemplateRequest, "TEST2");

    retrievedDocumentTemplate =
        documentService.getDocumentTemplate(createTenantDocumentTemplateRequest.getId());

    assertEquals(updateTenantDocumentTemplateRequest.getId(), retrievedDocumentTemplate.getId());
    assertEquals(
        updateTenantDocumentTemplateRequest.getCategoryId(),
        retrievedDocumentTemplate.getCategoryId());
    assertEquals(
        updateTenantDocumentTemplateRequest.getName(), retrievedDocumentTemplate.getName());
    assertEquals(
        updateTenantDocumentTemplateRequest.getDescription(),
        retrievedDocumentTemplate.getDescription());
    assertArrayEquals(
        updateTenantDocumentTemplateRequest.getData(), retrievedDocumentTemplate.getData());
    assertNotNull(retrievedDocumentTemplate.getCreated());
    assertEquals("TEST1", retrievedDocumentTemplate.getCreatedBy());
    assertNotNull(retrievedDocumentTemplate.getUpdated());
    assertEquals("TEST2", retrievedDocumentTemplate.getUpdatedBy());

    DocumentTemplateSummaries documentTemplateSummaries =
        documentService.getDocumentTemplateSummaries(
            TenantUtil.DEFAULT_TENANT_ID,
            "Test",
            DocumentTemplateSortBy.NAME,
            SortDirection.ASCENDING,
            0,
            10);

    assertEquals(2, documentTemplateSummaries.getTotal());
    assertEquals(
        createSharedDocumentTemplateRequest.getId(),
        documentTemplateSummaries.getDocumentTemplateSummaries().getFirst().getId());

    tenantDocumentTemplateCategory.setName(tenantDocumentTemplateCategory.getName() + " Updated");

    documentService.updateDocumentTemplateCategory(tenantDocumentTemplateCategory);

    retrievedDocumentTemplateCategory =
        documentService.getDocumentTemplateCategory(tenantDocumentTemplateCategory.getId());

    compareDocumentTemplateCategories(
        tenantDocumentTemplateCategory, retrievedDocumentTemplateCategory);

    List<DocumentTemplateSummary> retrievedDocumentTemplateSummaries =
        documentService.getDocumentTemplateSummaries(
            TenantUtil.DEFAULT_TENANT_ID, sharedDocumentTemplateCategory.getId());

    assertEquals(2, retrievedDocumentTemplateSummaries.size());

    documentService.deleteDocumentTemplateCategory(tenantDocumentTemplateCategory.getId());

    documentService.deleteDocumentTemplateCategory(sharedDocumentTemplateCategory.getId());

    assertThrows(
        DocumentTemplateCategoryNotFoundException.class,
        () -> {
          documentService.getDocumentTemplateCategory(sharedDocumentTemplateCategory.getId());
        });
  }

  private void compareDocumentAttributeDefinitions(
      DocumentAttributeDefinition documentAttributeDefinition1,
      DocumentAttributeDefinition documentAttributeDefinition2) {
    assertEquals(
        documentAttributeDefinition1.getDescription(),
        documentAttributeDefinition2.getDescription(),
        "The description values for the document attribute definitions do not match");
    assertEquals(
        documentAttributeDefinition1.getLabel(),
        documentAttributeDefinition2.getLabel(),
        "The label values for the document attribute definitions do not match");
    assertEquals(
        documentAttributeDefinition1.getName(),
        documentAttributeDefinition2.getName(),
        "The name values for the document attribute definitions do not match");
    assertEquals(
        documentAttributeDefinition1.getPattern(),
        documentAttributeDefinition2.getPattern(),
        "The pattern values for the document attribute definitions do not match");
    assertEquals(
        documentAttributeDefinition1.getType(),
        documentAttributeDefinition2.getType(),
        "The type values for the document attribute definitions do not match");
    assertEquals(
        documentAttributeDefinition1.isRequired(),
        documentAttributeDefinition2.isRequired(),
        "The required values for the document attribute definitions do not match");
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
        documentDefinition1.getDescription(),
        documentDefinition2.getDescription(),
        "The description values for the document definitions do not match");
    assertEquals(
        documentDefinition1.getTemplateId(),
        documentDefinition2.getTemplateId(),
        "The template ID values for the document definitions do not match");

    assertEquals(
        documentDefinition1.getAttributeDefinitions().size(),
        documentDefinition2.getAttributeDefinitions().size(),
        "The number of attribute definitions for the documents definitions do not match");

    documentDefinition1
        .getAttributeDefinitions()
        .forEach(
            documentAttributeDefinition1 -> {
              List<DocumentAttributeDefinition> foundAttributeDefinitions =
                  documentDefinition2.getAttributeDefinitions().stream()
                      .filter(
                          documentAttributeDefinition2 ->
                              Objects.equals(
                                  documentAttributeDefinition1, documentAttributeDefinition2))
                      .toList();

              assertEquals(1, foundAttributeDefinitions.size());

              compareDocumentAttributeDefinitions(
                  documentAttributeDefinition1, foundAttributeDefinitions.get(0));
            });
  }

  private void compareDocumentNotes(DocumentNote documentNote1, DocumentNote documentNote2) {
    assertEquals(
        documentNote1.getId(),
        documentNote2.getId(),
        "The ID values for the document notes do not match");
    assertEquals(
        documentNote1.getContent(),
        documentNote2.getContent(),
        "The content values for the document notes do not match");
    assertEquals(
        documentNote1.getCreated(),
        documentNote2.getCreated(),
        "The created values for the document notes do not match");
    assertEquals(
        documentNote1.getCreatedBy(),
        documentNote2.getCreatedBy(),
        "The created by values for the document notes do not match");
    assertEquals(
        documentNote1.getUpdated(),
        documentNote2.getUpdated(),
        "The updated values for the document notes do not match");
    assertEquals(
        documentNote1.getUpdatedBy(),
        documentNote2.getUpdatedBy(),
        "The updated by values for the document notes do not match");
  }

  private void compareDocumentTemplateCategories(
      DocumentTemplateCategory documentTemplateCategory1,
      DocumentTemplateCategory documentTemplateCategory2) {
    assertEquals(
        documentTemplateCategory1.getId(),
        documentTemplateCategory2.getId(),
        "The ID values for the document template categories do not match");
    assertEquals(
        documentTemplateCategory1.getTenantId(),
        documentTemplateCategory2.getTenantId(),
        "The ID values for the document template categories do not match");
    assertEquals(
        documentTemplateCategory1.getName(),
        documentTemplateCategory2.getName(),
        "The name values for the document template categories do not match");
  }

  private void compareDocuments(Document document1, Document document2) {
    assertEquals(
        document1.getCreated(),
        document2.getCreated(),
        "The created values for the documents do not match");
    assertEquals(
        document1.getCreatedBy(),
        document2.getCreatedBy(),
        "The created by values for the documents do not match");
    assertArrayEquals(
        document1.getData(), document2.getData(), "The data values for the documents do not match");
    assertEquals(
        document1.getDefinitionId(),
        document2.getDefinitionId(),
        "The definition ID values for the documents do not match");
    assertEquals(
        document1.getFileType(),
        document2.getFileType(),
        "The file type values for the documents do not match");
    assertEquals(
        document1.getHash(), document2.getHash(), "The hash values for the documents do not match");
    assertEquals(
        document1.getId(), document2.getId(), "The ID values for the documents do not match");
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
    assertEquals(
        document1.getUpdated(),
        document2.getUpdated(),
        "The updated values for the documents do not match");
    assertEquals(
        document1.getUpdatedBy(),
        document2.getUpdatedBy(),
        "The updated by values for the documents do not match");

    assertEquals(
        document1.getAttributes().size(),
        document2.getAttributes().size(),
        "The number of attributes for the documents do not match");

    document1
        .getAttributes()
        .forEach(
            documentAttribute1 -> {
              boolean foundAttribute =
                  document2.getAttributes().stream()
                      .anyMatch(
                          documentAttribute2 ->
                              Objects.equals(documentAttribute1, documentAttribute2));
              if (!foundAttribute) {
                fail(
                    "Failed to find the attribute ("
                        + documentAttribute1.getName()
                        + ") for the document ("
                        + document1.getId()
                        + ")");
              }
            });
  }

  private CreateDocumentRequest getCreateDocumentRequest(String documentDefinitionId) {
    byte[] data = "This is some test data.".getBytes(StandardCharsets.UTF_8);

    CreateDocumentRequest createDocumentRequest = new CreateDocumentRequest();

    createDocumentRequest.setAttributes(
        List.of(new DocumentAttribute("testDocumentAttribute", "test_document_attribute_value")));
    createDocumentRequest.setData(data);
    createDocumentRequest.setDefinitionId(documentDefinitionId);
    createDocumentRequest.setExpiryDate(LocalDate.now().plusMonths(6));
    createDocumentRequest.setExternalReferences(
        List.of(
            new DocumentExternalReference(
                "test_document_external_reference_code",
                "test_document_external_reference_value")));
    createDocumentRequest.setFileType(FileType.TEXT);
    createDocumentRequest.setIssueDate(LocalDate.of(2016, 7, 16));
    createDocumentRequest.setName("test.txt");
    createDocumentRequest.setSourceDocumentId(null);

    return createDocumentRequest;
  }

  private CreateDocumentTemplateRequest getCreateDocumentTemplateRequest(
      String id, String categoryId, UUID tenantId, String name, String description) {
    byte[] data = "This is some test data.".getBytes(StandardCharsets.UTF_8);

    CreateDocumentTemplateRequest createDocumentTemplateRequest =
        new CreateDocumentTemplateRequest();

    createDocumentTemplateRequest.setId(id);
    createDocumentTemplateRequest.setCategoryId(categoryId);
    createDocumentTemplateRequest.setTenantId(tenantId);
    createDocumentTemplateRequest.setName(name);
    createDocumentTemplateRequest.setDescription(description);
    createDocumentTemplateRequest.setData(data);

    return createDocumentTemplateRequest;
  }

  private UpdateDocumentRequest getUpdateDocumentRequest(Document document) {
    byte[] data =
        "<html><body>This is some HTML test data.</body></html>".getBytes(StandardCharsets.UTF_8);

    UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest();

    updateDocumentRequest.setAttributes(
        List.of(
            new DocumentAttribute(
                "testDocumentAttribute", "test_document_attribute_updated_value")));
    updateDocumentRequest.setData(data);
    updateDocumentRequest.setExpiryDate(LocalDate.now().plusMonths(3));
    updateDocumentRequest.setExternalReferences(
        List.of(
            new DocumentExternalReference(
                "test_document_external_reference_code",
                "test_document_external_reference_updated_value")));
    updateDocumentRequest.setFileType(FileType.HTML);
    updateDocumentRequest.setDocumentId(document.getId());
    updateDocumentRequest.setIssueDate(LocalDate.of(2017, 8, 17));
    updateDocumentRequest.setName("test.html");
    updateDocumentRequest.setSourceDocumentId(UUID.randomUUID());

    return updateDocumentRequest;
  }

  private UpdateDocumentTemplateRequest getUpdateDocumentTemplateRequest(
      CreateDocumentTemplateRequest createDocumentTemplateRequest) {
    byte[] data = "This is some updated test data.".getBytes(StandardCharsets.UTF_8);

    UpdateDocumentTemplateRequest updateDocumentTemplateRequest =
        new UpdateDocumentTemplateRequest();

    updateDocumentTemplateRequest.setId(createDocumentTemplateRequest.getId());
    updateDocumentTemplateRequest.setCategoryId(createDocumentTemplateRequest.getCategoryId());
    updateDocumentTemplateRequest.setTenantId(createDocumentTemplateRequest.getTenantId());
    updateDocumentTemplateRequest.setName(createDocumentTemplateRequest.getName() + " Updated");
    updateDocumentTemplateRequest.setDescription(
        createDocumentTemplateRequest.getDescription() + " Updated");
    updateDocumentTemplateRequest.setData(data);

    return updateDocumentTemplateRequest;
  }

  private String randomId() {
    return String.format("%04X", secureRandom.nextInt(0x10000));
  }
}
