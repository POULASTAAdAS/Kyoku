package com.poulastaa.kyoku.presentation.screen.auth.root

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.presentation.screen.common.CustomAuthButton
import com.poulastaa.kyoku.presentation.screen.common.CustomOkButton
import com.poulastaa.kyoku.presentation.screen.common.CustomTextFiled

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RootAuthScreenContent(
    email: String,
    onValueChange: (String) -> Unit,
    autoFillEmail: AutofillNode,
    passkeyContinueLoading: Boolean,
    emailAuthLoading: Boolean,
    googleAuthLoading: Boolean,
    autoFill: Autofill?,
    isError: Boolean,
    supportingText: String,
    passkeySignUpClicked: () -> Unit,
    emailSignUpClicked: () -> Unit,
    googleSignupClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(.3f))

        TopPart(
            modifier = Modifier.weight(1f),
            logo = painterResource(
                id =
                if (isSystemInDarkTheme()) R.drawable.night_logo else R.drawable.light_logo
            ),
            content = "Listen to Millions of songs.\n" +
                    "Add Free on Kyoku."
        )

        MidPart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.9f),
            value = email,
            autoFillEmail = autoFillEmail,
            passkeyContinueLoading = passkeyContinueLoading,
            autoFill = autoFill,
            onValueChange = onValueChange,
            isError = isError,
            supportingText = supportingText,
            onDone = passkeySignUpClicked
        )

        EndPart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.4f),
            emailSignUpClicked = emailSignUpClicked,
            googleSignupClicked = googleSignupClicked,
            googleAuthLoading = googleAuthLoading,
            emailAuthLoading = emailAuthLoading
        )
    }
}

@Composable
fun TopPart(
    modifier: Modifier,
    logo: Painter,
    content: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = logo,
            contentDescription = "app logo"
        )

        Text(
            text = content,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp,
            softWrap = true,
            style = TextStyle(
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Medium
            )
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MidPart(
    modifier: Modifier,
    value: String,
    autoFillEmail: AutofillNode,
    passkeyContinueLoading: Boolean,
    autoFill: Autofill?,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    supportingText: String,
    onDone: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomTextFiled(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    autoFillEmail.boundingBox = it.boundsInRoot()
                }
                .onFocusChanged {
                    autoFill?.run {
                        if (it.isFocused) requestAutofillForNode(autoFillEmail)
                    }
                },
            value = value,
            onValueChange = onValueChange,
            onDone = onDone,
            isError = isError,
            supportingText = supportingText
        )

        CustomOkButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Continue With Passkey",
            onClick = onDone,
            loading = passkeyContinueLoading
        )
    }
}


@Composable
fun EndPart(
    modifier: Modifier,
    emailAuthLoading: Boolean,
    googleAuthLoading: Boolean,
    emailSignUpClicked: () -> Unit,
    googleSignupClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomAuthButton(
            modifier = Modifier.fillMaxWidth(),
            icon = R.drawable.google,
            text = "Continue With Google",
            onClick = googleSignupClicked,
            loading = googleAuthLoading
        )

        CustomAuthButton(
            modifier = Modifier.fillMaxWidth(),
            icon = R.drawable.email,
            text = "Continue With Email",
            onClick = emailSignUpClicked,
            loading = emailAuthLoading
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {

        }
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail

    RootAuthScreenContent(
        autoFillEmail = autoFillEmail,
        email = "",
        onValueChange = {},
        autoFill = autoFill,
        isError = false,
        supportingText = "",
        passkeySignUpClicked = { },
        emailSignUpClicked = { },
        googleSignupClicked = {},
        passkeyContinueLoading = false,
        googleAuthLoading = false,
        emailAuthLoading = false
    )
}