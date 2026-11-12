package com.poulastaa.auth.ui.reset_password.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordUiAction
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordUiState
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordViewmodel
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.ElevatedDefaultButton
import com.poulastaa.common.ui.design_system.IconEyeClose
import com.poulastaa.common.ui.design_system.IconEyeOpen
import com.poulastaa.common.ui.design_system.IconPasswordLock
import com.poulastaa.common.ui.design_system.StringConfirmPassword
import com.poulastaa.common.ui.design_system.StringCreateNewPassword
import com.poulastaa.common.ui.design_system.StringInvalidPassword
import com.poulastaa.common.ui.design_system.StringPassword
import com.poulastaa.common.ui.design_system.StringPasswordVisibility
import com.poulastaa.common.ui.design_system.StringReset
import com.poulastaa.common.ui.design_system.dimens

@Composable
internal fun CompatVerticalResetPasswordScreen(
    state: ResetPasswordUiState,
    viewmodel: ResetPasswordViewmodel,
    focusManager: FocusManager,
    haptic: HapticFeedback,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(MaterialTheme.dimens.layout.contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = StringCreateNewPassword,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.small)

            val apiErrorAlpha by animateFloatAsState(if (state.isPasswordApiError) 1f else 0f)

            // Always laid out (an empty Text keeps one line of height) and only faded, so the screen never jumps.
            Text(
                text = state.passwordApiErrorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer { alpha = apiErrorAlpha },
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.extraLarge)

            AppOutlinedTextField(
                value = state.password.value,
                onValueChange = { viewmodel.onAction(ResetPasswordUiAction.OnPasswordChange(it)) },
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
                                viewmodel.onAction(ResetPasswordUiAction.OnPasswordVisibilityToggle)
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
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            )

            AppOutlinedTextField(
                value = state.confirmPassword.value,
                onValueChange = { viewmodel.onAction(ResetPasswordUiAction.OnConfirmPasswordChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = StringConfirmPassword,
                leadingIcon = IconPasswordLock,
                isError = state.confirmPassword.isError,
                supportingText = if (state.confirmPassword.isError) state.confirmPassword.errorMessage
                    ?: StringInvalidPassword else "",
                trailingContent = {
                    AnimatedContent(
                        targetState = state.isConfirmPasswordVisible,
                        modifier = Modifier.clip(CircleShape).clickable(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewmodel.onAction(ResetPasswordUiAction.OnConfirmPasswordVisibilityToggle)
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
                        if (state.isResetEnabled) viewmodel.onAction(ResetPasswordUiAction.ResetPassword)
                    }
                ),
                visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.extraLarge)

            ElevatedDefaultButton(
                width = 0.7f,
                enabled = state.isResetEnabled,
                onClick = {
                    focusManager.clearFocus(false)
                    viewmodel.onAction(ResetPasswordUiAction.ResetPassword)
                },
                colors = CardDefaults.elevatedCardColors(
                    contentColor = MaterialTheme.colorScheme.background,
                ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (state.isResetEnabled || state.isMakingApiCall) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small,
                        ).minimumInteractiveComponentSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = StringReset,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.alpha(if (state.isMakingApiCall) 0f else 1f),
                        color = MaterialTheme.colorScheme.background
                    )
                    CircularProgressIndicator(
                        Modifier.alpha(if (state.isMakingApiCall) 1f else 0f),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
    }
}
