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

package digital.inception.mongodb;

import com.mongodb.ConnectionString;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * The <b>DefaultMongoDBDatabaseFactoryCondition</b> class implements the condition that must be
 * matched to enable the default MongoDBDatabaseFactory, which is that MongoDB configuration is
 * specified under spring.data.mongodb.
 *
 * @author Marcus Portmann
 */
public class DefaultMongoDBDatabaseFactoryCondition implements Condition {

  /** Constructs a new <b>DefaultMongoDBDatabaseFactoryCondition</b>. */
  public DefaultMongoDBDatabaseFactoryCondition() {}

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    try {
      Environment environment = context.getEnvironment();

      // Ensure that we have a MongoDB URI specified via spring.data.mongodb.uri
      String mongodbUri = environment.getProperty("spring.data.mongodb.uri");

      if (!StringUtils.hasText(mongodbUri)) {
        return false;
      }

      /*
       * Check if the MongoDB URI includes the database name, otherwise check for an explicit
       * database name specified via spring.data.mongodb.database.
       */
      ConnectionString connectionString = new ConnectionString(mongodbUri);

      if (!StringUtils.hasText(connectionString.getDatabase())) {
        return StringUtils.hasText(environment.getProperty("spring.data.mongodb.database"));
      }

      return true;
    } catch (Throwable e) {
      return true;
    }
  }
}
