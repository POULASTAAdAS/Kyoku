package com.poulastaa.add.presentation.artist

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType

internal data class AddArtistUiState(
    val isSaving: Boolean = false,
    val loadingType: LoadingType = LoadingType.Loading,
    val query: String = "",
    val searchFilterType: AddArtistSearchUiFilterType = AddArtistSearchUiFilterType.ALL,
    val selectedArtist: List<UiArtist> = emptyList(),
) {
    val isEditEnabled = selectedArtist.isNotEmpty()
}

internal data class UiArtist(
    val id: ArtistId = -1,
    val name: String = "",
    val poster: String = "",
    val isSelected: Boolean = false,
)

internal enum class AddArtistSearchUiFilterType(
    @StringRes val value: Int,
    @DrawableRes val icon: Int,
) {
    ALL(R.string.all, R.drawable.ic_filter_all),
    INTERNATIONAL(R.string.international, R.drawable.ic_international),
}