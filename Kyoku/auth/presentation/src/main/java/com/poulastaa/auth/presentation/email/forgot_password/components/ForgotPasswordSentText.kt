package com.poulastaa.auth.presentation.email.forgot_password.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun ForgotPasswordSentText(
    isResendVerificationMailSent: Boolean,
) {
    AnimatedVisibility(visible = isResendVerificationMailSent) {
        Column {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.large1),
                text = stringResource(id = R.string.verification_mail_sent),
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))
        }
    }
}