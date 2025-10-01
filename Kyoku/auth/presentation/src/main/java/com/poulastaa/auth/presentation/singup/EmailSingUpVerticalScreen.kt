package com.poulastaa.auth.presentation.singup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun EmailSingUpVerticalScreen(
    modifier: Modifier = Modifier,
    state: EmailSingUpUiState,
    onAction: (EmailSingUpUiAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.dimens.medium1)
            .systemBarsPadding()
            .navigationBarsPadding()
            .then(modifier)
    ) {

    }
}

@PreviewCompactPortrait
@Composable
private fun Preview() {
    MaterialTheme {
        EmailSingUpVerticalScreen(state = EmailSingUpUiState()) { }
    }
}