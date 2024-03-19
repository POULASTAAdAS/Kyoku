package com.poulastaa.kyoku.presentation.screen.home_root.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCard
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardMore
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardPlaylistPrev
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun HomeScreenContentOldUser(
    paddingValues: PaddingValues,
    isSmallPhone: Boolean,
    data: HomeUiData,
    isCookie: Boolean,
    headerValue: String,
    isInternetError: Boolean,
    errorMessage: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            )
            .navigationBarsPadding(),
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

        // playlist  , favourites , artist , album
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
                        authHeader = headerValue,
                        isCookie = isCookie
                    ) {

                    }


                    // saved playlist
                    if (data.playlist.size >= 2)
                        HomeScreenCardPlaylistPrev(
                            modifier = Modifier
                                .fillMaxSize(),
                            name = data.playlist[1].name,
                            imageUrls = data.playlist[1].listOfUrl,
                            authHeader = headerValue,
                            isCookie = isCookie
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
                        authHeader = headerValue,
                        isCookie = isCookie
                    ) {

                    }


                    // saved playlist
                    if (data.playlist.size >= 2)
                        HomeScreenCardPlaylistPrev(
                            modifier = Modifier
                                .fillMaxSize(),
                            name = data.playlist[1].name,
                            imageUrls = data.playlist[1].listOfUrl,
                            authHeader = headerValue,
                            isCookie = isCookie
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
                        authHeader = headerValue,
                        isCookie = isCookie
                    ) {

                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
        }


        // artist
        items(data.artistPrev.size) { artistIndex ->
            Row(
                modifier = Modifier.height(if (isSmallPhone) 60.dp else 70.dp),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                HomeScreenCard(
                    size = 60.dp,
                    imageUrl = data.artistPrev[artistIndex].artistCover,
                    authHeader = headerValue,
                    isCookie = isCookie,
                    shape = CircleShape,
                    onClick = {

                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable(
                            onClick = {

                            },
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "More from")
                    Text(
                        text = data.artistPrev[artistIndex].name,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                items(data.artistPrev[artistIndex].lisOfPrevSong.size) { songIndex ->
                    Box(
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        HomeScreenCard(
                            size = 120.dp,
                            imageUrl = data.artistPrev[artistIndex]
                                .lisOfPrevSong[songIndex].coverImage,
                            authHeader = headerValue,
                            isCookie = isCookie
                        ) {

                        }

                        Text(
                            modifier = Modifier
                                .width(120.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.background.copy(.8f),
                                    shape = RoundedCornerShape(
                                        bottomEnd = MaterialTheme.dimens.small3,
                                        bottomStart = MaterialTheme.dimens.small3
                                    )
                                ),
                            text = data.artistPrev[artistIndex]
                                .lisOfPrevSong[songIndex].title,
                            maxLines = 2,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    HomeScreenCardMore {

                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
        }
    }
}