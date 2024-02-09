package com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.auth.email.SendForgotPasswordMailStatus
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.auth.email.forgot_password.ForgotPasswordState
import com.poulastaa.kyoku.data.model.auth.email.forgot_password.ForgotPasswordUiEvent
import com.poulastaa.kyoku.domain.repository.AuthRepository
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    @Named("AuthNetworkObserver") private val connectivity: NetworkObserver,
    private val validateEmail: ValidateEmail,
    @Named("AuthApiImpl") private val api: AuthRepository
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = ForgotPasswordState(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    private var email: String? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(ForgotPasswordState())
        private set

    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.OnEmailEnter -> {
                state = state.copy(
                    email = event.data,
                    emailSupportingText = "",
                    isEmailError = false
                )
            }

            is ForgotPasswordUiEvent.OnAutoFillEmailClick -> {
                state = state.copy(
                    email = event.email,
                    emailSupportingText = "",
                    isEmailError = false
                )
            }

            is ForgotPasswordUiEvent.EmitEmailSupportingText -> {
                state = state.copy(
                    isEmailError = true,
                    emailSupportingText = event.message
                )
            }

            ForgotPasswordUiEvent.OnGetEmailClick -> {
                if (checkInternetConnection()) {
                    if (!state.isLoading && state.isEnabled) {
                        if (validate()) {
                            state = state.copy(
                                isLoading = true
                            )
                            email = state.email

                            sendMail()
                        } else {
                            onEvent(ForgotPasswordUiEvent.EmitEmailSupportingText("Please enter a valid email"))
                        }
                    }

                } else onEvent(ForgotPasswordUiEvent.EmitToast("Please Check Your Internet Connection"))
            }

            ForgotPasswordUiEvent.OnNavigateBackClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = UiEvent.Navigate(Screens.AuthEmailLogin.route))
                }
            }


            is ForgotPasswordUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            ForgotPasswordUiEvent.OnCancel -> {
                state = ForgotPasswordState(
                    email = state.email
                )
            }

            ForgotPasswordUiEvent.SomeErrorOccurred -> onEvent(ForgotPasswordUiEvent.EmitToast("Opus Something went wrong"))
        }
    }

    private fun validate(): Boolean {
        val validEmail = when (validateEmail(state.email)) {
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

        return validEmail
    }

    private fun sendMail() {
        if (email != null)
            viewModelScope.launch(Dispatchers.IO) {
                api.forgotPassword(email!!).let { response ->
                    delay(2000)
                    when (response) {
                        SendForgotPasswordMailStatus.USER_EXISTS -> {
                            state = state.copy(
                                isEnabled = false,
                                isLoading = false,
                                emailSendText = "An Password reset mail is sent to you\n" +
                                        "Please Change The Password and login again"
                            )

                            enableTimer()
                        }

                        SendForgotPasswordMailStatus.USER_NOT_FOUND -> {
                            onEvent(ForgotPasswordUiEvent.OnCancel)
                            onEvent(ForgotPasswordUiEvent.EmitEmailSupportingText("Email not Registered"))
                        }

                        SendForgotPasswordMailStatus.SOMETHING_WENT_WRONG -> {
                            onEvent(ForgotPasswordUiEvent.OnCancel)
                            onEvent(ForgotPasswordUiEvent.SomeErrorOccurred)
                        }
                    }
                }
            }
    }

    private fun enableTimer() {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 39 downTo 1) {
                delay(1000)
                state = state.copy(
                    enableTimer = i
                )
            }

            state = state.copy(
                isEnabled = true,
                enableTimer = 40
            )
        }
    }
}