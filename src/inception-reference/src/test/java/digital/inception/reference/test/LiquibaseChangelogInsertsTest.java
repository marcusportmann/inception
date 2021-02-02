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

import static org.junit.Assert.fail;

import digital.inception.reference.ContactMechanismPurpose;
import digital.inception.reference.ContactMechanismType;
import digital.inception.reference.Country;
import digital.inception.reference.EmploymentStatus;
import digital.inception.reference.EmploymentType;
import digital.inception.reference.Gender;
import digital.inception.reference.IReferenceService;
import digital.inception.reference.IdentityDocumentType;
import digital.inception.reference.Language;
import digital.inception.reference.MaritalStatus;
import digital.inception.reference.MarriageType;
import digital.inception.reference.NextOfKinType;
import digital.inception.reference.Occupation;
import digital.inception.reference.PartyAttributeType;
import digital.inception.reference.PartyAttributeTypeCategory;
import digital.inception.reference.PartyRolePurpose;
import digital.inception.reference.PartyRoleType;
import digital.inception.reference.PhysicalAddressPurpose;
import digital.inception.reference.PhysicalAddressType;
import digital.inception.reference.PreferenceType;
import digital.inception.reference.PreferenceTypeCategory;
import digital.inception.reference.Race;
import digital.inception.reference.Region;
import digital.inception.reference.ResidencePermitType;
import digital.inception.reference.ResidencyStatus;
import digital.inception.reference.ResidentialType;
import digital.inception.reference.SourceOfFunds;
import digital.inception.reference.TaxNumberType;
import digital.inception.reference.TimeToContact;
import digital.inception.reference.Title;
import digital.inception.reference.VerificationMethod;
import digital.inception.reference.VerificationStatus;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.StringUtils;

/**
 * The <b>LiquibaseChangelogInsertsTest</b>.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
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
    boolean createContactMechanismTypeInserts = createLiquibaseInserts && false;
    boolean createContactMechanismPurposeInserts = createLiquibaseInserts && false;
    boolean createCountryInserts = createLiquibaseInserts && false;
    boolean createEmploymentStatusInserts = createLiquibaseInserts && false;
    boolean createEmploymentTypeInserts = createLiquibaseInserts && false;
    boolean createGenderInserts = createLiquibaseInserts && false;
    boolean createIdentityDocumentTypeInserts = createLiquibaseInserts && false;
    boolean createLanguageInserts = createLiquibaseInserts && false;
    boolean createMaritalStatusInserts = createLiquibaseInserts && false;
    boolean createMarriageTypeInserts = createLiquibaseInserts && false;
    boolean createMinorTypeInserts = createLiquibaseInserts && false;
    boolean createNextOfKinInserts = createLiquibaseInserts && false;
    boolean createOccupationInserts = createLiquibaseInserts && false;
    boolean createPartyAttributeTypeCategoryInserts = createLiquibaseInserts && true;
    boolean createPartyAttributeTypeInserts = createLiquibaseInserts && true;
    boolean createPartyRolePurposeInserts = createLiquibaseInserts && false;
    boolean createPartyRoleTypeInserts = createLiquibaseInserts && false;
    boolean createPhysicalAddressPurposeInserts = createLiquibaseInserts && false;
    boolean createPhysicalAddressTypeInserts = createLiquibaseInserts && false;
    boolean createPreferenceTypeCategoryInserts = createLiquibaseInserts && false;
    boolean createPreferenceTypeInserts = createLiquibaseInserts && false;
    boolean createRaceInserts = createLiquibaseInserts && false;
    boolean createRegionInserts = createLiquibaseInserts && false;
    boolean createResidencePermitTypeInserts = createLiquibaseInserts && false;
    boolean createResidencyStatusInserts = createLiquibaseInserts && false;
    boolean createResidentialTypeInserts = createLiquibaseInserts && false;
    boolean createSourceOfFundsInserts = createLiquibaseInserts && false;
    boolean createTaxNumberTypeInserts = createLiquibaseInserts && false;
    boolean createTimeToContactInserts = createLiquibaseInserts && false;
    boolean createTitleInserts = createLiquibaseInserts && false;
    boolean createVerificationMethodInserts = createLiquibaseInserts && false;
    boolean createVerificationStatusInserts = createLiquibaseInserts && false;

    if (createContactMechanismTypeInserts) {
      for (ContactMechanismType contactMechanismType :
          referenceService.getContactMechanismTypes("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"contact_mechanism_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + contactMechanismType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + contactMechanismType.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + contactMechanismType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + contactMechanismType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"plural\" value=\"" + contactMechanismType.getPlural() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + contactMechanismType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createContactMechanismPurposeInserts) {
      for (ContactMechanismPurpose contactMechanismPurpose :
          referenceService.getContactMechanismPurposes("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"contact_mechanism_purposes\">");
        System.out.println(
            "      <column name=\"type\" value=\"" + contactMechanismPurpose.getType() + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + contactMechanismPurpose.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + contactMechanismPurpose.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + contactMechanismPurpose.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + contactMechanismPurpose.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + contactMechanismPurpose.getDescription()
                + "\"/>");
        System.out.println(
            "      <column name=\"party_types\" value=\""
                + StringUtils.arrayToCommaDelimitedString(contactMechanismPurpose.getPartyTypes())
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createCountryInserts) {
      List<Country> sortedCountries =
          referenceService.getCountries("en-US").stream()
              .sorted(Comparator.comparing(Country::getName))
              .collect(Collectors.toList());

      int counter = 1;

      for (Country country : sortedCountries) {

        if (country.getNationality().contains(",")) {
          fail("Invalid nationality for country (" + country.getCode() + ")");
        }

        System.out.println("<insert schemaName=\"reference\" tableName=\"countries\">");
        System.out.println("  <column name=\"code\" value=\"" + country.getCode() + "\"/>");
        System.out.println(
            "  <column name=\"locale_id\" value=\"" + country.getLocaleId() + "\"/>");
        System.out.println("  <column name=\"sort_index\" value=\"" + counter + "\"/>");
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

        counter++;
      }

      System.out.println();
    }

    if (createEmploymentStatusInserts) {
      for (EmploymentStatus employmentStatus : referenceService.getEmploymentStatuses("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"employment_statuses\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + employmentStatus.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + employmentStatus.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + employmentStatus.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + employmentStatus.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + employmentStatus.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createEmploymentTypeInserts) {
      for (EmploymentType employmentType : referenceService.getEmploymentTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"employment_types\">");
        System.out.println(
            "      <column name=\"employment_status\" value=\""
                + employmentType.getEmploymentStatus()
                + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + employmentType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + employmentType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + employmentType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + employmentType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + employmentType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createGenderInserts) {
      for (Gender gender : referenceService.getGenders("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"genders\">");
        System.out.println("      <column name=\"code\" value=\"" + gender.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + gender.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + gender.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + gender.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + gender.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createIdentityDocumentTypeInserts) {
      for (IdentityDocumentType identityDocumentType :
          referenceService.getIdentityDocumentTypes("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"identity_document_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + identityDocumentType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + identityDocumentType.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + identityDocumentType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + identityDocumentType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + identityDocumentType.getDescription()
                + "\"/>");
        System.out.println(
            "      <column name=\"country_of_issue\" value=\""
                + identityDocumentType.getCountryOfIssue()
                + "\"/>");
        System.out.println(
            "      <column name=\"party_types\" value=\""
                + StringUtils.arrayToCommaDelimitedString(identityDocumentType.getPartyTypes())
                + "\"/>");

        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createLanguageInserts) {
      for (Language language : referenceService.getLanguages("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"languages\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + language.getCode().toUpperCase() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + language.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + language.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + language.getName() + "\"/>");
        System.out.println(
            "      <column name=\"short_name\" value=\"" + language.getShortName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + language.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createMaritalStatusInserts) {
      for (MaritalStatus maritalStatus : referenceService.getMaritalStatuses("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"marital_statuses\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + maritalStatus.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + maritalStatus.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + maritalStatus.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + maritalStatus.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + maritalStatus.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createMarriageTypeInserts) {
      for (MarriageType marriageType : referenceService.getMarriageTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"marriage_types\">");
        System.out.println(
            "      <column name=\"marital_status\" value=\""
                + marriageType.getMaritalStatus()
                + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + marriageType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + marriageType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + marriageType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + marriageType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + marriageType.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    //    if (createMinorTypeInserts) {
    //      for (MinorType minorType : referenceService.getMinorTypes("en-US")) {
    //
    //        System.out.println("    <insert schemaName=\"reference\" tableName=\"minor_types\">");
    //        System.out.println(
    //            "      <column name=\"code\" value=\"" + minorType.getCode() + "\"/>");
    //        System.out.println(
    //            "      <column name=\"locale_id\" value=\"" + minorType.getLocaleId() + "\"/>");
    //        System.out.println(
    //            "      <column name=\"sort_index\" value=\"" + minorType.getSortIndex() + "\"/>");
    //        System.out.println(
    //            "      <column name=\"name\" value=\"" + minorType.getName() + "\"/>");
    //        System.out.println(
    //            "      <column name=\"description\" value=\""
    //                + minorType.getDescription()
    //                + "\"/>");
    //        System.out.println("    </insert>");
    //      }
    //
    //      System.out.println();
    //    }

    if (createNextOfKinInserts) {
      for (NextOfKinType nextOfKinType : referenceService.getNextOfKinTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"next_of_kin_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + nextOfKinType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + nextOfKinType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + nextOfKinType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + nextOfKinType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + nextOfKinType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createOccupationInserts) {
      for (Occupation occupation : referenceService.getOccupations("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"occupations\">");
        System.out.println("      <column name=\"code\" value=\"" + occupation.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + occupation.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + occupation.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + occupation.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + occupation.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }





    if (createPartyAttributeTypeCategoryInserts) {
      for (PartyAttributeTypeCategory partyAttributeTypeCategory :
          referenceService.getPartyAttributeTypeCategories("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"party_attribute_type_categories\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + partyAttributeTypeCategory.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + partyAttributeTypeCategory.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + partyAttributeTypeCategory.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + partyAttributeTypeCategory.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + partyAttributeTypeCategory.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createPartyAttributeTypeInserts) {
      for (PartyAttributeType partyAttributeType : referenceService.getPartyAttributeTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"party_attribute_types\">");
        System.out.println(
            "      <column name=\"category\" value=\"" + partyAttributeType.getCategory() + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + partyAttributeType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + partyAttributeType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + partyAttributeType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + partyAttributeType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + partyAttributeType.getDescription()
                + "\"/>");
        System.out.println(
            "      <column name=\"party_types\" value=\""
                + StringUtils.arrayToCommaDelimitedString(partyAttributeType.getPartyTypes())
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }









    if (createPartyRolePurposeInserts) {
      for (PartyRolePurpose partyRolePurpose : referenceService.getPartyRolePurposes("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"party_role_purposes\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + partyRolePurpose.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + partyRolePurpose.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + partyRolePurpose.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + partyRolePurpose.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + partyRolePurpose.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createPartyRoleTypeInserts) {
      for (PartyRoleType partyRoleType : referenceService.getPartyRoleTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"party_role_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + partyRoleType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + partyRoleType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + partyRoleType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + partyRoleType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + partyRoleType.getDescription()
                + "\"/>");

        System.out.println(
            "      <column name=\"party_types\" value=\""
                + StringUtils.arrayToCommaDelimitedString(partyRoleType.getPartyTypes())
                + "\"/>");

        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createPhysicalAddressPurposeInserts) {
      for (PhysicalAddressPurpose physicalAddressPurpose :
          referenceService.getPhysicalAddressPurposes("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"physical_address_purposes\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + physicalAddressPurpose.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + physicalAddressPurpose.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + physicalAddressPurpose.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + physicalAddressPurpose.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + physicalAddressPurpose.getDescription()
                + "\"/>");

        System.out.println(
            "      <column name=\"party_types\" value=\""
                + StringUtils.arrayToCommaDelimitedString(physicalAddressPurpose.getPartyTypes())
                + "\"/>");

        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createPhysicalAddressTypeInserts) {
      for (PhysicalAddressType physicalAddressType :
          referenceService.getPhysicalAddressTypes("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"physical_address_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + physicalAddressType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + physicalAddressType.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + physicalAddressType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + physicalAddressType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + physicalAddressType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createPreferenceTypeCategoryInserts) {
      for (PreferenceTypeCategory preferenceTypeCategory :
          referenceService.getPreferenceTypeCategories("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"preference_type_categories\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + preferenceTypeCategory.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + preferenceTypeCategory.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + preferenceTypeCategory.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + preferenceTypeCategory.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + preferenceTypeCategory.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createPreferenceTypeInserts) {
      for (PreferenceType preferenceType : referenceService.getPreferenceTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"preference_types\">");
        System.out.println(
            "      <column name=\"category\" value=\"" + preferenceType.getCategory() + "\"/>");
        System.out.println(
            "      <column name=\"code\" value=\"" + preferenceType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + preferenceType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + preferenceType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + preferenceType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + preferenceType.getDescription()
                + "\"/>");
        System.out.println(
            "      <column name=\"party_types\" value=\""
                + StringUtils.arrayToCommaDelimitedString(preferenceType.getPartyTypes())
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createRaceInserts) {
      for (Race race : referenceService.getRaces("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"races\">");
        System.out.println("      <column name=\"code\" value=\"" + race.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + race.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + race.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + race.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + race.getDescription() + "\"/>");
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
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + region.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + region.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + region.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createResidencePermitTypeInserts) {
      for (ResidencePermitType residencePermitType :
          referenceService.getResidencePermitTypes("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"residence_permit_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + residencePermitType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + residencePermitType.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + residencePermitType.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + residencePermitType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + residencePermitType.getDescription()
                + "\"/>");
        System.out.println(
            "      <column name=\"country_of_issue\" value=\""
                + residencePermitType.getCountryOfIssue()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createResidencyStatusInserts) {
      for (ResidencyStatus residencyStatus : referenceService.getResidencyStatuses("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"residency_statuses\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + residencyStatus.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + residencyStatus.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + residencyStatus.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + residencyStatus.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + residencyStatus.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createResidentialTypeInserts) {
      for (ResidentialType residentialType : referenceService.getResidentialTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"residential_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + residentialType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + residentialType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + residentialType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + residentialType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + residentialType.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createSourceOfFundsInserts) {
      for (SourceOfFunds sourceOfFunds : referenceService.getSourcesOfFunds("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"sources_of_funds\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + sourceOfFunds.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + sourceOfFunds.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + sourceOfFunds.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + sourceOfFunds.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + sourceOfFunds.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createTaxNumberTypeInserts) {
      for (TaxNumberType taxNumberType : referenceService.getTaxNumberTypes("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"tax_number_types\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + taxNumberType.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + taxNumberType.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + taxNumberType.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + taxNumberType.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + taxNumberType.getDescription()
                + "\"/>");
        System.out.println(
            "      <column name=\"country_of_issue\" value=\""
                + taxNumberType.getCountryOfIssue()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createTimeToContactInserts) {
      for (TimeToContact timeToContact : referenceService.getTimesToContact("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"times_to_contact\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + timeToContact.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + timeToContact.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + timeToContact.getSortIndex() + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + timeToContact.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + timeToContact.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createTitleInserts) {
      for (Title title : referenceService.getTitles("en-US")) {

        System.out.println("    <insert schemaName=\"reference\" tableName=\"titles\">");
        System.out.println("      <column name=\"code\" value=\"" + title.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\"" + title.getLocaleId() + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\"" + title.getSortIndex() + "\"/>");
        System.out.println("      <column name=\"name\" value=\"" + title.getName() + "\"/>");
        System.out.println(
            "      <column name=\"abbreviation\" value=\"" + title.getAbbreviation() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\"" + title.getDescription() + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createVerificationMethodInserts) {
      for (VerificationMethod verificationMethod :
          referenceService.getVerificationMethods("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"verification_methods\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + verificationMethod.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + verificationMethod.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + verificationMethod.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + verificationMethod.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + verificationMethod.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }

    if (createVerificationStatusInserts) {
      for (VerificationStatus verificationStatus :
          referenceService.getVerificationStatuses("en-US")) {

        System.out.println(
            "    <insert schemaName=\"reference\" tableName=\"verification_statuses\">");
        System.out.println(
            "      <column name=\"code\" value=\"" + verificationStatus.getCode() + "\"/>");
        System.out.println(
            "      <column name=\"locale_id\" value=\""
                + verificationStatus.getLocaleId()
                + "\"/>");
        System.out.println(
            "      <column name=\"sort_index\" value=\""
                + verificationStatus.getSortIndex()
                + "\"/>");
        System.out.println(
            "      <column name=\"name\" value=\"" + verificationStatus.getName() + "\"/>");
        System.out.println(
            "      <column name=\"description\" value=\""
                + verificationStatus.getDescription()
                + "\"/>");
        System.out.println("    </insert>");
      }

      System.out.println();
    }
  }
}
