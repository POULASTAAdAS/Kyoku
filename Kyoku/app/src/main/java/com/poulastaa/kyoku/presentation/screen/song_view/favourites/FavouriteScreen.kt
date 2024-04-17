package com.poulastaa.kyoku.presentation.screen.song_view.favourites

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiModel
import com.poulastaa.kyoku.data.model.screens.song_view.UiPlaylistSong
import com.poulastaa.kyoku.presentation.screen.song_view.common.SongCard
import com.poulastaa.kyoku.presentation.screen.song_view.common.info
import com.poulastaa.kyoku.presentation.screen.song_view.common.navigateBackButton
import com.poulastaa.kyoku.presentation.screen.song_view.common.playControl
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens


@Composable
fun FavouriteScreen(
    data: SongViewUiModel,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    isSmallPhone: Boolean,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.inversePrimary,
        MaterialTheme.colorScheme.inversePrimary,
        MaterialTheme.colorScheme.secondary,
    ),
    navigateBack: () -> Unit
) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors
                    )
                )
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),

            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            navigateBackButton {
                navigateBack.invoke()
            }

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }

            item {
                Card(
                    modifier = Modifier
                        .size(if (isSmallPhone) 200.dp else 240.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .size(240.dp)
                            .background(
                                color = if (isDarkThem) MaterialTheme.colorScheme.tertiary
                                else MaterialTheme.colorScheme.tertiaryContainer
                            )
                            .padding(MaterialTheme.dimens.large1),
                        tint = if (isDarkThem) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.inversePrimary
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }

            info(
                name = "Favourites",
                size = data.listOfSong.size,
                totalTime = data.totalTime
            )

            playControl(
                isDownloading = false, // todo
                onDownloadClick = {

                },
                onShuffleClick = {

                },
                onPlayClick = {

                }
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }

            favouriteSongs(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                data = data.listOfSong,
                onSongClick = { id, name ->

                }
            )
        }
    }
}

private fun LazyListScope.favouriteSongs(
    data: List<UiPlaylistSong>,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    onSongClick: (id: Long, name: String) -> Unit
) {
    items(data.size) {
        SongCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(start = MaterialTheme.dimens.small3)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable { },
            isDarkThem = isDarkThem,
            isCookie = isCookie,
            headerValue = headerValue,
            title = data[it].title,
            artist = data[it].artist,
            coverImage = data[it].coverImage
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
//        val data = ArrayList<UiSong>()
//
//        for (i in 1..10) {
//            data.add(
//                UiSong(
//                    id = i.toLong(),
//                    title = "Title $i",
//                    artist = "Artist $i",
//                    album = "Album $i",
//                    coverImage = ""
//                )
//            )
//        }
//
//        FavouriteScreen(
//            data = data,
//            isDarkThem = isSystemInDarkTheme(),
//            isCookie = false,
//            headerValue = "",
//            isSmallPhone = false
//        ) {
//
//        }
    }
}