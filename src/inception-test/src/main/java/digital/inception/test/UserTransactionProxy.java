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

package digital.inception.test;

//~--- non-JDK imports --------------------------------------------------------

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserTransactionProxy</code> class provides a proxy that tracks the Java Transaction
 * (JTA) API transactions associated with the current thread and managed by a
 * <code>javax.transaction.UserTransaction</code> implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused"})
public class UserTransactionProxy
    implements UserTransaction {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UserTransactionProxy.class);

  /**
   * The stack traces for the active transactions associated with the current thread.
   */
  private static ThreadLocal<Map<Transaction, StackTraceElement[]>> activeTransactionStackTraces =
      ThreadLocal.withInitial(ConcurrentHashMap::new);

  /**
   * The JTA user transaction.
   */
  private UserTransaction userTransaction;

  /**
   * The JTA transaction manager.
   */
  private TransactionManager transactionManager;

  /**
   * Constructs a new <code>UserTransactionProxy</code>.
   *
   * @param userTransaction    the JTA user transaction
   * @param transactionManager the JTA transaction manager
   */
  public UserTransactionProxy(UserTransaction userTransaction,
      TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
    this.userTransaction = userTransaction;
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
  public void begin()
      throws NotSupportedException, SystemException {
    try {
      userTransaction.begin();
    } finally {
      Transaction afterTransaction = getCurrentTransaction();

      if (afterTransaction != null) {
        getActiveTransactionStackTraces().put(afterTransaction, Thread.currentThread()
            .getStackTrace());
      }
    }
  }

  @Override
  public void commit()
      throws RollbackException, HeuristicMixedException, HeuristicRollbackException,
      SecurityException, IllegalStateException, SystemException {
    Transaction beforeTransaction = getCurrentTransaction();

    try {
      userTransaction.commit();
    } finally {
      Transaction afterTransaction = getCurrentTransaction();

      if ((beforeTransaction != null) && (afterTransaction == null)) {
        getActiveTransactionStackTraces().remove(beforeTransaction);
      }
    }
  }

  @Override
  public int getStatus()
      throws SystemException {
    return userTransaction.getStatus();
  }

  @Override
  public void rollback()
      throws IllegalStateException, SecurityException, SystemException {
    Transaction beforeTransaction = getCurrentTransaction();

    try {
      userTransaction.rollback();
    } finally {
      Transaction afterTransaction = getCurrentTransaction();

      if ((beforeTransaction != null) && (afterTransaction == null)) {
        getActiveTransactionStackTraces().remove(beforeTransaction);
      }
    }
  }

  @Override
  public void setRollbackOnly()
      throws IllegalStateException, SystemException {
    userTransaction.setRollbackOnly();
  }

  @Override
  public void setTransactionTimeout(int i)
      throws SystemException {
    userTransaction.setTransactionTimeout(i);
  }

  /**
   * Returns the current transaction.
   *
   * @return the current transaction or <code>null</code> if there is no current transaction
   */
  private Transaction getCurrentTransaction() {
    try {
      return transactionManager.getTransaction();
    } catch (Throwable e) {
      logger.error("Failed to retrieve the current transaction", e);

      return null;
    }
  }
}
