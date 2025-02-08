package com.poulastaa.auth.presentation.email.signup

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.EmailAlternateIcon
import com.poulastaa.core.presentation.designsystem.PasswordIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.components.AppLoadingButton
import com.poulastaa.core.presentation.designsystem.components.AppPasswordField
import com.poulastaa.core.presentation.designsystem.components.AppTextField
import com.poulastaa.core.presentation.designsystem.components.MovingCirclesWithMetaballEffect
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailSignUpCompactExpandedScreen(
    autoFill: Autofill?,
    autoFillEmail: AutofillNode,
    autoFillPassword: AutofillNode,
    state: EmailSignUpUiState,
    onAction: (EmailSignUpUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Box {
        MovingCirclesWithMetaballEffect()

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.medium1)
                .align(Alignment.Center)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(.35f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.create),
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )
                Text(
                    text = stringResource(R.string.an_account),
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )


                Spacer(Modifier.height(MaterialTheme.dimens.large2))

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
                        text = stringResource(R.string.login),
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            onAction(EmailSignUpUiAction.OnEmailLogInClick)
                        }
                    )
                }

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                AppLoadingButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.continue_text),
                    isLoading = state.isMakingApiCall,
                    onClick = {
                        onAction(EmailSignUpUiAction.OnConformClick(context.resources.configuration.locales[0].country))
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                )
            }

            Spacer(Modifier.width(MaterialTheme.dimens.medium3))

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
                    modifier = Modifier.padding(MaterialTheme.dimens.medium1)
                ) {
                    AppTextField(
                        modifier = Modifier.fillMaxWidth()
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
                        onValueChange = { onAction(EmailSignUpUiAction.OnEmailChange(it)) },
                        label = stringResource(R.string.email),
                        isError = state.email.isErr,
                        supportingText = state.email.errText.asString(),
                        trailingIcon = if (state.email.isValid) CheckIcon else null,
                        leadingIcon = EmailAlternateIcon
                    )

                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.username.value,
                        onValueChange = { onAction(EmailSignUpUiAction.OnEmailChange(it)) },
                        label = stringResource(R.string.username),
                        isError = state.username.isErr,
                        supportingText = state.username.errText.asString(),
                        leadingIcon = UserIcon
                    )

                    AppPasswordField(
                        modifier = Modifier.fillMaxWidth()
                            .onGloballyPositioned {
                                autoFillPassword.boundingBox = it.boundsInParent()
                            }
                            .onFocusChanged {
                                autoFill?.run {
                                    if (it.isFocused) requestAutofillForNode(autoFillPassword)
                                    else cancelAutofillForNode(autoFillPassword)
                                }
                            },
                        text = state.password.value,
                        onValueChange = { onAction(EmailSignUpUiAction.OnPasswordChange(it)) },
                        label = stringResource(R.string.password),
                        isError = state.password.isErr,
                        supportingText = state.password.errText.asString(),
                        leadingIcon = PasswordIcon,
                        isPasswordVisible = state.isPasswordVisible,
                        onPasswordToggleClick = {
                            onAction(EmailSignUpUiAction.OnPasswordVisibilityToggle)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )

                    AppPasswordField(
                        modifier = Modifier.fillMaxWidth()
                            .onGloballyPositioned {
                                autoFillPassword.boundingBox = it.boundsInParent()
                            }
                            .onFocusChanged {
                                autoFill?.run {
                                    if (it.isFocused) requestAutofillForNode(autoFillPassword)
                                    else cancelAutofillForNode(autoFillPassword)
                                }
                            },
                        text = state.conformPassword.value,
                        onValueChange = { onAction(EmailSignUpUiAction.OnConformPasswordChange(it)) },
                        label = stringResource(R.string.conform_password),
                        isError = state.conformPassword.isErr,
                        supportingText = state.conformPassword.errText.asString(),
                        leadingIcon = PasswordIcon,
                        onPasswordToggleClick = {},
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onAction(EmailSignUpUiAction.OnConformClick(context.resources.configuration.locales[0].country))
                                focusManager.clearFocus()
                            }
                        )
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    widthDp = 940,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 940,
    heightDp = 540
)
@Composable
private fun Preview() {
    AppThem {
        val autoFill = LocalAutofill.current
        val autoFillPassword = AutofillNode(
            autofillTypes = listOf(AutofillType.Password),
            onFill = {}
        )
        val autoFillUserName = AutofillNode(
            autofillTypes = listOf(AutofillType.NewUsername),
            onFill = {}
        )

        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            EmailSignUpCompactExpandedScreen(
                autoFill = autoFill,
                autoFillPassword = autoFillPassword,
                autoFillEmail = autoFillUserName,
                state = EmailSignUpUiState(),
                onAction = {}
            )
        }
    }
}