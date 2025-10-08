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

import digital.inception.core.CoreConfiguration;
import digital.inception.core.converter.CodeEnumToStringConverter;
import digital.inception.core.converter.StringToCodeEnumConverterFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * The {@code MongoConfiguration} class provides the Spring configuration for the Mongo module.
 *
 * <p>This module customizes the default automatic initialization performed by the
 * spring-boot-starter-data-mongodb Spring Boot starter.
 *
 * @author Marcus Portmann
 */
@Configuration
@Import(CoreConfiguration.class)
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
   * Returns the application mongo database factory initialized from the nova.application.mongodb
   * configuration if it is available.
   *
   * @param applicationContext the Spring application context
   * @param applicationMongoDatabaseUri the URI for the application MongoDB database
   * @return the application mongo database factory initialized from the nova.application.mongodb
   *     configuration if it is available
   */
  @Bean("applicationMongoDatabaseFactory")
  @Primary
  @Conditional(ApplicationMongoDatabaseFactoryCondition.class)
  public org.springframework.data.mongodb.MongoDatabaseFactory applicationMongoDatabaseFactory(
      ApplicationContext applicationContext,
      @Value("${nova.application.mongodb.uri}") String applicationMongoDatabaseUri) {
    return DefaultMongoDatabaseFactory.fromUri(applicationContext, applicationMongoDatabaseUri);
  }

  /**
   * Returns the application MonoTemplate that leverages the application mongo database factory.
   *
   * @param applicationMongoDatabaseFactory the application mongo database factory
   * @param applicationMongoConverter the application mongo converter
   * @return the application MonoTemplate that leverages the application mongo database factory
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
   * @param applicationContext the Spring application context
   * @return the default mongo database factory initialized from the spring.data.mongodb
   *     configuration if it is available
   */
  @Bean("defaultMongoDatabaseFactory")
  @Primary
  @Conditional(DefaultMongoDatabaseFactoryCondition.class)
  public org.springframework.data.mongodb.MongoDatabaseFactory defaultMongoDatabaseFactory(
      ApplicationContext applicationContext) {
    return new DefaultMongoDatabaseFactory(applicationContext);
  }

  /**
   * Returns the default MonoTemplate that leverages the default mongo database factory.
   *
   * @param defaultMongoDatabaseFactory the default mongo database factory
   * @param defaultMongoConverter the default mongo converter
   * @return the default MonoTemplate that leverages the default mongo database factory
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
   * @param applicationContext the Spring application context
   * @return the MongoCustomConversions initialized with the writer converters and reader converters
   *     detected by scanning the classpath for classes annotated with the @WriterConverter
   *     and @ReaderConverter annotations
   */
  @Bean
  public MongoCustomConversions mongoCustomConversions(ApplicationContext applicationContext) {
    List<Object> converters = new ArrayList<>();

    // Find all beans annotated with @WritingConverter
    Map<String, Object> writingConverters =
        applicationContext.getBeansWithAnnotation(WritingConverter.class);
    converters.addAll(writingConverters.values());

    // Find all beans annotated with @ReadingConverter
    Map<String, Object> readingConverters =
        applicationContext.getBeansWithAnnotation(ReadingConverter.class);
    converters.addAll(readingConverters.values());

    /*
     * Add the converter that converts Enum values that implement the CodeEnum interface to String
     * snake case code values.
     */
    converters.add(new CodeEnumToStringConverter());

    /*
     * Add the converter that converts String snake case code values to enumeration values for
     * custom Enums that implement the CodeEnum interface.
     */
    converters.add(new StringToCodeEnumConverterFactory());

    return new MongoCustomConversions(converters);
  }
}
