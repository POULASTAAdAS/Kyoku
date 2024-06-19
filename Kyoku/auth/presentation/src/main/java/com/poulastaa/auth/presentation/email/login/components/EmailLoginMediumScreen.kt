package com.poulastaa.auth.presentation.email.login.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.auth.presentation.email.components.AuthContinueButton
import com.poulastaa.auth.presentation.email.components.AuthTextField
import com.poulastaa.auth.presentation.email.login.EmailLogInUiState
import com.poulastaa.auth.presentation.email.login.EmailLoginUiEvent
import com.poulastaa.core.presentation.designsystem.AppLogo
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.EmailIcon
import com.poulastaa.core.presentation.designsystem.EyeCloseIcon
import com.poulastaa.core.presentation.designsystem.EyeOpenIcon
import com.poulastaa.core.presentation.designsystem.PasswordIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun EmailLoginMediumScreen(
    state: EmailLogInUiState,
    onEvent: (EmailLoginUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Image(
        painter = AppLogo,
        contentDescription = null,
        modifier = Modifier.aspectRatio(2f)
    )


    Column(
        modifier = Modifier.fillMaxWidth(.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            text = state.email,
            onValueChange = { onEvent(EmailLoginUiEvent.OnEmailChange(it)) },
            label = stringResource(id = R.string.email),
            leadingIcon = {
                Icon(
                    imageVector = EmailIcon,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (state.isValidEmail) Icon(
                    imageVector = CheckIcon,
                    contentDescription = null
                ) else Unit
            },
            onDone = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            text = state.password,
            onValueChange = { onEvent(EmailLoginUiEvent.OnPasswordChange(it)) },
            label = stringResource(id = R.string.password),
            leadingIcon = {
                Icon(
                    imageVector = PasswordIcon,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onEvent(EmailLoginUiEvent.OnPasswordVisibilityToggle)
                    }
                ) {
                    Icon(
                        imageVector = if (state.isPasswordVisible) EyeCloseIcon else EyeOpenIcon,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onDone = {
                focusManager.clearFocus()
                onEvent(EmailLoginUiEvent.OnContinueClick)
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = Color.DarkGray,
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        onEvent(EmailLoginUiEvent.OnForgotPasswordClick)
                    }
                )
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${stringResource(id = R.string.dont_have_account)}  ",
                color = MaterialTheme.colorScheme.background
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))

            Text(
                text = stringResource(id = R.string.signUp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primaryContainer,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        onEvent(EmailLoginUiEvent.OnEmailSignUpClick)
                    }
                )
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium3))

        AuthContinueButton(
            text = stringResource(id = R.string.continue_text),
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(MaterialTheme.dimens.small1),
            isLoading = state.isMakingApiCall,
            onClick = {
                onEvent(EmailLoginUiEvent.OnContinueClick)
            }
        )


        AnimatedVisibility(visible = state.isResendVerificationMailVisible) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

                Text(text = stringResource(id = R.string.did_not_reveive__verification_mail))

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                AuthContinueButton(
                    text = if (state.canResendMailAgain) stringResource(id = R.string.send_again) else state.resendMailText,
                    enable = state.canResendMailAgain,
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .padding(MaterialTheme.dimens.small1),
                    isLoading = state.isResendMailLoading,
                    onClick = {
                        onEvent(EmailLoginUiEvent.OnResendMailClick)
                    }
                )
            }
        }
    }
}


@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480"
)
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onTertiary,
                            MaterialTheme.colorScheme.tertiary,
                        )
                    )
                )
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EmailLoginMediumScreen(
                state = EmailLogInUiState(
                    isResendVerificationMailVisible = false
                )
            ) {

            }
        }
    }
}