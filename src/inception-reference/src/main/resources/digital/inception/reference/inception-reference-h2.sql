-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA reference;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE reference.address_types (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX address_types_locale_ix ON reference.address_types(locale);

COMMENT ON COLUMN reference.address_types.code IS 'The code for the address type';

COMMENT ON COLUMN reference.address_types.locale IS 'The Unicode locale identifier for the address type';

COMMENT ON COLUMN reference.address_types.sort_index IS 'The sort index for the address type';

COMMENT ON COLUMN reference.address_types.name IS 'The name of the address type';

COMMENT ON COLUMN reference.address_types.description IS 'The description for the address type';


CREATE TABLE reference.communication_methods (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX communication_methods_locale_ix ON reference.communication_methods(locale);

COMMENT ON COLUMN reference.communication_methods.code IS 'The code for the communication method';

COMMENT ON COLUMN reference.communication_methods.locale IS 'The Unicode locale identifier for the communication method';

COMMENT ON COLUMN reference.communication_methods.sort_index IS 'The sort index for the communication method';

COMMENT ON COLUMN reference.communication_methods.name IS 'The name of the communication method';

COMMENT ON COLUMN reference.communication_methods.description IS 'The description for the communication method';


CREATE TABLE reference.countries (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50) NOT NULL,
  short_name  VARCHAR(30)  NOT NULL DEFAULT '',
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX countries_locale_ix ON reference.countries(locale);

COMMENT ON COLUMN reference.countries.code IS 'The code for the country';

COMMENT ON COLUMN reference.countries.locale IS 'The Unicode locale identifier for the country';

COMMENT ON COLUMN reference.countries.sort_index IS 'The sort index for the country';

COMMENT ON COLUMN reference.countries.name IS 'The name of the country';

COMMENT ON COLUMN reference.countries.short_name IS 'The short name for the country';

COMMENT ON COLUMN reference.countries.description IS 'The description for the country';


CREATE TABLE reference.employment_statuses (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX employment_statuses_locale_ix ON reference.employment_statuses(locale);

COMMENT ON COLUMN reference.employment_statuses.code IS 'The code for the employment status';

COMMENT ON COLUMN reference.employment_statuses.locale IS 'The Unicode locale identifier for the employment status';

COMMENT ON COLUMN reference.employment_statuses.sort_index IS 'The sort index for the employment status';

COMMENT ON COLUMN reference.employment_statuses.name IS 'The name of the employment status';

COMMENT ON COLUMN reference.employment_statuses.description IS 'The description for the employment status';


CREATE TABLE reference.employment_types (
  employment_status VARCHAR(10)  NOT NULL,
  code              VARCHAR(10)  NOT NULL,
  locale            VARCHAR(10)  NOT NULL,
  sort_index        INTEGER      NOT NULL,
  name              VARCHAR(50)  NOT NULL,
  description       VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (employment_status, code, locale),
  CONSTRAINT employment_types_employment_status_fk FOREIGN KEY (employment_status, locale) REFERENCES reference.employment_statuses(code, locale) ON DELETE CASCADE
);

CREATE INDEX employment_types_employment_status_ix ON reference.employment_types(employment_status);

CREATE INDEX employment_types_locale_ix ON reference.employment_types(locale);

COMMENT ON COLUMN reference.employment_types.employment_status IS 'The code for the employment status the employment type is associated with';

COMMENT ON COLUMN reference.employment_types.code IS 'The code for the employment type';

COMMENT ON COLUMN reference.employment_types.locale IS 'The Unicode locale identifier for the employment type';

COMMENT ON COLUMN reference.employment_types.sort_index IS 'The sort index for the employment type';

COMMENT ON COLUMN reference.employment_types.name IS 'The name of the employment type';

COMMENT ON COLUMN reference.employment_types.description IS 'The description for the employment type';


-- See: https://en.wikipedia.org/wiki/ISO/IEC_5218
CREATE TABLE reference.genders (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX genders_locale_ix ON reference.genders(locale);

COMMENT ON COLUMN reference.genders.code IS 'The code for the gender';

COMMENT ON COLUMN reference.genders.locale IS 'The Unicode locale identifier for the gender';

COMMENT ON COLUMN reference.genders.sort_index IS 'The sort index for the gender';

COMMENT ON COLUMN reference.genders.name IS 'The name of the gender';

COMMENT ON COLUMN reference.genders.description IS 'The description for the gender';


CREATE TABLE reference.identity_document_types (
  code             VARCHAR(10)  NOT NULL,
  locale           VARCHAR(10)  NOT NULL,
  sort_index       INTEGER      NOT NULL,
  name             VARCHAR(50) NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue VARCHAR(10)  NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX identity_document_types_locale_ix ON reference.identity_document_types(locale);

CREATE INDEX identity_document_types_country_of_issue_ix ON reference.identity_document_types(country_of_issue);

COMMENT ON COLUMN reference.identity_document_types.code IS 'The code for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.locale IS 'The Unicode locale identifier for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.sort_index IS 'The sort index for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.name IS 'The name of the identity document type';

COMMENT ON COLUMN reference.identity_document_types.description IS 'The description for the identity document type';

COMMENT ON COLUMN reference.identity_document_types.country_of_issue IS 'The optional code identifying the country of issue for the identity document type';


CREATE TABLE reference.languages (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50) NOT NULL,
  short_name  VARCHAR(30)  NOT NULL DEFAULT '',
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX languages_locale_ix ON reference.languages(locale);

COMMENT ON COLUMN reference.languages.code IS 'The code for the language';

COMMENT ON COLUMN reference.languages.locale IS 'The Unicode locale identifier for the language';

COMMENT ON COLUMN reference.languages.sort_index IS 'The sort index for the language';

COMMENT ON COLUMN reference.languages.name IS 'The name of the language';

COMMENT ON COLUMN reference.languages.short_name IS 'The short name for the language';

COMMENT ON COLUMN reference.languages.description IS 'The description for the language';


CREATE TABLE reference.marital_statuses (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX marital_statuses_locale_ix ON reference.marital_statuses(locale);

COMMENT ON COLUMN reference.marital_statuses.code IS 'The code for the marital status';

COMMENT ON COLUMN reference.marital_statuses.locale IS 'The Unicode locale identifier for the marital status';

COMMENT ON COLUMN reference.marital_statuses.sort_index IS 'The sort index for the marital status';

COMMENT ON COLUMN reference.marital_statuses.name IS 'The name of the marital status';

COMMENT ON COLUMN reference.marital_statuses.description IS 'The description for the marital status';


CREATE TABLE reference.marriage_types (
  marital_status VARCHAR(10)  NOT NULL,
  code           VARCHAR(10)  NOT NULL,
  locale         VARCHAR(10)  NOT NULL,
  sort_index     INTEGER      NOT NULL,
  name           VARCHAR(50)  NOT NULL,
  description    VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (marital_status, code, locale),
  CONSTRAINT marriage_types_marital_statuses_fk FOREIGN KEY (marital_status, locale) REFERENCES reference.marital_statuses(code, locale) ON DELETE CASCADE
);

CREATE INDEX marriage_types_marital_status_ix ON reference.marriage_types(marital_status);

CREATE INDEX marriage_types_locale_ix ON reference.marriage_types(locale);

COMMENT ON COLUMN reference.marriage_types.marital_status IS 'The code for the marital status the marriage type is associated with';

COMMENT ON COLUMN reference.marriage_types.code IS 'The code for the marriage type';

COMMENT ON COLUMN reference.marriage_types.locale IS 'The Unicode locale identifier for the marriage type';

COMMENT ON COLUMN reference.marriage_types.sort_index IS 'The sort index for the marriage type';

COMMENT ON COLUMN reference.marriage_types.name IS 'The name of the marriage type';

COMMENT ON COLUMN reference.marriage_types.description IS 'The description for the marriage type';


CREATE TABLE reference.minor_types (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX minor_types_locale_ix ON reference.minor_types(locale);

COMMENT ON COLUMN reference.minor_types.code IS 'The code for the minor type';

COMMENT ON COLUMN reference.minor_types.locale IS 'The Unicode locale identifier for the minor type';

COMMENT ON COLUMN reference.minor_types.sort_index IS 'The sort index for the minor type';

COMMENT ON COLUMN reference.minor_types.name IS 'The name of the minor type';

COMMENT ON COLUMN reference.minor_types.description IS 'The description for the minor type';


CREATE TABLE reference.next_of_kin_types (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX next_of_kin_types_locale_ix ON reference.next_of_kin_types(locale);

COMMENT ON COLUMN reference.next_of_kin_types.code IS 'The code for the next of kin type';

COMMENT ON COLUMN reference.next_of_kin_types.locale IS 'The Unicode locale identifier for the next of kin type';

COMMENT ON COLUMN reference.next_of_kin_types.sort_index IS 'The sort index for the next of kin type';

COMMENT ON COLUMN reference.next_of_kin_types.name IS 'The name of the next of kin type';

COMMENT ON COLUMN reference.next_of_kin_types.description IS 'The description for the next of kin type';


CREATE TABLE reference.occupations (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX occupations_locale_ix ON reference.occupations(locale);

COMMENT ON COLUMN reference.occupations.code IS 'The code for the occupation';

COMMENT ON COLUMN reference.occupations.locale IS 'The Unicode locale identifier for the occupation';

COMMENT ON COLUMN reference.occupations.sort_index IS 'The sort index for the occupation';

COMMENT ON COLUMN reference.occupations.name IS 'The name of the occupation';

COMMENT ON COLUMN reference.occupations.description IS 'The description for the occupation';


CREATE TABLE reference.permit_types (
  code             VARCHAR(10)  NOT NULL,
  locale           VARCHAR(10)  NOT NULL,
  sort_index       INTEGER      NOT NULL,
  name             VARCHAR(50)  NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue VARCHAR(10)  NOT NULL,

  PRIMARY KEY (code, locale),
  CONSTRAINT permit_types_country_fk FOREIGN KEY (country_of_issue, locale) REFERENCES reference.countries(code, locale) ON DELETE CASCADE
);

CREATE INDEX permit_types_locale_ix ON reference.permit_types(locale);

CREATE INDEX permit_types_country_of_issue_ix ON reference.permit_types(country_of_issue);

COMMENT ON COLUMN reference.permit_types.code IS 'The code for the permit type';

COMMENT ON COLUMN reference.permit_types.locale IS 'The Unicode locale identifier for the permit type';

COMMENT ON COLUMN reference.permit_types.sort_index IS 'The sort index for the permit type';

COMMENT ON COLUMN reference.permit_types.name IS 'The name of the permit type';

COMMENT ON COLUMN reference.permit_types.description IS 'The description for the permit type';

COMMENT ON COLUMN reference.permit_types.country_of_issue IS 'The code identifying the country of issue for the permit type';


CREATE TABLE reference.races (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX races_locale_ix ON reference.races(locale);

COMMENT ON COLUMN reference.races.code IS 'The code for the race';

COMMENT ON COLUMN reference.races.locale IS 'The Unicode locale identifier for the race';

COMMENT ON COLUMN reference.races.sort_index IS 'The sort index for the race';

COMMENT ON COLUMN reference.races.name IS 'The name of the race';

COMMENT ON COLUMN reference.races.description IS 'The description for the race';


CREATE TABLE reference.regions (
  country     VARCHAR(10)  NOT NULL,
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (country, code, locale),
  CONSTRAINT regions_country_fk FOREIGN KEY (country, locale) REFERENCES reference.countries(code, locale) ON DELETE CASCADE
);

CREATE INDEX regions_country_ix ON reference.regions(country);

CREATE INDEX regions_locale_ix ON reference.regions(locale);

COMMENT ON COLUMN reference.regions.country IS 'The code for the country the region is associated with';

COMMENT ON COLUMN reference.regions.code IS 'The code for the region';

COMMENT ON COLUMN reference.regions.locale IS 'The Unicode locale identifier for the region';

COMMENT ON COLUMN reference.regions.sort_index IS 'The sort index for the region';

COMMENT ON COLUMN reference.regions.name IS 'The name of the region';

COMMENT ON COLUMN reference.regions.description IS 'The description for the region';


CREATE TABLE reference.residential_statuses (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX residential_statuses_locale_ix ON reference.residential_statuses(locale);

COMMENT ON COLUMN reference.residential_statuses.code IS 'The code for the residential status';

COMMENT ON COLUMN reference.residential_statuses.locale IS 'The Unicode locale identifier for the residential status';

COMMENT ON COLUMN reference.residential_statuses.sort_index IS 'The sort index for the residential status';

COMMENT ON COLUMN reference.residential_statuses.name IS 'The name of the residential status';

COMMENT ON COLUMN reference.residential_statuses.description IS 'The description for the residential status';


CREATE TABLE reference.residential_types (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX residential_types_locale_ix ON reference.residential_types(locale);

COMMENT ON COLUMN reference.residential_types.code IS 'The code for the residential type';

COMMENT ON COLUMN reference.residential_types.locale IS 'The Unicode locale identifier for the residential type';

COMMENT ON COLUMN reference.residential_types.sort_index IS 'The sort index for the residential type';

COMMENT ON COLUMN reference.residential_types.name IS 'The name of the residential type';

COMMENT ON COLUMN reference.residential_types.description IS 'The description for the residential type';


CREATE TABLE reference.sources_of_funds (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX sources_of_funds_locale_ix ON reference.sources_of_funds(locale);

COMMENT ON COLUMN reference.sources_of_funds.code IS 'The code for the source of funds';

COMMENT ON COLUMN reference.sources_of_funds.locale IS 'The Unicode locale identifier for the source of funds';

COMMENT ON COLUMN reference.sources_of_funds.sort_index IS 'The sort index for the source of funds';

COMMENT ON COLUMN reference.sources_of_funds.name IS 'The name of the source of funds';

COMMENT ON COLUMN reference.sources_of_funds.description IS 'The description for the source of funds';


-- ADD FOREIGN KEYS
--
-- FINISH SIC CODES

CREATE TABLE reference.standard_industry_codes (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX standard_industry_codes_locale_ix ON reference.standard_industry_codes(locale);

COMMENT ON COLUMN reference.standard_industry_codes.code IS 'The code for the standard industry code';

COMMENT ON COLUMN reference.standard_industry_codes.locale IS 'The Unicode locale identifier for the standard industry code';

COMMENT ON COLUMN reference.standard_industry_codes.sort_index IS 'The sort index for the standard industry code';

COMMENT ON COLUMN reference.standard_industry_codes.name IS 'The name of the standard industry code';

COMMENT ON COLUMN reference.standard_industry_codes.description IS 'The description for the standard industry code';


CREATE TABLE reference.suitable_times_to_contact (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX suitable_times_to_contact_locale_ix ON reference.suitable_times_to_contact(locale);

COMMENT ON COLUMN reference.suitable_times_to_contact.code IS 'The code for the suitable time to contact';

COMMENT ON COLUMN reference.suitable_times_to_contact.locale IS 'The Unicode locale identifier for the suitable time to contact';

COMMENT ON COLUMN reference.suitable_times_to_contact.sort_index IS 'The sort index for the suitable time to contact';

COMMENT ON COLUMN reference.suitable_times_to_contact.name IS 'The name of the suitable time to contact';

COMMENT ON COLUMN reference.suitable_times_to_contact.description IS 'The description for the suitable time to contact';


CREATE TABLE reference.tax_number_types (
  code             VARCHAR(10)  NOT NULL,
  locale           VARCHAR(10)  NOT NULL,
  sort_index       INTEGER NOT NULL,
  name             VARCHAR(50)  NOT NULL,
  description      VARCHAR(200) NOT NULL DEFAULT '',
  country_of_issue VARCHAR(10)  NOT NULL,

  PRIMARY KEY (code, locale),
  CONSTRAINT tax_number_types_country_fk FOREIGN KEY (country_of_issue, locale) REFERENCES reference.countries(code, locale) ON DELETE CASCADE
);

CREATE INDEX tax_number_types_locale_ix ON reference.tax_number_types(locale);

CREATE INDEX tax_number_types_country_of_issue_ix ON reference.tax_number_types(country_of_issue);

COMMENT ON COLUMN reference.tax_number_types.code IS 'The code for the tax number type';

COMMENT ON COLUMN reference.tax_number_types.locale IS 'The Unicode locale identifier for the tax number type';

COMMENT ON COLUMN reference.tax_number_types.sort_index IS 'The sort index for the tax number type';

COMMENT ON COLUMN reference.tax_number_types.name IS 'The name of the tax number type';

COMMENT ON COLUMN reference.tax_number_types.description IS 'The description for the tax number type';

COMMENT ON COLUMN reference.tax_number_types.country_of_issue IS 'The code identifying the country of issue for the tax number type';


CREATE TABLE reference.titles (
  code         VARCHAR(10)  NOT NULL,
  locale       VARCHAR(10)  NOT NULL,
  sort_index   INTEGER      NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  abbreviation VARCHAR(20) NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX titles_locale_ix ON reference.titles(locale);

COMMENT ON COLUMN reference.titles.code IS 'The code for the title';

COMMENT ON COLUMN reference.titles.locale IS 'The Unicode locale identifier for the title';

COMMENT ON COLUMN reference.titles.sort_index IS 'The sort index for the title';

COMMENT ON COLUMN reference.titles.name IS 'The name of the title';

COMMENT ON COLUMN reference.titles.abbreviation IS 'The abbreviation for the title';

COMMENT ON COLUMN reference.titles.description IS 'The description for the title';


CREATE TABLE reference.verification_methods (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX verification_methods_locale_ix ON reference.verification_methods(locale);

COMMENT ON COLUMN reference.verification_methods.code IS 'The code for the verification method';

COMMENT ON COLUMN reference.verification_methods.locale IS 'The Unicode locale identifier for the verification method';

COMMENT ON COLUMN reference.verification_methods.sort_index IS 'The sort index for the verification method';

COMMENT ON COLUMN reference.verification_methods.name IS 'The name of the verification method';

COMMENT ON COLUMN reference.verification_methods.description IS 'The description for the verification method';


CREATE TABLE reference.verification_statuses (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER      NOT NULL,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code, locale)
);

CREATE INDEX verification_statuses_locale_ix ON reference.verification_statuses(locale);

COMMENT ON COLUMN reference.verification_statuses.code IS 'The code for the verification status';

COMMENT ON COLUMN reference.verification_statuses.locale IS 'The Unicode locale identifier for the verification status';

COMMENT ON COLUMN reference.verification_statuses.sort_index IS 'The sort index for the verification status';

COMMENT ON COLUMN reference.verification_statuses.name IS 'The name of the verification status';

COMMENT ON COLUMN reference.verification_statuses.description IS 'The description for the verification status';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO reference.address_types (code, locale, sort_index, name, description)
  VALUES ('B', 'en-US', 1, 'Building', 'Building');
INSERT INTO reference.address_types (code, locale, sort_index, name, description)
  VALUES ('C', 'en-US', 2, 'Complex', 'Complex');
INSERT INTO reference.address_types (code, locale, sort_index, name, description)
  VALUES ('F', 'en-US', 3, 'Farm', 'Farm');
INSERT INTO reference.address_types (code, locale, sort_index, name, description)
  VALUES ('I', 'en-US', 4, 'International', 'International');
INSERT INTO reference.address_types (code, locale, sort_index, name, description)
  VALUES ('T', 'en-US', 5, 'Site/Township', 'Site/Township');
INSERT INTO reference.address_types (code, locale, sort_index, name, description)
  VALUES ('S', 'en-US', 6, 'Street', 'Street');
INSERT INTO reference.address_types (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unstructured', 'Unstructured');

INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('E', 'en-US', 1, 'E-mail', 'E-mail');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('P', 'en-US', 2, 'Phone', 'Phone');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AF', 'en-US', 1, 'Afghanistan', 'Afghanistan', 'Afghanistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AL', 'en-US', 2, 'Albania', 'Albania', 'Albania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DZ', 'en-US', 3, 'Algeria', 'Algeria', 'Algeria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AS', 'en-US', 4, 'American Samoa', 'American Samoa', 'American Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AD', 'en-US', 5, 'Andorra', 'Andorra', 'Andorra');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AO', 'en-US', 6, 'Angola', 'Angola', 'Angola');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AI', 'en-US', 7, 'Anguilla', 'Anguilla', 'Anguilla');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AQ', 'en-US', 8, 'Antarctica', 'Antarctica', 'Antarctica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AG', 'en-US', 9, 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AR', 'en-US', 10, 'Argentina', 'Argentina', 'Argentina');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AM', 'en-US', 11, 'Armenia', 'Armenia', 'Armenia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AW', 'en-US', 12, 'Aruba', 'Aruba', 'Aruba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AU', 'en-US', 13, 'Australia', 'Australia', 'Australia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AT', 'en-US', 14, 'Austria', 'Austria', 'Austria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AZ', 'en-US', 15, 'Azerbaijan', 'Azerbaijan', 'Azerbaijan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BS', 'en-US', 16, 'Bahamas', 'Bahamas', 'Bahamas');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BH', 'en-US', 17, 'Bahrain', 'Bahrain', 'Bahrain');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BD', 'en-US', 18, 'Bangladesh', 'Bangladesh', 'Bangladesh');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BB', 'en-US', 19, 'Barbados', 'Barbados', 'Barbados');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BY', 'en-US', 20, 'Belarus', 'Belarus', 'Belarus');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BE', 'en-US', 21, 'Belgium', 'Belgium', 'Belgium');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BZ', 'en-US', 22, 'Belize', 'Belize', 'Belize');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BJ', 'en-US', 23, 'Benin', 'Benin', 'Benin');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BM', 'en-US', 24, 'Bermuda', 'Bermuda', 'Bermuda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BT', 'en-US', 25, 'Bhutan', 'Bhutan', 'Bhutan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BO', 'en-US', 26, 'Bolivia, Plurinational State of', 'Bolivia', 'Bolivia, Plurinational State of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BQ', 'en-US', 27, 'Bonaire, Sint Eustatius and Saba', 'Caribbean Netherlands', 'Bonaire, Sint Eustatius and Saba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BA', 'en-US', 28, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BW', 'en-US', 29, 'Botswana', 'Botswana', 'Botswana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BV', 'en-US', 30, 'Bouvet Island', 'Bouvet Island', 'Bouvet Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BR', 'en-US', 31, 'Brazil', 'Brazil', 'Brazil');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BN', 'en-US', 32, 'Brunei Darussalam', 'Brunei Darussalam', 'Brunei Darussalam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BG', 'en-US', 33, 'Bulgaria', 'Bulgaria', 'Bulgaria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BF', 'en-US', 34, 'Burkina Faso', 'Burkina Faso', 'Burkina Faso');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BI', 'en-US', 35, 'Burundi', 'Burundi', 'Burundi');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CV', 'en-US', 36, 'Cabo Verde', 'Cabo Verde', 'Cabo Verde');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KH', 'en-US', 37, 'Cambodia', 'Cambodia', 'Cambodia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CM', 'en-US', 38, 'Cameroon', 'Cameroon', 'Cameroon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CA', 'en-US', 39, 'Canada', 'Canada', 'Canada');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KY', 'en-US', 40, 'Cayman Islands', 'Cayman Islands', 'Cayman Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CF', 'en-US', 41, 'Central African Republic', 'Central African Republic', 'Central African Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TD', 'en-US', 42, 'Chad', 'Chad', 'Chad');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CL', 'en-US', 43, 'Chile', 'Chile', 'Chile');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CN', 'en-US', 44, 'China', 'China', 'China');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CX', 'en-US', 45, 'Christmas Island', 'Christmas Island', 'Christmas Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CC', 'en-US', 46, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CO', 'en-US', 47, 'Colombia', 'Colombia', 'Colombia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KM', 'en-US', 48, 'Comoros', 'Comoros', 'Comoros');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CG', 'en-US', 49, 'Congo', 'Congo', 'Congo');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CD', 'en-US', 50, 'Congo, the Democratic Republic of the', 'Congo, Dem. Rep.', 'Congo, the Democratic Republic of the');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CK', 'en-US', 51, 'Cook Islands', 'Cook Islands', 'Cook Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CR', 'en-US', 52, 'Costa Rica', 'Costa Rica', 'Costa Rica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HR', 'en-US', 53, 'Croatia', 'Croatia', 'Croatia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CU', 'en-US', 54, 'Cuba', 'Cuba', 'Cuba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CW', 'en-US', 55, 'Curaçao', 'Curaçao', 'Curaçao');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CY', 'en-US', 56, 'Cyprus', 'Cyprus', 'Cyprus');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CZ', 'en-US', 57, 'Czech Republic', 'Czech Republic', 'Czechia, Czech Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CI', 'en-US', 58, 'Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DK', 'en-US', 59, 'Denmark', 'Denmark', 'Denmark');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DJ', 'en-US', 60, 'Djibouti', 'Djibouti', 'Djibouti');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DM', 'en-US', 61, 'Dominica', 'Dominica', 'Dominica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DO', 'en-US', 62, 'Dominican Republic', 'Dominican Republic', 'Dominican Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EC', 'en-US', 63, 'Ecuador', 'Ecuador', 'Ecuador');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EG', 'en-US', 64, 'Egypt', 'Egypt', 'Egypt');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SV', 'en-US', 65, 'El Salvador', 'El Salvador', 'El Salvador');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GQ', 'en-US', 66, 'Equatorial Guinea', 'Equatorial Guinea', 'Equatorial Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ER', 'en-US', 67, 'Eritrea', 'Eritrea', 'Eritrea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EE', 'en-US', 68, 'Estonia', 'Estonia', 'Estonia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ET', 'en-US', 69, 'Ethiopia', 'Ethiopia', 'Ethiopia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FK', 'en-US', 70, 'Falkland Islands (Malvinas)', 'Falkland Islands', 'Falkland Islands (Malvinas)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FO', 'en-US', 71, 'Faroe Islands', 'Faroe Islands', 'Faroe Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FJ', 'en-US', 72, 'Fiji', 'Fiji', 'Fiji');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FI', 'en-US', 73, 'Finland', 'Finland', 'Finland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FR', 'en-US', 74, 'France', 'France', 'France');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GF', 'en-US', 75, 'French Guiana', 'French Guiana', 'French Guiana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PF', 'en-US', 76, 'French Polynesia', 'French Polynesia', 'French Polynesia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TF', 'en-US', 77, 'French Southern Territories', 'French Southern Territories', 'French Southern Territories');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GA', 'en-US', 78, 'Gabon', 'Gabon', 'Gabon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GM', 'en-US', 79, 'Gambia', 'Gambia', 'Gambia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GE', 'en-US', 80, 'Georgia', 'Georgia', 'Georgia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DE', 'en-US', 81, 'Germany', 'Germany', 'Germany');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GH', 'en-US', 82, 'Ghana', 'Ghana', 'Ghana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GI', 'en-US', 83, 'Gibraltar', 'Gibraltar', 'Gibraltar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GR', 'en-US', 84, 'Greece', 'Greece', 'Greece');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GL', 'en-US', 85, 'Greenland', 'Greenland', 'Greenland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GD', 'en-US', 86, 'Grenada', 'Grenada', 'Grenada');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GP', 'en-US', 87, 'Guadeloupe', 'Guadeloupe', 'Guadeloupe');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GU', 'en-US', 88, 'Guam', 'Guam', 'Guam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GT', 'en-US', 89, 'Guatemala', 'Guatemala', 'Guatemala');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GG', 'en-US', 90, 'Guernsey', 'Guernsey', 'Guernsey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GN', 'en-US', 91, 'Guinea', 'Guinea', 'Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GW', 'en-US', 92, 'Guinea-Bissau', 'Guinea-Bissau', 'Guinea-Bissau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GY', 'en-US', 93, 'Guyana', 'Guyana', 'Guyana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HT', 'en-US', 94, 'Haiti', 'Haiti', 'Haiti');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VA', 'en-US', 95, 'Holy See', 'Holy See', 'Holy See');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HN', 'en-US', 96, 'Honduras', 'Honduras', 'Honduras');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HK', 'en-US', 97, 'Hong Kong', 'Hong Kong', 'Hong Kong');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HU', 'en-US', 98, 'Hungary', 'Hungary', 'Hungary');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IS', 'en-US', 99, 'Iceland', 'Iceland', 'Iceland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IN', 'en-US', 100, 'India', 'India', 'India');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ID', 'en-US', 101, 'Indonesia', 'Indonesia', 'Indonesia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IR', 'en-US', 102, 'Iran, Islamic Republic of', 'Iran', 'Iran, Islamic Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IQ', 'en-US', 103, 'Iraq', 'Iraq', 'Iraq');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IE', 'en-US', 104, 'Ireland', 'Ireland', 'Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IM', 'en-US', 105, 'Isle of Man', 'Isle of Man', 'Isle of Man');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IL', 'en-US', 106, 'Israel', 'Israel', 'Israel');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IT', 'en-US', 107, 'Italy', 'Italy', 'Italy');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JM', 'en-US', 108, 'Jamaica', 'Jamaica', 'Jamaica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JP', 'en-US', 109, 'Japan', 'Japan', 'Japan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JE', 'en-US', 110, 'Jersey', 'Jersey', 'Jersey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JO', 'en-US', 111, 'Jordan', 'Jordan', 'Jordan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KZ', 'en-US', 112, 'Kazakhstan', 'Kazakhstan', 'Kazakhstan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KE', 'en-US', 113, 'Kenya', 'Kenya', 'Kenya');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KI', 'en-US', 114, 'Kiribati', 'Kiribati', 'Kiribati');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KP', 'en-US', 115, 'Korea, Democratic People''s Republic of', 'Korea, Dem. People’s Rep.', 'Korea, Democratic People''s Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KR', 'en-US', 116, 'Korea, Republic of', 'Korea, Rep.', 'Korea, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KW', 'en-US', 117, 'Kuwait', 'Kuwait', 'Kuwait');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KG', 'en-US', 118, 'Kyrgyz Republic', 'Kyrgyz Republic', 'Kyrgyz Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LA', 'en-US', 119, 'Lao People''s Democratic Republic', 'Lao PDR', 'Lao People''s Democratic Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LV', 'en-US', 120, 'Latvia', 'Latvia', 'Latvia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LB', 'en-US', 121, 'Lebanon', 'Lebanon', 'Lebanon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LS', 'en-US', 122, 'Lesotho', 'Lesotho', 'Lesotho');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LR', 'en-US', 123, 'Liberia', 'Liberia', 'Liberia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LY', 'en-US', 124, 'Libya', 'Libya', 'Libya');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LI', 'en-US', 125, 'Liechtenstein', 'Liechtenstein', 'Liechtenstein');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LT', 'en-US', 126, 'Lithuania', 'Lithuania', 'Lithuania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LU', 'en-US', 127, 'Luxembourg', 'Luxembourg', 'Luxembourg');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MO', 'en-US', 128, 'Macao', 'Macao', 'Macao');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MG', 'en-US', 129, 'Madagascar', 'Madagascar', 'Madagascar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MW', 'en-US', 130, 'Malawi', 'Malawi', 'Malawi');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MY', 'en-US', 131, 'Malaysia', 'Malaysia', 'Malaysia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MV', 'en-US', 132, 'Maldives', 'Maldives', 'Maldives');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ML', 'en-US', 133, 'Mali', 'Mali', 'Mali');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MT', 'en-US', 134, 'Malta', 'Malta', 'Malta');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MH', 'en-US', 135, 'Marshall Islands', 'Marshall Islands', 'Marshall Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MQ', 'en-US', 136, 'Martinique', 'Martinique', 'Martinique');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MR', 'en-US', 137, 'Mauritania', 'Mauritania', 'Mauritania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MU', 'en-US', 138, 'Mauritius', 'Mauritius', 'Mauritius');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('YT', 'en-US', 139, 'Mayotte', 'Mayotte', 'Mayotte');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MX', 'en-US', 140, 'Mexico', 'Mexico', 'Mexico');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FM', 'en-US', 141, 'Micronesia, Federated States of', 'Micronesia, Fed. Sts.', 'Micronesia, Federated States of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MD', 'en-US', 142, 'Moldova, Republic of', 'Moldova', 'Moldova, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MC', 'en-US', 143, 'Monaco', 'Monaco', 'Monaco');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MN', 'en-US', 144, 'Mongolia', 'Mongolia', 'Mongolia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ME', 'en-US', 145, 'Montenegro', 'Montenegro', 'Montenegro');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MS', 'en-US', 146, 'Montserrat', 'Montserrat', 'Montserrat');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MA', 'en-US', 147, 'Morocco', 'Morocco', 'Morocco');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MZ', 'en-US', 148, 'Mozambique', 'Mozambique', 'Mozambique');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MM', 'en-US', 149, 'Myanmar', 'Myanmar', 'Myanmar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NA', 'en-US', 150, 'Namibia', 'Namibia', 'Namibia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NR', 'en-US', 151, 'Nauru', 'Nauru', 'Nauru');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NP', 'en-US', 152, 'Nepal', 'Nepal', 'Nepal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NL', 'en-US', 153, 'Netherlands', 'Netherlands', 'Netherlands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NC', 'en-US', 154, 'New Caledonia', 'New Caledonia', 'New Caledonia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NZ', 'en-US', 155, 'New Zealand', 'New Zealand', 'New Zealand');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NI', 'en-US', 156, 'Nicaragua', 'Nicaragua', 'Nicaragua');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NE', 'en-US', 157, 'Niger', 'Niger', 'Niger');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NG', 'en-US', 158, 'Nigeria', 'Nigeria', 'Nigeria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NU', 'en-US', 159, 'Niue', 'Niue', 'Niue');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NF', 'en-US', 160, 'Norfolk Island', 'Norfolk Island', 'Norfolk Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MP', 'en-US', 161, 'Northern Mariana Islands', 'Northern Mariana Islands', 'Northern Mariana Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NO', 'en-US', 162, 'Norway', 'Norway', 'Norway');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('OM', 'en-US', 163, 'Oman', 'Oman', 'Oman');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PK', 'en-US', 164, 'Pakistan', 'Pakistan', 'Pakistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PW', 'en-US', 165, 'Palau', 'Palau', 'Palau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PS', 'en-US', 166, 'Palestine, State of', 'Palestine', 'Palestine, State of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PA', 'en-US', 167, 'Panama', 'Panama', 'Panama');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PG', 'en-US', 168, 'Papua New Guinea', 'Papua New Guinea', 'Papua New Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PY', 'en-US', 169, 'Paraguay', 'Paraguay', 'Paraguay');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PE', 'en-US', 170, 'Peru', 'Peru', 'Peru');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PH', 'en-US', 171, 'Philippines', 'Philippines', 'Philippines');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PN', 'en-US', 172, 'Pitcairn', 'Pitcairn', 'Pitcairn');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PL', 'en-US', 173, 'Poland', 'Poland', 'Poland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PT', 'en-US', 174, 'Portugal', 'Portugal', 'Portugal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PR', 'en-US', 175, 'Puerto Rico', 'Puerto Rico', 'Puerto Rico');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('QA', 'en-US', 176, 'Qatar', 'Qatar', 'Qatar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MK', 'en-US', 177, 'Republic of North Macedonia', 'North Macedonia', 'Republic of North Macedonia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RO', 'en-US', 178, 'Romania', 'Romania', 'Romania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RU', 'en-US', 179, 'Russian Federation', 'Russian Federation', 'Russian Federation');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RW', 'en-US', 180, 'Rwanda', 'Rwanda', 'Rwanda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RE', 'en-US', 181, 'Réunion', 'Réunion', 'Réunion');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BL', 'en-US', 182, 'Saint Barthélemy', 'St. Barthélemy', 'Saint Barthélemy');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KN', 'en-US', 183, 'Saint Kitts and Nevis', 'St. Kitts and Nevis', 'Saint Kitts and Nevis');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LC', 'en-US', 184, 'Saint Lucia', 'St. Lucia', 'Saint Lucia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MF', 'en-US', 185, 'Saint Martin (French part)', 'St. Martin (French part)', 'Saint Martin (French part)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PM', 'en-US', 186, 'Saint Pierre and Miquelon', 'St. Pierre and Miquelon', 'Saint Pierre and Miquelon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VC', 'en-US', 187, 'Saint Vincent and the Grenadines', 'St. Vincent and the Grenadines', 'Saint Vincent and the Grenadines');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('WS', 'en-US', 188, 'Samoa', 'Samoa', 'Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SM', 'en-US', 189, 'San Marino', 'San Marino', 'San Marino');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ST', 'en-US', 190, 'Sao Tome and Principe', 'Sao Tome and Principe', 'Sao Tome and Principe');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SA', 'en-US', 191, 'Saudi Arabia', 'Saudi Arabia', 'Saudi Arabia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SN', 'en-US', 192, 'Senegal', 'Senegal', 'Senegal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RS', 'en-US', 193, 'Serbia', 'Serbia', 'Serbia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SC', 'en-US', 194, 'Seychelles', 'Seychelles', 'Seychelles');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SL', 'en-US', 195, 'Sierra Leone', 'Sierra Leone', 'Sierra Leone');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SG', 'en-US', 196, 'Singapore', 'Singapore', 'Singapore');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SX', 'en-US', 197, 'Sint Maarten (Dutch part)', 'Sint Maarten (Dutch part)', 'Sint Maarten (Dutch part)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SK', 'en-US', 198, 'Slovak Republic', 'Slovak Republic', 'Slovak Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SI', 'en-US', 199, 'Slovenia', 'Slovenia', 'Slovenia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SB', 'en-US', 200, 'Solomon Islands', 'Solomon Islands', 'Solomon Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SO', 'en-US', 201, 'Somalia', 'Somalia', 'Somalia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZA', 'en-US', 202, 'South Africa', 'South Africa', 'South Africa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SS', 'en-US', 203, 'South Sudan', 'South Sudan', 'South Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ES', 'en-US', 204, 'Spain', 'Spain', 'Spain');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LK', 'en-US', 205, 'Sri Lanka', 'Sri Lanka', 'Sri Lanka');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SD', 'en-US', 206, 'Sudan', 'Sudan', 'Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SR', 'en-US', 207, 'Suriname', 'Suriname', 'Suriname');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SJ', 'en-US', 208, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SZ', 'en-US', 209, 'Swaziland', 'Swaziland', 'Swaziland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SE', 'en-US', 210, 'Sweden', 'Sweden', 'Sweden');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CH', 'en-US', 211, 'Switzerland', 'Switzerland', 'Switzerland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SY', 'en-US', 212, 'Syrian Arab Republic', 'Syrian Arab Republic', 'Syrian Arab Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TW', 'en-US', 213, 'Taiwan, Province of China', 'Taiwan', 'Taiwan, Province of China');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TJ', 'en-US', 214, 'Tajikistan', 'Tajikistan', 'Tajikistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TZ', 'en-US', 215, 'Tanzania, United Republic of', 'Tanzania', 'Tanzania, United Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TH', 'en-US', 216, 'Thailand', 'Thailand', 'Thailand');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TL', 'en-US', 217, 'Timor-Leste', 'Timor-Leste', 'Timor-Leste');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TG', 'en-US', 218, 'Togo', 'Togo', 'Togo');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TK', 'en-US', 219, 'Tokelau', 'Tokelau', 'Tokelau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TO', 'en-US', 220, 'Tonga', 'Tonga', 'Tonga');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TT', 'en-US', 221, 'Trinidad and Tobago', 'Trinidad and Tobago', 'Trinidad and Tobago');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TN', 'en-US', 222, 'Tunisia', 'Tunisia', 'Tunisia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TR', 'en-US', 223, 'Turkey', 'Turkey', 'Turkey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TM', 'en-US', 224, 'Turkmenistan', 'Turkmenistan', 'Turkmenistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TC', 'en-US', 225, 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TV', 'en-US', 226, 'Tuvalu', 'Tuvalu', 'Tuvalu');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UG', 'en-US', 227, 'Uganda', 'Uganda', 'Uganda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UA', 'en-US', 228, 'Ukraine', 'Ukraine', 'Ukraine');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AE', 'en-US', 229, 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GB', 'en-US', 230, 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UM', 'en-US', 231, 'United States Minor Outlying Islands', 'US Minor Outlying Islands', 'United States Minor Outlying Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('US', 'en-US', 232, 'United States of America', 'United States of America', 'United States of America');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UY', 'en-US', 233, 'Uruguay', 'Uruguay', 'Uruguay');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UZ', 'en-US', 234, 'Uzbekistan', 'Uzbekistan', 'Uzbekistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VU', 'en-US', 235, 'Vanuatu', 'Vanuatu', 'Vanuatu');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VE', 'en-US', 236, 'Venezuela, Bolivarian Republic of', 'Venezuela', 'Venezuela, Bolivarian Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VN', 'en-US', 237, 'Viet Nam', 'Viet Nam', 'Viet Nam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VG', 'en-US', 238, 'Virgin Islands, British', 'Virgin Islands, British', 'Virgin Islands, British');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VI', 'en-US', 239, 'Virgin Islands, U.S.', 'Virgin Islands, U.S.', 'Virgin Islands, U.S.');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('WF', 'en-US', 240, 'Wallis and Futuna', 'Wallis and Futuna', 'Wallis and Futuna');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EH', 'en-US', 241, 'Western Sahara', 'Western Sahara', 'Western Sahara');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('YE', 'en-US', 242, 'Yemen', 'Yemen', 'Yemen');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZM', 'en-US', 243, 'Zambia', 'Zambia', 'Zambia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZW', 'en-US', 244, 'Zimbabwe', 'Zimbabwe', 'Zimbabwe');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AX', 'en-US', 245, 'Åland Islands', 'Åland Islands', 'Åland Islands');

INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('E', 'en-US', 1, 'Employed', 'Employed');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('O', 'en-US', 2, 'Other', 'Other');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('E', 'F', 'en-US', 1, 'Full-time', 'Full-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('E', 'P', 'en-US', 2, 'Part-time', 'Part-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('E', 'C', 'en-US', 3, 'Contractor', 'Contractor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('E', 'S', 'en-US', 4, 'Self-employed', 'Self-employed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('O', 'R', 'en-US', 5, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('O', 'U', 'en-US', 6, 'Unemployed', 'Unemployed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('O', 'H', 'en-US', 7, 'Housewife/Home Executive', 'Housewife/Home Executive');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('O', 'S', 'en-US', 8, 'Student', 'Student');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('O', 'M', 'en-US', 9, 'Minor', 'Minor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('O', 'N', 'en-US', 10, 'Not Specified', 'Not Specified');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('U', 'U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('M', 'en-US', 1, 'Male', 'Male');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('F', 'en-US', 2, 'Female', 'Female');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('T', 'en-US', 3, 'Transgender', 'Transgender');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('N', 'en-US', 4, 'Non-binary', 'Non-binary');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAIDBOOK', 'en-US', 10001, 'South African ID Book', 'South African ID Book', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAIDCARD', 'en-US', 10002, 'South African ID Card', 'South African ID Card', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZADRVLIC', 'en-US', 10003, 'South African Driver''s License', 'South African Driver''s License', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('PASSPORT', 'en-US', 99999, 'Passport', 'Passport', '');

-- See: https://en.wikipedia.org/wiki/List_of_official_languages_by_country_and_territory
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('AB', 'en-US', 1, 'Abkhaz', 'Abkhaz', 'Abkhaz');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('AF', 'en-US', 2, 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('AM', 'en-US', 3, 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('AR', 'en-US', 4, 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('AY', 'en-US', 5, 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('AZ', 'en-US', 6, 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('BE', 'en-US', 7, 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('BG', 'en-US', 8, 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('BI', 'en-US', 9, 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('BN', 'en-US', 10, 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('BS', 'en-US', 11, 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('CS', 'en-US', 12, 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('DA', 'en-US', 13, 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('DE', 'en-US', 14, 'German', 'German', 'German');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('DZ', 'en-US', 15, 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('EL', 'en-US', 16, 'Greek', 'Greek', 'Greek');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('EN', 'en-US', 17, 'English', 'English', 'English');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ES', 'en-US', 18, 'Spanish', 'Spanish', 'Spanish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ET', 'en-US', 19, 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('FI', 'en-US', 20, 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('FJ', 'en-US', 21, 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('FR', 'en-US', 22, 'French', 'French', 'French');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('GN', 'en-US', 23, 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('HE', 'en-US', 24, 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('HI', 'en-US', 25, 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('HO', 'en-US', 26, 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('HR', 'en-US', 27, 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('HT', 'en-US', 28, 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('HU', 'en-US', 29, 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('HY', 'en-US', 30, 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ID', 'en-US', 31, 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('IS', 'en-US', 32, 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('IT', 'en-US', 33, 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('JA', 'en-US', 34, 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('KA', 'en-US', 35, 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('KK', 'en-US', 36, 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('KM', 'en-US', 37, 'Khmer', 'Khmer', 'Khmer');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('KO', 'en-US', 38, 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('KY', 'en-US', 39, 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('LB', 'en-US', 40, 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('LO', 'en-US', 41, 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('LT', 'en-US', 42, 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('LV', 'en-US', 43, 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('MG', 'en-US', 44, 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('MI', 'en-US', 45, 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('MK', 'en-US', 46, 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('MN', 'en-US', 47, 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('MR', 'en-US', 48, 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('MS', 'en-US', 49, 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('MT', 'en-US', 50, 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('MY', 'en-US', 51, 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ND', 'en-US', 52, 'Ndebele', 'Ndebele', 'Ndebele, Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('NE', 'en-US', 53, 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('NL', 'en-US', 54, 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('NO', 'en-US', 55, 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('NR', 'en-US', 56, 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('NY', 'en-US', 57, 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('OS', 'en-US', 58, 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('PL', 'en-US', 59, 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('PS', 'en-US', 60, 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('PT', 'en-US', 61, 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('QU', 'en-US', 62, 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('RM', 'en-US', 63, 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('RO', 'en-US', 64, 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('RU', 'en-US', 65, 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('RW', 'en-US', 66, 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SE', 'en-US', 67, 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SI', 'en-US', 68, 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SK', 'en-US', 69, 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SL', 'en-US', 70, 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SN', 'en-US', 71, 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SO', 'en-US', 72, 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SR', 'en-US', 73, 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ST', 'en-US', 74, 'Sotho', 'Sotho', 'Sotho, Southern Sotho');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SV', 'en-US', 75, 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('SW', 'en-US', 76, 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('TA', 'en-US', 77, 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('TG', 'en-US', 78, 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('TH', 'en-US', 79, 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('TI', 'en-US', 80, 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('TK', 'en-US', 81, 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('TN', 'en-US', 82, 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('TR', 'en-US', 83, 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('TS', 'en-US', 84, 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('UK', 'en-US', 85, 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('UR', 'en-US', 86, 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('UZ', 'en-US', 87, 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('VE', 'en-US', 88, 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('VI', 'en-US', 89, 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('XH', 'en-US', 90, 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ZH', 'en-US', 91, 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ZU', 'en-US', 92, 'Zulu', 'Zulu', 'Zulu');

INSERT INTO reference.marital_statuses (code, locale, sort_index, name, description)
  VALUES ('S', 'en-US', 1, 'Single', 'Single');
INSERT INTO reference.marital_statuses (code, locale, sort_index, name, description)
  VALUES ('M', 'en-US', 2, 'Married', 'Married');
INSERT INTO reference.marital_statuses (code, locale, sort_index, name, description)
  VALUES ('C', 'en-US', 3, 'Common Law', 'Common Law');
INSERT INTO reference.marital_statuses (code, locale, sort_index, name, description)
  VALUES ('D', 'en-US', 4, 'Divorced', 'Divorced');
INSERT INTO reference.marital_statuses (code, locale, sort_index, name, description)
  VALUES ('W', 'en-US', 5, 'Widowed', 'Widowed');
INSERT INTO reference.marital_statuses (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.marriage_types (marital_status, code, locale, sort_index, name, description)
  VALUES ('M', 'ICOP', 'en-US', 1, 'In Community Of Property', 'In Community Of Property');
INSERT INTO reference.marriage_types (marital_status, code, locale, sort_index, name, description)
  VALUES ('M', 'AWOA', 'en-US', 1, 'ANC Without Accrual', 'ANC Without Accrual');
INSERT INTO reference.marriage_types (marital_status, code, locale, sort_index, name, description)
  VALUES ('M', 'AWIA', 'en-US', 1, 'ANC With Accrual', 'ANC With Accrual');
INSERT INTO reference.marriage_types (marital_status, code, locale, sort_index, name, description)
  VALUES ('U', 'UNKW', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.minor_types (code, locale, sort_index, name, description)
  VALUES ('N', 'en-US', 1, 'None', 'None');
INSERT INTO reference.minor_types (code, locale, sort_index, name, description)
  VALUES ('M', 'en-US', 2, 'Minor', 'Minor');
INSERT INTO reference.minor_types (code, locale, sort_index, name, description)
  VALUES ('E', 'en-US', 3, 'Emancipated Minor', 'Emancipated Minor');
INSERT INTO reference.minor_types (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 99, 'Unknown', 'Unknown');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'Aunt', 'Aunt');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 1, 'Brother', 'Brother');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('3', 'en-US', 1, 'Cousin', 'Cousin');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('4', 'en-US', 1, 'Father', 'Father');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('5', 'en-US', 1, 'Friend', 'Friend');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('6', 'en-US', 1, 'Grandfather', 'Grandfather');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('7', 'en-US', 1, 'Grandmother', 'Grandmother');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('8', 'en-US', 1, 'Husband', 'Husband');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('9', 'en-US', 1, 'Life Partner', 'Life Partner');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('10', 'en-US', 1, 'Mother', 'Mother');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('11', 'en-US', 1, 'Nephew', 'Nephew');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('12', 'en-US', 1, 'Niece', 'Niece');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('13', 'en-US', 1, 'Sister', 'Sister');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('14', 'en-US', 1, 'Uncle', 'Uncle');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('15', 'en-US', 1, 'Wife', 'Wife');
INSERT INTO reference.next_of_kin_types (code, locale, sort_index, name, description)
  VALUES ('16', 'en-US', 1, 'Other', 'Other');

INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 99, 'Unknown', 'Unknown');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'Creative', 'Creative');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 2, 'Driver', 'Driver');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('3', 'en-US', 3, 'Executive', 'Executive');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('4', 'en-US', 4, 'Farmer', 'Farmer');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('5', 'en-US', 5, 'Government Official', 'Government Official');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('6', 'en-US', 6, 'Guard', 'Guard');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('7', 'en-US', 7, 'Labourer', 'Labourer');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('8', 'en-US', 8, 'Military / Police', 'Military / Police');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('9', 'en-US', 9, 'Manager', 'Manager');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('10', 'en-US', 10, 'Office Staff', 'Office Staff');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('11', 'en-US', 11, 'Pensioner / Retired', 'Pensioner / Retired');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('12', 'en-US', 12, 'Plant or Machine Operator or Assembler', 'Plant or Machine Operator or Assembler');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('13', 'en-US', 13, 'Professional: Business', 'Professional: Business');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('14', 'en-US', 14, 'Professional: Education', 'Professional: Education');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('15', 'en-US', 15, 'Professional: Engineering', 'Professional: Engineering');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('16', 'en-US', 16, 'Professional: Government', 'Professional: Government');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('17', 'en-US', 17, 'Professional: Legal', 'Professional: Legal');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('18', 'en-US', 18, 'Professional: Medical', 'Professional: Medical');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('19', 'en-US', 19, 'Professional: Scientific', 'Professional: Scientific');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('20', 'en-US', 20, 'Professional: Transport', 'Professional: Transport');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('21', 'en-US', 21, 'Religious / Charitable', 'Religious / Charitable');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('22', 'en-US', 22, 'Sales', 'Sales');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('23', 'en-US', 23, 'Semi-skilled Worker', 'Semi-skilled Worker');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('24', 'en-US', 24, 'Service', 'Service');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('25', 'en-US', 25, 'Skilled Worker', 'Skilled Worker');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('26', 'en-US', 26, 'Technician', 'Technician');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('27', 'en-US', 27, 'Trade Worker', 'Trade Worker');
INSERT INTO reference.occupations (code, locale, sort_index, name, description)
  VALUES ('50', 'en-US', 50, 'Unemployed', 'Unemployed');

INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZABV', 'en-US', 1, 'Business Visa', 'Business Visa', 'ZA');
INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAGWV', 'en-US', 2, 'General Work Visa', 'General Work Visa', 'ZA');
INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZACSWV', 'en-US', 3, 'Critical Skills Work Visa', 'Critical Skills Work Visa', 'ZA');
INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAITWV', 'en-US', 4, 'Intra-company Transfer Work Visa', 'Intra-company Transfer Work Visa', 'ZA');
INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZASV', 'en-US', 5, 'Study Visa', 'Study Visa', 'ZA');
INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAEV', 'en-US', 6, 'Exchange Visa', 'Exchange Visa', 'ZA');
INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZARPV', 'en-US', 7, 'Retired Persons Visa', 'Retired Persons Visa', 'ZA');
INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZARV', 'en-US', 8, 'Relatives Visa', 'Relatives Visa', 'ZA');
INSERT INTO reference.permit_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAMTV', 'en-US', 9, 'Medical Treatment Visa', 'Medical Treatment Visa', 'ZA');

INSERT INTO reference.races (code, locale, sort_index, name, description)
  VALUES ('B', 'en-US', 1, 'Black', 'Black');
INSERT INTO reference.races (code, locale, sort_index, name, description)
  VALUES ('C', 'en-US', 2, 'Coloured', 'Coloured');
INSERT INTO reference.races (code, locale, sort_index, name, description)
  VALUES ('I', 'en-US', 3, 'Indian', 'Indian');
INSERT INTO reference.races (code, locale, sort_index, name, description)
  VALUES ('A', 'en-US', 4, 'Asian', 'Asian');
INSERT INTO reference.races (code, locale, sort_index, name, description)
  VALUES ('W', 'en-US', 5, 'White', 'White');
INSERT INTO reference.races (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'EC', 'en-US', 1, 'Eastern Cape', 'Eastern Cape');
INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'FS', 'en-US', 1, 'Free State', 'Free State');
INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'GP', 'en-US', 1, 'Gauteng', 'Gauteng');
INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'LP', 'en-US', 1, 'Limpopo', 'Limpopo');
INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'MP', 'en-US', 1, 'Mpumalanga', 'Mpumalanga');
INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'NC', 'en-US', 1, 'Northern Cape', 'Northern Cape');
INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'NW', 'en-US', 1, 'Northwest', 'Northwest');
INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'WC', 'en-US', 1, 'Western Cape', 'Western Cape');
INSERT INTO reference.regions (country, code, locale, sort_index, name, description)
  VALUES ('ZA', 'ZN', 'en-US', 1, 'KwaZulu-Natal', 'KwaZulu-Natal');

INSERT INTO reference.residential_statuses (code, locale, sort_index, name, description)
  VALUES ('C', 'en-US', 1, 'Citizen', 'Citizen');
INSERT INTO reference.residential_statuses (code, locale, sort_index, name, description)
  VALUES ('P', 'en-US', 2, 'Permanent Resident', 'Permanent Resident');
INSERT INTO reference.residential_statuses (code, locale, sort_index, name, description)
  VALUES ('F', 'en-US', 3, 'Foreign National', 'Foreign National');
INSERT INTO reference.residential_statuses (code, locale, sort_index, name, description)
  VALUES ('R', 'en-US', 4, 'Refugee', 'Refugee');
INSERT INTO reference.residential_statuses (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.residential_types (code, locale, sort_index, name, description)
  VALUES ('O', 'en-US', 1, 'Owner', 'Owner');
INSERT INTO reference.residential_types (code, locale, sort_index, name, description)
  VALUES ('R', 'en-US', 2, 'Renter', 'Renter');
INSERT INTO reference.residential_types (code, locale, sort_index, name, description)
  VALUES ('C', 'en-US', 3, 'Co-Habitant', 'Co-Habitant');
INSERT INTO reference.residential_types (code, locale, sort_index, name, description)
  VALUES ('B', 'en-US', 4, 'Boarder', 'Boarder');
INSERT INTO reference.residential_types (code, locale, sort_index, name, description)
  VALUES ('L', 'en-US', 5, 'Living with Parents', 'Living with Parents');
INSERT INTO reference.residential_types (code, locale, sort_index, name, description)
  VALUES ('N', 'en-US', 6, 'Not Specified', 'Not Specified');
INSERT INTO reference.residential_types (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 99, 'Unknown', 'Unknown');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'Salary', 'Salary');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 2, 'Commission', 'Commission');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('3', 'en-US', 3, 'Rental Income', 'Rental Income');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('4', 'en-US', 4, 'Investments', 'Investments');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('5', 'en-US', 5, 'Retirement Annuity', 'Retirement Annuity');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('6', 'en-US', 6, 'Social Grant', 'Social Grant');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('7', 'en-US', 7, 'Inheritance', 'Inheritance');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('8', 'en-US', 8, 'Maintenance', 'Maintenance');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('9', 'en-US', 9, 'Pension', 'Pension');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('10', 'en-US', 10, 'Donations', 'Donations');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('11', 'en-US', 11, 'Allowance', 'Allowance');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('12', 'en-US', 12, 'Winnings', 'Winnings');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('13', 'en-US', 13, 'Owner Draws', 'Owner Draws');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('14', 'en-US', 14, 'Bonus/Incentive', 'Bonus/Incentive');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('15', 'en-US', 15, 'Bursary', 'Bursary');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('16', 'en-US', 16, 'Settlement', 'Settlement');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('17', 'en-US', 17, 'Trust', 'Trust');
INSERT INTO reference.sources_of_funds (code, locale, sort_index, name, description)
  VALUES ('18', 'en-US', 18, 'Other', 'Other');

INSERT INTO reference.suitable_times_to_contact (code, locale, sort_index, name, description)
  VALUES ('A', 'en-US', 1, 'Anytime', 'Anytime');
INSERT INTO reference.suitable_times_to_contact (code, locale, sort_index, name, description)
  VALUES ('L', 'en-US', 2, 'Long Hours', 'Long Hours');
INSERT INTO reference.suitable_times_to_contact (code, locale, sort_index, name, description)
  VALUES ('O', 'en-US', 3, 'Office Hours', 'Office Hours');
INSERT INTO reference.suitable_times_to_contact (code, locale, sort_index, name, description)
  VALUES ('N', 'en-US', 4, 'Do Not Contact', 'Do Not Contact');
INSERT INTO reference.suitable_times_to_contact (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.tax_number_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAITN', 'en-US', 1, 'Income Tax Number', 'South African Income Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAVTN', 'en-US', 2, 'VAT Tax Number', 'South African VAT Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('ZAOTN', 'en-US', 3, 'Other Tax Number', 'Other South African Tax Number', 'ZA');
INSERT INTO reference.tax_number_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('UKUTN', 'en-US', 1, 'UK Tax Number', 'UK Tax Number', 'GB');
INSERT INTO reference.tax_number_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('UKOTN', 'en-US', 2, 'UK Other Tax Number', 'UK Other Tax Number', 'GB');
INSERT INTO reference.tax_number_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('USTIN', 'en-US', 1, 'US Taxpayer Identification Number', 'US Taxpayer Identification Number', 'US');
INSERT INTO reference.tax_number_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('USOTN', 'en-US', 2, 'US Other Tax Number', 'US Other Tax Number', 'US');

INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('0', 'en-US', 99, 'Unknown', 'Unknown', 'Unknown');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('1', 'en-US', 1, 'Mr', 'Mr.', 'Mr');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('2', 'en-US', 1, 'Ms', 'Ms.', 'Ms');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('3', 'en-US', 1, 'Miss', 'Miss', 'Miss');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('4', 'en-US', 1, 'Mrs', 'Mrs.', 'Mrs');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('5', 'en-US', 1, 'Doctor', 'Dr.', 'Doctor');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('6', 'en-US', 1, 'The Honorable', 'The Hon.', 'The Honorable');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('7', 'en-US', 1, 'Reverend', 'Rev.', 'Reverend');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('8', 'en-US', 1, 'Father', 'Fr.', 'Father');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('9', 'en-US', 1, 'Rabbi', 'Rabbi', 'Rabbi');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('10', 'en-US', 1, 'Professor', 'Prof.', 'Professor');
INSERT INTO reference.titles (code, locale, sort_index, name, abbreviation, description)
  VALUES ('11', 'en-US', 1, 'Advocate', 'Adv.', 'Advocate');

INSERT INTO reference.verification_methods (code, locale, sort_index, name, description)
  VALUES ('M', 'en-US', 1, 'Manual', 'Manual');
INSERT INTO reference.verification_methods (code, locale, sort_index, name, description)
  VALUES ('S', 'en-US', 1, 'System', 'System');
INSERT INTO reference.verification_methods (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');

INSERT INTO reference.verification_statuses (code, locale, sort_index, name, description)
  VALUES ('N', 'en-US', 1, 'Not Verified', 'Not Verified');
INSERT INTO reference.verification_statuses (code, locale, sort_index, name, description)
  VALUES ('I', 'en-US', 1, 'In Progress', 'In Progress');
INSERT INTO reference.verification_statuses (code, locale, sort_index, name, description)
  VALUES ('C', 'en-US', 1, 'Complete', 'Complete');
INSERT INTO reference.verification_statuses (code, locale, sort_index, name, description)
  VALUES ('F', 'en-US', 1, 'Failed', 'Failed');
INSERT INTO reference.verification_statuses (code, locale, sort_index, name, description)
  VALUES ('U', 'en-US', 99, 'Unknown', 'Unknown');


