package com.poulastaa.auth.presentation.singup

import com.poulastaa.core.presentation.Email
import com.poulastaa.core.presentation.Password
import com.poulastaa.core.presentation.Username

internal sealed interface EmailSingUpUiAction {
    data class OnUsernameChange(val username: Username) : EmailSingUpUiAction
    data class OnEmailChange(val email: Email) : EmailSingUpUiAction
    data class OnPasswordChange(val password: Password) : EmailSingUpUiAction
    data object OnPasswordVisibilityToggle : EmailSingUpUiAction
    data object OnConformPasswordVisibilityToggle : EmailSingUpUiAction

    data class OnConformPasswordChange(val password: Password) : EmailSingUpUiAction

    data object OnSubmit : EmailSingUpUiAction

    data object OnEmailLogInClick : EmailSingUpUiAction
}