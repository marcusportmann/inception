-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA reference;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE reference.contact_mechanism_types (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  plural       VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX contact_mechanism_types_locale_id_ix ON reference.contact_mechanism_types(locale_id);

COMMENT ON COLUMN reference.contact_mechanism_types.code IS 'The code for the contact mechanism type';

COMMENT ON COLUMN reference.contact_mechanism_types.locale_id IS 'The Unicode locale identifier for the contact mechanism type';

COMMENT ON COLUMN reference.contact_mechanism_types.sort_index IS 'The sort index for the contact mechanism type';

COMMENT ON COLUMN reference.contact_mechanism_types.name IS 'The name of the contact mechanism type';

COMMENT ON COLUMN reference.contact_mechanism_types.plural IS 'The plural name for the contact mechanism type';

COMMENT ON COLUMN reference.contact_mechanism_types.description IS 'The description for the contact mechanism type';


CREATE TABLE reference.contact_mechanism_purposes (
  type         VARCHAR(30)  NOT NULL,
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',
  party_types  VARCHAR(300) NOT NULL,

  PRIMARY KEY (type, code, locale_id),
  CONSTRAINT contact_mechanism_purposes_contact_mechanism_type_fk FOREIGN KEY (type, locale_id) REFERENCES reference.contact_mechanism_types(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX contact_mechanism_purposes_type_ix ON reference.contact_mechanism_purposes(type);

CREATE INDEX contact_mechanism_purposes_locale_id_ix ON reference.contact_mechanism_purposes(locale_id);

CREATE UNIQUE INDEX contact_mechanism_purposes_locale_id_code_ix ON reference.contact_mechanism_purposes(locale_id, code);

COMMENT ON COLUMN reference.contact_mechanism_purposes.type IS 'The code for the contact mechanism type the contact mechanism purpose is associated with';

COMMENT ON COLUMN reference.contact_mechanism_purposes.code IS 'The code for the contact mechanism purpose';

COMMENT ON COLUMN reference.contact_mechanism_purposes.locale_id IS 'The Unicode locale identifier for the contact mechanism purpose';

COMMENT ON COLUMN reference.contact_mechanism_purposes.sort_index IS 'The sort index for the contact mechanism purpose';

COMMENT ON COLUMN reference.contact_mechanism_purposes.name IS 'The name of the contact mechanism purpose';

COMMENT ON COLUMN reference.contact_mechanism_purposes.description IS 'The description for the contact mechanism purpose';

COMMENT ON COLUMN reference.contact_mechanism_purposes.party_types IS 'The comma-delimited list of codes for the party types the contact mechanism purpose is associated with';


CREATE TABLE reference.countries (
  code            CHAR(2)      NOT NULL,
  locale_id       VARCHAR(10)  NOT NULL,
  sort_index      INTEGER      NOT NULL,
  name            VARCHAR(50)  NOT NULL,
  short_name      VARCHAR(30)  NOT NULL,
  description     VARCHAR(200) NOT NULL DEFAULT '',
  sovereign_state CHAR(2)      NOT NULL,
  nationality     VARCHAR(50)  NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX countries_locale_id_ix ON reference.countries(locale_id);

COMMENT ON COLUMN reference.countries.code IS 'The ISO 3166-1 alpha-2 code for the country';

COMMENT ON COLUMN reference.countries.locale_id IS 'The Unicode locale identifier for the country';

COMMENT ON COLUMN reference.countries.sort_index IS 'The sort index for the country';

COMMENT ON COLUMN reference.countries.name IS 'The name of the country';

COMMENT ON COLUMN reference.countries.short_name IS 'The short name for the country';

COMMENT ON COLUMN reference.countries.description IS 'The description for the country';

COMMENT ON COLUMN reference.countries.sovereign_state IS 'The ISO 3166-1 alpha-2 code for the sovereign state the country is associated with';

COMMENT ON COLUMN reference.countries.nationality IS 'The nationality for the country';


CREATE TABLE reference.employment_statuses (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX employment_statuses_locale_id_ix ON reference.employment_statuses(locale_id);

COMMENT ON COLUMN reference.employment_statuses.code IS 'The code for the employment status';

COMMENT ON COLUMN reference.employment_statuses.locale_id IS 'The Unicode locale identifier for the employment status';

COMMENT ON COLUMN reference.employment_statuses.sort_index IS 'The sort index for the employment status';

COMMENT ON COLUMN reference.employment_statuses.name IS 'The name of the employment status';

COMMENT ON COLUMN reference.employment_statuses.description IS 'The description for the employment status';


CREATE TABLE reference.employment_types (
  employment_status VARCHAR(30)  NOT NULL,
  code              VARCHAR(30)  NOT NULL,
  locale_id         VARCHAR(10)  NOT NULL,
  sort_index        INTEGER      NOT NULL,
  name              VARCHAR(50)  NOT NULL,
  description       VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (employment_status, code, locale_id),
  CONSTRAINT employment_types_employment_status_fk FOREIGN KEY (employment_status, locale_id) REFERENCES reference.employment_statuses(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX employment_types_employment_status_ix ON reference.employment_types(employment_status);

CREATE INDEX employment_types_locale_id_ix ON reference.employment_types(locale_id);

COMMENT ON COLUMN reference.employment_types.employment_status IS 'The code for the employment status the employment type is associated with';

COMMENT ON COLUMN reference.employment_types.code IS 'The code for the employment type';

COMMENT ON COLUMN reference.employment_types.locale_id IS 'The Unicode locale identifier for the employment type';

COMMENT ON COLUMN reference.employment_types.sort_index IS 'The sort index for the employment type';

COMMENT ON COLUMN reference.employment_types.name IS 'The name of the employment type';

COMMENT ON COLUMN reference.employment_types.description IS 'The description for the employment type';


CREATE TABLE reference.genders (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10) NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX genders_locale_id_ix ON reference.genders(locale_id);

COMMENT ON COLUMN reference.genders.code IS 'The code for the gender';

COMMENT ON COLUMN reference.genders.locale_id IS 'The Unicode locale identifier for the gender';

COMMENT ON COLUMN reference.genders.sort_index IS 'The sort index for the gender';

COMMENT ON COLUMN reference.genders.name IS 'The name of the gender';

COMMENT ON COLUMN reference.genders.description IS 'The description for the gender';


CREATE TABLE reference.identity_document_types (
  code             VARCHAR(30)  NOT NULL,
  locale_id        VARCHAR(10)  NOT NULL,
  sort_index       INTEGER      NOT NULL,
  name             VARCHAR(50)  NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue CHAR(2),
  party_types      VARCHAR(300) NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT identity_document_types_country_fk FOREIGN KEY (country_of_issue, locale_id) REFERENCES reference.countries(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX identity_document_types_locale_id_ix ON reference.identity_document_types(locale_id);

CREATE INDEX identity_document_types_country_of_issue_ix ON reference.identity_document_types(country_of_issue);

COMMENT ON COLUMN reference.identity_document_types.code IS 'The code for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.locale_id IS 'The Unicode locale identifier for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.sort_index IS 'The sort index for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.name IS 'The name of the identity document type';

COMMENT ON COLUMN reference.identity_document_types.description IS 'The description for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.country_of_issue IS 'The optional ISO 3166-1 alpha-2 code for the country of issue for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.party_types IS 'The comma-delimited list of codes for the party types the identity document type is associated with';


CREATE TABLE reference.languages (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  short_name  VARCHAR(30)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX languages_locale_id_ix ON reference.languages(locale_id);

COMMENT ON COLUMN reference.languages.code IS 'The code for the language';

COMMENT ON COLUMN reference.languages.locale_id IS 'The Unicode locale identifier for the language';

COMMENT ON COLUMN reference.languages.sort_index IS 'The sort index for the language';

COMMENT ON COLUMN reference.languages.name IS 'The name of the language';

COMMENT ON COLUMN reference.languages.short_name IS 'The short name for the language';

COMMENT ON COLUMN reference.languages.description IS 'The description for the language';


CREATE TABLE reference.marital_statuses (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX marital_statuses_locale_id_ix ON reference.marital_statuses(locale_id);

COMMENT ON COLUMN reference.marital_statuses.code IS 'The code for the marital status';

COMMENT ON COLUMN reference.marital_statuses.locale_id IS 'The Unicode locale identifier for the marital status';

COMMENT ON COLUMN reference.marital_statuses.sort_index IS 'The sort index for the marital status';

COMMENT ON COLUMN reference.marital_statuses.name IS 'The name of the marital status';

COMMENT ON COLUMN reference.marital_statuses.description IS 'The description for the marital status';


CREATE TABLE reference.marriage_types (
  marital_status VARCHAR(30)  NOT NULL,
  code           VARCHAR(30)  NOT NULL,
  locale_id      VARCHAR(10)  NOT NULL,
  sort_index     INTEGER      NOT NULL,
  name           VARCHAR(50)  NOT NULL,
  description    VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (marital_status, code, locale_id),
  CONSTRAINT marriage_types_marital_status_fk FOREIGN KEY (marital_status, locale_id) REFERENCES reference.marital_statuses(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX marriage_types_marital_status_ix ON reference.marriage_types(marital_status);

CREATE INDEX marriage_types_locale_id_ix ON reference.marriage_types(locale_id);

COMMENT ON COLUMN reference.marriage_types.marital_status IS 'The code for the marital status the marriage type is associated with';

COMMENT ON COLUMN reference.marriage_types.code IS 'The code for the marriage type';

COMMENT ON COLUMN reference.marriage_types.locale_id IS 'The Unicode locale identifier for the marriage type';

COMMENT ON COLUMN reference.marriage_types.sort_index IS 'The sort index for the marriage type';

COMMENT ON COLUMN reference.marriage_types.name IS 'The name of the marriage type';

COMMENT ON COLUMN reference.marriage_types.description IS 'The description for the marriage type';


CREATE TABLE reference.minor_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX minor_types_locale_id_ix ON reference.minor_types(locale_id);

COMMENT ON COLUMN reference.minor_types.code IS 'The code for the minor type';

COMMENT ON COLUMN reference.minor_types.locale_id IS 'The Unicode locale identifier for the minor type';

COMMENT ON COLUMN reference.minor_types.sort_index IS 'The sort index for the minor type';

COMMENT ON COLUMN reference.minor_types.name IS 'The name of the minor type';

COMMENT ON COLUMN reference.minor_types.description IS 'The description for the minor type';


CREATE TABLE reference.next_of_kin_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX next_of_kin_types_locale_id_ix ON reference.next_of_kin_types(locale_id);

COMMENT ON COLUMN reference.next_of_kin_types.code IS 'The code for the next of kin type';

COMMENT ON COLUMN reference.next_of_kin_types.locale_id IS 'The Unicode locale identifier for the next of kin type';

COMMENT ON COLUMN reference.next_of_kin_types.sort_index IS 'The sort index for the next of kin type';

COMMENT ON COLUMN reference.next_of_kin_types.name IS 'The name of the next of kin type';

COMMENT ON COLUMN reference.next_of_kin_types.description IS 'The description for the next of kin type';


CREATE TABLE reference.occupations (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX occupations_locale_id_ix ON reference.occupations(locale_id);

COMMENT ON COLUMN reference.occupations.code IS 'The code for the occupation';

COMMENT ON COLUMN reference.occupations.locale_id IS 'The Unicode locale identifier for the occupation';

COMMENT ON COLUMN reference.occupations.sort_index IS 'The sort index for the occupation';

COMMENT ON COLUMN reference.occupations.name IS 'The name of the occupation';

COMMENT ON COLUMN reference.occupations.description IS 'The description for the occupation';


CREATE TABLE reference.party_role_types (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',
  party_types  VARCHAR(300) NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX party_role_types_locale_id_ix ON reference.party_role_types(locale_id);

COMMENT ON COLUMN reference.party_role_types.code IS 'The code for the party role type';

COMMENT ON COLUMN reference.party_role_types.locale_id IS 'The Unicode locale identifier for the party role type';

COMMENT ON COLUMN reference.party_role_types.sort_index IS 'The sort index for the party role type';

COMMENT ON COLUMN reference.party_role_types.name IS 'The name of the party role type';

COMMENT ON COLUMN reference.party_role_types.description IS 'The description for the party role type';

COMMENT ON COLUMN reference.party_role_types.party_types IS 'The comma-delimited list of codes for the party types the party role type is associated with';


CREATE TABLE reference.party_role_purposes (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX party_role_purposes_locale_id_ix ON reference.party_role_purposes(locale_id);

COMMENT ON COLUMN reference.party_role_purposes.code IS 'The code for the party role purpose';

COMMENT ON COLUMN reference.party_role_purposes.locale_id IS 'The Unicode locale identifier for the party role purpose';

COMMENT ON COLUMN reference.party_role_purposes.sort_index IS 'The sort index for the party role purpose';

COMMENT ON COLUMN reference.party_role_purposes.name IS 'The name of the party role purpose';

COMMENT ON COLUMN reference.party_role_purposes.description IS 'The description for the party role purpose';


CREATE TABLE reference.physical_address_types (
   code         VARCHAR(30)  NOT NULL,
   locale_id    VARCHAR(10)  NOT NULL,
   sort_index   INTEGER      NOT NULL,
   name         VARCHAR(50)  NOT NULL,
   description  VARCHAR(200) NOT NULL DEFAULT '',

   PRIMARY KEY (code, locale_id)
);

CREATE INDEX physical_address_types_locale_id_ix ON reference.physical_address_types(locale_id);

COMMENT ON COLUMN reference.physical_address_types.code IS 'The code for the physical address type';

COMMENT ON COLUMN reference.physical_address_types.locale_id IS 'The Unicode locale identifier for the physical address type';

COMMENT ON COLUMN reference.physical_address_types.sort_index IS 'The sort index for the physical address type';

COMMENT ON COLUMN reference.physical_address_types.name IS 'The name of the physical address type';

COMMENT ON COLUMN reference.physical_address_types.description IS 'The description for the physical address type';


CREATE TABLE reference.physical_address_purposes (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',
  party_types  VARCHAR(300) NOT NULL,

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX physical_address_purposes_locale_id_ix ON reference.physical_address_purposes(locale_id);

COMMENT ON COLUMN reference.physical_address_purposes.code IS 'The code for the physical address purpose';

COMMENT ON COLUMN reference.physical_address_purposes.locale_id IS 'The Unicode locale identifier for the physical address purpose';

COMMENT ON COLUMN reference.physical_address_purposes.sort_index IS 'The sort index for the physical address purpose';

COMMENT ON COLUMN reference.physical_address_purposes.name IS 'The name of the physical address purpose';

COMMENT ON COLUMN reference.physical_address_purposes.description IS 'The description for the physical address purpose';

COMMENT ON COLUMN reference.physical_address_purposes.party_types IS 'The comma-delimited list of codes for the party types the physical address purpose is associated with';


CREATE TABLE reference.preference_type_categories (
    code        VARCHAR(30)  NOT NULL,
    locale_id   VARCHAR(10)  NOT NULL,
    sort_index  INTEGER      NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(200) NOT NULL DEFAULT '',

    PRIMARY KEY (code, locale_id)
);

CREATE INDEX preference_type_categories_locale_id_ix ON reference.preference_type_categories(locale_id);

COMMENT ON COLUMN reference.preference_type_categories.code IS 'The code for the preference type category';

COMMENT ON COLUMN reference.preference_type_categories.locale_id IS 'The Unicode locale identifier for the preference type category';

COMMENT ON COLUMN reference.preference_type_categories.sort_index IS 'The sort index for the preference type category';

COMMENT ON COLUMN reference.preference_type_categories.name IS 'The name of the preference type category';

COMMENT ON COLUMN reference.preference_type_categories.description IS 'The description for the preference type category';


CREATE TABLE reference.preference_types (
   category    VARCHAR(30)  NOT NULL,
   code        VARCHAR(30)  NOT NULL,
   locale_id   VARCHAR(10)  NOT NULL,
   sort_index  INTEGER      NOT NULL,
   name        VARCHAR(50)  NOT NULL,
   description VARCHAR(200) NOT NULL DEFAULT '',
   party_types VARCHAR(300) NOT NULL,

   PRIMARY KEY (code, locale_id),
   CONSTRAINT preference_types_preference_type_category_fk FOREIGN KEY (category, locale_id) REFERENCES reference.preference_type_categories(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX preference_types_category_ix ON reference.preference_types(category);

CREATE INDEX preference_types_locale_id_ix ON reference.preference_types(locale_id);

COMMENT ON COLUMN reference.preference_types.category IS 'The code for the preference category the preference type is associated with';

COMMENT ON COLUMN reference.preference_types.code IS 'The code for the preference type';

COMMENT ON COLUMN reference.preference_types.locale_id IS 'The Unicode locale identifier for the preference type';

COMMENT ON COLUMN reference.preference_types.sort_index IS 'The sort index for the preference type';

COMMENT ON COLUMN reference.preference_types.name IS 'The name of the preference type';

COMMENT ON COLUMN reference.preference_types.description IS 'The description for the preference type';

COMMENT ON COLUMN reference.preference_types.party_types IS 'The comma-delimited list of codes for the party types the preference type is associated with';


CREATE TABLE reference.races (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX races_locale_id_ix ON reference.races(locale_id);

COMMENT ON COLUMN reference.races.code IS 'The code for the race';

COMMENT ON COLUMN reference.races.locale_id IS 'The Unicode locale identifier for the race';

COMMENT ON COLUMN reference.races.sort_index IS 'The sort index for the race';

COMMENT ON COLUMN reference.races.name IS 'The name of the race';

COMMENT ON COLUMN reference.races.description IS 'The description for the race';


CREATE TABLE reference.regions (
  country     CHAR(2)      NOT NULL,
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (country, code, locale_id),
  CONSTRAINT regions_country_fk FOREIGN KEY (country, locale_id) REFERENCES reference.countries(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX regions_country_ix ON reference.regions(country);

CREATE INDEX regions_locale_id_ix ON reference.regions(locale_id);

COMMENT ON COLUMN reference.regions.country IS 'The ISO 3166-1 alpha-2 code for the country the region is associated with';

COMMENT ON COLUMN reference.regions.code IS 'The code for the region';

COMMENT ON COLUMN reference.regions.locale_id IS 'The Unicode locale identifier for the region';

COMMENT ON COLUMN reference.regions.sort_index IS 'The sort index for the region';

COMMENT ON COLUMN reference.regions.name IS 'The name of the region';

COMMENT ON COLUMN reference.regions.description IS 'The description for the region';


CREATE TABLE reference.residence_permit_types (
  code             VARCHAR(30)  NOT NULL,
  locale_id        VARCHAR(10)  NOT NULL,
  sort_index       INTEGER      NOT NULL,
  name             VARCHAR(50)  NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue CHAR(2)      NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT residence_permit_types_country_fk FOREIGN KEY (country_of_issue, locale_id) REFERENCES reference.countries(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX residence_permit_types_locale_id_ix ON reference.residence_permit_types(locale_id);

CREATE INDEX residence_permit_types_country_of_issue_ix ON reference.residence_permit_types(country_of_issue);

COMMENT ON COLUMN reference.residence_permit_types.code IS 'The code for the residence permit type';

COMMENT ON COLUMN reference.residence_permit_types.locale_id IS 'The Unicode locale identifier for the residence permit type';

COMMENT ON COLUMN reference.residence_permit_types.sort_index IS 'The sort index for the residence permit type';

COMMENT ON COLUMN reference.residence_permit_types.name IS 'The name of the residence permit type';

COMMENT ON COLUMN reference.residence_permit_types.description IS 'The description for the residence permit type';

COMMENT ON COLUMN reference.residence_permit_types.country_of_issue IS 'The ISO 3166-1 alpha-2 code for the country of issue for the residence permit type';


CREATE TABLE reference.residency_statuses (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX residency_statuses_locale_id_ix ON reference.residency_statuses(locale_id);

COMMENT ON COLUMN reference.residency_statuses.code IS 'The code for the residency status';

COMMENT ON COLUMN reference.residency_statuses.locale_id IS 'The Unicode locale identifier for the residency status';

COMMENT ON COLUMN reference.residency_statuses.sort_index IS 'The sort index for the residency status';

COMMENT ON COLUMN reference.residency_statuses.name IS 'The name of the residency status';

COMMENT ON COLUMN reference.residency_statuses.description IS 'The description for the residency status';


CREATE TABLE reference.residential_types (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX residential_types_locale_id_ix ON reference.residential_types(locale_id);

COMMENT ON COLUMN reference.residential_types.code IS 'The code for the residential type';

COMMENT ON COLUMN reference.residential_types.locale_id IS 'The Unicode locale identifier for the residential type';

COMMENT ON COLUMN reference.residential_types.sort_index IS 'The sort index for the residential type';

COMMENT ON COLUMN reference.residential_types.name IS 'The name of the residential type';

COMMENT ON COLUMN reference.residential_types.description IS 'The description for the residential type';


CREATE TABLE reference.sources_of_funds (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX sources_of_funds_locale_id_ix ON reference.sources_of_funds(locale_id);

COMMENT ON COLUMN reference.sources_of_funds.code IS 'The code for the source of funds';

COMMENT ON COLUMN reference.sources_of_funds.locale_id IS 'The Unicode locale identifier for the source of funds';

COMMENT ON COLUMN reference.sources_of_funds.sort_index IS 'The sort index for the source of funds';

COMMENT ON COLUMN reference.sources_of_funds.name IS 'The name of the source of funds';

COMMENT ON COLUMN reference.sources_of_funds.description IS 'The description for the source of funds';


-- ADD FOREIGN KEYS
--
-- FINISH SIC CODES

-- CREATE TABLE reference.standard_industry_codes (
--   code        VARCHAR(30)  NOT NULL,
--   locale_id   VARCHAR(10)  NOT NULL,
--   sort_index  INTEGER      NOT NULL,
--   name        VARCHAR(50)  NOT NULL,
--   description VARCHAR(200) NOT NULL DEFAULT '',
--
--   PRIMARY KEY (code, locale_id)
-- );
--
-- CREATE INDEX standard_industry_codes_locale_id_ix ON reference.standard_industry_codes(locale_id);
--
-- COMMENT ON COLUMN reference.standard_industry_codes.code IS 'The code for the standard industry code';
--
-- COMMENT ON COLUMN reference.standard_industry_codes.locale_id IS 'The Unicode locale identifier for the standard industry code';
--
-- COMMENT ON COLUMN reference.standard_industry_codes.sort_index IS 'The sort index for the standard industry code';
--
-- COMMENT ON COLUMN reference.standard_industry_codes.name IS 'The name of the standard industry code';
--
-- COMMENT ON COLUMN reference.standard_industry_codes.description IS 'The description for the standard industry code';


CREATE TABLE reference.tax_number_types (
  code             VARCHAR(30)  NOT NULL,
  locale_id        VARCHAR(10)  NOT NULL,
  sort_index       INTEGER      NOT NULL,
  name             VARCHAR(50)  NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue CHAR(2)      NOT NULL,

  PRIMARY KEY (code, locale_id),
  CONSTRAINT tax_number_types_country_fk FOREIGN KEY (country_of_issue, locale_id) REFERENCES reference.countries(code, locale_id) ON DELETE CASCADE
);

CREATE INDEX tax_number_types_locale_id_ix ON reference.tax_number_types(locale_id);

CREATE INDEX tax_number_types_country_of_issue_ix ON reference.tax_number_types(country_of_issue);

COMMENT ON COLUMN reference.tax_number_types.code IS 'The code for the tax number type';

COMMENT ON COLUMN reference.tax_number_types.locale_id IS 'The Unicode locale identifier for the tax number type';

COMMENT ON COLUMN reference.tax_number_types.sort_index IS 'The sort index for the tax number type';

COMMENT ON COLUMN reference.tax_number_types.name IS 'The name of the tax number type';

COMMENT ON COLUMN reference.tax_number_types.description IS 'The description for the tax number type';

COMMENT ON COLUMN reference.tax_number_types.country_of_issue IS 'The ISO 3166-1 alpha-2 code for the country of issue for the tax number type';


CREATE TABLE reference.times_to_contact (
    code        VARCHAR(30)  NOT NULL,
    locale_id   VARCHAR(10)  NOT NULL,
    sort_index  INTEGER      NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(200) NOT NULL DEFAULT '',

    PRIMARY KEY (code, locale_id)
);

CREATE INDEX times_to_contact_locale_id_ix ON reference.times_to_contact(locale_id);

COMMENT ON COLUMN reference.times_to_contact.code IS 'The code for the time to contact';

COMMENT ON COLUMN reference.times_to_contact.locale_id IS 'The Unicode locale identifier for the time to contact';

COMMENT ON COLUMN reference.times_to_contact.sort_index IS 'The sort index for the time to contact';

COMMENT ON COLUMN reference.times_to_contact.name IS 'The name of the time to contact';

COMMENT ON COLUMN reference.times_to_contact.description IS 'The description for the time to contact';


CREATE TABLE reference.titles (
  code         VARCHAR(30)  NOT NULL,
  locale_id    VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  abbreviation VARCHAR(20)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX titles_locale_id_ix ON reference.titles(locale_id);

COMMENT ON COLUMN reference.titles.code IS 'The code for the title';

COMMENT ON COLUMN reference.titles.locale_id IS 'The Unicode locale identifier for the title';

COMMENT ON COLUMN reference.titles.sort_index IS 'The sort index for the title';

COMMENT ON COLUMN reference.titles.name IS 'The name of the title';

COMMENT ON COLUMN reference.titles.abbreviation IS 'The abbreviation for the title';

COMMENT ON COLUMN reference.titles.description IS 'The description for the title';


CREATE TABLE reference.verification_methods (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX verification_methods_locale_id_ix ON reference.verification_methods(locale_id);

COMMENT ON COLUMN reference.verification_methods.code IS 'The code for the verification method';

COMMENT ON COLUMN reference.verification_methods.locale_id IS 'The Unicode locale identifier for the verification method';

COMMENT ON COLUMN reference.verification_methods.sort_index IS 'The sort index for the verification method';

COMMENT ON COLUMN reference.verification_methods.name IS 'The name of the verification method';

COMMENT ON COLUMN reference.verification_methods.description IS 'The description for the verification method';


CREATE TABLE reference.verification_statuses (
  code        VARCHAR(30)  NOT NULL,
  locale_id   VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale_id)
);

CREATE INDEX verification_statuses_locale_id_ix ON reference.verification_statuses(locale_id);

COMMENT ON COLUMN reference.verification_statuses.code IS 'The code for the verification status';

COMMENT ON COLUMN reference.verification_statuses.locale_id IS 'The Unicode locale identifier for the verification status';

COMMENT ON COLUMN reference.verification_statuses.sort_index IS 'The sort index for the verification status';

COMMENT ON COLUMN reference.verification_statuses.name IS 'The name of the verification status';

COMMENT ON COLUMN reference.verification_statuses.description IS 'The description for the verification status';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('mobile_number', 'en-US', 1, 'Mobile Number', 'Mobile Numbers', 'Mobile Number');
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('phone_number', 'en-US', 2, 'Phone Number', 'Phone Numbers', 'Phone Number');
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('fax_number', 'en-US', 3, 'Fax Number', 'Fax Numbers', 'Fax Number');
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('email_address', 'en-US', 4, 'E-mail Address', 'E-mail Addresses', 'E-mail Address');
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('social_media', 'en-US', 5, 'Social Media', 'Social Media', 'Social Media');

INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('mobile_number', 'en-ZA', 1, 'Mobile Number', 'Mobile Numbers', 'Mobile Number');
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('phone_number', 'en-ZA', 2, 'Phone Number', 'Phone Numbers', 'Phone Number');
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('fax_number', 'en-ZA', 3, 'Fax Number', 'Fax Numbers', 'Fax Number');
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('email_address', 'en-ZA', 4, 'E-mail Address', 'E-mail Addresses', 'E-mail Address');
INSERT INTO reference.contact_mechanism_types (code, locale_id, sort_index, name, plural, description)
  VALUES ('social_media', 'en-ZA', 5, 'Social Media', 'Social Media', 'Social Media');


INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'personal_mobile_number', 'en-US', 100, 'Personal Mobile Number', 'Personal Mobile Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'work_mobile_number', 'en-US', 101, 'Work Mobile Number', 'Work Mobile Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'other_mobile_number', 'en-US', 102, 'Other Mobile Number', 'Other Mobile Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'main_mobile_number', 'en-US', 110, 'Main Mobile Number', 'Main Mobile Number', 'organization');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'home_phone_number', 'en-US', 200, 'Home Phone Number', 'Home Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'work_phone_number', 'en-US', 201, 'Work Phone Number', 'Work Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'school_phone_number', 'en-US', 202, 'School Phone Number', 'School Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'pager_phone_number', 'en-US', 203, 'Pager Phone Number', 'Pager Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'other_phone_number', 'en-US', 204, 'Other Phone Number', 'Other Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'main_phone_number', 'en-US', 210, 'Main Phone Number', 'Main Phone Number', 'organization');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'home_fax_number', 'en-US', 300, 'Home Fax Number', 'Home Fax Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'work_fax_number', 'en-US', 301, 'Work Fax Number', 'Work Fax Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'other_fax_number', 'en-US', 302, 'Other Fax Number', 'Other Fax Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'main_fax_number', 'en-US', 310, 'Main Fax Number', 'Main Fax Number', 'organization');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'personal_email_address', 'en-US', 400, 'Personal E-mail Address', 'Personal E-mail Address', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'work_email_address', 'en-US', 401, 'Work E-mail Address', 'Work E-mail Address', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'school_email_address', 'en-US', 402, 'School E-mail Address', 'School E-mail Address', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'other_email_address', 'en-US', 403, 'Other E-mail Address', 'Other E-mail Address', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'main_email_address', 'en-US', 410, 'Main E-mail Address', 'Main E-mail Address', 'organization');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('social_media', 'whatsapp_user_id', 'en-US', 500, 'WhatsApp User ID', 'WhatsApp User ID', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('social_media', 'twitter_id', 'en-US', 501, 'Twitter ID', 'Twitter ID', 'person');

INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'personal_mobile_number', 'en-ZA', 100, 'Personal Mobile Number', 'Personal Mobile Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'work_mobile_number', 'en-ZA', 101, 'Work Mobile Number', 'Work Mobile Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'other_mobile_number', 'en-ZA', 102, 'Other Mobile Number', 'Other Mobile Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('mobile_number', 'main_mobile_number', 'en-ZA', 110, 'Main Mobile Number', 'Main Mobile Number', 'organization');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'home_phone_number', 'en-ZA', 200, 'Home Phone Number', 'Home Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'work_phone_number', 'en-ZA', 201, 'Work Phone Number', 'Work Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'school_phone_number', 'en-ZA', 202, 'School Phone Number', 'School Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'pager_phone_number', 'en-ZA', 203, 'Pager Phone Number', 'Pager Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'other_phone_number', 'en-ZA', 204, 'Other Phone Number', 'Other Phone Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('phone_number', 'main_phone_number', 'en-ZA', 210, 'Main Phone Number', 'Main Phone Number', 'organization');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'home_fax_number', 'en-ZA', 300, 'Home Fax Number', 'Home Fax Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'work_fax_number', 'en-ZA', 301, 'Work Fax Number', 'Work Fax Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'other_fax_number', 'en-ZA', 302, 'Other Fax Number', 'Other Fax Number', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('fax_number', 'main_fax_number', 'en-ZA', 310, 'Main Fax Number', 'Main Fax Number', 'organization');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'personal_email_address', 'en-ZA', 400, 'Personal E-mail Address', 'Personal E-mail Address', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'work_email_address', 'en-ZA', 401, 'Work E-mail Address', 'Work E-mail Address', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'school_email_address', 'en-ZA', 402, 'School E-mail Address', 'School E-mail Address', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'other_email_address', 'en-ZA', 403, 'Other E-mail Address', 'Other E-mail Address', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('email_address', 'main_email_address', 'en-ZA', 410, 'Main E-mail Address', 'Main E-mail Address', 'organization');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('social_media', 'whatsapp_user_id', 'en-ZA', 500, 'WhatsApp User ID', 'WhatsApp User ID', 'person');
INSERT INTO reference.contact_mechanism_purposes (type, code, locale_id, sort_index, name, description, party_types)
  VALUES ('social_media', 'twitter_id', 'en-ZA', 501, 'Twitter ID', 'Twitter ID', 'person');


INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AF', 'en-US', 1, 'Afghanistan', 'Afghanistan', 'Afghanistan', 'AF', 'Afghan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AL', 'en-US', 2, 'Republic of Albania', 'Albania', 'Republic of Albania', 'AL', 'Albanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DZ', 'en-US', 3, 'People''s Democratic Republic of Algeria', 'Algeria', 'People''s Democratic Republic of Algeria', 'DZ', 'Algerian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AS', 'en-US', 4, 'American Samoa', 'American Samoa', 'American Samoa', 'US', 'Samoan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AD', 'en-US', 5, 'Principality of Andorra', 'Andorra', 'Principality of Andorra', 'AD', 'Andorran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AO', 'en-US', 6, 'Republic of Angola', 'Angola', 'Republic of Angola', 'AO', 'Angolan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AI', 'en-US', 7, 'Anguilla', 'Anguilla', 'Anguilla', 'GB', 'Anguillan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AQ', 'en-US', 8, 'Antarctica', 'Antarctica', 'Antarctica', 'AQ', 'Antarctic');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AG', 'en-US', 9, 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda', 'AG', 'Antiguan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AR', 'en-US', 10, 'Argentine Republic', 'Argentina', 'Argentine Republic', 'AR', 'Argentine');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AM', 'en-US', 11, 'Republic of Armenia', 'Armenia', 'Republic of Armenia', 'AM', 'Armenian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AW', 'en-US', 12, 'Aruba', 'Aruba', 'Aruba', 'NL', 'Arubian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AU', 'en-US', 13, 'Commonwealth of Australia', 'Australia', 'Commonwealth of Australia', 'AU', 'Australian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AT', 'en-US', 14, 'Republic of Austria', 'Austria', 'Republic of Austria', 'AT', 'Austrian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AZ', 'en-US', 15, 'Republic of Azerbaijan', 'Azerbaijan', 'Republic of Azerbaijan', 'AZ', 'Azerbaijani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BS', 'en-US', 16, 'Commonwealth of The Bahamas', 'The Bahamas', 'Commonwealth of The Bahamas', 'BS', 'Bahamian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BH', 'en-US', 17, 'Kingdom of Bahrain', 'Bahrain', 'Kingdom of Bahrain', 'BH', 'Bahrainian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BD', 'en-US', 18, 'People''s Republic of Bangladesh', 'Bangladesh', 'People''s Republic of Bangladesh', 'BD', 'Bangladeshi');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BB', 'en-US', 19, 'Barbados', 'Barbados', 'Barbados', 'BB', 'Barbadian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BY', 'en-US', 20, ' Republic of Belarus', 'Belarus', ' Republic of Belarus', 'BY', 'Belarusian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BE', 'en-US', 21, 'Kingdom of Belgium', 'Belgium', 'Kingdom of Belgium', 'BE', 'Belgian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BZ', 'en-US', 22, 'Belize', 'Belize', 'Belize', 'BZ', 'Belizean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BJ', 'en-US', 23, 'Republic of Benin', 'Benin', 'Republic of Benin', 'BJ', 'Beninese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BM', 'en-US', 24, 'Bermuda', 'Bermuda', 'Bermuda', 'GB', 'Bermudan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BT', 'en-US', 25, 'Kingdom of Bhutan', 'Bhutan', 'Kingdom of Bhutan', 'BT', 'Bhutanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BO', 'en-US', 26, 'Plurinational State of Bolivia', 'Bolivia', 'Plurinational State of Bolivia', 'BO', 'Bolivian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BQ', 'en-US', 27, 'Bonaire, Sint Eustatius and Saba', 'Caribbean Netherlands', 'Bonaire, Sint Eustatius and Saba (Caribbean Netherlands)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BA', 'en-US', 28, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', '', 'Bosnian and Herzegovinian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BW', 'en-US', 29, 'Botswana', 'Botswana', 'Botswana', 'BW', 'Motswana');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BV', 'en-US', 30, 'Bouvet Island', 'Bouvet Island', 'Bouvet Island', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BR', 'en-US', 31, 'Brazil', 'Brazil', 'Brazil', 'BR', 'Brazilian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BN', 'en-US', 32, 'Nation of Brunei', 'Brunei', 'Nation of Brunei', 'BN', 'Bruneian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BG', 'en-US', 33, 'Republic of Bulgaria', 'Bulgaria', 'Republic of Bulgaria', 'BG', 'Bulgarian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BF', 'en-US', 34, 'Burkina Faso', 'Burkina Faso', 'Burkina Faso', 'BF', 'Burkinabe');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BI', 'en-US', 35, 'Republic of Burundi', 'Burundi', 'Republic of Burundi', 'BI', 'Burundian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CV', 'en-US', 36, 'Republic of Cabo Verde (Cape Verde)', 'Republic of Cabo Verde', 'Republic of Cabo Verde (Cape Verde)', 'CV', 'Cape Verdean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KH', 'en-US', 37, 'Kingdom of Cambodia', 'Cambodia', 'Kingdom of Cambodia', 'KH', 'Cambodian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CM', 'en-US', 38, 'Cameroon', 'Cameroon', 'Cameroon', 'CM', 'Cameroonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CA', 'en-US', 39, 'Canada', 'Canada', 'Canada', 'CA', 'Canadian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KY', 'en-US', 40, 'Cayman Islands', 'Cayman Islands', 'Cayman Islands', 'GB', 'Caymanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CF', 'en-US', 41, 'Central African Republic', 'Central African Republic', 'Central African Republic', 'CF', 'Central African');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TD', 'en-US', 42, 'Republic of Chad', 'Chad', 'Republic of Chad', 'TD', 'Chadian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CL', 'en-US', 43, 'Republic of Chile', 'Chile', 'Republic of Chile', 'CL', 'Chilean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CN', 'en-US', 44, 'China', 'China', 'China', 'CN', 'Chinese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CX', 'en-US', 45, 'Christmas Island', 'Christmas Island', 'Christmas Island', 'AU', 'Christmas Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CC', 'en-US', 46, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'AU', 'Cocos Islandia');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CO', 'en-US', 47, 'Republic of Colombia', 'Colombia', 'Republic of Colombia', 'CO', 'Colombian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KM', 'en-US', 48, 'Union of the Comoros', 'Comoros', 'Union of the Comoros', 'KM', 'Comoran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CG', 'en-US', 49, 'Republic of Congo', 'Congo', 'Republic of Congo', 'CG', 'Congolese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CD', 'en-US', 50, 'Democratic Republic of the Congo', 'Dem. Rep. of the Congo', 'Democratic Republic of the Congo', 'CD', 'Congolese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CK', 'en-US', 51, 'Cook Islands', 'Cook Islands', 'Cook Islands', 'CK', 'Cook Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CR', 'en-US', 52, 'Republic of Costa Rica', 'Costa Rica', 'Republic of Costa Rica', 'CR', 'Costa Rican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HR', 'en-US', 53, 'Republic of Croatia', 'Croatia', 'Republic of Croatia', 'HR', 'Croatian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CU', 'en-US', 54, 'Republic of Cuba', 'Cuba', 'Republic of Cuba', 'CU', 'Cuban');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CW', 'en-US', 55, 'Curaçao', 'Curaçao', 'Curaçao', 'NL', 'Curaçaoan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CY', 'en-US', 56, 'Republic of Cyprus', 'Cyprus', 'Republic of Cyprus', 'CY', 'Cypriot');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CZ', 'en-US', 57, 'Czech Republic', 'Czech Republic', 'Czechia, Czech Republic', 'CZ', 'Czech');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CI', 'en-US', 58, 'Republic of Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire (Ivory Coast)', 'CI', 'Ivorian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DK', 'en-US', 59, 'Kingdom of Denmark', 'Denmark', 'Kingdom of Denmark', 'DK', 'Danish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DJ', 'en-US', 60, 'Republic of Djibouti', 'Djibouti', 'Republic of Djibouti', 'DJ', 'Djiboutian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DM', 'en-US', 61, ' Commonwealth of Dominica', 'Dominica', ' Commonwealth of Dominica', 'DM', 'Dominican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DO', 'en-US', 62, 'Dominican Republic', 'Dominican Republic', 'Dominican Republic', 'DO', 'Dominican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EC', 'en-US', 63, 'Republic of Ecuador', 'Ecuador', 'Republic of Ecuador', 'EC', 'Ecuadorian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EG', 'en-US', 64, 'Arab Republic of Egypt', 'Egypt', 'Arab Republic of Egypt', 'EG', 'Egyptian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SV', 'en-US', 65, 'Republic of El Salvador', 'El Salvador', 'Republic of El Salvador', 'SV', 'Salvadoran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GQ', 'en-US', 66, 'Republic of Equatorial Guinea', 'Equatorial Guinea', 'Republic of Equatorial Guinea', 'GQ', 'Equatoguinean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ER', 'en-US', 67, 'State of Eritrea', 'Eritrea', 'State of Eritrea', 'ER', 'Eritrean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EE', 'en-US', 68, 'Republic of Estonia', 'Estonia', 'Republic of Estonia', 'EE', 'Estonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ET', 'en-US', 69, 'Federal Democratic Republic of Ethiopia', 'Ethiopia', 'Federal Democratic Republic of Ethiopia', 'ET', 'Ethiopian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FK', 'en-US', 70, 'Falkland Islands (Malvinas)', 'Falkland Islands', 'Falkland Islands (Malvinas)', 'GB', 'Falkland Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FO', 'en-US', 71, 'Faroe Islands', 'Faroe Islands', 'Faroe Islands', 'DK', 'Faroese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FJ', 'en-US', 72, 'Republic of Fiji', 'Fiji', 'Republic of Fiji', 'FJ', 'Fijian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FI', 'en-US', 73, 'Republic of Finland', 'Finland', 'Republic of Finland', 'FI', 'Finnish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FR', 'en-US', 74, 'French Republic', 'France', 'French Republic', 'FR', 'French');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GF', 'en-US', 75, 'French Guiana', 'French Guiana', 'French Guiana', 'FR', 'French Guianese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PF', 'en-US', 76, 'Collectivity of French Polynesia', 'French Polynesia', 'Collectivity of French Polynesia', 'FR', 'French Polynesian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TF', 'en-US', 77, 'French Southern and Antarctic Lands', 'French Southern Territories', 'French Southern and Antarctic Lands', 'FR', 'French');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GA', 'en-US', 78, 'Gabonese Republic', 'Gabon', 'Gabonese Republic', 'GA', 'Gabonese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GM', 'en-US', 79, 'Republic of The Gambia', 'Gambia', 'Republic of The Gambia', 'GM', 'Gambian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GE', 'en-US', 80, 'Georgia', 'Georgia', 'Georgia', 'GE', 'Georgian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DE', 'en-US', 81, 'Federal Republic of Germany', 'Germany', 'Federal Republic of Germany', 'DE', 'German');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GH', 'en-US', 82, 'Republic of Ghana', 'Ghana', 'Republic of Ghana', 'GH', 'Ghanaian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GI', 'en-US', 83, 'Gibraltar', 'Gibraltar', 'Gibraltar', 'GB', 'Gibraltarian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GR', 'en-US', 84, 'Hellenic Republic', 'Greece', 'Hellenic Republic', 'GR', 'Greek');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GL', 'en-US', 85, 'Greenland', 'Greenland', 'Greenland', 'GL', 'Greenlandic');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GD', 'en-US', 86, 'Grenada', 'Grenada', 'Grenada', 'GD', 'Grenadian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GP', 'en-US', 87, 'Guadeloupe', 'Guadeloupe', 'Guadeloupe', 'FR', 'Guadeloupean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GU', 'en-US', 88, 'Guam', 'Guam', 'Guam', 'US', 'Guamanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GT', 'en-US', 89, 'Republic of Guatemala', 'Guatemala', 'Republic of Guatemala', 'GT', 'Guatemalan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GG', 'en-US', 90, 'Guernsey', 'Guernsey', 'Guernsey', 'GB', 'British');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GN', 'en-US', 91, 'Republic of Guinea', 'Guinea', 'Republic of Guinea', 'GN', 'Guinean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GW', 'en-US', 92, 'Republic of Guinea-Bissau', 'Guinea-Bissau', 'Republic of Guinea-Bissau', 'GW', 'Guinean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GY', 'en-US', 93, 'Co-operative Republic of Guyana', 'Guyana', 'Co-operative Republic of Guyana', 'GY', 'Guyanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HT', 'en-US', 94, 'Republic of Haiti', 'Haiti', 'Republic of Haiti', 'HT', 'Haitian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VA', 'en-US', 95, 'Holy See', 'Holy See', 'Holy See', 'VA', 'Papal');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HN', 'en-US', 96, 'Republic of Honduras', 'Honduras', 'Republic of Honduras', 'HN', 'Honduran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HK', 'en-US', 97, 'Hong Kong', 'Hong Kong', 'Hong Kong Special Administrative Region of the People''s Republic of China', 'CN', 'Hong Konger');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HU', 'en-US', 98, 'Hungary', 'Hungary', 'Hungary', 'HU', 'Hungarian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IS', 'en-US', 99, 'Iceland', 'Iceland', 'Iceland', 'IS', 'Icelander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IN', 'en-US', 100, 'Republic of India', 'India', 'Republic of India', 'IN', 'Indian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ID', 'en-US', 101, 'Republic of Indonesia', 'Indonesia', 'Republic of Indonesia', 'ID', 'Indonesian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IR', 'en-US', 102, 'Islamic Republic of Iran', 'Iran', 'Islamic Republic of Iran', 'IR', 'Iranian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IQ', 'en-US', 103, 'Republic of Iraq', 'Iraq', 'Republic of Iraq', 'IQ', 'Iraqi');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IE', 'en-US', 104, 'Republic of Ireland', 'Ireland', 'Republic of Ireland', 'IE', 'Irish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IM', 'en-US', 105, 'Isle of Man', 'Isle of Man', 'Isle of Man', 'GB', 'Manx');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IL', 'en-US', 106, 'State of Israel', 'Israel', 'State of Israel', 'IL', 'Israeli');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IT', 'en-US', 107, 'Italian Republic', 'Italy', 'Italian Republic', 'IT', 'Italian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JM', 'en-US', 108, 'Jamaica', 'Jamaica', 'Jamaica', 'JM', 'Jamaican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JP', 'en-US', 109, 'Japan', 'Japan', 'Japan', 'JP', 'Japanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JE', 'en-US', 110, 'Bailiwick of Jersey', 'Jersey', 'Bailiwick of Jersey', 'GB', 'British');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JO', 'en-US', 111, 'Hashemite Kingdom of Jordan', 'Jordan', 'Hashemite Kingdom of Jordan', 'JO', 'Jordanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KZ', 'en-US', 112, 'Republic of Kazakhstan', 'Kazakhstan', 'Republic of Kazakhstan', 'KZ', 'Kazakhstani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KE', 'en-US', 113, 'Republic of Kenya', 'Kenya', 'Republic of Kenya', 'KE', 'Kenyan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KI', 'en-US', 114, 'Republic of Kiribati', 'Kiribati', 'Republic of Kiribati', 'KI', 'I-Kiribati');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KP', 'en-US', 115, 'Democratic People''s Republic of Korea', 'North Korea', 'Democratic People''s Republic of Korea', 'KP', 'North Korean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KR', 'en-US', 116, 'Republic of Korea', 'South Korea', 'Republic of Korea', 'KR', 'South Korean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KW', 'en-US', 117, 'State of Kuwait', 'Kuwait', 'State of Kuwait', 'KW', 'Kuwaiti');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KG', 'en-US', 118, 'Kyrgyz Republic', 'Kyrgyzstan', 'Kyrgyz Republic', 'KG', 'Kyrgyzstani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LA', 'en-US', 119, 'Lao People''s Democratic Republic', 'Laos', 'Lao People''s Democratic Republic', 'LA', 'Laotian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LV', 'en-US', 120, 'Republic of Latvia', 'Latvia', 'Republic of Latvia', 'LV', 'Latvian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LB', 'en-US', 121, 'Lebanese Republic', 'Lebanon', 'Lebanese Republic', 'LB', 'Lebanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LS', 'en-US', 122, 'Kingdom of Lesotho', 'Lesotho', 'Kingdom of Lesotho', 'LS', 'Mosotho');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LR', 'en-US', 123, 'Republic of Liberia', 'Liberia', 'Republic of Liberia', 'LR', 'Liberian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LY', 'en-US', 124, 'State of Libya', 'Libya', 'State of Libya', 'LY', 'Libyan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LI', 'en-US', 125, 'Principality of Liechtenstein', 'Liechtenstein', 'Principality of Liechtenstein', 'LI', 'Liechtensteiner');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LT', 'en-US', 126, 'Republic of Lithuania', 'Lithuania', 'Republic of Lithuania', 'LT', 'Lithunian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LU', 'en-US', 127, 'Grand Duchy of Luxembourg', 'Luxembourg', 'Grand Duchy of Luxembourg', 'LU', 'Luxembourger');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MO', 'en-US', 128, 'Macao', 'Macao', 'Macao', 'CN', 'Macanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MG', 'en-US', 129, 'Republic of Madagascar', 'Madagascar', 'Republic of Madagascar', 'MG', 'Malagasy');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MW', 'en-US', 130, 'Republic of Malawi', 'Malawi', 'Republic of Malawi', 'MW', 'Malawian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MY', 'en-US', 131, 'Malaysia', 'Malaysia', 'Malaysia', 'MY', 'Malaysian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MV', 'en-US', 132, 'Republic of Maldives', 'Maldives', 'Republic of Maldives', 'MV', 'Maldivan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ML', 'en-US', 133, 'Republic of Mali', 'Mali', 'Republic of Mali', 'ML', 'Malian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MT', 'en-US', 134, 'Republic of Malta', 'Malta', 'Republic of Malta', 'MT', 'Maltese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MH', 'en-US', 135, 'Republic of the Marshall Islands', 'Marshall Islands', 'Republic of the Marshall Islands', 'MH', 'Marshallese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MQ', 'en-US', 136, 'Martinique', 'Martinique', 'Martinique', 'FR', 'Martinican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MR', 'en-US', 137, 'Islamic Republic of Mauritania', 'Mauritania', 'Islamic Republic of Mauritania', 'MR', 'Mauritanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MU', 'en-US', 138, 'Republic of Mauritius', 'Mauritius', 'Republic of Mauritius', 'MU', 'Mauritian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('YT', 'en-US', 139, 'Department of Mayotte', 'Mayotte', 'Department of Mayotte', 'FR', 'Mahoran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MX', 'en-US', 140, 'United Mexican States', 'Mexico', 'United Mexican States', 'MX', 'Mexican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FM', 'en-US', 141, 'Federated States of Micronesia', 'Micronesia', 'Federated States of Micronesia', 'FM', 'Micronesian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MD', 'en-US', 142, 'Republic of Moldova', 'Moldova', 'Republic of Moldova', 'MD', 'Moldovan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MC', 'en-US', 143, ' Principality of Monaco', 'Monaco', ' Principality of Monaco', 'MC', 'Monacan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MN', 'en-US', 144, 'Mongolia', 'Mongolia', 'Mongolia', 'MN', 'Mongolian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ME', 'en-US', 145, 'Montenegro', 'Montenegro', 'Montenegro', 'ME', 'Montenegrin');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MS', 'en-US', 146, 'Montserrat', 'Montserrat', 'Montserrat', 'GB', 'Montserratian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MA', 'en-US', 147, 'Kingdom of Morocco', 'Morocco', 'Kingdom of Morocco', 'MA', 'Moroccan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MZ', 'en-US', 148, 'Republic of Mozambique', 'Mozambique', 'Republic of Mozambique', 'MZ', 'Mozambican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MM', 'en-US', 149, 'Republic of the Union of Myanmar', 'Myanmar', 'Republic of the Union of Myanmar', 'MM', 'Myanma');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NA', 'en-US', 150, 'Republic of Namibia', 'Namibia', 'Republic of Namibia', 'NA', 'Namibian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NR', 'en-US', 151, 'Republic of Nauru', 'Nauru', 'Republic of Nauru', 'NR', 'Nauruan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NP', 'en-US', 152, 'Federal Democratic Republic of Nepal', 'Nepal', 'Federal Democratic Republic of Nepal', 'NP', 'Nepalese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NL', 'en-US', 153, 'Netherlands', 'Netherlands', 'Netherlands', 'NL', 'Dutch');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NC', 'en-US', 154, 'New Caledonia', 'New Caledonia', 'New Caledonia', 'FR', 'New Caledonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NZ', 'en-US', 155, 'New Zealand', 'New Zealand', 'New Zealand', 'NZ', 'New Zealander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NI', 'en-US', 156, 'Republic of Nicaragua', 'Nicaragua', 'Republic of Nicaragua', 'NI', 'Nicaraguan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NE', 'en-US', 157, 'Republic of the Niger', 'Niger', 'Republic of the Niger', 'NE', 'Nigerien');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NG', 'en-US', 158, 'Federal Republic of Nigeria', 'Nigeria', 'Federal Republic of Nigeria', 'NG', 'Nigerian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NU', 'en-US', 159, 'Niue', 'Niue', 'Niue', 'NU', 'Niuean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NF', 'en-US', 160, 'Norfolk Island', 'Norfolk Island', 'Norfolk Island', 'AU', 'Norfolk Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MP', 'en-US', 161, 'Commonwealth of the Northern Mariana Islands', 'Northern Mariana Islands', 'Commonwealth of the Northern Mariana Islands', 'US', 'Northern Mariana Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NO', 'en-US', 162, 'Kingdom of Norway', 'Norway', 'Kingdom of Norway', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('OM', 'en-US', 163, 'Sultanate of Oman', 'Oman', 'Sultanate of Oman', 'OM', 'Omani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PK', 'en-US', 164, 'Islamic Republic of Pakistan', 'Pakistan', 'Islamic Republic of Pakistan', 'PK', 'Pakistani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PW', 'en-US', 165, 'Republic of Palau', 'Palau', 'Republic of Palau', 'PW', 'Palauan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PS', 'en-US', 166, 'State of Palestine', 'Palestine', 'State of Palestine', 'PS', 'Palestinian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PA', 'en-US', 167, 'Republic of Panama', 'Panama', 'Republic of Panama', 'PA', 'Panamanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PG', 'en-US', 168, 'Independent State of Papua New Guinea', 'Papua New Guinea', 'Independent State of Papua New Guinea', 'PG', 'Papua New Guinean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PY', 'en-US', 169, 'Republic of Paraguay', 'Paraguay', 'Republic of Paraguay', 'PY', 'Paraguayan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PE', 'en-US', 170, 'Republic of Peru', 'Peru', 'Republic of Peru', 'PE', 'Peruvian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PH', 'en-US', 171, 'Republic of the Philippines', 'Philippines', 'Republic of the Philippines', 'PH', 'Filipino');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PN', 'en-US', 172, 'Pitcairn Islands', 'Pitcairn Islands', 'Pitcairn Islands', 'GB', 'Pitcairn Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PL', 'en-US', 173, 'Republic of Poland', 'Poland', 'Republic of Poland', 'PL', 'Polish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PT', 'en-US', 174, 'Portuguese Republic', 'Portugal', 'Portuguese Republic', 'PT', 'Portuguese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PR', 'en-US', 175, 'Commonwealth of Puerto Rico', 'Puerto Rico', 'Commonwealth of Puerto Rico', 'PR', 'Puerto Rican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('QA', 'en-US', 176, 'State of Qatar', 'Qatar', 'State of Qatar', 'QA', 'Qatari');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MK', 'en-US', 177, 'Republic of North Macedonia', 'North Macedonia', 'Republic of North Macedonia', 'MK', 'Macedonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RO', 'en-US', 178, 'Romania', 'Romania', 'Romania', 'RO', 'Romanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RU', 'en-US', 179, 'Russian Federation', 'Russia', 'Russian Federation', 'RU', 'Russian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RW', 'en-US', 180, 'Republic of Rwanda', 'Rwanda', 'Republic of Rwanda', 'RW', 'Rwandan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RE', 'en-US', 181, 'Réunion', 'Réunion', 'Réunion', 'FR', 'French');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BL', 'en-US', 182, 'Saint Barthélemy', 'St. Barthélemy', 'Saint Barthélemy', 'FR', 'Barthélemois');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KN', 'en-US', 183, 'Federation of Saint Christopher and Nevis', 'St. Kitts and Nevis', 'Federation of Saint Christopher and Nevis', 'KN', 'Kittian or Nevisian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LC', 'en-US', 184, 'Saint Lucia', 'St. Lucia', 'Saint Lucia', 'LC', 'Saint Lucian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MF', 'en-US', 185, 'Saint Martin (French part)', 'St. Martin (French part)', 'Saint Martin (French part)', 'FR', 'French');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PM', 'en-US', 186, 'Saint Pierre and Miquelon', 'St. Pierre and Miquelon', 'Saint Pierre and Miquelon', 'FR', 'Miquelonnais');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VC', 'en-US', 187, 'Saint Vincent and the Grenadines', 'Saint Vincent', 'Saint Vincent and the Grenadines', 'VC', 'Saint Vincentian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('WS', 'en-US', 188, 'Independent State of Samoa', 'Samoa', 'Independent State of Samoa', 'WS', 'Samoan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SM', 'en-US', 189, 'Republic of San Marino', 'San Marino', 'Republic of San Marino', 'SM', 'Sammarinese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ST', 'en-US', 190, 'Democratic Republic of São Tomé and Príncipe', 'Sao Tome and Principe', 'Democratic Republic of São Tomé and Príncipe', 'ST', 'São Tomean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SA', 'en-US', 191, 'Kingdom of Saudi Arabia', 'Saudi Arabia', 'Kingdom of Saudi Arabia', 'SA', 'Saudi Arabian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SN', 'en-US', 192, 'Republic of Senegal', 'Senegal', 'Republic of Senegal', 'SN', 'Senegalese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RS', 'en-US', 193, 'Republic of Serbia', 'Serbia', 'Republic of Serbia', 'RS', 'Serbian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SC', 'en-US', 194, 'Republic of Seychelles', 'Seychelles', 'Republic of Seychelles', 'SC', 'Seychellois');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SL', 'en-US', 195, 'Republic of Sierra Leone', 'Sierra Leone', 'Republic of Sierra Leone', 'SL', 'Sierra Leonean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SG', 'en-US', 196, 'Republic of Singapore', 'Singapore', 'Republic of Singapore', 'SG', 'Singaporean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SX', 'en-US', 197, 'Sint Maarten (Dutch part)', 'Sint Maarten', 'Sint Maarten (Dutch part)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SK', 'en-US', 198, 'Slovak Republic', 'Slovakia', 'Slovak Republic', 'SK', 'Slovak');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SI', 'en-US', 199, 'Republic of Slovenia', 'Slovenia', 'Republic of Slovenia', 'SI', 'Slovene');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SB', 'en-US', 200, 'Solomon Islands', 'Solomon Islands', 'Solomon Islands', 'SB', 'Solomon Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SO', 'en-US', 201, 'Federal Republic of Somalia', 'Somalia', 'Federal Republic of Somalia', 'SO', 'Somali');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZA', 'en-US', 202, 'Republic of South Africa', 'South Africa', 'Republic of South Africa', 'ZA', 'South African');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SS', 'en-US', 203, 'Republic of South Sudan', 'South Sudan', 'Republic of South Sudan', 'SS', 'Sudanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ES', 'en-US', 204, 'Kingdom of Spain', 'Spain', 'Kingdom of Spain', 'ES', 'Spanish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LK', 'en-US', 205, 'Democratic Socialist Republic of Sri Lanka', 'Sri Lanka', 'Democratic Socialist Republic of Sri Lanka', 'LK', 'Sri Lankan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SD', 'en-US', 206, 'Republic of the Sudan', 'Sudan', 'Republic of the Sudan', 'SD', 'Sudanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SR', 'en-US', 207, 'Republic of Suriname', 'Suriname', 'Republic of Suriname', 'SR', 'Surinamese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SJ', 'en-US', 208, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SZ', 'en-US', 209, 'Kingdom of Eswatini', 'Swaziland', 'Kingdom of Eswatini', 'SZ', 'Swazi');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SE', 'en-US', 210, 'Kingdom of Sweden', 'Sweden', 'Kingdom of Sweden', 'SE', 'Swedish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CH', 'en-US', 211, 'Switzerland', 'Switzerland', 'Switzerland', 'CH', 'Swiss');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SY', 'en-US', 212, 'Syrian Arab Republic', 'Syria', 'Syrian Arab Republic', 'SY', 'Syrian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TW', 'en-US', 213, 'Republic of China', 'Taiwan', 'Republic of China', 'TW', 'Taiwanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TJ', 'en-US', 214, 'Republic of Tajikistan', 'Tajikistan', 'Republic of Tajikistan', 'TJ', 'Tajikistani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TZ', 'en-US', 215, 'United Republic of Tanzania', 'Tanzania', 'United Republic of Tanzania', 'TZ', 'Tanzanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TH', 'en-US', 216, 'Kingdom of Thailand', 'Thailand', 'Kingdom of Thailand', 'TH', 'Thai');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TL', 'en-US', 217, 'Democratic Republic of Timor-Leste', 'East Timor', 'Democratic Republic of Timor-Leste', 'TL', 'Timorese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TG', 'en-US', 218, 'Togolese Republic', 'Togo', 'Togolese Republic', 'TG', 'Togolese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TK', 'en-US', 219, 'Tokelau Islands', 'Tokelau', 'Tokelau Islands', 'NZ', 'Tokelauan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TO', 'en-US', 220, 'Kingdom of Tonga', 'Tonga', 'Kingdom of Tonga', 'TO', 'Tongan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TT', 'en-US', 221, 'Republic of Trinidad and Tobago', 'Trinidad and Tobago', 'Republic of Trinidad and Tobago', 'TT', 'Trinidadian and Tobagonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TN', 'en-US', 222, 'Republic of Tunisia', 'Tunisia', 'Republic of Tunisia', 'TN', 'Tunisian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TR', 'en-US', 223, 'Republic of Turkey', 'Turkey', 'Republic of Turkey', 'TR', 'Turkish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TM', 'en-US', 224, 'Turkmenistan', 'Turkmenistan', 'Turkmenistan', 'TM', 'Turkmen');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TC', 'en-US', 225, 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'GB', 'Turks and Caicos Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TV', 'en-US', 226, 'Tuvalu', 'Tuvalu', 'Tuvalu', 'TV', 'Tuvaluan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UG', 'en-US', 227, 'Republic of Uganda', 'Uganda', 'Republic of Uganda', 'UG', 'Ugandan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UA', 'en-US', 228, 'Ukraine', 'Ukraine', 'Ukraine', 'UA', 'Ukrainian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AE', 'en-US', 229, 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates', 'AE', 'Emirati');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GB', 'en-US', 230, 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland', 'GB', 'British');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UM', 'en-US', 231, 'United States Minor Outlying Islands', 'US Minor Outlying Islands', 'United States Minor Outlying Islands', 'UM', 'American');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('US', 'en-US', 232, 'United States of America', 'United States of America', 'United States of America', 'US', 'American');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UY', 'en-US', 233, 'Oriental Republic of Uruguay', 'Uruguay', 'Oriental Republic of Uruguay', 'UY', 'Uruguayan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UZ', 'en-US', 234, 'Republic of Uzbekistan', 'Uzbekistan', 'Republic of Uzbekistan', 'UZ', 'Uzbekistani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VU', 'en-US', 235, 'Republic of Vanuatu', 'Vanuatu', 'Republic of Vanuatu', 'VU', 'Ni-Vanuatu');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VE', 'en-US', 236, 'Bolivarian Republic of Venezuela', 'Venezuela', 'Bolivarian Republic of Venezuela', 'VE', 'Venezuelan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VN', 'en-US', 237, 'Socialist Republic of Vietnam', 'Vietnam', 'Socialist Republic of Vietnam', 'VN', 'Vietnamese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VG', 'en-US', 238, 'Virgin Islands', 'British Virgin Islands', 'Virgin Islands', 'GB', 'Virgin Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VI', 'en-US', 239, 'United States Virgin Islands', 'U.S. Virgin Islands', 'United States Virgin Islands', 'US', 'Virgin Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('WF', 'en-US', 240, 'Territory of the Wallis and Futuna Islands', 'Wallis and Futuna Islands', 'Territory of the Wallis and Futuna Islands', 'FR', 'Wallisian or Futunan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EH', 'en-US', 241, 'Western Sahara', 'Western Sahara', 'Western Sahara', '', 'Western Saharan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('YE', 'en-US', 242, 'Republic of Yemen', 'Yemen', 'Republic of Yemen', 'YE', 'Yemeni');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZM', 'en-US', 243, 'Republic of Zambia', 'Zambia', 'Republic of Zambia', 'ZM', 'Zambian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZW', 'en-US', 244, 'Republic of Zimbabwe', 'Zimbabwe', 'Republic of Zimbabwe', 'ZW', 'Zimbabwean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AX', 'en-US', 245, 'Åland Islands', 'Åland Islands', 'Åland Islands', 'FI', 'Ålandic');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZZ', 'en-US', 999, 'Unknown', 'Unknown', 'Unknown', 'ZA', 'Unknown');

INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AF', 'en-ZA', 1, 'Afghanistan', 'Afghanistan', 'Afghanistan', 'AF', 'Afghan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AL', 'en-ZA', 2, 'Republic of Albania', 'Albania', 'Republic of Albania', 'AL', 'Albanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DZ', 'en-ZA', 3, 'People''s Democratic Republic of Algeria', 'Algeria', 'People''s Democratic Republic of Algeria', 'DZ', 'Algerian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AS', 'en-ZA', 4, 'American Samoa', 'American Samoa', 'American Samoa', 'US', 'Samoan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AD', 'en-ZA', 5, 'Principality of Andorra', 'Andorra', 'Principality of Andorra', 'AD', 'Andorran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AO', 'en-ZA', 6, 'Republic of Angola', 'Angola', 'Republic of Angola', 'AO', 'Angolan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AI', 'en-ZA', 7, 'Anguilla', 'Anguilla', 'Anguilla', 'GB', 'Anguillan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AQ', 'en-ZA', 8, 'Antarctica', 'Antarctica', 'Antarctica', 'AQ', 'Antarctic');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AG', 'en-ZA', 9, 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda', 'AG', 'Antiguan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AR', 'en-ZA', 10, 'Argentine Republic', 'Argentina', 'Argentine Republic', 'AR', 'Argentine');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AM', 'en-ZA', 11, 'Republic of Armenia', 'Armenia', 'Republic of Armenia', 'AM', 'Armenian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AW', 'en-ZA', 12, 'Aruba', 'Aruba', 'Aruba', 'NL', 'Arubian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AU', 'en-ZA', 13, 'Commonwealth of Australia', 'Australia', 'Commonwealth of Australia', 'AU', 'Australian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AT', 'en-ZA', 14, 'Republic of Austria', 'Austria', 'Republic of Austria', 'AT', 'Austrian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AZ', 'en-ZA', 15, 'Republic of Azerbaijan', 'Azerbaijan', 'Republic of Azerbaijan', 'AZ', 'Azerbaijani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BS', 'en-ZA', 16, 'Commonwealth of The Bahamas', 'The Bahamas', 'Commonwealth of The Bahamas', 'BS', 'Bahamian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BH', 'en-ZA', 17, 'Kingdom of Bahrain', 'Bahrain', 'Kingdom of Bahrain', 'BH', 'Bahrainian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BD', 'en-ZA', 18, 'People''s Republic of Bangladesh', 'Bangladesh', 'People''s Republic of Bangladesh', 'BD', 'Bangladeshi');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BB', 'en-ZA', 19, 'Barbados', 'Barbados', 'Barbados', 'BB', 'Barbadian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BY', 'en-ZA', 20, ' Republic of Belarus', 'Belarus', ' Republic of Belarus', 'BY', 'Belarusian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BE', 'en-ZA', 21, 'Kingdom of Belgium', 'Belgium', 'Kingdom of Belgium', 'BE', 'Belgian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BZ', 'en-ZA', 22, 'Belize', 'Belize', 'Belize', 'BZ', 'Belizean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BJ', 'en-ZA', 23, 'Republic of Benin', 'Benin', 'Republic of Benin', 'BJ', 'Beninese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BM', 'en-ZA', 24, 'Bermuda', 'Bermuda', 'Bermuda', 'GB', 'Bermudan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BT', 'en-ZA', 25, 'Kingdom of Bhutan', 'Bhutan', 'Kingdom of Bhutan', 'BT', 'Bhutanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BO', 'en-ZA', 26, 'Plurinational State of Bolivia', 'Bolivia', 'Plurinational State of Bolivia', 'BO', 'Bolivian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BQ', 'en-ZA', 27, 'Bonaire, Sint Eustatius and Saba', 'Caribbean Netherlands', 'Bonaire, Sint Eustatius and Saba (Caribbean Netherlands)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BA', 'en-ZA', 28, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', '', 'Bosnian and Herzegovinian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BW', 'en-ZA', 29, 'Botswana', 'Botswana', 'Botswana', 'BW', 'Motswana');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BV', 'en-ZA', 30, 'Bouvet Island', 'Bouvet Island', 'Bouvet Island', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BR', 'en-ZA', 31, 'Brazil', 'Brazil', 'Brazil', 'BR', 'Brazilian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BN', 'en-ZA', 32, 'Nation of Brunei', 'Brunei', 'Nation of Brunei', 'BN', 'Bruneian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BG', 'en-ZA', 33, 'Republic of Bulgaria', 'Bulgaria', 'Republic of Bulgaria', 'BG', 'Bulgarian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BF', 'en-ZA', 34, 'Burkina Faso', 'Burkina Faso', 'Burkina Faso', 'BF', 'Burkinabe');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BI', 'en-ZA', 35, 'Republic of Burundi', 'Burundi', 'Republic of Burundi', 'BI', 'Burundian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CV', 'en-ZA', 36, 'Republic of Cabo Verde (Cape Verde)', 'Republic of Cabo Verde', 'Republic of Cabo Verde (Cape Verde)', 'CV', 'Cape Verdean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KH', 'en-ZA', 37, 'Kingdom of Cambodia', 'Cambodia', 'Kingdom of Cambodia', 'KH', 'Cambodian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CM', 'en-ZA', 38, 'Cameroon', 'Cameroon', 'Cameroon', 'CM', 'Cameroonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CA', 'en-ZA', 39, 'Canada', 'Canada', 'Canada', 'CA', 'Canadian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KY', 'en-ZA', 40, 'Cayman Islands', 'Cayman Islands', 'Cayman Islands', 'GB', 'Caymanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CF', 'en-ZA', 41, 'Central African Republic', 'Central African Republic', 'Central African Republic', 'CF', 'Central African');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TD', 'en-ZA', 42, 'Republic of Chad', 'Chad', 'Republic of Chad', 'TD', 'Chadian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CL', 'en-ZA', 43, 'Republic of Chile', 'Chile', 'Republic of Chile', 'CL', 'Chilean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CN', 'en-ZA', 44, 'China', 'China', 'China', 'CN', 'Chinese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CX', 'en-ZA', 45, 'Christmas Island', 'Christmas Island', 'Christmas Island', 'AU', 'Christmas Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CC', 'en-ZA', 46, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'AU', 'Cocos Islandia');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CO', 'en-ZA', 47, 'Republic of Colombia', 'Colombia', 'Republic of Colombia', 'CO', 'Colombian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KM', 'en-ZA', 48, 'Union of the Comoros', 'Comoros', 'Union of the Comoros', 'KM', 'Comoran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CG', 'en-ZA', 49, 'Republic of Congo', 'Congo', 'Republic of Congo', 'CG', 'Congolese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CD', 'en-ZA', 50, 'Democratic Republic of the Congo', 'Dem. Rep. of the Congo', 'Democratic Republic of the Congo', 'CD', 'Congolese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CK', 'en-ZA', 51, 'Cook Islands', 'Cook Islands', 'Cook Islands', 'CK', 'Cook Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CR', 'en-ZA', 52, 'Republic of Costa Rica', 'Costa Rica', 'Republic of Costa Rica', 'CR', 'Costa Rican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HR', 'en-ZA', 53, 'Republic of Croatia', 'Croatia', 'Republic of Croatia', 'HR', 'Croatian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CU', 'en-ZA', 54, 'Republic of Cuba', 'Cuba', 'Republic of Cuba', 'CU', 'Cuban');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CW', 'en-ZA', 55, 'Curaçao', 'Curaçao', 'Curaçao', 'NL', 'Curaçaoan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CY', 'en-ZA', 56, 'Republic of Cyprus', 'Cyprus', 'Republic of Cyprus', 'CY', 'Cypriot');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CZ', 'en-ZA', 57, 'Czech Republic', 'Czech Republic', 'Czechia, Czech Republic', 'CZ', 'Czech');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CI', 'en-ZA', 58, 'Republic of Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire (Ivory Coast)', 'CI', 'Ivorian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DK', 'en-ZA', 59, 'Kingdom of Denmark', 'Denmark', 'Kingdom of Denmark', 'DK', 'Danish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DJ', 'en-ZA', 60, 'Republic of Djibouti', 'Djibouti', 'Republic of Djibouti', 'DJ', 'Djiboutian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DM', 'en-ZA', 61, ' Commonwealth of Dominica', 'Dominica', ' Commonwealth of Dominica', 'DM', 'Dominican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DO', 'en-ZA', 62, 'Dominican Republic', 'Dominican Republic', 'Dominican Republic', 'DO', 'Dominican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EC', 'en-ZA', 63, 'Republic of Ecuador', 'Ecuador', 'Republic of Ecuador', 'EC', 'Ecuadorian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EG', 'en-ZA', 64, 'Arab Republic of Egypt', 'Egypt', 'Arab Republic of Egypt', 'EG', 'Egyptian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SV', 'en-ZA', 65, 'Republic of El Salvador', 'El Salvador', 'Republic of El Salvador', 'SV', 'Salvadoran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GQ', 'en-ZA', 66, 'Republic of Equatorial Guinea', 'Equatorial Guinea', 'Republic of Equatorial Guinea', 'GQ', 'Equatoguinean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ER', 'en-ZA', 67, 'State of Eritrea', 'Eritrea', 'State of Eritrea', 'ER', 'Eritrean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EE', 'en-ZA', 68, 'Republic of Estonia', 'Estonia', 'Republic of Estonia', 'EE', 'Estonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ET', 'en-ZA', 69, 'Federal Democratic Republic of Ethiopia', 'Ethiopia', 'Federal Democratic Republic of Ethiopia', 'ET', 'Ethiopian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FK', 'en-ZA', 70, 'Falkland Islands (Malvinas)', 'Falkland Islands', 'Falkland Islands (Malvinas)', 'GB', 'Falkland Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FO', 'en-ZA', 71, 'Faroe Islands', 'Faroe Islands', 'Faroe Islands', 'DK', 'Faroese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FJ', 'en-ZA', 72, 'Republic of Fiji', 'Fiji', 'Republic of Fiji', 'FJ', 'Fijian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FI', 'en-ZA', 73, 'Republic of Finland', 'Finland', 'Republic of Finland', 'FI', 'Finnish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FR', 'en-ZA', 74, 'French Republic', 'France', 'French Republic', 'FR', 'French');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GF', 'en-ZA', 75, 'French Guiana', 'French Guiana', 'French Guiana', 'FR', 'French Guianese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PF', 'en-ZA', 76, 'Collectivity of French Polynesia', 'French Polynesia', 'Collectivity of French Polynesia', 'FR', 'French Polynesian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TF', 'en-ZA', 77, 'French Southern and Antarctic Lands', 'French Southern Territories', 'French Southern and Antarctic Lands', 'FR', 'French');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GA', 'en-ZA', 78, 'Gabonese Republic', 'Gabon', 'Gabonese Republic', 'GA', 'Gabonese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GM', 'en-ZA', 79, 'Republic of The Gambia', 'Gambia', 'Republic of The Gambia', 'GM', 'Gambian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GE', 'en-ZA', 80, 'Georgia', 'Georgia', 'Georgia', 'GE', 'Georgian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('DE', 'en-ZA', 81, 'Federal Republic of Germany', 'Germany', 'Federal Republic of Germany', 'DE', 'German');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GH', 'en-ZA', 82, 'Republic of Ghana', 'Ghana', 'Republic of Ghana', 'GH', 'Ghanaian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GI', 'en-ZA', 83, 'Gibraltar', 'Gibraltar', 'Gibraltar', 'GB', 'Gibraltarian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GR', 'en-ZA', 84, 'Hellenic Republic', 'Greece', 'Hellenic Republic', 'GR', 'Greek');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GL', 'en-ZA', 85, 'Greenland', 'Greenland', 'Greenland', 'GL', 'Greenlandic');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GD', 'en-ZA', 86, 'Grenada', 'Grenada', 'Grenada', 'GD', 'Grenadian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GP', 'en-ZA', 87, 'Guadeloupe', 'Guadeloupe', 'Guadeloupe', 'FR', 'Guadeloupean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GU', 'en-ZA', 88, 'Guam', 'Guam', 'Guam', 'US', 'Guamanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GT', 'en-ZA', 89, 'Republic of Guatemala', 'Guatemala', 'Republic of Guatemala', 'GT', 'Guatemalan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GG', 'en-ZA', 90, 'Guernsey', 'Guernsey', 'Guernsey', 'GB', 'British');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GN', 'en-ZA', 91, 'Republic of Guinea', 'Guinea', 'Republic of Guinea', 'GN', 'Guinean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GW', 'en-ZA', 92, 'Republic of Guinea-Bissau', 'Guinea-Bissau', 'Republic of Guinea-Bissau', 'GW', 'Guinean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GY', 'en-ZA', 93, 'Co-operative Republic of Guyana', 'Guyana', 'Co-operative Republic of Guyana', 'GY', 'Guyanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HT', 'en-ZA', 94, 'Republic of Haiti', 'Haiti', 'Republic of Haiti', 'HT', 'Haitian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VA', 'en-ZA', 95, 'Holy See', 'Holy See', 'Holy See', 'VA', 'Papal');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HN', 'en-ZA', 96, 'Republic of Honduras', 'Honduras', 'Republic of Honduras', 'HN', 'Honduran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HK', 'en-ZA', 97, 'Hong Kong', 'Hong Kong', 'Hong Kong Special Administrative Region of the People''s Republic of China', 'CN', 'Hong Konger');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('HU', 'en-ZA', 98, 'Hungary', 'Hungary', 'Hungary', 'HU', 'Hungarian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IS', 'en-ZA', 99, 'Iceland', 'Iceland', 'Iceland', 'IS', 'Icelander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IN', 'en-ZA', 100, 'Republic of India', 'India', 'Republic of India', 'IN', 'Indian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ID', 'en-ZA', 101, 'Republic of Indonesia', 'Indonesia', 'Republic of Indonesia', 'ID', 'Indonesian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IR', 'en-ZA', 102, 'Islamic Republic of Iran', 'Iran', 'Islamic Republic of Iran', 'IR', 'Iranian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IQ', 'en-ZA', 103, 'Republic of Iraq', 'Iraq', 'Republic of Iraq', 'IQ', 'Iraqi');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IE', 'en-ZA', 104, 'Republic of Ireland', 'Ireland', 'Republic of Ireland', 'IE', 'Irish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IM', 'en-ZA', 105, 'Isle of Man', 'Isle of Man', 'Isle of Man', 'GB', 'Manx');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IL', 'en-ZA', 106, 'State of Israel', 'Israel', 'State of Israel', 'IL', 'Israeli');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('IT', 'en-ZA', 107, 'Italian Republic', 'Italy', 'Italian Republic', 'IT', 'Italian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JM', 'en-ZA', 108, 'Jamaica', 'Jamaica', 'Jamaica', 'JM', 'Jamaican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JP', 'en-ZA', 109, 'Japan', 'Japan', 'Japan', 'JP', 'Japanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JE', 'en-ZA', 110, 'Bailiwick of Jersey', 'Jersey', 'Bailiwick of Jersey', 'GB', 'British');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('JO', 'en-ZA', 111, 'Hashemite Kingdom of Jordan', 'Jordan', 'Hashemite Kingdom of Jordan', 'JO', 'Jordanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KZ', 'en-ZA', 112, 'Republic of Kazakhstan', 'Kazakhstan', 'Republic of Kazakhstan', 'KZ', 'Kazakhstani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KE', 'en-ZA', 113, 'Republic of Kenya', 'Kenya', 'Republic of Kenya', 'KE', 'Kenyan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KI', 'en-ZA', 114, 'Republic of Kiribati', 'Kiribati', 'Republic of Kiribati', 'KI', 'I-Kiribati');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KP', 'en-ZA', 115, 'Democratic People''s Republic of Korea', 'North Korea', 'Democratic People''s Republic of Korea', 'KP', 'North Korean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KR', 'en-ZA', 116, 'Republic of Korea', 'South Korea', 'Republic of Korea', 'KR', 'South Korean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KW', 'en-ZA', 117, 'State of Kuwait', 'Kuwait', 'State of Kuwait', 'KW', 'Kuwaiti');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KG', 'en-ZA', 118, 'Kyrgyz Republic', 'Kyrgyzstan', 'Kyrgyz Republic', 'KG', 'Kyrgyzstani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LA', 'en-ZA', 119, 'Lao People''s Democratic Republic', 'Laos', 'Lao People''s Democratic Republic', 'LA', 'Laotian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LV', 'en-ZA', 120, 'Republic of Latvia', 'Latvia', 'Republic of Latvia', 'LV', 'Latvian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LB', 'en-ZA', 121, 'Lebanese Republic', 'Lebanon', 'Lebanese Republic', 'LB', 'Lebanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LS', 'en-ZA', 122, 'Kingdom of Lesotho', 'Lesotho', 'Kingdom of Lesotho', 'LS', 'Mosotho');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LR', 'en-ZA', 123, 'Republic of Liberia', 'Liberia', 'Republic of Liberia', 'LR', 'Liberian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LY', 'en-ZA', 124, 'State of Libya', 'Libya', 'State of Libya', 'LY', 'Libyan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LI', 'en-ZA', 125, 'Principality of Liechtenstein', 'Liechtenstein', 'Principality of Liechtenstein', 'LI', 'Liechtensteiner');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LT', 'en-ZA', 126, 'Republic of Lithuania', 'Lithuania', 'Republic of Lithuania', 'LT', 'Lithunian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LU', 'en-ZA', 127, 'Grand Duchy of Luxembourg', 'Luxembourg', 'Grand Duchy of Luxembourg', 'LU', 'Luxembourger');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MO', 'en-ZA', 128, 'Macao', 'Macao', 'Macao', 'CN', 'Macanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MG', 'en-ZA', 129, 'Republic of Madagascar', 'Madagascar', 'Republic of Madagascar', 'MG', 'Malagasy');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MW', 'en-ZA', 130, 'Republic of Malawi', 'Malawi', 'Republic of Malawi', 'MW', 'Malawian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MY', 'en-ZA', 131, 'Malaysia', 'Malaysia', 'Malaysia', 'MY', 'Malaysian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MV', 'en-ZA', 132, 'Republic of Maldives', 'Maldives', 'Republic of Maldives', 'MV', 'Maldivan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ML', 'en-ZA', 133, 'Republic of Mali', 'Mali', 'Republic of Mali', 'ML', 'Malian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MT', 'en-ZA', 134, 'Republic of Malta', 'Malta', 'Republic of Malta', 'MT', 'Maltese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MH', 'en-ZA', 135, 'Republic of the Marshall Islands', 'Marshall Islands', 'Republic of the Marshall Islands', 'MH', 'Marshallese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MQ', 'en-ZA', 136, 'Martinique', 'Martinique', 'Martinique', 'FR', 'Martinican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MR', 'en-ZA', 137, 'Islamic Republic of Mauritania', 'Mauritania', 'Islamic Republic of Mauritania', 'MR', 'Mauritanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MU', 'en-ZA', 138, 'Republic of Mauritius', 'Mauritius', 'Republic of Mauritius', 'MU', 'Mauritian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('YT', 'en-ZA', 139, 'Department of Mayotte', 'Mayotte', 'Department of Mayotte', 'FR', 'Mahoran');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MX', 'en-ZA', 140, 'United Mexican States', 'Mexico', 'United Mexican States', 'MX', 'Mexican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('FM', 'en-ZA', 141, 'Federated States of Micronesia', 'Micronesia', 'Federated States of Micronesia', 'FM', 'Micronesian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MD', 'en-ZA', 142, 'Republic of Moldova', 'Moldova', 'Republic of Moldova', 'MD', 'Moldovan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MC', 'en-ZA', 143, ' Principality of Monaco', 'Monaco', ' Principality of Monaco', 'MC', 'Monacan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MN', 'en-ZA', 144, 'Mongolia', 'Mongolia', 'Mongolia', 'MN', 'Mongolian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ME', 'en-ZA', 145, 'Montenegro', 'Montenegro', 'Montenegro', 'ME', 'Montenegrin');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MS', 'en-ZA', 146, 'Montserrat', 'Montserrat', 'Montserrat', 'GB', 'Montserratian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MA', 'en-ZA', 147, 'Kingdom of Morocco', 'Morocco', 'Kingdom of Morocco', 'MA', 'Moroccan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MZ', 'en-ZA', 148, 'Republic of Mozambique', 'Mozambique', 'Republic of Mozambique', 'MZ', 'Mozambican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MM', 'en-ZA', 149, 'Republic of the Union of Myanmar', 'Myanmar', 'Republic of the Union of Myanmar', 'MM', 'Myanma');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NA', 'en-ZA', 150, 'Republic of Namibia', 'Namibia', 'Republic of Namibia', 'NA', 'Namibian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NR', 'en-ZA', 151, 'Republic of Nauru', 'Nauru', 'Republic of Nauru', 'NR', 'Nauruan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NP', 'en-ZA', 152, 'Federal Democratic Republic of Nepal', 'Nepal', 'Federal Democratic Republic of Nepal', 'NP', 'Nepalese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NL', 'en-ZA', 153, 'Netherlands', 'Netherlands', 'Netherlands', 'NL', 'Dutch');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NC', 'en-ZA', 154, 'New Caledonia', 'New Caledonia', 'New Caledonia', 'FR', 'New Caledonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NZ', 'en-ZA', 155, 'New Zealand', 'New Zealand', 'New Zealand', 'NZ', 'New Zealander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NI', 'en-ZA', 156, 'Republic of Nicaragua', 'Nicaragua', 'Republic of Nicaragua', 'NI', 'Nicaraguan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NE', 'en-ZA', 157, 'Republic of the Niger', 'Niger', 'Republic of the Niger', 'NE', 'Nigerien');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NG', 'en-ZA', 158, 'Federal Republic of Nigeria', 'Nigeria', 'Federal Republic of Nigeria', 'NG', 'Nigerian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NU', 'en-ZA', 159, 'Niue', 'Niue', 'Niue', 'NU', 'Niuean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NF', 'en-ZA', 160, 'Norfolk Island', 'Norfolk Island', 'Norfolk Island', 'AU', 'Norfolk Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MP', 'en-ZA', 161, 'Commonwealth of the Northern Mariana Islands', 'Northern Mariana Islands', 'Commonwealth of the Northern Mariana Islands', 'US', 'Northern Mariana Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('NO', 'en-ZA', 162, 'Kingdom of Norway', 'Norway', 'Kingdom of Norway', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('OM', 'en-ZA', 163, 'Sultanate of Oman', 'Oman', 'Sultanate of Oman', 'OM', 'Omani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PK', 'en-ZA', 164, 'Islamic Republic of Pakistan', 'Pakistan', 'Islamic Republic of Pakistan', 'PK', 'Pakistani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PW', 'en-ZA', 165, 'Republic of Palau', 'Palau', 'Republic of Palau', 'PW', 'Palauan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PS', 'en-ZA', 166, 'State of Palestine', 'Palestine', 'State of Palestine', 'PS', 'Palestinian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PA', 'en-ZA', 167, 'Republic of Panama', 'Panama', 'Republic of Panama', 'PA', 'Panamanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PG', 'en-ZA', 168, 'Independent State of Papua New Guinea', 'Papua New Guinea', 'Independent State of Papua New Guinea', 'PG', 'Papua New Guinean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PY', 'en-ZA', 169, 'Republic of Paraguay', 'Paraguay', 'Republic of Paraguay', 'PY', 'Paraguayan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PE', 'en-ZA', 170, 'Republic of Peru', 'Peru', 'Republic of Peru', 'PE', 'Peruvian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PH', 'en-ZA', 171, 'Republic of the Philippines', 'Philippines', 'Republic of the Philippines', 'PH', 'Filipino');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PN', 'en-ZA', 172, 'Pitcairn Islands', 'Pitcairn Islands', 'Pitcairn Islands', 'GB', 'Pitcairn Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PL', 'en-ZA', 173, 'Republic of Poland', 'Poland', 'Republic of Poland', 'PL', 'Polish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PT', 'en-ZA', 174, 'Portuguese Republic', 'Portugal', 'Portuguese Republic', 'PT', 'Portuguese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PR', 'en-ZA', 175, 'Commonwealth of Puerto Rico', 'Puerto Rico', 'Commonwealth of Puerto Rico', 'PR', 'Puerto Rican');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('QA', 'en-ZA', 176, 'State of Qatar', 'Qatar', 'State of Qatar', 'QA', 'Qatari');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MK', 'en-ZA', 177, 'Republic of North Macedonia', 'North Macedonia', 'Republic of North Macedonia', 'MK', 'Macedonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RO', 'en-ZA', 178, 'Romania', 'Romania', 'Romania', 'RO', 'Romanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RU', 'en-ZA', 179, 'Russian Federation', 'Russia', 'Russian Federation', 'RU', 'Russian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RW', 'en-ZA', 180, 'Republic of Rwanda', 'Rwanda', 'Republic of Rwanda', 'RW', 'Rwandan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RE', 'en-ZA', 181, 'Réunion', 'Réunion', 'Réunion', 'FR', 'French');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('BL', 'en-ZA', 182, 'Saint Barthélemy', 'St. Barthélemy', 'Saint Barthélemy', 'FR', 'Barthélemois');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('KN', 'en-ZA', 183, 'Federation of Saint Christopher and Nevis', 'St. Kitts and Nevis', 'Federation of Saint Christopher and Nevis', 'KN', 'Kittian or Nevisian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LC', 'en-ZA', 184, 'Saint Lucia', 'St. Lucia', 'Saint Lucia', 'LC', 'Saint Lucian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('MF', 'en-ZA', 185, 'Saint Martin (French part)', 'St. Martin (French part)', 'Saint Martin (French part)', 'FR', 'French');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('PM', 'en-ZA', 186, 'Saint Pierre and Miquelon', 'St. Pierre and Miquelon', 'Saint Pierre and Miquelon', 'FR', 'Miquelonnais');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VC', 'en-ZA', 187, 'Saint Vincent and the Grenadines', 'Saint Vincent', 'Saint Vincent and the Grenadines', 'VC', 'Saint Vincentian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('WS', 'en-ZA', 188, 'Independent State of Samoa', 'Samoa', 'Independent State of Samoa', 'WS', 'Samoan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SM', 'en-ZA', 189, 'Republic of San Marino', 'San Marino', 'Republic of San Marino', 'SM', 'Sammarinese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ST', 'en-ZA', 190, 'Democratic Republic of São Tomé and Príncipe', 'Sao Tome and Principe', 'Democratic Republic of São Tomé and Príncipe', 'ST', 'São Tomean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SA', 'en-ZA', 191, 'Kingdom of Saudi Arabia', 'Saudi Arabia', 'Kingdom of Saudi Arabia', 'SA', 'Saudi Arabian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SN', 'en-ZA', 192, 'Republic of Senegal', 'Senegal', 'Republic of Senegal', 'SN', 'Senegalese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('RS', 'en-ZA', 193, 'Republic of Serbia', 'Serbia', 'Republic of Serbia', 'RS', 'Serbian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SC', 'en-ZA', 194, 'Republic of Seychelles', 'Seychelles', 'Republic of Seychelles', 'SC', 'Seychellois');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SL', 'en-ZA', 195, 'Republic of Sierra Leone', 'Sierra Leone', 'Republic of Sierra Leone', 'SL', 'Sierra Leonean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SG', 'en-ZA', 196, 'Republic of Singapore', 'Singapore', 'Republic of Singapore', 'SG', 'Singaporean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SX', 'en-ZA', 197, 'Sint Maarten (Dutch part)', 'Sint Maarten', 'Sint Maarten (Dutch part)', 'NL', 'Dutch');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SK', 'en-ZA', 198, 'Slovak Republic', 'Slovakia', 'Slovak Republic', 'SK', 'Slovak');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SI', 'en-ZA', 199, 'Republic of Slovenia', 'Slovenia', 'Republic of Slovenia', 'SI', 'Slovene');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SB', 'en-ZA', 200, 'Solomon Islands', 'Solomon Islands', 'Solomon Islands', 'SB', 'Solomon Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SO', 'en-ZA', 201, 'Federal Republic of Somalia', 'Somalia', 'Federal Republic of Somalia', 'SO', 'Somali');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZA', 'en-ZA', 202, 'Republic of South Africa', 'South Africa', 'Republic of South Africa', 'ZA', 'South African');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SS', 'en-ZA', 203, 'Republic of South Sudan', 'South Sudan', 'Republic of South Sudan', 'SS', 'Sudanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ES', 'en-ZA', 204, 'Kingdom of Spain', 'Spain', 'Kingdom of Spain', 'ES', 'Spanish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('LK', 'en-ZA', 205, 'Democratic Socialist Republic of Sri Lanka', 'Sri Lanka', 'Democratic Socialist Republic of Sri Lanka', 'LK', 'Sri Lankan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SD', 'en-ZA', 206, 'Republic of the Sudan', 'Sudan', 'Republic of the Sudan', 'SD', 'Sudanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SR', 'en-ZA', 207, 'Republic of Suriname', 'Suriname', 'Republic of Suriname', 'SR', 'Surinamese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SJ', 'en-ZA', 208, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'NO', 'Norwegian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SZ', 'en-ZA', 209, 'Kingdom of Eswatini', 'Swaziland', 'Kingdom of Eswatini', 'SZ', 'Swazi');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SE', 'en-ZA', 210, 'Kingdom of Sweden', 'Sweden', 'Kingdom of Sweden', 'SE', 'Swedish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('CH', 'en-ZA', 211, 'Switzerland', 'Switzerland', 'Switzerland', 'CH', 'Swiss');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('SY', 'en-ZA', 212, 'Syrian Arab Republic', 'Syria', 'Syrian Arab Republic', 'SY', 'Syrian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TW', 'en-ZA', 213, 'Republic of China', 'Taiwan', 'Republic of China', 'TW', 'Taiwanese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TJ', 'en-ZA', 214, 'Republic of Tajikistan', 'Tajikistan', 'Republic of Tajikistan', 'TJ', 'Tajikistani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TZ', 'en-ZA', 215, 'United Republic of Tanzania', 'Tanzania', 'United Republic of Tanzania', 'TZ', 'Tanzanian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TH', 'en-ZA', 216, 'Kingdom of Thailand', 'Thailand', 'Kingdom of Thailand', 'TH', 'Thai');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TL', 'en-ZA', 217, 'Democratic Republic of Timor-Leste', 'East Timor', 'Democratic Republic of Timor-Leste', 'TL', 'Timorese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TG', 'en-ZA', 218, 'Togolese Republic', 'Togo', 'Togolese Republic', 'TG', 'Togolese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TK', 'en-ZA', 219, 'Tokelau Islands', 'Tokelau', 'Tokelau Islands', 'NZ', 'Tokelauan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TO', 'en-ZA', 220, 'Kingdom of Tonga', 'Tonga', 'Kingdom of Tonga', 'TO', 'Tongan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TT', 'en-ZA', 221, 'Republic of Trinidad and Tobago', 'Trinidad and Tobago', 'Republic of Trinidad and Tobago', 'TT', 'Trinidadian and Tobagonian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TN', 'en-ZA', 222, 'Republic of Tunisia', 'Tunisia', 'Republic of Tunisia', 'TN', 'Tunisian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TR', 'en-ZA', 223, 'Republic of Turkey', 'Turkey', 'Republic of Turkey', 'TR', 'Turkish');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TM', 'en-ZA', 224, 'Turkmenistan', 'Turkmenistan', 'Turkmenistan', 'TM', 'Turkmen');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TC', 'en-ZA', 225, 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'GB', 'Turks and Caicos Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('TV', 'en-ZA', 226, 'Tuvalu', 'Tuvalu', 'Tuvalu', 'TV', 'Tuvaluan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UG', 'en-ZA', 227, 'Republic of Uganda', 'Uganda', 'Republic of Uganda', 'UG', 'Ugandan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UA', 'en-ZA', 228, 'Ukraine', 'Ukraine', 'Ukraine', 'UA', 'Ukrainian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AE', 'en-ZA', 229, 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates', 'AE', 'Emirati');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('GB', 'en-ZA', 230, 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland', 'GB', 'British');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UM', 'en-ZA', 231, 'United States Minor Outlying Islands', 'US Minor Outlying Islands', 'United States Minor Outlying Islands', 'UM', 'American');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('US', 'en-ZA', 232, 'United States of America', 'United States of America', 'United States of America', 'US', 'American');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UY', 'en-ZA', 233, 'Oriental Republic of Uruguay', 'Uruguay', 'Oriental Republic of Uruguay', 'UY', 'Uruguayan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('UZ', 'en-ZA', 234, 'Republic of Uzbekistan', 'Uzbekistan', 'Republic of Uzbekistan', 'UZ', 'Uzbekistani');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VU', 'en-ZA', 235, 'Republic of Vanuatu', 'Vanuatu', 'Republic of Vanuatu', 'VU', 'Ni-Vanuatu');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VE', 'en-ZA', 236, 'Bolivarian Republic of Venezuela', 'Venezuela', 'Bolivarian Republic of Venezuela', 'VE', 'Venezuelan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VN', 'en-ZA', 237, 'Socialist Republic of Vietnam', 'Vietnam', 'Socialist Republic of Vietnam', 'VN', 'Vietnamese');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VG', 'en-ZA', 238, 'Virgin Islands', 'British Virgin Islands', 'Virgin Islands', 'GB', 'Virgin Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('VI', 'en-ZA', 239, 'United States Virgin Islands', 'U.S. Virgin Islands', 'United States Virgin Islands', 'US', 'Virgin Islander');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('WF', 'en-ZA', 240, 'Territory of the Wallis and Futuna Islands', 'Wallis and Futuna Islands', 'Territory of the Wallis and Futuna Islands', 'FR', 'Wallisian or Futunan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('EH', 'en-ZA', 241, 'Western Sahara', 'Western Sahara', 'Western Sahara', '', 'Western Saharan');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('YE', 'en-ZA', 242, 'Republic of Yemen', 'Yemen', 'Republic of Yemen', 'YE', 'Yemeni');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZM', 'en-ZA', 243, 'Republic of Zambia', 'Zambia', 'Republic of Zambia', 'ZM', 'Zambian');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZW', 'en-ZA', 244, 'Republic of Zimbabwe', 'Zimbabwe', 'Republic of Zimbabwe', 'ZW', 'Zimbabwean');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('AX', 'en-ZA', 245, 'Åland Islands', 'Åland Islands', 'Åland Islands', 'FI', 'Ålandic');
INSERT INTO reference.countries (code, locale_id, sort_index, name, short_name, description, sovereign_state, nationality)
  VALUES ('ZZ', 'en-ZA', 999, 'Unknown', 'Unknown', 'Unknown', 'ZA', 'Unknown');


INSERT INTO reference.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('employed', 'en-US', 1, 'Employed', 'Employed');
INSERT INTO reference.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-US', 2, 'Other', 'Other');
INSERT INTO reference.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');
  
INSERT INTO reference.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('employed', 'en-ZA', 1, 'Employed', 'Employed');
INSERT INTO reference.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-ZA', 2, 'Other', 'Other');
INSERT INTO reference.employment_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'full_time', 'en-US', 1, 'Full-time', 'Full-time');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'part_time', 'en-US', 2, 'Part-time', 'Part-time');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'contractor', 'en-US', 3, 'Contractor', 'Contractor');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'self_employed', 'en-US', 4, 'Self-employed', 'Self-employed');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'retired_pensioner', 'en-US', 5, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'unemployed', 'en-US', 6, 'Unemployed', 'Unemployed');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'home_executive', 'en-US', 7, 'Home Executive', 'Home Executive');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'student', 'en-US', 8, 'Student', 'Student');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'minor', 'en-US', 9, 'Minor', 'Minor');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'not_specified', 'en-US', 10, 'Not Specified', 'Not Specified');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'full_time', 'en-ZA', 1, 'Full-time', 'Full-time');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'part_time', 'en-ZA', 2, 'Part-time', 'Part-time');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'contractor', 'en-ZA', 3, 'Contractor', 'Contractor');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('employed', 'self_employed', 'en-ZA', 4, 'Self-employed', 'Self-employed');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'retired_pensioner', 'en-ZA', 5, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'unemployed', 'en-ZA', 6, 'Unemployed', 'Unemployed');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'home_executive', 'en-ZA', 7, 'Home Executive', 'Home Executive');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'student', 'en-ZA', 8, 'Student', 'Student');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'minor', 'en-ZA', 9, 'Minor', 'Minor');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'not_specified', 'en-ZA', 10, 'Not Specified', 'Not Specified');
INSERT INTO reference.employment_types (employment_status, code, locale_id, sort_index, name, description)
  VALUES ('other', 'unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('male', 'en-US', 1, 'Male', 'Male');
INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('female', 'en-US', 2, 'Female', 'Female');
INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('transgender', 'en-US', 3, 'Transgender', 'Transgender');
INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('non_binary', 'en-US', 4, 'Non-binary', 'Non-binary');
INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('male', 'en-ZA', 1, 'Male', 'Male');
INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('female', 'en-ZA', 2, 'Female', 'Female');
INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('transgender', 'en-ZA', 3, 'Transgender', 'Transgender');
INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('non_binary', 'en-ZA', 4, 'Non-binary', 'Non-binary');
INSERT INTO reference.genders (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_id_book', 'en-US', 10001, 'South African ID Book', 'South African ID Book', 'ZA', 'person');
INSERT INTO reference.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_id_card', 'en-US', 10002, 'South African ID Card', 'South African ID Card', 'ZA', 'person');
INSERT INTO reference.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_drivers_license', 'en-US', 10003, 'South African Driver''s License', 'South African Driver''s License', 'ZA', 'person');
INSERT INTO reference.identity_document_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('passport', 'en-US', 99999, 'Passport', 'Passport', 'person');

INSERT INTO reference.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_id_book', 'en-ZA', 10001, 'South African ID Book', 'South African ID Book', 'ZA', 'person');
INSERT INTO reference.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_id_card', 'en-ZA', 10002, 'South African ID Card', 'South African ID Card', 'ZA', 'person');
INSERT INTO reference.identity_document_types (code, locale_id, sort_index, name, description, country_of_issue, party_types)
  VALUES ('za_drivers_license', 'en-ZA', 10003, 'South African Driver''s License', 'South African Driver''s License', 'ZA', 'person');
INSERT INTO reference.identity_document_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('passport', 'en-ZA', 99999, 'Passport', 'Passport', 'person');


-- See: https://en.wikipedia.org/wiki/List_of_official_languages_by_country_and_territory
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AB', 'en-US', 1, 'Abkhaz', 'Abkhaz', 'Abkhaz');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AF', 'en-US', 2, 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AM', 'en-US', 3, 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AR', 'en-US', 4, 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AY', 'en-US', 5, 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AZ', 'en-US', 6, 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BE', 'en-US', 7, 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BG', 'en-US', 8, 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BI', 'en-US', 9, 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BN', 'en-US', 10, 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BS', 'en-US', 11, 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('CS', 'en-US', 12, 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('DA', 'en-US', 13, 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('DE', 'en-US', 14, 'German', 'German', 'German');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('DZ', 'en-US', 15, 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('EL', 'en-US', 16, 'Greek', 'Greek', 'Greek');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('EN', 'en-US', 17, 'English', 'English', 'English');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ES', 'en-US', 18, 'Spanish', 'Spanish', 'Spanish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ET', 'en-US', 19, 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('FI', 'en-US', 20, 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('FJ', 'en-US', 21, 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('FR', 'en-US', 22, 'French', 'French', 'French');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('GN', 'en-US', 23, 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HE', 'en-US', 24, 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HI', 'en-US', 25, 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HO', 'en-US', 26, 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HR', 'en-US', 27, 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HT', 'en-US', 28, 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HU', 'en-US', 29, 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HY', 'en-US', 30, 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ID', 'en-US', 31, 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('IS', 'en-US', 32, 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('IT', 'en-US', 33, 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('JA', 'en-US', 34, 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KA', 'en-US', 35, 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KK', 'en-US', 36, 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KM', 'en-US', 37, 'Khmer', 'Khmer', 'Khmer');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KO', 'en-US', 38, 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KY', 'en-US', 39, 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('LB', 'en-US', 40, 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('LO', 'en-US', 41, 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('LT', 'en-US', 42, 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('LV', 'en-US', 43, 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MG', 'en-US', 44, 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MI', 'en-US', 45, 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MK', 'en-US', 46, 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MN', 'en-US', 47, 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MR', 'en-US', 48, 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MS', 'en-US', 49, 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MT', 'en-US', 50, 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MY', 'en-US', 51, 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ND', 'en-US', 52, 'Ndebele', 'Ndebele', 'Ndebele, Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NE', 'en-US', 53, 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NL', 'en-US', 54, 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NO', 'en-US', 55, 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NR', 'en-US', 56, 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NY', 'en-US', 57, 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('OS', 'en-US', 58, 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('PL', 'en-US', 59, 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('PS', 'en-US', 60, 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('PT', 'en-US', 61, 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('QU', 'en-US', 62, 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('RM', 'en-US', 63, 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('RO', 'en-US', 64, 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('RU', 'en-US', 65, 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('RW', 'en-US', 66, 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SE', 'en-US', 67, 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SI', 'en-US', 68, 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SK', 'en-US', 69, 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SL', 'en-US', 70, 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SN', 'en-US', 71, 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SO', 'en-US', 72, 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SR', 'en-US', 73, 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ST', 'en-US', 74, 'Sotho', 'Sotho', 'Sotho, Southern Sotho');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SV', 'en-US', 75, 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SW', 'en-US', 76, 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TA', 'en-US', 77, 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TG', 'en-US', 78, 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TH', 'en-US', 79, 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TI', 'en-US', 80, 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TK', 'en-US', 81, 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TN', 'en-US', 82, 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TR', 'en-US', 83, 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TS', 'en-US', 84, 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('UK', 'en-US', 85, 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('UR', 'en-US', 86, 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('UZ', 'en-US', 87, 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('VE', 'en-US', 88, 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('VI', 'en-US', 89, 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('XH', 'en-US', 90, 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZH', 'en-US', 91, 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZU', 'en-US', 92, 'Zulu', 'Zulu', 'Zulu');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZZ', 'en-US', 999, 'Unknown', 'Unknown', 'Unknown');

INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AB', 'en-ZA', 1, 'Abkhaz', 'Abkhaz', 'Abkhaz');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AF', 'en-ZA', 2, 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AM', 'en-ZA', 3, 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AR', 'en-ZA', 4, 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AY', 'en-ZA', 5, 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('AZ', 'en-ZA', 6, 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BE', 'en-ZA', 7, 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BG', 'en-ZA', 8, 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BI', 'en-ZA', 9, 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BN', 'en-ZA', 10, 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('BS', 'en-ZA', 11, 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('CS', 'en-ZA', 12, 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('DA', 'en-ZA', 13, 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('DE', 'en-ZA', 14, 'German', 'German', 'German');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('DZ', 'en-ZA', 15, 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('EL', 'en-ZA', 16, 'Greek', 'Greek', 'Greek');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('EN', 'en-ZA', 17, 'English', 'English', 'English');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ES', 'en-ZA', 18, 'Spanish', 'Spanish', 'Spanish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ET', 'en-ZA', 19, 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('FI', 'en-ZA', 20, 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('FJ', 'en-ZA', 21, 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('FR', 'en-ZA', 22, 'French', 'French', 'French');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('GN', 'en-ZA', 23, 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HE', 'en-ZA', 24, 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HI', 'en-ZA', 25, 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HO', 'en-ZA', 26, 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HR', 'en-ZA', 27, 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HT', 'en-ZA', 28, 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HU', 'en-ZA', 29, 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('HY', 'en-ZA', 30, 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ID', 'en-ZA', 31, 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('IS', 'en-ZA', 32, 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('IT', 'en-ZA', 33, 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('JA', 'en-ZA', 34, 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KA', 'en-ZA', 35, 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KK', 'en-ZA', 36, 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KM', 'en-ZA', 37, 'Khmer', 'Khmer', 'Khmer');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KO', 'en-ZA', 38, 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('KY', 'en-ZA', 39, 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('LB', 'en-ZA', 40, 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('LO', 'en-ZA', 41, 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('LT', 'en-ZA', 42, 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('LV', 'en-ZA', 43, 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MG', 'en-ZA', 44, 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MI', 'en-ZA', 45, 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MK', 'en-ZA', 46, 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MN', 'en-ZA', 47, 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MR', 'en-ZA', 48, 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MS', 'en-ZA', 49, 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MT', 'en-ZA', 50, 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('MY', 'en-ZA', 51, 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ND', 'en-ZA', 52, 'Ndebele', 'Ndebele', 'Ndebele, Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NE', 'en-ZA', 53, 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NL', 'en-ZA', 54, 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NO', 'en-ZA', 55, 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NR', 'en-ZA', 56, 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('NY', 'en-ZA', 57, 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('OS', 'en-ZA', 58, 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('PL', 'en-ZA', 59, 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('PS', 'en-ZA', 60, 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('PT', 'en-ZA', 61, 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('QU', 'en-ZA', 62, 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('RM', 'en-ZA', 63, 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('RO', 'en-ZA', 64, 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('RU', 'en-ZA', 65, 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('RW', 'en-ZA', 66, 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SE', 'en-ZA', 67, 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SI', 'en-ZA', 68, 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SK', 'en-ZA', 69, 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SL', 'en-ZA', 70, 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SN', 'en-ZA', 71, 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SO', 'en-ZA', 72, 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SR', 'en-ZA', 73, 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ST', 'en-ZA', 74, 'Sotho', 'Sotho', 'Sotho, Southern Sotho');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SV', 'en-ZA', 75, 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('SW', 'en-ZA', 76, 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TA', 'en-ZA', 77, 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TG', 'en-ZA', 78, 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TH', 'en-ZA', 79, 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TI', 'en-ZA', 80, 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TK', 'en-ZA', 81, 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TN', 'en-ZA', 82, 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TR', 'en-ZA', 83, 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('TS', 'en-ZA', 84, 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('UK', 'en-ZA', 85, 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('UR', 'en-ZA', 86, 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('UZ', 'en-ZA', 87, 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('VE', 'en-ZA', 88, 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('VI', 'en-ZA', 89, 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('XH', 'en-ZA', 90, 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZH', 'en-ZA', 91, 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZU', 'en-ZA', 92, 'Zulu', 'Zulu', 'Zulu');
INSERT INTO reference.languages (code, locale_id, sort_index, name, short_name, description)
  VALUES ('ZZ', 'en-ZA', 999, 'Unknown', 'Unknown', 'Unknown');


INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('single', 'en-US', 1, 'Single', 'Single');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('married', 'en-US', 2, 'Married', 'Married');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('common_law', 'en-US', 3, 'Common Law', 'Common Law');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('divorced', 'en-US', 4, 'Divorced', 'Divorced');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('widowed', 'en-US', 5, 'Widowed', 'Widowed');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('single', 'en-ZA', 1, 'Single', 'Single');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('married', 'en-ZA', 2, 'Married', 'Married');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('common_law', 'en-ZA', 3, 'Common Law', 'Common Law');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('divorced', 'en-ZA', 4, 'Divorced', 'Divorced');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('widowed', 'en-ZA', 5, 'Widowed', 'Widowed');
INSERT INTO reference.marital_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'unknown', 'en-US', 99, 'Unknown', 'Unknown');
INSERT INTO reference.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'in_community_of_property', 'en-US', 1, 'In Community Of Property', 'In Community Of Property');
INSERT INTO reference.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_without_accrual', 'en-US', 1, 'ANC Without Accrual', 'ANC Without Accrual');
INSERT INTO reference.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_with_accrual', 'en-US', 1, 'ANC With Accrual', 'ANC With Accrual');

INSERT INTO reference.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'unknown', 'en-ZA', 99, 'Unknown', 'Unknown');
INSERT INTO reference.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'in_community_of_property', 'en-ZA', 1, 'In Community Of Property', 'In Community Of Property');
INSERT INTO reference.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_without_accrual', 'en-ZA', 1, 'ANC Without Accrual', 'ANC Without Accrual');
INSERT INTO reference.marriage_types (marital_status, code, locale_id, sort_index, name, description)
  VALUES ('married', 'anc_with_accrual', 'en-ZA', 1, 'ANC With Accrual', 'ANC With Accrual');


INSERT INTO reference.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('none', 'en-US', 1, 'None', 'None');
INSERT INTO reference.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('minor', 'en-US', 2, 'Minor', 'Minor');
INSERT INTO reference.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('emancipated_minor', 'en-US', 3, 'Emancipated Minor', 'Emancipated Minor');
INSERT INTO reference.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('none', 'en-ZA', 1, 'None', 'None');
INSERT INTO reference.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('minor', 'en-ZA', 2, 'Minor', 'Minor');
INSERT INTO reference.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('emancipated_minor', 'en-ZA', 3, 'Emancipated Minor', 'Emancipated Minor');
INSERT INTO reference.minor_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');
  

INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('aunt', 'en-US', 1, 'Aunt', 'Aunt');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('brother', 'en-US', 1, 'Brother', 'Brother');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('cousin', 'en-US', 1, 'Cousin', 'Cousin');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('father', 'en-US', 1, 'Father', 'Father');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('friend', 'en-US', 1, 'Friend', 'Friend');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('grandfather', 'en-US', 1, 'Grandfather', 'Grandfather');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('grandmother', 'en-US', 1, 'Grandmother', 'Grandmother');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('husband', 'en-US', 1, 'Husband', 'Husband');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('life_partner', 'en-US', 1, 'Life Partner', 'Life Partner');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('mother', 'en-US', 1, 'Mother', 'Mother');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('nephew', 'en-US', 1, 'Nephew', 'Nephew');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('niece', 'en-US', 1, 'Niece', 'Niece');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('sister', 'en-US', 1, 'Sister', 'Sister');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('uncle', 'en-US', 1, 'Uncle', 'Uncle');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('wife', 'en-US', 1, 'Wife', 'Wife');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-US', 1, 'Other', 'Other');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('aunt', 'en-ZA', 1, 'Aunt', 'Aunt');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('brother', 'en-ZA', 1, 'Brother', 'Brother');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('cousin', 'en-ZA', 1, 'Cousin', 'Cousin');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('father', 'en-ZA', 1, 'Father', 'Father');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('friend', 'en-ZA', 1, 'Friend', 'Friend');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('grandfather', 'en-ZA', 1, 'Grandfather', 'Grandfather');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('grandmother', 'en-ZA', 1, 'Grandmother', 'Grandmother');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('husband', 'en-ZA', 1, 'Husband', 'Husband');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('life_partner', 'en-ZA', 1, 'Life Partner', 'Life Partner');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('mother', 'en-ZA', 1, 'Mother', 'Mother');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('nephew', 'en-ZA', 1, 'Nephew', 'Nephew');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('niece', 'en-ZA', 1, 'Niece', 'Niece');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('sister', 'en-ZA', 1, 'Sister', 'Sister');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('uncle', 'en-ZA', 1, 'Uncle', 'Uncle');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('wife', 'en-ZA', 1, 'Wife', 'Wife');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-ZA', 1, 'Other', 'Other');
INSERT INTO reference.next_of_kin_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');
  

INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('creative', 'en-US', 1, 'Creative', 'Creative');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('driver', 'en-US', 2, 'Driver', 'Driver');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('executive', 'en-US', 3, 'Executive', 'Executive');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('farmer', 'en-US', 4, 'Farmer', 'Farmer');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('government', 'en-US', 5, 'Government Official', 'Government Official');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('guard', 'en-US', 6, 'Guard', 'Guard');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('labourer', 'en-US', 7, 'Labourer', 'Labourer');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('military_or_police', 'en-US', 8, 'Military / Police', 'Military / Police');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('manager', 'en-US', 9, 'Manager', 'Manager');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('office_staff', 'en-US', 10, 'Office Staff', 'Office Staff');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('pensioner_retired', 'en-US', 11, 'Pensioner / Retired', 'Pensioner / Retired');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('plant_or_machine_operator', 'en-US', 12, 'Plant or Machine Operator or Assembler', 'Plant or Machine Operator or Assembler');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_business', 'en-US', 13, 'Professional: Business', 'Professional: Business');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_education', 'en-US', 14, 'Professional: Education', 'Professional: Education');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_engineering', 'en-US', 15, 'Professional: Engineering', 'Professional: Engineering');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_government', 'en-US', 16, 'Professional: Government', 'Professional: Government');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_legal', 'en-US', 17, 'Professional: Legal', 'Professional: Legal');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_medical', 'en-US', 18, 'Professional: Medical', 'Professional: Medical');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_scientific', 'en-US', 19, 'Professional: Scientific', 'Professional: Scientific');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_transport', 'en-US', 20, 'Professional: Transport', 'Professional: Transport');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('religious_charity', 'en-US', 21, 'Religious / Charitable', 'Religious / Charitable');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('sales', 'en-US', 22, 'Sales', 'Sales');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('semi_skilled_worker', 'en-US', 23, 'Semi-skilled Worker', 'Semi-skilled Worker');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('service', 'en-US', 24, 'Service', 'Service');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('skilled_worker', 'en-US', 25, 'Skilled Worker', 'Skilled Worker');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('technician', 'en-US', 26, 'Technician', 'Technician');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('trade_worker', 'en-US', 27, 'Trade Worker', 'Trade Worker');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('unemployed', 'en-US', 50, 'Unemployed', 'Unemployed');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('creative', 'en-ZA', 1, 'Creative', 'Creative');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('driver', 'en-ZA', 2, 'Driver', 'Driver');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('executive', 'en-ZA', 3, 'Executive', 'Executive');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('farmer', 'en-ZA', 4, 'Farmer', 'Farmer');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('government', 'en-ZA', 5, 'Government Official', 'Government Official');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('guard', 'en-ZA', 6, 'Guard', 'Guard');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('labourer', 'en-ZA', 7, 'Labourer', 'Labourer');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('military_or_police', 'en-ZA', 8, 'Military / Police', 'Military / Police');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('manager', 'en-ZA', 9, 'Manager', 'Manager');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('office_staff', 'en-ZA', 10, 'Office Staff', 'Office Staff');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('pensioner_retired', 'en-ZA', 11, 'Pensioner / Retired', 'Pensioner / Retired');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('plant_or_machine_operator', 'en-ZA', 12, 'Plant or Machine Operator or Assembler', 'Plant or Machine Operator or Assembler');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_business', 'en-ZA', 13, 'Professional: Business', 'Professional: Business');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_education', 'en-ZA', 14, 'Professional: Education', 'Professional: Education');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_engineering', 'en-ZA', 15, 'Professional: Engineering', 'Professional: Engineering');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_government', 'en-ZA', 16, 'Professional: Government', 'Professional: Government');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_legal', 'en-ZA', 17, 'Professional: Legal', 'Professional: Legal');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_medical', 'en-ZA', 18, 'Professional: Medical', 'Professional: Medical');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_scientific', 'en-ZA', 19, 'Professional: Scientific', 'Professional: Scientific');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('professional_transport', 'en-ZA', 20, 'Professional: Transport', 'Professional: Transport');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('religious_charity', 'en-ZA', 21, 'Religious / Charitable', 'Religious / Charitable');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('sales', 'en-ZA', 22, 'Sales', 'Sales');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('semi_skilled_worker', 'en-ZA', 23, 'Semi-skilled Worker', 'Semi-skilled Worker');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('service', 'en-ZA', 24, 'Service', 'Service');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('skilled_worker', 'en-ZA', 25, 'Skilled Worker', 'Skilled Worker');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('technician', 'en-ZA', 26, 'Technician', 'Technician');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('trade_worker', 'en-ZA', 27, 'Trade Worker', 'Trade Worker');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('unemployed', 'en-ZA', 50, 'Unemployed', 'Unemployed');
INSERT INTO reference.occupations (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.party_role_purposes (code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-US', 1, 'Test', 'Test');

INSERT INTO reference.party_role_purposes (code, locale_id, sort_index, name, description)
  VALUES ('test', 'en-ZA', 1, 'Test', 'Test');


INSERT INTO reference.party_role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('employer', 'en-US', 1, 'Employer', 'Employer', 'organization,person');
INSERT INTO reference.party_role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('employee', 'en-US', 2, 'Employee', 'Employee', 'person');
INSERT INTO reference.party_role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('supplier', 'en-US', 3, 'Supplier', 'Supplier', 'organization,person');
INSERT INTO reference.party_role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('vendor', 'en-US', 4, 'Vendor', 'Vendor', 'organization,person');

INSERT INTO reference.party_role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('employer', 'en-ZA', 1, 'Employer', 'Employer', 'organization,person');
INSERT INTO reference.party_role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('employee', 'en-ZA', 2, 'Employee', 'Employee', 'person');
INSERT INTO reference.party_role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('supplier', 'en-ZA', 3, 'Supplier', 'Supplier', 'organization,person');
INSERT INTO reference.party_role_types (code, locale_id, sort_index, name, description, party_types)
  VALUES ('vendor', 'en-ZA', 4, 'Vendor', 'Vendor', 'organization,person');


INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('billing', 'en-US', 1, 'Billing', 'Billing Address', 'organization,person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('business', 'en-US', 2, 'Business', 'Business Address', 'organization');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'en-US', 3, 'Correspondence', 'Correspondence Address', 'organization,person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('delivery', 'en-US', 4, 'Delivery', 'Delivery Address', 'organization,person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('home', 'en-US', 5, 'Home', 'Home Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('main', 'en-US', 6, 'Main', 'Main Address', 'organization');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('permanent', 'en-US', 7, 'Permanent', 'Permanent Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('registered_office', 'en-US', 8, 'Registered Office', 'Registered Office Address', 'organization');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('residential', 'en-US', 9, 'Residential', 'Residential Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('service', 'en-US', 10, 'Service', 'Service Address', 'organization,person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('sole_trader', 'en-US', 11, 'Sole Trader', 'Sole Trader Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('temporary', 'en-US', 12, 'Temporary', 'Temporary Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('work', 'en-US', 13, 'Work', 'Work Address', 'person');

INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('billing', 'en-ZA', 1, 'Billing', 'Billing Address', 'organization,person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('business', 'en-ZA', 2, 'Business', 'Business Address', 'organization');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'en-ZA', 3, 'Correspondence', 'Correspondence Address', 'organization,person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('delivery', 'en-ZA', 4, 'Delivery', 'Delivery Address', 'organization,person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('home', 'en-ZA', 5, 'Home', 'Home Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('main', 'en-ZA', 6, 'Main', 'Main Address', 'organization');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('permanent', 'en-ZA', 7, 'Permanent', 'Permanent Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('registered_office', 'en-ZA', 8, 'Registered Office', 'Registered Office Address', 'organization');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('residential', 'en-ZA', 9, 'Residential', 'Residential Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('service', 'en-ZA', 10, 'Service', 'Service Address', 'organization,person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('sole_trader', 'en-ZA', 11, 'Sole Trader', 'Sole Trader Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('temporary', 'en-ZA', 12, 'Temporary', 'Temporary Address', 'person');
INSERT INTO reference.physical_address_purposes (code, locale_id, sort_index, name, description, party_types)
  VALUES ('work', 'en-ZA', 13, 'Work', 'Work Address', 'person');


INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('building', 'en-US', 1, 'Building', 'Building');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('complex', 'en-US', 2, 'Complex', 'Complex');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('farm', 'en-US', 3, 'Farm', 'Farm');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('international', 'en-US', 4, 'International', 'International');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('postal', 'en-US', 5, 'Postal', 'Postal');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('site', 'en-US', 6, 'Site', 'Site');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('street', 'en-US', 7, 'Street', 'Street');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('unstructured', 'en-US', 99, 'Unstructured', 'Unstructured');

INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('building', 'en-ZA', 1, 'Building', 'Building');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('complex', 'en-ZA', 2, 'Complex', 'Complex');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('farm', 'en-ZA', 3, 'Farm', 'Farm');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('international', 'en-ZA', 4, 'International', 'International');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('postal', 'en-ZA', 5, 'Postal', 'Postal');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('site', 'en-ZA', 6, 'Site', 'Site');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('street', 'en-ZA', 7, 'Street', 'Street');
INSERT INTO reference.physical_address_types (code, locale_id, sort_index, name, description)
  VALUES ('unstructured', 'en-ZA', 99, 'Unstructured', 'Unstructured');


INSERT INTO reference.preference_type_categories (code, locale_id, sort_index, name, description)
  VALUES ('correspondence', 'en-US', 0, 'Correspondence', 'Correspondence');

INSERT INTO reference.preference_type_categories (code, locale_id, sort_index, name, description)
  VALUES ('correspondence', 'en-ZA', 0, 'Correspondence', 'Correspondence');


INSERT INTO reference.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence','correspondence_language', 'en-US', 0, 'Correspondence Language', 'Correspondence Language', 'organization,person');
INSERT INTO reference.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence','time_to_contact', 'en-US', 0, 'Time To Contact', 'Suitable Time To Contact', 'person');

INSERT INTO reference.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'correspondence_language', 'en-ZA', 0, 'Correspondence Language', 'Correspondence Language', 'organization,person');
INSERT INTO reference.preference_types (category, code, locale_id, sort_index, name, description, party_types)
  VALUES ('correspondence', 'time_to_contact', 'en-ZA', 0, 'Time To Contact', 'Suitable Time To Contact', 'person');


INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('black', 'en-US', 1, 'Black', 'Black');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('coloured', 'en-US', 2, 'Coloured', 'Coloured');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('indian', 'en-US', 3, 'Indian', 'Indian');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('asian', 'en-US', 4, 'Asian', 'Asian');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('white', 'en-US', 5, 'White', 'White');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('black', 'en-ZA', 1, 'Black', 'Black');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('coloured', 'en-ZA', 2, 'Coloured', 'Coloured');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('indian', 'en-ZA', 3, 'Indian', 'Indian');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('asian', 'en-ZA', 4, 'Asian', 'Asian');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('white', 'en-ZA', 5, 'White', 'White');
INSERT INTO reference.races (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'EC', 'en-US', 1, 'Eastern Cape', 'Eastern Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'FS', 'en-US', 1, 'Free State', 'Free State');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'GP', 'en-US', 1, 'Gauteng', 'Gauteng');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'LP', 'en-US', 1, 'Limpopo', 'Limpopo');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'MP', 'en-US', 1, 'Mpumalanga', 'Mpumalanga');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'NC', 'en-US', 1, 'Northern Cape', 'Northern Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'NW', 'en-US', 1, 'Northwest', 'Northwest');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'WC', 'en-US', 1, 'Western Cape', 'Western Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'ZN', 'en-US', 1, 'KwaZulu-Natal', 'KwaZulu-Natal');
  
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'EC', 'en-ZA', 1, 'Eastern Cape', 'Eastern Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'FS', 'en-ZA', 1, 'Free State', 'Free State');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'GP', 'en-ZA', 1, 'Gauteng', 'Gauteng');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'LP', 'en-ZA', 1, 'Limpopo', 'Limpopo');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'MP', 'en-ZA', 1, 'Mpumalanga', 'Mpumalanga');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'NC', 'en-ZA', 1, 'Northern Cape', 'Northern Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'NW', 'en-ZA', 1, 'Northwest', 'Northwest');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'WC', 'en-ZA', 1, 'Western Cape', 'Western Cape');
INSERT INTO reference.regions (country, code, locale_id, sort_index, name, description)
  VALUES ('ZA', 'ZN', 'en-ZA', 1, 'KwaZulu-Natal', 'KwaZulu-Natal');
  

INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_business_visa', 'en-US', 1, 'Business Visa', 'Business Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_general_work_visa', 'en-US', 2, 'General Work Visa', 'General Work Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_critical_skills_visa', 'en-US', 3, 'Critical Skills Work Visa', 'Critical Skills Work Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_intra_company_transfer_visa', 'en-US', 4, 'Intra-company Transfer Work Visa', 'Intra-company Transfer Work Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_study_visa', 'en-US', 5, 'Study Visa', 'Study Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_exchange_visa', 'en-US', 6, 'Exchange Visa', 'Exchange Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_retired_persons_visa', 'en-US', 7, 'Retired Persons Visa', 'Retired Persons Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_relatives_visa', 'en-US', 8, 'Relatives Visa', 'Relatives Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_medical_treatment_visa', 'en-US', 9, 'Medical Treatment Visa', 'Medical Treatment Visa', 'ZA');

INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_business_visa', 'en-ZA', 1, 'Business Visa', 'Business Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_general_work_visa', 'en-ZA', 2, 'General Work Visa', 'General Work Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_critical_skills_visa', 'en-ZA', 3, 'Critical Skills Work Visa', 'Critical Skills Work Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_intra_company_transfer_visa', 'en-ZA', 4, 'Intra-company Transfer Work Visa', 'Intra-company Transfer Work Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_study_visa', 'en-ZA', 5, 'Study Visa', 'Study Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_exchange_visa', 'en-ZA', 6, 'Exchange Visa', 'Exchange Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_retired_persons_visa', 'en-ZA', 7, 'Retired Persons Visa', 'Retired Persons Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_relatives_visa', 'en-ZA', 8, 'Relatives Visa', 'Relatives Visa', 'ZA');
INSERT INTO reference.residence_permit_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_medical_treatment_visa', 'en-ZA', 9, 'Medical Treatment Visa', 'Medical Treatment Visa', 'ZA');


INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('citizen', 'en-US', 1, 'Citizen', 'Citizen');
INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('permanent_resident', 'en-US', 2, 'Permanent Resident', 'Permanent Resident');
INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('foreign_national', 'en-US', 3, 'Foreign National', 'Foreign National');
INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('refugee', 'en-US', 4, 'Refugee', 'Refugee');
INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('citizen', 'en-ZA', 1, 'Citizen', 'Citizen');
INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('permanent_resident', 'en-ZA', 2, 'Permanent Resident', 'Permanent Resident');
INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('foreign_national', 'en-ZA', 3, 'Foreign National', 'Foreign National');
INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('refugee', 'en-ZA', 4, 'Refugee', 'Refugee');
INSERT INTO reference.residency_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('owner', 'en-US', 1, 'Owner', 'Owner');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('renter', 'en-US', 2, 'Renter', 'Renter');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('cohabitant', 'en-US', 3, 'Co-Habitant', 'Co-Habitant');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('boarder', 'en-US', 4, 'Boarder', 'Boarder');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('living_with_parents', 'en-US', 5, 'Living with Parents', 'Living with Parents');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('not_specified', 'en-US', 6, 'Not Specified', 'Not Specified');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('owner', 'en-ZA', 1, 'Owner', 'Owner');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('renter', 'en-ZA', 2, 'Renter', 'Renter');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('cohabitant', 'en-ZA', 3, 'Co-Habitant', 'Co-Habitant');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('boarder', 'en-ZA', 4, 'Boarder', 'Boarder');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('living_with_parents', 'en-ZA', 5, 'Living with Parents', 'Living with Parents');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('not_specified', 'en-ZA', 6, 'Not Specified', 'Not Specified');
INSERT INTO reference.residential_types (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('salary', 'en-US', 1, 'Salary', 'Salary');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('commission', 'en-US', 2, 'Commission', 'Commission');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('rental_income', 'en-US', 3, 'Rental Income', 'Rental Income');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('investments', 'en-US', 4, 'Investments', 'Investments');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('retirement_annuity', 'en-US', 5, 'Retirement Annuity', 'Retirement Annuity');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('social_grant', 'en-US', 6, 'Social Grant', 'Social Grant');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('inheritance', 'en-US', 7, 'Inheritance', 'Inheritance');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('maintenance', 'en-US', 8, 'Maintenance', 'Maintenance');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('pension', 'en-US', 9, 'Pension', 'Pension');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('donations', 'en-US', 10, 'Donations', 'Donations');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('allowance', 'en-US', 11, 'Allowance', 'Allowance');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('winnings', 'en-US', 12, 'Winnings', 'Winnings');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('owner_draws', 'en-US', 13, 'Owner Draws', 'Owner Draws');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('bonus_incentive', 'en-US', 14, 'Bonus/Incentive', 'Bonus/Incentive');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('bursary', 'en-US', 15, 'Bursary', 'Bursary');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('settlement', 'en-US', 16, 'Settlement', 'Settlement');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('trust', 'en-US', 17, 'Trust', 'Trust');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-US', 18, 'Other', 'Other');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('salary', 'en-ZA', 1, 'Salary', 'Salary');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('commission', 'en-ZA', 2, 'Commission', 'Commission');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('rental_income', 'en-ZA', 3, 'Rental Income', 'Rental Income');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('investments', 'en-ZA', 4, 'Investments', 'Investments');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('retirement_annuity', 'en-ZA', 5, 'Retirement Annuity', 'Retirement Annuity');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('social_grant', 'en-ZA', 6, 'Social Grant', 'Social Grant');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('inheritance', 'en-ZA', 7, 'Inheritance', 'Inheritance');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('maintenance', 'en-ZA', 8, 'Maintenance', 'Maintenance');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('pension', 'en-ZA', 9, 'Pension', 'Pension');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('donations', 'en-ZA', 10, 'Donations', 'Donations');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('allowance', 'en-ZA', 11, 'Allowance', 'Allowance');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('winnings', 'en-ZA', 12, 'Winnings', 'Winnings');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('owner_draws', 'en-ZA', 13, 'Owner Draws', 'Owner Draws');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('bonus_incentive', 'en-ZA', 14, 'Bonus/Incentive', 'Bonus/Incentive');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('bursary', 'en-ZA', 15, 'Bursary', 'Bursary');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('settlement', 'en-ZA', 16, 'Settlement', 'Settlement');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('trust', 'en-ZA', 17, 'Trust', 'Trust');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('other', 'en-ZA', 18, 'Other', 'Other');
INSERT INTO reference.sources_of_funds (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_income_tax_number', 'en-US', 1, 'Income Tax Number', 'South African Income Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_vat_tax_number', 'en-US', 2, 'VAT Tax Number', 'South African VAT Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_other_tax_number', 'en-US', 3, 'Other Tax Number', 'Other South African Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('uk_tax_number', 'en-US', 1, 'UK Tax Number', 'UK Tax Number', 'GB');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('uk_other_tax_number', 'en-US', 2, 'UK Other Tax Number', 'UK Other Tax Number', 'GB');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('us_taxpayer_id_number', 'en-US', 1, 'US Taxpayer Identification Number', 'US Taxpayer Identification Number', 'US');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('us_other_tax_number', 'en-US', 2, 'US Other Tax Number', 'US Other Tax Number', 'US');
  
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_income_tax_number', 'en-ZA', 1, 'Income Tax Number', 'South African Income Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_vat_tax_number', 'en-ZA', 2, 'VAT Tax Number', 'South African VAT Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('za_other_tax_number', 'en-ZA', 3, 'Other Tax Number', 'Other South African Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('uk_tax_number', 'en-ZA', 1, 'UK Tax Number', 'UK Tax Number', 'GB');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('uk_other_tax_number', 'en-ZA', 2, 'UK Other Tax Number', 'UK Other Tax Number', 'GB');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('us_taxpayer_id_number', 'en-ZA', 1, 'US Taxpayer Identification Number', 'US Taxpayer Identification Number', 'US');
INSERT INTO reference.tax_number_types (code, locale_id, sort_index, name, description, country_of_issue)
  VALUES ('us_other_tax_number', 'en-ZA', 2, 'US Other Tax Number', 'US Other Tax Number', 'US');


INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('anytime', 'en-US', 1, 'Anytime', 'Anytime');
INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('long_hours', 'en-US', 2, 'Long Hours', 'Long Hours');
INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('office_hours', 'en-US', 3, 'Office Hours', 'Office Hours');
INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('do_not_contact', 'en-US', 4, 'Do Not Contact', 'Do Not Contact');
INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('anytime', 'en-ZA', 1, 'Anytime', 'Anytime');
INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('long_hours', 'en-ZA', 2, 'Long Hours', 'Long Hours');
INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('office_hours', 'en-ZA', 3, 'Office Hours', 'Office Hours');
INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('do_not_contact', 'en-ZA', 4, 'Do Not Contact', 'Do Not Contact');
INSERT INTO reference.times_to_contact (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mr', 'en-US', 1, 'Mr', 'Mr.', 'Mr');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('ms', 'en-US', 1, 'Ms', 'Ms.', 'Ms');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('miss', 'en-US', 1, 'Miss', 'Miss', 'Miss');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mrs', 'en-US', 1, 'Mrs', 'Mrs.', 'Mrs');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('doctor', 'en-US', 1, 'Doctor', 'Dr.', 'Doctor');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('the_honorable', 'en-US', 1, 'The Honorable', 'The Hon.', 'The Honorable');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('reverend', 'en-US', 1, 'Reverend', 'Rev.', 'Reverend');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('father', 'en-US', 1, 'Father', 'Fr.', 'Father');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('rabbi', 'en-US', 1, 'Rabbi', 'Rabbi', 'Rabbi');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('professor', 'en-US', 1, 'Professor', 'Prof.', 'Professor');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('advocate', 'en-US', 1, 'Advocate', 'Adv.', 'Advocate');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown', 'Unknown');

INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mr', 'en-ZA', 1, 'Mr', 'Mr.', 'Mr');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('ms', 'en-ZA', 1, 'Ms', 'Ms.', 'Ms');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('miss', 'en-ZA', 1, 'Miss', 'Miss', 'Miss');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('mrs', 'en-ZA', 1, 'Mrs', 'Mrs.', 'Mrs');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('doctor', 'en-ZA', 1, 'Doctor', 'Dr.', 'Doctor');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('the_honorable', 'en-ZA', 1, 'The Honorable', 'The Hon.', 'The Honorable');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('reverend', 'en-ZA', 1, 'Reverend', 'Rev.', 'Reverend');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('father', 'en-ZA', 1, 'Father', 'Fr.', 'Father');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('rabbi', 'en-ZA', 1, 'Rabbi', 'Rabbi', 'Rabbi');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('professor', 'en-ZA', 1, 'Professor', 'Prof.', 'Professor');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('advocate', 'en-ZA', 1, 'Advocate', 'Adv.', 'Advocate');
INSERT INTO reference.titles (code, locale_id, sort_index, name, abbreviation, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown', 'Unknown');


INSERT INTO reference.verification_methods (code, locale_id, sort_index, name, description)
  VALUES ('manual', 'en-US', 1, 'Manual', 'Manual');
INSERT INTO reference.verification_methods (code, locale_id, sort_index, name, description)
  VALUES ('system', 'en-US', 1, 'System', 'System');
INSERT INTO reference.verification_methods (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.verification_methods (code, locale_id, sort_index, name, description)
  VALUES ('manual', 'en-ZA', 1, 'Manual', 'Manual');
INSERT INTO reference.verification_methods (code, locale_id, sort_index, name, description)
  VALUES ('system', 'en-ZA', 1, 'System', 'System');
INSERT INTO reference.verification_methods (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');


INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('not_verified', 'en-US', 1, 'Not Verified', 'Not Verified');
INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('in_progress', 'en-US', 1, 'In Progress', 'In Progress');
INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('complete', 'en-US', 1, 'Complete', 'Complete');
INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('failed', 'en-US', 1, 'Failed', 'Failed');
INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('not_verified', 'en-ZA', 1, 'Not Verified', 'Not Verified');
INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('in_progress', 'en-ZA', 1, 'In Progress', 'In Progress');
INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('complete', 'en-ZA', 1, 'Complete', 'Complete');
INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('failed', 'en-ZA', 1, 'Failed', 'Failed');
INSERT INTO reference.verification_statuses (code, locale_id, sort_index, name, description)
  VALUES ('unknown', 'en-ZA', 99, 'Unknown', 'Unknown');