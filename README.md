# SalatKu: Consolidated Starter Codebase

SIF502 Pengembangan Aplikasi Bergerak, Odd 2026/2027. Package `com.example.shalatku`.

This project is SalatKu as it currently stands: a Jetpack Compose UI on an MVVM architecture with a single `StateFlow`-backed `ViewModel`, state hoisting, Material 3 theming, accessibility support, and an adaptive two-pane layout. It is a stable baseline, ready to be extended with Navigation and further features.

> **Not compiled.** This project was assembled and checked by static review only (no Android SDK or Gradle network access was available). Open it in Android Studio, sync, and run it once before using it in class. Fix any import the IDE complains about with Option+Enter.

## Open and build

1. Install Android Studio and let it install **Android SDK Platform 37** (`compileSdk = 37`) when it asks.
2. **File > Open** and choose this folder (`SalatKu-Project-Template`). Android Studio creates `local.properties` by itself. It is git-ignored, do not share it.
3. Wait for Gradle sync. The wrapper downloads Gradle 9.8.1. Use a JDK 17 or newer (the Android Studio bundled JDK is fine).
4. Run on an emulator or phone (**minSdk 26**, so API 26+). Turn on auto-rotate to check that state survives rotation.

Command line (needs `ANDROID_HOME` or a `local.properties` with `sdk.dir=...`):

```bash
./gradlew assembleDebug testDebugUnitTest
```

Identity: `applicationId` and `namespace` are `com.example.shalatku`. Theme function: `ShalatkuTheme`. ViewModel: `SalatKuViewModel`.

## Versions (frozen)

| Item | Version |
|---|---|
| Android Gradle Plugin | 9.4.1 |
| Kotlin | 2.3.21 |
| Compose BOM | 2026.09.00 |
| activity-compose / core-ktx | 1.13.0 / 1.19.1 |
| Gradle wrapper | 9.8.1 |
| compileSdk / targetSdk / minSdk | 37 / 36 / 26 |
| `lifecycle-runtime-ktx`, `lifecycle-viewmodel-compose`, `lifecycle-runtime-compose` | 2.11.0, all through `version.ref = "lifecycleRuntimeKtx"` |
| JUnit (tests only) | 4.13.2 |

## Features in this codebase

| Feature | Contribution | Files |
|---|---|---|
| **Compose Basics** | `PrayerCard` idea, project baseline (BOM, Material 3, activity-compose) | `app/build.gradle.kts`, `PrayerCard` in `HomeScreen.kt` |
| **Static Home Layout** | `Scaffold`, hero card, prayer rows | `HomeScreen.kt` (`HeroCard`, `PrayerCard`), `MainActivity.kt` (`Scaffold`) |
| **State Hoisting** | Stateless screens, events up / state down | `TasbihScreen.kt`, `TrackerScreen.kt`, `MainActivity.kt` |
| **Lists, Grids & Theming** | `LazyColumn` with keys, `LazyRow` date strip, `LazyVerticalGrid` log, SalatKu colour scheme and typography | `HomeScreen.kt`, `DateStrip.kt`, `LogScreen.kt`, `ui/theme/*` |
| **Accessibility & Adaptive Layout** | Accessibility semantics, 48dp targets, font-scale / dark / expanded previews, two-pane Home at width >= 840dp | `HomeScreen.kt`, `DateStrip.kt`, `TrackerScreen.kt`, `LogScreen.kt`, `MainActivity.kt` (`EXPANDED_WIDTH_DP`) |
| **MVVM & ViewModel** | `SalatKuViewModel`, `viewModel()` in `ShalatkuApp`, round counter at 33 | `SalatKuViewModel.kt`, `MainActivity.kt` |
| **Clear All + Round Size Selector** | Clear all (`clearPrayers()`, `onClear`). `roundSize` (default 33), `setRoundSize(size)`, 33 / 99 / 100 chips, resets count and keeps completed rounds | `SalatKuViewModel.kt`, `TrackerScreen.kt`, `TasbihScreen.kt` |
| **UI State + StateFlow** | `SalatKuUiState`, `MutableStateFlow` + `update { copy() }`, `collectAsStateWithLifecycle()`, `lifecycle-runtime-compose` dependency | `SalatKuUiState.kt`, `SalatKuViewModel.kt`, `MainActivity.kt`, `gradle/libs.versions.toml`, `app/build.gradle.kts` |
| **Derived State** | `doneCount` computed on the state class, shown in the Tracker header (pitfall demos are **not** in the source) | `SalatKuUiState.kt`, `TrackerScreen.kt`, `MainActivity.kt` |
| **Single Event Entry Point** | `SalatKuEvent` sealed interface and one public `onEvent()` | `SalatKuEvent.kt`, `SalatKuViewModel.kt`, `MainActivity.kt` |
| **Project Consolidation** | No new code — this project is the consolidated baseline itself | (this project) |

Files that are **byte-identical** to the original template: `HomeScreen.kt`, `DateStrip.kt`, `LogScreen.kt`, `Prayer.kt`, `ui/theme/Color.kt`, `Theme.kt`, `Type.kt`, all `res/`, `settings.gradle.kts`, root `build.gradle.kts`, `gradle.properties`, the Gradle wrapper.

## Screens and signatures (final)

| Screen | Signature |
|---|---|
| Home | `HomeScreen(prayers, selectedDay, onSelectDay, modifier, expanded = false)` |
| Tracker | `TrackerScreen(prayers, donePrayers, doneCount, onToggle, onClear, modifier)` |
| Tasbih | `TasbihScreen(count, rounds, roundSize, onIncrement, onReset, onSelectRoundSize, modifier)` |
| Log | `LogScreen(cells, modifier)` |

`SalatKuViewModel` public surface: `uiState: StateFlow<SalatKuUiState>` and `onEvent(event: SalatKuEvent)`. The six action functions are `private`.

`ShalatkuApp(viewModel: SalatKuViewModel = viewModel())` is the only composable that knows the ViewModel. The selected bottom-bar tab stays in `ShalatkuApp` with `rememberSaveable` and is **not** in `SalatKuUiState`.

## Assumptions

1. **The Single Event Entry Point feature is included.** It was originally marked an optional stretch goal, but it was requested, so `SalatKuEvent` and `onEvent()` are in. To remove it, make the six functions public again, delete `SalatKuEvent.kt` and call `viewModel::togglePrayer` etc. directly from `ShalatkuApp`.
2. **The Clear-All/Round-Size UI is a custom implementation.** The original spec only said "three buttons or chips" with no code given. Three Material 3 `FilterChip`s are used in a `Row`, driven by `private val roundSizeOptions = listOf(33, 99, 100)`. The selected chip is `selected = roundSize == size` (an expression, no `if`). The "Clear all" button is an `OutlinedButton` in the Tracker header item. Parameter names are `roundSize`, `onSelectRoundSize`, `onClear`.
3. **Real prayer-time data loading is not included.** No `PrayerTimesUiState`, `loadPrayerTimes()` or `PrayerTimesSection` — this baseline stops at the StateFlow/MVVM layer, before any network/data-loading feature.
4. **Unit tests were migrated to the event-based API.** `SalatKuViewModelTest.kt` reads `vm.uiState.value` and sends events (rather than calling the now-private functions directly). Four tests were added for the Clear-All/Round-Size feature and `doneCount`.
5. `kotlinx-coroutines` is not added to the catalog on purpose — it arrives transitively through the lifecycle libraries.
6. The window-size-class library is still **not** used: the breakpoint is a plain `screenWidthDp >= 840` check.

## Known limits

- A ViewModel survives rotation, not process death (expected).
- Dates in the date strip are fixed text (`Mon 20` to `Sun 26`).
- Coloured Log cells, some accessibility audit fixes, navigation, and any real data layer are not part of this baseline.

See `NOTES.md` for the instructor notes on the two StateFlow pitfalls.
