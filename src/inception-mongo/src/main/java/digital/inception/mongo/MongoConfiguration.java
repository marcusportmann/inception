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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * The <b>MongoConfiguration</b> class provides the Spring configuration for the Mongo module.
 *
 * <p>This module customizes the default automatic initialization performed by the
 * spring-boot-starter-data-mongodb Spring Boot starter.
 *
 * @author Marcus Portmann
 */
@Configuration
public class MongoConfiguration {

  /** Constructs a new <b>MongoConfiguration</b>. */
  public MongoConfiguration() {}

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
    List<Converter<?, ?>> converters = new ArrayList<>();

    // Find all beans annotated with @WritingConverter
    Map<String, Object> writingConverters =
        applicationContext.getBeansWithAnnotation(WritingConverter.class);
    writingConverters.values().forEach(bean -> converters.add((Converter<?, ?>) bean));

    // Find all beans annotated with @ReadingConverter
    Map<String, Object> readingConverters =
        applicationContext.getBeansWithAnnotation(ReadingConverter.class);
    readingConverters.values().forEach(bean -> converters.add((Converter<?, ?>) bean));

    return new MongoCustomConversions(converters);
  }
}
