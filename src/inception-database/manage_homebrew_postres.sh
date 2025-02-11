#!/usr/bin/env bash
#
# manage_homebrew_postgres.sh
#
# A script to manage multiple Homebrew-installed PostgreSQL versions on macOS.

# Detect Homebrew prefix automatically (works for both Intel and Apple Silicon, if brew is installed).
HOMEBREW_PREFIX="$(brew --prefix 2>/dev/null)"

# Fallback if brew is not found or --prefix fails
if [[ -z "$HOMEBREW_PREFIX" ]]; then
  echo "Error: Homebrew not found in PATH or \`brew --prefix\` failed."
  exit 1
fi

# Scan for installed PostgreSQL versions based on directories:
# e.g., /opt/homebrew/var/postgresql@14
declare -a VERSIONS=()

for dir in "$HOMEBREW_PREFIX/var"/postgresql@*; do
  # Ensure it's a directory before proceeding
  if [[ -d "$dir" ]]; then
    # Extract the version from the directory name
    ver="${dir##*@}"
    VERSIONS+=( "$ver" )
  fi
done

# If no versions found, exit
if [[ ${#VERSIONS[@]} -eq 0 ]]; then
  echo "No installed PostgreSQL versions found under '$HOMEBREW_PREFIX/var'."
  echo "You may need to install a version via Homebrew, e.g.: brew install postgresql@14"
  exit 0
fi

# Helper function to display all detected versions
function list_versions() {
  echo "Detected PostgreSQL versions:"
  for i in "${!VERSIONS[@]}"; do
    echo "  $((i+1)). PostgreSQL@${VERSIONS[$i]}"
  done
  echo
}

# Prompt user to select a PostgreSQL version
function select_version() {
  local choice
  list_versions
  read -rp "Select a version by number (1-${#VERSIONS[@]}): " choice
  # Basic validation
  if [[ "$choice" =~ ^[0-9]+$ ]] && (( choice >= 1 && choice <= ${#VERSIONS[@]} )); then
    SELECTED_VERSION="${VERSIONS[$((choice-1))]}"
  else
    echo "Invalid selection."
    exit 1
  fi
}

# Menu of actions
function print_menu() {
  cat <<EOF

What would you like to do with PostgreSQL@$SELECTED_VERSION?

1) Initialize or re-initialize (WARNING: this deletes the data directory!)
2) Start PostgreSQL@$SELECTED_VERSION in the foreground
3) Install/start PostgreSQL@$SELECTED_VERSION as a macOS service (auto-start at login)
4) Stop PostgreSQL@$SELECTED_VERSION and disable auto-start at login
5) Quit

EOF
}

# Main logic
select_version

while true; do
  print_menu
  read -rp "Enter your choice: " action
  case "$action" in
    1)
      echo "Re-initializing PostgreSQL@$SELECTED_VERSION..."
      DATA_DIR="$HOMEBREW_PREFIX/var/postgresql@$SELECTED_VERSION"
      BIN_DIR="$HOMEBREW_PREFIX/opt/postgresql@$SELECTED_VERSION/bin"

      # 1) Delete data directory
      if [[ -d "$DATA_DIR" ]]; then
        echo "Deleting data directory at '$DATA_DIR'..."
        rm -rf "$DATA_DIR"
      fi

      # 2) Run initdb
      echo "Running initdb..."
      mkdir -p "$DATA_DIR"
      "$BIN_DIR/initdb" --locale=C -E UTF-8 "$DATA_DIR"
      echo "Re-initialization complete."
      ;;

    2)
      echo "Starting PostgreSQL@$SELECTED_VERSION in the foreground..."
      DATA_DIR="$HOMEBREW_PREFIX/var/postgresql@$SELECTED_VERSION"
      BIN_DIR="$HOMEBREW_PREFIX/opt/postgresql@$SELECTED_VERSION/bin"
      # This runs in the foreground, blocking the script. 
      # In a real environment, you might want to run in background or in a separate terminal.
      exec "$BIN_DIR/postgres" -D "$DATA_DIR"
      ;;

    3)
      echo "Enabling PostgreSQL@$SELECTED_VERSION as a macOS service..."
      brew services start "postgresql@$SELECTED_VERSION"
      ;;

    4)
      echo "Stopping PostgreSQL@$SELECTED_VERSION service and disabling auto-start..."
      brew services stop "postgresql@$SELECTED_VERSION"
      ;;

    5)
      echo "Exiting..."
      exit 0
      ;;

    *)
      echo "Invalid option, please try again."
      ;;
  esac
done

