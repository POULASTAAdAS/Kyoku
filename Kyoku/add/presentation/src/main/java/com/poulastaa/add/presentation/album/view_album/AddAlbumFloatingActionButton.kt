package com.poulastaa.add.presentation.album.view_album

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.add.presentation.album.AddAlbumUiAction
import com.poulastaa.core.presentation.designsystem.R

@Composable
internal fun AddAlbumFloatingActionButton(
    size: Int,
    isEditEnabled: Boolean,
    onAction: (AddAlbumUiAction.OnViewSelectedToggle) -> Unit,
) {
    AnimatedVisibility(visible = isEditEnabled) {
        Button(
            onClick = {
                onAction(AddAlbumUiAction.OnViewSelectedToggle)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text(
                text = "$size ${stringResource(R.string.selected)}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}