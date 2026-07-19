package com.poulastaa.auth.ui.sign_up

import androidx.compose.runtime.Immutable
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.ui.utils.emailError
import com.poulastaa.auth.ui.utils.normalizedEmail
import com.poulastaa.auth.ui.utils.normalizedPassword
import com.poulastaa.auth.ui.utils.normalizedUsername
import com.poulastaa.auth.ui.utils.passwordError
import com.poulastaa.auth.ui.utils.usernameError
import com.poulastaa.auth.ui.utils.usernameInputError
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult
import com.poulastaa.common.ui.states.UiTextFiledState
import com.poulastaa.common.ui.viewmodel.BaseViewmodel
import com.poulastaa.common.network.Error as NetworkError

@Immutable
class SignUpViewmodel(
    private val repo: AuthRepository,
) : BaseViewmodel<SingUpUiState, SignUpUiAction, SignUpUiEvent>(
    initialSate = SingUpUiState(),
) {
    override suspend fun handleAction(action: SignUpUiAction) {
        val isGoogleAuthCompletion = action is SignUpUiAction.OnGoogleTokenReceived ||
                action == SignUpUiAction.OnGoogleAuthCanceled

        if ((_uiState.value.isMakingApiCall || _uiState.value.isGoogleAuthInProgress) &&
            action != SignUpUiAction.OnPasswordVisibilityToggle &&
            isGoogleAuthCompletion.not()
        ) return

        when (action) {
            is SignUpUiAction.OnEmailChange -> updateState {
                copy(email = UiTextFiledState(value = action.email.normalizedEmail()))
            }

            is SignUpUiAction.OnPasswordChange -> updateState {
                copy(password = UiTextFiledState(value = action.password.normalizedPassword()))
            }

            is SignUpUiAction.OnUsernameChange -> {
                val username = action.username.normalizedUsername()
                val usernameError = username.usernameInputError()

                updateState {
                    copy(
                        username = UiTextFiledState(
                            value = username,
                            isError = usernameError != null,
                            errorMessage = usernameError,
                        )
                    )
                }
            }

            SignUpUiAction.OnGoogleSignInClick -> updateState { copy(isGoogleAuthInProgress = true) }

            is SignUpUiAction.OnGoogleTokenReceived -> {
                when (val result = repo.googleAuth(action.token, TODO())) {
                    is ApiResult.Error -> {
                        updateState { copy(isGoogleAuthInProgress = false) }
                        handleSignUpError(result.error.error)
                    }

                    is ApiResult.Success -> {
                        updateState { copy(isGoogleAuthInProgress = false) }

                        if (result.response.isNewUser) onEvent(SignUpUiEvent.NavigateToImportPlaylist)
                        else onEvent(SignUpUiEvent.NavigateToHome)
                    }
                }
            }

            SignUpUiAction.OnGoogleAuthCanceled -> updateState { copy(isGoogleAuthInProgress = false) }

            is SignUpUiAction.SignUp -> {
                val email = action.email.normalizedEmail()
                val username = action.username.normalizedUsername()
                val password = action.password.normalizedPassword()

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

                if (emailError != null || passwordError != null || usernameError != null) return

                when (val result = repo.signUp(email, username, password)) {
                    is ApiResult.Error -> {
                        updateState { copy(isMakingApiCall = false) }
                        handleSignUpError(result.error.error)
                    }

                    is ApiResult.Success -> {
                        updateState { copy(isMakingApiCall = false) }

                        if (result.response.isNewUser) onEvent(SignUpUiEvent.NavigateToImportPlaylist)
                        else onEvent(SignUpUiEvent.NavigateToHome)
                    }
                }
            }

            SignUpUiAction.OnLoginClick -> onEvent(SignUpUiEvent.NavigateToLogIn)

            SignUpUiAction.OnPasswordVisibilityToggle -> updateState {
                copy(isPasswordVisible = isPasswordVisible.not())
            }
        }
    }

    private fun handleSignUpError(error: NetworkError) {
        if (handleCommonError(error)) return

        when (error) {
            ApiError.Authentication.EMAIL_ALREADY_IN_USE -> {
                setEmailError(ApiError.Authentication.EMAIL_ALREADY_IN_USE.message)
            }

            ApiError.Authentication.OLD_ACCOUNT_FOUND -> {
                setEmailError(ApiError.Authentication.OLD_ACCOUNT_FOUND.message)
            }

            ApiError.Authentication.ACCOUNT_NOT_FOUND -> {
                setEmailError(ApiError.Authentication.ACCOUNT_NOT_FOUND.message)
            }

            ApiError.Authentication.INVALID_EMAIL -> {
                setEmailError(ApiError.Authentication.INVALID_EMAIL.message)
            }

            ApiError.Authentication.INVALID_PASSWORD -> {
                setPasswordError(ApiError.Authentication.INVALID_PASSWORD.message)
            }

            ApiError.Authentication.EMAIL_NOT_VERIFIED -> {
                setEmailError(ApiError.Authentication.EMAIL_NOT_VERIFIED.message)
            }

            else -> {

            }
        }
    }

    private fun setEmailError(message: String) = updateState {
        copy(email = email.copy(isError = true, errorMessage = message))
    }

    private fun setPasswordError(message: String) = updateState {
        copy(password = password.copy(isError = true, errorMessage = message))
    }
}
