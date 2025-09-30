package com.poulastaa.auth.presentation.intro.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.poulastaa.auth.presentation.intro.IntroUiAction
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens


@Composable
internal fun DontHaveAnAccount(
    haptic: HapticFeedback,
    onAction: (IntroUiAction.OnEmailSingUpClick) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.small3),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.dont_have_an_account),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(Modifier.width(MaterialTheme.dimens.small2))

        Text(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    onAction(IntroUiAction.OnEmailSingUpClick)
                }
            ),
            text = stringResource(R.string.singup_button_text),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.primary
        )
    }
}