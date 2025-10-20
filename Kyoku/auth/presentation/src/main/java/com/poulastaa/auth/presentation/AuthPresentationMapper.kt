package com.poulastaa.auth.presentation

import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.core.domain.SavedScreen

internal fun IntroAllowedNavigationScreens.AppScreens.toNavigationScreen() = when (this) {
    IntroAllowedNavigationScreens.AppScreens.HOME -> SavedScreen.MAIN
    IntroAllowedNavigationScreens.AppScreens.SET_B_DATE -> SavedScreen.SET_B_DATE
    IntroAllowedNavigationScreens.AppScreens.PIC_GENRE -> SavedScreen.PIC_GENRE
    IntroAllowedNavigationScreens.AppScreens.PIC_ARTIST -> SavedScreen.PIC_ARTIST
    IntroAllowedNavigationScreens.AppScreens.IMPORT_SPOTIFY_PLAYLIST -> SavedScreen.IMPORT_SPOTIFY_PLAYLIST
}