# SOURCE CODE AND COMPILED CODE SUBMISSION GUIDE

---

## FILE MANA - MODERN TEXT EDITOR
### Complete Guide for Source Code and Compiled Code Submission

---

## PROJECT IDENTIFICATION

**Project Title:** File Mana - Modern Text Editor
**Course:** Advanced Java Programming with JavaFX  
**Institution:** Aptech Computer Education  
**University:** Alnasser University  
**Semester:** 2  
**Project Duration:** 20-May-2025 to 10-July-2025  

---

## TABLE OF CONTENTS

1. [Overview](#1-overview)
2. [Understanding Source Code vs Compiled Code](#2-understanding-source-code-vs-compiled-code)
3. [Source Code Preparation](#3-source-code-preparation)
4. [Compiled Code Preparation](#4-compiled-code-preparation)
5. [Submission Formats](#5-submission-formats)
6. [Documentation Requirements](#6-documentation-requirements)
7. [Quality Assurance Checklist](#7-quality-assurance-checklist)
8. [Common Submission Mistakes](#8-common-submission-mistakes)

---

## 1. OVERVIEW

### 1.1 Purpose

This guide explains how to properly prepare and submit both source code and compiled code for the File Mana project as part of your academic eProject requirements. Understanding the difference between these two types of code and how to present them professionally is crucial for academic and professional success.

### 1.2 Academic Requirements

Most academic institutions require both:
- **Source Code**: Human-readable code files that developers write
- **Compiled Code**: Machine-executable files generated from source code

### 1.3 Professional Standards

This guide follows industry best practices for:
- Code organization and structure
- Documentation standards
- Version control practices
- Deployment preparation

---

## 2. UNDERSTANDING SOURCE CODE VS COMPILED CODE

### 2.1 Source Code Definition

**Source Code** is the human-readable text written by programmers using programming languages. It contains:

- **Java Files (.java)**: The actual programming logic
- **Resource Files**: CSS, images, configuration files
- **Build Files**: Maven configuration (pom.xml)
- **Documentation**: README files, comments, documentation

**Example Source Code Structure:**
```
src/
├── main/
│   ├── java/
│   │   └── com/codemavriks/aptech/
│   │       ├── MainApp.java              ← Source Code
│   │       ├── EditorController.java     ← Source Code
│   │       ├── components/
│   │       │   ├── TextEditor.java       ← Source Code
│   │       │   ├── SidePanel.java        ← Source Code
│   │       │   └── FileNavigator.java    ← Source Code
│   │       └── services/
│   │           └── FileService.java      ← Source Code
│   └── resources/
│       └── com/codemavriks/aptech/
│           ├── styles/
│           │   └── modern-theme.css      ← Resource File
│           └── File-Mana-Logo.png        ← Resource File
├── pom.xml                               ← Build Configuration
└── README.md                             ← Documentation
```

### 2.2 Compiled Code Definition

**Compiled Code** is the machine-readable version created from source code. It includes:

- **Class Files (.class)**: Compiled Java bytecode
- **JAR Files (.jar)**: Packaged executable applications
- **Dependencies**: External libraries and frameworks
- **Runtime Resources**: Processed resources ready for execution

**Example Compiled Code Structure:**
```
target/
├── classes/
│   └── com/codemavriks/aptech/
│       ├── MainApp.class                 ← Compiled Code
│       ├── EditorController.class        ← Compiled Code
│       ├── components/
│       │   ├── TextEditor.class          ← Compiled Code
│       │   ├── SidePanel.class           ← Compiled Code
│       │   └── FileNavigator.class       ← Compiled Code
│       └── services/
│           └── FileService.class         ← Compiled Code
├── File-Mana-1.0-SNAPSHOT.jar           ← Executable JAR
└── lib/                                  ← Dependencies
    ├── javafx-controls-22.jar
    ├── javafx-base-22.jar
    └── javafx-graphics-22.jar
```

### 2.3 Key Differences

| Aspect | Source Code | Compiled Code |
|--------|-------------|---------------|
| **Readability** | Human-readable | Machine-readable |
| **File Extension** | .java, .css, .xml | .class, .jar |
| **Purpose** | Development and maintenance | Execution |
| **Size** | Smaller (text files) | Larger (includes dependencies) |
| **Portability** | Platform independent | Platform specific (JVM) |
| **Modification** | Can be edited | Cannot be easily modified |

---

## 3. SOURCE CODE PREPARATION

### 3.1 Organizing Source Code for Submission

**Step 1: Create Source Code Package**

Create a clean directory structure:
```bash
File-Mana-Source-Code/
├── src/                              # Main source directory
├── pom.xml                          # Maven configuration
├── mvnw, mvnw.cmd                   # Maven wrapper
├── README.md                        # Project documentation
├── NAMING_CONVENTION.md             # Coding standards
└── .gitignore                       # Version control exclusions
```

**Step 2: Clean the Project**

Remove generated files and IDE-specific files:
```bash
# Using Maven
mvn clean

# Manual cleanup (remove these directories/files)
target/                              # Compiled output
.idea/                               # IntelliJ IDEA files
.vscode/                             # VS Code files
*.iml                                # IntelliJ module files
.classpath, .project                 # Eclipse files
```

**Step 3: Verify Source Code Quality**

Ensure all source files have:
- **Proper Headers**: Author information, date, purpose
- **Comments**: Explaining complex logic
- **Consistent Formatting**: Proper indentation and spacing
- **No Hardcoded Paths**: Use relative paths and configuration

**Example Java File Header:**
```java
/**
 * File Mana - Modern Text Editor
 * 
 * @author NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
 * @student_id Student1554163
 * @course Advanced Java Programming with JavaFX
 * @institution Aptech Computer Education
 * @university Alnasser University
 * @date [Current Date]
 * @version 1.0
 * 
 * Description: Main application controller handling file operations
 * and user interface coordination for the File Mana text editor.
 */
package com.codemavriks.aptech;

import javafx.application.Application;
// ... rest of the code
```

### 3.2 Source Code Documentation

**Create Source Code README:**

```markdown
# FILE MANA - SOURCE CODE

## Project Information
- **Project**: File Mana - Modern Text Editor
- **Student**: NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
- **Student ID**: Student1554163
- **Course**: Advanced Java Programming with JavaFX

## Source Code Structure
- `src/main/java/` - Java source files
- `src/main/resources/` - Resources (CSS, images)
- `pom.xml` - Maven project configuration
- `README.md` - Project documentation

## How to Build and Run
1. Ensure Java 21 and Maven are installed
2. Run: `mvn clean compile`
3. Run: `mvn javafx:run`

## Dependencies
- Java 21 (LTS)
- JavaFX 22
- Maven 3.6+

## Author
NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
Student ID: Student1554163
Date: [Current Date]
```

### 3.3 Creating Source Code Archive

**Option 1: ZIP Archive**
```bash
# Create ZIP file
zip -r File-Mana-Source-Code.zip File-Mana-Source-Code/

# Exclude unnecessary files
zip -r File-Mana-Source-Code.zip File-Mana-Source-Code/ -x "*.class" "target/*" ".idea/*"
```

**Option 2: TAR Archive**
```bash
# Create TAR.GZ file
tar -czf File-Mana-Source-Code.tar.gz File-Mana-Source-Code/
```

---

## 4. COMPILED CODE PREPARATION

### 4.1 Building Compiled Code

**Step 1: Clean Build**
```bash
# Clean previous builds
mvn clean

# Compile and package
mvn clean package

# Verify build success
ls target/
```

**Step 2: Create Executable JAR**

The Maven build process creates:
- `File-Mana-1.0-SNAPSHOT.jar` - Main application JAR
- `lib/` directory - Dependencies (if using maven-dependency-plugin)

**Step 3: Test Compiled Application**
```bash
# Test the compiled JAR
java -jar target/File-Mana-1.0-SNAPSHOT.jar

# Or using Maven
mvn javafx:run
```

### 4.2 Organizing Compiled Code for Submission

**Create Compiled Code Package:**
```
File-Mana-Compiled-Code/
├── File-Mana.jar                    # Main executable
├── lib/                             # Dependencies
│   ├── javafx-controls-22.jar
│   ├── javafx-base-22.jar
│   └── javafx-graphics-22.jar
├── run.bat                          # Windows run script
├── run.sh                           # Unix/Linux run script
├── README.txt                       # Execution instructions
└── system-requirements.txt          # System requirements
```

**Create Run Scripts:**

**Windows (run.bat):**
```batch
@echo off
echo Starting File Mana - Modern Text Editor
echo.
java -jar File-Mana.jar
if errorlevel 1 (
    echo.
    echo Error: Failed to start application
    echo Please ensure Java 21 or higher is installed
    pause
)
```

**Unix/Linux (run.sh):**
```bash
#!/bin/bash
echo "Starting File Mana - Modern Text Editor"
echo
java -jar File-Mana.jar
if [ $? -ne 0 ]; then
    echo
    echo "Error: Failed to start application"
    echo "Please ensure Java 21 or higher is installed"
    read -p "Press Enter to continue..."
fi
```

### 4.3 Compiled Code Documentation

**Create Execution README:**
```
FILE MANA - COMPILED CODE EXECUTION GUIDE

Project: File Mana - Modern Text Editor
Student: NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
Student ID: Student1554163

SYSTEM REQUIREMENTS:
- Java 21 or higher
- Windows 10/11, macOS 10.14+, or Linux
- 4GB RAM minimum
- 100MB free disk space

EXECUTION INSTRUCTIONS:

Windows:
1. Double-click run.bat
2. Or open Command Prompt and run: java -jar File-Mana.jar

macOS/Linux:
1. Open Terminal
2. Navigate to this directory
3. Run: chmod +x run.sh
4. Run: ./run.sh
5. Or run: java -jar File-Mana.jar

TROUBLESHOOTING:
- If "java command not found": Install Java 21 JDK
- If application doesn't start: Check Java version with "java -version"
- For support: Contact student at provided email

APPLICATION FEATURES:
- Create and edit text files
- Automatic file variant generation (original, reversed, byte)
- Word replacement by position
- Modern dark theme interface
- Auto-save functionality
- File comparison tools

Author: NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
Date: [Current Date]
Version: 1.0
```

---

## 5. SUBMISSION FORMATS

### 5.1 Academic Submission Package

**Complete Submission Structure:**
```
NAJM-ALDEEN-Student1554163-File-Mana-Project/
├── 1-Documentation/
│   ├── Case_Study.pdf
│   ├── Development_Environment_Setup.pdf
│   ├── User_Guide.pdf
│   ├── Developer_Guide.pdf
│   └── Visual_Elements_Descriptions.pdf
├── 2-Source-Code/
│   ├── File-Mana-Source-Code.zip
│   └── Source-Code-README.txt
├── 3-Compiled-Code/
│   ├── File-Mana-Compiled-Code.zip
│   └── Execution-Instructions.txt
├── 4-Additional-Materials/
│   ├── Screenshots/
│   ├── Demo-Video.mp4
│   └── Presentation.pptx
└── PROJECT-SUMMARY.txt
```

### 5.2 Digital Submission Formats

**For Online Submission:**
- **ZIP Format**: Most compatible, smaller size
- **Maximum Size**: Usually 100MB-500MB limit
- **File Naming**: Use student ID and project name
- **Example**: `Student1554163-NAJM-ALDEEN-File-Mana-Project.zip`

**For Physical Submission:**
- **DVD/USB**: Include both source and compiled code
- **Printed Code**: Source code only (if required)
- **Documentation**: Bound report with code appendix

### 5.3 Version Control Submission

**Git Repository Structure:**
```
File-Mana-Project/
├── .git/                            # Git version control
├── src/                             # Source code
├── target/                          # Compiled code (gitignored)
├── docs/                            # Documentation
├── README.md                        # Project overview
├── .gitignore                       # Ignore compiled files
└── CHANGELOG.md                     # Version history
```

**Git Submission Commands:**
```bash
# Initialize repository
git init

# Add all source files
git add src/ pom.xml README.md docs/

# Commit with meaningful message
git commit -m "Final submission: File Mana v1.0 - Complete implementation"

# Tag the submission
git tag -a v1.0 -m "Academic submission version"

# Create archive
git archive --format=zip --output=File-Mana-Submission.zip v1.0
```

---

## 6. DOCUMENTATION REQUIREMENTS

### 6.1 Code Documentation Standards

**Inline Documentation:**
```java
/**
 * Handles word replacement at specified position while preserving formatting
 * 
 * @param content The original text content
 * @param wordIndex The 1-based position of word to replace
 * @param replacement The new word to insert
 * @return Modified content with word replaced
 * @throws IllegalArgumentException if wordIndex is out of range
 * 
 * @author NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
 * @since 1.0
 */
private String replaceWordAtPosition(String content, int wordIndex, String replacement) {
    // Implementation with detailed comments
}
```

**Class Documentation:**
```java
/**
 * Main controller class for File Mana text editor application.
 * 
 * This class coordinates between the UI components and file services,
 * handling user interactions and maintaining application state.
 * 
 * Key responsibilities:
 * - File operations (create, open, save)
 * - Text processing (reverse, byte conversion, word replacement)
 * - UI event handling and updates
 * - Auto-save functionality
 * 
 * @author NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
 * @version 1.0
 * @since 2025-05-20
 */
public class EditorController extends HBox {
    // Class implementation
}
```

### 6.2 External Documentation

**Technical Documentation:**
- **API Documentation**: Generated using JavaDoc
- **Architecture Diagrams**: System design and component interaction
- **Database Schema**: If applicable
- **Deployment Guide**: Installation and configuration

**User Documentation:**
- **User Manual**: Step-by-step usage instructions
- **Installation Guide**: System requirements and setup
- **Troubleshooting**: Common issues and solutions
- **FAQ**: Frequently asked questions

### 6.3 Academic Documentation

**Project Report Sections:**
1. **Abstract**: Project summary and achievements
2. **Introduction**: Problem statement and objectives
3. **Literature Review**: Related work and technologies
4. **Methodology**: Development approach and tools
5. **Implementation**: Technical details and code explanation
6. **Testing**: Test cases and results
7. **Results**: Project outcomes and evaluation
8. **Conclusion**: Summary and future work
9. **References**: Citations and sources
10. **Appendices**: Source code listings and additional materials

---

## 7. QUALITY ASSURANCE CHECKLIST

### 7.1 Source Code Quality Checklist

**Code Structure:**
- [ ] Proper package structure and naming conventions
- [ ] Consistent indentation and formatting
- [ ] Meaningful variable and method names
- [ ] No hardcoded values or paths
- [ ] Proper error handling and validation

**Documentation:**
- [ ] File headers with author and project information
- [ ] Class and method documentation (JavaDoc)
- [ ] Inline comments for complex logic
- [ ] README files with setup instructions
- [ ] Code examples and usage documentation

**Functionality:**
- [ ] All requirements implemented
- [ ] Code compiles without errors or warnings
- [ ] Application runs successfully
- [ ] All features work as expected
- [ ] Error handling works properly

### 7.2 Compiled Code Quality Checklist

**Build Process:**
- [ ] Clean build completes successfully
- [ ] No compilation errors or warnings
- [ ] All dependencies included
- [ ] JAR file created successfully
- [ ] Application starts and runs properly

**Packaging:**
- [ ] Executable JAR file included
- [ ] All required dependencies packaged
- [ ] Run scripts for different platforms
- [ ] Clear execution instructions
- [ ] System requirements documented

**Testing:**
- [ ] Application tested on target platforms
- [ ] All features work in compiled version
- [ ] Performance meets requirements
- [ ] Memory usage within acceptable limits
- [ ] No runtime errors or crashes

### 7.3 Submission Quality Checklist

**File Organization:**
- [ ] Clear directory structure
- [ ] Proper file naming conventions
- [ ] No unnecessary files included
- [ ] Archive files created properly
- [ ] File sizes within submission limits

**Documentation:**
- [ ] All required documents included
- [ ] Documents properly formatted
- [ ] Student information on all files
- [ ] Version numbers and dates current
- [ ] Professional presentation quality

**Completeness:**
- [ ] Source code package complete
- [ ] Compiled code package complete
- [ ] All documentation included
- [ ] Screenshots and demos included
- [ ] Contact information provided

---

## 8. COMMON SUBMISSION MISTAKES

### 8.1 Source Code Mistakes

**Common Errors:**
1. **Including Compiled Files**: Submitting .class files with source code
2. **Missing Dependencies**: Not including pom.xml or build files
3. **Hardcoded Paths**: Using absolute paths that won't work on other systems
4. **No Documentation**: Missing comments and README files
5. **IDE-Specific Files**: Including .idea/, .vscode/ directories

**How to Avoid:**
- Use `mvn clean` before packaging
- Test source code on different machine
- Use relative paths and configuration files
- Include comprehensive documentation
- Use .gitignore to exclude IDE files

### 8.2 Compiled Code Mistakes

**Common Errors:**
1. **Missing Dependencies**: JAR won't run due to missing libraries
2. **Wrong Java Version**: Compiled for different Java version
3. **No Execution Instructions**: Users can't run the application
4. **Platform-Specific Code**: Won't run on different operating systems
5. **Large File Sizes**: Including unnecessary dependencies

**How to Avoid:**
- Use Maven shade plugin for fat JAR
- Specify Java version in build configuration
- Include clear run scripts and instructions
- Test on multiple platforms
- Optimize dependencies and exclude unused libraries

### 8.3 Documentation Mistakes

**Common Errors:**
1. **Outdated Information**: Documentation doesn't match current code
2. **Missing Student Information**: No author or student ID
3. **Poor Formatting**: Inconsistent or unprofessional appearance
4. **Incomplete Instructions**: Users can't follow setup/run procedures
5. **No Version Control**: Can't track changes or submissions

**How to Avoid:**
- Update documentation with code changes
- Include student information in all files
- Use consistent formatting and templates
- Test instructions on fresh system
- Use version control and tagging

---

## CONCLUSION

### Summary

Proper preparation of source code and compiled code for academic submission requires:

1. **Understanding the Difference**: Know what source code and compiled code are
2. **Proper Organization**: Structure files logically and professionally
3. **Quality Documentation**: Include comprehensive instructions and comments
4. **Thorough Testing**: Verify everything works on different systems
5. **Professional Presentation**: Follow academic and industry standards

### Best Practices

**For Source Code:**
- Keep it clean, documented, and well-organized
- Include all necessary build files and dependencies
- Test compilation on different systems
- Provide clear setup and build instructions

**For Compiled Code:**
- Ensure it runs on target platforms
- Include all required dependencies
- Provide simple execution methods
- Document system requirements clearly

**For Submission:**
- Follow institutional guidelines exactly
- Use professional naming conventions
- Include all required documentation
- Test the complete submission package

### Final Recommendations

1. **Start Early**: Don't wait until the last minute to prepare submissions
2. **Test Everything**: Verify all code works on different systems
3. **Document Thoroughly**: Include comprehensive documentation
4. **Follow Standards**: Use professional coding and documentation standards
5. **Get Feedback**: Have others review your submission before final submission

**Success in academic projects comes from attention to detail, professional presentation, and thorough testing!**