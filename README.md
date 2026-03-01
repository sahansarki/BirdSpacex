# SpaceX Launches - Kotlin Multiplatform Case Study
Kotlin Multiplatform app that lists SpaceX launches and shows launch details (including rocket details, links, and video playback) with shared business logic and platform-specific integrations where needed.

## Architecture Overview
```text
Compose UI (screens/components)
    -> ViewModels (StateFlow + SharedFlow)
        -> UseCases
            -> SpaceRepository (interface)
                -> SpaceRepositoryImpl
                    -> SpaceXApi (network/fixture)
                    -> LaunchCacheDataSource + RocketCacheDataSource (Room cache)
```

`presentation` handles UI actions/events/state rendering, `domain` contains use cases + result/error contracts, and `data` owns API/cache/mapping/repository concerns. DI wiring is centralized in `di/*` using Koin. This keeps platform-independent logic in `commonMain` while platform details stay in `androidMain` / `iosMain`.

## Module & Source Set Structure
- `:composeApp` (Gradle module): KMP shared code + Android app entry point
- `iosApp` (Xcode project): iOS host app that embeds shared Compose UI

Source sets in `composeApp/src`:
- `commonMain`: shared business logic, models, mappers, repository, screens/viewmodels, expect declarations
- `commonTest`: shared unit tests for mappers/repository/viewmodels
- `androidMain`: Android actual implementations (HTTP engine, DB factory, URL opener, video)
- `iosMain`: iOS actual implementations (HTTP engine, DB factory, URL opener, video)

## Data Flow: Network -> Domain -> UI (Mappers)
- DTO/API models:
  - `data/remote/model/LaunchResponseModel.kt`
  - `data/remote/model/RocketResponseModel.kt`
- Mapper layer:
  - `data/mapper/LaunchListItemMapper.kt` maps `LaunchResponseModel -> LaunchListItemUiModel`
  - `data/mapper/RocketDetailMapper.kt` maps `RocketResponseModel -> RocketUiModel`
  - `data/mapper/LaunchDetailMapper.kt` maps `LaunchDetailSource -> LaunchDetailUiModel`
- Repository composition:
  - `data/repository/SpaceRepositoryImpl.kt` fetches API/cache models and converts them via mappers before returning use-case-ready models.


## State Management 
- State container:
  - `presentation/mvi/SpaceUiState.kt` (`isLoading`, `isRefreshing`, `error`, `launches`, `selectedLaunchDetail`)
- Action/event contracts:
  - `SpaceAction` and `SpaceEvent` sealed interfaces in `presentation/mvi`
- Result/error model:
  - `DomainResult` + `NetworkError` sealed interfaces in `domain/util`
- Emission and observation:
  - ViewModels update `StateFlow` and emit `SharedFlow` events in `presentation/viewmodel/SpaceViewModel.kt`
  - Screens observe with `collectAsStateWithLifecycle`


## Offline Caching
- What is cached:
  - Launch list rows in `launch_cache` (`LaunchCacheEntity`)
  - Rocket details in `rocket_cache` (`RocketCacheEntity`)
- Where:
  - `LaunchCacheDataSource` + `RocketCacheDataSource` backed by Room DAOs
- Cache policy:
  - TTL-based freshness checks with timestamps (`isFresh`, `isRocketFresh`)
  - TTL constant: `CACHE_TTL_MILLIS = 6 * 60 * 60 * 1000L` in `SpaceRepositoryImpl`
  - `getLaunches`: use fresh cache when available; otherwise fetch network; fallback to cache on network error
  - `getLaunchDetail`: fetch launch; fallback to cached launch; resolve rocket from cache/network

## Platform-specific Concerns
- HTTP engine:
  - expect: `platform/PlatformHttpEngine.kt`
  - actual: `androidMain/.../PlatformHttpEngine.android.kt` (OkHttp), `iosMain/.../PlatformHttpEngine.ios.kt` (Darwin)
- Database creation:
  - expect: `data/local/db/DatabaseFactory.kt`
  - actual: `DatabaseFactory.android.kt` (Room.databaseBuilder), `DatabaseFactory.ios.kt` (Room + BundledSQLiteDriver)
- App config:
  - expect: `network/AppConfig.kt`
  - actual: `AppConfig.android.kt` (BuildConfig), `AppConfig.ios.kt` (Info.plist `BASE_URL`)
- Media player:
  - expect: `platform/video/VideoPlayer.kt`
  - actual: `VideoPlayer.android.kt` (ExoPlayer/WebView), `VideoPlayer.ios.kt` (AVPlayer/WKWebView)

## Testing
Run all configured tests:
```bash
./gradlew :composeApp:allTests
```