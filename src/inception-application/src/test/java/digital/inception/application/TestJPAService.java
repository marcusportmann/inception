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

package digital.inception.application;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestJPAService</code> class provides the Test JPA Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("JpaQlInspection")
public class TestJPAService
  implements ITestJPAService
{
  /* Entity Manager */
  @PersistenceContext(unitName = "applicationPersistenceUnit")
  private EntityManager entityManager;

  /* Transaction Manager */
  @Autowired
  private PlatformTransactionManager transactionManager;

  /**
   * Create the test data.
   *
   * @param testData the test data
   */
  @Transactional
  public void createTestData(TestData testData)
    throws TestJPAServiceException
  {
    try
    {
      if (!entityManager.contains(testData))
      {
        testData = entityManager.merge(testData);
        entityManager.flush();
        entityManager.detach(testData);
      }
    }
    catch (Throwable e)
    {
      throw new TestJPAServiceException("Failed to create the test data with ID ("
          + testData.getId() + ")", e);
    }
  }

  /**
   * Create the test data in a new transaction.
   *
   * @param testData the test data
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createTestDataInNewTransaction(TestData testData)
    throws TestJPAServiceException
  {
    try
    {
      if (!entityManager.contains(testData))
      {
        testData = entityManager.merge(testData);
        entityManager.flush();
        entityManager.detach(testData);
      }
    }
    catch (Throwable e)
    {
      throw new TestJPAServiceException("Failed to create the test data with ID ("
          + testData.getId() + ")", e);
    }
  }

  /**
   * Create the test data in a new transaction with a checked exception.
   *
   * @param testData the test data
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createTestDataInNewTransactionWithCheckedException(TestData testData)
    throws TestJPAServiceException
  {
    createTestData(testData);

    throw new TestJPAServiceException("Failed with a checked exception in a new transaction");
  }

  /**
   * Create the test data in a new transaction with a runtime exception.
   *
   * @param testData the test data
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createTestDataInNewTransactionWithRuntimeException(TestData testData)
    throws TestJPAServiceException
  {
    createTestData(testData);

    throw new RuntimeException("Failed with a runtime exception in a new transaction");
  }

  /**
   * Create the test data with a checked exception.
   *
   * @param testData the test data
   */
  @Transactional
  public void createTestDataWithCheckedException(TestData testData)
    throws TestJPAServiceException
  {
    createTestData(testData);

    throw new TestJPAServiceException("Failed with a checked exception in an existing transaction");
  }

  /**
   * Create the test data with a runtime exception.
   *
   * @param testData the test data
   */
  @Transactional
  public void createTestDataWithRuntimeException(TestData testData)
    throws TestJPAServiceException
  {
    createTestData(testData);

    throw new RuntimeException("Failed with a runtime exception in an existing transaction");
  }

  /**
   * Retrieve the test data.
   *
   * @param id the ID
   *
   * @return the test data or <code>null</code> if the test data cannot be found
   */
  @Transactional
  public TestData getTestData(String id)
    throws TestJPAServiceException
  {
    try
    {
      String getTestDataSQL = "SELECT td FROM TestData td WHERE td.id = :id";

      TypedQuery<TestData> query = entityManager.createQuery(getTestDataSQL, TestData.class);

      query.setParameter("id", id);

      List<TestData> testData = query.getResultList();

      if (testData.size() == 0)
      {
        return null;
      }
      else
      {
        return testData.get(0);
      }
    }
    catch (Throwable e)
    {
      throw new TestJPAServiceException("Failed to retrieve the test data (" + id + ")", e);
    }
  }

  /**
   * Retrieve the test data without a transaction.
   *
   * @param id the ID
   *
   * @return the test data or <code>null</code> if the test data cannot be found
   */
  public TestData getTestDataWithoutTransaction(String id)
    throws TestJPAServiceException
  {
    try
    {
      String getTestDataSQL = "SELECT td FROM TestData td WHERE td.id = :id";

      TypedQuery<TestData> query = entityManager.createQuery(getTestDataSQL, TestData.class);

      query.setParameter("id", id);

      List<TestData> testData = query.getResultList();

      if (testData.size() == 0)
      {
        return null;
      }
      else
      {
        return testData.get(0);
      }
    }
    catch (Throwable e)
    {
      throw new TestJPAServiceException("Failed to retrieve the test data (" + id + ")", e);
    }

  }
}
