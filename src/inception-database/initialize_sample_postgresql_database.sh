#!/bin/sh
liquibase --changeLogFile=inception.changelog.xml --username=sample --password=Password1 --url=jdbc:postgresql://localhost:5432/sample --classpath=postgresql-42.2.8.jar --driver=org.postgresql.Driver update
liquibase --changeLogFile=sample.changelog.xml --username=sample --password=Password1 --url=jdbc:postgresql://localhost:5432/sample --classpath=postgresql-42.2.8.jar --driver=org.postgresql.Driver update

