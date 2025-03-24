# Tempo Timer Developer Guidelines

## Project Overview
Tempo Timer is a desktop client for Tempo Timesheets built with Kotlin Multiplatform and Compose Multiplatform. It allows users to track time on Jira issues directly into Tempo Timesheets.

## Project Structure
- **shared/**: Contains shared code across platforms
  - `commonMain`: Common code for all platforms
    - `arch`: Architecture components and base classes
    - `data`: Data layer (repositories, models, API clients)
    - `design`: Design system components
    - `feature`: Feature modules organized by functionality
    - `injection`: Dependency injection setup
    - `tooling`: Utility classes and tools
  - `commonTest`: Common test code
  - `desktopMain`: Desktop-specific implementations

## Tech Stack
- **Kotlin Multiplatform**: For sharing code across platforms
- **Compose Multiplatform**: UI framework with Material 3
- **Koin**: Dependency injection
- **Ktor/Ktorfit**: Networking and API calls
- **Room**: Database
- **DataStore**: Preferences storage
- **KotlinX Libraries**: Coroutines, Collections, DateTime, Serialization

## Building and Running
- **Build package**: `./gradlew packageDistributionForCurrentOS`
- **Run application**: `./gradlew runDistributable`
- **Run tests**: `./gradlew test`

## Testing
- Tests use Kotlin's built-in testing framework (`kotlin.test`)
- Tests are organized in the same package structure as the main code
- Use descriptive test names with backticks (e.g., `` `parseVersion parses standard semantic versions correctly` ``)
- Test files are named with the "Test" suffix (e.g., `VersionComparatorTest.kt`)

## Architecture
- The project follows a clean architecture approach with separation of concerns
- UI is built using Compose Multiplatform with Material 3
- State management is handled through ViewModels (called Models in this project)
- Features are organized in separate modules with clear boundaries

## Best Practices
1. **Code Organization**:
   - Follow the existing package structure
   - Keep feature code within its respective feature package
   - Place shared utilities in the `tooling` package

2. **UI Development**:
   - Use components from the `design` package
   - Follow Material 3 design guidelines
   - Separate UI from business logic

3. **Testing**:
   - Write tests for all business logic
   - Follow the existing test naming convention
   - Test edge cases thoroughly

4. **Dependency Injection**:
   - Register new dependencies in the appropriate Koin module
   - Use constructor injection

5. **Error Handling**:
   - Use Kotlin's `Result` type for error handling
   - Handle errors gracefully in the UI

## Workflow
1. Make sure you have Jira and Tempo API tokens (see README.md)
2. Set up the development environment
3. Run the application locally to test changes
4. Write tests for new functionality
5. Submit changes following the project's contribution guidelines
