package com.poulastaa.auth.presentation.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.auth.presentation.components.ArchedScreen
import com.poulastaa.auth.presentation.intro.components.AuthEmailTextField
import com.poulastaa.auth.presentation.intro.components.ConformButton
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.ArrowBackIcon
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ForgotPasswordCompactScreen(
    modifier: Modifier = Modifier,
    state: ForgotPasswordUiState,
    onAction: (action: ForgotPasswordUiAction) -> Unit,
) {
    val focus = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(ForgotPasswordUiAction.OnNavigateBack)
                        }
                    ) {
                        Icon(
                            imageVector = ArrowBackIcon,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        ArchedScreen(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(MaterialTheme.dimens.medium1)
                .navigationBarsPadding()
                .systemBarsPadding()
                .then(modifier)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.reset_password),
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(MaterialTheme.dimens.small1))

                Text(
                    text = stringResource(R.string.reset_password_subtitle),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    lineHeight = MaterialTheme.typography.bodySmall.fontSize
                )

                Spacer(Modifier.weight(.1f))

                AuthEmailTextField(
                    email = state.email,
                    onEmailChange = {
                        onAction(ForgotPasswordUiAction.OnEmailChange(it))
                    },
                    onMoveFocus = {
                        focus.moveFocus(FocusDirection.Enter)
                        onAction(ForgotPasswordUiAction.OnSummit)
                    }
                )

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                ConformButton(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .align(Alignment.CenterHorizontally),
                    isLoading = state.isLoading,
                    heading = if (state.isVerifyButtonEnabled) stringResource(R.string.verify_text)
                    else stringResource(R.string.continue_text),
                    onClick = {
                        onAction(ForgotPasswordUiAction.OnSummit)
                    }
                )

                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@PreviewCompactPortrait
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        ForgotPasswordCompactScreen(
            state = ForgotPasswordUiState()
        ) { }
    }
}