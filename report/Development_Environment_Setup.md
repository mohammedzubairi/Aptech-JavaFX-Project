# DEVELOPMENT ENVIRONMENT SETUP GUIDE

---

## FILE MANA - MODERN TEXT EDITOR
### Complete Development Environment Setup and Configuration

---

## PROJECT IDENTIFICATION

**Project Title:** File Mana - Modern Text Editor  
**Student Name:** NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH  
**Student ID:** Student1554163  
**Course:** Advanced Java Programming with JavaFX  
**Institution:** Aptech Computer Education  
**University:** Alnasser University  
**Semester:** 2  
**Project Duration:** 20-May-2025 to 10-July-2025  

---

## TABLE OF CONTENTS

1. [Overview](#1-overview)
2. [System Requirements](#2-system-requirements)
3. [Java Development Kit (JDK) Setup](#3-java-development-kit-jdk-setup)
4. [Maven Installation and Configuration](#4-maven-installation-and-configuration)
5. [IDE Setup and Configuration](#5-ide-setup-and-configuration)
6. [JavaFX Configuration](#6-javafx-configuration)
7. [Project Setup and Import](#7-project-setup-and-import)
8. [Build and Run Instructions](#8-build-and-run-instructions)
9. [Troubleshooting Guide](#9-troubleshooting-guide)
10. [Additional Tools and Resources](#10-additional-tools-and-resources)

---

## 1. OVERVIEW

### 1.1 Purpose

This guide provides comprehensive instructions for setting up a complete development environment for the File Mana - Modern Text Editor project. The setup includes all necessary tools, frameworks, and configurations required for development, building, and running the application.

### 1.2 Technology Stack

The File Mana project uses the following technologies:

| Component | Technology | Version | Purpose |
|-----------|------------|---------|---------|
| **Programming Language** | Java | 21 (LTS) | Core application development |
| **UI Framework** | JavaFX | 22 | Desktop user interface |
| **Build Tool** | Maven | 3.6+ | Dependency management and build automation |
| **Styling** | CSS | 3 | UI theming and responsive design |
| **Testing** | JUnit | 5.8+ | Unit and integration testing |

### 1.3 Project Structure

```
File Mana Project/
├── src/main/java/                    # Java source code
│   └── com/codemavriks/aptech/
│       ├── MainApp.java              # Application entry point
│       ├── EditorController.java     # Main controller
│       ├── components/               # UI components
│       └── services/                 # Business logic services
├── src/main/resources/               # Resources and assets
│   └── com/codemavriks/aptech/
│       ├── styles/modern-theme.css   # Application styling
│       └── File-Mana-Logo.png        # Application icon
├── target/                           # Compiled output (generated)
├── pom.xml                          # Maven configuration
├── mvnw                             # Maven wrapper (Unix)
├── mvnw.cmd                         # Maven wrapper (Windows)
└── README.md                        # Project documentation
```

---

## 2. SYSTEM REQUIREMENTS

### 2.1 Minimum System Requirements

| Component | Requirement |
|-----------|-------------|
| **Operating System** | Windows 10, macOS 10.14, or Linux (Ubuntu 18.04+) |
| **Processor** | Intel Core i3 or AMD equivalent |
| **Memory (RAM)** | 4 GB minimum, 8 GB recommended |
| **Storage** | 2 GB free disk space |
| **Display** | 1024x768 minimum resolution |
| **Internet Connection** | Required for initial setup and dependencies |

### 2.2 Recommended System Requirements

| Component | Recommendation |
|-----------|----------------|
| **Operating System** | Windows 11, macOS 12+, or Linux (Ubuntu 20.04+) |
| **Processor** | Intel Core i5 or AMD Ryzen 5 |
| **Memory (RAM)** | 8 GB or more |
| **Storage** | 5 GB free disk space (SSD recommended) |
| **Display** | 1920x1080 or higher resolution |

---

## 3. JAVA DEVELOPMENT KIT (JDK) SETUP

### 3.1 Download and Install JDK 21

**Step 1: Download JDK 21**

1. Visit the official Oracle JDK download page: https://www.oracle.com/java/technologies/downloads/
2. Select **Java 21** (LTS version)
3. Choose your operating system:
   - **Windows**: Download `jdk-21_windows-x64_bin.exe`
   - **macOS**: Download `jdk-21_macos-x64_bin.dmg` (Intel) or `jdk-21_macos-aarch64_bin.dmg` (Apple Silicon)
   - **Linux**: Download `jdk-21_linux-x64_bin.tar.gz`

**Step 2: Install JDK**

**Windows Installation:**
```cmd
1. Run the downloaded .exe file as Administrator
2. Follow the installation wizard
3. Accept the license agreement
4. Choose installation directory (default: C:\Program Files\Java\jdk-21)
5. Complete the installation
```

**macOS Installation:**
```bash
1. Open the downloaded .dmg file
2. Double-click the .pkg installer
3. Follow the installation prompts
4. Enter your administrator password when prompted
5. Complete the installation
```

**Linux Installation:**
```bash
# Extract the JDK
sudo tar -xzf jdk-21_linux-x64_bin.tar.gz -C /opt/

# Create symbolic link
sudo ln -s /opt/jdk-21 /opt/java

# Set up environment variables (add to ~/.bashrc or ~/.profile)
export JAVA_HOME=/opt/java
export PATH=$JAVA_HOME/bin:$PATH
```

### 3.2 Configure Environment Variables

**Windows Configuration:**

1. Open **System Properties** → **Advanced** → **Environment Variables**
2. Add new **System Variable**:
   - **Variable Name**: `JAVA_HOME`
   - **Variable Value**: `C:\Program Files\Java\jdk-21`
3. Edit **Path** variable and add: `%JAVA_HOME%\bin`
4. Click **OK** to save changes

**macOS Configuration:**

Add to `~/.zshrc` or `~/.bash_profile`:
```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
```

**Linux Configuration:**

Add to `~/.bashrc`:
```bash
export JAVA_HOME=/opt/java
export PATH=$JAVA_HOME/bin:$PATH
```

### 3.3 Verify Installation

Open terminal/command prompt and run:
```bash
java -version
javac -version
```

Expected output:
```
java version "21.0.x" 2024-xx-xx LTS
Java(TM) SE Runtime Environment (build 21.0.x+xx-LTS-xxx)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.x+xx-LTS-xxx, mixed mode, sharing)

javac 21.0.x
```

---

## 4. MAVEN INSTALLATION AND CONFIGURATION

### 4.1 Download and Install Maven

**Step 1: Download Maven**

1. Visit: https://maven.apache.org/download.cgi
2. Download **Binary zip archive**: `apache-maven-3.9.x-bin.zip`

**Step 2: Install Maven**

**Windows Installation:**
```cmd
1. Extract the zip file to C:\Program Files\Apache\maven
2. Add to System Environment Variables:
   - MAVEN_HOME: C:\Program Files\Apache\maven
   - Add to Path: %MAVEN_HOME%\bin
```

**macOS/Linux Installation:**
```bash
# Extract Maven
sudo tar -xzf apache-maven-3.9.x-bin.tar.gz -C /opt/
sudo ln -s /opt/apache-maven-3.9.x /opt/maven

# Add to ~/.bashrc or ~/.zshrc
export MAVEN_HOME=/opt/maven
export PATH=$MAVEN_HOME/bin:$PATH
```

### 4.2 Configure Maven Settings

**Create Maven Settings File:**

Create `~/.m2/settings.xml` (or `%USERPROFILE%\.m2\settings.xml` on Windows):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
  
  <localRepository>${user.home}/.m2/repository</localRepository>
  
  <profiles>
    <profile>
      <id>default</id>
      <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      </properties>
    </profile>
  </profiles>
  
  <activeProfiles>
    <activeProfile>default</activeProfile>
  </activeProfiles>
</settings>
```

### 4.3 Verify Maven Installation

```bash
mvn -version
```

Expected output:
```
Apache Maven 3.9.x
Maven home: /opt/maven
Java version: 21.0.x, vendor: Oracle Corporation
Java home: /opt/java
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "x.x.x", arch: "amd64", family: "unix"
```

### 4.4 Understanding Maven in File Mana Project

**Maven Project Structure:**
```
File Mana Project/
├── pom.xml                    # Project Object Model - Maven configuration
├── src/
│   ├── main/
│   │   ├── java/             # Java source code
│   │   └── resources/        # Resources (CSS, images, etc.)
│   │       └── com/codemavriks/aptech/
│   │           ├── MainApp.java
│   │           └── services/
│   └── test/
│       └── java/             # Test source code
├── target/                   # Compiled output (auto-generated)
└── mvnw, mvnw.cmd           # Maven wrapper scripts
```

**Key Maven Concepts:**

1. **POM (Project Object Model)**: `pom.xml` file contains project configuration
2. **Dependencies**: External libraries (JavaFX, JUnit) managed automatically
3. **Build Lifecycle**: Compile → Test → Package → Install → Deploy
4. **Maven Wrapper**: `mvnw`/`mvnw.cmd` ensures consistent Maven version

**Maven Commands for File Mana:**
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Run JavaFX application
mvn javafx:run

# Clean everything
mvn clean
```

---

## 5. IDE SETUP AND CONFIGURATION

### 5.1 IntelliJ IDEA Setup (Recommended)

**Step 1: Download and Install**

1. Visit: https://www.jetbrains.com/idea/download/
2. Download **Community Edition** (free) or **Ultimate Edition**
3. Install following the setup wizard

**Step 2: Configure IntelliJ for Java 21**

1. Open IntelliJ IDEA
2. Go to **File** → **Project Structure** → **Project**
3. Set **Project SDK** to Java 21
4. Set **Project language level** to 21

**Step 3: Install Required Plugins**

1. Go to **File** → **Settings** → **Plugins**
2. Install the following plugins:
   - **JavaFX** (if not already installed)
   - **Maven** (usually pre-installed)
   - **CSS Support** (for styling)

**Step 4: Configure JavaFX**

1. Go to **File** → **Settings** → **Build, Execution, Deployment** → **Build Tools** → **Maven**
2. Ensure **Maven home path** points to your Maven installation
3. Set **User settings file** to your `settings.xml`

### 5.2 Eclipse IDE Setup (Alternative)

**Step 1: Download and Install**

1. Visit: https://www.eclipse.org/downloads/
2. Download **Eclipse IDE for Java Developers**
3. Extract and run the installer

**Step 2: Configure Eclipse**

1. Set workspace location
2. Go to **Window** → **Preferences** → **Java** → **Installed JREs**
3. Add JDK 21 and set as default
4. Go to **Java** → **Compiler** and set compliance level to 21

**Step 3: Install JavaFX Plugin**

1. Go to **Help** → **Eclipse Marketplace**
2. Search for "JavaFX" and install **e(fx)clipse**
3. Restart Eclipse

### 5.3 Visual Studio Code Setup (Lightweight Option)

**Step 1: Install VS Code**

1. Visit: https://code.visualstudio.com/
2. Download and install for your OS

**Step 2: Install Java Extensions**

1. Open VS Code
2. Go to **Extensions** (Ctrl+Shift+X)
3. Install **Extension Pack for Java** (includes multiple Java tools)
4. Install **JavaFX Support** extension

**Step 3: Configure Java**

1. Open **Command Palette** (Ctrl+Shift+P)
2. Type "Java: Configure Runtime"
3. Set Java 21 as the runtime

---

## 6. JAVAFX CONFIGURATION

### 6.1 Understanding JavaFX in the Project

The File Mana project uses **JavaFX 22** for the user interface. JavaFX is included as a Maven dependency, so no separate installation is required.

**JavaFX Dependencies in pom.xml:**
```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>22</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>22</version>
    </dependency>
</dependencies>
```

### 6.2 JavaFX Runtime Configuration

**For Development (IDE):**

Add VM options when running the application:
```
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
```

**For Maven (Recommended):**

Use the JavaFX Maven plugin (already configured in pom.xml):
```bash
mvn javafx:run
```

### 6.3 JavaFX Module System

The project uses the Java Module System. Key files:

**module-info.java** (if present):
```java
module filemana {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    
    exports com.codemavriks.aptech;
}
```

---

## 7. PROJECT SETUP AND IMPORT

### 7.1 Download Project Source Code

**Option 1: From ZIP Archive**
1. Extract the project ZIP file to your workspace directory
2. Navigate to the extracted folder

**Option 2: From Version Control (if available)**
```bash
git clone <repository-url>
cd File-Mana-Project
```

### 7.2 Import Project into IDE

**IntelliJ IDEA:**
1. Open IntelliJ IDEA
2. Click **Open** or **Import Project**
3. Navigate to the project folder and select `pom.xml`
4. Click **Open as Project**
5. Wait for Maven to download dependencies

**Eclipse:**
1. Open Eclipse
2. Go to **File** → **Import** → **Existing Maven Projects**
3. Browse to the project folder
4. Select the project and click **Finish**

**VS Code:**
1. Open VS Code
2. Go to **File** → **Open Folder**
3. Select the project folder
4. VS Code will automatically detect the Maven project

### 7.3 Verify Project Structure

After import, verify the project structure:

```
File-Mana-Project/
├── .idea/                           # IntelliJ IDEA files
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/codemavriks/aptech/
│   │   │       ├── MainApp.java
│   │   │       ├── EditorController.java
│   │   │       ├── components/
│   │   │       │   ├── TextEditor.java
│   │   │       │   ├── SidePanel.java
│   │   │       │   └── FileNavigator.java
│   │   │       └── services/
│   │   │           └── FileService.java
│   │   └── resources/
│   │       └── com/codemavriks/aptech/
│   │           ├── styles/
│   │           │   └── modern-theme.css
│   │           └── File-Mana-Logo.png
│   └── test/
│       └── java/                    # Test files (if any)
├── target/                          # Generated during build
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## 8. BUILD AND RUN INSTRUCTIONS

### 8.1 Using Maven Commands

**Basic Build Commands:**

```bash
# Clean previous builds
mvn clean

# Compile the project
mvn compile

# Run tests (if any)
mvn test

# Package into JAR
mvn package

# Clean and package in one command
mvn clean package
```

**Running the Application:**

```bash
# Run using Maven JavaFX plugin (recommended)
mvn javafx:run

# Alternative: Run using Maven exec plugin
mvn exec:java -Dexec.mainClass="com.codemavriks.aptech.MainApp"
```

### 8.2 Using IDE

**IntelliJ IDEA:**
1. Right-click on `MainApp.java`
2. Select **Run 'MainApp.main()'**
3. Or use the green play button in the toolbar

**Eclipse:**
1. Right-click on `MainApp.java`
2. Select **Run As** → **Java Application**

**VS Code:**
1. Open `MainApp.java`
2. Click **Run** above the main method
3. Or use **F5** to debug

### 8.3 Creating Executable JAR

**Step 1: Build with Dependencies**
```bash
mvn clean package
```

**Step 2: Run the JAR**
```bash
java -jar target/File-Mana-1.0-SNAPSHOT.jar
```

**Note**: For JavaFX applications, you may need to specify module path:
```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/File-Mana-1.0-SNAPSHOT.jar
```

### 8.4 Maven Wrapper Usage

The project includes Maven wrapper scripts that ensure consistent Maven version:

**Windows:**
```cmd
# Use mvnw.cmd instead of mvn
mvnw.cmd clean compile
mvnw.cmd javafx:run
```

**Unix/Linux/macOS:**
```bash
# Use ./mvnw instead of mvn
./mvnw clean compile
./mvnw javafx:run
```

---

## 9. TROUBLESHOOTING GUIDE

### 9.1 Common Java Issues

**Issue: "java: error: release version 21 not supported"**

**Solution:**
1. Verify JDK 21 is installed: `java -version`
2. Check JAVA_HOME environment variable
3. In IDE, verify Project SDK is set to Java 21
4. Check Maven compiler plugin version in pom.xml

**Issue: "Could not find or load main class"**

**Solution:**
1. Ensure project is compiled: `mvn compile`
2. Check main class name in pom.xml
3. Verify package structure matches directory structure

### 9.2 Maven Issues

**Issue: "mvn command not found"**

**Solution:**
1. Verify Maven is installed: `mvn -version`
2. Check MAVEN_HOME and PATH environment variables
3. Use Maven wrapper: `./mvnw` (Unix) or `mvnw.cmd` (Windows)

**Issue: "Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin"**

**Solution:**
1. Check Java version compatibility
2. Update Maven compiler plugin version
3. Clear Maven cache: `mvn dependency:purge-local-repository`

### 9.3 JavaFX Issues

**Issue: "Error: JavaFX runtime components are missing"**

**Solution:**
1. Use Maven JavaFX plugin: `mvn javafx:run`
2. Add VM options: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`
3. Verify JavaFX dependencies in pom.xml

**Issue: "Module not found: javafx.controls"**

**Solution:**
1. Check JavaFX version in pom.xml
2. Ensure proper module configuration
3. Use Maven to run: `mvn javafx:run`

### 9.4 IDE-Specific Issues

**IntelliJ IDEA:**

**Issue: "Cannot resolve symbol 'javafx'"**

**Solution:**
1. Refresh Maven project: **View** → **Tool Windows** → **Maven** → **Refresh**
2. Invalidate caches: **File** → **Invalidate Caches and Restart**
3. Check Project Structure settings

**Eclipse:**

**Issue: "Project facet Java version 21 is not supported"**

**Solution:**
1. Update Eclipse to latest version
2. Install Java 21 support plugins
3. Check project facets in project properties

### 9.5 Runtime Issues

**Issue: Application starts but UI doesn't appear**

**Solution:**
1. Check console for error messages
2. Verify CSS files are in correct location
3. Check JavaFX threading (use Platform.runLater for UI updates)

**Issue: "Exception in Application start method"**

**Solution:**
1. Check MainApp.java start() method
2. Verify FXML files (if used) are in correct location
3. Check resource loading paths

---

## 10. ADDITIONAL TOOLS AND RESOURCES

### 10.1 Recommended Development Tools

**Version Control:**
- **Git**: https://git-scm.com/
- **GitHub Desktop**: https://desktop.github.com/ (GUI option)

**Database Tools (if needed):**
- **H2 Database**: Lightweight embedded database
- **SQLite**: File-based database

**Testing Tools:**
- **JUnit 5**: Unit testing framework (included in project)
- **TestFX**: JavaFX testing framework

**Code Quality:**
- **SpotBugs**: Static analysis tool
- **Checkstyle**: Code style checker
- **SonarLint**: Code quality plugin for IDEs

### 10.2 Useful Maven Plugins

Add to pom.xml if needed:

```xml
<build>
    <plugins>
        <!-- JavaFX Maven Plugin -->
        <plugin>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-maven-plugin</artifactId>
            <version>0.0.8</version>
            <configuration>
                <mainClass>com.codemavriks.aptech.MainApp</mainClass>
            </configuration>
        </plugin>
        
        <!-- Exec Maven Plugin -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <mainClass>com.codemavriks.aptech.MainApp</mainClass>
            </configuration>
        </plugin>
        
        <!-- Shade Plugin for Fat JAR -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.4.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 10.3 Learning Resources

**Java 21 Documentation:**
- Oracle Java 21 Documentation: https://docs.oracle.com/en/java/javase/21/
- Java 21 New Features: https://openjdk.org/projects/jdk/21/

**JavaFX Resources:**
- JavaFX Documentation: https://openjfx.io/
- JavaFX Tutorials: https://docs.oracle.com/javafx/2/
- Scene Builder: https://gluonhq.com/products/scene-builder/

**Maven Resources:**
- Maven Documentation: https://maven.apache.org/guides/
- Maven Central Repository: https://search.maven.org/

### 10.4 Performance Monitoring

**JVM Monitoring:**
```bash
# Monitor JVM performance
jconsole

# Memory usage
jstat -gc <pid>

# Thread dump
jstack <pid>
```

**Application Profiling:**
- **JProfiler**: Commercial profiler
- **VisualVM**: Free profiling tool
- **Java Flight Recorder**: Built-in profiling

---

## CONCLUSION

This comprehensive setup guide provides all the necessary information to establish a complete development environment for the File Mana - Modern Text Editor project. Following these instructions will ensure:

✅ **Proper Java 21 installation and configuration**  
✅ **Maven setup for dependency management and build automation**  
✅ **IDE configuration for optimal development experience**  
✅ **JavaFX integration for modern UI development**  
✅ **Project import and build verification**  
✅ **Troubleshooting knowledge for common issues**  

### Quick Start Checklist

- [ ] Install Java 21 JDK
- [ ] Configure JAVA_HOME environment variable
- [ ] Install Maven 3.6+
- [ ] Configure MAVEN_HOME and PATH
- [ ] Install and configure IDE (IntelliJ IDEA recommended)
- [ ] Import File Mana project
- [ ] Run `mvn clean compile` to verify setup
- [ ] Run `mvn javafx:run` to start the application

### Support and Resources

For additional help:
- Check the project README.md file
- Review Maven and JavaFX documentation
- Consult IDE-specific help resources
- Use the troubleshooting section for common issues

**Happy Coding!** 🚀

---

**Document Prepared By:** NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH  
**Student ID:** Student1554163  
**Date:** [Current Date]  
**Version:** 1.0  
**Status:** Final 