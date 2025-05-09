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

package digital.inception.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The {@code AuthenticationToken} class.
 *
 * @author Marcus Portmann
 */
public class AuthenticationToken extends AbstractAuthenticationToken {

  /** The user details. */
  private final UserDetails userDetails;

  /**
   * Constructs a new {@code AuthenticationToken}.
   *
   * @param userDetails the user details
   */
  public AuthenticationToken(UserDetails userDetails) {
    super(userDetails.getAuthorities());

    this.userDetails = userDetails;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return userDetails;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }
}
