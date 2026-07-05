package com.poulastaa.auth.ui.sign_in

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.ui.utils.emailError
import com.poulastaa.auth.ui.utils.normalizedEmail
import com.poulastaa.auth.ui.utils.normalizedPassword
import com.poulastaa.auth.ui.utils.passwordError
import com.poulastaa.common.ui.states.UiTextFiledState
import com.poulastaa.common.ui.viewmodel.BaseViewmodel
import kotlinx.coroutines.launch

@Immutable
class SignInViewmodel(
    private val repo: AuthRepository,
) : BaseViewmodel<SignInUiState, SignInUiAction, SignInUiEvent>(
    initialSate = SignInUiState(),
) {
    override fun handleAction(action: SignInUiAction) {
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

                viewModelScope.launch {
                    val result = repo.signIn(email, password)
                }
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
}
