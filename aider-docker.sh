#!/usr/bin/env bash
set -euo pipefail

REPO_PATH="/d/Develop/MMP/inception"
IMAGE_NAME="java21-maven-aider"
MODEL_NAME="lm_studio/openai/gpt-oss-20b"

docker run --rm -it \
  --add-host=host.docker.internal:host-gateway \
  --mount type=bind,source="${REPO_PATH}",target=/workspace \
  --mount type=bind,source="${HOME}/.m2",target=/root/.m2 \
  -w /workspace/src \
  "${IMAGE_NAME}" \
  aider --model "${MODEL_NAME}" \
    --no-show-model-warnings \
    --map-tokens 1024 \
    --subtree-only \
    --no-auto-commits

    