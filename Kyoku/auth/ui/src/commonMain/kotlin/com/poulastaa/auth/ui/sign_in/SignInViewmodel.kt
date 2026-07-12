package com.poulastaa.auth.ui.sign_in

import androidx.compose.runtime.Immutable
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.ui.utils.emailError
import com.poulastaa.auth.ui.utils.normalizedEmail
import com.poulastaa.auth.ui.utils.normalizedPassword
import com.poulastaa.auth.ui.utils.passwordError
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.ApiResult
import com.poulastaa.common.ui.states.UiTextFiledState
import com.poulastaa.common.ui.viewmodel.BaseViewmodel
import com.poulastaa.common.network.Error as NetworkError

@Immutable
class SignInViewmodel(
    private val repo: AuthRepository,
) : BaseViewmodel<SignInUiState, SignInUiAction, SignInUiEvent>(
    initialSate = SignInUiState(),
) {
    override suspend fun handleAction(action: SignInUiAction) {
        if (_uiState.value.isMakingApiCall && action != SignInUiAction.OnPasswordVisibilityToggle) return

        when (action) {
            is SignInUiAction.OnEmailChange -> updateState {
                copy(email = UiTextFiledState(value = action.email.normalizedEmail()))
            }

            is SignInUiAction.OnPasswordChange -> updateState {
                copy(password = UiTextFiledState(value = action.password.normalizedPassword()))
            }

            is SignInUiAction.SignIn -> {
                val email = action.email.normalizedEmail()
                val password = action.password.normalizedPassword()

                val emailError = email.emailError()
                val passwordError = password.passwordError()

                updateState {
                    copy(
                        isMakingApiCall = emailError == null && passwordError == null,
                        email = UiTextFiledState(
                            value = email,
                            isError = emailError != null,
                            errorMessage = emailError,
                        ),
                        password = UiTextFiledState(
                            value = password,
                            isError = passwordError != null,
                            errorMessage = passwordError,
                        ),
                    )
                }

                if (emailError != null || passwordError != null) return

                when (val result = repo.signIn(email, password)) {
                    is ApiResult.Error -> {
                        handleSignInError(result.error.error)
                    }

                    is ApiResult.Success -> {
                        // TODO: do something
                    }
                }

                updateState { copy(isMakingApiCall = false) }
            }

            is SignInUiAction.OnForgotPasswordClick -> onEvent(
                SignInUiEvent.NavigateToForgotPassword(
                    email = action.email.normalizedEmail().ifBlank { null },
                )
            )

            SignInUiAction.OnCreateAccountClick -> onEvent(SignInUiEvent.NavigateToSignUp)

            SignInUiAction.OnGoogleSignInClick -> onEvent(SignInUiEvent.StartGoogleAuthFlow)

            SignInUiAction.OnPasswordVisibilityToggle -> updateState { copy(isPasswordVisible = isPasswordVisible.not()) }
        }
    }

    private fun handleSignInError(error: NetworkError) {
        if (handleCommonError(error)) return

        when (error) {
            ApiError.Authentication.PASSWORD_DOES_NOT_MATCH -> {
                setPasswordError(ApiError.Authentication.PASSWORD_DOES_NOT_MATCH.message)
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
