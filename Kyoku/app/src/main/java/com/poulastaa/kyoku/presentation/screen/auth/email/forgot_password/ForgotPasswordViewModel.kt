package com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.auth.AuthUiEvent
import com.poulastaa.kyoku.data.model.auth.email.forgot_password.ForgotPasswordState
import com.poulastaa.kyoku.data.model.auth.email.forgot_password.ForgotPasswordUiEvent
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    @Named("AuthNetworkObserver") private val connectivity: NetworkObserver,
    private val validateEmail: ValidateEmail
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

    private val _uiEvent = Channel<AuthUiEvent>()
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

            ForgotPasswordUiEvent.OnGetEmailClick -> {
                if (checkInternetConnection()) {
                    if (!state.isLoading) {
                        val result = validate()

                        if (result) {
                            state = state.copy(
                                isLoading = true
                            )

                            // todo make api call
                        }
                    }

                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        _uiEvent.send(element = AuthUiEvent.ShowToast("Please Check Your Internet Connection"))
                    }
                }
            }

            ForgotPasswordUiEvent.OnNavigateBackClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = AuthUiEvent.Navigate(Screens.AuthEmailLogin.route))
                }
            }
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
}