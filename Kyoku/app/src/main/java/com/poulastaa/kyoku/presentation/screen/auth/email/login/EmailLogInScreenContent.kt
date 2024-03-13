package com.poulastaa.kyoku.presentation.screen.auth.email.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomButton
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomPasswordField
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomTextFiled
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EmailLogInScreenContent(
    email: String,
    password: String,
    autoFillEmail: AutofillNode,
    autoFillPassword: AutofillNode,
    autofill: Autofill?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    emailSupportingText: String,
    passwordSupportingText: String,
    isEmailError: Boolean,
    isPasswordError: Boolean,
    passwordVisibility: Boolean,
    changePasswordVisibility: () -> Unit,
    onDone: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignUpClick: () -> Unit,
    isLoading: Boolean,
    isResendVerificationMailPromptVisible: Boolean,
    sendVerificationMailTimer: Int,
    isResendMailEnabled: Boolean,
    resendButtonClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val mutableInteractionSource = remember {
        MutableInteractionSource()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(MaterialTheme.dimens.medium1)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // logo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f / 3),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = if (isSystemInDarkTheme()) R.drawable.night_logo
                    else R.drawable.light_logo
                ),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f / 2),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomTextFiled(
                value = email,
                onValueChange = onEmailChange,
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        autoFillEmail.boundingBox = it.boundsInRoot()
                    }
                    .onFocusChanged {
                        autofill?.run {
                            if (it.isFocused) requestAutofillForNode(autoFillEmail)
                        }
                    },
                isError = isEmailError,
                supportingText = emailSupportingText,
                shape = MaterialTheme.shapes.small
            )

            CustomPasswordField(
                value = password,
                onValueChange = onPasswordChange,
                onDone = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        autoFillPassword.boundingBox = it.boundsInRoot()
                    }
                    .onFocusChanged {
                        autofill?.run {
                            if (it.isFocused) requestAutofillForNode(autoFillPassword)
                            else cancelAutofillForNode(autoFillPassword)
                        }
                    },
                isError = isPasswordError,
                supportingText = passwordSupportingText,
                trailingIcon = {
                    IconButton(onClick = changePasswordVisibility) {
                        Icon(
                            painter = if (passwordVisibility) painterResource(id = R.drawable.ic_visibility_off)
                            else painterResource(id = R.drawable.ic_visibility_on),
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                shape = MaterialTheme.shapes.small
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier.clickable(
                        onClick = onForgotPassword,
                        indication = null,
                        interactionSource = mutableInteractionSource
                    ),
                    text = "Forgot Password ?",
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
        ) {
            FilledIconButton(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = MaterialTheme.dimens.large2)
                    .shadow(
                        elevation = 3.dp,
                        shape = MaterialTheme.shapes.small,
                        clip = true
                    ),
                shape = MaterialTheme.shapes.small
            ) {
                if (isLoading)
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                else
                    Text(
                        text = "C O N T I N U E",
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Text(
                    text = " Register Here",
                    modifier = Modifier.clickable(
                        onClick = onSignUpClick,
                        indication = null,
                        interactionSource = mutableInteractionSource
                    ),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
        ) {
            AnimatedVisibility(visible = isResendVerificationMailPromptVisible) {
                val temp = remember {
                    isResendVerificationMailPromptVisible
                }
                if (temp)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                    ) {
                        Text(
                            text = "Didn't Receive a mail? Send Again",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )

                        CustomButton(
                            text = sendVerificationMailTimer,
                            isEnabled = isResendMailEnabled,
                            onClick = resendButtonClicked,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(MaterialTheme.dimens.large2)
                        )
                    }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    name = "preview Day"
)
@Preview(
    name = "Preview Night",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {}
    )

    TestThem {
        EmailLogInScreenContent(
            email = "",
            password = "",
            autoFillEmail = autoFillEmail,
            autoFillPassword = autoFillEmail,
            autofill = null,
            onEmailChange = {},
            onPasswordChange = {},
            emailSupportingText = "",
            passwordSupportingText = "",
            isEmailError = false,
            isPasswordError = false,
            onDone = { /*TODO*/ },
            onForgotPassword = { /*TODO*/ },
            onSignUpClick = { /*TODO*/ },
            isLoading = false,
            passwordVisibility = false,
            changePasswordVisibility = {},
            isResendMailEnabled = true,
            isResendVerificationMailPromptVisible = true,
            sendVerificationMailTimer = 10,
            resendButtonClicked = {}
        )
    }
}