-- -------------------------------------------------------------------------------------------------
-- CREATE SCHEMAS
-- -------------------------------------------------------------------------------------------------
CREATE SCHEMA demo;


-- -------------------------------------------------------------------------------------------------
-- CREATE TABLES
-- -------------------------------------------------------------------------------------------------
CREATE TABLE demo.data (
  id              BIGINT        NOT NULL,
  integer_value   INTEGER,
  string_value    VARCHAR(4000),
  date_value      DATE,
  timestamp_value TIMESTAMP,

  PRIMARY KEY (id)
);

COMMENT ON COLUMN demo.data.id IS 'The ID for the data';

COMMENT ON COLUMN demo.data.integer_value IS 'The integer value for the data';

COMMENT ON COLUMN demo.data.string_value IS 'The string value for the data';

COMMENT ON COLUMN demo.data.date_value IS 'The date value for the data';

COMMENT ON COLUMN demo.data.timestamp_value IS 'The timestamp value for the data';



CREATE TABLE demo.vehicles (
   created   TIMESTAMP    NOT NULL,
   id        UUID         NOT NULL,
   name      VARCHAR(100) NOT NULL,
   type      VARCHAR(30)  NOT NULL,
   updated   TIMESTAMP,

   PRIMARY KEY (id)
);

CREATE INDEX vehicles_type_ix ON demo.vehicles(type);

COMMENT ON COLUMN demo.vehicles.created IS 'The date and time the vehicle was created';

COMMENT ON COLUMN demo.vehicles.id IS 'The Universally Unique Identifier (UUID) for the vehicle';

COMMENT ON COLUMN demo.vehicles.name IS 'The name of the vehicle';

COMMENT ON COLUMN demo.vehicles.type IS 'The code for the vehicle type';

COMMENT ON COLUMN demo.vehicles.updated IS 'The date and time the vehicle was last updated';


CREATE TABLE demo.cars (
  id              UUID    NOT NULL,
  number_of_doors INTEGER NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT cars_vehicle_fk FOREIGN KEY (id) REFERENCES demo.vehicles(id) ON DELETE CASCADE
);

COMMENT ON COLUMN demo.cars.id IS 'The Universally Unique Identifier (UUID) for the car';

COMMENT ON COLUMN demo.cars.number_of_doors IS 'The number of doors for the car';



CREATE TABLE demo.vehicle_attribute_types (
  code         VARCHAR(30)  NOT NULL,
  name         VARCHAR(50)  NOT NULL,
  description  VARCHAR(200) NOT NULL DEFAULT '',

  PRIMARY KEY (code)
);

COMMENT ON COLUMN demo.vehicle_attribute_types.code IS 'The code for the vehicle attribute type';

COMMENT ON COLUMN demo.vehicle_attribute_types.name IS 'The name of the vehicle attribute type';

COMMENT ON COLUMN demo.vehicle_attribute_types.description IS 'The description for the vehicle attribute type';




CREATE TABLE demo.vehicle_attributes (
  created    TIMESTAMP    NOT NULL,
  vehicle_id UUID         NOT NULL,
  type       VARCHAR(30)  NOT NULL,
  updated    TIMESTAMP,
  value      VARCHAR(100) NOT NULL,

  PRIMARY KEY (vehicle_id, type),
  CONSTRAINT vehicle_attributes_vehicle_fk FOREIGN KEY (vehicle_id) REFERENCES demo.vehicles(id) ON DELETE CASCADE,
  CONSTRAINT vehicle_attributes_vehicle_attribute_type_fk FOREIGN KEY (type) REFERENCES demo.vehicle_attribute_types(code) ON DELETE CASCADE
);

COMMENT ON COLUMN demo.vehicle_attributes.created IS 'The date and time the vehicle attribute was created';

COMMENT ON COLUMN demo.vehicle_attributes.vehicle_id IS 'The Universally Unique Identifier (UUID) for the vehicle the vehicle attribute is associated with';

COMMENT ON COLUMN demo.vehicle_attributes.type IS 'The code for the vehicle attribute type';

COMMENT ON COLUMN demo.vehicle_attributes.updated IS 'The date and time the vehicle attribute was updated';

COMMENT ON COLUMN demo.vehicle_attributes.value IS 'The value for the vehicle attribute';



-- -------------------------------------------------------------------------------------------------
-- POPULATE TABLES
-- -------------------------------------------------------------------------------------------------
INSERT INTO security.tenants (id, name, status, created)
  VALUES ('204e5b8f-48e7-4354-bd15-753e6543b64d', 'Demo', 1, NOW());

INSERT INTO security.user_directories (id, type, name, configuration, created)
  VALUES ('34ccdbc9-4a01-46f5-a284-ba13e095675c', 'InternalUserDirectory', 'Demo Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>', NOW());

-- INSERT INTO security.user_directories (id, type, name, configuration)
--  VALUES ('595d13ac-22d6-4ce2-b898-3add4658a748', 'LDAPUserDirectory', 'Demo LDAP User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>Host</name><value>localhost</value></parameter><parameter><name>Port</name><value>389</value></parameter><parameter><name>UseSSL</name><value>false</value></parameter><parameter><name>BindDN</name><value>cn=root,o=demo</value></parameter><parameter><name>BindPassword</name><value>Password1</value></parameter><parameter><name>BaseDN</name><value>ou=demo,ou=applications,o=demo</value></parameter><parameter><name>UserBaseDN</name><value>ou=users,ou=demo,ou=applications,o=demo</value></parameter><parameter><name>GroupBaseDN</name><value>ou=groups,ou=demo,ou=applications,o=demo</value></parameter><parameter><name>UserObjectClass</name><value>inetOrgPerson</value></parameter><parameter><name>UserUsernameAttribute</name><value>uid</value></parameter><parameter><name>UserNameAttribute</name><value>cn</value></parameter><parameter><name>UserPreferredNameAttribute</name><value>nickName</value></parameter><parameter><name>UserPhoneNumberAttribute</name><value>telephoneNumber</value></parameter><parameter><name>UserFaxNumberAttribute</name><value>facsimileTelephoneNumber</value></parameter><parameter><name>UserMobileNumberAttribute</name><value>mobile</value></parameter><parameter><name>UserEmailAttribute</name><value>mail</value></parameter><parameter><name>UserDescriptionAttribute</name><value>cn</value></parameter><parameter><name>GroupObjectClass</name><value>groupOfNames</value></parameter><parameter><name>GroupNameAttribute</name><value>cn</value></parameter><parameter><name>GroupMemberAttribute</name><value>member</value></parameter><parameter><name>GroupDescriptionAttribute</name><value>description</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter><parameter><name>MaxFilteredGroups</name><value>100</value></parameter></userDirectory>');

INSERT INTO security.user_directory_to_tenant_map (user_directory_id, tenant_id)
  VALUES ('34ccdbc9-4a01-46f5-a284-ba13e095675c', '204e5b8f-48e7-4354-bd15-753e6543b64d');
-- INSERT INTO security.user_directory_to_tenant_map (USER_DIRECTORY_ID, TENANT_ID)
--  VALUES ('595d13ac-22d6-4ce2-b898-3add4658a748', '204e5b8f-48e7-4354-bd15-753e6543b64d');

INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'joe', 1, 'Joe Bloggs', 'Joe', '', '', 'joe@demo.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'sally', 1, 'Sally Smith', 'Sally', '', '', 'sally@demo.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('01166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Bob.Smith@peoples.com'), 1, 'Bob Smith', 'Bob', '', '', 'Bob.Smith@peoples.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('02166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Jamiya.Stuart@advantageepic.com'), 1, 'Jamiya Stuart', 'Jamiya', '', '', 'Jamiya.Stuart@advantageepic.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('03166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Melany.Reed@bulkmailsweetpotato.com'), 1, 'Melany Reed', 'Melany', '', '', 'Melany.Reed@bulkmailsweetpotato.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('04166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Brendan.Best@chromaticvalley.com'), 1, 'Brendan Best', 'Brendan', '', '', 'Brendan.Best@chromaticvalley.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('05166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Reuben.Ali@cove.com'), 1, 'Reuben Ali', 'Reuben', '', '', 'Reuben.Ali@cove.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('06166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Jairo.Hull@turbineluster.com'), 1, 'Jairo Hull', 'Jairo', '', '', 'Jairo.Hull@turbineluster.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('07166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Nickolas.Sampson@leafblower.com'), 1, 'Nickolas Sampson', 'Nickolas', '', '', 'Nickolas.Sampson@leafblower.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('08166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Amiah.Sanford@dating.com'), 1, 'Amiah Sanford', 'Amiah', '', '', 'Amiah.Sanford@ dating.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('09166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Janae.Reeves@jollyfracture.com'), 1, 'Janae Reeves', 'Janae', '', '', 'Janae.Reeves@jollyfracture.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('10166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Sara.Meza@bestsellerprices.com'), 1, 'Sara Meza', 'Sara', '', '', 'Sara.Meza@bestsellerprices.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('11166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Gauge.Barrera@perspectivedealer.com'), 1, 'Gauge Barrera', 'Gauge', '', '', 'Gauge.Barrera@perspectivedealer.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('12166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Nickolas.Stone@healing.com'), 1, 'Nickolas Stone', 'Nickolas', '', '', 'Nickolas.Stone@healing.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('13166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Nayeli.Mcknight@retro.com'), 1, 'Nayeli Mcknight', 'Nayeli', '', '', 'Nayeli.Mcknight@retro.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('14166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Cristofer.Singleton@glowing.com'), 1, 'Cristofer Singleton', 'Cristofer', '', '', 'Cristofer.Singleton@glowing.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('15166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Gunnar.Manning@assets.com'), 1, 'Gunnar Manning', 'Gunnar', '', '', 'Gunnar.Manning@assets.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
 VALUES ('16166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Adalyn.Doyle@tadpole.com'), 1, 'Adalyn Doyle', 'Adalyn', '', '', 'Adalyn.Doyle@tadpole.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('17166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Oliver.Hoffman@beam.com'), 1, 'Oliver Hoffman', 'Oliver', '', '', 'Oliver.Hoffman@beam.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('18166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Ayana.Mccarty@cucumber.com'), 1, 'Ayana Mccarty', 'Ayana', '', '', 'Ayana.Mccarty@cucumber.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('19166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Nigel.Cohen@recordplayer.com'), 1, 'Nigel Cohen', 'Nigel', '', '', 'Nigel.Cohen@recordplayer.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('20166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Andrew.Wheeler@greatplainsfinances.com'), 1, 'Andrew Wheeler', 'Andrew', '', '', 'Andrew.Wheeler@greatplainsfinances.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('21166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Amelia.May@opera.com'), 1, 'Amelia May', 'Amelia', '', '', 'Amelia.May@opera.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('22166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', LOWER('Amir.Wells@refrigeratorsignal.com'), 1, 'Amir Wells', 'Amir', '', '', 'Amir.Wells@refrigeratorsignal.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());

INSERT INTO security.groups (id, user_directory_id, name, description, created)
  VALUES ('956c5550-cd3d-42de-8660-7749e1b4df52', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'Tenant Administrators', 'Tenant Administrators', NOW());
INSERT INTO security.groups (id, user_directory_id, name, description, created)
  VALUES ('146c5550-cd3d-42de-8660-7749e1b4df52', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'Password Resetters', 'Password Resetters', NOW());

INSERT INTO security.user_to_group_map (user_id, group_id)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', '956c5550-cd3d-42de-8660-7749e1b4df52');
INSERT INTO security.user_to_group_map (user_id, group_id)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', '146c5550-cd3d-42de-8660-7749e1b4df52');

INSERT INTO security.role_to_group_map (role_code, group_id)
  VALUES ('TenantAdministrator', '956c5550-cd3d-42de-8660-7749e1b4df52');
INSERT INTO security.role_to_group_map (role_code, group_id)
  VALUES ('PasswordResetter', '146c5550-cd3d-42de-8660-7749e1b4df52');

INSERT INTO demo.data (id, string_value)
  VALUES (1, 'Demo Value 1');
INSERT INTO demo.data (id, string_value)
  VALUES (2, 'Demo Value 2');
INSERT INTO demo.data (id, string_value)
  VALUES (3, 'Demo Value 3');
INSERT INTO demo.data (id, string_value)
  VALUES (4, 'Demo Value 4');
INSERT INTO demo.data (id, string_value)
  VALUES (5, 'Demo Value 5');
INSERT INTO demo.data (id, string_value)
  VALUES (6, 'Demo Value 6');
INSERT INTO demo.data (id, string_value)
  VALUES (7, 'Demo Value 7');
INSERT INTO demo.data (id, string_value)
  VALUES (8, 'Demo Value 8');
INSERT INTO demo.data (id, string_value)
  VALUES (9, 'Demo Value 9');

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory01', 'Test Code Category 01', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode011', 'TestCodeCategory01', 'Test Code Name 1.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode012', 'TestCodeCategory01', 'Test Code Name 1.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode013', 'TestCodeCategory01', 'Test Code Name 1.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory02', 'Test Code Category 02', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode021', 'TestCodeCategory02', 'Test Code Name 2.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode022', 'TestCodeCategory02', 'Test Code Name 2.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode023', 'TestCodeCategory02', 'Test Code Name 2.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory03', 'Test Code Category 03', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode031', 'TestCodeCategory03', 'Test Code Name 3.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode032', 'TestCodeCategory03', 'Test Code Name 3.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode033', 'TestCodeCategory03', 'Test Code Name 3.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory04', 'Test Code Category 04', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode041', 'TestCodeCategory04', 'Test Code Name 4.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode042', 'TestCodeCategory04', 'Test Code Name 4.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode043', 'TestCodeCategory04', 'Test Code Name 4.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory05', 'Test Code Category 05', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode051', 'TestCodeCategory05', 'Test Code Name 5.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode052', 'TestCodeCategory05', 'Test Code Name 5.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode053', 'TestCodeCategory05', 'Test Code Name 5.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory06', 'Test Code Category 06', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode061', 'TestCodeCategory06', 'Test Code Name 6.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode062', 'TestCodeCategory06', 'Test Code Name 6.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode063', 'TestCodeCategory06', 'Test Code Name 6.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory07', 'Test Code Category 07', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode071', 'TestCodeCategory07', 'Test Code Name 7.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode072', 'TestCodeCategory07', 'Test Code Name 7.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode073', 'TestCodeCategory07', 'Test Code Name 7.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory08', 'Test Code Category 08', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode081', 'TestCodeCategory08', 'Test Code Name 8.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode082', 'TestCodeCategory08', 'Test Code Name 8.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode083', 'TestCodeCategory08', 'Test Code Name 8.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory09', 'Test Code Category 09', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode091', 'TestCodeCategory09', 'Test Code Name 9.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode092', 'TestCodeCategory09', 'Test Code Name 9.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode093', 'TestCodeCategory09', 'Test Code Name 9.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory10', 'Test Code Category 10', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode101', 'TestCodeCategory10', 'Test Code Name 10.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode102', 'TestCodeCategory10', 'Test Code Name 10.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode103', 'TestCodeCategory10', 'Test Code Name 10.3', 'Test Code Value 3', NOW());

INSERT INTO codes.code_categories (id, name, data, created)
  VALUES ('TestCodeCategory11', 'Test Code Category 11', '<codes><code name="name1" value="value1"/></codes>', NOW());

INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode111', 'TestCodeCategory11', 'Test Code Name 11.1', 'Test Code Value 1', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode112', 'TestCodeCategory11', 'Test Code Name 11.2', 'Test Code Value 2', NOW());
INSERT INTO codes.codes (id, code_category_id, name, value, created)
  VALUES ('TestCode113', 'TestCodeCategory11', 'Test Code Name 11.3', 'Test Code Value 3;', NOW());


--INSERT INTO configuration.configuration (key, value, description) VALUES ('TestKey', 'TestValue', 'TestDescription');


-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b601', 'Test 1', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b602', 'Test 2', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b603', 'Test 3', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b604', 'Test 4', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b605', 'Test 5', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b606', 'Test 6', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b607', 'Test 7', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b608', 'Test 8', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b609', 'Test 9', 1);
-- INSERT INTO security.tenants (id, name, status)
--   VALUES ('204e5b8f-48e7-4354-bd15-753e6543b610', 'Test 10', 1);
















INSERT INTO security.user_directories (id, type, name, configuration, created)
  VALUES ('2a935e0d-ac7e-44c4-aa44-e986aeb8c00e', 'InternalUserDirectory', 'Another Internal User Directory', '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE userDirectory SYSTEM "UserDirectoryConfiguration.dtd"><userDirectory><parameter><name>MaxPasswordAttempts</name><value>5</value></parameter><parameter><name>PasswordExpiryMonths</name><value>12</value></parameter><parameter><name>PasswordHistoryMonths</name><value>24</value></parameter><parameter><name>MaxFilteredUsers</name><value>100</value></parameter></userDirectory>', NOW());

INSERT INTO security.user_directory_to_tenant_map (user_directory_id, tenant_id)
  VALUES ('2a935e0d-ac7e-44c4-aa44-e986aeb8c00e', '204e5b8f-48e7-4354-bd15-753e6543b64d');

INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('f4292748-7b98-4b6b-94bf-2abed5ccb0eb', '2a935e0d-ac7e-44c4-aa44-e986aeb8c00e', 'jonas', 1, 'Jonas Lang', 'Jonas', '', '', 'jonas@demo.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('963c604a-bcaf-4262-9e94-a2f2c55c029d', '2a935e0d-ac7e-44c4-aa44-e986aeb8c00e', 'denise', 1, 'Denise Rasmussen', 'Denise', '', '', 'denise@demo.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
  VALUES ('606112a5-5a01-45d6-a29b-cea8d22e592d', '2a935e0d-ac7e-44c4-aa44-e986aeb8c00e', 'davis', 1, 'Davis Parks', 'Davis', '', '', 'davis@demo.com', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());

INSERT INTO security.groups (id, user_directory_id, name, description, created)
  VALUES ('69aea0f3-5798-4ee8-9082-6c41feba3472', '2a935e0d-ac7e-44c4-aa44-e986aeb8c00e', 'Tenant Administrators', 'Tenant Administrators', NOW());
INSERT INTO security.groups (id, user_directory_id, name, description, created)
  VALUES ('dd9c3e26-5c86-4ef9-b7c7-f7856c2f836d', '2a935e0d-ac7e-44c4-aa44-e986aeb8c00e', 'Password Resetters', 'Password Resetters', NOW());

INSERT INTO security.user_to_group_map (user_id, group_id)
  VALUES ('f4292748-7b98-4b6b-94bf-2abed5ccb0eb', '69aea0f3-5798-4ee8-9082-6c41feba3472');
INSERT INTO security.user_to_group_map (user_id, group_id)
  VALUES ('606112a5-5a01-45d6-a29b-cea8d22e592d', 'dd9c3e26-5c86-4ef9-b7c7-f7856c2f836d');

INSERT INTO security.role_to_group_map (role_code, group_id)
  VALUES ('TenantAdministrator', '69aea0f3-5798-4ee8-9082-6c41feba3472');
INSERT INTO security.role_to_group_map (role_code, group_id)
  VALUES ('PasswordResetter', 'dd9c3e26-5c86-4ef9-b7c7-f7856c2f836d');



INSERT INTO scheduler.jobs (id, name, scheduling_pattern, job_class, enabled, status, created)
  VALUES ('DemoJob', 'Demo Job', '* * * * *', 'demo.job.DemoJob', true, 1, NOW());






INSERT INTO party.parties(id, tenant_id, type, name, created)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', '204e5b8f-48e7-4354-bd15-753e6543b64d', 'person', 'Joe Bloggs', NOW());
INSERT INTO party.persons(id, title, initials, given_name, surname, preferred_name, gender, race, date_of_birth, language, country_of_birth, country_of_residence, employment_status, employment_type, marital_status, marriage_type, occupation, residency_status, residential_type)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', 'mr', 'J', 'Joe', 'Bloggs', 'Joe', 'male', 'white', '1976-03-07', 'EN', 'ZA', 'ZA', 'other', 'unemployed', 'married', 'anc_without_accrual', 'unemployed', 'citizen', 'cohabitant');
INSERT INTO party.identity_documents(party_id, type, number, date_of_issue, country_of_issue, created)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', 'za_id_book', '7603079236083', '2007-08-04', 'ZA', NOW());
INSERT INTO party.contact_mechanisms(party_id, type, role, value, created)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', 'mobile_number', 'personal_mobile_number', '+27835551234', NOW());
INSERT INTO party.contact_mechanisms(party_id, type, role, value, created)
  VALUES ('54166574-6564-468a-b845-8a5c127a4345', 'email_address', 'personal_email_address', 'joe@demo.com', NOW());
INSERT INTO party.physical_addresses (id, party_id, type, role, line1, line2, city, region, country, postal_code, purposes, created)
  VALUES ('14166574-6564-468a-b845-8a5c127a4345', '54166574-6564-468a-b845-8a5c127a4345', 'unstructured', 'residential', '145 Apple Street', 'Fairland', 'Johannesburg', 'GP', 'ZA', '2170', 'billing,correspondence,delivery', NOW());


INSERT INTO party.parties (id, tenant_id, type, name, created)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', '204e5b8f-48e7-4354-bd15-753e6543b64d', 'person', 'Sally Smith', NOW());
INSERT INTO party.roles (party_id, type, effective_from, created)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', 'test_person_role', NOW(), NOW());
INSERT INTO party.persons (id, title, initials, given_name, surname, gender, race, date_of_birth, language, country_of_birth, country_of_residence, employment_status, employment_type, marital_status, occupation, residency_status, residential_type, countries_of_tax_residence)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', 'ms', 'S', 'Sally', 'Smith', 'female', 'asian', '1985-05-01', 'EN', 'GB', 'ZA', 'employed', 'full_time', 'single', 'executive', 'citizen', 'owner', 'ZA');
INSERT INTO party.identity_documents (party_id, type, number, date_of_issue, country_of_issue, created)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', 'za_id_card', '8505016777088', '2015-02-17', 'ZA', NOW());
INSERT INTO party.contact_mechanisms (party_id, type, role, value, created)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', 'email_address', 'work_email_address', 'sally@demo.com', NOW());
INSERT INTO party.physical_addresses (id, party_id, type, role, complex_name, complex_unit_number, street_number, street_name, suburb, city, region, country, postal_code, latitude, longitude, purposes, created)
  VALUES ('10166574-6564-468a-b845-8a5c127a4345', '00166574-6564-468a-b845-8a5c127a4345', 'complex', 'residential', 'Happy Place', '7', '15', 'Fifth Avenue', 'Linden', 'Johannesburg', 'GP', 'ZA', '2195', '-26.137005', '27.969069', 'billing,correspondence,delivery', NOW());
INSERT INTO party.preferences(party_id, type, value, created)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', 'correspondence_language', 'EN', NOW());
INSERT INTO party.preferences(party_id, type, value, created)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', 'time_to_contact', 'anytime', NOW());
INSERT INTO party.tax_numbers (party_id, type, number, country_of_issue, created)
  VALUES ('00166574-6564-468a-b845-8a5c127a4345', 'za_income_tax_number', '1234567890', 'ZA', NOW());





-- INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
-- VALUES ('54166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'joe', 1, 'Joe Bloggs', 'Joe', '', '', '', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());
-- INSERT INTO security.users (id, user_directory_id, username, status, name, preferred_name, phone_number, mobile_number, email, password, password_attempts, password_expiry, created)
-- VALUES ('00166574-6564-468a-b845-8a5c127a4345', '34ccdbc9-4a01-46f5-a284-ba13e095675c', 'sally', 1, 'Sally Smith', 'Sally', '', '', '', '$2a$10$Dig2ZjUaDC/yUpAEjQR3w.wzcnomyBlS6oRXB.5.n.07XnVlAARL.', 0, PARSEDATETIME('2050-12-31 00:00:00 GMT', 'yyyy-MM-dd HH:mm:ss z', 'en', 'GMT'), NOW());


INSERT INTO demo.vehicle_attribute_types (code, name, description)
  VALUES ('color', 'Color', 'The vehicle color');



