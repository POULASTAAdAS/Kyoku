package com.poulastaa.play.presentation.player.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.poulastaa.core.presentation.designsystem.FavouriteIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiSong


@Composable
fun PlayerSongInfo(
    modifier: Modifier = Modifier,
    song: PlayerUiSong,
    onEvent: (PlayerUiEvent) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            Text(
                text = song.title,
                fontWeight = FontWeight.SemiBold,
                color = song.colors[0],
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = song.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = song.colors[0],
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {

            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.small1),
                imageVector = if (song.isInFavourite) FavouriteIcon
                else Icons.Rounded.FavoriteBorder,
                contentDescription = null,
            )
        }
    }
}
