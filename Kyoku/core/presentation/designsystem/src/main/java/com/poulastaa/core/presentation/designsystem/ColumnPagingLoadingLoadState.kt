package com.poulastaa.core.presentation.designsystem

import androidx.compose.foundation.lazy.LazyListScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.poulastaa.core.presentation.designsystem.components.LoadingErrorContent
import com.poulastaa.core.presentation.designsystem.components.NotLoadingContent

fun LazyListScope.columnPagingLoadingLoadState(
    data: CombinedLoadStates,
    retry: () -> Unit,
    loadingContent: () -> Unit,
) {
    when (val loadState = data.append) {
        is LoadState.Error -> item {
            LoadingErrorContent(retry)
        }

        LoadState.Loading -> loadingContent()

        is LoadState.NotLoading -> if (loadState.endOfPaginationReached) item {
            NotLoadingContent()
        }
    }
}