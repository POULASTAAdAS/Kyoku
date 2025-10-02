package com.poulastaa.auth.presentation.intro.model

import com.poulastaa.core.presentation.Email

sealed class IntroAllowedNavigationScreens {
    data class SingUp(val email: Email?) : IntroAllowedNavigationScreens()
    data class ForgotPassword(val email: Email?) : IntroAllowedNavigationScreens()
}