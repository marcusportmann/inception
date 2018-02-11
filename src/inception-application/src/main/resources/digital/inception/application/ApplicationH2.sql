-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA service_registry;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE service_registry.service_registry (
  name                 VARCHAR(4000) NOT NULL,
  security_type        INTEGER       NOT NULL,
  supports_compression CHAR(1)       NOT NULL,
  endpoint             VARCHAR(4000) NOT NULL,
  service_class        VARCHAR(4000) NOT NULL,
  wsdl_location        VARCHAR(4000) NOT NULL,
  username             VARCHAR(4000),
  password             VARCHAR(4000),

  PRIMARY KEY (name)
);

COMMENT ON COLUMN service_registry.service_registry.name
IS 'The name used to uniquely identify the web service';

COMMENT ON COLUMN service_registry.service_registry.security_type
IS 'The type of security service implemented by the web service i.e. 0 = None, 1 = Mutual SSL, etc';

COMMENT ON COLUMN service_registry.service_registry.supports_compression
IS 'Does the web service support compression';

COMMENT ON COLUMN service_registry.service_registry.endpoint
IS 'The endpoint for the web service';

COMMENT ON COLUMN service_registry.service_registry.service_class
IS 'The fully qualified name of the Java service class';

COMMENT ON COLUMN service_registry.service_registry.wsdl_location
IS 'The location of the WSDL defining the web service on the classpath';

COMMENT ON COLUMN service_registry.service_registry.username
IS 'The username to use when accessing a web service with username-password security enabled';

COMMENT ON COLUMN service_registry.service_registry.password
IS 'The password to use when accessing a web service with username-password security enabled';

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
