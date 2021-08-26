/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.core.liquibase;

/**
 * The <b>InceptionLiquibaseChangelogs</b> class holds the lists of changelogs for the different
 * modules that form part of the Inception Framework.
 *
 * @author Marcus Portmann
 */
public class InceptionLiquibaseChangelogs {

  /** The Liquibase changelogs for the business modules. */
  public static final String[] BUSINESS_CHANGELOGS = {
    "db/inception-party.changelog.xml", "db/inception-reference.changelog.xml"
  };

  /** The Liquibase data changelogs for the business modules. */
  public static final String[] BUSINESS_DATA_CHANGELOGS = {
    "db/inception-party-data.changelog.xml", "db/inception-reference-data.changelog.xml"
  };

  /** The Liquibase changelogs for the core modules. */
  public static final String[] CORE_CHANGELOGS = {
    "db/inception-core.changelog.xml", "db/inception-application.changelog.xml"
  };

  /** The Liquibase test changelogs for all the modules. */
  public static final String[] TEST_CHANGELOGS = {
    "db/inception-application-test.changelog.xml", "db/inception-party-test.changelog.xml"
  };

  /** The Liquibase changelogs for the utility modules. */
  public static final String[] UTILITY_CHANGELOGS = {
    "db/inception-audit.changelog.xml",
    "db/inception-bmi.changelog.xml",
    "db/camunda-engine.changelog.xml",
    "db/camunda-case-engine.changelog.xml",
    "db/inception-codes.changelog.xml",
    "db/inception-config.changelog.xml",
    "db/inception-error.changelog.xml",
    "db/inception-mail.changelog.xml",
    "db/inception-messaging.changelog.xml",
    "db/inception-reporting.changelog.xml",
    "db/inception-security.changelog.xml",
    "db/inception-scheduler.changelog.xml",
    "db/inception-sms.changelog.xml"
  };
}
