-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA messaging;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE messaging.message_types (
  code VARCHAR(50)  NOT NULL,
  name VARCHAR(100) NOT NULL,

  PRIMARY KEY (code)
);

COMMENT ON COLUMN messaging.message_types.code IS 'The code for the message type';

COMMENT ON COLUMN messaging.message_types.name IS 'The name of the message type';


CREATE TABLE messaging.message_statuses (
  code INTEGER       NOT NULL,
  name VARCHAR(100) NOT NULL,

  PRIMARY KEY (code)
);

COMMENT ON COLUMN messaging.message_statuses.code IS 'The code for the message status';

COMMENT ON COLUMN messaging.message_statuses.name IS 'The name of the message status';


CREATE TABLE messaging.messages (
  id                UUID          NOT NULL,
  type              VARCHAR(50)   NOT NULL,
  username          VARCHAR(100)  NOT NULL,
  device_id         UUID          NOT NULL,
  correlation_id    UUID,
  priority          INTEGER       NOT NULL,
  status            INTEGER       NOT NULL,
  created           TIMESTAMP     NOT NULL,
  data_hash         VARCHAR(100),
  encryption_iv     VARCHAR(100),
  send_attempts     INTEGER,
  process_attempts  INTEGER,
  download_attempts INTEGER,
  lock_name         VARCHAR(100),
  last_processed    TIMESTAMP,
  data              BLOB,

  PRIMARY KEY (id),
  CONSTRAINT messages_message_type_fk FOREIGN KEY (type) REFERENCES messaging.message_types(code),
  CONSTRAINT messages_message_status_fk FOREIGN KEY (status) REFERENCES messaging.message_statuses(code)
);

CREATE INDEX messages_type_ix ON messaging.messages(type);

CREATE INDEX messages_username_ix ON messaging.messages(username);

CREATE INDEX messages_device_id_ix ON messaging.messages(device_id);

CREATE INDEX messages_priority_ix ON messaging.messages(priority);

CREATE INDEX messages_status_ix ON messaging.messages(status);

CREATE INDEX messages_lock_name_ix ON messaging.messages(lock_name);

COMMENT ON COLUMN messaging.messages.id IS 'The Universally Unique Identifier (UUID) for the message';

COMMENT ON COLUMN messaging.messages.type IS 'The code for the message type';

COMMENT ON COLUMN messaging.messages.username IS 'The username for the user associated with the message';

COMMENT ON COLUMN messaging.messages.device_id IS 'The Universally Unique Identifier (UUID) for the device associated with the message';

COMMENT ON COLUMN messaging.messages.correlation_id IS 'The optional Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN messaging.messages.priority IS 'The message priority';

COMMENT ON COLUMN messaging.messages.status IS 'The message status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN messaging.messages.created IS 'The date and time the message was created';

COMMENT ON COLUMN messaging.messages.data_hash IS 'The hash of the unencrypted data for the message if the message is encrypted';

COMMENT ON COLUMN messaging.messages.encryption_iv IS 'The base-64 encoded initialization vector for the encryption scheme for the message';

COMMENT ON COLUMN messaging.messages.send_attempts IS 'The number of times that the sending of the message was attempted';

COMMENT ON COLUMN messaging.messages.process_attempts IS 'The number of times that the processing of the message was attempted';

COMMENT ON COLUMN messaging.messages.download_attempts IS 'The number of times that an attempt was made to download the message';

COMMENT ON COLUMN messaging.messages.lock_name IS 'The name of the entity that has locked the message for processing';

COMMENT ON COLUMN messaging.messages.last_processed IS 'The date and time the last attempt was made to process the message';

COMMENT ON COLUMN messaging.messages.data IS 'The data for the message';


CREATE TABLE messaging.message_parts (
  id                     UUID          NOT NULL,
  part_no                INTEGER       NOT NULL,
  total_parts            INTEGER       NOT NULL,
  send_attempts          INTEGER,
  download_attempts      INTEGER,
  status                 INTEGER       NOT NULL,
  message_id             UUID          NOT NULL,
  message_type           VARCHAR(50)   NOT NULL,
  message_username       VARCHAR(100)  NOT NULL,
  message_device_id      UUID          NOT NULL,
  message_correlation_id UUID,
  message_priority       INTEGER       NOT NULL,
  message_created        TIMESTAMP     NOT NULL,
  message_data_hash      VARCHAR(100),
  message_encryption_iv  VARCHAR(100),
  message_checksum       VARCHAR(100)  NOT NULL,
  lock_name              VARCHAR(100),
  data                   BLOB,

  PRIMARY KEY (id),
  CONSTRAINT message_parts_message_type_fk FOREIGN KEY (message_type) REFERENCES messaging.message_types(code)
);

CREATE INDEX message_parts_status_ix ON messaging.message_parts(status);

CREATE INDEX message_parts_message_id_ix ON messaging.message_parts(message_id);

CREATE INDEX message_parts_message_type_ix ON messaging.message_parts(message_type);

CREATE INDEX message_parts_message_device_id_ix ON messaging.message_parts(message_device_id);

CREATE INDEX message_parts_lock_name_ix ON messaging.message_parts(lock_name);

COMMENT ON COLUMN messaging.message_parts.id IS 'The Universally Unique Identifier (UUID) for the message part';

COMMENT ON COLUMN messaging.message_parts.part_no IS 'The number of the message part in the set of message parts for the original message';

COMMENT ON COLUMN messaging.message_parts.total_parts IS 'The total number of parts in the set of message parts for the original message';

COMMENT ON COLUMN messaging.message_parts.send_attempts IS 'The number of times that the sending of the message part was attempted';

COMMENT ON COLUMN messaging.message_parts.download_attempts IS 'The number of times that an attempt was made to download the message part';

COMMENT ON COLUMN messaging.message_parts.status IS 'The message part status e.g. Initialised, QueuedForSending, etc';

COMMENT ON COLUMN messaging.message_parts.message_id IS 'The Universally Unique Identifier (UUID) for the original message';

COMMENT ON COLUMN messaging.message_parts.message_type IS 'The code for the message type for the original message';

COMMENT ON COLUMN messaging.message_parts.message_username IS 'The username for the user associated with the original message';

COMMENT ON COLUMN messaging.message_parts.message_device_id IS 'The Universally Unique Identifier (UUID) for the device associated with the original message';

COMMENT ON COLUMN messaging.message_parts.message_correlation_id IS 'The optional Universally Unique Identifier (UUID) used to correlate the original message';

COMMENT ON COLUMN messaging.message_parts.message_priority IS 'The priority for the original message';

COMMENT ON COLUMN messaging.message_parts.message_created IS 'The date and time the original message was created';

COMMENT ON COLUMN messaging.message_parts.message_data_hash IS 'The hash of the unencrypted data for the original message if the message was encrypted';

COMMENT ON COLUMN messaging.message_parts.message_encryption_iv IS 'The base-64 encoded initialization vector for the encryption scheme for the original message';

COMMENT ON COLUMN messaging.message_parts.message_checksum IS 'The checksum for the original message';

COMMENT ON COLUMN messaging.message_parts.lock_name IS 'The name of the entity that has locked the message part for processing';

COMMENT ON COLUMN messaging.message_parts.data IS 'The data for the message part';


CREATE TABLE messaging.archived_messages (
  id             UUID         NOT NULL,
  type           VARCHAR(50)  NOT NULL,
  username       VARCHAR(100) NOT NULL,
  device_id      UUID         NOT NULL,
  correlation_id UUID,
  created        TIMESTAMP    NOT NULL,
  archived       TIMESTAMP    NOT NULL,
  data           BLOB,

  PRIMARY KEY (id),
  CONSTRAINT archived_messages_message_type_fk FOREIGN KEY (type) REFERENCES messaging.message_types(code)
);

CREATE INDEX archived_messages_type_ix ON messaging.archived_messages(type);

CREATE INDEX archived_messages_username_ix ON messaging.archived_messages(username);

CREATE INDEX archived_messages_device_id_ix ON messaging.archived_messages(device_id);

COMMENT ON COLUMN messaging.archived_messages.id IS 'The Universally Unique Identifier (UUID) for the message';

COMMENT ON COLUMN messaging.archived_messages.type IS 'The code for the message type';

COMMENT ON COLUMN messaging.archived_messages.username IS 'The username for the user associated with the message';

COMMENT ON COLUMN messaging.archived_messages.device_id IS 'The Universally Unique Identifier (UUID) for the device associated with the message';

COMMENT ON COLUMN messaging.archived_messages.correlation_id IS 'The optional Universally Unique Identifier (UUID) used to correlate the message';

COMMENT ON COLUMN messaging.archived_messages.created IS 'The date and time the message was created';

COMMENT ON COLUMN messaging.archived_messages.archived IS 'The date and time the message was archived';

COMMENT ON COLUMN messaging.archived_messages.data IS 'The data for the message';


-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO messaging.message_types (code, name)
  VALUES ('AuthenticateRequest', 'Authenticate Request');
INSERT INTO messaging.message_types (code, name)
  VALUES ('AuthenticateResponse', 'Authenticate Response');
INSERT INTO messaging.message_types (code, name)
  VALUES ('CheckUserExistsRequest', 'Check User Exists Request');
INSERT INTO messaging.message_types (code, name)
  VALUES ('CheckUserExistsResponse', 'Check User Exists Response');
INSERT INTO messaging.message_types (code, name)
  VALUES ('GetCodeCategoryRequest', 'Get Code Category Request');
INSERT INTO messaging.message_types (code, name)
  VALUES ('GetCodeCategoryResponse', 'Get Code Category Response');
INSERT INTO messaging.message_types (code, name)
  VALUES ('TestRequest', 'Test Request');
INSERT INTO messaging.message_types (code, name)
  VALUES ('TestResponse', 'Test Response');
INSERT INTO messaging.message_types (code, name)
  VALUES ('AnotherTestRequest', 'Another Test Request');
INSERT INTO messaging.message_types (code, name)
  VALUES ('AnotherTestResponse', 'Another Test Response');
INSERT INTO messaging.message_types (code, name)
  VALUES ('SubmitErrorReportRequest', 'Submit Error Report Request');
INSERT INTO messaging.message_types (code, name)
  VALUES ('SubmitErrorReportResponse', 'Submit Error Report Response');

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
  VALUES (9, 'Processed');

