/**
 * The TokenResponse interface defines the structure of the token response returned by the Spring
 * OAuth2 authorization server.
 *
 * @author Marcus Portmann
 */
export interface TokenResponse {

  /**
   * The base-64 encoded OAuth2 JWT access token.
   */
  access_token?: string;

  /**
   * The epoch timestamp, for the local timezone, giving the date and time the OAuth2 JWT access
   * token will expire.
   */
  expires_in?: number;

  /**
   * The unique ID
   */
  jti?: string;

  /**
   * The base-64 encoded JWT refresh token.
   */
  refresh_token?: string;

  /**
   * The OAuth2 scopes.
   */
  scope?: string[];

  /**
   * The OAuth2 token type e.g. bearer.
   */
  token_type?: string;
}
