package com.poulastaa.auth.ui.components

import com.poulastaa.auth.domain.PasswordCredentialStore
import com.poulastaa.auth.domain.model.DtoPasswordCredential

/**
 * Password save / autofill is blocked on iOS, for both the Compose and SwiftUI UI.
 *
 * iCloud Keychain saving needs the Associated Domains (`webcredentials:`) capability, which only works with a paid
 * Apple Developer Program membership; the free personal team cannot use it. Kept as a no-op so the shared
 * viewmodels still resolve. See `time-capsule/plans/ios-password-autofill.md` to enable it later.
 */
internal object IosPasswordCredentialStore : PasswordCredentialStore {
    // true so the viewmodels never show the in-app "Save" snackbar fallback on iOS.
    override val hasSystemSavePrompt: Boolean = true

    override fun save(email: String, password: String) = Unit

    override suspend fun get(): DtoPasswordCredential? = null
}
