#!/bin/sh

# Execute the following command to allow the script to be executed on MacOS:
#   xattr -d com.apple.quarantine download_postgresql_jdbc_driver.sh

curl -o drivers/postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.6.0.jar
