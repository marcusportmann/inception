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

package digital.inception.ws.security;

/**
 * The enumeration giving the supported types of web service security.
 */
public enum WebServiceSecurityType
{
  /**
   * The value indicating that the web service does not implement a security service.
   */
  NONE(0, "None"),

  /**
   * The value indicating that the web service implements the WS-Security X509 token authentication
   * security service.
   */
  WSS_SECURITY_X509_TOKEN(1, "WS-Security X509 Token"),

  /**
   * The value indicating that the web service implements the WS-Security username token security
   * service.
   */
  WSS_SECURITY_USERNAME_TOKEN(2, "WS-Security Username Token"),

  /**
   * The value indicating that the web service implements the Mutual SSL security service.
   */
  MUTUAL_SSL(3, "Mutual SSL"),

  /**
   * The value indicating that the web service implements the basic HTTP authentication security
   * service.
   */
  HTTP_AUTHENTICATION(4, "HTTP Authentication"),

  /**
   * The value indicating that the web service implements the digest authentication security service.
   */
  DIGEST_AUTHENTICATION(5, "Digest Authentication");

  private int code;
  private String description;

  WebServiceSecurityType(int code, String description)
  {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the web service security type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the web service security type
   *
   * @return the web service security type given by the specified numeric code value
   */
  public static WebServiceSecurityType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return WebServiceSecurityType.NONE;

      case 1:
        return WebServiceSecurityType.WSS_SECURITY_X509_TOKEN;

      case 2:
        return WebServiceSecurityType.WSS_SECURITY_USERNAME_TOKEN;

      case 3:
        return WebServiceSecurityType.MUTUAL_SSL;

      case 4:
        return WebServiceSecurityType.HTTP_AUTHENTICATION;

      default:
        return WebServiceSecurityType.DIGEST_AUTHENTICATION;
    }
  }

  /**
   * Returns the numeric code value identifying the web service security type.
   *
   * @return the numeric code value identifying the web service security type
   */
  public int code()
  {
    return code;
  }

  /**
   * Returns the description for the web service security type.
   *
   * @return the description for the web service security type
   */
  public String description()
  {
    return description;
  }

  /**
   * Return the string representation of the <code>WebServiceSecurityType</code>
   * enumeration value.
   *
   * @return the string representation of the <code>WebServiceSecurityType</code>
   *         enumeration value
   */
  public String toString()
  {
    return description;
  }
}
