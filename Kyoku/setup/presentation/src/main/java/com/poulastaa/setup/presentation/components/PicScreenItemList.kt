package com.poulastaa.setup.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.paging.compose.LazyPagingItems
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
    }
}