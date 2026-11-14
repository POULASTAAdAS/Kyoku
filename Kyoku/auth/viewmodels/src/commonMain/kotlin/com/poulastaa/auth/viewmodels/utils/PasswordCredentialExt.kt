package com.poulastaa.auth.viewmodels.utils

import com.poulastaa.auth.domain.PasswordCredentialStore
import com.poulastaa.common.viewmodels.SnackbarAction
import com.poulastaa.common.viewmodels.SnackbarType
import com.poulastaa.common.viewmodels.stringSave
import com.poulastaa.common.viewmodels.stringSavePasswordPrompt

/**
 * Call only after the server verified the credential, so a wrong password is never stored.
 * Platforms with a system prompt save directly; others ask through the in-app snackbar first.
 */
internal suspend fun PasswordCredentialStore.offerToSave(
    email: String,
    password: String,
    showSnackbar: (message: String, type: SnackbarType, action: SnackbarAction?) -> Unit,
) {
    if (hasSystemSavePrompt) return save(email, password)

    showSnackbar(
        stringSavePasswordPrompt(),
        SnackbarType.Info,
        SnackbarAction(label = stringSave()) { save(email, password) },
    )
}
