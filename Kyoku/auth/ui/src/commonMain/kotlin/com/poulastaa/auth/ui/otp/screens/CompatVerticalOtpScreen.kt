package com.poulastaa.auth.ui.otp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import com.poulastaa.auth.ui.otp.components.OtpEmailMessage
import com.poulastaa.auth.ui.otp.components.OtpField
import com.poulastaa.auth.ui.otp.components.OtpSubmitButton
import com.poulastaa.auth.ui.otp.components.OtpTopBar
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.dimens

@Composable
internal fun CompatVerticalOtpScreen(
    email: String,
    otp: String,
    isOTPError: Boolean,
    isValidating: Boolean,
    isValidOTP: Boolean,
    focusManager: FocusManager,
    onOtpChange: (String) -> Unit,
    onValidateClick: () -> Unit,
) {
    Scaffold(
        topBar = { OtpTopBar() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(horizontal = MaterialTheme.dimens.layout.contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OtpEmailMessage(email)

            ColumnSpacer(MaterialTheme.dimens.spacing.small)
            OtpField(otp, isOTPError, isValidating, focusManager, onOtpChange)

            ColumnSpacer(MaterialTheme.dimens.spacing.large)
            OtpSubmitButton(isValidOTP, isValidating, onValidateClick)
        }
    }
}
