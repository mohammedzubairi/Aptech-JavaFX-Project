package com.codemavriks.aptech;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.*;

public class MainController {
    private static final String CREATED_FILES_DIR = "Created files/";
    private static final String ORIGINAL_FILE = CREATED_FILES_DIR + "original.txt";
    private static final String REVERSED_FILE = CREATED_FILES_DIR + "reversed.txt";
    private static final String BYTECODES_FILE = CREATED_FILES_DIR + "bytecodes.txt";

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
     * Handles file creation and content addition.
     * Saves the content from the TextArea to a file named 'original.txt'.
     */
    @FXML
    private void handleCreateFile() {
        String content = fileContentInputArea.getText();
        if (content == null || content.isEmpty()) {
            statusLabel.setText("Please enter some content to save.");
            return;
        }
        File dir = new File(CREATED_FILES_DIR);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(ORIGINAL_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            statusLabel.setText("File 'original.txt' created and content added.");
        } catch (IOException e) {
            statusLabel.setText("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Handles reversing the file content and saving to another file.
     * Reads from 'original.txt', reverses the content, and saves to 'reversed.txt'.
     */
    @FXML
    private void handleReverseFile() {
        File inputFile = new File(ORIGINAL_FILE);
        File outputFile = new File(REVERSED_FILE);
        if (!inputFile.exists()) {
            statusLabel.setText("'original.txt' does not exist. Please create the file first.");
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
            statusLabel.setText("Content reversed and saved to 'reversed.txt'.");
        } catch (IOException e) {
            statusLabel.setText("Error processing files: " + e.getMessage());
        }
    }

    /**
     * Handles comparing the original and reversed files.
     * Compares 'original.txt' and 'reversed.txt' and updates the status.
     */
    @FXML
    private void handleCompareFiles() {
        File file1 = new File(ORIGINAL_FILE);
        File file2 = new File(REVERSED_FILE);
        if (!file1.exists() || !file2.exists()) {
            statusLabel.setText("Both 'original.txt' and 'reversed.txt' must exist to compare.");
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
     */
    @FXML
    private void handleExtractReplace() {
        File file = new File(ORIGINAL_FILE);
        if (!file.exists()) {
            statusLabel.setText("'original.txt' does not exist. Please create the file first.");
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
     * Reads from 'original.txt', converts content to byte codes, saves to 'bytecodes.txt', and updates the status.
     */
    @FXML
    private void handleByteCode() {
        File inputFile = new File(ORIGINAL_FILE);
        File outputFile = new File(BYTECODES_FILE);
        if (!inputFile.exists()) {
            statusLabel.setText("'original.txt' does not exist. Please create the file first.");
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
            statusLabel.setText("File content converted to byte codes and saved to 'bytecodes.txt'.");
        } catch (IOException e) {
            statusLabel.setText("Error processing files: " + e.getMessage());
        }
    }

    @FXML
    private void handleShowFirstFile() {
        File file = new File(ORIGINAL_FILE);
        if (!file.exists()) {
            statusLabel.setText("'original.txt' does not exist. Please create the file first.");
            return;
        }
        try {
            String content = readFileContent(file);
            // Show in a new Alert dialog (new screen)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("First File Content");
            alert.setHeaderText("Content of the First File");
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
        dialog.setTitle("Create New File");
        dialog.setHeaderText("Enter the name for the new .txt file:");
        dialog.setContentText("File name:");
        ((TextInputDialog) dialog).getEditor().setPromptText("example.txt");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(name -> {
            if (name == null || name.trim().isEmpty() || !name.endsWith(".txt")) {
                statusLabel.setText("Invalid file name. Must end with .txt");
                return;
            }
            File file = new File(CREATED_FILES_DIR + name);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(fileContentInputArea.getText());
                statusLabel.setText("File '" + name + "' created.");
                populateFileList();
            } catch (IOException e) {
                statusLabel.setText("Error creating file: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleFileSelected() {
        String selected = fileListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        File file = new File(CREATED_FILES_DIR + selected);
        try {
            String content = readFileContent(file);
            fileDisplayArea.setText(content);
            saveFileButton.setVisible(false);
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