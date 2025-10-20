package com.poulastaa.auth.presentation.intro.model

import com.poulastaa.auth.presentation.components.AuthAllowedNavigationScreen
import com.poulastaa.core.domain.utils.Email


internal sealed class IntroAllowedNavigationScreens {
    data class App(val screen: AuthAllowedNavigationScreen) : IntroAllowedNavigationScreens()
    data class SingUp(val email: Email?) : IntroAllowedNavigationScreens()
    data class ForgotPassword(val email: Email?) : IntroAllowedNavigationScreens()
}