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

package digital.inception.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.inception.core.api.ProblemDetails;
import digital.inception.core.service.Problem;
import digital.inception.core.util.CryptoUtil;
import digital.inception.json.JsonUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

/**
 * The <b>ApiClientUtil</b> class provides API client related utility methods.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class ApiClientUtil {

  /** Private constructor to prevent instantiation. */
  private ApiClientUtil() {}

  /**
   * Create a new web client.
   *
   * @param keyStoreType the key store type when invoking an API using mutual TLS
   * @param keyStorePath the key store path when invoking an API using mutual TLS
   * @param keyStorePassword the key store password when invoking an API using mutual TLS
   * @param keyAlias the key alias when invoking an API using mutual TLS
   * @param keyPassword the key password when invoking an API using mutual TLS
   * @param trustStoreType the trust store type when invoking an API over TLS
   * @param trustStorePath the trust store path when invoking an API over TLS
   * @param trustStorePassword the trust store password when invoking an API over TLS
   * @return the new web client
   */
  public static WebClient createWebClient(
      String keyStoreType,
      String keyStorePath,
      String keyStorePassword,
      String keyAlias,
      String keyPassword,
      String trustStoreType,
      String trustStorePath,
      String trustStorePassword) {
    return createWebClient(
        null,
        keyStoreType,
        keyStorePath,
        keyStorePassword,
        keyAlias,
        keyPassword,
        trustStoreType,
        trustStorePath,
        trustStorePassword);
  }

  /**
   * Create a new web client.
   *
   * @param trustStoreType the trust store type when invoking an API over TLS
   * @param trustStorePath the trust store path when invoking an API over TLS
   * @param trustStorePassword the trust store password when invoking an API over TLS
   * @return the new web client
   */
  public static WebClient createWebClient(
      String trustStoreType, String trustStorePath, String trustStorePassword) {
    return createWebClient(
        null, null, null, null, null, null, trustStoreType, trustStorePath, trustStorePassword);
  }

  /**
   * Create a new web client.
   *
   * @param jwt the Java Web Token (JWT) to use when invoking an API using the web client
   * @param trustStoreType the trust store type when invoking an API over TLS
   * @param trustStorePath the trust store path when invoking an API over TLS
   * @param trustStorePassword the trust store password when invoking an API over TLS
   * @return the new web client
   */
  public static WebClient createWebClient(
      String jwt, String trustStoreType, String trustStorePath, String trustStorePassword) {
    return createWebClient(
        jwt, null, null, null, null, null, trustStoreType, trustStorePath, trustStorePassword);
  }

  /**
   * Create a new web client.
   *
   * @return the new web client
   */
  public static WebClient createWebClient() {
    return createWebClient(null, null, null, null, null, null, null, null, null);
  }

  /**
   * Create a new web client.
   *
   * @param jwt the Java Web Token (JWT) to use when invoking an API using the web client
   * @param keyStoreType the key store type when invoking an API using mutual TLS
   * @param keyStorePath the key store path when invoking an API using mutual TLS
   * @param keyStorePassword the key store password when invoking an API using mutual TLS
   * @param keyAlias the key alias when invoking an API using mutual TLS
   * @param keyPassword the key password when invoking an API using mutual TLS
   * @param trustStoreType the trust store type when invoking an API over TLS
   * @param trustStorePath the trust store path when invoking an API over TLS
   * @param trustStorePassword the trust store password when invoking an API over TLS
   * @return the new web client
   */
  public static WebClient createWebClient(
      String jwt,
      String keyStoreType,
      String keyStorePath,
      String keyStorePassword,
      String keyAlias,
      String keyPassword,
      String trustStoreType,
      String trustStorePath,
      String trustStorePassword) {

    try {
      KeyManagerFactory keyManagerFactory = null;

      if (hasKeyStoreConfiguration(keyStoreType, keyStorePath, keyStorePassword)) {
        keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        keyManagerFactory.init(
            keyStore(keyStoreType, keyStorePath, keyStorePassword), keyStorePassword.toCharArray());
      }

      TrustManagerFactory trustManagerFactory = null;

      if (hasTrustStoreConfiguration(trustStoreType, trustStorePath)) {
        trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        trustManagerFactory.init(trustStore(trustStoreType, trustStorePath, trustStorePassword));
      }

      HttpClient httpClient;

      if ((keyManagerFactory != null) || (trustManagerFactory != null)) {
        SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

        if (keyManagerFactory != null) {
          sslContextBuilder.keyManager(keyManagerFactory);
        }

        if (trustManagerFactory != null) {
          sslContextBuilder.trustManager(trustManagerFactory);
        }

        SslContext sslContext = sslContextBuilder.build();

        httpClient =
            HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
      } else {
        httpClient = HttpClient.create();
      }

      WebClient.Builder webClientBuilder = WebClient.builder();

      ExchangeStrategies strategies =
          ExchangeStrategies.builder()
              .codecs(
                  clientDefaultCodecsConfigurer -> {
                    ObjectMapper objectMapper = JsonUtil.getObjectMapper();

                    clientDefaultCodecsConfigurer
                        .defaultCodecs()
                        .jackson2JsonEncoder(
                            new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientDefaultCodecsConfigurer
                        .defaultCodecs()
                        .jackson2JsonDecoder(
                            new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                  })
              .build();
      webClientBuilder.exchangeStrategies(strategies);

      ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
      webClientBuilder.clientConnector(connector);

      // Add the persistent application JWT to all API calls if configured
      if (jwt != null) {
        webClientBuilder.defaultHeader("Authorization", "Bearer " + jwt);
      }

      return webClientBuilder.build();
    } catch (Throwable e) {
      throw new ApiClientException("Failed to create the web client", e);
    }
  }

  /**
   * Verify whether the web client response exception holds the information for a Problem Details
   * Object, as defined in RFC 7807, which matches the type annotated with the @Problem annotation.
   *
   * <p>The <b>type</b> attribute for the Problem Details Object is checked against the <b>type</b>
   * and <b>typeAlias</b> attributes on the @Problem annotation.
   *
   * @param webClientResponseException the web client response exception
   * @param annotatedClass the Java class annotated with the @Problem annotation
   * @return <b>true</b> if the web client response exception holds the information for a Problem
   *     Details Object that matches the type annotated with the @Problem annotation or <b>false</b>
   *     otherwise
   */
  public static boolean problemMatches(
      WebClientResponseException webClientResponseException, Class<?> annotatedClass) {

    if (webClientResponseException == null || annotatedClass == null) {
      return false;
    }

    Problem problem = annotatedClass.getAnnotation(Problem.class);
    if (problem == null) {
      return false;
    }

    ProblemDetails problemDetails =
        webClientResponseException.getResponseBodyAs(ProblemDetails.class);
    if (problemDetails == null) {
      return false;
    }

    String type = problemDetails.getType();
    return (problem.type().equals(type) || problem.typeAlias().equals(type));
  }

  /**
   * Verify whether the Problem Details Object, as defined in RFC 7807, matches the type annotated
   * with the @Problem annotation.
   *
   * <p>The <b>type</b> attribute for the Problem Details Object is checked against the <b>type</b>
   * and <b>typeAlias</b> attributes on the @Problem annotation.
   *
   * @param problemDetails the Problem Details Object
   * @param annotatedClass the Java class annotated with the @Problem annotation
   * @return <b>true</b> if the Problem Details Object matches the type annotated with the @Problem
   *     annotation or <b>false</b> otherwise
   */
  public static boolean problemMatches(ProblemDetails problemDetails, Class<?> annotatedClass) {

    if (problemDetails == null || annotatedClass == null) {
      return false;
    }

    Problem problem = annotatedClass.getAnnotation(Problem.class);
    if (problem == null) {
      return false;
    }

    String type = problemDetails.getType();
    return (problem.type().equals(type) || problem.typeAlias().equals(type));
  }

  /**
   * Returns whether the key store configuration has been specified.
   *
   * @param keyStoreType the key store type when invoking an API using mutual TLS
   * @param keyStorePath the key store path when invoking an API using mutual TLS
   * @param keyStorePassword the key store password when invoking an API using mutual TLS
   * @return <b>true</b> if the key store configuration has been specified or <b>false</b> otherwise
   */
  private static boolean hasKeyStoreConfiguration(
      String keyStoreType, String keyStorePath, String keyStorePassword) {
    return ((StringUtils.hasText(keyStoreType))
        && (StringUtils.hasText(keyStorePath))
        && (StringUtils.hasText(keyStorePassword)));
  }

  /**
   * Returns whether the trust store configuration has been specified.
   *
   * @param trustStoreType the trust store type when invoking an API over TLS
   * @param trustStorePath the trust store path when invoking an API over TLS
   * @return <b>true</b> if the trust store configuration has been specified or <b>false</b>
   *     otherwise
   */
  private static boolean hasTrustStoreConfiguration(String trustStoreType, String trustStorePath) {
    return ((StringUtils.hasText(trustStoreType)) && (StringUtils.hasText(trustStorePath)));
  }

  /**
   * Returns the key store when invoking an API using mutual TLS.
   *
   * @param keyStoreType the key store type when invoking an API using mutual TLS
   * @param keyStorePath the key store path when invoking an API using mutual TLS
   * @param keyStorePassword the key store password when invoking an API using mutual TLS
   * @return the application key store
   */
  private static KeyStore keyStore(
      String keyStoreType, String keyStorePath, String keyStorePassword) {
    try {
      return CryptoUtil.loadKeyStore(keyStoreType, keyStorePath, keyStorePassword);
    } catch (Throwable e) {
      throw new ApiClientException("Failed to initialize the key store for the web client", e);
    }
  }

  /**
   * Returns the trust store when invoking an API over TLS
   *
   * @param trustStoreType the trust store type when invoking an API over TLS
   * @param trustStorePath the trust store path when invoking an API over TLS
   * @param trustStorePassword the trust store password when invoking an API over TLS
   * @return the trust store when invoking an API over TLS
   */
  private static KeyStore trustStore(
      String trustStoreType, String trustStorePath, String trustStorePassword) {
    try {
      return CryptoUtil.loadTrustStore(trustStoreType, trustStorePath, trustStorePassword);
    } catch (Throwable e) {
      throw new ApiClientException("Failed to initialize the trust store for the web client", e);
    }
  }
}
