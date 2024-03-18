package com.poulastaa.kyoku.presentation.screen.home_root.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun HomeScreenContentNewUser(
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


            // Artist Mix
            Text(
                text = "Your Favourite Artist Mix",
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            HomeScreenCard(
                size = if (isSmallPhone) 120.dp else 130.dp,
                imageUrl = data.fevArtistMixPrev.random().coverImage,
                authHeader = headerValue,
                isCookie = isCookie,
                onClick = {

                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            Text(
                text = data.fevArtistMixPrev.map {
                    it.name.trim()
                }.toString().removePrefix("["),
                maxLines = 2,
                modifier = Modifier.width(if (isSmallPhone) 120.dp else 130.dp),
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))


            // Album
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
                            authHeader = headerValue,
                            isCookie = isCookie,
                            onClick = {

                            }
                        )

                        Text(text = it.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
        }

        // Artist
        items(
            items = data.artistPrev,
            key = {
                it.name
            }
        ) {
            Row(
                modifier = Modifier.height(if (isSmallPhone) 60.dp else 70.dp),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                HomeScreenCard(
                    size = 60.dp,
                    imageUrl = it.artistCover,
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
                        text = it.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        state = rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                it.lisOfPrevSong.forEach {
                    Box(
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        HomeScreenCard(
                            size = 120.dp,
                            imageUrl = it.coverImage,
                            authHeader = headerValue,
                            isCookie = isCookie,
                            onClick = {

                            }
                        )

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
                            text = it.title,
                            maxLines = 2,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                HomeScreenCardMore {

                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium3))
        }
    }
}