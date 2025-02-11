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

import digital.inception.operations.model.UpdateDocumentRequest;
import digital.inception.operations.service.DocumentService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The <b>ValidUpdateDocumentRequestValidator</b> class implements the custom constraint validator
 * for validating a request to update a document.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidUpdateDocumentRequestValidator
    implements ConstraintValidator<ValidUpdateDocumentRequest, UpdateDocumentRequest> {

  /** The Document Service */
  private final DocumentService documentService;

  /**
   * Constructs a new <b>ValidUpdateDocumentRequestValidator</b>.
   *
   * @param documentService the Document Service
   */
  @Autowired
  public ValidUpdateDocumentRequestValidator(DocumentService documentService) {
    this.documentService = documentService;
  }

  /** Constructs a new <b>ValidUpdateDocumentRequestValidator</b>. */
  public ValidUpdateDocumentRequestValidator() {
    this.documentService = null;
  }

  @Override
  public void initialize(ValidUpdateDocumentRequest constraintAnnotation) {}

  @Override
  public boolean isValid(
      UpdateDocumentRequest updateDocumentRequest,
      ConstraintValidatorContext constraintValidatorContext) {
    if (updateDocumentRequest != null) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {

        // TODO: IMPLEMENT VALIDATION

      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the update document request", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
