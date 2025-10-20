package com.poulastaa.auth.presentation.intro.model

import com.poulastaa.core.domain.utils.Email


internal sealed class IntroAllowedNavigationScreens {
    internal enum class AppScreens {
        HOME,
        SET_B_DATE,
        PIC_GENRE,
        PIC_ARTIST,
        IMPORT_SPOTIFY_PLAYLIST
    }

    data class App(val screen: AppScreens) : IntroAllowedNavigationScreens()
    data class SingUp(val email: Email?) : IntroAllowedNavigationScreens()
    data class ForgotPassword(val email: Email?) : IntroAllowedNavigationScreens()
}