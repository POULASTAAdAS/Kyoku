package com.poulastaa.auth.ui.sign_in.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.poulastaa.auth.ui.components.AuthTypeTitle
import com.poulastaa.auth.ui.components.LogInSignUpNavigation
import com.poulastaa.auth.ui.sign_in.SignInUiAction
import com.poulastaa.auth.ui.sign_in.SignInUiState
import com.poulastaa.auth.ui.sign_in.SignInViewmodel
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.AuthActionTypeList
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.IconEmail
import com.poulastaa.common.ui.design_system.IconEyeClose
import com.poulastaa.common.ui.design_system.IconEyeOpen
import com.poulastaa.common.ui.design_system.IconPasswordLock
import com.poulastaa.common.ui.design_system.StringCreateAccount
import com.poulastaa.common.ui.design_system.StringDontHaveAccount
import com.poulastaa.common.ui.design_system.StringEmail
import com.poulastaa.common.ui.design_system.StringForgotPassword
import com.poulastaa.common.ui.design_system.StringInvalidEmail
import com.poulastaa.common.ui.design_system.StringInvalidPassword
import com.poulastaa.common.ui.design_system.StringOrSigninWith
import com.poulastaa.common.ui.design_system.StringPassword
import com.poulastaa.common.ui.design_system.StringPasswordVisibility
import com.poulastaa.common.ui.design_system.StringS
import com.poulastaa.common.ui.design_system.StringSignIn
import com.poulastaa.common.ui.design_system.StringSignInRest
import com.poulastaa.common.ui.design_system.dimens

@Composable
internal fun ExpandedSignInScreen(
    state: SignInUiState,
    viewmodel: SignInViewmodel,
    focusManager: FocusManager,
    haptic: HapticFeedback,
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(MaterialTheme.dimens.layout.contentPadding)
            .navigationBarsPadding()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 1200.dp),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.layout.paneSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AuthActionTypeList(
                    buttonText = StringSignIn,
                    subTitle = StringOrSigninWith,
                    isMakingApiCall = state.isMakingApiCall,
                    isGoogleAuthInProgress = state.isGoogleAuthInProgress,
                    onEmailAuthClick = {
                        focusManager.clearFocus(false)
                        viewmodel.onAction(SignInUiAction.SignIn)
                    },
                    onGoogleAuthClick = {
                        viewmodel.onAction(SignInUiAction.OnGoogleSignInClick)
                    },
                    onGoogleAuthCancelClick = {
                        viewmodel.onAction(SignInUiAction.OnGoogleAuthCanceled)
                    }
                )

                ColumnSpacer(MaterialTheme.dimens.spacing.extraLarge)

                LogInSignUpNavigation(
                    title = StringDontHaveAccount,
                    navigationType = StringCreateAccount,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewmodel.onAction(SignInUiAction.OnCreateAccountClick)
                    }
                )
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.large,
            ) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.dimens.spacing.extraLarge),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                ) {
                    AuthTypeTitle(StringS, StringSignInRest)

                    ColumnSpacer(MaterialTheme.dimens.spacing.large)

                    AppOutlinedTextField(
                        value = state.email.value,
                        onValueChange = { viewmodel.onAction(SignInUiAction.OnEmailChange(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = StringEmail,
                        leadingIcon = IconEmail,
                        isError = state.email.isError,
                        supportingText = if (state.email.isError) state.email.errorMessage
                            ?: StringInvalidEmail else "",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                    )

                    AppOutlinedTextField(
                        value = state.password.value,
                        onValueChange = { viewmodel.onAction(SignInUiAction.OnPasswordChange(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = StringPassword,
                        leadingIcon = IconPasswordLock,
                        isError = state.password.isError,
                        supportingText = if (state.password.isError) state.password.errorMessage
                            ?: StringInvalidPassword else "",
                        trailingContent = {
                            AnimatedContent(
                                targetState = state.isPasswordVisible,
                                modifier = Modifier.clip(CircleShape).clickable(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewmodel.onAction(SignInUiAction.OnPasswordVisibilityToggle)
                                    }
                                )
                            ) {
                                Icon(
                                    imageVector = if (it) IconEyeClose else IconEyeOpen,
                                    contentDescription = StringPasswordVisibility,
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus(false)
                            }
                        ),
                        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = StringForgotPassword,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clip(MaterialTheme.shapes.small).clickable(
                                onClick = {
                                    viewmodel.onAction(
                                        SignInUiAction.OnForgotPasswordClick(state.email.value)
                                    )
                                }
                            ),
                        )
                    }
                }
            }
        }
    }
}
