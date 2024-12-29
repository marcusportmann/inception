#!/bin/sh

OTEL_JAVAAGENT_VERSION="2.10.0"

clear -X

# Function to download a file if it doesn't already exist
download_if_not_exists() {
  local url=$1
  local output_path=$2
  if [ ! -f "$output_path" ]; then
    echo "Downloading $(basename "$output_path")..."
    curl -sSLk -o "$output_path" "$url"
  else
    echo "$(basename "$output_path") already exists. Skipping download."
  fi
}

# Parse arguments
REBUILD=false
for arg in "$@"; do
  case $arg in
    --rebuild)
      REBUILD=true
      shift
      ;;
    *)
      # Unknown option
      ;;
  esac
done

# Rebuild the application if --rebuild is specified
if [ "$REBUILD" = true ]; then
  echo "Rebuilding the application..."
  mvn -T 1C -B package
else
  echo "Skipping rebuild. Use --rebuild to force a rebuild."
fi

# Define the target directory
TARGET_DIR="inception-demo/target"
mkdir -p "$TARGET_DIR"

# Download necessary JAR files if they don't exist
download_if_not_exists "https://repo1.maven.org/maven2/io/opentelemetry/javaagent/opentelemetry-javaagent/$OTEL_JAVAAGENT_VERSION/opentelemetry-javaagent-$OTEL_JAVAAGENT_VERSION.jar" "$TARGET_DIR/opentelemetry-javaagent.jar"

# Set environment variables required by the OpenTelemetry SDK configuration
export OTEL_SDK_DISABLED=${OTEL_SDK_DISABLED:-false}
export APPLICATION_NAME=${APPLICATION_NAME:-"inception-demo"}
export KUBERNETES_NAMESPACE_NAME=${KUBERNETES_NAMESPACE_NAME:-"local"}
export OTEL_TRACING_SPANS_PER_SECOND=${OTEL_TRACING_SPANS_PER_SECOND:-10}

# Run the application with the OpenTelemetry Java Agent
java -javaagent:$TARGET_DIR/opentelemetry-javaagent.jar \
  -Dotel.javaagent.debug=false \
  -Dotel.javaagent.extensions=inception-opentelemetry/target/inception-opentelemetry-1.0.0-SNAPSHOT.jar \
  -Dotel.experimental.config-file=inception-opentelemetry/opentelemetry-sdk-console-config.yml \
  -jar $TARGET_DIR/inception-demo.jar
