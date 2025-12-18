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
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * The {@code ApplicationMongoDatabaseFactoryCondition} class implements the condition that must be
 * matched to enable the application MongoDatabaseFactory, which is that the MongoDB configuration
 * is specified under inception.application.mongodb.
 *
 * @author Marcus Portmann
 */
public class ApplicationMongoDatabaseFactoryCondition implements Condition {

  private static final Logger log =
      LoggerFactory.getLogger(ApplicationMongoDatabaseFactoryCondition.class);

  /** Constructs a new {@code ApplicationMongoDatabaseFactoryCondition}. */
  public ApplicationMongoDatabaseFactoryCondition() {}

  @Override
  public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
    Environment environment = context.getEnvironment();

    String applicationMongoDbUri = environment.getProperty("inception.application.mongodb.uri");

    if (!StringUtils.hasText(applicationMongoDbUri)) {
      // Normal when an application doesn't use the inception.application Mongo config
      log.debug(
          "Application MongoDB disabled: property 'inception.application.mongodb.uri' is not set or blank");
      return false;
    }

    String sanitizedMongoDbUri = MongoUtil.sanitizedMongoDbUri(applicationMongoDbUri);

    try {
      ConnectionString connectionString = new ConnectionString(applicationMongoDbUri);

      // If the MongoDB URI includes a database, we're good
      if (StringUtils.hasText(connectionString.getDatabase())) {
        log.debug(
            "Application MongoDB enabled: 'inception.application.mongodb.uri' contains database ({})",
            sanitizedMongoDbUri);
        return true;
      }

      // Otherwise require explicit database property
      String mongoDbName = environment.getProperty("inception.application.mongodb.database");
      if (StringUtils.hasText(mongoDbName)) {
        log.debug(
            "Application MongoDB enabled: database provided via 'inception.application.mongodb.database' ({}) ({})",
            sanitizedMongoDbUri,
            mongoDbName);
        return true;
      }

      // Misconfiguration (URI present but no db anywhere)
      log.warn(
          "Application MongoDB configuration incomplete: 'inception.application.mongodb.uri' does not "
              + "include a database and 'inception.application.mongodb.database' is not set ({})",
          sanitizedMongoDbUri);
      return false;

    } catch (RuntimeException ex) {
      log.warn(
          "Invalid MongoDB URI in property 'inception.application.mongodb.uri' ({})",
          sanitizedMongoDbUri,
          ex);
      return false;
    }
  }
}
