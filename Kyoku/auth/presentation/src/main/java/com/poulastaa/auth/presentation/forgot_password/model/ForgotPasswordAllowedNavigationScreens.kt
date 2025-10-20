package com.poulastaa.auth.presentation.forgot_password.model

import com.poulastaa.core.domain.utils.Email


sealed class ForgotPasswordAllowedNavigationScreens {
    data class Verify(val email: Email? = null) : ForgotPasswordAllowedNavigationScreens()
    data object NavigateBack : ForgotPasswordAllowedNavigationScreens()
}