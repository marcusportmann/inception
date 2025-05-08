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

package digital.inception.jta.narayana;

import com.arjuna.ats.arjuna.coordinator.TxControl;
import com.arjuna.ats.arjuna.recovery.RecoveryManager;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.TransactionSynchronizationRegistry;
import jakarta.transaction.UserTransaction;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * The {@code NarayanaFactory} class produces the JTA transaction management beans for the Narayana
 * transactions toolkit.
 *
 * @author Marcus Portmann
 * @see <a href="https://narayana.io">Naryana</a>
 */
@Component
@SuppressWarnings("unused")
public class NarayanaFactory {

  static {
    TxControl.setXANodeName(nodeName());
  }

  /** Creates a new {@code NarayanaFactory} instance. */
  public NarayanaFactory() {}

  /**
   * Retrieve the XA node name.
   *
   * @return the XA node name
   */
  private static String nodeName() {
    try {
      java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();

      String nodeName = localMachine.getHostName().toLowerCase();

      if (nodeName.length() > 23) {
        nodeName = FNV1aHash.fnv1a64Base64(nodeName.getBytes(StandardCharsets.UTF_8));
      }

      return nodeName;
    } catch (Throwable e) {
      return "Unknown";
    }
  }

  /**
   * Returns the Narayana recovery manager.
   *
   * @return the Narayana recovery manager
   */
  @Bean("narayanaRecoveryManager")
  public RecoveryManager narayanaRecoveryManager() {
    RecoveryManager recoveryManager = RecoveryManager.manager();

    recoveryManager.initialize();

    return RecoveryManager.manager();
  }

  /**
   * Returns the Narayana JTA transaction manager.
   *
   * @return the Narayana JTA transaction manager
   */
  @Bean("narayanaTransactionManager")
  @Primary
  public TransactionManager narayanaTransactionManager() {
    return com.arjuna.ats.jta.TransactionManager.transactionManager();
  }

  /**
   * Returns the Narayana JTA transaction synchronization registry.
   *
   * @param transactionManager the transaction manager
   * @return the Narayana JTA transaction synchronization registry
   */
  @Bean("narayanaTransactionSynchronizationRegistry")
  public TransactionSynchronizationRegistry narayanaTransactionSynchronizationRegistry(
      TransactionManager transactionManager) {
    return new com.arjuna.ats.internal.jta.transaction.arjunacore
        .TransactionSynchronizationRegistryImple();
  }

  /**
   * Returns the Narayana JTA user transaction.
   *
   * @return the Narayana JTA user transaction
   */
  @Bean
  public UserTransaction narayanaUserTransaction() {
    return com.arjuna.ats.jta.UserTransaction.userTransaction();
  }

  /**
   * Returns the Spring JTA platform transaction manager.
   *
   * @param userTransaction the user transaction
   * @param transactionManager the transaction manager
   * @return the Spring JTA platform transaction manager
   */
  @Bean
  @Primary
  public PlatformTransactionManager transactionManager(
      UserTransaction userTransaction, TransactionManager transactionManager) {
    try {
      return new JtaTransactionManager(userTransaction, transactionManager);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to initialize the JTA transaction manager");
    }
  }

  /** The {@code FNV1aHash} class. */
  private static final class FNV1aHash {

    private static final long FNV_64_INIT = 0xcbf29ce484222325L;

    private static final long FNV_64_PRIME = 0x100000001b3L;

    /**
     * Generate a FNV1a hash of the specified data.
     *
     * @param data the data
     * @return the FNV1a hash of the specified data
     */
    public static long fnv1a64(byte[] data) {
      long hash = FNV_64_INIT;
      for (byte b : data) {
        hash ^= (b & 0xff);
        hash *= FNV_64_PRIME;
      }
      return hash;
    }

    /**
     * Generate a base-64 encoded FNV1a hash of the specified data.
     *
     * @param data the data
     * @return the base-64 encoded FNV1a hash of the specified data
     */
    public static String fnv1a64Base64(byte[] data) {
      long hash = fnv1a64(data);
      byte[] hashBytes = longToBytes(hash);
      return Base64.getEncoder().encodeToString(hashBytes);
    }

    private static byte[] longToBytes(long x) {
      byte[] bytes = new byte[8];
      for (int i = 0; i < 8; i++) {
        bytes[i] = (byte) ((x >> (8 * i)) & 0xff);
      }
      return bytes;
    }
  }
}
