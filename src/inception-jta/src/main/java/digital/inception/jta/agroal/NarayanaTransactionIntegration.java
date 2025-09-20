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

package digital.inception.jta.agroal;

import com.arjuna.ats.arjuna.recovery.RecoveryManager;
import com.arjuna.ats.internal.jta.recovery.arjunacore.XARecoveryModule;
import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
import digital.inception.jta.util.TransactionUtil;
import io.agroal.api.transaction.TransactionAware;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.TransactionSynchronizationRegistry;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.transaction.xa.XAResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The {@code NarayanaTransactionIntegration} class provides an Agroal {@code
 * TransactionIntegration} implementation that tracks a "primary" TransactionAware per JTA
 * transaction.
 *
 * @author Marcus Portmann
 */
@Component
public class NarayanaTransactionIntegration
    implements io.agroal.api.transaction.TransactionIntegration {

  private static final Logger log = LoggerFactory.getLogger(NarayanaTransactionIntegration.class);

  /**
   * Stable key used to store per-transaction state in the TSR.
   *
   * <p>The TransactionSynchronizationRegistry (TSR) is per-transaction storage, not a global map.
   * This key is just the name under which this bean stores its value inside each active
   * transaction’s registry. So even with a single Spring bean, there can be hundreds of concurrent
   * TSR entries—one per active JTA transaction—all using the same key object but living in
   * different transaction scopes.
   */
  private final UUID key = UUID.randomUUID();

  private final RecoveryManager recoveryManager;
  private final TransactionManager transactionManager;
  private final TransactionSynchronizationRegistry transactionSynchronizationRegistry;

  private final ConcurrentMap<ResourceRecoveryFactory, XAResourceRecoveryHelperImpl>
      xaResourceRecoveryHelperImplCache = new ConcurrentHashMap<>();

  /**
   * Constructs a new {@code NarayanaTransactionIntegration}.
   *
   * @param transactionManager the transaction manager
   * @param transactionSynchronizationRegistry the transaction synchronization registry
   */
  public NarayanaTransactionIntegration(
      TransactionManager transactionManager,
      TransactionSynchronizationRegistry transactionSynchronizationRegistry) {
    this(transactionManager, transactionSynchronizationRegistry, null);
  }

  /**
   * Constructs a new {@code NarayanaTransactionIntegration}.
   *
   * @param transactionManager the transaction manager
   * @param transactionSynchronizationRegistry the transaction synchronization registry
   * @param recoveryManager the recovery manager
   */
  public NarayanaTransactionIntegration(
      TransactionManager transactionManager,
      TransactionSynchronizationRegistry transactionSynchronizationRegistry,
      RecoveryManager recoveryManager) {
    this.transactionManager = transactionManager;
    this.transactionSynchronizationRegistry = transactionSynchronizationRegistry;
    this.recoveryManager = recoveryManager;
  }

  /**
   * Constructs a new {@code NarayanaTransactionIntegration}.
   *
   * @param applicationContext the Spring application context
   */
  @Autowired
  public NarayanaTransactionIntegration(ApplicationContext applicationContext) {
    try {
      this.transactionManager =
          applicationContext.getBean("narayanaTransactionManager", TransactionManager.class);
      this.transactionSynchronizationRegistry =
          applicationContext.getBean(
              "narayanaTransactionSynchronizationRegistry",
              TransactionSynchronizationRegistry.class);
      this.recoveryManager =
          applicationContext.getBean("narayanaRecoveryManager", RecoveryManager.class);
    } catch (Throwable e) {
      throw new BeanInitializationException(
          "Failed to initialize the Narayana Transaction Integration", e);
    }
  }

  @Override
  public void addResourceRecoveryFactory(ResourceRecoveryFactory resourceRecoveryFactory) {
    if (recoveryManager == null) {
      throw new IllegalStateException(
          "RecoveryManager is not configured. Use the constructor that provides a RecoveryManager or the Spring-bootstrapped constructor.");
    }

    XARecoveryModule xaRecoveryModule =
        (XARecoveryModule)
            recoveryManager.getModules().stream()
                .filter(m -> m instanceof XARecoveryModule)
                .findFirst()
                .orElse(null);

    if (xaRecoveryModule == null) {
      throw new IllegalStateException(
          "Failed to retrieve the XARecoveryModule from the Narayana Recovery Manager");
    }

    xaRecoveryModule.addXAResourceRecoveryHelper(
        xaResourceRecoveryHelperImplCache.computeIfAbsent(
            resourceRecoveryFactory,
            rrf -> new XAResourceRecoveryHelperImpl(new RecoveryXAResource(rrf))));
  }

  @Override
  public void associate(TransactionAware transactionAware, XAResource xaResource)
      throws SQLException {
    try {
      // Always provide the TX liveness callback so Agroal can re-check transaction state at any
      // time.
      transactionAware.transactionCheckCallback(this::transactionRunning);

      if (transactionRunning()) {
        TransactionAwares transactionAwares =
            (TransactionAwares) transactionSynchronizationRegistry.getResource(key);
        if (transactionAwares == null) {
          transactionAwares = new TransactionAwares();
          transactionSynchronizationRegistry.putResource(key, transactionAwares);
          transactionSynchronizationRegistry.registerInterposedSynchronization(
              new InterposedSynchronization(transactionAwares));
        }

        // Add and possibly set as primary if this is the first enlistment in this TX.
        transactionAwares.addAndMaybeSetPrimary(transactionAware);
        transactionAware.transactionStart();

        // Enlist (wrap if necessary)
        final XAResource toEnlist =
            (xaResource != null)
                ? new TransactionAwareXAResource(transactionAware, xaResource)
                : new LocalXAResource(transactionAware);
        transactionManager.getTransaction().enlistResource(toEnlist);
      } else {
        // No active JTA TX: still provide callback so pool can probe TX state.
        transactionAware.transactionCheckCallback(this::transactionRunning);
      }
    } catch (Throwable e) {
      throw new SQLException(
          "Failed to associate the TransactionAware resource with the current JTA transaction", e);
    }
  }

  @Override
  public boolean disassociate(TransactionAware transactionAware) {
    // Fetch the current TX-scoped set, if any
    final TransactionAwares transactionAwares =
        (TransactionAwares) transactionSynchronizationRegistry.getResource(key);

    if (transactionRunning()) {
      if (transactionAwares != null) {
        // Still in TX: just detach from the set; do NOT end here.
        transactionAwares.removeAndReassignPrimaryIfNeeded(transactionAware);
        if (transactionAwares.isEmpty()) {
          transactionSynchronizationRegistry.putResource(key, null);
        }
      }
    } else {
      // TX is not running. This can mean either:
      //  (a) there was never a JTA TX for this TA (non-JTA usage), or
      //  (b) the TX just completed and afterCompletion will (or already did) end it.

      boolean partOfCompletedTx =
          (transactionAwares != null && transactionAwares.contains(transactionAware));
      if (!partOfCompletedTx) {
        // Only end here if this TA is not tracked by the per-TX set
        try {
          transactionAware.transactionEnd();
        } catch (Throwable ignore) {
          // best-effort; avoid propagating double-close noise
        }
      }
      // If it *was* part of the set, skip: afterCompletion ends it (or already did).
    }
    return true;
  }

  @Override
  public TransactionAware getTransactionAware() {
    if (!transactionRunning()) return null;
    TransactionAwares transactionAwares =
        (TransactionAwares) transactionSynchronizationRegistry.getResource(key);
    return (transactionAwares == null) ? null : transactionAwares.getPrimary();
  }

  @Override
  public void removeResourceRecoveryFactory(ResourceRecoveryFactory resourceRecoveryFactory) {
    if (recoveryManager == null) {
      throw new IllegalStateException(
          "RecoveryManager is not configured. Use the constructor that provides a RecoveryManager or the Spring-bootstrapped constructor.");
    }

    final XAResourceRecoveryHelper helper =
        xaResourceRecoveryHelperImplCache.remove(resourceRecoveryFactory);
    if (helper == null) return;

    final XARecoveryModule xaRecoveryModule =
        (XARecoveryModule)
            recoveryManager.getModules().stream()
                .filter(m -> m instanceof XARecoveryModule)
                .findFirst()
                .orElseThrow(
                    () ->
                        new IllegalStateException(
                            "Failed to retrieve the XARecoveryModule from the Narayana Recovery Manager"));
    xaRecoveryModule.removeXAResourceRecoveryHelper(helper);
  }

  /** Shutdown the Narayana Transaction Integration. */
  @PreDestroy
  public void shutdown() {
    log.info("Shutting down the Narayana Transaction Integration");

    if (recoveryManager == null) {
      xaResourceRecoveryHelperImplCache.clear();
      return;
    }

    final XARecoveryModule xaRecoveryModule =
        (XARecoveryModule)
            recoveryManager.getModules().stream()
                .filter(m -> m instanceof XARecoveryModule)
                .findFirst()
                .orElse(null);

    if (xaRecoveryModule != null) {
      for (XAResourceRecoveryHelperImpl helper : xaResourceRecoveryHelperImplCache.values()) {
        try {
          xaRecoveryModule.removeXAResourceRecoveryHelper(helper);
        } catch (Throwable t) {
          log.warn("Failed to remove the XAResourceRecoveryHelper during shutdown; continuing", t);
        }
      }
    }
    xaResourceRecoveryHelperImplCache.clear();
  }

  private boolean transactionRunning() {
    return TransactionUtil.transactionExists(transactionManager);
  }

  /** Interposed synchronization that ends all enlisted TransactionAwares for the TX. */
  private static class InterposedSynchronization implements Synchronization {
    private final TransactionAwares txAwares;

    InterposedSynchronization(TransactionAwares txAwares) {
      this.txAwares = txAwares;
    }

    @Override
    public void afterCompletion(int status) {
      if (txAwares == null) return; // defensive

      for (TransactionAware ta : txAwares.snapshot()) {
        try {
          ta.transactionEnd();
        } catch (Throwable e) {
          log.warn("Failed to end transaction-aware after completion; continuing", e);
        }
      }
      txAwares.clear();
      // DO NOT access TSR here; transaction is no longer associated.
    }

    @Override
    public void beforeCompletion() {
      /* no-op */
    }
  }

  /**
   * Per-transaction holder of enlisted TransactionAwares, with a tracked "primary". Uses identity
   * semantics (no reliance on equals/hashCode of pool objects).
   */
  private static final class TransactionAwares {
    private final Set<TransactionAware> set =
        java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());

    // Tracks the primary TransactionAware for this TX (first successfully enlisted).
    private TransactionAware primary;

    synchronized void addAndMaybeSetPrimary(TransactionAware ta) {
      boolean added = set.add(ta);
      if (added && primary == null) {
        primary = ta; // first enlistment becomes primary
      }
    }

    synchronized void clear() {
      set.clear();
      primary = null;
    }

    synchronized boolean contains(TransactionAware ta) {
      return set.contains(ta);
    }

    synchronized TransactionAware getPrimary() {
      return primary;
    }

    synchronized boolean isEmpty() {
      return set.isEmpty();
    }

    synchronized void removeAndReassignPrimaryIfNeeded(TransactionAware ta) {
      if (!set.remove(ta)) return;
      if (ta == primary) {
        // Promote any remaining element to primary (if present), else clear.
        primary = set.isEmpty() ? null : set.iterator().next();
      }
    }

    synchronized Set<TransactionAware> snapshot() {
      return new java.util.HashSet<>(set);
    }
  }

  /** Minimal helper wrapper for Narayana's recovery SPI. */
  private static class XAResourceRecoveryHelperImpl implements XAResourceRecoveryHelper {
    private final XAResource[] xaResources;

    XAResourceRecoveryHelperImpl(XAResource xaResource) {
      this.xaResources = new XAResource[] {xaResource};
    }

    @Override
    public XAResource[] getXAResources() {
      return xaResources;
    }

    @Override
    public boolean initialise(String p) {
      return true;
    }
  }
}
