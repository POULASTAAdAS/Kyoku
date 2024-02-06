package com.poulastaa.kyoku.presentation.screen.auth.root

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.req.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.data.model.auth.AuthUiEvent
import com.poulastaa.kyoku.data.model.auth.root.RootUiEvent
import com.poulastaa.kyoku.domain.repository.AuthRepository
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.auth.root.passkey.createPasskey
import com.poulastaa.kyoku.presentation.screen.auth.root.passkey.getPasskey
import com.poulastaa.kyoku.utils.Constants.AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP
import com.poulastaa.kyoku.utils.toPasskeyAuthRequest
import com.poulastaa.kyoku.utils.toCreatePasskeyUserReq
import com.poulastaa.kyoku.utils.toGetPasskeyUserReq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RootAuthViewModel @Inject constructor(
    @Named("AuthNetworkObserver") private val connectivity: NetworkObserver,
    private val dataStore: DataStoreOperation,
    private val validateEmail: ValidateEmail,
    private val credentialManager: CredentialManager,
    @Named("AuthApiImpl") private val api: AuthRepository
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

    fun onEvent(event: RootUiEvent) {
        when (event) {
            is RootUiEvent.OnPasskeyEmailEnter -> {
                state = state.copy(
                    passkeyEmail = event.email,
                    isPasskeyEmailError = false,
                    passkeyEmailSupportingText = ""
                )
            }

            is RootUiEvent.OnAutoFillPasskeyEmail -> {
                state = state.copy(
                    passkeyEmail = event.email,
                    isAutoFillPasskeyEmail = true
                )
            }

            is RootUiEvent.OnPasskeyAuthClick -> {
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

                                startPasskeyAuth(state.toPasskeyAuthRequest(), event.activity)
                            }
                        }
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            RootUiEvent.OnGoogleAuthClick -> {
                if (!state.emailAuthLoading && !state.passkeyAuthLoading && !state.googleAuthLoading)
                    if (checkInternetConnection()) {
                        state = RootAuthScreenState(
                            googleAuthLoading = true
                        )
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            RootUiEvent.OnEmailAuthClick -> {
                if (!state.emailAuthLoading && !state.passkeyAuthLoading && !state.googleAuthLoading)
                    if (checkInternetConnection()) {
                        state = RootAuthScreenState()
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.Navigate(Screens.AuthEmailLogin.route))
                        }
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = AuthUiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            is RootUiEvent.SendGoogleAuthApiRequest -> {
                val token = event.token

                Log.d("token", token)
            }

            RootUiEvent.SendPasskeyAuthApiRequest -> {}

            RootUiEvent.NoGoogleAccountFound -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = AuthUiEvent.ShowToast("Please Create a Google account"))
                }
                // todo prompt to account creation
            }

            RootUiEvent.OnAuthCanceled -> {
                state = state.copy(
                    googleAuthLoading = false,
                    passkeyAuthLoading = false
                )
            }

            RootUiEvent.SomeErrorOccurredOnAuth -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = AuthUiEvent.ShowToast("Opus Something went wrong"))
                }
            }
        }
    }

    private fun startPasskeyAuth(
        req: PasskeyAuthReq,
        activity: Activity
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // make api call
            api.passkeyReq(req)?.let { passkeyJson ->
                if (passkeyJson.type == AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP) {
                    createPasskey( // create passkey from string and convert to CreatePublicKeyCredential
                        credentialManager = credentialManager,
                        jsonString = passkeyJson.req,
                        activity = activity,
                        challenge = passkeyJson.challenge,
                        sendUserToServer = { id ->
                            id?.let {
                                sendUserToServer( // create user
                                    user = it.toCreatePasskeyUserReq(
                                        email = state.passkeyEmail,
                                        token = passkeyJson.token
                                    )
                                )
                                return@createPasskey
                            }

                            // error occurred
                            onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
                            onEvent(RootUiEvent.OnAuthCanceled)
                        }
                    )
                } else {
                    getPasskey(
                        credentialManager,
                        passkeyJson.req,
                        activity,
                        passkeyJson.challenge,
                        getUserFromId = {
                            it?.let {
                                getUserFromServer(
                                    user = it.toGetPasskeyUserReq(passkeyJson.token)
                                )

                                return@getPasskey
                            }

                            // error occurred
                            onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
                            onEvent(RootUiEvent.OnAuthCanceled)
                        }
                    )
                }

                return@launch
            }

            // error occurred
            onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
            onEvent(RootUiEvent.OnAuthCanceled)
        }
    }

    private fun sendUserToServer(
        user: CreatePasskeyUserReq
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEvent.send(element = AuthUiEvent.ShowToast("Please wait while we get things ready"))

            api.createPasskeyUser(user)?.let {
                Log.d("user", it.toString())

                //todo handle type of userCreation status

                state = state.copy(
                    passkeyAuthLoading = false
                )

                // todo prompt user to next screen

                return@launch
            }

            // error occurred
            onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
            onEvent(RootUiEvent.OnAuthCanceled)
        }
    }

    private fun getUserFromServer(user: GetPasskeyUserReq) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEvent.send(element = AuthUiEvent.ShowToast("Please wait while we get things ready"))

            api.getPasskeyUser(user)?.let {
                Log.d("userrrrrrrrrrrrrrrrrrrrrrrrrr", it.toString())

                //todo handle type of userCreation status

                state = state.copy(
                    passkeyAuthLoading = false
                )

                return@launch
            }

            // error occurred
            onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
            onEvent(RootUiEvent.OnAuthCanceled)
        }
    }
}