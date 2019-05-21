/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.application;

//~--- non-JDK imports --------------------------------------------------------

import com.codahale.metrics.MetricRegistry;

import com.fasterxml.jackson.databind.SerializationFeature;

import digital.inception.core.configuration.ConfigurationException;
import digital.inception.core.util.CryptoUtil;
import digital.inception.core.util.NetworkUtil;
import digital.inception.json.databind.DateTimeModule;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import org.xnio.Options;
import org.xnio.SslClientAuthMode;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import javax.servlet.ServletContext;

import javax.xml.ws.Endpoint;

/**
 * The <code>ApplicationBase</code> class provides the base class that application classes can be
 * derived from.
 *
 * @author Marcus Portmann
 */
@Component
@ComponentScan(basePackages = { "digital.inception" }, lazyInit = true)
@SuppressWarnings({ "unused", "WeakerAccess" })
public abstract class ApplicationBase
  implements WebApplicationInitializer
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ApplicationBase.class);

  static
  {
    System.setProperty("com.atomikos.icatch.registered", "true");

    try
    {
      System.setProperty("com.atomikos.icatch.tm_unique_name", NetworkUtil.getLocalHostLANAddress()
          .getHostAddress());
    }
    catch (Throwable e)
    {
      logger.error("Failed to set the Atomikos transaction manager unique name", e);
    }
  }

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * Is server security enabled?
   */
  @Value("${server.security.enabled:#{false}}")
  private boolean isServerSecurityEnabled;

  /**
   * Is the Web Services Security X.509 Certificate Token Profile enabled?
   */
  @Value("${webServices.security.x509CertificateTokenProfile.enabled:false}")
  private boolean isWSSX509CertificateTokenProfileEnabled;

  /**
   * The server port.
   */
  @Value("${server.port:#{null}}")
  private Integer serverPort;

  /**
   * The optional server security client authentication mode.
   */
  @Value("${server.security.clientAuthMode:#{null}}")
  private String serverSecurityClientAuthMode;

  /**
   * The server security key store alias.
   */
  @Value("${server.security.keyStore.alias:#{null}}")
  private String serverSecurityKeyStoreAlias;

  /**
   * The server security key store password.
   */
  @Value("${server.security.keyStore.password:#{null}}")
  private String serverSecurityKeyStorePassword;

  /**
   * The server security key store path.
   */
  @Value("${server.security.keyStore.path:#{null}}")
  private String serverSecurityKeyStorePath;

  /**
   * The server security key store type.
   */
  @Value("${server.security.keyStore.type:#{null}}")
  private String serverSecurityKeyStoreType;

  /**
   * The server security port.
   */
  @Value("${server.security.port:#{null}}")
  private Integer serverSecurityPort;

  /**
   * The optional server security trust store password.
   */
  @Value("${server.security.trustStore.password:#{null}}")
  private String serverSecurityTrustStorePassword;

  /**
   * The optional server security trust store path.
   */
  @Value("${server.security.trustStore.path:#{null}}")
  private String serverSecurityTrustStorePath;

  /**
   * The optional server security trust store type.
   */
  @Value("${server.security.trustStore.type:#{null}}")
  private String serverSecurityTrustStoreType;

  /**
   * The Web Services Security X.509 Certificate Token Profile key store alias.
   */
  @Value("${webServices.security.x509CertificateTokenProfile.keyStore.alias:#{null}}")
  private String wssX509CertificateTokenProfileKeyStoreAlias;

  /**
   * The Web Services Security X.509 Certificate Token Profile key store password.
   */
  @Value("${webServices.security.x509CertificateTokenProfile.keyStore.password:#{null}}")
  private String wssX509CertificateTokenProfileKeyStorePassword;

  /**
   * The Web Services Security X.509 Certificate Token Profile key store path.
   */
  @Value("${webServices.security.x509CertificateTokenProfile.keyStore.path:#{null}}")
  private String wssX509CertificateTokenProfileKeyStorePath;

  /**
   * The Web Services Security X.509 Certificate Token Profile key store type.
   */
  @Value("${webServices.security.x509CertificateTokenProfile.keyStore.type:#{null}}")
  private String wssX509CertificateTokenProfileKeyStoreType;

  /**
   * The optional Web Services Security X.509 Certificate Token Profile trust store password.
   */
  @Value("${webServices.security.x509CertificateTokenProfile.trustStore.password:#{null}}")
  private String wssX509CertificateTokenProfileTrustStorePassword;

  /**
   * The optional Web Services Security X.509 Certificate Token Profile trust store path.
   */
  @Value("${webServices.security.x509CertificateTokenProfile.trustStore.path:#{null}}")
  private String wssX509CertificateTokenProfileTrustStorePath;

  /**
   * The optional Web Services Security X.509 Certificate Token Profile trust store type.
   */
  @Value("${webServices.security.x509CertificateTokenProfile.trustStore.type:#{null}}")
  private String wssX509CertificateTokenProfileTrustStoreType;

  /**
   * Constructs a new <code>ApplicationBase</code>.
   *
   * @param applicationContext the Spring application context
   */
  public ApplicationBase(ApplicationContext applicationContext)
  {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the Spring application context.
   *
   * @return the Spring application context
   */
  public ApplicationContext getApplicationContext()
  {
    return applicationContext;
  }

  @Override
  public void onStartup(ServletContext container)
  {
    // Create the 'root' Spring application context
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

    container.addListener(new ContextLoaderListener(rootContext));
  }

  /**
   * Create the web service endpoint.
   * <p/>
   * Requires the Apache CXF framework to have been initialized by adding the
   * <b>org.apache.cxf:cxf-rt-frontend-jaxws</b>, <b>org.apache.cxf:cxf-rt-transports-http</b>
   * <b>org.apache.cxf:cxf-rt-ws-security</b> and <b>org.apache.wss4j:wss4j-ws-security-common</b>
   * Maven dependencies to the project.
   *
   * @param name           the web service name
   * @param implementation the web service implementation
   *
   * @return the web service endpoint
   */
  protected Endpoint createWebServiceEndpoint(String name, Object implementation)
  {
    try
    {
      Class<? extends Endpoint> endpointImplClass = Thread.currentThread().getContextClassLoader()
          .loadClass("org.apache.cxf.jaxws.EndpointImpl").asSubclass(Endpoint.class);

      Class<?> busClass = Thread.currentThread().getContextClassLoader().loadClass(
          "org.apache.cxf.Bus");

      Class<?> springBusClass = Thread.currentThread().getContextClassLoader().loadClass(
          "org.apache.cxf.bus.spring.SpringBus");

      Object springBus = applicationContext.getBean(springBusClass);

      Constructor<? extends Endpoint> constructor = endpointImplClass.getConstructor(busClass,
          Object.class);

      Endpoint endpoint = constructor.newInstance(springBus, implementation);

      Method publishMethod = endpointImplClass.getMethod("publish", String.class);

      publishMethod.invoke(endpoint, "/" + name);

      applicationContext.getAutowireCapableBeanFactory().autowireBean(implementation);

      if (isWSSX509CertificateTokenProfileEnabled)
      {
        try
        {
          if (StringUtils.isEmpty(wssX509CertificateTokenProfileKeyStoreType))
          {
            throw new ConfigurationException(
                "The type was not specified for the server SSL key store");
          }

          if (StringUtils.isEmpty(wssX509CertificateTokenProfileKeyStorePath))
          {
            throw new ConfigurationException(
                "The path was not specified for the server SSL key store");
          }

          if (StringUtils.isEmpty(wssX509CertificateTokenProfileKeyStorePassword))
          {
            throw new ConfigurationException(
                "The password was not specified for the server SSL key store");
          }

          if (StringUtils.isEmpty(wssX509CertificateTokenProfileKeyStoreAlias))
          {
            throw new ConfigurationException(
                "The alias was not specified for the server SSL key store");
          }

          KeyStore keyStore;

          try
          {
            keyStore = CryptoUtil.loadKeyStore(wssX509CertificateTokenProfileKeyStoreType,
                wssX509CertificateTokenProfileKeyStorePath,
                wssX509CertificateTokenProfileKeyStorePassword,
                wssX509CertificateTokenProfileKeyStoreAlias);
          }
          catch (Throwable e)
          {
            throw new ApplicationException(
                "Failed to initialize the Web Services Security X.509 Certificate Token Profile key store",
                e);
          }

          KeyStore trustStore = keyStore;

          if ((!StringUtils.isEmpty(wssX509CertificateTokenProfileTrustStoreType))
              || (!StringUtils.isEmpty(wssX509CertificateTokenProfileTrustStorePath))
              || (!StringUtils.isEmpty(wssX509CertificateTokenProfileTrustStorePassword)))
          {
            if (StringUtils.isEmpty(wssX509CertificateTokenProfileTrustStoreType))
            {
              throw new ConfigurationException(
                  "The type was not specified for the Web Services Security X.509 Certificate Token Profile trust store");
            }

            if (StringUtils.isEmpty(wssX509CertificateTokenProfileTrustStorePath))
            {
              throw new ConfigurationException(
                  "The path was not specified for the Web Services Security X.509 Certificate Token Profile trust store");
            }

            if (StringUtils.isEmpty(wssX509CertificateTokenProfileTrustStorePassword))
            {
              throw new ConfigurationException(
                  "The password was not specified for the Web Services Security X.509 Certificate Token Profile trust store");
            }

            try
            {
              trustStore = CryptoUtil.loadTrustStore(wssX509CertificateTokenProfileTrustStoreType,
                  wssX509CertificateTokenProfileTrustStorePath,
                  wssX509CertificateTokenProfileTrustStorePassword);
            }
            catch (Throwable e)
            {
              throw new ApplicationException(
                  "Failed to initialize the Web Services Security X.509 Certificate Token Profile key store",
                  e);
            }
          }

          Class<?> cxfWSSX509CertificateTokenProfileEndpointConfigurator = Thread.currentThread()
              .getContextClassLoader().loadClass(
              "digital.inception.ws.security.CXFWSSX509CertificateTokenProfileEndpointConfigurator");

          Method configureEndpointMethod =
              cxfWSSX509CertificateTokenProfileEndpointConfigurator.getMethod("configureEndpoint",
              Endpoint.class, KeyStore.class, String.class, String.class, KeyStore.class);

          configureEndpointMethod.invoke(null, endpoint, keyStore,
              wssX509CertificateTokenProfileKeyStorePassword,
              wssX509CertificateTokenProfileKeyStoreAlias, trustStore);
        }
        catch (ClassNotFoundException e)
        {
          throw new ApplicationException(
              "Failed to configure the Web Services Security X.509 Certificate Token Profile for the service ("
              + name + "): The inception-ws library could not be found", e);
        }
        catch (Throwable e)
        {
          throw new ApplicationException(
              "Failed to configure the Web Services Security X.509 Certificate Token Profile for the service ("
              + name + ")", e);
        }
      }

      return endpoint;
    }
    catch (ClassNotFoundException e)
    {
      throw new ApplicationException("Failed to create the endpoint for the service (" + name
          + "): The Apache CXF framework has not been initialized", e);
    }
    catch (Throwable e)
    {
      throw new ApplicationException("Failed to create the endpoint for the service (" + name
          + ")", e);
    }
  }

  /**
   * Returns the insecure HTTP client bean.
   *
   * @return the insecure HTTP client bean
   */
  @Bean
  @ConditionalOnMissingBean(ApplicationSecurityConfiguration.class)
  protected HttpClient insecureHttpClient()
  {
    try
    {
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

      Registry<ConnectionSocketFactory> socketFactoryRegistry =
          RegistryBuilder.<ConnectionSocketFactory>create().register("http",
          new PlainConnectionSocketFactory()).build();

      PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
          socketFactoryRegistry);

      // Increase max total connection to 200
      connectionManager.setMaxTotal(200);

      // Increase default max connection per route to 20
      connectionManager.setDefaultMaxPerRoute(20);

      httpClientBuilder.setConnectionManager(connectionManager);

      return httpClientBuilder.build();
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialize the insecure HTTP client", e);
    }
  }

  /**
   * Returns the REST template bean.
   *
   * @return the REST template bean
   */
  @Bean
  @ConditionalOnMissingBean(ApplicationSecurityConfiguration.class)
  protected RestTemplate insecureRestTemplate()
  {
    try
    {
      HttpComponentsClientHttpRequestFactory requestFactory =
          new HttpComponentsClientHttpRequestFactory();

      requestFactory.setHttpClient(insecureHttpClient());

      return new RestTemplate(requestFactory);
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialize the REST template", e);
    }
  }

  /**
   * Returns the <code>Jackson2ObjectMapperBuilder</code> bean, which configures the Jackson JSON
   * processor package.
   *
   * @return the <code>Jackson2ObjectMapperBuilder</code> bean, which configures the Jackson JSON
   *         processor package
   */
  @Bean
  protected Jackson2ObjectMapperBuilder jacksonBuilder()
  {
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
    jackson2ObjectMapperBuilder.indentOutput(true);

    /*
     * NOTE: Changed the date format to fix timestamps in Spring errors that were only showing a
     *       date. This will make ISO8601 the default date format for JSON serialization and
     *       deserialization of dates. Which may have unintended consequences...
     */
    // jackson2ObjectMapperBuilder.dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    //jackson2ObjectMapperBuilder.dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

    /*
     * NOTE: We have implemented a custom module that supports the JSR310 date/time classes as the
     *       jackson-datatype-jsr310 module provided by Jackson does not handle timezones correctly
     *       for LocalDateTime objects.
     */
    jackson2ObjectMapperBuilder.modulesToInstall(new DateTimeModule());

    return jackson2ObjectMapperBuilder;
  }

  /**
   * Returns the metric registry.
   *
   * @return the metric registry
   */
  @Bean
  protected MetricRegistry metricRegistry()
  {
    return new MetricRegistry();
  }

  /**
   * Returns the secure HTTP client bean.
   *
   * @return the secure HTTP client bean
   */
  @Bean
  @ConditionalOnBean(ApplicationSecurityConfiguration.class)
  protected HttpClient secureHttpClient()
  {
    try
    {
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

      ApplicationSecurityConfiguration applicationSecurityConfiguration =
          applicationContext.getBean(ApplicationSecurityConfiguration.class);

      // Setup the key manager factory
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
          KeyManagerFactory.getDefaultAlgorithm());

      keyManagerFactory.init(applicationSecurityConfiguration.keyStore(),
          applicationSecurityConfiguration.keyStorePassword().toCharArray());

      // Setup the trust manager factory
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
          TrustManagerFactory.getDefaultAlgorithm());

      trustManagerFactory.init(applicationSecurityConfiguration.trustStore());

      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
          new SecureRandom());

      SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
          sslContext);

      Registry<ConnectionSocketFactory> socketFactoryRegistry =
          RegistryBuilder.<ConnectionSocketFactory>create().register("https",
          sslConnectionSocketFactory).register("http", new PlainConnectionSocketFactory()).build();

      PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
          socketFactoryRegistry);

      // Increase max total connection to 200
      connectionManager.setMaxTotal(200);

      // Increase default max connection per route to 20
      connectionManager.setDefaultMaxPerRoute(20);

      httpClientBuilder.setConnectionManager(connectionManager);

      return httpClientBuilder.build();
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialize the secure HTTP client", e);
    }
  }

  /**
   * Returns the secure REST template bean.
   *
   * @return the secure REST template bean
   */
  @Bean
  @ConditionalOnBean(ApplicationSecurityConfiguration.class)
  protected RestTemplate secureRestTemplate()
  {
    try
    {
      HttpComponentsClientHttpRequestFactory requestFactory =
          new HttpComponentsClientHttpRequestFactory();

      requestFactory.setHttpClient(secureHttpClient());

      return new RestTemplate(requestFactory);
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialize the REST template", e);
    }
  }

  /**
   * Returns the embedded servlet container factory used to configure the embedded Undertow servlet
   * container.
   *
   * @return the embedded servlet container factory used to configure the embedded Undertow servlet
   *         container
   */
  @Bean
  protected UndertowServletWebServerFactory undertowServletWebServerFactory()
  {
    UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();

    if (isServerSecurityEnabled)
    {
      factory.addBuilderCustomizers((UndertowBuilderCustomizer) builder ->
          {
            int serverSSLHttpListenerPort = (serverSecurityPort != null)
            ? serverSecurityPort
            : (serverPort != null)
            ? (serverPort >= 15000) && (serverPort < 16000)
            ? serverPort + 1000
            : 8443
            : 8443;

            try
            {
              if (StringUtils.isEmpty(serverSecurityKeyStoreType))
              {
                throw new ConfigurationException(
                    "The type was not specified for the server security key store");
              }

              if (StringUtils.isEmpty(serverSecurityKeyStorePath))
              {
                throw new ConfigurationException(
                    "The path was not specified for the server security key store");
              }

              if (StringUtils.isEmpty(serverSecurityKeyStorePassword))
              {
                throw new ConfigurationException(
                    "The password was not specified for the server security key store");
              }

              if (StringUtils.isEmpty(serverSecurityKeyStoreAlias))
              {
                throw new ConfigurationException(
                    "The alias was not specified for the server security key store");
              }

              KeyStore keyStore;

              try
              {
                keyStore = CryptoUtil.loadKeyStore(serverSecurityKeyStoreType,
                    serverSecurityKeyStorePath, serverSecurityKeyStorePassword,
                    serverSecurityKeyStoreAlias);
              }
              catch (Throwable e)
              {
                throw new ApplicationException("Failed to initialize the server SSL key store", e);
              }

              KeyStore trustStore = keyStore;

              if ((!StringUtils.isEmpty(serverSecurityTrustStoreType))
                  || (!StringUtils.isEmpty(serverSecurityTrustStorePath))
                  || (!StringUtils.isEmpty(serverSecurityTrustStorePassword)))
              {
                if (StringUtils.isEmpty(serverSecurityTrustStoreType))
                {
                  throw new ConfigurationException(
                      "The type was not specified for the server security trust store");
                }

                if (StringUtils.isEmpty(serverSecurityTrustStorePath))
                {
                  throw new ConfigurationException(
                      "The path was not specified for the server security trust store");
                }

                if (StringUtils.isEmpty(serverSecurityTrustStorePassword))
                {
                  throw new ConfigurationException(
                      "The password was not specified for the server security trust store");
                }

                try
                {
                  trustStore = CryptoUtil.loadTrustStore(serverSecurityTrustStoreType,
                      serverSecurityTrustStorePath, serverSecurityTrustStorePassword);
                }
                catch (Throwable e)
                {
                  throw new ApplicationException("Failed to initialize the server SSL key store",
                      e);
                }
              }

              // Setup the key manager factory
              KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                  KeyManagerFactory.getDefaultAlgorithm());

              keyManagerFactory.init(keyStore, serverSecurityKeyStorePassword.toCharArray());

              // Setup the trust manager factory
              TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                  TrustManagerFactory.getDefaultAlgorithm());

              trustManagerFactory.init(trustStore);

              SSLContext sslContext = SSLContext.getInstance("TLS");
              sslContext.init(keyManagerFactory.getKeyManagers(),
                  trustManagerFactory.getTrustManagers(), new SecureRandom());

              builder.addHttpsListener(serverSSLHttpListenerPort, "0.0.0.0", sslContext);

              if (!StringUtils.isEmpty(serverSecurityClientAuthMode))
              {
                if (serverSecurityClientAuthMode.equalsIgnoreCase("required"))
                {
                  builder.setSocketOption(Options.SSL_CLIENT_AUTH_MODE, SslClientAuthMode.REQUIRED);
                }
                else if (serverSecurityClientAuthMode.equalsIgnoreCase("requested"))
                {
                  builder.setSocketOption(Options.SSL_CLIENT_AUTH_MODE, SslClientAuthMode
                      .REQUESTED);
                }
                else
                {
                  builder.setSocketOption(Options.SSL_CLIENT_AUTH_MODE, SslClientAuthMode
                      .NOT_REQUESTED);
                }
              }
              else
              {
                builder.setSocketOption(Options.SSL_CLIENT_AUTH_MODE, SslClientAuthMode
                    .NOT_REQUESTED);
              }
            }
            catch (Throwable e)
            {
              logger.error("Failed to initialize the server SSL HTTP listener on port "
                  + serverSSLHttpListenerPort, e);
            }
          }
          );
    }

    factory.addInitializers();

    return factory;
  }
}
