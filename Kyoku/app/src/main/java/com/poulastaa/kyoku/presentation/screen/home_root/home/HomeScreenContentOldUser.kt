package com.poulastaa.kyoku.presentation.screen.home_root.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.home.BottomSheetData
import com.poulastaa.kyoku.data.model.screens.home.HomeLongClickType
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.data.model.screens.home.HomeUiEvent
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenBottomSheet
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCard
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardMore
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardPlaylistPrev
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardWithText
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.homeScreenArtistList
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.FavouritePrev
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryScreenPlaylistGridView
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContentOldUser(
    paddingValues: PaddingValues,
    sheetState: SheetState = rememberModalBottomSheetState(),
    isSmallPhone: Boolean,
    data: HomeUiData,
    bottomSheetData: BottomSheetData,
    isCookie: Boolean,
    headerValue: String,
    isInternetError: Boolean,
    errorMessage: String,
    bottomSheetState: Boolean,
    isBottomSheetLoading: Boolean,
    scope: CoroutineScope = rememberCoroutineScope(),
    onClick: (HomeUiEvent) -> Unit,
    onLongClick: (HomeUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
        contentPadding = PaddingValues(
            bottom = MaterialTheme.dimens.medium1
        )
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .windowInsetsPadding(
                        insets = WindowInsets(
                            left = MaterialTheme.dimens.medium1,
                            right = MaterialTheme.dimens.medium1
                        )
                    ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                if (data.playlist.isNotEmpty())
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp),
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
                                scope.launch {
                                    onClick.invoke(
                                        HomeUiEvent.ItemClick(
                                            type = ItemsType.PLAYLIST,
                                            id = data.playlist[0].id
                                        )
                                    )
                                }
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
                                    scope.launch {
                                        onClick.invoke(
                                            HomeUiEvent.ItemClick(
                                                type = ItemsType.PLAYLIST,
                                                id = data.playlist[1].id
                                            )
                                        )
                                    }
                                }
                            )
                    }

                if (data.savedAlbumPrev.isNotEmpty() || data.favourites || data.playlist.size >= 3)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                    ) {
                        // saved album
                        if (data.savedAlbumPrev.isNotEmpty())
                            HomeScreenCardWithText(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(1f / 2),
                                name = data.savedAlbumPrev[0].album,
                                imageUrl = data.savedAlbumPrev[0].coverImage,
                                isCookie = isCookie,
                                headerValue = headerValue,
                                onClick = {
                                    scope.launch {
                                        onClick.invoke(
                                            HomeUiEvent.ItemClick(
                                                type = ItemsType.ALBUM,
                                                name = data.savedAlbumPrev[0].album
                                            )
                                        )
                                    }
                                }
                            )
                        else if (data.playlist.size >= 3)
                            HomeScreenCardPlaylistPrev(
                                modifier = Modifier
                                    .fillMaxWidth(.5f)
                                    .fillMaxHeight(),
                                name = data.playlist[2].name,
                                imageUrls = data.playlist[2].listOfUrl,
                                isCookie = isCookie,
                                headerValue = headerValue,
                                onClick = {
                                    scope.launch {
                                        onClick.invoke(
                                            HomeUiEvent.ItemClick(
                                                type = ItemsType.PLAYLIST,
                                                id = data.playlist[2].id
                                            )
                                        )
                                    }
                                }
                            )

                        // favourites or album
                        if (data.favourites)
                            FavouritePrev(
                                modifier = Modifier
                                    .fillMaxSize(),
                                onLongClick = {},
                                onClick = {
                                    scope.launch {
                                        onClick.invoke(
                                            HomeUiEvent.ItemClick(
                                                type = ItemsType.FAVOURITE
                                            )
                                        )
                                    }
                                }
                            )
                        else if (data.savedAlbumPrev.size >= 2)
                            HomeScreenCardWithText(
                                modifier = Modifier
                                    .fillMaxSize(),
                                name = data.savedAlbumPrev[1].album,
                                imageUrl = data.savedAlbumPrev[1].coverImage,
                                isCookie = isCookie,
                                headerValue = headerValue,
                                onClick = {
                                    scope.launch {
                                        onClick.invoke(
                                            HomeUiEvent.ItemClick(
                                                type = ItemsType.ALBUM,
                                                name = data.savedAlbumPrev[1].album
                                            )
                                        )
                                    }
                                }
                            )
                    }
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
                    Column {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

                        CustomToast(
                            message = errorMessage,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }

        // explore more
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.dimens.medium1
                    ),
                text = "Explore More",
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                if (data.fevArtistMixPrev.isNotEmpty())
                    Column(
                        modifier = Modifier
                            .wrapContentWidth(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.combinedClickable(
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
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            HomeScreenCard(
                                size = if (isSmallPhone) 120.dp else 130.dp,
                                imageUrl = data.fevArtistMixPrev[0].coverImage,
                                isCookie = isCookie,
                                headerValue = headerValue
                            )

                            Text(
                                modifier = Modifier
                                    .width(if (isSmallPhone) 120.dp else 130.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.background.copy(.8f),
                                        shape = RoundedCornerShape(
                                            bottomEnd = MaterialTheme.dimens.small3,
                                            bottomStart = MaterialTheme.dimens.small3
                                        )
                                    ),
                                text = data.fevArtistMixPrev.map {
                                    it.name.trim()
                                }.toString().trim().removePrefix("["),
                                maxLines = 2,
                                fontWeight = FontWeight.Medium,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                        Text(
                            text = "Artist Mix",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    }


                if (data.dailyMixPrevUrls.isNotEmpty())
                    LibraryScreenPlaylistGridView(
                        modifier = Modifier
                            .size(if (isSmallPhone) 120.dp else 130.dp)
                            .combinedClickable(
                                onClick = {
                                    onClick.invoke(
                                        HomeUiEvent.ItemClick(
                                            type = ItemsType.DAILY_MIX
                                        )
                                    )
                                },
                                onLongClick = {
                                    onLongClick.invoke(
                                        HomeUiEvent.ItemLongClick(
                                            type = HomeLongClickType.DAILY_MIX
                                        )
                                    )
                                }
                            ),
                        isCookie = isCookie,
                        authHeader = headerValue,
                        name = "Daily Mix",
                        imageUrls = data.dailyMixPrevUrls
                    )
            }
        }


        // recently played
        if (data.historyPrev.isNotEmpty())
            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

                Text(
                    modifier = Modifier.padding(
                        start = MaterialTheme.dimens.medium1
                    ),
                    text = "Recently Played",
                    fontWeight = FontWeight.Black,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
                    contentPadding = PaddingValues(
                        start = MaterialTheme.dimens.medium1,
                        end = MaterialTheme.dimens.medium1
                    )
                ) {
                    items(data.historyPrev.size) { historySongIndex ->
                        Box(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    scope.launch {
                                        onClick.invoke(
                                            HomeUiEvent.ItemClick(
                                                type = ItemsType.SONG,
                                                id = data.historyPrev[historySongIndex].id
                                            )
                                        )
                                    }
                                },
                                onLongClick = {
                                    scope.launch {
                                        onLongClick.invoke(
                                            HomeUiEvent.ItemLongClick(
                                                type = HomeLongClickType.HISTORY_SONG,
                                                id = data.historyPrev[historySongIndex].id,
                                                name = data.historyPrev[historySongIndex].title
                                            )
                                        )
                                    }
                                }
                            ),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            HomeScreenCard(
                                size = if (isSmallPhone) 120.dp else 130.dp,
                                imageUrl = data.historyPrev[historySongIndex].coverImage,
                                isCookie = isCookie,
                                headerValue = headerValue
                            )

                            Text(
                                modifier = Modifier
                                    .width(if (isSmallPhone) 120.dp else 130.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.background.copy(.8f),
                                        shape = RoundedCornerShape(
                                            bottomEnd = MaterialTheme.dimens.small3,
                                            bottomStart = MaterialTheme.dimens.small3
                                        )
                                    ),
                                text = data.historyPrev[historySongIndex].title,
                                maxLines = 2,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    item {
                        HomeScreenCardMore(
                            text = "View All",
                            fontWeight = FontWeight.Black,
                            maxLine = 2,
                            size = if (isSmallPhone) 120.dp else 130.dp,
                            onClick = {
                                scope.launch {
                                    onClick.invoke(
                                        HomeUiEvent.ItemClick(
                                            type = ItemsType.HISTORY
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }

        // ArtistS
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
            isBottomSheetLoading = isBottomSheetLoading,
            isCookie = isCookie,
            headerValue = headerValue,
            data = bottomSheetData,
            onClick = onClick,
            cancelClick = {
                scope.launch {
                    sheetState.hide()
                }
            }
        )
}