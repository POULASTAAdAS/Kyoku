package com.poulastaa.play.presentation.create_playlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistPagingUiData
import com.poulastaa.play.presentation.root_drawer.home.components.CircularArtist

@Composable
fun CreatePlaylistArtistCard(
    modifier: Modifier = Modifier,
    header: String,
    data: CreatePlaylistPagingUiData
) {
    Row(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularArtist(
            header = header,
            url = data.coverImage,
            modifier = Modifier.aspectRatio(1f)
        )

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Text(
            text = data.title,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = ArrowDownIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(.7f),
            modifier = Modifier.rotate(-90f)
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            CreatePlaylistArtistCard(
                modifier = Modifier
                    .clickable { }
                    .padding(MaterialTheme.dimens.small2),
                header = "",
                data = CreatePlaylistPagingUiData(
                    id = 1,
                    title = "that cool title",
                    artist = "",
                    coverImage = "",
                    expandable = true,
                    isArtist = true
                )
            )
        }
    }
}