package com.poulastaa.auth.ui.reset_password.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import com.poulastaa.auth.ui.reset_password.components.ResetPasswordButton
import com.poulastaa.auth.ui.reset_password.components.ResetPasswordFields
import com.poulastaa.auth.ui.reset_password.components.ResetPasswordHeader
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordUiState
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordViewmodel
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.dimens

@Composable
internal fun CompatVerticalResetPasswordScreen(
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
                .padding(MaterialTheme.dimens.layout.contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ResetPasswordHeader(state)

            ColumnSpacer(MaterialTheme.dimens.spacing.extraLarge)

            ResetPasswordFields(state, viewmodel, focusManager, haptic)

            ColumnSpacer(MaterialTheme.dimens.spacing.extraLarge)

            ResetPasswordButton(state, viewmodel, focusManager)
        }
    }
}
