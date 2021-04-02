-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA party;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE party.attribute_type_categories (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX attribute_type_categories_locale_id_ix ON party.attribute_type_categories(locale_id);

COMMENT ON COLUMN party.attribute_type_categories.code IS 'The code for the attribute type category';

COMMENT ON COLUMN party.attribute_type_categories.locale_id IS 'The Unicode locale identifier for the attribute type category';

COMMENT ON COLUMN party.attribute_type_categories.sort_index IS 'The sort index for the attribute type category';

COMMENT ON COLUMN party.attribute_type_categories.name IS 'The name of the attribute type category';

COMMENT ON COLUMN party.attribute_type_categories.description IS 'The description for the attribute type category';


CREATE TABLE party.attribute_types (
  category    VARCHAR(30)  NOT NULL,
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',
  party_types VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT attribute_types_attribute_type_category_fk FOREIGN KEY (category, locale_id) REFERENCES party.attribute_type_categories(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX attribute_types_category_ix ON party.attribute_types(category);

CREATE INDEX attribute_types_locale_id_ix ON party.attribute_types(locale_id);

COMMENT ON COLUMN party.attribute_types.category IS 'The code for the attribute type category the attribute type is associated with';

COMMENT ON COLUMN party.attribute_types.code IS 'The code for the attribute type';

COMMENT ON COLUMN party.attribute_types.locale_id IS 'The Unicode locale identifier for the attribute type';

COMMENT ON COLUMN party.attribute_types.sort_index IS 'The sort index for the attribute type';

COMMENT ON COLUMN party.attribute_types.name IS 'The name of the attribute type';

COMMENT ON COLUMN party.attribute_types.description IS 'The description for the attribute type';

COMMENT ON COLUMN party.attribute_types.party_types IS 'The comma-delimited list of codes for the party types the attribute type is associated with';


CREATE TABLE party.consent_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX consent_types_locale_id_ix ON party.consent_types(locale_id);

COMMENT ON COLUMN party.consent_types.code IS 'The code for the consent type';

COMMENT ON COLUMN party.consent_types.locale_id IS 'The Unicode locale identifier for the consent type';

COMMENT ON COLUMN party.consent_types.sort_index IS 'The sort index for the consent type';

COMMENT ON COLUMN party.consent_types.name IS 'The name of the consent type';

COMMENT ON COLUMN party.consent_types.description IS 'The description for the consent type';


CREATE TABLE party.contact_mechanism_purposes (
  code                    VARCHAR(30)  NOT NULL,
  contact_mechanism_types VARCHAR(310) NOT NULL,
  locale_id               VARCHAR(10)  NOT NULL,
  sort_index              INTEGER      NOT NULL,
  name                    VARCHAR(50)  NOT NULL,
  description             VARCHAR(200) NOT NULL DEFAULT '',
  party_types             VARCHAR(310) NOT NULL,

 PRIMARY KEY (code, locale_id)
);

CREATE INDEX contact_mechanism_purposes_locale_id_ix ON party.contact_mechanism_purposes(locale_id);

COMMENT ON COLUMN party.contact_mechanism_purposes.code IS 'The code for the contact mechanism purpose';

COMMENT ON COLUMN party.contact_mechanism_purposes.contact_mechanism_types IS 'The comma-delimited list of codes for the contact mechanism types the contact mechanism purpose is associated with';

COMMENT ON COLUMN party.contact_mechanism_purposes.locale_id IS 'The Unicode locale identifier for the contact mechanism purpose';

COMMENT ON COLUMN party.contact_mechanism_purposes.sort_index IS 'The sort index for the contact mechanism purpose';

COMMENT ON COLUMN party.contact_mechanism_purposes.name IS 'The name of the contact mechanism purpose';

COMMENT ON COLUMN party.contact_mechanism_purposes.description IS 'The description for the contact mechanism purpose';

COMMENT ON COLUMN party.contact_mechanism_purposes.party_types IS 'The comma-delimited list of codes for the party types the contact mechanism purpose is associated with';


CREATE TABLE party.contact_mechanism_types (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  plural       VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX contact_mechanism_types_locale_id_ix ON party.contact_mechanism_types(locale_id);

COMMENT ON COLUMN party.contact_mechanism_types.code IS 'The code for the contact mechanism type';

COMMENT ON COLUMN party.contact_mechanism_types.locale_id IS 'The Unicode locale identifier for the contact mechanism type';

COMMENT ON COLUMN party.contact_mechanism_types.sort_index IS 'The sort index for the contact mechanism type';

COMMENT ON COLUMN party.contact_mechanism_types.name IS 'The name of the contact mechanism type';

COMMENT ON COLUMN party.contact_mechanism_types.plural IS 'The plural name for the contact mechanism type';

COMMENT ON COLUMN party.contact_mechanism_types.description IS 'The description for the contact mechanism type';


CREATE TABLE party.contact_mechanism_roles (
  contact_mechanism_type VARCHAR(30)  NOT NULL,
  code                   VARCHAR(30)  NOT NULL,
  locale_id              VARCHAR(10)  NOT NULL,
  sort_index             INTEGER      NOT NULL,
  name                   VARCHAR(50)  NOT NULL,
  description            VARCHAR(200) NOT NULL DEFAULT '',
  party_types            VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT contact_mechanism_roles_contact_mechanism_type_fk FOREIGN KEY (contact_mechanism_type, locale_id) REFERENCES party.contact_mechanism_types(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX contact_mechanism_roles_contact_mechanism_type_ix ON party.contact_mechanism_roles(contact_mechanism_type);

CREATE INDEX contact_mechanism_roles_locale_id_ix ON party.contact_mechanism_roles(locale_id);

CREATE UNIQUE INDEX contact_mechanism_roles_locale_id_code_ix ON party.contact_mechanism_roles(locale_id, code);

COMMENT ON COLUMN party.contact_mechanism_roles.contact_mechanism_type IS 'The code for the contact mechanism type the contact mechanism role is associated with';

COMMENT ON COLUMN party.contact_mechanism_roles.code IS 'The code for the contact mechanism role';

COMMENT ON COLUMN party.contact_mechanism_roles.locale_id IS 'The Unicode locale identifier for the contact mechanism role';

COMMENT ON COLUMN party.contact_mechanism_roles.sort_index IS 'The sort index for the contact mechanism role';

COMMENT ON COLUMN party.contact_mechanism_roles.name IS 'The name of the contact mechanism role';

COMMENT ON COLUMN party.contact_mechanism_roles.description IS 'The description for the contact mechanism role';

COMMENT ON COLUMN party.contact_mechanism_roles.party_types IS 'The comma-delimited list of codes for the party types the contact mechanism role is associated with';


CREATE TABLE party.employment_statuses (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX employment_statuses_locale_id_ix ON party.employment_statuses(locale_id);

COMMENT ON COLUMN party.employment_statuses.code IS 'The code for the employment status';

COMMENT ON COLUMN party.employment_statuses.locale_id IS 'The Unicode locale identifier for the employment status';

COMMENT ON COLUMN party.employment_statuses.sort_index IS 'The sort index for the employment status';

COMMENT ON COLUMN party.employment_statuses.name IS 'The name of the employment status';

COMMENT ON COLUMN party.employment_statuses.description IS 'The description for the employment status';


CREATE TABLE party.employment_types (
  employment_status VARCHAR(30)  NOT NULL,
  code              VARCHAR(30)  NOT NULL,
  locale_id         VARCHAR(10)  NOT NULL,
  sort_index        INTEGER      NOT NULL,
  name              VARCHAR(50)  NOT NULL,
  description       VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (employment_status, code, locale_id),
  CONSTRAINT employment_types_employment_status_fk FOREIGN KEY (employment_status, locale_id) REFERENCES party.employment_statuses(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX employment_types_employment_status_ix ON party.employment_types(employment_status);

CREATE INDEX employment_types_locale_id_ix ON party.employment_types(locale_id);

COMMENT ON COLUMN party.employment_types.employment_status IS 'The code for the employment status the employment type is associated with';

COMMENT ON COLUMN party.employment_types.code IS 'The code for the employment type';

COMMENT ON COLUMN party.employment_types.locale_id IS 'The Unicode locale identifier for the employment type';

COMMENT ON COLUMN party.employment_types.sort_index IS 'The sort index for the employment type';

COMMENT ON COLUMN party.employment_types.name IS 'The name of the employment type';

COMMENT ON COLUMN party.employment_types.description IS 'The description for the employment type';


CREATE TABLE party.fields_of_study (
  code           VARCHAR(50)  NOT NULL,
  locale_id      VARCHAR(10)  NOT NULL,
  sort_index     INTEGER      NOT NULL,
  name           VARCHAR(50)  NOT NULL,
  description    VARCHAR(200) NOT NULL DEFAULT '',
    
  PRIMARY KEY (code, locale_id)
);

CREATE INDEX fields_of_study_locale_id_ix ON party.fields_of_study(locale_id);

COMMENT ON COLUMN party.fields_of_study.code IS 'The code for the field of study';

COMMENT ON COLUMN party.fields_of_study.locale_id IS 'The Unicode locale identifier for the field of study';

COMMENT ON COLUMN party.fields_of_study.sort_index IS 'The sort index for the field of study';

COMMENT ON COLUMN party.fields_of_study.name IS 'The name of the field of study';

COMMENT ON COLUMN party.fields_of_study.description IS 'The description for the field of study';


CREATE TABLE party.genders (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10) NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX genders_locale_id_ix ON party.genders(locale_id);

COMMENT ON COLUMN party.genders.code IS 'The code for the gender';

COMMENT ON COLUMN party.genders.locale_id IS 'The Unicode locale identifier for the gender';

COMMENT ON COLUMN party.genders.sort_index IS 'The sort index for the gender';

COMMENT ON COLUMN party.genders.name IS 'The name of the gender';

COMMENT ON COLUMN party.genders.description IS 'The description for the gender';


CREATE TABLE party.identity_document_types (
  code             VARCHAR(30)  NOT NULL,
  locale_id        VARCHAR(10)  NOT NULL,
  sort_index       INTEGER      NOT NULL,
  name             VARCHAR(50)  NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue CHAR(2),
  party_types      VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX identity_document_types_locale_id_ix ON party.identity_document_types(locale_id);

CREATE INDEX identity_document_types_country_of_issue_ix ON party.identity_document_types(country_of_issue);

COMMENT ON COLUMN party.identity_document_types.code IS 'The code for the identity document type';

COMMENT ON COLUMN party.identity_document_types.locale_id IS 'The Unicode locale identifier for the identity document type';

COMMENT ON COLUMN party.identity_document_types.sort_index IS 'The sort index for the identity document type';

COMMENT ON COLUMN party.identity_document_types.name IS 'The name of the identity document type';

COMMENT ON COLUMN party.identity_document_types.description IS 'The description for the identity document type';

COMMENT ON COLUMN party.identity_document_types.country_of_issue IS 'The ISO 3166-1 alpha-2 code for the country of issue for the identity document type';

COMMENT ON COLUMN party.identity_document_types.party_types IS 'The comma-delimited list of codes for the party types the identity document type is associated with';


CREATE TABLE party.lock_type_categories (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX lock_type_categories_locale_id_ix ON party.lock_type_categories(locale_id);

COMMENT ON COLUMN party.lock_type_categories.code IS 'The code for the lock type category';

COMMENT ON COLUMN party.lock_type_categories.locale_id IS 'The Unicode locale identifier for the lock type category';

COMMENT ON COLUMN party.lock_type_categories.sort_index IS 'The sort index for the lock type category';

COMMENT ON COLUMN party.lock_type_categories.name IS 'The name of the lock type category';

COMMENT ON COLUMN party.lock_type_categories.description IS 'The description for the lock type category';


CREATE TABLE party.lock_types (
  category    VARCHAR(30)  NOT NULL,
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',
  party_types VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT lock_types_lock_type_category_fk FOREIGN KEY (category, locale_id) REFERENCES party.lock_type_categories(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX lock_types_category_ix ON party.lock_types(category);

CREATE INDEX lock_types_locale_id_ix ON party.lock_types(locale_id);

COMMENT ON COLUMN party.lock_types.category IS 'The code for the lock type category the lock type is associated with';

COMMENT ON COLUMN party.lock_types.code IS 'The code for the lock type';

COMMENT ON COLUMN party.lock_types.locale_id IS 'The Unicode locale identifier for the lock type';

COMMENT ON COLUMN party.lock_types.sort_index IS 'The sort index for the lock type';

COMMENT ON COLUMN party.lock_types.name IS 'The name of the lock type';

COMMENT ON COLUMN party.lock_types.description IS 'The description for the lock type';

COMMENT ON COLUMN party.lock_types.party_types IS 'The comma-delimited list of codes for the party types the lock type is associated with';


CREATE TABLE party.marital_statuses (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX marital_statuses_locale_id_ix ON party.marital_statuses(locale_id);

COMMENT ON COLUMN party.marital_statuses.code IS 'The code for the marital status';

COMMENT ON COLUMN party.marital_statuses.locale_id IS 'The Unicode locale identifier for the marital status';

COMMENT ON COLUMN party.marital_statuses.sort_index IS 'The sort index for the marital status';

COMMENT ON COLUMN party.marital_statuses.name IS 'The name of the marital status';

COMMENT ON COLUMN party.marital_statuses.description IS 'The description for the marital status';


CREATE TABLE party.marriage_types (
  marital_status VARCHAR(30)  NOT NULL,
  code           VARCHAR(30)  NOT NULL,
  locale_id      VARCHAR(10)  NOT NULL,
  sort_index     INTEGER      NOT NULL,
  name           VARCHAR(50)  NOT NULL,
  description    VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (marital_status, code, locale_id),
  CONSTRAINT marriage_types_marital_status_fk FOREIGN KEY (marital_status, locale_id) REFERENCES party.marital_statuses(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX marriage_types_marital_status_ix ON party.marriage_types(marital_status);

CREATE INDEX marriage_types_locale_id_ix ON party.marriage_types(locale_id);

COMMENT ON COLUMN party.marriage_types.marital_status IS 'The code for the marital status the marriage type is associated with';

COMMENT ON COLUMN party.marriage_types.code IS 'The code for the marriage type';

COMMENT ON COLUMN party.marriage_types.locale_id IS 'The Unicode locale identifier for the marriage type';

COMMENT ON COLUMN party.marriage_types.sort_index IS 'The sort index for the marriage type';

COMMENT ON COLUMN party.marriage_types.name IS 'The name of the marriage type';

COMMENT ON COLUMN party.marriage_types.description IS 'The description for the marriage type';


CREATE TABLE party.minor_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX minor_types_locale_id_ix ON party.minor_types(locale_id);

COMMENT ON COLUMN party.minor_types.code IS 'The code for the minor type';

COMMENT ON COLUMN party.minor_types.locale_id IS 'The Unicode locale identifier for the minor type';

COMMENT ON COLUMN party.minor_types.sort_index IS 'The sort index for the minor type';

COMMENT ON COLUMN party.minor_types.name IS 'The name of the minor type';

COMMENT ON COLUMN party.minor_types.description IS 'The description for the minor type';


CREATE TABLE party.next_of_kin_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX next_of_kin_types_locale_id_ix ON party.next_of_kin_types(locale_id);

COMMENT ON COLUMN party.next_of_kin_types.code IS 'The code for the next of kin type';

COMMENT ON COLUMN party.next_of_kin_types.locale_id IS 'The Unicode locale identifier for the next of kin type';

COMMENT ON COLUMN party.next_of_kin_types.sort_index IS 'The sort index for the next of kin type';

COMMENT ON COLUMN party.next_of_kin_types.name IS 'The name of the next of kin type';

COMMENT ON COLUMN party.next_of_kin_types.description IS 'The description for the next of kin type';


CREATE TABLE party.occupations (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX occupations_locale_id_ix ON party.occupations(locale_id);

COMMENT ON COLUMN party.occupations.code IS 'The code for the occupation';

COMMENT ON COLUMN party.occupations.locale_id IS 'The Unicode locale identifier for the occupation';

COMMENT ON COLUMN party.occupations.sort_index IS 'The sort index for the occupation';

COMMENT ON COLUMN party.occupations.name IS 'The name of the occupation';

COMMENT ON COLUMN party.occupations.description IS 'The description for the occupation';


CREATE TABLE party.preference_type_categories (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX preference_type_categories_locale_id_ix ON party.preference_type_categories(locale_id);

COMMENT ON COLUMN party.preference_type_categories.code IS 'The code for the preference type category';

COMMENT ON COLUMN party.preference_type_categories.locale_id IS 'The Unicode locale identifier for the preference type category';

COMMENT ON COLUMN party.preference_type_categories.sort_index IS 'The sort index for the preference type category';

COMMENT ON COLUMN party.preference_type_categories.name IS 'The name of the preference type category';

COMMENT ON COLUMN party.preference_type_categories.description IS 'The description for the preference type category';


CREATE TABLE party.preference_types (
  category    VARCHAR(30)  NOT NULL,
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',
  party_types VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT preference_types_preference_type_category_fk FOREIGN KEY (category, locale_id) REFERENCES party.preference_type_categories(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX preference_types_category_ix ON party.preference_types(category);

CREATE INDEX preference_types_locale_id_ix ON party.preference_types(locale_id);

COMMENT ON COLUMN party.preference_types.category IS 'The code for the preference type category the preference type is associated with';

COMMENT ON COLUMN party.preference_types.code IS 'The code for the preference type';

COMMENT ON COLUMN party.preference_types.locale_id IS 'The Unicode locale identifier for the preference type';

COMMENT ON COLUMN party.preference_types.sort_index IS 'The sort index for the preference type';

COMMENT ON COLUMN party.preference_types.name IS 'The name of the preference type';

COMMENT ON COLUMN party.preference_types.description IS 'The description for the preference type';

COMMENT ON COLUMN party.preference_types.party_types IS 'The comma-delimited list of codes for the party types the preference type is associated with';


CREATE TABLE party.physical_address_types (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX physical_address_types_locale_id_ix ON party.physical_address_types(locale_id);

COMMENT ON COLUMN party.physical_address_types.code IS 'The code for the physical address type';

COMMENT ON COLUMN party.physical_address_types.locale_id IS 'The Unicode locale identifier for the physical address type';

COMMENT ON COLUMN party.physical_address_types.sort_index IS 'The sort index for the physical address type';

COMMENT ON COLUMN party.physical_address_types.name IS 'The name of the physical address type';

COMMENT ON COLUMN party.physical_address_types.description IS 'The description for the physical address type';


CREATE TABLE party.physical_address_purposes (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',
  party_types  VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX physical_address_purposes_locale_id_ix ON party.physical_address_purposes(locale_id);

COMMENT ON COLUMN party.physical_address_purposes.code IS 'The code for the physical address purpose';

COMMENT ON COLUMN party.physical_address_purposes.locale_id IS 'The Unicode locale identifier for the physical address purpose';

COMMENT ON COLUMN party.physical_address_purposes.sort_index IS 'The sort index for the physical address purpose';

COMMENT ON COLUMN party.physical_address_purposes.name IS 'The name of the physical address purpose';

COMMENT ON COLUMN party.physical_address_purposes.description IS 'The description for the physical address purpose';

COMMENT ON COLUMN party.physical_address_purposes.party_types IS 'The comma-delimited list of codes for the party types the physical address purpose is associated with';


CREATE TABLE party.physical_address_roles (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',
  party_types  VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX physical_address_roles_locale_id_ix ON party.physical_address_roles(locale_id);

COMMENT ON COLUMN party.physical_address_roles.code IS 'The code for the physical address role';

COMMENT ON COLUMN party.physical_address_roles.locale_id IS 'The Unicode locale identifier for the physical address role';

COMMENT ON COLUMN party.physical_address_roles.sort_index IS 'The sort index for the physical address role';

COMMENT ON COLUMN party.physical_address_roles.name IS 'The name of the physical address role';

COMMENT ON COLUMN party.physical_address_roles.description IS 'The description for the physical address role';

COMMENT ON COLUMN party.physical_address_roles.party_types IS 'The comma-delimited list of codes for the party types the physical address role is associated with';


CREATE TABLE party.qualification_types (
  code           VARCHAR(30)  NOT NULL,
  field_of_study VARCHAR(50),
  locale_id      VARCHAR(10)  NOT NULL,
  sort_index     INTEGER      NOT NULL,
  name           VARCHAR(50)  NOT NULL,
  description    VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id),
  CONSTRAINT qualification_types_field_of_study_fk FOREIGN KEY (field_of_study, locale_id) REFERENCES party.fields_of_study(code, locale_id)
);

CREATE INDEX qualification_types_locale_id_ix ON party.qualification_types(locale_id);

COMMENT ON COLUMN party.qualification_types.code IS 'The code for the qualification type';

COMMENT ON COLUMN party.qualification_types.field_of_study IS 'The code for the field of study for the qualification type';

COMMENT ON COLUMN party.qualification_types.locale_id IS 'The Unicode locale identifier for the qualification type';

COMMENT ON COLUMN party.qualification_types.sort_index IS 'The sort index for the qualification type';

COMMENT ON COLUMN party.qualification_types.name IS 'The name of the qualification type';

COMMENT ON COLUMN party.qualification_types.description IS 'The description for the qualification type';


CREATE TABLE party.races (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX races_locale_id_ix ON party.races(locale_id);

COMMENT ON COLUMN party.races.code IS 'The code for the race';

COMMENT ON COLUMN party.races.locale_id IS 'The Unicode locale identifier for the race';

COMMENT ON COLUMN party.races.sort_index IS 'The sort index for the race';

COMMENT ON COLUMN party.races.name IS 'The name of the race';

COMMENT ON COLUMN party.races.description IS 'The description for the race';


CREATE TABLE party.residence_permit_types (
  code             VARCHAR(30)  NOT NULL,
  locale_id        VARCHAR(10)  NOT NULL,
  sort_index       INTEGER      NOT NULL,
  name             VARCHAR(50)  NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue CHAR(2)      NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX residence_permit_types_locale_id_ix ON party.residence_permit_types(locale_id);

CREATE INDEX residence_permit_types_country_of_issue_ix ON party.residence_permit_types(country_of_issue);

COMMENT ON COLUMN party.residence_permit_types.code IS 'The code for the residence permit type';

COMMENT ON COLUMN party.residence_permit_types.locale_id IS 'The Unicode locale identifier for the residence permit type';

COMMENT ON COLUMN party.residence_permit_types.sort_index IS 'The sort index for the residence permit type';

COMMENT ON COLUMN party.residence_permit_types.name IS 'The name of the residence permit type';

COMMENT ON COLUMN party.residence_permit_types.description IS 'The description for the residence permit type';

COMMENT ON COLUMN party.residence_permit_types.country_of_issue IS 'The ISO 3166-1 alpha-2 code for the country of issue for the residence permit type';


CREATE TABLE party.residency_statuses (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX residency_statuses_locale_id_ix ON party.residency_statuses(locale_id);

COMMENT ON COLUMN party.residency_statuses.code IS 'The code for the residency status';

COMMENT ON COLUMN party.residency_statuses.locale_id IS 'The Unicode locale identifier for the residency status';

COMMENT ON COLUMN party.residency_statuses.sort_index IS 'The sort index for the residency status';

COMMENT ON COLUMN party.residency_statuses.name IS 'The name of the residency status';

COMMENT ON COLUMN party.residency_statuses.description IS 'The description for the residency status';


CREATE TABLE party.residential_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX residential_types_locale_id_ix ON party.residential_types(locale_id);

COMMENT ON COLUMN party.residential_types.code IS 'The code for the residential type';

COMMENT ON COLUMN party.residential_types.locale_id IS 'The Unicode locale identifier for the residential type';

COMMENT ON COLUMN party.residential_types.sort_index IS 'The sort index for the residential type';

COMMENT ON COLUMN party.residential_types.name IS 'The name of the residential type';

COMMENT ON COLUMN party.residential_types.description IS 'The description for the residential type';


CREATE TABLE party.role_purposes (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX role_purposes_locale_id_ix ON party.role_purposes(locale_id);

COMMENT ON COLUMN party.role_purposes.code IS 'The code for the role purpose';

COMMENT ON COLUMN party.role_purposes.locale_id IS 'The Unicode locale identifier for the role purpose';

COMMENT ON COLUMN party.role_purposes.sort_index IS 'The sort index for the role purpose';

COMMENT ON COLUMN party.role_purposes.name IS 'The name of the role purpose';

COMMENT ON COLUMN party.role_purposes.description IS 'The description for the role purpose';


CREATE TABLE party.role_types (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',
  party_types  VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX role_types_locale_id_ix ON party.role_types(locale_id);

COMMENT ON COLUMN party.role_types.code IS 'The code for the role type';

COMMENT ON COLUMN party.role_types.locale_id IS 'The Unicode locale identifier for the role type';

COMMENT ON COLUMN party.role_types.sort_index IS 'The sort index for the role type';

COMMENT ON COLUMN party.role_types.name IS 'The name of the role type';

COMMENT ON COLUMN party.role_types.description IS 'The description for the role type';

COMMENT ON COLUMN party.role_types.party_types IS 'The comma-delimited list of codes for the party types the role type is associated with';


CREATE TABLE party.role_type_attribute_type_constraints (
  role_type                VARCHAR(30) NOT NULL,
  attribute_type           VARCHAR(30) NOT NULL,
  attribute_type_qualifier VARCHAR(30) NOT NULL DEFAULT '',
  type                     VARCHAR(30) NOT NULL,
  value                    VARCHAR(1000),

  PRIMARY KEY (role_type, attribute_type, attribute_type_qualifier, type)
);

COMMENT ON COLUMN party.role_type_attribute_type_constraints.role_type IS 'The code for the role type';

COMMENT ON COLUMN party.role_type_attribute_type_constraints.attribute_type IS 'The code for the attribute type';

COMMENT ON COLUMN party.role_type_attribute_type_constraints.attribute_type_qualifier IS 'The qualifier for the attribute type';

COMMENT ON COLUMN party.role_type_attribute_type_constraints.type IS 'The constraint type';

COMMENT ON COLUMN party.role_type_attribute_type_constraints.value IS 'The value to apply when validating the attribute value';


CREATE TABLE party.role_type_preference_type_constraints (
  role_type       VARCHAR(30) NOT NULL,
  preference_type VARCHAR(30) NOT NULL,
  type            VARCHAR(30) NOT NULL,
  value           VARCHAR(1000),

  PRIMARY KEY (role_type, preference_type, type)
);

COMMENT ON COLUMN party.role_type_preference_type_constraints.role_type IS 'The code for the role type';

COMMENT ON COLUMN party.role_type_preference_type_constraints.preference_type IS 'The code for the preference type';

COMMENT ON COLUMN party.role_type_preference_type_constraints.type IS 'The constraint type';

COMMENT ON COLUMN party.role_type_preference_type_constraints.value IS 'The value to apply when validating the preference value';


CREATE TABLE party.source_of_funds_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX source_of_funds_types_locale_id_ix ON party.source_of_funds_types(locale_id);

COMMENT ON COLUMN party.source_of_funds_types.code IS 'The code for the source of funds type';

COMMENT ON COLUMN party.source_of_funds_types.locale_id IS 'The Unicode locale identifier for the source of funds type';

COMMENT ON COLUMN party.source_of_funds_types.sort_index IS 'The sort index for the source of funds type';

COMMENT ON COLUMN party.source_of_funds_types.name IS 'The name of the source of funds type';

COMMENT ON COLUMN party.source_of_funds_types.description IS 'The description for the source of funds type';


CREATE TABLE party.source_of_wealth_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX source_of_wealth_types_locale_id_ix ON party.source_of_wealth_types(locale_id);

COMMENT ON COLUMN party.source_of_wealth_types.code IS 'The code for the source of wealth type';

COMMENT ON COLUMN party.source_of_wealth_types.locale_id IS 'The Unicode locale identifier for the source of wealth type';

COMMENT ON COLUMN party.source_of_wealth_types.sort_index IS 'The sort index for the source of wealth type';

COMMENT ON COLUMN party.source_of_wealth_types.name IS 'The name of the source of wealth type';

COMMENT ON COLUMN party.source_of_wealth_types.description IS 'The description for the source of wealth type';


CREATE TABLE party.status_type_categories (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX status_type_categories_locale_id_ix ON party.status_type_categories(locale_id);

COMMENT ON COLUMN party.status_type_categories.code IS 'The code for the status type category';

COMMENT ON COLUMN party.status_type_categories.locale_id IS 'The Unicode locale identifier for the status type category';

COMMENT ON COLUMN party.status_type_categories.sort_index IS 'The sort index for the status type category';

COMMENT ON COLUMN party.status_type_categories.name IS 'The name of the status type category';

COMMENT ON COLUMN party.status_type_categories.description IS 'The description for the status type category';


CREATE TABLE party.status_types (
  category    VARCHAR(30)  NOT NULL,
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',
  party_types VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT status_types_status_type_category_fk FOREIGN KEY (category, locale_id) REFERENCES party.status_type_categories(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX status_types_category_ix ON party.status_types(category);

CREATE INDEX status_types_locale_id_ix ON party.status_types(locale_id);

COMMENT ON COLUMN party.status_types.category IS 'The code for the status type category the status type is associated with';

COMMENT ON COLUMN party.status_types.code IS 'The code for the status type';

COMMENT ON COLUMN party.status_types.locale_id IS 'The Unicode locale identifier for the status type';

COMMENT ON COLUMN party.status_types.sort_index IS 'The sort index for the status type';

COMMENT ON COLUMN party.status_types.name IS 'The name of the status type';

COMMENT ON COLUMN party.status_types.description IS 'The description for the status type';

COMMENT ON COLUMN party.status_types.party_types IS 'The comma-delimited list of codes for the party types the status type is associated with';


CREATE TABLE party.tax_number_types (
  code             VARCHAR(30)  NOT NULL,
  locale_id        VARCHAR(10)  NOT NULL,
  sort_index       INTEGER      NOT NULL,
  name             VARCHAR(50)  NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue CHAR(2)      NOT NULL,
  party_types      VARCHAR(310) NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX tax_number_types_locale_id_ix ON party.tax_number_types(locale_id);

CREATE INDEX tax_number_types_country_of_issue_ix ON party.tax_number_types(country_of_issue);

COMMENT ON COLUMN party.tax_number_types.code IS 'The code for the tax number type';

COMMENT ON COLUMN party.tax_number_types.locale_id IS 'The Unicode locale identifier for the tax number type';

COMMENT ON COLUMN party.tax_number_types.sort_index IS 'The sort index for the tax number type';

COMMENT ON COLUMN party.tax_number_types.name IS 'The name of the tax number type';

COMMENT ON COLUMN party.tax_number_types.description IS 'The description for the tax number type';

COMMENT ON COLUMN party.tax_number_types.country_of_issue IS 'The ISO 3166-1 alpha-2 code for the country of issue for the tax number type';

COMMENT ON COLUMN party.tax_number_types.party_types IS 'The comma-delimited list of codes for the party types the tax number type is associated with';


CREATE TABLE party.times_to_contact (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX times_to_contact_locale_id_ix ON party.times_to_contact(locale_id);

COMMENT ON COLUMN party.times_to_contact.code IS 'The code for the time to contact';

COMMENT ON COLUMN party.times_to_contact.locale_id IS 'The Unicode locale identifier for the time to contact';

COMMENT ON COLUMN party.times_to_contact.sort_index IS 'The sort index for the time to contact';

COMMENT ON COLUMN party.times_to_contact.name IS 'The name of the time to contact';

COMMENT ON COLUMN party.times_to_contact.description IS 'The description for the time to contact';


CREATE TABLE party.titles (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  abbreviation VARCHAR(20)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX titles_locale_id_ix ON party.titles(locale_id);

COMMENT ON COLUMN party.titles.code IS 'The code for the title';

COMMENT ON COLUMN party.titles.locale_id IS 'The Unicode locale identifier for the title';

COMMENT ON COLUMN party.titles.sort_index IS 'The sort index for the title';

COMMENT ON COLUMN party.titles.name IS 'The name of the title';

COMMENT ON COLUMN party.titles.abbreviation IS 'The abbreviation for the title';

COMMENT ON COLUMN party.titles.description IS 'The description for the title';
















CREATE TABLE party.parties (
  created   TIMESTAMP    NOT NULL,
  id        UUID         NOT NULL,
  name      VARCHAR(100) NOT NULL,
  tenant_id UUID         NOT NULL,
  type      VARCHAR(30)  NOT NULL,
  updated   TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE INDEX parties_tenant_id_ix ON party.parties(tenant_id);

COMMENT ON COLUMN party.parties.created IS 'The date and time the party was created';

COMMENT ON COLUMN party.parties.id IS 'The Universally Unique Identifier (UUID) for the party';

COMMENT ON COLUMN party.parties.name IS 'The name of the party';

COMMENT ON COLUMN party.parties.tenant_id IS 'The Universally Unique Identifier (UUID) for the tenant the party is associated with';

COMMENT ON COLUMN party.parties.type IS 'The code for the party type';

COMMENT ON COLUMN party.parties.updated IS 'The date and time the party was last updated';


CREATE TABLE party.organizations (
  countries_of_tax_residence VARCHAR(30),
  id                         UUID          NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT organizations_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

COMMENT ON COLUMN party.organizations.countries_of_tax_residence IS 'The comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for the organization';

COMMENT ON COLUMN party.organizations.id IS 'The Universally Unique Identifier (UUID) for the organization';


CREATE TABLE party.persons (
  countries_of_citizenship   VARCHAR(30),
  countries_of_tax_residence VARCHAR(30),
  country_of_birth           CHAR(2),
  country_of_residence       CHAR(2),
  date_of_birth              DATE,
  date_of_death              DATE,
  employment_status          VARCHAR(30),
  employment_type            VARCHAR(30),
  gender                     VARCHAR(30),
  given_name                 VARCHAR(100),
  highest_qualification_type VARCHAR(30),
  id                         UUID          NOT NULL,
  initials                   VARCHAR(20),
  language                   VARCHAR(30),  
  maiden_name                VARCHAR(100),
  marital_status             VARCHAR(30),
  marital_status_date        DATE,
  marriage_type              VARCHAR(30),
  middle_names               VARCHAR(100),
  occupation                 VARCHAR(30),
  preferred_name             VARCHAR(100),
  race                       VARCHAR(30),
  residency_status           VARCHAR(30),
  residential_type           VARCHAR(30),
  surname                    VARCHAR(100),
  tax_number                 VARCHAR(30),
  tax_number_type            VARCHAR(30),
  title                      VARCHAR(30),

  PRIMARY KEY (id),
  CONSTRAINT persons_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX persons_date_of_birth_ix ON party.persons(date_of_birth);

COMMENT ON COLUMN party.persons.countries_of_citizenship IS 'The comma-delimited ISO 3166-1 alpha-2 codes for the countries of citizenship for the person';

COMMENT ON COLUMN party.persons.countries_of_tax_residence IS 'The comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for the person';

COMMENT ON COLUMN party.persons.country_of_birth IS 'The code for the country of birth for the person';

COMMENT ON COLUMN party.persons.country_of_residence IS 'The code for the country of residence for the person';

COMMENT ON COLUMN party.persons.date_of_birth IS 'The date of birth for the person';

COMMENT ON COLUMN party.persons.date_of_death IS 'The date of death for the person';

COMMENT ON COLUMN party.persons.employment_status IS 'The code for the employment status for the person';

COMMENT ON COLUMN party.persons.employment_type IS 'The code for the employment type for the person';

COMMENT ON COLUMN party.persons.gender IS 'The code for the gender for the person';

COMMENT ON COLUMN party.persons.given_name IS 'The given name for the person';

COMMENT ON COLUMN party.persons.highest_qualification_type IS 'The code for the highest qualification type for the person';

COMMENT ON COLUMN party.persons.id IS 'The Universally Unique Identifier (UUID) for the person';

COMMENT ON COLUMN party.persons.initials IS 'The initials for the person';

COMMENT ON COLUMN party.persons.language IS 'The code for the language for the person';

COMMENT ON COLUMN party.persons.maiden_name IS 'The maiden name for the person';

COMMENT ON COLUMN party.persons.marital_status IS 'The code for the marital status for the person';

COMMENT ON COLUMN party.persons.marital_status_date IS 'The date for the marital status for the person';

COMMENT ON COLUMN party.persons.marriage_type IS 'The code for the marriage type for the person if the person is married';

COMMENT ON COLUMN party.persons.middle_names IS 'The middle names for the person';

COMMENT ON COLUMN party.persons.occupation IS 'The code for the occupation for the person';

COMMENT ON COLUMN party.persons.preferred_name IS 'The preferred name for the person';

COMMENT ON COLUMN party.persons.race IS 'The code for the race for the person';

COMMENT ON COLUMN party.persons.residency_status IS 'The code for the residency status for the person';

COMMENT ON COLUMN party.persons.residential_type IS 'The code for the residential type for the person';

COMMENT ON COLUMN party.persons.surname IS 'The surname for the person';

COMMENT ON COLUMN party.persons.tax_number IS 'The tax number for the person';

COMMENT ON COLUMN party.persons.tax_number_type IS 'The code for the tax number type for the person';

COMMENT ON COLUMN party.persons.title IS 'The code for the title for the person';


CREATE TABLE party.attributes (
  created       TIMESTAMP    NOT NULL,
  party_id      UUID         NOT NULL,
  type          VARCHAR(30)  NOT NULL,
  updated       TIMESTAMP,
  boolean_value DOUBLE,
  date_value    DATE,
  double_value  DOUBLE,
  integer_value INTEGER,
  string_value  VARCHAR(200),

  PRIMARY KEY (party_id, type),
  CONSTRAINT attributes_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX attributes_party_id_ix ON party.attributes(party_id);

COMMENT ON COLUMN party.attributes.created IS 'The date and time the attribute was created';

COMMENT ON COLUMN party.attributes.party_id IS 'The Universally Unique Identifier (UUID) for the party the attribute is associated with';

COMMENT ON COLUMN party.attributes.type IS 'The code for the attribute type';

COMMENT ON COLUMN party.attributes.updated IS 'The date and time the attribute was last updated';

COMMENT ON COLUMN party.attributes.boolean_value IS 'The boolean value for the attribute';

COMMENT ON COLUMN party.attributes.date_value IS 'The date value for the attribute';

COMMENT ON COLUMN party.attributes.double_value IS 'The double value for the attribute';

COMMENT ON COLUMN party.attributes.integer_value IS 'The integer value for the attribute';

COMMENT ON COLUMN party.attributes.string_value IS 'The string value for the attribute';


CREATE TABLE party.consents (
  created        TIMESTAMP   NOT NULL,
  effective_from DATE        NOT NULL,
  effective_to   DATE,
  person_id      UUID        NOT NULL,
  type           VARCHAR(30) NOT NULL,
  updated        TIMESTAMP,

  PRIMARY KEY (person_id, type, effective_from),
  CONSTRAINT consents_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX consents_person_id_ix ON party.consents(person_id);

COMMENT ON COLUMN party.consents.created IS 'The date and time the consent was created';

COMMENT ON COLUMN party.consents.effective_from IS 'The date that the consent is effective from';

COMMENT ON COLUMN party.consents.effective_to IS 'The date that the consent is effective to';

COMMENT ON COLUMN party.consents.person_id IS 'The Universally Unique Identifier (UUID) for the person the consent is associated with';

COMMENT ON COLUMN party.consents.type IS 'The code for the consent type';

COMMENT ON COLUMN party.consents.updated IS 'The date and time the consent was last updated';


CREATE TABLE party.contact_mechanisms (
  created  TIMESTAMP    NOT NULL,
  party_id UUID         NOT NULL,
  purposes              VARCHAR(310),
  role     VARCHAR(30)  NOT NULL,
  type     VARCHAR(30)  NOT NULL,
  updated  TIMESTAMP,
  value    VARCHAR(200) NOT NULL,

  PRIMARY KEY (party_id, type, role),
  CONSTRAINT contact_mechanisms_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX contact_mechanisms_party_id_ix ON party.contact_mechanisms(party_id);

COMMENT ON COLUMN party.contact_mechanisms.created IS 'The date and time the contact mechanism was created';

COMMENT ON COLUMN party.contact_mechanisms.party_id IS 'The Universally Unique Identifier (UUID) for the party the contact mechanism is associated with';

COMMENT ON COLUMN party.contact_mechanisms.purposes IS 'The comma-delimited codes for the contact mechanism purposes';

COMMENT ON COLUMN party.contact_mechanisms.role IS 'The code for the contact mechanism role';

COMMENT ON COLUMN party.contact_mechanisms.type IS 'The code for the contact mechanism type';

COMMENT ON COLUMN party.contact_mechanisms.updated IS 'The date and time the contact mechanism was last updated';

COMMENT ON COLUMN party.contact_mechanisms.value IS 'The value for the contact mechanism';


CREATE TABLE party.educations (
  created             TIMESTAMP    NOT NULL,
  field_of_study      VARCHAR(50),
  first_year_attended INTEGER,
  id                  UUID         NOT NULL,
  institution_country CHAR(2),
  institution_name    VARCHAR(100) NOT NULL,
  last_year_attended  INTEGER,
  person_id           UUID         NOT NULL,
  qualification_name  VARCHAR(100),
  qualification_type  VARCHAR(30)  NOT NULL,
  qualification_year  INTEGER      NOT NULL,
  updated             TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT educations_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX educations_person_id_ix ON party.educations(person_id);

COMMENT ON COLUMN party.educations.created IS 'The date and time the education was created';

COMMENT ON COLUMN party.educations.field_of_study IS 'The code for the field of study';

COMMENT ON COLUMN party.educations.first_year_attended IS 'The first year attended';

COMMENT ON COLUMN party.educations.id IS 'The Universally Unique Identifier (UUID) for the education';

COMMENT ON COLUMN party.educations.institution_country IS 'The ISO 3166-1 alpha-2 code for the country the educational institution is located in';

COMMENT ON COLUMN party.educations.institution_name IS 'The name of the educational institution';

COMMENT ON COLUMN party.educations.last_year_attended IS 'The last year attended';

COMMENT ON COLUMN party.educations.person_id IS 'The Universally Unique Identifier (UUID) for the person the education is associated with';

COMMENT ON COLUMN party.educations.qualification_name IS 'The name of the qualification';

COMMENT ON COLUMN party.educations.qualification_type IS 'The code for the qualification type';

COMMENT ON COLUMN party.educations.qualification_year IS 'The year the qualification was obtained';

COMMENT ON COLUMN party.educations.updated IS 'The date and time the education was last updated';


CREATE TABLE party.employments (
  created                      TIMESTAMP    NOT NULL,
  employer_address_city        VARCHAR(50),
  employer_address_country     CHAR(2),
  employer_address_line1       VARCHAR(100),
  employer_address_line2       VARCHAR(100),
  employer_address_line3       VARCHAR(100),
  employer_address_postal_code VARCHAR(30),
  employer_address_region      VARCHAR(30),
  employer_address_suburb      VARCHAR(50),
  employer_contact_person      VARCHAR(100),
  employer_email_address       VARCHAR(100),
  employer_name                VARCHAR(100) NOT NULL,
  employer_phone_number        VARCHAR(50),
  end_date                     DATE,
  id                           UUID         NOT NULL,
  occupation                   VARCHAR(30),
  person_id                    UUID         NOT NULL,
  start_date                   DATE         NOT NULL,
  type                         VARCHAR(30),
  updated                      TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT employments_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX employments_person_id_ix ON party.employments(person_id);

COMMENT ON COLUMN party.employments.created IS 'The date and time the employment was created';

COMMENT ON COLUMN party.employments.employer_address_line1 IS 'The employer address line 1';

COMMENT ON COLUMN party.employments.employer_address_line2 IS 'The employer address line 2';

COMMENT ON COLUMN party.employments.employer_address_line3 IS 'The employer address line 3';

COMMENT ON COLUMN party.employments.employer_address_suburb IS 'The employer address suburb';

COMMENT ON COLUMN party.employments.employer_address_city IS 'The employer address city';

COMMENT ON COLUMN party.employments.employer_address_region IS 'The ISO 3166-2 subdivision code for the employer address region';

COMMENT ON COLUMN party.employments.employer_address_country IS 'The ISO 3166-1 alpha-2 code for the employer address country';

COMMENT ON COLUMN party.employments.employer_contact_person IS 'The employer contact person';

COMMENT ON COLUMN party.employments.employer_email_address IS 'The employer e-mail address';

COMMENT ON COLUMN party.employments.employer_name IS 'The employer name';

COMMENT ON COLUMN party.employments.employer_phone_number IS 'The employer phone number';

COMMENT ON COLUMN party.employments.end_date IS 'The end date for the employment';

COMMENT ON COLUMN party.employments.id IS 'The Universally Unique Identifier (UUID) for the employment';

COMMENT ON COLUMN party.employments.occupation IS 'The code for the occupation for the employment';

COMMENT ON COLUMN party.employments.person_id IS 'The Universally Unique Identifier (UUID) for the person the employment is associated with';

COMMENT ON COLUMN party.employments.start_date IS 'The start date for the employment';

COMMENT ON COLUMN party.employments.type IS 'The code for the employment type for the employment';

COMMENT ON COLUMN party.employments.updated IS 'The date and time the employment was last updated';


CREATE TABLE party.identity_documents (
  country_of_issue VARCHAR(2) NOT NULL,
  created          TIMESTAMP   NOT NULL,
  date_of_expiry   DATE,
  date_of_issue    DATE        NOT NULL,
  date_provided    DATE,
  id               UUID        NOT NULL,
  number           VARCHAR(30) NOT NULL,
  party_id         UUID        NOT NULL,
  type             VARCHAR(30) NOT NULL,
  updated          TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT identity_documents_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX identity_documents_party_id_ix ON party.identity_documents(party_id);

COMMENT ON COLUMN party.identity_documents.country_of_issue IS 'The ISO 3166-1 alpha-2 code for the country of issue for the identity document';

COMMENT ON COLUMN party.identity_documents.created IS 'The date and time the identity document was created';

COMMENT ON COLUMN party.identity_documents.date_of_expiry IS 'The date of expiry for the identity document';

COMMENT ON COLUMN party.identity_documents.date_of_issue IS 'The date of issue for the identity document';

COMMENT ON COLUMN party.identity_documents.date_provided IS 'The date the identity document was provided';

COMMENT ON COLUMN party.identity_documents.id IS 'The Universally Unique Identifier (UUID) for the identity document';

COMMENT ON COLUMN party.identity_documents.number IS 'The number for the identity document';

COMMENT ON COLUMN party.identity_documents.party_id IS 'The Universally Unique Identifier (UUID) for the party the identity document is associated with';

COMMENT ON COLUMN party.identity_documents.type IS 'The code for the identity document type';

COMMENT ON COLUMN party.identity_documents.updated IS 'The date and time the identity document was last updated';


CREATE TABLE party.language_proficiencies (
  created      TIMESTAMP   NOT NULL,
  person_id    UUID        NOT NULL,
  language     VARCHAR(30) NOT NULL,
  listen_level VARCHAR(15) NOT NULL,
  read_level   VARCHAR(15) NOT NULL,
  speak_level  VARCHAR(15) NOT NULL,
  updated      TIMESTAMP,
  write_level  VARCHAR(15) NOT NULL,

  PRIMARY KEY (person_id, language),
  CONSTRAINT language_proficiencies_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX language_proficiencies_person_id_ix ON party.language_proficiencies(person_id);

COMMENT ON COLUMN party.language_proficiencies.created IS 'The date and time the language proficiency was created';

COMMENT ON COLUMN party.language_proficiencies.person_id IS 'The Universally Unique Identifier (UUID) for the person the language proficiency is associated with';

COMMENT ON COLUMN party.language_proficiencies.language IS 'The ISO 639-1 alpha-2 code for the language';

COMMENT ON COLUMN party.language_proficiencies.listen_level IS 'The listen proficiency level for the language';

COMMENT ON COLUMN party.language_proficiencies.read_level IS 'The read proficiency level for the language';

COMMENT ON COLUMN party.language_proficiencies.speak_level IS 'The speak proficiency level for the language';

COMMENT ON COLUMN party.language_proficiencies.updated IS 'The date and time the language proficiency was last updated';

COMMENT ON COLUMN party.language_proficiencies.write_level IS 'The write proficiency level for the language';


CREATE TABLE party.locks (
  created        TIMESTAMP   NOT NULL,
  effective_from DATE        NOT NULL,
  effective_to   DATE,
  party_id       UUID        NOT NULL,
  type           VARCHAR(30) NOT NULL,
  updated        TIMESTAMP,

  PRIMARY KEY (party_id, type, effective_from),
  CONSTRAINT locks_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX locks_party_id_ix ON party.locks(party_id);

COMMENT ON COLUMN party.locks.created IS 'The date and time the lock was created';

COMMENT ON COLUMN party.locks.effective_from IS 'The date that the lock is effective from';

COMMENT ON COLUMN party.locks.effective_to IS 'The date that the lock is effective to';

COMMENT ON COLUMN party.locks.party_id IS 'The Universally Unique Identifier (UUID) for the party the lock is associated with';

COMMENT ON COLUMN party.locks.type IS 'The code for the lock type';

COMMENT ON COLUMN party.locks.updated IS 'The date and time the lock was last updated';


CREATE TABLE party.physical_addresses (
  building_floor      VARCHAR(20),
  building_name       VARCHAR(50),
  building_room       VARCHAR(30),
  city                VARCHAR(50),
  complex_name        VARCHAR(50),
  complex_unit_number VARCHAR(20),
  country             VARCHAR(2)    NOT NULL,
  created             TIMESTAMP     NOT NULL,
  farm_description    VARCHAR(50),
  farm_name           VARCHAR(50),
  farm_number         VARCHAR(50),
  id                  UUID          NOT NULL,
  latitude            VARCHAR(50),
  line1               VARCHAR(100),
  line2               VARCHAR(100),
  line3               VARCHAR(100),
  longitude           VARCHAR(50),
  party_id            UUID          NOT NULL,
  postal_code         VARCHAR(30)   NOT NULL,
  purposes            VARCHAR(310),
  region              VARCHAR(3),
  role                VARCHAR(30)   NOT NULL,
  site_block          VARCHAR(50),
  site_number         VARCHAR(50),
  street_name         VARCHAR(100),
  street_number       VARCHAR(30),
  suburb              VARCHAR(50),
  type                VARCHAR(30)   NOT NULL,
  updated             TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT physical_addresses_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX physical_addresses_party_id_ix ON party.physical_addresses(party_id);

COMMENT ON COLUMN party.physical_addresses.building_floor IS 'The building floor for the physical address';

COMMENT ON COLUMN party.physical_addresses.building_name IS 'The building name for the physical address that is required for a building address';

COMMENT ON COLUMN party.physical_addresses.building_room IS 'The building room for the physical address';

COMMENT ON COLUMN party.physical_addresses.city IS 'The town or city for the physical address that is required for a building, complex, postal, site or street address';

COMMENT ON COLUMN party.physical_addresses.complex_name IS 'The complex name for the physical address that is required for a complex address';

COMMENT ON COLUMN party.physical_addresses.complex_unit_number IS 'The complex unit number for the physical address that is required for a complex address';

COMMENT ON COLUMN party.physical_addresses.created IS 'The date and time the physical address was created';

COMMENT ON COLUMN party.physical_addresses.country IS 'The ISO 3166-1 alpha-2 code for the country for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_description IS 'The farm description for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_name IS 'The farm name for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_number IS 'The farm number for the physical address that is required for a farm address';

COMMENT ON COLUMN party.physical_addresses.id IS 'The Universally Unique Identifier (UUID) for the address';

COMMENT ON COLUMN party.physical_addresses.line1 IS 'The address line 1 for the physical address that is required for an international, postal or unstructured address';

COMMENT ON COLUMN party.physical_addresses.line2 IS 'The address line 2 for the physical address';

COMMENT ON COLUMN party.physical_addresses.line3 IS 'The address line 3 for the physical address';

COMMENT ON COLUMN party.physical_addresses.latitude IS 'The GPS latitude for the physical address';

COMMENT ON COLUMN party.physical_addresses.longitude IS 'The GPS longitude for the physical address';

COMMENT ON COLUMN party.physical_addresses.party_id IS 'The Universally Unique Identifier (UUID) for the party the physical address is associated with';

COMMENT ON COLUMN party.physical_addresses.postal_code IS 'The postal code for the physical address';

COMMENT ON COLUMN party.physical_addresses.purposes IS 'The comma-delimited codes for the physical address purposes';

COMMENT ON COLUMN party.physical_addresses.region IS 'The ISO 3166-2 subdivision code for the region for the physical address';

COMMENT ON COLUMN party.physical_addresses.role IS 'The code for the physical address role';

COMMENT ON COLUMN party.physical_addresses.site_block IS 'The site block for the physical address that is required for a site address';

COMMENT ON COLUMN party.physical_addresses.site_number IS 'The site number for the physical address that is required for a site address';

COMMENT ON COLUMN party.physical_addresses.street_name IS 'The street name for the physical address that is required for a street address';

COMMENT ON COLUMN party.physical_addresses.street_number IS 'The street number for the physical address that is required for a street address';

COMMENT ON COLUMN party.physical_addresses.suburb IS 'The suburb for the physical address';

COMMENT ON COLUMN party.physical_addresses.type IS 'The code for the physical address type';

COMMENT ON COLUMN party.physical_addresses.updated IS 'The date and time the physical address was last updated';


CREATE TABLE party.preferences (
  created  TIMESTAMP    NOT NULL,
  party_id UUID         NOT NULL,
  type     VARCHAR(30)  NOT NULL,
  updated  TIMESTAMP,
  value    VARCHAR(200) NOT NULL,

  PRIMARY KEY (party_id, type),
  CONSTRAINT preferences_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX preferences_party_id_ix ON party.preferences(party_id);

COMMENT ON COLUMN party.preferences.created IS 'The date and time the preference was created';

COMMENT ON COLUMN party.preferences.party_id IS 'The Universally Unique Identifier (UUID) for the party the preference is associated with';

COMMENT ON COLUMN party.preferences.type IS 'The code for the preference type';

COMMENT ON COLUMN party.preferences.updated IS 'The date and time the preference was last updated';

COMMENT ON COLUMN party.preferences.value IS 'The value for the preference';


CREATE TABLE party.residence_permits (
  country_of_issue VARCHAR(2)  NOT NULL,
  created          TIMESTAMP   NOT NULL,
  date_of_expiry   DATE,
  date_of_issue    DATE        NOT NULL,
  id               UUID        NOT NULL,
  number           VARCHAR(30) NOT NULL,
  person_id        UUID        NOT NULL,
  type             VARCHAR(30) NOT NULL,
  updated          TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT residence_permits_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX residence_permits_person_id_ix ON party.residence_permits(person_id);

COMMENT ON COLUMN party.residence_permits.country_of_issue IS 'The ISO 3166-1 alpha-2 code for the country of issue for the residence permit';

COMMENT ON COLUMN party.residence_permits.created IS 'The date and time the residence permit was created';

COMMENT ON COLUMN party.residence_permits.date_of_expiry IS 'The date of expiry for the residence permit';

COMMENT ON COLUMN party.residence_permits.date_of_issue IS 'The date of issue for the residence permit';

COMMENT ON COLUMN party.residence_permits.id IS 'The Universally Unique Identifier (UUID) for the residence permit';

COMMENT ON COLUMN party.residence_permits.number IS 'The number for the residence permit';

COMMENT ON COLUMN party.residence_permits.person_id IS 'The Universally Unique Identifier (UUID) for the person the residence permit is associated with';

COMMENT ON COLUMN party.residence_permits.type IS 'The code for the residence permit type';

COMMENT ON COLUMN party.residence_permits.updated IS 'The date and time the residence permit was last updated';


CREATE TABLE party.roles (
  created        TIMESTAMP    NOT NULL,
  effective_from DATE         NOT NULL,
  effective_to   DATE,
  party_id       UUID         NOT NULL,
  purpose        VARCHAR(30),
  type           VARCHAR(30)  NOT NULL,
  updated        TIMESTAMP,

  PRIMARY KEY (party_id, type, effective_from),
  CONSTRAINT roles_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX roles_party_id_ix ON party.roles(party_id);

COMMENT ON COLUMN party.roles.created IS 'The date and time the role was created';

COMMENT ON COLUMN party.roles.effective_from IS 'The date that the role is effective from';

COMMENT ON COLUMN party.roles.effective_to IS 'The date that the role is effective to';

COMMENT ON COLUMN party.roles.party_id IS 'The Universally Unique Identifier (UUID) for the party the role is associated with';

COMMENT ON COLUMN party.roles.purpose IS 'The code for the role purpose';

COMMENT ON COLUMN party.roles.type IS 'The code for the role type';

COMMENT ON COLUMN party.roles.updated IS 'The date and time the role was last updated';


CREATE TABLE party.sources_of_funds (
  created        TIMESTAMP   NOT NULL,
  effective_from DATE        NOT NULL,
  effective_to   DATE,
  person_id      UUID        NOT NULL,
  percentage     INTEGER     NOT NULL,
  type           VARCHAR(30) NOT NULL,
  updated        TIMESTAMP,

  PRIMARY KEY (person_id, type, effective_from),
  CONSTRAINT sources_of_funds_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX sources_of_funds_person_id_ix ON party.sources_of_funds(person_id);

COMMENT ON COLUMN party.sources_of_funds.created IS 'The date and time the source of funds was created';

COMMENT ON COLUMN party.sources_of_funds.effective_from IS 'The date that the source of funds is effective from';

COMMENT ON COLUMN party.sources_of_funds.effective_to IS 'The date that the source of funds is effective to';

COMMENT ON COLUMN party.sources_of_funds.person_id IS 'The Universally Unique Identifier (UUID) for the person the source of funds is associated with';

COMMENT ON COLUMN party.sources_of_funds.percentage IS 'The percentage of the total of all sources of funds attributed to this source of funds';

COMMENT ON COLUMN party.sources_of_funds.type IS 'The code for the source of funds type';

COMMENT ON COLUMN party.sources_of_funds.updated IS 'The date and time the source of funds was last updated';


CREATE TABLE party.sources_of_wealth (
  created        TIMESTAMP   NOT NULL,
  effective_from DATE        NOT NULL,
  effective_to   DATE,
  person_id      UUID        NOT NULL,
  type           VARCHAR(30) NOT NULL,
  updated        TIMESTAMP,

  PRIMARY KEY (person_id, type, effective_from),
  CONSTRAINT sources_of_wealth_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX sources_of_wealth_person_id_ix ON party.sources_of_wealth(person_id);

COMMENT ON COLUMN party.sources_of_wealth.created IS 'The date and time the source of wealth was created';

COMMENT ON COLUMN party.sources_of_wealth.effective_from IS 'The date that the source of wealth is effective from';

COMMENT ON COLUMN party.sources_of_wealth.effective_to IS 'The date that the source of wealth is effective to';

COMMENT ON COLUMN party.sources_of_wealth.person_id IS 'The Universally Unique Identifier (UUID) for the person the source of wealth is associated with';

COMMENT ON COLUMN party.sources_of_wealth.type IS 'The code for the source of wealth type';

COMMENT ON COLUMN party.sources_of_wealth.updated IS 'The date and time the source of wealth was last updated';


CREATE TABLE party.statuses (
  created        TIMESTAMP   NOT NULL,
  effective_from DATE        NOT NULL,
  effective_to   DATE,
  party_id       UUID        NOT NULL,
  type           VARCHAR(30) NOT NULL,
  updated        TIMESTAMP,

  PRIMARY KEY (party_id, type, effective_from),
  CONSTRAINT statuses_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX statuses_party_id_ix ON party.statuses(party_id);

COMMENT ON COLUMN party.statuses.created IS 'The date and time the status was created';

COMMENT ON COLUMN party.statuses.effective_from IS 'The date that the status is effective from';

COMMENT ON COLUMN party.statuses.effective_to IS 'The date that the status is effective to';

COMMENT ON COLUMN party.statuses.party_id IS 'The Universally Unique Identifier (UUID) for the party the status is associated with';

COMMENT ON COLUMN party.statuses.type IS 'The code for the status type';

COMMENT ON COLUMN party.statuses.updated IS 'The date and time the status was last updated';


CREATE TABLE party.tax_numbers (
  country_of_issue VARCHAR(30) NOT NULL,
  created          TIMESTAMP   NOT NULL,
  id               UUID        NOT NULL,
  number           VARCHAR(30) NOT NULL,
  party_id         UUID        NOT NULL,
  type             VARCHAR(30) NOT NULL,
  updated          TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT tax_numbers_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX tax_numbers_party_id_ix ON party.tax_numbers(party_id);

COMMENT ON COLUMN party.tax_numbers.country_of_issue IS 'The code for the country of issue for the tax number';

COMMENT ON COLUMN party.tax_numbers.created IS 'The date and time the tax number was created';

COMMENT ON COLUMN party.tax_numbers.id IS 'The Universally Unique Identifier (UUID) for the tax number';

COMMENT ON COLUMN party.tax_numbers.number IS 'The tax number';

COMMENT ON COLUMN party.tax_numbers.party_id IS 'The Universally Unique Identifier (UUID) for the party the tax number is associated with';

COMMENT ON COLUMN party.tax_numbers.type IS 'The code for the tax number type';

COMMENT ON COLUMN party.tax_numbers.updated IS 'The date and time the tax number was last updated';







-- CREATE TABLE party.relationships (
--   created          TIMESTAMP   NOT NULL,
--   party_id         UUID        NOT NULL,
--   related_party_id UUID        NOT NULL,
--   type             VARCHAR(30) NOT NULL,
--   updated          TIMESTAMP,
--
--   PRIMARY KEY (party_id, related_party_id, purpose),
--   CONSTRAINT relationships_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE,
--   CONSTRAINT relationships_related_party_fk FOREIGN KEY (related_party_id) REFERENCES party.parties(id) ON DELETE CASCADE
-- );
--
-- CREATE INDEX relationships_party_id_ix ON party.relationships(party_id);
--
-- CREATE INDEX relationships_related_party_id_ix ON party.relationships(related_party_id);
--
-- COMMENT ON COLUMN party.relationships.created IS 'The date and time the relationship was created';
--
-- COMMENT ON COLUMN party.relationships.party_id IS 'The Universally Unique Identifier (UUID) for the party the relationship is associated with';
--
-- COMMENT ON COLUMN party.relationships.related_party_id IS 'The Universally Unique Identifier (UUID) for the party the relationship is associated with';




-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO party.attribute_type_categories (code, locale_id, sort_index, name, description)
  VALUES ('anthropometric_measurements', 'en-US', 0, 'Anthropometric Measurements', 'Anthropometric Measurements');

INSERT INTO party.attribute_type_categories (code, locale_id, sort_index, name, description)
  VALUES ('anthropometric_measurements', 'en-ZA', 1, 'Anthropometric Measurements', 'Anthropometric Measurements');


INSERT INTO party.attribute_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('anthropometric_measurements','bmi', 'en-US', 101, 'Body Mass Index', 'Body Mass Index', 'person');
INSERT INTO party.attribute_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('anthropometric_measurements','height', 'en-US', 102, 'Height', 'Height', 'person');
INSERT INTO party.attribute_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('anthropometric_measurements','weight', 'en-US', 103, 'Weight', 'Weight', 'person');

INSERT INTO party.attribute_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('anthropometric_measurements','bmi', 'en-ZA', 101, 'Body Mass Index', 'Body Mass Index', 'person');
INSERT INTO party.attribute_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('anthropometric_measurements','height', 'en-ZA', 102, 'Height', 'Height', 'person');
INSERT INTO party.attribute_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('anthropometric_measurements','weight', 'en-ZA', 103, 'Weight', 'Weight', 'person');


INSERT INTO party.consent_types(code, locale_id, sort_index, name, description)
  VALUES ('marketing', 'en-US', 1, 'Marketing Consent', 'Marketing Consent');

INSERT INTO party.consent_types(code, locale_id, sort_index, name, description)
  VALUES ('marketing', 'en-ZA', 1, 'Marketing Consent', 'Marketing Consent');


INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, sort_index, name, description, party_types)
  VALUES ('general', 'email_address', 'en-US', 1, 'General Correspondence', 'General Correspondence', 'organization,person');
INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, sort_index, name, description, party_types)
  VALUES ('marketing', 'email_address', 'en-US', 2, 'Marketing Correspondence', 'Marketing Correspondence', 'organization,person');
INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, sort_index, name, description, party_types)
  VALUES ('security', 'email_address,mobile_number', 'en-US', 3, 'Security Alerts', 'Security Alerts', 'organization,person');

INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, sort_index, name, description, party_types)
  VALUES ('general', 'email_address', 'en-ZA', 1, 'General Correspondence', 'General Correspondence', 'organization,person');
INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, sort_index, name, description, party_types)
  VALUES ('marketing', 'email_address', 'en-ZA', 2, 'Marketing Correspondence', 'Marketing Correspondence', 'organization,person');
INSERT INTO party.contact_mechanism_purposes (code, contact_mechanism_types, locale_id, sort_index, name, description, party_types)
  VALUES ('security', 'email_address,mobile_number', 'en-ZA', 3, 'Security Alerts', 'Security Alerts', 'organization,person');


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


INSERT INTO party.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('employed', 'en-US', 1, 'Employed', 'Employed');
INSERT INTO party.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-US', 2, 'Other', 'Other');
INSERT INTO party.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('employed', 'en-ZA', 1, 'Employed', 'Employed');
INSERT INTO party.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-ZA', 2, 'Other', 'Other');
INSERT INTO party.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


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


INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('accounting', 'en-US', 1, 'Accounting', 'Accounting');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('actuarial_science', 'en-US', 2, 'Actuarial Science', 'Actuarial Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('advertising', 'en-US', 3, 'Advertising', 'Advertising');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('aeronautics_aviation_science', 'en-US', 4, 'Aeronautics and Aviation Science', 'Aeronautics and Aviation Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('african_literature', 'en-US', 5, 'African Literature', 'African Literature');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('agribusiness_management', 'en-US', 6, 'Agribusiness Management', 'Agribusiness Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('agricultural_economics', 'en-US', 7, 'Agricultural Economics', 'Agricultural Economics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('anatomy', 'en-US', 8, 'Anatomy', 'Anatomy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('anthropology', 'en-US', 9, 'Anthropology', 'Anthropology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('applied_development_economics', 'en-US', 10, 'Applied Development Economics', 'Applied Development Economics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('applied_mathematics', 'en-US', 11, 'Applied Mathematics', 'Applied Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('archaeology', 'en-US', 12, 'Archaeology', 'Archaeology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('architecture', 'en-US', 13, 'Architecture', 'Architecture');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('artificial_intelligence_robotics', 'en-US', 14, 'Artificial Intelligence and Robotics', 'Artificial Intelligence and Robotics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('arts_culture_heritage_studies', 'en-US', 15, 'Arts, Culture and Heritage Studies', 'Arts, Culture and Heritage Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('arts_humanities', 'en-US', 16, 'Arts and Humanities', 'Arts and Humanities');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('astronomy', 'en-US', 17, 'Astronomy', 'Astronomy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('audiology', 'en-US', 18, 'Audiology', 'Audiology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('auditing', 'en-US', 19, 'Auditing', 'Auditing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('automotive_trades', 'en-US', 20, 'Automotive Trades', 'Automotive Trades');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('banking', 'en-US', 21, 'Banking', 'Banking');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('banking_international_finance', 'en-US', 22, 'Banking and International Finance', 'Banking and International Finance');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('biochemistry', 'en-US', 23, 'Biochemistry', 'Biochemistry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('biodiversity', 'en-US', 24, 'Biodiversity', 'Biodiversity');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('biology', 'en-US', 25, 'Biology', 'Biology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('business_management_informatics', 'en-US', 26, 'Business Management and Informatics', 'Business Management and Informatics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('brand', 'en-US', 27, 'Brand', 'Brand');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('business_commerce_management_studies', 'en-US', 28, 'Business, Commerce and Management Studies', 'Business, Commerce and Management Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('business_administration', 'en-US', 29, 'Business Administration', 'Business Administration');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('business_management', 'en-US', 30, 'Business Management', 'Business Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('cfa', 'en-US', 31, 'CFA', 'CFA');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('cfp', 'en-US', 32, 'CFP', 'CFP');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('chemical_engineering', 'en-US', 33, 'Chemical Engineering', 'Chemical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('chemistry', 'en-US', 34, 'Chemistry', 'Chemistry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('child_youth_care', 'en-US', 35, 'Child and Youth Care', 'Child and Youth Care');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('cima', 'en-US', 36, 'CIMA', 'CIMA');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('civil_engineering', 'en-US', 37, 'Civil Engineering', 'Civil Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('clinical_psychology', 'en-US', 38, 'Clinical Psychology', 'Clinical Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('commerce', 'en-US', 39, 'Commerce', 'Commerce');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('communication_studies_language', 'en-US', 40, 'Communication Studies and Language', 'Communication Studies and Language');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('compliance_management', 'en-US', 41, 'Compliance Management', 'Compliance Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('computational_applied_mathematics', 'en-US', 42, 'Computational and Applied Mathematics', 'Computational and Applied Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('computer_information_science', 'en-US', 43, 'Computer and Information Science', 'Computer and Information Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('construction_management', 'en-US', 44, 'Construction Management', 'Construction Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('consumer_behaviour', 'en-US', 45, 'Consumer Behaviour', 'Consumer Behaviour');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('cosmetology', 'en-US', 46, 'Cosmetology', 'Cosmetology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('counselling_psychology', 'en-US', 47, 'Counselling Psychology', 'Counselling Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('creative_writing', 'en-US', 48, 'Creative Writing', 'Creative Writing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('criminology', 'en-US', 49, 'Criminology', 'Criminology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('culinary_arts', 'en-US', 50, 'Culinary Arts', 'Culinary Arts');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('data_science', 'en-US', 51, 'Data Science', 'Data Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('demography', 'en-US', 52, 'Demography', 'Demography');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('dentistry', 'en-US', 53, 'Dentistry', 'Dentistry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('development_studies', 'en-US', 54, 'Development Studies', 'Development Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('dietetics_nutrition', 'en-US', 55, 'Dietetics and Nutrition', 'Dietetics and Nutrition');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('digital_arts', 'en-US', 56, 'Digital Arts', 'Digital Arts');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('diplomacy', 'en-US', 57, 'Diplomacy', 'Diplomacy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('diplomacy_international_studies', 'en-US', 58, 'Diplomacy and International Studies', 'Diplomacy and International Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('diversity_studies', 'en-US', 59, 'Diversity Studies', 'Diversity Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('drama_film', 'en-US', 60, 'Drama and Film', 'Drama and Film');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('ecology', 'en-US', 61, 'Ecology', 'Ecology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('econometrics', 'en-US', 62, 'Econometrics', 'Econometrics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('economic_management_science', 'en-US', 63, 'Economic and Management Science', 'Economic and Management Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('economics', 'en-US', 64, 'Economics', 'Economics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('education', 'en-US', 65, 'Education', 'Education');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('education_training_development', 'en-US', 66, 'Education, Training and Development', 'Education, Training and Development');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('educational_psychology', 'en-US', 67, 'Educational Psychology', 'Educational Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('electrical_engineering', 'en-US', 68, 'Electrical Engineering', 'Electrical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('entrepreneurship', 'en-US', 69, 'Entrepreneurship', 'Entrepreneurship');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('environmental_science', 'en-US', 70, 'Environmental Science', 'Environmental Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('epidemiology', 'en-US', 71, 'Epidemiology', 'Epidemiology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('escience', 'en-US', 72, 'e-Science', 'e-Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('estate_trust_management', 'en-US', 73, 'Estate and Trust Management', 'Estate and Trust Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('exercise_science', 'en-US', 74, 'Exercise Science', 'Exercise Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('fashion_design', 'en-US', 75, 'Fashion Design', 'Fashion Design');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('finance_investment_management', 'en-US', 76, 'Finance and Investment Management', 'Finance and Investment Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('financial_economics', 'en-US', 77, 'Financial Economics', 'Financial Economics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('financial_planning', 'en-US', 78, 'Financial Planning', 'Financial Planning');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('financial_sciences', 'en-US', 79, 'Financial Sciences', 'Financial Sciences');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('fine_arts', 'en-US', 80, 'Fine Arts', 'Fine Arts');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('gender_sexuality_studies', 'en-US', 81, 'Gender and Sexuality Studies', 'Gender and Sexuality Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('general', 'en-US', 82, 'General', 'General');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('genetics', 'en-US', 83, 'Genetics', 'Genetics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geochemistry', 'en-US', 84, 'Geochemistry', 'Geochemistry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geography', 'en-US', 85, 'Geography', 'Geography');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geology', 'en-US', 86, 'Geology', 'Geology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geophysics', 'en-US', 87, 'Geophysics', 'Geophysics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geosciences', 'en-US', 88, 'Geosciences', 'Geosciences');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('global_change_studies', 'en-US', 89, 'Global Change Studies', 'Global Change Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('graphic_design', 'en-US', 90, 'Graphic Design', 'Graphic Design');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('haematology', 'en-US', 91, 'Haematology', 'Haematology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('health_sciences_social_services', 'en-US', 92, 'Health Sciences and Social Services', 'Health Sciences and Social Services');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('history', 'en-US', 93, 'History', 'History');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('human_social_studies', 'en-US', 94, 'Human and Social Studies', 'Human and Social Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('humanities', 'en-US', 95, 'Humanities', 'Humanities');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('human_resource_development', 'en-US', 96, 'Human Resource Development', 'Human Resource Development');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('human_resources_management', 'en-US', 97, 'Human Resources Management', 'Human Resources Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('human_rights_advocacy', 'en-US', 98, 'Human Rights Advocacy', 'Human Rights Advocacy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('hydrogeology', 'en-US', 99, 'Hydrogeology', 'Hydrogeology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('imas', 'en-US', 100, 'IMAS', 'IMAS');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('industrial_organisational_psychology', 'en-US', 101, 'Industrial and Organisational Psychology', 'Industrial and Organisational Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('information_systems', 'en-US', 102, 'Information Systems', 'Information Systems');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('information_technology', 'en-US', 103, 'Information Technology', 'Information Technology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('insurance_risk_management', 'en-US', 104, 'Insurance and Risk Management', 'Insurance and Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('interior_design', 'en-US', 105, 'Interior Design', 'Interior Design');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('internal_auditing', 'en-US', 106, 'Internal Auditing', 'Internal Auditing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('international_finance', 'en-US', 107, 'International Finance', 'International Finance');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('international_law', 'en-US', 108, 'International Law', 'International Law');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('international_relations', 'en-US', 109, 'International Relations', 'International Relations');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('international_studies', 'en-US', 110, 'International Studies', 'International Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('investment_management', 'en-US', 111, 'Investment Management', 'Investment Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('journalism', 'en-US', 112, 'Journalism', 'Journalism');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('labour_law', 'en-US', 113, 'Labour Law', 'Labour Law');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('law', 'en-US', 114, 'Law', 'Law');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('law_military_science_security', 'en-US', 115, 'Law, Military Science and Security', 'Law, Military Science and Security');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('library_information_science', 'en-US', 116, 'Library and Information Science', 'Library and Information Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('linguistics', 'en-US', 117, 'Linguistics', 'Linguistics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('Linguistics_languages', 'en-US', 118, 'Linguistics and Languages', 'Linguistics and Languages');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('logistics', 'en-US', 119, 'Logistics', 'Logistics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('management', 'en-US', 120, 'Management', 'Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('management_accounting', 'en-US', 121, 'Management Accounting', 'Management Accounting');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('manufacturing_engineering_technology', 'en-US', 122, 'Manufacturing, Engineering and Technology', 'Manufacturing, Engineering and Technology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('marketing', 'en-US', 123, 'Marketing', 'Marketing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('mathematics', 'en-US', 124, 'Mathematics', 'Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('mechanical_engineering', 'en-US', 125, 'Mechanical Engineering', 'Mechanical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('media_studies', 'en-US', 126, 'Media Studies', 'Media Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('medical_sciences', 'en-US', 127, 'Medical Sciences', 'Medical Sciences');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('metallurgical_engineering', 'en-US', 128, 'Metallurgical Engineering', 'Metallurgical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('meteorology', 'en-US', 129, 'Meteorology', 'Meteorology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('microelectronic_engineering', 'en-US', 130, 'Microelectronic Engineering', 'Microelectronic Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('migration_displacement', 'en-US', 131, 'Migration and Displacement', 'Migration and Displacement');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('mining', 'en-US', 132, 'Mining', 'Mining');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('money_laundering_control', 'en-US', 133, 'Money Laundering Control', 'Money Laundering Control');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('multimedia', 'en-US', 134, 'Multimedia', 'Multimedia');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('music', 'en-US', 135, 'Music', 'Music');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('neuropsychology', 'en-US', 136, 'Neuropsychology', 'Neuropsychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('nursing', 'en-US', 137, 'Nursing', 'Nursing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('occupational_therapy', 'en-US', 138, 'Occupational Therapy', 'Occupational Therapy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('oceanography', 'en-US', 139, 'Oceanography', 'Oceanography');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('office_administration', 'en-US', 140, 'Office Administration', 'Office Administration');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('operations_management', 'en-US', 141, 'Operations Management', 'Operations Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('optometry', 'en-US', 142, 'Optometry', 'Optometry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('pharmacology', 'en-US', 143, 'Pharmacology', 'Pharmacology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('philosophy', 'en-US', 144, 'Philosophy', 'Philosophy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('photography', 'en-US', 145, 'Photography', 'Photography');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('physical_mathematical_computer_life_sciences', 'en-US', 146, 'Physical, Mathematical, Computer and Life Sciences', 'Physical, Mathematical, Computer and Life Sciences');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('physical_planning_construction', 'en-US', 147, 'Physical Planning and Construction', 'Physical Planning and Construction');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('physics', 'en-US', 148, 'Physics', 'Physics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('physiotherapy', 'en-US', 149, 'Physiotherapy', 'Physiotherapy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('political_studies', 'en-US', 150, 'Political Studies', 'Political Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('politics_journalism', 'en-US', 151, 'Politics and Journalism', 'Politics and Journalism');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('programme_management', 'en-US', 152, 'Programme Management', 'Programme Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('project_management', 'en-US', 153, 'Project Management', 'Project Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('psychology', 'en-US', 154, 'Psychology', 'Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('public_administration', 'en-US', 155, 'Public Administration', 'Public Administration');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('public_health', 'en-US', 156, 'Public Health', 'Public Health');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('public_management_governance', 'en-US', 157, 'Public Management and Governance', 'Public Management and Governance');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('public_relations', 'en-US', 158, 'Public Relations', 'Public Relations');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('publishing', 'en-US', 159, 'Publishing', 'Publishing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('quantity_surveying', 'en-US', 160, 'Quantity Surveying', 'Quantity Surveying');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('radiation_protection', 'en-US', 161, 'Radiation Protection', 'Radiation Protection');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('religion_theology', 'en-US', 162, 'Religion and Theology', 'Religion and Theology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('research_psychology', 'en-US', 163, 'Research Psychology', 'Research Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('resource_conservation_biology', 'en-US', 164, 'Resource Conservation Biology', 'Resource Conservation Biology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('retailing', 'en-US', 165, 'Retailing', 'Retailing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('retail_management', 'en-US', 166, 'Retail Management', 'Retail Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('risk', 'en-US', 167, 'Risk', 'Risk');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('risk_management', 'en-US', 168, 'Risk Management', 'Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('risk_management_financial_risk_management', 'en-US', 169, 'Risk Management and Financial Risk Management', 'Risk Management and Financial Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('robotics', 'en-US', 170, 'Robotics', 'Robotics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('rural_development', 'en-US', 171, 'Rural Development', 'Rural Development');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('security_management', 'en-US', 172, 'Security Management', 'Security Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('services', 'en-US', 173, 'Services', 'Services');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('social_psychology', 'en-US', 174, 'Social Psychology', 'Social Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('social_work', 'en-US', 175, 'Social Work', 'Social Work');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('sociology', 'en-US', 176, 'Sociology', 'Sociology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('speech_language_studies', 'en-US', 177, 'Speech-Language Studies', 'Speech-Language Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('sports_management', 'en-US', 178, 'Sports Management', 'Sports Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('statistics', 'en-US', 179, 'Statistics', 'Statistics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('strategic_management', 'en-US', 180, 'Strategic Management', 'Strategic Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('supply_chain_management', 'en-US', 181, 'Supply Chain Management', 'Supply Chain Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('systems_development', 'en-US', 182, 'Systems Development', 'Systems Development');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('taxation', 'en-US', 183, 'Taxation', 'Taxation');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('tourism_hospitality', 'en-US', 184, 'Tourism and Hospitality', 'Tourism and Hospitality');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('translation_interpreting', 'en-US', 185, 'Translation and Interpreting', 'Translation and Interpreting');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('transportation_supply_chain_management', 'en-US', 186, 'Transportation and Supply Chain Management', 'Transportation and Supply Chain Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('urban_planning', 'en-US', 187, 'Urban Planning', 'Urban Planning');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('visual_studies', 'en-US', 188, 'Visual Studies', 'Visual Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('wealth_management', 'en-US', 189, 'Wealth Management', 'Wealth Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('women_gender_studies', 'en-US', 190, 'Women and Gender Studies', 'Women and Gender Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('zoology', 'en-US', 191, 'Zoology', 'Zoology');

INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('accounting', 'en-ZA', 1, 'Accounting', 'Accounting');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('actuarial_science', 'en-ZA', 2, 'Actuarial Science', 'Actuarial Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('advertising', 'en-ZA', 3, 'Advertising', 'Advertising');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('aeronautics_aviation_science', 'en-ZA', 4, 'Aeronautics and Aviation Science', 'Aeronautics and Aviation Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('african_literature', 'en-ZA', 5, 'African Literature', 'African Literature');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('agribusiness_management', 'en-ZA', 6, 'Agribusiness Management', 'Agribusiness Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('agricultural_economics', 'en-ZA', 7, 'Agricultural Economics', 'Agricultural Economics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('anatomy', 'en-ZA', 8, 'Anatomy', 'Anatomy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('anthropology', 'en-ZA', 9, 'Anthropology', 'Anthropology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('applied_development_economics', 'en-ZA', 10, 'Applied Development Economics', 'Applied Development Economics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('applied_mathematics', 'en-ZA', 11, 'Applied Mathematics', 'Applied Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('archaeology', 'en-ZA', 12, 'Archaeology', 'Archaeology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('architecture', 'en-ZA', 13, 'Architecture', 'Architecture');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('artificial_intelligence_robotics', 'en-ZA', 14, 'Artificial Intelligence and Robotics', 'Artificial Intelligence and Robotics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('arts_culture_heritage_studies', 'en-ZA', 15, 'Arts, Culture and Heritage Studies', 'Arts, Culture and Heritage Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('arts_humanities', 'en-ZA', 16, 'Arts and Humanities', 'Arts and Humanities');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('astronomy', 'en-ZA', 17, 'Astronomy', 'Astronomy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('audiology', 'en-ZA', 18, 'Audiology', 'Audiology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('auditing', 'en-ZA', 19, 'Auditing', 'Auditing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('automotive_trades', 'en-ZA', 20, 'Automotive Trades', 'Automotive Trades');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('banking', 'en-ZA', 21, 'Banking', 'Banking');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('banking_international_finance', 'en-ZA', 22, 'Banking and International Finance', 'Banking and International Finance');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('biochemistry', 'en-ZA', 23, 'Biochemistry', 'Biochemistry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('biodiversity', 'en-ZA', 24, 'Biodiversity', 'Biodiversity');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('biology', 'en-ZA', 25, 'Biology', 'Biology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('business_management_informatics', 'en-ZA', 26, 'Business Management and Informatics', 'Business Management and Informatics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('brand', 'en-ZA', 27, 'Brand', 'Brand');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('business_commerce_management_studies', 'en-ZA', 28, 'Business, Commerce and Management Studies', 'Business, Commerce and Management Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('business_administration', 'en-ZA', 29, 'Business Administration', 'Business Administration');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('business_management', 'en-ZA', 30, 'Business Management', 'Business Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('cfa', 'en-ZA', 31, 'CFA', 'CFA');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('cfp', 'en-ZA', 32, 'CFP', 'CFP');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('chemical_engineering', 'en-ZA', 33, 'Chemical Engineering', 'Chemical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('chemistry', 'en-ZA', 34, 'Chemistry', 'Chemistry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('child_youth_care', 'en-ZA', 35, 'Child and Youth Care', 'Child and Youth Care');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('cima', 'en-ZA', 36, 'CIMA', 'CIMA');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('civil_engineering', 'en-ZA', 37, 'Civil Engineering', 'Civil Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('clinical_psychology', 'en-ZA', 38, 'Clinical Psychology', 'Clinical Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('commerce', 'en-ZA', 39, 'Commerce', 'Commerce');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('communication_studies_language', 'en-ZA', 40, 'Communication Studies and Language', 'Communication Studies and Language');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('compliance_management', 'en-ZA', 41, 'Compliance Management', 'Compliance Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('computational_applied_mathematics', 'en-ZA', 42, 'Computational and Applied Mathematics', 'Computational and Applied Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('computer_information_science', 'en-ZA', 43, 'Computer and Information Science', 'Computer and Information Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('construction_management', 'en-ZA', 44, 'Construction Management', 'Construction Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('consumer_behaviour', 'en-ZA', 45, 'Consumer Behaviour', 'Consumer Behaviour');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('cosmetology', 'en-ZA', 46, 'Cosmetology', 'Cosmetology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('counselling_psychology', 'en-ZA', 47, 'Counselling Psychology', 'Counselling Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('creative_writing', 'en-ZA', 48, 'Creative Writing', 'Creative Writing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('criminology', 'en-ZA', 49, 'Criminology', 'Criminology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('culinary_arts', 'en-ZA', 50, 'Culinary Arts', 'Culinary Arts');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('data_science', 'en-ZA', 51, 'Data Science', 'Data Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('demography', 'en-ZA', 52, 'Demography', 'Demography');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('dentistry', 'en-ZA', 53, 'Dentistry', 'Dentistry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('development_studies', 'en-ZA', 54, 'Development Studies', 'Development Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('dietetics_nutrition', 'en-ZA', 55, 'Dietetics and Nutrition', 'Dietetics and Nutrition');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('digital_arts', 'en-ZA', 56, 'Digital Arts', 'Digital Arts');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('diplomacy', 'en-ZA', 57, 'Diplomacy', 'Diplomacy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('diplomacy_international_studies', 'en-ZA', 58, 'Diplomacy and International Studies', 'Diplomacy and International Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('diversity_studies', 'en-ZA', 59, 'Diversity Studies', 'Diversity Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('drama_film', 'en-ZA', 60, 'Drama and Film', 'Drama and Film');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('ecology', 'en-ZA', 61, 'Ecology', 'Ecology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('econometrics', 'en-ZA', 62, 'Econometrics', 'Econometrics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('economic_management_science', 'en-ZA', 63, 'Economic and Management Science', 'Economic and Management Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('economics', 'en-ZA', 64, 'Economics', 'Economics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('education', 'en-ZA', 65, 'Education', 'Education');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('education_training_development', 'en-ZA', 66, 'Education, Training and Development', 'Education, Training and Development');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('educational_psychology', 'en-ZA', 67, 'Educational Psychology', 'Educational Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('electrical_engineering', 'en-ZA', 68, 'Electrical Engineering', 'Electrical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('entrepreneurship', 'en-ZA', 69, 'Entrepreneurship', 'Entrepreneurship');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('environmental_science', 'en-ZA', 70, 'Environmental Science', 'Environmental Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('epidemiology', 'en-ZA', 71, 'Epidemiology', 'Epidemiology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('escience', 'en-ZA', 72, 'e-Science', 'e-Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('estate_trust_management', 'en-ZA', 73, 'Estate and Trust Management', 'Estate and Trust Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('exercise_science', 'en-ZA', 74, 'Exercise Science', 'Exercise Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('fashion_design', 'en-ZA', 75, 'Fashion Design', 'Fashion Design');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('finance_investment_management', 'en-ZA', 76, 'Finance and Investment Management', 'Finance and Investment Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('financial_economics', 'en-ZA', 77, 'Financial Economics', 'Financial Economics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('financial_planning', 'en-ZA', 78, 'Financial Planning', 'Financial Planning');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('financial_sciences', 'en-ZA', 79, 'Financial Sciences', 'Financial Sciences');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('fine_arts', 'en-ZA', 80, 'Fine Arts', 'Fine Arts');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('gender_sexuality_studies', 'en-ZA', 81, 'Gender and Sexuality Studies', 'Gender and Sexuality Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('general', 'en-ZA', 82, 'General', 'General');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('genetics', 'en-ZA', 83, 'Genetics', 'Genetics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geochemistry', 'en-ZA', 84, 'Geochemistry', 'Geochemistry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geography', 'en-ZA', 85, 'Geography', 'Geography');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geology', 'en-ZA', 86, 'Geology', 'Geology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geophysics', 'en-ZA', 87, 'Geophysics', 'Geophysics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('geosciences', 'en-ZA', 88, 'Geosciences', 'Geosciences');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('global_change_studies', 'en-ZA', 89, 'Global Change Studies', 'Global Change Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('graphic_design', 'en-ZA', 90, 'Graphic Design', 'Graphic Design');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('haematology', 'en-ZA', 91, 'Haematology', 'Haematology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('health_sciences_social_services', 'en-ZA', 92, 'Health Sciences and Social Services', 'Health Sciences and Social Services');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('history', 'en-ZA', 93, 'History', 'History');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('human_social_studies', 'en-ZA', 94, 'Human and Social Studies', 'Human and Social Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('humanities', 'en-ZA', 95, 'Humanities', 'Humanities');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('human_resource_development', 'en-ZA', 96, 'Human Resource Development', 'Human Resource Development');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('human_resources_management', 'en-ZA', 97, 'Human Resources Management', 'Human Resources Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('human_rights_advocacy', 'en-ZA', 98, 'Human Rights Advocacy', 'Human Rights Advocacy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('hydrogeology', 'en-ZA', 99, 'Hydrogeology', 'Hydrogeology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('imas', 'en-ZA', 100, 'IMAS', 'IMAS');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('industrial_organisational_psychology', 'en-ZA', 101, 'Industrial and Organisational Psychology', 'Industrial and Organisational Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('information_systems', 'en-ZA', 102, 'Information Systems', 'Information Systems');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('information_technology', 'en-ZA', 103, 'Information Technology', 'Information Technology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('insurance_risk_management', 'en-ZA', 104, 'Insurance and Risk Management', 'Insurance and Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('interior_design', 'en-ZA', 105, 'Interior Design', 'Interior Design');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('internal_auditing', 'en-ZA', 106, 'Internal Auditing', 'Internal Auditing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('international_finance', 'en-ZA', 107, 'International Finance', 'International Finance');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('international_law', 'en-ZA', 108, 'International Law', 'International Law');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('international_relations', 'en-ZA', 109, 'International Relations', 'International Relations');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('international_studies', 'en-ZA', 110, 'International Studies', 'International Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('investment_management', 'en-ZA', 111, 'Investment Management', 'Investment Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('journalism', 'en-ZA', 112, 'Journalism', 'Journalism');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('labour_law', 'en-ZA', 113, 'Labour Law', 'Labour Law');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('law', 'en-ZA', 114, 'Law', 'Law');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('law_military_science_security', 'en-ZA', 115, 'Law, Military Science and Security', 'Law, Military Science and Security');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('library_information_science', 'en-ZA', 116, 'Library and Information Science', 'Library and Information Science');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('linguistics', 'en-ZA', 117, 'Linguistics', 'Linguistics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('Linguistics_languages', 'en-ZA', 118, 'Linguistics and Languages', 'Linguistics and Languages');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('logistics', 'en-ZA', 119, 'Logistics', 'Logistics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('management', 'en-ZA', 120, 'Management', 'Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('management_accounting', 'en-ZA', 121, 'Management Accounting', 'Management Accounting');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('manufacturing_engineering_technology', 'en-ZA', 122, 'Manufacturing, Engineering and Technology', 'Manufacturing, Engineering and Technology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('marketing', 'en-ZA', 123, 'Marketing', 'Marketing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('mathematics', 'en-ZA', 124, 'Mathematics', 'Mathematics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('mechanical_engineering', 'en-ZA', 125, 'Mechanical Engineering', 'Mechanical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('media_studies', 'en-ZA', 126, 'Media Studies', 'Media Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('medical_sciences', 'en-ZA', 127, 'Medical Sciences', 'Medical Sciences');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('metallurgical_engineering', 'en-ZA', 128, 'Metallurgical Engineering', 'Metallurgical Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('meteorology', 'en-ZA', 129, 'Meteorology', 'Meteorology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('microelectronic_engineering', 'en-ZA', 130, 'Microelectronic Engineering', 'Microelectronic Engineering');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('migration_displacement', 'en-ZA', 131, 'Migration and Displacement', 'Migration and Displacement');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('mining', 'en-ZA', 132, 'Mining', 'Mining');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('money_laundering_control', 'en-ZA', 133, 'Money Laundering Control', 'Money Laundering Control');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('multimedia', 'en-ZA', 134, 'Multimedia', 'Multimedia');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('music', 'en-ZA', 135, 'Music', 'Music');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('neuropsychology', 'en-ZA', 136, 'Neuropsychology', 'Neuropsychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('nursing', 'en-ZA', 137, 'Nursing', 'Nursing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('occupational_therapy', 'en-ZA', 138, 'Occupational Therapy', 'Occupational Therapy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('oceanography', 'en-ZA', 139, 'Oceanography', 'Oceanography');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('office_administration', 'en-ZA', 140, 'Office Administration', 'Office Administration');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('operations_management', 'en-ZA', 141, 'Operations Management', 'Operations Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('optometry', 'en-ZA', 142, 'Optometry', 'Optometry');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('pharmacology', 'en-ZA', 143, 'Pharmacology', 'Pharmacology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('philosophy', 'en-ZA', 144, 'Philosophy', 'Philosophy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('photography', 'en-ZA', 145, 'Photography', 'Photography');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('physical_mathematical_computer_life_sciences', 'en-ZA', 146, 'Physical, Mathematical, Computer and Life Sciences', 'Physical, Mathematical, Computer and Life Sciences');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('physical_planning_construction', 'en-ZA', 147, 'Physical Planning and Construction', 'Physical Planning and Construction');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('physics', 'en-ZA', 148, 'Physics', 'Physics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('physiotherapy', 'en-ZA', 149, 'Physiotherapy', 'Physiotherapy');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('political_studies', 'en-ZA', 150, 'Political Studies', 'Political Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('politics_journalism', 'en-ZA', 151, 'Politics and Journalism', 'Politics and Journalism');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('programme_management', 'en-ZA', 152, 'Programme Management', 'Programme Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('project_management', 'en-ZA', 153, 'Project Management', 'Project Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('psychology', 'en-ZA', 154, 'Psychology', 'Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('public_administration', 'en-ZA', 155, 'Public Administration', 'Public Administration');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('public_health', 'en-ZA', 156, 'Public Health', 'Public Health');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('public_management_governance', 'en-ZA', 157, 'Public Management and Governance', 'Public Management and Governance');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('public_relations', 'en-ZA', 158, 'Public Relations', 'Public Relations');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('publishing', 'en-ZA', 159, 'Publishing', 'Publishing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('quantity_surveying', 'en-ZA', 160, 'Quantity Surveying', 'Quantity Surveying');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('radiation_protection', 'en-ZA', 161, 'Radiation Protection', 'Radiation Protection');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('religion_theology', 'en-ZA', 162, 'Religion and Theology', 'Religion and Theology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('research_psychology', 'en-ZA', 163, 'Research Psychology', 'Research Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('resource_conservation_biology', 'en-ZA', 164, 'Resource Conservation Biology', 'Resource Conservation Biology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('retailing', 'en-ZA', 165, 'Retailing', 'Retailing');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('retail_management', 'en-ZA', 166, 'Retail Management', 'Retail Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('risk', 'en-ZA', 167, 'Risk', 'Risk');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('risk_management', 'en-ZA', 168, 'Risk Management', 'Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('risk_management_financial_risk_management', 'en-ZA', 169, 'Risk Management and Financial Risk Management', 'Risk Management and Financial Risk Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('robotics', 'en-ZA', 170, 'Robotics', 'Robotics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('rural_development', 'en-ZA', 171, 'Rural Development', 'Rural Development');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('security_management', 'en-ZA', 172, 'Security Management', 'Security Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('services', 'en-ZA', 173, 'Services', 'Services');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('social_psychology', 'en-ZA', 174, 'Social Psychology', 'Social Psychology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('social_work', 'en-ZA', 175, 'Social Work', 'Social Work');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('sociology', 'en-ZA', 176, 'Sociology', 'Sociology');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('speech_language_studies', 'en-ZA', 177, 'Speech-Language Studies', 'Speech-Language Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('sports_management', 'en-ZA', 178, 'Sports Management', 'Sports Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('statistics', 'en-ZA', 179, 'Statistics', 'Statistics');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('strategic_management', 'en-ZA', 180, 'Strategic Management', 'Strategic Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('supply_chain_management', 'en-ZA', 181, 'Supply Chain Management', 'Supply Chain Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('systems_development', 'en-ZA', 182, 'Systems Development', 'Systems Development');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('taxation', 'en-ZA', 183, 'Taxation', 'Taxation');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('tourism_hospitality', 'en-ZA', 184, 'Tourism and Hospitality', 'Tourism and Hospitality');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('translation_interpreting', 'en-ZA', 185, 'Translation and Interpreting', 'Translation and Interpreting');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('transportation_supply_chain_management', 'en-ZA', 186, 'Transportation and Supply Chain Management', 'Transportation and Supply Chain Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('urban_planning', 'en-ZA', 187, 'Urban Planning', 'Urban Planning');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('visual_studies', 'en-ZA', 188, 'Visual Studies', 'Visual Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('wealth_management', 'en-ZA', 189, 'Wealth Management', 'Wealth Management');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('women_gender_studies', 'en-ZA', 190, 'Women and Gender Studies', 'Women and Gender Studies');
INSERT INTO party.fields_of_study (code, locale_id, sort_index, name, description)
  VALUES ('zoology', 'en-ZA', 191, 'Zoology', 'Zoology');


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


INSERT INTO party.lock_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('fraud', 'en-US', 1, 'Fraud', 'Fraud');
INSERT INTO party.lock_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('kyc', 'en-US', 2, 'KYC', 'Know Your Customer');
INSERT INTO party.lock_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-US', 99, 'Test Lock Category', 'Test Lock Category');

INSERT INTO party.lock_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('fraud', 'en-ZA', 1, 'Fraud', 'Fraud');
INSERT INTO party.lock_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('kyc', 'en-ZA', 2, 'KYC', 'Know Your Customer');
INSERT INTO party.lock_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-ZA', 99, 'Test Lock Category', 'Test Lock Category');


INSERT INTO party.lock_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fraud', 'suspected_fraud', 'en-US', 101, 'Suspected Fraud', 'Suspected Fraud', 'organization,person');
INSERT INTO party.lock_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('kyc', 'kyc_unverified', 'en-US', 201, 'KYC Unverified', 'KYC Unverified', 'organization,person');
INSERT INTO party.lock_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test', 'test', 'en-US', 9901, 'Test Lock', 'Test Lock', 'organization,person');

INSERT INTO party.lock_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fraud', 'suspected_fraud', 'en-ZA', 101, 'Suspected Fraud', 'Suspected Fraud', 'organization,person');
INSERT INTO party.lock_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('kyc', 'kyc_unverified', 'en-ZA', 201, 'KYC Unverified', 'KYC Unverified', 'organization,person');
INSERT INTO party.lock_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test', 'test', 'en-ZA', 9901, 'Test Lock', 'Test Lock', 'organization,person');


INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('single', 'en-US', 1, 'Single', 'Single');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('married', 'en-US', 2, 'Married', 'Married');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('common_law', 'en-US', 3, 'Common Law', 'Common Law');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('divorced', 'en-US', 4, 'Divorced', 'Divorced');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('widowed', 'en-US', 5, 'Widowed', 'Widowed');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('single', 'en-ZA', 1, 'Single', 'Single');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('married', 'en-ZA', 2, 'Married', 'Married');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('common_law', 'en-ZA', 3, 'Common Law', 'Common Law');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('divorced', 'en-ZA', 4, 'Divorced', 'Divorced');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('widowed', 'en-ZA', 5, 'Widowed', 'Widowed');
INSERT INTO party.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'in_community_of_property', 'en-US', 1, 'In Community Of Property', 'In Community Of Property');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_without_accrual', 'en-US', 1, 'ANC Without Accrual', 'ANC Without Accrual');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_with_accrual', 'en-US', 1, 'ANC With Accrual', 'ANC With Accrual');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'in_community_of_property', 'en-ZA', 1, 'In Community Of Property', 'In Community Of Property');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_without_accrual', 'en-ZA', 1, 'ANC Without Accrual', 'ANC Without Accrual');
INSERT INTO party.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_with_accrual', 'en-ZA', 1, 'ANC With Accrual', 'ANC With Accrual');
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


INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('aunt', 'en-US', 1, 'Aunt', 'Aunt');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('brother', 'en-US', 1, 'Brother', 'Brother');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('cousin', 'en-US', 1, 'Cousin', 'Cousin');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('father', 'en-US', 1, 'Father', 'Father');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('friend', 'en-US', 1, 'Friend', 'Friend');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('grandfather', 'en-US', 1, 'Grandfather', 'Grandfather');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('grandmother', 'en-US', 1, 'Grandmother', 'Grandmother');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('husband', 'en-US', 1, 'Husband', 'Husband');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('life_partner', 'en-US', 1, 'Life Partner', 'Life Partner');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('mother', 'en-US', 1, 'Mother', 'Mother');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('nephew', 'en-US', 1, 'Nephew', 'Nephew');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('niece', 'en-US', 1, 'Niece', 'Niece');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('sister', 'en-US', 1, 'Sister', 'Sister');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('uncle', 'en-US', 1, 'Uncle', 'Uncle');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('wife', 'en-US', 1, 'Wife', 'Wife');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-US', 1, 'Other', 'Other');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('aunt', 'en-ZA', 1, 'Aunt', 'Aunt');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('brother', 'en-ZA', 1, 'Brother', 'Brother');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('cousin', 'en-ZA', 1, 'Cousin', 'Cousin');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('father', 'en-ZA', 1, 'Father', 'Father');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('friend', 'en-ZA', 1, 'Friend', 'Friend');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('grandfather', 'en-ZA', 1, 'Grandfather', 'Grandfather');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('grandmother', 'en-ZA', 1, 'Grandmother', 'Grandmother');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('husband', 'en-ZA', 1, 'Husband', 'Husband');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('life_partner', 'en-ZA', 1, 'Life Partner', 'Life Partner');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('mother', 'en-ZA', 1, 'Mother', 'Mother');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('nephew', 'en-ZA', 1, 'Nephew', 'Nephew');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('niece', 'en-ZA', 1, 'Niece', 'Niece');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('sister', 'en-ZA', 1, 'Sister', 'Sister');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('uncle', 'en-ZA', 1, 'Uncle', 'Uncle');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('wife', 'en-ZA', 1, 'Wife', 'Wife');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-ZA', 1, 'Other', 'Other');
INSERT INTO party.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('creative', 'en-US', 1, 'Creative', 'Creative');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('driver', 'en-US', 2, 'Driver', 'Driver');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('executive', 'en-US', 3, 'Executive', 'Executive');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('farmer', 'en-US', 4, 'Farmer', 'Farmer');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('government', 'en-US', 5, 'Government Official', 'Government Official');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('guard', 'en-US', 6, 'Guard', 'Guard');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('labourer', 'en-US', 7, 'Labourer', 'Labourer');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('military_or_police', 'en-US', 8, 'Military / Police', 'Military / Police');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('manager', 'en-US', 9, 'Manager', 'Manager');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('office_staff', 'en-US', 10, 'Office Staff', 'Office Staff');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('pensioner_retired', 'en-US', 11, 'Pensioner / Retired', 'Pensioner / Retired');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('plant_or_machine_operator', 'en-US', 12, 'Plant or Machine Operator or Assembler', 'Plant or Machine Operator or Assembler');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_business', 'en-US', 13, 'Professional: Business', 'Professional: Business');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_education', 'en-US', 14, 'Professional: Education', 'Professional: Education');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_engineering', 'en-US', 15, 'Professional: Engineering', 'Professional: Engineering');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_government', 'en-US', 16, 'Professional: Government', 'Professional: Government');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_legal', 'en-US', 17, 'Professional: Legal', 'Professional: Legal');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_medical', 'en-US', 18, 'Professional: Medical', 'Professional: Medical');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_scientific', 'en-US', 19, 'Professional: Scientific', 'Professional: Scientific');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_transport', 'en-US', 20, 'Professional: Transport', 'Professional: Transport');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('religious_charity', 'en-US', 21, 'Religious / Charitable', 'Religious / Charitable');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('sales', 'en-US', 22, 'Sales', 'Sales');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('semi_skilled_worker', 'en-US', 23, 'Semi-skilled Worker', 'Semi-skilled Worker');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('service', 'en-US', 24, 'Service', 'Service');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('skilled_worker', 'en-US', 25, 'Skilled Worker', 'Skilled Worker');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('technician', 'en-US', 26, 'Technician', 'Technician');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('trade_worker', 'en-US', 27, 'Trade Worker', 'Trade Worker');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('unemployed', 'en-US', 50, 'Unemployed', 'Unemployed');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('creative', 'en-ZA', 1, 'Creative', 'Creative');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('driver', 'en-ZA', 2, 'Driver', 'Driver');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('executive', 'en-ZA', 3, 'Executive', 'Executive');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('farmer', 'en-ZA', 4, 'Farmer', 'Farmer');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('government', 'en-ZA', 5, 'Government Official', 'Government Official');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('guard', 'en-ZA', 6, 'Guard', 'Guard');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('labourer', 'en-ZA', 7, 'Labourer', 'Labourer');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('military_or_police', 'en-ZA', 8, 'Military / Police', 'Military / Police');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('manager', 'en-ZA', 9, 'Manager', 'Manager');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('office_staff', 'en-ZA', 10, 'Office Staff', 'Office Staff');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('pensioner_retired', 'en-ZA', 11, 'Pensioner / Retired', 'Pensioner / Retired');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('plant_or_machine_operator', 'en-ZA', 12, 'Plant or Machine Operator or Assembler', 'Plant or Machine Operator or Assembler');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_business', 'en-ZA', 13, 'Professional: Business', 'Professional: Business');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_education', 'en-ZA', 14, 'Professional: Education', 'Professional: Education');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_engineering', 'en-ZA', 15, 'Professional: Engineering', 'Professional: Engineering');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_government', 'en-ZA', 16, 'Professional: Government', 'Professional: Government');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_legal', 'en-ZA', 17, 'Professional: Legal', 'Professional: Legal');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_medical', 'en-ZA', 18, 'Professional: Medical', 'Professional: Medical');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_scientific', 'en-ZA', 19, 'Professional: Scientific', 'Professional: Scientific');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_transport', 'en-ZA', 20, 'Professional: Transport', 'Professional: Transport');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('religious_charity', 'en-ZA', 21, 'Religious / Charitable', 'Religious / Charitable');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('sales', 'en-ZA', 22, 'Sales', 'Sales');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('semi_skilled_worker', 'en-ZA', 23, 'Semi-skilled Worker', 'Semi-skilled Worker');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('service', 'en-ZA', 24, 'Service', 'Service');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('skilled_worker', 'en-ZA', 25, 'Skilled Worker', 'Skilled Worker');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('technician', 'en-ZA', 26, 'Technician', 'Technician');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('trade_worker', 'en-ZA', 27, 'Trade Worker', 'Trade Worker');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('unemployed', 'en-ZA', 50, 'Unemployed', 'Unemployed');
INSERT INTO party.occupations (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('billing', 'en-US', 1, 'Billing', 'Billing Address', 'organization,person');
INSERT INTO party.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'en-US', 2, 'Correspondence', 'Correspondence Address', 'organization,person');
INSERT INTO party.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('delivery', 'en-US', 3, 'Delivery', 'Delivery Address', 'organization,person');

INSERT INTO party.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('billing', 'en-ZA', 1, 'Billing', 'Billing Address', 'organization,person');
INSERT INTO party.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'en-ZA', 2, 'Correspondence', 'Correspondence Address', 'organization,person');
INSERT INTO party.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('delivery', 'en-ZA', 3, 'Delivery', 'Delivery Address', 'organization,person');


INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('business', 'en-US', 1, 'Business', 'Business Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('home', 'en-US', 2, 'Home', 'Home Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('main', 'en-US', 3, 'Main', 'Main Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('permanent', 'en-US', 4, 'Permanent', 'Permanent Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('postal', 'en-US', 5, 'Postal', 'Postal Address', 'organization,person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('registered_office', 'en-US', 6, 'Registered Office', 'Registered Office Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('residential', 'en-US', 7, 'Residential', 'Residential Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('service', 'en-US', 8, 'Service', 'Service Address', 'organization,person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('sole_trader', 'en-US', 9, 'Sole Trader', 'Sole Trader Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('temporary', 'en-US', 10, 'Temporary', 'Temporary Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('work', 'en-US', 11, 'Work', 'Work Address', 'person');

INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('business', 'en-ZA', 1, 'Business', 'Business Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('home', 'en-ZA', 2, 'Home', 'Home Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('main', 'en-ZA', 3, 'Main', 'Main Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('permanent', 'en-ZA', 4, 'Permanent', 'Permanent Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('postal', 'en-ZA', 5, 'Postal', 'Postal Address', 'organization,person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('registered_office', 'en-ZA', 6, 'Registered Office', 'Registered Office Address', 'organization');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('residential', 'en-ZA', 7, 'Residential', 'Residential Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('service', 'en-ZA', 8, 'Service', 'Service Address', 'organization,person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('sole_trader', 'en-ZA', 9, 'Sole Trader', 'Sole Trader Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('temporary', 'en-ZA', 10, 'Temporary', 'Temporary Address', 'person');
INSERT INTO party.physical_address_roles (code, locale_id, sort_index, name, description, party_types)
  VALUES ('work', 'en-ZA', 11, 'Work', 'Work Address', 'person');


INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('building', 'en-US', 1, 'Building', 'Building');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('complex', 'en-US', 2, 'Complex', 'Complex');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('farm', 'en-US', 3, 'Farm', 'Farm');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('international', 'en-US', 4, 'International', 'International');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('postal', 'en-US', 5, 'Postal', 'Postal');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('site', 'en-US', 6, 'Site', 'Site');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('street', 'en-US', 7, 'Street', 'Street');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('unstructured', 'en-US', 99, 'Unstructured', 'Unstructured');

INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('building', 'en-ZA', 1, 'Building', 'Building');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('complex', 'en-ZA', 2, 'Complex', 'Complex');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('farm', 'en-ZA', 3, 'Farm', 'Farm');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('international', 'en-ZA', 4, 'International', 'International');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('postal', 'en-ZA', 5, 'Postal', 'Postal');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('site', 'en-ZA', 6, 'Site', 'Site');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('street', 'en-ZA', 7, 'Street', 'Street');
INSERT INTO party.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('unstructured', 'en-ZA', 99, 'Unstructured', 'Unstructured');


INSERT INTO party.preference_type_categories (code, locale_id, sort_index, name, description)
  VALUES ('correspondence', 'en-US', 1, 'Correspondence', 'Correspondence');
INSERT INTO party.preference_type_categories (code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-US', 99, 'Test', 'Test');

INSERT INTO party.preference_type_categories (code, locale_id, sort_index, name, description)
  VALUES ('correspondence', 'en-ZA', 1, 'Correspondence', 'Correspondence');
INSERT INTO party.preference_type_categories (code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-ZA', 99, 'Test', 'Test');


INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence','contact_person', 'en-US', 101, 'Contact Person', 'Contact Person', 'organization');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence','correspondence_language', 'en-US', 102, 'Correspondence Language', 'Correspondence Language', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'preferred_contact_mechanism', 'en-US', 103, 'Preferred Contact Mechanism', 'Preferred Contact Mechanism', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence','time_to_contact', 'en-US', 104, 'Time To Contact', 'Suitable Time To Contact', 'person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_contact_mechanism_type', 'en-US', 9901, 'Test Contact Mechanism Type', 'Test Contact Mechanism Type', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_country', 'en-US', 9902, 'Test Country', 'Test Country', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_language', 'en-US', 9903, 'Test Language', 'Test Language', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_preference', 'en-US', 9904, 'Test Preference', 'Test Preference', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_size', 'en-US', 9905, 'Test Size', 'Test Size', 'organization,person');

INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence','contact_person', 'en-ZA', 101, 'Contact Person', 'Contact Person', 'organization');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'correspondence_language', 'en-ZA', 102, 'Correspondence Language', 'Correspondence Language', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'preferred_contact_mechanism', 'en-ZA', 103, 'Preferred Contact Mechanism', 'Preferred Contact Mechanism', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'time_to_contact', 'en-ZA', 104, 'Time To Contact', 'Suitable Time To Contact', 'person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_contact_mechanism_type', 'en-ZA', 9901, 'Test Contact Mechanism Type', 'Test Contact Mechanism Type', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_country', 'en-ZA', 9902, 'Test Country', 'Test Country', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_language', 'en-ZA', 9903, 'Test Language', 'Test Language', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_preference', 'en-ZA', 9904, 'Test Preference', 'Test Preference', 'organization,person');
INSERT INTO party.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test','test_size', 'en-ZA', 9905, 'Test Size', 'Test Size', 'organization,person');


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


INSERT INTO party.role_purposes (code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-US', 1, 'Test', 'Test');

INSERT INTO party.role_purposes (code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-ZA', 1, 'Test', 'Test');


INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('employer', 'en-US', 101, 'Employer', 'Employer', 'organization');
INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('vendor', 'en-US', 102, 'Vendor', 'Vendor', 'organization');
INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('test_organization_role', 'en-US', 199, 'Test Organization Role', 'Test Organization Role', 'organization');
INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('employee', 'en-US', 201, 'Employee', 'Employee', 'person');
INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('test_person_role', 'en-US', 299, 'Test Person Role', 'Test Person Role', 'person');

INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('employer', 'en-ZA', 101, 'Employer', 'Employer', 'organization');
INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('vendor', 'en-ZA', 102, 'Vendor', 'Vendor', 'organization');
INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('test_organization_role', 'en-ZA', 199, 'Test Organization Role', 'Test Organization Role', 'organization');
INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('employee', 'en-ZA', 201, 'Employee', 'Employee', 'person');
INSERT INTO party.role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('test_person_role', 'en-ZA', 299, 'Test Person Role', 'Test Person Role', 'person');


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
  VALUES ('test_person_role', 'educations', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'employment_status', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'employment_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'employments', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'gender', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'given_name', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'highest_qualification_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'identity_documents', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'initials', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'language', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'marital_status', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'marital_status_date', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'marriage_type', 'required');
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
  VALUES ('test_person_role', 'residency_status', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'residential_type', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'sources_of_funds', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'sources_of_wealth', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'surname', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_person_role', 'tax_numbers', 'required');
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
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'identity_documents', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, type)
  VALUES ('test_organization_role', 'physical_addresses', 'required');
INSERT INTO party.role_type_attribute_type_constraints(role_type, attribute_type, attribute_type_qualifier, type)
  VALUES ('test_organization_role', 'physical_address', 'main', 'required');
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


INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('salary', 'en-US', 1, 'Salary', 'Salary');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('commission', 'en-US', 2, 'Commission', 'Commission');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('rental_income', 'en-US', 3, 'Rental Income', 'Rental Income');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('investments', 'en-US', 4, 'Investments', 'Investments');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('retirement_annuity', 'en-US', 5, 'Retirement Annuity', 'Retirement Annuity');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('social_grant', 'en-US', 6, 'Social Grant', 'Social Grant');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('inheritance', 'en-US', 7, 'Inheritance', 'Inheritance');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('maintenance', 'en-US', 8, 'Maintenance', 'Maintenance');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('pension', 'en-US', 9, 'Pension', 'Pension');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('donations', 'en-US', 10, 'Donations', 'Donations');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('allowance', 'en-US', 11, 'Allowance', 'Allowance');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('winnings', 'en-US', 12, 'Winnings', 'Winnings');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('owner_draws', 'en-US', 13, 'Owner Draws', 'Owner Draws');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('bonus_incentive', 'en-US', 14, 'Bonus/Incentive', 'Bonus/Incentive');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('bursary', 'en-US', 15, 'Bursary', 'Bursary');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('settlement', 'en-US', 16, 'Settlement', 'Settlement');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('trust', 'en-US', 17, 'Trust', 'Trust');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-US', 18, 'Other', 'Other');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('salary', 'en-ZA', 1, 'Salary', 'Salary');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('commission', 'en-ZA', 2, 'Commission', 'Commission');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('rental_income', 'en-ZA', 3, 'Rental Income', 'Rental Income');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('investments', 'en-ZA', 4, 'Investments', 'Investments');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('retirement_annuity', 'en-ZA', 5, 'Retirement Annuity', 'Retirement Annuity');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('social_grant', 'en-ZA', 6, 'Social Grant', 'Social Grant');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('inheritance', 'en-ZA', 7, 'Inheritance', 'Inheritance');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('maintenance', 'en-ZA', 8, 'Maintenance', 'Maintenance');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('pension', 'en-ZA', 9, 'Pension', 'Pension');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('donations', 'en-ZA', 10, 'Donations', 'Donations');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('allowance', 'en-ZA', 11, 'Allowance', 'Allowance');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('winnings', 'en-ZA', 12, 'Winnings', 'Winnings');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('owner_draws', 'en-ZA', 13, 'Owner Draws', 'Owner Draws');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('bonus_incentive', 'en-ZA', 14, 'Bonus/Incentive', 'Bonus/Incentive');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('bursary', 'en-ZA', 15, 'Bursary', 'Bursary');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('settlement', 'en-ZA', 16, 'Settlement', 'Settlement');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('trust', 'en-ZA', 17, 'Trust', 'Trust');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-ZA', 18, 'Other', 'Other');
INSERT INTO party.source_of_funds_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO party.source_of_wealth_types (code, locale_id, sort_index, name, description)
  VALUES ('employment', 'en-US', 1, 'Employment', 'Employment');
INSERT INTO party.source_of_wealth_types (code, locale_id, sort_index, name, description)
  VALUES ('inheritance', 'en-US', 2, 'Inheritance', 'Inheritance');
INSERT INTO party.source_of_wealth_types (code, locale_id, sort_index, name, description)
  VALUES ('investments', 'en-US', 3, 'Investments', 'Investments');

INSERT INTO party.source_of_wealth_types (code, locale_id, sort_index, name, description)
  VALUES ('employment', 'en-ZA', 1, 'Employment', 'Employment');
INSERT INTO party.source_of_wealth_types (code, locale_id, sort_index, name, description)
  VALUES ('inheritance', 'en-ZA', 2, 'Inheritance', 'Inheritance');
INSERT INTO party.source_of_wealth_types (code, locale_id, sort_index, name, description)
  VALUES ('investments', 'en-ZA', 3, 'Investments', 'Investments');


INSERT INTO party.status_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('fraud', 'en-US', 1, 'Fraud', 'Fraud');
INSERT INTO party.status_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('kyc', 'en-US', 2, 'KYC', 'Know Your Customer');
INSERT INTO party.status_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-US', 99, 'Test', 'Test');

INSERT INTO party.status_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('fraud', 'en-ZA', 1, 'Fraud', 'Fraud');
INSERT INTO party.status_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('kyc', 'en-ZA', 2, 'KYC', 'Know Your Customer');
INSERT INTO party.status_type_categories(code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-ZA', 99, 'Test', 'Test');


INSERT INTO party.status_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fraud', 'fraud_investigation', 'en-US', 101, 'Fraud Investigation', 'Fraud Investigation', 'organization,person');
INSERT INTO party.status_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('kyc', 'kyc_verified', 'en-US', 201, 'KYC Verified', 'KYC Verified', 'organization,person');
INSERT INTO party.status_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test', 'test', 'en-US', 9901, 'Test Status', 'Test Status', 'organization,person');

INSERT INTO party.status_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fraud', 'fraud_investigation', 'en-ZA', 101, 'Fraud Investigation', 'Fraud Investigation', 'organization,person');
INSERT INTO party.status_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('kyc', 'kyc_verified', 'en-ZA', 201, 'KYC Verified', 'KYC Verified', 'organization,person');
INSERT INTO party.status_types(category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('test', 'test', 'en-ZA', 9901, 'Test Status', 'Test Status', 'organization,person');


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
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('doctor', 'en-US', 5, 'Doctor', 'Dr.', 'Doctor');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('the_honorable', 'en-US', 6, 'The Honorable', 'The Hon.', 'The Honorable');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('reverend', 'en-US', 7, 'Reverend', 'Rev.', 'Reverend');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('father', 'en-US', 8, 'Father', 'Fr.', 'Father');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('rabbi', 'en-US', 9, 'Rabbi', 'Rabbi', 'Rabbi');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('professor', 'en-US', 10, 'Professor', 'Prof.', 'Professor');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('advocate', 'en-US', 11, 'Advocate', 'Adv.', 'Advocate');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown', 'Unknown');

INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mr', 'en-ZA', 1, 'Mr', 'Mr.', 'Mr');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('ms', 'en-ZA', 2, 'Ms', 'Ms.', 'Ms');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('miss', 'en-ZA', 3, 'Miss', 'Miss', 'Miss');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mrs', 'en-ZA', 4, 'Mrs', 'Mrs.', 'Mrs');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('doctor', 'en-ZA', 5, 'Doctor', 'Dr.', 'Doctor');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('the_honorable', 'en-ZA', 6, 'The Honorable', 'The Hon.', 'The Honorable');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('reverend', 'en-ZA', 7, 'Reverend', 'Rev.', 'Reverend');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('father', 'en-ZA', 8, 'Father', 'Fr.', 'Father');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('rabbi', 'en-ZA', 9, 'Rabbi', 'Rabbi', 'Rabbi');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('professor', 'en-ZA', 10, 'Professor', 'Prof.', 'Professor');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('advocate', 'en-ZA', 11, 'Advocate', 'Adv.', 'Advocate');
INSERT INTO party.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown', 'Unknown');





