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
                copy(email = UiTextFiledState(value = action.email))
            }

            is SignUpUiAction.OnPasswordChange -> updateState {
                copy(password = UiTextFiledState(value = action.password))
            }

            is SignUpUiAction.OnUsernameChange -> updateState {
                copy(username = UiTextFiledState(value = action.username))
            }

            SignUpUiAction.OnGoogleSignInClick -> onEvent(SignUpUiEvent.StartGoogleAuthFlow)

            is SignUpUiAction.SignUp -> TODO()

            SignUpUiAction.OnLoginClick -> onEvent(SignUpUiEvent.NavigateToLogIn)
        }
    }
}