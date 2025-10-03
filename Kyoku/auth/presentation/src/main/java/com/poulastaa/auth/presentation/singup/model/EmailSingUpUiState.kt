package com.poulastaa.auth.presentation.singup.model

import androidx.compose.runtime.Stable
import com.poulastaa.auth.presentation.intro.model.EmailTextProp
import com.poulastaa.auth.presentation.intro.model.PasswordTextProp

@Stable
internal data class EmailSingUpUiState(
    val isLoading: Boolean = false,
    val isOldUser: Boolean = false,

    val username: UsernameTextProp = UsernameTextProp(),
    val email: EmailTextProp = EmailTextProp(),
    val password: PasswordTextProp = PasswordTextProp(),
    val conformPassword: PasswordTextProp = PasswordTextProp(),
)
