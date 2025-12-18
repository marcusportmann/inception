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

import digital.inception.operations.model.Workflow;
import digital.inception.operations.service.WorkflowService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The {@code ValidWorkflowValidator} class implements the custom constraint validator for
 * validating a workflow.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidWorkflowValidator implements ConstraintValidator<ValidWorkflow, Workflow> {

  /** The Workflow Service */
  private final WorkflowService workflowService;

  /**
   * Constructs a new {@code ValidWorkflowValidator}.
   *
   * @param workflowService the Workflow Service
   */
  @Autowired
  public ValidWorkflowValidator(WorkflowService workflowService) {
    this.workflowService = workflowService;
  }

  /** Constructs a new {@code ValidWorkflowValidator}. */
  public ValidWorkflowValidator() {
    this.workflowService = null;
  }

  @Override
  public void initialize(ValidWorkflow constraintAnnotation) {}

  @Override
  public boolean isValid(Workflow workflow, ConstraintValidatorContext constraintValidatorContext) {
    if (workflowService != null) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        if (!workflowService.workflowDefinitionVersionExists(
            workflow.getDefinitionId(), workflow.getDefinitionVersion())) {
          hibernateConstraintValidatorContext
              .addMessageParameter("workflowDefinitionId", workflow.getDefinitionId())
              .addMessageParameter("workflowDefinitionVersion", workflow.getDefinitionVersion())
              .buildConstraintViolationWithTemplate(
                  "{digital.inception.operations.constraint.ValidWorkflow.invalidWorkflowDefinitionVersion.message}")
              .addPropertyNode("workflowDefinitionId")
              .addPropertyNode("workflowDefinitionVersion")
              .addConstraintViolation();

          isValid = false;
        }
      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the workflow", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
