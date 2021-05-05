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

package digital.inception.party.test;

import digital.inception.party.AttributeTypeCategory;
import digital.inception.party.IPartyReferenceService;
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

  /** The Party Reference Service. */
  @Autowired private IPartyReferenceService partyReferenceService;

  /** Create the Liquibase changelog inserts. */
  @Test
  public void createLiquibaseChangelogInserts() throws Exception {
    boolean createLiquibaseInserts = true;
    boolean createAttributeTypeCategoryInserts = createLiquibaseInserts && true;

    //    boolean createContactMechanismTypeInserts = createLiquibaseInserts && false;
    //    boolean createContactMechanismRoleInserts = createLiquibaseInserts && false;
    //    boolean createEmploymentStatusInserts = createLiquibaseInserts && false;
    //    boolean createEmploymentTypeInserts = createLiquibaseInserts && false;
    //    boolean createGenderInserts = createLiquibaseInserts && false;
    //    boolean createIdentityDocumentTypeInserts = createLiquibaseInserts && false;
    //    boolean createMaritalStatusInserts = createLiquibaseInserts && false;
    //    boolean createMarriageTypeInserts = createLiquibaseInserts && false;
    //    boolean createMinorTypeInserts = createLiquibaseInserts && false;
    //    boolean createNextOfKinInserts = createLiquibaseInserts && false;
    //    boolean createOccupationInserts = createLiquibaseInserts && false;
    //    boolean createAttributeTypeCategoryInserts = createLiquibaseInserts && true;
    //    boolean createAttributeTypeInserts = createLiquibaseInserts && true;
    //    boolean createRolePurposeInserts = createLiquibaseInserts && false;
    //    boolean createRoleTypeInserts = createLiquibaseInserts && false;
    //    boolean createPhysicalAddressPurposeInserts = createLiquibaseInserts && false;
    //    boolean createPhysicalAddressTypeInserts = createLiquibaseInserts && false;
    //    boolean createPreferenceTypeCategoryInserts = createLiquibaseInserts && false;
    //    boolean createPreferenceTypeInserts = createLiquibaseInserts && false;
    //    boolean createRaceInserts = createLiquibaseInserts && false;
    //    boolean createResidencePermitTypeInserts = createLiquibaseInserts && false;
    //    boolean createResidencyStatusInserts = createLiquibaseInserts && false;
    //    boolean createResidentialTypeInserts = createLiquibaseInserts && false;
    //    boolean createSourceOfFundsInserts = createLiquibaseInserts && false;
    //    boolean createTaxNumberTypeInserts = createLiquibaseInserts && false;
    //    boolean createTimeToContactInserts = createLiquibaseInserts && false;
    //    boolean createTitleInserts = createLiquibaseInserts && false;

    String fileName =
        System.getProperty("user.dir")
            + File.separator
            + "target"
            + File.separator
            + "party-reference-liquibase-inserts.txt";

    System.out.println("File Name: " + fileName);

    PrintWriter writer = new PrintWriter(new FileWriter(fileName));

    for (String localeId : LOCALE_IDS) {

      if (createAttributeTypeCategoryInserts) {
        for (AttributeTypeCategory attributeTypeCategory :
            partyReferenceService.getAttributeTypeCategories(localeId)) {

          writer.println(
              "    <insert schemaName=\"party\" tableName=\"attribute_type_categories\">");
          writer.println(
              "      <column name=\"code\" value=\"" + attributeTypeCategory.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + attributeTypeCategory.getLocaleId()
                  + "\"/>");
          if (attributeTypeCategory.getTenantId() != null) {
            writer.println(
                "      <column name=\"tenant_id\" value=\""
                    + attributeTypeCategory.getTenantId()
                    + "\"/>");
          }

          if (attributeTypeCategory.getSortIndex() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + attributeTypeCategory.getSortIndex()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + attributeTypeCategory.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + attributeTypeCategory.getDescription()
                  + "\"/>");
          writer.println("    </insert>");
        }

        writer.println();
      }

      //    if (createContactMechanismTypeInserts) {
      //      for (ContactMechanismType contactMechanismType :
      //          referenceService.getContactMechanismTypes("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"contact_mechanism_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + contactMechanismType.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + contactMechanismType.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + contactMechanismType.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + contactMechanismType.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"plural\" value=\"" + contactMechanismType.getPlural() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + contactMechanismType.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createContactMechanismRoleInserts) {
      //      for (ContactMechanismRole contactMechanismRole :
      //          referenceService.getContactMechanismRoles("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"contact_mechanism_roles\">");
      //        writer.println(
      //            "      <column name=\"type\" value=\"" + contactMechanismRole.getType() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + contactMechanismRole.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + contactMechanismRole.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + contactMechanismRole.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + contactMechanismRole.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + contactMechanismRole.getDescription()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"party_types\" value=\""
      //                +
      // StringUtils.arrayToCommaDelimitedString(contactMechanismRole.getPartyTypes())
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createEmploymentStatusInserts) {
      //      for (EmploymentStatus employmentStatus :
      // referenceService.getEmploymentStatuses("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"employment_statuses\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + employmentStatus.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + employmentStatus.getLocaleId() +
      // "\"/>");
      //    if (employmentStatus.getSortIndex() != null) {
      //      writer.println("  <column name=\"sort_index\" value=\"" +
      // employmentStatus.getSortIndex() + "\"/>");
      //    }
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + employmentStatus.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + employmentStatus.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createEmploymentTypeInserts) {
      //      for (EmploymentType employmentType : referenceService.getEmploymentTypes("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"employment_types\">");
      //        writer.println(
      //            "      <column name=\"employment_status\" value=\""
      //                + employmentType.getEmploymentStatus()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + employmentType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + employmentType.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + employmentType.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + employmentType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + employmentType.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createGenderInserts) {
      //      for (Gender gender : referenceService.getGenders("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\" tableName=\"genders\">");
      //        writer.println("      <column name=\"code\" value=\"" + gender.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + gender.getLocaleId() + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + gender.getSortIndex() + "\"/>");
      //        writer.println("      <column name=\"name\" value=\"" + gender.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\"" + gender.getDescription() +
      // "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createIdentityDocumentTypeInserts) {
      //      for (IdentityDocumentType identityDocumentType :
      //          referenceService.getIdentityDocumentTypes("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"identity_document_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + identityDocumentType.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + identityDocumentType.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + identityDocumentType.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + identityDocumentType.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + identityDocumentType.getDescription()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"country_of_issue\" value=\""
      //                + identityDocumentType.getCountryOfIssue()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"party_types\" value=\""
      //                +
      // StringUtils.arrayToCommaDelimitedString(identityDocumentType.getPartyTypes())
      //                + "\"/>");
      //
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createMaritalStatusInserts) {
      //      for (MaritalStatus maritalStatus : referenceService.getMaritalStatuses("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"marital_statuses\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + maritalStatus.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + maritalStatus.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + maritalStatus.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + maritalStatus.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + maritalStatus.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createMarriageTypeInserts) {
      //      for (MarriageType marriageType : referenceService.getMarriageTypes("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"marriage_types\">");
      //        writer.println(
      //            "      <column name=\"marital_status\" value=\""
      //                + marriageType.getMaritalStatus()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + marriageType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + marriageType.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + marriageType.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + marriageType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\"" + marriageType.getDescription() +
      // "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createMinorTypeInserts) {
      //      for (MinorType minorType : referenceService.getMinorTypes("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\" tableName=\"minor_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + minorType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + minorType.getLocaleId() + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + minorType.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + minorType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + minorType.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createNextOfKinInserts) {
      //      for (NextOfKinType nextOfKinType : referenceService.getNextOfKinTypes("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"next_of_kin_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + nextOfKinType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + nextOfKinType.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + nextOfKinType.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + nextOfKinType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + nextOfKinType.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createOccupationInserts) {
      //      for (Occupation occupation : referenceService.getOccupations("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\" tableName=\"occupations\">");
      //        writer.println("      <column name=\"code\" value=\"" + occupation.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + occupation.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + occupation.getSortIndex() +
      // "\"/>");
      //        writer.println("      <column name=\"name\" value=\"" + occupation.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\"" + occupation.getDescription() +
      // "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createAttributeTypeCategoryInserts) {
      //      for (AttributeTypeCategory attributeTypeCategory :
      //          referenceService.getAttributeTypeCategories("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\"
      // tableName=\"attribute_type_categories\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + attributeTypeCategory.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + attributeTypeCategory.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + attributeTypeCategory.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + attributeTypeCategory.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + attributeTypeCategory.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createAttributeTypeInserts) {
      //      for (AttributeType attributeType :
      //          referenceService.getAttributeTypes("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"attribute_types\">");
      //        writer.println(
      //            "      <column name=\"category\" value=\"" + attributeType.getCategory() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + attributeType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + attributeType.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + attributeType.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + attributeType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + attributeType.getDescription()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"party_types\" value=\""
      //                + StringUtils.arrayToCommaDelimitedString(attributeType.getPartyTypes())
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createRolePurposeInserts) {
      //      for (RolePurpose rolePurpose : referenceService.getRolePurposes("en-US"))
      // {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"role_purposes\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + rolePurpose.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + rolePurpose.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + rolePurpose.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + rolePurpose.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + rolePurpose.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createRoleTypeInserts) {
      //      for (RoleType roleType : referenceService.getRoleTypes("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"role_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + roleType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + roleType.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + roleType.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + roleType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + roleType.getDescription()
      //                + "\"/>");
      //
      //        writer.println(
      //            "      <column name=\"party_types\" value=\""
      //                + StringUtils.arrayToCommaDelimitedString(roleType.getPartyTypes())
      //                + "\"/>");
      //
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createPhysicalAddressPurposeInserts) {
      //      for (PhysicalAddressPurpose physicalAddressPurpose :
      //          referenceService.getPhysicalAddressPurposes("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\"
      // tableName=\"physical_address_purposes\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + physicalAddressPurpose.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + physicalAddressPurpose.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + physicalAddressPurpose.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + physicalAddressPurpose.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + physicalAddressPurpose.getDescription()
      //                + "\"/>");
      //
      //        writer.println(
      //            "      <column name=\"party_types\" value=\""
      //                +
      // StringUtils.arrayToCommaDelimitedString(physicalAddressPurpose.getPartyTypes())
      //                + "\"/>");
      //
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createPhysicalAddressTypeInserts) {
      //      for (PhysicalAddressType physicalAddressType :
      //          referenceService.getPhysicalAddressTypes("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"physical_address_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + physicalAddressType.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + physicalAddressType.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + physicalAddressType.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + physicalAddressType.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + physicalAddressType.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createPreferenceTypeCategoryInserts) {
      //      for (PreferenceTypeCategory preferenceTypeCategory :
      //          referenceService.getPreferenceTypeCategories("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\"
      // tableName=\"preference_type_categories\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + preferenceTypeCategory.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + preferenceTypeCategory.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + preferenceTypeCategory.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + preferenceTypeCategory.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + preferenceTypeCategory.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createPreferenceTypeInserts) {
      //      for (PreferenceType preferenceType : referenceService.getPreferenceTypes("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"preference_types\">");
      //        writer.println(
      //            "      <column name=\"category\" value=\"" + preferenceType.getCategory() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + preferenceType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + preferenceType.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + preferenceType.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + preferenceType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + preferenceType.getDescription()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"party_types\" value=\""
      //                + StringUtils.arrayToCommaDelimitedString(preferenceType.getPartyTypes())
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createRaceInserts) {
      //      for (Race race : referenceService.getRaces("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\" tableName=\"races\">");
      //        writer.println("      <column name=\"code\" value=\"" + race.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + race.getLocaleId() + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + race.getSortIndex() + "\"/>");
      //        writer.println("      <column name=\"name\" value=\"" + race.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\"" + race.getDescription() + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createResidencePermitTypeInserts) {
      //      for (ResidencePermitType residencePermitType :
      //          referenceService.getResidencePermitTypes("en-US")) {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"residence_permit_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + residencePermitType.getCode() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\""
      //                + residencePermitType.getLocaleId()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\""
      //                + residencePermitType.getSortIndex()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + residencePermitType.getName() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + residencePermitType.getDescription()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"country_of_issue\" value=\""
      //                + residencePermitType.getCountryOfIssue()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createResidencyStatusInserts) {
      //      for (ResidencyStatus residencyStatus : referenceService.getResidencyStatuses("en-US"))
      // {
      //
      //        writer.println(
      //            "    <insert schemaName=\"reference\" tableName=\"residency_statuses\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + residencyStatus.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + residencyStatus.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + residencyStatus.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + residencyStatus.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + residencyStatus.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createResidentialTypeInserts) {
      //      for (ResidentialType residentialType : referenceService.getResidentialTypes("en-US"))
      // {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"residential_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + residentialType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + residentialType.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + residentialType.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + residentialType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + residentialType.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createSourceOfFundsInserts) {
      //      for (SourceOfFunds sourceOfFunds : referenceService.getSourcesOfFunds("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"sources_of_funds\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + sourceOfFunds.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + sourceOfFunds.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + sourceOfFunds.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + sourceOfFunds.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + sourceOfFunds.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createTaxNumberTypeInserts) {
      //      for (TaxNumberType taxNumberType : referenceService.getTaxNumberTypes("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"tax_number_types\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + taxNumberType.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + taxNumberType.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + taxNumberType.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + taxNumberType.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + taxNumberType.getDescription()
      //                + "\"/>");
      //        writer.println(
      //            "      <column name=\"country_of_issue\" value=\""
      //                + taxNumberType.getCountryOfIssue()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createTimeToContactInserts) {
      //      for (TimeToContact timeToContact : referenceService.getTimesToContact("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\"
      // tableName=\"times_to_contact\">");
      //        writer.println(
      //            "      <column name=\"code\" value=\"" + timeToContact.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + timeToContact.getLocaleId() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + timeToContact.getSortIndex() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"name\" value=\"" + timeToContact.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\""
      //                + timeToContact.getDescription()
      //                + "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }

      //    if (createTitleInserts) {
      //      for (Title title : referenceService.getTitles("en-US")) {
      //
      //        writer.println("    <insert schemaName=\"reference\" tableName=\"titles\">");
      //        writer.println("      <column name=\"code\" value=\"" + title.getCode() + "\"/>");
      //        writer.println(
      //            "      <column name=\"locale_id\" value=\"" + title.getLocaleId() + "\"/>");
      //        writer.println(
      //            "      <column name=\"sort_index\" value=\"" + title.getSortIndex() + "\"/>");
      //        writer.println("      <column name=\"name\" value=\"" + title.getName() + "\"/>");
      //        writer.println(
      //            "      <column name=\"abbreviation\" value=\"" + title.getAbbreviation() +
      // "\"/>");
      //        writer.println(
      //            "      <column name=\"description\" value=\"" + title.getDescription() +
      // "\"/>");
      //        writer.println("    </insert>");
      //      }
      //
      //      writer.println();
      //    }
      writer.println();
      writer.println();
    }

    writer.flush();
    writer.close();
  }
}
