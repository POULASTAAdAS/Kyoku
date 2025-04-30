package com.poulastaa.add.presentation.album

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.TextHolder

internal data class AddAlbumUiState(
    val loadingType: LoadingType = LoadingType.Loading,
    val isSavingAlbums: Boolean = false,
    val query: TextHolder = TextHolder(),
    val searchFilterType: AddAlbumSearchUiFilterType = AddAlbumSearchUiFilterType.MOST_POPULAR,
    val isClearAllDialogOpen: Boolean = false,
    val selectedAlbums: List<UiAlbum> = emptyList(),
    val viewAlbumScreenState: ViewAlbumUiState = ViewAlbumUiState(),
    val isSelectedAlbumOpen: Boolean = false,
) {
    val isEditEnabled = selectedAlbums.isNotEmpty()
}

internal data class ViewAlbumUiState(
    val album: UiAlbum = UiAlbum(),
    val isVisible: Boolean = false,
)

internal data class UiAlbum(
    val id: AlbumId = -1,
    val name: String = "",
    val poster: String = "",
    val artist: String? = null,
    val releaseYear: Int? = null,
    val isSelected: Boolean = false,
)

internal enum class AddAlbumSearchUiFilterType(
    @StringRes val value: Int,
    @DrawableRes val icon: Int,
) {
    MOST_POPULAR(R.string.most_popular, R.drawable.ic_popular),
    RELEASE_YEAR(R.string.release_year, R.drawable.ic_b_date),
}