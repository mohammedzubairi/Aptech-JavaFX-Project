package com.codemavriks.aptech;

import com.codemavriks.aptech.components.TextEditor;
import com.codemavriks.aptech.components.SidePanel;
import com.codemavriks.aptech.services.FileService;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class EditorController extends HBox {
    private final TextEditor textEditor;
    private final SidePanel sidePanel;
    private final FileService fileService;
    private Timer autoSaveTimer;
    private String currentFileBaseName = null;
    private boolean isAutoSaveEnabled = true;

    public EditorController() {
        this.textEditor = new TextEditor();
        this.sidePanel = new SidePanel("Created files/");
        this.fileService = new FileService();
        
        setupLayout();
        setupEventHandlers();
        setupAutoSave();
        
        getStyleClass().add("main-layout");
    }

    private void setupLayout() {
        // Set up responsive layout: 70% editor, 30% side panel
        HBox.setHgrow(textEditor, Priority.ALWAYS);
        
        // Add components to layout
        getChildren().addAll(sidePanel, textEditor);
        
        // Set spacing
        setSpacing(0);
    }

    private void setupEventHandlers() {
        // Side panel events
        sidePanel.addSidePanelListener(new SidePanel.SidePanelListener() {
            @Override
            public void onNewFileRequested(String baseName) {
                handleNewFile(baseName);
            }

            @Override
            public void onSaveRequested() {
                handleSave();
            }

            @Override
            public void onWordReplaceRequested(int wordIndex, String replacement) {
                handleWordReplacement(wordIndex, replacement);
            }

            @Override
            public void onFileSelected(File file) {
                handleFileSelection(file);
            }

            @Override
            public void onFileOpened(File file) {
                handleFileOpen(file);
            }

            @Override
            public void onFileDeleted(File file) {
                handleFileDeleted(file);
            }

            @Override
            public void onFileRenamed(File oldFile, File newFile) {
                handleFileRenamed(oldFile, newFile);
            }
        });

        // Text editor events
        textEditor.addEventHandler(TextEditor.SaveRequestEvent.SAVE_REQUEST, e -> handleSave());
        textEditor.addEventHandler(TextEditor.NewFileRequestEvent.NEW_FILE_REQUEST, e -> showNewFileDialog());

        // Track unsaved changes
        textEditor.hasUnsavedChangesProperty().addListener((obs, oldVal, newVal) -> {
            sidePanel.setHasUnsavedChanges(newVal);
        });

        // File service auto-save callback
        fileService.setAutoSaveCallback(new FileService.AutoSaveCallback() {
            @Override
            public void onAutoSave(String baseName) {
                Platform.runLater(() -> {
                    sidePanel.updateStatus("Auto-saved: " + baseName);
                    textEditor.markAsSaved();
                    sidePanel.refreshFileNavigator();
                });
            }

            @Override
            public void onAutoSaveError(String error) {
                Platform.runLater(() -> {
                    sidePanel.updateStatus("Auto-save error: " + error);
                });
            }
        });
    }

    private void setupAutoSave() {
        // Auto-save every 30 seconds
        autoSaveTimer = new Timer(true);
        autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isAutoSaveEnabled && currentFileBaseName != null && textEditor.hasUnsavedChangesProperty().get()) {
                    Platform.runLater(() -> {
                        try {
                            String content = textEditor.getContent();
                            fileService.updateFileSet(currentFileBaseName, content);
                            textEditor.markAsSaved();
                            sidePanel.updateStatus("Auto-saved: " + currentFileBaseName);
                            sidePanel.refreshFileNavigator();
                        } catch (IOException e) {
                            sidePanel.updateStatus("Auto-save failed: " + e.getMessage());
                        }
                    });
                }
            }
        }, 30000, 30000); // 30 seconds interval
    }

    private void handleNewFile(String baseName) {
        // Check for unsaved changes
        if (textEditor.hasUnsavedChangesProperty().get()) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Unsaved Changes");
            confirmDialog.setHeaderText("You have unsaved changes");
            confirmDialog.setContentText("Do you want to save your changes before creating a new file?");
            
            // Apply dark theme to dialog
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmDialog);
            
            ButtonType saveButton = new ButtonType("Save");
            ButtonType discardButton = new ButtonType("Discard");
            ButtonType cancelButton = new ButtonType("Cancel");
            
            confirmDialog.getButtonTypes().setAll(saveButton, discardButton, cancelButton);
            
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent()) {
                if (result.get() == saveButton) {
                    handleSave();
                } else if (result.get() == cancelButton) {
                    return; // Cancel operation
                }
                // If discard, continue with new file creation
            }
        }

        // Check if file already exists and create unique name
        String uniqueBaseName = getUniqueBaseName(baseName);
        if (!uniqueBaseName.equals(baseName)) {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("File Name Modified");
            infoAlert.setHeaderText("File already exists");
            infoAlert.setContentText("A file with the name '" + baseName + "' already exists.\nCreated with name: " + uniqueBaseName);
            
            // Apply dark theme to dialog
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(infoAlert);
            
            infoAlert.showAndWait();
        }

        // Clear the text editor content for new file
        String defaultContent = "// New file: " + uniqueBaseName + "\n// Start typing your content here...";

        try {
            fileService.createFileSet(uniqueBaseName, defaultContent);
            currentFileBaseName = uniqueBaseName;
            fileService.setCurrentFileBaseName(uniqueBaseName);
            
            // Clear and set default content in text editor
            textEditor.setContent(defaultContent);
            textEditor.setCurrentFilePath(fileService.getFilePath(uniqueBaseName, "org"));
            textEditor.markAsSaved();
            
            sidePanel.updateStatus("Created file set: " + uniqueBaseName);
            sidePanel.refreshFileNavigator();
            
            // Show success popup
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("File Created");
            successAlert.setHeaderText("File Set Created Successfully");
            successAlert.setContentText("Created files in folder '" + uniqueBaseName + "':\n• " + uniqueBaseName + "-org.txt\n• " + uniqueBaseName + "-rev.txt\n• " + uniqueBaseName + "-byte.txt");
            
            // Apply dark theme to dialog
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(successAlert);
            
            successAlert.showAndWait();
            
        } catch (IOException e) {
            sidePanel.updateStatus("Error creating file: " + e.getMessage());
            showErrorDialog("File Creation Error", "Failed to create file set: " + e.getMessage());
        }
    }

    private String getUniqueBaseName(String baseName) {
        String uniqueName = baseName;
        int counter = 1;
        
        while (fileService.fileSetExists(uniqueName)) {
            uniqueName = baseName + " (" + counter + ")";
            counter++;
        }
        
        return uniqueName;
    }

    private void handleSave() {
        if (currentFileBaseName == null) {
            showNewFileDialog();
            return;
        }

        String content = textEditor.getContent();
        
        // Create async task for saving
        Task<Void> saveTask = fileService.createAsyncFileTask(currentFileBaseName, content);
        
        saveTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                textEditor.markAsSaved();
                sidePanel.updateStatus("Saved: " + currentFileBaseName);
                sidePanel.refreshFileNavigator();
            });
        });
        
        saveTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                Throwable exception = saveTask.getException();
                sidePanel.updateStatus("Save failed: " + exception.getMessage());
                showErrorDialog("Save Error", "Failed to save files: " + exception.getMessage());
            });
        });
        
        // Run save task in background
        Thread saveThread = new Thread(saveTask);
        saveThread.setDaemon(true);
        saveThread.start();
    }

    private void handleFileSelection(File file) {
        // Update current file base name when a file is selected
        String baseName = fileService.extractBaseName(file.getAbsolutePath());
        if (!baseName.equals(currentFileBaseName)) {
            currentFileBaseName = baseName;
            fileService.setCurrentFileBaseName(baseName);
        }
    }

    private void handleFileOpen(File file) {
        // Check for unsaved changes before opening new file
        if (textEditor.hasUnsavedChangesProperty().get()) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Unsaved Changes");
            confirmDialog.setHeaderText("You have unsaved changes");
            confirmDialog.setContentText("Do you want to save your changes before opening another file?");
            
            // Apply dark theme to dialog
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmDialog);
            
            ButtonType saveButton = new ButtonType("Save");
            ButtonType discardButton = new ButtonType("Discard");
            ButtonType cancelButton = new ButtonType("Cancel");
            
            confirmDialog.getButtonTypes().setAll(saveButton, discardButton, cancelButton);
            
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent()) {
                if (result.get() == saveButton) {
                    handleSave();
                } else if (result.get() == cancelButton) {
                    return; // Cancel operation
                }
            }
        }

        try {
            // Load file content
            String content = fileService.readFile(file.getAbsolutePath());
            
            // Set content in text editor
            textEditor.setContent(content);
            textEditor.setCurrentFilePath(file.getAbsolutePath());
            
            // Update current file base name
            currentFileBaseName = fileService.extractBaseName(file.getAbsolutePath());
            fileService.setCurrentFileBaseName(currentFileBaseName);
            
            sidePanel.updateStatus("Opened: " + file.getName());
            
        } catch (IOException e) {
            sidePanel.updateStatus("Error opening file: " + e.getMessage());
            showErrorDialog("File Open Error", "Failed to open file: " + e.getMessage());
        }
    }

    private void handleFileDeleted(File file) {
        // If the deleted file/folder was related to the current file, clear the editor
        if (currentFileBaseName != null) {
            String deletedBaseName = null;
            
            // Check if a file from the current file set was deleted
            if (file.isFile()) {
                deletedBaseName = fileService.extractBaseName(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                // If a directory was deleted, check if it matches the current file base name
                deletedBaseName = file.getName();
            }
            
            // If the deleted item is related to the current file, clear the editor
            if (deletedBaseName != null && deletedBaseName.equals(currentFileBaseName)) {
                textEditor.clear();
                currentFileBaseName = null;
                fileService.setCurrentFileBaseName(null);
                sidePanel.updateStatus("File set deleted, editor cleared");
            }
        }
        sidePanel.refreshFileNavigator();
    }

    private void handleFileRenamed(File oldFile, File newFile) {
        // Update current file base name if the renamed file was the current file
        if (currentFileBaseName != null && oldFile.getName().contains(currentFileBaseName)) {
            String newBaseName = fileService.extractBaseName(newFile.getAbsolutePath());
            currentFileBaseName = newBaseName;
            fileService.setCurrentFileBaseName(newBaseName);
            textEditor.setCurrentFilePath(newFile.getAbsolutePath());
            sidePanel.updateStatus("File renamed to: " + newFile.getName());
        }
        sidePanel.refreshFileNavigator();
    }

    private void showNewFileDialog() {
        sidePanel.addSidePanelListener(new SidePanel.SidePanelListener() {
            @Override
            public void onNewFileRequested(String baseName) {
                handleNewFile(baseName);
            }
        });
    }

    private void showErrorDialog(String title, String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(errorAlert);
        
        errorAlert.showAndWait();
    }

    private void handleWordReplacement(int wordIndex, String replacement) {
        if (currentFileBaseName == null) {
            sidePanel.updateStatus("No file is currently open");
            showErrorDialog("No File Open", "Please open a file before attempting word replacement.");
            return;
        }

        String content = textEditor.getContent();
        if (content.trim().isEmpty()) {
            sidePanel.updateStatus("File is empty");
            showErrorDialog("Empty File", "The current file is empty. No words to replace.");
            return;
        }

        // Split content into words
        String[] words = content.split("\\s+");
        
        if (wordIndex < 1 || wordIndex > words.length) {
            sidePanel.updateStatus("Invalid word index: " + wordIndex);
            showErrorDialog("Invalid Word Index", 
                "Word index " + wordIndex + " is out of range. The file contains " + words.length + " words.");
            return;
        }

        // Replace the word at the specified index
        String originalWord = words[wordIndex - 1];
        words[wordIndex - 1] = replacement;
        
        // Reconstruct the content
        String newContent = String.join(" ", words);
        
        // Update the text editor
        textEditor.setContent(newContent);
        
        sidePanel.updateStatus("Replaced word '" + originalWord + "' at position " + wordIndex + " with '" + replacement + "'");
        
        // Show success message
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Word Replaced");
        successAlert.setHeaderText("Word Replacement Successful");
        successAlert.setContentText("Replaced word '" + originalWord + "' at position " + wordIndex + " with '" + replacement + "'");
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(successAlert);
        
        successAlert.showAndWait();
    }

    // Public methods for external control
    public void setAutoSaveEnabled(boolean enabled) {
        this.isAutoSaveEnabled = enabled;
        fileService.setAutoSaveEnabled(enabled);
    }

    public void shutdown() {
        if (autoSaveTimer != null) {
            autoSaveTimer.cancel();
        }
        fileService.shutdown();
    }

    public TextEditor getTextEditor() {
        return textEditor;
    }

    public SidePanel getSidePanel() {
        return sidePanel;
    }

    public String getCurrentFileBaseName() {
        return currentFileBaseName;
    }

    public boolean hasUnsavedChanges() {
        return textEditor.hasUnsavedChangesProperty().get();
    }
} 