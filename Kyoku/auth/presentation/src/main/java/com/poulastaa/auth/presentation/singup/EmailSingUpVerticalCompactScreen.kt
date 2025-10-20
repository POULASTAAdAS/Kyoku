package com.poulastaa.auth.presentation.singup

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.auth.presentation.intro.components.AppLogo
import com.poulastaa.auth.presentation.intro.components.ConformButton
import com.poulastaa.auth.presentation.singup.components.InfoCard
import com.poulastaa.auth.presentation.singup.components.SingUpCard
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.PreviewLandscape
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun EmailSingUpVerticalCompactScreen(
    modifier: Modifier = Modifier,
    state: EmailSingUpUiState,
    onAction: (EmailSingUpUiAction) -> Unit,
) {
    val context = LocalContext.current
    val focus = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.dimens.medium1)
            .navigationBarsPadding()
            .systemBarsPadding()
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.4f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppLogo(Modifier.aspectRatio(3f))

            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            InfoCard(state, haptic, onAction)

            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            ConformButton(
                modifier = Modifier.fillMaxWidth(.5f),
                isLoading = state.isLoading,
                onClick = {
                    focus.clearFocus()
                    onAction(EmailSingUpUiAction.OnSubmit(context))
                }
            )
        }

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SingUpCard(
                modifier = Modifier.fillMaxWidth(.9f),
                state = state,
                onAction = onAction,
                focus = focus,
                haptic = haptic
            )
        }
    }
}


@PreviewLandscape
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        EmailSingUpVerticalCompactScreen(state = EmailSingUpUiState(isOldUser = true)) { }
    }
}