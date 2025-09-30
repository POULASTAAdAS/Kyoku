package com.poulastaa.auth.presentation.intro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.auth.presentation.intro.components.AppLogo
import com.poulastaa.auth.presentation.intro.components.AuthEmailTextField
import com.poulastaa.auth.presentation.intro.components.AuthPasswordTextFiled
import com.poulastaa.auth.presentation.intro.components.ContinueButton
import com.poulastaa.auth.presentation.intro.components.ContinueWithGoogleCard
import com.poulastaa.auth.presentation.intro.components.DontHaveAnAccount
import com.poulastaa.auth.presentation.intro.components.InNewUserCard
import com.poulastaa.auth.presentation.intro.components.LogInCard
import com.poulastaa.auth.presentation.intro.model.IntroUiState
import com.poulastaa.core.presentation.ui.AppThem
import com.poulastaa.core.presentation.ui.PreviewCompactLandscape
import com.poulastaa.core.presentation.ui.PreviewSmallLandscape
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun IntroHorizontalCompactScreen(
    modifier: Modifier = Modifier,
    state: IntroUiState,
    isSmall: Boolean = false,
    onAction: (action: IntroUiAction) -> Unit,
) {
    val focus = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(MaterialTheme.dimens.medium1)
            .navigationBarsPadding()
            .then(modifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.4f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(1f))

            AppLogo(Modifier.aspectRatio(1.5f))

            Spacer(Modifier.weight(1f))

            AnimatedVisibility(state.isNewEmailUser) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InNewUserCard(haptic, onAction)

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }
            }

            ContinueWithGoogleCard(state.isGoogleAuthLoading) {
                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                onAction(IntroUiAction.OnGoogleAuthClick)
            }
        }

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogInCard(
                modifier = Modifier.fillMaxWidth(.9f),
                heading = stringResource(R.string.sing_in)
            ) {
                AuthEmailTextField(state.email, onAction, focus)

                Spacer(Modifier.height(MaterialTheme.dimens.small1))

                AuthPasswordTextFiled(state.password, onAction, haptic, focus)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.dimens.small1),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onAction(IntroUiAction.OnForgotPasswordClick)
                                }
                            ),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.forgot_password),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(
                    if (isSmall) MaterialTheme.dimens.small1 else
                        MaterialTheme.dimens.medium1
                )
            )

            DontHaveAnAccount(haptic, onAction)

            Spacer(
                Modifier.height(
                    if (isSmall) MaterialTheme.dimens.small1 else
                        MaterialTheme.dimens.medium1
                )
            )

            ContinueButton(
                Modifier.fillMaxWidth(.5f),
                isEmailAuthLoading = state.isEmailAuthLoading,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    onAction(IntroUiAction.OnEmailSubmit)
                },
            )
        }
    }
}

@PreviewSmallLandscape
@PreviewCompactLandscape
@Composable
private fun Preview() {
    AppThem(isSystemInDarkTheme()) {
        IntroHorizontalCompactScreen(
            state = IntroUiState(
                isNewEmailUser = true
            ),
            isSmall = LocalWindowInfo.current.containerSize.height < 1080,
            onAction = {}
        )
    }
}