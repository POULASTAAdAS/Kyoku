package com.poulastaa.core.presentation.designsystem

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.poulastaa.core.presentation.designsystem.components.LoadingErrorContent
import com.poulastaa.core.presentation.designsystem.components.NotLoadingContent

fun LazyGridScope.gridPagingLoadingContent(
    gridSize: Int,
    data: CombinedLoadStates,
    retry: () -> Unit,
    loadingContent: @Composable () -> Unit,
) {
    when (val loadState = data.append) {
        is LoadState.Error -> item(
            span = { GridItemSpan(gridSize) }
        ) {
            LoadingErrorContent(retry)
        }

        LoadState.Loading -> item(
            span = { GridItemSpan(gridSize) }
        ) {
            loadingContent()
        }

        is LoadState.NotLoading -> if (loadState.endOfPaginationReached) item(
            span = { GridItemSpan(gridSize) }
        ) {
            NotLoadingContent()
        }
    }
}


