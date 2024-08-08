package com.poulastaa.play.presentation.root_drawer.home.model

import com.poulastaa.play.presentation.root_drawer.model.HomeItemClickType

data class ItemBottomSheetUiState(
    val isOpen: Boolean = false,
    val isBottomSheetLoading: Boolean = true,
    val isQueue: Boolean = false,
    val flag: Boolean = false,
    val isInFavourite: Boolean = false,
    val id: Long? = null,
    val title: String = "",
    val urls: List<String> = emptyList(),
    val itemType: HomeItemClickType = HomeItemClickType.NON,
)