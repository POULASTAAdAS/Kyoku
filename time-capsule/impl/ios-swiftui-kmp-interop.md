# iOS SwiftUI ↔ KMP ViewModel interop (SKIE + typed Swift extensions)

Implemented 2026-11-15. Debug build verified on iPhone 17 simulator (iOS 27).

## Problem

SwiftUI screens use the shared Kotlin ViewModels, but Kotlin/Native exports through an Objective-C header:

- Sealed actions/events arrive as flat, mangled classes (`SignInUiActionOnEmailChange`, `SignInUiActionSignIn.shared`), so Xcode autocomplete is poor.
- Event handling used `switch event { case is …: … default: break }`. If a cast was misspelled or a new Kotlin event was added, `default` swallowed it silently at runtime.

## Research

- **Industry (2026):** SKIE (Touchlab) is still the standard. Swift Export is experimental and loses exhaustive sealed switches. SKIE turns sealed classes/enums into Swift enums via `onEnum(of:)`. KaMPKit dropped its hand-written adapters after adopting SKIE. kmp-showcase applies SKIE only to the module that builds the framework. Google's KMP ViewModel docs and most guides wrap the shared VM in a Swift `ObservableObject`.
- **hoichoi (`work/hoichoi`, tvOS and iOS share `iosApp/Hoichoi/ViewModels/*`):** one hand-written Swift `ObservableObject` per screen (e.g. `SearchVM` wraps the Kotlin `SearchViewModel` and exposes `@Published` Swift properties). Views never touch Kotlin names. No SKIE, so events are still loose casts.
- **Kyoku before this change:** one generic `ObservableViewModel<State, Action, Event>` (`iosApp/iosApp/core/viewmodel/ObservableViewModel.swift`). Views built the Kotlin action classes and cast events themselves.

## Implementation

1. **SKIE 0.10.15** (supports Kotlin 2.3.20):
   - `gradle/libs.versions.toml`: `skie` version and plugin alias.
   - Root `build.gradle.kts`: `alias(libs.plugins.skie) apply false`.
   - `composeApp/build.gradle.kts`: plugin applied only here, since this module builds the `ComposeApp` framework.
   - `coroutinesInterop` is set to `false` because flows already reach Swift through `FlowWrapper` / `ObservableViewModel`.
2. **Typed per-screen Swift extension:** `iosApp/iosApp/ui/auth/SignInViewModel.swift`:
   - `typealias SignInViewModel = ObservableViewModel<…>`, plus an extension constrained to those types with a `convenience init()`.
   - Typed methods: `emailChanged`, `passwordChanged`, `signIn()`, `googleAuthFinished(_:)`, …
   - Kotlin action names appear only in this file.
3. **`SignInView.swift`:**
   - Calls the typed methods.
   - Event handling is `switch onEnum(of: event)` with no `default`, so a new Kotlin event breaks the Swift build.
   - The `GoogleAuthResult` switch is also exhaustive (`.success`, `.canceled`, `.error`).

Xcode uses synchronized folders, so new Swift files need no `project.pbxproj` edits.

## Pattern for new or other screens

- Create `<Screen>ViewModel.swift` next to the view, containing:
  - a typealias,
  - a constrained `extension ObservableViewModel` with `convenience init()`,
  - one method per action.
- In the view, handle events with `switch onEnum(of: event)` and never add `default`.
- Still to do: `SignUpView` and `ForgotPasswordView`. They create their viewmodel but don't send actions yet. `RootView` has no events.

## Known risks

- **SKIE issue #207:** on Kotlin ≥ 2.3.20, turning on `defaultArgumentsInExternalLibraries` globally conflicts with the Kotlin/Native compiler cache. It's off. If you ever need it, enable it only for specific classes or functions.
- **Extra link time:** SKIE only runs when Gradle relinks the `ComposeApp` framework (Kotlin change or clean build). Swift-only changes aren't affected. Not measured.

## Sources

- https://touchlab.co/the-future-of-kmps-ios-interop
- https://medium.com/codex/the-state-of-kotlin-multiplatform-in-2026-c87a2d71421b
- https://github.com/igorwojda/kmp-showcase
- https://developer.android.com/kotlin/multiplatform/viewmodel
- https://github.com/touchlab/SKIE/releases
- https://github.com/touchlab/SKIE/issues/174
