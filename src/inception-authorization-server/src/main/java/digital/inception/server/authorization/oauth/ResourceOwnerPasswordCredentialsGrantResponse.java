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

package digital.inception.server.authorization.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import digital.inception.json.JsonUtil;
import org.springframework.http.HttpStatus;

/**
 * The {@code ResourceOwnerPasswordCredentialsGrantResponse} class holds the information for a
 * Resource Owner Password Credentials Grant response.
 *
 * @author Marcus Portmann
 */
public class ResourceOwnerPasswordCredentialsGrantResponse extends Response {

  /** The access token. */
  @JsonProperty("access_token")
  private final String accessToken;

  /** The lifetime in seconds of the access token. */
  @JsonProperty("expires_in")
  private final long expiresIn;

  /** The refresh token. */
  @JsonProperty("refresh_token")
  private final String refreshToken;

  /** The access token scope. */
  @JsonProperty("scope")
  private final String scope;

  /** The token type. */
  @JsonProperty("token_type")
  private final String tokenType = "bearer";

  /**
   * Constructs a new {@code ResourceOwnerPasswordCredentialsGrantResponse}.
   *
   * @param accessToken the access token
   * @param expiresIn the access token validity in seconds
   * @param scope the access token scope
   * @param refreshToken the refresh token
   */
  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public ResourceOwnerPasswordCredentialsGrantResponse(
      String accessToken, int expiresIn, String scope, String refreshToken) {
    super(HttpStatus.OK);

    this.accessToken = accessToken;
    this.expiresIn = expiresIn;
    this.scope = scope;
    this.refreshToken = refreshToken;
  }

  /**
   * Constructs a new {@code ResourceOwnerPasswordCredentialsGrantResponse}.
   *
   * @param accessToken the access token
   * @param expiresIn the access token validity in seconds
   * @param refreshToken the refresh token
   */
  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public ResourceOwnerPasswordCredentialsGrantResponse(
      String accessToken, long expiresIn, String refreshToken) {
    super(HttpStatus.OK);

    this.accessToken = accessToken;
    this.expiresIn = expiresIn;
    this.scope = null;
    this.refreshToken = refreshToken;
  }

  /**
   * Constructs a new {@code ResourceOwnerPasswordCredentialsGrantResponse}.
   *
   * @param accessToken the access token
   * @param expiresIn the lifetime in seconds of the access token
   */
  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public ResourceOwnerPasswordCredentialsGrantResponse(String accessToken, long expiresIn) {
    super(HttpStatus.OK);

    this.accessToken = accessToken;
    this.expiresIn = expiresIn;
    this.scope = null;
    this.refreshToken = null;
  }

  /**
   * Returns the access token.
   *
   * @return the access token
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Returns the body for the OAuth2 response.
   *
   * @return the body for the OAuth2 response
   */
  @Override
  @JsonIgnore
  public String getBody() {
    try {
      return JsonUtil.getObjectMapper().writeValueAsString(this);
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to construct the body for the Resource Owner Password Credentials Grant response",
          e);
    }
  }

  /**
   * Returns the lifetime in seconds of the access token.
   *
   * @return the lifetime in seconds of the access token
   */
  public long getExpiresIn() {
    return expiresIn;
  }

  /**
   * Returns the refresh token.
   *
   * @return the refresh token
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Returns the access token scope.
   *
   * @return the access token scope
   */
  public String getScope() {
    return scope;
  }

  /**
   * Returns the token type.
   *
   * @return the token type
   */
  public String getTokenType() {
    return tokenType;
  }
}
