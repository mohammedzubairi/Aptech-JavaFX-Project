/**
 * File Mana - Modern Text Editor
 * Side Panel Component - UI Control Center
 * 
 * This class represents the side panel component of the File Mana text editor application.
 * It serves as the primary control center for file operations, text manipulation, and 
 * application status monitoring. The side panel integrates file navigation, control buttons,
 * and status display in a cohesive, user-friendly interface.
 * 
 * Key Responsibilities:
 * - File management operations (create, save, delete, rename)
 * - Word replacement functionality with position-based indexing
 * - File comparison and analysis tools
 * - Status monitoring and user feedback
 * - Integration with FileNavigator for file tree display
 * - Event-driven communication with parent controller
 * 
 * Architecture Pattern: Component-based UI with Observer Pattern
 * - Extends VBox for flexible vertical layout
 * - Implements listener pattern for component communication
 * - Encapsulates file navigator and control elements
 * - Provides clean API for external interaction
 * 
 * Component Layout Structure:
 * ┌─────────────────────────────────────────────────────────┐
 * │                    SidePanel (VBox)                     │
 * ├─────────────────────────────────────────────────────────┤
 * │  Button Section                                         │
 * │  ├── New File Button                                    │
 * │  ├── Save Button                                        │
 * │  ├── Word Replacement Controls                          │
 * │  │   ├── Word Index Field                              │
 * │  │   ├── Replacement Field                             │
 * │  │   └── Replace Button                                │
 * │  ├── Compare Files Button                              │
 * │  └── Status Display                                    │
 * ├─────────────────────────────────────────────────────────┤
 * │  File Navigator Section (grows to fill space)          │
 * │  └── FileNavigator Component                           │
 * └─────────────────────────────────────────────────────────┘
 * 
 * Integration with MVC Architecture:
 * - View Component: Displays UI and handles user interactions
 * - Controller Communication: Uses observer pattern with SidePanelListener
 * - Model Interaction: Indirectly through EditorController and FileService
 * - Event Flow: User Action → SidePanel → EditorController → FileService
 * 
 * Design Patterns Used:
 * - Observer Pattern: Event notification system for component communication
 * - Composite Pattern: Contains FileNavigator and other UI components
 * - Command Pattern: Action handlers for button clicks and user operations
 * - Strategy Pattern: Different dialog strategies for various operations
 * 
 * @course Advanced Java Programming with JavaFX
 * @institution Aptech Computer Education
 * @university Alnasser University
 * @version 1.0
 * @since 2025-05-20
 * 
 * Dependencies:
 * - FileNavigator: For file tree display and navigation
 * - MainApp: For dialog theming utilities
 * - JavaFX Controls: For UI components and layout
 * - Observer Pattern: For event-driven component communication
 */
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
import javafx.stage.FileChooser;
import javafx.scene.control.CheckBoxTreeItem;

/**
 * Side Panel Component for File Mana Text Editor
 * 
 * This class extends VBox to create a comprehensive side panel that serves as the
 * control center for the File Mana application. It provides file management,
 * text manipulation, and monitoring capabilities through an intuitive user interface.
 * 
 * The side panel is designed to occupy 30% of the application window width and
 * integrates seamlessly with the main text editor component (70% width) to create
 * a balanced, professional layout similar to modern IDEs like VS Code.
 * 
 * Component Composition:
 * - Control buttons for core operations (New File, Save)
 * - Word replacement tools with position-based indexing
 * - File comparison utilities
 * - Status monitoring and feedback system
 * - Integrated file navigator with tree view
 * - Event-driven communication system
 * 
 * Thread Safety:
 * - All UI operations are performed on JavaFX Application Thread
 * - File operations are delegated to background services
 * - Property bindings ensure consistent state updates
 */
public class SidePanel extends VBox {
    
    // ============================================================================
    // UI COMPONENT DECLARATIONS
    // ============================================================================
    
    /**
     * Button for creating new file sets.
     * Triggers a dialog to collect base name and creates org/rev/byte variants.
     */
    private final Button newFileButton;
    
    /**
     * Button for saving current file content.
     * Enabled only when there are unsaved changes to prevent unnecessary operations.
     */
    private final Button saveButton;
    
    /**
     * Text field for entering word index in word replacement operation.
     * Accepts 1-based index numbers (user-friendly counting).
     */
    private final TextField wordIndexField;
    
    /**
     * Text field for entering replacement text in word replacement operation.
     * Supports any text content including special characters and Unicode.
     */
    private final TextField replacementField;
    
    /**
     * Button for executing word replacement operation.
     * Validates input and triggers position-based word replacement.
     */
    private final Button replaceButton;
    
    /**
     * Button for comparing multiple files.
     * Opens a dialog with file tree to select 2-3 files for comparison.
     */
    private final Button compareFilesButton;
    
    /**
     * File navigation component for browsing and managing files.
     * Displays folder structure and provides file operations.
     */
    private final FileNavigator fileNavigator;
    
    /**
     * Label for displaying status messages and user feedback.
     * Shows current operation status, error messages, and system notifications.
     */
    private final Label statusLabel;
    
    // ============================================================================
    // STATE MANAGEMENT PROPERTIES
    // ============================================================================
    
    /**
     * Property to track unsaved changes in the current file.
     * Bound to save button's disable property to control button state.
     * Uses JavaFX property binding for automatic UI updates.
     */
    private final BooleanProperty hasUnsavedChanges = new SimpleBooleanProperty(false);
    
    /**
     * Observable list of listeners for side panel events.
     * Implements observer pattern for component communication.
     * Allows multiple listeners to respond to side panel events.
     */
    private final ObservableList<SidePanelListener> listeners = FXCollections.observableArrayList();

    // ============================================================================
    // CONSTRUCTOR AND INITIALIZATION
    // ============================================================================

    /**
     * Constructor - Initializes the side panel with all components and event handlers.
     * 
     * Initialization Process:
     * 1. Create all UI components (buttons, fields, labels)
     * 2. Initialize FileNavigator with working directory
     * 3. Set up component properties and styling
     * 4. Configure layout structure and spacing
     * 5. Bind event handlers for user interactions
     * 6. Apply CSS styling for consistent appearance
     * 
     * @param workingDirectory The base directory for file operations
     *                        (typically "Created files/")
     */
    public SidePanel(String workingDirectory) {
        // Initialize core UI components
        this.newFileButton = new Button("New File");
        this.saveButton = new Button("Save");
        this.wordIndexField = new TextField();
        this.replacementField = new TextField();
        this.replaceButton = new Button("Replace Word");
        this.compareFilesButton = new Button("Compare Files");
        this.fileNavigator = new FileNavigator(workingDirectory);
        this.statusLabel = new Label("Ready");
        
        // Configure component properties and styling
        setupComponents();
        
        // Set up layout structure and spacing
        setupLayout();
        
        // Bind event handlers for user interactions
        setupEventHandlers();
        
        // Apply CSS styling class for consistent appearance
        getStyleClass().add("side-panel");
    }

    // ============================================================================
    // COMPONENT CONFIGURATION
    // ============================================================================

    /**
     * Configures properties and styling for all UI components.
     * 
     * This method sets up:
     * - Button styling with CSS classes for consistent appearance
     * - Button sizing to fill available width
     * - Text field properties including prompt text and styling
     * - Property bindings for reactive UI updates
     * - Status label configuration for text wrapping
     * 
     * CSS Classes Applied:
     * - primary-button: For the New File button (accent color)
     * - accent-button: For the Save button (highlight color)
     * - secondary-button: For Replace and Compare buttons (standard color)
     * - side-panel-button: Common styling for all buttons
     * - side-panel-field: Styling for text input fields
     * - status-label: Styling for status display
     */
    private void setupComponents() {
        // Style buttons with appropriate CSS classes
        newFileButton.getStyleClass().addAll("primary-button", "side-panel-button");
        saveButton.getStyleClass().addAll("accent-button", "side-panel-button");
        replaceButton.getStyleClass().addAll("secondary-button", "side-panel-button");
        compareFilesButton.getStyleClass().addAll("secondary-button", "side-panel-button");
        
        // Set button properties for responsive layout
        newFileButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        replaceButton.setMaxWidth(Double.MAX_VALUE);
        compareFilesButton.setMaxWidth(Double.MAX_VALUE);
        
        // Bind save button state to unsaved changes property
        // This ensures the save button is only enabled when there are changes to save
        saveButton.disableProperty().bind(hasUnsavedChanges.not());
        
        // Setup text fields with proper width settings and user guidance
        wordIndexField.setPromptText("Word index (1, 2, 3...)");
        wordIndexField.getStyleClass().add("side-panel-field");
        wordIndexField.setMaxWidth(Double.MAX_VALUE);
        wordIndexField.setPrefWidth(Region.USE_COMPUTED_SIZE);
        
        replacementField.setPromptText("Replacement text");
        replacementField.getStyleClass().add("side-panel-field");
        replacementField.setMaxWidth(Double.MAX_VALUE);
        replacementField.setPrefWidth(Region.USE_COMPUTED_SIZE);
        
        // Status label configuration
        statusLabel.getStyleClass().add("status-label");
        statusLabel.setWrapText(true);  // Enable text wrapping for long messages
    }

    // ============================================================================
    // LAYOUT CONFIGURATION
    // ============================================================================

    /**
     * Sets up the layout structure for the side panel.
     * 
     * Layout Structure:
     * - Button Section (fixed height): Contains all control buttons and input fields
     * - File Navigator Section (flexible height): Contains file tree and grows to fill space
     * - Proper spacing and padding for visual hierarchy
     * - Responsive design that adapts to window resizing
     * 
     * Layout Strategy:
     * - Use VBox for main vertical layout
     * - Group related controls in sections
     * - Apply consistent spacing between sections
     * - Use VBox.setVgrow() for flexible sizing
     * - Add separators for visual grouping
     */
    private void setupLayout() {
        // Top section with control buttons and input fields
        VBox buttonSection = new VBox(10);
        buttonSection.getStyleClass().add("button-section");
        buttonSection.setPadding(new Insets(15));
        buttonSection.setFillWidth(true);
        
        // Primary action buttons container
        VBox buttonContainer = new VBox(8);
        buttonContainer.setFillWidth(true);
        buttonContainer.getChildren().addAll(newFileButton, saveButton);
        
        // Word replacement controls section
        VBox replacementSection = new VBox(8);
        replacementSection.setFillWidth(true);
        
        Label replacementLabel = new Label("Word Replacement:");
        replacementLabel.getStyleClass().add("section-title");
        
        replacementSection.getChildren().addAll(
            replacementLabel,
            wordIndexField,
            replacementField,
            replaceButton,
            compareFilesButton
        );
        
        // Status display section
        VBox statusSection = new VBox(5);
        
        Label statusTitleLabel = new Label("Status:");
        statusTitleLabel.getStyleClass().add("section-title");
        
        statusSection.getChildren().addAll(
            new Separator(),
            statusTitleLabel,
            statusLabel
        );
        
        // Add all subsections to the main button section
        buttonSection.getChildren().addAll(buttonContainer, replacementSection, statusSection);
        
        // File navigator section (grows to fill available space)
        VBox navigatorSection = new VBox();
        navigatorSection.getStyleClass().add("navigator-section");
        
        Label navigatorTitle = new Label("Files");
        navigatorTitle.getStyleClass().add("section-title");
        
        navigatorSection.getChildren().addAll(navigatorTitle, fileNavigator);
        VBox.setVgrow(fileNavigator, Priority.ALWAYS);  // Allow file navigator to grow
        
        // Add main sections to the side panel
        getChildren().addAll(buttonSection, navigatorSection);
        VBox.setVgrow(navigatorSection, Priority.ALWAYS);  // Allow navigator section to grow
        
        // Set main container properties
        setSpacing(0);
        setPadding(new Insets(0));
    }

    // ============================================================================
    // EVENT HANDLER CONFIGURATION
    // ============================================================================

    /**
     * Sets up event handlers for all user interactions.
     * 
     * This method configures:
     * - Button click handlers for all action buttons
     * - File navigator event listeners for file operations
     * - Input validation for text fields
     * - Dialog management for user interactions
     * - Error handling and user feedback
     * 
     * Event Handling Strategy:
     * - Validate user input before processing
     * - Show confirmation dialogs for destructive operations
     * - Apply consistent theming to all dialogs
     * - Provide clear feedback for all operations
     * - Handle errors gracefully with user-friendly messages
     */
    private void setupEventHandlers() {
        // ========================================================================
        // NEW FILE BUTTON HANDLER
        // ========================================================================
        
        /**
         * Handles new file creation with user input validation.
         * 
         * Process:
         * 1. Show input dialog for base name
         * 2. Validate and sanitize input
         * 3. Remove .txt extension if user added it
         * 4. Notify listeners of new file request
         * 5. Apply consistent theming to dialog
         */
        newFileButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create New File Set");
            dialog.setHeaderText("Enter the base name for the new file set:");
            dialog.setContentText("Base name (without extension):");
            
            // Add placeholder text for user guidance
            TextField textField = dialog.getEditor();
            textField.setPromptText("e.g., MyProject, Document1, etc.");
            
            // Apply dark theme to dialog for consistent appearance
            com.codemavriks.aptech.MainApp.applyDarkThemeToDialog(dialog);
            
            dialog.showAndWait().ifPresent(baseName -> {
                if (!baseName.trim().isEmpty()) {
                    // Sanitize input by removing .txt extension if user added it
                    baseName = baseName.trim();
                    if (baseName.toLowerCase().endsWith(".txt")) {
                        baseName = baseName.substring(0, baseName.length() - 4);
                    }
                    // Notify listeners of new file request
                    notifyNewFileRequested(baseName);
                }
            });
        });

        // ========================================================================
        // SAVE BUTTON HANDLER
        // ========================================================================
        
        /**
         * Handles save operation with user feedback.
         * 
         * Process:
         * 1. Notify listeners of save request
         * 2. Show success confirmation dialog
         * 3. Apply consistent theming to dialog
         */
        saveButton.setOnAction(e -> {
            // Show save confirmation dialog
            Alert saveAlert = new Alert(Alert.AlertType.INFORMATION);
            saveAlert.setTitle("Save File");
            saveAlert.setHeaderText("File Saved Successfully");
            saveAlert.setContentText("The file and its variants (org, rev, byte) have been updated.");
            
            // Apply dark theme to dialog for consistent appearance
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(saveAlert);
            
            // Notify listeners of save request
            notifySaveRequested();
            
            // Show the alert after save operation
            saveAlert.showAndWait();
        });

        // ========================================================================
        // REPLACE BUTTON HANDLER
        // ========================================================================
        
        /**
         * Handles word replacement with comprehensive input validation.
         * 
         * Process:
         * 1. Validate input fields (index and replacement text)
         * 2. Parse and validate word index (must be positive integer)
         * 3. Show confirmation dialog with operation details
         * 4. Execute replacement if confirmed
         * 5. Clear input fields after successful operation
         * 6. Handle errors with user-friendly messages
         */
        replaceButton.setOnAction(e -> {
            String indexText = wordIndexField.getText().trim();
            String replacement = replacementField.getText().trim();
            
            // Validate that both fields have content
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
                // Parse word index and validate it's a positive integer
                int wordIndex = Integer.parseInt(indexText);
                if (wordIndex < 1) {
                    throw new NumberFormatException("Index must be positive");
                }
                
                // Show confirmation dialog with operation details
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Replacement");
                confirmAlert.setHeaderText("Replace word at position " + wordIndex);
                confirmAlert.setContentText("Replace word at position " + wordIndex + " with: \"" + replacement + "\"?");
                
                // Apply dark theme to dialog
                com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmAlert);
                
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Execute word replacement
                        notifyWordReplaceRequested(wordIndex, replacement);
                        // Clear fields after successful replacement
                        wordIndexField.clear();
                        replacementField.clear();
                    }
                });
                
            } catch (NumberFormatException ex) {
                // Handle invalid number format
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Invalid Index");
                errorAlert.setHeaderText("Invalid Word Index");
                errorAlert.setContentText("Please enter a valid positive number for the word index.");
                
                // Apply dark theme to dialog
                com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(errorAlert);
                
                errorAlert.showAndWait();
            }
        });

        // ========================================================================
        // COMPARE FILES BUTTON HANDLER
        // ========================================================================
        
        /**
         * Handles file comparison dialog display.
         * Delegates to specialized method for complex dialog management.
         */
        compareFilesButton.setOnAction(e -> {
            showCompareFilesDialog();
        });

        // ========================================================================
        // FILE NAVIGATOR EVENT LISTENERS
        // ========================================================================
        
        /**
         * Sets up file navigator event listeners using observer pattern.
         * 
         * This establishes communication between the FileNavigator component
         * and the parent controller through event delegation.
         */
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

    // ============================================================================
    // PUBLIC API METHODS
    // ============================================================================

    /**
     * Updates the status label with a message.
     * 
     * This method provides a way for external components to update the status
     * display in the side panel. It's commonly used to show operation results,
     * error messages, or current state information.
     * 
     * @param message The status message to display
     */
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Sets the unsaved changes state and updates UI accordingly.
     * 
     * This method controls the save button's enabled state and updates the
     * status message to reflect the current save state. It's bound to the
     * text editor's change detection system.
     * 
     * @param hasChanges True if there are unsaved changes, false otherwise
     */
    public void setHasUnsavedChanges(boolean hasChanges) {
        hasUnsavedChanges.set(hasChanges);
        if (hasChanges) {
            updateStatus("Unsaved changes");
        } else {
            updateStatus("All changes saved");
        }
    }

    /**
     * Refreshes the file navigator to show current file system state.
     * 
     * This method triggers a refresh of the file tree view, which is useful
     * after file operations that might have changed the file system structure.
     */
    public void refreshFileNavigator() {
        fileNavigator.refreshTree();
    }

    /**
     * Selects a specific file in the file navigator.
     * 
     * This method programmatically selects a file in the file tree, which is
     * useful for synchronizing the file navigator with the current editor state.
     * 
     * @param file The file to select in the navigator
     */
    public void selectFile(File file) {
        fileNavigator.selectFile(file);
    }

    // ============================================================================
    // EVENT NOTIFICATION METHODS (Observer Pattern Implementation)
    // ============================================================================

    /**
     * Notifies all listeners of a new file request.
     * 
     * @param baseName The base name for the new file set
     */
    private void notifyNewFileRequested(String baseName) {
        listeners.forEach(listener -> listener.onNewFileRequested(baseName));
    }

    /**
     * Notifies all listeners of a save request.
     */
    private void notifySaveRequested() {
        listeners.forEach(listener -> listener.onSaveRequested());
    }

    /**
     * Notifies all listeners of a word replacement request.
     * 
     * @param wordIndex The 1-based index of the word to replace
     * @param replacement The replacement text
     */
    private void notifyWordReplaceRequested(int wordIndex, String replacement) {
        listeners.forEach(listener -> listener.onWordReplaceRequested(wordIndex, replacement));
    }

    /**
     * Notifies all listeners of a file selection event.
     * 
     * @param file The selected file
     */
    private void notifyFileSelected(File file) {
        listeners.forEach(listener -> listener.onFileSelected(file));
    }

    /**
     * Notifies all listeners of a file open event.
     * 
     * @param file The opened file
     */
    private void notifyFileOpened(File file) {
        listeners.forEach(listener -> listener.onFileOpened(file));
    }

    /**
     * Notifies all listeners of a file deletion event.
     * 
     * @param file The deleted file
     */
    private void notifyFileDeleted(File file) {
        listeners.forEach(listener -> listener.onFileDeleted(file));
    }

    /**
     * Notifies all listeners of a file rename event.
     * 
     * @param oldFile The original file
     * @param newFile The renamed file
     */
    private void notifyFileRenamed(File oldFile, File newFile) {
        listeners.forEach(listener -> listener.onFileRenamed(oldFile, newFile));
    }

    // ============================================================================
    // LISTENER MANAGEMENT
    // ============================================================================

    /**
     * Adds a listener for side panel events.
     * 
     * This method allows external components (typically the main controller)
     * to register for side panel events using the observer pattern.
     * 
     * @param listener The listener to add
     */
    public void addSidePanelListener(SidePanelListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener for side panel events.
     * 
     * @param listener The listener to remove
     */
    public void removeSidePanelListener(SidePanelListener listener) {
        listeners.remove(listener);
    }

    // ============================================================================
    // LISTENER INTERFACE
    // ============================================================================

    /**
     * Interface for side panel event callbacks.
     * 
     * This interface defines the contract for components that want to receive
     * notifications about side panel events. It uses default methods to make
     * implementation optional for listeners that don't need all events.
     * 
     * Design Pattern: Observer Pattern
     * - Defines the observer interface for side panel events
     * - Uses default methods for flexible implementation
     * - Enables loose coupling between components
     */
    public interface SidePanelListener {
        /**
         * Called when a new file is requested.
         * 
         * @param baseName The base name for the new file set
         */
        default void onNewFileRequested(String baseName) {}
        
        /**
         * Called when a save operation is requested.
         */
        default void onSaveRequested() {}
        
        /**
         * Called when a word replacement is requested.
         * 
         * @param wordIndex The 1-based index of the word to replace
         * @param replacement The replacement text
         */
        default void onWordReplaceRequested(int wordIndex, String replacement) {}
        
        /**
         * Called when a file is selected.
         * 
         * @param file The selected file
         */
        default void onFileSelected(File file) {}
        
        /**
         * Called when a file is opened.
         * 
         * @param file The opened file
         */
        default void onFileOpened(File file) {}
        
        /**
         * Called when a file is deleted.
         * 
         * @param file The deleted file
         */
        default void onFileDeleted(File file) {}
        
        /**
         * Called when a file is renamed.
         * 
         * @param oldFile The original file
         * @param newFile The renamed file
         */
        default void onFileRenamed(File oldFile, File newFile) {}
    }

    // ============================================================================
    // FILE COMPARISON DIALOG METHODS
    // ============================================================================

    /**
     * Shows a dialog for comparing multiple files.
     * 
     * This method creates and displays a complex dialog that allows users to
     * select 2-3 files for comparison. It uses a checkbox tree view to present
     * the file system and validates the selection before performing the comparison.
     * 
     * Features:
     * - Checkbox tree view for file selection
     * - Validation to ensure 2-3 files are selected
     * - Content comparison with detailed results
     * - Consistent theming with the rest of the application
     * - Error handling for file reading operations
     */
    private void showCompareFilesDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Compare Files");
        dialog.setHeaderText("Select 2 or 3 files to compare");
        com.codemavriks.aptech.MainApp.applyDarkThemeToDialog(dialog);

        // Create the file tree with checkboxes for selection
        CheckBoxTreeItem<File> rootItem = createCheckBoxFileTreeRoot();
        TreeView<File> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(false);
        treeView.setCellFactory(javafx.scene.control.cell.CheckBoxTreeCell.forTreeView());
        treeView.setPrefHeight(350);
        treeView.setPrefWidth(400);

        // Track selected files using observable list
        ObservableList<File> selectedFiles = FXCollections.observableArrayList();
        addCheckBoxSelectionListener(rootItem, selectedFiles);

        // Create compare button and set up validation
        ButtonType compareButtonType = new ButtonType("Compare", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(compareButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(treeView);
        Button compareButton = (Button) dialog.getDialogPane().lookupButton(compareButtonType);
        compareButton.setDisable(true);

        // Enable compare button only when 2-3 files are selected
        selectedFiles.addListener((javafx.collections.ListChangeListener<File>) c -> {
            compareButton.setDisable(selectedFiles.size() < 2 || selectedFiles.size() > 3);
        });

        // Handle dialog result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == compareButtonType) {
                if (selectedFiles.size() >= 2 && selectedFiles.size() <= 3) {
                    compareSelectedFiles(selectedFiles);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Creates a checkbox tree item structure for file selection.
     * 
     * This method builds a tree structure with checkboxes that represents the
     * file system. It filters to show only .txt files and directories, and
     * provides a hierarchical view for easy file selection.
     * 
     * @return Root tree item with checkbox functionality
     */
    private CheckBoxTreeItem<File> createCheckBoxFileTreeRoot() {
        File rootDir = new File(fileNavigator != null ? fileNavigator.getWorkingDirectory() : "Created files");
        CheckBoxTreeItem<File> rootItem = new CheckBoxTreeItem<>(rootDir);
        rootItem.setExpanded(true);
        
        // Build tree structure with directories and .txt files
        for (File file : rootDir.listFiles()) {
            if (file.isDirectory()) {
                CheckBoxTreeItem<File> dirItem = new CheckBoxTreeItem<>(file);
                dirItem.setExpanded(true);
                for (File child : file.listFiles()) {
                    if (child.isFile() && child.getName().toLowerCase().endsWith(".txt")) {
                        dirItem.getChildren().add(new CheckBoxTreeItem<>(child));
                    }
                }
                rootItem.getChildren().add(dirItem);
            } else if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                rootItem.getChildren().add(new CheckBoxTreeItem<>(file));
            }
        }
        return rootItem;
    }

    /**
     * Adds selection listeners to checkbox tree items.
     * 
     * This method recursively adds listeners to all checkbox tree items to
     * track which files are selected. It maintains a list of selected files
     * that updates automatically when checkboxes are toggled.
     * 
     * @param root The root tree item to process
     * @param selectedFiles List to maintain selected files
     */
    private void addCheckBoxSelectionListener(CheckBoxTreeItem<File> root, ObservableList<File> selectedFiles) {
        for (TreeItem<File> child : root.getChildren()) {
            if (child instanceof CheckBoxTreeItem) {
                CheckBoxTreeItem<File> cbItem = (CheckBoxTreeItem<File>) child;
                cbItem.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    File file = cbItem.getValue();
                    if (file.isFile()) {
                        if (isNowSelected) {
                            if (!selectedFiles.contains(file)) selectedFiles.add(file);
                        } else {
                            selectedFiles.remove(file);
                        }
                    }
                });
                // Recursively process child items
                addCheckBoxSelectionListener(cbItem, selectedFiles);
            }
        }
    }

    /**
     * Compares the selected files and displays the result.
     * 
     * This method performs content comparison on the selected files and shows
     * a dialog with the comparison results. It handles file reading errors
     * gracefully and provides clear feedback to the user.
     * 
     * Comparison Logic:
     * - Reads all selected files
     * - Compares content for exact matches
     * - Shows "similar" if all files have identical content
     * - Shows "not similar" if any files differ
     * - Handles file reading errors with appropriate messages
     * 
     * @param files List of files to compare
     */
    private void compareSelectedFiles(ObservableList<File> files) {
        try {
            boolean allEqual = true;
            String firstContent = null;
            
            // Read and compare all files
            for (File file : files) {
                String content = java.nio.file.Files.readString(file.toPath());
                if (firstContent == null) {
                    firstContent = content;
                } else if (!firstContent.equals(content)) {
                    allEqual = false;
                    break;
                }
            }
            
            // Display comparison result
            Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
            resultAlert.setTitle("File Comparison Result");
            if (allEqual) {
                resultAlert.setHeaderText("The files are similar");
                resultAlert.setContentText("The contents of the selected files are identical.");
            } else {
                resultAlert.setHeaderText("The files are not similar");
                resultAlert.setContentText("The contents of the selected files are different.");
            }
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(resultAlert);
            resultAlert.showAndWait();
            
        } catch (Exception ex) {
            // Handle file reading errors
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error Comparing Files");
            errorAlert.setHeaderText("Could not compare files");
            errorAlert.setContentText(ex.getMessage());
            com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(errorAlert);
            errorAlert.showAndWait();
        }
    }
} 