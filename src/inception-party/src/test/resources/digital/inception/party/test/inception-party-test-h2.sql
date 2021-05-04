-- Test Tenant-specific Reference Data
INSERT INTO party.attribute_type_categories (code, locale_id, tenant_id, name, description)
  VALUES ('test_attribute_type_category', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Attribute Type Category', 'Test Attribute Type Category');

INSERT INTO party.attribute_type_categories (code, locale_id, tenant_id, name, description)
  VALUES ('test_attribute_type_category', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Attribute Type Category', 'Test Attribute Type Category');


INSERT INTO party.attribute_types (category, code, locale_id, tenant_id, name, description, party_types, value_type)
  VALUES ('test_attribute_type_category','test_attribute_type', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Attribute Type', 'Test Attribute Type', 'organization,person', 'string');

INSERT INTO party.attribute_types (category, code, locale_id, tenant_id, name, description, party_types, value_type)
  VALUES ('test_attribute_type_category','test_attribute_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Attribute Type', 'Test Attribute Type', 'organization,person', 'string');


INSERT INTO party.consent_types(code, locale_id, tenant_id, name, description)
  VALUES ('test_consent_type', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Consent Type', 'Test Consent Type');

INSERT INTO party.consent_types(code, locale_id, tenant_id, name, description)
  VALUES ('test_consent_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Consent Type', 'Test Consent Type');


INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_purpose', 'test_contact_mechanism_type', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Purpose', 'Test Purpose', 'organization,person');

INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_purpose', 'test_contact_mechanism_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Purpose', 'Test Purpose', 'organization,person');


INSERT INTO party.contact_mechanism_types (code, locale_id, tenant_id, sort_index, name, plural, description)
  VALUES ('test_contact_mechanism_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Contact Mechanism Type', 'Test Contact Mechanism Types', 'Test Contact Mechanism Type');

INSERT INTO party.contact_mechanism_types (code, locale_id, tenant_id, sort_index, name, plural, description)
  VALUES ('test_contact_mechanism_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Contact Mechanism Type', 'Test Contact Mechanism Types', 'Test Contact Mechanism Type');


INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, tenant_id, sort_index, name, description, party_types)
  VALUES ('test_contact_mechanism_type', 'test_contact_mechanism_role', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Personal Mobile Number', 'Personal Mobile Number', 'person');

INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, tenant_id, sort_index, name, description, party_types)
  VALUES ('test_contact_mechanism_type', 'test_contact_mechanism_role', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Personal Mobile Number', 'Personal Mobile Number', 'person');


INSERT INTO party.employment_statuses (code, locale_id, tenant_id, name, description)
  VALUES ('test_employment_status', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Employment Status', 'Test Employment Status');

INSERT INTO party.employment_statuses (code, locale_id, tenant_id, name, description)
  VALUES ('test_employment_status', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Employment Status', 'Test Employment Status');


INSERT INTO party.employment_types (employment_status, code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_employment_status', 'test_employment_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Employment Type', 'Test Employment Type');

INSERT INTO party.employment_types (employment_status, code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_employment_status', 'test_employment_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Employment Type', 'Test Employment Type');


INSERT INTO party.external_reference_types (code, locale_id, tenant_id, sort_index, name, description, party_types)
  VALUES ('test_external_reference_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test External Reference Type', 'Test External Reference Type', 'organization,person');

INSERT INTO party.external_reference_types (code, locale_id, tenant_id, sort_index, name, description, party_types)
  VALUES ('test_external_reference_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test External Reference Type', 'Test External Reference Type', 'organization,person');


INSERT INTO party.fields_of_study (code, locale_id, tenant_id, name, description)
  VALUES ('test_field_of_study', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Field Of Study', 'Test Field Of Study');

INSERT INTO party.fields_of_study (code, locale_id, tenant_id, name, description)
  VALUES ('test_field_of_study', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Field Of Study', 'Test Field Of Study');


INSERT INTO party.genders (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_gender', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Gender', 'Test Gender');

INSERT INTO party.genders (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_gender', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Gender', 'Test Gender');


INSERT INTO party.identity_document_types (code, locale_id, tenant_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('test_identity_document_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Identity Document Type', 'Test Identity Document Type', 'ZA', 'person');

INSERT INTO party.identity_document_types (code, locale_id, tenant_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('test_identity_document_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Identity Document Type', 'Test Identity Document Type', 'ZA', 'person');


INSERT INTO party.lock_type_categories(code, locale_id, tenant_id, name, description)
  VALUES ('test_lock_type_category', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Lock Type Category', 'Test Lock Type Category');

INSERT INTO party.lock_type_categories(code, locale_id, tenant_id, name, description)
  VALUES ('test_lock_type_category', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Lock Type Category', 'Test Lock Type Category');


INSERT INTO party.lock_types(category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_lock_type_category', 'test_lock_type', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Lock', 'Test Lock', 'organization,person');

INSERT INTO party.lock_types(category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_lock_type_category', 'test_lock_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Lock', 'Test Lock', 'organization,person');


INSERT INTO party.marital_statuses (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_marital_status', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Marital Status', 'Test Marital Status');

INSERT INTO party.marital_statuses (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_marital_status', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Marital Status', 'Test Marital Status');


INSERT INTO party.marriage_types (marital_status, code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_marital_status', 'test_marriage_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Marriage Type', 'Test Marriage Type');

INSERT INTO party.marriage_types (marital_status, code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_marital_status', 'test_marriage_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Marriage Type', 'Test Marriage Type');


INSERT INTO party.minor_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_minor_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Minor Type', 'Test Minor Type');

INSERT INTO party.minor_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_minor_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Minor Type', 'Test Minor Type');


INSERT INTO party.next_of_kin_types (code, locale_id, tenant_id, name, description)
  VALUES ('test_next_of_kin_type', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Next Of Kin Type', 'Test Next Of Kin Type');

INSERT INTO party.next_of_kin_types (code, locale_id, tenant_id, name, description)
  VALUES ('test_next_of_kin_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Next Of Kin Type', 'Test Next Of Kin Type');


INSERT INTO party.occupations (code, locale_id, tenant_id, name, description)
  VALUES ('test_occupation', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Occupation', 'Test Occupation');

INSERT INTO party.occupations (code, locale_id, tenant_id, name, description)
  VALUES ('test_occupation', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Occupation', 'Test Occupation');


INSERT INTO party.physical_address_purposes (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_physical_address_purpose', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Physical Address Purpose', 'Test Physical Address Purpose', 'organization,person');

INSERT INTO party.physical_address_purposes (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_physical_address_purpose', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Physical Address Purpose', 'Test Physical Address Purpose', 'organization,person');


INSERT INTO party.physical_address_roles (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_physical_address_role', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Physical Address Role', 'Test Physical Address Role', 'organization');

INSERT INTO party.physical_address_roles (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_physical_address_role', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Physical Address Role', 'Test Physical Address Role', 'organization');


INSERT INTO party.preference_type_categories (code, locale_id, tenant_id, name, description)
  VALUES ('test_preference_type_category', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Preference Type Category', 'Test Preference Type Category');

INSERT INTO party.preference_type_categories (code, locale_id, tenant_id, name, description)
  VALUES ('test_preference_type_category', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Preference Type Category', 'Test Preference Type Category');


INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_contact_mechanism_type', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Contact Mechanism Type', 'Test Contact Mechanism Type', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_country', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Country', 'Test Country', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_language', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Language', 'Test Language', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_preference', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Preference', 'Test Preference', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_size', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Size', 'Test Size', 'organization,person');

INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_contact_mechanism_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Contact Mechanism Type', 'Test Contact Mechanism Type', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_country', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Country', 'Test Country', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_language', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Language', 'Test Language', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_preference', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Preference', 'Test Preference', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_preference_type_category','test_size', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Size', 'Test Size', 'organization,person');


INSERT INTO party.qualification_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_qualification_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Qualification Type', 'Test Qualification Type');

INSERT INTO party.qualification_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_qualification_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Qualification Type', 'Test Qualification Type');


INSERT INTO party.races (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_race', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Race', 'Test Race');

INSERT INTO party.races (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_race', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Race', 'Test Race');


INSERT INTO party.residence_permit_types (code, locale_id, tenant_id, sort_index, name, description, country_of_issue)
  VALUES ('test_residence_permit_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Residence Permit Type', 'Test Residence Permit Type', 'ZA');

INSERT INTO party.residence_permit_types (code, locale_id, tenant_id, sort_index, name, description, country_of_issue)
  VALUES ('test_residence_permit_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Residence Permit Type', 'Test Residence Permit Type', 'ZA');


INSERT INTO party.residency_statuses (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_residency_status', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Residency Status', 'Test Residency Status');

INSERT INTO party.residency_statuses (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_residency_status', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Residency Status', 'Test Residency Status');


INSERT INTO party.residential_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_residential_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Residential Type', 'Test Residential Type');

INSERT INTO party.residential_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_residential_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Residential Type', 'Test Residential Type');


INSERT INTO party.role_purposes (code, locale_id, tenant_id, name, description)
  VALUES ('test_role_purpose', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Role Purpose', 'Test Role Purpose');

INSERT INTO party.role_purposes (code, locale_id, tenant_id, name, description)
  VALUES ('test_role_purpose', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Role Purpose', 'Test Role Purpose');


INSERT INTO party.role_types (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_organization_role', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Organization Role', 'Test Organization Role', 'organization');
INSERT INTO party.role_types (code, locale_id, tenant_id,  name, description, party_types)
  VALUES ('test_person_role', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Person Role', 'Test Person Role', 'person');
INSERT INTO party.role_types (code, locale_id, tenant_id,  name, description, party_types)
  VALUES ('test_first_person_role', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test First Person Role', 'Test First Person Role', 'person');
INSERT INTO party.role_types (code, locale_id, tenant_id,  name, description, party_types)
  VALUES ('test_second_person_role', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Second Person Role', 'Test Second Person Role', 'person');

INSERT INTO party.role_types (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_organization_role', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Organization Role', 'Test Organization Role', 'organization');
INSERT INTO party.role_types (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_person_role', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Person Role', 'Test Person Role', 'person');
INSERT INTO party.role_types (code, locale_id, tenant_id,  name, description, party_types)
  VALUES ('test_first_person_role', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test First Person Role', 'Test First Person Role', 'person');
INSERT INTO party.role_types (code, locale_id, tenant_id,  name, description, party_types)
  VALUES ('test_second_person_role', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Second Person Role', 'Test Second Person Role', 'person');


INSERT INTO party.relationship_types(code, locale_id, tenant_id, name, description, first_party_role, second_party_role)
  VALUES ('test_relationship_type', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Relationship Type', 'Test Relationship Type', 'test_first_person_role', 'test_second_person_role');

INSERT INTO party.relationship_types(code, locale_id, tenant_id, name, description, first_party_role, second_party_role)
  VALUES ('test_relationship_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Relationship Type', 'Test Relationship Type', 'test_first_person_role', 'test_second_person_role');


INSERT INTO party.relationship_property_types (relationship_type, code, locale_id, tenant_id, sort_index, name, description, value_type)
  VALUES ('test_relationship_type', 'test_relationship_property', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Relationship Property', 'Test Relationship Property', 'string');

INSERT INTO party.relationship_property_types (relationship_type, code, locale_id, tenant_id, sort_index, name, description, value_type)
  VALUES ('test_relationship_type', 'test_relationship_property', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Relationship Property', 'Test Relationship Property', 'string');


INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_person_role', 'consent', 'test_consent_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'contact_mechanisms', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_person_role', 'contact_mechanism', 'email_address', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_person_role', 'contact_mechanism', 'fax_number', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_person_role', 'contact_mechanism', 'mobile_number', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_person_role', 'contact_mechanism', 'phone_number', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'countries_of_citizenship', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'countries_of_tax_residence', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'country_of_birth', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'country_of_residence', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'date_of_birth', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'date_of_death', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'educations', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'employment_status', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'employment_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'employments', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_person_role', 'external_reference', 'test_external_reference_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'gender', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'given_name', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'highest_qualification_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_person_role', 'identity_document', 'test_identity_document_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'identity_documents', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'initials', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'language', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'language_proficiencies', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'marital_status', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'marital_status_date', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'marriage_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'measurement_system', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'occupation', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'physical_addresses', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_person_role', 'physical_address', 'residential', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'preferred_name', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'race', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'residence_permits', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'residency_status', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'residential_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'segment_allocations', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'sources_of_funds', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'sources_of_wealth', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'surname', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'tax_numbers', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'time_zone', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'title', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'test_attribute', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_person_role', 'test_attribute', 'min_size', '5');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_person_role', 'test_attribute', 'max_size', '20');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_person_role', 'test_attribute', 'pattern', '^[a-zA-Z0-9]*$');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'test_contact_mechanism_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_person_role', 'test_contact_mechanism_type', 'reference', 'contact_mechanism_type');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'test_country', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_person_role', 'test_country', 'reference', 'country');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'test_language', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_person_role', 'test_language', 'reference', 'language');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'test_size', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_person_role', 'test_size', 'size', '10');

INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'contact_mechanisms', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_organization_role', 'contact_mechanism', 'email_address', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_organization_role', 'contact_mechanism', 'fax_number', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_organization_role', 'contact_mechanism', 'mobile_number', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_organization_role', 'contact_mechanism', 'phone_number', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'countries_of_tax_residence', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_organization_role', 'external_reference', 'test_external_reference_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'identity_documents', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'physical_addresses', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_organization_role', 'physical_address', 'main', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'segment_allocations', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'tax_numbers', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'test_attribute', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_organization_role', 'test_attribute', 'size', '10');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type, value)
  VALUES ('test_organization_role', 'test_attribute', 'pattern', '^[0-9]*$');


INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type)
  VALUES ('test_person_role', 'test_preference', 'required');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_person_role', 'test_preference', 'min_size', '5');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_person_role', 'test_preference', 'max_size', '20');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_person_role', 'test_preference', 'pattern', '^[a-zA-Z0-9]*$');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type)
  VALUES ('test_person_role', 'test_contact_mechanism_type', 'required');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_person_role', 'test_contact_mechanism_type', 'reference', 'contact_mechanism_type');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type)
  VALUES ('test_person_role', 'test_country', 'required');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_person_role', 'test_country', 'reference', 'country');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type)
  VALUES ('test_person_role', 'test_language', 'required');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_person_role', 'test_language', 'reference', 'language');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type)
  VALUES ('test_person_role', 'test_size', 'required');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_person_role', 'test_size', 'size', '10');

INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type)
  VALUES ('test_organization_role', 'test_preference', 'required');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_organization_role', 'test_preference', 'min_size', '5');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_organization_role', 'test_preference', 'max_size', '20');
INSERT INTO party.role_type_preference_type_constraints(role_type, preference_type, type, value)
  VALUES ('test_organization_role', 'test_preference', 'pattern', '^[0-9]*$');


INSERT INTO party.segments (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_organization_segment', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Organization Segment', 'Test Organization Segment', 'organization');
INSERT INTO party.segments (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_person_segment', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Person Segment', 'Test Person Segment', 'person');

INSERT INTO party.segments (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_organization_segment', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Organization Segment', 'Test Organization Segment', 'organization');
INSERT INTO party.segments (code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_person_segment', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Person Segment', 'Test Person Segment', 'person');


INSERT INTO party.source_of_funds_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_source_of_funds_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Source Of Funds Type', 'Test Source Of Funds Type');

INSERT INTO party.source_of_funds_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_source_of_funds_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Source Of Funds Type', 'Test Source Of Funds Type');


INSERT INTO party.source_of_wealth_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_source_of_wealth_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Source Of Wealth Type', 'Test Source Of Wealth Type');

INSERT INTO party.source_of_wealth_types (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_source_of_wealth_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Source Of Wealth Type', 'Test Source Of Wealth Type');


INSERT INTO party.status_type_categories(code, locale_id, tenant_id, name, description)
  VALUES ('test_status_type_category', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Status Type Category', 'Test Status Type Category');

INSERT INTO party.status_type_categories(code, locale_id, tenant_id, name, description)
  VALUES ('test_status_type_category', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Status Type Category', 'Test Status Type Category');


INSERT INTO party.status_types(category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_status_type_category', 'test_status_type', 'en-US', '00000000-0000-0000-0000-000000000000', 'Test Status Type', 'Test Status Type', 'organization,person');

INSERT INTO party.status_types(category, code, locale_id, tenant_id, name, description, party_types)
  VALUES ('test_status_type_category', 'test_status_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 'Test Status Type', 'Test Status Type', 'organization,person');


INSERT INTO party.tax_number_types (code, locale_id, tenant_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('test_tax_number_type', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Tax Number Type', 'Test Tax Number Type', 'ZA', 'organization,person');

INSERT INTO party.tax_number_types (code, locale_id, tenant_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('test_tax_number_type', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Tax Number Type', 'Test Tax Number Type', 'ZA', 'organization,person');


INSERT INTO party.times_to_contact (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_time_to_contact', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Time To Contact', 'Test Time To Contact');

INSERT INTO party.times_to_contact (code, locale_id, tenant_id, sort_index, name, description)
  VALUES ('test_time_to_contact', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Time To Contact', 'Test Time To Contact');


INSERT INTO party.titles (code, locale_id, tenant_id, sort_index, name, abbreviation, description)
  VALUES ('test_title', 'en-US', '00000000-0000-0000-0000-000000000000', 666, 'Test Title', 'Tst.', 'Test Title');

INSERT INTO party.titles (code, locale_id, tenant_id, sort_index, name, abbreviation, description)
  VALUES ('test_title', 'en-ZA', '00000000-0000-0000-0000-000000000000', 666, 'Test Title', 'Tst.', 'Test Title');
