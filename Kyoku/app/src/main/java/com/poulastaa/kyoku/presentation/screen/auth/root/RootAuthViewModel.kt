package com.poulastaa.kyoku.presentation.screen.auth.root

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.data.model.auth.AuthUiEvent
import com.poulastaa.kyoku.data.model.auth.root.RootAuthUiEvent
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootAuthViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val dataStore: DataStoreOperation,
    private val validateEmail: ValidateEmail
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = RootAuthScreenState(
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

    var state by mutableStateOf(RootAuthScreenState())
        private set

    fun onEvent(event: RootAuthUiEvent) {
        when (event) {
            is RootAuthUiEvent.OnPasskeyEmailEnter -> {
                state = state.copy(
                    passkeyEmail = event.email,
                    isPasskeyEmailError = false,
                    passkeyEmailSupportingText = ""
                )
            }

            is RootAuthUiEvent.OnAuthFillPasskeyEmail -> {
                state = state.copy(
                    passkeyEmail = event.email,
                    isAutoFillPasskeyEmail = true
                )
            }

            RootAuthUiEvent.OnPasskeyAuthClick -> {
                if (!state.emailAuthLoading && !state.passkeyAuthLoading && !state.googleAuthLoading)
                    if (checkInternetConnection()) {
                        when (validateEmail(state.passkeyEmail)) {
                            ValidateEmail.EmailVerificationType.TYPE_EMAIL_FIELD_EMPTY -> {
                                state = RootAuthScreenState(
                                    passkeyEmailSupportingText = "Please Enter an Email",
                                    isPasskeyEmailError = true
                                )
                            }

                            ValidateEmail.EmailVerificationType.TYPE_INVALID_EMAIL -> {
                                state = RootAuthScreenState(
                                    passkeyEmailSupportingText = "Please Enter a valid Email",
                                    passkeyEmail = state.passkeyEmail,
                                    isPasskeyEmailError = true
                                )
                            }

                            ValidateEmail.EmailVerificationType.TYPE_EMAIL_SUCCESS -> {
                                state = RootAuthScreenState(
                                    passkeyEmail = state.passkeyEmail,
                                    passkeyAuthLoading = true
                                )

                                // todo start passkey auth
                            }
                        }
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            RootAuthUiEvent.OnGoogleAuthClick -> {
                if (!state.emailAuthLoading && !state.passkeyAuthLoading && !state.googleAuthLoading)
                    if (checkInternetConnection()) {
                        state = RootAuthScreenState(
                            googleAuthLoading = true
                        )

                        // todo start google auth

                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            RootAuthUiEvent.OnEmailAuthClick -> {
                if (!state.emailAuthLoading && !state.passkeyAuthLoading && !state.googleAuthLoading)
                    if (checkInternetConnection()) {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.Navigate(Screens.AuthEmailLogin.route))
                        }
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }
        }
    }
}