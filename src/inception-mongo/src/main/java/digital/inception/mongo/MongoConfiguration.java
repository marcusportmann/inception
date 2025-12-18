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

package digital.inception.mongo;

import com.mongodb.ConnectionString;
import digital.inception.core.CoreConfiguration;
import digital.inception.core.converter.CodeEnumToStringConverter;
import digital.inception.core.converter.DateToLocalDateTimeConverter;
import digital.inception.core.converter.InstantToStringConverter;
import digital.inception.core.converter.LocalDateTimeToDateConverter;
import digital.inception.core.converter.LocalDateTimeToStringConverter;
import digital.inception.core.converter.LocalDateToStringConverter;
import digital.inception.core.converter.LocalTimeToStringConverter;
import digital.inception.core.converter.OffsetDateTimeToStringConverter;
import digital.inception.core.converter.OffsetTimeToStringConverter;
import digital.inception.core.converter.StringToCodeEnumConverterFactory;
import digital.inception.core.converter.StringToInstantConverter;
import digital.inception.core.converter.StringToLocalDateConverter;
import digital.inception.core.converter.StringToLocalDateTimeConverter;
import digital.inception.core.converter.StringToLocalTimeConverter;
import digital.inception.core.converter.StringToOffsetDateTimeConverter;
import digital.inception.core.converter.StringToOffsetTimeConverter;
import digital.inception.core.converter.StringToZonedDateTimeConverter;
import digital.inception.core.converter.UUIDToStringConverter;
import digital.inception.core.converter.ZonedDateTimeToStringConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * The {@code MongoConfiguration} class provides the Spring configuration for the Mongo module.
 *
 * <p>This module customizes the default automatic initialization performed by the
 * spring-boot-starter-data-mongodb Spring Boot starter.
 */
@Configuration
@Import(CoreConfiguration.class)
@ComponentScan(basePackages = {"digital.inception"})
public class MongoConfiguration {

  /** Constructs a new {@code MongoConfiguration}. */
  public MongoConfiguration() {}

  /**
   * Returns the application GridFsTemplate that leverages the application mongo database factory.
   *
   * @param applicationMongoDatabaseFactory the application mongo database factory
   * @param applicationMongoTemplate the application mongo template
   * @return the application GridFsTemplate that leverages the application mongo database factory
   */
  @Bean("applicationGridFsTemplate")
  @Primary
  @Conditional(ApplicationMongoDatabaseFactoryCondition.class)
  public GridFsTemplate applicationGridFsTemplate(
      @Qualifier("applicationMongoDatabaseFactory")
          org.springframework.data.mongodb.MongoDatabaseFactory applicationMongoDatabaseFactory,
      @Qualifier("applicationMongoTemplate") MongoTemplate applicationMongoTemplate) {
    return new GridFsTemplate(
        applicationMongoDatabaseFactory, applicationMongoTemplate.getConverter());
  }

  /**
   * Returns the application MongoConverter that leverages the application mongo database factory.
   *
   * @param applicationMongoDatabaseFactory the application mongo database factory
   * @param mongoCustomConversions the MongoCustomConversions
   * @return the application MongoConverter that leverages the application mongo database factory
   */
  @Bean("applicationMongoConverter")
  @Primary
  @Conditional(ApplicationMongoDatabaseFactoryCondition.class)
  public MongoConverter applicationMongoConverter(
      @Qualifier("applicationMongoDatabaseFactory")
          org.springframework.data.mongodb.MongoDatabaseFactory applicationMongoDatabaseFactory,
      MongoCustomConversions mongoCustomConversions) {
    return MongoUtil.createMongoConverter(applicationMongoDatabaseFactory, mongoCustomConversions);
  }

  /**
   * Returns the application mongo database factory initialized from the
   * inception.application.mongodb configuration if it is available.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param applicationMongoDbUri the URI for the application MongoDB database
   * @param applicationMongoDbEmbeddedServerVersion the version of the embedded MongoDB server to
   *     use for the application or blank if the embedded MongoDB server should not be used
   * @return the application mongo database factory initialized from the
   *     inception.application.mongodb configuration if it is available
   */
  @Bean("applicationMongoDatabaseFactory")
  @Primary
  @Conditional(ApplicationMongoDatabaseFactoryCondition.class)
  public org.springframework.data.mongodb.MongoDatabaseFactory applicationMongoDatabaseFactory(
      ApplicationContext applicationContext,
      @Value("${inception.application.mongodb.uri}") String applicationMongoDbUri,
      @Value("${inception.application.mongodb.embedded:}")
          String applicationMongoDbEmbeddedServerVersion) {
    return initMongoDatabaseFactory(
        applicationContext, applicationMongoDbUri, applicationMongoDbEmbeddedServerVersion);
  }

  /**
   * Returns the application MongoTemplate that leverages the application mongo database factory.
   *
   * @param applicationMongoDatabaseFactory the application mongo database factory
   * @param applicationMongoConverter the application mongo converter
   * @return the application MongoTemplate that leverages the application mongo database factory
   */
  @Bean("applicationMongoTemplate")
  @Primary
  @Conditional(ApplicationMongoDatabaseFactoryCondition.class)
  public MongoTemplate applicationMongoTemplate(
      @Qualifier("applicationMongoDatabaseFactory")
          org.springframework.data.mongodb.MongoDatabaseFactory applicationMongoDatabaseFactory,
      @Qualifier("applicationMongoConverter") MongoConverter applicationMongoConverter) {
    return new MongoTemplate(applicationMongoDatabaseFactory, applicationMongoConverter);
  }

  /**
   * Returns the default GridFsTemplate that leverages the default mongo database factory.
   *
   * @param defaultMongoDatabaseFactory the default mongo database factory
   * @param defaultMongoTemplate the default mongo template
   * @return the default GridFsTemplate that leverages the default mongo database factory
   */
  @Bean("defaultGridFsTemplate")
  @Primary
  @Conditional(DefaultMongoDatabaseFactoryCondition.class)
  public GridFsTemplate defaultGridFsTemplate(
      @Qualifier("defaultMongoDatabaseFactory")
          org.springframework.data.mongodb.MongoDatabaseFactory defaultMongoDatabaseFactory,
      @Qualifier("defaultMongoTemplate") MongoTemplate defaultMongoTemplate) {
    return new GridFsTemplate(defaultMongoDatabaseFactory, defaultMongoTemplate.getConverter());
  }

  /**
   * Returns the default MongoConverter that leverages the default mongo database factory.
   *
   * @param defaultMongoDatabaseFactory the default mongo database factory
   * @param mongoCustomConversions the MongoCustomConversions
   * @return the default MongoConverter that leverages the default mongo database factory
   */
  @Bean("defaultMongoConverter")
  @Primary
  @Conditional(DefaultMongoDatabaseFactoryCondition.class)
  public MongoConverter defaultMongoConverter(
      @Qualifier("defaultMongoDatabaseFactory")
          org.springframework.data.mongodb.MongoDatabaseFactory defaultMongoDatabaseFactory,
      MongoCustomConversions mongoCustomConversions) {
    return MongoUtil.createMongoConverter(defaultMongoDatabaseFactory, mongoCustomConversions);
  }

  /**
   * Returns the default mongo database factory initialized from the spring.data.mongodb
   * configuration if it is available.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param springMongoDbUri the URI for the Spring Data MongoDB database
   * @param springMongoDbEmbeddedServerVersion the version of the embedded MongoDB server to use for
   *     the Spring Data MongoDB database or blank if the embedded MongoDB server should not be used
   * @return the default mongo database factory initialized from the spring.data.mongodb
   *     configuration if it is available
   */
  @Bean("defaultMongoDatabaseFactory")
  @Primary
  @Conditional(DefaultMongoDatabaseFactoryCondition.class)
  public org.springframework.data.mongodb.MongoDatabaseFactory defaultMongoDatabaseFactory(
      ApplicationContext applicationContext,
      @Value("${spring.data.mongodb.uri}") String springMongoDbUri,
      @Value("${spring.data.mongodb.embedded:}") String springMongoDbEmbeddedServerVersion) {
    return initMongoDatabaseFactory(
        applicationContext, springMongoDbUri, springMongoDbEmbeddedServerVersion);
  }

  /**
   * Returns the default MongoTemplate that leverages the default mongo database factory.
   *
   * @param defaultMongoDatabaseFactory the default mongo database factory
   * @param defaultMongoConverter the default mongo converter
   * @return the default MongoTemplate that leverages the default mongo database factory
   */
  @Bean("defaultMongoTemplate")
  @Primary
  @Conditional(DefaultMongoDatabaseFactoryCondition.class)
  public MongoTemplate defaultMongoTemplate(
      @Qualifier("defaultMongoDatabaseFactory")
          org.springframework.data.mongodb.MongoDatabaseFactory defaultMongoDatabaseFactory,
      @Qualifier("defaultMongoConverter") MongoConverter defaultMongoConverter) {
    return new MongoTemplate(defaultMongoDatabaseFactory, defaultMongoConverter);
  }

  /**
   * Returns the MongoCustomConversions initialized with the writer converters and reader converters
   * detected by scanning the classpath for classes annotated with the @WriterConverter
   * and @ReaderConverter annotations.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @return the MongoCustomConversions initialized with the writer converters and reader converters
   *     detected by scanning the classpath for classes annotated with the @WriterConverter
   *     and @ReaderConverter annotations
   */
  @Bean
  public MongoCustomConversions mongoCustomConversions(ApplicationContext applicationContext) {
    // Preserve insertion order but avoid duplicates by class
    List<Object> converters = new ArrayList<>();
    java.util.Set<Class<?>> seen = new java.util.LinkedHashSet<>();

    java.util.function.Consumer<Object> addIfAbsent =
        c -> {
          if (c != null && seen.add(c.getClass())) {
            converters.add(c);
          }
        };

    // Find all beans annotated with @WritingConverter
    Map<String, Object> writingConverters =
        applicationContext.getBeansWithAnnotation(WritingConverter.class);
    writingConverters.values().forEach(addIfAbsent);

    // Find all beans annotated with @ReadingConverter
    Map<String, Object> readingConverters =
        applicationContext.getBeansWithAnnotation(ReadingConverter.class);
    readingConverters.values().forEach(addIfAbsent);

    // Inception Converters (add only if not already present)
    addIfAbsent.accept(new DateToLocalDateTimeConverter());
    addIfAbsent.accept(new InstantToStringConverter());
    addIfAbsent.accept(new LocalDateTimeToDateConverter());
    addIfAbsent.accept(new LocalDateTimeToStringConverter());
    addIfAbsent.accept(new LocalDateToStringConverter());
    addIfAbsent.accept(new LocalTimeToStringConverter());
    addIfAbsent.accept(new OffsetDateTimeToStringConverter());
    addIfAbsent.accept(new OffsetTimeToStringConverter());
    addIfAbsent.accept(new StringToInstantConverter());
    addIfAbsent.accept(new StringToLocalDateConverter());
    addIfAbsent.accept(new StringToLocalDateTimeConverter());
    addIfAbsent.accept(new StringToLocalTimeConverter());
    addIfAbsent.accept(new StringToOffsetDateTimeConverter());
    addIfAbsent.accept(new StringToOffsetTimeConverter());
    addIfAbsent.accept(new StringToZonedDateTimeConverter());
    addIfAbsent.accept(new UUIDToStringConverter());
    addIfAbsent.accept(new ZonedDateTimeToStringConverter());

    /*
     * Add the converter that converts Enum values that implement the CodeEnum interface to String
     * snake case code values.
     */
    addIfAbsent.accept(new CodeEnumToStringConverter());

    /*
     * Add the converter that converts String snake case code values to enumeration values for
     * custom Enums that implement the CodeEnum interface.
     */
    addIfAbsent.accept(new StringToCodeEnumConverterFactory());

    return new MongoCustomConversions(converters);
  }

  /**
   * Returns the {@link MongoMappingContext} used by Spring Data MongoDB.
   *
   * <p>This bean is required by the Mongo auditing infrastructure. The bean name is explicitly set
   * to {@code "mongoMappingContext"} so that components such as the {@code mongoAuditingHandler}
   * can locate it.
   *
   * <p>The mapping context is configured with the {@link MongoCustomConversions} so that the same
   * custom conversion rules are applied when mapping domain types to and from MongoDB.
   *
   * @param mongoCustomConversions the custom Mongo conversions registered for the application
   * @return the configured {@link MongoMappingContext} instance
   */
  @Bean(name = "mongoMappingContext")
  public MongoMappingContext mongoMappingContext(MongoCustomConversions mongoCustomConversions) {
    MongoMappingContext mappingContext = new MongoMappingContext();

    // Make sure the mapping context knows about your custom conversions
    mappingContext.setSimpleTypeHolder(mongoCustomConversions.getSimpleTypeHolder());

    // Optional: if you want auto-index creation:
    // mappingContext.setAutoIndexCreation(true);

    return mappingContext;
  }

  private static FlapdoodleEmbeddedMongoHandle getOrCreateEmbeddedMongoHandle(
      ApplicationContext applicationContext,
      ConnectionString connectionString,
      String embeddedMongoDbServerVersion) {

    if (applicationContext
        instanceof org.springframework.context.support.GenericApplicationContext gac) {

      String beanName = "inceptionEmbeddedMongoHandle_" + embeddedMongoDbServerVersion;

      if (gac.containsBean(beanName)) {
        return gac.getBean(beanName, FlapdoodleEmbeddedMongoHandle.class);
      }

      gac.registerBean(
          beanName,
          FlapdoodleEmbeddedMongoHandle.class,
          () ->
              FlapdoodleEmbeddedMongoHandle.startIfAvailable(connectionString, embeddedMongoDbServerVersion),
          bd -> bd.setDestroyMethodName("close"));

      return gac.getBean(beanName, FlapdoodleEmbeddedMongoHandle.class);
    }

    throw new IllegalStateException(
        "ApplicationContext is not a GenericApplicationContext; cannot register embedded Mongo lifecycle bean");
  }

  private static String rewriteToHostPort(String originalUri, String host, int port) {
    if (!StringUtils.hasText(originalUri)) {
      throw new IllegalArgumentException("MongoDB URI is blank");
    }

    if (originalUri.startsWith("mongodb+srv://")) {
      throw new IllegalArgumentException(
          "mongodb+srv:// URIs are not supported when using embedded MongoDB. Use mongodb:// instead");
    }

    var connectionString = new ConnectionString(originalUri);

    // Use mongodb:// for embedded always
    String scheme = "mongodb://";

    // Preserve credentials as originally written (best-effort)
    // ConnectionString does not expose raw userinfo, only decoded username/password separately.
    // So we extract raw userinfo from the original string.
    String rawUserInfo = extractRawUserInfo(originalUri);

    // Preserve the "/db?...options" tail verbatim from the original URI (so no encoding changes)
    String tail = extractTail(originalUri, connectionString.getDatabase());

    return scheme + rawUserInfo + host + ":" + port + tail;
  }

  private static String extractRawUserInfo(String uri) {
    int schemeSeparatorIndex = uri.indexOf("://");
    if (schemeSeparatorIndex < 0) return "";
    int authorityStart = schemeSeparatorIndex + 3;

    int slash = uri.indexOf('/', authorityStart);
    int queryIndex = uri.indexOf('?', authorityStart);
    int authorityEnd =
        (slash >= 0 && queryIndex >= 0)
            ? Math.min(slash, queryIndex)
            : (slash >= 0) ? slash : (queryIndex >= 0) ? queryIndex : uri.length();

    String authority = uri.substring(authorityStart, authorityEnd);
    int atIndex = authority.lastIndexOf('@');
    return (atIndex >= 0) ? authority.substring(0, atIndex + 1) : "";
  }

  private static String extractTail(String uri, String parsedDb) {
    int schemeSeparatorIndex = uri.indexOf("://");

    if (schemeSeparatorIndex < 0) {
      return "";
    }

    int authorityStart = schemeSeparatorIndex + 3;

    int slashIndex = uri.indexOf('/', authorityStart);
    int queryIndex = uri.indexOf('?', authorityStart);

    if (slashIndex >= 0) {
      // keep everything from first '/' onward
      return uri.substring(slashIndex);
    }

    if (queryIndex >= 0) {
      // query but no path: ensure "/" exists before query
      return uri.substring(queryIndex);
    }

    // no path, no query — but if DB exists, keep it
    if (parsedDb != null && !parsedDb.isBlank()) {
      return "/" + parsedDb;
    }
    return "";
  }

  private static MongoJavaServerEmbeddedMongoHandle getOrCreateMongoJavaServerHandle(
      ApplicationContext applicationContext,
      ConnectionString connectionString) {

    if (applicationContext instanceof org.springframework.context.support.GenericApplicationContext gac) {

      String beanName = "inceptionEmbeddedMongoJavaServerHandle";

      if (gac.containsBean(beanName)) {
        return gac.getBean(beanName, MongoJavaServerEmbeddedMongoHandle.class);
      }

      gac.registerBean(
          beanName,
          MongoJavaServerEmbeddedMongoHandle.class,
          () -> MongoJavaServerEmbeddedMongoHandle.start(connectionString),
          bd -> bd.setDestroyMethodName("close"));

      return gac.getBean(beanName, MongoJavaServerEmbeddedMongoHandle.class);
    }

    throw new IllegalStateException(
        "ApplicationContext is not a GenericApplicationContext; cannot register embedded Mongo lifecycle bean");
  }

  @NonNull
  private DefaultMongoDatabaseFactory initMongoDatabaseFactory(
      ApplicationContext applicationContext,
      String mongoDbUri,
      String mongoDbEmbeddedServerVersion) {
    var connectionString = new ConnectionString(mongoDbUri);

    if (!StringUtils.hasText(mongoDbEmbeddedServerVersion)) {
      return DefaultMongoDatabaseFactory.fromUri(applicationContext, mongoDbUri);
    }

    mongoDbEmbeddedServerVersion = mongoDbEmbeddedServerVersion.trim();

    if ("mongo-java-server".equalsIgnoreCase(mongoDbEmbeddedServerVersion)) {
      if (!isMongoJavaServerAvailable()) {
        return DefaultMongoDatabaseFactory.fromUri(applicationContext, mongoDbUri);
      }

      MongoJavaServerEmbeddedMongoHandle handle = getOrCreateMongoJavaServerHandle(applicationContext, connectionString);

      String rewritten = rewriteToHostPort(mongoDbUri, handle.host(), handle.port());
      return DefaultMongoDatabaseFactory.fromUri(applicationContext, rewritten);
    }

    if (!isFlapdoodleAvailable()) {
      return DefaultMongoDatabaseFactory.fromUri(applicationContext, mongoDbUri);
    }

    // Start embedded and register for lifecycle management
    FlapdoodleEmbeddedMongoHandle handle =
        getOrCreateEmbeddedMongoHandle(
            applicationContext, connectionString, mongoDbEmbeddedServerVersion);

    // Point the configured URI at the embedded host/port
    String rewritten = rewriteToHostPort(mongoDbUri, handle.host(), handle.port());

    return DefaultMongoDatabaseFactory.fromUri(applicationContext, rewritten);
  }

  private boolean isFlapdoodleAvailable() {
    try {
      Class.forName("de.flapdoodle.embed.mongo.distribution.Version");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  private boolean isMongoJavaServerAvailable() {
    try {
      Class.forName("de.bwaldvogel.mongo.MongoServer");
      Class.forName("de.bwaldvogel.mongo.backend.memory.MemoryBackend");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
