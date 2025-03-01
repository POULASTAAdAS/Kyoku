package com.poulastaa.main.presentation.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.components.LoadingScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeExpandedLoadingScreen(
    paddingValues: PaddingValues,
    topAppBarScroll: TopAppBarScrollBehavior,
) {
    val config = LocalConfiguration.current
    val isTablet = config.screenWidthDp > 980
    val cardHeight =
        (config.screenHeightDp - (config.screenHeightDp / if (isTablet) 1.8 else 2.8)).dp
    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background
    )

    LoadingScreenWrapper(paddingValues, topAppBarScroll) {
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .padding(horizontal = MaterialTheme.dimens.medium1)
        ) {
            Card(
                shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                modifier = Modifier.aspectRatio(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                colors = cardColors
            ) {
                HomeLoadingShimmer()
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HomeLoadingTopRow(
                    itemHeight = 70.dp,
                    itemSpacing = MaterialTheme.dimens.medium1
                ) {
                    if (isTablet) repeat(3) {
                        HomeLoadingAlbumCard(
                            Modifier.weight(1f),
                            cardColors,
                            CircleShape
                        )
                    }
                    else repeat(2) {
                        HomeLoadingAlbumCard(
                            Modifier.weight(1f),
                            cardColors,
                            CircleShape
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                HomeLoadingTextCard(Modifier.fillMaxWidth(.4f))

                Spacer(Modifier.height(MaterialTheme.dimens.small2))

                HomeLoadingItemRow {
                    repeat(3) {
                        Card(
                            shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                            modifier = Modifier.aspectRatio(1f),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            ),
                            colors = cardColors
                        ) {
                            HomeLoadingShimmer()
                        }
                    }
                }
            }
        }

        HomeLoadingCompactMediumCommon(cardColors)

        repeat(5) {
            HomeLoadingOtherCard(
                itemCount = 5,
                cardColors = cardColors
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small3))
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium3))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    widthDp = 1280,
    heightDp = 650
)
@Preview(
    widthDp = 900,
    heightDp = 480,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    AppThem {
        HomeExpandedLoadingScreen(
            paddingValues = PaddingValues(0.dp),
            topAppBarScroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        )
    }
}