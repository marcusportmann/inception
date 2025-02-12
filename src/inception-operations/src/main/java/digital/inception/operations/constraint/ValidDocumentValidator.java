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

package digital.inception.operations.constraint;

import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentDefinition;
import digital.inception.operations.model.RequiredDocumentAttribute;
import digital.inception.operations.service.DocumentService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The <b>ValidDocumentValidator</b> class implements the custom constraint validator for validating
 * a document.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidDocumentValidator implements ConstraintValidator<ValidDocument, Document> {

  /** The Document Service */
  private final DocumentService documentService;

  /**
   * Constructs a new <b>ValidDocumentValidator</b>.
   *
   * @param documentService the Document Service
   */
  @Autowired
  public ValidDocumentValidator(DocumentService documentService) {
    this.documentService = documentService;
  }

  /** Constructs a new <b>ValidDocumentValidator</b>. */
  public ValidDocumentValidator() {
    this.documentService = null;
  }

  @Override
  public void initialize(ValidDocument constraintAnnotation) {}

  @Override
  public boolean isValid(Document document, ConstraintValidatorContext constraintValidatorContext) {
    if (documentService != null) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        if (documentService.documentDefinitionExists(document.getDefinitionId())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("documentDefinitionId", document.getDefinitionId())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.operations.constraint.ValidDocument.invalidDocumentDefinition.message}")
              .addPropertyNode("definitionId")
              .addConstraintViolation();

          isValid = false;
        } else {
          DocumentDefinition documentDefinition =
              documentService.getDocumentDefinition(document.getDefinitionId());

          for (RequiredDocumentAttribute requiredDocumentAttribute :
              documentDefinition.getRequiredDocumentAttributes()) {
            switch (requiredDocumentAttribute) {
              case EXPIRY_DATE -> {
                if (document.getExpiryDate() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("documentDefinitionId", document.getDefinitionId())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.operations.constraint.ValidDocument.expiryDateRequired.message}")
                      .addPropertyNode("expiryDate")
                      .addConstraintViolation();

                  isValid = false;
                }
              }
              case EXTERNAL_REFERENCE -> {
                if (!StringUtils.hasText(document.getExternalReference())) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("documentDefinitionId", document.getDefinitionId())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.operations.constraint.ValidDocument.externalReferenceRequired.message}")
                      .addPropertyNode("externalReference")
                      .addConstraintViolation();

                  isValid = false;
                }
              }
              case ISSUE_DATE -> {
                if (document.getIssueDate() == null) {
                  hibernateConstraintValidatorContext
                      .addMessageParameter("documentDefinitionId", document.getDefinitionId())
                      .buildConstraintViolationWithTemplate(
                          "{digital.inception.operations.constraint.ValidDocument.issueDateRequired.message}")
                      .addPropertyNode("issueDate")
                      .addConstraintViolation();

                  isValid = false;
                }
              }
              case null, default -> {
              }
            }
          }
        }
      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the document", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
