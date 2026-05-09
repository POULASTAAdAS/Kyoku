package com.poulastaa.auth.ui.sign_up

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.poulastaa.auth.ui.components.AuthTypeTitle
import com.poulastaa.auth.ui.components.LogInSignUpNavigation
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.AuthActionTypeList
import com.poulastaa.common.ui.components.BackButton
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.IconAppLogo
import com.poulastaa.common.ui.design_system.IconEmail
import com.poulastaa.common.ui.design_system.IconEyeClose
import com.poulastaa.common.ui.design_system.IconEyeOpen
import com.poulastaa.common.ui.design_system.IconPasswordLock
import com.poulastaa.common.ui.design_system.IconUser
import com.poulastaa.common.ui.design_system.StringAlreadyHaveAccount
import com.poulastaa.common.ui.design_system.StringAppIcon
import com.poulastaa.common.ui.design_system.StringEmail
import com.poulastaa.common.ui.design_system.StringInvalidEmail
import com.poulastaa.common.ui.design_system.StringInvalidUsername
import com.poulastaa.common.ui.design_system.StringLogIn
import com.poulastaa.common.ui.design_system.StringOrSignUpWith
import com.poulastaa.common.ui.design_system.StringPassword
import com.poulastaa.common.ui.design_system.StringPasswordVisibility
import com.poulastaa.common.ui.design_system.StringS
import com.poulastaa.common.ui.design_system.StringSignUp
import com.poulastaa.common.ui.design_system.StringSignUpRest
import com.poulastaa.common.ui.design_system.StringSignUpWelcomeBackMessage
import com.poulastaa.common.ui.design_system.StringUsername
import com.poulastaa.common.ui.design_system.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingUpScreen() {
    val navController = LocalNavController.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    // TODO: will be moved to viewmodel
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isError by remember { mutableStateOf(false) }
    val isPasswordVisible = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.dimens.layout.contentPadding)
            .navigationBarsPadding()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopStart
        ) {
            Box(
                Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = IconAppLogo,
                    contentDescription = StringAppIcon,
                    modifier = Modifier.fillMaxSize(1f),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            BackButton(
                onClick = navController::popBackStack
            )
        }

        ColumnSpacer(MaterialTheme.dimens.spacing.small)

        Text(
            text = StringSignUpWelcomeBackMessage,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.weight(0.2f))

        AuthTypeTitle(StringS, StringSignUpRest)

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

        AppOutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = StringUsername,
            leadingIcon = IconUser,
            isError = isError,
            supportingText = if (isError) StringInvalidUsername else "",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )

        AppOutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = StringEmail,
            leadingIcon = IconEmail,
            isError = isError,
            supportingText = if (isError) StringInvalidEmail else "",
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
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = StringPassword,
            leadingIcon = IconPasswordLock,
            supportingText = "",
            trailingContent = {
                AnimatedContent(
                    targetState = isPasswordVisible.value,
                    modifier = Modifier.clip(CircleShape).clickable(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isPasswordVisible.value = !isPasswordVisible.value
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
            visualTransformation = if (isPasswordVisible.value) VisualTransformation.None
            else PasswordVisualTransformation(),
        )

        Spacer(Modifier.weight(0.2f))

        AuthActionTypeList(
            emailButtonText = StringSignUp,
            subTitle = StringOrSignUpWith,
            onEmailAuthClick = {

            },
            onGoogleAuthClick = {

            }
        )

        Spacer(Modifier.weight(0.1f))

        LogInSignUpNavigation(
            title = StringAlreadyHaveAccount,
            navigationType = StringLogIn,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                navController.popBackStack()
            }
        )

        ColumnSpacer(MaterialTheme.dimens.spacing.extraSmall)
    }
}