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

package digital.inception.party.test;

import digital.inception.party.model.AssociationPropertyType;
import digital.inception.party.model.AssociationType;
import digital.inception.party.model.AttributeType;
import digital.inception.party.model.AttributeTypeCategory;
import digital.inception.party.model.ConsentType;
import digital.inception.party.model.ContactMechanismPurpose;
import digital.inception.party.model.ContactMechanismRole;
import digital.inception.party.model.ContactMechanismType;
import digital.inception.party.model.EmploymentStatus;
import digital.inception.party.model.EmploymentType;
import digital.inception.party.model.ExternalReferenceType;
import digital.inception.party.model.FieldOfStudy;
import digital.inception.party.model.Gender;
import digital.inception.party.model.IdentificationType;
import digital.inception.party.model.LockType;
import digital.inception.party.model.LockTypeCategory;
import digital.inception.party.model.MaritalStatus;
import digital.inception.party.model.MarriageType;
import digital.inception.party.model.NextOfKinType;
import digital.inception.party.model.Occupation;
import digital.inception.party.model.PhysicalAddressPurpose;
import digital.inception.party.model.PhysicalAddressRole;
import digital.inception.party.model.PhysicalAddressType;
import digital.inception.party.model.PreferenceType;
import digital.inception.party.model.PreferenceTypeCategory;
import digital.inception.party.model.QualificationType;
import digital.inception.party.model.Race;
import digital.inception.party.model.ResidencePermitType;
import digital.inception.party.model.ResidencyStatus;
import digital.inception.party.model.ResidentialType;
import digital.inception.party.model.RolePurpose;
import digital.inception.party.model.RoleType;
import digital.inception.party.model.RoleTypeAttributeTypeConstraint;
import digital.inception.party.model.RoleTypePreferenceTypeConstraint;
import digital.inception.party.model.Segment;
import digital.inception.party.model.SourceOfFundsType;
import digital.inception.party.model.SourceOfWealthType;
import digital.inception.party.model.StatusType;
import digital.inception.party.model.StatusTypeCategory;
import digital.inception.party.model.TaxNumberType;
import digital.inception.party.model.TimeToContact;
import digital.inception.party.model.Title;
import digital.inception.party.service.PartyReferenceService;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.util.StringUtils;

/**
 * The {@code GenerateLiquibaseChangelog} class.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
@ComponentScan(
    basePackages = {"digital.inception"},
    lazyInit = true,
    excludeFilters = {
      @ComponentScan.Filter(value = SpringBootApplication.class, type = FilterType.ANNOTATION),
      @ComponentScan.Filter(
          pattern = "digital\\.inception\\.application\\.ApplicationDataSourceConfiguration",
          type = FilterType.REGEX),
      @ComponentScan.Filter(
          pattern = "digital\\.inception\\.application\\.ApplicationTransactionManager",
          type = FilterType.REGEX),
      @ComponentScan.Filter(
          pattern = "digital\\.inception\\.persistence\\.PersistenceConfiguration",
          type = FilterType.REGEX)
    })
public class GenerateLiquibaseDataChangelog implements CommandLineRunner {

  private static final Set<String> LOCALE_IDS = Set.of("en-US", "en-ZA");

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(GenerateLiquibaseDataChangelog.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The Party Reference Service. */
  private final PartyReferenceService partyReferenceService;

  /**
   * Constructs a new {@code GenerateLiquibaseDataChangelog}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param partyReferenceService the Party Reference Service
   */
  public GenerateLiquibaseDataChangelog(
      ApplicationContext applicationContext, PartyReferenceService partyReferenceService) {
    this.applicationContext = applicationContext;
    this.partyReferenceService = partyReferenceService;
  }

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(GenerateLiquibaseDataChangelog.class);
    app.setBannerMode(Mode.OFF);
    app.setWebApplicationType(WebApplicationType.NONE);
    app.run(args);
  }

  @Override
  public void run(String... args) throws Exception {
    String filename =
        System.getProperty("user.dir")
            + File.separator
            + "inception-database"
            + File.separator
            + "inception-party-data.changelog.xml";

    log.info("Writing party data to file " + filename);

    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
      writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      writer.println("");
      writer.println("<databaseChangeLog");
      writer.println("  xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"");
      writer.println("  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
      writer.println("  xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog");
      writer.println("         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd\">");
      writer.println("");
      writer.println("  <changeSet id=\"inception-party-data-1.0.0\" author=\"Marcus Portmann\">");
      writer.println("    <comment>Inception - Party - Data - 1.0.0</comment>");
      writer.println("");

      writeReferenceData(writer);

      writer.println("  </changeSet>");
      writer.println("</databaseChangeLog>");
    }

    System.exit(SpringApplication.exit(applicationContext));
  }

  private void writeReferenceData(PrintWriter writer) throws Exception {
    for (String localeId : LOCALE_IDS) {
      // Association Types
      for (AssociationType associationType : partyReferenceService.getAssociationTypes(localeId)) {

        if (associationType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"association_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + associationType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + associationType.getLocaleId() + "\"/>");

          if (associationType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + associationType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + associationType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + associationType.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"first_party_role\" value=\""
                  + associationType.getFirstPartyRole()
                  + "\"/>");
          writer.println(
              "      <column name=\"second_party_role\" value=\""
                  + associationType.getSecondPartyRole()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Association Property Types
      for (AssociationPropertyType associationPropertyType :
          partyReferenceService.getAssociationPropertyTypes(localeId)) {

        if (associationPropertyType.getTenantId() == null) {
          writer.println(
              "    <insert schemaName=\"party\" tableName=\"association_property_types\">");
          writer.println(
              "      <column name=\"association_type\" value=\""
                  + associationPropertyType.getAssociationType()
                  + "\"/>");
          writer.println(
              "      <column name=\"code\" value=\"" + associationPropertyType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + associationPropertyType.getLocaleId()
                  + "\"/>");

          if (associationPropertyType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + associationPropertyType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + associationPropertyType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + associationPropertyType.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"value_type\" value=\""
                  + associationPropertyType.getValueType().code()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Attribute Type Categories
      for (AttributeTypeCategory attributeTypeCategory :
          partyReferenceService.getAttributeTypeCategories(localeId)) {

        if (attributeTypeCategory.getTenantId() == null) {
          writer.println(
              "    <insert schemaName=\"party\" tableName=\"attribute_type_categories\">");
          writer.println(
              "      <column name=\"code\" value=\"" + attributeTypeCategory.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + attributeTypeCategory.getLocaleId()
                  + "\"/>");

          if (attributeTypeCategory.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + attributeTypeCategory.getSortOrder()
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
      }

      writer.println();

      // Attribute Types
      for (AttributeType attributeType : partyReferenceService.getAttributeTypes(localeId)) {

        if (attributeType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"attribute_types\">");
          writer.println(
              "      <column name=\"category\" value=\"" + attributeType.getCategory() + "\"/>");
          writer.println("      <column name=\"code\" value=\"" + attributeType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + attributeType.getLocaleId() + "\"/>");

          if (attributeType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + attributeType.getSortOrder()
                    + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + attributeType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + attributeType.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(attributeType.getPartyTypes())
                  + "\"/>");
          if (attributeType.getUnitType() != null) {
            writer.println(
                "      <column name=\"unit_type\" value=\""
                    + attributeType.getUnitType().code()
                    + "\"/>");
          }
          writer.println(
              "      <column name=\"value_type\" value=\""
                  + attributeType.getValueType().code()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Consent Types
      for (ConsentType consentType : partyReferenceService.getConsentTypes(localeId)) {

        if (consentType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"consent_types\">");
          writer.println("      <column name=\"code\" value=\"" + consentType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + consentType.getLocaleId() + "\"/>");

          if (consentType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + consentType.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + consentType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + consentType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Contact Mechanism Purposes
      for (ContactMechanismPurpose contactMechanismPurpose :
          partyReferenceService.getContactMechanismPurposes(localeId)) {

        if (contactMechanismPurpose.getTenantId() == null) {
          writer.println(
              "    <insert schemaName=\"party\" tableName=\"contact_mechanism_purposes\">");
          writer.println(
              "      <column name=\"code\" value=\"" + contactMechanismPurpose.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + contactMechanismPurpose.getLocaleId()
                  + "\"/>");

          if (contactMechanismPurpose.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + contactMechanismPurpose.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + contactMechanismPurpose.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + contactMechanismPurpose.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"contact_mechanism_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(
                      contactMechanismPurpose.getContactMechanismTypes())
                  + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(
                      contactMechanismPurpose.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Contact Mechanism Types
      for (ContactMechanismType contactMechanismType :
          partyReferenceService.getContactMechanismTypes(localeId)) {

        if (contactMechanismType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"contact_mechanism_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + contactMechanismType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + contactMechanismType.getLocaleId()
                  + "\"/>");

          if (contactMechanismType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + contactMechanismType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + contactMechanismType.getName() + "\"/>");
          writer.println(
              "      <column name=\"plural\" value=\"" + contactMechanismType.getPlural() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + contactMechanismType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Contact Mechanism Roles
      for (ContactMechanismRole contactMechanismRole :
          partyReferenceService.getContactMechanismRoles(localeId)) {

        if (contactMechanismRole.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"contact_mechanism_roles\">");
          writer.println(
              "      <column name=\"contact_mechanism_type\" value=\""
                  + contactMechanismRole.getContactMechanismType()
                  + "\"/>");
          writer.println(
              "      <column name=\"code\" value=\"" + contactMechanismRole.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + contactMechanismRole.getLocaleId()
                  + "\"/>");

          if (contactMechanismRole.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + contactMechanismRole.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + contactMechanismRole.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + contactMechanismRole.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(
                      contactMechanismRole.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Employment Statuses
      for (EmploymentStatus employmentStatus :
          partyReferenceService.getEmploymentStatuses(localeId)) {

        if (employmentStatus.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"employment_statuses\">");
          writer.println(
              "      <column name=\"code\" value=\"" + employmentStatus.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + employmentStatus.getLocaleId()
                  + "\"/>");

          if (employmentStatus.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + employmentStatus.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + employmentStatus.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + employmentStatus.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Employment Statuses
      for (EmploymentType employmentType : partyReferenceService.getEmploymentTypes(localeId)) {

        if (employmentType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"employment_types\">");
          writer.println(
              "      <column name=\"employment_status\" value=\""
                  + employmentType.getEmploymentStatus()
                  + "\"/>");
          writer.println(
              "      <column name=\"code\" value=\"" + employmentType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + employmentType.getLocaleId() + "\"/>");

          if (employmentType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + employmentType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + employmentType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + employmentType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // External Reference Types
      for (ExternalReferenceType externalReferenceType :
          partyReferenceService.getExternalReferenceTypes(localeId)) {

        if (externalReferenceType.getTenantId() == null) {
          writer.println(
              "    <insert schemaName=\"party\" tableName=\"external_reference_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + externalReferenceType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + externalReferenceType.getLocaleId()
                  + "\"/>");

          if (externalReferenceType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + externalReferenceType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + externalReferenceType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + externalReferenceType.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(
                      externalReferenceType.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Fields Of Study
      for (FieldOfStudy fieldOfStudy : partyReferenceService.getFieldsOfStudy(localeId)) {

        if (fieldOfStudy.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"fields_of_study\">");
          writer.println("      <column name=\"code\" value=\"" + fieldOfStudy.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + fieldOfStudy.getLocaleId() + "\"/>");

          if (fieldOfStudy.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + fieldOfStudy.getSortOrder()
                    + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + fieldOfStudy.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + fieldOfStudy.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Genders
      for (Gender gender : partyReferenceService.getGenders(localeId)) {

        if (gender.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"genders\">");
          writer.println("      <column name=\"code\" value=\"" + gender.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + gender.getLocaleId() + "\"/>");

          if (gender.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + gender.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + gender.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + gender.getDescription() + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Identification Document Types
      for (IdentificationType identificationType :
          partyReferenceService.getIdentificationTypes(localeId)) {

        if (identificationType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"identification_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + identificationType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + identificationType.getLocaleId()
                  + "\"/>");

          if (identificationType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + identificationType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + identificationType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + identificationType.getDescription()
                  + "\"/>");
          if (identificationType.getCountryOfIssue() != null) {
            writer.println(
                "      <column name=\"country_of_issue\" value=\""
                    + identificationType.getCountryOfIssue()
                    + "\"/>");
          }
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(identificationType.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Lock Type Categories
      for (LockTypeCategory lockTypeCategory :
          partyReferenceService.getLockTypeCategories(localeId)) {

        if (lockTypeCategory.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"lock_type_categories\">");
          writer.println(
              "      <column name=\"code\" value=\"" + lockTypeCategory.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + lockTypeCategory.getLocaleId()
                  + "\"/>");

          if (lockTypeCategory.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + lockTypeCategory.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + lockTypeCategory.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + lockTypeCategory.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Lock Types
      for (LockType lockType : partyReferenceService.getLockTypes(localeId)) {

        if (lockType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"lock_types\">");
          writer.println(
              "      <column name=\"category\" value=\"" + lockType.getCategory() + "\"/>");
          writer.println("      <column name=\"code\" value=\"" + lockType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + lockType.getLocaleId() + "\"/>");

          if (lockType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + lockType.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + lockType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + lockType.getDescription() + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(lockType.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Marital Statuses
      for (MaritalStatus maritalStatus : partyReferenceService.getMaritalStatuses(localeId)) {

        if (maritalStatus.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"marital_statuses\">");
          writer.println("      <column name=\"code\" value=\"" + maritalStatus.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + maritalStatus.getLocaleId() + "\"/>");

          if (maritalStatus.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + maritalStatus.getSortOrder()
                    + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + maritalStatus.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + maritalStatus.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Marriage Types
      for (MarriageType marriageType : partyReferenceService.getMarriageTypes(localeId)) {

        if (marriageType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"marriage_types\">");
          writer.println(
              "      <column name=\"marital_status\" value=\""
                  + marriageType.getMaritalStatus()
                  + "\"/>");
          writer.println("      <column name=\"code\" value=\"" + marriageType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + marriageType.getLocaleId() + "\"/>");

          if (marriageType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + marriageType.getSortOrder()
                    + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + marriageType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + marriageType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Next of Kin Types
      for (NextOfKinType nextOfKinType : partyReferenceService.getNextOfKinTypes(localeId)) {

        if (nextOfKinType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"next_of_kin_types\">");
          writer.println("      <column name=\"code\" value=\"" + nextOfKinType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + nextOfKinType.getLocaleId() + "\"/>");

          if (nextOfKinType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + nextOfKinType.getSortOrder()
                    + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + nextOfKinType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + nextOfKinType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Occupations
      for (Occupation occupation : partyReferenceService.getOccupations(localeId)) {

        if (occupation.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"occupations\">");
          writer.println("      <column name=\"code\" value=\"" + occupation.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + occupation.getLocaleId() + "\"/>");

          if (occupation.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + occupation.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + occupation.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + occupation.getDescription() + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Physical Address Types
      for (PhysicalAddressType physicalAddressType :
          partyReferenceService.getPhysicalAddressTypes(localeId)) {

        if (physicalAddressType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"physical_address_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + physicalAddressType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + physicalAddressType.getLocaleId()
                  + "\"/>");

          if (physicalAddressType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + physicalAddressType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + physicalAddressType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + physicalAddressType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Physical Address Purposes
      for (PhysicalAddressPurpose physicalAddressPurpose :
          partyReferenceService.getPhysicalAddressPurposes(localeId)) {

        if (physicalAddressPurpose.getTenantId() == null) {
          writer.println(
              "    <insert schemaName=\"party\" tableName=\"physical_address_purposes\">");
          writer.println(
              "      <column name=\"code\" value=\"" + physicalAddressPurpose.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + physicalAddressPurpose.getLocaleId()
                  + "\"/>");

          if (physicalAddressPurpose.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + physicalAddressPurpose.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + physicalAddressPurpose.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + physicalAddressPurpose.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(
                      physicalAddressPurpose.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Physical Address Roles
      for (PhysicalAddressRole physicalAddressRole :
          partyReferenceService.getPhysicalAddressRoles(localeId)) {

        if (physicalAddressRole.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"physical_address_roles\">");
          writer.println(
              "      <column name=\"code\" value=\"" + physicalAddressRole.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + physicalAddressRole.getLocaleId()
                  + "\"/>");

          if (physicalAddressRole.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + physicalAddressRole.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + physicalAddressRole.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + physicalAddressRole.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(
                      physicalAddressRole.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Preference Type Categories
      for (PreferenceTypeCategory preferenceTypeCategory :
          partyReferenceService.getPreferenceTypeCategories(localeId)) {

        if (preferenceTypeCategory.getTenantId() == null) {
          writer.println(
              "    <insert schemaName=\"party\" tableName=\"preference_type_categories\">");
          writer.println(
              "      <column name=\"code\" value=\"" + preferenceTypeCategory.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + preferenceTypeCategory.getLocaleId()
                  + "\"/>");

          if (preferenceTypeCategory.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + preferenceTypeCategory.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + preferenceTypeCategory.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + preferenceTypeCategory.getDescription()
                  + "\"/>");
          writer.println("    </insert>");
        }
      }

      writer.println();

      // Preference Types"
      for (PreferenceType preferenceType : partyReferenceService.getPreferenceTypes(localeId)) {

        if (preferenceType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"preference_types\">");
          writer.println(
              "      <column name=\"category\" value=\"" + preferenceType.getCategory() + "\"/>");
          writer.println(
              "      <column name=\"code\" value=\"" + preferenceType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + preferenceType.getLocaleId() + "\"/>");

          if (preferenceType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + preferenceType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + preferenceType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + preferenceType.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(preferenceType.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Qualification Types
      for (QualificationType qualificationType :
          partyReferenceService.getQualificationTypes(localeId)) {

        if (qualificationType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"qualification_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + qualificationType.getCode() + "\"/>");
          if (qualificationType.getFieldOfStudy() != null) {
            writer.println(
                "      <column name=\"field_of_study\" value=\""
                    + qualificationType.getFieldOfStudy()
                    + "\"/>");
          }
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + qualificationType.getLocaleId()
                  + "\"/>");

          if (qualificationType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + qualificationType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + qualificationType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + qualificationType.getDescription()
                  + "\"/>");
          writer.println("    </insert>");
        }
      }

      writer.println();

      // Races
      for (Race race : partyReferenceService.getRaces(localeId)) {

        if (race.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"races\">");
          writer.println("      <column name=\"code\" value=\"" + race.getCode() + "\"/>");
          writer.println("      <column name=\"locale_id\" value=\"" + race.getLocaleId() + "\"/>");

          if (race.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + race.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + race.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + race.getDescription() + "\"/>");
          writer.println("    </insert>");
        }
      }

      writer.println();

      // Residence Permit Types
      for (ResidencePermitType residencePermitType :
          partyReferenceService.getResidencePermitTypes(localeId)) {

        if (residencePermitType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"residence_permit_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + residencePermitType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + residencePermitType.getLocaleId()
                  + "\"/>");

          if (residencePermitType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + residencePermitType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + residencePermitType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + residencePermitType.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"country_of_issue\" value=\""
                  + residencePermitType.getCountryOfIssue()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Residency Statuses
      for (ResidencyStatus residencyStatus : partyReferenceService.getResidencyStatuses(localeId)) {

        if (residencyStatus.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"residency_statuses\">");
          writer.println(
              "      <column name=\"code\" value=\"" + residencyStatus.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + residencyStatus.getLocaleId() + "\"/>");

          if (residencyStatus.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + residencyStatus.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + residencyStatus.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + residencyStatus.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Residential Types
      for (ResidentialType residentialType : partyReferenceService.getResidentialTypes(localeId)) {

        if (residentialType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"residential_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + residentialType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + residentialType.getLocaleId() + "\"/>");

          if (residentialType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + residentialType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + residentialType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + residentialType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Role Purposes
      for (RolePurpose rolePurpose : partyReferenceService.getRolePurposes(localeId)) {

        if (rolePurpose.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"role_purposes\">");
          writer.println("      <column name=\"code\" value=\"" + rolePurpose.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + rolePurpose.getLocaleId() + "\"/>");

          if (rolePurpose.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + rolePurpose.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + rolePurpose.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + rolePurpose.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Role Types
      for (RoleType roleType : partyReferenceService.getRoleTypes(localeId)) {

        if (roleType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"role_types\">");
          writer.println("      <column name=\"code\" value=\"" + roleType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + roleType.getLocaleId() + "\"/>");

          if (roleType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + roleType.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + roleType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + roleType.getDescription() + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(roleType.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Segments
      for (Segment segment : partyReferenceService.getSegments(localeId)) {

        if (segment.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"segments\">");
          writer.println("      <column name=\"code\" value=\"" + segment.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + segment.getLocaleId() + "\"/>");

          if (segment.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + segment.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + segment.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + segment.getDescription() + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(segment.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Source Of Funds Types
      for (SourceOfFundsType sourceOfFundsType :
          partyReferenceService.getSourceOfFundsTypes(localeId)) {

        if (sourceOfFundsType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"source_of_funds_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + sourceOfFundsType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + sourceOfFundsType.getLocaleId()
                  + "\"/>");

          if (sourceOfFundsType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + sourceOfFundsType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + sourceOfFundsType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + sourceOfFundsType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Source Of Wealth Types
      for (SourceOfWealthType sourceOfWealthType :
          partyReferenceService.getSourceOfWealthTypes(localeId)) {

        if (sourceOfWealthType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"source_of_wealth_types\">");
          writer.println(
              "      <column name=\"code\" value=\"" + sourceOfWealthType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + sourceOfWealthType.getLocaleId()
                  + "\"/>");

          if (sourceOfWealthType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + sourceOfWealthType.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + sourceOfWealthType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + sourceOfWealthType.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Status Type Categories
      for (StatusTypeCategory statusTypeCategory :
          partyReferenceService.getStatusTypeCategories(localeId)) {

        if (statusTypeCategory.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"status_type_categories\">");
          writer.println(
              "      <column name=\"code\" value=\"" + statusTypeCategory.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\""
                  + statusTypeCategory.getLocaleId()
                  + "\"/>");

          if (statusTypeCategory.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + statusTypeCategory.getSortOrder()
                    + "\"/>");
          }

          writer.println(
              "      <column name=\"name\" value=\"" + statusTypeCategory.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + statusTypeCategory.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Status Types
      for (StatusType statusType : partyReferenceService.getStatusTypes(localeId)) {

        if (statusType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"status_types\">");
          writer.println(
              "      <column name=\"category\" value=\"" + statusType.getCategory() + "\"/>");
          writer.println("      <column name=\"code\" value=\"" + statusType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + statusType.getLocaleId() + "\"/>");

          if (statusType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + statusType.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + statusType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + statusType.getDescription() + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(statusType.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Tax Number Types
      for (TaxNumberType taxNumberType : partyReferenceService.getTaxNumberTypes(localeId)) {

        if (taxNumberType.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"tax_number_types\">");
          writer.println("      <column name=\"code\" value=\"" + taxNumberType.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + taxNumberType.getLocaleId() + "\"/>");

          if (taxNumberType.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + taxNumberType.getSortOrder()
                    + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + taxNumberType.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + taxNumberType.getDescription()
                  + "\"/>");
          writer.println(
              "      <column name=\"country_of_issue\" value=\""
                  + taxNumberType.getCountryOfIssue()
                  + "\"/>");
          writer.println(
              "      <column name=\"party_types\" value=\""
                  + StringUtils.collectionToCommaDelimitedString(taxNumberType.getPartyTypes())
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Times To Contact
      for (TimeToContact timeToContact : partyReferenceService.getTimesToContact(localeId)) {

        if (timeToContact.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"times_to_contact\">");
          writer.println("      <column name=\"code\" value=\"" + timeToContact.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + timeToContact.getLocaleId() + "\"/>");

          if (timeToContact.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\""
                    + timeToContact.getSortOrder()
                    + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + timeToContact.getName() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\""
                  + timeToContact.getDescription()
                  + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      // Titles
      for (Title title : partyReferenceService.getTitles(localeId)) {

        if (title.getTenantId() == null) {
          writer.println("    <insert schemaName=\"party\" tableName=\"titles\">");
          writer.println("      <column name=\"code\" value=\"" + title.getCode() + "\"/>");
          writer.println(
              "      <column name=\"locale_id\" value=\"" + title.getLocaleId() + "\"/>");

          if (title.getSortOrder() != null) {
            writer.println(
                "      <column name=\"sort_index\" value=\"" + title.getSortOrder() + "\"/>");
          }

          writer.println("      <column name=\"name\" value=\"" + title.getName() + "\"/>");
          writer.println(
              "      <column name=\"abbreviation\" value=\"" + title.getAbbreviation() + "\"/>");
          writer.println(
              "      <column name=\"description\" value=\"" + title.getDescription() + "\"/>");

          writer.println("    </insert>");
        }
      }

      writer.println();

      writer.println();
    }

    // Role Type Attribute Type Constraints
    for (RoleTypeAttributeTypeConstraint roleTypeAttributeTypeConstraint :
        partyReferenceService.getRoleTypeAttributeTypeConstraints()) {

      writer.println(
          "    <insert schemaName=\"party\" tableName=\"role_type_attribute_type_constraints\">");
      writer.println(
          "      <column name=\"role_type\" value=\""
              + roleTypeAttributeTypeConstraint.getRoleType()
              + "\"/>");
      writer.println(
          "      <column name=\"attribute_type\" value=\""
              + roleTypeAttributeTypeConstraint.getAttributeType()
              + "\"/>");
      writer.println(
          "      <column name=\"attribute_type_qualifier\" value=\""
              + roleTypeAttributeTypeConstraint.getAttributeTypeQualifier()
              + "\"/>");
      writer.println(
          "      <column name=\"type\" value=\""
              + roleTypeAttributeTypeConstraint.getType()
              + "\"/>");
      writer.println(
          "      <column name=\"value\" value=\""
              + roleTypeAttributeTypeConstraint.getValue()
              + "\"/>");

      writer.println("    </insert>");
    }

    writer.println();

    // Role Type Preference Type Constraints
    for (RoleTypePreferenceTypeConstraint roleTypePreferenceTypeConstraint :
        partyReferenceService.getRoleTypePreferenceTypeConstraints()) {

      writer.println(
          "    <insert schemaName=\"party\" tableName=\"role_type_preference_type_constraints\">");
      writer.println(
          "      <column name=\"role_type\" value=\""
              + roleTypePreferenceTypeConstraint.getRoleType()
              + "\"/>");
      writer.println(
          "      <column name=\"preference_type\" value=\""
              + roleTypePreferenceTypeConstraint.getPreferenceType()
              + "\"/>");
      writer.println(
          "      <column name=\"type\" value=\""
              + roleTypePreferenceTypeConstraint.getType()
              + "\"/>");
      writer.println(
          "      <column name=\"value\" value=\""
              + roleTypePreferenceTypeConstraint.getValue()
              + "\"/>");

      writer.println("    </insert>");
    }
  }
}
