/**
 * File Mana - Modern Text Editor
 * Main Controller Class (MVC Pattern)
 * 
 * This class serves as the central controller in the MVC architecture, coordinating
 * between the UI components (TextEditor, SidePanel) and the business logic (FileService).
 * It manages the complete application workflow including file operations, text processing,
 * auto-save functionality, and user interaction handling.
 * 
 * Key Responsibilities:
 * - Coordinate between UI components and business logic
 * - Handle file operations (create, open, save, delete, rename)
 * - Manage text processing (word replacement, content reversal, byte conversion)
 * - Implement auto-save functionality with configurable intervals
 * - Provide error handling and user feedback
 * - Manage application state and lifecycle
 * 
 * Architecture Pattern: Model-View-Controller (MVC)
 * - View: TextEditor and SidePanel components
 * - Model: FileService and file system data
 * - Controller: This class (EditorController)
 * 
 * Design Patterns Used:
 * - Observer Pattern: Event listeners for component communication
 * - Command Pattern: Action handlers for user operations
 * - Strategy Pattern: Different file operation strategies
 * 
 * 
 */
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

/**
 * Main Controller class for File Mana text editor application.
 * 
 * This class extends HBox to serve as both the main layout container and the
 * central controller. It coordinates between the UI components and file services,
 * handling user interactions and maintaining application state.
 * 
 * Component Architecture:
 * ┌─────────────────────────────────────────────────────────┐
 * │                EditorController (HBox)                  │
 * ├─────────────────────┬───────────────────────────────────┤
 * │     SidePanel       │         TextEditor                │
 * │   (30% width)       │       (70% width)                 │
 * │                     │                                   │
 * │ - File Navigator    │ - Text Editing Area               │
 * │ - Control Panel     │ - Syntax Highlighting             │
 * │ - Status Display    │ - Auto-save Indicators            │
 * └─────────────────────┴───────────────────────────────────┘
 * 
 * Event Flow:
 * User Action → SidePanel/TextEditor → EditorController → FileService → File System
 *                                   ↓
 * UI Updates ← Status Updates ← Controller ← Service Response
 */
public class EditorController extends HBox {
    
    // ============================================================================
    // COMPONENT REFERENCES (MVC Architecture)
    // ============================================================================
    
    /**
     * Text editing component (View in MVC).
     * Handles all text input, editing, and display functionality.
     */
    private final TextEditor textEditor;
    
    /**
     * Side panel component (View in MVC).
     * Contains file navigator, control buttons, and status display.
     */
    private final SidePanel sidePanel;
    
    /**
     * File service component (Model in MVC).
     * Handles all file system operations and business logic.
     */
    private final FileService fileService;
    
    // ============================================================================
    // STATE MANAGEMENT VARIABLES
    // ============================================================================
    
    /**
     * Timer for auto-save functionality.
     * Runs in background thread to periodically save unsaved changes.
     */
    private Timer autoSaveTimer;
    
    /**
     * Base name of the currently open file set.
     * Used to track which file set is currently being edited.
     * Format: "filename" (without extension or suffix)
     */
    private String currentFileBaseName = null;
    
    /**
     * Flag to control auto-save functionality.
     * Can be disabled for performance or user preference.
     */
    private boolean isAutoSaveEnabled = true;

    // ============================================================================
    // CONSTRUCTOR AND INITIALIZATION
    // ============================================================================

    /**
     * Constructor - Initializes the main controller and all components.
     * 
     * Initialization Sequence:
     * 1. Create UI components (TextEditor, SidePanel)
     * 2. Create business logic service (FileService)
     * 3. Set up responsive layout
     * 4. Configure event handlers for component communication
     * 5. Start auto-save timer
     * 6. Apply CSS styling
     */
    public EditorController() {
        // Initialize core components
        this.textEditor = new TextEditor();
        this.sidePanel = new SidePanel("Created files/");  // Default file directory
        this.fileService = new FileService();
        
        // Set up the UI layout and component relationships
        setupLayout();
        
        // Configure event handlers for component communication
        setupEventHandlers();
        
        // Start auto-save functionality
        setupAutoSave();
        
        // Apply CSS styling for the main layout
        getStyleClass().add("main-layout");
    }

    // ============================================================================
    // LAYOUT CONFIGURATION
    // ============================================================================

    /**
     * Sets up the responsive layout for the main application window.
     * 
     * Layout Strategy:
     * - SidePanel: Fixed 30% width with file navigator and controls
     * - TextEditor: Flexible 70% width that grows with window resize
     * - No spacing between components for seamless appearance
     * 
     * Responsive Behavior:
     * - TextEditor grows/shrinks with window resize
     * - SidePanel maintains consistent width
     * - Minimum window size enforced by MainApp
     */
    private void setupLayout() {
        // Configure text editor to grow and fill available space
        HBox.setHgrow(textEditor, Priority.ALWAYS);
        
        // Add components to layout in order: SidePanel, TextEditor
        getChildren().addAll(sidePanel, textEditor);
        
        // Set spacing to 0 for seamless appearance
        setSpacing(0);
    }

    // ============================================================================
    // EVENT HANDLER CONFIGURATION
    // ============================================================================

    /**
     * Configures all event handlers for component communication.
     * 
     * This method sets up the Observer pattern implementation that allows
     * components to communicate without tight coupling. It establishes:
     * 
     * 1. SidePanel event listeners for file operations
     * 2. TextEditor event listeners for editor actions
     * 3. FileService callbacks for async operations
     * 4. Property change listeners for state synchronization
     */
    private void setupEventHandlers() {
        // ========================================================================
        // SIDE PANEL EVENT LISTENERS
        // ========================================================================
        
        // Set up listener for side panel events using Observer pattern
        sidePanel.addSidePanelListener(new SidePanel.SidePanelListener() {
            /**
             * Handles new file creation requests from the side panel.
             * @param baseName The base name for the new file set
             */
            @Override
            public void onNewFileRequested(String baseName) {
                handleNewFile(baseName);
            }

            /**
             * Handles save requests from the side panel.
             */
            @Override
            public void onSaveRequested() {
                handleSave();
            }

            /**
             * Handles word replacement requests from the side panel.
             * @param wordIndex The 1-based position of the word to replace
             * @param replacement The new word to insert
             */
            @Override
            public void onWordReplaceRequested(int wordIndex, String replacement) {
                handleWordReplacement(wordIndex, replacement);
            }

            /**
             * Handles file selection in the file navigator.
             * @param file The selected file
             */
            @Override
            public void onFileSelected(File file) {
                handleFileSelection(file);
            }

            /**
             * Handles file open requests from the file navigator.
             * @param file The file to open
             */
            @Override
            public void onFileOpened(File file) {
                handleFileOpen(file);
            }

            /**
             * Handles file deletion requests from the file navigator.
             * @param file The file to delete
             */
            @Override
            public void onFileDeleted(File file) {
                handleFileDeleted(file);
            }

            /**
             * Handles file rename operations from the file navigator.
             * @param oldFile The original file
             * @param newFile The renamed file
             */
            @Override
            public void onFileRenamed(File oldFile, File newFile) {
                handleFileRenamed(oldFile, newFile);
            }
        });

        // ========================================================================
        // TEXT EDITOR EVENT LISTENERS
        // ========================================================================

        // Handle save requests from text editor (Ctrl+S shortcut)
        textEditor.addEventHandler(TextEditor.SaveRequestEvent.SAVE_REQUEST, e -> handleSave());
        
        // Handle new file requests from text editor (Ctrl+N shortcut)
        textEditor.addEventHandler(TextEditor.NewFileRequestEvent.NEW_FILE_REQUEST, e -> showNewFileDialog());

        // Track unsaved changes and update side panel status
        textEditor.hasUnsavedChangesProperty().addListener((obs, oldVal, newVal) -> {
            sidePanel.setHasUnsavedChanges(newVal);
        });

        // ========================================================================
        // FILE SERVICE CALLBACKS
        // ========================================================================

        // Set up callbacks for file service auto-save operations
        fileService.setAutoSaveCallback(new FileService.AutoSaveCallback() {
            /**
             * Called when auto-save completes successfully.
             * @param baseName The base name of the saved file set
             */
            @Override
            public void onAutoSave(String baseName) {
                // Update UI on JavaFX Application Thread
                Platform.runLater(() -> {
                    sidePanel.updateStatus("Auto-saved: " + baseName);
                    textEditor.markAsSaved();
                    sidePanel.refreshFileNavigator();
                });
            }

            /**
             * Called when auto-save encounters an error.
             * @param error The error message
             */
            @Override
            public void onAutoSaveError(String error) {
                // Update UI on JavaFX Application Thread
                Platform.runLater(() -> {
                    sidePanel.updateStatus("Auto-save error: " + error);
                });
            }
        });
    }

    // ============================================================================
    // AUTO-SAVE FUNCTIONALITY
    // ============================================================================

    /**
     * Sets up the auto-save timer for periodic saving of unsaved changes.
     * 
     * Auto-save Strategy:
     * - Runs every 30 seconds in a background daemon thread
     * - Only saves if there are unsaved changes
     * - Only saves if a file is currently open
     * - Can be disabled via isAutoSaveEnabled flag
     * - Updates UI on successful save
     * - Provides error feedback on failure
     * 
     * Thread Safety:
     * - Timer runs in background thread
     * - UI updates are performed on JavaFX Application Thread using Platform.runLater
     */
    private void setupAutoSave() {
        // Create daemon timer thread for auto-save
        autoSaveTimer = new Timer(true);  // true = daemon thread
        
        // Schedule auto-save task to run every 30 seconds
        autoSaveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Check if auto-save should run
                if (isAutoSaveEnabled && 
                    currentFileBaseName != null && 
                    textEditor.hasUnsavedChangesProperty().get()) {
                    
                    // Perform auto-save on JavaFX Application Thread
                    Platform.runLater(() -> {
                        try {
                            // Get current content from text editor
                            String content = textEditor.getContent();
                            
                            // Save to all file variants (org, rev, byte)
                            fileService.updateFileSet(currentFileBaseName, content);
                            
                            // Update UI to reflect saved state
                            textEditor.markAsSaved();
                            sidePanel.updateStatus("Auto-saved: " + currentFileBaseName);
                            sidePanel.refreshFileNavigator();
                            
                        } catch (IOException e) {
                            // Handle auto-save errors gracefully
                            sidePanel.updateStatus("Auto-save failed: " + e.getMessage());
                        }
                    });
                }
            }
        }, 30000, 30000); // Initial delay: 30s, Repeat interval: 30s
    }

    // ============================================================================
    // FILE OPERATION HANDLERS
    // ============================================================================

    /**
     * Handles new file creation with unsaved changes detection.
     * 
     * Process Flow:
     * 1. Check for unsaved changes in current file
     * 2. Prompt user to save/discard/cancel if changes exist
     * 3. Generate unique file name if requested name exists
     * 4. Create file set (org, rev, byte variants)
     * 5. Update text editor with default content
     * 6. Update UI and file navigator
     * 7. Show success confirmation
     * 
     * @param baseName The base name for the new file set (without extension)
     */
    private void handleNewFile(String baseName) {
        // ========================================================================
        // STEP 1: CHECK FOR UNSAVED CHANGES
        // ========================================================================
        
        if (textEditor.hasUnsavedChangesProperty().get()) {
            // Create confirmation dialog for unsaved changes
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Unsaved Changes");
            confirmDialog.setHeaderText("You have unsaved changes");
            confirmDialog.setContentText("Do you want to save your changes before creating a new file?");
            
            // Apply consistent dark theme to dialog
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmDialog);
            
            // Create custom button types for user choice
            ButtonType saveButton = new ButtonType("Save");
            ButtonType discardButton = new ButtonType("Discard");
            ButtonType cancelButton = new ButtonType("Cancel");
            
            confirmDialog.getButtonTypes().setAll(saveButton, discardButton, cancelButton);
            
            // Handle user's choice
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent()) {
                if (result.get() == saveButton) {
                    // Save current changes before proceeding
                    handleSave();
                } else if (result.get() == cancelButton) {
                    // Cancel the new file operation
                    return;
                }
                // If discard was chosen, continue with new file creation
            }
        }

        // ========================================================================
        // STEP 2: GENERATE UNIQUE FILE NAME
        // ========================================================================
        
        // Ensure the file name is unique to prevent conflicts
        String uniqueBaseName = getUniqueBaseName(baseName);
        
        // Notify user if name was modified
        if (!uniqueBaseName.equals(baseName)) {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("File Name Modified");
            infoAlert.setHeaderText("File already exists");
            infoAlert.setContentText("A file with the name '" + baseName + "' already exists.\nCreated with name: " + uniqueBaseName);
            
            // Apply consistent theming
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(infoAlert);
            
            infoAlert.showAndWait();
        }

        // ========================================================================
        // STEP 3: CREATE FILE SET AND UPDATE UI
        // ========================================================================

        // Create default content for new file
        String defaultContent = "// New file: " + uniqueBaseName + "\n// Start typing your content here...";

        try {
            // Create the file set (org, rev, byte variants)
            fileService.createFileSet(uniqueBaseName, defaultContent);
            
            // Update controller state
            currentFileBaseName = uniqueBaseName;
            fileService.setCurrentFileBaseName(uniqueBaseName);
            
            // Update text editor with new content
            textEditor.setContent(defaultContent);
            textEditor.setCurrentFilePath(fileService.getFilePath(uniqueBaseName, "org"));
            textEditor.markAsSaved();
            
            // Update UI components
            sidePanel.updateStatus("Created file set: " + uniqueBaseName);
            sidePanel.refreshFileNavigator();
            
            // Show success confirmation
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

    /**
     * Generates a unique base name by appending a counter if the name already exists.
     * 
     * This method ensures that file sets don't conflict with existing ones by
     * appending a counter in parentheses (e.g., "myfile (1)", "myfile (2)").
     * 
     * @param baseName The desired base name for the file set
     * @return A unique base name that doesn't conflict with existing file sets
     */
    private String getUniqueBaseName(String baseName) {
        String uniqueName = baseName;
        int counter = 1;
        
        while (fileService.fileSetExists(uniqueName)) {
            uniqueName = baseName + " (" + counter + ")";
            counter++;
        }
        
        return uniqueName;
    }

    /**
     * Handles save operations for the current file set.
     * 
     * This method performs asynchronous saving to prevent UI blocking:
     * 1. Checks if a file is currently open
     * 2. Shows new file dialog if no file is open
     * 3. Creates async task for file saving
     * 4. Updates UI based on save success/failure
     * 5. Refreshes file navigator on successful save
     * 
     * The save operation updates all three file variants (org, rev, byte)
     * and runs in a background thread to maintain UI responsiveness.
     */
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

    /**
     * Handles file selection events from the file navigator.
     * 
     * Updates the current file base name when a different file is selected
     * in the file navigator. This ensures that operations like save and
     * word replacement work with the correct file set.
     * 
     * @param file The selected file from the file navigator
     */
    private void handleFileSelection(File file) {
        // Update current file base name when a file is selected
        String baseName = fileService.extractBaseName(file.getAbsolutePath());
        if (!baseName.equals(currentFileBaseName)) {
            currentFileBaseName = baseName;
            fileService.setCurrentFileBaseName(baseName);
        }
    }

    /**
     * Handles file open operations with unsaved changes detection.
     * 
     * Process Flow:
     * 1. Check for unsaved changes in current file
     * 2. Prompt user to save/discard/cancel if changes exist
     * 3. Load content from selected file
     * 4. Update text editor with loaded content
     * 5. Update current file tracking and UI state
     * 6. Show success/error feedback
     * 
     * @param file The file to open in the text editor
     */
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

    /**
     * Handles file deletion events from the file navigator.
     * 
     * This method responds to file or folder deletion by:
     * 1. Checking if the deleted item relates to the currently open file
     * 2. Clearing the text editor if the current file set was deleted
     * 3. Updating the current file tracking state
     * 4. Refreshing the file navigator to reflect changes
     * 5. Providing user feedback about the deletion
     * 
     * Handles both individual file deletion and entire folder deletion.
     * 
     * @param file The deleted file or folder
     */
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

    /**
     * Handles file rename events from the file navigator.
     * 
     * Updates the current file tracking when a file that's currently
     * being edited gets renamed. This ensures that subsequent operations
     * work with the correct file paths and names.
     * 
     * @param oldFile The original file before renaming
     * @param newFile The file after renaming
     */
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

    /**
     * Shows the new file dialog by setting up a listener on the side panel.
     * 
     * This method creates a temporary listener that will handle the new file
     * request when triggered from the side panel. It's used when the user
     * attempts to save but no file is currently open.
     */
    private void showNewFileDialog() {
        sidePanel.addSidePanelListener(new SidePanel.SidePanelListener() {
            @Override
            public void onNewFileRequested(String baseName) {
                handleNewFile(baseName);
            }
        });
    }

    /**
     * Shows an error dialog with consistent theming.
     * 
     * Creates and displays a styled error dialog that matches the application's
     * dark theme. Used throughout the application for consistent error reporting.
     * 
     * @param title The title for the error dialog
     * @param message The error message to display
     */
    private void showErrorDialog(String title, String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(errorAlert);
        
        errorAlert.showAndWait();
    }

    /**
     * Handles word replacement operations with validation and feedback.
     * 
     * This method performs position-based word replacement in the current document:
     * 1. Validates that a file is currently open
     * 2. Checks that the file contains content
     * 3. Validates the word index is within range
     * 4. Performs the word replacement while preserving formatting
     * 5. Saves the changes to all file variants
     * 6. Provides user feedback on success or failure
     * 
     * Word indexing is 1-based for user-friendly interaction (first word = 1).
     * The replacement preserves original spacing and text formatting.
     * 
     * @param wordIndex The 1-based position of the word to replace
     * @param replacement The new word to insert at the specified position
     */
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

        // Split content into words while preserving original spacing and structure
        String[] words = content.split("\\s+");
        
        if (wordIndex < 1 || wordIndex > words.length) {
            sidePanel.updateStatus("Invalid word index: " + wordIndex);
            showErrorDialog("Invalid Word Index", 
                "Word index " + wordIndex + " is out of range. The file contains " + words.length + " words.");
            return;
        }

        // Store the original word for feedback
        String originalWord = words[wordIndex - 1];
        
        // Use regex to replace the specific word occurrence while preserving spacing
        String newContent = replaceWordAtPosition(content, wordIndex, replacement);
        
        // Update the text editor
        textEditor.setContent(newContent);
        
        // Save the changes to all files immediately
        try {
            fileService.updateFileSet(currentFileBaseName, newContent);
            textEditor.markAsSaved(); // Mark as saved since we just saved
            sidePanel.refreshFileNavigator();
            sidePanel.updateStatus("Replaced word '" + originalWord + "' at position " + wordIndex + " with '" + replacement + "' and saved");
            
            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Word Replaced");
            successAlert.setHeaderText("Word Replacement Successful");
            successAlert.setContentText("Replaced word '" + originalWord + "' at position " + wordIndex + " with '" + replacement + "'\nChanges have been saved to all files.");
            
            // Apply dark theme to dialog
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(successAlert);
            
            successAlert.showAndWait();
            
        } catch (IOException e) {
            sidePanel.updateStatus("Word replaced but save failed: " + e.getMessage());
            showErrorDialog("Save Error", "Word was replaced in the editor but failed to save to files: " + e.getMessage());
        }
    }
    
    /**
     * Replaces a word at a specific position while preserving original spacing and newlines.
     * 
     * This method performs intelligent word replacement that maintains the original
     * document formatting, including:
     * - Preserving all whitespace (spaces, tabs, newlines)
     * - Maintaining original text structure and layout
     * - Counting words based on whitespace separation
     * - Replacing only the specific occurrence at the given position
     * 
     * Algorithm:
     * 1. Iterate through the content character by character
     * 2. Preserve all whitespace characters in their original positions
     * 3. Extract words separated by whitespace
     * 4. Replace the target word when the correct position is reached
     * 5. Continue with remaining content unchanged
     * 
     * @param content The original text content
     * @param wordIndex The 1-based position of the word to replace
     * @param replacement The new word to insert
     * @return The modified content with the word replaced and formatting preserved
     */
    private String replaceWordAtPosition(String content, int wordIndex, String replacement) {
        // Split content into words to count them
        String[] words = content.split("\\s+");
        
        if (wordIndex < 1 || wordIndex > words.length) {
            return content; // Return original if index is invalid
        }
        
        // Get the target word
        String targetWord = words[wordIndex - 1];
        
        // Find and replace the specific occurrence while preserving whitespace
        int currentWordCount = 0;
        StringBuilder result = new StringBuilder();
        int i = 0;
        
        while (i < content.length()) {
            // Skip whitespace and add it to result
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                result.append(content.charAt(i));
                i++;
            }
            
            // If we've reached the end, break
            if (i >= content.length()) {
                break;
            }
            
            // Extract the next word
            int wordStart = i;
            while (i < content.length() && !Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            
            String currentWord = content.substring(wordStart, i);
            currentWordCount++;
            
            // If this is the word we want to replace
            if (currentWordCount == wordIndex) {
                result.append(replacement);
            } else {
                result.append(currentWord);
            }
        }
        
        return result.toString();
    }

    // ============================================================================
    // PUBLIC API METHODS
    // ============================================================================

    /**
     * Enables or disables the auto-save functionality.
     * 
     * When disabled, auto-save will not run even if there are unsaved changes.
     * This setting affects both the controller's auto-save timer and the
     * file service's auto-save capabilities.
     * 
     * @param enabled true to enable auto-save, false to disable
     */
    public void setAutoSaveEnabled(boolean enabled) {
        this.isAutoSaveEnabled = enabled;
        fileService.setAutoSaveEnabled(enabled);
    }

    /**
     * Shuts down the editor controller and cleans up resources.
     * 
     * This method performs cleanup operations:
     * - Cancels the auto-save timer to stop background operations
     * - Shuts down the file service and its executor
     * - Releases any other resources held by the controller
     * 
     * Should be called when the application is closing or the controller
     * is being disposed of to ensure proper resource cleanup.
     */
    public void shutdown() {
        if (autoSaveTimer != null) {
            autoSaveTimer.cancel();
        }
        fileService.shutdown();
    }

    /**
     * Gets the text editor component.
     * 
     * Provides access to the text editor for external components that need
     * to interact with the text editing functionality directly.
     * 
     * @return The TextEditor component instance
     */
    public TextEditor getTextEditor() {
        return textEditor;
    }

    /**
     * Gets the side panel component.
     * 
     * Provides access to the side panel for external components that need
     * to interact with file navigation and control functionality.
     * 
     * @return The SidePanel component instance
     */
    public SidePanel getSidePanel() {
        return sidePanel;
    }

    /**
     * Gets the base name of the currently open file set.
     * 
     * @return The current file base name, or null if no file is open
     */
    public String getCurrentFileBaseName() {
        return currentFileBaseName;
    }

    /**
     * Checks if there are unsaved changes in the current document.
     * 
     * @return true if there are unsaved changes, false otherwise
     */
    public boolean hasUnsavedChanges() {
        return textEditor.hasUnsavedChangesProperty().get();
    }
} 