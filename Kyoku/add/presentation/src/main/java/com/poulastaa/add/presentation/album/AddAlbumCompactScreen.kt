package com.poulastaa.add.presentation.album

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun AddAlbumCompactScreen(
    state: AddAlbumUiState,
    album: LazyPagingItems<UiAlbum>,
    onAction: (AddAlbumUiAction) -> Unit,
    navigateBack: () -> Unit,
) {

}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddAlbumCompactScreen(
                state = AddAlbumUiState(),
                album = flowOf(
                    PagingData.from((1..10).map { UiAlbum() })
                ).collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}