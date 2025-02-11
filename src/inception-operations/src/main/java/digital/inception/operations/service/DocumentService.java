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

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.ValidationError;
import digital.inception.operations.model.Case;
import digital.inception.operations.model.CaseDefinition;
import digital.inception.operations.model.CaseDefinitionId;
import digital.inception.operations.model.CaseDefinitionNotFoundException;
import digital.inception.operations.model.CaseDefinitionVersionNotFoundException;
import digital.inception.operations.model.CaseNotFoundException;
import digital.inception.operations.model.CaseStatus;
import digital.inception.operations.model.CreateCaseRequest;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.DocumentDefinitionNotFoundException;
import digital.inception.operations.model.DocumentNotFoundException;
import digital.inception.operations.model.DuplicateCaseDefinitionException;
import digital.inception.operations.model.DuplicateDocumentDefinitionException;
import digital.inception.operations.model.DuplicateDocumentException;
import digital.inception.operations.persistence.CaseDefinitionRepository;
import digital.inception.operations.persistence.DocumentDefinitionRepository;
import digital.inception.operations.store.ICaseStore;
import digital.inception.operations.store.IDocumentStore;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


Change this to work the same as the Case Service where we use CreateDocumentRquest and UpdateDocumentRequest

Figure out how to do this with CaseDocument???


/**
 * The <b>DocumentService</b> class provides the Document Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class DocumentService implements IDocumentService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CaseService.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The Document Store. */
  private final IDocumentStore documentStore;

  /** The Document Definition Repository. */
  private final DocumentDefinitionRepository documentDefinitionRepository;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <b>CaseService</b>.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-380 validator
   * @param documentStore the Document Store
   * @param documentDefinitionRepository the Document Definition Repository
   */
  public DocumentService(
      ApplicationContext applicationContext,
      Validator validator,
      IDocumentStore documentStore,
      DocumentDefinitionRepository documentDefinitionRepository) {
    this.applicationContext = applicationContext;
    this.validator = validator;
    this.documentStore = documentStore;
    this.documentDefinitionRepository = documentDefinitionRepository;
  }

  @Override
  public Document createDocument(Document document, String createdBy)
      throws InvalidArgumentException,
      DuplicateDocumentException,
      ServiceUnavailableException {
    XXX

    validateDocument(document);

    calculateDocumentHash(document);

    return documentStore.createDocument(document);
  }

  @Override
  public void createDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
      DuplicateDocumentDefinitionException,
      ServiceUnavailableException {
    validateDocumentDefinition(documentDefinition);

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
  public void deleteDocument(UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    if (documentId == null) {
      throw new InvalidArgumentException("documentId");
    }

    documentStore.deleteDocument(documentId);
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
  public Document getDocument(UUID documentId)
      throws InvalidArgumentException, DocumentNotFoundException, ServiceUnavailableException {
    if (documentId == null) {
      throw new InvalidArgumentException("documentId");
    }

    return documentStore.getDocument(documentId);
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
  public Document updateDocument(Document document, String updatedBy)
      throws InvalidArgumentException,
      CaseNotFoundException,
      DocumentNotFoundException,
      ServiceUnavailableException {
    validateDocument(document);

    calculateDocumentHash(document);

    return caseStore.updateDocument(document);
  }

  @Override
  public void updateDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException,
      DocumentDefinitionNotFoundException,
      ServiceUnavailableException {
    validateDocumentDefinition(documentDefinition);

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

  private void calculateDocumentHash(Document document) throws ServiceUnavailableException {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      digest.update(document.getData());

      document.setHash(Base64.getEncoder().encodeToString(digest.digest()));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to calculate the SHA-256 hash of the data for the document ("
              + document.getId()
              + ")",
          e);
    }
  }

  private void validateCase(Case theCase)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (theCase == null) {
      throw new InvalidArgumentException("case");
    }

    Set<ConstraintViolation<Case>> constraintViolations = validator.validate(theCase);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "case", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateCaseDefinition(CaseDefinition caseDefinition)
      throws InvalidArgumentException {
    if (caseDefinition == null) {
      throw new InvalidArgumentException("caseDefinition");
    }

    Set<ConstraintViolation<CaseDefinition>> constraintViolations =
        validator.validate(caseDefinition);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "caseDefinition", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateCreateCaseRequest(CreateCaseRequest createCaseRequest)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (createCaseRequest == null) {
      throw new InvalidArgumentException("createCaseRequest");
    }

    Set<ConstraintViolation<CreateCaseRequest>> constraintViolations =
        validator.validate(createCaseRequest);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "createCaseRequest", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateDocument(Document document) throws InvalidArgumentException {
    if (document == null) {
      throw new InvalidArgumentException("document");
    }

    Set<ConstraintViolation<Document>> constraintViolations = validator.validate(document);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "document", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  private void validateDocumentDefinition(DocumentDefinition documentDefinition)
      throws InvalidArgumentException {
    if (documentDefinition == null) {
      throw new InvalidArgumentException("documentDefinition");
    }

    Set<ConstraintViolation<DocumentDefinition>> constraintViolations =
        validator.validate(documentDefinition);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "documentDefinition", ValidationError.toValidationErrors(constraintViolations));
    }
  }
}
