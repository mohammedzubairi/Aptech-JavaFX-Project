# Use BellSoft Liberica JDK 21 with JavaFX (Debian-based for best compatibility)
FROM bellsoft/liberica-openjdk-debian:21

# Install Maven, Git, and minimal JavaFX native dependencies for GUI support
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        maven \
        git \
        libxext6 \
        libxrender1 \
        libxtst6 \
        libxi6 \
        libxrandr2 \
        libgtk-3-0 \
        libxt6 \
        libxxf86vm1 \
        libgl1-mesa-glx \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /workspace

# Copy Maven wrapper and pom.xml first for better build caching
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY .mvn/ .mvn/
COPY pom.xml pom.xml

# Fix line endings and permissions for mvnw
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Pre-download dependencies (improves build speed for devs)
RUN ./mvnw dependency:go-offline

# Copy the rest of the source code
COPY src/ src/
COPY CreatedFiles/ CreatedFiles/
COPY report/ report/

# Expose a volume for live code sharing (for development)
VOLUME ["/workspace/src"]

# Expose JavaFX ports if needed (optional, for remote GUIs)
# EXPOSE 8080

# Default command: run the JavaFX app via Maven
CMD ["./mvnw", "javafx:run"] 