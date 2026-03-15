# AGENTS.md — Architecture Reference Guide

This document describes the architecture of this Android project. Its purpose is to serve as a
replication guide for agents and developers working on new projects that follow the same patterns.

---

## Project Overview

A multi-module Android app built with Jetpack Compose, following Clean Architecture with strict
module-level layer separation, MVVM, Unidirectional Data Flow (UDF), and Room as the Single
Source of Truth (SSoT).

---

## Module Structure

```
root/
├── app/         → Presentation layer + all Android/framework implementations
├── data/        → Data layer: repositories + data source interfaces (pure JVM)
├── domain/      → Domain entities + error types (pure Kotlin, zero dependencies)
├── usecases/    → One use case per class (pure JVM)
└── testShared/  → Shared test fixtures and sample data (used by all modules)
```

Dependency direction is strictly one-way — no cycles:

```
app → usecases → data → domain
app →   data   →        domain
app →            domain
```

The `:domain` module must compile to a plain JAR. It must have zero Android, Retrofit, Room, or
Hilt dependencies. The `:data` module is also a pure JVM library — no Android SDK imports.

Build configuration lives in `buildSrc/`:
- `AppConfig.kt` — SDK versions, app ID, version codes
- `Libs.kt` — all library coordinates in one place
- `Modules.kt` — module path constants (`:app`, `:data`, `:domain`, `:usecases`, `:testShared`)
- `ClassPath.kt`, `Plugins.kt`, `Constants.kt`

---

## Tech Stack

| Category         | Library / Tool                                             |
|------------------|------------------------------------------------------------|
| Language         | Kotlin, JVM target 17                                      |
| UI               | Jetpack Compose + Material3 (Compose BOM)                  |
| DI               | Hilt (KSP)                                                 |
| Networking       | Retrofit2 + OkHttp3 + Moshi (KotlinJsonAdapterFactory)     |
| Local DB         | Room (KSP, `fallbackToDestructiveMigration`)               |
| Image loading    | Coil 3 (`coil-compose`, `coil-gif`, `coil-network-okhttp`) |
| Async            | Kotlin Coroutines + Flow + StateFlow                       |
| Error handling   | Arrow-kt `Either<Error, T>`                                |
| Navigation       | Compose Navigation (type-safe sealed class pattern)        |
| Video            | ExoPlayer (Media3)                                         |
| AI               | Google Generative AI SDK (Gemini), OpenAI via Retrofit     |
| Animations       | Lottie                                                     |
| HTML parsing     | JSoup                                                      |
| Testing          | JUnit4, Mockito-Kotlin, Turbine, MockWebServer, Espresso   |

---

## Clean Architecture Layers

### 1. Domain Layer (`:domain`)

Pure Kotlin. No framework dependencies. Compiles to a plain JAR.

Contains:
- **Entities**: plain `data class` or `data object` representing the core business objects.
- **Error type**: a `sealed interface Error` with exactly three cases:
  ```kotlin
  sealed interface Error {
      class Server(val code: Int) : Error
      object Connectivity : Error
      class Unknown(val message: String) : Error
  }
  ```
- **Enums and state holders**: `ServerType`, `LoadingState`, `NotificationType`, etc.
- **API URL data classes**: `ApiUrl`, `ApiVideoUrl` — hold base URL strings as plain Kotlin types so
  the domain layer can express configuration without knowing about Retrofit.

Rules for this layer:
- No `import android.*`
- No `import retrofit2.*`
- No `import androidx.*`
- No Hilt or DI annotations
- Entities are immutable `data class` values

---

### 2. Data Layer (`:data`)

Pure JVM library. No Android SDK.

Contains:
- **Data source interfaces** (`datasource/` package): one interface per data source concern.
  Concrete implementations live in `:app`.
  ```kotlin
  // Example
  interface WaifusImLocalDataSource {
      val waifusIm: Flow<List<WaifuImItem>>
      suspend fun isImEmpty(): Boolean
      suspend fun saveIm(waifus: List<WaifuImItem>)
      suspend fun deleteIm()
  }
  ```
- **Repository concrete classes**: depend only on data source interfaces — never on Room or Retrofit
  directly. The repository is the only place that coordinates local and remote data.
- **Mapper extension functions**: live at file level inside the data source implementation files.
  Never in the domain entities themselves.
- **Error conversion helpers**:
  ```kotlin
  fun Throwable.toError(): Error = when (this) {
      is IOException  -> Error.Connectivity
      is HttpException -> Error.Server(code())
      else            -> Error.Unknown(message ?: "")
  }

  inline fun <T> tryCall(action: () -> T): Either<Error, T> = try {
      Either.Right(action())
  } catch (e: Exception) {
      Either.Left(e.toError())
  }

  inline fun trySave(action: () -> Unit): Error? = try {
      action(); null
  } catch (e: Exception) {
      e.toError()
  }
  ```

Repository pattern (cache-first, SSoT):
```kotlin
// Always expose a Flow from the local DB — never from the network directly
val savedWaifus: Flow<List<WaifuItem>> = localDataSource.waifus

// Fetch from network only when needed; always persist to DB before returning
suspend fun requestWaifus(...): Error? {
    if (localDataSource.isEmpty()) {
        val result = remoteDataSource.fetchWaifus(...)
        result.fold(ifLeft = { return it }) { localDataSource.save(it) }
    }
    return null  // the Flow above emits automatically
}
```

Rules for this layer:
- Repositories must not expose `Either<Error, T>` for streaming data — use `Flow<List<T>>` from DB.
- `Either<Error, T>` is only used for one-shot write/fetch operations.
- Network data must always be written to Room before being consumed by the UI.

---

### 3. Use Case Layer (`:usecases`)

Pure JVM. One class per use case. All classes use `operator fun invoke()`.

```kotlin
class GetWaifusUseCase @Inject constructor(private val repo: WaifusRepository) {
    operator fun invoke(): Flow<List<WaifuItem>> = repo.savedWaifus
}

class RequestWaifusUseCase @Inject constructor(private val repo: WaifusRepository) {
    suspend operator fun invoke(params: ...): Error? = repo.requestWaifus(params)
}
```

Rules for this layer:
- One responsibility per class — no use case does both read and write.
- No Android dependencies.
- No Hilt-specific annotations needed here (use `@Inject constructor` for pure DI).
- Use cases never talk to data sources directly — only to repositories.

---

### 4. Presentation Layer (`:app`)

Android module. Contains:
- All `@HiltViewModel` ViewModels
- All Composable screens and navigation
- Hilt DI modules (`AppModule`, `AppDataModule`)
- Room database + DAOs (concrete `LocalDataSource` implementations)
- Retrofit services (concrete `RemoteDataSource` implementations)
- `@HiltAndroidApp Application` subclass
- `@AndroidEntryPoint Activity`

---

## MVVM Pattern

Every screen follows this exact structure:

### ViewModel
```kotlin
@HiltViewModel
class FeatureViewModel @Inject constructor(
    getItemsUseCase: GetItemsUseCase,
    private val requestItemsUseCase: RequestItemsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getItemsUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { items -> _state.update { UiState(items = items) } }
        }
    }

    fun onRequestItems(params: ...) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val error = requestItemsUseCase(params)
            _state.update { it.copy(isLoading = false, error = error) }
        }
    }

    data class UiState(
        val isLoading: Boolean? = null,
        val items: List<Item>? = null,
        val error: Error? = null,
    )
}
```

Rules:
- `UiState` is always a **nested `data class`** inside the ViewModel.
- `_state` is always `private` `MutableStateFlow`; public surface is `StateFlow` via `.asStateFlow()`.
- State is updated exclusively through `_state.update { it.copy(...) }`.
- `init {}` collects long-running Flows. Named `fun` methods handle one-shot events.

### Composable Screen
```kotlin
@Composable
fun FeatureScreen(viewModel: FeatureViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Render state
    state.items?.let { ItemList(items = it) }
    state.isLoading?.let { if (it) LoadingIndicator() }
    state.error?.let { ErrorMessage(error = it) }

    // User events flow upward to ViewModel
    Button(onClick = { viewModel.onRequestItems(params) }) { ... }
}
```

Rules:
- Never use `collectAsState()` — always `collectAsStateWithLifecycle()`.
- Composables never write to any state directly. They only call ViewModel methods.
- The UI is a pure function of `UiState`.

---

## Unidirectional Data Flow (UDF)

```
User Event
    ↓
Composable calls ViewModel.method()
    ↓
ViewModel calls UseCase.invoke()
    ↓
UseCase calls Repository method
    ↓
Repository reads from / writes to Local DataSource (Room)
    ↓       (on write: also calls Remote DataSource first)
Room emits Flow<List<T>> via LiveData/StateFlow
    ↓
Repository exposes Flow<List<DomainItem>>
    ↓
UseCase returns the Flow
    ↓
ViewModel collects → _state.update { ... }
    ↓
UI collects state via collectAsStateWithLifecycle()
    ↓
Recomposition renders new UI
```

This cycle is strictly one-directional. No step can write to a layer above itself.

---

## Single Source of Truth (SSoT)

**Room is the SSoT for all persistent data.**

- The ViewModel never holds a local copy of the list — it only collects from the repository Flow.
- The repository Flow is derived from Room, not from the network response.
- Network responses are persisted to Room before the ViewModel is aware of them.
- There is no code path where the UI can receive data from both the network and Room simultaneously.

---

## SOLID Principles

| Principle | Implementation |
|---|---|
| **S** Single Responsibility | Each use case does exactly one thing. Each repository handles one bounded context. Each data source interface covers one concern (local reads, remote fetches). |
| **O** Open/Closed | Data source interfaces allow swapping implementations (e.g., Room → in-memory) without modifying repositories. |
| **L** Liskov Substitution | All data source implementations are substitutable for their interface. Fakes in tests prove this. |
| **I** Interface Segregation | Separate interfaces: `LocalDataSource` (reads/writes), `RemoteDataSource` (network). No fat interfaces combining both. |
| **D** Dependency Inversion | `:data` repositories depend on interfaces. Concrete Room/Retrofit classes live in `:app` and are wired by Hilt `@Binds`. |

---

## Dependency Injection (Hilt)

Entry points:
```kotlin
@HiltAndroidApp class App : Application()
@AndroidEntryPoint class MainActivity : AppCompatActivity()
```

Module split:
- `@Module @InstallIn(SingletonComponent::class) object AppModule` — all `@Provides` bindings
  (database, DAOs, Retrofit, OkHttpClient, Moshi, API URL objects).
- `@Module @InstallIn(SingletonComponent::class) abstract class AppDataModule` — all `@Binds`
  bindings mapping interfaces to concrete implementations.

Binding pattern:
```kotlin
// AppDataModule.kt
@Binds abstract fun bindLocalDataSource(impl: RoomDataSource): LocalDataSource
@Binds abstract fun bindRemoteDataSource(impl: ServerDataSource): RemoteDataSource
@Binds abstract fun bindPermissionChecker(impl: AndroidPermissionChecker): PermissionChecker
```

When Hilt cannot manage a ViewModel (e.g., it requires a runtime parameter like a `GenerativeModel`
configuration), use the ViewModel factory pattern instead of `@HiltViewModel`.

---

## Navigation

Type-safe navigation with a `sealed class NavItem` + `enum class NavArg`:

```kotlin
sealed class NavItem(
    internal val baseRoute: String,
    private val navArgs: List<NavArg> = emptyList()
) {
    object Home : NavItem("home_screen")
    object Detail : NavItem("detail_screen", listOf(NavArg.ItemId)) {
        fun createRoute(id: Int) = "$baseRoute/$id"
    }

    val route = listOf(baseRoute).plus(navArgs.map { "{${it.key}}" }).joinToString("/")
    val args = navArgs.map { navArgument(it.key) { type = it.navType } }
}

enum class NavArg(val key: String, val navType: NavType<*>) {
    ItemId("itemId", NavType.IntType),
    // ...
}
```

Argument extraction with type safety:
```kotlin
private inline fun <reified T> NavBackStackEntry.findArg(arg: NavArg): T {
    val value = arguments?.get(arg.key)
    requireNotNull(value) { "Argument ${arg.key} not found" }
    return value as T
}
```

URL path segments that contain special characters must be encoded before passing:
```kotlin
navController.navigate(NavItem.Detail.createRoute(url.urlEncoder()))
```

Splash screens pop themselves off the back stack with `inclusive = true` so the back button does
not return to them.

---

## UI State Patterns

### Standard screens — flat `data class UiState`
Use a flat `data class` with nullable fields when the screen has a single "loaded" state:
```kotlin
data class UiState(
    val isLoading: Boolean? = null,
    val items: List<Item>? = null,
    val error: Error? = null,
)
```

### Discrete state machine screens — `sealed interface`
Use a sealed interface when the screen progresses through distinct, mutually exclusive states
(e.g., AI/streaming screens):
```kotlin
sealed interface FeatureUiState {
    data object Initial : FeatureUiState
    data object Loading : FeatureUiState
    data class Success(val result: String) : FeatureUiState
    data class Error(val message: String) : FeatureUiState
}
```

### Live chat — `mutableStateListOf`
For append-only, observable lists (e.g., chat history), prefer Compose's reactive list to avoid
wrapping a `List` in `StateFlow`:
```kotlin
val chatHistory = chatHistory.toMutableStateList()
```

---

## Error Handling

Errors never cross layer boundaries as raw exceptions. They are mapped to the domain `Error` sealed
interface at the data source level and propagated upward as typed values.

```
IOException / HttpException
    ↓ (mapped in tryCall/trySave)
domain.Error (Connectivity | Server | Unknown)
    ↓
Error? returned from Repository → UseCase → ViewModel
    ↓
UiState.error field
    ↓
UI maps Error → user-facing message string via exhaustive `when`
```

The UI never catches exceptions. The ViewModel stores `Error?` in `UiState`. Composables display
a user message based on the error subtype.

---

## Room Database

One `@Database` class in `:app` with all entities and DAOs. Singleton pattern using `@Volatile`
and `synchronized`.

Key conventions:
- Use `fallbackToDestructiveMigration()` during development.
- Complex fields (nested objects, lists) are serialized as JSON strings using Moshi type adapters
  defined in the Room data source class — never in the entity class itself.
- Every entity has a corresponding `DbItem` suffix (e.g., `WaifuImDbItem`) to distinguish from
  domain entities.
- Mappers (`DbItem.toDomainModel()`, `DomainItem.toDbItem()`) are private file-level extension
  functions inside the data source class.

---

## Networking (Retrofit)

- One `OkHttpClient` singleton shared across all Retrofit instances.
- Multiple Retrofit instances are created if the app talks to different base URLs. They share the
  same `OkHttpClient` and `Moshi` instance.
- All service instances are bundled in a single wrapper data class (e.g., `RemoteConnect`) that
  holds references to each Retrofit service interface.
- Response → domain mapping happens in the remote data source class using private extension
  functions at file level.

---

## Testing Strategy

The project follows the full testing pyramid.

### Unit Tests (`:usecases/src/test`, `:data/src/test`)
- One test class per use case / repository.
- Use Mockito-Kotlin (`mock {}`, `whenever {}`, `verify {}`).
- Use Turbine for `Flow` assertions (`.test {}`).
- Use `runTest` from `kotlinx.coroutines.test`.

```kotlin
@Test
fun `invoke returns flow from repository`() = runTest {
    val expected = listOf(sampleItem)
    whenever(repository.savedItems).thenReturn(flowOf(expected))
    getItemsUseCase().test {
        assertEquals(expected, awaitItem())
        awaitComplete()
    }
}
```

### Integration Tests (`:app/src/test`)
- ViewModel tests using real use cases wired with fakes (not mocks).
- Fakes implement data source interfaces and hold in-memory state.
- Fakes live in `app/src/testShared/` (shared between `test/` and `androidTest/` source sets).

### Instrumented Tests (`:app/src/androidTest`)
- Full Hilt component (`@HiltAndroidTest`, custom `HiltTestRunner`).
- `MockWebServer` stubs API responses with JSON fixture files.
- Compose UI Test APIs for assertions.
- Espresso for non-Compose UI.

### CoroutinesTestRule
Provide a JUnit `@Rule` that replaces `Dispatchers.Main` with a `TestCoroutineDispatcher`:
```kotlin
class CoroutinesTestRule : TestWatcher() {
    val testDispatcher = TestCoroutineDispatcher()
    override fun starting(d: Description) { Dispatchers.setMain(testDispatcher) }
    override fun finished(d: Description) { Dispatchers.resetMain() }
}
```

### Shared Fixtures
- `testShared/` module: `Samples.kt` — domain-level sample objects available to all modules.
- `app/src/testShared/`: fakes for all data source interfaces, accessible from both unit and
  instrumented tests.

---

## Build Variants

| Variant    | App ID suffix | Notes                          |
|------------|---------------|--------------------------------|
| `debug`    | `.debug`      | Development                    |
| `release`  | (none)        | Production                     |
| `enhanced` | `.enhanced`   | Alternate icon/name/features   |

---

## Conventions Summary

- One use case class per operation. Name it `VerbNounUseCase`.
- One repository per bounded context.
- One data source interface per concern (local / remote), one implementation per technology.
- `UiState` is always a nested `data class` inside the ViewModel.
- `_state` is always private; `state` is always a public `StateFlow`.
- State transitions use `.update { it.copy(...) }`.
- Domain entities are immutable. Mutation happens through new object creation.
- Errors propagate as typed `domain.Error` values, never as raw exceptions past the data layer.
- Room is the SSoT. Network data is always persisted before the UI observes it.
- Navigation arguments are type-safe via `NavItem` sealed class and `NavArg` enum.
- All `@Provides` go in an `object` module; all `@Binds` go in an `abstract class` module.
