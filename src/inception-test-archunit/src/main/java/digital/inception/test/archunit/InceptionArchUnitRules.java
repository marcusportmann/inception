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

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

/**
 * The {@code InceptionArchUnitRules} class provides the standard ArchUnit tests that verify an
 * application aligns with structural conventions for applications based on the Inception Framework.
 *
 * @author Marcus Portmann
 */
public final class InceptionArchUnitRules {

  /** Constructs a new {@code InceptionArchUnitRules}. */
  public InceptionArchUnitRules() {}

  /**
   * Check the ArchUnit rules.
   *
   * @param packages the packages the ArchUnit rules should be applied to
   */
  public static void executeArchUnitRules(String... packages) {
    JavaClasses classes = new ClassFileImporter().importPackages(packages);

    /*
     * Check the ArchUnit rules for exceptions.
     */
    ExceptionConventionsRules.check(classes);

    /*
     * Check the ArchUnit rules for the API controllers.
     */
    ApiControllerConventionsRules.check(classes);

    /*
     * Check the ArchUnit rules for the Spring Data JPA repositories.
     */
    JpaRepositoryConventionsRules.check(classes);

    /*
     * Check the ArchUnit rules for the Spring Data Mongo repositories.
     */
    MongoRepositoryConventionsRules.check(classes);

    /*
     * Check the ArchUnit rules for the Spring services.
     */
    ServiceConventionsRules.check(classes);

    /*
     * Check the ArchUnit rules for the web services.
     */
    WebServiceConventionsRules.check(classes);

    /*
     * Check the ArchUnit rules for the Inception Executor module.
     */
    ExecutorConventionsRules.check(classes);

    /*
     * Check the ArchUnit rules for the Inception Scheduler module.
     */
    SchedulerConventionsRules.check(classes);

    /*
     * Check the ArchUnit rules for XML.
     */
    XmlConventionsRules.check(classes);
  }
}
