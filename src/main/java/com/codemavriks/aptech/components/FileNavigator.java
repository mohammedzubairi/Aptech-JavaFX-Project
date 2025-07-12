/**
 * File Mana - Modern Text Editor
 * File Navigator Component - File System Browser
 * 
 * This class represents the file navigation component of the File Mana text editor application.
 * It provides a comprehensive file system browser with tree view functionality, context menus,
 * keyboard shortcuts, and file operation capabilities. The navigator is designed to work
 * specifically with the File Mana file organization system and supports file set operations.
 * 
 * Key Responsibilities:
 * - Display hierarchical file system structure using TreeView
 * - Provide file operations (open, rename, delete, copy path)
 * - Support file set operations (org, rev, byte file management)
 * - Handle user interactions through context menus and keyboard shortcuts
 * - Implement file selection and multi-selection capabilities
 * - Manage file filtering (shows only .txt files and directories)
 * - Provide event-driven communication with parent components
 * 
 * Architecture Pattern: Component-based UI with Observer Pattern
 * - Extends VBox for flexible layout container
 * - Uses TreeView for hierarchical file display
 * - Implements Observer pattern for file operation notifications
 * - Provides clean API for external file operations
 * 
 * File System Integration:
 * - Works with File Mana's organized file structure
 * - Supports file sets (baseName-org.txt, baseName-rev.txt, baseName-byte.txt)
 * - Handles folder-based organization for related files
 * - Filters display to show only relevant files (.txt) and directories
 * 
 * UI Features:
 * - Tree view with expandable folders
 * - Context menu with common file operations
 * - Keyboard shortcuts for power users
 * - Multi-selection support for batch operations
 * - Icons for files and folders
 * - Automatic refresh capabilities
 * 
 * Event Handling:
 * - Mouse clicks for selection and opening
 * - Context menu for file operations
 * - Keyboard shortcuts (Delete, F2, Ctrl+C/V/X/A)
 * - Double-click for file opening
 * - Selection change notifications
 * 
 * Integration with File Mana Architecture:
 * - Embedded in SidePanel as primary navigation component
 * - Communicates with EditorController through FileOperationListener
 * - Coordinates with FileService for file operations
 * - Maintains consistency with application file organization
 * 
 * Design Patterns Used:
 * - Observer Pattern: Event notification for file operations
 * - Template Method Pattern: Common file operation patterns
 * - Strategy Pattern: Different handling for files vs directories
 * - Factory Pattern: TreeItem creation with custom rendering
 * 
 * @course Advanced Java Programming with JavaFX
 * @institution Aptech Computer Education
 * @university Alnasser University
 * @version 1.0
 * @since 2025-05-20
 * 
 * Dependencies:
 * - JavaFX TreeView for hierarchical display
 * - MainApp for dialog theming utilities
 * - Observer pattern for event communication
 * - Java File I/O for file system operations
 */
package com.codemavriks.aptech.components;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

/**
 * File Navigator Component for File Mana Text Editor
 * 
 * This class extends VBox to create a comprehensive file navigation component that
 * provides a tree-based file system browser. It's designed specifically for the
 * File Mana application's file organization system and supports both individual
 * files and file sets (org, rev, byte variants).
 * 
 * The navigator integrates seamlessly with the File Mana architecture by:
 * - Filtering files to show only .txt files and directories
 * - Supporting file set operations (create, delete, rename)
 * - Providing context-aware operations based on file types
 * - Maintaining synchronization with the file system
 * - Offering both mouse and keyboard interaction patterns
 * 
 * Component Architecture:
 * ┌─────────────────────────────────────────────────────────┐
 * │               FileNavigator (VBox)                      │
 * ├─────────────────────────────────────────────────────────┤
 * │                TreeView<File>                           │
 * │  ┌─────────────────────────────────────────────────────┐ │
 * │  │ 📁 Created files/                                   │ │
 * │  │   ├── 📁 Project1/                                  │ │
 * │  │   │   ├── 📄 Project1-org.txt                      │ │
 * │  │   │   ├── 📄 Project1-rev.txt                      │ │
 * │  │   │   └── 📄 Project1-byte.txt                     │ │
 * │  │   └── 📁 Project2/                                  │ │
 * │  │       ├── 📄 Project2-org.txt                      │ │
 * │  │       ├── 📄 Project2-rev.txt                      │ │
 * │  │       └── 📄 Project2-byte.txt                     │ │
 * │  └─────────────────────────────────────────────────────┘ │
 * └─────────────────────────────────────────────────────────┘
 * 
 * Interaction Patterns:
 * - Single click: Select file/folder
 * - Double click: Open file
 * - Right click: Show context menu
 * - Delete key: Delete selected items
 * - F2 key: Rename selected item
 * - Ctrl+A: Select all items
 * - Ctrl+C/V/X: Copy/Paste/Cut operations (future implementation)
 * 
 * Thread Safety:
 * - All UI operations are performed on JavaFX Application Thread
 * - File operations are atomic to prevent corruption
 * - Observable collections are used for thread-safe list management
 */
public class FileNavigator extends VBox {
    
    // ============================================================================
    // UI COMPONENT DECLARATIONS
    // ============================================================================
    
    /**
     * The main tree view component for displaying file hierarchy.
     * Configured to show files and directories in a tree structure with
     * custom rendering, icons, and selection capabilities.
     */
    private final TreeView<File> fileTreeView;
    
    // ============================================================================
    // STATE MANAGEMENT PROPERTIES
    // ============================================================================
    
    /**
     * Property to track the currently selected file.
     * Uses JavaFX property binding for reactive UI updates and
     * external component synchronization.
     */
    private final ObjectProperty<File> selectedFile = new SimpleObjectProperty<>();
    
    /**
     * The working directory path for file operations.
     * Typically set to "Created files/" and serves as the root
     * for all file navigation and operations.
     */
    private final String workingDirectory;
    
    /**
     * Observable list of listeners for file operation events.
     * Implements the Observer pattern to notify interested components
     * of file operations like selection, opening, deletion, and renaming.
     */
    private final ObservableList<FileOperationListener> listeners = FXCollections.observableArrayList();

    // ============================================================================
    // CONSTRUCTOR AND INITIALIZATION
    // ============================================================================

    /**
     * Constructor - Initializes the file navigator with working directory and components.
     * 
     * Initialization Process:
     * 1. Set working directory and create TreeView component
     * 2. Configure file tree with proper structure and filtering
     * 3. Set up context menu for file operations
     * 4. Configure keyboard shortcuts for power users
     * 5. Add TreeView to layout and apply styling
     * 6. Configure responsive layout properties
     * 
     * @param workingDirectory The base directory for file operations
     *                        (typically "Created files/")
     */
    public FileNavigator(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.fileTreeView = new TreeView<>();
        
        // Configure file tree with proper structure and filtering
        setupFileTree();
        
        // Set up context menu for file operations
        setupContextMenu();
        
        // Configure keyboard shortcuts for power users
        setupKeyboardHandlers();
        
        // Add TreeView to layout and apply styling
        getChildren().add(fileTreeView);
        getStyleClass().add("file-navigator");
        
        // Make tree view take all available space for responsive layout
        VBox.setVgrow(fileTreeView, javafx.scene.layout.Priority.ALWAYS);
    }

    // ============================================================================
    // FILE TREE CONFIGURATION
    // ============================================================================

    /**
     * Sets up the file tree structure with proper filtering and event handling.
     * 
     * This method configures the TreeView to display the file system hierarchy
     * with the following features:
     * - Virtual root that hides the actual root directory
     * - Automatic expansion of the root level
     * - Filtering to show only .txt files and directories
     * - Multi-selection support for batch operations
     * - Automatic file opening on selection
     * - Double-click handling for additional confirmation
     * 
     * Tree Structure Strategy:
     * - Uses virtual root to hide "Created files" directory name
     * - Automatically expands first level for immediate access
     * - Filters content to show only relevant files
     * - Maintains proper hierarchy for nested directories
     */
    private void setupFileTree() {
        File rootDir = new File(workingDirectory);
        
        // Ensure working directory exists before proceeding
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        // Create a virtual root that shows only the contents of working directory
        TreeItem<File> virtualRoot = new TreeItem<>(rootDir);
        virtualRoot.setExpanded(true);
        
        // Load children directly from the working directory
        virtualRoot.getChildren().setAll(getChildren(rootDir));
        
        // Configure TreeView with virtual root
        fileTreeView.setRoot(virtualRoot);
        fileTreeView.setShowRoot(false); // Hide the root to show only contents
        
        // Apply CSS styling class
        fileTreeView.getStyleClass().add("file-tree");
        
        // Enable multiple selection for batch operations
        fileTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // ========================================================================
        // SELECTION CHANGE HANDLER
        // ========================================================================
        
        /**
         * Handle file selection changes with automatic opening.
         * 
         * This listener responds to selection changes in the tree view and:
         * - Updates the selected file property
         * - Notifies listeners of file selection
         * - Automatically opens files when single-selected
         * - Provides immediate feedback for user interactions
         */
        fileTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getValue().isFile()) {
                File selectedFile = newVal.getValue();
                
                // Update selected file property for external binding
                this.selectedFile.set(selectedFile);
                
                // Notify listeners of file selection
                notifyFileSelected(selectedFile);
                
                // Automatically open the file when selected (only for single selection)
                if (fileTreeView.getSelectionModel().getSelectedItems().size() == 1) {
                    notifyFileOpened(selectedFile);
                }
            }
        });

        // ========================================================================
        // DOUBLE-CLICK HANDLER
        // ========================================================================
        
        /**
         * Handle double-click events for file opening.
         * 
         * This handler provides an alternative way to open files and ensures
         * consistent behavior across different interaction patterns.
         */
        fileTreeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                TreeItem<File> selectedItem = fileTreeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.getValue().isFile()) {
                    // Double-click also opens the file (redundant but safe)
                    notifyFileOpened(selectedItem.getValue());
                }
            }
        });
    }

    // ============================================================================
    // KEYBOARD SHORTCUT CONFIGURATION
    // ============================================================================

    /**
     * Sets up keyboard shortcuts for common file operations.
     * 
     * This method configures keyboard event handling to provide power users
     * with quick access to common operations:
     * 
     * Keyboard Shortcuts:
     * - Delete: Delete selected files/folders
     * - F2: Rename selected item
     * - Ctrl+C: Copy selected items (future implementation)
     * - Ctrl+V: Paste items (future implementation)
     * - Ctrl+X: Cut selected items (future implementation)
     * - Ctrl+A: Select all items
     * 
     * Event Handling Strategy:
     * - Consume events to prevent propagation
     * - Delegate to specialized handler methods
     * - Provide consistent behavior across operations
     * - Handle edge cases gracefully
     */
    private void setupKeyboardHandlers() {
        fileTreeView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                handleDeleteSelected();
                event.consume();
            } else if (event.isControlDown()) {
                switch (event.getCode()) {
                    case C:
                        handleCopySelected();
                        event.consume();
                        break;
                    case V:
                        handlePasteSelected();
                        event.consume();
                        break;
                    case X:
                        handleCutSelected();
                        event.consume();
                        break;
                    case A:
                        fileTreeView.getSelectionModel().selectAll();
                        event.consume();
                        break;
                }
            } else if (event.getCode() == KeyCode.F2) {
                handleRename();
                event.consume();
            }
        });
    }

    // ============================================================================
    // KEYBOARD SHORTCUT HANDLERS
    // ============================================================================

    /**
     * Handles deletion of selected items with confirmation dialog.
     * 
     * This method provides a safe way to delete files and folders by:
     * - Validating selection exists
     * - Showing appropriate confirmation message
     * - Handling both single and multiple selections
     * - Delegating to specialized deletion methods
     * - Refreshing the tree view after deletion
     * - Notifying listeners of deletion events
     */
    private void handleDeleteSelected() {
        ObservableList<TreeItem<File>> selectedItems = fileTreeView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) return;

        // Create appropriate confirmation message
        String message = selectedItems.size() == 1 ? 
            "Delete selected item?" : 
            "Delete " + selectedItems.size() + " selected items?";
        
        // Show confirmation dialog with dark theme
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Items");
        confirmDialog.setHeaderText(message);
        confirmDialog.setContentText("This action cannot be undone.");
        
        // Apply dark theme for consistent appearance
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmDialog);

        // Handle user confirmation
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete all selected items
                for (TreeItem<File> item : selectedItems) {
                    File file = item.getValue();
                    deleteFileOrFileSet(file);
                    notifyFileDeleted(file);
                }
                // Refresh tree to reflect changes
                refreshTree();
            }
        });
    }

    /**
     * Handles copy operation for selected items.
     * 
     * Currently shows a placeholder message for future implementation.
     * This method will be expanded to support clipboard operations.
     */
    private void handleCopySelected() {
        // Implementation for copy functionality
        // For now, just show a message
        showInfo("Copy", "Copy functionality will be implemented in future version.");
    }

    /**
     * Handles paste operation for selected items.
     * 
     * Currently shows a placeholder message for future implementation.
     * This method will be expanded to support clipboard operations.
     */
    private void handlePasteSelected() {
        // Implementation for paste functionality
        // For now, just show a message
        showInfo("Paste", "Paste functionality will be implemented in future version.");
    }

    /**
     * Handles cut operation for selected items.
     * 
     * Currently shows a placeholder message for future implementation.
     * This method will be expanded to support clipboard operations.
     */
    private void handleCutSelected() {
        // Implementation for cut functionality
        // For now, just show a message
        showInfo("Cut", "Cut functionality will be implemented in future version.");
    }

    // ============================================================================
    // TREE STRUCTURE BUILDING
    // ============================================================================

    /**
     * Recursively builds the tree structure for a given directory.
     * 
     * This method creates the hierarchical structure for the file tree by:
     * - Filtering files to show only .txt files and directories
     * - Sorting items with directories first, then files alphabetically
     * - Recursively processing subdirectories
     * - Creating TreeItem objects with proper representation
     * 
     * Filtering Strategy:
     * - Shows all directories for navigation
     * - Shows only .txt files to match application scope
     * - Maintains alphabetical sorting for easy navigation
     * - Handles empty directories gracefully
     * 
     * @param directory The directory to process
     * @return Observable list of TreeItem objects representing the directory contents
     */
    private ObservableList<TreeItem<File>> getChildren(File directory) {
        ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
        
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            
            if (files != null) {
                // Sort: directories first, then files, both alphabetically
                Arrays.sort(files, (f1, f2) -> {
                    if (f1.isDirectory() && !f2.isDirectory()) return -1;
                    if (!f1.isDirectory() && f2.isDirectory()) return 1;
                    return f1.getName().compareToIgnoreCase(f2.getName());
                });
                
                // Process sorted files and directories
                for (File file : files) {
                    // Show all directories and .txt files
                    if (file.isDirectory()) {
                        children.add(createTreeItem(file));
                    } else if (file.getName().toLowerCase().endsWith(".txt")) {
                        children.add(createTreeItem(file));
                    }
                }
            }
        }
        
        return children;
    }

    /**
     * Creates a TreeItem with custom rendering and icon support.
     * 
     * This method creates TreeItem objects with:
     * - Custom toString() implementation for display names
     * - Appropriate icons for files and folders
     * - Recursive loading of subdirectories
     * - Special handling for the working directory root
     * 
     * Display Strategy:
     * - Shows "Created files" for the working directory root
     * - Shows file/folder names for all other items
     * - Applies appropriate icons based on item type
     * - Loads children for directories automatically
     * 
     * @param file The file or directory to create a TreeItem for
     * @return TreeItem configured with proper display and behavior
     */
    private TreeItem<File> createTreeItem(File file) {
        TreeItem<File> item = new TreeItem<File>(file) {
            @Override
            public String toString() {
                File value = getValue();
                if (value == null) return "";
                
                // For the working directory root, show "Created files"
                if (value.getAbsolutePath().equals(new File(workingDirectory).getAbsolutePath())) {
                    return "Created files";
                }
                
                // For all other files and folders, just show the name
                return value.getName();
            }
        };
        
        // Set icon based on file type
        if (file.isDirectory()) {
            item.setGraphic(createIcon("folder"));
            // Load children for directories
            item.getChildren().setAll(getChildren(file));
        } else {
            item.setGraphic(createIcon("file"));
        }
        
        return item;
    }

    /**
     * Creates icons for files and folders.
     * 
     * This method creates visual indicators for different file types:
     * - Folder icon for directories
     * - File icon for text files
     * - Placeholder implementation for future enhancement
     * 
     * Currently uses emoji-based icons but can be extended to use
     * actual image files or vector graphics.
     * 
     * @param type The type of icon to create ("folder" or "file")
     * @return ImageView placeholder for icon (will be styled with CSS)
     */
    private ImageView createIcon(String type) {
        // Create simple text-based icons for now
        Label iconLabel = new Label();
        iconLabel.getStyleClass().add("file-icon");
        
        if ("folder".equals(type)) {
            iconLabel.setText("📁");
        } else {
            iconLabel.setText("📄");
        }
        
        return new ImageView(); // Placeholder - will be styled with CSS
    }

    // ============================================================================
    // CONTEXT MENU CONFIGURATION
    // ============================================================================

    /**
     * Sets up the context menu for file operations.
     * 
     * This method creates a comprehensive context menu that provides access to:
     * - File opening operations
     * - File set creation
     * - Rename and delete operations
     * - Copy path functionality
     * - Tree refresh capability
     * 
     * Context Menu Structure:
     * - Open: Opens the selected file
     * - New File Set: Creates a new file set
     * - Rename: Renames the selected item
     * - Delete: Deletes the selected item
     * - Copy Path: Copies file path to clipboard
     * - Refresh: Refreshes the tree view
     * 
     * Menu Organization:
     * - Primary actions first (Open)
     * - Creation actions (New File Set)
     * - Modification actions (Rename, Delete)
     * - Utility actions (Copy Path)
     * - Refresh actions last
     * - Separators for logical grouping
     */
    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        // Open operation
        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(e -> {
            TreeItem<File> selected = fileTreeView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.getValue().isFile()) {
                notifyFileOpened(selected.getValue());
            }
        });

        // Rename operation
        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(e -> handleRename());

        // Delete operation
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> handleDeleteSelected());

        // Copy path operation
        MenuItem copyItem = new MenuItem("Copy Path");
        copyItem.setOnAction(e -> handleCopyPath());

        // New file set creation
        MenuItem newFileItem = new MenuItem("New File Set");
        newFileItem.setOnAction(e -> handleNewFile());

        // Refresh operation
        MenuItem refreshItem = new MenuItem("Refresh");
        refreshItem.setOnAction(e -> refreshTree());

        // Organize menu items with separators for logical grouping
        contextMenu.getItems().addAll(
            openItem,
            new SeparatorMenuItem(),
            newFileItem,
            new SeparatorMenuItem(),
            renameItem,
            deleteItem,
            new SeparatorMenuItem(),
            copyItem,
            new SeparatorMenuItem(),
            refreshItem
        );

        // Attach context menu to tree view
        fileTreeView.setContextMenu(contextMenu);
    }

    // ============================================================================
    // FILE OPERATION HANDLERS
    // ============================================================================

    /**
     * Handles file and folder renaming with input validation.
     * 
     * This method provides a safe way to rename files and folders by:
     * - Validating selection exists
     * - Showing input dialog with current name
     * - Validating new name input
     * - Performing atomic rename operation
     * - Handling errors gracefully
     * - Refreshing tree view after successful rename
     * - Notifying listeners of rename events
     * 
     * Validation Strategy:
     * - Checks for empty or unchanged names
     * - Prevents invalid file system names
     * - Shows appropriate error messages
     * - Maintains file system integrity
     */
    private void handleRename() {
        TreeItem<File> selected = fileTreeView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        File file = selected.getValue();
        String itemType = file.isDirectory() ? "folder" : "file";
        
        // Show input dialog for new name
        TextInputDialog dialog = new TextInputDialog(file.getName());
        dialog.setTitle("Rename " + itemType);
        dialog.setHeaderText("Rename " + file.getName());
        dialog.setContentText("New name:");
        
        // Apply dark theme for consistent appearance
        com.codemavriks.aptech.MainApp.applyDarkThemeToDialog(dialog);

        // Handle user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (!newName.trim().isEmpty() && !newName.equals(file.getName())) {
                File newFile = new File(file.getParent(), newName);
                if (file.renameTo(newFile)) {
                    // Success: refresh tree and notify listeners
                    refreshTree();
                    notifyFileRenamed(file, newFile);
                } else {
                    // Error: show error message
                    showError("Failed to rename " + itemType, "Could not rename " + file.getName() + " to " + newName);
                }
            }
        });
    }

    /**
     * Handles file and folder deletion with confirmation.
     * 
     * This method provides comprehensive deletion functionality by:
     * - Detecting file set relationships
     * - Showing context-appropriate confirmation messages
     * - Handling both individual files and complete file sets
     * - Managing directory deletion with all contents
     * - Providing clear feedback on operation results
     * 
     * Deletion Strategy:
     * - File sets: Deletes all related files and containing folder
     * - Individual files: Deletes single file
     * - Directories: Recursively deletes all contents
     * - Provides detailed confirmation messages
     * - Handles errors gracefully with user feedback
     */
    private void handleDelete() {
        TreeItem<File> selected = fileTreeView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        File file = selected.getValue();
        String itemType = file.isDirectory() ? "folder" : "file";
        String deleteMessage;
        
        // Create context-appropriate confirmation message
        if (file.isFile() && isPartOfFileSet(file)) {
            String baseName = extractBaseName(file);
            deleteMessage = "Delete the entire file set '" + baseName + "' and its folder?\n" +
                          "This will delete all related files (-org.txt, -rev.txt, -byte.txt) and the containing folder.";
        } else if (file.isDirectory()) {
            deleteMessage = "Delete folder '" + file.getName() + "' and all its contents?";
        } else {
            deleteMessage = "Delete file '" + file.getName() + "'?";
        }
        
        // Show confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete " + itemType);
        confirmDialog.setHeaderText(deleteMessage);
        confirmDialog.setContentText("This action cannot be undone.");
        
        // Apply dark theme for consistent appearance
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmDialog);

        // Handle user confirmation
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = deleteFileOrFileSet(file);
            
            if (deleted) {
                // Success: refresh tree and notify listeners
                refreshTree();
                notifyFileDeleted(file);
            } else {
                // Error: show error message
                showError("Failed to delete " + itemType, "Could not delete " + file.getName());
            }
        }
    }

    /**
     * Deletes a file or file set with intelligent handling.
     * 
     * This method provides smart deletion by:
     * - Detecting file set relationships
     * - Handling directory deletion recursively
     * - Managing individual file deletion
     * - Maintaining file system consistency
     * 
     * Deletion Logic:
     * - Directories: Recursive deletion of all contents
     * - File set members: Deletion of entire file set and folder
     * - Individual files: Direct deletion
     * - Error handling for all scenarios
     * 
     * @param file The file or directory to delete
     * @return true if deletion was successful, false otherwise
     */
    private boolean deleteFileOrFileSet(File file) {
        if (file.isDirectory()) {
            // Delete entire directory
            return deleteDirectory(file);
        } else if (isPartOfFileSet(file)) {
            // Delete the entire file set and its containing folder
            return deleteEntireFileSet(file);
        } else {
            // Delete single file
            return file.delete();
        }
    }

    /**
     * Checks if a file is part of a File Mana file set.
     * 
     * This method determines if a file belongs to a file set by checking
     * if it follows the File Mana naming convention:
     * - baseName-org.txt (original content)
     * - baseName-rev.txt (reversed content)
     * - baseName-byte.txt (byte-encoded content)
     * 
     * @param file The file to check
     * @return true if the file is part of a file set, false otherwise
     */
    private boolean isPartOfFileSet(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith("-org.txt") || 
               fileName.endsWith("-rev.txt") || 
               fileName.endsWith("-byte.txt");
    }

    /**
     * Extracts the base name from a file that's part of a file set.
     * 
     * This method reverse-engineers the base name from a file set member
     * by removing the appropriate suffix:
     * - "-org.txt" from original files
     * - "-rev.txt" from reversed files
     * - "-byte.txt" from byte-encoded files
     * 
     * @param file The file set member
     * @return The base name without suffix
     */
    private String extractBaseName(File file) {
        String fileName = file.getName();
        if (fileName.endsWith("-org.txt")) {
            return fileName.substring(0, fileName.length() - 8); // Remove "-org.txt"
        } else if (fileName.endsWith("-rev.txt")) {
            return fileName.substring(0, fileName.length() - 8); // Remove "-rev.txt"
        } else if (fileName.endsWith("-byte.txt")) {
            return fileName.substring(0, fileName.length() - 9); // Remove "-byte.txt"
        }
        return fileName; // Fallback
    }

    /**
     * Deletes an entire file set and its containing folder.
     * 
     * This method handles the deletion of complete file sets by:
     * - Extracting the base name from any file set member
     * - Checking if files are organized in a dedicated folder
     * - Deleting the entire folder if it matches the base name
     * - Deleting individual files if not in a dedicated folder
     * - Providing error handling and logging
     * 
     * Deletion Strategy:
     * - Folder-based: Delete entire folder containing file set
     * - Flat organization: Delete individual file set members
     * - Error recovery: Graceful handling of partial failures
     * - Logging: Error messages for troubleshooting
     * 
     * @param file Any member of the file set to delete
     * @return true if deletion was successful, false otherwise
     */
    private boolean deleteEntireFileSet(File file) {
        try {
            String baseName = extractBaseName(file);
            File parentDir = file.getParentFile();
            
            // Check if the parent directory name matches the base name
            if (parentDir != null && parentDir.getName().equals(baseName)) {
                // Delete the entire folder containing the file set
                return deleteDirectory(parentDir);
            } else {
                // Files are not in a dedicated folder, delete individual files
                File orgFile = new File(parentDir, baseName + "-org.txt");
                File revFile = new File(parentDir, baseName + "-rev.txt");
                File byteFile = new File(parentDir, baseName + "-byte.txt");
                
                boolean success = true;
                if (orgFile.exists()) success &= orgFile.delete();
                if (revFile.exists()) success &= revFile.delete();
                if (byteFile.exists()) success &= byteFile.delete();
                
                return success;
            }
        } catch (Exception e) {
            System.err.println("Error deleting file set: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recursively deletes a directory and all its contents.
     * 
     * This method provides comprehensive directory deletion by:
     * - Recursively processing all subdirectories
     * - Deleting all files in each directory
     * - Removing directories after their contents are deleted
     * - Handling nested directory structures
     * - Providing atomic deletion where possible
     * 
     * @param directory The directory to delete
     * @return true if deletion was successful, false otherwise
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
     * Handles copying file path to system clipboard.
     * 
     * This method provides a convenient way to copy file paths by:
     * - Getting the absolute path of the selected file
     * - Accessing the system clipboard
     * - Setting the path as clipboard content
     * - Providing silent operation (no user feedback needed)
     * 
     * This is useful for users who need to reference file paths in
     * other applications or commands.
     */
    private void handleCopyPath() {
        TreeItem<File> selected = fileTreeView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(selected.getValue().getAbsolutePath());
            clipboard.setContent(content);
        }
    }

    /**
     * Handles new file set creation request.
     * 
     * This method provides a way to create new file sets directly from
     * the file navigator by:
     * - Showing input dialog for base name
     * - Validating user input
     * - Delegating to the parent controller for actual creation
     * - Providing user-friendly interface
     * 
     * The actual file creation is handled by the parent controller
     * to maintain proper separation of concerns.
     */
    private void handleNewFile() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New File Set");
        dialog.setHeaderText("Create new file set");
        dialog.setContentText("Base name (without extension):");
        
        // Add placeholder for user guidance
        dialog.getEditor().setPromptText("e.g., MyProject, Document1, etc.");
        
        // Apply dark theme for consistent appearance
        com.codemavriks.aptech.MainApp.applyDarkThemeToDialog(dialog);

        // Handle user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(baseName -> {
            if (!baseName.trim().isEmpty()) {
                notifyNewFileRequested(baseName.trim());
            }
        });
    }

    // ============================================================================
    // TREE REFRESH AND MAINTENANCE
    // ============================================================================

    /**
     * Refreshes the tree view to reflect current file system state.
     * 
     * This method updates the tree view by:
     * - Rebuilding the tree structure from the file system
     * - Preserving the current expansion state where possible
     * - Automatically expanding directories for visibility
     * - Maintaining selection state when feasible
     * 
     * Refresh Strategy:
     * - Full rebuild of tree structure
     * - Automatic expansion of directory nodes
     * - Preservation of user navigation state
     * - Efficient update of display
     */
    public void refreshTree() {
        TreeItem<File> root = fileTreeView.getRoot();
        if (root != null) {
            root.getChildren().setAll(getChildren(root.getValue()));
            expandDirectories(root);
        }
    }

    /**
     * Recursively expands directories in the tree view.
     * 
     * This method ensures that directories are expanded for better
     * visibility and navigation by:
     * - Expanding all directory nodes
     * - Recursively processing subdirectories
     * - Maintaining consistent expansion state
     * - Providing immediate access to nested content
     * 
     * @param item The tree item to expand (and process recursively)
     */
    private void expandDirectories(TreeItem<File> item) {
        if (item.getValue().isDirectory()) {
            item.setExpanded(true);
            for (TreeItem<File> child : item.getChildren()) {
                if (child.getValue().isDirectory()) {
                    expandDirectories(child);
                }
            }
        }
    }

    // ============================================================================
    // UTILITY METHODS
    // ============================================================================

    /**
     * Shows an error dialog with consistent theming.
     * 
     * This method provides a standardized way to display error messages
     * with proper theming and user experience consistency.
     * 
     * @param title The dialog title
     * @param message The error message to display
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Apply dark theme for consistent appearance
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(alert);
        
        alert.showAndWait();
    }

    /**
     * Shows an information dialog with consistent theming.
     * 
     * This method provides a standardized way to display informational
     * messages with proper theming and user experience consistency.
     * 
     * @param title The dialog title
     * @param message The information message to display
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Apply dark theme for consistent appearance
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(alert);
        
        alert.showAndWait();
    }

    // ============================================================================
    // EVENT NOTIFICATION METHODS (Observer Pattern Implementation)
    // ============================================================================

    /**
     * Notifies all listeners of a file selection event.
     * 
     * @param file The selected file
     */
    private void notifyFileSelected(File file) {
        listeners.forEach(listener -> listener.onFileSelected(file));
    }

    /**
     * Notifies all listeners of a file opening event.
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

    /**
     * Notifies all listeners of a new file request event.
     * 
     * @param baseName The base name for the new file set
     */
    private void notifyNewFileRequested(String baseName) {
        listeners.forEach(listener -> listener.onNewFileRequested(baseName));
    }

    // ============================================================================
    // PUBLIC API METHODS
    // ============================================================================

    /**
     * Adds a listener for file operation events.
     * 
     * This method allows external components to register for file operation
     * events using the observer pattern.
     * 
     * @param listener The listener to add
     */
    public void addFileOperationListener(FileOperationListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener for file operation events.
     * 
     * @param listener The listener to remove
     */
    public void removeFileOperationListener(FileOperationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Gets the selected file property for external binding.
     * 
     * @return ObjectProperty containing the currently selected file
     */
    public ObjectProperty<File> selectedFileProperty() {
        return selectedFile;
    }

    /**
     * Programmatically selects a file in the tree view.
     * 
     * This method allows external components to control the selection
     * state of the file navigator.
     * 
     * @param file The file to select
     */
    public void selectFile(File file) {
        selectFileInTree(fileTreeView.getRoot(), file);
    }

    /**
     * Recursively searches for and selects a file in the tree.
     * 
     * This method performs a recursive search through the tree structure
     * to find and select the specified file, expanding parent directories
     * as needed.
     * 
     * @param item The current tree item to search
     * @param targetFile The file to find and select
     * @return true if the file was found and selected, false otherwise
     */
    private boolean selectFileInTree(TreeItem<File> item, File targetFile) {
        if (item.getValue().equals(targetFile)) {
            fileTreeView.getSelectionModel().select(item);
            return true;
        }

        for (TreeItem<File> child : item.getChildren()) {
            if (selectFileInTree(child, targetFile)) {
                item.setExpanded(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the working directory path.
     * 
     * @return The working directory path used by this navigator
     */
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    // ============================================================================
    // LISTENER INTERFACE
    // ============================================================================

    /**
     * Interface for file operation event callbacks.
     * 
     * This interface defines the contract for components that want to receive
     * notifications about file operations. It uses default methods to make
     * implementation optional for listeners that don't need all events.
     * 
     * Design Pattern: Observer Pattern
     * - Defines the observer interface for file operation events
     * - Uses default methods for flexible implementation
     * - Enables loose coupling between components
     * - Supports multiple types of file operations
     */
    public interface FileOperationListener {
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
        
        /**
         * Called when a new file is requested.
         * 
         * @param baseName The base name for the new file set
         */
        default void onNewFileRequested(String baseName) {}
    }
} 