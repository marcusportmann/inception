/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.banking.customer;

import digital.inception.core.validation.InvalidArgumentException;
import digital.inception.core.validation.ValidationError;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <b>CustomerService</b> class provides the Customer Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class CustomerService implements ICustomerService {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The Individual Customer Repository. */
  private final IndividualCustomerRepository individualCustomerRepository;

  /** The JSR-303 validator. */
  private final Validator validator;

  /**
   * Constructs a new <b>CustomerService</b>.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-303 validator
   * @param individualCustomerRepository the Individual Customer Repository
   */
  public CustomerService(
      ApplicationContext applicationContext,
      Validator validator,
      IndividualCustomerRepository individualCustomerRepository) {
    this.applicationContext = applicationContext;
    this.validator = validator;
    this.individualCustomerRepository = individualCustomerRepository;
  }

  /**
   * Create the new individual customer.
   *
   * @param individualCustomer the individual customer
   */
  @Override
  @Transactional
  public void createIndividualCustomer(IndividualCustomer individualCustomer)
      throws InvalidArgumentException, DuplicateIndividualCustomerException,
          CustomerServiceException {
    if (individualCustomer == null) {
      throw new InvalidArgumentException("individualCustomer");
    }

    Set<ConstraintViolation<IndividualCustomer>> constraintViolations =
        validator.validate(individualCustomer);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "individualCustomer", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (individualCustomerRepository.existsById(individualCustomer.getId())) {
        throw new DuplicateIndividualCustomerException(individualCustomer.getId());
      }

      individualCustomerRepository.saveAndFlush(individualCustomer);
    } catch (DuplicateIndividualCustomerException e) {
      throw e;
    } catch (Throwable e) {
      throw new CustomerServiceException(
          "Failed to create the individual customer (" + individualCustomer.getId() + ")", e);
    }
  }

  /**
   * Delete the individual customer.
   *
   * @param individualCustomerId the Universally Unique Identifier (UUID) for the individual
   *     customer
   */
  @Override
  @Transactional
  public void deleteIndividualCustomer(UUID individualCustomerId)
      throws InvalidArgumentException, IndividualCustomerNotFoundException,
          CustomerServiceException {
    if (individualCustomerId == null) {
      throw new InvalidArgumentException("individualCustomerId");
    }

    try {
      if (!individualCustomerRepository.existsById(individualCustomerId)) {
        throw new IndividualCustomerNotFoundException(individualCustomerId);
      }

      individualCustomerRepository.deleteById(individualCustomerId);
    } catch (IndividualCustomerNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CustomerServiceException(
          "Failed to delete the individual customer (" + individualCustomerId + ")", e);
    }
  }

  /**
   * Retrieve the individual customer.
   *
   * @param individualCustomerId the Universally Unique Identifier (UUID) for the individual
   *     customer
   * @return the individual customer
   */
  @Override
  public IndividualCustomer getIndividualCustomer(UUID individualCustomerId)
      throws InvalidArgumentException, IndividualCustomerNotFoundException,
          CustomerServiceException {
    if (individualCustomerId == null) {
      throw new InvalidArgumentException("individualCustomerId");
    }

    try {
      Optional<IndividualCustomer> individualCustomerOptional =
          individualCustomerRepository.findById(individualCustomerId);

      if (individualCustomerOptional.isPresent()) {
        return individualCustomerOptional.get();
      } else {
        throw new IndividualCustomerNotFoundException(individualCustomerId);
      }
    } catch (IndividualCustomerNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CustomerServiceException(
          "Failed to retrieve the individual customer (" + individualCustomerId + ")", e);
    }
  }

  /**
   * Validate the individual customer.
   *
   * @param individualCustomer the individual customer
   * @return the constraint violations for the individual customer
   */
  public Set<ConstraintViolation<IndividualCustomer>> validateIndividualCustomer(
      IndividualCustomer individualCustomer) {
    return validator.validate(individualCustomer);
  }
}
