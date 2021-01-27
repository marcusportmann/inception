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

package demo.model;

import digital.inception.core.validation.ValidationError;
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

/**
 * The <code>DemoService</code> class provides the Demo Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class DataService implements IDataService {

  /** The JSR-303 validator. */
  private final Validator validator;

  /* Entity Manager */
  @PersistenceContext(unitName = "applicationPersistenceUnit")
  private EntityManager entityManager;

  /**
   * Constructs a new <code>DemoService</code>.
   *
   * @param validator the JSR-303 validator
   */
  public DataService(Validator validator) {
    this.validator = validator;
  }

  /**
   * Add the data.
   *
   * @param data the data
   */
  @Override
  @Transactional
  public void createData(Data data) throws DemoServiceException {
    try {
      entityManager.persist(data);
    } catch (Throwable e) {
      throw new DemoServiceException("Failed to add the data", e);
    }
  }

  /** Add the data. */
  @Override
  @Transactional
  public void createSampleData() throws DemoServiceException {
    try {
      Data newData = new Data();
      newData.setId(666);
      newData.setStringValue("New String Value");
      newData.setDateValue(LocalDate.now());
      newData.setTimestampValue(LocalDateTime.now());

      entityManager.persist(newData);
    } catch (Throwable e) {
      throw new DemoServiceException("Failed to add the data", e);
    }
  }

  /**
   * Returns the data.
   *
   * @return the data
   */
  @Override
  @Transactional
  public List<Data> getAllData() throws DemoServiceException {
    try {
      TypedQuery<Data> query = entityManager.createQuery("SELECT d FROM Data d", Data.class);

      return query.getResultList();
    } catch (Throwable e) {
      throw new DemoServiceException("Failed to retrieve the data", e);
    }
  }

  /**
   * Returns the data.
   *
   * @param id the ID for the data
   * @return the data
   */
  @Override
  @Transactional
  public Data getData(long id) throws DemoServiceException {
    try {
      TypedQuery<Data> query =
          entityManager.createQuery("SELECT d FROM Data d WHERE d.id=:id", Data.class);

      query.setParameter("id", id);

      List<Data> list = query.getResultList();

      if (list.size() > 0) {
        return list.get(0);
      } else {
        return null;
      }
    } catch (Throwable e) {
      throw new DemoServiceException("Failed to retrieve the data (" + id + ")", e);
    }
  }

  /**
   * Validate the data.
   *
   * @return the validation errors
   */
  @Override
  public List<ValidationError> validateData(Data data) throws DemoServiceException {
    try {
      Set<ConstraintViolation<Data>> constraintViolations = validator.validate(data);

      List<ValidationError> errors = new ArrayList<>();

      for (ConstraintViolation<Data> constraintViolation : constraintViolations) {
        errors.add(new ValidationError(constraintViolation));
      }

      return errors;
    } catch (Throwable e) {
      throw new DemoServiceException("Failed to validate the data", e);
    }
  }
}
