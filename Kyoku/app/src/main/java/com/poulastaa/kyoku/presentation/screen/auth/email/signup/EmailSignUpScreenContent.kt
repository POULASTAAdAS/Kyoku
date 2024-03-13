package com.poulastaa.kyoku.presentation.screen.auth.email.signup

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun EmailSignUpScreenContent(
    email: String,
    password: String,
    userName: String,
    conformPassword: String,

    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    onConformPasswordChange: (String) -> Unit,

    emailSupportingText: String,
    passwordSupportingText: String,
    conformPasswordSupportingText: String,
    usernameSupportingText: String,

    isEmailError: Boolean,
    isPasswordError: Boolean,
    isConformPasswordError: Boolean,
    isUsernameError: Boolean,

    autoFillEmail: AutofillNode,
    autoFillPassword: AutofillNode,
    autoFillUserName: AutofillNode,
    autofill: Autofill?,

    passwordVisibility: Boolean,
    changePasswordVisibility: () -> Unit,

    isResendVerificationMailPromptVisible: Boolean,
    sendVerificationMailTimer: Int,
    isResendMailEnabled: Boolean,
    resendButtonClicked: () -> Unit,

    onLogInClick: () -> Unit,
    onDone: () -> Unit,
    isLoading: Boolean
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.dimens.medium1)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(2.1f / 3),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // logo
            Image(
                painter = painterResource(
                    id = if (isSystemInDarkTheme()) R.drawable.night_logo
                    else R.drawable.light_logo
                ),
                contentDescription = null,
                modifier = if (LocalConfiguration.current.screenWidthDp >= 659) Modifier
                else Modifier.size(120.dp) // fixed size for smaller screen
            )

            // email filed
            CustomTextFiled(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        autoFillEmail.boundingBox = it.boundsInRoot()
                    }
                    .onFocusChanged {
                        autofill?.run {
                            if (it.isFocused) requestAutofillForNode(autoFillEmail)
                            else cancelAutofillForNode(autoFillEmail)
                        }
                    },
                value = email,
                onValueChange = onEmailChange,
                onDone = {},
                isError = isEmailError,
                supportingText = emailSupportingText,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            // password filed
            CustomPasswordField(
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
                value = password,
                onValueChange = onPasswordChange,
                onDone = {},
                isError = isPasswordError,
                supportingText = passwordSupportingText,
                shape = MaterialTheme.shapes.small,
                trailingIcon = {
                    IconButton(onClick = changePasswordVisibility) {
                        Icon(
                            painter = if (passwordVisibility) painterResource(id = R.drawable.ic_visibility_off)
                            else painterResource(id = R.drawable.ic_visibility_on),
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            // username field
            CustomTextFiled(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        autoFillUserName.boundingBox = it.boundsInRoot()
                    }
                    .onFocusChanged {
                        autofill?.run {
                            if (it.isFocused) requestAutofillForNode(autoFillUserName)
                            else cancelAutofillForNode(autoFillUserName)
                        }
                    },
                value = userName,
                onValueChange = onUserNameChange,
                shape = MaterialTheme.shapes.small,
                label = "Username",
                onDone = {},
                isError = isUsernameError,
                supportingText = usernameSupportingText,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            //Conform password filed
            CustomPasswordField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = "Conform Password",
                value = conformPassword,
                onValueChange = onConformPasswordChange,
                onDone = {
                    focusManager.clearFocus()
                    onDone.invoke()
                },
                isError = isConformPasswordError,
                supportingText = conformPasswordSupportingText,
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {},
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f / 2),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Text(
                    text = " LogIn Here",
                    modifier = Modifier.clickable(
                        onClick = onLogInClick,
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(52.dp)
                        )
                    }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(
    name = "Preview Day"
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
        EmailSignUpScreenContent(
            email = "",
            password = "",
            userName = "",
            conformPassword = "",
            onEmailChange = {},
            onPasswordChange = {},
            onUserNameChange = {},
            onConformPasswordChange = {},
            emailSupportingText = "",
            passwordSupportingText = "",
            conformPasswordSupportingText = "",
            usernameSupportingText = "",
            isEmailError = false,
            isPasswordError = false,
            isConformPasswordError = false,
            isUsernameError = false,
            autoFillEmail = autoFillEmail,
            autoFillPassword = autoFillEmail,
            autoFillUserName = autoFillEmail,
            autofill = null,
            passwordVisibility = true,
            changePasswordVisibility = { /*TODO*/ },
            onLogInClick = { /*TODO*/ },
            onDone = { /*TODO*/ },
            isLoading = false,
            isResendMailEnabled = false,
            resendButtonClicked = {},
            isResendVerificationMailPromptVisible = true,
            sendVerificationMailTimer = 10
        )
    }
}