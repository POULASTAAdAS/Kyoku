package com.poulastaa.main.presentation.main

import kotlinx.serialization.Serializable

sealed interface ScreensCore {
    @Serializable
    data object Home : ScreensCore

    @Serializable
    data object Library : ScreensCore
}