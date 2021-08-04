

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO party.attribute_type_categories (code, locale_id, name, description)
  VALUES ('anthropometric_measurements', 'en-US', 'Anthropometric Measurements', 'Anthropometric Measurements');

INSERT INTO party.attribute_type_categories (code, locale_id, name, description)
  VALUES ('anthropometric_measurements', 'en-ZA', 'Anthropometric Measurements', 'Anthropometric Measurements');


INSERT INTO party.attribute_types (category, code, locale_id, name, description, party_types, value_type)
  VALUES ('anthropometric_measurements','bmi', 'en-US', 'Body Mass Index', 'Body Mass Index', 'person', 'decimal');
INSERT INTO party.attribute_types (category, code, locale_id, name, description, party_types, unit_type, value_type)
  VALUES ('anthropometric_measurements','height', 'en-US', 'Height', 'Height', 'person', 'length', 'decimal');
INSERT INTO party.attribute_types (category, code, locale_id, name, description, party_types, unit_type, value_type)
  VALUES ('anthropometric_measurements','weight', 'en-US', 'Weight', 'Weight', 'person', 'mass', 'decimal');

INSERT INTO party.attribute_types (category, code, locale_id, name, description, party_types, value_type)
  VALUES ('anthropometric_measurements','bmi', 'en-ZA', 'Body Mass Index', 'Body Mass Index', 'person', 'decimal');
INSERT INTO party.attribute_types (category, code, locale_id, name, description, party_types, unit_type, value_type)
  VALUES ('anthropometric_measurements','height', 'en-ZA', 'Height', 'Height', 'person', 'length', 'decimal');
INSERT INTO party.attribute_types (category, code, locale_id, name, description, party_types, unit_type, value_type)
  VALUES ('anthropometric_measurements','weight', 'en-ZA', 'Weight', 'Weight', 'person', 'mass', 'decimal');


INSERT INTO party.consent_types(code, locale_id, name, description)
  VALUES ('marketing', 'en-US', 'Marketing Consent', 'Marketing Consent');

INSERT INTO party.consent_types(code, locale_id, name, description)
  VALUES ('marketing', 'en-ZA', 'Marketing Consent', 'Marketing Consent');


INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, name, description, party_types)
  VALUES ('general', 'email_address', 'en-US', 'General Correspondence', 'General Correspondence', 'organization,person');
INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, name, description, party_types)
  VALUES ('marketing', 'email_address', 'en-US', 'Marketing Correspondence', 'Marketing Correspondence', 'organization,person');
INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, name, description, party_types)
  VALUES ('security', 'email_address,mobile_number', 'en-US', 'Security Alerts', 'Security Alerts', 'organization,person');

INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, name, description, party_types)
  VALUES ('general', 'email_address', 'en-ZA', 'General Correspondence', 'General Correspondence', 'organization,person');
INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, name, description, party_types)
  VALUES ('marketing', 'email_address', 'en-ZA', 'Marketing Correspondence', 'Marketing Correspondence', 'organization,person');
INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, name, description, party_types)
  VALUES ('security', 'email_address,mobile_number', 'en-ZA', 'Security Alerts', 'Security Alerts', 'organization,person');


INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('mobile_number', 'en-US', 1, 'Mobile Number', 'Mobile Numbers', 'Mobile Number');
INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('phone_number', 'en-US', 2, 'Phone Number', 'Phone Numbers', 'Phone Number');
INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('fax_number', 'en-US', 3, 'Fax Number', 'Fax Numbers', 'Fax Number');
INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('email_address', 'en-US', 4, 'E-mail Address', 'E-mail Addresses', 'E-mail Address');
INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('social_media', 'en-US', 5, 'Social Media', 'Social Media', 'Social Media');

INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('mobile_number', 'en-ZA', 1, 'Mobile Number', 'Mobile Numbers', 'Mobile Number');
INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('phone_number', 'en-ZA', 2, 'Phone Number', 'Phone Numbers', 'Phone Number');
INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('fax_number', 'en-ZA', 3, 'Fax Number', 'Fax Numbers', 'Fax Number');
INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('email_address', 'en-ZA', 4, 'E-mail Address', 'E-mail Addresses', 'E-mail Address');
INSERT INTO party.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('social_media', 'en-ZA', 5, 'Social Media', 'Social Media', 'Social Media');


INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'personal_mobile_number', 'en-US', 101, 'Personal Mobile Number', 'Personal Mobile Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'work_mobile_number', 'en-US', 102, 'Work Mobile Number', 'Work Mobile Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'other_mobile_number', 'en-US', 103, 'Other Mobile Number', 'Other Mobile Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'alternate_mobile_number', 'en-US', 104, 'Alternate Mobile Number', 'Alternate Mobile Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'main_mobile_number', 'en-US', 110, 'Main Mobile Number', 'Main Mobile Number', 'organization');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'home_phone_number', 'en-US', 201, 'Home Phone Number', 'Home Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'work_phone_number', 'en-US', 202, 'Work Phone Number', 'Work Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'school_phone_number', 'en-US', 203, 'School Phone Number', 'School Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'pager_phone_number', 'en-US', 204, 'Pager Phone Number', 'Pager Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'other_phone_number', 'en-US', 205, 'Other Phone Number', 'Other Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'alternate_phone_number', 'en-US', 206, 'Alternate Phone Number', 'Alternate Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'main_phone_number', 'en-US', 210, 'Main Phone Number', 'Main Phone Number', 'organization');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'home_fax_number', 'en-US', 301, 'Home Fax Number', 'Home Fax Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'work_fax_number', 'en-US', 302, 'Work Fax Number', 'Work Fax Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'other_fax_number', 'en-US', 303, 'Other Fax Number', 'Other Fax Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'alternate_fax_number', 'en-US', 304, 'Alternate Fax Number', 'Alternate Fax Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'main_fax_number', 'en-US', 310, 'Main Fax Number', 'Main Fax Number', 'organization');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'personal_email_address', 'en-US', 401, 'Personal E-mail Address', 'Personal E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'work_email_address', 'en-US', 402, 'Work E-mail Address', 'Work E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'school_email_address', 'en-US', 403, 'School E-mail Address', 'School E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'other_email_address', 'en-US', 404, 'Other E-mail Address', 'Other E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'alternate_email_address', 'en-US', 405, 'Alternate E-mail Address', 'Alternate E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'main_email_address', 'en-US', 410, 'Main E-mail Address', 'Main E-mail Address', 'organization');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('social_media', 'whatsapp_user_id', 'en-US', 501, 'WhatsApp User ID', 'WhatsApp User ID', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('social_media', 'twitter_id', 'en-US', 502, 'Twitter ID', 'Twitter ID', 'person');

INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'personal_mobile_number', 'en-ZA', 101, 'Personal Mobile Number', 'Personal Mobile Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'work_mobile_number', 'en-ZA', 102, 'Work Mobile Number', 'Work Mobile Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'other_mobile_number', 'en-ZA', 103, 'Other Mobile Number', 'Other Mobile Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'alternate_mobile_number', 'en-ZA', 104, 'Alternate Mobile Number', 'Alternate Mobile Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'main_mobile_number', 'en-ZA', 110, 'Main Mobile Number', 'Main Mobile Number', 'organization');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'home_phone_number', 'en-ZA', 201, 'Home Phone Number', 'Home Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'work_phone_number', 'en-ZA', 202, 'Work Phone Number', 'Work Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'school_phone_number', 'en-ZA', 203, 'School Phone Number', 'School Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'pager_phone_number', 'en-ZA', 204, 'Pager Phone Number', 'Pager Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'other_phone_number', 'en-ZA', 205, 'Other Phone Number', 'Other Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'alternate_phone_number', 'en-ZA', 206, 'Alternate Phone Number', 'Alternate Phone Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'main_phone_number', 'en-ZA', 210, 'Main Phone Number', 'Main Phone Number', 'organization');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'home_fax_number', 'en-ZA', 301, 'Home Fax Number', 'Home Fax Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'work_fax_number', 'en-ZA', 302, 'Work Fax Number', 'Work Fax Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'other_fax_number', 'en-ZA', 303, 'Other Fax Number', 'Other Fax Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'alternate_fax_number', 'en-ZA', 304, 'Alternate Fax Number', 'Alternate Fax Number', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'main_fax_number', 'en-ZA', 310, 'Main Fax Number', 'Main Fax Number', 'organization');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'personal_email_address', 'en-ZA', 401, 'Personal E-mail Address', 'Personal E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'work_email_address', 'en-ZA', 402, 'Work E-mail Address', 'Work E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'school_email_address', 'en-ZA', 403, 'School E-mail Address', 'School E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'other_email_address', 'en-ZA', 404, 'Other E-mail Address', 'Other E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'alternate_email_address', 'en-ZA', 405, 'Alternate E-mail Address', 'Alternate E-mail Address', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'main_email_address', 'en-ZA', 410, 'Main E-mail Address', 'Main E-mail Address', 'organization');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('social_media', 'whatsapp_user_id', 'en-ZA', 501, 'WhatsApp User ID', 'WhatsApp User ID', 'person');
INSERT INTO party.contact_mechanism_roles (contact_mechanism_type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('social_media', 'twitter_id', 'en-ZA', 502, 'Twitter ID', 'Twitter ID', 'person');


INSERT INTO party.employment_statuses (code, locale_id, name, description)
  VALUES ('employed', 'en-US', 'Employed', 'Employed');
INSERT INTO party.employment_statuses (code, locale_id, name, description)
  VALUES ('other', 'en-US', 'Other', 'Other');
INSERT INTO party.employment_statuses (code, locale_id, name, description)
  VALUES ('unknown', 'en-US', 'Unknown', 'Unknown');

INSERT INTO party.employment_statuses (code, locale_id, name, description)
  VALUES ('employed', 'en-ZA', 'Employed', 'Employed');
INSERT INTO party.employment_statuses (code, locale_id, name, description)
  VALUES ('other', 'en-ZA', 'Other', 'Other');
INSERT INTO party.employment_statuses (code, locale_id, name, description)
  VALUES ('unknown', 'en-ZA', 'Unknown', 'Unknown');


INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'full_time', 'en-US', 1, 'Full-time', 'Full-time');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'part_time', 'en-US', 2, 'Part-time', 'Part-time');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'contractor', 'en-US', 3, 'Contractor', 'Contractor');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'self_employed', 'en-US', 4, 'Self-employed', 'Self-employed');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'retired_pensioner', 'en-US', 5, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'unemployed', 'en-US', 6, 'Unemployed', 'Unemployed');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'home_executive', 'en-US', 7, 'Home Executive', 'Home Executive');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'student', 'en-US', 8, 'Student', 'Student');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'minor', 'en-US', 9, 'Minor', 'Minor');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'not_specified', 'en-US', 10, 'Not Specified', 'Not Specified');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'full_time', 'en-ZA', 1, 'Full-time', 'Full-time');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'part_time', 'en-ZA', 2, 'Part-time', 'Part-time');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'contractor', 'en-ZA', 3, 'Contractor', 'Contractor');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'self_employed', 'en-ZA', 4, 'Self-employed', 'Self-employed');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'retired_pensioner', 'en-ZA', 5, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'unemployed', 'en-ZA', 6, 'Unemployed', 'Unemployed');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'home_executive', 'en-ZA', 7, 'Home Executive', 'Home Executive');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'student', 'en-ZA', 8, 'Student', 'Student');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'minor', 'en-ZA', 9, 'Minor', 'Minor');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'not_specified', 'en-ZA', 10, 'Not Specified', 'Not Specified');
INSERT INTO party.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.external_reference_types (code, locale_id, name, description, party_types)
  VALUES ('legacy_customer_code', 'en-US', 'Legacy Customer Code', 'Legacy Customer Code', 'organization,person');

INSERT INTO party.external_reference_types (code, locale_id, name, description, party_types)
  VALUES ('legacy_customer_code', 'en-ZA', 'Legacy Customer Code', 'Legacy Customer Code', 'organization,person');


INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('accounting', 'en-US', 'Accounting', 'Accounting');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('actuarial_science', 'en-US', 'Actuarial Science', 'Actuarial Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('advertising', 'en-US', 'Advertising', 'Advertising');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('aeronautics_aviation_science', 'en-US', 'Aeronautics and Aviation Science', 'Aeronautics and Aviation Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('african_literature', 'en-US', 'African Literature', 'African Literature');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('agribusiness_management', 'en-US', 'Agribusiness Management', 'Agribusiness Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('agricultural_economics', 'en-US', 'Agricultural Economics', 'Agricultural Economics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('anatomy', 'en-US', 'Anatomy', 'Anatomy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('anthropology', 'en-US', 'Anthropology', 'Anthropology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('applied_development_economics', 'en-US', 'Applied Development Economics', 'Applied Development Economics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('applied_mathematics', 'en-US', 'Applied Mathematics', 'Applied Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('archaeology', 'en-US', 'Archaeology', 'Archaeology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('architecture', 'en-US', 'Architecture', 'Architecture');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('artificial_intelligence_robotics', 'en-US', 'Artificial Intelligence and Robotics', 'Artificial Intelligence and Robotics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('arts_culture_heritage_studies', 'en-US', 'Arts, Culture and Heritage Studies', 'Arts, Culture and Heritage Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('arts_humanities', 'en-US', 'Arts and Humanities', 'Arts and Humanities');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('astronomy', 'en-US', 'Astronomy', 'Astronomy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('audiology', 'en-US', 'Audiology', 'Audiology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('auditing', 'en-US', 'Auditing', 'Auditing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('automotive_trades', 'en-US', 'Automotive Trades', 'Automotive Trades');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('banking', 'en-US', 'Banking', 'Banking');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('banking_international_finance', 'en-US', 'Banking and International Finance', 'Banking and International Finance');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('biochemistry', 'en-US', 'Biochemistry', 'Biochemistry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('biodiversity', 'en-US', 'Biodiversity', 'Biodiversity');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('biology', 'en-US', 'Biology', 'Biology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('business_management_informatics', 'en-US', 'Business Management and Informatics', 'Business Management and Informatics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('brand', 'en-US', 'Brand', 'Brand');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('business_commerce_management_studies', 'en-US', 'Business, Commerce and Management Studies', 'Business, Commerce and Management Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('business_administration', 'en-US', 'Business Administration', 'Business Administration');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('business_management', 'en-US', 'Business Management', 'Business Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('cfa', 'en-US', 'CFA', 'CFA');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('cfp', 'en-US', 'CFP', 'CFP');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('chemical_engineering', 'en-US', 'Chemical Engineering', 'Chemical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('chemistry', 'en-US', 'Chemistry', 'Chemistry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('child_youth_care', 'en-US', 'Child and Youth Care', 'Child and Youth Care');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('cima', 'en-US', 'CIMA', 'CIMA');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('civil_engineering', 'en-US', 'Civil Engineering', 'Civil Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('clinical_psychology', 'en-US', 'Clinical Psychology', 'Clinical Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('commerce', 'en-US', 'Commerce', 'Commerce');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('communication_studies_language', 'en-US', 'Communication Studies and Language', 'Communication Studies and Language');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('compliance_management', 'en-US', 'Compliance Management', 'Compliance Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('computational_applied_mathematics', 'en-US', 'Computational and Applied Mathematics', 'Computational and Applied Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('computer_information_science', 'en-US', 'Computer and Information Science', 'Computer and Information Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('construction_management', 'en-US', 'Construction Management', 'Construction Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('consumer_behaviour', 'en-US', 'Consumer Behaviour', 'Consumer Behaviour');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('cosmetology', 'en-US', 'Cosmetology', 'Cosmetology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('counselling_psychology', 'en-US', 'Counselling Psychology', 'Counselling Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('creative_writing', 'en-US', 'Creative Writing', 'Creative Writing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('criminology', 'en-US', 'Criminology', 'Criminology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('culinary_arts', 'en-US', 'Culinary Arts', 'Culinary Arts');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('data_science', 'en-US', 'Data Science', 'Data Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('demography', 'en-US', 'Demography', 'Demography');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('dentistry', 'en-US', 'Dentistry', 'Dentistry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('development_studies', 'en-US', 'Development Studies', 'Development Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('dietetics_nutrition', 'en-US', 'Dietetics and Nutrition', 'Dietetics and Nutrition');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('digital_arts', 'en-US', 'Digital Arts', 'Digital Arts');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('diplomacy', 'en-US', 'Diplomacy', 'Diplomacy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('diplomacy_international_studies', 'en-US', 'Diplomacy and International Studies', 'Diplomacy and International Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('diversity_studies', 'en-US', 'Diversity Studies', 'Diversity Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('drama_film', 'en-US', 'Drama and Film', 'Drama and Film');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('ecology', 'en-US', 'Ecology', 'Ecology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('econometrics', 'en-US', 'Econometrics', 'Econometrics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('economic_management_science', 'en-US', 'Economic and Management Science', 'Economic and Management Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('economics', 'en-US', 'Economics', 'Economics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('education', 'en-US', 'Education', 'Education');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('education_training_development', 'en-US', 'Education, Training and Development', 'Education, Training and Development');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('educational_psychology', 'en-US', 'Educational Psychology', 'Educational Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('electrical_engineering', 'en-US', 'Electrical Engineering', 'Electrical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('entrepreneurship', 'en-US', 'Entrepreneurship', 'Entrepreneurship');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('environmental_science', 'en-US', 'Environmental Science', 'Environmental Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('epidemiology', 'en-US', 'Epidemiology', 'Epidemiology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('escience', 'en-US', 'e-Science', 'e-Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('estate_trust_management', 'en-US', 'Estate and Trust Management', 'Estate and Trust Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('exercise_science', 'en-US', 'Exercise Science', 'Exercise Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('fashion_design', 'en-US', 'Fashion Design', 'Fashion Design');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('finance_investment_management', 'en-US', 'Finance and Investment Management', 'Finance and Investment Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('financial_economics', 'en-US', 'Financial Economics', 'Financial Economics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('financial_planning', 'en-US', 'Financial Planning', 'Financial Planning');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('financial_sciences', 'en-US', 'Financial Sciences', 'Financial Sciences');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('fine_arts', 'en-US', 'Fine Arts', 'Fine Arts');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('gender_sexuality_studies', 'en-US', 'Gender and Sexuality Studies', 'Gender and Sexuality Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('general', 'en-US', 'General', 'General');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('genetics', 'en-US', 'Genetics', 'Genetics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geochemistry', 'en-US', 'Geochemistry', 'Geochemistry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geography', 'en-US', 'Geography', 'Geography');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geology', 'en-US', 'Geology', 'Geology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geophysics', 'en-US', 'Geophysics', 'Geophysics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geosciences', 'en-US', 'Geosciences', 'Geosciences');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('global_change_studies', 'en-US', 'Global Change Studies', 'Global Change Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('graphic_design', 'en-US', 'Graphic Design', 'Graphic Design');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('haematology', 'en-US', 'Haematology', 'Haematology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('health_sciences_social_services', 'en-US', 'Health Sciences and Social Services', 'Health Sciences and Social Services');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('history', 'en-US', 'History', 'History');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('human_social_studies', 'en-US', 'Human and Social Studies', 'Human and Social Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('humanities', 'en-US', 'Humanities', 'Humanities');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('human_resource_development', 'en-US', 'Human Resource Development', 'Human Resource Development');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('human_resources_management', 'en-US', 'Human Resources Management', 'Human Resources Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('human_rights_advocacy', 'en-US', 'Human Rights Advocacy', 'Human Rights Advocacy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('hydrogeology', 'en-US', 'Hydrogeology', 'Hydrogeology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('imas', 'en-US', 'IMAS', 'IMAS');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('industrial_organisational_psychology', 'en-US', 'Industrial and Organisational Psychology', 'Industrial and Organisational Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('information_systems', 'en-US', 'Information Systems', 'Information Systems');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('information_technology', 'en-US', 'Information Technology', 'Information Technology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('insurance_risk_management', 'en-US', 'Insurance and Risk Management', 'Insurance and Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('interior_design', 'en-US', 'Interior Design', 'Interior Design');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('internal_auditing', 'en-US', 'Internal Auditing', 'Internal Auditing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('international_finance', 'en-US', 'International Finance', 'International Finance');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('international_law', 'en-US', 'International Law', 'International Law');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('international_relations', 'en-US', 'International Relations', 'International Relations');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('international_studies', 'en-US', 'International Studies', 'International Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('investment_management', 'en-US', 'Investment Management', 'Investment Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('journalism', 'en-US', 'Journalism', 'Journalism');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('labour_law', 'en-US',  'Labour Law', 'Labour Law');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('law', 'en-US', 'Law', 'Law');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('law_military_science_security', 'en-US', 'Law, Military Science and Security', 'Law, Military Science and Security');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('library_information_science', 'en-US', 'Library and Information Science', 'Library and Information Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('linguistics', 'en-US', 'Linguistics', 'Linguistics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('Linguistics_languages', 'en-US', 'Linguistics and Languages', 'Linguistics and Languages');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('logistics', 'en-US', 'Logistics', 'Logistics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('management', 'en-US', 'Management', 'Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('management_accounting', 'en-US', 'Management Accounting', 'Management Accounting');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('manufacturing_engineering_technology', 'en-US',  'Manufacturing, Engineering and Technology', 'Manufacturing, Engineering and Technology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('marketing', 'en-US', 'Marketing', 'Marketing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('mathematics', 'en-US', 'Mathematics', 'Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('mechanical_engineering', 'en-US', 'Mechanical Engineering', 'Mechanical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('media_studies', 'en-US', 'Media Studies', 'Media Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('medical_sciences', 'en-US', 'Medical Sciences', 'Medical Sciences');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('metallurgical_engineering', 'en-US', 'Metallurgical Engineering', 'Metallurgical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('meteorology', 'en-US', 'Meteorology', 'Meteorology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('microelectronic_engineering', 'en-US', 'Microelectronic Engineering', 'Microelectronic Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('migration_displacement', 'en-US', 'Migration and Displacement', 'Migration and Displacement');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('mining', 'en-US', 'Mining', 'Mining');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('money_laundering_control', 'en-US', 'Money Laundering Control', 'Money Laundering Control');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('multimedia', 'en-US', 'Multimedia', 'Multimedia');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('music', 'en-US', 'Music', 'Music');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('neuropsychology', 'en-US', 'Neuropsychology', 'Neuropsychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('nursing', 'en-US', 'Nursing', 'Nursing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('occupational_therapy', 'en-US', 'Occupational Therapy', 'Occupational Therapy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('oceanography', 'en-US', 'Oceanography', 'Oceanography');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('office_administration', 'en-US', 'Office Administration', 'Office Administration');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('operations_management', 'en-US', 'Operations Management', 'Operations Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('optometry', 'en-US', 'Optometry', 'Optometry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('pharmacology', 'en-US', 'Pharmacology', 'Pharmacology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('philosophy', 'en-US', 'Philosophy', 'Philosophy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('photography', 'en-US', 'Photography', 'Photography');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('physical_mathematical_computer_life_sciences', 'en-US', 'Physical, Mathematical, Computer and Life Sciences', 'Physical, Mathematical, Computer and Life Sciences');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('physical_planning_construction', 'en-US', 'Physical Planning and Construction', 'Physical Planning and Construction');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('physics', 'en-US', 'Physics', 'Physics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('physiotherapy', 'en-US', 'Physiotherapy', 'Physiotherapy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('political_studies', 'en-US', 'Political Studies', 'Political Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('politics_journalism', 'en-US', 'Politics and Journalism', 'Politics and Journalism');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('programme_management', 'en-US', 'Programme Management', 'Programme Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('project_management', 'en-US', 'Project Management', 'Project Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('psychology', 'en-US', 'Psychology', 'Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('public_administration', 'en-US', 'Public Administration', 'Public Administration');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('public_health', 'en-US', 'Public Health', 'Public Health');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('public_management_governance', 'en-US', 'Public Management and Governance', 'Public Management and Governance');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('public_relations', 'en-US', 'Public Relations', 'Public Relations');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('publishing', 'en-US', 'Publishing', 'Publishing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('quantity_surveying', 'en-US', 'Quantity Surveying', 'Quantity Surveying');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('radiation_protection', 'en-US', 'Radiation Protection', 'Radiation Protection');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('religion_theology', 'en-US', 'Religion and Theology', 'Religion and Theology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('research_psychology', 'en-US', 'Research Psychology', 'Research Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('resource_conservation_biology', 'en-US', 'Resource Conservation Biology', 'Resource Conservation Biology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('retailing', 'en-US', 'Retailing', 'Retailing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('retail_management', 'en-US', 'Retail Management', 'Retail Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('risk', 'en-US', 'Risk', 'Risk');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('risk_management', 'en-US', 'Risk Management', 'Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('risk_management_financial_risk_management', 'en-US', 'Risk Management and Financial Risk Management', 'Risk Management and Financial Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('robotics', 'en-US', 'Robotics', 'Robotics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('rural_development', 'en-US', 'Rural Development', 'Rural Development');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('security_management', 'en-US', 'Security Management', 'Security Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('services', 'en-US', 'Services', 'Services');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('social_psychology', 'en-US', 'Social Psychology', 'Social Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('social_work', 'en-US', 'Social Work', 'Social Work');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('sociology', 'en-US', 'Sociology', 'Sociology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('speech_language_studies', 'en-US', 'Speech-Language Studies', 'Speech-Language Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('sports_management', 'en-US', 'Sports Management', 'Sports Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('statistics', 'en-US', 'Statistics', 'Statistics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('strategic_management', 'en-US', 'Strategic Management', 'Strategic Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('supply_chain_management', 'en-US', 'Supply Chain Management', 'Supply Chain Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('systems_development', 'en-US', 'Systems Development', 'Systems Development');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('taxation', 'en-US', 'Taxation', 'Taxation');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('tourism_hospitality', 'en-US', 'Tourism and Hospitality', 'Tourism and Hospitality');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('translation_interpreting', 'en-US', 'Translation and Interpreting', 'Translation and Interpreting');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('transportation_supply_chain_management', 'en-US', 'Transportation and Supply Chain Management', 'Transportation and Supply Chain Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('urban_planning', 'en-US', 'Urban Planning', 'Urban Planning');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('visual_studies', 'en-US', 'Visual Studies', 'Visual Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('wealth_management', 'en-US', 'Wealth Management', 'Wealth Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('women_gender_studies', 'en-US', 'Women and Gender Studies', 'Women and Gender Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('zoology', 'en-US', 'Zoology', 'Zoology');

INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('accounting', 'en-ZA', 'Accounting', 'Accounting');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('actuarial_science', 'en-ZA', 'Actuarial Science', 'Actuarial Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('advertising', 'en-ZA', 'Advertising', 'Advertising');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('aeronautics_aviation_science', 'en-ZA', 'Aeronautics and Aviation Science', 'Aeronautics and Aviation Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('african_literature', 'en-ZA', 'African Literature', 'African Literature');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('agribusiness_management', 'en-ZA', 'Agribusiness Management', 'Agribusiness Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('agricultural_economics', 'en-ZA', 'Agricultural Economics', 'Agricultural Economics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('anatomy', 'en-ZA', 'Anatomy', 'Anatomy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('anthropology', 'en-ZA', 'Anthropology', 'Anthropology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('applied_development_economics', 'en-ZA', 'Applied Development Economics', 'Applied Development Economics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('applied_mathematics', 'en-ZA', 'Applied Mathematics', 'Applied Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('archaeology', 'en-ZA', 'Archaeology', 'Archaeology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('architecture', 'en-ZA', 'Architecture', 'Architecture');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('artificial_intelligence_robotics', 'en-ZA', 'Artificial Intelligence and Robotics', 'Artificial Intelligence and Robotics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('arts_culture_heritage_studies', 'en-ZA', 'Arts, Culture and Heritage Studies', 'Arts, Culture and Heritage Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('arts_humanities', 'en-ZA', 'Arts and Humanities', 'Arts and Humanities');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('astronomy', 'en-ZA', 'Astronomy', 'Astronomy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('audiology', 'en-ZA', 'Audiology', 'Audiology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('auditing', 'en-ZA', 'Auditing', 'Auditing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('automotive_trades', 'en-ZA', 'Automotive Trades', 'Automotive Trades');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('banking', 'en-ZA', 'Banking', 'Banking');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('banking_international_finance', 'en-ZA', 'Banking and International Finance', 'Banking and International Finance');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('biochemistry', 'en-ZA', 'Biochemistry', 'Biochemistry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('biodiversity', 'en-ZA', 'Biodiversity', 'Biodiversity');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('biology', 'en-ZA', 'Biology', 'Biology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('business_management_informatics', 'en-ZA', 'Business Management and Informatics', 'Business Management and Informatics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('brand', 'en-ZA', 'Brand', 'Brand');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('business_commerce_management_studies', 'en-ZA', 'Business, Commerce and Management Studies', 'Business, Commerce and Management Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('business_administration', 'en-ZA', 'Business Administration', 'Business Administration');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('business_management', 'en-ZA', 'Business Management', 'Business Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('cfa', 'en-ZA', 'CFA', 'CFA');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('cfp', 'en-ZA', 'CFP', 'CFP');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('chemical_engineering', 'en-ZA', 'Chemical Engineering', 'Chemical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('chemistry', 'en-ZA', 'Chemistry', 'Chemistry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('child_youth_care', 'en-ZA', 'Child and Youth Care', 'Child and Youth Care');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('cima', 'en-ZA', 'CIMA', 'CIMA');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('civil_engineering', 'en-ZA', 'Civil Engineering', 'Civil Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('clinical_psychology', 'en-ZA', 'Clinical Psychology', 'Clinical Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('commerce', 'en-ZA', 'Commerce', 'Commerce');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('communication_studies_language', 'en-ZA', 'Communication Studies and Language', 'Communication Studies and Language');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('compliance_management', 'en-ZA', 'Compliance Management', 'Compliance Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('computational_applied_mathematics', 'en-ZA', 'Computational and Applied Mathematics', 'Computational and Applied Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('computer_information_science', 'en-ZA', 'Computer and Information Science', 'Computer and Information Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('construction_management', 'en-ZA', 'Construction Management', 'Construction Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('consumer_behaviour', 'en-ZA', 'Consumer Behaviour', 'Consumer Behaviour');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('cosmetology', 'en-ZA', 'Cosmetology', 'Cosmetology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('counselling_psychology', 'en-ZA', 'Counselling Psychology', 'Counselling Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('creative_writing', 'en-ZA', 'Creative Writing', 'Creative Writing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('criminology', 'en-ZA', 'Criminology', 'Criminology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('culinary_arts', 'en-ZA', 'Culinary Arts', 'Culinary Arts');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('data_science', 'en-ZA', 'Data Science', 'Data Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('demography', 'en-ZA', 'Demography', 'Demography');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('dentistry', 'en-ZA', 'Dentistry', 'Dentistry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('development_studies', 'en-ZA', 'Development Studies', 'Development Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('dietetics_nutrition', 'en-ZA', 'Dietetics and Nutrition', 'Dietetics and Nutrition');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('digital_arts', 'en-ZA', 'Digital Arts', 'Digital Arts');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('diplomacy', 'en-ZA', 'Diplomacy', 'Diplomacy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('diplomacy_international_studies', 'en-ZA', 'Diplomacy and International Studies', 'Diplomacy and International Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('diversity_studies', 'en-ZA', 'Diversity Studies', 'Diversity Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('drama_film', 'en-ZA', 'Drama and Film', 'Drama and Film');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('ecology', 'en-ZA', 'Ecology', 'Ecology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('econometrics', 'en-ZA', 'Econometrics', 'Econometrics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('economic_management_science', 'en-ZA', 'Economic and Management Science', 'Economic and Management Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('economics', 'en-ZA', 'Economics', 'Economics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('education', 'en-ZA', 'Education', 'Education');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('education_training_development', 'en-ZA', 'Education, Training and Development', 'Education, Training and Development');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('educational_psychology', 'en-ZA', 'Educational Psychology', 'Educational Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('electrical_engineering', 'en-ZA', 'Electrical Engineering', 'Electrical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('entrepreneurship', 'en-ZA', 'Entrepreneurship', 'Entrepreneurship');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('environmental_science', 'en-ZA', 'Environmental Science', 'Environmental Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('epidemiology', 'en-ZA', 'Epidemiology', 'Epidemiology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('escience', 'en-ZA', 'e-Science', 'e-Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('estate_trust_management', 'en-ZA', 'Estate and Trust Management', 'Estate and Trust Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('exercise_science', 'en-ZA', 'Exercise Science', 'Exercise Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('fashion_design', 'en-ZA', 'Fashion Design', 'Fashion Design');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('finance_investment_management', 'en-ZA', 'Finance and Investment Management', 'Finance and Investment Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('financial_economics', 'en-ZA', 'Financial Economics', 'Financial Economics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('financial_planning', 'en-ZA', 'Financial Planning', 'Financial Planning');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('financial_sciences', 'en-ZA', 'Financial Sciences', 'Financial Sciences');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('fine_arts', 'en-ZA', 'Fine Arts', 'Fine Arts');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('gender_sexuality_studies', 'en-ZA', 'Gender and Sexuality Studies', 'Gender and Sexuality Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('general', 'en-ZA', 'General', 'General');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('genetics', 'en-ZA', 'Genetics', 'Genetics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geochemistry', 'en-ZA', 'Geochemistry', 'Geochemistry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geography', 'en-ZA', 'Geography', 'Geography');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geology', 'en-ZA', 'Geology', 'Geology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geophysics', 'en-ZA', 'Geophysics', 'Geophysics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('geosciences', 'en-ZA', 'Geosciences', 'Geosciences');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('global_change_studies', 'en-ZA', 'Global Change Studies', 'Global Change Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('graphic_design', 'en-ZA', 'Graphic Design', 'Graphic Design');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('haematology', 'en-ZA', 'Haematology', 'Haematology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('health_sciences_social_services', 'en-ZA', 'Health Sciences and Social Services', 'Health Sciences and Social Services');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('history', 'en-ZA', 'History', 'History');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('human_social_studies', 'en-ZA', 'Human and Social Studies', 'Human and Social Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('humanities', 'en-ZA', 'Humanities', 'Humanities');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('human_resource_development', 'en-ZA', 'Human Resource Development', 'Human Resource Development');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('human_resources_management', 'en-ZA', 'Human Resources Management', 'Human Resources Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('human_rights_advocacy', 'en-ZA', 'Human Rights Advocacy', 'Human Rights Advocacy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('hydrogeology', 'en-ZA', 'Hydrogeology', 'Hydrogeology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('imas', 'en-ZA', 'IMAS', 'IMAS');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('industrial_organisational_psychology', 'en-ZA', 'Industrial and Organisational Psychology', 'Industrial and Organisational Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('information_systems', 'en-ZA', 'Information Systems', 'Information Systems');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('information_technology', 'en-ZA', 'Information Technology', 'Information Technology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('insurance_risk_management', 'en-ZA', 'Insurance and Risk Management', 'Insurance and Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('interior_design', 'en-ZA', 'Interior Design', 'Interior Design');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('internal_auditing', 'en-ZA', 'Internal Auditing', 'Internal Auditing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('international_finance', 'en-ZA', 'International Finance', 'International Finance');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('international_law', 'en-ZA', 'International Law', 'International Law');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('international_relations', 'en-ZA', 'International Relations', 'International Relations');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('international_studies', 'en-ZA', 'International Studies', 'International Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('investment_management', 'en-ZA', 'Investment Management', 'Investment Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('journalism', 'en-ZA', 'Journalism', 'Journalism');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('labour_law', 'en-ZA',  'Labour Law', 'Labour Law');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('law', 'en-ZA', 'Law', 'Law');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('law_military_science_security', 'en-ZA', 'Law, Military Science and Security', 'Law, Military Science and Security');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('library_information_science', 'en-ZA', 'Library and Information Science', 'Library and Information Science');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('linguistics', 'en-ZA', 'Linguistics', 'Linguistics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('Linguistics_languages', 'en-ZA', 'Linguistics and Languages', 'Linguistics and Languages');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('logistics', 'en-ZA', 'Logistics', 'Logistics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('management', 'en-ZA', 'Management', 'Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('management_accounting', 'en-ZA', 'Management Accounting', 'Management Accounting');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('manufacturing_engineering_technology', 'en-ZA',  'Manufacturing, Engineering and Technology', 'Manufacturing, Engineering and Technology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('marketing', 'en-ZA', 'Marketing', 'Marketing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('mathematics', 'en-ZA', 'Mathematics', 'Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('mechanical_engineering', 'en-ZA', 'Mechanical Engineering', 'Mechanical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('media_studies', 'en-ZA', 'Media Studies', 'Media Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('medical_sciences', 'en-ZA', 'Medical Sciences', 'Medical Sciences');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('metallurgical_engineering', 'en-ZA', 'Metallurgical Engineering', 'Metallurgical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('meteorology', 'en-ZA', 'Meteorology', 'Meteorology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('microelectronic_engineering', 'en-ZA', 'Microelectronic Engineering', 'Microelectronic Engineering');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('migration_displacement', 'en-ZA', 'Migration and Displacement', 'Migration and Displacement');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('mining', 'en-ZA', 'Mining', 'Mining');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('money_laundering_control', 'en-ZA', 'Money Laundering Control', 'Money Laundering Control');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('multimedia', 'en-ZA', 'Multimedia', 'Multimedia');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('music', 'en-ZA', 'Music', 'Music');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('neuropsychology', 'en-ZA', 'Neuropsychology', 'Neuropsychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('nursing', 'en-ZA', 'Nursing', 'Nursing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('occupational_therapy', 'en-ZA', 'Occupational Therapy', 'Occupational Therapy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('oceanography', 'en-ZA', 'Oceanography', 'Oceanography');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('office_administration', 'en-ZA', 'Office Administration', 'Office Administration');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('operations_management', 'en-ZA', 'Operations Management', 'Operations Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('optometry', 'en-ZA', 'Optometry', 'Optometry');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('pharmacology', 'en-ZA', 'Pharmacology', 'Pharmacology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('philosophy', 'en-ZA', 'Philosophy', 'Philosophy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('photography', 'en-ZA', 'Photography', 'Photography');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('physical_mathematical_computer_life_sciences', 'en-ZA', 'Physical, Mathematical, Computer and Life Sciences', 'Physical, Mathematical, Computer and Life Sciences');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('physical_planning_construction', 'en-ZA', 'Physical Planning and Construction', 'Physical Planning and Construction');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('physics', 'en-ZA', 'Physics', 'Physics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('physiotherapy', 'en-ZA', 'Physiotherapy', 'Physiotherapy');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('political_studies', 'en-ZA', 'Political Studies', 'Political Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('politics_journalism', 'en-ZA', 'Politics and Journalism', 'Politics and Journalism');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('programme_management', 'en-ZA', 'Programme Management', 'Programme Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('project_management', 'en-ZA', 'Project Management', 'Project Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('psychology', 'en-ZA', 'Psychology', 'Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('public_administration', 'en-ZA', 'Public Administration', 'Public Administration');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('public_health', 'en-ZA', 'Public Health', 'Public Health');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('public_management_governance', 'en-ZA', 'Public Management and Governance', 'Public Management and Governance');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('public_relations', 'en-ZA', 'Public Relations', 'Public Relations');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('publishing', 'en-ZA', 'Publishing', 'Publishing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('quantity_surveying', 'en-ZA', 'Quantity Surveying', 'Quantity Surveying');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('radiation_protection', 'en-ZA', 'Radiation Protection', 'Radiation Protection');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('religion_theology', 'en-ZA', 'Religion and Theology', 'Religion and Theology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('research_psychology', 'en-ZA', 'Research Psychology', 'Research Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('resource_conservation_biology', 'en-ZA', 'Resource Conservation Biology', 'Resource Conservation Biology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('retailing', 'en-ZA', 'Retailing', 'Retailing');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('retail_management', 'en-ZA', 'Retail Management', 'Retail Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('risk', 'en-ZA', 'Risk', 'Risk');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('risk_management', 'en-ZA', 'Risk Management', 'Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('risk_management_financial_risk_management', 'en-ZA', 'Risk Management and Financial Risk Management', 'Risk Management and Financial Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('robotics', 'en-ZA', 'Robotics', 'Robotics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('rural_development', 'en-ZA', 'Rural Development', 'Rural Development');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('security_management', 'en-ZA', 'Security Management', 'Security Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('services', 'en-ZA', 'Services', 'Services');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('social_psychology', 'en-ZA', 'Social Psychology', 'Social Psychology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('social_work', 'en-ZA', 'Social Work', 'Social Work');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('sociology', 'en-ZA', 'Sociology', 'Sociology');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('speech_language_studies', 'en-ZA', 'Speech-Language Studies', 'Speech-Language Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('sports_management', 'en-ZA', 'Sports Management', 'Sports Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('statistics', 'en-ZA', 'Statistics', 'Statistics');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('strategic_management', 'en-ZA', 'Strategic Management', 'Strategic Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('supply_chain_management', 'en-ZA', 'Supply Chain Management', 'Supply Chain Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('systems_development', 'en-ZA', 'Systems Development', 'Systems Development');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('taxation', 'en-ZA', 'Taxation', 'Taxation');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('tourism_hospitality', 'en-ZA', 'Tourism and Hospitality', 'Tourism and Hospitality');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('translation_interpreting', 'en-ZA', 'Translation and Interpreting', 'Translation and Interpreting');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('transportation_supply_chain_management', 'en-ZA', 'Transportation and Supply Chain Management', 'Transportation and Supply Chain Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('urban_planning', 'en-ZA', 'Urban Planning', 'Urban Planning');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('visual_studies', 'en-ZA', 'Visual Studies', 'Visual Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('wealth_management', 'en-ZA', 'Wealth Management', 'Wealth Management');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('women_gender_studies', 'en-ZA', 'Women and Gender Studies', 'Women and Gender Studies');
INSERT INTO party.fields_of_study (code, locale_id, name, description)
  VALUES ('zoology', 'en-ZA', 'Zoology', 'Zoology');


INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('male', 'en-US', 1, 'Male', 'Male');
INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('female', 'en-US', 2, 'Female', 'Female');
INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('transgender', 'en-US', 3, 'Transgender', 'Transgender');
INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('non_binary', 'en-US', 4, 'Non-binary', 'Non-binary');
INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('male', 'en-ZA', 1, 'Male', 'Male');
INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('female', 'en-ZA', 2, 'Female', 'Female');
INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('transgender', 'en-ZA', 3, 'Transgender', 'Transgender');
INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('non_binary', 'en-ZA', 4, 'Non-binary', 'Non-binary');
INSERT INTO party.genders (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_id_book', 'en-US', 10001, 'South African ID Book', 'South African ID Book', 'ZA', 'person');
INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_id_card', 'en-US', 10002, 'South African ID Card', 'South African ID Card', 'ZA', 'person');
INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_drivers_license', 'en-US', 10003, 'South African Driver''s License', 'South African Driver''s License', 'ZA', 'person');
INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('passport', 'en-US', 99999, 'Passport', 'Passport', 'person');
INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_company_registration', 'en-US', 20001, 'South African Company Registration', 'South African Company Registration', 'ZA', 'organization');

INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_id_book', 'en-ZA', 10001, 'South African ID Book', 'South African ID Book', 'ZA', 'person');
INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_id_card', 'en-ZA', 10002, 'South African ID Card', 'South African ID Card', 'ZA', 'person');
INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_drivers_license', 'en-ZA', 10003, 'South African Driver''s License', 'South African Driver''s License', 'ZA', 'person');
INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('passport', 'en-ZA', 99999, 'Passport', 'Passport', 'person');
INSERT INTO party.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_company_registration', 'en-ZA', 20001, 'South African Company Registration', 'South African Company Registration', 'ZA', 'organization');


INSERT INTO party.lock_type_categories(code, locale_id, name, description)
  VALUES ('fraud', 'en-US', 'Fraud', 'Fraud');
INSERT INTO party.lock_type_categories(code, locale_id, name, description)
  VALUES ('kyc', 'en-US', 'KYC', 'Know Your Customer');

INSERT INTO party.lock_type_categories(code, locale_id, name, description)
  VALUES ('fraud', 'en-ZA', 'Fraud', 'Fraud');
INSERT INTO party.lock_type_categories(code, locale_id, name, description)
  VALUES ('kyc', 'en-ZA', 'KYC', 'Know Your Customer');


INSERT INTO party.lock_types(category, code, locale_id, name, description, party_types)
  VALUES ('fraud', 'suspected_fraud', 'en-US', 'Suspected Fraud', 'Suspected Fraud', 'organization,person');
INSERT INTO party.lock_types(category, code, locale_id, name, description, party_types)
  VALUES ('kyc', 'kyc_unverified', 'en-US', 'KYC Unverified', 'KYC Unverified', 'organization,person');

INSERT INTO party.lock_types(category, code, locale_id, name, description, party_types)
  VALUES ('fraud', 'suspected_fraud', 'en-ZA', 'Suspected Fraud', 'Suspected Fraud', 'organization,person');
INSERT INTO party.lock_types(category, code, locale_id, name, description, party_types)
  VALUES ('kyc', 'kyc_unverified', 'en-ZA', 'KYC Unverified', 'KYC Unverified', 'organization,person');


INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('single', 'en-US', 1, 'Single', 'Single');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('married', 'en-US', 2, 'Married', 'Married');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('civil_partnership', 'en-US', 3, 'Civil Partnership', 'Civil Partnership');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('civil_union', 'en-US', 4, 'Civil Union', 'Civil Union');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('common_law', 'en-US', 5, 'Common Law', 'Common Law');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('divorced', 'en-US', 6, 'Divorced', 'Divorced');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('widowed', 'en-US', 7, 'Widowed', 'Widowed');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('single', 'en-ZA', 1, 'Single', 'Single');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('married', 'en-ZA', 2, 'Married', 'Married');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('civil_partnership', 'en-ZA', 3, 'Civil Partnership', 'Civil Partnership');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('civil_union', 'en-ZA', 4, 'Civil Union', 'Civil Union');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('common_law', 'en-ZA', 5, 'Common Law', 'Common Law');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('divorced', 'en-ZA', 6, 'Divorced', 'Divorced');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('widowed', 'en-ZA', 7, 'Widowed', 'Widowed');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('civil_partnership', 'civil_partnership', 'en-US', 1, 'Civil Partnership', 'Civil Partnership');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('civil_union', 'civil_union', 'en-US', 2, 'Civil Union', 'Civil Union');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'in_community_of_property', 'en-US', 3, 'In Community Of Property', 'In Community Of Property');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_without_accrual', 'en-US', 4, 'ANC Without Accrual', 'ANC Without Accrual');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_with_accrual', 'en-US', 5, 'ANC With Accrual', 'ANC With Accrual');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('civil_partnership', 'civil_partnership', 'en-ZA', 1, 'Civil Partnership', 'Civil Partnership');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('civil_union', 'civil_union', 'en-ZA', 2, 'Civil Union', 'Civil Union');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'in_community_of_property', 'en-ZA', 3, 'In Community Of Property', 'In Community Of Property');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_without_accrual', 'en-ZA', 4, 'ANC Without Accrual', 'ANC Without Accrual');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_with_accrual', 'en-ZA', 5, 'ANC With Accrual', 'ANC With Accrual');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('none', 'en-US', 1, 'None', 'None');
INSERT INTO party.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('minor', 'en-US', 2, 'Minor', 'Minor');
INSERT INTO party.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('emancipated_minor', 'en-US', 3, 'Emancipated Minor', 'Emancipated Minor');
INSERT INTO party.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('none', 'en-ZA', 1, 'None', 'None');
INSERT INTO party.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('minor', 'en-ZA', 2, 'Minor', 'Minor');
INSERT INTO party.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('emancipated_minor', 'en-ZA', 3, 'Emancipated Minor', 'Emancipated Minor');
INSERT INTO party.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('aunt', 'en-US', 'Aunt', 'Aunt');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('brother', 'en-US', 'Brother', 'Brother');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('cousin', 'en-US', 'Cousin', 'Cousin');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('father', 'en-US', 'Father', 'Father');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('friend', 'en-US', 'Friend', 'Friend');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('grandfather', 'en-US', 'Grandfather', 'Grandfather');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('grandmother', 'en-US', 'Grandmother', 'Grandmother');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('husband', 'en-US', 'Husband', 'Husband');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('life_partner', 'en-US', 'Life Partner', 'Life Partner');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('mother', 'en-US', 'Mother', 'Mother');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('nephew', 'en-US', 'Nephew', 'Nephew');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('niece', 'en-US', 'Niece', 'Niece');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('sister', 'en-US', 'Sister', 'Sister');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('uncle', 'en-US', 'Uncle', 'Uncle');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('wife', 'en-US', 'Wife', 'Wife');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('other', 'en-US', 'Other', 'Other');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('unknown', 'en-US', 'Unknown', 'Unknown');

INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('aunt', 'en-ZA', 'Aunt', 'Aunt');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('brother', 'en-ZA', 'Brother', 'Brother');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('cousin', 'en-ZA', 'Cousin', 'Cousin');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('father', 'en-ZA', 'Father', 'Father');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('friend', 'en-ZA', 'Friend', 'Friend');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('grandfather', 'en-ZA', 'Grandfather', 'Grandfather');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('grandmother', 'en-ZA', 'Grandmother', 'Grandmother');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('husband', 'en-ZA', 'Husband', 'Husband');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('life_partner', 'en-ZA', 'Life Partner', 'Life Partner');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('mother', 'en-ZA', 'Mother', 'Mother');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('nephew', 'en-ZA', 'Nephew', 'Nephew');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('niece', 'en-ZA', 'Niece', 'Niece');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('sister', 'en-ZA', 'Sister', 'Sister');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('uncle', 'en-ZA', 'Uncle', 'Uncle');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('wife', 'en-ZA', 'Wife', 'Wife');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('other', 'en-ZA', 'Other', 'Other');
INSERT INTO party.next_of_kin_types (code, locale_id, name, description)
  VALUES ('unknown', 'en-ZA', 'Unknown', 'Unknown');


INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('creative', 'en-US', 'Creative', 'Creative');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('driver', 'en-US', 'Driver', 'Driver');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('executive', 'en-US', 'Executive', 'Executive');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('farmer', 'en-US', 'Farmer', 'Farmer');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('government', 'en-US', 'Government Official', 'Government Official');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('guard', 'en-US', 'Guard', 'Guard');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('labourer', 'en-US', 'Labourer', 'Labourer');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('military_or_police', 'en-US', 'Military / Police', 'Military / Police');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('manager', 'en-US', 'Manager', 'Manager');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('office_staff', 'en-US', 'Office Staff', 'Office Staff');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('pensioner_retired', 'en-US', 'Pensioner / Retired', 'Pensioner / Retired');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('plant_or_machine_operator', 'en-US', 'Plant or Machine Operator or Assembler', 'Plant or Machine Operator or Assembler');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_business', 'en-US', 'Professional: Business', 'Professional: Business');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_education', 'en-US', 'Professional: Education', 'Professional: Education');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_engineering', 'en-US', 'Professional: Engineering', 'Professional: Engineering');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_government', 'en-US', 'Professional: Government', 'Professional: Government');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_legal', 'en-US', 'Professional: Legal', 'Professional: Legal');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_medical', 'en-US', 'Professional: Medical', 'Professional: Medical');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_scientific', 'en-US', 'Professional: Scientific', 'Professional: Scientific');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_transport', 'en-US', 'Professional: Transport', 'Professional: Transport');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('religious_charity', 'en-US', 'Religious / Charitable', 'Religious / Charitable');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('sales', 'en-US', 'Sales', 'Sales');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('semi_skilled_worker', 'en-US', 'Semi-skilled Worker', 'Semi-skilled Worker');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('service', 'en-US', 'Service', 'Service');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('skilled_worker', 'en-US', 'Skilled Worker', 'Skilled Worker');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('technician', 'en-US', 'Technician', 'Technician');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('trade_worker', 'en-US', 'Trade Worker', 'Trade Worker');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('unemployed', 'en-US', 'Unemployed', 'Unemployed');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('unknown', 'en-US', 'Unknown', 'Unknown');

INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('creative', 'en-ZA', 'Creative', 'Creative');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('driver', 'en-ZA', 'Driver', 'Driver');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('executive', 'en-ZA', 'Executive', 'Executive');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('farmer', 'en-ZA', 'Farmer', 'Farmer');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('government', 'en-ZA', 'Government Official', 'Government Official');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('guard', 'en-ZA', 'Guard', 'Guard');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('labourer', 'en-ZA', 'Labourer', 'Labourer');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('military_or_police', 'en-ZA', 'Military / Police', 'Military / Police');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('manager', 'en-ZA', 'Manager', 'Manager');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('office_staff', 'en-ZA', 'Office Staff', 'Office Staff');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('pensioner_retired', 'en-ZA', 'Pensioner / Retired', 'Pensioner / Retired');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('plant_or_machine_operator', 'en-ZA', 'Plant or Machine Operator or Assembler', 'Plant or Machine Operator or Assembler');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_business', 'en-ZA', 'Professional: Business', 'Professional: Business');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_education', 'en-ZA', 'Professional: Education', 'Professional: Education');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_engineering', 'en-ZA', 'Professional: Engineering', 'Professional: Engineering');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_government', 'en-ZA', 'Professional: Government', 'Professional: Government');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_legal', 'en-ZA', 'Professional: Legal', 'Professional: Legal');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_medical', 'en-ZA', 'Professional: Medical', 'Professional: Medical');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_scientific', 'en-ZA', 'Professional: Scientific', 'Professional: Scientific');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('professional_transport', 'en-ZA', 'Professional: Transport', 'Professional: Transport');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('religious_charity', 'en-ZA', 'Religious / Charitable', 'Religious / Charitable');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('sales', 'en-ZA', 'Sales', 'Sales');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('semi_skilled_worker', 'en-ZA', 'Semi-skilled Worker', 'Semi-skilled Worker');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('service', 'en-ZA', 'Service', 'Service');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('skilled_worker', 'en-ZA', 'Skilled Worker', 'Skilled Worker');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('technician', 'en-ZA', 'Technician', 'Technician');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('trade_worker', 'en-ZA', 'Trade Worker', 'Trade Worker');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('unemployed', 'en-ZA', 'Unemployed', 'Unemployed');
INSERT INTO party.occupations (code, locale_id, name, description)
  VALUES ('unknown', 'en-ZA', 'Unknown', 'Unknown');


INSERT INTO party.physical_address_purposes (code, locale_id, name, description, party_types)
  VALUES ('billing', 'en-US', 'Billing', 'Billing Address', 'organization,person');
INSERT INTO party.physical_address_purposes (code, locale_id, name, description, party_types)
  VALUES ('correspondence', 'en-US', 'Correspondence', 'Correspondence Address', 'organization,person');
INSERT INTO party.physical_address_purposes (code, locale_id, name, description, party_types)
  VALUES ('delivery', 'en-US', 'Delivery', 'Delivery Address', 'organization,person');

INSERT INTO party.physical_address_purposes (code, locale_id, name, description, party_types)
  VALUES ('billing', 'en-ZA', 'Billing', 'Billing Address', 'organization,person');
INSERT INTO party.physical_address_purposes (code, locale_id, name, description, party_types)
  VALUES ('correspondence', 'en-ZA', 'Correspondence', 'Correspondence Address', 'organization,person');
INSERT INTO party.physical_address_purposes (code, locale_id, name, description, party_types)
  VALUES ('delivery', 'en-ZA', 'Delivery', 'Delivery Address', 'organization,person');


INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('business', 'en-US', 'Business', 'Business Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('home', 'en-US', 'Home', 'Home Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('main', 'en-US', 'Main', 'Main Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('permanent', 'en-US', 'Permanent', 'Permanent Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('postal', 'en-US', 'Postal', 'Postal Address', 'organization,person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('registered_office', 'en-US', 'Registered Office', 'Registered Office Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('residential', 'en-US', 'Residential', 'Residential Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('service', 'en-US', 'Service', 'Service Address', 'organization,person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('sole_trader', 'en-US', 'Sole Trader', 'Sole Trader Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('temporary', 'en-US', 'Temporary', 'Temporary Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('work', 'en-US', 'Work', 'Work Address', 'person');

INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('business', 'en-ZA', 'Business', 'Business Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('home', 'en-ZA', 'Home', 'Home Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('main', 'en-ZA', 'Main', 'Main Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('permanent', 'en-ZA', 'Permanent', 'Permanent Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('postal', 'en-ZA', 'Postal', 'Postal Address', 'organization,person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('registered_office', 'en-ZA', 'Registered Office', 'Registered Office Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('residential', 'en-ZA', 'Residential', 'Residential Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('service', 'en-ZA', 'Service', 'Service Address', 'organization,person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('sole_trader', 'en-ZA', 'Sole Trader', 'Sole Trader Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('temporary', 'en-ZA', 'Temporary', 'Temporary Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, name, description, party_types)
  VALUES ('work', 'en-ZA', 'Work', 'Work Address', 'person');


INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('building', 'en-US', 'Building', 'Building');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('complex', 'en-US', 'Complex', 'Complex');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('farm', 'en-US', 'Farm', 'Farm');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('international', 'en-US', 'International', 'International');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('postal', 'en-US', 'Postal', 'Postal');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('site', 'en-US', 'Site', 'Site');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('street', 'en-US', 'Street', 'Street');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('unstructured', 'en-US', 'Unstructured', 'Unstructured');

INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('building', 'en-ZA', 'Building', 'Building');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('complex', 'en-ZA', 'Complex', 'Complex');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('farm', 'en-ZA', 'Farm', 'Farm');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('international', 'en-ZA', 'International', 'International');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('postal', 'en-ZA', 'Postal', 'Postal');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('site', 'en-ZA', 'Site', 'Site');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('street', 'en-ZA', 'Street', 'Street');
INSERT INTO party.physical_address_types (code, locale_id, name, description)
  VALUES ('unstructured', 'en-ZA', 'Unstructured', 'Unstructured');


INSERT INTO party.preference_type_categories (code, locale_id, name, description)
  VALUES ('correspondence', 'en-US', 'Correspondence', 'Correspondence');

INSERT INTO party.preference_type_categories (code, locale_id, name, description)
  VALUES ('correspondence', 'en-ZA', 'Correspondence', 'Correspondence');


INSERT INTO party.preference_types (category, code, locale_id, name, description, party_types)
  VALUES ('correspondence','contact_person', 'en-US', 'Contact Person', 'Contact Person', 'organization');
INSERT INTO party.preference_types (category, code, locale_id, name, description, party_types)
  VALUES ('correspondence','correspondence_language', 'en-US', 'Correspondence Language', 'Correspondence Language', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, name, description, party_types)
  VALUES ('correspondence', 'preferred_contact_mechanism', 'en-US', 'Preferred Contact Mechanism', 'Preferred Contact Mechanism', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, name, description, party_types)
  VALUES ('correspondence','time_to_contact', 'en-US', 'Time To Contact', 'Suitable Time To Contact', 'person');

INSERT INTO party.preference_types (category, code, locale_id, name, description, party_types)
  VALUES ('correspondence','contact_person', 'en-ZA', 'Contact Person', 'Contact Person', 'organization');
INSERT INTO party.preference_types (category, code, locale_id, name, description, party_types)
  VALUES ('correspondence', 'correspondence_language', 'en-ZA', 'Correspondence Language', 'Correspondence Language', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, name, description, party_types)
  VALUES ('correspondence', 'preferred_contact_mechanism', 'en-ZA', 'Preferred Contact Mechanism', 'Preferred Contact Mechanism', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, name, description, party_types)
  VALUES ('correspondence', 'time_to_contact', 'en-ZA', 'Time To Contact', 'Suitable Time To Contact', 'person');


INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('below_grade10', 'en-US', 1, 'Below Grade 10', 'Below Grade 10');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('grade10', 'en-US', 2, 'Grade 10', 'Grade 10');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('national_senior_certificate', 'en-US', 3, 'National Senior Certificate', 'National Senior Certificate');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('higher_certificate', 'en-US', 4, 'Higher Certificate', 'Higher Certificate');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('diploma', 'en-US', 5, 'Diploma', 'Diploma');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('advanced_diploma', 'en-US', 6, 'Advanced Diploma', 'Advanced Diploma');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('bachelors_degree', 'en-US', 7, 'Bachelor''s Degree', 'Bachelor''s Degree');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('graduate_certificate', 'en-US', 8, 'Graduate Certificate', 'Graduate Certificate');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('graduate_diploma', 'en-US', 9, 'Graduate Diploma', 'Graduate Diploma');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('masters_degree', 'en-US', 10, 'Master''s Degree', 'Master''s Degree');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('doctoral_degree', 'en-US', 11, 'Doctoral Degree', 'Doctoral Degree');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('professional_registration', 'en-US', 50, 'Professional Registration', 'Professional Registration');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('other_certificate', 'en-US', 98, 'Other Certificate', 'Other Certificate');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('other_qualification', 'en-US', 99, 'Other Qualification', 'Other Qualification');


INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('below_grade10', 'en-ZA', 1, 'Below Grade 10', 'Below Grade 10');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('grade10', 'en-ZA', 2, 'Grade 10', 'Grade 10');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('national_senior_certificate', 'en-ZA', 3, 'National Senior Certificate', 'National Senior Certificate');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('higher_certificate', 'en-ZA', 4, 'Higher Certificate', 'Higher Certificate');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('diploma', 'en-ZA', 5, 'Diploma', 'Diploma');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('advanced_diploma', 'en-ZA', 6, 'Advanced Diploma', 'Advanced Diploma');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('bachelors_degree', 'en-ZA', 7, 'Bachelor''s Degree', 'Bachelor''s Degree');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('graduate_certificate', 'en-ZA', 8, 'Graduate Certificate', 'Graduate Certificate');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('graduate_diploma', 'en-ZA', 9, 'Graduate Diploma', 'Graduate Diploma');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('masters_degree', 'en-ZA', 10, 'Master''s Degree', 'Master''s Degree');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('doctoral_degree', 'en-ZA', 11, 'Doctoral Degree', 'Doctoral Degree');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('professional_registration', 'en-ZA', 50, 'Professional Registration', 'Professional Registration');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('other_certificate', 'en-ZA', 98, 'Other Certificate', 'Other Certificate');
INSERT INTO party.qualification_types (code, locale_id, sort_index, name, description)
  VALUES ('other_qualification', 'en-ZA', 99, 'Other Qualification', 'Other Qualification');


INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('black', 'en-US', 1, 'Black', 'Black');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('coloured', 'en-US', 2, 'Coloured', 'Coloured');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('indian', 'en-US', 3, 'Indian', 'Indian');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('asian', 'en-US', 4, 'Asian', 'Asian');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('white', 'en-US', 5, 'White', 'White');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('black', 'en-ZA', 1, 'Black', 'Black');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('coloured', 'en-ZA', 2, 'Coloured', 'Coloured');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('indian', 'en-ZA', 3, 'Indian', 'Indian');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('asian', 'en-ZA', 4, 'Asian', 'Asian');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('white', 'en-ZA', 5, 'White', 'White');
INSERT INTO party.races (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_business_visa', 'en-US', 1, 'Business Visa', 'Business Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_general_work_visa', 'en-US', 2, 'General Work Visa', 'General Work Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_critical_skills_visa', 'en-US', 3, 'Critical Skills Work Visa', 'Critical Skills Work Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_intra_company_transfer_visa', 'en-US', 4, 'Intra-company Transfer Work Visa', 'Intra-company Transfer Work Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_study_visa', 'en-US', 5, 'Study Visa', 'Study Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_exchange_visa', 'en-US', 6, 'Exchange Visa', 'Exchange Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_retired_persons_visa', 'en-US', 7, 'Retired Persons Visa', 'Retired Persons Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_relatives_visa', 'en-US', 8, 'Relatives Visa', 'Relatives Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_medical_treatment_visa', 'en-US', 9, 'Medical Treatment Visa', 'Medical Treatment Visa', 'ZA');

INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_business_visa', 'en-ZA', 1, 'Business Visa', 'Business Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_general_work_visa', 'en-ZA', 2, 'General Work Visa', 'General Work Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_critical_skills_visa', 'en-ZA', 3, 'Critical Skills Work Visa', 'Critical Skills Work Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_intra_company_transfer_visa', 'en-ZA', 4, 'Intra-company Transfer Work Visa', 'Intra-company Transfer Work Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_study_visa', 'en-ZA', 5, 'Study Visa', 'Study Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_exchange_visa', 'en-ZA', 6, 'Exchange Visa', 'Exchange Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_retired_persons_visa', 'en-ZA', 7, 'Retired Persons Visa', 'Retired Persons Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_relatives_visa', 'en-ZA', 8, 'Relatives Visa', 'Relatives Visa', 'ZA');
INSERT INTO party.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_medical_treatment_visa', 'en-ZA', 9, 'Medical Treatment Visa', 'Medical Treatment Visa', 'ZA');


INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('citizen', 'en-US', 1, 'Citizen', 'Citizen');
INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('permanent_resident', 'en-US', 2, 'Permanent Resident', 'Permanent Resident');
INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('foreign_national', 'en-US', 3, 'Foreign National', 'Foreign National');
INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('refugee', 'en-US', 4, 'Refugee', 'Refugee');
INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('citizen', 'en-ZA', 1, 'Citizen', 'Citizen');
INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('permanent_resident', 'en-ZA', 2, 'Permanent Resident', 'Permanent Resident');
INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('foreign_national', 'en-ZA', 3, 'Foreign National', 'Foreign National');
INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('refugee', 'en-ZA', 4, 'Refugee', 'Refugee');
INSERT INTO party.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('owner', 'en-US', 1, 'Owner', 'Owner');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('renter', 'en-US', 2, 'Renter', 'Renter');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('cohabitant', 'en-US', 3, 'Co-Habitant', 'Co-Habitant');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('boarder', 'en-US', 4, 'Boarder', 'Boarder');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('living_with_parents', 'en-US', 5, 'Living with Parents', 'Living with Parents');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('not_specified', 'en-US', 6, 'Not Specified', 'Not Specified');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('owner', 'en-ZA', 1, 'Owner', 'Owner');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('renter', 'en-ZA', 2, 'Renter', 'Renter');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('cohabitant', 'en-ZA', 3, 'Co-Habitant', 'Co-Habitant');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('boarder', 'en-ZA', 4, 'Boarder', 'Boarder');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('living_with_parents', 'en-ZA', 5, 'Living with Parents', 'Living with Parents');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('not_specified', 'en-ZA', 6, 'Not Specified', 'Not Specified');
INSERT INTO party.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('affiliate', 'en-US', 'Affiliate', 'A company under common control with another company', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('agency', 'en-US', 'Agency', 'An organization providing a particular service on behalf of another organization', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('agent', 'en-US', 'Agent', 'A person who acts on behalf of an organization', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('association', 'en-US', 'Association', 'An organization that provides services such as networking and sharing of information within particular fields of interest or industries', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('association_member', 'en-US', 'Association Member', 'An organization or person who is a member of an association', 'organization,person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('business_customer', 'en-US', 'Business Customer', 'A business who buys goods or services from a shop or business', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('child', 'en-US', 'Child', 'A boy or girl from the time of birth until he or she is an adult, or a son or daughter of any age', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('civil_partner', 'en-US', 'Civil Partner', 'A significant other in a civil partnership', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('company', 'en-US', 'Company', 'A commercial business', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('competitor', 'en-US', 'Competitor', 'An organization engaged in commercial competition with others', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('contractor', 'en-US', 'Contractor', 'A person paid to work on a project for a particular period and amount of money', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('department', 'en-US', 'Department', 'A division of a large organization dealing with a specific area of activity', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('director', 'en-US', 'Director', 'A person who is in charge of an activity, department, or organization', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('distributor', 'en-US', 'Distributor', 'An organization that buys noncompeting products or product lines and sells them direct to end users or customers', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('division', 'en-US', 'Division', 'A major section of an organization with responsibility for a particular area of activity', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('employee', 'en-US', 'Employee', 'A person employed for a salary or wages', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('employer', 'en-US', 'Employer', 'An organization or person that has employees', 'organization,person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('family', 'en-US', 'Family', 'A group of one or more parents and their children living together as a unit', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('family_member', 'en-US', 'Family Member', 'A person who is a member of a family', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('household', 'en-US', 'Household', 'A group of people living together', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('household_member', 'en-US', 'Household Member', 'A person who is a member of a household', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('individual_customer', 'en-US', 'Individual Customer', 'A person who buys goods or services from a shop or business', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('joint_venture', 'en-US', 'Joint Venture', 'A joint venture is a business entity created by two or more organizations, generally characterized by shared ownership, shared returns and risks, and shared governance', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('life_partner', 'en-US', 'Life Partner', 'Either member of a couple in a long-term relationship', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('organization', 'en-US', 'Organization', 'A group of people bound together in a formal relationship to achieve common objectives', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('parent', 'en-US', 'Parent', 'A person''s father or mother', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('partner', 'en-US', 'Partner', 'A person associated with another or others as a principal or a contributor of capital in a business or a joint venture', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('partnership', 'en-US', 'Partnership', 'A formal arrangement by two or more parties to manage and operate a business and share its profits', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('prospect', 'en-US', 'Prospect', 'An organization or a person who is a potential customer', 'organization,person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('regulatory_authority', 'en-US', 'Regulatory Authority', 'A government agency that is responsible for exercising autonomous dominion over some area of human activity in a regulatory or monitoring capacity', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('sibling', 'en-US', 'Sibling', 'Each of two or more children or offspring having one or both parents in common', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('shareholder', 'en-US', 'Shareholder', 'An organization or person that legally owns one or more shares of the share capital of a public or private company', 'organization,person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('spouse', 'en-US', 'Spouse', 'A significant other in a marriage', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('subsidiary', 'en-US', 'Subsidiary', 'A company controlled by a parent company or holding company', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('supplier', 'en-US', 'Supplier', 'A person or organization that provides something needed such as a product or service', 'organization,person');

INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('affiliate', 'en-ZA', 'Affiliate', 'A company under common control with another company', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('agency', 'en-ZA', 'Agency', 'An organization providing a particular service on behalf of another organization', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('agent', 'en-ZA', 'Agent', 'A person who acts on behalf of an organization', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('association', 'en-ZA', 'Association', 'An organization that provides services such as networking and sharing of information within particular fields of interest or industries', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('association_member', 'en-ZA', 'Association Member', 'An organization or person who is a member of an association', 'organization,person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('business_customer', 'en-ZA', 'Business Customer', 'A business who buys goods or services from a shop or business', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('child', 'en-ZA', 'Child', 'A boy or girl from the time of birth until he or she is an adult, or a son or daughter of any age', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('civil_partner', 'en-ZA', 'Civil Partner', 'A significant other in a civil partnership', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('company', 'en-ZA', 'Company', 'A commercial business', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('competitor', 'en-ZA', 'Competitor', 'An organization engaged in commercial competition with others', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('contractor', 'en-ZA', 'Contractor', 'A person paid to work on a project for a particular period and amount of money', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('department', 'en-ZA', 'Department', 'A division of a large organization dealing with a specific area of activity', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('director', 'en-ZA', 'Director', 'A person who is in charge of an activity, department, or organization', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('distributor', 'en-ZA', 'Distributor', 'An organization that buys noncompeting products or product lines and sells them direct to end users or customers', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('division', 'en-ZA', 'Division', 'A major section of an organization with responsibility for a particular area of activity', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('employee', 'en-ZA', 'Employee', 'A person employed for a salary or wages', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('employer', 'en-ZA', 'Employer', 'An organization or person that has employees', 'organization,person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('family', 'en-ZA', 'Family', 'A group of one or more parents and their children living together as a unit', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('family_member', 'en-ZA', 'Family Member', 'A person who is a member of a family', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('household', 'en-ZA', 'Household', 'A group of people living together', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('household_member', 'en-ZA', 'Household Member', 'A person who is a member of a household', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('individual_customer', 'en-ZA', 'Individual Customer', 'A person who buys goods or services from a shop or business', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('joint_venture', 'en-ZA', 'Joint Venture', 'A joint venture is a business entity created by two or more organizations, generally characterized by shared ownership, shared returns and risks, and shared governance', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('life_partner', 'en-ZA', 'Life Partner', 'Either member of a couple in a long-term relationship', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('organization', 'en-ZA', 'Organization', 'A group of people bound together in a formal relationship to achieve common objectives', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('parent', 'en-ZA', 'Parent', 'A person''s father or mother', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('partner', 'en-ZA', 'Partner', 'A person associated with another or others as a principal or a contributor of capital in a business or a joint venture', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('partnership', 'en-ZA', 'Partnership', 'A formal arrangement by two or more parties to manage and operate a business and share its profits', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('prospect', 'en-ZA', 'Prospect', 'An organization or a person who is a potential customer', 'organization,person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('regulatory_authority', 'en-ZA', 'Regulatory Authority', 'A government agency that is responsible for exercising autonomous dominion over some area of human activity in a regulatory or monitoring capacity', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('sibling', 'en-ZA', 'Sibling', 'Each of two or more children or offspring having one or both parents in common', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('shareholder', 'en-ZA', 'Shareholder', 'An organization or person that legally owns one or more shares of the share capital of a public or private company', 'organization,person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('spouse', 'en-ZA', 'Spouse', 'A significant other in a marriage', 'person');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('subsidiary', 'en-ZA', 'Subsidiary', 'A company controlled by a parent company or holding company', 'organization');
INSERT INTO party.role_types (code, locale_id, name, description, party_types)
  VALUES ('supplier', 'en-ZA', 'Supplier', 'A person or organization that provides something needed such as a product or service', 'organization,person');


INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('affiliation', 'en-US', 'Affiliation', 'A relationship between affiliates', 'affiliate', 'affiliate');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('association_membership', 'en-US', 'Association Membership', 'A relationship identifying an organization as a member of an association', 'association', 'association_member');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('civil_partnership', 'en-US', 'Civil Partnership', 'A legally recognized union with rights similar to those of marriage', 'civil_partner', 'civil_partner');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('company_shareholder', 'en-US', 'Company Shareholder', 'A relationship between a company and an organization or person that legally owns one or more shares of the share capital of the company', 'company', 'shareholder');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('company_subsidiary', 'en-US', 'Company Subsidiary', 'A relationship between a parent company or holding company and a subsidiary', 'company', 'subsidiary');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('competitor', 'en-US', 'Competitor', 'A relationship between two organizations that are engaged in commercial competition with each other', 'competitor', 'competitor');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('division_department', 'en-US', 'Division Department', 'A relationship between a division and a department that forms part of the division', 'division', 'department');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('directorship', 'en-US', 'Directorship', 'A relationship identifying a person as a directory of a company', 'company', 'director');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('employment', 'en-US', 'Employment', 'A relationship between two parties that is usually based on contract where work is paid for', 'employer', 'employee');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('family_membership', 'en-US', 'Family Membership', 'A relationship identifying a person as a member of a family', 'family', 'family_member');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('household_membership', 'en-US', 'Household Membership', 'A relationship identifying a person as a member of a household', 'household', 'household_member');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('joint_venture', 'en-US', 'Joint Venture', 'A relationship identifying an organization as a participant in a joint venture arrangement', 'joint_venture', 'organization');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('life_partnership', 'en-US', 'Life Partnership', 'A relationship between a parent and child', 'life_partner', 'life_partner');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('marriage', 'en-US', 'Marriage', 'A legally recognized union of two people as partners in a personal relationship ', 'spouse', 'spouse');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('organization_agency', 'en-US', 'Organization Agency', 'A relationship where an organization provides a particular service on behalf of another organization', 'organization', 'agency');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('organization_agent', 'en-US', 'Organization Agent', 'A relationship between an organization and a person who acts on behalf of the organization', 'organization', 'agent');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('organization_department', 'en-US', 'Organization Department', 'A relationship between an organization and a department that forms part of the organization', 'organization', 'department');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('organization_division', 'en-US', 'Organization Division', 'A relationship between an organization and a division that forms part of the organization', 'organization', 'division');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('parent_child', 'en-US', 'Parent-Child', 'A relationship between a parent and a child', 'parent', 'child');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('partnership', 'en-US', 'Partnership', 'A relationship between a partnership and one of its partners', 'partnership', 'partner');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('sibling', 'en-US', 'Sibling', 'A relationship between two children or offspring having one or both parents in common', 'sibling', 'sibling');

INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('affiliation', 'en-ZA', 'Affiliation', 'A relationship between affiliates', 'affiliate', 'affiliate');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('association_membership', 'en-ZA', 'Association Membership', 'A relationship identifying an organization as a member of an association', 'association', 'association_member');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('civil_partnership', 'en-ZA', 'Civil Partnership', 'A legally recognized union with rights similar to those of marriage', 'civil_partner', 'civil_partner');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('company_shareholder', 'en-ZA', 'Company Shareholder', 'A relationship between a company and an organization or person that legally owns one or more shares of the share capital of the company', 'company', 'shareholder');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('company_subsidiary', 'en-ZA', 'Company Subsidiary', 'A relationship between a parent company or holding company and a subsidiary', 'company', 'subsidiary');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('competitor', 'en-ZA', 'Competitor', 'A relationship between two organizations that are engaged in commercial competition with each other', 'competitor', 'competitor');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('division_department', 'en-ZA', 'Division Department', 'A relationship between a division and a department that forms part of the division', 'division', 'department');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('directorship', 'en-ZA', 'Directorship', 'A relationship identifying a person as a directory of a company', 'company', 'director');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('employment', 'en-ZA', 'Employment', 'A relationship between two parties that is usually based on contract where work is paid for', 'employer', 'employee');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('family_membership', 'en-ZA', 'Family Membership', 'A relationship identifying a person as a member of a family', 'family', 'family_member');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('household_membership', 'en-ZA', 'Household Membership', 'A relationship identifying a person as a member of a household', 'household', 'household_member');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('joint_venture', 'en-ZA', 'Joint Venture', 'A relationship identifying an organization as a participant in a joint venture arrangement', 'joint_venture', 'organization');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('life_partnership', 'en-ZA', 'Life Partnership', 'A relationship between a parent and child', 'life_partner', 'life_partner');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('marriage', 'en-ZA', 'Marriage', 'A legally recognized union of two people as partners in a personal relationship ', 'spouse', 'spouse');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('organization_agency', 'en-ZA', 'Organization Agency', 'A relationship where an organization provides a particular service on behalf of another organization', 'organization', 'agency');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('organization_agent', 'en-ZA', 'Organization Agent', 'A relationship between an organization and a person who acts on behalf of the organization', 'organization', 'agent');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('organization_department', 'en-ZA', 'Organization Department', 'A relationship between an organization and a department that forms part of the organization', 'organization', 'department');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('organization_division', 'en-ZA', 'Organization Division', 'A relationship between an organization and a division that forms part of the organization', 'organization', 'division');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('parent_child', 'en-ZA', 'Parent-Child', 'A relationship between a parent and a child', 'parent', 'child');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('partnership', 'en-ZA', 'Partnership', 'A relationship between a partnership and one of its partners', 'partnership', 'partner');
INSERT INTO party.association_types(code, locale_id, name, description, first_party_role, second_party_role)
  VALUES ('sibling', 'en-ZA', 'Sibling', 'A relationship between two children or offspring having one or both parents in common', 'sibling', 'sibling');


INSERT INTO party.association_property_types (association_type, code, locale_id, name, description, value_type)
  VALUES ('company_shareholder', 'shareholding', 'en-US', 'Shareholding', 'Shareholding', 'decimal');

INSERT INTO party.association_property_types (association_type, code, locale_id, name, description, value_type)
  VALUES ('company_shareholder', 'shareholding', 'en-ZA', 'Shareholding', 'Shareholding', 'decimal');


INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('salary_wages', 'en-US', 1, 'Salary/Wages', 'Salary/Wages');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('allowance', 'en-US', 'Allowance', 'Allowance');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('bonus_incentive', 'en-US', 'Bonus/Incentive', 'Bonus/Incentive');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('bursary', 'en-US', 'Bursary', 'Bursary');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('commission', 'en-US', 'Commission', 'Commission');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('court_award', 'en-US', 'Court Award', 'Court Award');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('development_funding', 'en-US', 'Development Funding', 'Development Funding');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('donation', 'en-US', 'Donation', 'Donation');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('entertainment_sports', 'en-US', 'Entertainment/Sports', 'Entertainment/Sports');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('family_fortune', 'en-US', 'Family Fortune', 'Family Fortune');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('gift', 'en-US', 'Gift', 'Gift');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('grant_other', 'en-US', 'Grant - Other', 'Grant - Other');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('grant_social', 'en-US', 'Grant - Social', 'Grant - Social');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('inheritance', 'en-US', 'Inheritance', 'Inheritance');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('insurance_claim', 'en-US', 'Insurance Claim', 'Insurance Claim');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('investments_other', 'en-US', 'Investments - Other', 'Investments - Other');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('investments_property', 'en-US', 'Investments - Property', 'Investments - Property (Commercial and Residential)');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('investments_securities', 'en-US', 'Investments - Securities', 'Investments - Securities');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('life_policy', 'en-US', 'Maturity/Surrender of Life Policy', 'Maturity/Surrender of Life Policy');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('loan', 'en-US', 'Loan', 'Loan');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('maintenance_alimony', 'en-US', 'Maintenance/Alimony', 'Maintenance/Alimony');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('other', 'en-US', 'Other', 'Other');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('owner_draws', 'en-US', 'Owner Draws', 'Owner Draws');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('parent_company', 'en-US', 'Parent Company', 'Parent Company');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('parent', 'en-US', 'Parent', 'Parent');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('pension', 'en-US', 'Pension', 'Pension');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('relative', 'en-US', 'Relative', 'Relative');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('retirement_annuity', 'en-US', 'Retirement Annuity', 'Retirement Annuity');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_assets', 'en-US', 'Sale of Assets', 'Sale of Assets');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_business', 'en-US', 'Sale of Business', 'Sale of Business');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_investments', 'en-US', 'Sale of Investments', 'Sale of Investments');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_property', 'en-US', 'Sale of Property', 'Sale of Property');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_vehicle', 'en-US', 'Sale of Vehicle', 'Sale of Vehicle');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('savings', 'en-US', 'Savings', 'Savings');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('settlement', 'en-US', 'Settlement', 'Settlement');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('shareholder_contributions', 'en-US', 'Shareholder Contributions', 'Shareholder Contributions');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('tax_rebate', 'en-US', 'Tax Rebate', 'Tax Rebate');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('trading_income_sales', 'en-US', 'Trading Income - Sales', 'Trading Income - Sales');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('trading_income_services', 'en-US', 'Trading Income - Services', 'Trading Income - Services');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('trust_foundation', 'en-US', 'Trust/Foundation', 'Beneficiary of Trust or Foundation');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('winnings', 'en-US', 'Winnings', 'Winnings');

INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('salary_wages', 'en-ZA', 1, 'Salary/Wages', 'Salary/Wages');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('allowance', 'en-ZA', 'Allowance', 'Allowance');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('bonus_incentive', 'en-ZA', 'Bonus/Incentive', 'Bonus/Incentive');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('bursary', 'en-ZA', 'Bursary', 'Bursary');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('commission', 'en-ZA', 'Commission', 'Commission');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('court_award', 'en-ZA', 'Court Award', 'Court Award');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('development_funding', 'en-ZA', 'Development Funding', 'Development Funding');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('donation', 'en-ZA', 'Donation', 'Donation');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('entertainment_sports', 'en-ZA', 'Entertainment/Sports', 'Entertainment/Sports');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('family_fortune', 'en-ZA', 'Family Fortune', 'Family Fortune');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('gift', 'en-ZA', 'Gift', 'Gift');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('grant_other', 'en-ZA', 'Grant - Other', 'Grant - Other');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('grant_social', 'en-ZA', 'Grant - Social', 'Grant - Social');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('inheritance', 'en-ZA', 'Inheritance', 'Inheritance');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('insurance_claim', 'en-ZA', 'Insurance Claim', 'Insurance Claim');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('investments_other', 'en-ZA', 'Investments - Other', 'Investments - Other');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('investments_property', 'en-ZA', 'Investments - Property', 'Investments - Property (Commercial and Residential)');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('investments_securities', 'en-ZA', 'Investments - Securities', 'Investments - Securities');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('life_policy', 'en-ZA', 'Maturity/Surrender of Life Policy', 'Maturity/Surrender of Life Policy');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('loan', 'en-ZA', 'Loan', 'Loan');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('maintenance_alimony', 'en-ZA', 'Maintenance/Alimony', 'Maintenance/Alimony');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('other', 'en-ZA', 'Other', 'Other');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('owner_draws', 'en-ZA', 'Owner Draws', 'Owner Draws');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('parent_company', 'en-ZA', 'Parent Company', 'Parent Company');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('parent', 'en-ZA', 'Parent', 'Parent');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('pension', 'en-ZA', 'Pension', 'Pension');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('relative', 'en-ZA', 'Relative', 'Relative');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('retirement_annuity', 'en-ZA', 'Retirement Annuity', 'Retirement Annuity');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_assets', 'en-ZA', 'Sale of Assets', 'Sale of Assets');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_business', 'en-ZA', 'Sale of Business', 'Sale of Business');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_investments', 'en-ZA', 'Sale of Investments', 'Sale of Investments');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_property', 'en-ZA', 'Sale of Property', 'Sale of Property');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('sale_vehicle', 'en-ZA', 'Sale of Vehicle', 'Sale of Vehicle');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('savings', 'en-ZA', 'Savings', 'Savings');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('settlement', 'en-ZA', 'Settlement', 'Settlement');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('shareholder_contributions', 'en-ZA', 'Shareholder Contributions', 'Shareholder Contributions');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('tax_rebate', 'en-ZA', 'Tax Rebate', 'Tax Rebate');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('trading_income_sales', 'en-ZA', 'Trading Income - Sales', 'Trading Income - Sales');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('trading_income_services', 'en-ZA', 'Trading Income - Services', 'Trading Income - Services');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('trust_foundation', 'en-ZA', 'Trust/Foundation', 'Beneficiary of Trust or Foundation');
INSERT INTO party.source_of_funds_types (code, locale_id, name, description)
  VALUES ('winnings', 'en-ZA', 'Winnings', 'Winnings');


INSERT INTO party.source_of_wealth_types (code, locale_id, sort_index, name, description)
  VALUES ('savings', 'en-US', 1, 'Savings', 'Savings from Employment Income, Director Fees, Dividends');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('commercial_activities', 'en-US', 'Commercial Activities', 'Commercial Activities');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('grant_government', 'en-US', 'Grant - Government', 'Grant - Government');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('grant_other', 'en-US', 'Grant - Other', 'Grant - Other');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('inheritance', 'en-US', 'Inheritance', 'Inheritance');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('investments', 'en-US', 'Investments', 'Investments');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('other', 'en-US', 'Other', 'Other');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('property_of_business_sale', 'en-US', 'Property of Business Sale', 'Property of Business Sale');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('royalties', 'en-US', 'Royalties', 'Royalties');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('sale_of_business', 'en-US', 'Sale of Business', 'Sale of Business');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('sale_of_property', 'en-US', 'Sale of Property', 'Sale of Property');

INSERT INTO party.source_of_wealth_types (code, locale_id, sort_index, name, description)
  VALUES ('savings', 'en-ZA', 1, 'Savings', 'Savings from Employment Income, Director Fees, Dividends');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('commercial_activities', 'en-ZA', 'Commercial Activities', 'Commercial Activities');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('grant_government', 'en-ZA', 'Grant - Government', 'Grant - Government');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('grant_other', 'en-ZA', 'Grant - Other', 'Grant - Other');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('inheritance', 'en-ZA', 'Inheritance', 'Inheritance');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('investments', 'en-ZA', 'Investments', 'Investments');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('other', 'en-ZA', 'Other', 'Other');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('property_of_business_sale', 'en-ZA', 'Property of Business Sale', 'Property of Business Sale');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('royalties', 'en-ZA', 'Royalties', 'Royalties');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('sale_of_business', 'en-ZA', 'Sale of Business', 'Sale of Business');
INSERT INTO party.source_of_wealth_types (code, locale_id, name, description)
  VALUES ('sale_of_property', 'en-ZA', 'Sale of Property', 'Sale of Property');


INSERT INTO party.status_type_categories(code, locale_id, name, description)
  VALUES ('fraud', 'en-US', 'Fraud', 'Fraud');
INSERT INTO party.status_type_categories(code, locale_id, name, description)
  VALUES ('kyc', 'en-US', 'KYC', 'Know Your Customer');

INSERT INTO party.status_type_categories(code, locale_id, name, description)
  VALUES ('fraud', 'en-ZA', 'Fraud', 'Fraud');
INSERT INTO party.status_type_categories(code, locale_id, name, description)
  VALUES ('kyc', 'en-ZA', 'KYC', 'Know Your Customer');


INSERT INTO party.status_types(category, code, locale_id, name, description, party_types)
  VALUES ('fraud', 'fraud_investigation', 'en-US', 'Fraud Investigation', 'Fraud Investigation', 'organization,person');
INSERT INTO party.status_types(category, code, locale_id, name, description, party_types)
  VALUES ('kyc', 'kyc_verified', 'en-US', 'KYC Verified', 'KYC Verified', 'organization,person');

INSERT INTO party.status_types(category, code, locale_id, name, description, party_types)
  VALUES ('fraud', 'fraud_investigation', 'en-ZA', 'Fraud Investigation', 'Fraud Investigation', 'organization,person');
INSERT INTO party.status_types(category, code, locale_id, name, description, party_types)
  VALUES ('kyc', 'kyc_verified', 'en-ZA', 'KYC Verified', 'KYC Verified', 'organization,person');


INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_income_tax_number', 'en-US', 1, 'Income Tax Number', 'South African Income Tax Reference Number', 'ZA', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_vat_tax_number', 'en-US', 2, 'VAT Tax Number', 'South African VAT Tax Number', 'ZA', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_other_tax_number', 'en-US', 3, 'Other Tax Number', 'Other South African Tax Number', 'ZA', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('uk_tax_number', 'en-US', 1, 'UK Tax Number', 'UK Tax Number', 'GB', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('uk_other_tax_number', 'en-US', 2, 'UK Other Tax Number', 'UK Other Tax Number', 'GB', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('us_taxpayer_id_number', 'en-US', 1, 'US Taxpayer Identification Number', 'US Taxpayer Identification Number', 'US', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('us_other_tax_number', 'en-US', 2, 'US Other Tax Number', 'US Other Tax Number', 'US', 'organization,person');

INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_income_tax_number', 'en-ZA', 1, 'Income Tax Number', 'South African Income Tax Reference Number', 'ZA', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_vat_tax_number', 'en-ZA', 2, 'VAT Tax Number', 'South African VAT Tax Number', 'ZA', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_other_tax_number', 'en-ZA', 3, 'Other Tax Number', 'Other South African Tax Number', 'ZA', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('uk_tax_number', 'en-ZA', 1, 'UK Tax Number', 'UK Tax Number', 'GB', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('uk_other_tax_number', 'en-ZA', 2, 'UK Other Tax Number', 'UK Other Tax Number', 'GB', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('us_taxpayer_id_number', 'en-ZA', 1, 'US Taxpayer Identification Number', 'US Taxpayer Identification Number', 'US', 'organization,person');
INSERT INTO party.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('us_other_tax_number', 'en-ZA', 2, 'US Other Tax Number', 'US Other Tax Number', 'US', 'organization,person');


INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('anytime', 'en-US', 1, 'Anytime', 'Anytime');
INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('long_hours', 'en-US', 2, 'Long Hours', 'Long Hours');
INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('office_hours', 'en-US', 3, 'Office Hours', 'Office Hours');
INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('do_not_contact', 'en-US', 4, 'Do Not Contact', 'Do Not Contact');
INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('anytime', 'en-ZA', 1, 'Anytime', 'Anytime');
INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('long_hours', 'en-ZA', 2, 'Long Hours', 'Long Hours');
INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('office_hours', 'en-ZA', 3, 'Office Hours', 'Office Hours');
INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('do_not_contact', 'en-ZA', 4, 'Do Not Contact', 'Do Not Contact');
INSERT INTO party.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mr', 'en-US', 1, 'Mr', 'Mr.', 'Mr');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('ms', 'en-US', 2, 'Ms', 'Ms.', 'Ms');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('miss', 'en-US', 3, 'Miss', 'Miss', 'Miss');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mrs', 'en-US', 4, 'Mrs', 'Mrs.', 'Mrs');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('advocate', 'en-US', 'Advocate', 'Adv.', 'Advocate');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('doctor', 'en-US', 'Doctor', 'Dr.', 'Doctor');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('father', 'en-US', 'Father', 'Fr.', 'Father');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('professor', 'en-US', 'Professor', 'Prof.', 'Professor');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('rabbi', 'en-US', 'Rabbi', 'Rabbi', 'Rabbi');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('reverend', 'en-US', 'Reverend', 'Rev.', 'Reverend');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('the_honorable', 'en-US', 'The Honorable', 'The Hon.', 'The Honorable');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('unknown', 'en-US', 'Unknown', 'Unknown', 'Unknown');

INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mr', 'en-ZA', 1, 'Mr', 'Mr.', 'Mr');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('ms', 'en-ZA', 2, 'Ms', 'Ms.', 'Ms');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('miss', 'en-ZA', 3, 'Miss', 'Miss', 'Miss');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mrs', 'en-ZA', 4, 'Mrs', 'Mrs.', 'Mrs');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('advocate', 'en-ZA', 'Advocate', 'Adv.', 'Advocate');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('doctor', 'en-ZA', 'Doctor', 'Dr.', 'Doctor');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('father', 'en-ZA', 'Father', 'Fr.', 'Father');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('professor', 'en-ZA', 'Professor', 'Prof.', 'Professor');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('rabbi', 'en-ZA', 'Rabbi', 'Rabbi', 'Rabbi');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('reverend', 'en-ZA', 'Reverend', 'Rev.', 'Reverend');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('the_honorable', 'en-ZA', 'The Honorable', 'The Hon.', 'The Honorable');
INSERT INTO party.titles (code, locale_id, name, abbreviation, description)
  VALUES ('unknown', 'en-ZA', 'Unknown', 'Unknown', 'Unknown');

