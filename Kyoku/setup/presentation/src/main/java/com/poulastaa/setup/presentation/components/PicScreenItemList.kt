package com.poulastaa.setup.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.RetryIcon
import com.poulastaa.core.presentation.designsystem.SadIcon
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun <T : Any> PicScreenItemList(
    modifier: Modifier = Modifier,
    gridSize: Int,
    searchBarHeight: Dp,
    data: LazyPagingItems<T>,
    contentPadding: Dp,
    itemContent: @Composable LazyGridItemScope.(item: T) -> Unit,
) {
    var retryAttempt by rememberSaveable { mutableIntStateOf(0) }
    val haptic = LocalHapticFeedback.current

    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(
            top = MaterialTheme.dimens.small3 + searchBarHeight,
            start = MaterialTheme.dimens.small3,
            end = MaterialTheme.dimens.small3,
            bottom = MaterialTheme.dimens.large2
        ),
        verticalArrangement = Arrangement.spacedBy(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(contentPadding),
        columns = GridCells.Fixed(gridSize)
    ) {
        items(data.itemCount) { index ->
            data[index]?.let { uiData ->
                itemContent(uiData)
            }
        }

        when (val state = data.loadState.append) {
            is LoadState.Error -> item(
                span = { GridItemSpan(gridSize) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.dimens.medium1),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedContent(
                        targetState = retryAttempt <= 3
                    ) { state ->
                        when (state) {
                            true -> Icon(
                                imageVector = RetryIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = null
                                    ) {
                                        retryAttempt++
                                        data.retry()
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    },
                                tint = MaterialTheme.colorScheme.primary
                            )

                            false -> Icon(
                                imageVector = SadIcon,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            LoadState.Loading -> item(
                span = { GridItemSpan(gridSize) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.dimens.medium1),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.NotLoading -> item(
                span = { GridItemSpan(gridSize) }
            ) {
                if (state.endOfPaginationReached) Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = MaterialTheme.dimens.medium1),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.end_of_list),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}