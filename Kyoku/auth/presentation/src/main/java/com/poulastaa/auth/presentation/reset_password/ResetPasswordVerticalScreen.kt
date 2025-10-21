package com.poulastaa.auth.presentation.reset_password

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.auth.presentation.components.ArchedScreen
import com.poulastaa.auth.presentation.intro.components.AuthPasswordTextFiled
import com.poulastaa.auth.presentation.intro.components.ConformButton
import com.poulastaa.auth.presentation.reset_password.components.CreateNewPasswordCard
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.ArrowBackIcon
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait
import com.poulastaa.core.presentation.ui.PreviewLargePortrait
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ResetPasswordVerticalScreen(
    modifier: Modifier = Modifier,
    state: ResetPasswordUiState,
    haptic: HapticFeedback,
    onAction: (ResetPasswordUiAction) -> Unit,
) {
    val focus = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(ResetPasswordUiAction.OnNavigateBack) }
                    ) {
                        Icon(
                            imageVector = ArrowBackIcon,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary,
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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.medium1)
                    .then(modifier),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.create_new_password),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = stringResource(R.string.create_new_password_sub_heading),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                )

                Spacer(Modifier.height(MaterialTheme.dimens.large1))

                CreateNewPasswordCard(
                    Modifier.fillMaxWidth(),
                    state,
                    onAction,
                    haptic,
                    focus
                )

                Spacer(Modifier.height(MaterialTheme.dimens.medium3))

                ConformButton(
                    modifier = Modifier.fillMaxWidth(.3f),
                    isLoading = state.isLoading,
                    heading = stringResource(R.string.reset),
                    onClick = {
                        focus.clearFocus()
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAction(ResetPasswordUiAction.OnSummit)
                    }
                )
            }
        }
    }
}


@PreviewCompactPortrait
@PreviewLargePortrait
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        val haptic = LocalHapticFeedback.current

        ResetPasswordVerticalScreen(
            state = ResetPasswordUiState(),
            haptic = haptic,
            onAction = {}
        )
    }
}