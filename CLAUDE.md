# Development Guidelines

## Build Commands
- Build package: `./gradlew packageDistributionForCurrentOS`
- Run app: `./gradlew runDistributable`
- Run desktop app: `./gradlew :desktopApp:run`

## Code Style
- **Naming**: Classes = PascalCase, Functions/Variables = camelCase
- **Architecture**: MVVM pattern with UI, State (Model), and Data layers
- **State Management**: Kotlin Flows (StateFlow for UI state)
- **DI**: Koin for dependency injection
- **Imports**: Grouped by purpose, explicit imports preferred over wildcards
- **Error Handling**: Repository pattern for data operations, proper error propagation through Flows

## Project Structure
- `feature/` - App features organized by domain (tracker, search, settings)
- `data/` - Repository interfaces, database models, network services
- `design/` - UI components, theme definitions
- `arch/` - Base architectural components
- `injection/` - Dependency injection modules

## Testing
- Unit tests should follow the same structure as the feature they're testing
- Model tests should verify state transitions and event handling