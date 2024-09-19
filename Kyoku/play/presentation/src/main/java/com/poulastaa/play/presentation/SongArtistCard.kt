package com.poulastaa.play.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReqUser
import com.poulastaa.play.presentation.song_artist.SongArtistUiArtist


@Composable
fun SongArtistCard(
    modifier: Modifier = Modifier,
    header: String,
    artist: SongArtistUiArtist,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .aspectRatio(1f),
            shape = MaterialTheme.shapes.extraSmall,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 7.dp
            )
        ) {
            AsyncImage(
                model = imageReqUser(
                    header = header,
                    url = artist.coverImage
                ),
                modifier = Modifier
                    .aspectRatio(1f),
                contentDescription = null,
            )
        }

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column {
            Text(
                text = artist.name,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small1))

            Text(
                text = artist.popularity.toString(),
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
            )
        }
    }
}