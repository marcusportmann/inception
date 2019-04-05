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

package digital.inception.rs.oauth;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * The <code>ClientDetailsService</code> class provides the Client Details Service implementation
 * that provides the details for OAuth2 clients.
 *
 * @author Marcus Portmann
 */
public class ClientDetailsService
  implements org.springframework.security.oauth2.provider.ClientDetailsService
{
  @Override
  public org.springframework.security.oauth2.provider.ClientDetails loadClientByClientId(
      String clientId)
    throws ClientRegistrationException
  {
    ClientDetails clientDetails = new ClientDetails(clientId);

    return clientDetails;
  }
}
