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

import digital.inception.core.model.CodeEnum;

/** The enumeration giving the supported types of web service security. */
public enum WebServiceSecurityType implements CodeEnum {
  /** The value indicating that the web service is not secured. */
  NONE("none", "None"),

  /**
   * The value indicating that the web service implements WS-Security X509 token authentication
   * security.
   */
  WSS_SECURITY_X509_TOKEN("wss_security_x509_token", "WS-Security X509 Token"),

  /** The value indicating that the web service implements WS-Security username token security. */
  WSS_SECURITY_USERNAME_TOKEN("wss_security_username_token", "WS-Security Username Token"),

  /** The value indicating that the web service implements Mutual SSL security. */
  MUTUAL_SSL("mutual_ssl", "Mutual SSL"),

  /** The value indicating that the web service implements basic HTTP authentication security. */
  HTTP_AUTHENTICATION("http_authentication", "HTTP Authentication"),

  /** The value indicating that the web service implements digest authentication security. */
  DIGEST_AUTHENTICATION("digest_authentication", "Digest Authentication");

  private final String code;

  private final String description;

  WebServiceSecurityType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the web service security type.
   *
   * @return the code for the web service security type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the web service security type.
   *
   * @return the description for the web service security type
   */
  public String description() {
    return description;
  }

  /**
   * Returns the string representation of the enumeration value.
   *
   * @return the string representation of the enumeration value
   */
  public String toString() {
    return description;
  }
}
