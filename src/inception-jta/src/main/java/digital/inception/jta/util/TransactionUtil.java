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

package digital.inception.jta.util;

import jakarta.transaction.Status;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;
import org.springframework.transaction.TransactionSystemException;

/**
 * The {@code TransactionUtil} class provides a helper class for working with transactions.
 *
 * @author Marcus Portmann
 */
public final class TransactionUtil {

  /** Private constructor to prevent instantiation. */
  private TransactionUtil() {}

  /**
   * Check whether there is an existing JTA transaction.
   *
   * @param transactionManager the JTA transaction manager
   * @return {@code true} if there is an existing JTA transaction or {@code false} otherwise
   */
  public static boolean transactionExists(TransactionManager transactionManager) {
    try {
      Transaction transaction = transactionManager.getTransaction();
      if (transaction == null) {
        return false;
      }
      int status = transaction.getStatus();

      return switch (status) {
        case Status.STATUS_ACTIVE,
            Status.STATUS_MARKED_ROLLBACK,
            Status.STATUS_PREPARING,
            Status.STATUS_PREPARED,
            Status.STATUS_COMMITTING,
            Status.STATUS_ROLLING_BACK ->
            true;
        default -> false;
      };
    } catch (Throwable e) {
      throw new TransactionSystemException(
          "Failed to check for an existing active JTA transaction", e);
    }
  }
}
