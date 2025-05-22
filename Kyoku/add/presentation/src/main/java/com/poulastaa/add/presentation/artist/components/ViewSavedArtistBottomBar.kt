package com.poulastaa.add.presentation.artist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.add.presentation.artist.AddArtistUiAction
import com.poulastaa.add.presentation.artist.UiArtist
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun ViewSavedArtistBottomBar(
    list: List<UiArtist>,
    onAction: (AddArtistUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    ModalBottomSheet(
        onDismissRequest = {
            onAction(AddArtistUiAction.OnViewSelectedToggle)
        },
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = {
            Box(
                Modifier
                    .padding(MaterialTheme.dimens.medium1)
                    .clip(CircleShape)
                    .fillMaxWidth(.3f)
                    .height(5.dp)
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
        ) {

        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        ViewSavedArtistBottomBar(
            list = (1..10).map {
                UiArtist(name = "That Cool Artist")
            },
            onAction = {}
        )
    }
}