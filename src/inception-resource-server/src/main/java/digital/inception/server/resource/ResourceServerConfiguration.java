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

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import digital.inception.core.util.ResourceUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * The <b>ResourceServerConfiguration</b> class provides the Spring configuration for the Inception
 * Resource Server module.
 *
 * @author Marcus Portmann
 */
@Configuration
@EnableWebSecurity
@ConditionalOnWebApplication(type = Type.ANY)
@ConfigurationProperties(prefix = "inception.resource-server", ignoreUnknownFields = false)
@EnableConfigurationProperties
public class ResourceServerConfiguration {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ResourceServerConfiguration.class);

  /** The JWT configuration. */
  private JwtConfiguration jwtConfiguration;

  /** The configuration for the policy decision point. */
  private PolicyDecisionPointConfiguration policyDecisionPoint;

  @Autowired private ResourceLoader resourceLoader;

  /** Constructs a new <b>ResourceServerConfiguration</b>. */
  public ResourceServerConfiguration() {}

  /**
   * Returns the security filter chain.
   *
   * @param httpSecurity the HTTP security
   * @return the security filter chain
   * @throws Exception if the filter chain could not be initialized
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .httpBasic(Customizer.withDefaults())
        .csrf(
            csrfCustomizer -> {
              csrfCustomizer.disable();
            })
        .authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers(antMatcher("/**"))
                    .permitAll()
                    .requestMatchers(antMatcher("/api/**"))
                    .authenticated())
        .oauth2ResourceServer(
            oauth2ResourceServerCustomizer ->
                oauth2ResourceServerCustomizer.jwt(
                    jwt -> {
                      jwt.decoder(getJwtDecoder());
                      jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter());
                    }))
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return httpSecurity.build();
  }

  /**
   * Returns the JWT configuration.
   *
   * @return the JWT configuration
   */
  public JwtConfiguration getJwt() {
    return this.jwtConfiguration;
  }

  /**
   * Returns the configuration for the policy decision point.
   *
   * @return the configuration for the policy decision point
   */
  public PolicyDecisionPointConfiguration getPolicyDecisionPoint() {
    return policyDecisionPoint;
  }

  /**
   * Set the JWT configuration.
   *
   * @param jwtConfiguration the JWT configuration
   */
  public void setJwt(JwtConfiguration jwtConfiguration) {
    this.jwtConfiguration = jwtConfiguration;
  }

  /**
   * Set the configuration for the policy decision point.
   *
   * @param policyDecisionPoint the configuration for the policy decision point
   */
  public void setPolicyDecisionPoint(PolicyDecisionPointConfiguration policyDecisionPoint) {
    this.policyDecisionPoint = policyDecisionPoint;
  }

  private Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        getJwtGrantedAuthoritiesConverter());
    return jwtAuthenticationConverter;
  }

  private JwtDecoder getJwtDecoder() {

    if (jwtConfiguration.getRsaPublicKey() != null) {
      logger.info("Using a RS256 RSA public key JWT decoder");

      RSAPublicKey jwtRsaPublicKey =
          ResourceUtil.getRSAPublicKeyResource(resourceLoader, jwtConfiguration.getRsaPublicKey());

      return NimbusJwtDecoder.withPublicKey(jwtRsaPublicKey)
          .signatureAlgorithm(SignatureAlgorithm.from("RS256"))
          .build();
    } else if (StringUtils.hasText(jwtConfiguration.getSecretKey())) {
      logger.info("Using a HS256 secret key JWT decoder");

      SecretKeySpec secretKeySpec =
          new SecretKeySpec(jwtConfiguration.getSecretKey().getBytes(), MacAlgorithm.HS256.name());
      return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
    } else {
      Map<String, JwtDecoder> jwtDecoders = new HashMap<>();

      for (JwtKeyConfiguration jwtKeyConfiguration : jwtConfiguration.getKeys()) {
        try {
          switch (jwtKeyConfiguration.getAlgorithm()) {
            case "RS256":
              if (!StringUtils.hasText(jwtKeyConfiguration.getLocation())) {
                throw new BeanInitializationException(
                    "No location specified for the JWT key (" + jwtKeyConfiguration.id + ")");
              }

              // Initialize and store the JWT decoder using the RSA public key
              try {
                RSAPublicKey jwtRsaPublicKey =
                    ResourceUtil.getRSAPublicKeyResource(
                        resourceLoader, jwtKeyConfiguration.getLocation());

                jwtDecoders.put(
                    jwtKeyConfiguration.getId(),
                    NimbusJwtDecoder.withPublicKey(jwtRsaPublicKey)
                        .signatureAlgorithm(SignatureAlgorithm.from("RS256"))
                        .build());
              } catch (Throwable e) {
                throw new BeanInitializationException(
                    "Failed to initialize the JWT decoder for the key ("
                        + jwtKeyConfiguration.getId()
                        + ") using the RSA public key",
                    e);
              }

              break;

            case "HS256":
              try {
                SecretKeySpec secretKeySpec;
                if (StringUtils.hasText(jwtKeyConfiguration.getData())) {
                  secretKeySpec =
                      new SecretKeySpec(
                          jwtKeyConfiguration.getData().getBytes(), MacAlgorithm.HS256.name());
                } else if (StringUtils.hasText(jwtKeyConfiguration.getLocation())) {
                  String keyData =
                      StreamUtils.copyToString(
                          resourceLoader
                              .getResource(jwtKeyConfiguration.getLocation())
                              .getInputStream(),
                          StandardCharsets.UTF_8);
                  secretKeySpec = new SecretKeySpec(keyData.getBytes(), MacAlgorithm.HS256.name());
                } else {
                  throw new BeanInitializationException(
                      "No data or location specified for the JWT key ("
                          + jwtKeyConfiguration.id
                          + ")");
                }

                jwtDecoders.put(
                    jwtKeyConfiguration.getId(),
                    NimbusJwtDecoder.withSecretKey(secretKeySpec).build());
              } catch (Throwable e) {
                throw new BeanInitializationException(
                    "Failed to initialize the JWT decoder for the key ("
                        + jwtKeyConfiguration.getId()
                        + ") using the secret key",
                    e);
              }

              break;

            default:
              throw new BeanInitializationException(
                  "Failed to initialize the JWT decoder for the key ("
                      + jwtKeyConfiguration.getId()
                      + ") with the unrecognized algorithm ("
                      + jwtKeyConfiguration.getAlgorithm()
                      + ")");
          }
        } catch (Throwable e) {
          throw new BeanInitializationException(
              "Failed to initialize the JWT decoder for the key ("
                  + jwtKeyConfiguration.getId()
                  + ")",
              e);
        }
      }

      logger.info("Using a multi-issuer JWT decoder with " + jwtDecoders.size() + " decoders");

      if (jwtConfiguration.getRevokedTokens() != null) {
        return new MultiIssuerJwtDecoder(
            jwtDecoders,
            jwtConfiguration.getRevokedTokens().getEnabled(),
            jwtConfiguration.getRevokedTokens().getEndpoint(),
            jwtConfiguration.getRevokedTokens().getReloadPeriod());
      } else {
        return new MultiIssuerJwtDecoder(jwtDecoders, false, null, -1);
      }
    }
  }

  private Converter<Jwt, Collection<GrantedAuthority>> getJwtGrantedAuthoritiesConverter() {
    return new JwtGrantedAuthoritiesConverter();
  }

  /**
   * The <b>ClasspathPoliciesConfiguration</b> class holds the classpath policies configuration for
   * the policy decision point.
   *
   * @author Marcus Portmann
   */
  public static class ClasspathPoliciesConfiguration {

    /** Should policy sets and policies be loaded from the classpath. */
    private boolean enabled;

    /** Constructs a new <b>ClasspathPoliciesConfiguration</b>. */
    public ClasspathPoliciesConfiguration() {}

    /**
     * Returns whether policy sets and policies should be loaded from the classpath.
     *
     * @return <b>true</b> if policy sets and policies should be loaded from the classpath or
     *     <b>false</b> otherwise
     */
    public boolean getEnabled() {
      return enabled;
    }

    /**
     * Set whether policy sets and policies should be loaded from the classpath.
     *
     * @param enabled <b>true</b> if policy sets and policies should be loaded from the classpath or
     *     <b>false</b> otherwise
     */
    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }
  }

  /**
   * The <b>ExternalPoliciesConfiguration</b> class holds the external policies configuration for
   * the policy decision point.
   *
   * @author Marcus Portmann
   */
  public static class ExternalPoliciesConfiguration {

    /** Should policy sets and policies be loaded by invoking an external API. */
    private boolean enabled;

    /** The external API endpoint used to retrieve policy sets and policies. */
    private String endpoint;

    /** The reload period in seconds for external policy sets and policies. */
    private int reloadPeriod = 43200;

    /** Constructs a new <b>ExternalPoliciesConfiguration</b>. */
    public ExternalPoliciesConfiguration() {}

    /**
     * Returns whether policy sets and policies should be loaded by invoking an external API.
     *
     * @return <b>true</b> if policy sets and policies should be loaded by invoking an external API
     *     or <b>false</b> otherwise
     */
    public boolean getEnabled() {
      return enabled;
    }

    /**
     * Returns the external API endpoint used to retrieve policy sets and policies.
     *
     * @return the external API endpoint used to retrieve policy sets and policies
     */
    public String getEndpoint() {
      return endpoint;
    }

    /**
     * Returns the reload period in seconds for external policy sets and policies.
     *
     * @return the reload period in seconds for external policy sets and policies
     */
    public int getReloadPeriod() {
      return reloadPeriod;
    }

    /**
     * Set whether policy sets and policies should be loaded by invoking an external API.
     *
     * @param enabled <b>true</b> if policy sets and policies should be loaded by invoking an
     *     external API or <b>false</b> otherwise
     */
    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    /**
     * Set the external API endpoint used to retrieve policy sets and policies.
     *
     * @param endpoint the external API endpoint used to retrieve policy sets and policies
     */
    public void setEndpoint(String endpoint) {
      this.endpoint = endpoint;
    }

    /**
     * Set the reload period in seconds for external policy sets and policies.
     *
     * @param reloadPeriod the reload period in seconds for external policy sets and policies
     */
    public void setReloadPeriod(int reloadPeriod) {
      this.reloadPeriod = reloadPeriod;
    }
  }

  /**
   * The <b>JwtConfiguration</b> class holds the JWT-related configuration for a resource server.
   *
   * @author Marcus Portmann
   */
  public static class JwtConfiguration {

    /** The configuration for the keys used to verify JWTs. */
    private List<JwtKeyConfiguration> keys;

    /** The configuration for managing revoked tokens. */
    private JwtRevokedTokensConfiguration revokedTokens;

    /* The location of the RSA public key used to verify the JWTs. */
    private String rsaPublicKey;

    /* The secret key used to verify the JWTs. */
    private String secretKey;

    /** Constructs a new <b>JwtConfiguration</b>. */
    public JwtConfiguration() {}

    /**
     * Returns the configuration for the keys used to verify JWTs.
     *
     * @return the configuration for the keys used to verify JWTs
     */
    public List<JwtKeyConfiguration> getKeys() {
      return keys;
    }

    /**
     * Returns the configuration for managing revoked tokens.
     *
     * @return the configuration for managing revoked tokens
     */
    public JwtRevokedTokensConfiguration getRevokedTokens() {
      return revokedTokens;
    }

    /**
     * Returns the location of the RSA public key used to verify the JWT.
     *
     * @return the location of the RSA public key used to verify the JWT
     */
    public String getRsaPublicKey() {
      return rsaPublicKey;
    }

    /**
     * Returns the secret key used to verify the JWTs.
     *
     * @return the secret key used to verify the JWTs
     */
    public String getSecretKey() {
      return secretKey;
    }

    /**
     * Se the configuration for the keys used to verify JWTs.
     *
     * @param keys the configuration for the keys used to verify JWTs
     */
    public void setKeys(List<JwtKeyConfiguration> keys) {
      this.keys = keys;
    }

    /**
     * Set the configuration for managing revoked tokens.
     *
     * @param revokedTokens the configuration for managing revoked tokens
     */
    public void setRevokedTokens(JwtRevokedTokensConfiguration revokedTokens) {
      this.revokedTokens = revokedTokens;
    }

    /**
     * Set the location of the RSA public key used to verify the JWT.
     *
     * @param rsaPublicKey the location of the RSA public key used to verify the JWT
     */
    public void setRsaPublicKey(String rsaPublicKey) {
      this.rsaPublicKey = rsaPublicKey;
    }

    /**
     * Set the secret key used to verify the JWTs.
     *
     * @param secretKey the secret key used to verify the JWTs
     */
    public void setSecretKey(String secretKey) {
      this.secretKey = secretKey;
    }
  }

  /**
   * The <b>JwtKeyConfiguration</b> class holds the configuration for a key used to verify JWTs.
   *
   * @author Marcus Portmann
   */
  public static class JwtKeyConfiguration {

    /**
     * The algorithm for the key.
     *
     * <p>This is one of the algorithms defined by the JSON Web Algorithms (JWA) specification, e.g.
     * RS256.
     */
    @NotBlank
    @Pattern(regexp = "(HS256|RS256)")
    private String algorithm;

    /**
     * The data for the key.
     *
     * <p>For example, the HS256 shared secret key.
     */
    private String data;

    /**
     * The unique ID of the key.
     *
     * <p>This matches the "kid" claim in a JWT token.
     */
    @NotBlank private String id;

    /**
     * The location of the file containing the data for the key.
     *
     * <p>For example, the file on the classpath containing the RS256 RSA public key data or the
     * HS256 shared secret key.
     */
    private String location;

    /** Constructs a new <b>JwtKey</b>. */
    public JwtKeyConfiguration() {}

    /**
     * Returns the algorithm for the key.
     *
     * <p>This is one of the algorithms defined by the JSON Web Algorithms (JWA) specification, e.g.
     * RS256.
     *
     * @return the algorithm for the key
     */
    public String getAlgorithm() {
      return algorithm;
    }

    /**
     * Returns the data for the key.
     *
     * <p>For example, the HS256 shared secret key.
     *
     * @return the data for the key
     */
    public String getData() {
      return data;
    }

    /**
     * Returns the unique ID of the key.
     *
     * <p>This matches the "kid" claim in a JWT token.
     *
     * @return the unique ID of the key
     */
    public String getId() {
      return id;
    }

    /**
     * Returns the location of the file containing the data for the key.
     *
     * <p>For example, the file on the classpath containing the RS256 RSA public key data or the
     * HS256 shared secret key.
     *
     * @return the location of the file containing the data for the key
     */
    public String getLocation() {
      return location;
    }

    /**
     * Set the algorithm for the key.
     *
     * <p>This is one of the algorithms defined by the JSON Web Algorithms (JWA) specification, e.g.
     * RS256.
     *
     * @param algorithm the algorithm for the key
     */
    public void setAlgorithm(String algorithm) {
      this.algorithm = algorithm;
    }

    /**
     * Set the data for the key.
     *
     * <p>For example, the HS256 shared secret key.
     *
     * @param data the data for the key
     */
    public void setData(String data) {
      this.data = data;
    }

    /**
     * Set the unique ID of the key.
     *
     * <p>This matches the "kid" claim in a JWT token.
     *
     * @param id the unique ID of the key
     */
    public void setId(String id) {
      this.id = id;
    }

    /**
     * Set the location of the file containing the data for the key.
     *
     * <p>For example, the file on the classpath containing the RS256 RSA public key data or the
     * HS256 shared secret key.
     *
     * @param location the location of the file containing the data for the key
     */
    public void setLocation(String location) {
      this.location = location;
    }
  }

  /**
   * The <b>JwtRevokedTokens</b> class holds the configuration for managing revoked tokens.
   *
   * @author Marcus Portmann
   */
  public static class JwtRevokedTokensConfiguration {

    /** Is support for revoked tokens enabled? */
    private boolean enabled;

    /** The external API endpoint used to retrieve the revoked tokens. */
    private String endpoint;

    /** The reload period in seconds for revoked tokens. */
    private int reloadPeriod = 43200;

    /** Constructs a new <b>JwtRevokedTokensConfiguration</b>. */
    public JwtRevokedTokensConfiguration() {}

    /**
     * Returns whether support for revoked tokens is enabled.
     *
     * @return <b>true</b> if support for revoked tokens is enabled or <b>false</b> otherwise
     */
    public boolean getEnabled() {
      return enabled;
    }

    /**
     * Returns the external API endpoint used to retrieve the revoked tokens.
     *
     * @return the external API endpoint used to retrieve the revoked tokens
     */
    public String getEndpoint() {
      return endpoint;
    }

    /**
     * Returns the reload period in seconds for revoked tokens.
     *
     * @return the reload period in seconds for revoked tokens
     */
    public int getReloadPeriod() {
      return reloadPeriod;
    }

    /**
     * Set whether support for revoked tokens is enabled.
     *
     * @param enabled <b>true</b> if support for revoked tokens is enabled or <b>false</b> otherwise
     */
    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    /**
     * Set the external API endpoint used to retrieve the revoked tokens.
     *
     * @param endpoint the external API endpoint used to retrieve the revoked tokens
     */
    public void setEndpoint(String endpoint) {
      this.endpoint = endpoint;
    }

    /**
     * Set the reload period in seconds for revoked tokens.
     *
     * @param reloadPeriod the reload period in seconds for revoked tokens
     */
    public void setReloadPeriod(int reloadPeriod) {
      this.reloadPeriod = reloadPeriod;
    }
  }

  /**
   * The <b>PolicyDecisionPointConfiguration</b> class holds the configuration for the policy
   * decision point.
   *
   * @author Marcus Portmann
   */
  public static class PolicyDecisionPointConfiguration {

    /** The classpath policies configuration for the policy decision point. */
    private ClasspathPoliciesConfiguration classpathPolicies;

    /** The external policies configuration for the policy decision point. */
    private ExternalPoliciesConfiguration externalPolicies;

    /** Constructs a new <b>PolicyDecisionPointConfiguration</b>. */
    public PolicyDecisionPointConfiguration() {}

    /**
     * Returns the classpath policies configuration for the policy decision point.
     *
     * @return the classpath policies configuration for the policy decision point
     */
    public ClasspathPoliciesConfiguration getClasspathPolicies() {
      return classpathPolicies;
    }

    /**
     * Returns the external policies configuration for the policy decision point.
     *
     * @return the external policies configuration for the policy decision point
     */
    public ExternalPoliciesConfiguration getExternalPolicies() {
      return externalPolicies;
    }

    /**
     * Set the classpath policies configuration for the policy decision point.
     *
     * @param classpathPolicies the classpath policies configuration for the policy decision point
     */
    public void setClasspathPolicies(ClasspathPoliciesConfiguration classpathPolicies) {
      this.classpathPolicies = classpathPolicies;
    }

    /**
     * Set the external policies configuration for the policy decision point.
     *
     * @param externalPolicies the external policies configuration for the policy decision point
     */
    public void setExternalPolicies(ExternalPoliciesConfiguration externalPolicies) {
      this.externalPolicies = externalPolicies;
    }
  }
}
