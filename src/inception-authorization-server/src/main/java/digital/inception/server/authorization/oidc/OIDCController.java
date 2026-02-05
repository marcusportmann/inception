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

package digital.inception.server.authorization.oidc;

import com.nimbusds.jose.jwk.JWK;
import digital.inception.server.authorization.oauth.RefreshAccessTokenGrantRequest;
import digital.inception.server.authorization.oauth.ResourceOwnerPasswordCredentialsGrantRequest;
import digital.inception.server.authorization.token.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The {@code OIDCController} class implements the OpenID Connect endpoints.
 *
 * @author Marcus Portmann
 */
@Controller
@CrossOrigin
@Tag(name = "OIDC")
public class OIDCController {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(OIDCController.class);

  private final TokenService tokenService;

  /**
   * Constructs a new {@code OIDCController}.
   *
   * @param tokenService the Token Service
   */
  public OIDCController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  /**
   * Returns the JSON Web Key Set (JWKS) for this authorization server.
   *
   * <p>Publishes the server’s current (and, during rotation, recent) <em>public</em> signing keys
   * so that clients can validate JSON Web Tokens (JWTs). The response conforms to <a
   * href="https://www.rfc-editor.org/rfc/rfc7517">RFC&nbsp;7517</a> and is a JSON object with a
   * single {@code "keys"} property whose value is an array of JWKs. Each entry represents a public
   * key and may include fields such as {@code kid}, {@code kty}, {@code n}/{@code e} (RSA), or
   * curve-specific parameters.
   *
   * <p>Multiple keys may be returned to support key rotation; clients should select the correct key
   * by matching the JWT header’s {@code kid}. Only public material is exposed—no private keys are
   * returned by this endpoint.
   *
   * <p>A {@code Cache-Control} header is added to permit short-term caching (e.g., 10 minutes). If
   * keys change frequently, reduce the cache duration and retain retired keys until all tokens
   * signed with them have expired.
   *
   * <p>On failure, a standard error response (for example, a {@code ProblemDetails} payload from
   * global exception handling) may be produced by the platform.
   *
   * @return a {@link ResponseEntity} whose body is a map containing a single {@code "keys"} entry
   *     with the array of public {@link JWK JWKs}, along with appropriate caching headers
   * @see <a
   *     href="https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata">OpenID
   *     Connect Discovery: {@code jwks_uri}</a>
   * @see <a href="https://www.rfc-editor.org/rfc/rfc7517">RFC 7517: JSON Web Key (JWK)</a>
   */
  @Operation(
      summary = "Retrieve the JSON Web Key Set (JWKS)",
      description = "Retrieve the JSON Web Key Set (/.well-known/jwks.json).")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "The JSON Web Key Set was retrieved"),
    @ApiResponse(responseCode = "503", description = "Service unavailable")
  })
  @GetMapping(path = "/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Map<String, Object>> jwks() {
    List<JWK> keys = List.of(tokenService.getJwtRsaPublicKeyJwk());

    Map<String, Object> body = new HashMap<>();
    body.put("keys", keys.stream().map(JWK::toJSONObject).toList());

    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(Duration.ofDays(1)).cachePublic())
        .body(body);
  }

  /**
   * Returns the OpenID Provider Configuration (OIDC Discovery) document.
   *
   * <p>Exposes metadata that allows OpenID Connect/OAuth 2.0 clients to discover this authorization
   * server’s endpoints and capabilities, as defined by the OpenID Connect Discovery specification.
   * The response is a JSON object containing (among others) the following fields:
   *
   * <ul>
   *   <li><b>issuer</b> — Canonical issuer identifier (must be an exact-match string)
   *   <li><b>authorization_endpoint</b> — Authorization endpoint URL
   *   <li><b>token_endpoint</b> — Token endpoint URL
   *   <li><b>userinfo_endpoint</b> — UserInfo endpoint URL
   *   <li><b>jwks_uri</b> — JWKS endpoint URL publishing public signing keys
   *   <li><b>registration_endpoint</b> — Dynamic client registration endpoint URL (if supported)
   *   <li><b>revocation_endpoint</b> — Token revocation endpoint URL (RFC 7009)
   *   <li><b>introspection_endpoint</b> — Token introspection endpoint URL (RFC 7662)
   *   <li><b>scopes_supported</b> — Supported OAuth scopes (e.g., {@code openid}, {@code profile})
   *   <li><b>response_types_supported</b> — Supported response types (e.g., {@code code}, {@code
   *       id_token})
   *   <li><b>grant_types_supported</b> — Supported grant types (e.g., {@code authorization_code},
   *       {@code refresh_token})
   *   <li><b>subject_types_supported</b> — Supported subject types (e.g., {@code public}, {@code
   *       pairwise})
   *   <li><b>id_token_signing_alg_values_supported</b> — Supported ID Token JWS algorithms (e.g.,
   *       {@code RS256})
   *   <li><b>token_endpoint_auth_methods_supported</b> — Supported client auth methods at the token
   *       endpoint
   *   <li><b>code_challenge_methods_supported</b> — Supported PKCE methods (e.g., {@code S256})
   *   <li><b>claims_supported</b> — Supported standard claims (e.g., {@code sub}, {@code email})
   *   <li><b>request_parameter_supported}</b>, <b>request_uri_parameter_supported</b>,
   *       <b>claims_parameter_supported</b> — Optional feature toggles
   * </ul>
   *
   * <p>A {@code Cache-Control} header is applied to allow clients and intermediaries to cache the
   * document for a short period.
   *
   * @return a {@link ResponseEntity} whose body is the discovery metadata map and which includes
   *     appropriate caching headers
   */
  @Operation(
      summary = "Retrieve the OpenID Connect Discovery document",
      description =
          "Retrieve the OpenID Connect Discovery document (/.well-known/openid-configuration).")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "The OpenID Connect Discovery document was retrieved"),
    @ApiResponse(responseCode = "503", description = "Service unavailable")
  })
  @GetMapping(
      path = "/.well-known/openid-configuration",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Map<String, Object>> openIdConfiguration() {
    Map<String, Object> body = new LinkedHashMap<>();

    // Core metadata
    body.put("issuer", tokenService.getIssuer());
    body.put("token_endpoint", url("/oauth/token"));
    body.put("jwks_uri", url("/.well-known/jwks.json"));

    body.put("scopes_supported", List.of("openid", "profile", "email", "offline_access"));
    body.put(
        "grant_types_supported",
        List.of(
            ResourceOwnerPasswordCredentialsGrantRequest.GRANT_TYPE,
            RefreshAccessTokenGrantRequest.GRANT_TYPE));
    body.put("subject_types_supported", List.of("public"));
    body.put("id_token_signing_alg_values_supported", List.of("RS256"));

    // body.put("userinfo_endpoint", url("/userinfo"));
    // if you support dynamic client registration
    // body.put("registration_endpoint", url("/connect/register"));
    // body.put("revocation_endpoint", url("/oauth2/revoke"));        // RFC 7009
    // body.put("introspection_endpoint", url("/oauth2/introspect")); // RFC 7662

    // Note: No authorization endpoint on this server, so we intentionally omit
    //       "authorization_endpoint", "response_types_supported", and PKCE fields.
    // body.put("authorization_endpoint", url("/oauth/authorize"));
    // body.put(
    //    "response_types_supported",
    //    List.of(
    //        "code",
    //        "id_token",
    //        "code id_token",
    //        "token id_token",
    //        "code token",
    //        "code token id_token"));

    // body.put(
    //    "token_endpoint_auth_methods_supported",
    //    List.of("client_secret_basic", "client_secret_post", "private_key_jwt"));

    // body.put("code_challenge_methods_supported", List.of("S256"));
    // body.put("claims_supported", List.of(
    //    "sub","iss","aud","exp","iat","auth_time","nonce","acr","amr", "email","email_verified",
    //    "name","given_name","family_name","preferred_username","updated_at"));

    // Optional toggles
    // body.put("request_parameter_supported", false);
    // body.put("request_uri_parameter_supported", false);
    // body.put("claims_parameter_supported", false);

    // Cache for a short period; bump if your keys/config change rarely
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(Duration.ofDays(1)).cachePublic())
        .body(body);
  }

  private String url(String path) {
    return tokenService.getIssuer() + path;
  }
}
