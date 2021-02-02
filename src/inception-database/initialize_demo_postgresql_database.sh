#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine initialize_sample_postgresql_database.sh

liquibase --changeLogFile=inception-core.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
# liquibase --changeLogFile=inception-application.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update



#liquibase --changeLogFile=inception-audit.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
#liquibase --changeLogFile=inception-bmi.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-codes.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-configuration.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-error.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-mail.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-messaging.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-reporting.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-scheduler.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-security.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-sms.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update



liquibase --changeLogFile=inception-reference.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=inception-party.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update



# liquibase --changeLogFile=inception-banking.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update



liquibase --changeLogFile=inception-sample.changelog.xml --username=demo --password=demo --url=jdbc:postgresql://localhost:5432/demo --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update

