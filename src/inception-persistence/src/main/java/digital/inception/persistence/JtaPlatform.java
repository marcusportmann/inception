/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.persistence;

// ~--- non-JDK imports --------------------------------------------------------

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;
import org.springframework.transaction.jta.JtaTransactionManager;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>JtaPlatform</code> class.
 *
 * @author Marcus Portmann
 */
public class JtaPlatform extends AbstractJtaPlatform {

  private final JtaTransactionManager transactionManager;

  /**
   * Constructs a new <code>JtaPlatform</code>.
   *
   * @param transactionManager the JTA transaction manager
   */
  JtaPlatform(JtaTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  protected TransactionManager locateTransactionManager() {
    return transactionManager.getTransactionManager();
  }

  @Override
  protected UserTransaction locateUserTransaction() {
    return transactionManager.getUserTransaction();
  }
}
