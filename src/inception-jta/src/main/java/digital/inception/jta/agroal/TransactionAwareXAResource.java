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
import io.agroal.api.transaction.TransactionAware;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code TransactionAwareXAResource} class provides a wrapper for a transactional resource,
 * which is represented as both a XAResource object and an Agroal transaction-aware connection
 * resource. The wrapper ensures that transactional behavior, i.e., start, commit, rollback, etc.,
 * is propagated to both the underlying XAResource object and the TransactionAware object.
 *
 * @author Marcus Portmann
 * @see <a
 *     href="https://github.com/agroal/agroal/blob/master/agroal-narayana/src/main/java/io/agroal/narayana/BaseXAResource.java">BaseXAResource.java</a>
 */
public class TransactionAwareXAResource implements XAResource {

  private static final Logger log = LoggerFactory.getLogger(TransactionAwareXAResource.class);

  /** The Agroal transaction aware connection resource. */
  private final TransactionAware transactionAware;

  /** The underlying XA resource. */
  private final XAResource xaResource;

  /**
   * Constructs a new {@code TransactionAwareXAResource}.
   *
   * @param transactionAware the Agroal transaction aware connection resource
   * @param xaResource the underlying XA resource
   */
  public TransactionAwareXAResource(TransactionAware transactionAware, XAResource xaResource) {
    this.transactionAware = transactionAware;
    this.xaResource = xaResource;
  }

  private static String flagsToString(int flags) {
    if (flags == 0) {
      return "NONE";
    }
    StringBuilder sb = new StringBuilder();
    if ((flags & XAResource.TMNOFLAGS) != 0) sb.append("TMNOFLAGS|");
    if ((flags & XAResource.TMJOIN) != 0) sb.append("TMJOIN|");
    if ((flags & XAResource.TMRESUME) != 0) sb.append("TMRESUME|");
    if ((flags & XAResource.TMSUCCESS) != 0) sb.append("TMSUCCESS|");
    if ((flags & XAResource.TMFAIL) != 0) sb.append("TMFAIL|");
    if ((flags & XAResource.TMSUSPEND) != 0) sb.append("TMSUSPEND|");
    if ((flags & XAResource.TMSTARTRSCAN) != 0) sb.append("TMSTARTRSCAN|");
    if ((flags & XAResource.TMENDRSCAN) != 0) sb.append("TMENDRSCAN|");

    // Trim trailing '|'
    if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == '|') {
      sb.setLength(sb.length() - 1);
    }
    return sb.toString();
  }

  @Override
  public void commit(Xid xid, boolean onePhase) throws XAException {
    try {
      // transactionAware.transactionBeforeCompletion(true);  // REMOVE
      xaResource.commit(xid, onePhase);
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          onePhase ? XAException.XA_RBROLLBACK : XAException.XAER_RMERR,
          "Failed to commit the XA transaction ("
              + xid
              + ") for the XA resource: "
              + e.getMessage(),
          e);
    }
  }

  @Override
  public void end(Xid xid, int flags) throws XAException {
    if (log.isDebugEnabled()) {
      log.debug("XA end   xid={}, flags={}", xid, flagsToString(flags));
    }

    try {
      xaResource.end(xid, flags);
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to end the transaction (" + xid + ") for the XA resource: " + e.getMessage(),
          e);
    }
  }

  @Override
  public void forget(Xid xid) throws XAException {
    try {
      xaResource.forget(xid);
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to forget the transaction (" + xid + ") for the XA resource: " + e.getMessage(),
          e);
    }
  }

  @Override
  public int getTransactionTimeout() throws XAException {
    try {
      return xaResource.getTransactionTimeout();
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to retrieve the transaction timeout for the XA resource: " + e.getMessage(),
          e);
    }
  }

  @Override
  public boolean isSameRM(XAResource xaResource) throws XAException {
    try {
      if (xaResource instanceof TransactionAwareXAResource) {
        return this.xaResource.isSameRM(((TransactionAwareXAResource) xaResource).xaResource);
      } else {
        return this.xaResource.isSameRM(xaResource);
      }
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to determine whether the XA resource has the same resource manager: "
              + e.getMessage(),
          e);
    }
  }

  @Override
  public int prepare(Xid xid) throws XAException {
    try {
      return xaResource.prepare(xid);
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to prepare the transaction for the XA resource: " + e.getMessage(),
          e);
    }
  }

  @Override
  public Xid[] recover(int flag) throws XAException {
    try {
      return xaResource.recover(flag);
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to retrieve the list of prepared transaction branches from the resource manager "
              + "for the XA resource: "
              + e.getMessage(),
          e);
    }
  }

  @Override
  public void rollback(Xid xid) throws XAException {
    try {
      // transactionAware.transactionBeforeCompletion(false); // REMOVE
      xaResource.rollback(xid);
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to rollback the transaction (" + xid + ") for the XA resource: " + e.getMessage(),
          e);
    }
  }

  @Override
  public boolean setTransactionTimeout(int seconds) throws XAException {
    try {
      return xaResource.setTransactionTimeout(seconds);
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to set the transaction timeout for the XA resource: " + e.getMessage(),
          e);
    }
  }

  @Override
  public void start(Xid xid, int flags) throws XAException {
    if (log.isDebugEnabled()) {
      log.debug("XA start xid={}, flags={}", xid, flagsToString(flags));
    }

    try {
      xaResource.start(xid, flags); // start branch first
      transactionAware.transactionStart(); // then tell the pool
    } catch (XAException e) {
      transactionAware.setFlushOnly();
      throw e;
    } catch (Throwable e) {
      transactionAware.setFlushOnly();
      throw XAExceptionUtil.xaException(
          XAException.XAER_RMERR,
          "Failed to start the transaction (" + xid + ") for the XA resource: " + e.getMessage(),
          e);
    }
  }
}
