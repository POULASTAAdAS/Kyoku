package com.poulastaa.auth.presentation.intro.model

import com.poulastaa.core.presentation.Email

sealed class IntroNavigationScreens {
    data class SingUp(val email: Email?) : IntroNavigationScreens()
    data class ForgotPassword(val email: Email?) : IntroNavigationScreens()
}