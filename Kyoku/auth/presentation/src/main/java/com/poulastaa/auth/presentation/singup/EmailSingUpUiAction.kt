package com.poulastaa.auth.presentation.singup

import android.content.Context
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Password
import com.poulastaa.core.domain.utils.Username


internal sealed interface EmailSingUpUiAction {
    data class OnUsernameChange(val username: Username) : EmailSingUpUiAction
    data class OnEmailChange(val email: Email) : EmailSingUpUiAction
    data class OnPasswordChange(val password: Password) : EmailSingUpUiAction
    data object OnPasswordVisibilityToggle : EmailSingUpUiAction
    data object OnConformPasswordVisibilityToggle : EmailSingUpUiAction

    data class OnConformPasswordChange(val password: Password) : EmailSingUpUiAction

    data class OnSubmit(val context: Context) : EmailSingUpUiAction

    data object OnEmailLogInClick : EmailSingUpUiAction
}