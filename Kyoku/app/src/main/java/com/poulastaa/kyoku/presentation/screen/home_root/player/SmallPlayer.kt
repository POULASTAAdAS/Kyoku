package com.poulastaa.kyoku.presentation.screen.home_root.player

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.player.PlayerControllerOperation
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun SmallPlayer(
    isPlaying: Boolean,
    hasNext: Boolean,
    hasPrev: Boolean,
    brushColor: List<Color>,
    isLoading: Boolean,
    playingData: PlayerSong,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    playerControllerOperation: (PlayerControllerOperation) -> Unit
) {
    if (isLoading) CircularProgressIndicator()
    else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(.87f)
                    .fillMaxWidth()
            ) {
                CustomImageView(
                    modifier = Modifier
                        .fillMaxWidth(.18f)
                        .clip(MaterialTheme.shapes.small),
                    isDarkThem = isDarkThem,
                    isCookie = isCookie,
                    headerValue = header,
                    url = playingData.url
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(.45f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = playingData.title,
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (isDarkThem) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                    )

                    Text(
                        text = playingData.artist.toString().trimStart('[').trimEnd(']'),
                        fontWeight = FontWeight.Light,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (isDarkThem) MaterialTheme.colorScheme.onBackground else Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    PlayControlButton(
                        modifier = Modifier
                            .rotate(90f)
                            .size(35.dp),
                        icon = R.drawable.ic_next,
                        color = IconButtonDefaults.iconButtonColors(
                            contentColor = brushColor[0]
                        ),
                        enabled = hasPrev
                    ) {
                        playerControllerOperation.invoke(PlayerControllerOperation.PLAY_PREV)
                    }

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                    PlayControlButton(
                        modifier = Modifier
                            .size(50.dp),
                        icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                        color = IconButtonDefaults.iconButtonColors(
                            contentColor = brushColor[0]
                        ),
                    ) {
                        if (isPlaying) playerControllerOperation.invoke(PlayerControllerOperation.PAUSE)
                        else playerControllerOperation.invoke(PlayerControllerOperation.PLAY)
                    }

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                    PlayControlButton(
                        modifier = Modifier
                            .size(35.dp),
                        icon = R.drawable.ic_next,
                        color = IconButtonDefaults.iconButtonColors(
                            contentColor = brushColor[0]
                        ),
                        enabled = hasNext
                    ) {
                        playerControllerOperation.invoke(PlayerControllerOperation.PLAY_NEXT)
                    }

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))
                }
            }

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { .3f },
                strokeCap = StrokeCap.Round,
                color = brushColor[1],
                trackColor = brushColor[0]
            )
        }
    }
}

@Composable
fun PlayControlButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    color: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = color,
        enabled = enabled
    ) {
        Icon(
            modifier = modifier,
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.background,
                        )
                    )
                )
                .padding(MaterialTheme.dimens.small1),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SmallPlayer(
                isPlaying = false,
                hasNext = false,
                hasPrev = false,
                brushColor = listOf(
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.background,
                ),
                isLoading = false,
                playingData = PlayerSong(
                    title = "Title",
                    artist = listOf("artist1", "artist2")
                ),
                isDarkThem = isSystemInDarkTheme(),
                isCookie = false,
                header = ""
            ) {}
        }
    }
}