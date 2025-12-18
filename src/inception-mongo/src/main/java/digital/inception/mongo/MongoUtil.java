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
import org.springframework.util.StringUtils;

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

  /**
   * Returns a sanitized MongoDB connection URI suitable for logging by redacting any {@code
   * userinfo} component (typically {@code username[:password]@}) from the authority section.
   *
   * <p>If the input contains credentials (e.g. {@code mongodb://user:pass@host:27017/db}), the
   * returned value replaces the credential portion with {@code "***:***@"} while preserving the
   * remainder of the URI (hosts, database path, and query options).
   *
   * <p>This method is intentionally conservative and does not attempt full URI parsing; it performs
   * a simple string-based redaction to avoid leaking credentials into logs.
   *
   * <p>Special return values:
   *
   * <ul>
   *   <li>{@code "<empty>"} if {@code uri} is {@code null}, empty, or only whitespace
   *   <li>{@code "<unparseable>"} if {@code uri} does not contain a scheme separator ({@code ://})
   * </ul>
   *
   * @param mongoDbUri the MongoDB connection URI that may contain credentials
   * @return a sanitized URI with any userinfo redacted, or a placeholder value if empty or
   *     unparseable
   */
  public static String sanitizedMongoDbUri(String mongoDbUri) {
    if (!StringUtils.hasText(mongoDbUri)) return "<empty>";

    int schemeSeparatorIndex = mongoDbUri.indexOf("://");
    if (schemeSeparatorIndex < 0) return "<unparseable>";

    int authorityStart = schemeSeparatorIndex + 3;
    int atIndex = mongoDbUri.indexOf('@', authorityStart);
    if (atIndex < 0) {
      return mongoDbUri;
    }

    return mongoDbUri.substring(0, authorityStart) + "***:***@" + mongoDbUri.substring(atIndex + 1);
  }
}
