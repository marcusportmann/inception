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

package digital.inception.ws.security;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.auth.DigestAuthSupplier;

/**
 * The <b>CXFDigestSecurityProxyConfigurator</b> class provides the capability to configure a CXF
 * web service proxy for digest authentication.
 *
 * @author Marcus Portmann
 */
public final class CXFDigestSecurityProxyConfigurator {

  /** Private constructor to prevent instantiation. */
  private CXFDigestSecurityProxyConfigurator() {}

  /**
   * Configure the CXF web service proxy to support digest authentication.
   *
   * @param proxy the web service proxy to configure
   * @param username the username to use when authenticating using digest authentication
   * @param password the password to use when authenticating using digest authentication
   */
  public static void configureProxy(Object proxy, String username, String password) {
    InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);

    if (invocationHandler instanceof ClientProxy clientProxy) {

      Conduit conduit = clientProxy.getClient().getConduit();

      if (conduit instanceof HTTPConduit httpConduit) {

        AuthorizationPolicy authorizationPolicy = httpConduit.getAuthorization();

        authorizationPolicy.setAuthorization("Digest");
        authorizationPolicy.setAuthorizationType("Digest");
        authorizationPolicy.setUserName(username);
        authorizationPolicy.setPassword(password);

        httpConduit.setAuthSupplier(new DigestAuthSupplier());

        httpConduit.getClient().setAllowChunking(false);
      }
    }
  }
}
