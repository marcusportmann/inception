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
  type      INTEGER      NOT NULL,
  updated   TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE INDEX parties_tenant_id_ix ON party.parties(tenant_id);

COMMENT ON COLUMN party.parties.created IS 'The date and time the party was created';

COMMENT ON COLUMN party.parties.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the party';

COMMENT ON COLUMN party.parties.name IS 'The name of the party';

COMMENT ON COLUMN party.parties.tenant_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the tenant the party is associated with';

COMMENT ON COLUMN party.parties.type IS 'The code identifying the type of party';

COMMENT ON COLUMN party.parties.updated IS 'The date and time the party was last updated';


CREATE TABLE party.persons (
  correspondence_language VARCHAR(10),
  country_of_birth        VARCHAR(10),
  country_of_residence    VARCHAR(10),
  date_of_birth           DATE,
  date_of_death           DATE,
  employment_status       VARCHAR(10),
  employment_type         VARCHAR(10),
  gender                  VARCHAR(10),
  given_name              VARCHAR(100),
  home_language           VARCHAR(10),
  id                      UUID          NOT NULL,
  initials                VARCHAR(20),
  maiden_name             VARCHAR(100),
  marital_status          VARCHAR(10),
  marriage_type           VARCHAR(10),
  middle_names            VARCHAR(100),
  preferred_name          VARCHAR(100),
  race                    VARCHAR(10),
  residency_status        VARCHAR(10),
  surname                 VARCHAR(100),
  title                   VARCHAR(10),

  PRIMARY KEY (id),
  CONSTRAINT persons_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX persons_date_of_birth_ix ON party.persons(date_of_birth);

COMMENT ON COLUMN party.persons.correspondence_language IS 'The optional code identifying the correspondence language for the person';

COMMENT ON COLUMN party.persons.country_of_birth IS 'The optional code identifying the country of birth for the person';

COMMENT ON COLUMN party.persons.country_of_residence IS 'The optional code identifying the country of residence for the person';

COMMENT ON COLUMN party.persons.date_of_birth IS 'The optional date of birth for the person';

COMMENT ON COLUMN party.persons.date_of_death IS 'The optional date of death for the person';

COMMENT ON COLUMN party.persons.employment_status IS 'The optional code identifying the employment status for the person';

COMMENT ON COLUMN party.persons.employment_type IS 'The optional code identifying the employment type for the person';

COMMENT ON COLUMN party.persons.gender IS 'The optional code identifying the gender for the person';

COMMENT ON COLUMN party.persons.given_name IS 'The optional given name for the person';

COMMENT ON COLUMN party.persons.home_language IS 'The optional code identifying the home language for the person';

COMMENT ON COLUMN party.persons.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the person';

COMMENT ON COLUMN party.persons.initials IS 'The optional initials for the person';

COMMENT ON COLUMN party.persons.maiden_name IS 'The optional maiden name for the person';

COMMENT ON COLUMN party.persons.marital_status IS 'The optional code identifying the marital status for the person';

COMMENT ON COLUMN party.persons.marriage_type IS 'The optional code identifying the marriage type for the person if the person is married';

COMMENT ON COLUMN party.persons.middle_names IS 'The optional middle names for the person';

COMMENT ON COLUMN party.persons.preferred_name IS 'The optional preferred name for the person';

COMMENT ON COLUMN party.persons.race IS 'The optional code identifying the race for the person';

COMMENT ON COLUMN party.persons.residency_status IS 'The optional code identifying the residency status for the person';

COMMENT ON COLUMN party.persons.surname IS 'The optional surname for the person';

COMMENT ON COLUMN party.persons.title IS 'The optional code identifying the title for the person';


CREATE TABLE party.identity_documents (
  country_of_issue VARCHAR(10) NOT NULL,
  created          TIMESTAMP   NOT NULL,
  date_of_expiry   DATE,
  date_of_issue    DATE        NOT NULL,
  number           VARCHAR(50) NOT NULL,
  person_id        UUID        NOT NULL,
  type             VARCHAR(10) NOT NULL,
  updated          TIMESTAMP,

  PRIMARY KEY (person_id, type, country_of_issue, date_of_issue),
  CONSTRAINT identity_documents_person_fk FOREIGN KEY (person_id) REFERENCES party.persons(id) ON DELETE CASCADE
);

CREATE INDEX identity_documents_person_id_ix ON party.identity_documents(person_id);

COMMENT ON COLUMN party.identity_documents.country_of_issue IS 'The code identifying the country of issue for the identity document';

COMMENT ON COLUMN party.identity_documents.created IS 'The date and time the identity document was created';

COMMENT ON COLUMN party.identity_documents.date_of_expiry IS 'The optional date of expiry for the identity document';

COMMENT ON COLUMN party.identity_documents.date_of_issue IS 'The date of issue for the identity document';

COMMENT ON COLUMN party.identity_documents.number IS 'The number for the identity document';

COMMENT ON COLUMN party.identity_documents.person_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the person the identity document is associated with';

COMMENT ON COLUMN party.identity_documents.type IS 'The code identifying the type of identity document';

COMMENT ON COLUMN party.identity_documents.updated IS 'The date and time the identity document was last updated';


CREATE TABLE party.organizations (
  id UUID NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT organizations_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

COMMENT ON COLUMN party.organizations.id IS 'The Universally Unique Identifier (UUID) uniquely identifying the organization';


CREATE TABLE party.contact_mechanisms (
  created  TIMESTAMP    NOT NULL,
  party_id UUID         NOT NULL,
  purpose  INTEGER      NOT NULL,
  type     INTEGER      NOT NULL,
  updated  TIMESTAMP,
  value    VARCHAR(200) NOT NULL,

  PRIMARY KEY (party_id, type, purpose),
  CONSTRAINT contact_mechanisms_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX contact_mechanisms_party_id_ix ON party.contact_mechanisms(party_id);

COMMENT ON COLUMN party.contact_mechanisms.created IS 'The date and time the contact mechanism was created';

COMMENT ON COLUMN party.contact_mechanisms.party_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the party the contact mechanism is associated with';

COMMENT ON COLUMN party.contact_mechanisms.purpose IS 'The code identifying the contact mechanism purpose';

COMMENT ON COLUMN party.contact_mechanisms.type IS 'The code identifying the contact mechanism type';

COMMENT ON COLUMN party.contact_mechanisms.updated IS 'The date and time the contact mechanism was last updated';

COMMENT ON COLUMN party.contact_mechanisms.value IS 'The value for the contact mechanism';


CREATE TABLE party.physical_addresses (
  building_floor      VARCHAR(20),
  building_name       VARCHAR(50),
  building_room       VARCHAR(20),
  city                VARCHAR(50),
  complex_name        VARCHAR(50),
  complex_unit_number VARCHAR(20),
  created             TIMESTAMP    NOT NULL,
  country             VARCHAR(10)  NOT NULL,
  farm_description    VARCHAR(50),
  farm_name           VARCHAR(50),
  farm_number         VARCHAR(50),
  line1               VARCHAR(100),
  line2               VARCHAR(100),
  line3               VARCHAR(100),
  latitude            VARCHAR(50),
  longitude           VARCHAR(50),
  party_id            UUID         NOT NULL,
  postal_code         VARCHAR(30)  NOT NULL,
  purpose             INTEGER      NOT NULL,
  region              VARCHAR(10),
  site_block          VARCHAR(50),
  site_number         VARCHAR(50),
  street_name         VARCHAR(100),
  street_number       VARCHAR(30),
  suburb              VARCHAR(50),
  type                INTEGER      NOT NULL,
  updated             TIMESTAMP,

  PRIMARY KEY (party_id, type, purpose),
  CONSTRAINT physical_addresses_party_fk FOREIGN KEY (party_id) REFERENCES party.parties(id) ON DELETE CASCADE
);

CREATE INDEX physical_addresses_party_id_ix ON party.physical_addresses(party_id);


COMMENT ON COLUMN party.physical_addresses.building_floor IS 'The building floor for the physical address';

COMMENT ON COLUMN party.physical_addresses.building_name IS 'The building name for the physical address that is required for a building address';

COMMENT ON COLUMN party.physical_addresses.building_room IS 'The building room for the physical address';

COMMENT ON COLUMN party.physical_addresses.city IS 'The town or town or city for the physical address';

COMMENT ON COLUMN party.physical_addresses.complex_name IS 'The complex name for the physical address that is required for a complex address';

COMMENT ON COLUMN party.physical_addresses.complex_unit_number IS 'The complex unit number for the physical address that is required for a complex address';

COMMENT ON COLUMN party.physical_addresses.created IS 'The date and time the physical address was created';

COMMENT ON COLUMN party.physical_addresses.country IS 'The ISO 3166-1 alpha-2 code for the country for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_description IS 'The farm description for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_name IS 'The farm name for the physical address';

COMMENT ON COLUMN party.physical_addresses.farm_number IS 'The farm number for the physical address that is required for a farm address';

COMMENT ON COLUMN party.physical_addresses.line1 IS 'The address line 1 for the physical address that is required for an international or unstructured address';

COMMENT ON COLUMN party.physical_addresses.line2 IS 'The address line 2 for the physical address';

COMMENT ON COLUMN party.physical_addresses.line3 IS 'The address line 3 for the physical address';

COMMENT ON COLUMN party.physical_addresses.latitude IS 'The optional GPS latitude for the physical address';

COMMENT ON COLUMN party.physical_addresses.longitude IS 'The optional GPS longitude for the physical address';

COMMENT ON COLUMN party.physical_addresses.party_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the party the physical address is associated with';

COMMENT ON COLUMN party.physical_addresses.postal_code IS 'The postal code for the physical address';

COMMENT ON COLUMN party.physical_addresses.purpose IS 'The code identifying the physical address purpose';

COMMENT ON COLUMN party.physical_addresses.region IS 'The optional code identifying the region for the physical address';

COMMENT ON COLUMN party.physical_addresses.site_block IS 'The site block for the physical address that is required for a site address';

COMMENT ON COLUMN party.physical_addresses.site_number IS 'The site number for the physical address that is required for a site address';

COMMENT ON COLUMN party.physical_addresses.street_name IS 'The street name for the physical address that is required for a street address';

COMMENT ON COLUMN party.physical_addresses.street_number IS 'The street number for the physical address that is required for a street address';

COMMENT ON COLUMN party.physical_addresses.suburb IS 'The optional suburb for the physical address';

COMMENT ON COLUMN party.physical_addresses.type IS 'The code identifying the physical address type';

COMMENT ON COLUMN party.physical_addresses.updated IS 'The date and time the physical address was last updated';




-- CREATE TABLE party.relationships (
--   created          TIMESTAMP   NOT NULL,
--   party_id         UUID        NOT NULL,
--   related_party_id UUID        NOT NULL,
--   type             VARCHAR(10) NOT NULL,
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
-- COMMENT ON COLUMN party.relationships.party_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the party the relationship is associated with';
--
-- COMMENT ON COLUMN party.relationships.related_party_id IS 'The Universally Unique Identifier (UUID) uniquely identifying the party the relationship is associated with';




-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
