/**
 * File Mana - Modern Text Editor Application Module
 * 
 * This module defines the complete File Mana text editor application, including
 * all necessary dependencies, exports, and opens declarations for JavaFX integration.
 * 
 * Module Overview:
 * File Mana is a modern, feature-rich text editor built with JavaFX that provides
 * advanced text editing capabilities, file management, and content processing features.
 * The application follows the MVC (Model-View-Controller) architecture pattern and
 * implements a component-based UI design for scalability and maintainability.
 * 
 * Key Features:
 * - Modern text editing with syntax highlighting and dark theme support
 * - File set management (original, reversed, and byte-encoded variants)
 * - Advanced file navigation with tree-based browser
 * - Auto-save functionality with background processing
 * - Word replacement with position-based indexing
 * - File comparison and analysis tools
 * - Responsive UI design optimized for various screen sizes
 * - Keyboard shortcuts and menu-based operations
 * 
 * Application Architecture:
 * <pre>
 * ┌─────────────────────────────────────────────────────────┐
 * │                      MainApp                            │
 * │               (Application Entry Point)                 │
 * ├─────────────────────────────────────────────────────────┤
 * │                 EditorController                        │
 * │                (Main MVC Controller)                    │
 * ├─────────────────────┬───────────────────────────────────┤
 * │     SidePanel       │         TextEditor                │
 * │   (File Navigation  │      (Content Editing)           │
 * │    and Controls)    │                                   │
 * └─────────────────────┴───────────────────────────────────┘
 * │                                                         │
 * │                   FileService                           │
 * │                (Business Logic Layer)                   │
 * └─────────────────────────────────────────────────────────┘
 * </pre>
 * 
 * Dependencies:
 * - javafx.controls: Core JavaFX UI controls and components
 * - javafx.fxml: FXML support for declarative UI (future expansion)
 * 
 * Exports:
 * - com.codemavriks.aptech: Main application package
 * 
 * Opens:
 * - com.codemavriks.aptech: Allows JavaFX FXML access for reflection
 * 
 * Target Java Version: 11+
 * JavaFX Version: 21+
 * 
 * @author NAJM ALDEEN MOHAMMED SALEH HAMOD AL-ZORQAH - Student1554163
 * @author Advanced Java Programming with JavaFX
 * @author Aptech Computer Education - Alnasser University
 * @version 1.0
 * @since 2025-05-20
 */
module com.codemavriks.aptech {
    // Required JavaFX modules for UI functionality
    requires javafx.controls;   // Core JavaFX controls and components
    requires javafx.fxml;       // FXML support for declarative UI

    // Open main package to JavaFX for FXML access and reflection
    opens com.codemavriks.aptech to javafx.fxml;
    
    // Export main application package for external access
    exports com.codemavriks.aptech;
}