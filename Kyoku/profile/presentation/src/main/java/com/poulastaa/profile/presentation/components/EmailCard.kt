package com.poulastaa.profile.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.core.presentation.designsystem.noRippleClickable
import com.poulastaa.core.presentation.designsystem.ui.EditIcon
import com.poulastaa.core.presentation.designsystem.ui.UserIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.profile.presentation.ProfileUiAction

@Composable
internal fun EmailCard(
    username: String,
    haptic: HapticFeedback,
    onAction: (ProfileUiAction.OnEditUserName) -> Unit,
) {
    ProfileOutlinedCard(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(MaterialTheme.dimens.medium1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = UserIcon,
                contentDescription = null
            )

            Spacer(Modifier.width(MaterialTheme.dimens.small3))

            Text(
                text = username,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = EditIcon,
                contentDescription = null,
                modifier = Modifier.noRippleClickable {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onAction(ProfileUiAction.OnEditUserName)
                }
            )
        }
    }
}