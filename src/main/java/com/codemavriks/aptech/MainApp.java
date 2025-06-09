package com.codemavriks.aptech;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;

public class MainApp extends Application {
    private EditorController editorController;
    private static String cssPath;
    private static Image titleBarIcon;
    private static Image taskbarIcon;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Store CSS path for use in dialogs
        cssPath = getClass().getResource("/com/codemavriks/aptech/styles/modern-theme.css").toExternalForm();
        
        // Load icons
        loadIcons();
        
        // Create the main editor controller
        editorController = new EditorController();
        
        // Create scene with responsive sizing
        Scene scene = new Scene(editorController, 1200, 800);
        
        // Apply modern theme CSS
        scene.getStylesheets().add(cssPath);
        
        // Add application root style class
        editorController.getStyleClass().add("application-root");
        
        // Configure stage
        primaryStage.setTitle("File Mana - Modern Text Editor");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Set application icons
        setStageIcons(primaryStage);
        
        // Handle application close
        primaryStage.setOnCloseRequest(event -> {
            // Check for unsaved changes
            if (editorController.hasUnsavedChanges()) {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Unsaved Changes");
                confirmDialog.setHeaderText("You have unsaved changes");
                confirmDialog.setContentText("Do you want to save your changes before exiting?");
                
                // Apply dark theme and icon to dialog
                applyDarkThemeToDialog(confirmDialog);
                setDialogIcon(confirmDialog);
                
                javafx.scene.control.ButtonType saveButton = new javafx.scene.control.ButtonType("Save & Exit");
                javafx.scene.control.ButtonType discardButton = new javafx.scene.control.ButtonType("Exit Without Saving");
                javafx.scene.control.ButtonType cancelButton = new javafx.scene.control.ButtonType("Cancel");
                
                confirmDialog.getButtonTypes().setAll(saveButton, discardButton, cancelButton);
                
                java.util.Optional<javafx.scene.control.ButtonType> result = confirmDialog.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == saveButton) {
                        // Save and exit
                        editorController.getSidePanel().addSidePanelListener(new com.codemavriks.aptech.components.SidePanel.SidePanelListener() {
                            @Override
                            public void onSaveRequested() {
                                Platform.runLater(() -> {
                                    editorController.shutdown();
                                    Platform.exit();
                                });
                            }
                        });
                        // Trigger save
                        return; // Don't close yet, wait for save completion
                    } else if (result.get() == cancelButton) {
                        event.consume(); // Cancel the close operation
                        return;
                    }
                    // If discard, continue with shutdown
                }
            }
            
            // Shutdown services
            editorController.shutdown();
        });
        
        // Show the stage
        primaryStage.show();
        
        // Focus on the text editor
        Platform.runLater(() -> {
            editorController.getTextEditor().requestFocus();
        });
    }

    private void loadIcons() {
        try {
            // Load title bar icon (black logo for title bars and dialogs)
            titleBarIcon = new Image(getClass().getResourceAsStream("/com/codemavriks/aptech/File-Mana-Logo-Black.png"));
        } catch (Exception e) {
            System.err.println("Could not load title bar icon: " + e.getMessage());
        }
        
        try {
            // Load taskbar icon (regular logo for taskbar)
            taskbarIcon = new Image(getClass().getResourceAsStream("/com/codemavriks/aptech/File-Mana-Logo.png"));
        } catch (Exception e) {
            System.err.println("Could not load taskbar icon: " + e.getMessage());
        }
    }

    private void setStageIcons(Stage stage) {
        if (titleBarIcon != null) {
            stage.getIcons().add(titleBarIcon); // Title bar icon (black logo)
        }
        if (taskbarIcon != null) {
            stage.getIcons().add(taskbarIcon); // Taskbar icon (regular logo)
        }
    }

    /**
     * Apply dark theme to any dialog and set the appropriate icon
     */
    public static void applyDarkThemeToDialog(Dialog<?> dialog) {
        if (cssPath != null && dialog.getDialogPane() != null) {
            dialog.getDialogPane().getStylesheets().add(cssPath);
            dialog.getDialogPane().getStyleClass().add("styled-dialog");
            
            // Apply dark theme to the dialog's stage if available
            if (dialog.getDialogPane().getScene() != null && 
                dialog.getDialogPane().getScene().getWindow() instanceof Stage) {
                Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
                dialogStage.getScene().getStylesheets().add(cssPath);
                
                // Set dialog icon
                setDialogIcon(dialog);
            }
        }
    }

    /**
     * Set icon for dialog windows
     */
    public static void setDialogIcon(Dialog<?> dialog) {
        if (titleBarIcon != null && dialog.getDialogPane().getScene() != null && 
            dialog.getDialogPane().getScene().getWindow() instanceof Stage) {
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().clear();
            dialogStage.getIcons().add(titleBarIcon);
        }
    }

    /**
     * Apply dark theme to any alert and set the appropriate icon
     */
    public static void applyDarkThemeToAlert(Alert alert) {
        applyDarkThemeToDialog(alert);
        setDialogIcon(alert);
    }

    @Override
    public void stop() throws Exception {
        // Ensure proper cleanup
        if (editorController != null) {
            editorController.shutdown();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 