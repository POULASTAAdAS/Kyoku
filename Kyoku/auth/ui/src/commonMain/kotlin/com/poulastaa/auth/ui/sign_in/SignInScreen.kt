package com.poulastaa.auth.ui.sign_in

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.poulastaa.auth.ui.components.AuthTypeTitle
import com.poulastaa.auth.ui.components.GoogleAuthResult
import com.poulastaa.auth.ui.components.GoogleAuthWrapper
import com.poulastaa.auth.ui.components.LogInSignUpNavigation
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.Screens
import com.poulastaa.common.ui.Screens.AuthScreens.ForgotPassword
import com.poulastaa.common.ui.Screens.AuthScreens.SignUp
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.AuthActionTypeList
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.IconAppLogo
import com.poulastaa.common.ui.design_system.IconEmail
import com.poulastaa.common.ui.design_system.IconEyeClose
import com.poulastaa.common.ui.design_system.IconEyeOpen
import com.poulastaa.common.ui.design_system.IconPasswordLock
import com.poulastaa.common.ui.design_system.StringAppIcon
import com.poulastaa.common.ui.design_system.StringCreateAccount
import com.poulastaa.common.ui.design_system.StringDontHaveAccount
import com.poulastaa.common.ui.design_system.StringEmail
import com.poulastaa.common.ui.design_system.StringForgotPassword
import com.poulastaa.common.ui.design_system.StringInvalidEmail
import com.poulastaa.common.ui.design_system.StringInvalidPassword
import com.poulastaa.common.ui.design_system.StringLogInWelcomeBackMessage
import com.poulastaa.common.ui.design_system.StringOrSigninWith
import com.poulastaa.common.ui.design_system.StringPassword
import com.poulastaa.common.ui.design_system.StringPasswordVisibility
import com.poulastaa.common.ui.design_system.StringS
import com.poulastaa.common.ui.design_system.StringSignIn
import com.poulastaa.common.ui.design_system.StringSignInRest
import com.poulastaa.common.ui.design_system.dimens
import com.poulastaa.common.ui.viewmodel.CommonUiEvent
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SignInScreen(
    viewmodel: SignInViewmodel = koinViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val navController = LocalNavController.current
    val googleAuthWrapper = koinInject<GoogleAuthWrapper>()
    val state by viewmodel.uiState.collectAsState()

    DisposableEffect(googleAuthWrapper, viewmodel) {
        googleAuthWrapper.onResult = { result ->
            when (result) {
                is GoogleAuthResult.Success -> viewmodel.onAction(SignInUiAction.OnGoogleAuthSuccess(result.token))
                GoogleAuthResult.Canceled,
                is GoogleAuthResult.Error -> viewmodel.onAction(SignInUiAction.OnGoogleAuthCanceled)
            }
        }

        onDispose { googleAuthWrapper.onResult = null }
    }

    LaunchedEffect(state.isGoogleAuthInProgress, googleAuthWrapper) {
        if (state.isGoogleAuthInProgress) googleAuthWrapper.startGoogleAuth()
    }

    LaunchedEffect(viewmodel) {
        viewmodel.event.collect { event ->
            when (event) {
                is SignInUiEvent.NavigateToForgotPassword -> navController.navigate(
                    ForgotPassword(event.email)
                )

                SignInUiEvent.NavigateToSignUp -> navController.navigate(SignUp)

                SignInUiEvent.NavigateToHome -> navController.navigate(Screens.MainScreens.Home)
                SignInUiEvent.NavigateToImportPlaylist -> navController.navigate(Screens.SetupScreens.ImportPlaylist)
            }
        }
    }

    LaunchedEffect(viewmodel) {
        viewmodel.commonEvent.collect { event ->
            when (event) {
                is CommonUiEvent.ShowError -> {
                    // TODO: handle errors
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(MaterialTheme.dimens.layout.contentPadding)
            .navigationBarsPadding()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.3f)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.small)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = IconAppLogo,
                contentDescription = StringAppIcon,
                modifier = Modifier.fillMaxSize(1f),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.weight(0.2f))

        Text(
            text = StringLogInWelcomeBackMessage,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        ColumnSpacer(MaterialTheme.dimens.spacing.large)

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
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                        viewmodel.onAction(SignInUiAction.OnForgotPasswordClick(state.email.value))
                    }
                ),
            )
        }

        Spacer(Modifier.weight(0.3f))

        AuthActionTypeList(
            buttonText = StringSignIn,
            subTitle = StringOrSigninWith,
            isMakingApiCall = state.isMakingApiCall,
            isGoogleAuthInProgress = state.isGoogleAuthInProgress,
            onEmailAuthClick = {
                focusManager.clearFocus(false)
                viewmodel.onAction(
                    SignInUiAction.SignIn(
                        email = state.email.value,
                        password = state.password.value,
                    )
                )
            },
            onGoogleAuthClick = {
                viewmodel.onAction(SignInUiAction.OnGoogleSignInClick)
            }
        )

        Spacer(Modifier.weight(0.1f))

        LogInSignUpNavigation(
            title = StringDontHaveAccount,
            navigationType = StringCreateAccount,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                viewmodel.onAction(SignInUiAction.OnCreateAccountClick)
            }
        )

        ColumnSpacer(MaterialTheme.dimens.spacing.extraSmall)
    }
}
