package com.poulastaa.auth.ui.otp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
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
internal fun CompatHorizontalOtpScreen(
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
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(horizontal = MaterialTheme.dimens.layout.contentPadding)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.layout.paneSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(0.4f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OtpEmailMessage(email)
            }

            Column(
                modifier = Modifier.weight(0.6f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OtpField(otp, isOTPError, isValidating, focusManager, onOtpChange)

                ColumnSpacer(MaterialTheme.dimens.spacing.large)
                OtpSubmitButton(isValidOTP, isValidating, onValidateClick)
            }
        }
    }
}
