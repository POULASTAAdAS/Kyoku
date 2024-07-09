package com.poulastaa.play.presentation.root_drawer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.domain.HomeToDrawerEvent
import com.poulastaa.play.presentation.root_drawer.home.components.GridImageCard
import com.poulastaa.play.presentation.root_drawer.home.components.HomeAlbumCard
import com.poulastaa.play.presentation.root_drawer.home.components.HomeAppbar
import com.poulastaa.play.presentation.root_drawer.home.components.MoreFromArtist
import com.poulastaa.play.presentation.root_drawer.home.components.SingleSongCard
import com.poulastaa.play.presentation.root_drawer.home.components.SuggestedArtistCard
import com.poulastaa.play.presentation.root_drawer.home.components.ViewMore
import com.poulastaa.play.presentation.root_drawer.home.model.UiArtistWithSong
import com.poulastaa.play.presentation.root_drawer.home.model.UiHomeData
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevSong
import com.poulastaa.play.presentation.root_drawer.home.model.UiSongWithInfo

@Composable
fun HomeCompactScreen(
    profileUrl: String,
    viewModel: HomeViewModel = hiltViewModel(),
    onEvent: (HomeToDrawerEvent) -> Unit,
) {
    HomeScreen(
        profileUrl = profileUrl,
        state = viewModel.state,
        onProfileClick = {
            onEvent(HomeToDrawerEvent.PROFILE_CLICK)
        },
        onSearchClick = {
            onEvent(HomeToDrawerEvent.SEARCH_CLICK)
        },
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
        topBar = {
            HomeAppbar(
                scrollBehavior = appBarScrollBehavior,
                title = state.heading,
                profileUrl = profileUrl,
                onProfileClick = onProfileClick,
                onSearchClick = onSearchClick
            )
        }
    ) {
        if (!state.canShowUi) Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(it),
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(70.dp),
                strokeCap = StrokeCap.Round,
                strokeWidth = 2.dp
            )
        }
        else LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }

            item {
                Text(
                    text = stringResource(id = R.string.explore_more),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    modifier = Modifier.windowInsetsPadding(
                        insets = WindowInsets(
                            left = MaterialTheme.dimens.medium1,
                        )
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .windowInsetsPadding(
                            insets = WindowInsets(
                                left = MaterialTheme.dimens.medium1,
                                right = MaterialTheme.dimens.medium1
                            )
                        ),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium2)
                ) {
                    GridImageCard(
                        size = 150.dp,
                        header = state.header,
                        songs = state.data.popularSongMix,
                        title = stringResource(id = R.string.popular_song_mix)
                    ) {

                    }

                    GridImageCard(
                        size = 150.dp,
                        header = state.header,
                        songs = state.data.popularSongFromYourTime,
                        title = stringResource(id = R.string.popular_songs_from_your_time)
                    ) {

                    }

                    GridImageCard(
                        size = 150.dp,
                        header = state.header,
                        songs = state.data.favouriteArtistMix,
                        title = stringResource(id = R.string.popular_artist_mix)
                    ) {

                    }
                }
            }

            item {}

            item {
                Text(
                    text = stringResource(id = R.string.artist_you_may_like),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    modifier = Modifier.windowInsetsPadding(
                        insets = WindowInsets(
                            left = MaterialTheme.dimens.medium1,
                        )
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    state.data.suggestedArtist.forEach { artist ->
                        SuggestedArtistCard(
                            modifier = Modifier.size(220.dp),
                            artist = artist,
                            header = state.header
                        )
                    }
                }
            }

            item {}

            item {
                Text(
                    text = stringResource(id = R.string.popular_albums),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    modifier = Modifier.windowInsetsPadding(
                        insets = WindowInsets(
                            left = MaterialTheme.dimens.medium1,
                        )
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .windowInsetsPadding(
                            insets = WindowInsets(
                                left = MaterialTheme.dimens.medium1,
                                right = MaterialTheme.dimens.medium1
                            )
                        ),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium2)
                ) {
                    state.data.popularAlbum.forEach { album ->
                        HomeAlbumCard(
                            modifier = Modifier.size(150.dp),
                            header = state.header,
                            prevAlbum = album,
                            onClick = {

                            }
                        )
                    }

                    ViewMore(
                        modifier = Modifier.size(150.dp)
                    )
                }
            }

            item {}

            item {
                Text(
                    text = stringResource(id = R.string.best_from_artist),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    modifier = Modifier.windowInsetsPadding(
                        insets = WindowInsets(
                            left = MaterialTheme.dimens.medium1,
                        )
                    )
                )
            }

            items(
                items = state.data.popularArtistSong,
                key = { artist ->
                    artist.artist.id
                }
            ) { songData ->
                MoreFromArtist(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(end = MaterialTheme.dimens.small2),
                    header = state.header,
                    artist = songData.artist
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .windowInsetsPadding(
                            insets = WindowInsets(
                                left = MaterialTheme.dimens.medium1,
                                right = MaterialTheme.dimens.medium1
                            )
                        ),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium2)
                ) {
                    songData.listOfSong.forEach { song ->
                        SingleSongCard(
                            modifier = Modifier.size(150.dp),
                            header = state.header,
                            song = song
                        )
                    }

                    ViewMore(
                        modifier = Modifier.size(150.dp)
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        HomeScreen(
            state = HomeUiState(
                heading = "Good Morning",
                isNewUser = false,
                isDataLoading = false,
                header = "",
                data = prevData
            ),
            profileUrl = "",
            onProfileClick = { /*TODO*/ },
            onSearchClick = { /*TODO*/ }) {}
    }
}

private val prevData = UiHomeData(
    popularSongMix = listOf(
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        )
    ),
    popularSongFromYourTime = listOf(
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        )
    ),
    favouriteArtistMix = listOf(
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        )
    ),
    dayTypeSong = listOf(
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        )
    ),
    popularAlbum = (1..7).map {
        UiPrevAlbum(
            id = it.toLong(),
            name = "Album $it",
            coverImage = ""
        )
    },
    suggestedArtist = (1..7).map {
        UiArtist(
            id = it.toLong(),
            name = "Artist $it",
            coverImageUrl = ""
        )
    },
    popularArtistSong = (1..4).map {
        UiArtistWithSong(
            UiArtist(
                id = it.toLong(),
                name = "Artist $it",
                coverImageUrl = ""
            ),
            listOfSong = (1..7).map {
                UiSongWithInfo(
                    id = it.toLong(),
                    title = "Song $it",
                    coverImage = ""
                )
            }
        )
    }
)