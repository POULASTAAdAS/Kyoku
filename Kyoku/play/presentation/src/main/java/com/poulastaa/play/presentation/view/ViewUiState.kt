package com.poulastaa.play.presentation.view


data class ViewUiState(
    val isDataLoading: Boolean = true,
    val topBarTitle: String = "",

    val isMakingAPiCall: Boolean = false,
    val header: String = "",
    val data: ViewUiData = ViewUiData()
)

data class ViewUiData(
    val id: Long = -1,
    val name: String = "",
    val listOfSong: List<ViewUiSong> = emptyList()
)

data class ViewUiSong(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val isExpanded: Boolean = false
)
