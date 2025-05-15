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

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * The {@code InceptionArchUnitTests} class provides the standard ArchUnit tests that verify an
 * application aligns with structural conventions for applications based on the Inception Framework.
 *
 * @author Marcus Portmann
 */
public class InceptionArchUnitTests {

  /**
   * The ArchUnit rule that verifies that API controller implementations are named correctly, have
   * the correct annotations, and are in the correct package.
   */
  @ArchTest
  public static final ArchRule API_CONTROLLERS_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION =
      ApiControllerConventionsRules.API_CONTROLLERS_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION;

  /**
   * The ArchUnit rule that verifies that API controller interfaces are named correctly, have the
   * correct annotations, and are in the correct package.
   */
  @ArchTest
  public static final ArchRule
      API_CONTROLLER_INTERFACES_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION =
          ApiControllerConventionsRules
              .API_CONTROLLER_INTERFACES_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION;

  /**
   * The ArchUnit rule that verifies that API controller methods have the correct annotations, and
   * are fully documented.
   */
  @ArchTest
  public static final ArchRule API_CONTROLLER_METHODS_FOLLOW_ANNOTATIONS_AND_FULLY_DOCUMENTED =
      ApiControllerConventionsRules.API_CONTROLLER_METHODS_FOLLOW_ANNOTATIONS_AND_FULLY_DOCUMENTED;

  /**
   * The ArchUnit rule that verifies that Spring Data JPA repository interfaces are public, named
   * correctly and in the correct package.
   */
  @ArchTest
  public static final ArchRule JPA_REPOSITORY_INTERFACES_ARE_PUBLIC_FOLLOW_NAMING_AND_LOCATION =
      JpaRepositoryConventionsRules.JPA_REPOSITORY_INTERFACES_ARE_PUBLIC_FOLLOW_NAMING_AND_LOCATION;

  /**
   * The ArchUnit rule that verifies that Spring Data Mongo repository interfaces are public, named
   * correctly and in the correct package.
   */
  @ArchTest
  public static final ArchRule MONGO_REPOSITORY_INTERFACES_ARE_PUBLIC_FOLLOW_NAMING_AND_LOCATION =
      MongoRepositoryConventionsRules
          .MONGO_REPOSITORY_INTERFACES_ARE_PUBLIC_FOLLOW_NAMING_AND_LOCATION;

  /**
   * The ArchUnit rule that verifies that Spring service implementations are named correctly, and
   * are in the correct package.
   */
  @ArchTest
  public static final ArchRule SERVICE_IMPLS_FOLLOW_NAMING_AND_LOCATION =
      ServiceConventionsRules.SERVICE_IMPLS_FOLLOW_NAMING_AND_LOCATION;

  /** Constructs a new {@code InceptionArchUnitTests}. */
  public InceptionArchUnitTests() {}
}
