package com.poulastaa.auth.presentation.reset_password.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poulastaa.auth.presentation.components.AuthPasswordTextFiled
import com.poulastaa.auth.presentation.reset_password.ResetPasswordUiAction
import com.poulastaa.auth.presentation.reset_password.ResetPasswordUiState
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun CreateNewPasswordCard(
    modifier: Modifier,
    state: ResetPasswordUiState,
    onAction: (ResetPasswordUiAction) -> Unit,
    haptic: HapticFeedback,
    focus: FocusManager,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
        ) {
            Spacer(Modifier.height(MaterialTheme.dimens.small2))

            AuthPasswordTextFiled(
                password = state.password,
                onPasswordChange = { onAction(ResetPasswordUiAction.OnPasswordChange(it)) },
                onVisibilityToggle = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(ResetPasswordUiAction.OnPasswordVisibilityToggle)
                },
                onSubmit = {
                    focus.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small1))

            AuthPasswordTextFiled(
                password = state.conformPassword,
                heading = stringResource(R.string.conform_password),
                onPasswordChange = {
                    onAction(ResetPasswordUiAction.OnConformPasswordChange(it))
                },
                onVisibilityToggle = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(ResetPasswordUiAction.OnConformPasswordVisibilityToggle)
                },
                onSubmit = {
                    focus.clearFocus()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(ResetPasswordUiAction.OnSummit)
                }
            )
        }
    }
}