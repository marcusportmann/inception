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

package digital.inception.sms.smsportal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The {@code AuthenticationResponse} class.
 *
 * @author Marcus Portmann
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"token", "schema", "expiresInMinutes"})
public class AuthenticationResponse {

  /** The expiry time in minutes for the token. */
  @JsonProperty(required = true)
  private int expiresInMinutes;

  /** The token schema. */
  @JsonProperty(required = true)
  private String schema;

  /** The token. */
  @JsonProperty(required = true)
  private String token;

  /** Creates a new {@code AuthenticationResponse} instance. */
  public AuthenticationResponse() {}

  /**
   * Returns the expiry time in minutes for the token.
   *
   * @return the expiry time in minutes for the token
   */
  public int getExpiresInMinutes() {
    return expiresInMinutes;
  }

  /**
   * Returns the token schema.
   *
   * @return the token schema
   */
  public String getSchema() {
    return schema;
  }

  /**
   * Returns the token.
   *
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * Set the expiry time in minutes for the token.
   *
   * @param expiresInMinutes the expiry time in minutes for the token
   */
  public void setExpiresInMinutes(int expiresInMinutes) {
    this.expiresInMinutes = expiresInMinutes;
  }

  /**
   * Set the token schema.
   *
   * @param schema the token schema
   */
  public void setSchema(String schema) {
    this.schema = schema;
  }

  /**
   * Set the token.
   *
   * @param token the token
   */
  public void setToken(String token) {
    this.token = token;
  }
}
