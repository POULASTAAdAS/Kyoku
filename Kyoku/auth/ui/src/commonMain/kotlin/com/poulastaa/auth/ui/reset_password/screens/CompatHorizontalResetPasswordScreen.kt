package com.poulastaa.auth.ui.reset_password.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import com.poulastaa.auth.ui.reset_password.components.ResetPasswordButton
import com.poulastaa.auth.ui.reset_password.components.ResetPasswordFields
import com.poulastaa.auth.ui.reset_password.components.ResetPasswordHeader
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordUiState
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordViewmodel
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.dimens

@Composable
internal fun CompatHorizontalResetPasswordScreen(
    state: ResetPasswordUiState,
    viewmodel: ResetPasswordViewmodel,
    focusManager: FocusManager,
    haptic: HapticFeedback,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(MaterialTheme.dimens.layout.contentPadding)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ResetPasswordHeader(state)

            ColumnSpacer(MaterialTheme.dimens.spacing.medium)

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.layout.paneSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(0.4f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ResetPasswordButton(state, viewmodel, focusManager)
                }

                // Two fields with supporting text overflow a landscape phone's height, so this pane scrolls.
                Column(
                    modifier = Modifier.weight(0.6f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    ResetPasswordFields(state, viewmodel, focusManager, haptic)
                }
            }
        }
    }
}
