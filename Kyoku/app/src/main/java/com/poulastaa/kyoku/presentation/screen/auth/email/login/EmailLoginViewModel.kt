package com.poulastaa.kyoku.presentation.screen.auth.email.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.auth.AuthUiEvent
import com.poulastaa.kyoku.data.model.auth.email.login.EmailLoginState
import com.poulastaa.kyoku.data.model.auth.email.login.EmailLoginUiEvent
import com.poulastaa.kyoku.data.model.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.usecase.UseCases
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.domain.usecase.ValidatePassword
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EmailLoginViewModel @Inject constructor(
    @Named("AuthNetworkObserver") private val connectivity: NetworkObserver,
    private val dataStore: DataStoreOperation,
    private val useCases: UseCases
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = EmailLoginState(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    var state by mutableStateOf(EmailLoginState())
        private set

    private val _uiEvent = Channel<AuthUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: EmailLoginUiEvent) {
        when (event) {
            is EmailLoginUiEvent.OnEmailEnter -> {
                state = state.copy(
                    email = event.data,
                    isEmailError = false,
                    emailSupportingText = ""
                )
            }

            is EmailLoginUiEvent.OnPasswordEnter -> {
                state = state.copy(
                    password = event.data,
                    isPasswordError = false,
                    passwordSupportingText = ""
                )
            }

            is EmailLoginUiEvent.OnAutoFillEmail -> {
                state = state.copy(
                    email = event.email,
                    isEmailError = false,
                    emailSupportingText = ""
                )
            }

            is EmailLoginUiEvent.OnAutoFillPassword -> {
                state = state.copy(
                    password = event.password,
                    isPasswordError = false,
                    passwordSupportingText = ""
                )
            }

            EmailLoginUiEvent.OnPasswordVisibilityChange -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            EmailLoginUiEvent.OnContinueClick -> {
                if (!state.isLoading)
                    if (checkInternetConnection()) {
                        val result = validate()

                        if (result) {
                            state = state.copy(
                                isLoading = true
                            )

                            // todo make api call
                        }

                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            EmailLoginUiEvent.OnForgotPasswordClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = AuthUiEvent.Navigate(Screens.ForgotPassword.route))
                }
                state = EmailLoginState()
            }

            EmailLoginUiEvent.OnSignUpClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = AuthUiEvent.Navigate(Screens.AuthEmailSignUp.route))
                }
                state = EmailLoginState()
            }
        }
    }

    private fun validate(): Boolean {
        val validEmail = when (useCases.validateEmail(state.email)) {
            ValidateEmail.EmailVerificationType.TYPE_EMAIL_FIELD_EMPTY -> {
                state = state.copy(
                    emailSupportingText = "Please Enter an Email",
                    isEmailError = true
                )
                false
            }

            ValidateEmail.EmailVerificationType.TYPE_INVALID_EMAIL -> {
                state = state.copy(
                    emailSupportingText = "Please Enter a valid Email",
                    isEmailError = true
                )
                false
            }

            ValidateEmail.EmailVerificationType.TYPE_EMAIL_SUCCESS -> {
                true
            }
        }

        val validPassword = when (useCases.validatePassword(state.password)) {
            ValidatePassword.PasswordValidityType.TYPE_PASSWORD_TO_SHORT -> {
                state = state.copy(
                    passwordSupportingText = "Password to Short",
                    isPasswordError = true
                )
                false
            }

            ValidatePassword.PasswordValidityType.TYPE_PASSWORD_FIELD_EMPTY -> {
                state = state.copy(
                    passwordSupportingText = "Password is empty",
                    isPasswordError = true
                )
                false
            }

            ValidatePassword.PasswordValidityType.TYPE_PASSWORD_SUCCESS -> {
                true
            }
        }

        return validEmail && validPassword
    }
}