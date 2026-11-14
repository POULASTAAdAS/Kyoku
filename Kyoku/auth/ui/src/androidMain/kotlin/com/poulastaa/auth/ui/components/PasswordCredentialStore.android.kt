package com.poulastaa.auth.ui.components

import android.app.Activity
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.poulastaa.auth.domain.PasswordCredentialStore
import com.poulastaa.auth.domain.model.DtoPasswordCredential
import com.poulastaa.common.domain.Log
import com.poulastaa.common.viewmodels.utils.dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val TAG = "PasswordCredentialStore"

/**
 * Saves to / reads from the user's credential provider (Google Password Manager by default).
 */
internal class AndroidPasswordCredentialStore(
    private val activityTracker: CurrentActivityTracker,
) : PasswordCredentialStore {
    private val scope = CoroutineScope(SupervisorJob() + dispatchers.main)

    // Save after reset and the picker on the next sign-in screen would otherwise open two sheets at once.
    private val mutex = Mutex()

    override val hasSystemSavePrompt: Boolean = true

    override fun save(email: String, password: String) {
        val activity = activityTracker.activity
        if (activity == null) {
            Log.e(TAG, "Save password skipped: no active activity")
            return
        }

        scope.launch {
            mutex.withLock { create(activity, email, password) }
        }
    }

    private suspend fun create(
        activity: Activity,
        email: String,
        password: String,
    ) {
        try {
            CredentialManager.create(activity).createCredential(
                context = activity,
                request = CreatePasswordRequest(id = email, password = password),
            )
            Log.d(TAG, "Password saved to credential provider")
        } catch (ex: CreateCredentialCancellationException) {
            Log.d(TAG, "Save password canceled by user", ex)
        } catch (ex: Exception) {
            Log.e(TAG, "Save password failed", ex)
        }
    }

    override suspend fun get(): DtoPasswordCredential? = mutex.withLock {
        val activity = activityTracker.activity ?: return@withLock null

        try {
            val credential = CredentialManager.create(activity).getCredential(
                context = activity,
                request = GetCredentialRequest.Builder()
                    .addCredentialOption(GetPasswordOption())
                    .build(),
            ).credential

            (credential as? PasswordCredential)?.let { DtoPasswordCredential(it.id, it.password) }
        } catch (ex: NoCredentialException) {
            Log.d(TAG, "No saved password found", ex)
            null
        } catch (ex: GetCredentialCancellationException) {
            Log.d(TAG, "Saved password picker canceled", ex)
            null
        } catch (ex: Exception) {
            Log.e(TAG, "Reading saved password failed", ex)
            null
        }
    }
}
