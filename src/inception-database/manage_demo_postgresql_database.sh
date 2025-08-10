#!/bin/sh
#
# manage_demo_postgresql_database.sh
#
# This script can:
#   1) Recreate and initialize the "demo" PostgreSQL database
#   2) Initialize (Liquibase only) the existing "demo" database
#   3) Rollback Liquibase changes to a specified date
#
# It automatically:
#   - Determines the latest PostgreSQL JDBC driver version from Maven Central
#   - Downloads that driver into a versioned jar (e.g., postgresql-42.6.0.jar)
#     if it doesn't already exist
#   - Runs Liquibase scripts
#   - Displays how many Liquibase changelogs succeeded/failed for both apply and rollback,
#     and also lists the names of any failed scripts.

set -euo pipefail

# ----------------------------------------------------------------------------------
# 0) Menu Prompt
# ----------------------------------------------------------------------------------
echo "Select an action to perform on the 'demo' database:"
echo "1) Recreate and initialize the demo database"
echo "2) Initialize (Liquibase only) the existing demo database"
echo "3) Rollback the Liquibase scripts (to-date) on the existing demo database"
echo

read -rp "Enter your choice [1-3]: " USER_CHOICE

# ----------------------------------------------------------------------------------
# 1) Determine the latest PostgreSQL JDBC driver version from Maven Central
# ----------------------------------------------------------------------------------
echo
echo "Determining latest PostgreSQL JDBC driver version from Maven Central..."

set +e
MAVEN_CENTRAL_JSON="$(
  curl -s "https://search.maven.org/solrsearch/select?q=g:org.postgresql+AND+a:postgresql&core=gav&rows=1&wt=json&sort=version+desc"
)"

if [ -z "$MAVEN_CENTRAL_JSON" ]; then
  echo "ERROR: Unable to retrieve Maven Central search results. Using version 42.7.5."
  LATEST_VERSION="42.7.5"
else
  LATEST_VERSION="$(
    echo "$MAVEN_CENTRAL_JSON" \
    | grep -oE '"v":"[^"]+"' \
    | head -1 \
    | sed -E 's/"v":"([^"]+)"/\1/'
  )"

  if [ -z "$LATEST_VERSION" ]; then
    echo "ERROR: Could not parse the latest version from Maven Central response."
    exit 1
  fi

  echo "Latest PostgreSQL JDBC driver version detected: $LATEST_VERSION"
fi
set -e

# ----------------------------------------------------------------------------------
# 2) Setup: directories, driver path, DB credentials
# ----------------------------------------------------------------------------------
JDBC_DRIVER_DIR="drivers"
LATEST_JAR_NAME="postgresql-${LATEST_VERSION}.jar"
JDBC_DRIVER_PATH="${JDBC_DRIVER_DIR}/${LATEST_JAR_NAME}"

DB_NAME="demo"
DB_USER="demo"
DB_PASS="demo"
DB_URL="jdbc:postgresql://localhost:5432/${DB_NAME}"

# ----------------------------------------------------------------------------------
# 3) Download driver if missing
# ----------------------------------------------------------------------------------
DOWNLOAD_URL="https://repo1.maven.org/maven2/org/postgresql/postgresql/${LATEST_VERSION}/${LATEST_JAR_NAME}"

if [ -f "$JDBC_DRIVER_PATH" ]; then
  echo
  echo "PostgreSQL JDBC driver '${LATEST_JAR_NAME}' already exists in '${JDBC_DRIVER_DIR}'."
  echo "No download needed. Using existing driver."
else
  echo
  echo "PostgreSQL JDBC driver '${LATEST_JAR_NAME}' not found. Downloading..."
  mkdir -p "$JDBC_DRIVER_DIR"
  curl -sSL "$DOWNLOAD_URL" -o "$JDBC_DRIVER_PATH"
  echo "Download complete: $JDBC_DRIVER_PATH"
fi

# ----------------------------------------------------------------------------------
# 4) Liquibase configuration
# ----------------------------------------------------------------------------------
LIQUIBASE_BASE_ARGS="--username=${DB_USER} \
                     --password=${DB_PASS} \
                     --url=${DB_URL} \
                     --classpath=${JDBC_DRIVER_PATH} \
                     --driver=org.postgresql.Driver"

LIQUIBASE_UPDATE_ARGS="${LIQUIBASE_BASE_ARGS} update"

# Use a simple date format Liquibase 4.31.0 might accept
LIQUIBASE_ROLLBACK_CMD='rollback-to-date --date=2020-01-01'

# Changelogs
CHANGELOGS=(
  "../inception-core/src/main/resources/db/inception-core.changelog.xml"

  "../inception-application/src/main/resources/db/inception-application.changelog.xml"

  "../inception-audit/src/main/resources/db/inception-audit.changelog.xml"

  "../inception-codes/src/main/resources/db/inception-codes.changelog.xml"

  "../inception-config/src/main/resources/db/inception-config.changelog.xml"

  "../inception-error/src/main/resources/db/inception-error.changelog.xml"

  "../inception-executor/src/main/resources/db/inception-executor.changelog.xml"

  "../inception-flowable/src/main/resources/db/flowable-all.changelog.xml"
  "../inception-flowable/src/main/resources/db/inception-flowable.changelog.xml"

  "../inception-mail/src/main/resources/db/inception-mail.changelog.xml"

  "../inception-operations/src/main/resources/db/inception-operations.changelog.xml"

  "../inception-party/src/main/resources/db/inception-party.changelog.xml"
  "../inception-party/src/main/resources/db/inception-party-data.changelog.xml"
  "../inception-party/src/main/resources/db/inception-party-industry-classification-data.changelog.xml"
  "../inception-party/src/main/resources/db/inception-party-skills-data.changelog.xml"

  "../inception-reference/src/main/resources/db/inception-reference.changelog.xml"
  "../inception-reference/src/main/resources/db/inception-reference-data.changelog.xml"

  "../inception-reporting/src/main/resources/db/inception-reporting.changelog.xml"

  "../inception-scheduler/src/main/resources/db/inception-scheduler.changelog.xml"

  "../inception-security/src/main/resources/db/inception-security.changelog.xml"

  "../inception-sms/src/main/resources/db/inception-sms.changelog.xml"

  "../inception-demo/src/main/resources/db/demo.changelog.xml"
  "../inception-demo/src/main/resources/db/demo-data.changelog.xml"
  "../inception-demo/src/main/resources/db/demo-party-data.changelog.xml"
)

# ----------------------------------------------------------------------------------
# 5) Define functions for each operation
# ----------------------------------------------------------------------------------

recreate_and_initialize() {
  echo
  echo "=== Recreating and initializing the '${DB_NAME}' database ==="

  echo "Dropping '${DB_NAME}' if it exists..."
  dropdb --if-exists "${DB_NAME}"

  echo "Creating new database '${DB_NAME}'..."
  createdb --template=template0 --encoding=UTF8 "${DB_NAME}"

  echo "Configuring user/role '${DB_USER}'..."

  psql --dbname="${DB_NAME}" --command="DROP ROLE IF EXISTS ${DB_USER};"
  psql --dbname="${DB_NAME}" --command="CREATE ROLE ${DB_USER} WITH LOGIN PASSWORD '${DB_PASS}';"
  psql --dbname="${DB_NAME}" --command="GRANT ALL PRIVILEGES ON DATABASE ${DB_NAME} TO ${DB_USER};"
  psql --dbname="${DB_NAME}" --command="ALTER USER ${DB_USER} WITH SUPERUSER;"

  initialize_liquibase
}

initialize_liquibase() {
  echo
  echo "=== Running Liquibase scripts ==="

  number_of_successful_scripts=0
  number_of_failed_scripts=0
  # Array to capture the names of any failed scripts
  failed_script_names=()

  # Temporarily disable "exit on error" so we can capture each run's success/fail
  set +e
  for changelog in "${CHANGELOGS[@]}"; do
    echo "  => $changelog"
    liquibase --changeLogFile="$changelog" $LIQUIBASE_UPDATE_ARGS

    if [ $? -eq 0 ]; then
      number_of_successful_scripts=$((number_of_successful_scripts + 1))
    else
      number_of_failed_scripts=$((number_of_failed_scripts + 1))
      failed_script_names+=("$changelog")
    fi
  done
  set -e

  # Summaries
  echo
  echo "=== Liquibase Summary (Initialize) ==="
  echo "Scripts: Successes = $number_of_successful_scripts, Failures = $number_of_failed_scripts"

  if [ $number_of_failed_scripts -gt 0 ]; then
    echo
    echo "The following scripts failed:"
    for script in "${failed_script_names[@]}"; do
      echo " - $script"
    done
  fi

  # If any failures occurred, exit with an error code.
  if [ $number_of_failed_scripts -gt 0 ]; then
    echo
    echo "One or more Liquibase scripts failed. Exiting with status 1."
    exit 1
  fi

  echo
  echo "Liquibase initialization complete. The '${DB_NAME}' database is ready."
}

rollback_liquibase() {
  echo
  echo "=== Rolling back Liquibase changes on '${DB_NAME}' to 2020-01-01 ==="

  number_of_successful_scripts=0
  number_of_failed_scripts=0
  failed_script_names=()

  # Scripts in reverse order
  echo
  echo "Rolling back CHANGELOGS in reverse order..."
  set +e
  for (( i=${#CHANGELOGS[@]}-1 ; i>=0 ; i-- )); do
    changelog="${CHANGELOGS[$i]}"
    echo "  => $changelog"
    liquibase --changeLogFile="$changelog" \
      $LIQUIBASE_BASE_ARGS $LIQUIBASE_ROLLBACK_CMD

    if [ $? -eq 0 ]; then
      number_of_successful_scripts=$((number_of_successful_scripts + 1))
    else
      number_of_failed_scripts=$((number_of_failed_scripts + 1))
      failed_script_names+=("$changelog")
    fi
  done
  set -e

  # Summaries
  echo
  echo "=== Liquibase Summary (Rollback) ==="
  echo "Scripts rolled back: Successes = $number_of_successful_scripts, Failures = $number_of_failed_scripts"

  if [ $number_of_failed_scripts -gt 0 ]; then
    echo
    echo "The following scripts failed to roll back:"
    for script in "${failed_script_names[@]}"; do
      echo " - $script"
    done
  fi

  if [ $number_of_failed_scripts -gt 0 ]; then
    echo
    echo "One or more rollback steps failed. Exiting with status 1."
    exit 1
  fi

  echo
  echo "Rollback complete."
}

# ----------------------------------------------------------------------------------
# 6) Main Entry: Dispatch according to user choice
# ----------------------------------------------------------------------------------
case "$USER_CHOICE" in
  1)
    recreate_and_initialize
    ;;
  2)
    echo
    echo "=== Initializing Liquibase on existing '${DB_NAME}' database ==="
    initialize_liquibase
    ;;
  3)
    rollback_liquibase
    ;;
  *)
    echo
    echo "Invalid option. Exiting."
    exit 1
    ;;
esac

echo
echo "Done."
