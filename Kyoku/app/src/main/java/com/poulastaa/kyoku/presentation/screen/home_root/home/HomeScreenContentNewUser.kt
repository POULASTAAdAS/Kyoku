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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCard
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.homeScreenArtistList
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun HomeScreenContentNewUser(
    paddingValues: PaddingValues,
    isSmallPhone: Boolean,
    data: HomeUiData,
    isInternetError: Boolean,
    errorMessage: String
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

        // Artist Mix
        item {
            Text(
                text = "Your Favourite Artist Mix",
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            HomeScreenCard(
                size = if (isSmallPhone) 120.dp else 130.dp,
                imageUrl = data.fevArtistMixPrev[0].coverImage,
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
        homeScreenArtistList(
            artistPrev = data.artistPrev,
            isSmallPhone = isSmallPhone
        )
    }
}