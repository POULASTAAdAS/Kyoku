package com.poulastaa.auth.presentation.intro.model

import com.poulastaa.core.domain.Email


sealed class IntroAllowedNavigationScreens {
    data class SingUp(val email: Email?) : IntroAllowedNavigationScreens()
    data class ForgotPassword(val email: Email?) : IntroAllowedNavigationScreens()
}