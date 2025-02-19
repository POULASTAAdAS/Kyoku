package com.poulastaa.main.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.designsystem.ui.gradiantBackground


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoadingScreenWrapper(
    paddingValues: PaddingValues,
    scroll: TopAppBarScrollBehavior,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = gradiantBackground()
                )
            )
            .fillMaxSize()
            .nestedScroll(scroll.nestedScrollConnection)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets(top = MAIN_TOP_BAR_PADDING))
            .padding(paddingValues),
        content = content
    )
}

@Composable
internal fun HomeLoadingAlbumCard(
    modifier: Modifier,
    cardColors: CardColors,
    shape: RoundedCornerShape,
) {
    Card(
        shape = shape,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        colors = cardColors
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .shimmerEffect()
        ) {
            Card(
                shape = shape,
                modifier = Modifier.aspectRatio(1f),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                HomeLoadingShimmer()
            }
        }
    }
}

@Composable
internal fun HomeLoadingTextCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(40.dp)
            .padding(horizontal = MaterialTheme.dimens.medium1),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(MaterialTheme.dimens.small1),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        HomeLoadingShimmer()
    }
}

@Composable
internal fun HomeLoadingTopRow(
    itemHeight: Dp,
    itemSpacing: Dp,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .padding(horizontal = MaterialTheme.dimens.medium1),
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        content = content
    )
}


@Composable
internal fun HomeLoadingCompactMediumCommon(cardColors: CardColors) {
    HomeLoadingBoxItem(cardColors)

    Spacer(Modifier.height(MaterialTheme.dimens.large2))

    HomeLoadingTextCard(Modifier.fillMaxWidth(.5f))

    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

    HomeLoadingItemRow {
        repeat(10) {
            HomeLoadingCircleCard(cardColors)
        }
    }

    repeat(3) {
        HomeLoadingBoxItem(cardColors)
    }

    Spacer(Modifier.height(MaterialTheme.dimens.large2))

    HomeLoadingTextCard(Modifier.fillMaxWidth(.7f))

    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
}

@Composable
internal fun HomeLoadingOtherCard(
    itemCount: Int,
    cardColors: CardColors,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(
                WindowInsets(
                    left = MaterialTheme.dimens.medium1,
                    right = MaterialTheme.dimens.medium1
                )
            ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        content = {
            repeat(itemCount) {
                Card(
                    shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                    colors = cardColors
                ) {
                    HomeLoadingShimmer()
                }
            }
        }
    )
}

@Composable
internal fun HomeLoadingCircleCard(cardColors: CardColors) {
    Card(
        shape = CircleShape,
        modifier = Modifier.aspectRatio(1f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        colors = cardColors
    ) {
        HomeLoadingShimmer()
    }
}

@Composable
internal fun HomeLoadingBoxItem(
    cardColors: CardColors,
) {
    Spacer(Modifier.height(MaterialTheme.dimens.large2))

    HomeLoadingTextCard(Modifier.fillMaxWidth(.4f))

    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

    HomeLoadingItemRow {
        repeat(10) {
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


@Composable
internal fun HomeLoadingItemRow(
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .horizontalScroll(rememberScrollState())
            .windowInsetsPadding(
                WindowInsets(
                    left = MaterialTheme.dimens.medium1,
                    right = MaterialTheme.dimens.medium1
                )
            ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        content = content
    )
}

@Stable
@Composable
internal fun HomeLoadingShimmer() {
    Box(
        Modifier
            .fillMaxSize()
            .shimmerEffect()
    )
}