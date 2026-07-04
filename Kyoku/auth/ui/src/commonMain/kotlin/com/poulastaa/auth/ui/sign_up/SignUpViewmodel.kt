package com.poulastaa.auth.ui.sign_up

import androidx.compose.runtime.Immutable
import com.poulastaa.auth.ui.utils.emailError
import com.poulastaa.auth.ui.utils.normalizedEmail
import com.poulastaa.auth.ui.utils.normalizedPassword
import com.poulastaa.auth.ui.utils.normalizedUsername
import com.poulastaa.auth.ui.utils.passwordError
import com.poulastaa.auth.ui.utils.usernameError
import com.poulastaa.auth.ui.utils.usernameInputError
import com.poulastaa.common.ui.states.UiTextFiledState
import com.poulastaa.common.ui.viewmodel.BaseViewmodel

@Immutable
class SignUpViewmodel : BaseViewmodel<SingUpUiState, SignUpUiAction, SignUpUiEvent>(
    initialSate = SingUpUiState(),
) {
    override fun handleAction(action: SignUpUiAction) {
        if (action == SignUpUiAction.OnPasswordVisibilityToggle) {
            updateState { copy(isPasswordVisible = isPasswordVisible.not()) }
            return
        }

        if (_uiState.value.isMakingApiCall) return

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

            SignUpUiAction.OnGoogleSignInClick -> onEvent(SignUpUiEvent.StartGoogleAuthFlow)

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

                // TODO: make api request
            }

            SignUpUiAction.OnLoginClick -> onEvent(SignUpUiEvent.NavigateToLogIn)

            SignUpUiAction.OnPasswordVisibilityToggle -> Unit
        }
    }
}
