package com.poulastaa.auth.ui.sign_up

import androidx.compose.runtime.Immutable
import com.poulastaa.common.ui.states.UiTextFiledState
import com.poulastaa.common.ui.viewmodel.BaseViewmodel

@Immutable
class SignUpViewmodel :
    BaseViewmodel<SingUpUiState, SignUpUiAction, SignUpUiEvent>(SingUpUiState()) {

    override fun handleAction(action: SignUpUiAction) {
        when (action) {
            is SignUpUiAction.OnEmailChange -> updateState {
                copy(email = UiTextFiledState(value = action.email.trim()))
            }

            is SignUpUiAction.OnPasswordChange -> updateState {
                copy(password = UiTextFiledState(value = action.password.filterNot { it.isWhitespace() }))
            }

            is SignUpUiAction.OnUsernameChange -> {
                val rawUsername = action.username.trim()
                val username = rawUsername.filterUsernameInput()
                val filterError = rawUsername.usernameInputError()

                updateState {
                    copy(
                        username = UiTextFiledState(
                            value = username,
                            isError = filterError != null,
                            errorMessage = filterError,
                        )
                    )
                }
            }

            SignUpUiAction.OnGoogleSignInClick -> onEvent(SignUpUiEvent.StartGoogleAuthFlow)

            is SignUpUiAction.SignUp -> {
                val email = action.email.trim()
                val username = action.username.trim().filterUsernameInput()
                val password = action.password.filterNot { it.isWhitespace() }

                val emailError = email.emailError()
                val usernameError = username.usernameError()
                val passwordError = password.passwordError()

                updateState {
                    copy(
                        isMakingApiCall = emailError == null && usernameError == null && passwordError == null,
                        email = UiTextFiledState(
                            value = email,
                            isError = emailError != null,
                            errorMessage = emailError,
                        ),
                        username = UiTextFiledState(
                            value = username,
                            isError = usernameError != null,
                            errorMessage = usernameError,
                        ),
                        password = UiTextFiledState(
                            value = password,
                            isError = passwordError != null,
                            errorMessage = passwordError,
                        ),
                    )
                }
            }

            SignUpUiAction.OnLoginClick -> onEvent(SignUpUiEvent.NavigateToLogIn)
        }
    }

    private fun String.emailError(): String? = if (EMAIL_REGEX.matches(this)) null else INVALID_EMAIL_ERROR

    private fun String.usernameError(): String? = when {
        length < MIN_USERNAME_LENGTH -> USERNAME_LENGTH_ERROR
        length > MAX_USERNAME_LENGTH -> USERNAME_MAX_LENGTH_ERROR
        take(MIN_USERNAME_LENGTH).all { it.isEnglishLetter() }.not() -> USERNAME_FIRST_FOUR_ERROR
        drop(MIN_USERNAME_LENGTH).all { it.isLetterOrDigit() || it == '_' || it == '.' }.not() -> USERNAME_CHAR_ERROR
        else -> null
    }

    private fun String.passwordError(): String? = when {
        length < MIN_PASSWORD_LENGTH -> PASSWORD_LENGTH_ERROR
        none { it.isLetter() } -> PASSWORD_LETTER_ERROR
        none { it.isDigit() } -> PASSWORD_NUMBER_ERROR
        else -> null
    }

    private fun String.filterUsernameInput(): String = buildString {
        this@filterUsernameInput.forEach { char ->
            when {
                length < MIN_USERNAME_LENGTH && char.isEnglishLetter() -> append(char)
                length >= MIN_USERNAME_LENGTH && (char.isLetterOrDigit() || char == '_' || char == '.') -> append(char)
            }
        }
    }

    private fun String.usernameInputError(): String? {
        var acceptedLength = 0

        forEach { char ->
            when {
                acceptedLength < MIN_USERNAME_LENGTH && char.isEnglishLetter() -> acceptedLength++
                acceptedLength < MIN_USERNAME_LENGTH -> return USERNAME_FIRST_FOUR_ERROR
                char.isLetterOrDigit() || char == '_' || char == '.' -> acceptedLength++
                else -> return USERNAME_CHAR_ERROR
            }
        }

        return null
    }

    private fun Char.isEnglishLetter(): Boolean = this in 'A'..'Z' || this in 'a'..'z'

    private companion object {
        const val MIN_USERNAME_LENGTH = 4
        const val MAX_USERNAME_LENGTH = 24
        const val MIN_PASSWORD_LENGTH = 8
        const val INVALID_EMAIL_ERROR = "Invalid email"
        const val USERNAME_LENGTH_ERROR = "Use 4+ characters"
        const val USERNAME_MAX_LENGTH_ERROR = "Max 24 characters"
        const val USERNAME_FIRST_FOUR_ERROR = "First 4 must be letters"
        const val USERNAME_CHAR_ERROR = "Use letters, numbers, . or _"
        const val PASSWORD_LENGTH_ERROR = "Use 8+ characters"
        const val PASSWORD_LETTER_ERROR = "Add a letter"
        const val PASSWORD_NUMBER_ERROR = "Add a number"
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
