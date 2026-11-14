# iOS: save / autofill passwords (iCloud Keychain)

**Blocked** on iOS for both `iosUi=compose` and `iosUi=swiftui`. Android (Credential Manager → Google Password Manager)
and desktop (OS keyring via java-keyring) are done through `PasswordCredentialStore` (`auth/domain`). iOS binds a no-op
`IosPasswordCredentialStore` (`auth/ui/src/iosMain/.../PasswordCredentialStore.ios.kt`).

## Why blocked
- iCloud Keychain saving/autofill needs the Associated Domains capability (`webcredentials:<domain>`). That capability
  requires a paid Apple Developer Program membership (99 USD/year); the free personal team cannot use it.
- Unblock only once a paid membership is available.

## Other notes
- `SignInView.swift` is still a blank placeholder and there is no SwiftUI reset-password view.
- No Swift code is needed for the Compose path: the store can call `SecAddSharedWebCredential` (save) and
  `ASAuthorizationPasswordProvider` (get) from Kotlin/Native; the same store also serves SwiftUI via the shared viewmodels.
- Compose fields only get QuickType suggestions from Compose Multiplatform 1.13+ (project is on 1.10.3); optional.

## Decided values
- Domain: `kyoku-gateway.poulastaa.dev` (serves the AASA file).
- App ID: `64XD27MTWT.com.poulastaa.kyoku.Kyoku` (Team `64XD27MTWT`, `TEAM_ID` in `Config.xcconfig` is empty).

## Steps
1. Gateway: serve `GET /.well-known/apple-app-site-association` (no auth, `Content-Type: application/json`, no redirect):
   ```json
   { "webcredentials": { "apps": ["64XD27MTWT.com.poulastaa.kyoku.Kyoku"] } }
   ```
   It must be ordered before `global-fallback-route` (which 404s `/**`).
2. Xcode: add Associated Domains capability → `webcredentials:kyoku-gateway.poulastaa.dev` (creates `iosApp.entitlements`,
   sets `CODE_SIGN_ENTITLEMENTS`).
3. SwiftUI fields:
   - Sign-in email: `.textContentType(.username)` + `.keyboardType(.emailAddress)`; password `SecureField` + `.textContentType(.password)`.
   - Reset password (both fields): `.textContentType(.newPassword)`; include a hidden/read-only `.username` field with the email
     so Keychain updates the right entry.
   - iOS shows "Save password?" itself when the form is dismissed after submit → `hasSystemSavePrompt = true`, `save()` stays a no-op.
4. If Compose iOS is used instead: bump Compose Multiplatform to ≥ 1.13 and set
   `KeyboardOptions(platformImeOptions = PlatformImeOptions { textContentType(...) })` on the auth fields in `iosMain`.
5. Optional: `get()` via `ASAuthorizationPasswordProvider` to show the Keychain picker on sign-in open.
