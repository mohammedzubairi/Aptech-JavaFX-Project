# DEVELOPER GUIDE

---

## FILE MANA - MODERN TEXT EDITOR
### Complete Developer Documentation and Module Descriptions

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

1. [Introduction](#1-introduction)
2. [Development Environment Setup](#2-development-environment-setup)
3. [Project Structure](#3-project-structure)
4. [Architecture Overview](#4-architecture-overview)
5. [Module Descriptions](#5-module-descriptions)
6. [API Documentation](#6-api-documentation)
7. [Database/File Structure](#7-databasefile-structure)
8. [Build and Deployment](#8-build-and-deployment)
9. [Testing Framework](#9-testing-framework)
10. [Extending the Application](#10-extending-the-application)

---

## 1. INTRODUCTION

### 1.1 Purpose of This Guide

This Developer Guide provides comprehensive technical documentation for the File Mana application, enabling developers to understand, maintain, and extend the codebase. It includes detailed module descriptions, API documentation, and architectural insights.

### 1.2 Target Audience

- **Developers**: Looking to understand or modify the codebase
- **Maintainers**: Responsible for ongoing application support
- **Contributors**: Planning to add new features or fix bugs
- **Students**: Learning from the implementation patterns and techniques

### 1.3 Technology Stack

| Component | Technology | Version | Purpose |
|-----------|------------|---------|---------|
| **Language** | Java | 21 | Core application development |
| **UI Framework** | JavaFX | 22 | Desktop user interface |
| **Build Tool** | Maven | 3.6+ | Dependency management and build |
| **Styling** | CSS | 3 | UI theming and responsive design |
| **Testing** | JUnit | 5.8+ | Unit and integration testing |

### 1.4 Design Principles

- **Separation of Concerns**: Clear distinction between UI, business logic, and data access
- **Single Responsibility**: Each class has one primary responsibility
- **Open/Closed Principle**: Open for extension, closed for modification
- **Dependency Injection**: Loose coupling between components
- **Observer Pattern**: Event-driven communication

---

## 2. DEVELOPMENT ENVIRONMENT SETUP

### 2.1 Prerequisites

**Required Software:**
```bash
# Java Development Kit 21
java -version
javac -version

# Maven 3.6+
mvn -version

# Git (for version control)
git --version
```

**Recommended IDEs:**
- **IntelliJ IDEA** (Community or Ultimate)
- **Eclipse IDE** with JavaFX plugin
- **Visual Studio Code** with Java extensions

### 2.2 Project Setup

**Clone and Build:**
```bash
# Clone the repository
git clone <repository-url>
cd Aptech-JavaFX-Project

# Build the project
mvn clean compile

# Run tests
mvn test

# Package application
mvn package
```

**IDE Configuration:**
```xml
<!-- Maven dependencies (pom.xml) -->
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

### 2.3 Running the Application

**Development Mode:**
```bash
# Run from Maven
mvn javafx:run

# Run from IDE
# Set main class: com.codemavriks.aptech.MainApp
# Add VM options: --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
```

**Production Mode:**
```bash
# Run packaged JAR
java -jar target/FileMana.jar
```

---

## 3. PROJECT STRUCTURE

### 3.1 Directory Layout

```
Aptech-JavaFX-Project/
├── src/main/java/com/codemavriks/aptech/
│   ├── MainApp.java                    # Application entry point
│   ├── EditorController.java           # Main MVC controller
│   ├── components/                     # UI components
│   │   ├── TextEditor.java            # Text editing component
│   │   ├── SidePanel.java             # Sidebar component
│   │   └── FileNavigator.java         # File tree navigator
│   └── services/                       # Business logic services
│       └── FileService.java           # File operations service
├── src/main/resources/com/codemavriks/aptech/
│   ├── styles/
│   │   └── modern-theme.css           # Application styling
│   └── File-Mana-Logo.png             # Application icon
├── src/test/java/                      # Test classes
├── target/                             # Compiled output
├── pom.xml                             # Maven configuration
└── README.md                           # Project documentation
```

### 3.2 Package Organization

**com.codemavriks.aptech**
- Main application package containing entry point and controller

**com.codemavriks.aptech.components**
- Reusable UI components and custom controls

**com.codemavriks.aptech.services**
- Business logic and service layer classes

**com.codemavriks.aptech.styles**
- CSS styling and theme resources

### 3.3 Naming Conventions

**Classes:**
- PascalCase (e.g., `FileService`, `TextEditor`)
- Descriptive names indicating purpose
- Suffix indicates type (Controller, Service, Component)

**Methods:**
- camelCase (e.g., `createFileSet`, `updateContent`)
- Verb-noun pattern for actions
- Boolean methods start with `is`, `has`, `can`

**Variables:**
- camelCase (e.g., `currentFileBaseName`, `autoSaveTimer`)
- Descriptive names avoiding abbreviations
- Constants in UPPER_SNAKE_CASE

---

## 4. ARCHITECTURE OVERVIEW

### 4.1 Architectural Pattern

**Model-View-Controller (MVC)**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      VIEW       │    │   CONTROLLER    │    │      MODEL      │
│   (JavaFX UI)   │◄──►│ EditorController│◄──►│  FileService    │
│   Components    │    │                 │    │  Data Objects   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

**Component Architecture**
```
┌─────────────────────────────────────────────────────────────┐
│                    MainApp (Entry Point)                   │
├─────────────────────────────────────────────────────────────┤
│                 EditorController (Main Controller)         │
├─────────────────────────────────────────────────────────────┤
│  TextEditor  │  SidePanel   │  FileNavigator  │ Components │
├─────────────────────────────────────────────────────────────┤
│              FileService (Business Logic)                  │
├─────────────────────────────────────────────────────────────┤
│                File System (Data Layer)                    │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Design Patterns Used

**Observer Pattern**
- Event-driven communication between components
- Property change listeners for UI updates
- File change notifications

**Service Layer Pattern**
- Centralized business logic in service classes
- Separation of concerns between UI and business logic
- Reusable service methods

**Component Pattern**
- Modular UI components with clear interfaces
- Encapsulated functionality and state
- Reusable across different contexts

---

## 5. MODULE DESCRIPTIONS

### 5.1 MainApp Module

**File:** `src/main/java/com/codemavriks/aptech/MainApp.java`

**Purpose:** Application entry point and window management

**Key Responsibilities:**
- JavaFX application initialization
- Primary stage setup and configuration
- Icon loading and window properties
- Application lifecycle management

**Code Structure:**
```java
public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML and create scene
        // Set up window properties
        // Load application icons
        // Show primary stage
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
```

**Key Methods:**
- `start(Stage primaryStage)`: Main application startup
- `loadIcons()`: Loads application icons for title bar and taskbar
- `setStageIcons(Stage stage)`: Applies icons to stage

**Dependencies:**
- JavaFX Application class
- EditorController for main UI
- Resource loading utilities

### 5.2 EditorController Module

**File:** `src/main/java/com/codemavriks/aptech/EditorController.java`

**Purpose:** Main MVC controller coordinating all application functionality

**Key Responsibilities:**
- UI component initialization and coordination
- Event handling and user interaction management
- Business logic coordination through services
- State management and data binding

**Code Structure:**
```java
@Controller
public class EditorController {
    // FXML injected components
    @FXML private TextEditor textEditor;
    @FXML private SidePanel sidePanel;
    @FXML private FileNavigator fileNavigator;
    
    // Service dependencies
    private FileService fileService;
    
    // Initialization and event handlers
    public void initialize() { /* Setup logic */ }
    
    // Event handler methods
    @FXML private void handleCreateFiles() { /* File creation */ }
    @FXML private void handleReplaceWord() { /* Word replacement */ }
}
```

**Key Methods:**
- `initialize()`: Component initialization and setup
- `handleCreateFiles()`: File creation event handler
- `handleReplaceWord()`: Word replacement event handler
- `updateFileNavigator()`: Refreshes file tree display
- `setupAutoSave()`: Configures automatic saving

**Event Handling:**
- Button click events for file operations
- Text change events for auto-save triggering
- File selection events from navigator
- Keyboard shortcut handling

### 5.3 TextEditor Component

**File:** `src/main/java/com/codemavriks/aptech/components/TextEditor.java`

**Purpose:** Full-featured text editing component with advanced capabilities

**Key Responsibilities:**
- Text content display and editing
- Syntax highlighting and formatting
- Undo/redo functionality
- Find and replace operations
- Content change notifications

**Code Structure:**
```java
public class TextEditor extends TextArea {
    private UndoManager undoManager;
    private FindReplaceDialog findDialog;
    
    public TextEditor() {
        setupEditor();
        setupEventHandlers();
        setupKeyboardShortcuts();
    }
    
    private void setupEditor() {
        // Configure text area properties
        // Set up styling and appearance
        // Initialize undo/redo system
    }
}
```

**Key Features:**
- **Syntax Highlighting**: Color-coded text for better readability
- **Line Numbers**: Optional line numbering for navigation
- **Word Wrap**: Automatic line wrapping for long text
- **Undo/Redo**: Full undo/redo stack with Ctrl+Z/Ctrl+Y
- **Find/Replace**: Integrated search and replace functionality

**API Methods:**
```java
// Content management
public void setContent(String content)
public String getContent()
public void appendContent(String content)

// Editing operations
public void undo()
public void redo()
public boolean canUndo()
public boolean canRedo()

// Search functionality
public void showFindDialog()
public void findNext(String searchText)
public void replaceAll(String searchText, String replacement)
```

### 5.4 SidePanel Component

**File:** `src/main/java/com/codemavriks/aptech/components/SidePanel.java`

**Purpose:** Sidebar component containing file creation and word replacement controls

**Key Responsibilities:**
- File creation form management
- Word replacement input handling
- Status information display
- User input validation

**Code Structure:**
```java
public class SidePanel extends VBox {
    // UI components
    private TextField baseNameField;
    private TextArea contentArea;
    private TextField positionField;
    private TextField replacementField;
    private Label statusLabel;
    
    public SidePanel() {
        setupLayout();
        setupEventHandlers();
        setupValidation();
    }
}
```

**Form Sections:**
1. **File Creation Section**
   - Base name input field
   - Content text area
   - Create Files button
   - Clear form button

2. **Word Replacement Section**
   - Position input field (numeric)
   - Replacement text field
   - Replace Word button
   - Clear fields button

3. **Status Section**
   - Current operation status
   - File count display
   - Auto-save indicator

**Validation Logic:**
```java
// Input validation methods
private boolean validateBaseName(String baseName)
private boolean validatePosition(String position)
private boolean validateContent(String content)

// Error handling
private void showValidationError(String message)
private void clearValidationErrors()
```

### 5.5 FileNavigator Component

**File:** `src/main/java/com/codemavriks/aptech/components/FileNavigator.java`

**Purpose:** VSCode-like file tree navigator with context menu operations

**Key Responsibilities:**
- Hierarchical file structure display
- File and folder navigation
- Context menu operations
- File selection and opening

**Code Structure:**
```java
public class FileNavigator extends TreeView<File> {
    private ContextMenu contextMenu;
    private TreeItem<File> rootItem;
    
    public FileNavigator() {
        setupTreeView();
        setupContextMenu();
        setupEventHandlers();
    }
    
    private void buildFileTree() {
        // Recursively build tree structure
        // Add file and folder icons
        // Set up selection handling
    }
}
```

**Tree Structure:**
```
📁 Created files/
├── 📁 Ahmed/
│   ├── 📄 Ahmed-org.txt
│   ├── 📄 Ahmed-rev.txt
│   └── 📄 Ahmed-byte.txt
├── 📁 Document/
│   ├── 📄 Document-org.txt
│   ├── 📄 Document-rev.txt
│   └── 📄 Document-byte.txt
└── ...
```

**Context Menu Operations:**
- Open file in editor
- Delete file or folder
- Rename file or folder
- Refresh tree view
- Show file properties

### 5.6 FileService Module

**File:** `src/main/java/com/codemavriks/aptech/services/FileService.java`

**Purpose:** Centralized file operations and business logic service

**Key Responsibilities:**
- File creation and management
- Content processing (reversal, byte conversion)
- Auto-save functionality
- File system operations

**Code Structure:**
```java
@Service
public class FileService {
    private static final String BASE_DIRECTORY = "Created files";
    private Timer autoSaveTimer;
    private String currentFileBaseName;
    private String lastSavedContent;
    
    // Core file operations
    public void createFileSet(String baseName, String content)
    public void updateFileSet(String baseName, String content)
    public String loadFileContent(String filePath)
    
    // Content processing
    public String reverseContent(String content)
    public String convertToByteString(String content)
    public String replaceWordAtPosition(String content, int position, String replacement)
}
```

**File Operations API:**
```java
// File set management
public void createFileSet(String baseName, String content) throws IOException
public void updateFileSet(String baseName, String content) throws IOException
public boolean fileSetExists(String baseName)
public void deleteFileSet(String baseName) throws IOException

// Content processing
public String reverseContent(String content)
public String convertToByteString(String content)
public String replaceWordAtPosition(String content, int position, String replacement)

// Auto-save functionality
public void enableAutoSave(Runnable onSave, Runnable onError)
public void disableAutoSave()
public boolean isAutoSaveEnabled()
```

**Auto-Save Implementation:**
```java
private void setupAutoSave() {
    autoSaveTimer = new Timer(true);
    autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            if (hasContentChanged()) {
                try {
                    saveCurrentContent();
                    Platform.runLater(() -> notifyAutoSaveSuccess());
                } catch (IOException e) {
                    Platform.runLater(() -> notifyAutoSaveError(e));
                }
            }
        }
    }, AUTO_SAVE_INTERVAL, AUTO_SAVE_INTERVAL);
}
```

---

## 6. API DOCUMENTATION

### 6.1 Core APIs

**FileService API**
```java
public class FileService {
    /**
     * Creates a new file set with three synchronized files
     * @param baseName The base name for the file set
     * @param content The initial content
     * @throws IOException If file creation fails
     */
    public void createFileSet(String baseName, String content) throws IOException
    
    /**
     * Updates an existing file set with new content
     * @param baseName The base name of the file set
     * @param content The new content
     * @throws IOException If file update fails
     */
    public void updateFileSet(String baseName, String content) throws IOException
    
    /**
     * Reverses the character order of the input string
     * @param content The string to reverse
     * @return The reversed string
     */
    public String reverseContent(String content)
    
    /**
     * Converts text to UTF-8 byte representation
     * @param content The text to convert
     * @return Space-separated byte values
     */
    public String convertToByteString(String content)
}
```

**TextEditor API**
```java
public class TextEditor extends TextArea {
    /**
     * Sets the content of the text editor
     * @param content The text content to display
     */
    public void setContent(String content)
    
    /**
     * Gets the current content of the text editor
     * @return The current text content
     */
    public String getContent()
    
    /**
     * Undoes the last editing operation
     */
    public void undo()
    
    /**
     * Redoes the last undone operation
     */
    public void redo()
}
```

### 6.2 Event System

**Event Types:**
```java
// File operation events
public class FileCreatedEvent extends Event
public class FileUpdatedEvent extends Event
public class FileDeletedEvent extends Event

// Content change events
public class ContentChangedEvent extends Event
public class WordReplacedEvent extends Event

// Auto-save events
public class AutoSaveSuccessEvent extends Event
public class AutoSaveErrorEvent extends Event
```

**Event Handling:**
```java
// Register event listeners
fileService.addFileOperationListener(event -> {
    // Handle file operation events
});

textEditor.addContentChangeListener(event -> {
    // Handle content changes
});
```

### 6.3 Configuration API

**Application Configuration:**
```java
public class AppConfig {
    // Auto-save settings
    public static final int AUTO_SAVE_INTERVAL = 30000; // 30 seconds
    public static final boolean AUTO_SAVE_ENABLED = true;
    
    // File settings
    public static final String BASE_DIRECTORY = "Created files";
    public static final String FILE_ENCODING = "UTF-8";
    
    // UI settings
    public static final double SIDEBAR_WIDTH_RATIO = 0.3;
    public static final double EDITOR_WIDTH_RATIO = 0.7;
}
```

---

## 7. DATABASE/FILE STRUCTURE

### 7.1 File System Organization

**Directory Structure:**
```
Application Root/
├── Created files/                    # Main data directory
│   ├── [BaseName1]/                 # File set folder
│   │   ├── [BaseName1]-org.txt      # Original content
│   │   ├── [BaseName1]-rev.txt      # Reversed content
│   │   └── [BaseName1]-byte.txt     # Byte representation
│   ├── [BaseName2]/
│   │   ├── [BaseName2]-org.txt
│   │   ├── [BaseName2]-rev.txt
│   │   └── [BaseName2]-byte.txt
│   └── ...
├── FileMana.jar                     # Application executable
└── logs/                            # Application logs (if enabled)
```

### 7.2 File Naming Convention

**Pattern:** `[BaseName]-[Type].txt`

**File Types:**
- `org`: Original content file
- `rev`: Reversed content file  
- `byte`: Byte codes file

**Naming Rules:**
- Base names are sanitized to remove special characters
- Duplicate names get automatic suffixes (_1, _2, etc.)
- All files use UTF-8 encoding
- Extensions are always `.txt`

### 7.3 Data Persistence

**File Content Format:**
```
Original File (basename-org.txt):
Plain text content as entered by user

Reversed File (basename-rev.txt):
Character-reversed version of original content

Byte File (basename-byte.txt):
Space-separated UTF-8 byte values (0-255)
Example: "72 101 108 108 111" for "Hello"
```

**Metadata Storage:**
- No separate metadata files
- File system timestamps used for modification tracking
- File sizes calculated dynamically
- Content integrity verified through comparison

---

## 8. BUILD AND DEPLOYMENT

### 8.1 Maven Build Configuration

**pom.xml Structure:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.codemavriks</groupId>
    <artifactId>file-mana</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <javafx.version>22</javafx.version>
        <junit.version>5.8.2</junit.version>
    </properties>
    
    <dependencies>
        <!-- JavaFX dependencies -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        
        <!-- Testing dependencies -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### 8.2 Build Commands

**Development Build:**
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package without tests
mvn package -DskipTests

# Run application
mvn javafx:run
```

**Production Build:**
```bash
# Full build with tests
mvn clean package

# Create executable JAR
mvn clean package shade:shade

# Generate documentation
mvn javadoc:javadoc
```

### 8.3 Deployment Options

**Standalone JAR:**
```bash
# Create fat JAR with dependencies
mvn clean package

# Run standalone
java -jar target/FileMana.jar
```

**Platform-Specific Packages:**
```bash
# Windows installer
jpackage --input target/ --name FileMana --main-jar FileMana.jar --type msi

# macOS app bundle
jpackage --input target/ --name FileMana --main-jar FileMana.jar --type dmg

# Linux package
jpackage --input target/ --name FileMana --main-jar FileMana.jar --type deb
```

---

## 9. TESTING FRAMEWORK

### 9.1 Test Structure

**Test Organization:**
```
src/test/java/com/codemavriks/aptech/
├── services/
│   └── FileServiceTest.java         # Service layer tests
├── components/
│   ├── TextEditorTest.java         # Component tests
│   └── SidePanelTest.java
├── integration/
│   └── FileOperationIntegrationTest.java
└── utils/
    └── TestUtils.java              # Test utilities
```

### 9.2 Unit Testing

**FileService Tests:**
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileServiceTest {
    private FileService fileService;
    private Path testDirectory;
    
    @BeforeAll
    void setUp() throws IOException {
        testDirectory = Files.createTempDirectory("file-mana-test");
        fileService = new FileService(testDirectory.toString());
    }
    
    @Test
    void testCreateFileSet() throws IOException {
        String baseName = "TestFile";
        String content = "Hello World";
        
        fileService.createFileSet(baseName, content);
        
        assertTrue(fileService.fileSetExists(baseName));
        assertEquals(content, fileService.loadFileContent(baseName + "-org.txt"));
        assertEquals("dlroW olleH", fileService.loadFileContent(baseName + "-rev.txt"));
    }
    
    @Test
    void testContentReversal() {
        String input = "Hello World";
        String expected = "dlroW olleH";
        String actual = fileService.reverseContent(input);
        
        assertEquals(expected, actual);
    }
    
    @Test
    void testByteConversion() {
        String input = "Hi";
        String expected = "72 105";
        String actual = fileService.convertToByteString(input);
        
        assertEquals(expected, actual);
    }
}
```

### 9.3 Integration Testing

**File Operation Integration:**
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileOperationIntegrationTest {
    private EditorController controller;
    private FileService fileService;
    
    @Test
    void testCompleteFileWorkflow() {
        // Create file set
        controller.createFileSet("IntegrationTest", "Sample content");
        
        // Verify files created
        assertTrue(fileService.fileSetExists("IntegrationTest"));
        
        // Update content
        controller.updateContent("Updated content");
        
        // Verify synchronization
        assertEquals("Updated content", 
            fileService.loadFileContent("IntegrationTest-org.txt"));
        assertEquals("tnetnoc detatdpU", 
            fileService.loadFileContent("IntegrationTest-rev.txt"));
    }
}
```

### 9.4 Test Utilities

**TestUtils Class:**
```java
public class TestUtils {
    public static Path createTempDirectory() throws IOException {
        return Files.createTempDirectory("file-mana-test");
    }
    
    public static void cleanupDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
    
    public static String generateRandomContent(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
```

---

## 10. EXTENDING THE APPLICATION

### 10.1 Adding New Features

**Step 1: Define Requirements**
- Identify the new functionality needed
- Determine impact on existing modules
- Plan integration points

**Step 2: Design Implementation**
- Choose appropriate design patterns
- Define new classes and interfaces
- Plan data flow and dependencies

**Step 3: Implement Feature**
- Create new modules following existing patterns
- Update existing modules as needed
- Add comprehensive tests

**Example: Adding File Export Feature**
```java
// New service method
public class FileService {
    public void exportFileSet(String baseName, String format) throws IOException {
        switch (format.toLowerCase()) {
            case "pdf":
                exportToPdf(baseName);
                break;
            case "html":
                exportToHtml(baseName);
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}

// UI integration
public class SidePanel {
    private void addExportSection() {
        ComboBox<String> formatCombo = new ComboBox<>();
        formatCombo.getItems().addAll("PDF", "HTML", "TXT");
        
        Button exportButton = new Button("Export");
        exportButton.setOnAction(e -> handleExport());
    }
}
```

### 10.2 Customizing UI Components

**Creating Custom Components:**
```java
public class CustomTextEditor extends TextEditor {
    private LineNumberFactory lineNumbers;
    
    public CustomTextEditor() {
        super();
        setupLineNumbers();
        setupCustomStyling();
    }
    
    private void setupLineNumbers() {
        lineNumbers = new LineNumberFactory(this);
        setParagraphGraphicFactory(lineNumbers);
    }
}
```

**Extending Existing Components:**
```java
public class EnhancedSidePanel extends SidePanel {
    private TabPane tabPane;
    
    public EnhancedSidePanel() {
        super();
        convertToTabbedInterface();
    }
    
    private void convertToTabbedInterface() {
        tabPane = new TabPane();
        tabPane.getTabs().addAll(
            new Tab("Files", createFileSection()),
            new Tab("Replace", createReplaceSection()),
            new Tab("Settings", createSettingsSection())
        );
    }
}
```

### 10.3 Plugin Architecture

**Plugin Interface:**
```java
public interface FileManPlugin {
    String getName();
    String getVersion();
    void initialize(PluginContext context);
    void shutdown();
    List<MenuItem> getMenuItems();
}

public class PluginContext {
    private FileService fileService;
    private EditorController controller;
    
    // Provide access to core functionality
    public FileService getFileService() { return fileService; }
    public EditorController getController() { return controller; }
}
```

**Plugin Manager:**
```java
public class PluginManager {
    private List<FileManPlugin> plugins = new ArrayList<>();
    
    public void loadPlugin(String pluginPath) throws Exception {
        // Load plugin JAR
        // Instantiate plugin class
        // Initialize plugin
        // Register with application
    }
    
    public void unloadPlugin(String pluginName) {
        // Find plugin
        // Shutdown plugin
        // Remove from registry
    }
}
```

### 10.4 Performance Optimization

**Memory Optimization:**
```java
// Lazy loading for large files
public class LazyFileLoader {
    private WeakReference<String> contentCache;
    
    public String getContent() {
        String content = contentCache != null ? contentCache.get() : null;
        if (content == null) {
            content = loadFromFile();
            contentCache = new WeakReference<>(content);
        }
        return content;
    }
}

// Asynchronous operations
public class AsyncFileService {
    private ExecutorService executor = Executors.newCachedThreadPool();
    
    public CompletableFuture<Void> createFileSetAsync(String baseName, String content) {
        return CompletableFuture.runAsync(() -> {
            try {
                createFileSet(baseName, content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
```

---

## CONCLUSION

This Developer Guide provides comprehensive documentation for understanding, maintaining, and extending the File Mana application. The modular architecture, clear separation of concerns, and well-defined APIs make it easy for developers to work with the codebase.

**Key Development Principles:**
- **Maintainable Code**: Clear structure and comprehensive documentation
- **Extensible Design**: Plugin-ready architecture for future enhancements
- **Testable Components**: Comprehensive test coverage and utilities
- **Performance Conscious**: Optimized for responsiveness and efficiency

**Next Steps for Developers:**
- **Study the Modules**: Understand each component's role and responsibilities
- **Run the Tests**: Verify your development environment setup
- **Experiment**: Try adding small features to understand the architecture
- **Contribute**: Follow the established patterns when adding new functionality

---

**Document Prepared By:** NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH  
**Student ID:** Student1554163  
**Date:** [Current Date]  
**Version:** 1.0  
**Status:** Final 