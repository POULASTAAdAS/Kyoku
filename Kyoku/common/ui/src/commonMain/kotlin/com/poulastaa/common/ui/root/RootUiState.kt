package com.poulastaa.common.ui.root

import com.poulastaa.common.ui.Screens

data class RootUiState(
    val isLoading: Boolean = true,
    val startDestination: Screens = Screens.Loading,
)
