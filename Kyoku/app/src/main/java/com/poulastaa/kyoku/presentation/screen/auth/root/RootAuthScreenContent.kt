package com.poulastaa.kyoku.presentation.screen.auth.root

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomTextFiled
import com.poulastaa.kyoku.presentation.screen.auth.common.IconTextButton
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

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
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(MaterialTheme.dimens.medium1)
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f / 2),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(
                    id = if (isSystemInDarkTheme()) R.drawable.night_logo
                    else R.drawable.light_logo
                ),
                contentDescription = null
            )

            Text(
                text = "Listen to Millions of songs.\n" +
                        "Add Free on Kyoku.",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
                softWrap = true,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1.1f / 2),
            verticalArrangement = Arrangement.Center
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
                shape = MaterialTheme.shapes.large,
                value = email,
                onValueChange = onValueChange,
                onDone = passkeySignUpClicked,
                isError = isError,
                supportingText = supportingText
            )


            FilledIconButton(
                onClick = passkeySignUpClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = MaterialTheme.dimens.large2),
                shape = MaterialTheme.shapes.medium
            ) {
                if (passkeyContinueLoading)
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                else
                    Text(
                        text = "Continue With Passkey",
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            IconTextButton(
                icon = painterResource(id = R.drawable.google),
                text = "Continue With Google",
                modifier = Modifier.fillMaxWidth(),
                isLoading = googleAuthLoading,
                onClick = googleSignupClicked
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            IconTextButton(
                icon = painterResource(id = R.drawable.email),
                text = "Continue With Email",
                modifier = Modifier.fillMaxWidth(),
                isLoading = emailAuthLoading,
                onClick = emailSignUpClicked
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)

@Preview(
    name = "Preview Night",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewAll() {
    val autoFillEmail = AutofillNode(
        autofillTypes = listOf(AutofillType.EmailAddress),
        onFill = {

        }
    )

    val autoFill = LocalAutofill.current
    LocalAutofillTree.current += autoFillEmail

    TestThem {
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
}