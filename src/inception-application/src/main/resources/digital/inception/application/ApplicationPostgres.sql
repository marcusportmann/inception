-- -------------------------------------------------------------------------------------------------
--  Execute the following command to start the database server if it is not running:
--
--    OS X: pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/postgres.log start
--    OS X (Homebrew): brew services start postgresql
--    CentOS (as root): service postgresql-9.6 start
--
--  Execute the following command to create the database:
--
--    OS X: createdb  --template=template0 --encoding=UTF8 dbname
--    CentOS (as root): sudo su postgres -c 'createdb --template=template1 --encoding=UTF8 dbname'
--
--  Execute the following command to initialise the database:
--
--    OS X: psql -d dbname -f application-name-postgres.sql
--    CentOS (as root): su postgres -c 'psql -d dbname -f application-name-postgres.sql'
--
--  Execute the following command to delete the database:
--
--    OS X: dropdb dbname
--    CentOS (as root): su postgres -c 'dropdb dbname'
--
--  Execute the following command to clean-up unreferenced large objects on the database:
--
--    OS X: vacuumlo dbname
--    CentOS (as root): su postgres -c 'vacuumlo dbname'
--
-- -------------------------------------------------------------------------------------------------
SET client_min_messages = 'warning';


-- -------------------------------------------------------------------------------------------------
-- DROP TABLES
-- -------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS error.error_reports CASCADE;
DROP TABLE IF EXISTS sms.sms CASCADE;
DROP TABLE IF EXISTS reporting.report_definitions CASCADE;
DROP TABLE IF EXISTS messaging.archived_messages CASCADE;
DROP TABLE IF EXISTS messaging.message_parts CASCADE;
DROP TABLE IF EXISTS messaging.messages CASCADE;
DROP TABLE IF EXISTS messaging.message_statuses CASCADE;
DROP TABLE IF EXISTS messaging.message_types CASCADE;
DROP TABLE IF EXISTS codes.codes CASCADE;
DROP TABLE IF EXISTS codes.code_categories CASCADE;
DROP TABLE IF EXISTS scheduler.job_parameters CASCADE;
DROP TABLE IF EXISTS scheduler.jobs CASCADE;
DROP TABLE IF EXISTS security.role_to_group_map CASCADE;
DROP TABLE IF EXISTS security.function_to_role_map CASCADE;
DROP TABLE IF EXISTS security.roles CASCADE;
DROP TABLE IF EXISTS security.functions CASCADE;
DROP TABLE IF EXISTS security.user_to_group_map CASCADE;
DROP TABLE IF EXISTS security.groups CASCADE;
DROP TABLE IF EXISTS security.users_password_history CASCADE;
DROP TABLE IF EXISTS security.users CASCADE;
DROP TABLE IF EXISTS security.user_directory_to_organization_map CASCADE;
DROP TABLE IF EXISTS security.user_directories CASCADE;
DROP TABLE IF EXISTS security.user_directory_types CASCADE;
DROP TABLE IF EXISTS security.organizations CASCADE;
DROP TABLE IF EXISTS service_registry.service_registry CASCADE;
DROP TABLE IF EXISTS configuration.configuration CASCADE;
DROP TABLE IF EXISTS idgenerator.idgenerator CASCADE;
DROP TABLE IF EXISTS test.test_data CASCADE;


-- -------------------------------------------------------------------------------------------------
-- DROP SEQUENCES
-- -------------------------------------------------------------------------------------------------
DROP SEQUENCE IF EXISTS sms.sms_id_seq;


-- -------------------------------------------------------------------------------------------------
-- DROP SCHEMAS
-- -------------------------------------------------------------------------------------------------
DROP SCHEMA IF EXISTS test CASCADE;
DROP SCHEMA IF EXISTS error CASCADE;
DROP SCHEMA IF EXISTS sms CASCADE;
DROP SCHEMA IF EXISTS service_registry CASCADE;
DROP SCHEMA IF EXISTS security CASCADE;
DROP SCHEMA IF EXISTS scheduler CASCADE;
DROP SCHEMA IF EXISTS reporting CASCADE;
DROP SCHEMA IF EXISTS messaging CASCADE;
DROP SCHEMA IF EXISTS idgenerator CASCADE;
DROP SCHEMA IF EXISTS configuration CASCADE;
DROP SCHEMA IF EXISTS codes CASCADE;


-- -------------------------------------------------------------------------------------------------
-- DROP ROLES
-- -------------------------------------------------------------------------------------------------
DROP
OWNED BY dbuser CASCADE;
DROP ROLE IF EXISTS dbuser;


-- -------------------------------------------------------------------------------------------------
-- CREATE ROLES
-- -------------------------------------------------------------------------------------------------
CREATE ROLE dbuser WITH LOGIN
  PASSWORD 'Password1';
ALTER
ROLE dbuser
WITH login;


-- -------------------------------------------------------------------------------------------------
-- CREATE PROCEDURES
-- -------------------------------------------------------------------------------------------------
--
-- CREATE OR REPLACE FUNCTION bytea_import(p_path text, p_result out bytea)
--                    language plpgsql as $$
-- declare
--   l_oid oid;
--   r record;
-- begin
--   p_result := '';
--   select lo_import(p_path) into l_oid;
--   for r in ( select data
--              from pg_largeobject
--              where loid = l_oid
--              order by pageno ) loop
--     p_result = p_result || r.data;
--   end loop;
--   perform lo_unlink(l_oid);
-- end;$$;


-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA codes;
CREATE SCHEMA configuration;
CREATE SCHEMA idgenerator;
CREATE SCHEMA messaging;
CREATE SCHEMA reporting;
CREATE SCHEMA scheduler;
CREATE SCHEMA security;
CREATE SCHEMA service_registry;
CREATE SCHEMA sms;
CREATE SCHEMA error;
CREATE SCHEMA test;


-------------------------------------------------------------------------------------------------
-- CREATE SEQUENCES
-- -------------------------------------------------------------------------------------------------
CREATE SEQUENCE sms.sms_id_seq;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE codes.code_categories (
  id      TEXT NOT NULL,
  name    TEXT NOT NULL,
  data    TEXT,
  updated TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN codes.code_categories.id IS 'The ID used to uniquely identify the code category';

COMMENT ON COLUMN codes.code_categories.name IS 'The name of the code category';

COMMENT ON COLUMN codes.code_categories.data IS 'The code data for the code category';

COMMENT ON COLUMN codes.code_categories.updated IS 'The date and time the code category was last updated';


CREATE TABLE codes.codes (
  id               TEXT NOT NULL,
  code_category_id TEXT NOT NULL,
  name             TEXT NOT NULL,
  value            TEXT NOT NULL,

  PRIMARY KEY (id, code_category_id),
  CONSTRAINT codes_code_category_fk FOREIGN KEY (code_category_id) REFERENCES codes.code_categories(id) ON DELETE CASCADE
);

CREATE INDEX codes_code_category_id_ix ON codes.codes(code_category_id);

COMMENT ON COLUMN codes.codes.id IS 'The ID used to uniquely identify the code';

COMMENT ON COLUMN codes.codes.code_category_id IS 'The ID used to uniquely identify the code category the code is associated with';

COMMENT ON COLUMN codes.codes.name IS 'The name of the code';

COMMENT ON COLUMN codes.codes.value IS 'The value for the code';


CREATE TABLE configuration.configuration (
  key         TEXT NOT NULL,
  value       TEXT NOT NULL,
  description TEXT NOT NULL,

  PRIMARY KEY (key)
);

COMMENT ON COLUMN configuration.configuration.key IS 'The key used to uniquely identify the configuration value';

COMMENT ON COLUMN configuration.configuration.value IS 'The value for the configuration value';

COMMENT ON COLUMN configuration.configuration.description IS 'The description for the configuration value';


CREATE TABLE idgenerator.idgenerator (
  name    TEXT NOT NULL,
  current BIGINT DEFAULT 0,

  PRIMARY KEY (name)
);

COMMENT ON COLUMN idgenerator.idgenerator.name IS 'The name giving the type of entity associated with the generated ID';

COMMENT ON COLUMN idgenerator.idgenerator.current IS 'The current ID for the type';


CREATE TABLE messaging.message_types (
  id   UUID NOT NULL,
  name TEXT NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN messaging.message_types.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message type';

COMMENT ON COLUMN messaging.message_types.name IS 'The name of the message type';


CREATE TABLE messaging.message_statuses (
  code INTEGER NOT NULL,
  name TEXT    NOT NULL,

  PRIMARY KEY (code)
);

COMMENT ON COLUMN messaging.message_statuses.code IS 'The code identifying the message status';

COMMENT ON COLUMN messaging.message_statuses.name IS 'The name of the message status';


CREATE TABLE messaging.messages (
  id                UUID      NOT NULL,
  username          TEXT      NOT NULL,
  device_id         UUID      NOT NULL,
  type_id           UUID      NOT NULL,
  correlation_id    UUID,
  priority          INTEGER   NOT NULL,
  status            INTEGER   NOT NULL,
  created           TIMESTAMP NOT NULL,
  data_hash         TEXT,
  encryption_iv     TEXT,
  send_attempts     INTEGER,
  process_attempts  INTEGER,
  download_attempts INTEGER,
  lock_name         TEXT,
  last_processed    TIMESTAMP,
  data              BYTEA,

  PRIMARY KEY (id),
  CONSTRAINT messages_message_type_fk FOREIGN KEY (type_id) REFERENCES messaging.message_types(id),
  CONSTRAINT messages_message_status_fk FOREIGN KEY (status) REFERENCES messaging.message_statuses(code)
);

CREATE INDEX messages_username_ix ON messaging.messages(username);

CREATE INDEX messages_device_id_ix ON messaging.messages(device_id);

CREATE INDEX messages_type_id_ix ON messaging.messages(type_id);

CREATE INDEX messages_priority_ix ON messaging.messages(priority);

CREATE INDEX messages_status_ix ON messaging.messages(status);

CREATE INDEX messages_lock_name_ix ON messaging.messages(lock_name);

COMMENT ON COLUMN messaging.messages.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN messaging.messages.username IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN messaging.messages.device_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from';

COMMENT ON COLUMN messaging.messages.type_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of message';

COMMENT ON COLUMN messaging.messages.correlation_id IS 'The optional Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN messaging.messages.priority IS 'The message priority';

COMMENT ON COLUMN messaging.messages.status IS 'The message status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN messaging.messages.created IS 'The date and time the message was created';

COMMENT ON COLUMN messaging.messages.data_hash IS 'The hash of the unencrypted data for the message if the message is encrypted';

COMMENT ON COLUMN messaging.messages.encryption_iv IS 'The base-64 encoded initialisation vector for the encryption scheme for the message';

COMMENT ON COLUMN messaging.messages.send_attempts IS 'The number of times that the sending of the message was attempted';

COMMENT ON COLUMN messaging.messages.process_attempts IS 'The number of times that the processing of the message was attempted';

COMMENT ON COLUMN messaging.messages.download_attempts IS 'The number of times that an attempt was made to download the message';

COMMENT ON COLUMN messaging.messages.lock_name IS 'The name of the entity that has locked the message for processing';

COMMENT ON COLUMN messaging.messages.last_processed IS 'The date and time the last attempt was made to process the message';

COMMENT ON COLUMN messaging.messages.data IS 'The data for the message';


CREATE TABLE messaging.message_parts (
  id                     UUID      NOT NULL,
  part_no                INTEGER   NOT NULL,
  total_parts            INTEGER   NOT NULL,
  send_attempts          INTEGER,
  download_attempts      INTEGER,
  status                 INTEGER   NOT NULL,
  message_id             UUID      NOT NULL,
  message_username       TEXT      NOT NULL,
  message_device_id      UUID      NOT NULL,
  message_type_id        UUID      NOT NULL,
  message_correlation_id UUID,
  message_priority       INTEGER   NOT NULL,
  message_created        TIMESTAMP NOT NULL,
  message_data_hash      TEXT,
  message_encryption_iv  TEXT,
  message_checksum       TEXT      NOT NULL,
  lock_name              TEXT,
  data                   BYTEA,

  PRIMARY KEY (id),
  CONSTRAINT message_parts_message_type_fk FOREIGN KEY (message_type_id) REFERENCES messaging.message_types(id)
);

CREATE INDEX message_parts_status_ix ON messaging.message_parts(status);

CREATE INDEX message_parts_message_id_ix ON messaging.message_parts(message_id);

CREATE INDEX message_parts_message_device_id_ix ON messaging.message_parts(message_device_id);

CREATE INDEX message_parts_message_type_id_ix ON messaging.message_parts(message_type_id);

CREATE INDEX message_parts_lock_name_ix ON messaging.message_parts(lock_name);

COMMENT ON COLUMN messaging.message_parts.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message part';

COMMENT ON COLUMN messaging.message_parts.part_no IS 'The number of the message part in the set of message parts for the original message';

COMMENT ON COLUMN messaging.message_parts.total_parts IS 'The total number of parts in the set of message parts for the original message';

COMMENT ON COLUMN messaging.message_parts.send_attempts IS 'The number of times that the sending of the message part was attempted';

COMMENT ON COLUMN messaging.message_parts.download_attempts IS 'The number of times that an attempt was made to download the message part';

COMMENT ON COLUMN messaging.message_parts.status IS 'The message part status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN messaging.message_parts.message_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the original message';

COMMENT ON COLUMN messaging.message_parts.message_username IS 'The username identifying the user associated with the original message';

COMMENT ON COLUMN messaging.message_parts.message_device_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the original message originated from';

COMMENT ON COLUMN messaging.message_parts.message_type_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of the original message';

COMMENT ON COLUMN messaging.message_parts.message_correlation_id IS 'The optional Universally Unique Identifier (UUID) used to correlate the original message';

COMMENT ON COLUMN messaging.message_parts.message_priority IS 'The priority for the original message';

COMMENT ON COLUMN messaging.message_parts.message_created IS 'The date and time the original message was created';

COMMENT ON COLUMN messaging.message_parts.message_data_hash IS 'The hash of the unencrypted data for the original message if the message was encrypted';

COMMENT ON COLUMN messaging.message_parts.message_encryption_iv IS 'The base-64 encoded initialisation vector for the encryption scheme for the original message';

COMMENT ON COLUMN messaging.message_parts.message_checksum IS 'The checksum for the original message';

COMMENT ON COLUMN messaging.message_parts.lock_name IS 'The name of the entity that has locked the message part for processing';

COMMENT ON COLUMN messaging.message_parts.data IS 'The data for the message part';


CREATE TABLE messaging.archived_messages (
  id             UUID      NOT NULL,
  username       TEXT      NOT NULL,
  device_id      UUID      NOT NULL,
  type_id        UUID      NOT NULL,
  correlation_id UUID,
  created        TIMESTAMP NOT NULL,
  archived       TIMESTAMP NOT NULL,
  data           BYTEA,

  PRIMARY KEY (id),
  CONSTRAINT archived_messages_message_type_fk FOREIGN KEY (type_id) REFERENCES messaging.message_types(id)
);

CREATE INDEX archived_messages_username_ix ON messaging.archived_messages(username);

CREATE INDEX archived_messages_device_id_ix ON messaging.archived_messages(device_id);

CREATE INDEX archived_messages_type_id_ix ON messaging.archived_messages(type_id);

COMMENT ON COLUMN messaging.archived_messages.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the message';

COMMENT ON COLUMN messaging.archived_messages.username IS 'The username identifying the user associated with the message';

COMMENT ON COLUMN messaging.archived_messages.device_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the device the message originated from';

COMMENT ON COLUMN messaging.archived_messages.type_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the type of message';

COMMENT ON COLUMN messaging.archived_messages.correlation_id IS 'The optional Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN messaging.archived_messages.created IS 'The date and time the message was created';

COMMENT ON COLUMN messaging.archived_messages.archived IS 'The date and time the message was archived';

COMMENT ON COLUMN messaging.archived_messages.data IS 'The data for the message';



CREATE TABLE reporting.report_definitions (
  id       UUID  NOT NULL,
  name     TEXT  NOT NULL,
  template BYTEA NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN reporting.report_definitions.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the report definition';

COMMENT ON COLUMN reporting.report_definitions.name IS 'The name of the report definition';

COMMENT ON COLUMN reporting.report_definitions.template IS 'The JasperReports template for the report definition';


CREATE TABLE scheduler.jobs (
  id                 UUID    NOT NULL,
  name               TEXT    NOT NULL,
  scheduling_pattern TEXT    NOT NULL,
  job_class          TEXT    NOT NULL,
  enabled            BOOLEAN NOT NULL,
  status             INTEGER NOT NULL,
  execution_attempts INTEGER,
  lock_name          TEXT,
  last_executed      TIMESTAMP,
  next_execution     TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN scheduler.jobs.id IS 'The ID used to uniquely identify the job';

COMMENT ON COLUMN scheduler.jobs.name IS 'The name of the job';

COMMENT ON COLUMN scheduler.jobs.scheduling_pattern IS 'The cron-style scheduling pattern for the job';

COMMENT ON COLUMN scheduler.jobs.job_class IS 'The fully qualified name of the Java class that implements the job';

COMMENT ON COLUMN scheduler.jobs.enabled IS 'Is the job enabled for execution';

COMMENT ON COLUMN scheduler.jobs.status IS 'The status of the job';

COMMENT ON COLUMN scheduler.jobs.execution_attempts IS 'The number of times the current execution of the job has been attempted';

COMMENT ON COLUMN scheduler.jobs.lock_name IS 'The name of the entity that has locked the job for execution';

COMMENT ON COLUMN scheduler.jobs.last_executed IS 'The date and time the job was last executed';

COMMENT ON COLUMN scheduler.jobs.next_execution IS 'The date and time created the job will next be executed';


CREATE TABLE scheduler.job_parameters (
  job_id UUID NOT NULL,
  name   TEXT NOT NULL,
  value  TEXT NOT NULL,

  PRIMARY KEY (job_id, name),
  CONSTRAINT job_parameters_job_fk FOREIGN KEY (job_id) REFERENCES scheduler.jobs(id) ON DELETE CASCADE
);

CREATE INDEX job_parameters_job_id_ix ON scheduler.job_parameters(job_id);

CREATE INDEX job_parameters_name_ix ON scheduler.job_parameters(name);

COMMENT ON COLUMN scheduler.job_parameters.job_id IS 'The ID used to uniquely identify the job';

COMMENT ON COLUMN scheduler.job_parameters.name IS 'The name of the job parameter';

COMMENT ON COLUMN scheduler.job_parameters.value IS 'The value of the job parameter';


CREATE TABLE security.organizations (
  id     UUID    NOT NULL,
  name   TEXT    NOT NULL,
  status INTEGER NOT NULL,

  PRIMARY KEY (id)
);

CREATE INDEX organizations_name_ix ON security.organizations(name);

COMMENT ON COLUMN security.organizations.id IS 'The ID used to uniquely identify the organization';

COMMENT ON COLUMN security.organizations.name IS 'The name of the organization';

COMMENT ON COLUMN security.organizations.status IS 'The status for the organization';


CREATE TABLE security.user_directory_types (
  code                 TEXT NOT NULL,
  name                 TEXT NOT NULL,
  user_directory_class TEXT NOT NULL,

  PRIMARY KEY (code)
);

COMMENT ON COLUMN security.user_directory_types.code IS 'The code used to uniquely identify the user directory type';

COMMENT ON COLUMN security.user_directory_types.name IS 'The name of the user directory type';

COMMENT ON COLUMN security.user_directory_types.user_directory_class IS 'The fully qualified name of the Java class that implements the user directory type';


CREATE TABLE security.user_directories (
  id            UUID NOT NULL,
  type          TEXT NOT NULL,
  name          TEXT NOT NULL,
  configuration TEXT NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT user_directories_user_directory_type_fk FOREIGN KEY (type) REFERENCES security.user_directory_types(code) ON DELETE CASCADE
);

CREATE INDEX user_directories_name_ix ON security.user_directories(name);

COMMENT ON COLUMN security.user_directories.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN security.user_directories.type IS 'The code used to uniquely identify the user directory type';

COMMENT ON COLUMN security.user_directories.name IS 'The name of the user directory';

COMMENT ON COLUMN security.user_directories.configuration IS 'The XML configuration data for the user directory';


CREATE TABLE security.user_directory_to_organization_map (
  user_directory_id UUID NOT NULL,
  organization_id   UUID NOT NULL,

  PRIMARY KEY (user_directory_id, organization_id),
  CONSTRAINT user_directory_to_organization_map_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories(id) ON DELETE CASCADE,
  CONSTRAINT user_directory_to_organization_map_organization_fk FOREIGN KEY (organization_id) REFERENCES security.organizations(id) ON DELETE CASCADE
);

CREATE INDEX user_directory_to_organization_map_user_directory_id_ix ON security.user_directory_to_organization_map(user_directory_id);

CREATE INDEX user_directory_to_organization_map_organization_id_ix ON security.user_directory_to_organization_map(organization_id);

COMMENT ON COLUMN security.user_directory_to_organization_map.user_directory_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory';

COMMENT ON COLUMN security.user_directory_to_organization_map.organization_id IS 'The ID used to uniquely identify the organization';


CREATE TABLE security.users (
  id                UUID    NOT NULL,
  user_directory_id UUID    NOT NULL,
  username          TEXT    NOT NULL,
  status            INTEGER NOT NULL,
  first_name        TEXT    NOT NULL DEFAULT '',
  last_name         TEXT    NOT NULL DEFAULT '',
  phone             TEXT    NOT NULL DEFAULT '',
  mobile            TEXT    NOT NULL DEFAULT '',
  email             TEXT    NOT NULL DEFAULT '',
  password          TEXT    NOT NULL DEFAULT '',
  password_attempts INTEGER NOT NULL DEFAULT 0,
  password_expiry   TIMESTAMP NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT users_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories(id) ON DELETE CASCADE
);

CREATE INDEX users_user_directory_id_ix ON security.users(user_directory_id);

CREATE UNIQUE INDEX users_username_ix ON security.users(username);

COMMENT ON COLUMN security.users.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user';

COMMENT ON COLUMN security.users.user_directory_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the user is associated with';

COMMENT ON COLUMN security.users.username IS 'The username for the user';

COMMENT ON COLUMN security.users.status IS 'The status for the user';

COMMENT ON COLUMN security.users.first_name IS 'The first name for the user';

COMMENT ON COLUMN security.users.last_name IS 'The last name for the user';

COMMENT ON COLUMN security.users.phone IS 'The phone number for the user';

COMMENT ON COLUMN security.users.mobile IS 'The mobile number for the user';

COMMENT ON COLUMN security.users.email IS 'The e-mail address for the user';

COMMENT ON COLUMN security.users.password IS 'The password for the user';

COMMENT ON COLUMN security.users.password_attempts IS 'The number of failed attempts to authenticate the user';

COMMENT ON COLUMN security.users.password_expiry IS 'The date and time that the user''s password expires';


CREATE TABLE security.users_password_history (
  id               UUID      NOT NULL,
  user_id UUID      NOT NULL,
  changed          TIMESTAMP NOT NULL,
  password         TEXT,

  PRIMARY KEY (id),
  CONSTRAINT users_password_history_user_id_fk FOREIGN KEY (user_id) REFERENCES security.users(id) ON DELETE CASCADE
);

CREATE INDEX users_password_history_user_id_ix ON security.users_password_history(user_id);

CREATE INDEX users_password_history_changed_ix ON security.users_password_history(changed);

COMMENT ON COLUMN security.users_password_history.id IS 'The ID used to uniquely identify the password history entry';

COMMENT ON COLUMN security.users_password_history.user_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user';

COMMENT ON COLUMN security.users_password_history.changed IS 'When the password change took place for the user';

COMMENT ON COLUMN security.users_password_history.password IS 'The password for the user';


CREATE TABLE security.groups (
  id                UUID NOT NULL,
  user_directory_id UUID NOT NULL,
  groupname         TEXT NOT NULL,
  description       TEXT,

  PRIMARY KEY (id),
  CONSTRAINT groups_user_directory_fk FOREIGN KEY (user_directory_id) REFERENCES security.user_directories(id) ON DELETE CASCADE
);

CREATE INDEX groups_user_directory_id_ix ON security.groups(user_directory_id);

CREATE INDEX groups_groupname_ix ON security.groups(groupname);

COMMENT ON COLUMN security.groups.id IS 'The ID used to uniquely identify the group';

COMMENT ON COLUMN security.groups.user_directory_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user directory the group is associated with';

COMMENT ON COLUMN security.groups.groupname IS 'The group name for the group';

COMMENT ON COLUMN security.groups.description IS 'A description for the group';


CREATE TABLE security.user_to_group_map (
  user_id  UUID NOT NULL,
  group_id UUID NOT NULL,

  PRIMARY KEY (user_id, group_id),
  CONSTRAINT user_to_group_map_user_fk FOREIGN KEY (user_id) REFERENCES security.users(id) ON DELETE CASCADE,
  CONSTRAINT user_to_group_map_group_fk FOREIGN KEY (group_id) REFERENCES security.groups(id) ON DELETE CASCADE
);

CREATE INDEX user_to_group_map_user_id_ix ON security.user_to_group_map(user_id);

CREATE INDEX user_to_group_map_group_id_ix ON security.user_to_group_map(group_id);

COMMENT ON COLUMN security.user_to_group_map.user_id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the user';

COMMENT ON COLUMN security.user_to_group_map.group_id IS 'The ID used to uniquely identify the group';


CREATE TABLE security.functions (
  code        TEXT NOT NULL,
  name        TEXT NOT NULL,
  description TEXT,

  PRIMARY KEY (code)
);

COMMENT ON COLUMN security.functions.code IS 'The code used to uniquely identify the function';

COMMENT ON COLUMN security.functions.name IS 'The name of the function';

COMMENT ON COLUMN security.functions.description IS 'A description for the function';


CREATE TABLE security.roles (
  code        TEXT NOT NULL,
  name        TEXT NOT NULL,
  description TEXT,

  PRIMARY KEY (code)
);

COMMENT ON COLUMN security.roles.code IS 'The code used to uniquely identify the role';

COMMENT ON COLUMN security.roles.name IS 'The name of the role';

COMMENT ON COLUMN security.roles.description IS 'The description for the role';


CREATE TABLE security.function_to_role_map (
  function_code TEXT NOT NULL,
  role_code     TEXT NOT NULL,

  PRIMARY KEY (function_code, role_code),
  CONSTRAINT function_to_role_map_function_fk FOREIGN KEY (function_code) REFERENCES security.functions(code) ON DELETE CASCADE,
  CONSTRAINT function_to_role_map_role_fk FOREIGN KEY (role_code) REFERENCES security.roles(code) ON DELETE CASCADE
);

CREATE INDEX function_to_role_map_function_code_ix ON security.function_to_role_map(function_code);

CREATE INDEX function_to_role_map_role_code_ix ON security.function_to_role_map(role_code);

COMMENT ON COLUMN security.function_to_role_map.function_code IS 'The code used to uniquely identify the function';

COMMENT ON COLUMN security.function_to_role_map.role_code IS 'The code used to uniquely identify the role';


CREATE TABLE security.role_to_group_map (
  role_code TEXT NOT NULL,
  group_id  UUID NOT NULL,

  PRIMARY KEY (role_code, group_id),
  CONSTRAINT role_to_group_map_role_fk FOREIGN KEY (role_code) REFERENCES security.roles(code) ON DELETE CASCADE,
  CONSTRAINT role_to_group_map_group_fk FOREIGN KEY (group_id) REFERENCES security.groups(id) ON DELETE CASCADE
);

CREATE INDEX role_to_group_map_role_code_ix ON security.role_to_group_map(role_code);

CREATE INDEX role_to_group_map_group_id_ix ON security.role_to_group_map(group_id);

COMMENT ON COLUMN security.role_to_group_map.role_code IS 'The code used to uniquely identify the role';

COMMENT ON COLUMN security.role_to_group_map.group_id IS 'The ID used to uniquely identify the group';


CREATE TABLE service_registry.service_registry (
  name                 TEXT    NOT NULL,
  security_type        INTEGER NOT NULL,
  supports_compression CHAR(1) NOT NULL,
  endpoint             TEXT    NOT NULL,
  service_class        TEXT    NOT NULL,
  wsdl_location        TEXT    NOT NULL,
  username             TEXT,
  password             TEXT,

  PRIMARY KEY (name)
);

COMMENT ON COLUMN service_registry.service_registry.name IS 'The name used to uniquely identify the web service';

COMMENT ON COLUMN service_registry.service_registry.security_type IS 'The type of security model implemented by the web service i.e. 0 = None, 1 = Mutual SSL, etc';

COMMENT ON COLUMN service_registry.service_registry.supports_compression IS 'Does the web service support compression';

COMMENT ON COLUMN service_registry.service_registry.endpoint IS 'The endpoint for the web service';

COMMENT ON COLUMN service_registry.service_registry.service_class IS 'The fully qualified name of the Java service class';

COMMENT ON COLUMN service_registry.service_registry.wsdl_location IS 'The location of the WSDL defining the web service on the classpath';

COMMENT ON COLUMN service_registry.service_registry.username IS 'The username to use created accessing a web service with username-password security enabled';

COMMENT ON COLUMN service_registry.service_registry.password IS 'The password to use created accessing a web service with username-password security enabled';


CREATE TABLE sms.sms (
  id             BIGINT  NOT NULL,
  mobile_number  TEXT    NOT NULL,
  message        TEXT    NOT NULL,
  status         INTEGER NOT NULL,
  send_attempts  INTEGER,
  lock_name      TEXT,
  last_processed TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE INDEX sms_mobile_number_ix ON sms.sms(mobile_number);

COMMENT ON COLUMN sms.sms.id IS 'The ID used to uniquely identify the SMS';

COMMENT ON COLUMN sms.sms.mobile_number IS 'The mobile number to send the SMS to';

COMMENT ON COLUMN sms.sms.message IS 'The message to send';

COMMENT ON COLUMN sms.sms.status IS 'The status of the SMS';

COMMENT ON COLUMN sms.sms.send_attempts IS 'The number of times that the sending of the SMS was attempted';

COMMENT ON COLUMN sms.sms.lock_name IS 'The name of the entity that has locked the SMS for sending';

COMMENT ON COLUMN sms.sms.last_processed IS 'The date and time the last attempt was made to send the SMS';


CREATE TABLE error.error_reports (
  id                  UUID      NOT NULL,
  application_id      TEXT      NOT NULL,
  application_version TEXT      NOT NULL,
  description         TEXT      NOT NULL,
  detail              TEXT      NOT NULL,
  created             TIMESTAMP NOT NULL,
  who                 TEXT,
  device_id           UUID,
  feedback            TEXT,
  data                TEXT,

  PRIMARY KEY (id)
);

CREATE INDEX error_reports_application_id_ix ON error.error_reports(application_id);

CREATE INDEX error_reports_created_ix ON error.error_reports(created);

CREATE INDEX error_reports_who_ix ON error.error_reports(who);

COMMENT ON COLUMN error.error_reports.id IS 'The Universally Unique Identifier (UUID) used to uniquely identify the error report';

COMMENT ON COLUMN error.error_reports.application_id IS 'The ID used to uniquely identify the application that generated the error report';

COMMENT ON COLUMN error.error_reports.application_version IS 'The version of the application that generated the error report';

COMMENT ON COLUMN error.error_reports.description IS 'The description of the error';

COMMENT ON COLUMN error.error_reports.detail IS 'The error detail e.g. a stack trace';

COMMENT ON COLUMN error.error_reports.created IS 'The date and time the error report was created';

COMMENT ON COLUMN error.error_reports.who IS 'The optional username identifying the user associated with the error report';

COMMENT ON COLUMN error.error_reports.device_id IS 'The optional Universally Unique Identifier (UUID) used to uniquely identify the device the error report originated from';

COMMENT ON COLUMN error.error_reports.feedback IS 'The optional feedback provided by the user for the error';

COMMENT ON COLUMN error.error_reports.data IS 'The optional base-64 encoded data associated with the error report';


CREATE TABLE test.test_data (
  id    TEXT NOT NULL,
  name  TEXT NOT NULL,
  value TEXT NOT NULL,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN test.test_data.id IS 'The ID used to uniquely identify the test data';

COMMENT ON COLUMN test.test_data.name IS 'The name for the test data';

COMMENT ON COLUMN test.test_data.value IS 'The value for the test data';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO security.organizations (id, name, status)
  VALUES ('c1685b92-9fe5-453a-995b-89d8c0f29cb5', 'Administration', 1);

INSERT INTO security.user_directory_types (code, name, user_directory_class)
  VALUES ('InternalUserDirectory', 'Internal User Directory', 'digital.inception.security.InternalUserDirectory');
INSERT INTO security.user_directory_types (code, name, user_directory_class)
  VALUES ('LDAPUserDirectory', 'LDAP User Directory', 'digital.inception.security.LDAPUserDirectory');

INSERT INTO security.user_directories (id, type, name, configuration)
  VALUES ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'InternalUserDirectory', 'Administration Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>');

INSERT INTO security.user_directory_to_organization_map (user_directory_id, organization_id)
  VALUES ('4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'c1685b92-9fe5-453a-995b-89d8c0f29cb5');

INSERT INTO security.users (id, user_directory_id, username, status, first_name, last_name, phone, mobile, email, password, password_attempts)
  VALUES ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'administrator', 1, '', '', '', '', '', 'GVE/3J2k+3KkoF62aRdUjTyQ/5TVQZ4fI2PuqJ3+4d0=', 0);

INSERT INTO security.groups (id, user_directory_id, groupname, description)
  VALUES ('a9e01fa2-f017-46e2-8187-424bf50a4f33', '4ef18395-423a-4df6-b7d7-6bcdd85956e4', 'Administrators', 'Administrators');

INSERT INTO security.user_to_group_map (user_id, group_id)
  VALUES ('b2bbf431-4af8-4104-b96c-d33b5f66d1e4', 'a9e01fa2-f017-46e2-8187-424bf50a4f33');

INSERT INTO security.functions (code, name, description)
  VALUES ('Application.Dashboard', 'Dashboard', 'Dashboard');
INSERT INTO security.functions (code, name, description)
  VALUES ('Application.SecureHome', 'Secure Home', 'Secure Home');
INSERT INTO security.functions (code, name, description)
  VALUES ('Codes.CodeAdministration', 'Code Administration', 'Code Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Configuration.ConfigurationAdministration', 'Configuration Administration', 'Configuration Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Error.ErrorReportAdministration', 'Error Report Administration', 'Error Report Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Process.ProcessDefinitionAdministration', 'Process Definition Administration', 'Process Definition Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Process.ViewProcess', 'View Process', 'View Process');
INSERT INTO security.functions (code, name, description)
  VALUES ('Reporting.ReportDefinitionAdministration', 'Report Definition Administration', 'Report Definition Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Reporting.ViewReport', 'View Report', 'View Report');
INSERT INTO security.functions (code, name, description)
  VALUES ('Scheduler.SchedulerAdministration', 'Scheduler Administration', 'Scheduler Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.GroupAdministration', 'Group Administration', 'Group Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.OrganizationAdministration', 'Organization Administration', 'Organization Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.ResetUserPassword', 'Reset User Password', 'Reset User Password');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.SecurityAdministration', 'Security Administration', 'Security Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.UserAdministration', 'User Administration', 'User Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.UserDirectoryAdministration', 'User Directory Administration', 'User Directory Administration');
INSERT INTO security.functions (code, name, description)
  VALUES ('Security.UserGroups', 'User Groups', 'User Groups');

INSERT INTO security.roles (code, name, description)
  VALUES ('Administrator', 'Administrator', 'Administrator');
INSERT INTO security.roles (code, name, description)
  VALUES ('OrganizationAdministrator', 'Organization Administrator', 'Organization Administrator');
INSERT INTO security.roles (code, name, description)
  VALUES ('PasswordResetter', 'Password Resetter', 'Password Resetter');

INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Application.SecureHome', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Application.Dashboard', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Reporting.ViewReport', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.ResetUserPassword', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.UserAdministration', 'OrganizationAdministrator');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.UserGroups', 'OrganizationAdministrator');

INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Application.SecureHome', 'PasswordResetter');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Application.Dashboard', 'PasswordResetter');
INSERT INTO security.function_to_role_map (function_code, role_code)
  VALUES ('Security.ResetUserPassword', 'PasswordResetter');

INSERT INTO security.role_to_group_map (role_code, group_id)
  VALUES ('Administrator', 'a9e01fa2-f017-46e2-8187-424bf50a4f33'); -- Assign the Administrator role to the Administrators group

INSERT INTO messaging.message_types (id, name)
  VALUES ('d21fb54e-5c5b-49e8-881f-ce00c6ced1a3', 'AuthenticateRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('82223035-1726-407f-8703-3977708e792c', 'AuthenticateResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('cc005e6a-b01b-48eb-98a0-026297be69f3', 'CheckUserExistsRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('a38bd55e-3470-46f1-a96a-a6b08a9adc63', 'CheckUserExistsResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('94d60eb6-a062-492d-b5e7-9fb1f05cf088', 'GetCodeCategoryRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('0336b544-91e5-4eb9-81db-3dd94e116c92', 'GetCodeCategoryResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('3500a28a-6a2c-482b-b81f-a849c9c3ef79', 'GetCodeCategoryWithParametersRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('12757310-9eee-4a3a-970c-9b4ee0e1108e', 'GetCodeCategoryWithParametersResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('a589dc87-2328-4a9b-bdb6-970e55ca2323', 'TestRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('a3bad7ba-f9d4-4403-b54a-cb1f335ebbad', 'TestResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('e9918051-8ebc-48f1-bad7-13c59b550e1a', 'AnotherTestRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('a714a9c6-2914-4498-ab59-64be9991bf37', 'AnotherTestResponse');
INSERT INTO messaging.message_types (id, name)
  VALUES ('ff638c33-b4f1-4e79-804c-9560da2543d6', 'SubmitErrorReportRequest');
INSERT INTO messaging.message_types (id, name)
  VALUES ('8be50cfa-2fb1-4634-9bfa-d01e77eaf766', 'SubmitErrorReportResponse');

INSERT INTO messaging.message_statuses (code, name)
  VALUES (0, 'Initialised');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (1, 'QueuedForSending');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (2, 'QueuedForProcessing');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (3, 'Aborted');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (4, 'Failed');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (5, 'Processing');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (6, 'Sending');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (7, 'QueuedForDownload');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (8, 'Downloading');
INSERT INTO messaging.message_statuses (code, name)
  VALUES (10, 'Processed');

INSERT INTO test.test_data (id, name, value)
  VALUES (1, 'Sample Name 1', 'Sample Value 1');
INSERT INTO test.test_data (id, name, value)
  VALUES (2, 'Sample Name 2', 'Sample Value 2');
INSERT INTO test.test_data (id, name, value)
  VALUES (3, 'Sample Name 3', 'Sample Value 3');
INSERT INTO test.test_data (id, name, value)
  VALUES (4, 'Sample Name 4', 'Sample Value 4');
INSERT INTO test.test_data (id, name, value)
  VALUES (5, 'Sample Name 5', 'Sample Value 5');
INSERT INTO test.test_data (id, name, value)
  VALUES (6, 'Sample Name 6', 'Sample Value 6');
INSERT INTO test.test_data (id, name, value)
  VALUES (7, 'Sample Name 7', 'Sample Value 7');
INSERT INTO test.test_data (id, name, value)
  VALUES (8, 'Sample Name 8', 'Sample Value 8');
INSERT INTO test.test_data (id, name, value)
  VALUES (9, 'Sample Name 9', 'Sample Value 9');

-- -------------------------------------------------------------------------------------------------
-- SET PERMISSIONS
-- -------------------------------------------------------------------------------------------------
GRANT ALL ON SCHEMA codes TO dbuser;
GRANT ALL ON SCHEMA configuration TO dbuser;
GRANT ALL ON SCHEMA idgenerator TO dbuser;
GRANT ALL ON SCHEMA messaging TO dbuser;
GRANT ALL ON SCHEMA reporting TO dbuser;
GRANT ALL ON SCHEMA scheduler TO dbuser;
GRANT ALL ON SCHEMA security TO dbuser;
GRANT ALL ON SCHEMA service_registry TO dbuser;
GRANT ALL ON SCHEMA sms TO dbuser;
GRANT ALL ON SCHEMA error TO dbuser;
GRANT ALL ON SCHEMA test TO dbuser;

GRANT ALL ON table codes.code_categories TO dbuser;
GRANT ALL ON table codes.codes TO dbuser;
GRANT ALL ON table configuration.configuration TO dbuser;
GRANT ALL ON table idgenerator.idgenerator TO dbuser;
GRANT ALL ON table messaging.message_types TO dbuser;
GRANT ALL ON table messaging.message_statuses TO dbuser;
GRANT ALL ON table messaging.messages TO dbuser;
GRANT ALL ON table messaging.message_parts TO dbuser;
GRANT ALL ON table messaging.archived_messages TO dbuser;
GRANT ALL ON table reporting.report_definitions TO dbuser;
GRANT ALL ON table scheduler.jobs TO dbuser;
GRANT ALL ON table scheduler.job_parameters TO dbuser;
GRANT ALL ON table security.organizations TO dbuser;
GRANT ALL ON table security.user_directory_types TO dbuser;
GRANT ALL ON table security.user_directories TO dbuser;
GRANT ALL ON table security.user_directory_to_organization_map TO dbuser;
GRANT ALL ON table security.users TO dbuser;
GRANT ALL ON table security.users_password_history TO dbuser;
GRANT ALL ON table security.groups TO dbuser;
GRANT ALL ON table security.user_to_group_map TO dbuser;
GRANT ALL ON table security.functions TO dbuser;
GRANT ALL ON table security.roles TO dbuser;
GRANT ALL ON table security.function_to_role_map TO dbuser;
GRANT ALL ON table security.role_to_group_map TO dbuser;
GRANT ALL ON table service_registry.service_registry TO dbuser;
GRANT ALL ON table sms.sms TO dbuser;
GRANT ALL ON table error.error_reports TO dbuser;
GRANT ALL ON table test.test_data TO dbuser;
