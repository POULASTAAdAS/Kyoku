package com.poulastaa.kyoku.presentation.screen.home_root.home

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCard
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardPlaylistPrev
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.homeScreenArtistList
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun HomeScreenContentOldUser(
    paddingValues: PaddingValues,
    isSmallPhone: Boolean,
    data: HomeUiData,
    isInternetError: Boolean,
    errorMessage: String,
    onClick: () -> Unit
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
            end = MaterialTheme.dimens.medium1
        )
    ) {
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

        // todo playlist , favourites , artist , album
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
                    // saved album or playlist
                    HomeScreenCardPlaylistPrev(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(1f / 2),
                        name = data.playlist[0].name,
                        imageUrls = data.playlist[0].listOfUrl,
                        
                    ) {
                        onClick.invoke()
                    }


                    // saved playlist
                    if (data.playlist.size >= 2)
                        HomeScreenCardPlaylistPrev(
                            modifier = Modifier
                                .fillMaxSize(),
                            name = data.playlist[1].name,
                            imageUrls = data.playlist[1].listOfUrl,
                        ) {

                        }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    //saved artist

                    // favourites

                    // saved album or playlist
                    HomeScreenCardPlaylistPrev(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(1f / 2),
                        name = data.playlist[0].name,
                        imageUrls = data.playlist[0].listOfUrl,
                        
                    ) {

                    }


                    // saved playlist
                    if (data.playlist.size >= 2)
                        HomeScreenCardPlaylistPrev(
                            modifier = Modifier
                                .fillMaxSize(),
                            name = data.playlist[1].name,
                            imageUrls = data.playlist[1].listOfUrl,
                            
                        ) {

                        }
                }
            }
        }

        // explore more
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

            Text(
                text = "Explore More",
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            // todo content

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
        }


        // recently played
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
                items(data.albumPrev.size) { albumIndex ->
                    HomeScreenCard(
                        size = if (isSmallPhone) 120.dp else 130.dp,
                        imageUrl = data.albumPrev[albumIndex].listOfSong[0].coverImage,
                        
                    ) {

                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
        }


        // Artist
        homeScreenArtistList(
            artistPrev = data.artistPrev,
            
            isSmallPhone = isSmallPhone
        )
    }
}