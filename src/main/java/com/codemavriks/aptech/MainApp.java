/**
 * File Mana - Modern Text Editor
 * Main Application Entry Point
 * 
 * This class serves as the primary entry point for the File Mana text editor application.
 * It extends JavaFX Application and handles the complete application lifecycle including
 * initialization, UI setup, theme application, icon management, and graceful shutdown.
 * 
 * Key Responsibilities:
 * - Application initialization and JavaFX stage setup
 * - CSS theme loading and application
 * - Icon management for title bars and taskbar
 * - Graceful shutdown handling with unsaved changes detection
 * - Dialog styling and theming utilities
 * 
 * Architecture Pattern: Entry Point / Bootstrap
 * - Initializes the main EditorController (MVC Controller)
 * - Sets up the primary scene and stage
 * - Provides static utilities for dialog theming
 * 
 * @author NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
 * @student_id Student1554163
 * @course Advanced Java Programming with JavaFX
 * @institution Aptech Computer Education
 * @university Alnasser University
 * @version 1.0
 * @since 2025-05-20
 */
package com.codemavriks.aptech;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;

/**
 * Main Application class for File Mana - Modern Text Editor
 * 
 * This class bootstraps the entire application and manages the primary JavaFX stage.
 * It follows the JavaFX Application lifecycle pattern and provides centralized
 * utilities for theming and icon management across the application.
 */
public class MainApp extends Application {
    
    /**
     * Main controller that coordinates all application components.
     * Manages the overall application state and component interactions.
     */
    private EditorController editorController;
    
    /**
     * Path to the CSS stylesheet for consistent application theming.
     * Used across all dialogs and components for dark theme consistency.
     */
    private static String cssPath;
    
    /**
     * Title bar icon (black logo variant).
     * Used for dialog title bars and window decorations.
     */
    private static Image titleBarIcon;
    
    /**
     * Taskbar icon (regular logo variant).
     * Used for taskbar representation and application identification.
     */
    private static Image taskbarIcon;

    /**
     * Primary application initialization method called by JavaFX framework.
     * Sets up the entire application UI, applies theming, and configures event handlers.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Store CSS path for use in dialogs throughout the application
        cssPath = getClass().getResource("/com/codemavriks/aptech/styles/modern-theme.css").toExternalForm();
        
        // Load application icons for different contexts
        loadIcons();
        
        // Create the main editor controller (MVC pattern)
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
        
        // Handle application close with unsaved changes detection
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

    /**
     * Loads application icons from resources.
     * Uses two different icon variants for different contexts.
     */
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

    /**
     * Sets appropriate icons for the main application stage.
     * Order matters: title bar icon first, then taskbar icon.
     * 
     * @param stage The stage to set icons for
     */
    private void setStageIcons(Stage stage) {
        if (titleBarIcon != null) {
            stage.getIcons().add(titleBarIcon); // Title bar icon (black logo)
        }
        if (taskbarIcon != null) {
            stage.getIcons().add(taskbarIcon); // Taskbar icon (regular logo)
        }
    }

    /**
     * Apply dark theme to any dialog and set the appropriate icon.
     * Static utility method for consistent theming across all dialogs.
     * 
     * @param dialog The dialog to apply dark theme styling to
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
     * Set icon for dialog windows using the title bar icon (black version).
     * 
     * @param dialog The dialog to set the icon for
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
     * Convenience method to apply dark theme to Alert dialogs specifically.
     * 
     * @param alert The alert dialog to apply dark theme styling to
     */
    public static void applyDarkThemeToAlert(Alert alert) {
        applyDarkThemeToDialog(alert);
        setDialogIcon(alert);
    }

    /**
     * Application cleanup method called during shutdown.
     * Ensures proper cleanup of all resources and services.
     */
    @Override
    public void stop() throws Exception {
        // Ensure proper cleanup
        if (editorController != null) {
            editorController.shutdown();
        }
        super.stop();
    }

    /**
     * Main method - Application entry point.
     * Launches the JavaFX application.
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
} 