package com.codemavriks.aptech;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.*;

/**
 * @deprecated This class has been replaced by the new modular architecture.
 * Use EditorController with TextEditor, SidePanel, and FileNavigator components instead.
 * 
 * The new architecture provides:
 * - Better separation of concerns
 * - Modular, reusable components
 * - Modern UI with VSCode-like file navigator
 * - Auto-save functionality
 * - Responsive design
 * - Better error handling
 * 
 * @see EditorController
 * @see com.codemavriks.aptech.components.TextEditor
 * @see com.codemavriks.aptech.components.SidePanel
 * @see com.codemavriks.aptech.components.FileNavigator
 * @see com.codemavriks.aptech.services.FileService
 */
@Deprecated
public class MainController {
    private static final String CREATED_FILES_DIR = "Created files/";
    // Remove hardcoded file constants as we'll use dynamic naming
    
    // Add field to store current file base name
    private String currentFileBaseName = null;

    @FXML private TextArea fileContentInputArea;
    @FXML private TextArea fileDisplayArea;
    @FXML private Button saveFileButton;
    @FXML private ListView<String> fileListView;
    @FXML private TextField extractPositionField;
    @FXML private TextField replacementField;
    @FXML private Label statusLabel;
    // Add more FXML fields as needed

    // Methods for handling UI actions will be implemented here

    @FXML
    public void initialize() {
        populateFileList();
        saveFileButton.setVisible(false);
        fileDisplayArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (fileListView.getSelectionModel().getSelectedItem() != null) {
                saveFileButton.setVisible(true);
            }
        });
    }

    private void populateFileList() {
        File dir = new File(CREATED_FILES_DIR);
        if (!dir.exists()) dir.mkdirs();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        fileListView.getItems().clear();
        if (files != null) {
            for (File f : files) {
                fileListView.getItems().add(f.getName());
            }
        }
    }

    /**
     * Utility method to generate file paths based on base name and type
     */
    private String getFilePath(String baseName, String type) {
        return CREATED_FILES_DIR + baseName + "-" + type + ".txt";
    }

    /**
     * Handles file creation with new naming convention.
     * Creates three files simultaneously: baseName-org.txt, baseName-rev.txt, baseName-byte.txt
     */
    @FXML
    private void handleCreateFile() {
        String content = fileContentInputArea.getText();
        if (content == null || content.isEmpty()) {
            statusLabel.setText("Please enter some content to save.");
            return;
        }
        
        if (currentFileBaseName == null || currentFileBaseName.isEmpty()) {
            statusLabel.setText("Please create a file set first using 'Create File' button.");
            return;
        }
        
        File dir = new File(CREATED_FILES_DIR);
        if (!dir.exists()) dir.mkdirs();
        
        try {
            // Create original file
            File originalFile = new File(getFilePath(currentFileBaseName, "org"));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(originalFile))) {
                writer.write(content);
            }
            
            // Create reversed file
            String reversedContent = new StringBuilder(content).reverse().toString();
            File reversedFile = new File(getFilePath(currentFileBaseName, "rev"));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(reversedFile))) {
                writer.write(reversedContent);
            }
            
            // Create byte codes file
            byte[] bytes = content.getBytes();
            StringBuilder byteCodes = new StringBuilder();
            for (byte b : bytes) {
                byteCodes.append(b).append(" ");
            }
            File byteFile = new File(getFilePath(currentFileBaseName, "byte"));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(byteFile))) {
                writer.write(byteCodes.toString().trim());
            }
            
            statusLabel.setText("File set '" + currentFileBaseName + "' created with all three variants.");
            populateFileList();
            
        } catch (IOException e) {
            statusLabel.setText("Error creating files: " + e.getMessage());
        }
    }

    /**
     * Handles reversing the file content and saving to another file.
     * Now works with the new naming convention.
     */
    @FXML
    private void handleReverseFile() {
        if (currentFileBaseName == null || currentFileBaseName.isEmpty()) {
            statusLabel.setText("No file set selected. Please create or select a file set first.");
            return;
        }
        
        File inputFile = new File(getFilePath(currentFileBaseName, "org"));
        File outputFile = new File(getFilePath(currentFileBaseName, "rev"));
        
        if (!inputFile.exists()) {
            statusLabel.setText("Original file does not exist. Please create the file set first.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            if (content.length() > 0) {
                content.setLength(content.length() - System.lineSeparator().length());
            }
            String reversed = content.reverse().toString();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(reversed);
            }
            statusLabel.setText("Content reversed and saved to '" + currentFileBaseName + "-rev.txt'.");
            populateFileList();
        } catch (IOException e) {
            statusLabel.setText("Error processing files: " + e.getMessage());
        }
    }

    /**
     * Handles comparing the original and reversed files.
     * Updated to work with new naming convention.
     */
    @FXML
    private void handleCompareFiles() {
        if (currentFileBaseName == null || currentFileBaseName.isEmpty()) {
            statusLabel.setText("No file set selected. Please create or select a file set first.");
            return;
        }
        
        File file1 = new File(getFilePath(currentFileBaseName, "org"));
        File file2 = new File(getFilePath(currentFileBaseName, "rev"));
        
        if (!file1.exists() || !file2.exists()) {
            statusLabel.setText("Both original and reversed files must exist to compare.");
            return;
        }
        
        try {
            String content1 = readFileContent(file1);
            String content2 = readFileContent(file2);
            if (content1.equals(content2)) {
                statusLabel.setText("The contents of both files are the SAME.");
            } else {
                statusLabel.setText("The contents of both files are DIFFERENT.");
            }
        } catch (IOException e) {
            statusLabel.setText("Error comparing files: " + e.getMessage());
        }
    }

    /**
     * Utility method to read the entire content of a file as a String.
     */
    private String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    /**
     * Handles extracting a word at a given position and replacing it.
     * Updated to work with new naming convention.
     */
    @FXML
    private void handleExtractReplace() {
        if (currentFileBaseName == null || currentFileBaseName.isEmpty()) {
            statusLabel.setText("No file set selected. Please create or select a file set first.");
            return;
        }
        
        File file = new File(getFilePath(currentFileBaseName, "org"));
        if (!file.exists()) {
            statusLabel.setText("Original file does not exist. Please create the file set first.");
            return;
        }
        
        String posText = extractPositionField.getText();
        String replacement = replacementField.getText();
        if (posText == null || posText.isEmpty() || replacement == null || replacement.isEmpty()) {
            statusLabel.setText("Please enter both position and replacement.");
            return;
        }
        
        int pos;
        try {
            pos = Integer.parseInt(posText);
        } catch (NumberFormatException e) {
            statusLabel.setText("Position must be a valid integer.");
            return;
        }
        
        try {
            String content = readFileContent(file).trim();
            String[] words = content.split("\\s+");
            if (pos < 1 || pos > words.length) {
                statusLabel.setText("Position out of range. There are only " + words.length + " words.");
                return;
            }
            words[pos - 1] = replacement;
            String newContent = String.join(" ", words);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(newContent);
            }
            fileDisplayArea.setText(newContent);
            statusLabel.setText("Word at position " + pos + " replaced with '" + replacement + "'.");
        } catch (IOException e) {
            statusLabel.setText("Error processing file: " + e.getMessage());
        }
    }

    /**
     * Handles converting file content to byte codes and saving.
     * Updated to work with new naming convention.
     */
    @FXML
    private void handleByteCode() {
        if (currentFileBaseName == null || currentFileBaseName.isEmpty()) {
            statusLabel.setText("No file set selected. Please create or select a file set first.");
            return;
        }
        
        File inputFile = new File(getFilePath(currentFileBaseName, "org"));
        File outputFile = new File(getFilePath(currentFileBaseName, "byte"));
        
        if (!inputFile.exists()) {
            statusLabel.setText("Original file does not exist. Please create the file set first.");
            return;
        }
        
        try {
            String content = readFileContent(inputFile);
            byte[] bytes = content.getBytes();
            StringBuilder byteCodes = new StringBuilder();
            for (byte b : bytes) {
                byteCodes.append(b).append(" ");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(byteCodes.toString().trim());
            }
            statusLabel.setText("File content converted to byte codes and saved to '" + currentFileBaseName + "-byte.txt'.");
            populateFileList();
        } catch (IOException e) {
            statusLabel.setText("Error processing files: " + e.getMessage());
        }
    }

    @FXML
    private void handleShowFirstFile() {
        if (currentFileBaseName == null || currentFileBaseName.isEmpty()) {
            statusLabel.setText("No file set selected. Please create or select a file set first.");
            return;
        }
        
        File file = new File(getFilePath(currentFileBaseName, "org"));
        if (!file.exists()) {
            statusLabel.setText("Original file does not exist. Please create the file set first.");
            return;
        }
        
        try {
            String content = readFileContent(file);
            // Show in a new Alert dialog (new screen)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("First File Content");
            alert.setHeaderText("Content of " + currentFileBaseName + "-org.txt");
            TextArea area = new TextArea(content);
            area.setEditable(false);
            area.setWrapText(true);
            area.setPrefWidth(400);
            area.setPrefHeight(300);
            alert.getDialogPane().setContent(area);
            alert.showAndWait();
        } catch (IOException e) {
            statusLabel.setText("Error reading file: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateFilePopup() {
        Dialog<String> dialog = new TextInputDialog();
        dialog.setTitle("Create New File Set");
        dialog.setHeaderText("Enter the base name for the new file set:");
        dialog.setContentText("Base name (without extension):");
        ((TextInputDialog) dialog).getEditor().setPromptText("Ahmed");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(baseName -> {
            if (baseName == null || baseName.trim().isEmpty()) {
                statusLabel.setText("Invalid base name. Please enter a valid name.");
                return;
            }
            
            // Remove .txt extension if user accidentally added it
            baseName = baseName.trim();
            if (baseName.toLowerCase().endsWith(".txt")) {
                baseName = baseName.substring(0, baseName.length() - 4);
            }
            
            // Set current file base name
            currentFileBaseName = baseName;
            
            String content = fileContentInputArea.getText();
            if (content == null || content.isEmpty()) {
                statusLabel.setText("File set '" + baseName + "' selected. Add content and click 'Create File' to generate all three files.");
                return;
            }
            
            // Create all three files immediately if content exists
            File dir = new File(CREATED_FILES_DIR);
            if (!dir.exists()) dir.mkdirs();
            
            try {
                // Create original file
                File originalFile = new File(getFilePath(baseName, "org"));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(originalFile))) {
                    writer.write(content);
                }
                
                // Create reversed file
                String reversedContent = new StringBuilder(content).reverse().toString();
                File reversedFile = new File(getFilePath(baseName, "rev"));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(reversedFile))) {
                    writer.write(reversedContent);
                }
                
                // Create byte codes file
                byte[] bytes = content.getBytes();
                StringBuilder byteCodes = new StringBuilder();
                for (byte b : bytes) {
                    byteCodes.append(b).append(" ");
                }
                File byteFile = new File(getFilePath(baseName, "byte"));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(byteFile))) {
                    writer.write(byteCodes.toString().trim());
                }
                
                statusLabel.setText("File set '" + baseName + "' created successfully with all three variants!");
                populateFileList();
                
            } catch (IOException e) {
                statusLabel.setText("Error creating file set: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleFileSelected() {
        String selected = fileListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        // Extract base name from selected file
        if (selected.contains("-org.txt")) {
            currentFileBaseName = selected.replace("-org.txt", "");
        } else if (selected.contains("-rev.txt")) {
            currentFileBaseName = selected.replace("-rev.txt", "");
        } else if (selected.contains("-byte.txt")) {
            currentFileBaseName = selected.replace("-byte.txt", "");
        } else {
            // Handle legacy files or other formats
            currentFileBaseName = selected.replace(".txt", "");
        }
        
        File file = new File(CREATED_FILES_DIR + selected);
        try {
            String content = readFileContent(file);
            fileDisplayArea.setText(content);
            saveFileButton.setVisible(false);
            statusLabel.setText("Selected file set: " + currentFileBaseName);
        } catch (IOException e) {
            statusLabel.setText("Error reading file: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveFile() {
        String selected = fileListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        File file = new File(CREATED_FILES_DIR + selected);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileDisplayArea.getText());
            statusLabel.setText("File '" + selected + "' saved.");
            saveFileButton.setVisible(false);
        } catch (IOException e) {
            statusLabel.setText("Error saving file: " + e.getMessage());
        }
    }
} 