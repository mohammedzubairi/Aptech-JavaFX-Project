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

public class FileService {
    private static final String CREATED_FILES_DIR = "Created files/";
    private final ScheduledExecutorService autoSaveExecutor;
    private boolean autoSaveEnabled = true;
    private String currentFileBaseName = null;
    private String lastSavedContent = "";
    private AutoSaveCallback autoSaveCallback;

    public FileService() {
        this.autoSaveExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "AutoSave-Thread");
            t.setDaemon(true);
            return t;
        });
        
        // Ensure directory exists
        createDirectoryIfNotExists();
    }

    private void createDirectoryIfNotExists() {
        File dir = new File(CREATED_FILES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Generate file path based on base name and type (now with folder grouping)
     */
    public String getFilePath(String baseName, String type) {
        return CREATED_FILES_DIR + baseName + "/" + baseName + "-" + type + ".txt";
    }

    /**
     * Get folder path for a file set
     */
    public String getFolderPath(String baseName) {
        return CREATED_FILES_DIR + baseName + "/";
    }

    /**
     * Extract base name from file path
     */
    public String extractBaseName(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        String parentName = file.getParentFile().getName();
        
        // If the parent is the base name folder, use that
        if (!parentName.equals("Created files")) {
            return parentName;
        }
        
        // Fallback to old method
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

    /**
     * Check if a file set exists
     */
    public boolean fileSetExists(String baseName) {
        File folder = new File(getFolderPath(baseName));
        return folder.exists() && folder.isDirectory();
    }

    /**
     * Create a new file set with all three variants in a folder
     */
    public void createFileSet(String baseName, String content) throws IOException {
        createDirectoryIfNotExists();
        
        // Create folder for the file set
        File folder = new File(getFolderPath(baseName));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        // Create original file
        writeToFile(getFilePath(baseName, "org"), content);
        
        // Create reversed file
        String reversedContent = new StringBuilder(content).reverse().toString();
        writeToFile(getFilePath(baseName, "rev"), reversedContent);
        
        // Create byte codes file
        String byteContent = convertToByteString(content);
        writeToFile(getFilePath(baseName, "byte"), byteContent);
        
        currentFileBaseName = baseName;
        lastSavedContent = content;
    }

    /**
     * Update all files in a file set
     */
    public void updateFileSet(String baseName, String content) throws IOException {
        createDirectoryIfNotExists();
        
        // Ensure folder exists
        File folder = new File(getFolderPath(baseName));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        // Update original file
        writeToFile(getFilePath(baseName, "org"), content);
        
        // Update reversed file
        String reversedContent = new StringBuilder(content).reverse().toString();
        writeToFile(getFilePath(baseName, "rev"), reversedContent);
        
        // Update byte codes file
        String byteContent = convertToByteString(content);
        writeToFile(getFilePath(baseName, "byte"), byteContent);
        
        lastSavedContent = content;
    }

    /**
     * Delete entire file set (folder and all files)
     */
    public boolean deleteFileSet(String baseName) {
        File folder = new File(getFolderPath(baseName));
        return deleteDirectory(folder);
    }

    /**
     * Delete a directory and all its contents
     */
    private boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            return directory.delete();
        }
        return false;
    }

    /**
     * Read content from a file
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
     * Write content to a file
     */
    private void writeToFile(String filePath, String content) throws IOException {
        // Ensure parent directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    /**
     * Convert string content to byte representation
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