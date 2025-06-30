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

public class FileNavigator extends VBox {
    private final TreeView<File> fileTreeView;
    private final ObjectProperty<File> selectedFile = new SimpleObjectProperty<>();
    private final String workingDirectory;
    private final ObservableList<FileOperationListener> listeners = FXCollections.observableArrayList();

    public FileNavigator(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.fileTreeView = new TreeView<>();
        
        setupFileTree();
        setupContextMenu();
        setupKeyboardHandlers();
        
        getChildren().add(fileTreeView);
        getStyleClass().add("file-navigator");
        
        // Make tree view take all available space
        VBox.setVgrow(fileTreeView, javafx.scene.layout.Priority.ALWAYS);
    }

    private void setupFileTree() {
        File rootDir = new File(workingDirectory);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        // Create a virtual root that shows only the contents of "Created files"
        TreeItem<File> virtualRoot = new TreeItem<>(rootDir);
        virtualRoot.setExpanded(true);
        
        // Load children directly from the working directory
        virtualRoot.getChildren().setAll(getChildren(rootDir));
        
        fileTreeView.setRoot(virtualRoot);
        fileTreeView.setShowRoot(false); // Hide the root to show only contents
        
        fileTreeView.getStyleClass().add("file-tree");
        
        // Enable multiple selection
        fileTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // Handle file selection - automatically open files when selected
        fileTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getValue().isFile()) {
                File selectedFile = newVal.getValue();
                
                this.selectedFile.set(selectedFile);
                notifyFileSelected(selectedFile);
                
                // Automatically open the file when selected (only for single selection)
                if (fileTreeView.getSelectionModel().getSelectedItems().size() == 1) {
                    notifyFileOpened(selectedFile);
                }
            }
        });

        // Handle double-click for additional confirmation (optional)
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

    private void handleDeleteSelected() {
        ObservableList<TreeItem<File>> selectedItems = fileTreeView.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) return;

        String message = selectedItems.size() == 1 ? 
            "Delete selected item?" : 
            "Delete " + selectedItems.size() + " selected items?";
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Items");
        confirmDialog.setHeaderText(message);
        confirmDialog.setContentText("This action cannot be undone.");
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmDialog);

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                for (TreeItem<File> item : selectedItems) {
                    File file = item.getValue();
                    deleteFileOrFileSet(file);
                    notifyFileDeleted(file);
                }
                refreshTree();
            }
        });
    }

    private void handleCopySelected() {
        // Implementation for copy functionality
        // For now, just show a message
        showInfo("Copy", "Copy functionality will be implemented in future version.");
    }

    private void handlePasteSelected() {
        // Implementation for paste functionality
        // For now, just show a message
        showInfo("Paste", "Paste functionality will be implemented in future version.");
    }

    private void handleCutSelected() {
        // Implementation for cut functionality
        // For now, just show a message
        showInfo("Cut", "Cut functionality will be implemented in future version.");
    }

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

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(e -> {
            TreeItem<File> selected = fileTreeView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.getValue().isFile()) {
                notifyFileOpened(selected.getValue());
            }
        });

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(e -> handleRename());

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> handleDeleteSelected());

        MenuItem copyItem = new MenuItem("Copy Path");
        copyItem.setOnAction(e -> handleCopyPath());

        MenuItem newFileItem = new MenuItem("New File Set");
        newFileItem.setOnAction(e -> handleNewFile());

        MenuItem refreshItem = new MenuItem("Refresh");
        refreshItem.setOnAction(e -> refreshTree());

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

        fileTreeView.setContextMenu(contextMenu);
    }

    private void handleRename() {
        TreeItem<File> selected = fileTreeView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        File file = selected.getValue();
        String itemType = file.isDirectory() ? "folder" : "file";
        
        TextInputDialog dialog = new TextInputDialog(file.getName());
        dialog.setTitle("Rename " + itemType);
        dialog.setHeaderText("Rename " + file.getName());
        dialog.setContentText("New name:");
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToDialog(dialog);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (!newName.trim().isEmpty() && !newName.equals(file.getName())) {
                File newFile = new File(file.getParent(), newName);
                if (file.renameTo(newFile)) {
                    refreshTree();
                    notifyFileRenamed(file, newFile);
                } else {
                    showError("Failed to rename " + itemType, "Could not rename " + file.getName() + " to " + newName);
                }
            }
        });
    }

    private void handleDelete() {
        TreeItem<File> selected = fileTreeView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        File file = selected.getValue();
        String itemType = file.isDirectory() ? "folder" : "file";
        String deleteMessage;
        
        if (file.isFile() && isPartOfFileSet(file)) {
            String baseName = extractBaseName(file);
            deleteMessage = "Delete the entire file set '" + baseName + "' and its folder?\n" +
                          "This will delete all related files (-org.txt, -rev.txt, -byte.txt) and the containing folder.";
        } else if (file.isDirectory()) {
            deleteMessage = "Delete folder '" + file.getName() + "' and all its contents?";
        } else {
            deleteMessage = "Delete file '" + file.getName() + "'?";
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete " + itemType);
        confirmDialog.setHeaderText(deleteMessage);
        confirmDialog.setContentText("This action cannot be undone.");
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(confirmDialog);

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = deleteFileOrFileSet(file);
            
            if (deleted) {
                refreshTree();
                notifyFileDeleted(file);
            } else {
                showError("Failed to delete " + itemType, "Could not delete " + file.getName());
            }
        }
    }

    /**
     * Delete a file or file set. If the file is part of a file set, delete the entire set and folder.
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
     * Check if a file is part of a file set (ends with -org.txt, -rev.txt, or -byte.txt)
     */
    private boolean isPartOfFileSet(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith("-org.txt") || 
               fileName.endsWith("-rev.txt") || 
               fileName.endsWith("-byte.txt");
    }

    /**
     * Extract the base name from a file that's part of a file set
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
     * Delete the entire file set and its containing folder
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

    private void handleCopyPath() {
        TreeItem<File> selected = fileTreeView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(selected.getValue().getAbsolutePath());
            clipboard.setContent(content);
        }
    }

    private void handleNewFile() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New File Set");
        dialog.setHeaderText("Create new file set");
        dialog.setContentText("Base name (without extension):");
        
        // Add placeholder
        dialog.getEditor().setPromptText("e.g., MyProject, Document1, etc.");
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToDialog(dialog);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(baseName -> {
            if (!baseName.trim().isEmpty()) {
                notifyNewFileRequested(baseName.trim());
            }
        });
    }

    public void refreshTree() {
        TreeItem<File> root = fileTreeView.getRoot();
        if (root != null) {
            root.getChildren().setAll(getChildren(root.getValue()));
            expandDirectories(root);
        }
    }

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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(alert);
        
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Apply dark theme to dialog
        com.codemavriks.aptech.MainApp.applyDarkThemeToAlert(alert);
        
        alert.showAndWait();
    }

    // Event notification methods
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

    private void notifyNewFileRequested(String baseName) {
        listeners.forEach(listener -> listener.onNewFileRequested(baseName));
    }

    // Public methods
    public void addFileOperationListener(FileOperationListener listener) {
        listeners.add(listener);
    }

    public void removeFileOperationListener(FileOperationListener listener) {
        listeners.remove(listener);
    }

    public ObjectProperty<File> selectedFileProperty() {
        return selectedFile;
    }

    public void selectFile(File file) {
        selectFileInTree(fileTreeView.getRoot(), file);
    }

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

    // Interface for file operation callbacks
    public interface FileOperationListener {
        default void onFileSelected(File file) {}
        default void onFileOpened(File file) {}
        default void onFileDeleted(File file) {}
        default void onFileRenamed(File oldFile, File newFile) {}
        default void onNewFileRequested(String baseName) {}
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }
} 