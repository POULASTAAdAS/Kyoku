package com.poulastaa.kyoku.presentation.screen.home_root.library

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.library.LibraryUiEvent
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.FavouritePrev
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryScreenArtistGridView
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryScreenArtistListView
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryScreenPlaylistGridView
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryScreenPlaylistListView
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.headLineSeparator
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.largeSpace
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.libraryScreenItemHeading
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    isSmallPhone: Boolean,
    isCookie: Boolean,
    headerValue: String,
    context: Context,
    paddingValues: PaddingValues,
) {
    LaunchedEffect(key1 = viewModel.state.isInternetAvailable) {
        viewModel.loadData(context)
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {

                }

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

    if (
        (!viewModel.state.data.all.isFavourite &&
                viewModel.state.data.all.playlist.isEmpty() &&
                viewModel.state.data.all.artist.isEmpty()) ||
        viewModel.state.isLoading
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (viewModel.state.isGrid) 3 else 1),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
            contentPadding = PaddingValues(
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {

            // sort types : playlist , artist , album
            item(
                span = {
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(1f / 3)
                    ) {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                        // todo sort types :: playlist , artist , album
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                    }
                }
            }

            // list , grid sort icon
            item(
                span = {
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(LibraryUiEvent.SortTypeClick)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (viewModel.state.isGrid) R.drawable.list
                                else R.drawable.grid
                            ),
                            contentDescription = null
                        )
                    }
                }
            }


            // Toast
            item(
                span = {
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                }
            ) {
                AnimatedVisibility(
                    visible = viewModel.state.isInternetError,
                    enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandIn(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkOut()
                ) {
                    val temp = remember {
                        viewModel.state.isInternetError
                    }

                    if (temp) {
                        CustomToast(
                            message = viewModel.state.errorMessage,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }


            // pinned heading
            item(
                span = {
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                }
            ) {
                Text(
                    text = "Pinned",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Black,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
            }

            headLineSeparator(viewModel.state.isGrid)

            // pinned items
            item(
                span = {
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                }
            ) {
                if (
                    !viewModel.state.data.pinned.isFavourite ||
                    viewModel.state.data.pinned.playlist.isEmpty() ||
                    viewModel.state.data.pinned.artist.isEmpty()
                ) {
                    Text(
                        text = "Long press to pin",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Light,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary
                    )

                } else {
                    // todo
                }
            }


            largeSpace(
                GridItemSpan(
                    if (viewModel.state.isGrid) viewModel.state.maxGridSize
                    else viewModel.state.minGridSize
                )
            )

            // favourite
            if (viewModel.state.data.all.isFavourite) {
                item(
                    span = {
                        GridItemSpan(
                            if (viewModel.state.isGrid) viewModel.state.maxGridSize
                            else viewModel.state.minGridSize
                        )
                    }
                ) {
                    FavouritePrev(
                        modifier = Modifier
                            .fillMaxWidth(.2f)
                            .height(80.dp),
                        onClick = {

                        }
                    )
                }
            }

            largeSpace(
                GridItemSpan(
                    if (viewModel.state.isGrid) viewModel.state.maxGridSize
                    else viewModel.state.minGridSize
                )
            )

            // playlist
            if (viewModel.state.data.all.playlist.isNotEmpty()) {
                libraryScreenItemHeading(
                    heading = "Playlist",
                    isGrid = viewModel.state.isGrid,
                    onClick = {

                    }
                )

                headLineSeparator(viewModel.state.isGrid)

                items(viewModel.state.data.all.playlist.size) {
                    if (viewModel.state.isGrid)
                        LibraryScreenPlaylistGridView(
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.small1)
                                .size(if (isSmallPhone) 120.dp else 130.dp),
                            isCookie = isCookie,
                            authHeader = headerValue,
                            name = viewModel.state.data.all.playlist[it].name,
                            imageUrls = viewModel.state.data.all.playlist[it].listOfUrl,
                            onClick = {

                            }
                        )
                    else
                        LibraryScreenPlaylistListView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.dimens.small1)
                                .height(80.dp),
                            isCookie = isCookie,
                            authHeader = headerValue,
                            name = viewModel.state.data.all.playlist[it].name,
                            imageUrls = viewModel.state.data.all.playlist[it].listOfUrl,
                            onClick = {

                            }
                        )
                }
            }

            largeSpace(
                GridItemSpan(
                    if (viewModel.state.isGrid) viewModel.state.maxGridSize
                    else viewModel.state.minGridSize
                )
            )


            // artist
            if (viewModel.state.data.all.artist.isNotEmpty()) {
                libraryScreenItemHeading(
                    heading = "Artist",
                    isGrid = viewModel.state.isGrid,
                    onClick = {

                    }
                )

                headLineSeparator(viewModel.state.isGrid)

                items(viewModel.state.data.all.artist.size) {
                    if (viewModel.state.isGrid)
                        LibraryScreenArtistGridView(
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.small3)
                                .size(90.dp),
                            name = viewModel.state.data.all.artist[it].name,
                            imageUrl = viewModel.state.data.all.artist[it].imageUrl,
                            isCookie = isCookie,
                            headerValue = headerValue,
                            onClick = {

                            }
                        )
                    else
                        LibraryScreenArtistListView(
                            modifier = Modifier
                                .padding(MaterialTheme.dimens.small3)
                                .size(90.dp),
                            name = viewModel.state.data.all.artist[it].name,
                            imageUrl = viewModel.state.data.all.artist[it].imageUrl,
                            isCookie = isCookie,
                            headerValue = headerValue,
                            onClick = {

                            }
                        )
                }
            }
        }
    }
}



