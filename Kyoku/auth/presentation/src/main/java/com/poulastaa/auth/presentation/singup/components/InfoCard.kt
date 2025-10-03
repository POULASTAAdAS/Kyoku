package com.poulastaa.auth.presentation.singup.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import com.poulastaa.auth.presentation.intro.components.InNewOrOldUserCard
import com.poulastaa.auth.presentation.intro.components.LogInSingUpSwitcher
import com.poulastaa.auth.presentation.singup.EmailSingUpUiAction
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun InfoCard(
    state: EmailSingUpUiState,
    haptic: HapticFeedback,
    onAction: (EmailSingUpUiAction) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        AnimatedContent(
            state.isOldUser
        ) {
            when (it) {
                true -> InNewOrOldUserCard(
                    title = stringResource(R.string.sing_in),
                    content = stringResource(R.string.email_already_registered)
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(EmailSingUpUiAction.OnEmailLogInClick)
                }

                false -> LogInSingUpSwitcher(
                    type = stringResource(R.string.sing_in),
                    content = stringResource(R.string.already_have_an_account)
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(EmailSingUpUiAction.OnEmailLogInClick)
                }
            }
        }
    }
}
