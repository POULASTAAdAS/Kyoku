package com.poulastaa.kyoku.data.model.screens.home

import com.poulastaa.kyoku.data.model.database.table.PlaylistTable

data class HomeUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,

    val data: HomeUiData = HomeUiData()
)

data class HomeUiData(
    val playlist: List<PlaylistTable> = emptyList()
)
