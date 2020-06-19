/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.sample.model;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.validation.ValidationError;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleService</code> class provides the Sample Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class SampleService implements ISampleService {

  private static final String VERSION = "1.0.0";

  /* Entity Manager */
  @PersistenceContext(unitName = "applicationPersistenceUnit")
  private EntityManager entityManager;

  /** The JSR-303 validator. */
  private Validator validator;

  /**
   * Constructs a new <code>SampleService</code>.
   *
   * @param validator the JSR-303 validator
   */
  public SampleService(Validator validator) {
    this.validator = validator;
  }

  /** Add the data. */
  @Override
  @Transactional
  public void addData() throws SampleServiceException {
    try {
      Data newData = new Data();
      newData.setId(666);
      newData.setName("New Name");
      newData.setStringValue("New String Value");
      newData.setDateValue(LocalDate.now());
      newData.setTimestampValue(LocalDateTime.now());

      entityManager.persist(newData);
    } catch (Throwable e) {
      throw new SampleServiceException("Failed to add the data", e);
    }
  }

  /**
   * Add the data.
   *
   * @param data the data
   */
  @Override
  @Transactional
  public void addData(Data data) throws SampleServiceException {
    try {
      entityManager.persist(data);
    } catch (Throwable e) {
      throw new SampleServiceException("Failed to add the data", e);
    }
  }

  /**
   * Returns the data.
   *
   * @return the data
   */
  @Override
  @Transactional
  public List<Data> getAllData() throws SampleServiceException {
    try {
      TypedQuery<Data> query = entityManager.createQuery("SELECT d FROM Data d", Data.class);

      return query.getResultList();
    } catch (Throwable e) {
      throw new SampleServiceException("Failed to retrieve the data", e);
    }
  }

  /**
   * Returns the data.
   *
   * @param id the ID uniquely identifying the data
   * @return the data
   */
  @Override
  @Transactional
  public Data getData(long id) throws SampleServiceException {
    try {
      TypedQuery<Data> query =
          entityManager.createQuery("SELECT d FROM Data d WHERE ID=:id", Data.class);

      query.setParameter("id", id);

      List<Data> list = query.getResultList();

      if (list.size() > 0) {
        return list.get(0);
      } else {
        return null;
      }
    } catch (Throwable e) {
      throw new SampleServiceException("Failed to retrieve the data (" + id + ")", e);
    }
  }

  /** The test method. */
  @Override
  public void testMethod() {
    System.out.println("[DEBUG] Hello world from the test method!!!");
  }

  /**
   * Validate the data.
   *
   * @return the validation errors
   */
  @Override
  public List<ValidationError> validate(Data data) throws SampleServiceException {
    try {
      Set<ConstraintViolation<Data>> constraintViolations = validator.validate(data);

      List<ValidationError> errors = new ArrayList<>();

      for (ConstraintViolation<Data> constraintViolation : constraintViolations) {
        errors.add(new ValidationError(constraintViolation));
      }

      return errors;
    } catch (Throwable e) {
      throw new SampleServiceException("Failed to validate the data", e);
    }
  }
}
