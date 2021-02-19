-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA customer;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE customer.customers (
   id            UUID        NOT NULL,
   customer_type VARCHAR(30) NOT NULL,

   PRIMARY KEY (id),
   CONSTRAINT customers_party_fk FOREIGN KEY (id) REFERENCES party.parties(id) ON DELETE CASCADE
);

COMMENT ON COLUMN customer.customers.id IS 'The Universally Unique Identifier (UUID) for the customer';

COMMENT ON COLUMN customer.customers.customer_type IS 'The code for the customer type';


CREATE TABLE customer.business_customers (
  id                UUID         NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT business_customers_customer_fk FOREIGN KEY (id) REFERENCES customer.customers(id) ON DELETE CASCADE
);

COMMENT ON COLUMN customer.business_customers.id IS 'The Universally Unique Identifier (UUID) for the business customer';


CREATE TABLE customer.individual_customers (
  emancipated_minor BOOLEAN,
  id                UUID         NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT individual_customers_customer_fk FOREIGN KEY (id) REFERENCES customer.customers(id) ON DELETE CASCADE
);

COMMENT ON COLUMN customer.individual_customers.emancipated_minor IS 'The optional boolean flag indicating whether the individual customer is an emancipated minor';

COMMENT ON COLUMN customer.individual_customers.id IS 'The Universally Unique Identifier (UUID) for the individual customer';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
