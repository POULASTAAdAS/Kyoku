package com.poulastaa.main.domain.model

enum class AppNavigationRailState {
    OPENED,
    CLOSED
}

fun AppNavigationRailState.isOpened() = this == AppNavigationRailState.OPENED
