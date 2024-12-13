#!/bin/bash

# Default namespace
DEFAULT_NAMESPACE="test"

# Function to display usage information
function usage() {
  echo "Usage: $0 [app-name] [environment-variable-name] [namespace]"
  echo ""
  echo "Arguments:"
  echo "  app-name                  The name of the application (used to derive deployment and app label)."
  echo "  environment-variable-name The name of the environment variable to retrieve."
  echo "  namespace                 (Optional) The namespace to query. Defaults to '${DEFAULT_NAMESPACE}'."
  echo ""
  echo "Options:"
  echo "  -h, --help                Show this help message and exit."
  exit 0
}

# Check for help flag
if [[ "$1" == "-h" || "$1" == "--help" ]]; then
  usage
fi

# Input arguments
APP_NAME="$1"
ENV_VAR_NAME="$2"
NAMESPACE="${3:-$DEFAULT_NAMESPACE}"  # Use the provided namespace or default to "test"

# Prompt for app name if not specified
if [[ -z "${APP_NAME}" ]]; then
  read -p "Enter the app name: " APP_NAME
  if [[ -z "${APP_NAME}" ]]; then
    echo "App name is required."
    exit 1
  fi
fi

# Prompt for environment variable name if not specified
if [[ -z "${ENV_VAR_NAME}" ]]; then
  read -p "Enter the environment variable name: " ENV_VAR_NAME
  if [[ -z "${ENV_VAR_NAME}" ]]; then
    echo "Environment variable name is required."
    exit 1
  fi
fi

APP_LABEL="${APP_NAME}"
DEPLOYMENT_NAME="${APP_NAME}-deployment"

# Check if kubectl is installed
if ! command -v kubectl &> /dev/null; then
  echo "kubectl is not installed. Please install it and try again."
  exit 1
fi

# Verify namespace exists
if ! kubectl get namespace "${NAMESPACE}" &> /dev/null; then
  echo "Namespace '${NAMESPACE}' does not exist."
  exit 1
fi

# Verify deployment exists in the namespace
if ! kubectl get deployment "${DEPLOYMENT_NAME}" -n "${NAMESPACE}" &> /dev/null; then
  echo "Deployment '${DEPLOYMENT_NAME}' does not exist in namespace '${NAMESPACE}'."
  exit 1
fi

# Get the namespace of the deployment
KUBERNETES_NAMESPACE_NAME=$(kubectl get deployment "${DEPLOYMENT_NAME}" -n "${NAMESPACE}" -o=jsonpath='{.metadata.namespace}')

# Output the namespace name
if [[ -n "${KUBERNETES_NAMESPACE_NAME}" ]]; then
  echo "The KUBERNETES_NAMESPACE_NAME for deployment '${DEPLOYMENT_NAME}' is: ${KUBERNETES_NAMESPACE_NAME}"
else
  echo "Failed to retrieve the namespace for deployment '${DEPLOYMENT_NAME}'."
  exit 1
fi

# Retrieve the pod associated with the deployment
POD_NAME=$(kubectl get pods -n "${NAMESPACE}" -l app="${APP_LABEL}" -o jsonpath='{.items[0].metadata.name}' 2>/dev/null || echo "")

if [[ -z "${POD_NAME}" ]]; then
  echo "No pod found for deployment '${DEPLOYMENT_NAME}' in namespace '${NAMESPACE}'."
  kubectl describe deployment "${DEPLOYMENT_NAME}" -n "${NAMESPACE}"
  kubectl get pods -n "${NAMESPACE}" -l app="${APP_LABEL}"
  exit 1
fi

# Verify the value of the specified environment variable in the container
ENV_VALUE=$(kubectl exec -n "${NAMESPACE}" "${POD_NAME}" -- env | grep "^${ENV_VAR_NAME}=" | cut -d '=' -f 2)

if [[ -n "${ENV_VALUE}" ]]; then
  echo "The value of the environment variable '${ENV_VAR_NAME}' is: ${ENV_VALUE}"
else
  echo "The environment variable '${ENV_VAR_NAME}' is not set in the container."
  exit 1
fi

