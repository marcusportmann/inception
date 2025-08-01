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

package demo.service;

import demo.model.Data;
import demo.model.ReactiveData;
import demo.persistence.r2dbc.ReactiveDataRepository;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.service.AbstractServiceBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

/**
 * The {@code DataServiceImpl} class provides the Demo Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class DataServiceImpl extends AbstractServiceBase implements DataService {

  /** The Reactive Data Repository. */
  private final ReactiveDataRepository reactiveDataRepository;

  /* Entity Manager */
  @PersistenceContext(unitName = "application")
  private EntityManager entityManager;

  /**
   * Constructs a new {@code DataServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param reactiveDataRepository the Reactive Data Repository
   */
  public DataServiceImpl(
      ApplicationContext applicationContext, ReactiveDataRepository reactiveDataRepository) {
    super(applicationContext);

    this.reactiveDataRepository = reactiveDataRepository;
  }

  @Override
  @Transactional
  public void createData(Data data) throws ServiceUnavailableException {
    try {
      entityManager.persist(data);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to add the data", e);
    }
  }

  @Override
  @Transactional
  public void createSampleData() throws ServiceUnavailableException {
    try {
      Data newData = new Data();
      newData.setId(666);
      newData.setBooleanValue(true);
      newData.setDateValue(LocalDate.now());
      newData.setIntegerValue(777);
      newData.setStringValue("New String Value");
      newData.setTimestampValue(LocalDateTime.now());
      newData.setTimestampWithTimeZoneValue(OffsetDateTime.now());

      entityManager.persist(newData);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to add the data", e);
    }
  }

  @Override
  @Transactional
  public List<Data> getAllData() throws ServiceUnavailableException {
    try {
      TypedQuery<Data> query = entityManager.createQuery("SELECT d FROM Data d", Data.class);

      return query.getResultList();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the data", e);
    }
  }

  @Override
  public Flux<ReactiveData> getAllReactiveData() throws ServiceUnavailableException {
    return reactiveDataRepository.findAll();
  }

  @Override
  @Transactional
  public Data getData(long id) throws ServiceUnavailableException {
    try {
      TypedQuery<Data> query =
          entityManager.createQuery("SELECT d FROM Data d WHERE d.id=:id", Data.class);

      query.setParameter("id", id);

      List<Data> list = query.getResultList();

      if (!list.isEmpty()) {
        return list.getFirst();
      } else {
        return null;
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the data (" + id + ")", e);
    }
  }

  @Override
  public void validateData(Data data) throws InvalidArgumentException, ServiceUnavailableException {
    try {
      validateArgument("data", data);
    } catch (InvalidArgumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to validate the data", e);
    }
  }
}
