@echo off
setlocal

set REPO_PATH=D:\Develop\MMP\inception
set IMAGE_NAME=java21-maven-aider

rem set MODEL_NAME=lm_studio/openai/gpt-oss-20b
rem set MODEL_NAME=lm_studio/qwen/qwen3.6-27b
set MODEL_NAME=lm_studio/kimi-k2.5-pruned-20b


docker run --rm -it ^
  --add-host=host.docker.internal:host-gateway ^
  --mount type=bind,source="%REPO_PATH%",target=/workspace ^
  --mount type=bind,source="%USERPROFILE%\.m2",target=/root/.m2 ^
  -w /workspace/src ^
  %IMAGE_NAME% ^
  aider --model %MODEL_NAME% ^
    --no-show-model-warnings ^
    --map-tokens 1024 ^
    --subtree-only ^
    --no-auto-commits

endlocal
