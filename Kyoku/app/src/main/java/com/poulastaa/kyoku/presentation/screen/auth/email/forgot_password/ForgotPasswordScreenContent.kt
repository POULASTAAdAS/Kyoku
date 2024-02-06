package com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.presentation.screen.common.CustomOkButton
import com.poulastaa.kyoku.presentation.screen.common.CustomTextFiled

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
    onDone: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding() + 40.dp,
                    bottom = it.calculateBottomPadding(),
                    start = 15.dp,
                    end = 15.dp
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
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomOkButton(
                text = "get email",
                modifier = Modifier.fillMaxWidth(),
                loading = isLoading,
                shape = RoundedCornerShape(8.dp),
                onClick = onDone
            )
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

    ForgotPasswordScreenContent(
        email = "",
        autoFillEmail = autoFillEmail,
        autofill = null,
        onValueChange = {},
        isError = false,
        emailSupportingText = "",
        isLoading = false,
        onNavigateBack = {}
    ) {

    }
}