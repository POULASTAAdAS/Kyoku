package com.poulastaa.setup.presentation.pic_genre

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.KyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.setup.presentation.components.PicItemCompactScreen
import com.poulastaa.setup.presentation.components.PicItemExtendedScreen
import com.poulastaa.setup.presentation.pic_genre.component.GenreCard
import com.poulastaa.setup.presentation.pic_genre.component.GenreLoadingCard

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalLayoutApi::class)
@Composable
fun PicGenreRootScreen(
    viewmodel: PicGenreViewmodel = hiltViewModel(),
    navigateToPicArtist: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val genre = viewmodel.genre.collectAsLazyPagingItems()
    val config = LocalConfiguration.current

    val windowSizeClass = calculateWindowSizeClass(activity)
    val verticalScroll = rememberScrollState()

    ObserveAsEvent(viewmodel.event) { event ->
        when (event) {
            is PicGenreUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            PicGenreUiEvent.OnSuccess -> navigateToPicArtist()
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            PicItemCompactScreen(
                title = R.string.pic_genre_title,
                lessSelected = R.string.less_genre_selected,
                label = R.string.genre_label,
                gridSize = 2,
                query = state.searchGenre.value,
                isMinLimitReached = state.isMinLimitReached,
                isMakingApiCall = state.isMakingApiCall,
                data = genre,
                itemLoadingContent = {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = it)
                            .padding(MaterialTheme.dimens.small2)
                            .verticalScroll(verticalScroll),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Center,
                        maxItemsInEachRow = 2,
                    ) {
                        repeat(40) {
                            GenreLoadingCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                },
                itemContent = { item ->
                    GenreCard(
                        genre = item,
                        modifier = Modifier.height(80.dp),
                        onClick = {
                            viewmodel.onAction(
                                PicGenreUiAction.OnGenreSelect(
                                    item.id,
                                    item.isSelected
                                )
                            )
                        }
                    )
                },
                onFloatingActionButtonClick = {
                    viewmodel.onAction(PicGenreUiAction.OnContinueClick)
                },
                onQueryChange = { data ->
                    viewmodel.onAction(PicGenreUiAction.OnGenreChange(data))
                },
                onClearClick = {
                    viewmodel.onAction(PicGenreUiAction.OnGenreChange(""))
                }
            )
        },
        mediumContent = {
            PicItemCompactScreen(
                title = R.string.pic_genre_title,
                lessSelected = R.string.less_genre_selected,
                label = R.string.genre_label,
                gridSize = 3,
                query = state.searchGenre.value,
                isMinLimitReached = state.isMinLimitReached,
                isMakingApiCall = state.isMakingApiCall,
                data = genre,
                itemLoadingContent = {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = it)
                            .padding(MaterialTheme.dimens.small2)
                            .verticalScroll(verticalScroll),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Center,
                        maxItemsInEachRow = 3,
                    ) {
                        repeat(42) {
                            GenreLoadingCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(95.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                },
                itemContent = { item ->
                    GenreCard(
                        genre = item,
                        modifier = Modifier.height(95.dp),
                        onClick = {
                            viewmodel.onAction(
                                PicGenreUiAction.OnGenreSelect(
                                    item.id,
                                    item.isSelected
                                )
                            )
                        }
                    )
                },
                onFloatingActionButtonClick = {
                    viewmodel.onAction(PicGenreUiAction.OnContinueClick)
                },
                onQueryChange = { data ->
                    viewmodel.onAction(PicGenreUiAction.OnGenreChange(data))
                },
                onClearClick = {
                    viewmodel.onAction(PicGenreUiAction.OnGenreChange(""))
                }
            )
        },
        expandedContent = {
            if (config.screenWidthDp > 980) PicItemExtendedScreen(
                title = R.string.pic_genre_title,
                lessSelected = R.string.less_genre_selected,
                label = R.string.genre_label,
                gridSize = 3,
                query = state.searchGenre.value,
                isMinLimitReached = state.isMinLimitReached,
                isMakingApiCall = state.isMakingApiCall,
                data = genre,
                itemLoadingContent = {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.small1)
                            .verticalScroll(verticalScroll),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Center,
                        maxItemsInEachRow = 3
                    ) {
                        repeat(42) {
                            GenreLoadingCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(95.dp)
                                    .padding(3.dp)
                            )
                        }
                    }
                },
                itemContent = { item ->
                    GenreCard(
                        genre = item,
                        modifier = Modifier.height(95.dp),
                        onClick = {
                            viewmodel.onAction(
                                PicGenreUiAction.OnGenreSelect(
                                    item.id,
                                    item.isSelected
                                )
                            )
                        }
                    )
                },
                onFloatingActionButtonClick = {
                    viewmodel.onAction(PicGenreUiAction.OnContinueClick)
                },
                onQueryChange = { data ->
                    viewmodel.onAction(PicGenreUiAction.OnGenreChange(data))
                },
                onClearClick = {
                    viewmodel.onAction(PicGenreUiAction.OnGenreChange(""))
                }
            ) else PicItemExtendedScreen(
                title = R.string.pic_genre_title,
                lessSelected = R.string.less_genre_selected,
                label = R.string.genre_label,
                gridSize = 2,
                query = state.searchGenre.value,
                isMinLimitReached = state.isMinLimitReached,
                isMakingApiCall = state.isMakingApiCall,
                data = genre,
                itemLoadingContent = {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(MaterialTheme.dimens.small1)
                            .verticalScroll(verticalScroll),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Center,
                        maxItemsInEachRow = 2
                    ) {
                        repeat(40) {
                            GenreLoadingCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .padding(3.dp)
                            )
                        }
                    }
                },
                itemContent = { item ->
                    GenreCard(
                        genre = item,
                        modifier = Modifier.height(80.dp),
                        onClick = {
                            viewmodel.onAction(
                                PicGenreUiAction.OnGenreSelect(
                                    item.id,
                                    item.isSelected
                                )
                            )
                        }
                    )
                },
                onFloatingActionButtonClick = {
                    viewmodel.onAction(PicGenreUiAction.OnContinueClick)
                },
                onQueryChange = { data ->
                    viewmodel.onAction(PicGenreUiAction.OnGenreChange(data))
                },
                onClearClick = {
                    viewmodel.onAction(PicGenreUiAction.OnGenreChange(""))
                }
            )
        }
    )
}