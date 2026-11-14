package com.poulastaa.auth.viewmodels.sign_in

import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.PasswordCredentialStore
import com.poulastaa.auth.domain.model.DtoPasswordCredential
import com.poulastaa.auth.viewmodels.utils.MAX_AUTH_POLLING_COUNT
import com.poulastaa.auth.viewmodels.utils.emailError
import com.poulastaa.auth.viewmodels.utils.normalizedEmail
import com.poulastaa.auth.viewmodels.utils.normalizedPassword
import com.poulastaa.auth.viewmodels.utils.offerToSave
import com.poulastaa.auth.viewmodels.utils.passwordError
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.viewmodels.BaseViewmodel
import com.poulastaa.common.viewmodels.SnackbarType
import com.poulastaa.common.viewmodels.states.UiTextFiledState
import com.poulastaa.common.viewmodels.stringVerificationMailSent
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import com.poulastaa.common.domain.result.Error as NetworkError

class SignInViewmodel(
    private val repo: AuthRepository,
    private val credentialStore: PasswordCredentialStore,
) : BaseViewmodel<SignInUiState, SignInUiAction, SignInUiEvent>(
    initialSate = SignInUiState(),
) {
    private var currentAuthPollingCount = 0
    private var hasRequestedSavedCredential = false

    // Credential already in the store; signing in with it again must not ask to save it again.
    private var savedCredential: DtoPasswordCredential? = null

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
                            password = password,
                            isNewUser = result.response,
                        )
                    }
                }
            }

            SignInUiAction.LoadSavedCredential -> loadSavedCredential()

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
        password: String,
        isNewUser: Boolean,
    ) {
        while (true) {
            if (currentAuthPollingCount >= MAX_AUTH_POLLING_COUNT) {
                updateState { copy(isMakingApiCall = false) }
                currentAuthPollingCount = 0
                return
            }

            delay(VERIFICATION_POLL_INTERVAL_MS.milliseconds)
            currentAuthPollingCount += 1

            when (val result = repo.checkVerificationStatus(email)) {
                is AppResult.Error -> {
                    if (result.error.error == ApiError.Network.UNAUTHORIZED) continue

                    updateState { copy(isMakingApiCall = false) }
                    handleSignInError(result.error.error)
                    return
                }

                is AppResult.Success -> {
                    updateState { copy(isMakingApiCall = false) }
                    offerToSaveCredential(email, password)

                    if (isNewUser) onEvent(SignInUiEvent.NavigateToImportPlaylist)
                    else onEvent(SignInUiEvent.NavigateToHome)
                    return
                }
            }
        }
    }

    // Asked once per screen instance, and never over anything the user already typed.
    private suspend fun loadSavedCredential() {
        if (hasRequestedSavedCredential) return
        hasRequestedSavedCredential = true

        if (hasTypedInput()) return
        val stored = credentialStore.get() ?: return
        // Normalized like the fields, so the "already saved" check compares like with like.
        val credential = DtoPasswordCredential(
            email = stored.email.normalizedEmail(),
            password = stored.password.normalizedPassword(),
        )
        savedCredential = credential
        if (hasTypedInput()) return

        updateState {
            copy(
                email = UiTextFiledState(value = credential.email),
                password = UiTextFiledState(value = credential.password),
            )
        }
    }

    private fun hasTypedInput() = _uiState.value.email.value.isNotEmpty() ||
            _uiState.value.password.value.isNotEmpty()

    // Only called after the server verified the credential, so a wrong password is never stored.
    private suspend fun offerToSaveCredential(email: String, password: String) {
        // This will check data not hashcode.
        if (savedCredential == DtoPasswordCredential(email, password)) return

        // Android saves directly; desktop (Windows / macOS / Linux) asks through the in-app snackbar.
        credentialStore.offerToSave(email, password, ::showSnackbar)
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
