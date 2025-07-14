/**
 * File Mana - Modern Text Editor
 * Text Editor Component - Main Content Editing Area
 * 
 * This class represents the primary text editing component of the File Mana text editor application.
 * It provides a full-featured text editing environment with syntax highlighting, menu operations,
 * keyboard shortcuts, search functionality, and change tracking. The component is designed to
 * integrate seamlessly with the application's dark theme and responsive layout system.
 * 
 * Key Responsibilities:
 * - Primary text content editing and display
 * - Change tracking for unsaved content detection
 * - Text manipulation operations (cut, copy, paste, undo, redo)
 * - Search and find functionality with text highlighting
 * - Keyboard shortcut handling for power users
 * - Menu-based operation access
 * - Event-driven communication with parent components
 * 
 * Architecture Pattern: Component-based UI with Event Communication
 * - Extends VBox for flexible vertical layout (menu bar + text area)
 * - Uses JavaFX TextArea for core text editing functionality
 * - Implements custom events for parent component communication
 * - Provides property binding for reactive UI updates
 * 
 * Component Layout Structure:
 * ┌─────────────────────────────────────────────────────────┐
 * │                  TextEditor (VBox)                      │
 * ├─────────────────────────────────────────────────────────┤
 * │  MenuBar                                                │
 * │  ├── Edit Menu                                          │
 * │  │   ├── Undo / Redo                                   │
 * │  │   ├── Cut / Copy / Paste                            │
 * │  │   ├── Select All                                    │
 * │  │   └── Find                                          │
 * ├─────────────────────────────────────────────────────────┤
 * │  TextArea (grows to fill available space)              │
 * │  ├── Text content with dark theme styling              │
 * │  ├── Word wrapping enabled                             │
 * │  ├── Change tracking for unsaved indicators            │
 * │  └── Keyboard shortcut handling                        │
 * └─────────────────────────────────────────────────────────┘
 * 
 * Integration with MVC Architecture:
 * - View Component: Handles text display and user input
 * - Controller Communication: Uses custom events for save/new file requests
 * - Model Interaction: Tracks content changes and file path state
 * - Event Flow: User Input → TextEditor → Custom Events → EditorController
 * 
 * Design Patterns Used:
 * - Observer Pattern: Property change notifications for content tracking
 * - Command Pattern: Menu item action handlers and keyboard shortcuts
 * - Event System: Custom events for parent component communication
 * - Template Method Pattern: Common text operation patterns
 * 
 * Features:
 * - Full text editing with undo/redo support
 * - Dark theme integration with custom styling
 * - Keyboard shortcuts (Ctrl+S, Ctrl+N, Ctrl+F, etc.)
 * - Find functionality with text highlighting
 * - Change tracking with unsaved changes detection
 * - File path tracking for current document
 * - Responsive layout that grows with window size
 * - Menu-based access to all operations
 * 
 * Thread Safety:
 * - All operations are performed on JavaFX Application Thread
 * - Property bindings ensure thread-safe state updates
 * - Event firing is handled by JavaFX event system
 * 
 * @author NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH
 * @student_id Student1554163
 * @course Advanced Java Programming with JavaFX
 * @institution Aptech Computer Education
 * @university Alnasser University
 * @version 1.0
 * @since 2025-05-20
 * 
 * Dependencies:
 * - JavaFX Controls: For TextArea, MenuBar, and other UI components
 * - JavaFX Properties: For reactive property binding and change tracking
 * - JavaFX Events: For custom event communication with parent components
 */
package com.codemavriks.aptech.components;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Text Editor Component for File Mana Text Editor
 * 
 * This class extends VBox to create a comprehensive text editing component that serves
 * as the main content area for the File Mana application. It provides a full-featured
 * editing environment with menu operations, keyboard shortcuts, and change tracking.
 * 
 * The component is designed to occupy 70% of the application window width and
 * integrates seamlessly with the side panel (30% width) to create a balanced,
 * professional layout similar to modern code editors like VS Code and Sublime Text.
 * 
 * Component Composition:
 * - MenuBar with Edit menu for common operations
 * - TextArea for primary text editing with dark theme styling
 * - Property bindings for change tracking and state management
 * - Custom event system for parent component communication
 * - Keyboard shortcut handling for power user efficiency
 * 
 * State Management:
 * - Tracks unsaved changes through content comparison
 * - Maintains current file path for document identification
 * - Provides property access for external component binding
 * - Handles original content baseline for change detection
 */
public class TextEditor extends VBox {
    
    // ============================================================================
    // UI COMPONENT DECLARATIONS
    // ============================================================================
    
    /**
     * The main text editing area component.
     * Configured with word wrapping, dark theme styling, and change tracking.
     * Takes up the majority of the component space for text editing.
     */
    private final TextArea textArea;
    
    /**
     * Menu bar providing access to text editing operations.
     * Contains Edit menu with standard operations like cut, copy, paste,
     * undo, redo, select all, and find functionality.
     */
    private final MenuBar menuBar;
    
    // ============================================================================
    // STATE MANAGEMENT PROPERTIES
    // ============================================================================
    
    /**
     * Property tracking whether the current content has unsaved changes.
     * Automatically updated when text content changes compared to original.
     * Used by parent components to show unsaved indicators and handle closing.
     */
    private final BooleanProperty hasUnsavedChanges = new SimpleBooleanProperty(false);
    
    /**
     * Property storing the current file path being edited.
     * Used for window titles, status display, and file management operations.
     * Empty string indicates a new, unsaved document.
     */
    private final StringProperty currentFilePath = new SimpleStringProperty("");
    
    /**
     * Baseline content for change detection.
     * Stores the last saved version of the content to compare against
     * current text for unsaved changes detection.
     */
    private String originalContent = "";

    // ============================================================================
    // CONSTRUCTOR AND INITIALIZATION
    // ============================================================================

    /**
     * Constructor - Initializes the text editor component with all UI elements.
     * 
     * Initialization Process:
     * 1. Create TextArea and MenuBar components
     * 2. Configure text area properties and styling
     * 3. Set up keyboard shortcuts for power users
     * 4. Add components to layout (MenuBar at top, TextArea filling space)
     * 5. Apply CSS styling class for consistent theming
     * 
     * Layout Strategy:
     * - MenuBar at top with fixed height
     * - TextArea grows to fill remaining vertical space
     * - Dark theme styling applied for consistent appearance
     * - Change tracking configured for unsaved content detection
     */
    public TextEditor() {
        this.textArea = new TextArea();
        this.menuBar = createMenuBar();
        
        setupTextArea();
        setupKeyboardShortcuts();
        
        getChildren().addAll(menuBar, textArea);
        getStyleClass().add("text-editor");
    }

    // ============================================================================
    // TEXT AREA CONFIGURATION
    // ============================================================================

    /**
     * Configures the main text area component with properties and styling.
     * 
     * This method sets up:
     * - Word wrapping for long lines
     * - Dark theme styling for consistent appearance
     * - Change tracking through text property listeners
     * - Layout growth properties for responsive design
     * - CSS styling classes for theme integration
     * 
     * The text area is configured to detect changes by comparing current
     * content with the original (last saved) content, automatically updating
     * the hasUnsavedChanges property for external components to use.
     */
    private void setupTextArea() {
        textArea.setWrapText(true);
        textArea.getStyleClass().add("main-text-area");
        
        // Ensure dark theme styling
        textArea.setStyle("-fx-control-inner-background: #1E1E1E; -fx-background-color: #1E1E1E; -fx-text-fill: #CCCCCC;");
        
        // Track changes for unsaved indicator
        textArea.textProperty().addListener((obs, oldVal, newVal) -> {
            hasUnsavedChanges.set(!newVal.equals(originalContent));
        });
        
        // Make text area take all available space
        VBox.setVgrow(textArea, javafx.scene.layout.Priority.ALWAYS);
    }

    // ============================================================================
    // MENU BAR CREATION
    // ============================================================================

    /**
     * Creates and configures the menu bar with Edit menu operations.
     * 
     * The menu bar provides quick access to common text editing operations
     * with proper keyboard accelerators. It includes:
     * 
     * Edit Menu:
     * - Undo (Ctrl+Z): Reverse last operation
     * - Redo (Ctrl+Y): Reapply undone operation
     * - Cut (Ctrl+X): Cut selected text to clipboard
     * - Copy (Ctrl+C): Copy selected text to clipboard
     * - Paste (Ctrl+V): Paste clipboard content
     * - Select All (Ctrl+A): Select entire document
     * - Find (Ctrl+F): Open find dialog for text search
     * 
     * @return Configured MenuBar with Edit menu and operations
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("editor-menu-bar");
        
        // Edit Menu
        Menu editMenu = new Menu("Edit");
        
        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        undoItem.setOnAction(e -> textArea.undo());
        
        MenuItem redoItem = new MenuItem("Redo");
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        redoItem.setOnAction(e -> textArea.redo());
        
        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        cutItem.setOnAction(e -> textArea.cut());
        
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        copyItem.setOnAction(e -> textArea.copy());
        
        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        pasteItem.setOnAction(e -> textArea.paste());
        
        MenuItem selectAllItem = new MenuItem("Select All");
        selectAllItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
        selectAllItem.setOnAction(e -> textArea.selectAll());
        
        MenuItem findItem = new MenuItem("Find");
        findItem.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        findItem.setOnAction(e -> showFindDialog());
        
        editMenu.getItems().addAll(
            undoItem, redoItem, new SeparatorMenuItem(),
            cutItem, copyItem, pasteItem, new SeparatorMenuItem(),
            selectAllItem, new SeparatorMenuItem(),
            findItem
        );
        
        menuBar.getMenus().add(editMenu);
        return menuBar;
    }

    // ============================================================================
    // KEYBOARD SHORTCUT CONFIGURATION
    // ============================================================================

    /**
     * Sets up additional keyboard shortcuts for power user efficiency.
     * 
     * This method configures keyboard shortcuts that trigger custom events
     * for communication with parent components:
     * 
     * - Ctrl+S: Save current document (fires SaveRequestEvent)
     * - Ctrl+N: Create new document (fires NewFileRequestEvent)
     * 
     * These shortcuts complement the menu-based operations and provide
     * quick access to commonly used file operations. Events are consumed
     * to prevent default handling and fired as custom events for parent
     * components to handle appropriately.
     */
    private void setupKeyboardShortcuts() {
        // Additional keyboard shortcuts
        textArea.setOnKeyPressed(event -> {
            if (event.isControlDown()) {
                switch (event.getCode()) {
                    case S:
                        // Save shortcut - will be handled by parent component
                        event.consume();
                        fireEvent(new SaveRequestEvent());
                        break;
                    case N:
                        // New file shortcut
                        event.consume();
                        fireEvent(new NewFileRequestEvent());
                        break;
                }
            }
        });
    }

    // ============================================================================
    // FIND FUNCTIONALITY
    // ============================================================================

    /**
     * Shows the find dialog for text search functionality.
     * 
     * Opens a text input dialog asking the user for search text, then
     * performs case-insensitive search in the document. If text is found,
     * it's highlighted and focused. If not found, shows an information dialog.
     * 
     * Search Features:
     * - Case-insensitive text matching
     * - Automatic text selection and highlighting
     * - Focus management for found text
     * - User feedback for search results
     */
    private void showFindDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Find");
        dialog.setHeaderText("Find text in document");
        dialog.setContentText("Enter text to find:");
        
        dialog.showAndWait().ifPresent(searchText -> {
            if (!searchText.isEmpty()) {
                findAndHighlight(searchText);
            }
        });
    }

    /**
     * Finds and highlights the specified text in the document.
     * 
     * Performs case-insensitive search for the given text and highlights
     * the first occurrence found. If text is found, selects it and sets
     * focus to the text area. If not found, shows an information dialog.
     * 
     * @param searchText The text to search for in the document
     */
    private void findAndHighlight(String searchText) {
        String content = textArea.getText();
        int index = content.toLowerCase().indexOf(searchText.toLowerCase());
        
        if (index >= 0) {
            textArea.selectRange(index, index + searchText.length());
            textArea.requestFocus();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Find");
            alert.setHeaderText(null);
            alert.setContentText("Text not found: " + searchText);
            alert.showAndWait();
        }
    }

    // ============================================================================
    // PUBLIC API METHODS
    // ============================================================================

    /**
     * Sets the content of the text editor and marks it as saved.
     * 
     * This method updates both the displayed text and the baseline content
     * for change tracking. The hasUnsavedChanges property is reset to false
     * since this represents freshly loaded or saved content.
     * 
     * @param content The text content to display in the editor
     */
    public void setContent(String content) {
        textArea.setText(content);
        originalContent = content;
        hasUnsavedChanges.set(false);
    }

    /**
     * Gets the current content of the text editor.
     * 
     * @return The complete text content currently in the editor
     */
    public String getContent() {
        return textArea.getText();
    }

    /**
     * Marks the current content as saved by updating the baseline.
     * 
     * This method updates the original content baseline to the current
     * text and resets the unsaved changes flag. Used after successful
     * save operations to prevent false unsaved indicators.
     */
    public void markAsSaved() {
        originalContent = textArea.getText();
        hasUnsavedChanges.set(false);
    }

    /**
     * Gets the property for tracking unsaved changes.
     * 
     * This property automatically updates when text content changes
     * compared to the original (saved) content. External components
     * can bind to this property for reactive UI updates.
     * 
     * @return BooleanProperty indicating whether there are unsaved changes
     */
    public BooleanProperty hasUnsavedChangesProperty() {
        return hasUnsavedChanges;
    }

    /**
     * Gets the property for tracking the current file path.
     * 
     * This property stores the path of the currently loaded file and
     * can be bound to external components for display in window titles,
     * status bars, or other UI elements.
     * 
     * @return StringProperty containing the current file path
     */
    public StringProperty currentFilePathProperty() {
        return currentFilePath;
    }

    /**
     * Sets the current file path for the document being edited.
     * 
     * Used to track which file is currently loaded in the editor.
     * An empty string indicates a new, unsaved document.
     * 
     * @param path The file path of the current document
     */
    public void setCurrentFilePath(String path) {
        currentFilePath.set(path);
    }

    /**
     * Clears the editor content and resets all state.
     * 
     * This method:
     * - Clears all text content
     * - Resets the original content baseline
     * - Clears the current file path
     * - Resets the unsaved changes flag
     * 
     * Used when creating a new document or closing the current one.
     */
    public void clear() {
        textArea.clear();
        originalContent = "";
        currentFilePath.set("");
        hasUnsavedChanges.set(false);
    }

    /**
     * Requests focus for the text editing area.
     * 
     * Sets keyboard focus to the text area so the user can immediately
     * start typing. Used after loading files or when switching focus
     * back to the editor.
     */
    public void requestFocus() {
        textArea.requestFocus();
    }

    // ============================================================================
    // CUSTOM EVENT CLASSES
    // ============================================================================

    /**
     * Custom event for requesting save operations.
     * 
     * This event is fired when the user presses Ctrl+S or uses other
     * save-related shortcuts. Parent components can listen for this
     * event to handle save operations appropriately.
     */
    public static class SaveRequestEvent extends javafx.event.Event {
        /** Event type for save request events. */
        public static final javafx.event.EventType<SaveRequestEvent> SAVE_REQUEST = 
            new javafx.event.EventType<>("SAVE_REQUEST");

        /**
         * Constructor for save request events.
         */
        public SaveRequestEvent() {
            super(SAVE_REQUEST);
        }
    }

    /**
     * Custom event for requesting new file operations.
     * 
     * This event is fired when the user presses Ctrl+N or uses other
     * new file shortcuts. Parent components can listen for this event
     * to handle new file creation appropriately.
     */
    public static class NewFileRequestEvent extends javafx.event.Event {
        /** Event type for new file request events. */
        public static final javafx.event.EventType<NewFileRequestEvent> NEW_FILE_REQUEST = 
            new javafx.event.EventType<>("NEW_FILE_REQUEST");

        /**
         * Constructor for new file request events.
         */
        public NewFileRequestEvent() {
            super(NEW_FILE_REQUEST);
        }
    }
} 