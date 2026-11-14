package com.poulastaa.auth.ui.components

import com.github.javakeyring.Keyring
import com.poulastaa.auth.domain.PasswordCredentialStore
import com.poulastaa.auth.domain.model.DtoPasswordCredential
import com.poulastaa.common.domain.Log
import com.poulastaa.common.viewmodels.utils.dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "PasswordCredentialStore"

// Storage key, not a display label: must never change or saved passwords are orphaned, so it is not the (translatable) app name.
private const val SERVICE = "com.poulastaa.kyoku"

// The keyring is keyed by account, so the last signed-in email is kept as its own entry to know which account to read.
private const val LAST_EMAIL_ACCOUNT = "kyoku.last-email"

/**
 * OS credential store: macOS Keychain, Windows Credential Manager, Linux Secret Service.
 */
internal class JvmPasswordCredentialStore : PasswordCredentialStore {
    private val scope = CoroutineScope(SupervisorJob() + dispatchers.io)

    override val hasSystemSavePrompt: Boolean = false

    override fun save(email: String, password: String) {
        scope.launch {
            runCatching {
                Keyring.create().use {
                    it.setPassword(SERVICE, email, password)
                    it.setPassword(SERVICE, LAST_EMAIL_ACCOUNT, email)
                }
            }.onSuccess { Log.d(TAG, "Password saved to OS keyring") }
                .onFailure { Log.e(TAG, "Save password to OS keyring failed", it) }
        }
    }

    override suspend fun get(): DtoPasswordCredential? = withContext(dispatchers.io) {
        runCatching {
            Keyring.create().use {
                val email = it.getPassword(SERVICE, LAST_EMAIL_ACCOUNT)
                DtoPasswordCredential(email, it.getPassword(SERVICE, email))
            }
        }.onFailure { Log.d(TAG, "No saved password in OS keyring", it) }
            .getOrNull()
    }
}
