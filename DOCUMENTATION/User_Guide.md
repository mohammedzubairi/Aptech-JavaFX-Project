# USER GUIDE

---

## FILE MANA - MODERN TEXT EDITOR
### Complete User Manual and Operating Instructions

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
2. [System Requirements](#2-system-requirements)
3. [Installation Guide](#3-installation-guide)
4. [Getting Started](#4-getting-started)
5. [User Interface Overview](#5-user-interface-overview)
6. [Core Features](#6-core-features)
7. [Step-by-Step Operations](#7-step-by-step-operations)
8. [Keyboard Shortcuts](#8-keyboard-shortcuts)
9. [Troubleshooting](#9-troubleshooting)
10. [Frequently Asked Questions](#10-frequently-asked-questions)

---

## 1. INTRODUCTION

### 1.1 Welcome to File Mana

File Mana is a modern, feature-rich text editor designed to simplify file creation, content manipulation, and text processing tasks. Built with JavaFX, it provides a professional, user-friendly interface similar to popular code editors like Visual Studio Code.

### 1.2 Key Features

- **Smart File Management**: Automatic creation of three synchronized file variants
- **Content Reversal**: Instant text reversal with real-time updates
- **Word Replacement**: Position-based word extraction and replacement
- **Byte Conversion**: UTF-8 text to byte code conversion
- **Auto-Save**: Automatic saving every 30 seconds
- **Modern UI**: Dark theme with responsive design
- **File Navigator**: VSCode-like file tree with context menus

### 1.3 Who Should Use This Guide

This user guide is designed for:
- Students learning text processing concepts
- Developers needing quick file manipulation tools
- Anyone requiring automated file creation and content processing
- Users who prefer modern, intuitive interfaces

---

## 2. SYSTEM REQUIREMENTS

### 2.1 Minimum Requirements

| Component | Requirement |
|-----------|-------------|
| **Operating System** | Windows 10, macOS 10.14, or Linux (Ubuntu 18.04+) |
| **Java Version** | Java 21 or higher |
| **Memory (RAM)** | 4 GB minimum, 8 GB recommended |
| **Storage** | 100 MB free disk space |
| **Display** | 1024x768 minimum resolution |

### 2.2 Recommended Requirements

| Component | Recommendation |
|-----------|----------------|
| **Operating System** | Windows 11, macOS 12+, or Linux (Ubuntu 20.04+) |
| **Java Version** | Java 21 (latest update) |
| **Memory (RAM)** | 8 GB or more |
| **Storage** | 500 MB free disk space |
| **Display** | 1920x1080 or higher resolution |

### 2.3 Software Dependencies

- **Java Runtime Environment (JRE) 21+**: Required for running the application
- **JavaFX 22**: Included with the application package
- **No additional software required**: All dependencies are bundled

---

## 3. INSTALLATION GUIDE

### 3.1 Pre-Installation Steps

1. **Verify Java Installation**:
   ```bash
   java -version
   ```
   Ensure Java 21 or higher is installed.

2. **Download File Mana**:
   - Obtain the `FileMana.jar` file from your instructor or download location
   - Save it to a convenient location (e.g., Desktop or Documents)

### 3.2 Installation Process

**Option 1: Direct Execution (Recommended)**
1. Double-click the `FileMana.jar` file
2. If prompted, select "Open with Java Platform SE Binary"
3. The application will launch automatically

**Option 2: Command Line Execution**
1. Open Command Prompt (Windows) or Terminal (macOS/Linux)
2. Navigate to the directory containing `FileMana.jar`
3. Run the command:
   ```bash
   java -jar FileMana.jar
   ```

### 3.3 First Launch

1. **Application Startup**: The File Mana window will appear
2. **Initial Setup**: No configuration required - ready to use immediately
3. **File Structure**: The application will create a "Created files" folder in its directory

---

## 4. GETTING STARTED

### 4.1 Launching the Application

1. **Start File Mana** using one of the installation methods above
2. **Main Window**: The application opens with a clean, modern interface
3. **Ready to Use**: No login or setup required

### 4.2 First Steps

1. **Familiarize with Interface**: Explore the layout (sidebar and main editor)
2. **Create Your First File**: Use the "Create Files" button
3. **Enter Sample Text**: Type some content to see the features in action
4. **Observe Auto-Creation**: Notice the three files created automatically

### 4.3 Basic Workflow

```
1. Enter base name → 2. Type content → 3. Click "Create Files" 
                                              ↓
4. Files created automatically ← 3. View in file navigator
                                              ↓
5. Edit content in main editor → 6. Auto-save keeps files synchronized
```

---

## 5. USER INTERFACE OVERVIEW

### 5.1 Main Window Layout

```
┌─────────────────────────────────────────────────────────────────┐
│ File Mana - Modern Text Editor                    [_] [□] [×]   │
├─────────────────────────────────────────────────────────────────┤
│ File  Edit  View  Tools  Help                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│ ┌─────────────────┐ ┌─────────────────────────────────────────┐ │
│ │   SIDEBAR       │ │         MAIN EDITOR                     │ │
│ │   (30% width)   │ │         (70% width)                     │ │
│ │                 │ │                                         │ │
│ │ • File Navigator│ │  Your text content appears here        │ │
│ │ • Create Files  │ │  with syntax highlighting and          │ │
│ │ • Replace Words │ │  line numbers for easy editing         │ │
│ │ • Status Info   │ │                                         │ │
│ │                 │ │                                         │ │
│ └─────────────────┘ └─────────────────────────────────────────┘ │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│ Status: Ready | Auto-save: ON | Files: 3 | Words: 156          │
└─────────────────────────────────────────────────────────────────┘
```

### 5.2 Sidebar Components

**File Navigator (Top Section)**
- **Tree View**: Hierarchical display of created files
- **Folder Icons**: 📁 for directories, 📄 for files
- **Context Menu**: Right-click for additional options
- **File Types**: Shows -org.txt, -rev.txt, and -byte.txt files

**Create Files Section (Middle)**
- **Base Name Field**: Enter the name for your file set
- **Content Area**: Type or paste your text content
- **Create Files Button**: Generates the three synchronized files
- **Clear Button**: Resets the form for new input

**Word Replacement Section (Bottom)**
- **Position Field**: Enter the word position (1-based numbering)
- **Replacement Field**: Enter the new word
- **Replace Button**: Performs the word replacement
- **Clear Fields Button**: Resets the replacement form

### 5.3 Main Editor Area

**Text Editor Features**
- **Syntax Highlighting**: Enhanced readability with color coding
- **Line Numbers**: Easy navigation and reference
- **Word Wrap**: Automatic line wrapping for long text
- **Scroll Bars**: Vertical and horizontal scrolling as needed
- **Real-time Updates**: Changes reflected immediately

**Editor Capabilities**
- **Full Text Editing**: Cut, copy, paste, select all
- **Undo/Redo**: Ctrl+Z and Ctrl+Y for operation reversal
- **Find/Replace**: Ctrl+F for text search and replacement
- **Auto-Save Integration**: Changes saved automatically every 30 seconds

### 5.4 Status Bar

**Information Display**
- **Current Status**: Shows current operation or "Ready"
- **Auto-save Indicator**: ON/OFF status with timer
- **File Count**: Number of file sets created
- **Word Count**: Total words in current content
- **Character Count**: Total characters including spaces

---

## 6. CORE FEATURES

### 6.1 Smart File Creation

**How It Works**
File Mana automatically creates three synchronized files for each base name:

1. **Original File** (`basename-org.txt`): Contains your original text
2. **Reversed File** (`basename-rev.txt`): Contains character-reversed text
3. **Byte File** (`basename-byte.txt`): Contains UTF-8 byte codes

**Benefits**
- **Automatic Organization**: Files grouped in named folders
- **Real-time Synchronization**: All files update when content changes
- **Unique Naming**: Prevents conflicts with automatic suffixes
- **Easy Management**: Clear naming convention for easy identification

### 6.2 Content Reversal

**Functionality**
- **Character-by-Character**: Reverses text at character level
- **Unicode Support**: Handles international characters correctly
- **Real-time Updates**: Reversed file updates automatically
- **Maintains Formatting**: Preserves original text structure

**Example**
```
Original: "Hello World"
Reversed: "dlroW olleH"
```

### 6.3 Word Replacement by Position

**How to Use**
1. **Identify Position**: Words are numbered starting from 1
2. **Enter Position**: Type the word number in the Position field
3. **Enter Replacement**: Type the new word in the Replacement field
4. **Execute**: Click "Replace Word" to perform the operation

**Example**
```
Original: "The quick brown fox jumps"
Position 3: "brown" → Replace with "red"
Result: "The quick red fox jumps"
```

### 6.4 Byte Code Conversion

**Technical Details**
- **UTF-8 Encoding**: Uses standard UTF-8 character encoding
- **Space Separation**: Byte values separated by spaces
- **Unsigned Format**: Displays bytes as unsigned integers (0-255)
- **Automatic Updates**: Byte file updates when content changes

**Example**
```
Text: "Hi"
Bytes: "72 105"
```

### 6.5 Auto-Save System

**Features**
- **30-Second Interval**: Automatic saving every 30 seconds
- **Change Detection**: Only saves when content has changed
- **Background Operation**: Non-intrusive, runs in background
- **Status Indication**: Shows auto-save status in status bar
- **Manual Save**: Ctrl+S for immediate saving

---

## 7. STEP-BY-STEP OPERATIONS

### 7.1 Creating Your First File Set

**Step 1: Prepare Your Content**
1. Think of a base name for your files (e.g., "MyDocument")
2. Prepare the text content you want to process

**Step 2: Enter Information**
1. Click in the "Base Name" field in the sidebar
2. Type your desired base name (without .txt extension)
3. Click in the large text area below
4. Type or paste your content

**Step 3: Create Files**
1. Click the "Create Files" button
2. Wait for the success message
3. Observe the new folder in the file navigator
4. See your content appear in the main editor

**Step 4: Verify Creation**
1. Expand the folder in the file navigator
2. See three files: -org.txt, -rev.txt, -byte.txt
3. Click on each file to view its content
4. Verify the reversed and byte versions

### 7.2 Editing Existing Content

**Step 1: Select File**
1. Navigate to the file navigator
2. Click on any -org.txt file to open it
3. Content appears in the main editor

**Step 2: Make Changes**
1. Click in the main editor area
2. Edit the text as needed
3. Use standard editing operations (cut, copy, paste)

**Step 3: Save Changes**
1. Press Ctrl+S for immediate save, or
2. Wait for auto-save (30 seconds)
3. Observe status bar for save confirmation
4. Check that all three files are updated

### 7.3 Replacing Words by Position

**Step 1: Identify Target Word**
1. Look at your content in the main editor
2. Count the position of the word you want to replace
3. Remember that counting starts from 1

**Step 2: Enter Replacement Details**
1. In the sidebar, find the "Word Replacement" section
2. Enter the word position in the "Position" field
3. Enter the new word in the "Replace" field

**Step 3: Execute Replacement**
1. Click the "Replace Word" button
2. Confirm the operation if prompted
3. Observe the change in the main editor
4. Check that all files are updated automatically

**Step 4: Verify Results**
1. Review the updated content
2. Check the reversed file for corresponding changes
3. Verify the byte file reflects the new content
4. Use Ctrl+Z if you need to undo the change

### 7.4 Managing Multiple File Sets

**Creating Multiple Sets**
1. Use the "Clear" button to reset the form
2. Enter a new base name
3. Add different content
4. Create additional file sets

**Switching Between Sets**
1. Use the file navigator to browse folders
2. Click on different -org.txt files
3. Each file set maintains its own content
4. Auto-save works independently for each set

**Organizing Files**
1. File sets are automatically organized in folders
2. Each folder contains exactly three files
3. Use descriptive base names for easy identification
4. The navigator shows the folder structure clearly

---

## 8. KEYBOARD SHORTCUTS

### 8.1 File Operations

| Shortcut | Action | Description |
|----------|--------|-------------|
| **Ctrl+N** | New File Set | Clears form for new file creation |
| **Ctrl+S** | Save | Immediately saves current content |
| **Ctrl+O** | Open | Opens file selection dialog |
| **F5** | Refresh | Refreshes file navigator |

### 8.2 Text Editing

| Shortcut | Action | Description |
|----------|--------|-------------|
| **Ctrl+Z** | Undo | Reverses last action |
| **Ctrl+Y** | Redo | Repeats last undone action |
| **Ctrl+X** | Cut | Cuts selected text |
| **Ctrl+C** | Copy | Copies selected text |
| **Ctrl+V** | Paste | Pastes clipboard content |
| **Ctrl+A** | Select All | Selects all text |

### 8.3 Search and Replace

| Shortcut | Action | Description |
|----------|--------|-------------|
| **Ctrl+F** | Find | Opens find dialog |
| **Ctrl+H** | Replace | Opens find and replace dialog |
| **F3** | Find Next | Finds next occurrence |
| **Shift+F3** | Find Previous | Finds previous occurrence |

### 8.4 Navigation

| Shortcut | Action | Description |
|----------|--------|-------------|
| **Ctrl+Home** | Go to Start | Moves cursor to beginning |
| **Ctrl+End** | Go to End | Moves cursor to end |
| **Ctrl+G** | Go to Line | Opens go to line dialog |
| **Page Up** | Page Up | Scrolls up one page |
| **Page Down** | Page Down | Scrolls down one page |

### 8.5 Application

| Shortcut | Action | Description |
|----------|--------|-------------|
| **F1** | Help | Opens help documentation |
| **Alt+F4** | Exit | Closes the application |
| **F11** | Full Screen | Toggles full screen mode |
| **Ctrl+,** | Settings | Opens settings dialog |

---

## 9. TROUBLESHOOTING

### 9.1 Common Issues and Solutions

**Issue: Application Won't Start**

*Symptoms:* Double-clicking the JAR file does nothing, or error messages appear.

*Solutions:*
1. **Check Java Version**:
   ```bash
   java -version
   ```
   Ensure Java 21 or higher is installed.

2. **Try Command Line**:
   ```bash
   java -jar FileMana.jar
   ```
   This may show error messages not visible otherwise.

3. **Reinstall Java**: Download and install the latest Java from Oracle's website.

**Issue: Files Not Creating**

*Symptoms:* Clicking "Create Files" doesn't generate files, or error messages appear.

*Solutions:*
1. **Check Permissions**: Ensure you have write permissions in the application directory.
2. **Verify Base Name**: Use only alphanumeric characters and avoid special symbols.
3. **Check Disk Space**: Ensure sufficient free disk space is available.
4. **Restart Application**: Close and reopen File Mana.

**Issue: Auto-Save Not Working**

*Symptoms:* Changes are lost when switching files or closing the application.

*Solutions:*
1. **Check Status Bar**: Verify auto-save shows "ON" in the status bar.
2. **Manual Save**: Use Ctrl+S to save manually.
3. **Wait for Timer**: Auto-save occurs every 30 seconds after changes.
4. **Check File Permissions**: Ensure files are not read-only.

**Issue: Word Replacement Fails**

*Symptoms:* Word replacement doesn't work or shows error messages.

*Solutions:*
1. **Verify Position**: Ensure the position number is valid (1 to word count).
2. **Check Content**: Make sure there is text in the editor.
3. **Use Valid Position**: Count words carefully, starting from 1.
4. **Avoid Empty Replacement**: Enter a valid replacement word.

### 9.2 Performance Issues

**Issue: Slow Performance**

*Symptoms:* Application responds slowly, especially with large files.

*Solutions:*
1. **Increase Memory**: Start with more memory:
   ```bash
   java -Xmx2G -jar FileMana.jar
   ```
2. **Close Other Applications**: Free up system resources.
3. **Reduce File Size**: Work with smaller text files if possible.
4. **Restart Application**: Close and reopen to clear memory.

**Issue: High Memory Usage**

*Symptoms:* System becomes slow, memory usage is high.

*Solutions:*
1. **Limit File Size**: Avoid extremely large text files.
2. **Close Unused Files**: Work with one file set at a time.
3. **Restart Regularly**: Restart the application periodically.
4. **Check System Resources**: Monitor system memory usage.

### 9.3 File System Issues

**Issue: Files Appear Corrupted**

*Symptoms:* File content is garbled or unreadable.

*Solutions:*
1. **Check Encoding**: Files use UTF-8 encoding by default.
2. **Restore from Backup**: Use auto-save backups if available.
3. **Recreate Files**: Create a new file set with the same content.
4. **Check Disk Health**: Run disk check utilities.

**Issue: Cannot Delete Files**

*Symptoms:* Error messages when trying to delete files or folders.

*Solutions:*
1. **Close Application**: Ensure File Mana is not using the files.
2. **Check Permissions**: Verify you have delete permissions.
3. **Use File Manager**: Try deleting through the system file manager.
4. **Restart System**: Reboot if files are locked by the system.

---

## 10. FREQUENTLY ASKED QUESTIONS

### 10.1 General Questions

**Q: What file formats does File Mana support?**
A: File Mana works with plain text files (.txt) using UTF-8 encoding. It automatically creates three variants: original, reversed, and byte-encoded versions.

**Q: Can I use File Mana with non-English text?**
A: Yes, File Mana fully supports Unicode and UTF-8 encoding, making it compatible with text in any language including Arabic, Chinese, Japanese, and others.

**Q: Is there a limit to file size?**
A: While there's no hard limit, File Mana is optimized for files up to 10MB. Larger files may experience slower performance.

**Q: Can I run File Mana on different operating systems?**
A: Yes, File Mana runs on Windows, macOS, and Linux systems that have Java 21 or higher installed.

### 10.2 Feature Questions

**Q: How does the word replacement feature count words?**
A: Words are counted by splitting text on whitespace characters (spaces, tabs, newlines). Punctuation attached to words is included with the word. Counting starts from position 1.

**Q: What happens if I enter an invalid word position?**
A: File Mana will display an error message indicating the valid range (1 to the total number of words) and will not perform the replacement.

**Q: Can I undo word replacements?**
A: Yes, use Ctrl+Z to undo the last word replacement, or any other editing operation.

**Q: How often does auto-save occur?**
A: Auto-save runs every 30 seconds, but only saves if the content has actually changed since the last save.

### 10.3 Technical Questions

**Q: Where are my files stored?**
A: Files are stored in a "Created files" folder within the same directory as the File Mana application. Each file set gets its own subfolder.

**Q: What is the byte conversion format?**
A: Text is converted to UTF-8 bytes, with each byte displayed as an unsigned integer (0-255) separated by spaces.

**Q: Can I customize the auto-save interval?**
A: The current version uses a fixed 30-second interval. Future versions may include customizable intervals.

**Q: How do I backup my files?**
A: Simply copy the entire "Created files" folder to another location. This preserves all your file sets and their organization.

### 10.4 Troubleshooting Questions

**Q: Why won't the application start?**
A: The most common cause is not having Java 21 or higher installed. Download and install the latest Java version from Oracle's website.

**Q: What should I do if files aren't synchronizing?**
A: Try manually saving with Ctrl+S, check file permissions, and ensure the application has write access to its directory.

**Q: How do I report bugs or request features?**
A: Contact your instructor or the development team with detailed information about the issue, including steps to reproduce it.

**Q: Can I recover deleted files?**
A: File Mana doesn't include a recycle bin feature. Use your operating system's file recovery tools or restore from backups.

---

## CONCLUSION

File Mana provides a powerful yet user-friendly solution for text processing and file management tasks. This user guide covers all essential operations and troubleshooting scenarios to help you make the most of the application.

**Key Takeaways:**
- **Simple Interface**: Modern, intuitive design requires minimal learning
- **Powerful Features**: Automated file creation, content processing, and synchronization
- **Reliable Operation**: Auto-save and error handling ensure data safety
- **Cross-Platform**: Works consistently across different operating systems

**Getting Help:**
- **This Guide**: Comprehensive reference for all features
- **Keyboard Shortcuts**: Quick access to common operations
- **Troubleshooting**: Solutions for common issues
- **FAQ**: Answers to frequently asked questions

**Next Steps:**
- **Practice**: Try all features with sample content
- **Explore**: Experiment with different file sets and content types
- **Customize**: Learn keyboard shortcuts for efficient operation
- **Share**: Help others learn to use File Mana effectively