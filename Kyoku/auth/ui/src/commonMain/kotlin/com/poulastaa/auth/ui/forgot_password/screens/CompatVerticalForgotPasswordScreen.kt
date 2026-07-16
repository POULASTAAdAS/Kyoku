package com.poulastaa.auth.ui.forgot_password.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.poulastaa.auth.ui.forgot_password.ForgotPasswordUiAction
import com.poulastaa.auth.ui.forgot_password.ForgotPasswordUiState
import com.poulastaa.auth.ui.forgot_password.ForgotPasswordViewmodel
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.ElevatedDefaultButton
import com.poulastaa.common.ui.components.top_bar.DefaultTopBar
import com.poulastaa.common.ui.design_system.IconEmail
import com.poulastaa.common.ui.design_system.StringEmail
import com.poulastaa.common.ui.design_system.StringGetOtp
import com.poulastaa.common.ui.design_system.StringInvalidEmail
import com.poulastaa.common.ui.design_system.StringResetPassword
import com.poulastaa.common.ui.design_system.StringResetPasswordMessage
import com.poulastaa.common.ui.design_system.dimens

@Composable
internal fun CompatVerticalForgotPasswordScreen(
    navController: NavHostController,
    state: ForgotPasswordUiState,
    viewmodel: ForgotPasswordViewmodel,
    focusManager: FocusManager,
    isGetOtpEnabled: Boolean,
) {
    Scaffold(
        topBar = {
            DefaultTopBar(rotation = 270f, onBackClick = navController::popBackStack)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = MaterialTheme.dimens.layout.contentPadding),
        ) {
            Text(
                text = StringResetPassword,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.small)

            Text(
                text = StringResetPasswordMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.extraLarge)

            AppOutlinedTextField(
                value = state.email.value,
                onValueChange = { viewmodel.onAction(ForgotPasswordUiAction.OnEmailChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = StringEmail,
                leadingIcon = IconEmail,
                isError = state.email.isError,
                supportingText = if (state.email.isError) state.email.errorMessage ?: StringInvalidEmail else "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.small)

            ElevatedDefaultButton(
                width = 0.7f,
                enabled = isGetOtpEnabled,
                onClick = {
                    focusManager.clearFocus(false)
                    viewmodel.onAction(ForgotPasswordUiAction.GetOtp(state.email.value))
                },
                colors = CardDefaults.elevatedCardColors(
                    contentColor = if (isGetOtpEnabled) MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.onBackground,
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isGetOtpEnabled) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small,
                        ).minimumInteractiveComponentSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = StringGetOtp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
