/*
 * Copyright 2018 Marcus Portmann
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

import digital.inception.core.configuration.ConfigurationException;
import digital.inception.core.util.CryptoUtil;
import digital.inception.core.util.NetworkUtil;
import digital.inception.core.util.StringUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import org.xnio.Options;
import org.xnio.SslClientAuthMode;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.security.KeyStore;
import java.security.SecureRandom;

import java.text.SimpleDateFormat;

import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import javax.xml.ws.Endpoint;

/**
 * The <code>ApplicationBase</code> class provides the base class that application classes can be
 * derived from.
 *
 * @author Marcus Portmann
 */
@Component
@ComponentScan(basePackages = { "digital.inception" }, lazyInit = true)
@SpringBootApplication
@SuppressWarnings({ "unused", "WeakerAccess" })
public abstract class ApplicationBase
  implements ServletContextInitializer
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
  @Autowired
  private ApplicationContext applicationContext;

  /**
   * The application key store alias.
   */
  @Value("${application.security.keyStore.alias:#{null}}")
  private String applicationKeyStoreAlias;

  /**
   * The application key store password.
   */
  @Value("${application.security.keyStore.password:#{null}}")
  private String applicationKeyStorePassword;

  /**
   * The application key store path.
   */
  @Value("${application.security.keyStore.path:#{null}}")
  private String applicationKeyStorePath;

  /**
   * The application key store type.
   */
  @Value("${application.security.keyStore.type:#{null}}")
  private String applicationKeyStoreType;

  /**
   * The optional application trust store password.
   */
  @Value("${application.security.trustStore.password:#{null}}")
  private String applicationTrustStorePassword;

  /**
   * The optional application trust store path.
   */
  @Value("${application.security.trustStore.path:#{null}}")
  private String applicationTrustStorePath;

  /**
   * The optional application trust store type.
   */
  @Value("${application.security.trustStore.type:#{null}}")
  private String applicationTrustStoreType;

  /**
   * Enable cross-origin resource sharing (CORS).
   */
  @Value("${application.security.enableCORS:#{false}}")
  private boolean enableCORS;

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
   * Configure the given {@link ServletContext} with any servlets, filters, listeners,
   * context-params and attributes necessary for initialization.
   *
   * @param servletContext the {@code ServletContext} to initialize
   */
  @Override
  public void onStartup(ServletContext servletContext)
  {
    try
    {
      Class<? extends Servlet> dispatcherServletClass = Thread.currentThread()
          .getContextClassLoader().loadClass("org.springframework.web.servlet.DispatcherServlet")
          .asSubclass(Servlet.class);

      ServletRegistration dispatcherServlet = servletContext.addServlet("DispatcherServlet",
          (dispatcherServletClass));
      dispatcherServlet.addMapping("/*");

      dispatcherServlet.setInitParameter("contextClass",
          "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");

      logger.info("Initializing the Spring Dispatcher servlet");
    }
    catch (ClassNotFoundException ignored) {}

    try
    {
      Class<? extends Servlet> cxfServletClass = Thread.currentThread().getContextClassLoader()
          .loadClass("org.apache.cxf.transport.servlet.CXFServlet").asSubclass(Servlet.class);

      ServletRegistration cxfServlet = servletContext.addServlet("CXFServlet", (cxfServletClass));
      cxfServlet.addMapping("/service/*");

      logger.info("Initializing the Apache CXF framework");
    }
    catch (ClassNotFoundException ignored) {}
  }

  /**
   * Returns the web services bean factory post processor.
   *
   * @return web services bean factory post processor
   */
  @Bean
  protected static BeanFactoryPostProcessor webServicesBeanFactoryPostProcessor()
  {
    return beanFactory ->
        {
          try
          {
            Class<?> springBusClass = Thread.currentThread().getContextClassLoader().loadClass(
                "org.apache.cxf.bus.spring.SpringBus");

            Object springBus = springBusClass.getConstructor().newInstance();

            beanFactory.registerSingleton("cxf", springBus);
          }
          catch (ClassNotFoundException ignored) {}
          catch (Throwable e)
          {
            throw new FatalBeanException(
                "Failed to initialize the org.apache.cxf.bus.spring.SpringBus bean", e);
          }
        }
        ;
  }

  /**
   * Returns the cross-origin resource sharing (CORS) filter registration bean.
   *
   * @return the cross-origin resource sharing (CORS) filter registration bean
   */
  @Bean
  protected FilterRegistrationBean corsFilterRegistrationBean()
  {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    if (enableCORS)
    {
      CorsConfiguration config = new CorsConfiguration();
      config.applyPermitDefaultValues();
      config.setAllowCredentials(true);
      config.setAllowedOrigins(Arrays.asList("*"));
      config.setAllowedHeaders(Arrays.asList("*"));
      config.setAllowedMethods(Arrays.asList("*"));
      config.setExposedHeaders(Arrays.asList("content-length"));
      config.setMaxAge(3600L);
      source.registerCorsConfiguration("/**", config);
    }

    FilterRegistrationBean<?> bean = new FilterRegistrationBean<>(new CorsFilter(source));
    bean.setOrder(0);

    return bean;
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
  @SuppressWarnings("ConstantConditions")
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
          if (StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileKeyStoreType))
          {
            throw new ConfigurationException(
                "The type was not specified for the server SSL key store");
          }

          if (StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileKeyStorePath))
          {
            throw new ConfigurationException(
                "The path was not specified for the server SSL key store");
          }

          if (StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileKeyStorePassword))
          {
            throw new ConfigurationException(
                "The password was not specified for the server SSL key store");
          }

          if (StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileKeyStoreAlias))
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

          if ((!StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileTrustStoreType))
              || (!StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileTrustStorePath))
              || (!StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileTrustStorePassword)))
          {
            if (StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileTrustStoreType))
            {
              throw new ConfigurationException(
                  "The type was not specified for the Web Services Security X.509 Certificate Token Profile trust store");
            }

            if (StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileTrustStorePath))
            {
              throw new ConfigurationException(
                  "The path was not specified for the Web Services Security X.509 Certificate Token Profile trust store");
            }

            if (StringUtil.isNullOrEmpty(wssX509CertificateTokenProfileTrustStorePassword))
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
   * Returns the HTTP client bean.
   *
   * @return the HTTP client bean
   */
  @Bean
  protected HttpClient httpClient()
  {
    try
    {
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

      if ((!StringUtil.isNullOrEmpty(applicationKeyStoreType))
          || (!StringUtil.isNullOrEmpty(applicationKeyStorePath))
          || (!StringUtil.isNullOrEmpty(applicationKeyStorePassword)))
      {
        if (StringUtil.isNullOrEmpty(applicationKeyStoreType))
        {
          throw new ConfigurationException(
              "The type was not specified for the application key store");
        }

        if (StringUtil.isNullOrEmpty(applicationKeyStorePath))
        {
          throw new ConfigurationException(
              "The path was not specified for the application key store");
        }

        if (StringUtil.isNullOrEmpty(applicationKeyStorePassword))
        {
          throw new ConfigurationException(
              "The password was not specified for the application key store");
        }

        KeyStore keyStore;

        try
        {
          keyStore = CryptoUtil.loadKeyStore(applicationKeyStoreType, applicationKeyStorePath,
              applicationKeyStorePassword);
        }
        catch (Throwable e)
        {
          throw new FatalBeanException("Failed to initialize the application key store", e);
        }

        KeyStore trustStore = null;

        if ((!StringUtil.isNullOrEmpty(applicationTrustStoreType))
            || (!StringUtil.isNullOrEmpty(applicationTrustStorePath))
            || (!StringUtil.isNullOrEmpty(applicationTrustStorePassword)))
        {
          if (StringUtil.isNullOrEmpty(applicationTrustStoreType))
          {
            throw new ConfigurationException(
                "The type was not specified for the application trust store");
          }

          if (StringUtil.isNullOrEmpty(applicationTrustStorePath))
          {
            throw new ConfigurationException(
                "The path was not specified for the application trust store");
          }

          applicationTrustStorePassword = StringUtil.notNull(applicationTrustStorePassword);

          try
          {
            trustStore = CryptoUtil.loadTrustStore(applicationTrustStoreType,
                applicationTrustStorePath, applicationTrustStorePassword);
          }
          catch (Throwable e)
          {
            throw new ApplicationException("Failed to initialize the application trust store", e);
          }
        }

        // Setup the key manager factory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm());

        keyManagerFactory.init(keyStore, applicationKeyStorePassword.toCharArray());

        // Setup the trust manager factory
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm());

        if (trustStore != null)
        {
          trustManagerFactory.init(trustStore);
        }

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
            new SecureRandom());

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
            sslContext);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
            RegistryBuilder.<ConnectionSocketFactory>create().register("https",
            sslConnectionSocketFactory).register("http", new PlainConnectionSocketFactory())
            .build();

        PoolingHttpClientConnectionManager connectionManager =
            new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        httpClientBuilder.setConnectionManager(connectionManager);
      }
      else
      {
        Registry<ConnectionSocketFactory> socketFactoryRegistry =
            RegistryBuilder.<ConnectionSocketFactory>create().register("http",
            new PlainConnectionSocketFactory()).build();

        PoolingHttpClientConnectionManager connectionManager =
            new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        // Increase max total connection to 200
        connectionManager.setMaxTotal(200);

        // Increase default max connection per route to 20
        connectionManager.setDefaultMaxPerRoute(20);

        httpClientBuilder.setConnectionManager(connectionManager);
      }

      return httpClientBuilder.build();
    }
    catch (Throwable e)
    {
      throw new FatalBeanException("Failed to initialize the HTTP client", e);
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
    jackson2ObjectMapperBuilder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
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
   * Returns the REST template bean.
   *
   * @return the REST template bean
   */
  @Bean
  protected RestTemplate restTemplate()
  {
    try
    {
      HttpComponentsClientHttpRequestFactory requestFactory =
          new HttpComponentsClientHttpRequestFactory();

      requestFactory.setHttpClient(httpClient());

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
              if (StringUtil.isNullOrEmpty(serverSecurityKeyStoreType))
              {
                throw new ConfigurationException(
                    "The type was not specified for the server security key store");
              }

              if (StringUtil.isNullOrEmpty(serverSecurityKeyStorePath))
              {
                throw new ConfigurationException(
                    "The path was not specified for the server security key store");
              }

              if (StringUtil.isNullOrEmpty(serverSecurityKeyStorePassword))
              {
                throw new ConfigurationException(
                    "The password was not specified for the server security key store");
              }

              if (StringUtil.isNullOrEmpty(serverSecurityKeyStoreAlias))
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

              if ((!StringUtil.isNullOrEmpty(serverSecurityTrustStoreType))
                  || (!StringUtil.isNullOrEmpty(serverSecurityTrustStorePath))
                  || (!StringUtil.isNullOrEmpty(serverSecurityTrustStorePassword)))
              {
                if (StringUtil.isNullOrEmpty(serverSecurityTrustStoreType))
                {
                  throw new ConfigurationException(
                      "The type was not specified for the server security trust store");
                }

                if (StringUtil.isNullOrEmpty(serverSecurityTrustStorePath))
                {
                  throw new ConfigurationException(
                      "The path was not specified for the server security trust store");
                }

                if (StringUtil.isNullOrEmpty(serverSecurityTrustStorePassword))
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

              if (!StringUtil.isNullOrEmpty(serverSecurityClientAuthMode))
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
