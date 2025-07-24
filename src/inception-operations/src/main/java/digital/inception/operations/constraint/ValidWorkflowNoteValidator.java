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

import digital.inception.operations.model.WorkflowNote;
import digital.inception.operations.service.WorkflowService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The {@code ValidWorkflowNoteValidator} class implements the custom constraint validator for
 * validating a workflow note.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidWorkflowNoteValidator
    implements ConstraintValidator<ValidWorkflowNote, WorkflowNote> {

  /** The Workflow Service */
  private final WorkflowService workflowService;

  /**
   * Constructs a new {@code ValidWorkflowNoteValidator}.
   *
   * @param workflowService the Workflow Service
   */
  @Autowired
  public ValidWorkflowNoteValidator(WorkflowService workflowService) {
    this.workflowService = workflowService;
  }

  /** Constructs a new {@code ValidWorkflowNoteValidator}. */
  public ValidWorkflowNoteValidator() {
    this.workflowService = null;
  }

  @Override
  public void initialize(ValidWorkflowNote constraintAnnotation) {}

  @Override
  public boolean isValid(
      WorkflowNote workflowNote, ConstraintValidatorContext constraintValidatorContext) {
    if (workflowService != null) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        // TODO: IMPLEMENT VALIDATION

      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the workflow note", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
