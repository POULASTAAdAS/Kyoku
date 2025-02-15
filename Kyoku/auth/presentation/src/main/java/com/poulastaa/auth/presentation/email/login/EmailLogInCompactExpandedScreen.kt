package com.poulastaa.auth.presentation.email.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.AppLogo
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.EmailAlternateIcon
import com.poulastaa.core.presentation.designsystem.ui.PasswordIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppLoadingButton
import com.poulastaa.core.presentation.ui.components.AppPasswordField
import com.poulastaa.core.presentation.ui.components.AppTextField
import com.poulastaa.core.presentation.ui.components.MovingCirclesWithMetaballEffect

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailLogInCompactExpandedScreen(
    autoFill: Autofill?,
    autoFillEmail: AutofillNode,
    autoFillPassword: AutofillNode,
    state: EmailLoginUiState,
    onAction: (EmailLogInUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MovingCirclesWithMetaballEffect()

        Row(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .systemBarsPadding()
                .padding(MaterialTheme.dimens.medium1),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(.35f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = AppLogo,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(2f)
                )

                Spacer(Modifier.height(MaterialTheme.dimens.large1))

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
                        modifier = Modifier.noRippleClickable {
                            onAction(EmailLogInUiAction.OnEmailSignUpClick)
                        }
                    )
                }

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                AppLoadingButton(
                    modifier = Modifier
                        .fillMaxWidth(.8f),
                    text = stringResource(R.string.continue_text),
                    isLoading = state.isMakingApiCall,
                    onClick = {
                        onAction(EmailLogInUiAction.OnConformClick)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                )
            }

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

            Column(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box {
                    Card(
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned {
                                        autoFillEmail.boundingBox = it.boundsInParent()
                                    }
                                    .onFocusChanged {
                                        autoFill?.run {
                                            if (it.isFocused) requestAutofillForNode(autoFillEmail)
                                            else cancelAutofillForNode(autoFillEmail)
                                        }
                                    },
                                text = state.email.value,
                                onValueChange = { onAction(EmailLogInUiAction.OnEmailChange(it)) },
                                label = stringResource(R.string.email),
                                isError = state.email.isErr,
                                supportingText = state.email.errText.asString(),
                                trailingIcon = if (state.email.isValid) CheckIcon else null,
                                leadingIcon = EmailAlternateIcon
                            )

                            AppPasswordField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned {
                                        autoFillPassword.boundingBox = it.boundsInParent()
                                    }
                                    .onFocusChanged {
                                        autoFill?.run {
                                            if (it.isFocused) requestAutofillForNode(
                                                autoFillPassword
                                            )
                                            else cancelAutofillForNode(autoFillPassword)
                                        }
                                    },
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
                        modifier = Modifier.noRippleClickable {
                            onAction(EmailLogInUiAction.OnForgotPasswordClick)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    widthDp = 640,
    heightDp = 340
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 640,
    heightDp = 340
)
@Composable
private fun Preview() {
    AppThem {
        val autoFill = LocalAutofill.current
        val autoFillPassword = AutofillNode(
            autofillTypes = listOf(AutofillType.Password),
            onFill = {}
        )

        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            EmailLogInCompactExpandedScreen(
                autoFill = autoFill,
                autoFillEmail = autoFillPassword,
                autoFillPassword = autoFillPassword,
                state = EmailLoginUiState(),
                onAction = {}
            )
        }
    }
}