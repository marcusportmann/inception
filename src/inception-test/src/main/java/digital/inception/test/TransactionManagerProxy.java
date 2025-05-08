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

package digital.inception.test;

import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.InvalidTransactionException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code TransactionManagerProxy} class provides a proxy that tracks the Java Transaction (JTA)
 * API transactions associated with the current thread and managed by a <b>
 * javax.transaction.TransactionManager</b> implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused"})
public class TransactionManagerProxy implements TransactionManager {

  /** The stack traces for the active transactions associated with the current thread. */
  private static final ThreadLocal<Map<Transaction, StackTraceElement[]>>
      activeTransactionStackTraces = ThreadLocal.withInitial(ConcurrentHashMap::new);

  /** The JTA transaction manager. */
  private final TransactionManager transactionManager;

  /**
   * Creates a new {@code TransactionManagerProxy} instance.
   *
   * @param transactionManager the JTA transaction manager
   */
  public TransactionManagerProxy(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  /**
   * Returns the active transaction stack traces for the current thread.
   *
   * @return the active transaction stack traces for the current thread
   */
  static Map<Transaction, StackTraceElement[]> getActiveTransactionStackTraces() {
    return activeTransactionStackTraces.get();
  }

  @Override
  public void begin() throws NotSupportedException, SystemException {
    try {
      transactionManager.begin();
    } finally {
      Transaction afterTransaction = getTransaction();

      if (afterTransaction != null) {
        getActiveTransactionStackTraces()
            .put(afterTransaction, Thread.currentThread().getStackTrace());
      }
    }
  }

  @Override
  public void commit()
      throws RollbackException,
          HeuristicMixedException,
          HeuristicRollbackException,
          SecurityException,
          IllegalStateException,
          SystemException {
    Transaction beforeTransaction = getTransaction();

    try {
      transactionManager.commit();
    } finally {
      Transaction afterTransaction = getTransaction();

      if ((beforeTransaction != null) && (afterTransaction == null)) {
        getActiveTransactionStackTraces().remove(beforeTransaction);
      }
    }
  }

  @Override
  public int getStatus() throws SystemException {
    return transactionManager.getStatus();
  }

  @Override
  public Transaction getTransaction() throws SystemException {
    return transactionManager.getTransaction();
  }

  @Override
  public void resume(Transaction transaction)
      throws InvalidTransactionException, IllegalStateException, SystemException {
    transactionManager.resume(transaction);
  }

  @Override
  public void rollback() throws IllegalStateException, SecurityException, SystemException {
    Transaction beforeTransaction = getTransaction();

    try {
      transactionManager.rollback();
    } finally {
      Transaction afterTransaction = getTransaction();

      if ((beforeTransaction != null) && (afterTransaction == null)) {
        getActiveTransactionStackTraces().remove(beforeTransaction);
      }
    }
  }

  @Override
  public void setRollbackOnly() throws IllegalStateException, SystemException {
    /*
     * This check to confirm that we have a valid transaction was added to handle the issue
     * where the Hibernate JPA implementation would try to roll back a transaction even if one
     * didn't exist when a non-hibernate exception was thrown.
     */
    if (getTransaction() != null) {
      transactionManager.setRollbackOnly();
    }
  }

  @Override
  public void setTransactionTimeout(int i) throws SystemException {
    transactionManager.setTransactionTimeout(i);
  }

  @Override
  public Transaction suspend() throws SystemException {
    return transactionManager.suspend();
  }
}
