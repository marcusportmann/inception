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

import com.tngtech.archunit.lang.ArchRule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@code JpaRepositoryConventionsRules} class holds the ArchUnit rules that verify structural
 * conventions for Spring Data JPA repository interfaces.
 *
 * @author Marcus Portmann
 */
public class JpaRepositoryConventionsRules {

  /**
   * The ArchUnit rule that verifies that Spring Data JPA repository interfaces are public, named
   * correctly and in the correct package.
   */
  public static final ArchRule JPA_REPOSITORY_INTERFACES_ARE_PUBLIC_FOLLOW_NAMING_AND_LOCATION =
      classes()
          .that()
          .areInterfaces()
          .and()
          .areAssignableTo(JpaRepository.class)
          .should()
          .bePublic() // 1 – must be public
          .andShould()
          .haveSimpleNameEndingWith("Repository")
          .andShould()
          .resideInAPackage("..persistence..jpa..")
          .allowEmptyShould(true);

  /** Constructs a new {@code JpaRepositoryConventionsRules}. */
  public JpaRepositoryConventionsRules() {}
}
