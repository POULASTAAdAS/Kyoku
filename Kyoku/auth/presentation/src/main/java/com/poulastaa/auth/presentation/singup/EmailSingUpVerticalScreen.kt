package com.poulastaa.auth.presentation.singup

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.poulastaa.auth.presentation.intro.components.AppLogo
import com.poulastaa.auth.presentation.intro.components.ConformButton
import com.poulastaa.auth.presentation.singup.components.InfoCard
import com.poulastaa.auth.presentation.singup.components.SingUpCard
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailSingUpVerticalScreen(
    modifier: Modifier = Modifier,
    state: EmailSingUpUiState,
    onAction: (EmailSingUpUiAction) -> Unit,
) {
    val context = LocalContext.current
    val focus = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.dimens.medium1)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        AppLogo(Modifier.aspectRatio(1.7f))

        Spacer(Modifier.height(MaterialTheme.dimens.medium3))

        SingUpCard(
            modifier = Modifier.fillMaxWidth(),
            heading = stringResource(R.string.sing_up),
            state = state,
            onAction = onAction,
            focus = focus,
            haptic = haptic
        )

        Spacer(Modifier.height(MaterialTheme.dimens.large1))

        ConformButton(
            modifier = Modifier.fillMaxWidth(.5f),
            isLoading = state.isLoading,
            onClick = {
                focus.clearFocus()
                onAction(EmailSingUpUiAction.OnSubmit(context))
            }
        )

        Spacer(Modifier.weight(1f))

        InfoCard(state, haptic, onAction)

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
    }
}


@PreviewCompactPortrait
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        EmailSingUpVerticalScreen(state = EmailSingUpUiState(isOldUser = true)) { }
    }
}