package com.poulastaa.auth.presentation

import com.poulastaa.auth.presentation.components.AuthAllowedNavigationScreen
import com.poulastaa.core.domain.SavedScreen

internal fun AuthAllowedNavigationScreen.toNavigationScreen() = when (this) {
    AuthAllowedNavigationScreen.HOME -> SavedScreen.MAIN
    AuthAllowedNavigationScreen.SET_B_DATE -> SavedScreen.SET_B_DATE
    AuthAllowedNavigationScreen.PIC_GENRE -> SavedScreen.PIC_GENRE
    AuthAllowedNavigationScreen.PIC_ARTIST -> SavedScreen.PIC_ARTIST
    AuthAllowedNavigationScreen.IMPORT_SPOTIFY_PLAYLIST -> SavedScreen.IMPORT_SPOTIFY_PLAYLIST
}