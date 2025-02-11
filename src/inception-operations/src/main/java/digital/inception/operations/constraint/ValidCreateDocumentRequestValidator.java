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

import digital.inception.operations.model.CreateDocumentRequest;
import digital.inception.operations.service.DocumentService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The <b>ValidCreateDocumentRequestValidator</b> class implements the custom constraint validator
 * for validating a request to create a document.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidCreateDocumentRequestValidator
    implements ConstraintValidator<ValidCreateDocumentRequest, CreateDocumentRequest> {

  /** The Document Service */
  private final DocumentService documentService;

  /**
   * Constructs a new <b>ValidCreateDocumentRequestValidator</b>.
   *
   * @param documentService the Document Service
   */
  @Autowired
  public ValidCreateDocumentRequestValidator(DocumentService documentService) {
    this.documentService = documentService;
  }

  /** Constructs a new <b>ValidCreateDocumentRequestValidator</b>. */
  public ValidCreateDocumentRequestValidator() {
    this.documentService = null;
  }

  @Override
  public void initialize(ValidCreateDocumentRequest constraintAnnotation) {}

  @Override
  public boolean isValid(
      CreateDocumentRequest createDocumentRequest,
      ConstraintValidatorContext constraintValidatorContext) {
    if (createDocumentRequest != null) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {

        // TODO: IMPLEMENT VALIDATION

      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the create document request", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
