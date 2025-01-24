package com.poulastaa.setup.presentation.pic_artist

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.KyokuWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.setup.presentation.components.PicItemCompactScreen
import com.poulastaa.setup.presentation.components.PicItemExtendedScreen
import com.poulastaa.setup.presentation.pic_artist.components.ArtistCard
import com.poulastaa.setup.presentation.pic_genre.PicGenreUiAction
import com.poulastaa.setup.presentation.pic_genre.component.GenreCard

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun PicArtistRootScreen(
    viewmodel: PicArtistViewmodel = hiltViewModel(),
    navigateToHome: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val artist = viewmodel.artist.collectAsLazyPagingItems()
    val config = LocalConfiguration.current

    val windowSizeClass = calculateWindowSizeClass(activity)

    ObserveAsEvent(viewmodel.event) { event ->
        when (event) {
            is PicArtistUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            PicArtistUiEvent.OnSuccess -> navigateToHome()
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            PicItemCompactScreen(
                title = R.string.pic_artist_title,
                lessSelected = R.string.less_artist_selected,
                label = R.string.artist,
                gridSize = 3,
                query = state.artistQuery.value,
                isMinLimitReached = state.isMinLimitReached,
                isMakingApiCall = state.isMakingApiCall,
                data = artist,
                contentPadding = MaterialTheme.dimens.medium1,
                itemContent = { item ->
                    ArtistCard(
                        modifier = Modifier.fillMaxSize(),
                        artist = item,
                        onClick = {
                            viewmodel.onAction(PicArtistUiAction.OnArtistToggle(item.id))
                        }
                    )
                },
                onFloatingActionButtonClick = {
                    viewmodel.onAction(PicArtistUiAction.OnSubmit)
                },
                onQueryChange = { data ->
                    viewmodel.onAction(PicArtistUiAction.OnQueryChange(data))
                },
                onClearClick = {
                    viewmodel.onAction(PicArtistUiAction.OnQueryChange(""))
                }
            )
        },
        mediumContent = {
            PicItemCompactScreen(
                title = R.string.pic_artist_title,
                lessSelected = R.string.less_artist_selected,
                label = R.string.artist,
                gridSize = 4,
                query = state.artistQuery.value,
                isMinLimitReached = state.isMinLimitReached,
                isMakingApiCall = state.isMakingApiCall,
                data = artist,
                contentPadding = MaterialTheme.dimens.medium1,
                itemContent = { item ->
                    ArtistCard(
                        modifier = Modifier.fillMaxSize(),
                        artist = item,
                        onClick = {
                            viewmodel.onAction(PicArtistUiAction.OnArtistToggle(item.id))
                        }
                    )
                },
                onFloatingActionButtonClick = {
                    viewmodel.onAction(PicArtistUiAction.OnSubmit)
                },
                onQueryChange = { data ->
                    viewmodel.onAction(PicArtistUiAction.OnQueryChange(data))
                },
                onClearClick = {
                    viewmodel.onAction(PicArtistUiAction.OnQueryChange(""))
                }
            )
        },
        expandedContent = {
            if (config.screenWidthDp > 980) PicItemExtendedScreen(
                title = R.string.pic_artist_title,
                lessSelected = R.string.less_artist_selected,
                label = R.string.artist,
                gridSize = 5,
                query = state.artistQuery.value,
                isMinLimitReached = state.isMinLimitReached,
                isMakingApiCall = state.isMakingApiCall,
                data = artist,
                contentPadding = MaterialTheme.dimens.medium1,
                itemContent = { item ->
                    ArtistCard(
                        modifier = Modifier.fillMaxSize(),
                        artist = item,
                        onClick = {
                            viewmodel.onAction(PicArtistUiAction.OnArtistToggle(item.id))
                        }
                    )
                },
                onFloatingActionButtonClick = {
                    viewmodel.onAction(PicArtistUiAction.OnSubmit)
                },
                onQueryChange = { data ->
                    viewmodel.onAction(PicArtistUiAction.OnQueryChange(data))
                },
                onClearClick = {
                    viewmodel.onAction(PicArtistUiAction.OnQueryChange(""))
                }
            ) else  PicItemExtendedScreen(
                title = R.string.pic_artist_title,
                lessSelected = R.string.less_artist_selected,
                label = R.string.artist,
                gridSize = 4,
                query = state.artistQuery.value,
                isMinLimitReached = state.isMinLimitReached,
                isMakingApiCall = state.isMakingApiCall,
                data = artist,
                contentPadding = MaterialTheme.dimens.medium1,
                itemContent = { item ->
                    ArtistCard(
                        modifier = Modifier.fillMaxSize(),
                        artist = item,
                        onClick = {
                            viewmodel.onAction(PicArtistUiAction.OnArtistToggle(item.id))
                        }
                    )
                },
                onFloatingActionButtonClick = {
                    viewmodel.onAction(PicArtistUiAction.OnSubmit)
                },
                onQueryChange = { data ->
                    viewmodel.onAction(PicArtistUiAction.OnQueryChange(data))
                },
                onClearClick = {
                    viewmodel.onAction(PicArtistUiAction.OnQueryChange(""))
                }
            )
        },
    )
}