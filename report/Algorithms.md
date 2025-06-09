# ALGORITHMS

---

## FILE MANA - MODERN TEXT EDITOR
### Core Algorithms and Processing Logic

---

## 1. FILE CREATION ALGORITHM

### Algorithm 1.1: Smart File Set Creation
```
ALGORITHM: createFileSet(baseName, content)
INPUT: baseName (String), content (String)
OUTPUT: Three files created with naming convention

BEGIN
    1. VALIDATE input parameters
        IF baseName is null OR empty THEN
            THROW IllegalArgumentException
        END IF
    
    2. SANITIZE baseName
        baseName = baseName.trim()
        IF baseName.endsWith(".txt") THEN
            baseName = baseName.substring(0, baseName.length() - 4)
        END IF
    
    3. CREATE folder structure
        folderPath = "Created files/" + baseName + "/"
        CREATE directory at folderPath
    
    4. GENERATE file paths
        orgPath = folderPath + baseName + "-org.txt"
        revPath = folderPath + baseName + "-rev.txt"
        bytePath = folderPath + baseName + "-byte.txt"
    
    5. CREATE original file
        WRITE content TO orgPath
    
    6. CREATE reversed file
        reversedContent = REVERSE(content)
        WRITE reversedContent TO revPath
    
    7. CREATE byte codes file
        byteContent = CONVERT_TO_BYTES(content)
        WRITE byteContent TO bytePath
    
    8. UPDATE internal state
        currentFileBaseName = baseName
        lastSavedContent = content
END
```

### Algorithm 1.2: Unique Name Generation
```
ALGORITHM: getUniqueBaseName(baseName)
INPUT: baseName (String)
OUTPUT: uniqueBaseName (String)

BEGIN
    1. INITIALIZE counter = 1
    2. originalBaseName = baseName
    3. WHILE fileSetExists(baseName) DO
        baseName = originalBaseName + "_" + counter
        counter = counter + 1
    END WHILE
    4. RETURN baseName
END
```

## 2. CONTENT REVERSAL ALGORITHM

### Algorithm 2.1: String Reversal
```
ALGORITHM: reverseContent(content)
INPUT: content (String)
OUTPUT: reversedContent (String)

BEGIN
    1. IF content is null OR empty THEN
        RETURN empty string
    END IF
    
    2. CREATE StringBuilder from content
    3. CALL reverse() method on StringBuilder
    4. CONVERT StringBuilder to String
    5. RETURN reversed string
END

TIME COMPLEXITY: O(n) where n is length of content
SPACE COMPLEXITY: O(n) for StringBuilder storage
```

### Algorithm 2.2: Line-by-Line Reversal (Alternative)
```
ALGORITHM: reverseContentByLines(content)
INPUT: content (String)
OUTPUT: reversedContent (String)

BEGIN
    1. SPLIT content by line separators
    2. FOR each line in lines DO
        reversedLine = REVERSE(line)
        ADD reversedLine to result
    END FOR
    3. JOIN reversed lines with line separators
    4. RETURN result
END
```

## 3. BYTE CONVERSION ALGORITHM

### Algorithm 3.1: Text to Byte Conversion
```
ALGORITHM: convertToByteString(content)
INPUT: content (String)
OUTPUT: byteString (String)

BEGIN
    1. IF content is null THEN
        RETURN empty string
    END IF
    
    2. CONVERT content to byte array using UTF-8 encoding
        bytes = content.getBytes("UTF-8")
    
    3. CREATE StringBuilder for result
    4. FOR each byte in bytes DO
        CONVERT byte to unsigned integer
        APPEND integer to StringBuilder
        IF not last byte THEN
            APPEND space separator
        END IF
    END FOR
    
    5. RETURN StringBuilder as String
END

EXAMPLE:
Input: "Hello"
Output: "72 101 108 108 111"
```

### Algorithm 3.2: Byte to Text Conversion (Reverse)
```
ALGORITHM: convertFromByteString(byteString)
INPUT: byteString (String)
OUTPUT: originalContent (String)

BEGIN
    1. SPLIT byteString by spaces
    2. CREATE byte array of same length
    3. FOR each string number DO
        CONVERT to integer
        CAST to byte
        ADD to byte array
    END FOR
    4. CREATE String from byte array using UTF-8
    5. RETURN resulting string
END
```

## 4. WORD REPLACEMENT ALGORITHM

### Algorithm 4.1: Word Extraction by Position
```
ALGORITHM: extractWordByPosition(content, position)
INPUT: content (String), position (int)
OUTPUT: word (String)

BEGIN
    1. VALIDATE inputs
        IF content is null OR position < 1 THEN
            THROW IllegalArgumentException
        END IF
    
    2. SPLIT content into words using regex "\\s+"
    3. IF position > words.length THEN
        THROW IndexOutOfBoundsException
    END IF
    
    4. RETURN words[position - 1]  // Convert to 0-based index
END
```

### Algorithm 4.2: Word Replacement by Position
```
ALGORITHM: replaceWordAtPosition(content, position, replacement)
INPUT: content (String), position (int), replacement (String)
OUTPUT: modifiedContent (String)

BEGIN
    1. VALIDATE inputs
        IF content is null OR position < 1 OR replacement is null THEN
            THROW IllegalArgumentException
        END IF
    
    2. SPLIT content into words using regex "\\s+"
    3. IF position > words.length THEN
        THROW IndexOutOfBoundsException
    END IF
    
    4. words[position - 1] = replacement
    5. JOIN words with single space
    6. RETURN modified content
END
```

### Algorithm 4.3: Advanced Word Replacement (Preserving Formatting)
```
ALGORITHM: replaceWordPreservingFormat(content, position, replacement)
INPUT: content (String), position (int), replacement (String)
OUTPUT: modifiedContent (String)

BEGIN
    1. CREATE Pattern for word boundaries
    2. CREATE Matcher for content
    3. wordCount = 0
    4. WHILE matcher.find() DO
        wordCount = wordCount + 1
        IF wordCount == position THEN
            REPLACE matched word with replacement
            BREAK
        END IF
    END WHILE
    5. RETURN modified content
END
```

## 5. FILE COMPARISON ALGORITHM

### Algorithm 5.1: Content Comparison
```
ALGORITHM: compareFileContents(file1Path, file2Path)
INPUT: file1Path (String), file2Path (String)
OUTPUT: isEqual (boolean)

BEGIN
    1. READ content1 from file1Path
    2. READ content2 from file2Path
    3. RETURN content1.equals(content2)
END
```

### Algorithm 5.2: Detailed Content Analysis
```
ALGORITHM: analyzeContentDifferences(original, reversed)
INPUT: original (String), reversed (String)
OUTPUT: analysisReport (ComparisonResult)

BEGIN
    1. CREATE ComparisonResult object
    2. SET isEqual = original.equals(reversed)
    3. SET lengthDifference = Math.abs(original.length() - reversed.length())
    4. CALCULATE character differences
    5. CALCULATE word count differences
    6. RETURN analysisReport
END
```

## 6. AUTO-SAVE ALGORITHM

### Algorithm 6.1: Periodic Auto-Save
```
ALGORITHM: autoSaveProcess()
INPUT: None (uses internal state)
OUTPUT: None (side effect: files updated)

BEGIN
    1. IF autoSaveEnabled AND currentFileBaseName != null THEN
        2. GET current content from text editor
        3. IF content != lastSavedContent THEN
            4. TRY
                updateFileSet(currentFileBaseName, content)
                lastSavedContent = content
                NOTIFY success callback
            5. CATCH IOException
                NOTIFY error callback
            END TRY
        END IF
    END IF
END

SCHEDULING: Execute every 30 seconds using Timer
```

## 7. FILE NAVIGATION ALGORITHM

### Algorithm 7.1: Directory Tree Building
```
ALGORITHM: buildFileTree(rootPath)
INPUT: rootPath (String)
OUTPUT: treeRoot (TreeItem)

BEGIN
    1. CREATE root TreeItem for rootPath
    2. GET all files and directories in rootPath
    3. FOR each item DO
        IF item is directory THEN
            childItem = buildFileTree(item.path)  // Recursive call
        ELSE IF item is .txt file THEN
            childItem = CREATE TreeItem for file
        END IF
        ADD childItem to root
    END FOR
    4. RETURN root
END

TIME COMPLEXITY: O(n) where n is total number of files/directories
```

### Algorithm 7.2: File Filtering
```
ALGORITHM: filterFiles(files, criteria)
INPUT: files (List<File>), criteria (FilterCriteria)
OUTPUT: filteredFiles (List<File>)

BEGIN
    1. CREATE empty result list
    2. FOR each file in files DO
        IF file.isDirectory() OR file.name.endsWith(".txt") THEN
            ADD file to result
        END IF
    END FOR
    3. RETURN result
END
```

## 8. ERROR HANDLING ALGORITHMS

### Algorithm 8.1: Graceful Error Recovery
```
ALGORITHM: handleFileOperationError(operation, exception)
INPUT: operation (String), exception (Exception)
OUTPUT: None (side effect: user notification)

BEGIN
    1. LOG error details
    2. DETERMINE error type
        CASE IOException:
            message = "File operation failed: " + operation
        CASE SecurityException:
            message = "Permission denied for: " + operation
        DEFAULT:
            message = "Unexpected error in: " + operation
    END CASE
    3. DISPLAY user-friendly error dialog
    4. ATTEMPT recovery if possible
END
```

## 9. PERFORMANCE OPTIMIZATION ALGORITHMS

### Algorithm 9.1: Lazy Loading for Large Files
```
ALGORITHM: loadFileContent(filePath, maxSize)
INPUT: filePath (String), maxSize (long)
OUTPUT: content (String)

BEGIN
    1. GET file size
    2. IF fileSize > maxSize THEN
        LOAD first maxSize bytes
        APPEND "... [Content truncated]"
    ELSE
        LOAD entire file
    END IF
    3. RETURN content
END
```

### Algorithm 9.2: Asynchronous File Operations
```
ALGORITHM: performAsyncFileOperation(operation, callback)
INPUT: operation (Runnable), callback (Callback)
OUTPUT: Future<Void>

BEGIN
    1. CREATE Task extending JavaFX Task
    2. OVERRIDE call() method with operation logic
    3. SET onSucceeded and onFailed handlers
    4. SUBMIT task to executor service
    5. RETURN task future
END
```

---

## ALGORITHM COMPLEXITY ANALYSIS

| Algorithm | Time Complexity | Space Complexity | Notes |
|-----------|----------------|------------------|-------|
| File Creation | O(n) | O(n) | n = content length |
| Content Reversal | O(n) | O(n) | Linear string operation |
| Byte Conversion | O(n) | O(n) | UTF-8 encoding |
| Word Replacement | O(n) | O(n) | String splitting/joining |
| File Comparison | O(n) | O(1) | Character-by-character |
| Directory Tree | O(m) | O(m) | m = number of files |
| Auto-save | O(n) | O(1) | Periodic execution |

---

**Document Prepared By:** [Student Name]  
**Date:** [Current Date]  
**Version:** 1.0  
**Technical Review:** [Supervisor Name] 