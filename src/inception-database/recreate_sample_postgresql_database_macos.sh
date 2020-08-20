#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine recreate_sample_postgresql_database_macos.sh

dropdb --if-exists sample
createdb  --template=template0 --encoding=UTF8 sample
psql --dbname=sample --command="DROP ROLE IF EXISTS sample;"
psql --dbname=sample --command="CREATE ROLE sample WITH LOGIN PASSWORD 'sample';"
psql --dbname=sample --command="GRANT ALL PRIVILEGES ON DATABASE sample to sample;"
psql --dbname=sample --command="ALTER USER sample WITH SUPERUSER;"
psql --dbname=sample --command="CREATE SCHEMA liquibase;"



