package com.poulastaa.auth.presentation.reset_password

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.auth.presentation.components.ConformButton
import com.poulastaa.auth.presentation.reset_password.components.CreateNewPasswordCard
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.ArrowBackIcon
import com.poulastaa.core.presentation.ui.PreviewCompactLandscape
import com.poulastaa.core.presentation.ui.PreviewLargeLandscape
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ResetPasswordHorizontalCompactScreen(
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
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
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
                    }
                },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1,
                    bottom = MaterialTheme.dimens.medium1
                )
                .padding(paddingValues)
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CreateNewPasswordCard(
                Modifier.fillMaxWidth(.5f),
                state,
                onAction,
                haptic,
                focus
            )

            Spacer(Modifier.height(MaterialTheme.dimens.medium3))

            ConformButton(
                modifier = Modifier.fillMaxWidth(.2f),
                isLoading = state.isLoading,
                heading = stringResource(R.string.reset),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(ResetPasswordUiAction.OnSummit)
                }
            )
        }
    }
}

@PreviewCompactLandscape
@PreviewLargeLandscape
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        val haptic = LocalHapticFeedback.current

        ResetPasswordHorizontalCompactScreen(
            state = ResetPasswordUiState(),
            haptic = haptic,
            onAction = {}
        )
    }
}