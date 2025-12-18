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

package digital.inception.r2dbc;

import java.util.Set;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * The {@code OracleUtil} class provides utility methods for working with Oracle, in the context of
 * R2DBC database access.
 *
 * @author Marcus Portmann
 */
public final class OracleUtil {

  /**
   * Set of Oracle error codes that typically indicate transient, retryable failures such as
   * connection loss, listener/network issues, RAC/TAF failover, or database startup/shutdown.
   *
   * <p>The codes are expected to appear in the exception message in the form {@code "ORA-xxxxx"}.
   */
  private static final Set<String> RETRYABLE_ORACLE_CODES =
      Set.of(
          // Session/connection lost
          "ORA-01012", // not logged on
          "ORA-03113", // end-of-file on a communication channel
          "ORA-03114", // not connected to ORACLE
          "ORA-03135", // connection lost contact
          // Listener/network/timeouts
          "ORA-12170", // TNS:Connect timeout occurred
          "ORA-12514", // TNS:listener does not currently know of the service requested
          "ORA-12541", // TNS:no listener
          "ORA-12537", // TNS:connection closed
          "ORA-12547", // TNS:lost contact
          "ORA-12570", // TNS:packet reader failure
          "ORA-12571", // TNS:packet writer failure
          // DB startup/shutdown transient
          "ORA-01033", // ORACLE initialization or shutdown in progress
          "ORA-01034", // ORACLE not available
          // Session killed/expired
          "ORA-00028", // your session has been killed
          "ORA-02396", // exceeded max idle time, please reconnect
          // Package invalidation (an object recompiled)
          "ORA-04061", // existing state of package has been invalidated
          "ORA-04065", // not executed, altered/compiled error
          "ORA-04068", // the existing state of packages has been discarded
          // RAC/TAF failover indicator
          "ORA-25408" // cannot safely replay a call
          );

  /** Private default constructor to prevent instantiation. */
  private OracleUtil() {}

  /**
   * Determine whether the given exception represents a retryable Oracle error.
   *
   * <p>An error is considered retryable if either:
   *
   * <ul>
   *   <li>it is an instance of {@link DataAccessResourceFailureException}; or
   *   <li>its message, or the message of any cause in its causal chain, contains one of the {@code
   *       ORA-xxxxx} codes listed in {@link #RETRYABLE_ORACLE_CODES}.
   * </ul>
   *
   * @param ex the exception to inspect
   * @return {@code true} if the error is considered retryable; {@code false} otherwise
   */
  public static boolean isRetryableError(Throwable ex) {
    return ex instanceof DataAccessResourceFailureException || hasAnyOracleCode(ex);
  }

  /**
   * Check whether the supplied exception or any of its causes contains one of the configured Oracle
   * error codes in its message.
   *
   * @param ex the exception whose causal chain should be inspected (may be {@code null})
   * @return {@code true} if any exception in the causal chain has a message containing one of the
   *     {@link #RETRYABLE_ORACLE_CODES}; {@code false} otherwise
   */
  private static boolean hasAnyOracleCode(Throwable ex) {
    for (Throwable current = ex; current != null; current = current.getCause()) {
      if (containsRetryableOracleCode(current.getMessage())) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsRetryableOracleCode(String message) {
    if (message == null) {
      return false;
    }
    for (String code : RETRYABLE_ORACLE_CODES) {
      if (message.contains(code)) {
        return true;
      }
    }
    return false;
  }
}
