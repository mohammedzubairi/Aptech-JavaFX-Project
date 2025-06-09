package com.codemavriks.aptech.components;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import javafx.scene.layout.Region;

public class SidePanel extends VBox {
    private final Button newFileButton;
    private final Button saveButton;
    private final TextField wordIndexField;
    private final TextField replacementField;
    private final Button replaceButton;
    private final FileNavigator fileNavigator;
    private final Label statusLabel;
    private final BooleanProperty hasUnsavedChanges = new SimpleBooleanProperty(false);
    private final ObservableList<SidePanelListener> listeners = FXCollections.observableArrayList();

    public SidePanel(String workingDirectory) {
        this.newFileButton = new Button("New File");
        this.saveButton = new Button("Save");
        this.wordIndexField = new TextField();
        this.replacementField = new TextField();
        this.replaceButton = new Button("Replace Word");
        this.fileNavigator = new FileNavigator(workingDirectory);
        this.statusLabel = new Label("Ready");
        
        setupComponents();
        setupLayout();
        setupEventHandlers();
        
        getStyleClass().add("side-panel");
    }

    private void setupComponents() {
        // Style buttons
        newFileButton.getStyleClass().addAll("primary-button", "side-panel-button");
        saveButton.getStyleClass().addAll("accent-button", "side-panel-button");
        replaceButton.getStyleClass().addAll("secondary-button", "side-panel-button");
        
        // Set button properties
        newFileButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        replaceButton.setMaxWidth(Double.MAX_VALUE);
        
        // Bind save button state to unsaved changes
        saveButton.disableProperty().bind(hasUnsavedChanges.not());
        
        // Setup text fields with proper width settings
        wordIndexField.setPromptText("Word index (1, 2, 3...)");
        wordIndexField.getStyleClass().add("side-panel-field");
        wordIndexField.setMaxWidth(Double.MAX_VALUE);
        wordIndexField.setPrefWidth(Region.USE_COMPUTED_SIZE);
        
        replacementField.setPromptText("Replacement text");
        replacementField.getStyleClass().add("side-panel-field");
        replacementField.setMaxWidth(Double.MAX_VALUE);
        replacementField.setPrefWidth(Region.USE_COMPUTED_SIZE);
        
        // Status label
        statusLabel.getStyleClass().add("status-label");
        statusLabel.setWrapText(true);
    }

    private void setupLayout() {
        // Top section with buttons
        VBox buttonSection = new VBox(10);
        buttonSection.getStyleClass().add("button-section");
        buttonSection.setPadding(new Insets(15));
        buttonSection.setFillWidth(true);
        
        // Button container
        VBox buttonContainer = new VBox(8);
        buttonContainer.setFillWidth(true);
        buttonContainer.getChildren().addAll(newFileButton, saveButton);
        
        // Word replacement section
        VBox replacementSection = new VBox(8);
        replacementSection.setFillWidth(true);
        
        Label replacementLabel = new Label("Word Replacement:");
        replacementLabel.getStyleClass().add("section-title");
        
        replacementSection.getChildren().addAll(
            replacementLabel,
            wordIndexField,
            replacementField,
            replaceButton
        );
        
        // Status section
        VBox statusSection = new VBox(5);
        
        Label statusTitleLabel = new Label("Status:");
        statusTitleLabel.getStyleClass().add("section-title");
        
        statusSection.getChildren().addAll(
            new Separator(),
            statusTitleLabel,
            statusLabel
        );
        
        buttonSection.getChildren().addAll(buttonContainer, replacementSection, statusSection);
        
        // File navigator section
        VBox navigatorSection = new VBox();
        navigatorSection.getStyleClass().add("navigator-section");
        
        Label navigatorTitle = new Label("Files");
        navigatorTitle.getStyleClass().add("section-title");
        
        navigatorSection.getChildren().addAll(navigatorTitle, fileNavigator);
        VBox.setVgrow(fileNavigator, Priority.ALWAYS);
        
        // Add sections to main container
        getChildren().addAll(buttonSection, navigatorSection);
        VBox.setVgrow(navigatorSection, Priority.ALWAYS);
        
        // Set spacing and padding
        setSpacing(0);
        setPadding(new Insets(0));
    }

    private void setupEventHandlers() {
        // New file button
        newFileButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create New File Set");
            dialog.setHeaderText("Enter the base name for the new file set:");
            dialog.setContentText("Base name (without extension):");
            
            // Add placeholder text
            TextField textField = dialog.getEditor();
            textField.setPromptText("e.g., MyProject, Document1, etc.");
            
            // Apply dark theme to dialog
            com.codemavriks.aptech.MainApp.applyDarkThemeToDialog(dialog);
            
            dialog.showAndWait().ifPresent(baseName -> {
                if (!baseName.trim().isEmpty()) {
                    // Remove .txt extension if user accidentally added it
                    baseName = baseName.trim();
                    if (baseName.toLowerCase().endsWith(".txt")) {
                        baseName = baseName.substring(0, baseName.length() - 4);
                    }
                    notifyNewFileRequested(baseName);
                }
            });
        });

        // Save button
        saveButton.setOnAction(e -> {
            // Show save confirmation
            Alert saveAlert = new Alert(Alert.AlertType.INFORMATION);
            saveAlert.setTitle("Save File");
            saveAlert.setHeaderText("File Saved Successfully");
            saveAlert.setContentText("The file and its variants (org, rev, byte) have been updated.");
            
            // Apply dark theme to dialog
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(saveAlert);
            
            notifySaveRequested();
            
            // Show the alert after save operation
            saveAlert.showAndWait();
        });

        // Replace button
        replaceButton.setOnAction(e -> {
            String indexText = wordIndexField.getText().trim();
            String replacement = replacementField.getText().trim();
            
            if (indexText.isEmpty() || replacement.isEmpty()) {
                Alert errorAlert = new Alert(Alert.AlertType.WARNING);
                errorAlert.setTitle("Invalid Input");
                errorAlert.setHeaderText("Missing Information");
                errorAlert.setContentText("Please enter both word index and replacement text.");
                
                // Apply dark theme to dialog
                com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(errorAlert);
                
                errorAlert.showAndWait();
                return;
            }
            
            try {
                int wordIndex = Integer.parseInt(indexText);
                if (wordIndex < 1) {
                    throw new NumberFormatException("Index must be positive");
                }
                
                // Show confirmation dialog
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Replacement");
                confirmAlert.setHeaderText("Replace word at position " + wordIndex);
                confirmAlert.setContentText("Replace word at position " + wordIndex + " with: \"" + replacement + "\"?");
                
                // Apply dark theme to dialog
                com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmAlert);
                
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        notifyWordReplaceRequested(wordIndex, replacement);
                        // Clear fields after successful replacement
                        wordIndexField.clear();
                        replacementField.clear();
                    }
                });
                
            } catch (NumberFormatException ex) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Invalid Index");
                errorAlert.setHeaderText("Invalid Word Index");
                errorAlert.setContentText("Please enter a valid positive number for the word index.");
                
                // Apply dark theme to dialog
                com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(errorAlert);
                
                errorAlert.showAndWait();
            }
        });

        // File navigator events
        fileNavigator.addFileOperationListener(new FileNavigator.FileOperationListener() {
            @Override
            public void onFileSelected(File file) {
                updateStatus("Selected: " + file.getName());
                notifyFileSelected(file);
            }

            @Override
            public void onFileOpened(File file) {
                updateStatus("Opened: " + file.getName());
                notifyFileOpened(file);
            }

            @Override
            public void onFileDeleted(File file) {
                updateStatus("Deleted: " + file.getName());
                notifyFileDeleted(file);
            }

            @Override
            public void onFileRenamed(File oldFile, File newFile) {
                updateStatus("Renamed: " + oldFile.getName() + " → " + newFile.getName());
                notifyFileRenamed(oldFile, newFile);
            }

            @Override
            public void onNewFileRequested(String baseName) {
                notifyNewFileRequested(baseName);
            }
        });
    }

    // Public methods
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    public void setHasUnsavedChanges(boolean hasChanges) {
        hasUnsavedChanges.set(hasChanges);
        if (hasChanges) {
            updateStatus("Unsaved changes");
        } else {
            updateStatus("All changes saved");
        }
    }

    public void refreshFileNavigator() {
        fileNavigator.refreshTree();
    }

    public void selectFile(File file) {
        fileNavigator.selectFile(file);
    }

    // Event notification methods
    private void notifyNewFileRequested(String baseName) {
        listeners.forEach(listener -> listener.onNewFileRequested(baseName));
    }

    private void notifySaveRequested() {
        listeners.forEach(listener -> listener.onSaveRequested());
    }

    private void notifyWordReplaceRequested(int wordIndex, String replacement) {
        listeners.forEach(listener -> listener.onWordReplaceRequested(wordIndex, replacement));
    }

    private void notifyFileSelected(File file) {
        listeners.forEach(listener -> listener.onFileSelected(file));
    }

    private void notifyFileOpened(File file) {
        listeners.forEach(listener -> listener.onFileOpened(file));
    }

    private void notifyFileDeleted(File file) {
        listeners.forEach(listener -> listener.onFileDeleted(file));
    }

    private void notifyFileRenamed(File oldFile, File newFile) {
        listeners.forEach(listener -> listener.onFileRenamed(oldFile, newFile));
    }

    // Listener management
    public void addSidePanelListener(SidePanelListener listener) {
        listeners.add(listener);
    }

    public void removeSidePanelListener(SidePanelListener listener) {
        listeners.remove(listener);
    }

    // Interface for side panel callbacks
    public interface SidePanelListener {
        default void onNewFileRequested(String baseName) {}
        default void onSaveRequested() {}
        default void onWordReplaceRequested(int wordIndex, String replacement) {}
        default void onFileSelected(File file) {}
        default void onFileOpened(File file) {}
        default void onFileDeleted(File file) {}
        default void onFileRenamed(File oldFile, File newFile) {}
    }
} 