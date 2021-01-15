-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA customer;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE customer.individual_customers (
  emancipated_minor BOOLEAN,
  id                UUID         NOT NULL,

  PRIMARY KEY (id)
);

-- CREATE INDEX parties_tenant_id_ix ON party.parties(tenant_id);

COMMENT ON COLUMN customer.individual_customers.emancipated_minor IS 'The optional boolean flag indicating whether the individual customer is an emancipated minor';

COMMENT ON COLUMN customer.individual_customers.id IS 'The Universally Unique Identifier (UUID) for the individual customer';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
