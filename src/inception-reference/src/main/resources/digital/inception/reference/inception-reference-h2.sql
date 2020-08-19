-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA reference;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE reference.communication_methods (
  code        VARCHAR(10)  NOT NULL,
  locale      VARCHAR(10)  NOT NULL,
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(20)  NOT NULL,
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
  name        VARCHAR(100) NOT NULL,
  short_name  VARCHAR(50) NOT NULL DEFAULT '',
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
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(20)  NOT NULL,
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
  sort_index        INTEGER NOT NULL,
  name              VARCHAR(50)  NOT NULL,
  description       VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (employment_status, code, locale)
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
  sort_index  INTEGER NOT NULL,
  name        VARCHAR(20)  NOT NULL,
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
  name             VARCHAR(100)  NOT NULL,
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
  name        VARCHAR(100) NOT NULL,
  short_name  VARCHAR(50) NOT NULL DEFAULT '',
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


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 3, 'Not known', 'Not known');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'E-mail', 'E-mail');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 2, 'Phone', 'Phone');

INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('0', 'en-ZA', 3, 'Not known', 'Not known');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('1', 'en-ZA', 1, 'E-mail', 'E-mail');
INSERT INTO reference.communication_methods (code, locale, sort_index, name, description)
  VALUES ('2', 'en-ZA', 2, 'Phone', 'Phone');

INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AD', 'en-US', 1, 'Andorra', 'Andorra', 'Andorra');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AE', 'en-US', 2, 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AF', 'en-US', 3, 'Afghanistan', 'Afghanistan', 'Afghanistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AG', 'en-US', 4, 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AI', 'en-US', 5, 'Anguilla', 'Anguilla', 'Anguilla');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AL', 'en-US', 6, 'Albania', 'Albania', 'Albania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AM', 'en-US', 7, 'Armenia', 'Armenia', 'Armenia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AO', 'en-US', 8, 'Angola', 'Angola', 'Angola');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AQ', 'en-US', 9, 'Antarctica', 'Antarctica', 'Antarctica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AR', 'en-US', 10, 'Argentina', 'Argentina', 'Argentina');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AS', 'en-US', 11, 'American Samoa', 'American Samoa', 'American Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AT', 'en-US', 12, 'Austria', 'Austria', 'Austria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AU', 'en-US', 13, 'Australia', 'Australia', 'Australia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AW', 'en-US', 14, 'Aruba', 'Aruba', 'Aruba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AX', 'en-US', 15, 'Åland Islands', 'Åland Islands', 'Åland Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AZ', 'en-US', 16, 'Azerbaijan', 'Azerbaijan', 'Azerbaijan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BA', 'en-US', 17, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BB', 'en-US', 18, 'Barbados', 'Barbados', 'Barbados');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BD', 'en-US', 19, 'Bangladesh', 'Bangladesh', 'Bangladesh');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BE', 'en-US', 20, 'Belgium', 'Belgium', 'Belgium');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BF', 'en-US', 21, 'Burkina Faso', 'Burkina Faso', 'Burkina Faso');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BG', 'en-US', 22, 'Bulgaria', 'Bulgaria', 'Bulgaria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BH', 'en-US', 23, 'Bahrain', 'Bahrain', 'Bahrain');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BI', 'en-US', 24, 'Burundi', 'Burundi', 'Burundi');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BJ', 'en-US', 25, 'Benin', 'Benin', 'Benin');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BL', 'en-US', 26, 'Saint Barthélemy', 'Saint Barthélemy', 'Saint Barthélemy');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BM', 'en-US', 27, 'Bermuda', 'Bermuda', 'Bermuda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BN', 'en-US', 28, 'Brunei Darussalam', 'Brunei Darussalam', 'Brunei Darussalam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BO', 'en-US', 29, 'Bolivia, Plurinational State of', 'Bolivia', 'Bolivia, Plurinational State of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BQ', 'en-US', 30, 'Bonaire, Sint Eustatius and Saba', 'Bonaire, Sint Eustatius and Saba', 'Bonaire, Sint Eustatius and Saba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BR', 'en-US', 31, 'Brazil', 'Brazil', 'Brazil');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BS', 'en-US', 32, 'Bahamas', 'Bahamas', 'Bahamas');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BT', 'en-US', 33, 'Bhutan', 'Bhutan', 'Bhutan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BV', 'en-US', 34, 'Bouvet Island', 'Bouvet Island', 'Bouvet Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BW', 'en-US', 35, 'Botswana', 'Botswana', 'Botswana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BY', 'en-US', 36, 'Belarus', 'Belarus', 'Belarus');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BZ', 'en-US', 37, 'Belize', 'Belize', 'Belize');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CA', 'en-US', 38, 'Canada', 'Canada', 'Canada');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CC', 'en-US', 39, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CD', 'en-US', 40, 'Congo, the Democratic Republic of the', 'Democratic Republic of Congo', 'Congo, the Democratic Republic of the');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CF', 'en-US', 41, 'Central African Republic', 'Central African Republic', 'Central African Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CG', 'en-US', 42, 'Congo', 'Congo', 'Congo');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CH', 'en-US', 43, 'Switzerland', 'Switzerland', 'Switzerland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CI', 'en-US', 44, 'Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CK', 'en-US', 45, 'Cook Islands', 'Cook Islands', 'Cook Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CL', 'en-US', 46, 'Chile', 'Chile', 'Chile');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CM', 'en-US', 47, 'Cameroon', 'Cameroon', 'Cameroon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CN', 'en-US', 48, 'China', 'China', 'China');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CO', 'en-US', 49, 'Colombia', 'Colombia', 'Colombia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CR', 'en-US', 50, 'Costa Rica', 'Costa Rica', 'Costa Rica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CU', 'en-US', 51, 'Cuba', 'Cuba', 'Cuba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CV', 'en-US', 52, 'Cabo Verde', 'Cabo Verde', 'Cabo Verde');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CW', 'en-US', 53, 'Curaçao', 'Curaçao', 'Curaçao');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CX', 'en-US', 54, 'Christmas Island', 'Christmas Island', 'Christmas Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CY', 'en-US', 55, 'Cyprus', 'Cyprus', 'Cyprus');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CZ', 'en-US', 56, 'Czechia', 'Czechia', 'Czechia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DE', 'en-US', 57, 'Germany', 'Germany', 'Germany');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DJ', 'en-US', 58, 'Djibouti', 'Djibouti', 'Djibouti');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DK', 'en-US', 59, 'Denmark', 'Denmark', 'Denmark');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DM', 'en-US', 60, 'Dominica', 'Dominica', 'Dominica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DO', 'en-US', 61, 'Dominican Republic', 'Dominican Republic', 'Dominican Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DZ', 'en-US', 62, 'Algeria', 'Algeria', 'Algeria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EC', 'en-US', 63, 'Ecuador', 'Ecuador', 'Ecuador');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EE', 'en-US', 64, 'Estonia', 'Estonia', 'Estonia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EG', 'en-US', 65, 'Egypt', 'Egypt', 'Egypt');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EH', 'en-US', 66, 'Western Sahara', 'Western Sahara', 'Western Sahara');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ER', 'en-US', 67, 'Eritrea', 'Eritrea', 'Eritrea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ES', 'en-US', 68, 'Spain', 'Spain', 'Spain');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ET', 'en-US', 69, 'Ethiopia', 'Ethiopia', 'Ethiopia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FI', 'en-US', 70, 'Finland', 'Finland', 'Finland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FJ', 'en-US', 71, 'Fiji', 'Fiji', 'Fiji');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FK', 'en-US', 72, 'Falkland Islands (Malvinas)', 'Falkland Islands (Malvinas)', 'Falkland Islands (Malvinas)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FM', 'en-US', 73, 'Micronesia, Federated States of', 'Micronesia', 'Micronesia, Federated States of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FO', 'en-US', 74, 'Faroe Islands', 'Faroe Islands', 'Faroe Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FR', 'en-US', 75, 'France', 'France', 'France');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GA', 'en-US', 76, 'Gabon', 'Gabon', 'Gabon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GB', 'en-US', 77, 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GD', 'en-US', 78, 'Grenada', 'Grenada', 'Grenada');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GE', 'en-US', 79, 'Georgia', 'Georgia', 'Georgia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GF', 'en-US', 80, 'French Guiana', 'French Guiana', 'French Guiana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GG', 'en-US', 81, 'Guernsey', 'Guernsey', 'Guernsey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GH', 'en-US', 82, 'Ghana', 'Ghana', 'Ghana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GI', 'en-US', 83, 'Gibraltar', 'Gibraltar', 'Gibraltar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GL', 'en-US', 84, 'Greenland', 'Greenland', 'Greenland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GM', 'en-US', 85, 'Gambia', 'Gambia', 'Gambia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GN', 'en-US', 86, 'Guinea', 'Guinea', 'Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GP', 'en-US', 87, 'Guadeloupe', 'Guadeloupe', 'Guadeloupe');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GQ', 'en-US', 88, 'Equatorial Guinea', 'Equatorial Guinea', 'Equatorial Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GR', 'en-US', 89, 'Greece', 'Greece', 'Greece');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GS', 'en-US', 90, 'South Georgia and the South Sandwich Islands', 'South Georgia and the South Sandwich Islands', 'South Georgia and the South Sandwich Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GT', 'en-US', 91, 'Guatemala', 'Guatemala', 'Guatemala');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GU', 'en-US', 92, 'Guam', 'Guam', 'Guam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GW', 'en-US', 93, 'Guinea-Bissau', 'Guinea-Bissau', 'Guinea-Bissau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GY', 'en-US', 94, 'Guyana', 'Guyana', 'Guyana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HK', 'en-US', 95, 'Hong Kong', 'Hong Kong', 'Hong Kong');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HM', 'en-US', 96, 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HN', 'en-US', 97, 'Honduras', 'Honduras', 'Honduras');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HR', 'en-US', 98, 'Croatia', 'Croatia', 'Croatia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HT', 'en-US', 99, 'Haiti', 'Haiti', 'Haiti');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HU', 'en-US', 100, 'Hungary', 'Hungary', 'Hungary');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ID', 'en-US', 101, 'Indonesia', 'Indonesia', 'Indonesia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IE', 'en-US', 102, 'Ireland', 'Ireland', 'Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IL', 'en-US', 103, 'Israel', 'Israel', 'Israel');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IM', 'en-US', 104, 'Isle of Man', 'Isle of Man', 'Isle of Man');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IN', 'en-US', 105, 'India', 'India', 'India');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IO', 'en-US', 106, 'British Indian Ocean Territory', 'British Indian Ocean Territory', 'British Indian Ocean Territory');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IQ', 'en-US', 107, 'Iraq', 'Iraq', 'Iraq');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IR', 'en-US', 108, 'Iran, Islamic Republic of', 'Iran', 'Iran, Islamic Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IS', 'en-US', 109, 'Iceland', 'Iceland', 'Iceland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IT', 'en-US', 110, 'Italy', 'Italy', 'Italy');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JE', 'en-US', 111, 'Jersey', 'Jersey', 'Jersey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JM', 'en-US', 112, 'Jamaica', 'Jamaica', 'Jamaica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JO', 'en-US', 113, 'Jordan', 'Jordan', 'Jordan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JP', 'en-US', 114, 'Japan', 'Japan', 'Japan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KE', 'en-US', 115, 'Kenya', 'Kenya', 'Kenya');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KG', 'en-US', 116, 'Kyrgyzstan', 'Kyrgyzstan', 'Kyrgyzstan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KH', 'en-US', 117, 'Cambodia', 'Cambodia', 'Cambodia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KI', 'en-US', 118, 'Kiribati', 'Kiribati', 'Kiribati');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KM', 'en-US', 119, 'Comoros', 'Comoros', 'Comoros');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KN', 'en-US', 120, 'Saint Kitts and Nevis', 'Saint Kitts and Nevis', 'Saint Kitts and Nevis');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KP', 'en-US', 121, 'Korea, Democratic People''s Republic of', 'North Korea', 'Korea, Democratic People''s Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KR', 'en-US', 122, 'Korea, Republic of', 'South Korea', 'Korea, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KW', 'en-US', 123, 'Kuwait', 'Kuwait', 'Kuwait');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KY', 'en-US', 124, 'Cayman Islands', 'Cayman Islands', 'Cayman Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KZ', 'en-US', 125, 'Kazakhstan', 'Kazakhstan', 'Kazakhstan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LA', 'en-US', 126, 'Lao People''s Democratic Republic', 'Lao People''s Democratic Republic', 'Lao People''s Democratic Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LB', 'en-US', 127, 'Lebanon', 'Lebanon', 'Lebanon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LC', 'en-US', 128, 'Saint Lucia', 'Saint Lucia', 'Saint Lucia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LI', 'en-US', 129, 'Liechtenstein', 'Liechtenstein', 'Liechtenstein');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LK', 'en-US', 130, 'Sri Lanka', 'Sri Lanka', 'Sri Lanka');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LR', 'en-US', 131, 'Liberia', 'Liberia', 'Liberia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LS', 'en-US', 132, 'Lesotho', 'Lesotho', 'Lesotho');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LT', 'en-US', 133, 'Lithuania', 'Lithuania', 'Lithuania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LU', 'en-US', 134, 'Luxembourg', 'Luxembourg', 'Luxembourg');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LV', 'en-US', 135, 'Latvia', 'Latvia', 'Latvia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LY', 'en-US', 136, 'Libya', 'Libya', 'Libya');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MA', 'en-US', 137, 'Morocco', 'Morocco', 'Morocco');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MC', 'en-US', 138, 'Monaco', 'Monaco', 'Monaco');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MD', 'en-US', 139, 'Moldova, Republic of', 'Moldova', 'Moldova, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ME', 'en-US', 140, 'Montenegro', 'Montenegro', 'Montenegro');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MF', 'en-US', 141, 'Saint Martin (French part)', 'Saint Martin (French part)', 'Saint Martin (French part)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MG', 'en-US', 142, 'Madagascar', 'Madagascar', 'Madagascar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MH', 'en-US', 143, 'Marshall Islands', 'Marshall Islands', 'Marshall Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MK', 'en-US', 144, 'Macedonia, the former Yugoslav Republic of', 'Macedonia', 'Macedonia, the former Yugoslav Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ML', 'en-US', 145, 'Mali', 'Mali', 'Mali');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MM', 'en-US', 146, 'Myanmar', 'Myanmar', 'Myanmar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MN', 'en-US', 147, 'Mongolia', 'Mongolia', 'Mongolia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MO', 'en-US', 148, 'Macao', 'Macao', 'Macao');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MP', 'en-US', 149, 'Northern Mariana Islands', 'Northern Mariana Islands', 'Northern Mariana Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MQ', 'en-US', 150, 'Martinique', 'Martinique', 'Martinique');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MR', 'en-US', 151, 'Mauritania', 'Mauritania', 'Mauritania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MS', 'en-US', 152, 'Montserrat', 'Montserrat', 'Montserrat');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MT', 'en-US', 153, 'Malta', 'Malta', 'Malta');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MU', 'en-US', 154, 'Mauritius', 'Mauritius', 'Mauritius');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MV', 'en-US', 155, 'Maldives', 'Maldives', 'Maldives');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MW', 'en-US', 156, 'Malawi', 'Malawi', 'Malawi');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MX', 'en-US', 157, 'Mexico', 'Mexico', 'Mexico');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MY', 'en-US', 158, 'Malaysia', 'Malaysia', 'Malaysia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MZ', 'en-US', 159, 'Mozambique', 'Mozambique', 'Mozambique');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NA', 'en-US', 160, 'Namibia', 'Namibia', 'Namibia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NC', 'en-US', 161, 'New Caledonia', 'New Caledonia', 'New Caledonia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NE', 'en-US', 162, 'Niger', 'Niger', 'Niger');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NF', 'en-US', 163, 'Norfolk Island', 'Norfolk Island', 'Norfolk Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NG', 'en-US', 164, 'Nigeria', 'Nigeria', 'Nigeria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NI', 'en-US', 165, 'Nicaragua', 'Nicaragua', 'Nicaragua');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NL', 'en-US', 166, 'Netherlands', 'Netherlands', 'Netherlands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NO', 'en-US', 167, 'Norway', 'Norway', 'Norway');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NP', 'en-US', 168, 'Nepal', 'Nepal', 'Nepal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NR', 'en-US', 169, 'Nauru', 'Nauru', 'Nauru');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NU', 'en-US', 170, 'Niue', 'Niue', 'Niue');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NZ', 'en-US', 171, 'New Zealand', 'New Zealand', 'New Zealand');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('OM', 'en-US', 172, 'Oman', 'Oman', 'Oman');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PA', 'en-US', 173, 'Panama', 'Panama', 'Panama');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PE', 'en-US', 174, 'Peru', 'Peru', 'Peru');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PF', 'en-US', 175, 'French Polynesia', 'French Polynesia', 'French Polynesia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PG', 'en-US', 176, 'Papua New Guinea', 'Papua New Guinea', 'Papua New Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PH', 'en-US', 177, 'Philippines', 'Philippines', 'Philippines');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PK', 'en-US', 178, 'Pakistan', 'Pakistan', 'Pakistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PL', 'en-US', 179, 'Poland', 'Poland', 'Poland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PM', 'en-US', 180, 'Saint Pierre and Miquelon', 'Saint Pierre and Miquelon', 'Saint Pierre and Miquelon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PN', 'en-US', 181, 'Pitcairn', 'Pitcairn', 'Pitcairn');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PR', 'en-US', 182, 'Puerto Rico', 'Puerto Rico', 'Puerto Rico');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PS', 'en-US', 183, 'Palestine, State of', 'Palestine', 'Palestine, State of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PT', 'en-US', 184, 'Portugal', 'Portugal', 'Portugal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PW', 'en-US', 185, 'Palau', 'Palau', 'Palau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PY', 'en-US', 186, 'Paraguay', 'Paraguay', 'Paraguay');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('QA', 'en-US', 187, 'Qatar', 'Qatar', 'Qatar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RE', 'en-US', 188, 'Réunion', 'Réunion', 'Réunion');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RO', 'en-US', 189, 'Romania', 'Romania', 'Romania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RS', 'en-US', 190, 'Serbia', 'Serbia', 'Serbia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RU', 'en-US', 191, 'Russian Federation', 'Russian Federation', 'Russian Federation');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RW', 'en-US', 192, 'Rwanda', 'Rwanda', 'Rwanda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SA', 'en-US', 193, 'Saudi Arabia', 'Saudi Arabia', 'Saudi Arabia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SB', 'en-US', 194, 'Solomon Islands', 'Solomon Islands', 'Solomon Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SC', 'en-US', 195, 'Seychelles', 'Seychelles', 'Seychelles');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SD', 'en-US', 196, 'Sudan', 'Sudan', 'Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SE', 'en-US', 197, 'Sweden', 'Sweden', 'Sweden');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SG', 'en-US', 198, 'Singapore', 'Singapore', 'Singapore');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SH', 'en-US', 199, 'Saint Helena, Ascension and Tristan da Cunha', 'Saint Helena, Ascension and Tristan da Cunha', 'Saint Helena, Ascension and Tristan da Cunha');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SI', 'en-US', 200, 'Slovenia', 'Slovenia', 'Slovenia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SJ', 'en-US', 201, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SK', 'en-US', 202, 'Slovakia', 'Slovakia', 'Slovakia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SL', 'en-US', 203, 'Sierra Leone', 'Sierra Leone', 'Sierra Leone');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SM', 'en-US', 204, 'San Marino', 'San Marino', 'San Marino');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SN', 'en-US', 205, 'Senegal', 'Senegal', 'Senegal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SO', 'en-US', 206, 'Somalia', 'Somalia', 'Somalia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SR', 'en-US', 207, 'Suriname', 'Suriname', 'Suriname');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SS', 'en-US', 208, 'South Sudan', 'South Sudan', 'South Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ST', 'en-US', 209, 'Sao Tome and Principe', 'Sao Tome and Principe', 'Sao Tome and Principe');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SV', 'en-US', 210, 'El Salvador', 'El Salvador', 'El Salvador');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SX', 'en-US', 211, 'Sint Maarten (Dutch part)', 'Sint Maarten (Dutch part)', 'Sint Maarten (Dutch part)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SY', 'en-US', 212, 'Syrian Arab Republic', 'Syrian Arab Republic', 'Syrian Arab Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SZ', 'en-US', 213, 'Swaziland', 'Swaziland', 'Swaziland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TC', 'en-US', 214, 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TD', 'en-US', 215, 'Chad', 'Chad', 'Chad');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TF', 'en-US', 216, 'French Southern Territories', 'French Southern Territories', 'French Southern Territories');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TG', 'en-US', 217, 'Togo', 'Togo', 'Togo');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TH', 'en-US', 218, 'Thailand', 'Thailand', 'Thailand');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TJ', 'en-US', 219, 'Tajikistan', 'Tajikistan', 'Tajikistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TK', 'en-US', 220, 'Tokelau', 'Tokelau', 'Tokelau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TL', 'en-US', 221, 'Timor-Leste', 'Timor-Leste', 'Timor-Leste');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TM', 'en-US', 222, 'Turkmenistan', 'Turkmenistan', 'Turkmenistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TN', 'en-US', 223, 'Tunisia', 'Tunisia', 'Tunisia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TO', 'en-US', 224, 'Tonga', 'Tonga', 'Tonga');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TR', 'en-US', 225, 'Turkey', 'Turkey', 'Turkey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TT', 'en-US', 226, 'Trinidad and Tobago', 'Trinidad and Tobago', 'Trinidad and Tobago');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TV', 'en-US', 227, 'Tuvalu', 'Tuvalu', 'Tuvalu');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TW', 'en-US', 228, 'Taiwan, Province of China', 'Taiwan', 'Taiwan, Province of China');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TZ', 'en-US', 229, 'Tanzania, United Republic of', 'Tanzania', 'Tanzania, United Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UA', 'en-US', 230, 'Ukraine', 'Ukraine', 'Ukraine');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UG', 'en-US', 231, 'Uganda', 'Uganda', 'Uganda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UM', 'en-US', 232, 'United States Minor Outlying Islands', 'United States Minor Outlying Islands', 'United States Minor Outlying Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('US', 'en-US', 233, 'United States of America', 'United States of America', 'United States of America');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UY', 'en-US', 234, 'Uruguay', 'Uruguay', 'Uruguay');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UZ', 'en-US', 235, 'Uzbekistan', 'Uzbekistan', 'Uzbekistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VA', 'en-US', 236, 'Holy See', 'Holy See', 'Holy See');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VC', 'en-US', 237, 'Saint Vincent and the Grenadines', 'Saint Vincent and the Grenadines', 'Saint Vincent and the Grenadines');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VE', 'en-US', 238, 'Venezuela, Bolivarian Republic of', 'Venezuela', 'Venezuela, Bolivarian Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VG', 'en-US', 239, 'Virgin Islands, British', 'Virgin Islands, British', 'Virgin Islands, British');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VI', 'en-US', 240, 'Virgin Islands, U.S.', 'Virgin Islands, U.S.', 'Virgin Islands, U.S.');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VN', 'en-US', 241, 'Viet Nam', 'Viet Nam', 'Viet Nam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VU', 'en-US', 242, 'Vanuatu', 'Vanuatu', 'Vanuatu');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('WF', 'en-US', 243, 'Wallis and Futuna', 'Wallis and Futuna', 'Wallis and Futuna');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('WS', 'en-US', 244, 'Samoa', 'Samoa', 'Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('YE', 'en-US', 245, 'Yemen', 'Yemen', 'Yemen');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('YT', 'en-US', 246, 'Mayotte', 'Mayotte', 'Mayotte');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZA', 'en-US', 247, 'South Africa', 'South Africa', 'South Africa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZM', 'en-US', 248, 'Zambia', 'Zambia', 'Zambia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZW', 'en-US', 249, 'Zimbabwe', 'Zimbabwe', 'Zimbabwe');

INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AD', 'en-ZA', 1, 'Andorra', 'Andorra', 'Andorra');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AE', 'en-ZA', 2, 'United Arab Emirates', 'United Arab Emirates', 'United Arab Emirates');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AF', 'en-ZA', 3, 'Afghanistan', 'Afghanistan', 'Afghanistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AG', 'en-ZA', 4, 'Antigua and Barbuda', 'Antigua and Barbuda', 'Antigua and Barbuda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AI', 'en-ZA', 5, 'Anguilla', 'Anguilla', 'Anguilla');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AL', 'en-ZA', 6, 'Albania', 'Albania', 'Albania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AM', 'en-ZA', 7, 'Armenia', 'Armenia', 'Armenia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AO', 'en-ZA', 8, 'Angola', 'Angola', 'Angola');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AQ', 'en-ZA', 9, 'Antarctica', 'Antarctica', 'Antarctica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AR', 'en-ZA', 10, 'Argentina', 'Argentina', 'Argentina');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AS', 'en-ZA', 11, 'American Samoa', 'American Samoa', 'American Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AT', 'en-ZA', 12, 'Austria', 'Austria', 'Austria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AU', 'en-ZA', 13, 'Australia', 'Australia', 'Australia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AW', 'en-ZA', 14, 'Aruba', 'Aruba', 'Aruba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AX', 'en-ZA', 15, 'Åland Islands', 'Åland Islands', 'Åland Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('AZ', 'en-ZA', 16, 'Azerbaijan', 'Azerbaijan', 'Azerbaijan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BA', 'en-ZA', 17, 'Bosnia and Herzegovina', 'Bosnia and Herzegovina', 'Bosnia and Herzegovina');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BB', 'en-ZA', 18, 'Barbados', 'Barbados', 'Barbados');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BD', 'en-ZA', 19, 'Bangladesh', 'Bangladesh', 'Bangladesh');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BE', 'en-ZA', 20, 'Belgium', 'Belgium', 'Belgium');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BF', 'en-ZA', 21, 'Burkina Faso', 'Burkina Faso', 'Burkina Faso');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BG', 'en-ZA', 22, 'Bulgaria', 'Bulgaria', 'Bulgaria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BH', 'en-ZA', 23, 'Bahrain', 'Bahrain', 'Bahrain');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BI', 'en-ZA', 24, 'Burundi', 'Burundi', 'Burundi');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BJ', 'en-ZA', 25, 'Benin', 'Benin', 'Benin');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BL', 'en-ZA', 26, 'Saint Barthélemy', 'Saint Barthélemy', 'Saint Barthélemy');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BM', 'en-ZA', 27, 'Bermuda', 'Bermuda', 'Bermuda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BN', 'en-ZA', 28, 'Brunei Darussalam', 'Brunei Darussalam', 'Brunei Darussalam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BO', 'en-ZA', 29, 'Bolivia, Plurinational State of', 'Bolivia', 'Bolivia, Plurinational State of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BQ', 'en-ZA', 30, 'Bonaire, Sint Eustatius and Saba', 'Bonaire, Sint Eustatius and Saba', 'Bonaire, Sint Eustatius and Saba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BR', 'en-ZA', 31, 'Brazil', 'Brazil', 'Brazil');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BS', 'en-ZA', 32, 'Bahamas', 'Bahamas', 'Bahamas');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BT', 'en-ZA', 33, 'Bhutan', 'Bhutan', 'Bhutan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BV', 'en-ZA', 34, 'Bouvet Island', 'Bouvet Island', 'Bouvet Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BW', 'en-ZA', 35, 'Botswana', 'Botswana', 'Botswana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BY', 'en-ZA', 36, 'Belarus', 'Belarus', 'Belarus');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('BZ', 'en-ZA', 37, 'Belize', 'Belize', 'Belize');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CA', 'en-ZA', 38, 'Canada', 'Canada', 'Canada');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CC', 'en-ZA', 39, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CD', 'en-ZA', 40, 'Congo, the Democratic Republic of the', 'Democratic Republic of Congo', 'Congo, the Democratic Republic of the');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CF', 'en-ZA', 41, 'Central African Republic', 'Central African Republic', 'Central African Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CG', 'en-ZA', 42, 'Congo', 'Congo', 'Congo');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CH', 'en-ZA', 43, 'Switzerland', 'Switzerland', 'Switzerland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CI', 'en-ZA', 44, 'Côte d''Ivoire', 'Côte d''Ivoire', 'Côte d''Ivoire');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CK', 'en-ZA', 45, 'Cook Islands', 'Cook Islands', 'Cook Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CL', 'en-ZA', 46, 'Chile', 'Chile', 'Chile');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CM', 'en-ZA', 47, 'Cameroon', 'Cameroon', 'Cameroon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CN', 'en-ZA', 48, 'China', 'China', 'China');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CO', 'en-ZA', 49, 'Colombia', 'Colombia', 'Colombia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CR', 'en-ZA', 50, 'Costa Rica', 'Costa Rica', 'Costa Rica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CU', 'en-ZA', 51, 'Cuba', 'Cuba', 'Cuba');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CV', 'en-ZA', 52, 'Cabo Verde', 'Cabo Verde', 'Cabo Verde');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CW', 'en-ZA', 53, 'Curaçao', 'Curaçao', 'Curaçao');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CX', 'en-ZA', 54, 'Christmas Island', 'Christmas Island', 'Christmas Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CY', 'en-ZA', 55, 'Cyprus', 'Cyprus', 'Cyprus');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('CZ', 'en-ZA', 56, 'Czechia', 'Czechia', 'Czechia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DE', 'en-ZA', 57, 'Germany', 'Germany', 'Germany');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DJ', 'en-ZA', 58, 'Djibouti', 'Djibouti', 'Djibouti');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DK', 'en-ZA', 59, 'Denmark', 'Denmark', 'Denmark');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DM', 'en-ZA', 60, 'Dominica', 'Dominica', 'Dominica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DO', 'en-ZA', 61, 'Dominican Republic', 'Dominican Republic', 'Dominican Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('DZ', 'en-ZA', 62, 'Algeria', 'Algeria', 'Algeria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EC', 'en-ZA', 63, 'Ecuador', 'Ecuador', 'Ecuador');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EE', 'en-ZA', 64, 'Estonia', 'Estonia', 'Estonia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EG', 'en-ZA', 65, 'Egypt', 'Egypt', 'Egypt');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('EH', 'en-ZA', 66, 'Western Sahara', 'Western Sahara', 'Western Sahara');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ER', 'en-ZA', 67, 'Eritrea', 'Eritrea', 'Eritrea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ES', 'en-ZA', 68, 'Spain', 'Spain', 'Spain');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ET', 'en-ZA', 69, 'Ethiopia', 'Ethiopia', 'Ethiopia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FI', 'en-ZA', 70, 'Finland', 'Finland', 'Finland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FJ', 'en-ZA', 71, 'Fiji', 'Fiji', 'Fiji');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FK', 'en-ZA', 72, 'Falkland Islands (Malvinas)', 'Falkland Islands (Malvinas)', 'Falkland Islands (Malvinas)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FM', 'en-ZA', 73, 'Micronesia, Federated States of', 'Micronesia', 'Micronesia, Federated States of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FO', 'en-ZA', 74, 'Faroe Islands', 'Faroe Islands', 'Faroe Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('FR', 'en-ZA', 75, 'France', 'France', 'France');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GA', 'en-ZA', 76, 'Gabon', 'Gabon', 'Gabon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GB', 'en-ZA', 77, 'United Kingdom', 'United Kingdom', 'United Kingdom of Great Britain and Northern Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GD', 'en-ZA', 78, 'Grenada', 'Grenada', 'Grenada');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GE', 'en-ZA', 79, 'Georgia', 'Georgia', 'Georgia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GF', 'en-ZA', 80, 'French Guiana', 'French Guiana', 'French Guiana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GG', 'en-ZA', 81, 'Guernsey', 'Guernsey', 'Guernsey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GH', 'en-ZA', 82, 'Ghana', 'Ghana', 'Ghana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GI', 'en-ZA', 83, 'Gibraltar', 'Gibraltar', 'Gibraltar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GL', 'en-ZA', 84, 'Greenland', 'Greenland', 'Greenland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GM', 'en-ZA', 85, 'Gambia', 'Gambia', 'Gambia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GN', 'en-ZA', 86, 'Guinea', 'Guinea', 'Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GP', 'en-ZA', 87, 'Guadeloupe', 'Guadeloupe', 'Guadeloupe');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GQ', 'en-ZA', 88, 'Equatorial Guinea', 'Equatorial Guinea', 'Equatorial Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GR', 'en-ZA', 89, 'Greece', 'Greece', 'Greece');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GS', 'en-ZA', 90, 'South Georgia and the South Sandwich Islands', 'South Georgia and the South Sandwich Islands', 'South Georgia and the South Sandwich Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GT', 'en-ZA', 91, 'Guatemala', 'Guatemala', 'Guatemala');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GU', 'en-ZA', 92, 'Guam', 'Guam', 'Guam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GW', 'en-ZA', 93, 'Guinea-Bissau', 'Guinea-Bissau', 'Guinea-Bissau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('GY', 'en-ZA', 94, 'Guyana', 'Guyana', 'Guyana');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HK', 'en-ZA', 95, 'Hong Kong', 'Hong Kong', 'Hong Kong');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HM', 'en-ZA', 96, 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HN', 'en-ZA', 97, 'Honduras', 'Honduras', 'Honduras');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HR', 'en-ZA', 98, 'Croatia', 'Croatia', 'Croatia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HT', 'en-ZA', 99, 'Haiti', 'Haiti', 'Haiti');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('HU', 'en-ZA', 100, 'Hungary', 'Hungary', 'Hungary');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ID', 'en-ZA', 101, 'Indonesia', 'Indonesia', 'Indonesia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IE', 'en-ZA', 102, 'Ireland', 'Ireland', 'Ireland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IL', 'en-ZA', 103, 'Israel', 'Israel', 'Israel');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IM', 'en-ZA', 104, 'Isle of Man', 'Isle of Man', 'Isle of Man');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IN', 'en-ZA', 105, 'India', 'India', 'India');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IO', 'en-ZA', 106, 'British Indian Ocean Territory', 'British Indian Ocean Territory', 'British Indian Ocean Territory');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IQ', 'en-ZA', 107, 'Iraq', 'Iraq', 'Iraq');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IR', 'en-ZA', 108, 'Iran, Islamic Republic of', 'Iran', 'Iran, Islamic Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IS', 'en-ZA', 109, 'Iceland', 'Iceland', 'Iceland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('IT', 'en-ZA', 110, 'Italy', 'Italy', 'Italy');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JE', 'en-ZA', 111, 'Jersey', 'Jersey', 'Jersey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JM', 'en-ZA', 112, 'Jamaica', 'Jamaica', 'Jamaica');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JO', 'en-ZA', 113, 'Jordan', 'Jordan', 'Jordan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('JP', 'en-ZA', 114, 'Japan', 'Japan', 'Japan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KE', 'en-ZA', 115, 'Kenya', 'Kenya', 'Kenya');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KG', 'en-ZA', 116, 'Kyrgyzstan', 'Kyrgyzstan', 'Kyrgyzstan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KH', 'en-ZA', 117, 'Cambodia', 'Cambodia', 'Cambodia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KI', 'en-ZA', 118, 'Kiribati', 'Kiribati', 'Kiribati');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KM', 'en-ZA', 119, 'Comoros', 'Comoros', 'Comoros');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KN', 'en-ZA', 120, 'Saint Kitts and Nevis', 'Saint Kitts and Nevis', 'Saint Kitts and Nevis');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KP', 'en-ZA', 121, 'Korea, Democratic People''s Republic of', 'North Korea', 'Korea, Democratic People''s Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KR', 'en-ZA', 122, 'Korea, Republic of', 'South Korea', 'Korea, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KW', 'en-ZA', 123, 'Kuwait', 'Kuwait', 'Kuwait');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KY', 'en-ZA', 124, 'Cayman Islands', 'Cayman Islands', 'Cayman Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('KZ', 'en-ZA', 125, 'Kazakhstan', 'Kazakhstan', 'Kazakhstan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LA', 'en-ZA', 126, 'Lao People''s Democratic Republic', 'Lao People''s Democratic Republic', 'Lao People''s Democratic Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LB', 'en-ZA', 127, 'Lebanon', 'Lebanon', 'Lebanon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LC', 'en-ZA', 128, 'Saint Lucia', 'Saint Lucia', 'Saint Lucia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LI', 'en-ZA', 129, 'Liechtenstein', 'Liechtenstein', 'Liechtenstein');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LK', 'en-ZA', 130, 'Sri Lanka', 'Sri Lanka', 'Sri Lanka');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LR', 'en-ZA', 131, 'Liberia', 'Liberia', 'Liberia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LS', 'en-ZA', 132, 'Lesotho', 'Lesotho', 'Lesotho');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LT', 'en-ZA', 133, 'Lithuania', 'Lithuania', 'Lithuania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LU', 'en-ZA', 134, 'Luxembourg', 'Luxembourg', 'Luxembourg');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LV', 'en-ZA', 135, 'Latvia', 'Latvia', 'Latvia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('LY', 'en-ZA', 136, 'Libya', 'Libya', 'Libya');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MA', 'en-ZA', 137, 'Morocco', 'Morocco', 'Morocco');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MC', 'en-ZA', 138, 'Monaco', 'Monaco', 'Monaco');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MD', 'en-ZA', 139, 'Moldova, Republic of', 'Moldova', 'Moldova, Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ME', 'en-ZA', 140, 'Montenegro', 'Montenegro', 'Montenegro');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MF', 'en-ZA', 141, 'Saint Martin (French part)', 'Saint Martin (French part)', 'Saint Martin (French part)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MG', 'en-ZA', 142, 'Madagascar', 'Madagascar', 'Madagascar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MH', 'en-ZA', 143, 'Marshall Islands', 'Marshall Islands', 'Marshall Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MK', 'en-ZA', 144, 'Macedonia, the former Yugoslav Republic of', 'Macedonia', 'Macedonia, the former Yugoslav Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ML', 'en-ZA', 145, 'Mali', 'Mali', 'Mali');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MM', 'en-ZA', 146, 'Myanmar', 'Myanmar', 'Myanmar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MN', 'en-ZA', 147, 'Mongolia', 'Mongolia', 'Mongolia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MO', 'en-ZA', 148, 'Macao', 'Macao', 'Macao');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MP', 'en-ZA', 149, 'Northern Mariana Islands', 'Northern Mariana Islands', 'Northern Mariana Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MQ', 'en-ZA', 150, 'Martinique', 'Martinique', 'Martinique');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MR', 'en-ZA', 151, 'Mauritania', 'Mauritania', 'Mauritania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MS', 'en-ZA', 152, 'Montserrat', 'Montserrat', 'Montserrat');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MT', 'en-ZA', 153, 'Malta', 'Malta', 'Malta');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MU', 'en-ZA', 154, 'Mauritius', 'Mauritius', 'Mauritius');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MV', 'en-ZA', 155, 'Maldives', 'Maldives', 'Maldives');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MW', 'en-ZA', 156, 'Malawi', 'Malawi', 'Malawi');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MX', 'en-ZA', 157, 'Mexico', 'Mexico', 'Mexico');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MY', 'en-ZA', 158, 'Malaysia', 'Malaysia', 'Malaysia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('MZ', 'en-ZA', 159, 'Mozambique', 'Mozambique', 'Mozambique');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NA', 'en-ZA', 160, 'Namibia', 'Namibia', 'Namibia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NC', 'en-ZA', 161, 'New Caledonia', 'New Caledonia', 'New Caledonia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NE', 'en-ZA', 162, 'Niger', 'Niger', 'Niger');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NF', 'en-ZA', 163, 'Norfolk Island', 'Norfolk Island', 'Norfolk Island');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NG', 'en-ZA', 164, 'Nigeria', 'Nigeria', 'Nigeria');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NI', 'en-ZA', 165, 'Nicaragua', 'Nicaragua', 'Nicaragua');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NL', 'en-ZA', 166, 'Netherlands', 'Netherlands', 'Netherlands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NO', 'en-ZA', 167, 'Norway', 'Norway', 'Norway');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NP', 'en-ZA', 168, 'Nepal', 'Nepal', 'Nepal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NR', 'en-ZA', 169, 'Nauru', 'Nauru', 'Nauru');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NU', 'en-ZA', 170, 'Niue', 'Niue', 'Niue');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('NZ', 'en-ZA', 171, 'New Zealand', 'New Zealand', 'New Zealand');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('OM', 'en-ZA', 172, 'Oman', 'Oman', 'Oman');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PA', 'en-ZA', 173, 'Panama', 'Panama', 'Panama');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PE', 'en-ZA', 174, 'Peru', 'Peru', 'Peru');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PF', 'en-ZA', 175, 'French Polynesia', 'French Polynesia', 'French Polynesia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PG', 'en-ZA', 176, 'Papua New Guinea', 'Papua New Guinea', 'Papua New Guinea');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PH', 'en-ZA', 177, 'Philippines', 'Philippines', 'Philippines');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PK', 'en-ZA', 178, 'Pakistan', 'Pakistan', 'Pakistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PL', 'en-ZA', 179, 'Poland', 'Poland', 'Poland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PM', 'en-ZA', 180, 'Saint Pierre and Miquelon', 'Saint Pierre and Miquelon', 'Saint Pierre and Miquelon');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PN', 'en-ZA', 181, 'Pitcairn', 'Pitcairn', 'Pitcairn');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PR', 'en-ZA', 182, 'Puerto Rico', 'Puerto Rico', 'Puerto Rico');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PS', 'en-ZA', 183, 'Palestine, State of', 'Palestine', 'Palestine, State of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PT', 'en-ZA', 184, 'Portugal', 'Portugal', 'Portugal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PW', 'en-ZA', 185, 'Palau', 'Palau', 'Palau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('PY', 'en-ZA', 186, 'Paraguay', 'Paraguay', 'Paraguay');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('QA', 'en-ZA', 187, 'Qatar', 'Qatar', 'Qatar');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RE', 'en-ZA', 188, 'Réunion', 'Réunion', 'Réunion');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RO', 'en-ZA', 189, 'Romania', 'Romania', 'Romania');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RS', 'en-ZA', 190, 'Serbia', 'Serbia', 'Serbia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RU', 'en-ZA', 191, 'Russian Federation', 'Russian Federation', 'Russian Federation');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('RW', 'en-ZA', 192, 'Rwanda', 'Rwanda', 'Rwanda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SA', 'en-ZA', 193, 'Saudi Arabia', 'Saudi Arabia', 'Saudi Arabia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SB', 'en-ZA', 194, 'Solomon Islands', 'Solomon Islands', 'Solomon Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SC', 'en-ZA', 195, 'Seychelles', 'Seychelles', 'Seychelles');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SD', 'en-ZA', 196, 'Sudan', 'Sudan', 'Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SE', 'en-ZA', 197, 'Sweden', 'Sweden', 'Sweden');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SG', 'en-ZA', 198, 'Singapore', 'Singapore', 'Singapore');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SH', 'en-ZA', 199, 'Saint Helena, Ascension and Tristan da Cunha', 'Saint Helena, Ascension and Tristan da Cunha', 'Saint Helena, Ascension and Tristan da Cunha');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SI', 'en-ZA', 200, 'Slovenia', 'Slovenia', 'Slovenia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SJ', 'en-ZA', 201, 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen', 'Svalbard and Jan Mayen');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SK', 'en-ZA', 202, 'Slovakia', 'Slovakia', 'Slovakia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SL', 'en-ZA', 203, 'Sierra Leone', 'Sierra Leone', 'Sierra Leone');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SM', 'en-ZA', 204, 'San Marino', 'San Marino', 'San Marino');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SN', 'en-ZA', 205, 'Senegal', 'Senegal', 'Senegal');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SO', 'en-ZA', 206, 'Somalia', 'Somalia', 'Somalia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SR', 'en-ZA', 207, 'Suriname', 'Suriname', 'Suriname');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SS', 'en-ZA', 208, 'South Sudan', 'South Sudan', 'South Sudan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ST', 'en-ZA', 209, 'Sao Tome and Principe', 'Sao Tome and Principe', 'Sao Tome and Principe');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SV', 'en-ZA', 210, 'El Salvador', 'El Salvador', 'El Salvador');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SX', 'en-ZA', 211, 'Sint Maarten (Dutch part)', 'Sint Maarten (Dutch part)', 'Sint Maarten (Dutch part)');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SY', 'en-ZA', 212, 'Syrian Arab Republic', 'Syrian Arab Republic', 'Syrian Arab Republic');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('SZ', 'en-ZA', 213, 'Swaziland', 'Swaziland', 'Swaziland');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TC', 'en-ZA', 214, 'Turks and Caicos Islands', 'Turks and Caicos Islands', 'Turks and Caicos Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TD', 'en-ZA', 215, 'Chad', 'Chad', 'Chad');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TF', 'en-ZA', 216, 'French Southern Territories', 'French Southern Territories', 'French Southern Territories');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TG', 'en-ZA', 217, 'Togo', 'Togo', 'Togo');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TH', 'en-ZA', 218, 'Thailand', 'Thailand', 'Thailand');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TJ', 'en-ZA', 219, 'Tajikistan', 'Tajikistan', 'Tajikistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TK', 'en-ZA', 220, 'Tokelau', 'Tokelau', 'Tokelau');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TL', 'en-ZA', 221, 'Timor-Leste', 'Timor-Leste', 'Timor-Leste');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TM', 'en-ZA', 222, 'Turkmenistan', 'Turkmenistan', 'Turkmenistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TN', 'en-ZA', 223, 'Tunisia', 'Tunisia', 'Tunisia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TO', 'en-ZA', 224, 'Tonga', 'Tonga', 'Tonga');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TR', 'en-ZA', 225, 'Turkey', 'Turkey', 'Turkey');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TT', 'en-ZA', 226, 'Trinidad and Tobago', 'Trinidad and Tobago', 'Trinidad and Tobago');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TV', 'en-ZA', 227, 'Tuvalu', 'Tuvalu', 'Tuvalu');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TW', 'en-ZA', 228, 'Taiwan, Province of China', 'Taiwan', 'Taiwan, Province of China');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('TZ', 'en-ZA', 229, 'Tanzania, United Republic of', 'Tanzania', 'Tanzania, United Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UA', 'en-ZA', 230, 'Ukraine', 'Ukraine', 'Ukraine');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UG', 'en-ZA', 231, 'Uganda', 'Uganda', 'Uganda');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UM', 'en-ZA', 232, 'United States Minor Outlying Islands', 'United States Minor Outlying Islands', 'United States Minor Outlying Islands');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('US', 'en-ZA', 233, 'United States of America', 'United States of America', 'United States of America');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UY', 'en-ZA', 234, 'Uruguay', 'Uruguay', 'Uruguay');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('UZ', 'en-ZA', 235, 'Uzbekistan', 'Uzbekistan', 'Uzbekistan');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VA', 'en-ZA', 236, 'Holy See', 'Holy See', 'Holy See');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VC', 'en-ZA', 237, 'Saint Vincent and the Grenadines', 'Saint Vincent and the Grenadines', 'Saint Vincent and the Grenadines');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VE', 'en-ZA', 238, 'Venezuela, Bolivarian Republic of', 'Venezuela', 'Venezuela, Bolivarian Republic of');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VG', 'en-ZA', 239, 'Virgin Islands, British', 'Virgin Islands, British', 'Virgin Islands, British');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VI', 'en-ZA', 240, 'Virgin Islands, U.S.', 'Virgin Islands, U.S.', 'Virgin Islands, U.S.');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VN', 'en-ZA', 241, 'Viet Nam', 'Viet Nam', 'Viet Nam');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('VU', 'en-ZA', 242, 'Vanuatu', 'Vanuatu', 'Vanuatu');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('WF', 'en-ZA', 243, 'Wallis and Futuna', 'Wallis and Futuna', 'Wallis and Futuna');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('WS', 'en-ZA', 244, 'Samoa', 'Samoa', 'Samoa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('YE', 'en-ZA', 245, 'Yemen', 'Yemen', 'Yemen');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('YT', 'en-ZA', 246, 'Mayotte', 'Mayotte', 'Mayotte');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZA', 'en-ZA', 247, 'South Africa', 'South Africa', 'South Africa');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZM', 'en-ZA', 248, 'Zambia', 'Zambia', 'Zambia');
INSERT INTO reference.countries (code, locale, sort_index, name, short_name, description)
   VALUES ('ZW', 'en-ZA', 249, 'Zimbabwe', 'Zimbabwe', 'Zimbabwe');

INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 3, 'Not known', 'Not known');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'Other', 'Other');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 2, 'Employed', 'Employed');

INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('0', 'en-ZA', 3, 'Not known', 'Not known');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('1', 'en-ZA', 1, 'Other', 'Other');
INSERT INTO reference.employment_statuses (code, locale, sort_index, name, description)
  VALUES ('2', 'en-ZA', 2, 'Employed', 'Employed');
  
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('0', '0', 'en-US', 13, 'Not known', 'Not known');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '1', 'en-US', 1, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '2', 'en-US', 2, 'Unemployed', 'Unemployed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '3', 'en-US', 3, 'Housewife/Home Executive', 'Housewife/Home Executive');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '4', 'en-US', 4, 'Student', 'Student');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '5', 'en-US', 5, 'Dismissed', 'Dismissed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '6', 'en-US', 6, 'Suspended', 'Suspended');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '7', 'en-US', 7, 'Minor', 'Minor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '8', 'en-US', 8, 'Unspecified', 'Unspecified');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '1', 'en-US', 9, 'Full-time', 'Full-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '2', 'en-US', 10, 'Part-time', 'Part-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '3', 'en-US', 11, 'Contractor', 'Contractor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '4', 'en-US', 12, 'Self-employed', 'Self-employed');  

INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('0', '0', 'en-ZA', 13, 'Not known', 'Not known');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '1', 'en-ZA', 1, 'Retired/Pensioner', 'Retired/Pensioner');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '2', 'en-ZA', 2, 'Unemployed', 'Unemployed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '3', 'en-ZA', 3, 'Housewife/Home Executive', 'Housewife/Home Executive');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '4', 'en-ZA', 4, 'Student', 'Student');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '5', 'en-ZA', 5, 'Dismissed', 'Dismissed');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '6', 'en-ZA', 6, 'Suspended', 'Suspended');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '7', 'en-ZA', 7, 'Minor', 'Minor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('1', '8', 'en-ZA', 8, 'Unspecified', 'Unspecified');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '1', 'en-ZA', 9, 'Full-time', 'Full-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '2', 'en-ZA', 10, 'Part-time', 'Part-time');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '3', 'en-ZA', 11, 'Contractor', 'Contractor');
INSERT INTO reference.employment_types (employment_status, code, locale, sort_index, name, description)
  VALUES ('2', '4', 'en-ZA', 12, 'Self-employed', 'Self-employed');

INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('0', 'en-US', 6, 'Not known', 'Not known');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('1', 'en-US', 1, 'Male', 'Male');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('2', 'en-US', 2, 'Female', 'Female');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('3', 'en-US', 3, 'Transgender', 'Transgender');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('4', 'en-US', 4, 'Non-binary', 'Non-binary');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('9', 'en-US', 5, 'Not applicable', 'Not applicable');

INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('0', 'en-ZA', 6, 'Not known', 'Not known');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('1', 'en-ZA', 1, 'Male', 'Male');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('2', 'en-ZA', 2, 'Female', 'Female');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('3', 'en-ZA', 3, 'Transgender', 'Transgender');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('4', 'en-ZA', 4, 'Non-binary', 'Non-binary');
INSERT INTO reference.genders (code, locale, sort_index, name, description)
  VALUES ('9', 'en-ZA', 5, 'Not applicable', 'Not applicable');

INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('10001', 'en-US', 10001, 'South African ID Book', 'South African ID Book', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('10002', 'en-US', 10002, 'South African ID Card', 'South African ID Card', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('10003', 'en-US', 10003, 'South African Driver''s License', 'South African Driver''s License', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('99999', 'en-US', 99999, 'Passport', 'Passport', '');

INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('10001', 'en-ZA', 10001, 'South African ID Book', 'South African ID Book', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('10002', 'en-ZA', 10002, 'South African ID Card', 'South African ID Card', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('10003', 'en-ZA', 10003, 'South African Driver''s License', 'South African Driver''s License', 'ZA');
INSERT INTO reference.identity_document_types (code, locale, sort_index, name, description, country_of_issue)
  VALUES ('99999', 'en-ZA', 99999, 'Passport', 'Passport', '');

INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('aa', 'en-US', 1, 'Afar', 'Afar', 'Afar');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ab', 'en-US', 2, 'Abkhazian', 'Abkhazian', 'Abkhazian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('af', 'en-US', 4, 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ak', 'en-US', 5, 'Akan', 'Akan', 'Akan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('am', 'en-US', 6, 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ar', 'en-US', 8, 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('as', 'en-US', 9, 'Assamese', 'Assamese', 'Assamese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('av', 'en-US', 10, 'Avaric', 'Avaric', 'Avaric');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ay', 'en-US', 11, 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('az', 'en-US', 12, 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ba', 'en-US', 13, 'Bashkir', 'Bashkir', 'Bashkir');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('be', 'en-US', 14, 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bg', 'en-US', 15, 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bh', 'en-US', 16, 'Bihari', 'Bihari', 'Bihari languages');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bi', 'en-US', 17, 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bm', 'en-US', 18, 'Bambara', 'Bambara', 'Bambara');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bn', 'en-US', 19, 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bo', 'en-US', 20, 'Tibetan', 'Tibetan', 'Tibetan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bs', 'en-US', 22, 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ca', 'en-US', 23, 'Catalan', 'Catalan', 'Catalan, Valencian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ce', 'en-US', 24, 'Chechen', 'Chechen', 'Chechen');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('co', 'en-US', 26, 'Corsican', 'Corsican', 'Corsican');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cr', 'en-US', 27, 'Cree', 'Cree', 'Cree');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cs', 'en-US', 28, 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cu', 'en-US', 29, 'Church Slavic', 'Church Slavic', 'Church Slavic, Old Slavonic, Church Slavonic, Old Bulgarian, Old Church Slavonic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cv', 'en-US', 30, 'Chuvash', 'Chuvash', 'Chuvash');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cy', 'en-US', 31, 'Welsh', 'Welsh', 'Welsh');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('da', 'en-US', 32, 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('de', 'en-US', 33, 'German', 'German', 'German');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('dv', 'en-US', 34, 'Maldivian', 'Maldivian', 'Divehi, Dhivehi, Maldivian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('dz', 'en-US', 35, 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ee', 'en-US', 36, 'Ewe', 'Ewe', 'Ewe');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('el', 'en-US', 37, 'Greek', 'Greek', 'Greek Modern (1453-)');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('en', 'en-US', 38, 'English', 'English', 'English');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('es', 'en-US', 40, 'Spanish', 'Spanish', 'Spanish, Castilian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('et', 'en-US', 41, 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('eu', 'en-US', 42, 'Basque', 'Basque', 'Basque');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fa', 'en-US', 43, 'Persian', 'Persian', 'Persian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ff', 'en-US', 44, 'Fulah', 'Fulah', 'Fulah');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fi', 'en-US', 45, 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fj', 'en-US', 46, 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fo', 'en-US', 47, 'Faroese', 'Faroese', 'Faroese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fr', 'en-US', 48, 'French', 'French', 'French');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fy', 'en-US', 49, 'Western Frisian', 'Western Frisian', 'Western Frisian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ga', 'en-US', 50, 'Irish', 'Irish', 'Irish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('gd', 'en-US', 51, 'Scottish Gaelic', 'Scottish Gaelic', 'Gaelic, Scottish Gaelic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('gl', 'en-US', 52, 'Galician', 'Galician', 'Galician');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('gn', 'en-US', 53, 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('gu', 'en-US', 54, 'Gujarati', 'Gujarati', 'Gujarati');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ha', 'en-US', 56, 'Hausa', 'Hausa', 'Hausa');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('he', 'en-US', 57, 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hi', 'en-US', 58, 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ho', 'en-US', 59, 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hr', 'en-US', 60, 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ht', 'en-US', 61, 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hu', 'en-US', 62, 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hy', 'en-US', 63, 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hz', 'en-US', 64, 'Herero', 'Herero', 'Herero');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('id', 'en-US', 66, 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ig', 'en-US', 68, 'Igbo', 'Igbo', 'Igbo');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ii', 'en-US', 69, 'Nuosu', 'Nuosu', 'Sichuan Yi, Nuosu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ik', 'en-US', 70, 'Inupiaq', 'Inupiaq', 'Inupiaq');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('is', 'en-US', 72, 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('it', 'en-US', 73, 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('iu', 'en-US', 74, 'Inuktitut', 'Inuktitut', 'Inuktitut');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ja', 'en-US', 75, 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('jv', 'en-US', 76, 'Javanese', 'Javanese', 'Javanese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ka', 'en-US', 77, 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kg', 'en-US', 78, 'Kongo', 'Kongo', 'Kongo');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ki', 'en-US', 79, 'Kikuyu', 'Kikuyu', 'Kikuyu, Gikuyu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kj', 'en-US', 80, 'Kwanyama', 'Kwanyama', 'Kuanyama, Kwanyama');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kk', 'en-US', 81, 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kl', 'en-US', 82, 'Kalaallisut', 'Kalaallisut', 'Kalaallisut, Greenlandic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('km', 'en-US', 83, 'Central Khmer', 'Central Khmer', 'Central Khmer');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kn', 'en-US', 84, 'Kannada', 'Kannada', 'Kannada');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ko', 'en-US', 85, 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kr', 'en-US', 86, 'Kanuri', 'Kanuri', 'Kanuri');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ks', 'en-US', 87, 'Kashmiri', 'Kashmiri', 'Kashmiri');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ku', 'en-US', 88, 'Kurdish', 'Kurdish', 'Kurdish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kv', 'en-US', 89, 'Komi', 'Komi', 'Komi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kw', 'en-US', 90, 'Cornish', 'Cornish', 'Cornish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ky', 'en-US', 91, 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('la', 'en-US', 92, 'Latin', 'Latin', 'Latin');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lb', 'en-US', 93, 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lg', 'en-US', 94, 'Ganda', 'Ganda', 'Ganda');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('li', 'en-US', 95, 'Limburgish', 'Limburgish', 'Limburgan, Limburger, Limburgish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ln', 'en-US', 96, 'Lingala', 'Lingala', 'Lingala');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lo', 'en-US', 97, 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lt', 'en-US', 98, 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lu', 'en-US', 99, 'Luba-Katanga', 'Luba-Katanga', 'Luba-Katanga');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lv', 'en-US', 100, 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mg', 'en-US', 101, 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mi', 'en-US', 103, 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mk', 'en-US', 104, 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ml', 'en-US', 105, 'Malayalam', 'Malayalam', 'Malayalam');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mn', 'en-US', 106, 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mr', 'en-US', 107, 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ms', 'en-US', 108, 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mt', 'en-US', 109, 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('my', 'en-US', 110, 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('nd', 'en-US', 112, 'Northern Ndebele', 'Northern Ndebele', 'Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ne', 'en-US', 113, 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ng', 'en-US', 114, 'Ndonga', 'Ndonga', 'Ndonga');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('nl', 'en-US', 115, 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('no', 'en-US', 116, 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('nr', 'en-US', 117, 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('nv', 'en-US', 118, 'Navajo', 'Navajo', 'Navajo, Navaho');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ny', 'en-US', 119, 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('oc', 'en-US', 120, 'Occitan', 'Occitan', 'Occitan (post 1500)');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('oj', 'en-US', 121, 'Ojibwa', 'Ojibwa', 'Ojibwa');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('om', 'en-US', 122, 'Oromo', 'Oromo', 'Oromo');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('or', 'en-US', 123, 'Oriya', 'Oriya', 'Oriya');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('os', 'en-US', 124, 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('pa', 'en-US', 125, 'Punjabi', 'Punjabi', 'Panjabi, Punjabi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('pl', 'en-US', 127, 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ps', 'en-US', 128, 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('pt', 'en-US', 129, 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('qu', 'en-US', 130, 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('rm', 'en-US', 131, 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('rn', 'en-US', 132, 'Rundi', 'Rundi', 'Rundi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ro', 'en-US', 133, 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ru', 'en-US', 134, 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('rw', 'en-US', 135, 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sc', 'en-US', 137, 'Sardinian', 'Sardinian', 'Sardinian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sd', 'en-US', 138, 'Sindhi', 'Sindhi', 'Sindhi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('se', 'en-US', 139, 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sg', 'en-US', 140, 'Sango', 'Sango', 'Sango');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('si', 'en-US', 141, 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sk', 'en-US', 142, 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sl', 'en-US', 143, 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sm', 'en-US', 144, 'Samoan', 'Samoan', 'Samoan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sn', 'en-US', 145, 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('so', 'en-US', 146, 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sq', 'en-US', 147, 'Albanian', 'Albanian', 'Albanian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sr', 'en-US', 148, 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ss', 'en-US', 149, 'Swati', 'Swati', 'Swati');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('st', 'en-US', 150, 'Southern Sotho', 'Southern Sotho', ' Southern Sotho');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('su', 'en-US', 151, 'Sundanese', 'Sundanese', 'Sundanese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sv', 'en-US', 152, 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sw', 'en-US', 153, 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ta', 'en-US', 154, 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('te', 'en-US', 155, 'Telugu', 'Telugu', 'Telugu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tg', 'en-US', 156, 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('th', 'en-US', 157, 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ti', 'en-US', 158, 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tk', 'en-US', 159, 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tl', 'en-US', 160, 'Tagalog', 'Tagalog', 'Tagalog');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tn', 'en-US', 161, 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('to', 'en-US', 162, 'Tongan', 'Tongan', 'Tonga (Tonga Islands)');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tr', 'en-US', 163, 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ts', 'en-US', 164, 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tt', 'en-US', 165, 'Tatar', 'Tatar', 'Tatar');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tw', 'en-US', 166, 'Twi', 'Twi', 'Twi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ty', 'en-US', 167, 'Tahitian', 'Tahitian', 'Tahitian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ug', 'en-US', 168, 'Uyghur', 'Uyghur', 'Uighur, Uyghur');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('uk', 'en-US', 169, 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ur', 'en-US', 170, 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('uz', 'en-US', 171, 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ve', 'en-US', 172, 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('vi', 'en-US', 173, 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('wa', 'en-US', 175, 'Walloon', 'Walloon', 'Walloon');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('wo', 'en-US', 176, 'Wolof', 'Wolof', 'Wolof');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('xh', 'en-US', 177, 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('yi', 'en-US', 178, 'Yiddish', 'Yiddish', 'Yiddish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('yo', 'en-US', 179, 'Yoruba', 'Yoruba', 'Yoruba');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('za', 'en-US', 180, 'Zhuang', 'Zhuang', 'Zhuang, Chuang');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('zh', 'en-US', 181, 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('zu', 'en-US', 182, 'Zulu', 'Zulu', 'Zulu');

INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('aa', 'en-ZA', 1, 'Afar', 'Afar', 'Afar');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ab', 'en-ZA', 2, 'Abkhazian', 'Abkhazian', 'Abkhazian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('af', 'en-ZA', 4, 'Afrikaans', 'Afrikaans', 'Afrikaans');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ak', 'en-ZA', 5, 'Akan', 'Akan', 'Akan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('am', 'en-ZA', 6, 'Amharic', 'Amharic', 'Amharic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ar', 'en-ZA', 8, 'Arabic', 'Arabic', 'Arabic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('as', 'en-ZA', 9, 'Assamese', 'Assamese', 'Assamese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('av', 'en-ZA', 10, 'Avaric', 'Avaric', 'Avaric');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ay', 'en-ZA', 11, 'Aymara', 'Aymara', 'Aymara');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('az', 'en-ZA', 12, 'Azerbaijani', 'Azerbaijani', 'Azerbaijani');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ba', 'en-ZA', 13, 'Bashkir', 'Bashkir', 'Bashkir');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('be', 'en-ZA', 14, 'Belarusian', 'Belarusian', 'Belarusian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bg', 'en-ZA', 15, 'Bulgarian', 'Bulgarian', 'Bulgarian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bh', 'en-ZA', 16, 'Bihari', 'Bihari', 'Bihari languages');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bi', 'en-ZA', 17, 'Bislama', 'Bislama', 'Bislama');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bm', 'en-ZA', 18, 'Bambara', 'Bambara', 'Bambara');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bn', 'en-ZA', 19, 'Bengali', 'Bengali', 'Bengali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bo', 'en-ZA', 20, 'Tibetan', 'Tibetan', 'Tibetan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('bs', 'en-ZA', 22, 'Bosnian', 'Bosnian', 'Bosnian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ca', 'en-ZA', 23, 'Catalan', 'Catalan', 'Catalan, Valencian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ce', 'en-ZA', 24, 'Chechen', 'Chechen', 'Chechen');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('co', 'en-ZA', 26, 'Corsican', 'Corsican', 'Corsican');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cr', 'en-ZA', 27, 'Cree', 'Cree', 'Cree');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cs', 'en-ZA', 28, 'Czech', 'Czech', 'Czech');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cu', 'en-ZA', 29, 'Church Slavic', 'Church Slavic', 'Church Slavic, Old Slavonic, Church Slavonic, Old Bulgarian, Old Church Slavonic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cv', 'en-ZA', 30, 'Chuvash', 'Chuvash', 'Chuvash');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('cy', 'en-ZA', 31, 'Welsh', 'Welsh', 'Welsh');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('da', 'en-ZA', 32, 'Danish', 'Danish', 'Danish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('de', 'en-ZA', 33, 'German', 'German', 'German');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('dv', 'en-ZA', 34, 'Maldivian', 'Maldivian', 'Divehi, Dhivehi, Maldivian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('dz', 'en-ZA', 35, 'Dzongkha', 'Dzongkha', 'Dzongkha');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ee', 'en-ZA', 36, 'Ewe', 'Ewe', 'Ewe');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('el', 'en-ZA', 37, 'Greek', 'Greek', 'Greek Modern (1453-)');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('en', 'en-ZA', 38, 'English', 'English', 'English');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('es', 'en-ZA', 40, 'Spanish', 'Spanish', 'Spanish, Castilian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('et', 'en-ZA', 41, 'Estonian', 'Estonian', 'Estonian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('eu', 'en-ZA', 42, 'Basque', 'Basque', 'Basque');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fa', 'en-ZA', 43, 'Persian', 'Persian', 'Persian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ff', 'en-ZA', 44, 'Fulah', 'Fulah', 'Fulah');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fi', 'en-ZA', 45, 'Finnish', 'Finnish', 'Finnish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fj', 'en-ZA', 46, 'Fijian', 'Fijian', 'Fijian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fo', 'en-ZA', 47, 'Faroese', 'Faroese', 'Faroese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fr', 'en-ZA', 48, 'French', 'French', 'French');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('fy', 'en-ZA', 49, 'Western Frisian', 'Western Frisian', 'Western Frisian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ga', 'en-ZA', 50, 'Irish', 'Irish', 'Irish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('gd', 'en-ZA', 51, 'Scottish Gaelic', 'Scottish Gaelic', 'Gaelic, Scottish Gaelic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('gl', 'en-ZA', 52, 'Galician', 'Galician', 'Galician');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('gn', 'en-ZA', 53, 'Guarani', 'Guarani', 'Guarani');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('gu', 'en-ZA', 54, 'Gujarati', 'Gujarati', 'Gujarati');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ha', 'en-ZA', 56, 'Hausa', 'Hausa', 'Hausa');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('he', 'en-ZA', 57, 'Hebrew', 'Hebrew', 'Hebrew');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hi', 'en-ZA', 58, 'Hindi', 'Hindi', 'Hindi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ho', 'en-ZA', 59, 'Hiri Motu', 'Hiri Motu', 'Hiri Motu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hr', 'en-ZA', 60, 'Croatian', 'Croatian', 'Croatian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ht', 'en-ZA', 61, 'Haitian Creole', 'Haitian Creole', 'Haitian, Haitian Creole');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hu', 'en-ZA', 62, 'Hungarian', 'Hungarian', 'Hungarian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hy', 'en-ZA', 63, 'Armenian', 'Armenian', 'Armenian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('hz', 'en-ZA', 64, 'Herero', 'Herero', 'Herero');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('id', 'en-ZA', 66, 'Indonesian', 'Indonesian', 'Indonesian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ie', 'en-ZA', 67, 'Interlingue', 'Interlingue', 'Interlingue, Occidental');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ig', 'en-ZA', 68, 'Igbo', 'Igbo', 'Igbo');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ii', 'en-ZA', 69, 'Nuosu', 'Nuosu', 'Sichuan Yi, Nuosu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ik', 'en-ZA', 70, 'Inupiaq', 'Inupiaq', 'Inupiaq');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('is', 'en-ZA', 72, 'Icelandic', 'Icelandic', 'Icelandic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('it', 'en-ZA', 73, 'Italian', 'Italian', 'Italian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('iu', 'en-ZA', 74, 'Inuktitut', 'Inuktitut', 'Inuktitut');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ja', 'en-ZA', 75, 'Japanese', 'Japanese', 'Japanese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('jv', 'en-ZA', 76, 'Javanese', 'Javanese', 'Javanese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ka', 'en-ZA', 77, 'Georgian', 'Georgian', 'Georgian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kg', 'en-ZA', 78, 'Kongo', 'Kongo', 'Kongo');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ki', 'en-ZA', 79, 'Kikuyu', 'Kikuyu', 'Kikuyu, Gikuyu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kj', 'en-ZA', 80, 'Kwanyama', 'Kwanyama', 'Kuanyama, Kwanyama');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kk', 'en-ZA', 81, 'Kazakh', 'Kazakh', 'Kazakh');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kl', 'en-ZA', 82, 'Kalaallisut', 'Kalaallisut', 'Kalaallisut, Greenlandic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('km', 'en-ZA', 83, 'Central Khmer', 'Central Khmer', 'Central Khmer');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kn', 'en-ZA', 84, 'Kannada', 'Kannada', 'Kannada');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ko', 'en-ZA', 85, 'Korean', 'Korean', 'Korean');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kr', 'en-ZA', 86, 'Kanuri', 'Kanuri', 'Kanuri');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ks', 'en-ZA', 87, 'Kashmiri', 'Kashmiri', 'Kashmiri');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ku', 'en-ZA', 88, 'Kurdish', 'Kurdish', 'Kurdish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kv', 'en-ZA', 89, 'Komi', 'Komi', 'Komi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('kw', 'en-ZA', 90, 'Cornish', 'Cornish', 'Cornish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ky', 'en-ZA', 91, 'Kyrgyz', 'Kyrgyz', 'Kirghiz, Kyrgyz');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('la', 'en-ZA', 92, 'Latin', 'Latin', 'Latin');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lb', 'en-ZA', 93, 'Luxembourgish', 'Luxembourgish', 'Luxembourgish, Letzeburgesch');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lg', 'en-ZA', 94, 'Ganda', 'Ganda', 'Ganda');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('li', 'en-ZA', 95, 'Limburgish', 'Limburgish', 'Limburgan, Limburger, Limburgish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ln', 'en-ZA', 96, 'Lingala', 'Lingala', 'Lingala');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lo', 'en-ZA', 97, 'Lao', 'Lao', 'Lao');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lt', 'en-ZA', 98, 'Lithuanian', 'Lithuanian', 'Lithuanian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lu', 'en-ZA', 99, 'Luba-Katanga', 'Luba-Katanga', 'Luba-Katanga');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('lv', 'en-ZA', 100, 'Latvian', 'Latvian', 'Latvian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mg', 'en-ZA', 101, 'Malagasy', 'Malagasy', 'Malagasy');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mi', 'en-ZA', 103, 'Maori', 'Maori', 'Maori');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mk', 'en-ZA', 104, 'Macedonian', 'Macedonian', 'Macedonian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ml', 'en-ZA', 105, 'Malayalam', 'Malayalam', 'Malayalam');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mn', 'en-ZA', 106, 'Mongolian', 'Mongolian', 'Mongolian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mr', 'en-ZA', 107, 'Marathi', 'Marathi', 'Marathi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ms', 'en-ZA', 108, 'Malay', 'Malay', 'Malay');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('mt', 'en-ZA', 109, 'Maltese', 'Maltese', 'Maltese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('my', 'en-ZA', 110, 'Burmese', 'Burmese', 'Burmese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('nd', 'en-ZA', 112, 'Northern Ndebele', 'Northern Ndebele', 'Northern Ndebele, North Ndebele');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ne', 'en-ZA', 113, 'Nepali', 'Nepali', 'Nepali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ng', 'en-ZA', 114, 'Ndonga', 'Ndonga', 'Ndonga');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('nl', 'en-ZA', 115, 'Dutch', 'Dutch', 'Dutch, Flemish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('no', 'en-ZA', 116, 'Norwegian', 'Norwegian', 'Norwegian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('nr', 'en-ZA', 117, 'Southern Ndebele', 'Southern Ndebele', 'Southern Ndebele, South Ndebele');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('nv', 'en-ZA', 118, 'Navajo', 'Navajo', 'Navajo, Navaho');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ny', 'en-ZA', 119, 'Chewa', 'Chewa', 'Chichewa, Chewa, Nyanja');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('oc', 'en-ZA', 120, 'Occitan', 'Occitan', 'Occitan (post 1500)');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('oj', 'en-ZA', 121, 'Ojibwa', 'Ojibwa', 'Ojibwa');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('om', 'en-ZA', 122, 'Oromo', 'Oromo', 'Oromo');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('or', 'en-ZA', 123, 'Oriya', 'Oriya', 'Oriya');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('os', 'en-ZA', 124, 'Ossetian', 'Ossetian', 'Ossetian, Ossetic');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('pa', 'en-ZA', 125, 'Punjabi', 'Punjabi', 'Panjabi, Punjabi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('pl', 'en-ZA', 127, 'Polish', 'Polish', 'Polish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ps', 'en-ZA', 128, 'Pashto', 'Pashto', 'Pushto, Pashto');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('pt', 'en-ZA', 129, 'Portuguese', 'Portuguese', 'Portuguese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('qu', 'en-ZA', 130, 'Quechua', 'Quechua', 'Quechua');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('rm', 'en-ZA', 131, 'Romansh', 'Romansh', 'Romansh');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('rn', 'en-ZA', 132, 'Rundi', 'Rundi', 'Rundi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ro', 'en-ZA', 133, 'Romanian', 'Romanian', 'Romanian, Moldavian, Moldovan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ru', 'en-ZA', 134, 'Russian', 'Russian', 'Russian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('rw', 'en-ZA', 135, 'Kinyarwanda', 'Kinyarwanda', 'Kinyarwanda');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sc', 'en-ZA', 137, 'Sardinian', 'Sardinian', 'Sardinian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sd', 'en-ZA', 138, 'Sindhi', 'Sindhi', 'Sindhi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('se', 'en-ZA', 139, 'Northern Sami', 'Northern Sami', 'Northern Sami');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sg', 'en-ZA', 140, 'Sango', 'Sango', 'Sango');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('si', 'en-ZA', 141, 'Sinhala', 'Sinhala', 'Sinhala, Sinhalese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sk', 'en-ZA', 142, 'Slovak', 'Slovak', 'Slovak');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sl', 'en-ZA', 143, 'Slovenian', 'Slovenian', 'Slovenian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sm', 'en-ZA', 144, 'Samoan', 'Samoan', 'Samoan');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sn', 'en-ZA', 145, 'Shona', 'Shona', 'Shona');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('so', 'en-ZA', 146, 'Somali', 'Somali', 'Somali');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sq', 'en-ZA', 147, 'Albanian', 'Albanian', 'Albanian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sr', 'en-ZA', 148, 'Serbian', 'Serbian', 'Serbian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ss', 'en-ZA', 149, 'Swati', 'Swati', 'Swati');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('st', 'en-ZA', 150, 'Southern Sotho', 'Southern Sotho', ' Southern Sotho');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('su', 'en-ZA', 151, 'Sundanese', 'Sundanese', 'Sundanese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sv', 'en-ZA', 152, 'Swedish', 'Swedish', 'Swedish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('sw', 'en-ZA', 153, 'Swahili', 'Swahili', 'Swahili');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ta', 'en-ZA', 154, 'Tamil', 'Tamil', 'Tamil');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('te', 'en-ZA', 155, 'Telugu', 'Telugu', 'Telugu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tg', 'en-ZA', 156, 'Tajik', 'Tajik', 'Tajik');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('th', 'en-ZA', 157, 'Thai', 'Thai', 'Thai');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ti', 'en-ZA', 158, 'Tigrinya', 'Tigrinya', 'Tigrinya');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tk', 'en-ZA', 159, 'Turkmen', 'Turkmen', 'Turkmen');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tl', 'en-ZA', 160, 'Tagalog', 'Tagalog', 'Tagalog');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tn', 'en-ZA', 161, 'Tswana', 'Tswana', 'Tswana');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('to', 'en-ZA', 162, 'Tongan', 'Tongan', 'Tonga (Tonga Islands)');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tr', 'en-ZA', 163, 'Turkish', 'Turkish', 'Turkish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ts', 'en-ZA', 164, 'Tsonga', 'Tsonga', 'Tsonga');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tt', 'en-ZA', 165, 'Tatar', 'Tatar', 'Tatar');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('tw', 'en-ZA', 166, 'Twi', 'Twi', 'Twi');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ty', 'en-ZA', 167, 'Tahitian', 'Tahitian', 'Tahitian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ug', 'en-ZA', 168, 'Uyghur', 'Uyghur', 'Uighur, Uyghur');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('uk', 'en-ZA', 169, 'Ukrainian', 'Ukrainian', 'Ukrainian');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ur', 'en-ZA', 170, 'Urdu', 'Urdu', 'Urdu');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('uz', 'en-ZA', 171, 'Uzbek', 'Uzbek', 'Uzbek');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('ve', 'en-ZA', 172, 'Venda', 'Venda', 'Venda');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('vi', 'en-ZA', 173, 'Vietnamese', 'Vietnamese', 'Vietnamese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('wa', 'en-ZA', 175, 'Walloon', 'Walloon', 'Walloon');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('wo', 'en-ZA', 176, 'Wolof', 'Wolof', 'Wolof');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('xh', 'en-ZA', 177, 'Xhosa', 'Xhosa', 'Xhosa');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('yi', 'en-ZA', 178, 'Yiddish', 'Yiddish', 'Yiddish');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('yo', 'en-ZA', 179, 'Yoruba', 'Yoruba', 'Yoruba');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('za', 'en-ZA', 180, 'Zhuang', 'Zhuang', 'Zhuang, Chuang');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('zh', 'en-ZA', 181, 'Chinese', 'Chinese', 'Chinese');
INSERT INTO reference.languages (code, locale, sort_index, name, short_name, description)
   VALUES ('zu', 'en-ZA', 182, 'Zulu', 'Zulu', 'Zulu');   