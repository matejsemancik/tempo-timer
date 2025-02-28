# Development Guidelines

## Build Commands
- Build package: `./gradlew packageDistributionForCurrentOS`
- Run app: `./gradlew runDistributable`
- Run desktop app: `./gradlew :desktopApp:run`

## Code Style
- **Naming**: Classes = PascalCase, Functions/Variables = camelCase
- **Architecture**: MVVM pattern with UI, State (Model), and Data layers
- **State Management**: Kotlin Flows (StateFlow for UI state)
- **Visibility Modifiers**:
  - Use the most restrictive visibility modifier possible
  - Prefer `internal` over `public` for implementation classes
  - Use `private` for all properties and functions that don't need wider visibility
  - Use `public` only for APIs that need to be accessed outside the module
- **DI**: Koin for dependency injection
  - Use constructor injection whenever possible instead of property injection
  - Constructor parameters make dependencies explicit and improve testability
- **Imports**: Grouped by purpose, explicit imports preferred over wildcards
- **Error Handling**: Repository pattern for data operations, proper error propagation through Flows
- **Expressive Kotlin Features**:
  - Use `runCatching` instead of try-catch blocks
  - Use `.use` extension for closeables
  - Prefer `kotlin.io` extensions over Java IO streams
  - Use scope functions (`let`, `apply`, `run`, `with`, `also`) appropriately
  - Leverage extension functions for better readability
  - Use property delegates (`by lazy`, `by Delegates`) where appropriate
  - Use Elvis operator (`?:`) for fallback values

## Architecture Guidelines
- Network models (from APIs) should not be propagated to the presentation layer
- Always map network models to domain models in the repository layer
- Service layer handles API communication, Repository layer handles domain translation
- One-shot data fetching should use suspend functions, not Flows
- Only use Flows when there's data to continuously observe
- Classes should receive their dependencies through constructor parameters:
  ```kotlin
  // Preferred: Constructor injection
  class MyRepository(
      private val apiManager: ApiManager,
      private val database: AppDatabase
  )
  
  // Avoid: Property injection
  class MyRepository : KoinComponent {
      private val apiManager: ApiManager by inject()
      private val database: AppDatabase by inject()
  }
  ```
- Limit KoinComponent usage to top-level components (ViewModels, Application class)
- Apply appropriate visibility modifiers to control access:
  ```kotlin
  // Preferred: Implementation classes are internal
  internal class MyRepositoryImpl(
      private val apiManager: ApiManager
  ) : MyRepository {
      // Private implementation details
      private fun processData(data: Data): ProcessedData {
          // ...
      }
  }
  
  // Public interfaces define the contract
  interface MyRepository {
      suspend fun getData(): Data
  }
  ```

## Project Structure
- `feature/` - App features organized by domain (tracker, search, settings)
- `data/` - Repository interfaces, database models, network services
- `design/` - UI components, theme definitions
- `arch/` - Base architectural components
- `injection/` - Dependency injection modules

## Testing
- Unit tests should follow the same structure as the feature they're testing
- Model tests should verify state transitions and event handling