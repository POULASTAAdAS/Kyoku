package com.poulastaa.play.presentation.create_playlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AddIcon
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.UiSong
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid

@Composable
fun CreatePlaylistSongCard(
    modifier: Modifier = Modifier,
    header: String,
    song: UiSong
) {
    Row(
        modifier = Modifier
            .height(90.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageGrid(
            header = header,
            urls = listOf(song.coverImage),
            elevation = CardDefaults.cardElevation()
        )

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier.fillMaxWidth(.85f)
        ) {
            Text(
                text = song.title,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = song.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground.copy(.6f)
            )
        }

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = AddIcon,
            contentDescription = null,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                    shape = CircleShape
                ),
            tint = MaterialTheme.colorScheme.onBackground.copy(.7f)
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
                .padding(MaterialTheme.dimens.medium1)
        ) {
            CreatePlaylistSongCard(
                modifier = Modifier.clickable { },
                header = "",
                song = UiSong(
                    id = 1,
                    title = "that cool title",
                    artist = "that cool artist",
                    coverImage = ""
                )
            )
        }
    }
}