package com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomTextFiled
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreenContent(
    email: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    isLoading: Boolean,
    autoFillEmail: AutofillNode,
    autofill: Autofill?,
    emailSupportingText: String,
    onNavigateBack: () -> Unit,
    isEnabled: Boolean,
    enableTimer: Int,
    emailSendText: String,
    onDone: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    FilledIconButton(
                        onClick = onNavigateBack,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.inversePrimary
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + MaterialTheme.dimens.large1,
                    bottom = paddingValues.calculateBottomPadding(),
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextFiled(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        autoFillEmail.boundingBox = it.boundsInRoot()
                    }
                    .onFocusChanged {
                        autofill?.run {
                            if (it.isFocused) requestAutofillForNode(autoFillEmail) else cancelAutofillForNode(
                                autoFillEmail
                            )
                        }
                    },
                value = email,
                onValueChange = onValueChange,
                onDone = onDone,
                isError = isError,
                supportingText = emailSupportingText,
                shape = MaterialTheme.shapes.small
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

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
                        text = if (isEnabled) "Get Email" else "$enableTimer",
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium3))

            AnimatedVisibility(visible = !isEnabled) {
                val temp = remember {
                    !isEnabled
                }

                if (temp)
                    Text(
                        text = emailSendText,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
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
        ForgotPasswordScreenContent(
            email = "",
            autoFillEmail = autoFillEmail,
            autofill = null,
            onValueChange = {},
            isError = false,
            emailSupportingText = "",
            isLoading = false,
            onNavigateBack = {},
            isEnabled = false,
            enableTimer = 10,
            emailSendText = "An Password reset mail is sent to you\n" +
                    "Please Change The Password and login again"
        ) {

        }
    }
}