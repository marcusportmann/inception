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

package digital.inception.server.authorization;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The <b>AuthorizationServerConfiguration</b> class provides the Spring configuration for the
 * Inception Authorization Server module.
 *
 * @author Marcus Portmann
 */
@Configuration
public class AuthorizationServerConfiguration {

  /* The RSA private key used to sign the JWTs. */
  @Value("${inception.authorization-server.jwt.rsa-private-key}")
  private RSAPrivateKey jwtRsaPrivateKey;

  /* The RSA public key used to verify the JWTs. */
  @Value("${inception.authorization-server.jwt.rsa-public-key}")
  private RSAPublicKey jwtRsaPublicKey;

  /** Constructs a new <b>AuthorizationServerConfiguration</b>. */
  public AuthorizationServerConfiguration() {}
}
