package com.poulastaa.settings.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.DayIcon
import com.poulastaa.core.presentation.designsystem.ui.NightIcon
import com.poulastaa.settings.presentation.SettingsUiAction


@Composable
internal fun ThemModeChanger(
    onAction: (SettingsUiAction.OnStartThemChange) -> Unit,
) {
    var offset: Offset = remember { Offset(0f, 0f) }

    Switch(
        modifier = Modifier.onGloballyPositioned {
            offset = Offset(
                x = it.positionInWindow().x + it.size.width / 2,
                y = it.positionInWindow().y + it.size.height / 2,
            )
        },
        checked = ThemModeChanger.themMode,
        onCheckedChange = {
            onAction(SettingsUiAction.OnStartThemChange(offset))
        },
        thumbContent = {
            AnimatedContent(
                ThemModeChanger.themMode,
                label = "switch thumb animation",
            ) {
                when (it) {
                    true -> Icon(
                        imageVector = NightIcon,
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp)
                    )

                    false -> Icon(
                        imageVector = DayIcon,
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        },
        colors = SwitchDefaults.colors(
            uncheckedBorderColor = MaterialTheme.colorScheme.primary,
            checkedBorderColor = MaterialTheme.colorScheme.primary,

            checkedThumbColor = MaterialTheme.colorScheme.primary,
            uncheckedThumbColor = MaterialTheme.colorScheme.primary,

            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer,

            checkedIconColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedIconColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}