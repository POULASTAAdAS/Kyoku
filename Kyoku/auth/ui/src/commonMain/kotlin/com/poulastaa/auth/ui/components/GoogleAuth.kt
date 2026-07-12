package com.poulastaa.auth.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun StartActivityForResult(
    key: Boolean,
    onSuccess: (token: String) -> Unit,
    onCanceled: () -> Unit,
)
