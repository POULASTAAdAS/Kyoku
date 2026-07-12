package com.poulastaa.auth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun StartActivityForResult(
    key: Boolean,
    clientId: String,
    onSuccess: (token: String) -> Unit,
    onCanceled: () -> Unit,
) {
    LaunchedEffect(key) {
        if (key) onCanceled()
    }
}
