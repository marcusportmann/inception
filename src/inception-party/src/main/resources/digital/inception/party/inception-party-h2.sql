-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA party;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
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
  countries_of_tax_residence VARCHAR(100),
  id                         UUID          NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT organizations_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

COMMENT ON COLUMN party.organizations.countries_of_tax_residence IS 'The optional comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for the organization';

COMMENT ON COLUMN party.organizations.id IS 'The Universally Unique Identifier (UUID) for the organization';


CREATE TABLE party.persons (
  countries_of_tax_residence VARCHAR(100),
  country_of_birth           VARCHAR(30),
  country_of_residence       VARCHAR(30),
  date_of_birth              DATE,
  date_of_death              DATE,
  employment_status          VARCHAR(30),
  employment_type            VARCHAR(30),
  gender                     VARCHAR(30),
  given_name                 VARCHAR(100),
  home_language              VARCHAR(30),
  id                         UUID          NOT NULL,
  initials                   VARCHAR(20),
  maiden_name                VARCHAR(100),
  marital_status             VARCHAR(30),
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

COMMENT ON COLUMN party.persons.country_of_birth IS 'The optional code for the country of birth for the person';

COMMENT ON COLUMN party.persons.country_of_residence IS 'The optional code for the country of residence for the person';

COMMENT ON COLUMN party.persons.countries_of_tax_residence IS 'The optional comma-delimited ISO 3166-1 alpha-2 codes for the countries of tax residence for the person';

COMMENT ON COLUMN party.persons.date_of_birth IS 'The optional date of birth for the person';

COMMENT ON COLUMN party.persons.date_of_death IS 'The optional date of death for the person';

COMMENT ON COLUMN party.persons.employment_status IS 'The optional code for the employment status for the person';

COMMENT ON COLUMN party.persons.employment_type IS 'The optional code for the employment type for the person';

COMMENT ON COLUMN party.persons.gender IS 'The optional code for the gender for the person';

COMMENT ON COLUMN party.persons.given_name IS 'The optional given name for the person';

COMMENT ON COLUMN party.persons.home_language IS 'The optional code for the home language for the person';

COMMENT ON COLUMN party.persons.id IS 'The Universally Unique Identifier (UUID) for the person';

COMMENT ON COLUMN party.persons.initials IS 'The optional initials for the person';

COMMENT ON COLUMN party.persons.maiden_name IS 'The optional maiden name for the person';

COMMENT ON COLUMN party.persons.marital_status IS 'The optional code for the marital status for the person';

COMMENT ON COLUMN party.persons.marriage_type IS 'The optional code for the marriage type for the person if the person is married';

COMMENT ON COLUMN party.persons.middle_names IS 'The optional middle names for the person';

COMMENT ON COLUMN party.persons.occupation IS 'The optional code for the occupation for the person';

COMMENT ON COLUMN party.persons.preferred_name IS 'The optional preferred name for the person';

COMMENT ON COLUMN party.persons.race IS 'The optional code for the race for the person';

COMMENT ON COLUMN party.persons.residency_status IS 'The optional code for the residency status for the person';

COMMENT ON COLUMN party.persons.residential_type IS 'The optional code for the residential type for the person';

COMMENT ON COLUMN party.persons.surname IS 'The optional surname for the person';

COMMENT ON COLUMN party.persons.tax_number IS 'The optional tax number for the person';

COMMENT ON COLUMN party.persons.tax_number_type IS 'The optional code for the tax number type for the person';

COMMENT ON COLUMN party.persons.title IS 'The optional code for the title for the person';




CREATE TABLE party.contact_mechanisms (
  created  TIMESTAMP    NOT NULL,
  party_id UUID         NOT NULL,
  purpose  VARCHAR(30)  NOT NULL,
  type     VARCHAR(30)  NOT NULL,
  updated  TIMESTAMP,
  value    VARCHAR(200) NOT NULL,

  PRIMARY KEY (party_id, type, purpose),
  CONSTRAINT contact_mechanisms_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX contact_mechanisms_party_id_ix ON party.contact_mechanisms(party_id);

COMMENT ON COLUMN party.contact_mechanisms.created IS 'The date and time the contact mechanism was created';

COMMENT ON COLUMN party.contact_mechanisms.party_id IS 'The Universally Unique Identifier (UUID) for the party the contact mechanism is associated with';

COMMENT ON COLUMN party.contact_mechanisms.purpose IS 'The code for the contact mechanism purpose';

COMMENT ON COLUMN party.contact_mechanisms.type IS 'The code for the contact mechanism type';

COMMENT ON COLUMN party.contact_mechanisms.updated IS 'The date and time the contact mechanism was last updated';

COMMENT ON COLUMN party.contact_mechanisms.value IS 'The value for the contact mechanism';


CREATE TABLE party.identity_documents (
  country_of_issue VARCHAR(30) NOT NULL,
  created          TIMESTAMP   NOT NULL,
  date_of_expiry   DATE,
  date_of_issue    DATE        NOT NULL,
  number           VARCHAR(30) NOT NULL,
  party_id         UUID        NOT NULL,
  type             VARCHAR(30) NOT NULL,
  updated          TIMESTAMP,

  PRIMARY KEY (party_id, type, country_of_issue, date_of_issue),
  CONSTRAINT identity_documents_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX identity_documents_party_id_ix ON party.identity_documents(party_id);

COMMENT ON COLUMN party.identity_documents.country_of_issue IS 'The code for the country of issue for the identity document';

COMMENT ON COLUMN party.identity_documents.created IS 'The date and time the identity document was created';

COMMENT ON COLUMN party.identity_documents.date_of_expiry IS 'The optional date of expiry for the identity document';

COMMENT ON COLUMN party.identity_documents.date_of_issue IS 'The date of issue for the identity document';

COMMENT ON COLUMN party.identity_documents.number IS 'The number for the identity document';

COMMENT ON COLUMN party.identity_documents.party_id IS 'The Universally Unique Identifier (UUID) for the party the identity document is associated with';

COMMENT ON COLUMN party.identity_documents.type IS 'The code for the identity document type';

COMMENT ON COLUMN party.identity_documents.updated IS 'The date and time the identity document was last updated';




CREATE TABLE party.party_roles (
  created  TIMESTAMP    NOT NULL,
  party_id UUID         NOT NULL,
  purpose  VARCHAR(30),
  type     VARCHAR(30)  NOT NULL,
  updated  TIMESTAMP,

  PRIMARY KEY (party_id, type),
  CONSTRAINT party_roles_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX party_roles_party_id_ix ON party.party_roles(party_id);

COMMENT ON COLUMN party.party_roles.created IS 'The date and time the party role was created';

COMMENT ON COLUMN party.party_roles.party_id IS 'The Universally Unique Identifier (UUID) for the party the party role is associated with';

COMMENT ON COLUMN party.party_roles.purpose IS 'The optional code for the party role purpose';

COMMENT ON COLUMN party.party_roles.type IS 'The code for the party role type';

COMMENT ON COLUMN party.party_roles.updated IS 'The date and time the party role was last updated';


CREATE TABLE party.physical_addresses (
  building_floor      VARCHAR(20),
  building_name       VARCHAR(50),
  building_room       VARCHAR(30),
  city                VARCHAR(50),
  complex_name        VARCHAR(50),
  complex_unit_number VARCHAR(20),
  country             VARCHAR(30)   NOT NULL,
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
  purposes            VARCHAR(300),
  region              VARCHAR(30),
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

COMMENT ON COLUMN party.physical_addresses.building_floor IS 'The optional building floor for the physical address';

COMMENT ON COLUMN party.physical_addresses.building_name IS 'The building name for the physical address that is required for a building address';

COMMENT ON COLUMN party.physical_addresses.building_room IS 'The optional building room for the physical address';

COMMENT ON COLUMN party.physical_addresses.city IS 'The town or city for the physical address that is required for a building, complex, postal, site or street address';

COMMENT ON COLUMN party.physical_addresses.complex_name IS 'The complex name for the physical address that is required for a complex address';

COMMENT ON COLUMN party.physical_addresses.complex_unit_number IS 'The complex unit number for the physical address that is required for a complex address';

COMMENT ON COLUMN party.physical_addresses.created IS 'The date and time the physical address was created';

COMMENT ON COLUMN party.physical_addresses.country IS 'The ISO 3166-1 alpha-2 code for the country for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_description IS 'The optional farm description for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_name IS 'The optional farm name for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_number IS 'The farm number for the physical address that is required for a farm address';

COMMENT ON COLUMN party.physical_addresses.id IS 'The Universally Unique Identifier (UUID) for the address';

COMMENT ON COLUMN party.physical_addresses.line1 IS 'The address line 1 for the physical address that is required for an international, postal or unstructured address';

COMMENT ON COLUMN party.physical_addresses.line2 IS 'The optional address line 2 for the physical address';

COMMENT ON COLUMN party.physical_addresses.line3 IS 'The optional address line 3 for the physical address';

COMMENT ON COLUMN party.physical_addresses.latitude IS 'The optional GPS latitude for the physical address';

COMMENT ON COLUMN party.physical_addresses.longitude IS 'The optional GPS longitude for the physical address';

COMMENT ON COLUMN party.physical_addresses.party_id IS 'The Universally Unique Identifier (UUID) for the party the physical address is associated with';

COMMENT ON COLUMN party.physical_addresses.postal_code IS 'The postal code for the physical address';

COMMENT ON COLUMN party.physical_addresses.purposes IS 'The optional comma-delimited codes for the physical address purposes';

COMMENT ON COLUMN party.physical_addresses.region IS 'The optional code for the region for the physical address';

COMMENT ON COLUMN party.physical_addresses.site_block IS 'The site block for the physical address that is required for a site address';

COMMENT ON COLUMN party.physical_addresses.site_number IS 'The site number for the physical address that is required for a site address';

COMMENT ON COLUMN party.physical_addresses.street_name IS 'The street name for the physical address that is required for a street address';

COMMENT ON COLUMN party.physical_addresses.street_number IS 'The street number for the physical address that is required for a street address';

COMMENT ON COLUMN party.physical_addresses.suburb IS 'The optional suburb for the physical address';

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

COMMENT ON COLUMN party.preferences.created IS 'The date and time the preferences was created';

COMMENT ON COLUMN party.preferences.party_id IS 'The Universally Unique Identifier (UUID) for the party the preference is associated with';

COMMENT ON COLUMN party.preferences.type IS 'The code for the preference type';

COMMENT ON COLUMN party.preferences.updated IS 'The date and time the preference was last updated';

COMMENT ON COLUMN party.preferences.value IS 'The value for the preference';


CREATE TABLE party.tax_numbers (
  country_of_issue VARCHAR(30) NOT NULL,
  created          TIMESTAMP   NOT NULL,
  number           VARCHAR(30) NOT NULL,
  party_id         UUID        NOT NULL,
  type             VARCHAR(30) NOT NULL,
  updated          TIMESTAMP,

  PRIMARY KEY (party_id, type),
  CONSTRAINT tax_numbers_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX tax_numbers_party_id_ix ON party.tax_numbers(party_id);

COMMENT ON COLUMN party.tax_numbers.country_of_issue IS 'The code for the country of issue for the tax number';

COMMENT ON COLUMN party.tax_numbers.created IS 'The date and time the tax number was created';

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
