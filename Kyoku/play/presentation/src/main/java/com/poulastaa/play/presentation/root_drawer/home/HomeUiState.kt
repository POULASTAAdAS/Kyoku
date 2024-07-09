package com.poulastaa.play.presentation.root_drawer.home

import com.poulastaa.play.presentation.root_drawer.home.model.UiHomeData

data class HomeUiState(
    val heading: String = "",
    val isDataLoading: Boolean = true,
    val isNewUser: Boolean = true,

    val header: String = "",

    val data: UiHomeData = UiHomeData(),
) {
    val canShowUi: Boolean
        get() = !isNewUser && !isDataLoading
}








