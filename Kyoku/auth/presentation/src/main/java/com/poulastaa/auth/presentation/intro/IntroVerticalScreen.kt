package com.poulastaa.auth.presentation.intro

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.auth.presentation.intro.components.AppLogo
import com.poulastaa.auth.presentation.intro.components.AuthCard
import com.poulastaa.auth.presentation.intro.components.AuthEmailTextField
import com.poulastaa.auth.presentation.intro.components.AuthPasswordTextFiled
import com.poulastaa.auth.presentation.intro.components.ConformButton
import com.poulastaa.auth.presentation.intro.components.ContinueWithGoogleCard
import com.poulastaa.auth.presentation.intro.components.InNewOrOldUserCard
import com.poulastaa.auth.presentation.intro.components.LogInSingUpSwitcher
import com.poulastaa.auth.presentation.intro.model.IntroUiState
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun IntroVerticalScreen(
    modifier: Modifier = Modifier,
    state: IntroUiState,
    onAction: (action: IntroUiAction) -> Unit,
) {
    val focus = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(MaterialTheme.dimens.medium1)
            .scrollable(
                state = rememberScrollState(0),
                orientation = Orientation.Horizontal
            )
            .navigationBarsPadding()
            .systemBarsPadding()
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(.2f))

        AppLogo(Modifier.aspectRatio(2f))

        Spacer(Modifier.weight(.2f))

        AuthCard(
            modifier = Modifier.fillMaxWidth(),
            heading = stringResource(R.string.sing_in)
        ) {
            AuthEmailTextField(
                email = state.email,
                onEmailChange = {
                    onAction(IntroUiAction.OnEmailChange(it))
                },
                onMoveFocus = {
                    focus.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small1))

            AuthPasswordTextFiled(
                password = state.password,
                onPasswordChange = { onAction(IntroUiAction.OnPasswordChange(it)) },
                onVisibilityToggle = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(IntroUiAction.ObPasswordVisibilityToggle)
                },
                onSubmit = {
                    focus.clearFocus()
                    onAction(IntroUiAction.OnEmailSubmit)
                },
            )

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

        Spacer(Modifier.weight(.3f))

        ConformButton(
            Modifier.fillMaxWidth(.6f),
            isLoading = state.isEmailAuthLoading,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                onAction(IntroUiAction.OnEmailSubmit)
            },
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Row(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                Modifier
                    .height(1.5.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .weight(1f)
            )

            Text(
                text = stringResource(R.string.or_login_with),
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small3)
            )

            Spacer(
                Modifier
                    .height(1.5.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .weight(1f)
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        ContinueWithGoogleCard(state.isGoogleAuthLoading) {
            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
            onAction(IntroUiAction.OnGoogleAuthClick)
        }

        Spacer(Modifier.weight(.5f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            AnimatedContent(
                state.isNewEmailUser
            ) {
                when (it) {
                    true -> InNewOrOldUserCard(
                        title = stringResource(R.string.email_not_registered),
                        content = stringResource(R.string.create_new_account)
                    ) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAction(IntroUiAction.OnEmailSingUpClick)
                    }

                    false -> LogInSingUpSwitcher(
                        type = stringResource(R.string.sing_up),
                        content = stringResource(R.string.dont_have_an_account)
                    ) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAction(IntroUiAction.OnEmailSingUpClick)
                    }
                }
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
    }
}

@PreviewCompactPortrait
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        IntroVerticalScreen(
            state = IntroUiState(),
            onAction = {}
        )
    }
}