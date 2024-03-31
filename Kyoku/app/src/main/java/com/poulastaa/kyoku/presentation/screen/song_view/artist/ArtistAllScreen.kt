package com.poulastaa.kyoku.presentation.screen.song_view.artist

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistAlbum
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.ArtistAllUiEvent
import com.poulastaa.kyoku.presentation.screen.song_view.artist.components.ArtistAllItem
import com.poulastaa.kyoku.presentation.screen.song_view.artist.components.Header
import com.poulastaa.kyoku.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistAllScreen(
    name: String,
    isFromMore: Boolean,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(
            rememberTopAppBarState()
        ),
    viewModel: ArtistAllViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    navigateBack: () -> Unit,
    navigate: (UiEvent) -> Unit
) {
    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navigate.invoke(event)
                is UiEvent.NavigateWithData -> navigate.invoke(event)
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadAlbum(name)
        viewModel.loadSong(name)
    }

    val album = viewModel.albums.collectAsLazyPagingItems()
    val song = viewModel.songs.collectAsLazyPagingItems()

    if (album.itemCount == 0) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = name,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = MaterialTheme.dimens.medium1)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = navigateBack,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                contentPadding = PaddingValues(
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1,
                    bottom = MaterialTheme.dimens.medium1
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isFromMore)
                    album(album, viewModel, isDarkThem, false)

                song(song, viewModel, isDarkThem, isFromMore)

                if (isFromMore)
                    album(album, viewModel, isDarkThem, true)
            }
        }
}

private fun LazyListScope.album(
    album: LazyPagingItems<ArtistAlbum>,
    viewModel: ArtistAllViewModel,
    isDarkThem: Boolean,
    isFromMore: Boolean
) {
    item {
        if (!isFromMore) Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        else Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Header(modifier = Modifier.fillMaxWidth(), text = "Albums")

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
    }


    items(album.itemCount) {
        if (album[it]?.name != null)
            ArtistAllItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(color = MaterialTheme.colorScheme.background)
                    .clickable {
                        viewModel.onEvent(
                            ArtistAllUiEvent
                                .ItemClick
                                .AlbumClick(id = album[it]!!.id)
                        )
                    },
                isDarkThem = isDarkThem,
                title = album[it]!!.name,
                year = album[it]!!.year,
                coverImage = album[it]!!.coverImage,
                isCookie = viewModel.state.isCooke,
                headerValue = viewModel.state.headerValue
            )
    }
}

private fun LazyListScope.song(
    song: LazyPagingItems<SongPreview>,
    viewModel: ArtistAllViewModel,
    isDarkThem: Boolean,
    isFromMore: Boolean
) {
    item {
        if (isFromMore) Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        else Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Header(modifier = Modifier.fillMaxWidth(), text = "Songs")

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
    }

    items(song.itemCount) {
        if (song[it]?.coverImage != null)
            ArtistAllItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(color = MaterialTheme.colorScheme.background)
                    .clickable {
                        viewModel.onEvent(
                            ArtistAllUiEvent
                                .ItemClick
                                .SongClick(id = song[it]!!.id.toLong())
                        )
                    },
                isDarkThem = isDarkThem,
                title = song[it]!!.title,
                year = song[it]!!.year,
                coverImage = song[it]!!.coverImage,
                isCookie = viewModel.state.isCooke,
                headerValue = viewModel.state.headerValue
            )
    }
}