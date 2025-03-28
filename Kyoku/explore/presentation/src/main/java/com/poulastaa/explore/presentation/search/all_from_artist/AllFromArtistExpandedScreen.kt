package com.poulastaa.explore.presentation.search.all_from_artist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ui.FilterAlbumIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.explore.presentation.search.all_from_artist.components.AllFromArtistTopBar
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllFromArtistExpandedScreen(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    state: AllFromArtistUiState,
    song: LazyPagingItems<AllFromArtistUiItem>,
    album: LazyPagingItems<AllFromArtistUiItem>,
    onAction: (AllFromArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            if (state.loadingType is LoadingType.Content) AllFromArtistTopBar(
                modifier = modifier,
                scroll = scroll,
                artist = state.artist.name,
                isSearchOpen = state.isSearchOpen,
                query = state.query.value,
                onQueryChange = { onAction(AllFromArtistUiAction.OnSearchQueryChange(it)) },
                navigateBack = {
                    when {
                        state.isSearchOpen &&
                                state.query.value.isNotEmpty() -> onAction(AllFromArtistUiAction.OnSearchQueryClear)

                        state.isSearchOpen -> onAction(AllFromArtistUiAction.OnToggleSearch)
                        else -> navigateBack()
                    }
                },
                onSearch = {
                    focusManager.clearFocus()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DummySearch(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .height(38.dp),
                isSearchOpen = state.isSearchOpen,
                onAction = onAction
            )

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(.5f)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.album),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    items(album.itemCount) { index ->
                        Spacer(Modifier.height(MaterialTheme.dimens.small3))

                        album[index]?.let { album ->
                            Spacer(Modifier.height(MaterialTheme.dimens.small2))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier.aspectRatio(1f),
                                    shape = MaterialTheme.shapes.extraSmall,
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    SubcomposeAsyncImage(
                                        model = CacheImageReq.imageReq(
                                            album.poster,
                                            LocalContext.current
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds,
                                        loading = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.fillMaxSize(.4f),
                                                    strokeWidth = 2.dp,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        error = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = FilterAlbumIcon,
                                                    contentDescription = null,
                                                    modifier = Modifier.fillMaxSize(.7f),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    )
                                }

                                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(.8f),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(
                                        text = album.title,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        text = album.artist,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                                    )
                                }

                                Spacer(Modifier.weight(1f))

                                IconButton(
                                    onClick = {
                                        onAction(AllFromArtistUiAction.OnAlbumClick(album.id))
                                    }
                                ) {
                                    Icon(
                                        imageVector = ArrowBackIcon,
                                        contentDescription = null,
                                        modifier = Modifier.rotate(180f)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.width(MaterialTheme.dimens.small1))
                Spacer(
                    Modifier
                        .fillMaxHeight()
                        .width(MaterialTheme.dimens.small1)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(bottom = MaterialTheme.dimens.medium1)
                )
                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.songs),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    items(song.itemCount) { index ->
                        Spacer(Modifier.height(MaterialTheme.dimens.small3))

                        song[index]?.let { song ->
                            Spacer(Modifier.height(MaterialTheme.dimens.small2))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier.aspectRatio(1f),
                                    shape = MaterialTheme.shapes.extraSmall,
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    SubcomposeAsyncImage(
                                        model = CacheImageReq.imageReq(
                                            song.poster,
                                            LocalContext.current
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds,
                                        loading = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.fillMaxSize(.4f),
                                                    strokeWidth = 2.dp,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                        error = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = FilterAlbumIcon,
                                                    contentDescription = null,
                                                    modifier = Modifier.fillMaxSize(.7f),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    )
                                }

                                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(
                                        text = song.title,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        text = song.artist,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    widthDp = 1024,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 1280,
    heightDp = 740
)
@Composable
private fun Preview() {
    var search by remember { mutableStateOf(false) }

    AppThem {
        Surface {
            AllFromArtistExpandedScreen(
                modifier = Modifier.fillMaxWidth(.7f),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = AllFromArtistUiState(
                    loadingType = LoadingType.Content,
                    artist = UiPrevArtist(
                        name = "That Cool Artist",
                    ),
                    isSearchOpen = search,
                ),
                song = flowOf(PagingData.from((1..10).map {
                    AllFromArtistUiItem(
                        id = it.toLong(),
                        title = "That Cool Song",
                        artist = (1..3).joinToString(",") { "Artist $it" }
                    )
                })).collectAsLazyPagingItems(),
                album = flowOf(PagingData.from((1..5).map {
                    AllFromArtistUiItem(
                        id = it.toLong(),
                        title = "That Cool Album",
                        artist = (1..3).joinToString(",") { "Artist $it" }
                    )
                })).collectAsLazyPagingItems(),
                onAction = {
                    if (it == AllFromArtistUiAction.OnToggleSearch) search = !search
                },
                navigateBack = {}
            )
        }
    }
}