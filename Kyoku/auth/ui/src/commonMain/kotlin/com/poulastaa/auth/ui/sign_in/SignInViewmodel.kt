package com.poulastaa.auth.ui.sign_in

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableIntStateOf
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.ui.utils.MAX_AUTH_POLLING_COUNT
import com.poulastaa.auth.ui.utils.emailError
import com.poulastaa.auth.ui.utils.normalizedEmail
import com.poulastaa.auth.ui.utils.normalizedPassword
import com.poulastaa.auth.ui.utils.passwordError
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.AppResult
import com.poulastaa.common.ui.design_system.stringVerificationMailSent
import com.poulastaa.common.ui.states.UiTextFiledState
import com.poulastaa.common.ui.viewmodel.BaseViewmodel
import com.poulastaa.common.ui.viewmodel.SnackbarType
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import com.poulastaa.common.network.Error as NetworkError

@Immutable
class SignInViewmodel(
    private val repo: AuthRepository,
) : BaseViewmodel<SignInUiState, SignInUiAction, SignInUiEvent>(
    initialSate = SignInUiState(),
) {
    private val currentAuthPollingCount = mutableIntStateOf(0)

    override suspend fun handleAction(action: SignInUiAction) {
        val isGoogleAuthCompletion = action is SignInUiAction.OnGoogleAuthSuccess ||
                action == SignInUiAction.OnGoogleAuthCanceled

        if ((_uiState.value.isMakingApiCall || _uiState.value.isGoogleAuthInProgress) &&
            action != SignInUiAction.OnPasswordVisibilityToggle &&
            isGoogleAuthCompletion.not()
        ) return

        when (action) {
            is SignInUiAction.OnEmailChange -> updateState {
                copy(email = UiTextFiledState(value = action.email.normalizedEmail()))
            }

            is SignInUiAction.OnPasswordChange -> updateState {
                copy(password = UiTextFiledState(value = action.password.normalizedPassword()))
            }

            is SignInUiAction.SignIn -> {
                val email = _uiState.value.email.value.normalizedEmail()
                val password = _uiState.value.password.value.normalizedPassword()

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
                    is AppResult.Error -> {
                        updateState { copy(isMakingApiCall = false) }
                        handleSignInError(result.error.error)
                    }

                    is AppResult.Success -> {
                        showSnackbar(
                            message = stringVerificationMailSent(),
                            type = SnackbarType.Success,
                        )

                        pollVerificationStatus(
                            email = email,
                            isNewUser = result.response,
                        )
                    }
                }
            }

            is SignInUiAction.OnForgotPasswordClick -> onEvent(
                SignInUiEvent.NavigateToForgotPassword(
                    email = action.email.normalizedEmail().ifBlank { null },
                )
            )

            SignInUiAction.OnCreateAccountClick -> onEvent(SignInUiEvent.NavigateToSignUp)

            SignInUiAction.OnGoogleSignInClick -> {
                updateState { copy(isGoogleAuthInProgress = true) }
            }

            is SignInUiAction.OnGoogleAuthSuccess -> {
                when (val result = repo.googleAuth(action.token)) {
                    is AppResult.Error -> {
                        updateState { copy(isGoogleAuthInProgress = false) }
                        handleSignInError(result.error.error)
                    }

                    is AppResult.Success -> {
                        updateState { copy(isGoogleAuthInProgress = false) }

                        if (result.response.isNewUser) onEvent(SignInUiEvent.NavigateToImportPlaylist)
                        else onEvent(SignInUiEvent.NavigateToHome)
                    }
                }
            }

            SignInUiAction.OnGoogleAuthCanceled -> updateState { copy(isGoogleAuthInProgress = false) }

            SignInUiAction.OnPasswordVisibilityToggle -> updateState { copy(isPasswordVisible = isPasswordVisible.not()) }
        }
    }

    private suspend fun pollVerificationStatus(
        email: String,
        isNewUser: Boolean,
    ) {
        while (true) {
            if (currentAuthPollingCount.value >= MAX_AUTH_POLLING_COUNT) {
                updateState { copy(isMakingApiCall = false) }
                currentAuthPollingCount.value = 0
                return
            }

            delay(VERIFICATION_POLL_INTERVAL_MS.milliseconds)
            currentAuthPollingCount.value += 1

            when (val result = repo.checkVerificationStatus(email)) {
                is AppResult.Error -> {
                    if (result.error.error == ApiError.Network.UNAUTHORIZED) continue

                    updateState { copy(isMakingApiCall = false) }
                    handleSignInError(result.error.error)
                    return
                }

                is AppResult.Success -> {
                    updateState { copy(isMakingApiCall = false) }

                    if (isNewUser) onEvent(SignInUiEvent.NavigateToImportPlaylist)
                    else onEvent(SignInUiEvent.NavigateToHome)
                    return
                }
            }
        }
    }

    private fun handleSignInError(error: NetworkError) {
        if (handleCommonError(error)) return

        when (error) {
            ApiError.Authentication.PASSWORD_DOES_NOT_MATCH -> setPasswordError(ApiError.Authentication.PASSWORD_DOES_NOT_MATCH.message)
            ApiError.Authentication.USER_NOT_FOUND -> setEmailError(ApiError.Authentication.USER_NOT_FOUND.message)
            ApiError.Authentication.EMAIL_NOT_VALID -> setEmailError(ApiError.Authentication.EMAIL_NOT_VALID.message)
            ApiError.Authentication.INVALID_PASSWORD -> setPasswordError(ApiError.Authentication.INVALID_PASSWORD.message)
            else -> setEmailError(ApiError.Network.SOMETHING_WENT_WRONG.message)
        }
    }

    private fun setEmailError(message: String) = updateState {
        copy(email = email.copy(isError = true, errorMessage = message))
    }

    private fun setPasswordError(message: String) = updateState {
        copy(password = password.copy(isError = true, errorMessage = message))
    }

    private companion object {
        const val VERIFICATION_POLL_INTERVAL_MS = 5_000L
    }
}
