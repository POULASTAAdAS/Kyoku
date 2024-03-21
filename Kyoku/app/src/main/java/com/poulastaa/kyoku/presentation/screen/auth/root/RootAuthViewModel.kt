package com.poulastaa.kyoku.presentation.screen.auth.root

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.auth.UserCreationStatus
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthResponse
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.data.model.screens.auth.root.RootUiEvent
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.AuthRepository
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.presentation.screen.auth.root.passkey.createPasskey
import com.poulastaa.kyoku.presentation.screen.auth.root.passkey.getPasskey
import com.poulastaa.kyoku.utils.Constants.AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP
import com.poulastaa.kyoku.utils.extractTokenOrCookie
import com.poulastaa.kyoku.utils.storeAuthType
import com.poulastaa.kyoku.utils.storeCookieOrAccessToken
import com.poulastaa.kyoku.utils.storeData
import com.poulastaa.kyoku.utils.storeEmail
import com.poulastaa.kyoku.utils.storeProfilePicUri
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.storeUsername
import com.poulastaa.kyoku.utils.toCreatePasskeyUserReq
import com.poulastaa.kyoku.utils.toGetPasskeyUserReq
import com.poulastaa.kyoku.utils.toGoogleAuthReq
import com.poulastaa.kyoku.utils.toPasskeyAuthRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.CookieManager
import javax.inject.Inject

@HiltViewModel
class RootAuthViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val cookieManager: CookieManager,
    private val ds: DataStoreOperation,
    private val db: DatabaseRepositoryImpl,
    private val validateEmail: ValidateEmail,
    private val cred: CredentialManager,
    private val api: AuthRepository
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

    private val _uiEvent = Channel<UiEvent>()
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
                        if (validate()) startPasskeyAuth(
                            state.toPasskeyAuthRequest(),
                            event.activity
                        )
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = UiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            RootUiEvent.OnGoogleAuthClick -> {
                if (!state.emailAuthLoading && !state.passkeyAuthLoading && !state.googleAuthLoading)
                    if (checkInternetConnection()) {
                        state = state.copy(
                            googleAuthLoading = true
                        )
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = UiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            is RootUiEvent.SendGoogleAuthApiRequest -> {
                val localeList = event.activity.resources.configuration.locales

                if (!localeList.isEmpty)
                    startGoogleAuth(
                        event.token.toGoogleAuthReq(
                            countryCode = localeList[0].country
                        ),
                        context = event.activity
                    )
                else {
                    onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
                    onEvent(RootUiEvent.OnAuthCanceled)
                }
            }

            RootUiEvent.OnEmailAuthClick -> {
                if (!state.emailAuthLoading && !state.passkeyAuthLoading && !state.googleAuthLoading)
                    if (checkInternetConnection()) {
                        state = RootAuthScreenState()
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = UiEvent.Navigate(Screens.AuthEmailLogin.route))
                        }
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = UiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            RootUiEvent.NoGoogleAccountFound -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = UiEvent.ShowToast("Please Create a Google account"))
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
                    _uiEvent.send(element = UiEvent.ShowToast("Opus Something went wrong"))
                }
            }
        }
    }

    private fun validate(): Boolean {
        return when (validateEmail(state.passkeyEmail)) {
            ValidateEmail.EmailVerificationType.TYPE_EMAIL_FIELD_EMPTY -> {
                state = state.copy(
                    passkeyEmailSupportingText = "Please Enter an Email",
                    isPasskeyEmailError = true
                )
                false
            }

            ValidateEmail.EmailVerificationType.TYPE_INVALID_EMAIL -> {
                state = state.copy(
                    passkeyEmailSupportingText = "Please Enter a valid Email",
                    isPasskeyEmailError = true
                )
                false
            }

            ValidateEmail.EmailVerificationType.TYPE_EMAIL_SUCCESS -> {
                true
            }
        }
    }

    private fun startPasskeyAuth(
        req: PasskeyAuthReq,
        activity: Activity
    ) {
        state = state.copy(
            passkeyAuthLoading = true
        )

        viewModelScope.launch(Dispatchers.IO) {
            // make api call

            val localeList = activity.resources.configuration.locales

            if (!localeList.isEmpty)
                api.passkeyReq(req)?.let { passkeyJson ->
                    if (passkeyJson.type == AUTH_RESPONSE_PASSKEY_TYPE_SIGN_UP) {
                        // create passkey from string and convert to CreatePublicKeyCredential
                        createPasskey(
                            credentialManager = cred,
                            jsonString = passkeyJson.req,
                            activity = activity,
                            challenge = passkeyJson.challenge,
                            sendUserToServer = { id ->
                                id?.let {
                                    sendPasskeyUserToServer( // create user
                                        user = it.toCreatePasskeyUserReq(
                                            email = state.passkeyEmail,
                                            token = passkeyJson.token,
                                            countryCode = localeList[0].country
                                        ),
                                        context = activity
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
                            cred,
                            passkeyJson.req,
                            activity,
                            passkeyJson.challenge,
                            getUserFromId = {
                                it?.let {
                                    getPasskeyUserFromServer(
                                        user = it.toGetPasskeyUserReq(passkeyJson.token),
                                        context = activity
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

    private fun sendPasskeyUserToServer(
        user: CreatePasskeyUserReq,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEvent.send(element = UiEvent.ShowToast("Please wait while we get things ready"))

            api.createPasskeyUser(user)?.let { response -> // get response from server

                when (response.status) {
                    UserCreationStatus.CREATED -> setupPasskeyUser(response, context)

                    UserCreationStatus.TOKEN_NOT_VALID -> {
                        onEvent(RootUiEvent.OnAuthCanceled)
                        _uiEvent.send(element = UiEvent.ShowToast("Please try SigningIn again"))
                    }

                    else -> {
                        onEvent(RootUiEvent.OnAuthCanceled)
                        onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
                    }
                }
                return@launch
            }

            // error occurred on  getting response from server
            onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
            onEvent(RootUiEvent.OnAuthCanceled)
        }
    }

    private fun getPasskeyUserFromServer(user: GetPasskeyUserReq, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEvent.send(element = UiEvent.ShowToast("Please wait while we get things ready"))

            api.getPasskeyUser(user)?.let { response -> // get user from server
                when (response.status) {
                    UserCreationStatus.CONFLICT -> setupPasskeyUser(response, context)

                    UserCreationStatus.TOKEN_NOT_VALID -> {
                        onEvent(RootUiEvent.OnAuthCanceled)
                        _uiEvent.send(element = UiEvent.ShowToast("Please try SigningIn again"))
                    }

                    else -> {
                        onEvent(RootUiEvent.OnAuthCanceled)
                        onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
                    }
                }
                return@launch
            }


            // error occurred on  getting user from server
            onEvent(RootUiEvent.OnAuthCanceled)
            onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
        }
    }

    private fun setupPasskeyUser(response: PasskeyAuthResponse, context: Context) {
        val cookie = cookieManager.extractTokenOrCookie()

        storeCookieOrAccessToken(cookie, ds)
        storeAuthType(AuthType.SESSION_AUTH, ds)

        storeProfilePicUri(uri = response.user.profilePic, ds)
        storeUsername(username = response.user.userName, ds)

        storeEmail(state.passkeyEmail, ds)

        when (response.status) {
            UserCreationStatus.CREATED -> storeSignInState(
                data = SignInStatus.GET_SPOTIFY_PLAYLIST,
                ds
            )

            UserCreationStatus.CONFLICT -> {
                storeSignInState(data = SignInStatus.OLD_USER, ds)

                storeData(
                    context = context,
                    tokenOrCookie = cookie,
                    response = response.data,
                    db = db
                )
            }

            else -> Unit
        }
    }

    private fun startGoogleAuth(req: GoogleAuthReq, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            api.googleAuth(req)?.let { response ->
                val cookie = cookieManager.extractTokenOrCookie()

                storeCookieOrAccessToken(cookie, ds)
                storeAuthType(AuthType.SESSION_AUTH, ds)

                storeProfilePicUri(uri = response.user.profilePic, ds)
                storeUsername(username = response.user.userName, ds)

                when (response.status) {
                    UserCreationStatus.CREATED -> storeSignInState(
                        data = SignInStatus.GET_SPOTIFY_PLAYLIST,
                        ds
                    )

                    UserCreationStatus.CONFLICT -> {
                        storeSignInState(
                            data = SignInStatus.OLD_USER,
                            ds
                        )

                        storeData(
                            context = context,
                            tokenOrCookie = cookie,
                            response = response.data,
                            db = db
                        )
                    }

                    UserCreationStatus.TOKEN_NOT_VALID -> {
                        _uiEvent.send(element = UiEvent.ShowToast("Please try SigningIn again"))
                    }

                    else -> Unit
                }

                return@launch
            }

            onEvent(RootUiEvent.SomeErrorOccurredOnAuth)
        }
    }
}