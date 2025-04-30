package com.poulastaa.add.presentation.album.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.add.presentation.album.AddAlbumUiAction
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon

@Composable
internal fun AddAlbumSavedButton(
    isEditEnabled: Boolean,
    isSavingAlbums: Boolean,
    onAction: (AddAlbumUiAction.OnSaveClick) -> Unit,
) {
    AnimatedVisibility(
        isEditEnabled,
        enter = fadeIn(tween(600)),
        exit = fadeOut(tween(600)),
    ) {
        IconButton(
            onClick = {
                onAction(AddAlbumUiAction.OnSaveClick)
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.background,
                containerColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            if (isSavingAlbums) CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize(.45f),
                strokeWidth = 2.5.dp
            ) else Icon(
                imageVector = CheckIcon,
                contentDescription = null
            )
        }
    }
}