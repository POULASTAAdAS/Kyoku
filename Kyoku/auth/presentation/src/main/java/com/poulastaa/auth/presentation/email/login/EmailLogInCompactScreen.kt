package com.poulastaa.auth.presentation.email.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppLogo
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.EmailAlternateIcon
import com.poulastaa.core.presentation.designsystem.PasswordIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppLoadingButton
import com.poulastaa.core.presentation.designsystem.components.AppPasswordField
import com.poulastaa.core.presentation.designsystem.components.AppTextField
import com.poulastaa.core.presentation.designsystem.components.MovingCirclesWithMetaballEffect
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun EmailLogInCompactScreen(
    state: EmailLoginUiState,
    onAction: (EmailLogInUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MovingCirclesWithMetaballEffect()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.medium1)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            Image(
                painter = AppLogo,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(2f)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.weight(.5f))

            Box {
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.typography.displayMedium.fontSize.value.toInt().dp)
                            .padding(MaterialTheme.dimens.medium2),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppTextField(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.email.value,
                            onValueChange = { onAction(EmailLogInUiAction.OnEmailChange(it)) },
                            label = stringResource(R.string.email),
                            isError = state.email.isErr,
                            supportingText = state.email.errText.asString(),
                            trailingIcon = if (state.email.isValid) CheckIcon else null,
                            leadingIcon = EmailAlternateIcon,
                            keyboardType = KeyboardType.Email
                        )

                        AppPasswordField(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.password.value,
                            onValueChange = { onAction(EmailLogInUiAction.OnPasswordChange(it)) },
                            label = stringResource(R.string.password),
                            isError = state.password.isErr,
                            supportingText = state.password.errText.asString(),
                            leadingIcon = PasswordIcon,
                            isPasswordVisible = state.isPasswordVisible,
                            onPasswordToggleClick = {
                                onAction(EmailLogInUiAction.OnPasswordVisibilityToggle)
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            imeAction = ImeAction.Done,
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onAction(EmailLogInUiAction.OnConformClick)
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    focusManager.clearFocus()
                                }
                            )
                        )
                    }
                }

                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .offset(
                            y = (-MaterialTheme.typography.displayMedium.fontSize.value.toInt().dp)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.login)
                            .lowercase()
                            .replaceFirstChar { it.uppercaseChar() },
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.displayMedium.fontSize,
                        modifier = Modifier
                            .padding(
                                vertical = MaterialTheme.dimens.small3,
                                horizontal = MaterialTheme.dimens.medium2
                            )
                            .padding(bottom = MaterialTheme.dimens.medium3),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        onAction(EmailLogInUiAction.OnForgotPasswordClick)
                    }
                )
            }

            Spacer(Modifier.weight(1.3f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.dont_have_account),
                    color = MaterialTheme.colorScheme.tertiary,
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = stringResource(R.string.signUp),
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        onAction(EmailLogInUiAction.OnEmailSignUpClick)
                    }
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            AppLoadingButton(
                modifier = Modifier
                    .fillMaxWidth(.75f),
                text = stringResource(R.string.continue_text),
                onClick = {
                    onAction(EmailLogInUiAction.OnConformClick)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )

            Spacer(Modifier.weight(.5f))
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            EmailLogInCompactScreen(
                state = EmailLoginUiState(),
                onAction = {}
            )
        }
    }
}