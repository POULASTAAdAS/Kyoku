package com.poulastaa.play.presentation.player.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.MoveIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.presentation.player.PlayerUiSong
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerSongCard(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    header: String,
    song: PlayerUiSong,
    onMove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .fillMaxWidth()
            .height(70.dp),
    ) {
        Icon(
            imageVector = MoveIcon,
            contentDescription = null,
            tint = colors[0],
            modifier = Modifier
                .fillMaxHeight()
                .combinedClickable(
                    indication = null,
                    interactionSource = null,
                    onClick = {},
                    onLongClick = onMove
                )
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageGrid(
                header = header,
                urls = listOf(song.coverImage),
                elevation = CardDefaults.cardElevation()
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.9f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.SemiBold,
                    color = colors[0],
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = song.artist,
                    color = colors[0],
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))


        }
    }
}