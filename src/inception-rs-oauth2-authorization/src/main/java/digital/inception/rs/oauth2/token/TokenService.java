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

package digital.inception.rs.oauth2.token;

import digital.inception.security.ISecurityService;
import org.springframework.stereotype.Service;

/**
 * The <code>TokenService</code> class provides the Token Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class TokenService implements ITokenService {

  /* Security Service */
  private ISecurityService securityService;

  /**
   * Constructs a new <code>TokenService</code>.
   *
   * @param securityService the Security Service
   * @param tokenRepository the Token Repository
   */
  public TokenService(ISecurityService securityService) {
    this.securityService = securityService;
  }
}
