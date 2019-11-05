#!/bin/sh
dropdb --if-exists sample
createdb  --template=template0 --encoding=UTF8 sample
psql --dbname=sample --command="DROP ROLE IF EXISTS sample;"
psql --dbname=sample --command="CREATE ROLE sample WITH LOGIN PASSWORD 'Password1';"
psql --dbname=sample --command="ALTER USER sample WITH SUPERUSER;"
