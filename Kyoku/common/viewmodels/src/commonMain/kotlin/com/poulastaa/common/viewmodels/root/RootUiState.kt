package com.poulastaa.common.viewmodels.root

import com.poulastaa.common.viewmodels.Screens

data class RootUiState(
    val isLoading: Boolean = true,
    val startDestination: Screens = Screens.AuthGraph,
)
