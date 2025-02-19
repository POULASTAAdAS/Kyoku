package com.poulastaa.setup.presentation.set_bdate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.setup.presentation.set_bdate.components.SetBDateContent

@Composable
internal fun SetBDateCompactScreen(
    state: SetBDateUiState,
    onAction: (SetBDateUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = ThemChanger.getGradiantBackground()
                    )
                )
                .padding(paddingValues)
                .padding(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SetBDateContent(onAction, haptic, state)
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            SetBDateCompactScreen(
                state = SetBDateUiState(
                    date = TextHolder(
                        value = "10-10-2024",
                    )
                ),
                onAction = { }
            )
        }
    }
}