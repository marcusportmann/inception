Got it — here is the **true raw Markdown** (no outer code block, no extra IDs, ready to paste into a `.md` file):

---

# 🚀 Run Aider + Java 21 + Maven in Docker (with LM Studio)

This guide walks you through:

1. Installing Docker
2. Building a custom image (Java 21 + Maven + Aider)
3. Connecting Aider to **LM Studio (local LLM)**
4. Mounting your Git repo + Maven cache

---

# 1. Install Docker

## Windows (recommended: Docker Desktop)

1. Download Docker Desktop:
   [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)

2. Install and enable:

   * WSL2 backend
   * Drive sharing for `C:`

3. Verify:

```bash
docker --version
docker run hello-world
```

---

## macOS

```bash
brew install --cask docker
```

Start Docker Desktop, then:

```bash
docker run hello-world
```

---

## Linux (Ubuntu)

```bash
sudo apt update
sudo apt install docker.io -y
sudo systemctl enable --now docker
sudo usermod -aG docker $USER
```

Log out/in, then:

```bash
docker run hello-world
```

---

# 2. Create Docker Image (Java 21 + Maven + Aider)

Create a file named:

```
Dockerfile
```

Paste:

```dockerfile
FROM maven:3.9.9-eclipse-temurin-21

# Install dependencies
RUN apt-get update && apt-get install -y \
    git \
    curl \
    python3 \
    python3-pip \
    python3-venv \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Install Aider in virtualenv
RUN python3 -m venv /opt/aider-venv \
    && /opt/aider-venv/bin/pip install --upgrade pip \
    && /opt/aider-venv/bin/pip install aider-chat

# Add Aider to PATH
ENV PATH="/opt/aider-venv/bin:${PATH}"

# LM Studio config (OpenAI-compatible API)
ENV LM_STUDIO_API_BASE="http://host.docker.internal:1234/v1"
ENV LM_STUDIO_API_KEY="dummy-api-key"

WORKDIR /workspace

CMD ["bash"]
```

---

# 3. Build the Image

From the folder containing your Dockerfile:

```bash
docker build -t java21-maven-aider .
```

---

# 4. Start LM Studio (Local LLM)

1. Open LM Studio
2. Download a coding model:

   * qwen2.5-coder:7b
   * deepseek-coder
3. Start the server

Default endpoint:

```
http://localhost:1234/v1
```

Test:

```bash
curl http://localhost:1234/v1/models
```

---

# 5. Run Container with Your Git Repo

## If using Git Bash (Windows)

Your path:

```
C:\Data\Develop\MMP\inception
```

Becomes:

```
/c/Data/Develop/MMP/inception
```

---

## Run container

```bash
docker run --rm -it \
  --add-host=host.docker.internal:host-gateway \
  -v "/c/Data/Develop/MMP/inception:/workspace" \
  -v "$HOME/.m2:/root/.m2" \
  -w /workspace \
  java21-maven-aider
```

---

## What this does

| Mount      | Purpose                |
| ---------- | ---------------------- |
| /workspace | Your Git repo          |
| /root/.m2  | Persistent Maven cache |

---

# 6. Run Aider

Inside the container:

```bash
aider --model lm_studio/your-model-name
```

Example:

```bash
aider --model lm_studio/qwen2.5-coder-7b-instruct
```

---

# 7. Verify Java + Maven

```bash
java -version
mvn -version
mvn test
```

---

# 8. Optional: One-command script

Create:

```
aider-docker.sh
```

```bash
#!/usr/bin/env bash

docker run --rm -it \
  --add-host=host.docker.internal:host-gateway \
  -v "/c/Data/Develop/MMP/inception:/workspace" \
  -v "$HOME/.m2:/root/.m2" \
  -w /workspace \
  java21-maven-aider \
  aider --model lm_studio/your-model-name
```

Run:

```bash
chmod +x aider-docker.sh
./aider-docker.sh
```

---

# Notes & Gotchas

## LM Studio connectivity

Inside Docker use:

```
host.docker.internal
```

NOT `localhost`.

---

## Linux users

Add if needed:

```bash
--add-host=host.docker.internal:host-gateway
```

---

## Maven cache performance

```bash
-v "$HOME/.m2:/root/.m2"
```

prevents re-downloading dependencies.

---

## Aider safety

Aider can:

* edit files
* run commands

Commit or stash changes before using it.

---

# Result

You now have:

* Local LLM (LM Studio)
* Dockerized dev environment
* Java 21 + Maven
* Aider coding agent
* Persistent dependencies
* Full control over your codebase

---

If you want, I can tailor this specifically for **IntelliJ integration + debugging workflows**, which is usually the next step.
