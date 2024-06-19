package com.poulastaa.auth.presentation.email.forgot_password.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.auth.presentation.email.components.AuthContinueButton
import com.poulastaa.auth.presentation.email.components.AuthTextField
import com.poulastaa.auth.presentation.email.forgot_password.ForgotPasswordUiEvent
import com.poulastaa.auth.presentation.email.forgot_password.ForgotPasswordUiState
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.BackButton
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun ColumnScope.ForgotPasswordCompact(
    state: ForgotPasswordUiState,
    onEvent: (ForgotPasswordUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    BackButton(
        modifier = Modifier.align(Alignment.Start)
    ) {
        onEvent(ForgotPasswordUiEvent.OnBackClick)
    }

    Spacer(modifier = Modifier.weight(1f))

    AuthTextField(
        text = state.email,
        onValueChange = { onEvent(ForgotPasswordUiEvent.OnEmailChange(it)) },
        label = stringResource(id = R.string.email),
        onDone = {
            focusManager.clearFocus()
            onEvent(ForgotPasswordUiEvent.OnSendClick)
        }
    )


    AnimatedVisibility(visible = state.isResendVerificationMailSent) {
        Column {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.large1),
                text = stringResource(id = R.string.verification_maail_sent),
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                color = MaterialTheme.colorScheme.primaryContainer,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))
        }
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

    AuthContinueButton(
        modifier = Modifier
            .fillMaxWidth(.7f)
            .padding(MaterialTheme.dimens.small1),
        text = if (state.canResendMail) stringResource(id = R.string.send) else "${state.resendMailCounter} s",
        enable = state.canResendMail,
        isLoading = state.isMakingApiCall,
        fontWeight = FontWeight.SemiBold,
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        onClick = {
            focusManager.clearFocus()
            onEvent(ForgotPasswordUiEvent.OnSendClick)
        }
    )

    Spacer(modifier = Modifier.weight(2f))
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onTertiary,
                            MaterialTheme.colorScheme.tertiary,
                        )
                    )
                )
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ForgotPasswordCompact(
                state = ForgotPasswordUiState(
                    isResendVerificationMailSent = true
                )
            ) {

            }
        }
    }
}