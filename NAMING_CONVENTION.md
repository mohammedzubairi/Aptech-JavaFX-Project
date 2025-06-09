# File Naming Convention System

## Overview
This JavaFX application now uses an enhanced naming convention system that automatically creates three related files when a user creates a new file set.

## Naming Convention
When a user enters a base name (e.g., "Ahmed"), the system automatically creates three files:

1. **Original File**: `Ahmed-org.txt` - Contains the original content
2. **Reversed File**: `Ahmed-rev.txt` - Contains the reversed content
3. **Byte Codes File**: `Ahmed-byte.txt` - Contains the byte representation of the content

## Key Features

### Automatic File Creation
- User enters only the base name (without extension)
- System automatically appends appropriate suffixes and `.txt` extension
- All three files are created simultaneously when content is provided

### Extension Handling
- Users don't need to specify `.txt` extension
- If user accidentally includes `.txt`, it's automatically removed from the base name
- System always uses `.txt` as the file extension

### File Operations
- **Create File Set**: Creates all three variants at once
- **Update Operations**: Individual operations can update specific file types
- **File Selection**: Clicking any file in the list selects the entire file set

## Usage Workflow

1. **Enter Content**: Type your content in the input area
2. **Create File Set**: Click "Create File Set" button
3. **Enter Base Name**: Enter name like "Ahmed" (without extension)
4. **Automatic Creation**: System creates:
   - `Ahmed-org.txt`
   - `Ahmed-rev.txt` 
   - `Ahmed-byte.txt`

## File Operations
- All operations now work with the selected file set
- Selecting any file from a set (org/rev/byte) automatically selects the entire set
- Operations like reverse, compare, and byte conversion work on the current file set

## Benefits
- Consistent naming across related files
- No need to manually specify extensions
- Automatic creation of all variants
- Easy identification of file relationships
- Streamlined workflow for file operations 