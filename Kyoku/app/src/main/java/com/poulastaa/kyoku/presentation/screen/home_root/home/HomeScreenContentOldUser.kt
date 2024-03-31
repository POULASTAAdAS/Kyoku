package com.poulastaa.kyoku.presentation.screen.home_root.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.data.model.screens.home.HomeUiEvent
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCard
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardMore
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardPlaylistPrev
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardWithText
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.homeScreenArtistList
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.FavouritePrev
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreenContentOldUser(

    paddingValues: PaddingValues,
    isSmallPhone: Boolean,
    data: HomeUiData,
    isCookie: Boolean,
    headerValue: String,
    isInternetError: Boolean,
    errorMessage: String,
    scope: CoroutineScope = rememberCoroutineScope(),
    onClick: (HomeUiEvent.ItemClick) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
        contentPadding = PaddingValues(
            start = MaterialTheme.dimens.medium1,
            end = MaterialTheme.dimens.medium1,
            top = MaterialTheme.dimens.medium1
        )
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
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
                                            id = data.playlist[0].id
                                        )
                                    )
                                }
                            }
                        )
                }
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

                    // favourites or album
                    if (data.favourites)
                        FavouritePrev(
                            modifier = Modifier
                                .fillMaxWidth(.5f)
                                .fillMaxHeight(),
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
                                .fillMaxWidth()
                                .fillMaxHeight(),
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
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium3))

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

        // explore more
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium3))

            Text(
                text = "Explore More",
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            // todo content

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
        }


        // recently played
        if (data.historyPrev.isNotEmpty())
            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

                Text(
                    text = "Recently Played",
                    fontWeight = FontWeight.Black,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    items(data.historyPrev.size) { historySongIndex ->
                        Box(
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            HomeScreenCard(
                                size = if (isSmallPhone) 120.dp else 130.dp,
                                imageUrl = data.historyPrev[historySongIndex].coverImage,
                                isCookie = isCookie,
                                headerValue = headerValue,
                                onClick = {
                                    scope.launch {
                                        onClick.invoke(
                                            HomeUiEvent.ItemClick(
                                                type = ItemsType.SONG,
                                                id = data.historyPrev[historySongIndex].id
                                            )
                                        )
                                    }
                                }
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

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
            }


        // ArtistS
        homeScreenArtistList(
            artistPrev = data.artistPrev,
            isSmallPhone = isSmallPhone,
            isCookie = isCookie,
            headerValue = headerValue,
            scope = scope,
            onClick = onClick
        )
    }
}