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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.poulastaa.auth.presentation.intro.components.AppLogo
import com.poulastaa.auth.presentation.intro.components.ConformButton
import com.poulastaa.auth.presentation.singup.components.InfoCard
import com.poulastaa.auth.presentation.singup.components.SingUpCard
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.PreviewLargeLandscape
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun EmailSingUpVerticalExpandedScreen(
    modifier: Modifier = Modifier,
    state: EmailSingUpUiState,
    onAction: (EmailSingUpUiAction) -> Unit,
) {
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
                .fillMaxWidth(.35f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppLogo(Modifier.aspectRatio(1.2f))
        }

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SingUpCard(
                modifier = Modifier.fillMaxWidth(.9f),
                heading = stringResource(R.string.sing_up),
                state = state,
                onAction = onAction,
                focus = focus,
                haptic = haptic
            )

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            InfoCard(state, haptic, onAction)

            Spacer(Modifier.height(MaterialTheme.dimens.large2))

            ConformButton(
                modifier = Modifier.fillMaxWidth(.3f),
                isLoading = state.isLoading,
                onClick = {
                    focus.clearFocus()
                    onAction(EmailSingUpUiAction.OnSubmit)
                }
            )
        }
    }
}


@PreviewLargeLandscape
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        EmailSingUpVerticalExpandedScreen(state = EmailSingUpUiState(isOldUser = isSystemInDarkTheme())) { }
    }
}