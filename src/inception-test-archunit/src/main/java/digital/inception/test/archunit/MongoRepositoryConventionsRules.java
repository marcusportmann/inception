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

package digital.inception.test.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;

/**
 * The {@code MongoRepositoryConventionsRules} class holds the ArchUnit rules that verify structural
 * conventions for Spring Data Mongo repository interfaces.
 *
 * @author Marcus Portmann
 */
public final class MongoRepositoryConventionsRules {

  /**
   * The ArchUnit rule that verifies that Spring Data Mongo repository interfaces are public, named
   * correctly and in the correct package.
   */
  public static final ArchRule MONGO_REPOSITORY_INTERFACES_ARE_PUBLIC_FOLLOW_NAMING_AND_LOCATION =
      classes()
          .that()
          .areInterfaces()
          .and()
          .areAssignableTo("org.springframework.data.mongodb.repository.MongoRepository")
          .should()
          .bePublic() // 1 – must be public
          .andShould()
          .haveSimpleNameEndingWith("Repository")
          .andShould()
          .resideInAPackage("..persistence..mongo..")
          .allowEmptyShould(true);

  /** Constructs a new {@code MongoRepositoryConventionsRules}. */
  public MongoRepositoryConventionsRules() {}

  /**
   * Check that the ArchUnit rules are being adhered to.
   *
   * @param classes the Java classes to check
   */
  public static void check(JavaClasses classes) {
    try {
      Class.forName("org.springframework.data.mongodb.repository.MongoRepository");
    } catch (ClassNotFoundException ignored) {
      return;
    }

    MONGO_REPOSITORY_INTERFACES_ARE_PUBLIC_FOLLOW_NAMING_AND_LOCATION.check(classes);
  }
}
