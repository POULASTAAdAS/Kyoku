package com.poulastaa.auth.domain

import com.poulastaa.auth.domain.model.DtoPasswordCredential

interface PasswordCredentialStore {
    /**
     * true when the platform shows its own "save password?" prompt (Android Credential Manager),
     * false when the app has to ask the user before calling [save] (desktop keyring).
     */
    val hasSystemSavePrompt: Boolean

    /** Fire-and-forget: runs in the store's own scope so it outlives the calling screen. */
    fun save(email: String, password: String)

    suspend fun get(): DtoPasswordCredential?
}
