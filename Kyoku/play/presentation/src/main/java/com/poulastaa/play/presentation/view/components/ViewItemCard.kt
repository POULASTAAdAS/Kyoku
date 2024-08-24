package com.poulastaa.play.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.MoveIcon
import com.poulastaa.core.presentation.designsystem.ThreeDotIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReqSongCover
import com.poulastaa.core.presentation.ui.model.ViewUiSong

@Composable
fun ViewItemCard(
    modifier: Modifier = Modifier,
    header: String,
    song: ViewUiSong,
    onMove: () -> Unit,
    onThreeDotCLick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.small3)
            .height(80.dp),
    ) {
        Icon(
            imageVector = MoveIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(.8f),
            modifier = Modifier
                .fillMaxHeight()
                .clickable(
                    onClick = onMove,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
        )

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.aspectRatio(1f),
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                AsyncImage(
                    model = imageReqSongCover(
                        header = header,
                        url = song.coverImage
                    ),
                    modifier = Modifier
                        .aspectRatio(1f),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.8f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = song.name,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artist,
                    color = MaterialTheme.colorScheme.onBackground.copy(.7f),
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onThreeDotCLick) {
                Icon(
                    imageVector = ThreeDotIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(.6f)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            ViewItemCard(
                song = ViewUiSong(
                    name = "That cool song",
                    artist = "That cool artist"
                ),
                onMove = {},
                header = ""
            ) {}
        }
    }
}