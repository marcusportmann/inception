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

package digital.inception.executor.constraint;

import digital.inception.executor.model.Task;
import digital.inception.executor.model.TaskTypeNotFoundException;
import digital.inception.executor.service.ExecutorService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The <b>ValidTaskValidator</b> class implements the custom constraint validator for validating a
 * task.
 *
 * <p>NOTE: The <b>@Autowired</b> constructor is not used when Spring Boot automatically invokes the
 * JSR 380 validation when persisting an entity. Instead, the default constructor is invoked when
 * the validator is initialized by Hibernate. As a result the custom validation that requires the
 * injected services will not be executed.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidTaskValidator implements ConstraintValidator<ValidTask, Task> {

  /** The Executor Service. */
  private final ExecutorService executorService;

  /**
   * Constructs a new <b>ValidTaskValidator</b>.
   *
   * @param executorService the Executor Service
   */
  @Autowired
  public ValidTaskValidator(ExecutorService executorService) {
    this.executorService = executorService;
  }

  /** Constructs a new <b>ValidTaskValidator</b>. */
  public ValidTaskValidator() {
    executorService = null;
  }

  @Override
  public boolean isValid(Task task, ConstraintValidatorContext constraintValidatorContext) {

    if (executorService != null) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        if (StringUtils.hasText(task.getType())) {
          try {
            executorService.getTaskType(task.getType());
          } catch (TaskTypeNotFoundException e) {
            hibernateConstraintValidatorContext
                .addMessageParameter("type", task.getType())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.executor.constraint.ValidTask.invalidType.message}")
                .addPropertyNode("type")
                .addConstraintViolation();
          }
        }
      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the task", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
