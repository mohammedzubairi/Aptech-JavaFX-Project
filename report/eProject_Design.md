# EPROJECT DESIGN

---

## FILE MANA - MODERN TEXT EDITOR
### System Design and Architecture Documentation

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

## 1. SYSTEM ARCHITECTURE DESIGN

### 1.1 High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    FILE MANA APPLICATION                       │
├─────────────────────────────────────────────────────────────────┤
│  PRESENTATION LAYER (JavaFX UI)                                │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐              │
│  │   MainApp   │ │ EditorCtrl  │ │ Components  │              │
│  │   (Entry)   │ │ (MVC Ctrl)  │ │ (UI Parts)  │              │
│  └─────────────┘ └─────────────┘ └─────────────┘              │
├─────────────────────────────────────────────────────────────────┤
│  BUSINESS LOGIC LAYER (Services)                               │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐              │
│  │ FileService │ │ContentProc. │ │Comparison   │              │
│  │ (File Ops)  │ │(Text Proc.) │ │Service      │              │
│  └─────────────┘ └─────────────┘ └─────────────┘              │
├─────────────────────────────────────────────────────────────────┤
│  DATA ACCESS LAYER (File System)                               │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐              │
│  │FileManager  │ │ConfigMgr    │ │BackupMgr    │              │
│  │(I/O Ops)    │ │(Settings)   │ │(Recovery)   │              │
│  └─────────────┘ └─────────────┘ └─────────────┘              │
└─────────────────────────────────────────────────────────────────┘
```

### 1.2 Component Architecture

**Core Components:**
- **MainApp.java**: Application entry point and window management
- **EditorController.java**: Main controller implementing MVC pattern
- **TextEditor.java**: Full-featured text editing component
- **SidePanel.java**: Control panel with file operations
- **FileNavigator.java**: VSCode-like file tree navigator
- **FileService.java**: Centralized file operations and auto-save

**Design Patterns Used:**
- **Model-View-Controller (MVC)**: Separation of concerns
- **Observer Pattern**: Event-driven communication
- **Service Layer Pattern**: Business logic encapsulation
- **Component Pattern**: Reusable UI components
- **Singleton Pattern**: Configuration and resource management

---

## 2. DATA FLOW DIAGRAMS (DFDs)

### 2.1 Context Diagram (Level 0 DFD)

```
                    ┌─────────────────┐
                    │      USER       │
                    └─────────────────┘
                             │
                    ┌────────▼────────┐
                    │   File Content  │
                    │   Base Name     │
                    │   Position      │
                    │   Replacement   │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │   FILE MANA     │
                    │   APPLICATION   │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │  Original File  │
                    │  Reversed File  │
                    │  Byte File      │
                    │  Status Updates │
                    └─────────────────┘
                             │
                    ┌────────▼────────┐
                    │  FILE SYSTEM    │
                    └─────────────────┘
```

### 2.2 Level 1 DFD - Main Processes

```
┌─────────┐    File Content    ┌─────────────────┐
│  USER   │ ──────────────────▶│  1. CREATE      │
└─────────┘                    │     FILES       │
     │                         └─────────────────┘
     │                                  │
     │ Base Name                        │ Original Content
     │                                  ▼
     │                         ┌─────────────────┐    Reversed Content
     │                         │  2. REVERSE     │ ──────────────────┐
     │                         │     CONTENT     │                   │
     │                         └─────────────────┘                   │
     │                                  │                            │
     │ Position & Replacement           │ Content                    │
     │                                  ▼                            │
     ▼                         ┌─────────────────┐                   │
┌─────────────────┐            │  3. REPLACE     │                   │
│  4. DISPLAY     │◀───────────│     WORDS       │                   │
│     CONTENT     │  Updated   └─────────────────┘                   │
└─────────────────┘  Content            │                            │
                                        │ Modified Content           │
                                        ▼                            │
                              ┌─────────────────┐                   │
                              │  5. CONVERT     │                   │
                              │     TO BYTES    │                   │
                              └─────────────────┘                   │
                                        │                            │
                                        │ Byte Content               │
                                        ▼                            │
                              ┌─────────────────┐                   │
                              │  6. SAVE        │◀──────────────────┘
                              │     FILES       │
                              └─────────────────┘
                                        │
                                        ▼
                              ┌─────────────────┐
                              │  FILE SYSTEM    │
                              └─────────────────┘
```

### 2.3 Level 2 DFD - File Creation Process

```
┌─────────┐  Base Name  ┌─────────────────┐  Sanitized Name  ┌─────────────────┐
│  USER   │ ──────────▶│ 1.1 SANITIZE    │ ───────────────▶│ 1.2 CREATE      │
└─────────┘             │     NAME        │                  │     FOLDER      │
     │                  └─────────────────┘                  └─────────────────┘
     │                                                                │
     │ Content                                                       │ Folder Path
     │                                                               ▼
     ▼                                                      ┌─────────────────┐
┌─────────────────┐                                        │ 1.3 GENERATE    │
│ 1.4 WRITE       │◀───────────────────────────────────────│     PATHS       │
│     FILES       │  File Paths                            └─────────────────┘
└─────────────────┘
     │
     ▼
┌─────────────────┐
│  FILE SYSTEM    │
└─────────────────┘
```

---

## 3. FLOWCHARTS

### 3.1 Main Application Flow

```
        ┌─────────────┐
        │    START    │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Initialize  │
        │ Application │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Load UI     │
        │ Components  │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Setup Event │
        │ Handlers    │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Start Auto- │
        │ Save Timer  │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Wait for    │
        │ User Input  │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Process     │
        │ User Action │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Update UI   │
        │ and Files   │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Continue?   │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │    EXIT     │
        └─────────────┘
```

### 3.2 File Creation Flowchart

```
        ┌─────────────┐
        │   START     │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Get Base    │
        │ Name & Text │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Validate    │
        │ Input       │
        └──────┬──────┘
               │
        ┌──────▼──────┐      No
        │ Valid?      │ ─────────┐
        └──────┬──────┘          │
               │ Yes              │
        ┌──────▼──────┐          │
        │ Sanitize    │          │
        │ Base Name   │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Create      │          │
        │ Folder      │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Generate    │          │
        │ File Paths  │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Write       │          │
        │ Original    │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Write       │          │
        │ Reversed    │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Write       │          │
        │ Byte File   │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Update UI   │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │   SUCCESS   │          │
        └─────────────┘          │
                                 │
        ┌─────────────┐          │
        │ Show Error  │◀─────────┘
        │ Message     │
        └─────────────┘
```

### 3.3 Word Replacement Flowchart

```
        ┌─────────────┐
        │   START     │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Get Current │
        │ Content     │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Show Input  │
        │ Dialog      │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Get Position│
        │ & Replace   │
        └──────┬──────┘
               │
        ┌──────▼──────┐
        │ Validate    │
        │ Position    │
        └──────┬──────┘
               │
        ┌──────▼──────┐      No
        │ Valid?      │ ─────────┐
        └──────┬──────┘          │
               │ Yes              │
        ┌──────▼──────┐          │
        │ Split Text  │          │
        │ into Words  │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Check       │          │
        │ Position    │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐      No  │
        │ In Range?   │ ─────────┤
        └──────┬──────┘          │
               │ Yes              │
        ┌──────▼──────┐          │
        │ Replace     │          │
        │ Word        │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Join Words  │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Update      │          │
        │ Content     │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │ Save Files  │          │
        └──────┬──────┘          │
               │                 │
        ┌──────▼──────┐          │
        │   SUCCESS   │          │
        └─────────────┘          │
                                 │
        ┌─────────────┐          │
        │ Show Error  │◀─────────┘
        │ Message     │
        └─────────────┘
```

---

## 4. PROCESS DIAGRAMS

### 4.1 Auto-Save Process Diagram

```
┌─────────────────┐    Timer Event    ┌─────────────────┐
│   TIMER         │ ─────────────────▶│ AUTO-SAVE       │
│   (30 seconds)  │                   │ PROCESS         │
└─────────────────┘                   └─────────┬───────┘
                                                │
                                      ┌─────────▼───────┐
                                      │ Check Content   │
                                      │ Changed?        │
                                      └─────────┬───────┘
                                                │
                                      ┌─────────▼───────┐      No
                                      │ Content         │ ──────────┐
                                      │ Modified?       │           │
                                      └─────────┬───────┘           │
                                                │ Yes               │
                                      ┌─────────▼───────┐           │
                                      │ Get Current     │           │
                                      │ Content         │           │
                                      └─────────┬───────┘           │
                                                │                   │
                                      ┌─────────▼───────┐           │
                                      │ Update File Set │           │
                                      └─────────┬───────┘           │
                                                │                   │
                                      ┌─────────▼───────┐           │
                                      │ Show Success    │           │
                                      │ Notification    │           │
                                      └─────────────────┘           │
                                                                    │
                                      ┌─────────────────┐           │
                                      │ Continue Timer  │◀──────────┘
                                      └─────────────────┘
```

### 4.2 File Synchronization Process

```
┌─────────────────┐
│ CONTENT CHANGE  │
│ EVENT           │
└─────────┬───────┘
          │
┌─────────▼───────┐
│ Get New Content │
└─────────┬───────┘
          │
┌─────────▼───────┐
│ Generate        │
│ Reversed        │
└─────────┬───────┘
          │
┌─────────▼───────┐
│ Generate        │
│ Byte Codes      │
└─────────┬───────┘
          │
┌─────────▼───────┐
│ Update Original │
│ File            │
└─────────┬───────┘
          │
┌─────────▼───────┐
│ Update Reversed │
│ File            │
└─────────┬───────┘
          │
┌─────────▼───────┐
│ Update Byte     │
│ File            │
└─────────┬───────┘
          │
┌─────────▼───────┐
│ Notify UI       │
│ Components      │
└─────────────────┘
```

---

## 5. DATABASE DESIGN / FILE STRUCTURE

### 5.1 File System Structure

```
File Mana Application/
├── Created files/                    # Main file storage directory
│   ├── [BaseName1]/                 # Individual file set folder
│   │   ├── [BaseName1]-org.txt      # Original content file
│   │   ├── [BaseName1]-rev.txt      # Reversed content file
│   │   └── [BaseName1]-byte.txt     # Byte codes file
│   ├── [BaseName2]/
│   │   ├── [BaseName2]-org.txt
│   │   ├── [BaseName2]-rev.txt
│   │   └── [BaseName2]-byte.txt
│   └── ...
├── src/main/java/                   # Source code directory
│   └── com/codemavriks/aptech/
│       ├── MainApp.java
│       ├── EditorController.java
│       ├── components/
│       │   ├── TextEditor.java
│       │   ├── SidePanel.java
│       │   └── FileNavigator.java
│       └── services/
│           └── FileService.java
├── src/main/resources/              # Resources directory
│   └── com/codemavriks/aptech/
│       ├── styles/
│       │   └── modern-theme.css
│       └── File-Mana-Logo.png
└── target/                          # Compiled output
    └── classes/
```

### 5.2 File Naming Convention

**Pattern:** `[BaseName]-[Type].txt`

| Type | Description | Example |
|------|-------------|---------|
| `org` | Original content file | `Ahmed-org.txt` |
| `rev` | Reversed content file | `Ahmed-rev.txt` |
| `byte` | Byte codes file | `Ahmed-byte.txt` |

**Folder Structure:**
- Each file set gets its own folder named after the base name
- Prevents file conflicts and provides organization
- Supports unlimited file sets with unique naming

### 5.3 Configuration Data Structure

**Application Settings (In-Memory):**
```java
public class AppConfig {
    private String currentFileBaseName;
    private String lastSavedContent;
    private boolean autoSaveEnabled;
    private int autoSaveInterval;
    private String defaultDirectory;
    private Map<String, String> recentFiles;
}
```

**File Metadata Structure:**
```java
public class FileMetadata {
    private String baseName;
    private String folderPath;
    private long originalSize;
    private long reversedSize;
    private long byteSize;
    private Date lastModified;
    private String checksum;
}
```

---

## 6. USER INTERFACE DESIGN

### 6.1 Main Window Layout

```
┌─────────────────────────────────────────────────────────────────┐
│ File Mana - Modern Text Editor                    [_] [□] [×]   │
├─────────────────────────────────────────────────────────────────┤
│ File  Edit  View  Tools  Help                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│ ┌─────────────────┐ ┌─────────────────────────────────────────┐ │
│ │  FILE NAVIGATOR │ │           TEXT EDITOR                   │ │
│ │                 │ │                                         │ │
│ │ 📁 Created files│ │  Line 1: Hello World                    │ │
│ │  └📁 Ahmed      │ │  Line 2: This is a test                │ │
│ │    ├📄 org.txt  │ │  Line 3: Content goes here             │ │
│ │    ├📄 rev.txt  │ │  Line 4: ...                           │ │
│ │    └📄 byte.txt │ │                                         │ │
│ │                 │ │                                         │ │
│ │ ┌─────────────┐ │ │                                         │ │
│ │ │CREATE FILES │ │ │                                         │ │
│ │ └─────────────┘ │ │                                         │ │
│ │                 │ │                                         │ │
│ │ ┌─────────────┐ │ │                                         │ │
│ │ │REPLACE WORD │ │ │                                         │ │
│ │ └─────────────┘ │ │                                         │ │
│ │                 │ │                                         │ │
│ │ Position: [  ]  │ │                                         │ │
│ │ Replace: [    ] │ │                                         │ │
│ │                 │ │                                         │ │
│ └─────────────────┘ └─────────────────────────────────────────┘ │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│ Status: Ready | Auto-save: ON | Files: 3 | Words: 156          │
└─────────────────────────────────────────────────────────────────┘
```

### 6.2 Component Specifications

**Text Editor (70% width):**
- Full-featured text area with syntax highlighting
- Line numbers and word wrap
- Undo/redo functionality
- Find and replace capabilities
- Real-time content updates

**Side Panel (30% width):**
- File navigator with tree view
- Create Files button and form
- Word replacement controls
- Status information display
- Recent files list

**Status Bar:**
- Current operation status
- Auto-save indicator
- File count and statistics
- Word count and character count

### 6.3 Color Scheme and Styling

**Dark Theme Colors:**
- **Background:** #181A20 (Dark gray-blue)
- **Text:** #E8E8E8 (Light gray)
- **Accent:** #007ACC (Blue)
- **Success:** #4CAF50 (Green)
- **Warning:** #FF9800 (Orange)
- **Error:** #F44336 (Red)

**Typography:**
- **Main Font:** "Segoe UI", Arial, sans-serif
- **Code Font:** "Consolas", "Monaco", monospace
- **Font Sizes:** 14px (normal), 16px (headers), 12px (status)

---

## 7. SECURITY DESIGN

### 7.1 File Security

**Access Control:**
- File operations restricted to application directory
- Input validation to prevent path traversal attacks
- Sanitization of file names to prevent injection

**Data Integrity:**
- Atomic file operations to prevent corruption
- Backup creation before modifications
- Checksum validation for file integrity

### 7.2 Input Validation

**User Input Security:**
- Position validation for word replacement
- Content length limits to prevent memory issues
- Special character handling in file names
- SQL injection prevention (though no database used)

---

## 8. PERFORMANCE DESIGN

### 8.1 Optimization Strategies

**File I/O Optimization:**
- Asynchronous file operations using JavaFX Task
- Buffered readers/writers for large files
- Lazy loading for file content display
- Caching of frequently accessed files

**Memory Management:**
- Efficient string operations using StringBuilder
- Garbage collection optimization
- Resource cleanup and proper disposal
- Memory profiling and leak detection

### 8.2 Scalability Considerations

**File Size Handling:**
- Progressive loading for large files
- Streaming operations for byte conversion
- Pagination for very large content
- Memory-mapped files for extreme cases

**Concurrent Operations:**
- Thread-safe file operations
- Background processing for auto-save
- Non-blocking UI updates
- Proper synchronization mechanisms

---

## CONCLUSION

The File Mana application design provides a comprehensive, scalable, and maintainable solution for the specified requirements. The modular architecture, clear separation of concerns, and professional UI design ensure both functionality and user experience excellence.

**Key Design Strengths:**
- **Modular Architecture:** Easy to maintain and extend
- **Professional UI:** Modern, intuitive interface design
- **Robust File Handling:** Comprehensive error handling and recovery
- **Performance Optimized:** Asynchronous operations and efficient algorithms
- **Security Conscious:** Input validation and secure file operations

---

**Document Prepared By:** NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH  
**Student ID:** Student1554163  
**Date:** [Current Date]  
**Version:** 1.0  
**Status:** Final 