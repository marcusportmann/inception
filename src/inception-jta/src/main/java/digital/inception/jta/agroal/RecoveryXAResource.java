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

import digital.inception.jta.util.XAExceptionUtil;
import io.agroal.api.transaction.TransactionIntegration.ResourceRecoveryFactory;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

/**
 * The {@code RecoveryXAResource} class provides a closeable wrapper around the XAResource
 * associated with an XAConnection that keeps track of the lifecycle of the XAResource and closes
 * the connection when required.
 *
 * @author Marcus Portmann
 */
public class RecoveryXAResource implements AutoCloseable, XAResource {

  private final ResourceRecoveryFactory resourceRecoveryFactory;

  private volatile XAConnection xaConnection;

  private volatile XAResource xaResource;

  /**
   * Constructs a new {@code RecoveryXAResource}.
   *
   * @param resourceRecoveryFactory the resource recovery factory
   */
  public RecoveryXAResource(ResourceRecoveryFactory resourceRecoveryFactory) {
    this.resourceRecoveryFactory = resourceRecoveryFactory;
  }

  @Override
  public synchronized void close() throws XAException {
    try {
      if (xaConnection != null) {
        xaConnection.close();
      }
    } catch (Throwable e) {
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMFAIL, "Failed to close the XAConnection", e);
    } finally {
      xaConnection = null;
      xaResource = null;
    }
  }

  @Override
  public void commit(Xid xid, boolean onePhase) throws XAException {
    delegateOrThrow().commit(xid, onePhase);
  }

  @Override
  public void end(Xid xid, int flags) throws XAException {
    delegateOrThrow().end(xid, flags);
  }

  @Override
  public void forget(Xid xid) throws XAException {
    delegateOrThrow().forget(xid);
  }

  @Override
  public int getTransactionTimeout() throws XAException {
    return delegateOrThrow().getTransactionTimeout();
  }

  @Override
  public boolean isSameRM(XAResource other) throws XAException {
    XAResource d = delegateOrThrow();
    if (other instanceof RecoveryXAResource) {
      XAResource od = ((RecoveryXAResource) other).xaResource;
      if (od == null) return false;
      return d.isSameRM(od);
    }
    return d.isSameRM(other);
  }

  @Override
  public int prepare(Xid xid) throws XAException {
    return delegateOrThrow().prepare(xid);
  }

  @Override
  public synchronized Xid[] recover(int flag) throws XAException {
    // Open on first use OR when a new scan starts. Use bitmask because flags can be combined.
    boolean startScan = (flag & TMSTARTRSCAN) != 0;

    if (startScan || xaConnection == null || xaResource == null) {
      // (Re)open a recovery connection if needed
      closeAndReset(); // ensure we don't leak a previous connection
      try {
        xaConnection = resourceRecoveryFactory.getRecoveryConnection();
        if (xaConnection == null) {
          throw new XAException(XAException.XAER_RMFAIL);
        }
        xaResource = xaConnection.getXAResource();

        // NOTE: Critical guard against null from driver
        if (xaResource == null) {
          throw new XAException(XAException.XAER_RMFAIL);
        }
      } catch (Throwable e) {
        closeAndReset();
        throw XAExceptionUtil.xaException(
            XAException.XAER_RMFAIL,
            "Failed to retrieve the recovery XAConnection/XAResource from the ResourceRecoveryFactory",
            e);
      }
    }

    // At this point, both must be non-null or we bail out.
    if (xaResource == null) {
      throw new XAException(XAException.XAER_RMFAIL);
    }

    Xid[] xids = xaResource.recover(flag);

    // Close when the scan ends. Some TMs may end with END even if XIDs were returned.
    boolean endScan = (flag & TMENDRSCAN) != 0;
    if (endScan) {
      // You can choose to always close on END, or keep your existing optimization:
      // if (xids == null || xids.length == 0) close();
      closeAndReset();
    }

    return xids;
  }

  @Override
  public void rollback(Xid xid) throws XAException {
    delegateOrThrow().rollback(xid);
  }

  @Override
  public boolean setTransactionTimeout(int seconds) throws XAException {
    return delegateOrThrow().setTransactionTimeout(seconds);
  }

  @Override
  public void start(Xid xid, int flags) throws XAException {
    delegateOrThrow().start(xid, flags);
  }

  private synchronized void closeAndReset() {
    try {
      if (xaConnection != null) xaConnection.close();
    } catch (Throwable ignore) {
      // swallow during quiet close
    } finally {
      xaConnection = null;
      xaResource = null;
    }
  }

  private XAResource delegateOrThrow() throws XAException {
    if (xaResource == null) throw new XAException(XAException.XAER_RMERR);
    return xaResource;
  }
}
