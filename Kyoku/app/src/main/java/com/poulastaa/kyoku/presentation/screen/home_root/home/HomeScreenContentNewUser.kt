package com.poulastaa.kyoku.presentation.screen.home_root.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.data.model.screens.home.HomeUiEvent
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.ArtistMixCard
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCard
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardPlaylistPrev
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.homeScreenArtistList
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.CoroutineScope

@Composable
fun HomeScreenContentNewUser(
    paddingValues: PaddingValues,
    isSmallPhone: Boolean,
    isCookie: Boolean,
    headerValue: String,
    data: HomeUiData,
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
        if (data.playlist.isNotEmpty()) {
            item {
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
                            onClick.invoke(
                                HomeUiEvent.ItemClick(
                                    type = ItemsType.PLAYLIST,
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
                                        name = data.playlist[1].name
                                    )
                                )
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
            Text(
                text = "Your Favourite Artist Mix",
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            ArtistMixCard(
                coverImage = data.fevArtistMixPrev[0].coverImage,
                label = data.fevArtistMixPrev.map {
                    it.name.trim()
                }.toString().trim().removePrefix("["),
                isCookie = isCookie,
                headerValue = headerValue,
                isSmallPhone = isSmallPhone,
                onClick = onClick
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
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
                    ),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                data.albumPrev.forEach {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                    ) {
                        HomeScreenCard(
                            size = if (isSmallPhone) 120.dp else 130.dp,
                            imageUrl = it.listOfSong[0].coverImage,
                            isCookie = isCookie,
                            headerValue = headerValue,
                            onClick = {
                                onClick.invoke(
                                    HomeUiEvent.ItemClick(
                                        type = ItemsType.ALBUM_PREV,
                                        id = it.id,
                                        name = it.name,
                                        isApiCall = true
                                    )
                                )
                            }
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
            onClick = onClick
        )
    }
}