package com.poulastaa.play.presentation.view


data class ViewUiState(
    val loadingState: ViewLoadingState = ViewLoadingState.LOADING,
    val topBarTitle: String = "",

    val isMakingAPiCall: Boolean = false,
    val header: String = "",
    val data: ViewUiData = ViewUiData()
)

data class ViewUiData(
    val id: Long = -1,
    val name: String = "",
    val urls: List<String> = emptyList(),
    val listOfSong: List<ViewUiSong> = emptyList()
)

data class ViewUiSong(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val isExpanded: Boolean = false
)

enum class ViewLoadingState {
    LOADING,
    LOADED,
    ERROR
}
