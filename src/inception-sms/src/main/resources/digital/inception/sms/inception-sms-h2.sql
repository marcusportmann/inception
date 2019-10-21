-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA sms;

-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE sms.sms (
  id             UUID          NOT NULL,
  mobile_number  VARCHAR(100)  NOT NULL,
  message        VARCHAR(1000) NOT NULL,
  status         INTEGER       NOT NULL,
  send_attempts  INTEGER,
  lock_name      VARCHAR(100),
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

-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
