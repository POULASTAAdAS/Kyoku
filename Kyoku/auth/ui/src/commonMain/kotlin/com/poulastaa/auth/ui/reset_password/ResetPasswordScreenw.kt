package com.poulastaa.auth.ui.reset_password

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.ElevatedGradientButton
import com.poulastaa.common.ui.design_system.IconEyeClose
import com.poulastaa.common.ui.design_system.IconEyeOpen
import com.poulastaa.common.ui.design_system.IconPasswordLock
import com.poulastaa.common.ui.design_system.StringPassword
import com.poulastaa.common.ui.design_system.StringPasswordVisibility
import com.poulastaa.common.ui.design_system.dimens

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen() {
    val navController = LocalNavController.current
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val isValidPassword = remember(password, confirmPassword) { password == confirmPassword }
    val haptic = LocalHapticFeedback.current

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
                text = "Create new password",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.small)

            Text(
                text = "Your new password must not be same as your old password.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            ColumnSpacer(MaterialTheme.dimens.spacing.extraLarge)

            AppOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = StringPassword,
                leadingIcon = IconPasswordLock,
                supportingText = "",
                trailingContent = {
                    AnimatedContent(
                        targetState = passwordVisible,
                        modifier = Modifier.clip(CircleShape).clickable(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                passwordVisible = passwordVisible.not()
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
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            )

            AppOutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = "confirm password",
                leadingIcon = IconPasswordLock,
                supportingText = "",
                trailingContent = {
                    AnimatedContent(
                        targetState = confirmPasswordVisible,
                        modifier = Modifier.clip(CircleShape).clickable(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                confirmPasswordVisible = confirmPasswordVisible.not()
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
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            )


            ColumnSpacer(MaterialTheme.dimens.spacing.extraLarge)

            ElevatedGradientButton(
                width = 0.7f,
                onClick = {

                },
                colors = CardDefaults.elevatedCardColors(
                    contentColor = MaterialTheme.colorScheme.background,
                ),
                enabled = isValidPassword
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small,
                        ).minimumInteractiveComponentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "REST", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    BackHandler {}
}