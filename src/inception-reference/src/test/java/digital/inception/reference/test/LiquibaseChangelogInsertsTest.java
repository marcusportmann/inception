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

  /** The Reference Service. */
  @Autowired private IReferenceService referenceService;

  /** Create the Liquibase changelog inserts. */
  @Test
  public void createLiquibaseChangelogInserts() throws Exception {
    boolean createLiquibaseInserts = true;
    boolean createCountryInserts = createLiquibaseInserts && false;
    boolean createLanguageInserts = createLiquibaseInserts && false;
    boolean createMeasurementSystemInserts = createLiquibaseInserts && false;
    boolean createMeasurementUnitTypeInserts = createLiquibaseInserts && false;
    boolean createMeasurementUnitInserts = createLiquibaseInserts && false;
    boolean createRegionInserts = createLiquibaseInserts && false;

    if (createCountryInserts) {
      for (Country country : referenceService.getCountries("en-US")) {

        if (country.getNationality().contains(",")) {
          fail("Invalid nationality for country (" + country.getCode() + ")");
        }

        System.out.println("<insert schemaName=\"reference\" tableName=\"countries\">");
        System.out.println("  <column name=\"code\" value=\"" + country.getCode() + "\"/>");
        System.out.println(
            "  <column name=\"iso3_code\" value=\"" + country.getIso3Code() + "\"/>");
        System.out.println(
            "  <column name=\"locale_id\" value=\"" + country.getLocaleId() + "\"/>");

        if (country.getSortIndex() != null) {
          System.out.println(
              "  <column name=\"sort_index\" value=\"" + country.getSortIndex() + "\"/>");
        }

        System.out.println("  <column name=\"name\" value=\"" + country.getName() + "\"/>");
        System.out.println(
            "  <column name=\"short_name\" value=\"" + country.getShortName() + "\"/>");
        System.out.println(
            "  <column name=\"description\" value=\"" + country.getDescription() + "\"/>");
        System.out.println(
            "  <column name=\"sovereign_state\" value=\"" + country.getSovereignState() + "\"/>");
        System.out.println(
            "  <column name=\"nationality\" value=\"" + country.getNationality() + "\"/>");
        System.out.println("</insert>");
      }

      System.out.println();
    }

    if (createLanguageInserts) {
      for (Language language : referenceService.getLanguages("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"languages\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + language.getCode().toUpperCase() + "\"/>");
        System.out.println(
            "      <column name=\"iso3_code\" value=\""
                + language.getIso3Code().toUpperCase()
                + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + language.getLocaleId() + "\"/>");

        if (language.getSortIndex() != null) {
          System.out.println(
              "  <column name=\"sort_index\" value=\"" + language.getSortIndex() + "\"/>");
        }

        System.out.println("      <column name=\"name\" value=\"" + language.getName() + "\"/>");
        System.out.println(
            "      <column name=\"short_name\" value=\"" + language.getShortName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + language.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createMeasurementSystemInserts) {
      for (MeasurementSystem measurementSystem : referenceService.getMeasurementSystems("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"measurement_systems\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + measurementSystem.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + measurementSystem.getLocaleId() + "\"/>");

        if (measurementSystem.getSortIndex() != null) {
          System.out.println(
              "  <column name=\"sort_index\" value=\"" + measurementSystem.getSortIndex() + "\"/>");
        }

        System.out.println("      <column name=\"name\" value=\"" + measurementSystem.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + measurementSystem.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createMeasurementUnitTypeInserts) {
      for (MeasurementUnitType measurementUnitType : referenceService.getMeasurementUnitTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"measurement_unit_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + measurementUnitType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + measurementUnitType.getLocaleId() + "\"/>");

        if (measurementUnitType.getSortIndex() != null) {
          System.out.println(
              "  <column name=\"sort_index\" value=\"" + measurementUnitType.getSortIndex() + "\"/>");
        }

        System.out.println("      <column name=\"name\" value=\"" + measurementUnitType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + measurementUnitType.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createMeasurementUnitInserts) {
      for (MeasurementUnit measurementUnit : referenceService.getMeasurementUnits("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"measurement_units\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + measurementUnit.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + measurementUnit.getLocaleId() + "\"/>");

        if (measurementUnit.getSortIndex() != null) {
          System.out.println(
              "  <column name=\"sort_index\" value=\"" + measurementUnit.getSortIndex() + "\"/>");
        }

        System.out.println("      <column name=\"name\" value=\"" + measurementUnit.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + measurementUnit.getDescription() + "\"/>");
        System.out.println(
            "      <column name=\"system\" value=\"" + measurementUnit.getSystem() + "\"/>");
        System.out.println(
            "      <column name=\"type\" value=\"" + measurementUnit.getType() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createRegionInserts) {
      for (Region region : referenceService.getRegions("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"regions\">");
        System.out.println(
            "      <column name=\"country\" value=\"" + region.getCountry() + "\"/>");
        System.out.println("      <column name=\"code\" value=\"" + region.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + region.getLocaleId() + "\"/>");

        if (region.getSortIndex() != null) {
          System.out.println(
              "  <column name=\"sort_index\" value=\"" + region.getSortIndex() + "\"/>");
        }

        System.out.println("      <column name=\"name\" value=\"" + region.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + region.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }
  }
}
