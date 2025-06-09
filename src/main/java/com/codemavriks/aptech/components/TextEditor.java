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

public class TextEditor extends VBox {
    private final TextArea textArea;
    private final MenuBar menuBar;
    private final BooleanProperty hasUnsavedChanges = new SimpleBooleanProperty(false);
    private final StringProperty currentFilePath = new SimpleStringProperty("");
    private String originalContent = "";

    public TextEditor() {
        this.textArea = new TextArea();
        this.menuBar = createMenuBar();
        
        setupTextArea();
        setupKeyboardShortcuts();
        
        getChildren().addAll(menuBar, textArea);
        getStyleClass().add("text-editor");
    }

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

    // Public methods for external control
    public void setContent(String content) {
        textArea.setText(content);
        originalContent = content;
        hasUnsavedChanges.set(false);
    }

    public String getContent() {
        return textArea.getText();
    }

    public void markAsSaved() {
        originalContent = textArea.getText();
        hasUnsavedChanges.set(false);
    }

    public BooleanProperty hasUnsavedChangesProperty() {
        return hasUnsavedChanges;
    }

    public StringProperty currentFilePathProperty() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String path) {
        currentFilePath.set(path);
    }

    public void clear() {
        textArea.clear();
        originalContent = "";
        currentFilePath.set("");
        hasUnsavedChanges.set(false);
    }

    public void requestFocus() {
        textArea.requestFocus();
    }

    // Custom events for communication with parent
    public static class SaveRequestEvent extends javafx.event.Event {
        public static final javafx.event.EventType<SaveRequestEvent> SAVE_REQUEST = 
            new javafx.event.EventType<>("SAVE_REQUEST");

        public SaveRequestEvent() {
            super(SAVE_REQUEST);
        }
    }

    public static class NewFileRequestEvent extends javafx.event.Event {
        public static final javafx.event.EventType<NewFileRequestEvent> NEW_FILE_REQUEST = 
            new javafx.event.EventType<>("NEW_FILE_REQUEST");

        public NewFileRequestEvent() {
            super(NEW_FILE_REQUEST);
        }
    }
} 