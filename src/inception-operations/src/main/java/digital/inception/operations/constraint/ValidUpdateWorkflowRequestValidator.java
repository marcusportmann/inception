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

import digital.inception.operations.model.UpdateWorkflowRequest;
import digital.inception.operations.service.WorkflowService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The <b>ValidUpdateWorkflowRequestValidator</b> class implements the custom constraint validator
 * for validating a request to update a workflow.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidUpdateWorkflowRequestValidator
    implements ConstraintValidator<ValidUpdateWorkflowRequest, UpdateWorkflowRequest> {

  /** The Workflow Service */
  private final WorkflowService workflowService;

  /**
   * Constructs a new <b>ValidUpdateWorkflowRequestValidator</b>.
   *
   * @param workflowService the Workflow Service
   */
  @Autowired
  public ValidUpdateWorkflowRequestValidator(WorkflowService workflowService) {
    this.workflowService = workflowService;
  }

  /** Constructs a new <b>ValidUpdateWorkflowRequestValidator</b>. */
  public ValidUpdateWorkflowRequestValidator() {
    this.workflowService = null;
  }

  @Override
  public void initialize(ValidUpdateWorkflowRequest constraintAnnotation) {}

  @Override
  public boolean isValid(
      UpdateWorkflowRequest updateWorkflowRequest,
      ConstraintValidatorContext constraintValidatorContext) {
    if (updateWorkflowRequest != null) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {

        // TODO: IMPLEMENT VALIDATION

      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the update workflow request", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
