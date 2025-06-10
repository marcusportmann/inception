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
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;

/**
 * The {@code WebServiceConventionsRules} class holds the ArchUnit rules that verify structural
 * conventions for web services.
 *
 * @author Marcus Portmann
 */
public final class WebServiceConventionsRules {

  /**
   * The ArchUnit rule that verifies that web services are named correctly, have the correct
   * annotations, and are in the correct package.
   */
  public static final ArchRule WEB_SERVICES_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION =
      classes()
          .that()
          .areAnnotatedWith("jakarta.jws.WebService")
          .should()
          .haveSimpleNameEndingWith("WebService")
          .andShould()
          .resideInAPackage("..ws..")
          .allowEmptyShould(true);

  /**
   * The ArchUnit rule that verifies that the public methods on web services have the @WebMethod
   * annotation.
   */
  public static final ArchRule WEB_SERVICE_PUBLIC_METHODS_HAVE_WEB_METHOD =
      methods()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith("jakarta.jws.WebService")
          .and()
          .arePublic()
          .should()
          .beAnnotatedWith("jakarta.jws.WebMethod")
          .allowEmptyShould(true);

  /** Constructs a new {@code WebServiceConventionsRules}. */
  public WebServiceConventionsRules() {}

  /**
   * Check that the ArchUnit rules are being adhered to.
   *
   * @param classes the Java classes to check
   */
  public static void check(JavaClasses classes) {
    try {
      Class.forName("jakarta.jws.WebService");
    } catch (ClassNotFoundException e) {
      return;
    }

    WEB_SERVICES_FOLLOW_NAMING_AND_ANNOTATIONS_AND_LOCATION.check(classes);

    WEB_SERVICE_PUBLIC_METHODS_HAVE_WEB_METHOD.check(classes);
  }
}
