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

package digital.inception.server.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nimbusds.jose.HeaderParameterNames;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jose.util.JSONObjectUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * The {@code MultiIssuerJwtDecoder} class provides a JWT decoder implementation that is capable of
 * decoding JWTs signed by different issuers using different RSA keys or secret keys.
 *
 * @author Marcus Portmann
 */
public class MultiIssuerJwtDecoder implements JwtDecoder {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(MultiIssuerJwtDecoder.class);

  /** The JWT decoders for the different issuers using different RSA keys or secret keys. */
  private final Map<String, JwtDecoder> jwtDecoders;

  /** Is the management of revoked tokens enabled? */
  private final boolean revokedTokensEnabled;

  /** The external API endpoint used to retrieve the revoked tokens. */
  private final String revokedTokensEndpoint;

  /** The reload period in seconds for revoked tokens. */
  private final int revokedTokensReloadPeriod;

  /** The date and time the revoked tokens should be retrieved. */
  private LocalDateTime retrieveRevokedTokensWhen;

  /** The revoked tokens. */
  private List<RevokedToken> revokedTokens = new ArrayList<>();

  /**
   * Creates a new {@code JwtDecoder} instance.
   *
   * @param jwtDecoders the JWT decoders for the different issuers using different RSA keys or
   *     secret keys
   * @param revokedTokensEnabled is the management of revoked tokens enabled
   * @param revokedTokensEndpoint the external API endpoint used to retrieve the revoked tokens
   * @param revokedTokensReloadPeriod the reload period in seconds for revoked tokens
   */
  MultiIssuerJwtDecoder(
      Map<String, JwtDecoder> jwtDecoders,
      boolean revokedTokensEnabled,
      String revokedTokensEndpoint,
      int revokedTokensReloadPeriod) {
    this.jwtDecoders = jwtDecoders;
    this.revokedTokensEnabled = revokedTokensEnabled;
    this.revokedTokensEndpoint = revokedTokensEndpoint;
    this.revokedTokensReloadPeriod = revokedTokensReloadPeriod;
  }

  @Override
  public Jwt decode(String token) throws JwtException {
    int firstDotPos = token.indexOf(".");

    if (firstDotPos == -1) {
      throw new BadJwtException(
          "Failed to extract the Key ID from the JWT: Missing dot delimiter(s)");
    }

    Base64URL header = new Base64URL(token.substring(0, firstDotPos));

    Map<String, Object> jsonObject;
    try {
      jsonObject = JSONObjectUtils.parse(header.decodeToString());
    } catch (Throwable e) {
      throw new BadJwtException(
          "Failed to extract the Key ID from the JWT: Failed to parse the JSON for the JWT header",
          e);
    }

    String keyId;
    try {
      keyId = JSONObjectUtils.getString(jsonObject, HeaderParameterNames.KEY_ID);

      if (keyId == null) {
        keyId = "";
      }
    } catch (Throwable e) {
      throw new BadJwtException(
          "Failed to extract the Key ID from the JWT: Failed to retrieve the "
              + HeaderParameterNames.KEY_ID
              + " parameter from the JWT header",
          e);
    }

    Jwt jwt;

    if (StringUtils.hasText(keyId)) {
      JwtDecoder jwtDecoder = jwtDecoders.get(keyId);

      if (jwtDecoder != null) {
        jwt = jwtDecoder.decode(token);
      } else {
        jwt = null;
      }
    } else {
      JwtDecoder jwtDecoder = jwtDecoders.get("*");

      if (jwtDecoder != null) {
        jwt = jwtDecoder.decode(token);
      } else {
        jwt = null;
      }
    }

    if (jwt != null) {
      if (revokedTokensEnabled) {
        String jwtId = jwt.getClaimAsString("jti");

        List<RevokedToken> revokedTokens = getRevokedTokens();

        if (revokedTokens == null) {
          throw new JwtException(
              "Failed to verify the JWT with ID ("
                  + jwtId
                  + "): Failed to retrieve the revoked tokens");
        }

        revokedTokens.stream()
            .filter(revokedToken -> revokedToken.id().equals(jwtId))
            .forEach(
                revokedToken -> {
                  throw new BadJwtException(
                      "Failed to verify the revoked JWT with ID (" + jwtId + ")");
                });
      }

      return jwt;
    }

    throw new JwtException("No JWT decoder found to decode JWT with key ID (" + keyId + ")");
  }

  private List<RevokedToken> getRevokedTokens() {
    if (revokedTokensEnabled && StringUtils.hasText(revokedTokensEndpoint)) {
      if ((retrieveRevokedTokensWhen == null)
          || (LocalDateTime.now().isAfter(retrieveRevokedTokensWhen))) {
        try {
          RestTemplate restTemplate = new RestTemplate();

          ResponseEntity<List<RevokedToken>> response =
              restTemplate.exchange(
                  revokedTokensEndpoint,
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<>() {});

          if (response.getStatusCode() == HttpStatus.OK) {
            revokedTokens = response.getBody();

            retrieveRevokedTokensWhen = LocalDateTime.now().plusSeconds(revokedTokensReloadPeriod);

            log.info("Successfully retrieved the revoked tokens");
          } else {
            log.error(
                "Failed to retrieve the revoked tokens using the API endpoint ("
                    + revokedTokensEndpoint
                    + "): "
                    + response);

            revokedTokens = null;
          }
        } catch (Throwable e) {
          log.error(
              "Failed to retrieve the revoked tokens using the API endpoint ("
                  + revokedTokensEndpoint
                  + ")",
              e);
          revokedTokens = null;
        }
      }
    }

    return revokedTokens;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record RevokedToken(
      String id,
      String type,
      String name,
      OffsetDateTime issued,
      LocalDate validFrom,
      LocalDate expires,
      LocalDate revoked) {}
}
