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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * The {@code DefaultMongoDatabaseFactoryCondition} class implements the condition that must be
 * matched to enable the default MongoDatabaseFactory, which is that the MongoDB configuration is
 * specified under spring.data.mongodb, and the application MongoDB configuration under
 * inception.application.mongodb must not be specified.
 *
 * @author Marcus Portmann
 */
public class DefaultMongoDatabaseFactoryCondition implements Condition {

  private static final Logger log =
      LoggerFactory.getLogger(DefaultMongoDatabaseFactoryCondition.class);

  /** Constructs a new {@code DefaultMongoDatabaseFactoryCondition}. */
  public DefaultMongoDatabaseFactoryCondition() {}

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Environment environment = context.getEnvironment();

    String applicationMongoDbUri = environment.getProperty("inception.application.mongodb.uri");
    if (StringUtils.hasText(applicationMongoDbUri)) {
      // Expected: application config takes precedence, so default must not activate
      log.debug(
          "Default MongoDB disabled: 'inception.application.mongodb.uri' is set so application MongoDB configuration takes precedence ({})",
          MongoUtil.sanitizedMongoDbUri(applicationMongoDbUri));
      return false;
    }

    String springMongoDbUri = environment.getProperty("spring.data.mongodb.uri");
    if (!StringUtils.hasText(springMongoDbUri)) {
      // Expected when default mongo is not configured
      log.debug("Default MongoDB disabled: property 'spring.data.mongodb.uri' is not set or blank");
      return false;
    }

    String sanitizedMongoDbUri = MongoUtil.sanitizedMongoDbUri(springMongoDbUri);

    try {
      ConnectionString connectionString = new ConnectionString(springMongoDbUri);

      if (StringUtils.hasText(connectionString.getDatabase())) {
        log.debug(
            "Default MongoDB enabled: 'spring.data.mongodb.uri' contains database ({})",
            sanitizedMongoDbUri);
        return true;
      }

      String mongoDbName = environment.getProperty("spring.data.mongodb.database");
      if (StringUtils.hasText(mongoDbName)) {
        log.debug(
            "Default MongoDB enabled: database provided via 'spring.data.mongodb.database' ({}) ({})",
            sanitizedMongoDbUri,
            mongoDbName);
        return true;
      }

      // Misconfiguration: URI present but no db anywhere
      log.warn(
          "Default MongoDB configuration incomplete: 'spring.data.mongodb.uri' does not include "
              + "a database  and 'spring.data.mongodb.database' is not set ({})",
          sanitizedMongoDbUri);
      return false;

    } catch (RuntimeException ex) {
      // Fail closed: invalid URI should not cause this condition to match
      log.warn(
          "Invalid MongoDB URI in property 'spring.data.mongodb.uri' ({})",
          sanitizedMongoDbUri,
          ex);
      return false;
    }
  }
}
