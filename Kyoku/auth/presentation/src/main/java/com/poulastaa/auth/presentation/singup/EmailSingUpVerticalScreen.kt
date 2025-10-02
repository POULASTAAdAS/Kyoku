package com.poulastaa.auth.presentation.singup

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.core.presentation.ui.AppThem
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailSingUpVerticalScreen(
    modifier: Modifier = Modifier,
    state: EmailSingUpUiState,
    onAction: (EmailSingUpUiAction) -> Unit,
) {
}

@PreviewCompactPortrait
@Composable
private fun Preview() {
    AppThem(isSystemInDarkTheme()) {
        EmailSingUpVerticalScreen(state = EmailSingUpUiState()) { }
    }
}