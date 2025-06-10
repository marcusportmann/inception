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

package digital.inception.server.authorization.token.model;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;
import digital.inception.server.authorization.token.exception.OAuth2AccessTokenException;
import java.io.Serial;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * The {@code OAuth2RefreshToken} class holds the information for an OAuth2 refresh token.
 *
 * @author Marcus Portmann
 */
public class OAuth2RefreshToken
    extends org.springframework.security.oauth2.core.OAuth2RefreshToken {

  /** The name of the scope claim. */
  public static final String SCOPE_CLAIM = "scope";

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new {@code OAuth2RefreshToken}.
   *
   * @param tokenValue the token value
   * @param issuedAt the time at which the token was issued
   */
  public OAuth2RefreshToken(String tokenValue, Instant issuedAt) {
    super(tokenValue, issuedAt);
  }

  /**
   * Build a new {@code }OAuth2RefreshToken}.
   *
   * @param username the username for the user the token is being issued for
   * @param scopes the scope(s) associated to the token
   * @param validFor the number of seconds the token should be valid for
   * @param rsaKeyId the ID of the RSA key used to sign the token
   * @param rsaPrivateKey the RSA private key used to sign the token
   * @return the OAuth2 refresh token
   */
  public static OAuth2RefreshToken build(
      String username,
      Set<String> scopes,
      int validFor,
      String rsaKeyId,
      RSAPrivateKey rsaPrivateKey) {
    try {
      Instant issuedAt = Instant.now();
      Instant expiresAt = issuedAt.plusSeconds(validFor);

      JWSSigner signer = new RSASSASigner(rsaPrivateKey);

      JWTClaimsSet.Builder jwtClaimsSetBuilder = new Builder();

      jwtClaimsSetBuilder.subject(username);

      if ((scopes != null) && (!scopes.isEmpty())) {
        jwtClaimsSetBuilder.claim(
            SCOPE_CLAIM, StringUtils.collectionToDelimitedString(scopes, " "));
      }

      jwtClaimsSetBuilder.issueTime(Date.from(issuedAt));

      jwtClaimsSetBuilder.expirationTime(Date.from(expiresAt));

      SignedJWT signedJWT =
          new SignedJWT(
              new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKeyId).build(),
              jwtClaimsSetBuilder.build());

      signedJWT.sign(signer);

      return new OAuth2RefreshToken(signedJWT.serialize(), issuedAt);
    } catch (Throwable e) {
      throw new OAuth2AccessTokenException("Failed to build the OAuth2 refresh token", e);
    }
  }
}
