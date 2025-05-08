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

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * The {@code MongoUtil} class provides mongo-related utility methods.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class MongoUtil {

  /** Private constructor to prevent instantiation. */
  private MongoUtil() {}

  /**
   * Create the mongo converter.
   *
   * @param mongoDatabaseFactory the mongo database factory
   * @param mongoCustomConversions the mongo custom conversions
   * @return the mongo converter
   */
  public static MongoConverter createMongoConverter(
      MongoDatabaseFactory mongoDatabaseFactory, MongoCustomConversions mongoCustomConversions) {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);

    MongoMappingContext mappingContext = new MongoMappingContext();
    mappingContext.setSimpleTypeHolder(mongoCustomConversions.getSimpleTypeHolder());
    mappingContext.afterPropertiesSet();

    MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
    converter.setCustomConversions(mongoCustomConversions);
    converter.setCodecRegistryProvider(mongoDatabaseFactory);
    converter.afterPropertiesSet();
    return converter;
  }
}
