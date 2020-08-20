#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine initialize_sample_postgresql_database.sh

liquibase --changeLogFile=inception.changelog.xml --username=sample --password=Password1 --url=jdbc:postgresql://localhost:5432/sample --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update
liquibase --changeLogFile=sample.changelog.xml --username=sample --password=Password1 --url=jdbc:postgresql://localhost:5432/sample --classpath=postgresql-42.2.14.jar --driver=org.postgresql.Driver --liquibaseSchemaName=liquibase update

