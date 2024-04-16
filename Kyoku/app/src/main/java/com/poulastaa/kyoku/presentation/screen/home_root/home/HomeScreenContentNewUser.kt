package com.poulastaa.kyoku.presentation.screen.home_root.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.home.BottomSheetData
import com.poulastaa.kyoku.data.model.screens.home.HomeLongClickType
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.data.model.screens.home.HomeUiEvent
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenBottomSheet
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCard
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardPlaylistPrev
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.homeScreenArtistList
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryScreenPlaylistGridView
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContentNewUser(
    paddingValues: PaddingValues,
    sheetState: SheetState = rememberModalBottomSheetState(),
    isSmallPhone: Boolean,
    isCookie: Boolean,
    headerValue: String,
    isInternetError: Boolean,
    errorMessage: String,
    bottomSheetState: Boolean,
    isBottomSheetLoading: Boolean,
    data: HomeUiData,
    bottomSheetData: BottomSheetData,
    scope: CoroutineScope = rememberCoroutineScope(),
    onClick: (HomeUiEvent) -> Unit,
    onLongClick: (HomeUiEvent) -> Unit
) {
    LaunchedEffect(key1 = bottomSheetState) {
        if (bottomSheetState) scope.launch {
            sheetState.show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
        contentPadding = PaddingValues(
            bottom = MaterialTheme.dimens.medium1,
        )
    ) {
        if (data.playlist.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(
                            start = MaterialTheme.dimens.medium1,
                            end = MaterialTheme.dimens.medium1
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    // saved playlist
                    HomeScreenCardPlaylistPrev(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(1f / 2),
                        name = data.playlist[0].name,
                        imageUrls = data.playlist[0].listOfUrl,
                        isCookie = isCookie,
                        headerValue = headerValue,
                        onClick = {
                            onClick.invoke(
                                HomeUiEvent.ItemClick(
                                    type = ItemsType.PLAYLIST,
                                    id = data.playlist[0].id,
                                    name = data.playlist[0].name
                                )
                            )
                        }
                    )


                    // saved playlist
                    if (data.playlist.size >= 2)
                        HomeScreenCardPlaylistPrev(
                            modifier = Modifier
                                .fillMaxSize(),
                            name = data.playlist[1].name,
                            imageUrls = data.playlist[1].listOfUrl,
                            isCookie = isCookie,
                            headerValue = headerValue,
                            onClick = {
                                onClick.invoke(
                                    HomeUiEvent.ItemClick(
                                        type = ItemsType.PLAYLIST,
                                        id = data.playlist[1].id,
                                        name = data.playlist[1].name
                                    )
                                )
                            }
                        )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.medium1)
                )

                if (data.playlist.size >= 3)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(
                                start = MaterialTheme.dimens.medium1,
                                end = MaterialTheme.dimens.medium1
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                    ) {
                        // saved playlist
                        if (data.playlist.size >= 3)
                            HomeScreenCardPlaylistPrev(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f / 2),
                                name = data.playlist[2].name,
                                imageUrls = data.playlist[2].listOfUrl,
                                isCookie = isCookie,
                                headerValue = headerValue,
                                onClick = {
                                    onClick.invoke(
                                        HomeUiEvent.ItemClick(
                                            type = ItemsType.PLAYLIST,
                                            id = data.playlist[2].id,
                                            name = data.playlist[2].name
                                        )
                                    )
                                }
                            )


                        // saved playlist
                        if (data.playlist.size >= 4)
                            HomeScreenCardPlaylistPrev(
                                modifier = Modifier
                                    .fillMaxSize(),
                                name = data.playlist[3].name,
                                imageUrls = data.playlist[3].listOfUrl,
                                isCookie = isCookie,
                                headerValue = headerValue,
                                onClick = {
                                    onClick.invoke(
                                        HomeUiEvent.ItemClick(
                                            type = ItemsType.PLAYLIST,
                                            id = data.playlist[3].id,
                                            name = data.playlist[3].name
                                        )
                                    )
                                }
                            )
                    }
            }
        }

        // Artist Mix
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.dimens.medium1
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(
                        top = MaterialTheme.dimens.large1
                    ),
                    text = "Your Favourite Artist Mix",
                    fontWeight = FontWeight.Black,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                LibraryScreenPlaylistGridView(
                    modifier = Modifier
                        .size(if (isSmallPhone) 120.dp else 130.dp)
                        .align(Alignment.Start)
                        .combinedClickable(
                            onClick = {
                                onClick.invoke(
                                    HomeUiEvent.ItemClick(
                                        type = ItemsType.ARTIST_MIX
                                    )
                                )
                            },
                            onLongClick = {
                                onLongClick.invoke(
                                    HomeUiEvent.ItemLongClick(
                                        type = HomeLongClickType.ARTIST_MIX
                                    )
                                )
                            }
                        ),
                    isCookie = isCookie,
                    authHeader = headerValue,
                    name = "",
                    imageUrls = data.fevArtistMixPrevUrls
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
            }
        }


        // Toast
        item {
            AnimatedVisibility(
                visible = isInternetError,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkOut()
            ) {
                val temp = remember {
                    isInternetError
                }

                if (temp) {
                    CustomToast(
                        message = errorMessage,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Album
        item {
            Text(
                modifier = Modifier.padding(start = MaterialTheme.dimens.medium1),
                text = "Some Album You May Like",
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        state = rememberScrollState()
                    )
                    .windowInsetsPadding(
                        insets = WindowInsets(
                            left = MaterialTheme.dimens.medium1,
                            right = MaterialTheme.dimens.medium1
                        )
                    ),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                data.albumPrev.forEach {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                    ) {
                        HomeScreenCard(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    onClick.invoke(
                                        HomeUiEvent.ItemClick(
                                            type = ItemsType.ALBUM_PREV,
                                            id = it.id,
                                            name = it.name,
                                            isApiCall = true
                                        )
                                    )
                                },
                                onLongClick = {
                                    onLongClick.invoke(
                                        HomeUiEvent.ItemLongClick(
                                            type = HomeLongClickType.ALBUM_PREV,
                                            id = it.id,
                                            name = it.name
                                        )
                                    )
                                }
                            ),
                            size = if (isSmallPhone) 120.dp else 130.dp,
                            imageUrl = it.coverImage,
                            isCookie = isCookie,
                            headerValue = headerValue
                        )

                        Text(text = it.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
        }

        // Artist
        homeScreenArtistList(
            artistPrev = data.artistPrev,
            isSmallPhone = isSmallPhone,
            isCookie = isCookie,
            headerValue = headerValue,
            scope = scope,
            onClick = onClick,
            onLongClick = onLongClick
        )
    }


    if (bottomSheetState)
        HomeScreenBottomSheet(
            sheetState = sheetState,
            scope = scope,
            isBottomSheetLoading = isBottomSheetLoading,
            isCookie = isCookie,
            headerValue = headerValue,
            data = bottomSheetData,
            onClick = { event ->
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onClick.invoke(event)
                }
            },
            cancelClick = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onClick.invoke(
                        HomeUiEvent.BottomSheetItemClick.CancelClicked
                    )
                }
            }
        )
}