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

package digital.inception.reference.test;

import static org.junit.jupiter.api.Assertions.fail;

import digital.inception.reference.Country;
import digital.inception.reference.IReferenceService;
import digital.inception.reference.Language;
import digital.inception.reference.MeasurementSystem;
import digital.inception.reference.MeasurementUnit;
import digital.inception.reference.MeasurementUnitType;
import digital.inception.reference.Region;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>LiquibaseChangelogInsertsTest</b>.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class LiquibaseChangelogInsertsTest {

  private static final Set<String> LOCALE_IDS = Set.of("en-US", "en-ZA");

  /** The Reference Service. */
  @Autowired private IReferenceService referenceService;

  /** Create the Liquibase changelog inserts. */
  @Test
  public void createLiquibaseChangelogInserts() throws Exception {
    boolean createLiquibaseInserts = true;
    boolean createCountryInserts = createLiquibaseInserts && true;
    boolean createLanguageInserts = createLiquibaseInserts && true;
    boolean createMeasurementSystemInserts = createLiquibaseInserts && true;
    boolean createMeasurementUnitTypeInserts = createLiquibaseInserts && true;
    boolean createMeasurementUnitInserts = createLiquibaseInserts && true;
    boolean createRegionInserts = createLiquibaseInserts && true;

    String fileName =
        System.getProperty("user.dir")
            + File.separator
            + "target"
            + File.separator
            + "reference-liquibase-inserts.txt";

    System.out.println("File Name: " + fileName);

    PrintWriter writer = new PrintWriter(new FileWriter(fileName));

    for (String localeId : LOCALE_IDS) {

      if (createCountryInserts) {
        for (Country country : referenceService.getCountries(localeId)) {

          if (country.getNationality().contains(",")) {
            fail("Invalid nationality for country (" + country.getCode() + ")");
          }

          writer.println("    <insert schemaName=\"reference\" tableName=\"countries\">");
          writer.println("      <column name=\"code\" value=\"" + country.getCode() + "\"/>");
          writer.println(
              "      <column name=\"iso3_code\" value=\"" + country.getIso3Code() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + country.getLocaleId() + "\"/>");

          if (country.getSortIndex() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + country.getSortIndex() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + country.getName() + "\"/>");
          writer.println(
              "      <column name=\"short_name\" value=\"" + country.getShortName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + country.getDescription() + "\"/>");
          writer.println(
              "      <column name=\"sovereign_state\" value=\""
                  + country.getSovereignState()
                  + "\"/>");
          writer.println(
              "      <column name=\"nationality\" value=\"" + country.getNationality() + "\"/>");
          writer.println("    </insert>");
        }

        writer.println();
      }

      if (createLanguageInserts) {
        for (Language language : referenceService.getLanguages(localeId)) {

          writer.println("    <insert schemaName=\"reference\" tableName=\"languages\">");
          writer.println(
              "      <column name=\"code\" value=\"" + language.getCode().toUpperCase() + "\"/>");
          writer.println(
              "      <column name=\"iso3_code\" value=\""
                  + language.getIso3Code().toUpperCase()
                  + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + language.getLocaleId() + "\"/>");

          if (language.getSortIndex() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + language.getSortIndex() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + language.getName() + "\"/>");
          writer.println(
              "      <column name=\"short_name\" value=\"" + language.getShortName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + language.getDescription() + "\"/>");
          writer.println("    </insert>");
        }

        writer.println();
      }

      if (createMeasurementSystemInserts) {
        for (MeasurementSystem measurementSystem :
            referenceService.getMeasurementSystems(localeId)) {

          writer.println("    <insert schemaName=\"reference\" tableName=\"measurement_systems\">");
          writer.println(
              "      <column name=\"code\" value=\"" + measurementSystem.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + measurementSystem.getLocaleId()
                  + "\"/>");

          if (measurementSystem.getSortIndex() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + measurementSystem.getSortIndex()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + measurementSystem.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + measurementSystem.getDescription()
                  + "\"/>");
          writer.println("    </insert>");
        }

        writer.println();
      }

      if (createMeasurementUnitTypeInserts) {
        for (MeasurementUnitType measurementUnitType :
            referenceService.getMeasurementUnitTypes(localeId)) {

          writer.println(
              "    <insert schemaName=\"reference\" tableName=\"measurement_unit_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + measurementUnitType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + measurementUnitType.getLocaleId()
                  + "\"/>");

          if (measurementUnitType.getSortIndex() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + measurementUnitType.getSortIndex()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + measurementUnitType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + measurementUnitType.getDescription()
                  + "\"/>");
          writer.println("    </insert>");
        }

        writer.println();
      }

      if (createMeasurementUnitInserts) {
        for (MeasurementUnit measurementUnit : referenceService.getMeasurementUnits(localeId)) {

          writer.println("    <insert schemaName=\"reference\" tableName=\"measurement_units\">");
          writer.println(
              "      <column name=\"code\" value=\"" + measurementUnit.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + measurementUnit.getLocaleId() + "\"/>");

          if (measurementUnit.getSortIndex() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + measurementUnit.getSortIndex()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + measurementUnit.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + measurementUnit.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"system\" value=\"" + measurementUnit.getSystem() + "\"/>");
          writer.println(
              "      <column name=\"type\" value=\"" + measurementUnit.getType() + "\"/>");
          writer.println("    </insert>");
        }

        writer.println();
      }

      if (createRegionInserts) {
        for (Region region : referenceService.getRegions(localeId)) {

          writer.println("    <insert schemaName=\"reference\" tableName=\"regions\">");
          writer.println("      <column name=\"country\" value=\"" + region.getCountry() + "\"/>");
          writer.println("      <column name=\"code\" value=\"" + region.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + region.getLocaleId() + "\"/>");

          if (region.getSortIndex() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + region.getSortIndex() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + region.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + region.getDescription() + "\"/>");
          writer.println("    </insert>");
        }

        writer.println();
      }

      writer.println();
      writer.println();
    }

    writer.flush();
    writer.close();
  }
}
