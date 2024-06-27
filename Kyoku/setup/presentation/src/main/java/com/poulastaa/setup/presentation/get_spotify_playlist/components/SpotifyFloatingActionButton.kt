package com.poulastaa.setup.presentation.get_spotify_playlist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun SpotifyFloatingActionButton(
    isData: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.padding(MaterialTheme.dimens.medium1),
        onClick = onClick,
        shape = if (isExpanded) MaterialTheme.shapes.medium else MaterialTheme.shapes.small,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp
        )
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.dimens.small1),
            text = if (isData) stringResource(id = R.string.continue_text)
            else stringResource(id = R.string.skip),
            letterSpacing = 2.sp,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppThem {
        SpotifyFloatingActionButton(
            isData = true,
            isExpanded = false
        ) {

        }
    }
}