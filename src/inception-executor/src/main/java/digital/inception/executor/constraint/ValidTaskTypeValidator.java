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

import digital.inception.executor.model.ITaskExecutor;
import digital.inception.executor.model.TaskType;
import digital.inception.executor.service.IExecutorService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * The <b>ValidTaskTypeValidator</b> class implements the custom constraint validator for validating
 * a task type.
 *
 * <p>NOTE: The <b>@Autowired</b> constructor is not used when Spring Boot automatically invokes the
 * JSR 380 validation when persisting an entity. Instead, the default constructor is invoked when
 * the validator is initialized by Hibernate. As a result the custom validation that requires the
 * injected services will not be executed.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidTaskTypeValidator implements ConstraintValidator<ValidTaskType, TaskType> {

  /** The Executor Service. */
  private final IExecutorService executorService;

  /**
   * Constructs a new <b>ValidTaskTypeValidator</b>.
   *
   * @param executorService the Executor Service
   */
  @Autowired
  public ValidTaskTypeValidator(IExecutorService executorService) {
    this.executorService = executorService;
  }

  /** Constructs a new <b>ValidTaskTypeValidator</b>. */
  public ValidTaskTypeValidator() {
    executorService = null;
  }

  @Override
  public boolean isValid(TaskType taskType, ConstraintValidatorContext constraintValidatorContext) {

    if (executorService != null) {
      boolean isValid = true;

      // Disable the default constraint violation
      constraintValidatorContext.disableDefaultConstraintViolation();

      HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
          constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);

      try {
        if (StringUtils.hasText(taskType.getExecutorClass())) {
          try {
            Class<?> taskExecutorClass =
                Thread.currentThread()
                    .getContextClassLoader()
                    .loadClass(taskType.getExecutorClass());

            if (!ITaskExecutor.class.isAssignableFrom(taskExecutorClass)) {
              hibernateConstraintValidatorContext
                  .addMessageParameter("executorClass", taskType.getExecutorClass())
                  .buildConstraintViolationWithTemplate(
                      "{digital.inception.executor.constraint.ValidTaskType.executorClassMustImplementITaskExecutorInterface.message}")
                  .addPropertyNode("executorClass")
                  .addConstraintViolation();
              isValid = false;
            }
          } catch (ClassNotFoundException e) {
            hibernateConstraintValidatorContext
                .addMessageParameter("executorClass", taskType.getExecutorClass())
                .buildConstraintViolationWithTemplate(
                    "{digital.inception.executor.constraint.ValidTaskType.invalidExecutorClass.message}")
                .addPropertyNode("executorClass")
                .addConstraintViolation();
            isValid = false;
          }
        }
      } catch (Throwable e) {
        throw new ValidationException("Failed to validate the task type", e);
      }

      return isValid;
    } else {
      return true;
    }
  }
}
