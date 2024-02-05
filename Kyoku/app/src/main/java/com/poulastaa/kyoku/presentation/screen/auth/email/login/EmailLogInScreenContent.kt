package com.poulastaa.kyoku.presentation.screen.auth.email.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.presentation.screen.common.CustomTextFiled
import com.poulastaa.kyoku.presentation.screen.common.CustomOkButton
import com.poulastaa.kyoku.presentation.screen.common.CustomPasswordField

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
    isLoading: Boolean
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // logo
        Image(
            painter = if (isSystemInDarkTheme()) painterResource(id = R.drawable.night_logo)
            else painterResource(id = R.drawable.light_logo),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(40.dp))

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
            onDone = {
                focusManager.clearFocus()
                onDone.invoke()
            },
            isError = isPasswordError,
            supportingText = passwordSupportingText,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                IconButton(onClick = changePasswordVisibility) {
                    Icon(
                        painter = if (passwordVisibility) painterResource(id = R.drawable.ic_visibility_off)
                        else painterResource(id = R.drawable.ic_visibility_on),
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.clickable(
                    onClick = onForgotPassword,
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ),
                text = "Forgot Password ?",
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomOkButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                shape = RoundedCornerShape(8.dp),
                text = "C O N T I N U E",
                loading = isLoading,
                onClick = {
                    focusManager.clearFocus()
                    onDone.invoke()
                }
            )

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
                        interactionSource = MutableInteractionSource()
                    ),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {}
    )

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
        changePasswordVisibility = {}
    )
}