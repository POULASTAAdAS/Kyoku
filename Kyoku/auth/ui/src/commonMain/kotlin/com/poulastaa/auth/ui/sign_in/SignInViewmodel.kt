package com.poulastaa.auth.ui.sign_in

import androidx.compose.runtime.Immutable
import com.poulastaa.auth.ui.utils.emailError
import com.poulastaa.auth.ui.utils.normalizedEmail
import com.poulastaa.auth.ui.utils.normalizedPassword
import com.poulastaa.auth.ui.utils.passwordError
import com.poulastaa.common.ui.states.UiTextFiledState
import com.poulastaa.common.ui.viewmodel.BaseViewmodel

@Immutable
class SignInViewmodel : BaseViewmodel<SignInUiState, SignInUiAction, SignInUiEvent>(
    initialSate = SignInUiState(),
) {
    override fun handleAction(action: SignInUiAction) {
        if (action == SignInUiAction.OnPasswordVisibilityToggle) {
            updateState { copy(isPasswordVisible = isPasswordVisible.not()) }
            return
        }

        if (_uiState.value.isMakingApiCall) return

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

                // TODO: make api request
            }

            is SignInUiAction.OnForgotPasswordClick -> onEvent(
                SignInUiEvent.NavigateToForgotPassword(
                    email = action.email.normalizedEmail().ifBlank { null },
                )
            )

            SignInUiAction.OnCreateAccountClick -> onEvent(SignInUiEvent.NavigateToSignUp)

            SignInUiAction.OnGoogleSignInClick -> onEvent(SignInUiEvent.StartGoogleAuthFlow)

            SignInUiAction.OnPasswordVisibilityToggle -> Unit
        }
    }
}
