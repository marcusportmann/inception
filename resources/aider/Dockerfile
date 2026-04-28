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

