package com.poulastaa.kyoku.presentation.screen.home_root.player

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallPlayer(
    brushColor: List<Color>,
    isLoading: Boolean,
    isPlaying: Boolean,
    playingData: PlayerSong,
    durationUpdate: Float,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    onDurationChange: (Float) -> Unit,
    playControl: (HomeRootUiEvent) -> Unit
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
                        .fillMaxWidth(.5f)
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
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PlayControlButton(
                        modifier = Modifier
                            .rotate(90f)
                            .size(40.dp),
                        icon = R.drawable.ic_next,
                        color = IconButtonDefaults.iconButtonColors(
                            contentColor = brushColor[0]
                        )
                    ) {
                        playControl.invoke(HomeRootUiEvent.PlayerUiEvent.Backward)
                    }



                    PlayControlButton(
                        modifier = Modifier
                            .size(60.dp),
                        icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                        color = IconButtonDefaults.iconButtonColors(
                            contentColor = brushColor[0]
                        )
                    ) {
                        playControl.invoke(HomeRootUiEvent.PlayerUiEvent.PlayPause)
                    }


                    PlayControlButton(
                        modifier = Modifier
                            .size(40.dp),
                        icon = R.drawable.ic_next,
                        color = IconButtonDefaults.iconButtonColors(
                            contentColor = brushColor[0]
                        )
                    ) {
                        playControl.invoke(HomeRootUiEvent.PlayerUiEvent.Forward)
                    }
                }
            }

            Slider(
                value = durationUpdate,
                onValueChange = onDurationChange,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = SliderDefaults.colors(
                    activeTrackColor = brushColor[0],
                    inactiveTrackColor = brushColor[1]
                ),
                valueRange = 0f..100f,
                thumb = {
                    SliderDefaults.Thumb(
                        modifier = Modifier.offset(x = 3.dp, y = 4.dp),
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        thumbSize = DpSize(15.dp, 10.dp),
                        colors = SliderColors(
                            thumbColor = brushColor[0],
                            activeTrackColor = brushColor[0],
                            activeTickColor = brushColor[1],
                            inactiveTickColor = Color.Unspecified,
                            disabledThumbColor = Color.Unspecified,
                            disabledActiveTickColor = Color.Unspecified,
                            disabledInactiveTrackColor = Color.Unspecified,
                            disabledInactiveTickColor = Color.Unspecified,
                            inactiveTrackColor = Color.Unspecified,
                            disabledActiveTrackColor = Color.Unspecified
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun PlayControlButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    color: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = color
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
                brushColor = listOf(
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.background,
                ),
                isPlaying = false,
                isLoading = false,
                durationUpdate = 2.6023964E-6f,
                playingData = PlayerSong(
                    title = "Title",
                    artist = listOf("artist1", "artist2")
                ),
                isDarkThem = isSystemInDarkTheme(),
                isCookie = false,
                header = "",
                onDurationChange = {}
            ) {}
        }
    }
}