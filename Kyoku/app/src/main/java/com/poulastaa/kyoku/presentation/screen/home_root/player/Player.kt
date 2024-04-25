package com.poulastaa.kyoku.presentation.screen.home_root.player

import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens


@Composable
fun Player(
    isSmallPhone: Boolean,
    paddingValue: PaddingValues,
    playerSong: PlayerSong,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    context: Context,
    brushColor: List<Color> = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.background,
    ),
    navigateBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = brushColor
                )
            )
            .padding(
                top = paddingValue.calculateTopPadding(),
                bottom = paddingValue.calculateBottomPadding(),
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = navigateBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = brushColor[1],
                        contentColor = brushColor[0]
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }

        item {
            Card(
                modifier = Modifier.aspectRatio(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.small
            ) {
                CustomImageView(
                    modifier = Modifier.fillParentMaxSize(),
                    isDarkThem = isDarkThem,
                    isCookie = isCookie,
                    headerValue = header,
                    url = playerSong.url,
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = playerSong.title,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontWeight = FontWeight.Medium,
                        color = brushColor[0],
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = playerSong.artist.toString().trimStart('[').trimEnd(']'),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = brushColor[0],
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }


            }
        }

        item {
            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.large2)
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { .3f },
                    modifier = Modifier.fillMaxWidth(),
                    strokeCap = StrokeCap.Round,
                    color = brushColor[1],
                    trackColor = brushColor[0]
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "00.00",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = brushColor[0]
                    )
                    Text(
                        text = playerSong.totalTime,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = brushColor[0]
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.medium3)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomIconButton(
                    iconModifier = Modifier.size(35.dp),
                    icon = R.drawable.ic_shuffle,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {

                }

                CustomIconButton(
                    iconModifier = Modifier
                        .size(35.dp)
                        .rotate(180f),
                    icon = R.drawable.ic_next,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {

                }

                CustomIconButton(
                    buttonModifier = Modifier.size(70.dp),
                    iconModifier = Modifier.size(65.dp),
                    icon = R.drawable.ic_play,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {

                }

                CustomIconButton(
                    iconModifier = Modifier.size(35.dp),
                    icon = R.drawable.ic_next,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {

                }

                CustomIconButton(
                    iconModifier = Modifier.size(35.dp),
                    icon = R.drawable.ic_add_to_library,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = brushColor[0]
                    )
                ) {

                }
            }
        }
    }

    BackHandler {
        navigateBack.invoke()
    }
}


@Composable
fun CustomIconButton(
    buttonModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    IconButton(
        modifier = buttonModifier,
        onClick = onClick,
        colors = colors
    ) {
        Icon(
            modifier = iconModifier,
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
        Player(
            isSmallPhone = false,
            paddingValue = PaddingValues(),
            playerSong = PlayerSong(
                title = "Title",
                artist = listOf(
                    "artist1",
                    "artist2",
                    "artist1",
                    "artist2",
                    "artist1",
                    "artist2",
                    "artist1",
                    "artist2"
                ),
                totalTime = "4.40"
            ),
            isDarkThem = isSystemInDarkTheme(),
            isCookie = false,
            header = "",
            context = LocalContext.current,
            brushColor = listOf(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.background,
            )
        ) {

        }
    }
}