package com.poulastaa.auth.presentation.forgot_password.model

import com.poulastaa.core.presentation.Email

sealed class ForgotPasswordAllowedNavigationScreens {
    data class Verify(val email: Email? = null) : ForgotPasswordAllowedNavigationScreens()
    data object NavigateBack : ForgotPasswordAllowedNavigationScreens()
}