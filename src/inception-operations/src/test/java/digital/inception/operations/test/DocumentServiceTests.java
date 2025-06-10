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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.operations.OperationsConfiguration;
import digital.inception.operations.exception.DocumentDefinitionCategoryNotFoundException;
import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionCategoryException;
import digital.inception.operations.exception.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionCategory;
import digital.inception.operations.model.RequiredDocumentAttribute;
import digital.inception.operations.service.DocumentService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.util.List;
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

  /** The Document Service. */
  @Autowired private DocumentService documentService;

  /** The Jackson Object Mapper. */
  @Autowired private ObjectMapper objectMapper;

  /** Test the document definition functionality. */
  @Test
  public void documentDefinitionTest() throws Exception {
    DocumentDefinitionCategory testSharedDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_shared_document_definition_category", "Test Shared Document Definition Category");

    documentService.createDocumentDefinitionCategory(testSharedDocumentDefinitionCategory);

    DocumentDefinitionCategory retrievedDocumentDefinitionCategory =
        documentService.getDocumentDefinitionCategory(testSharedDocumentDefinitionCategory.getId());

    compareDocumentDefinitionCategories(
        testSharedDocumentDefinitionCategory, retrievedDocumentDefinitionCategory);

    DocumentDefinitionCategory testTenantDocumentDefinitionCategory =
        new DocumentDefinitionCategory(
            "test_tenant_document_definition_category",
            DocumentService.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition Category");

    documentService.createDocumentDefinitionCategory(testTenantDocumentDefinitionCategory);

    DuplicateDocumentDefinitionCategoryException duplicateDocumentDefinitionCategoryException =
        assertThrows(
            DuplicateDocumentDefinitionCategoryException.class,
            () -> {
              documentService.createDocumentDefinitionCategory(
                  testTenantDocumentDefinitionCategory);
            });

    retrievedDocumentDefinitionCategory =
        documentService.getDocumentDefinitionCategory(testTenantDocumentDefinitionCategory.getId());

    compareDocumentDefinitionCategories(
        testTenantDocumentDefinitionCategory, retrievedDocumentDefinitionCategory);

    DocumentDefinition testSharedDocumentDefinition =
        new DocumentDefinition(
            "test_shared_document_definition_" + System.currentTimeMillis(),
            testSharedDocumentDefinitionCategory.getId(),
            "Test Shared Document Definition",
            List.of(
                RequiredDocumentAttribute.EXPIRY_DATE,
                RequiredDocumentAttribute.EXTERNAL_REFERENCE,
                RequiredDocumentAttribute.ISSUE_DATE));

    documentService.createDocumentDefinition(testSharedDocumentDefinition);

    DocumentDefinition retrievedDocumentDefinition =
        documentService.getDocumentDefinition(testSharedDocumentDefinition.getId());

    assertNotNull(retrievedDocumentDefinition.getRequiredDocumentAttributes());

    assertTrue(
        retrievedDocumentDefinition
            .getRequiredDocumentAttributes()
            .contains(RequiredDocumentAttribute.EXTERNAL_REFERENCE));

    compareDocumentDefinitions(testSharedDocumentDefinition, retrievedDocumentDefinition);

    DocumentDefinition testTenantDocumentDefinition =
        new DocumentDefinition(
            "test_tenant_document_definition_" + System.currentTimeMillis(),
            testTenantDocumentDefinitionCategory.getId(),
            DocumentService.DEFAULT_TENANT_ID,
            "Test Tenant Document Definition");

    documentService.createDocumentDefinition(testTenantDocumentDefinition);

    DuplicateDocumentDefinitionException duplicateDocumentDefinitionException =
        assertThrows(
            DuplicateDocumentDefinitionException.class,
            () -> {
              documentService.createDocumentDefinition(testTenantDocumentDefinition);
            });

    retrievedDocumentDefinition =
        documentService.getDocumentDefinition(testTenantDocumentDefinition.getId());

    compareDocumentDefinitions(testTenantDocumentDefinition, retrievedDocumentDefinition);

    DocumentDefinition testNoCategoryDocumentDefinition =
        new DocumentDefinition(
            "test_no_category_document_definition_" + System.currentTimeMillis(),
            "Test No Category Document Definition");

    documentService.createDocumentDefinition(testNoCategoryDocumentDefinition);

    retrievedDocumentDefinition =
        documentService.getDocumentDefinition(testNoCategoryDocumentDefinition.getId());

    compareDocumentDefinitions(testNoCategoryDocumentDefinition, retrievedDocumentDefinition);

    documentService.deleteDocumentDefinition(testNoCategoryDocumentDefinition.getId());

    documentService.deleteDocumentDefinition(testTenantDocumentDefinition.getId());

    documentService.deleteDocumentDefinition(testSharedDocumentDefinition.getId());

    DocumentDefinitionNotFoundException documentDefinitionNotFoundException =
        assertThrows(
            DocumentDefinitionNotFoundException.class,
            () -> {
              documentService.getDocumentDefinition(testSharedDocumentDefinition.getId());
            });

    documentService.deleteDocumentDefinitionCategory(testTenantDocumentDefinitionCategory.getId());

    documentService.deleteDocumentDefinitionCategory(testSharedDocumentDefinitionCategory.getId());

    DocumentDefinitionCategoryNotFoundException documentDefinitionCategoryNotFoundException =
        assertThrows(
            DocumentDefinitionCategoryNotFoundException.class,
            () -> {
              documentService.getDocumentDefinitionCategory(
                  testSharedDocumentDefinitionCategory.getId());
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
}
