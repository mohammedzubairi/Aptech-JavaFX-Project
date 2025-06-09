# FILE MANA - DEVELOPER DOCUMENTATION

## Complete Codebase Architecture and Implementation Guide

---

**Project:** File Mana - Modern Text Editor  
**Author:** NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH  
**Student ID:** Student1554163  
**Course:** Advanced Java Programming with JavaFX  
**Institution:** Aptech Computer Education  
**University:** Alnasser University  
**Version:** 1.0  
**Date:** 2025-05-20  

---

## TABLE OF CONTENTS

1. [Architecture Overview](#1-architecture-overview)
2. [Package Structure](#2-package-structure)
3. [Core Components](#3-core-components)
4. [Design Patterns](#4-design-patterns)
5. [Key Algorithms](#5-key-algorithms)
6. [Event Flow](#6-event-flow)
7. [File Management](#7-file-management)
8. [Threading Model](#8-threading-model)
9. [Error Handling](#9-error-handling)
10. [Extension Points](#10-extension-points)

---

## 1. ARCHITECTURE OVERVIEW

### 1.1 High-Level Architecture

File Mana follows a **Model-View-Controller (MVC)** architecture with additional service layers:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │    MainApp      │  │  TextEditor     │  │   SidePanel     │ │
│  │  (Bootstrap)    │  │    (View)       │  │    (View)       │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                   CONTROLLER LAYER                          │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │              EditorController                           │ │
│  │           (Central Coordinator)                         │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                    SERVICE LAYER                            │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │                FileService                              │ │
│  │            (Business Logic)                             │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                     DATA LAYER                              │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │                File System                              │ │
│  │         (Created files/ directory)                      │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 Component Relationships

```
MainApp
├── EditorController (Main Controller)
│   ├── TextEditor (Text editing component)
│   ├── SidePanel (Control panel)
│   │   └── FileNavigator (File tree view)
│   └── FileService (Business logic service)
└── Static utilities (Dialog theming, icon management)
```

---

## 2. PACKAGE STRUCTURE

### 2.1 Source Code Organization

```
src/main/java/com/codemavriks/aptech/
├── MainApp.java                    # Application entry point
├── EditorController.java           # Main MVC controller
├── components/                     # UI components package
│   ├── TextEditor.java            # Text editing component
│   ├── SidePanel.java             # Control panel component
│   └── FileNavigator.java         # File tree component
└── services/                      # Business logic package
    └── FileService.java           # File operations service

src/main/resources/com/codemavriks/aptech/
├── styles/
│   └── modern-theme.css           # Application styling
├── File-Mana-Logo.png             # Application icon (colored)
└── File-Mana-Logo-Black.png       # Title bar icon (black)
```

### 2.2 Package Responsibilities

| Package | Responsibility | Key Classes |
|---------|----------------|-------------|
| **Root** | Application bootstrap and main controller | MainApp, EditorController |
| **components** | UI components and user interaction | TextEditor, SidePanel, FileNavigator |
| **services** | Business logic and file operations | FileService |
| **resources** | Static assets and styling | CSS, images |

---

## 3. CORE COMPONENTS

### 3.1 MainApp.java - Application Bootstrap

**Purpose:** Application entry point and lifecycle management

**Key Responsibilities:**
- JavaFX application initialization
- Primary stage setup and configuration
- CSS theme loading and application
- Icon management (title bar vs taskbar)
- Graceful shutdown with unsaved changes detection
- Static utilities for dialog theming

**Important Methods:**
```java
start(Stage primaryStage)           // Main initialization
loadIcons()                         // Load application icons
applyDarkThemeToDialog(Dialog)      // Static utility for dialog theming
stop()                              // Cleanup on shutdown
```

**Design Patterns:**
- **Singleton** (implicit through JavaFX Application)
- **Factory** (dialog creation utilities)

### 3.2 EditorController.java - Main Controller

**Purpose:** Central coordinator implementing MVC pattern

**Key Responsibilities:**
- Coordinate between UI components and business logic
- Handle all user interactions and events
- Manage application state (current file, unsaved changes)
- Implement auto-save functionality
- Provide error handling and user feedback

**Important Methods:**
```java
handleNewFile(String baseName)      // Create new file set
handleSave()                        // Save current content
handleWordReplacement(int, String)  // Replace word at position
setupAutoSave()                     // Configure auto-save timer
replaceWordAtPosition(...)          // Core word replacement algorithm
```

**Design Patterns:**
- **MVC Controller** (coordinates Model and View)
- **Observer** (event listeners for component communication)
- **Command** (action handlers for user operations)

### 3.3 TextEditor.java - Text Editing Component

**Purpose:** Rich text editing interface with modern features

**Key Responsibilities:**
- Provide text editing capabilities with undo/redo
- Track unsaved changes and notify parent components
- Handle keyboard shortcuts (Ctrl+S, Ctrl+N, Ctrl+F, etc.)
- Implement find/search functionality
- Maintain current file path and content state

**Important Methods:**
```java
setContent(String content)          // Load content and mark as saved
getContent()                        // Get current text content
markAsSaved()                       // Reset unsaved changes flag
showFindDialog()                    // Text search functionality
findAndHighlight(String)            // Highlight search results
```

**Custom Events:**
```java
SaveRequestEvent                    // Fired on Ctrl+S
NewFileRequestEvent                 // Fired on Ctrl+N
```

**Design Patterns:**
- **Observer** (property change notifications)
- **Command** (menu actions and keyboard shortcuts)
- **Composite** (VBox container with child components)

### 3.4 SidePanel.java - Control Panel Component

**Purpose:** Control panel with file operations and status display

**Key Responsibilities:**
- Provide buttons for file operations (New, Save, Replace)
- Handle word replacement input and validation
- Display application status and feedback
- Integrate file navigator for file management
- Coordinate user actions with main controller

**Important Methods:**
```java
updateStatus(String message)        // Update status display
setHasUnsavedChanges(boolean)       // Update save button state
refreshFileNavigator()              // Refresh file tree
notifyNewFileRequested(String)      // Notify listeners of new file request
```

**Design Patterns:**
- **Observer** (listener pattern for event notification)
- **Composite** (VBox container with multiple sections)

### 3.5 FileNavigator.java - File Tree Component

**Purpose:** VSCode-inspired file tree for navigation

**Key Responsibilities:**
- Display hierarchical file structure
- Handle file selection and opening
- Provide context menu for file operations (delete, rename)
- Support drag-and-drop operations
- Refresh automatically when files change

**Important Methods:**
```java
refreshFileTree()                   // Rebuild file tree
createTreeItem(File)                // Create tree node for file/folder
showContextMenu(File)               // Show right-click menu
handleFileOperation(...)            // Process file operations
```

**Design Patterns:**
- **Composite** (tree structure)
- **Observer** (file change notifications)
- **Command** (context menu actions)

### 3.6 FileService.java - Business Logic Service

**Purpose:** Encapsulates all file operations and business logic

**Key Responsibilities:**
- Manage file set creation (org, rev, byte variants)
- Handle content processing (reversal, byte encoding)
- Provide asynchronous file operations
- Implement auto-save functionality
- Manage file system organization

**Important Methods:**
```java
createFileSet(String, String)       // Create new file set
updateFileSet(String, String)       // Update existing file set
convertToByteString(String)         // Convert text to byte representation
deleteFileSet(String)               // Remove complete file set
extractBaseName(String)             // Parse base name from file path
```

**Design Patterns:**
- **Service Layer** (encapsulates business logic)
- **Template Method** (common file operation patterns)
- **Strategy** (different content processing strategies)

---

## 4. DESIGN PATTERNS

### 4.1 Model-View-Controller (MVC)

**Implementation:**
- **Model:** FileService + file system data
- **View:** TextEditor, SidePanel, FileNavigator
- **Controller:** EditorController

**Benefits:**
- Clear separation of concerns
- Testable business logic
- Flexible UI modifications
- Maintainable codebase

### 4.2 Observer Pattern

**Usage:** Component communication without tight coupling

**Examples:**
```java
// Property change listeners
textEditor.hasUnsavedChangesProperty().addListener(...)

// Custom event listeners
sidePanel.addSidePanelListener(new SidePanel.SidePanelListener() {...})

// File service callbacks
fileService.setAutoSaveCallback(new FileService.AutoSaveCallback() {...})
```

### 4.3 Command Pattern

**Usage:** Encapsulating user actions

**Examples:**
```java
// Menu actions
MenuItem saveItem = new MenuItem("Save");
saveItem.setOnAction(e -> handleSave());

// Keyboard shortcuts
case S: fireEvent(new SaveRequestEvent()); break;
```

### 4.4 Service Layer Pattern

**Usage:** Encapsulating business logic

**Implementation:**
```java
public class FileService {
    // Encapsulates all file-related business logic
    // Provides clean API for controllers
    // Handles threading and async operations
}
```

---

## 5. KEY ALGORITHMS

### 5.1 Word Replacement Algorithm

**Purpose:** Replace word at specific position while preserving formatting

**Algorithm:**
```java
private String replaceWordAtPosition(String content, int wordIndex, String replacement) {
    int currentWordCount = 0;
    StringBuilder result = new StringBuilder();
    int i = 0;
    
    while (i < content.length()) {
        // Skip and preserve whitespace (spaces, tabs, newlines)
        while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
            result.append(content.charAt(i));
            i++;
        }
        
        // Extract and process words
        if (i < content.length()) {
            int wordStart = i;
            while (i < content.length() && !Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            
            String currentWord = content.substring(wordStart, i);
            currentWordCount++;
            
            if (currentWordCount == wordIndex) {
                result.append(replacement);  // Replace target word
            } else {
                result.append(currentWord);  // Keep original word
            }
        }
    }
    
    return result.toString();
}
```

**Key Features:**
- Preserves all whitespace (spaces, tabs, newlines)
- 1-based word indexing for user friendliness
- Character-by-character parsing for accuracy
- O(n) time complexity

### 5.2 Content Reversal Algorithm

**Purpose:** Reverse text content character by character

**Algorithm:**
```java
public String reverseContent(String content) {
    if (content == null || content.isEmpty()) {
        return "";
    }
    return new StringBuilder(content).reverse().toString();
}
```

**Features:**
- Uses StringBuilder.reverse() for efficiency
- Null safety with early return
- Unicode character support

### 5.3 Byte Encoding Algorithm

**Purpose:** Convert text to UTF-8 byte representation

**Algorithm:**
```java
private String convertToByteString(String content) {
    if (content == null) return "";
    
    byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
    StringBuilder result = new StringBuilder();
    
    for (int i = 0; i < bytes.length; i++) {
        if (i > 0) result.append(" ");
        result.append(bytes[i] & 0xFF);  // Convert to unsigned
    }
    
    return result.toString();
}
```

**Features:**
- UTF-8 encoding for international character support
- Space-separated format for readability
- Unsigned byte values (0-255 range)

---

## 6. EVENT FLOW

### 6.1 User Action Flow

```
User Action → UI Component → Controller → Service → File System
                    ↓
UI Update ← Status Update ← Controller ← Service Response
```

### 6.2 File Creation Flow

```
1. User clicks "New File" button in SidePanel
2. SidePanel shows input dialog for base name
3. SidePanel notifies EditorController via listener
4. EditorController calls FileService.createFileSet()
5. FileService creates three files (org, rev, byte)
6. EditorController updates TextEditor content
7. UI components refresh to show new file
```

### 6.3 Auto-Save Flow

```
1. Timer triggers every 30 seconds
2. Check if auto-save conditions are met:
   - Auto-save enabled
   - File is open
   - Unsaved changes exist
3. Get current content from TextEditor
4. Call FileService.updateFileSet() in background
5. Update UI on JavaFX Application Thread
6. Show status message and refresh file navigator
```

### 6.4 Word Replacement Flow

```
1. User enters word index and replacement text
2. User clicks "Replace Word" button
3. SidePanel validates input and shows confirmation
4. SidePanel notifies EditorController
5. EditorController gets current content from TextEditor
6. EditorController applies word replacement algorithm
7. EditorController updates all file variants via FileService
8. TextEditor content is updated with new text
9. Status message confirms successful replacement
```

---

## 7. FILE MANAGEMENT

### 7.1 File Organization Strategy

```
Created files/
├── [BaseName1]/
│   ├── [BaseName1]-org.txt     # Original content
│   ├── [BaseName1]-rev.txt     # Character-reversed content
│   └── [BaseName1]-byte.txt    # UTF-8 byte representation
├── [BaseName2]/
│   ├── [BaseName2]-org.txt
│   ├── [BaseName2]-rev.txt
│   └── [BaseName2]-byte.txt
└── [BaseName3]/
    ├── [BaseName3]-org.txt
    ├── [BaseName3]-rev.txt
    └── [BaseName3]-byte.txt
```

### 7.2 File Naming Convention

| Component | Format | Example |
|-----------|--------|---------|
| **Folder** | `[BaseName]/` | `MyProject/` |
| **Original** | `[BaseName]-org.txt` | `MyProject-org.txt` |
| **Reversed** | `[BaseName]-rev.txt` | `MyProject-rev.txt` |
| **Byte Codes** | `[BaseName]-byte.txt` | `MyProject-byte.txt` |

### 7.3 File Synchronization

**Strategy:** All three file variants are always kept in sync

**Implementation:**
- Atomic operations ensure consistency
- Single source of truth (original content)
- Automatic generation of derived content (reversed, byte)
- Error handling prevents partial updates

---

## 8. THREADING MODEL

### 8.1 Thread Usage

| Thread Type | Purpose | Implementation |
|-------------|---------|----------------|
| **JavaFX Application Thread** | UI updates and user interaction | Main thread for all UI operations |
| **Auto-Save Thread** | Background file saving | Single daemon thread via ScheduledExecutorService |
| **File I/O Threads** | Asynchronous file operations | Task-based threading for large operations |

### 8.2 Thread Safety

**Principles:**
- All UI updates on JavaFX Application Thread
- Background operations use Platform.runLater() for UI callbacks
- File operations are atomic to prevent corruption
- Shared state is minimized and properly synchronized

**Example:**
```java
// Background auto-save operation
autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        // Background thread work
        Platform.runLater(() -> {
            // UI updates on JavaFX thread
            sidePanel.updateStatus("Auto-saved");
        });
    }
}, 30000, 30000);
```

---

## 9. ERROR HANDLING

### 9.1 Error Handling Strategy

**Levels:**
1. **Input Validation:** Prevent invalid operations
2. **Service Layer:** Handle business logic errors
3. **File System:** Manage I/O exceptions
4. **UI Layer:** Display user-friendly error messages

### 9.2 Error Types and Handling

| Error Type | Handling Strategy | User Feedback |
|------------|-------------------|---------------|
| **Invalid Input** | Validation with alerts | Warning dialog with guidance |
| **File I/O Errors** | Try-catch with fallback | Error dialog with retry option |
| **Auto-Save Failures** | Graceful degradation | Status message, continue operation |
| **System Errors** | Logging and recovery | Error dialog with contact info |

### 9.3 Error Recovery

**Mechanisms:**
- Auto-save prevents data loss
- Backup creation before destructive operations
- Graceful degradation when possible
- Clear error messages with suggested actions

---

## 10. EXTENSION POINTS

### 10.1 Adding New File Types

**Steps:**
1. Extend FileService with new content processing method
2. Update file creation/update methods to handle new type
3. Add UI controls in SidePanel for new operations
4. Update file naming convention

**Example:**
```java
// In FileService
private String convertToUpperCase(String content) {
    return content.toUpperCase();
}

// In createFileSet method
writeToFile(getFilePath(baseName, "upper"), convertToUpperCase(content));
```

### 10.2 Adding New UI Components

**Steps:**
1. Create new component class in components package
2. Implement Observer pattern for communication
3. Add component to EditorController layout
4. Update event handling in EditorController

### 10.3 Adding New Text Processing Features

**Steps:**
1. Add processing method to FileService
2. Add UI controls to SidePanel
3. Add event handling in EditorController
4. Update word replacement or content processing logic

### 10.4 Customizing Themes

**Steps:**
1. Modify modern-theme.css for styling changes
2. Add new CSS classes for custom components
3. Update component initialization to apply new styles
4. Test across all UI components

---

## CONCLUSION

This documentation provides a comprehensive guide to the File Mana codebase architecture and implementation. The application demonstrates modern software engineering principles including:

- **Clean Architecture** with clear separation of concerns
- **Design Patterns** for maintainable and extensible code
- **Event-Driven Programming** for loose coupling
- **Asynchronous Operations** for responsive UI
- **Comprehensive Error Handling** for robust operation

Future developers can use this documentation to:
- Understand the overall system architecture
- Navigate the codebase efficiently
- Implement new features following established patterns
- Maintain and extend the application effectively

**For additional details, refer to the inline JavaDoc comments in each source file.**

---

**Document Prepared By:** NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH  
**Student ID:** Student1554163  
**Date:** 2025-05-20  
**Version:** 1.0  
**Status:** Complete 