package com.poulastaa.view.presentation.saved

import androidx.annotation.StringRes
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType

internal data class ViewSavedUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val type: ViewSavedUiItemType = ViewSavedUiItemType.NONE,
    val items: List<ViewSavedUiItem> = emptyList(),
    val isEditEnabled: Boolean = false,
    val isDeleteDialogOpen: Boolean = false,
    val isNewPlaylistDialogOpen: Boolean = false,
)

enum class ViewSavedUiItemType(@StringRes val value: Int) {
    ARTIST(R.string.artist),
    PLAYLIST(R.string.playlist),
    ALBUM(R.string.album),
    NONE(R.string.no),
}
