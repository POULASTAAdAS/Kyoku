package com.poulastaa.main.domain.model

enum class AppDrawerState {
    OPENED,
    CLOSED
}

fun AppDrawerState.isOpened() = this == AppDrawerState.OPENED