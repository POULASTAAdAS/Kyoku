package com.poulastaa.kyoku.data.model.home_nav_drawer

import com.poulastaa.kyoku.presentation.screen.home_root.PlayerSong

data class HomeRootUiState(
    val isCookie: Boolean = false,
    val isLoading: Boolean = false,
    val headerValue: String = "",
    val homeTopBarTitle: String = "Good Morning",
    val libraryTopBarTitle: String = "Your Library",
    val profilePicUrl: String = "",
    val userName: String = "",
    val nav: Nav = Nav.HOME,

    val isPlayer: Boolean = false,
    val isPlayerLoading: Boolean = true,
    val playerData: List<PlayerSong> = emptyList()
)

enum class Nav {
    HOME,
    LIB,
    NON
}
