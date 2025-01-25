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

package digital.inception.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * The <b>AbstractServiceBase</b> class provides an abstract base class that Spring service
 * implementations can extend to access common functionality.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused"})
public abstract class AbstractServiceBase {

  /** The logger for the derived class. */
  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** Is debugging enabled for the Inception Framework? */
  private final boolean inDebugMode;

  /**
   * The Jackson Object Mapper, which provides functionality for reading and writing JSON, either to
   * and from basic POJOs (Plain Old Java Objects), or to and from a general-purpose JSON Tree Model
   * (JsonNode), as well as related functionality for performing conversions.
   */
  private final ObjectMapper objectMapper;

  /** The JSR-380 validator. */
  private final Validator validator;

  /**
   * Constructs a <b>AbstractServiceBase</b>.
   *
   * @param applicationContext the Spring application context
   */
  protected AbstractServiceBase(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;

    // Check if debugging is enabled for the Inception Framework
    boolean inDebugMode = false;
    try {
      if (StringUtils.hasText(
          applicationContext.getEnvironment().getProperty("inception.debug.enabled"))) {
        inDebugMode =
            Boolean.parseBoolean(
                applicationContext.getEnvironment().getProperty("inception.debug.enabled"));
      }
    } catch (Throwable ignored) {
    }
    this.inDebugMode = inDebugMode;

    try {
      objectMapper = applicationContext.getBean(ObjectMapper.class);
    } catch (Throwable e) {
      throw new BeanCreationException(
          "Failed to retrieve the Jackson Object Mapper from the Spring application context", e);
    }

    /*
     * Register the InceptionDateTimeModule module dynamically with the Jackson Object Mapper if not
     * already registered
     */
    try {
      // Check if the InceptionDateTimeModule module is already registered
      boolean isModuleRegistered =
          objectMapper.getRegisteredModuleIds().contains("InceptionDateTimeModule");

      if (!isModuleRegistered) {
        // Check if the class for the InceptionDateTimeModule module is on the classpath
        Class<?> moduleClass = Class.forName("digital.inception.json.InceptionDateTimeModule");

        // Dynamically instantiate and register InceptionDateTimeModule the module
        Object moduleInstance = moduleClass.getDeclaredConstructor().newInstance();

        if (moduleInstance instanceof com.fasterxml.jackson.databind.Module) {
          objectMapper.registerModule((com.fasterxml.jackson.databind.Module) moduleInstance);
          log.info(
              "Successfully registered the InceptionDateTimeModule module with the Jackson Object Mapper");
        }
      }
    } catch (ClassNotFoundException e) {
      log.warn("The InceptionDateTimeModule class was not found on the classpath and could not be registered with the Jackson Object Mapper");
    } catch (Exception e) {
      throw new BeanCreationException(
          "Failed to dynamically register the InceptionDateTimeModule module with the Jackson Object Mapper",
          e);
    }

    try {
      validator = applicationContext.getBean(Validator.class);
    } catch (Throwable e) {
      throw new BeanCreationException(
          "Failed to retrieve the JSR-380 validator from the Spring application context", e);
    }
  }

  /**
   * Returns the Spring application context.
   *
   * @return the Spring application context
   */
  protected ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * Returns the Jackson Object Mapper.
   *
   * <p>This provides functionality for reading and writing JSON, either to and from basic POJOs
   * (Plain Old Java Objects), or to and from a general-purpose JSON Tree Model (JsonNode), as well
   * as related functionality for performing conversions.
   *
   * @return the Jackson Object Mapper
   */
  protected ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  /**
   * Returns the JSR-380 validator.
   *
   * @return the JSR-380 validator
   */
  protected Validator getValidator() {
    return validator;
  }

  /**
   * Returns whether debugging is enabled for the Inception Framework.
   *
   * @return <b>true</b> if debugging is enabled for the Inception Framework or <b>false</b>
   *     otherwise
   */
  protected boolean inDebugMode() {
    return inDebugMode;
  }

  /**
   * Validates the provided argument of any type with the specified name using the JSR-380
   * validator.
   *
   * @param <T> the type of the argument to validate
   * @param argument the argument to validate
   * @param argumentName the name of the argument (for error messages)
   * @throws InvalidArgumentException if the argument is null or validation errors occur
   */
  protected <T> void validateArgument(T argument, String argumentName)
      throws InvalidArgumentException {
    if (argument == null) {
      throw new InvalidArgumentException(argumentName);
    }

    Set<ConstraintViolation<T>> constraintViolations = validator.validate(argument);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          argumentName, ValidationError.toValidationErrors(constraintViolations));
    }
  }
}
