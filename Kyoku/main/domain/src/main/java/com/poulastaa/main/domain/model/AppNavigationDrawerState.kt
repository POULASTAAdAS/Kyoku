package com.poulastaa.main.domain.model

enum class AppNavigationDrawerState {
    OPENED,
    CLOSED
}

fun AppNavigationDrawerState.isOpened() = this == AppNavigationDrawerState.OPENED