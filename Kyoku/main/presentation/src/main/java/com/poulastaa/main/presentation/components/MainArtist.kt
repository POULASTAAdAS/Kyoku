package com.poulastaa.main.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.FilterArtistIcon


@Composable
fun MainArtist(
    modifier: Modifier = Modifier,
    artist: UiPrevArtist,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = onClick
    ) {
        MainImageCard(
            errorIcon = FilterArtistIcon,
            url = artist.cover,
            contentDescription = stringResource(R.string.artist),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            MainArtist(
                modifier = Modifier.size(120.dp),
                artist = UiPrevArtist(
                    name = "Artist Name",
                )
            ) {}
        }
    }
}