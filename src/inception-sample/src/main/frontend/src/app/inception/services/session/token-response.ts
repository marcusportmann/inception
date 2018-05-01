/**
 * The TokenResponse interface defines the structure of the token response returned by the Spring
 * OAuth2 authorization server.
 *
 * @author Marcus Portmann
 */
export interface TokenResponse {

  /**
   * The base-64 encoded JWT access token.
   */
  access_token?: string;

  /**
   *
   */
  expires_in?: number;

  jti?: string;

  /**
   * The base-64 encoded JWT refresh token.
   */
  refresh_token?: string;

  scope?: string[];

  token_type?: string;
}
