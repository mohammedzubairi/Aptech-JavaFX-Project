# File Mana - Modern Text Editor

A modern, responsive text editor built with JavaFX featuring VSCode-like file navigation, auto-save functionality, and automatic file variant generation.

## 🚀 Features

### Core Functionality
- **Full-Featured Text Editor**: Complete text editing with undo/redo, copy/paste, find functionality
- **Automatic File Generation**: Creates three file variants automatically (original, reversed, byte codes)
- **Smart Naming Convention**: Enter "Ahmed" → generates `Ahmed-org.txt`, `Ahmed-rev.txt`, `Ahmed-byte.txt`
- **Auto-Save**: Automatic saving every 30 seconds with visual feedback
- **File System Navigator**: VSCode-like file explorer with context menus

### User Interface
- **Responsive Design**: 70% editor, 30% sidebar layout that adapts to screen size
- **Modern Dark Theme**: Professional VSCode-inspired styling with dark background
- **Application Icon**: Custom File Mana logo for window and taskbar
- **Modular Components**: Clean, maintainable component architecture
- **Intuitive Controls**: Keyboard shortcuts and context menus

### File Operations
- **Right-Click Context Menu**: Open, rename, delete, copy path, refresh
- **Full Path Display**: Shows complete file paths in navigator
- **Unsaved Changes Detection**: Prompts before losing work
- **File Type Filtering**: Shows only .txt files and directories

## 🏗️ Architecture

### Component Structure
```
src/main/java/com/codemavriks/aptech/
├── components/
│   ├── TextEditor.java          # Full-featured text editor component
│   ├── FileNavigator.java       # VSCode-like file tree navigator
│   └── SidePanel.java          # Sidebar with buttons and file navigator
├── services/
│   └── FileService.java        # File operations and auto-save service
├── EditorController.java       # Main application controller
└── MainApp.java               # Application entry point with File Mana branding
```

### Key Components

#### TextEditor Component
- Full text editing capabilities
- Keyboard shortcuts (Ctrl+S, Ctrl+C, Ctrl+V, etc.)
- Find functionality (Ctrl+F)
- Undo/Redo support
- Change tracking for auto-save

#### FileNavigator Component
- Tree view of file system
- Context menu operations
- File type filtering
- Automatic refresh
- Event-driven architecture

#### SidePanel Component
- New File and Save buttons
- Status display
- File navigator integration
- Responsive button layout

#### FileService
- Async file operations
- Auto-save functionality
- File naming convention management
- Error handling and recovery

## 🎨 Styling

### Modern Theme Features
- **Dark Theme**: Professional dark color scheme
- **Responsive Design**: Adapts to different screen sizes
- **Smooth Animations**: Hover effects and transitions
- **Consistent Typography**: Modern font stack
- **Accessible Colors**: High contrast for readability

### CSS Architecture
```
src/main/resources/com/codemavriks/aptech/styles/
└── modern-theme.css            # Complete modern theme
```

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- JavaFX 22
- Maven 3.6+

### Running the Application
```bash
# Clone the repository
git clone <repository-url>

# Navigate to project directory
cd Aptech-JavaFX-Project

# Run with Maven
mvn javafx:run

# Or compile and run
mvn clean compile
mvn javafx:run
```

### Building
```bash
# Create executable JAR
mvn clean package

# The JAR will be in target/ directory
```

## 📖 Usage Guide

### Creating a New File
1. Click "New File" button in sidebar
2. Enter base name (e.g., "Ahmed") - no extension needed
3. System automatically creates three files:
   - `Ahmed-org.txt` (original content)
   - `Ahmed-rev.txt` (reversed content)
   - `Ahmed-byte.txt` (byte representation)

### Editing Files
1. Click on any file in the navigator to open it
2. Edit content in the main text area
3. Use keyboard shortcuts for common operations:
   - `Ctrl+S`: Save
   - `Ctrl+N`: New file
   - `Ctrl+F`: Find
   - `Ctrl+Z`: Undo
   - `Ctrl+Y`: Redo

### File Operations
- **Right-click** on files for context menu
- **Double-click** to open files
- **Single-click** to select files
- Files auto-save every 30 seconds

### Keyboard Shortcuts
| Shortcut | Action |
|----------|--------|
| `Ctrl+S` | Save current file |
| `Ctrl+N` | Create new file |
| `Ctrl+F` | Find in document |
| `Ctrl+Z` | Undo |
| `Ctrl+Y` | Redo |
| `Ctrl+C` | Copy |
| `Ctrl+V` | Paste |
| `Ctrl+X` | Cut |
| `Ctrl+A` | Select all |

## 🔧 Configuration

### Auto-Save Settings
Auto-save is enabled by default with a 30-second interval. To modify:

```java
// In EditorController constructor
editorController.setAutoSaveEnabled(false); // Disable auto-save
```

### File Directory
Files are saved to `Created files/` directory by default. To change:

```java
// In SidePanel constructor
new SidePanel("your-custom-directory/");
```

### Theme Customization
Modify `src/main/resources/com/codemavriks/aptech/styles/modern-theme.css` to customize:
- Colors
- Fonts
- Spacing
- Animations

## 🧪 Testing

### Manual Testing
1. Create new files with different names
2. Test file operations (rename, delete, copy)
3. Verify auto-save functionality
4. Test keyboard shortcuts
5. Check responsive design at different window sizes

### File Naming Convention Testing
- Enter "Test" → Should create `Test-org.txt`, `Test-rev.txt`, `Test-byte.txt`
- Enter "Test.txt" → Should create same files (extension removed)
- Verify content is properly distributed across files

## 🤝 Contributing

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep components modular and loosely coupled

### Adding New Features
1. Create new components in `components/` package
2. Add services in `services/` package
3. Update CSS in `styles/` directory
4. Follow the event-driven architecture pattern

## 📝 File Naming Convention

The application uses a smart naming convention:

### Input → Output
- `Ahmed` → `Ahmed-org.txt`, `Ahmed-rev.txt`, `Ahmed-byte.txt`
- `Document` → `Document-org.txt`, `Document-rev.txt`, `Document-byte.txt`
- `Test.txt` → `Test-org.txt`, `Test-rev.txt`, `Test-byte.txt` (extension removed)

### File Types
- **-org.txt**: Original content as entered
- **-rev.txt**: Character-reversed content
- **-byte.txt**: Byte representation of content

## 🐛 Troubleshooting

### Common Issues

**Application won't start**
- Verify Java 21+ is installed
- Check JavaFX dependencies in pom.xml
- Ensure Maven is properly configured

**Files not saving**
- Check write permissions in project directory
- Verify "Created files/" directory exists
- Check console for error messages

**Auto-save not working**
- Ensure file is properly opened/created
- Check status messages in sidebar
- Verify content has actually changed

**UI not responsive**
- Check CSS file is loading properly
- Verify modern-theme.css exists
- Try resizing window to test responsiveness

## 📄 License

This project is part of the Aptech JavaFX coursework.

## 🙏 Acknowledgments

- JavaFX community for excellent documentation
- VSCode for UI/UX inspiration
- Modern web design principles for styling guidance 