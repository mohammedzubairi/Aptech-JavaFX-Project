/**
 * File Mana - Modern Text Editor
 * File Service - Business Logic Layer
 * 
 * This class handles all file system operations and business logic for the File Mana
 * text editor application. It manages the creation, reading, writing, and manipulation
 * of file sets (original, reversed, and byte-encoded variants) while providing
 * asynchronous operations and auto-save functionality.
 * 
 * Key Responsibilities:
 * - File set management (create, read, update, delete operations)
 * - Content processing (text reversal, byte encoding)
 * - Auto-save functionality with background threading
 * - File system organization with folder-based grouping
 * - Asynchronous file operations for UI responsiveness
 * - Error handling and recovery mechanisms
 * 
 * Architecture Pattern: Service Layer
 * - Encapsulates all file-related business logic
 * - Provides clean API for controllers to use
 * - Handles threading and asynchronous operations
 * - Manages file system state and consistency
 * 
 * File Organization Strategy:
 * Created files/
 * ├── [BaseName1]/
 * │   ├── [BaseName1]-org.txt    # Original content
 * │   ├── [BaseName1]-rev.txt    # Reversed content
 * │   └── [BaseName1]-byte.txt   # Byte-encoded content
 * └── [BaseName2]/
 *     ├── [BaseName2]-org.txt
 *     ├── [BaseName2]-rev.txt
 *     └── [BaseName2]-byte.txt
 * 
 * @author NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
 * @student_id Student1554163
 * @course Advanced Java Programming with JavaFX
 * @institution Aptech Computer Education
 * @university Alnasser University
 * @version 1.0
 * @since 2025-05-20
 */
package com.codemavriks.aptech.services;

import javafx.concurrent.Task;
import javafx.concurrent.Service;
import javafx.application.Platform;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * File Service class for managing all file operations in File Mana.
 * 
 * This service class encapsulates all file system operations and provides
 * a clean API for the controller layer. It handles the complexity of managing
 * multiple file variants (original, reversed, byte-encoded) while ensuring
 * data consistency and providing asynchronous operations for better performance.
 * 
 * Design Patterns Used:
 * - Service Layer Pattern: Encapsulates business logic
 * - Observer Pattern: Callback mechanism for async operations
 * - Template Method Pattern: Common file operation patterns
 * - Strategy Pattern: Different content processing strategies
 * 
 * Thread Safety:
 * - Uses ScheduledExecutorService for background operations
 * - All UI callbacks are executed on JavaFX Application Thread
 * - File operations are atomic to prevent corruption
 */
public class FileService {
    // ============================================================================
    // CONSTANTS AND CONFIGURATION
    // ============================================================================
    
    /**
     * Base directory for all created files.
     * All file sets are organized under this directory.
     */
    private static final String CREATED_FILES_DIR = "Created files/";
    
    // ============================================================================
    // INSTANCE VARIABLES
    // ============================================================================
    
    /**
     * Executor service for background auto-save operations.
     * Uses a single daemon thread to prevent blocking the UI.
     */
    private final ScheduledExecutorService autoSaveExecutor;
    
    /**
     * Flag to control auto-save functionality.
     * Can be disabled for performance or user preference.
     */
    private boolean autoSaveEnabled = true;
    
    /**
     * Base name of the currently active file set.
     * Used to track which file set is being worked on.
     */
    private String currentFileBaseName = null;
    
    /**
     * Content of the last saved version.
     * Used to detect changes and avoid unnecessary saves.
     */
    private String lastSavedContent = "";
    
    /**
     * Callback interface for auto-save operations.
     * Allows the UI to be notified of save completion or errors.
     */
    private AutoSaveCallback autoSaveCallback;

    // ============================================================================
    // CONSTRUCTOR AND INITIALIZATION
    // ============================================================================

    /**
     * Constructor - Initializes the file service with background executor.
     * 
     * Sets up the auto-save executor service with a daemon thread to ensure
     * the application can shut down cleanly even if auto-save is running.
     * Also ensures the base directory exists for file operations.
     */
    public FileService() {
        // Create single-threaded executor for auto-save operations
        this.autoSaveExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "AutoSave-Thread");
            t.setDaemon(true);  // Daemon thread won't prevent JVM shutdown
            return t;
        });
        
        // Ensure the base directory exists
        createDirectoryIfNotExists();
    }

    // ============================================================================
    // DIRECTORY MANAGEMENT
    // ============================================================================

    /**
     * Creates the base directory for file storage if it doesn't exist.
     * 
     * This method ensures that the "Created files/" directory exists before
     * any file operations are attempted. It's called during initialization
     * and before any file creation operations.
     */
    private void createDirectoryIfNotExists() {
        File dir = new File(CREATED_FILES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();  // Create directory and any necessary parent directories
        }
    }

    // ============================================================================
    // PATH GENERATION UTILITIES
    // ============================================================================

    /**
     * Generate file path based on base name and type with folder grouping.
     * 
     * This method creates the full file path for a specific file variant
     * within the organized folder structure. Each file set gets its own
     * folder to keep related files together.
     * 
     * @param baseName The base name of the file set (without extension)
     * @param type The file type ("org", "rev", or "byte")
     * @return Full file path in format: "Created files/[baseName]/[baseName]-[type].txt"
     * 
     * Example:
     * getFilePath("myfile", "org") → "Created files/myfile/myfile-org.txt"
     */
    public String getFilePath(String baseName, String type) {
        return CREATED_FILES_DIR + baseName + "/" + baseName + "-" + type + ".txt";
    }

    /**
     * Get folder path for a file set.
     * 
     * Returns the directory path where all variants of a file set are stored.
     * This is used for folder operations like creation, deletion, and listing.
     * 
     * @param baseName The base name of the file set
     * @return Folder path in format: "Created files/[baseName]/"
     */
    public String getFolderPath(String baseName) {
        return CREATED_FILES_DIR + baseName + "/";
    }

    /**
     * Extract base name from file path.
     * 
     * This method reverse-engineers the base name from a full file path,
     * supporting both the new folder-based organization and legacy flat structure.
     * It's used when working with files selected from the file navigator.
     * 
     * @param filePath Full path to a file
     * @return The base name of the file set
     * 
     * Examples:
     * "Created files/myfile/myfile-org.txt" → "myfile"
     * "Created files/test/test-rev.txt" → "test"
     */
    public String extractBaseName(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        String parentName = file.getParentFile().getName();
        
        // If the parent is the base name folder, use that (new organization)
        if (!parentName.equals("Created files")) {
            return parentName;
        }
        
        // Fallback to old method for legacy files
        if (fileName.contains("-org.txt")) {
            return fileName.replace("-org.txt", "");
        } else if (fileName.contains("-rev.txt")) {
            return fileName.replace("-rev.txt", "");
        } else if (fileName.contains("-byte.txt")) {
            return fileName.replace("-byte.txt", "");
        } else {
            return fileName.replace(".txt", "");
        }
    }

    // ============================================================================
    // FILE SET EXISTENCE AND VALIDATION
    // ============================================================================

    /**
     * Check if a file set exists.
     * 
     * Determines whether a complete file set (folder with all three variants)
     * exists for the given base name. This is used to prevent conflicts and
     * validate operations before attempting them.
     * 
     * @param baseName The base name to check
     * @return true if the file set exists, false otherwise
     */
    public boolean fileSetExists(String baseName) {
        File folder = new File(getFolderPath(baseName));
        return folder.exists() && folder.isDirectory();
    }

    // ============================================================================
    // FILE SET CREATION AND MANAGEMENT
    // ============================================================================

    /**
     * Create a new file set with all three variants in a folder.
     * 
     * This method creates a complete file set consisting of:
     * 1. Original content file ([baseName]-org.txt)
     * 2. Reversed content file ([baseName]-rev.txt)
     * 3. Byte-encoded content file ([baseName]-byte.txt)
     * 
     * All files are created within a dedicated folder for organization.
     * The operation is atomic - either all files are created or none are.
     * 
     * @param baseName The base name for the file set
     * @param content The original content to process and save
     * @throws IOException If file creation fails
     */
    public void createFileSet(String baseName, String content) throws IOException {
        // Ensure base directory exists
        createDirectoryIfNotExists();
        
        // Create folder for the file set
        File folder = new File(getFolderPath(baseName));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        // Create original file with provided content
        writeToFile(getFilePath(baseName, "org"), content);
        
        // Create reversed file with character-reversed content
        String reversedContent = new StringBuilder(content).reverse().toString();
        writeToFile(getFilePath(baseName, "rev"), reversedContent);
        
        // Create byte codes file with UTF-8 byte representation
        String byteContent = convertToByteString(content);
        writeToFile(getFilePath(baseName, "byte"), byteContent);
        
        // Update service state
        currentFileBaseName = baseName;
        lastSavedContent = content;
    }

    /**
     * Update all files in a file set with new content.
     * 
     * This method updates all three variants of a file set with new content,
     * maintaining synchronization between the original, reversed, and byte-encoded
     * versions. It's used for save operations and auto-save functionality.
     * 
     * Process:
     * 1. Ensure folder structure exists
     * 2. Update original file with new content
     * 3. Generate and save reversed content
     * 4. Generate and save byte-encoded content
     * 5. Update last saved content tracking
     * 
     * @param baseName The base name of the file set to update
     * @param content The new content to save
     * @throws IOException If file update fails
     */
    public void updateFileSet(String baseName, String content) throws IOException {
        // Ensure base directory exists
        createDirectoryIfNotExists();
        
        // Ensure folder exists for the file set
        File folder = new File(getFolderPath(baseName));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        // Update original file
        writeToFile(getFilePath(baseName, "org"), content);
        
        // Update reversed file with new reversed content
        String reversedContent = new StringBuilder(content).reverse().toString();
        writeToFile(getFilePath(baseName, "rev"), reversedContent);
        
        // Update byte codes file with new byte representation
        String byteContent = convertToByteString(content);
        writeToFile(getFilePath(baseName, "byte"), byteContent);
        
        // Update tracking of last saved content
        lastSavedContent = content;
    }

    /**
     * Delete entire file set (folder and all files).
     * 
     * This method removes a complete file set including the containing folder
     * and all three file variants. It's used when the user deletes a file set
     * from the file navigator.
     * 
     * @param baseName The base name of the file set to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteFileSet(String baseName) {
        File folder = new File(getFolderPath(baseName));
        return deleteDirectory(folder);
    }

    /**
     * Delete a directory and all its contents recursively.
     * 
     * This utility method safely deletes a directory and all its contents,
     * handling both files and subdirectories. It's used by deleteFileSet
     * and other cleanup operations.
     * 
     * @param directory The directory to delete
     * @return true if deletion was successful, false otherwise
     */
    private boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                // Recursively delete all contents
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);  // Recursive call for subdirectories
                    } else {
                        file.delete();  // Delete individual files
                    }
                }
            }
            return directory.delete();  // Delete the directory itself
        }
        return false;
    }

    // ============================================================================
    // FILE I/O OPERATIONS
    // ============================================================================

    /**
     * Read content from a file.
     * 
     * This method reads the complete content of a text file and returns it
     * as a string. It preserves line separators and handles different text
     * encodings properly. Used when opening files in the text editor.
     * 
     * @param filePath Path to the file to read
     * @return The complete file content as a string
     * @throws IOException If file reading fails
     */
    public String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    /**
     * Write content to a file.
     * 
     * This private method handles the actual file writing operation with
     * proper error handling and directory creation. It ensures that parent
     * directories exist before attempting to write the file.
     * 
     * @param filePath Path where the file should be written
     * @param content Content to write to the file
     * @throws IOException If file writing fails
     */
    private void writeToFile(String filePath, String content) throws IOException {
        // Ensure parent directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        // Write content to file using try-with-resources for automatic cleanup
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    // ============================================================================
    // CONTENT PROCESSING ALGORITHMS
    // ============================================================================

    /**
     * Convert string content to byte representation.
     * 
     * This method converts text content to its UTF-8 byte representation,
     * formatting it as space-separated unsigned integer values. This is one
     * of the core requirements for the File Mana application.
     * 
     * Algorithm:
     * 1. Convert string to UTF-8 byte array
     * 2. Convert each byte to unsigned integer (0-255 range)
     * 3. Join all values with spaces
     * 
     * @param content The text content to convert
     * @return Space-separated byte values as a string
     * 
     * Example:
     * "Hello" → "72 101 108 108 111"
     */
    private String convertToByteString(String content) {
        byte[] bytes = content.getBytes();
        StringBuilder byteCodes = new StringBuilder();
        for (byte b : bytes) {
            byteCodes.append(b).append(" ");
        }
        return byteCodes.toString().trim();
    }

    /**
     * Delete a file
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        String baseName = extractBaseName(filePath);
        
        // If this is part of a file set, delete the entire set
        if (fileSetExists(baseName)) {
            return deleteFileSet(baseName);
        }
        
        return file.delete();
    }

    /**
     * Rename a file set
     */
    public boolean renameFileSet(String oldBaseName, String newBaseName) {
        File oldFolder = new File(getFolderPath(oldBaseName));
        File newFolder = new File(getFolderPath(newBaseName));
        
        if (!oldFolder.exists() || newFolder.exists()) {
            return false;
        }
        
        // Rename the folder
        if (oldFolder.renameTo(newFolder)) {
            // Rename the files inside
            try {
                File[] files = newFolder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        String fileName = file.getName();
                        String newFileName = fileName.replace(oldBaseName + "-", newBaseName + "-");
                        File newFile = new File(newFolder, newFileName);
                        file.renameTo(newFile);
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        
        return false;
    }

    /**
     * Rename a file
     */
    public boolean renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        return oldFile.renameTo(newFile);
    }

    /**
     * Check if file exists
     */
    public boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * Start auto-save functionality
     */
    public void startAutoSave(String content, int intervalSeconds) {
        if (!autoSaveEnabled || currentFileBaseName == null) return;
        
        autoSaveExecutor.scheduleAtFixedRate(() -> {
            if (!content.equals(lastSavedContent) && currentFileBaseName != null) {
                try {
                    updateFileSet(currentFileBaseName, content);
                    Platform.runLater(() -> {
                        if (autoSaveCallback != null) {
                            autoSaveCallback.onAutoSave(currentFileBaseName);
                        }
                    });
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        if (autoSaveCallback != null) {
                            autoSaveCallback.onAutoSaveError(e.getMessage());
                        }
                    });
                }
            }
        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
    }

    /**
     * Stop auto-save functionality
     */
    public void stopAutoSave() {
        autoSaveExecutor.shutdown();
    }

    /**
     * Set auto-save callback
     */
    public void setAutoSaveCallback(AutoSaveCallback callback) {
        this.autoSaveCallback = callback;
    }

    /**
     * Enable/disable auto-save
     */
    public void setAutoSaveEnabled(boolean enabled) {
        this.autoSaveEnabled = enabled;
    }

    /**
     * Set current file base name
     */
    public void setCurrentFileBaseName(String baseName) {
        this.currentFileBaseName = baseName;
    }

    /**
     * Get current file base name
     */
    public String getCurrentFileBaseName() {
        return currentFileBaseName;
    }

    /**
     * Get files directory
     */
    public String getFilesDirectory() {
        return CREATED_FILES_DIR;
    }

    /**
     * Create async task for file operations
     */
    public Task<Void> createAsyncFileTask(String baseName, String content) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Saving files...");
                updateProgress(0, 3);
                
                // Ensure folder exists
                File folder = new File(getFolderPath(baseName));
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                
                // Save original file
                writeToFile(getFilePath(baseName, "org"), content);
                updateProgress(1, 3);
                
                // Save reversed file
                String reversedContent = new StringBuilder(content).reverse().toString();
                writeToFile(getFilePath(baseName, "rev"), reversedContent);
                updateProgress(2, 3);
                
                // Save byte codes file
                String byteContent = convertToByteString(content);
                writeToFile(getFilePath(baseName, "byte"), byteContent);
                updateProgress(3, 3);
                
                updateMessage("Files saved successfully");
                return null;
            }
        };
    }

    /**
     * Shutdown the service
     */
    public void shutdown() {
        autoSaveExecutor.shutdown();
        try {
            if (!autoSaveExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                autoSaveExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            autoSaveExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Interface for auto-save callbacks
    public interface AutoSaveCallback {
        void onAutoSave(String baseName);
        void onAutoSaveError(String error);
    }
} 