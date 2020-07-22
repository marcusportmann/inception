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

import java.io.Serializable;

/**
 * The <code>OAuth2RefreshToken</code> class holds the information for an OAuth2 refresh token.
 *
 * @author Marcus Portmann
 */
public class OAuth2AccessToken implements Serializable {

  private static final long serialVersionUID = 1000000;

  /**
   * The type of the token issued as described in <a
   * href="https://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-7.1">Section 7.1</a>. Value is
   * case insensitive. This value is REQUIRED.
   */
  public static String TOKEN_TYPE = "token_type";
}
