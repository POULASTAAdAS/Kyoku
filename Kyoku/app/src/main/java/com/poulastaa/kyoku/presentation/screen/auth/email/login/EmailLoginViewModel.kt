package com.poulastaa.kyoku.presentation.screen.auth.email.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInResponse
import com.poulastaa.kyoku.data.model.api.auth.email.EmailLoginStatus
import com.poulastaa.kyoku.data.model.api.auth.email.ResendVerificationMailStatus
import com.poulastaa.kyoku.data.model.api.req.EmailLogInReq
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.auth.email.login.EmailLogInState
import com.poulastaa.kyoku.data.model.auth.email.login.EmailLoginUiEvent
import com.poulastaa.kyoku.domain.repository.AuthRepository
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.usecase.AuthUseCases
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.domain.usecase.ValidatePassword
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.utils.storeAuthType
import com.poulastaa.kyoku.utils.storeCookieOrAccessToken
import com.poulastaa.kyoku.utils.storeProfilePicUri
import com.poulastaa.kyoku.utils.storeRefreshToken
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.storeUsername
import com.poulastaa.kyoku.utils.toEmailLogInReq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EmailLoginViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val authUseCases: AuthUseCases,
    @Named("AuthApiImpl") private val api: AuthRepository
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = EmailLogInState(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    private var email: String? = null
    private var password: String? = null

    private var emailVerificationJob: Job? = null
    private var resendVerificationMailJob: Job? = null

    var state by mutableStateOf(EmailLogInState())
        private set

    private val _uiEvent = Channel<UiEvent>()
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

                            email = state.email
                            password = state.password

                            startEmailLogIn(state.toEmailLogInReq())
                        }

                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(element = UiEvent.ShowToast("Please Check Your Internet Connection"))
                        }
                    }
            }

            EmailLoginUiEvent.OnForgotPasswordClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = UiEvent.Navigate(Screens.ForgotPassword.route))
                }
                state = EmailLogInState()
            }

            EmailLoginUiEvent.OnSignUpClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = UiEvent.Navigate(Screens.AuthEmailSignUp.route))
                }
                state = EmailLogInState()
            }

            is EmailLoginUiEvent.EmitEmailSupportingText -> {
                state = state.copy(
                    isEmailError = true,
                    emailSupportingText = event.message
                )
            }

            is EmailLoginUiEvent.EmitPasswordSupportingText -> {
                state = state.copy(
                    isPasswordError = true,
                    passwordSupportingText = event.message
                )
            }

            is EmailLoginUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = UiEvent.ShowToast(event.message))
                }
            }

            EmailLoginUiEvent.OnAuthCanceled -> {
                state = state.copy(
                    isLoading = false
                )
            }

            EmailLoginUiEvent.SomeErrorOccurredOnAuth -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = UiEvent.ShowToast("Opus Something went wrong"))
                }
            }


            EmailLoginUiEvent.OnResendVerificationMailClick -> {
                state = state.copy(
                    isResendMailEnabled = false
                )
                handleEmailVerification()
            }
        }
    }

    private fun validate(): Boolean {
        val validEmail = when (authUseCases.validateEmail(state.email)) {
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

        val validPassword = when (authUseCases.validatePassword(state.password)) {
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

    private fun startEmailLogIn(req: EmailLogInReq) {
        viewModelScope.launch(Dispatchers.IO) {
            api.emailLogIn(req)?.let { response ->
                when (response.status) {
                    EmailLoginStatus.USER_PASS_MATCHED -> setUpUser(response)
                    EmailLoginStatus.PASSWORD_DOES_NOT_MATCH -> {
                        onEvent(EmailLoginUiEvent.OnAuthCanceled)
                        onEvent(EmailLoginUiEvent.EmitPasswordSupportingText("Password does not match"))
                    }

                    EmailLoginStatus.EMAIL_NOT_VERIFIED -> {
                        handleEmailVerification()
                    }

                    EmailLoginStatus.USER_DOES_NOT_EXISTS -> {
                        onEvent(EmailLoginUiEvent.OnAuthCanceled)
                        onEvent(EmailLoginUiEvent.EmitEmailSupportingText("Email not Registered"))
                    }

                    EmailLoginStatus.EMAIL_NOT_VALID -> {
                        onEvent(EmailLoginUiEvent.OnAuthCanceled)
                        onEvent(EmailLoginUiEvent.EmitEmailSupportingText("Not a valid Email"))
                    }

                    EmailLoginStatus.SOMETHING_WENT_WRONG -> {
                        onEvent(EmailLoginUiEvent.OnAuthCanceled)
                        onEvent(EmailLoginUiEvent.EmitToast("Please try Logging In again"))
                    }
                }

                return@launch
            }

            onEvent(EmailLoginUiEvent.OnAuthCanceled)
            onEvent(EmailLoginUiEvent.SomeErrorOccurredOnAuth)
        }
    }

    private fun handleEmailVerification() {
        if (email != null)
            viewModelScope.launch(Dispatchers.IO) {
                api.resendVerificationMail(email!!).let { status ->
                    when (status) {
                        ResendVerificationMailStatus.VERIFICATION_MAIL_SEND -> {
                            onEvent(EmailLoginUiEvent.EmitToast("An verification mail is sent to you please conform it to continue"))

                            // start checking
                            emailVerificationJob?.cancel()
                            emailVerificationJob = checkEmailVerificationState()

                            // set timer for resend email
                            viewModelScope.launch(Dispatchers.IO) {
                                delay(8000) // 8's
                                state = state.copy(
                                    isResendVerificationMailPromptVisible = true
                                )
                                resendVerificationMailJob?.cancel()
                                resendVerificationMailJob = resendVerificationMailTimer()
                            }
                        }

                        else -> {
                            onEvent(EmailLoginUiEvent.OnAuthCanceled)
                            onEvent(EmailLoginUiEvent.SomeErrorOccurredOnAuth)
                        }
                    }
                }
            }
        else {
            onEvent(EmailLoginUiEvent.EmitToast("Please try SigningIn again"))
            onEvent(EmailLoginUiEvent.OnAuthCanceled)
        }
    }

    private fun checkEmailVerificationState(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            for (i in 1..70) { // 3.5 minute
                delay(3000) // 3's

                api.isEmailVerified(state.email).let {
                    if (it) {
                        if (email != null && password != null) {
                            startEmailLogIn(  // send login req again
                                state.toEmailLogInReq(
                                    email!!,
                                    password!!
                                )
                            )
                            emailVerificationJob?.cancel()
                        } else {
                            onEvent(EmailLoginUiEvent.EmitToast("Please try SigningIn again"))
                            emailVerificationJob?.cancel()
                        }
                    }
                }
            }
            onEvent(EmailLoginUiEvent.EmitToast("Please try SigningIn again"))
            emailVerificationJob?.cancel()
        }
    }

    private fun resendVerificationMailTimer(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            for (i in 20 downTo 1) {
                delay(1000) // 1's
                state = state.copy(
                    sendVerificationMailTimer = i
                )
            }

            state = state.copy(
                isResendMailEnabled = true
            )
        }
    }

    private fun setUpUser(response: EmailLogInResponse) {
        storeCookieOrAccessToken(data = "Bearer ${response.accessToken}", ds)
        storeRefreshToken(response.refreshToken, ds)

        storeAuthType(AuthType.JWT_AUTH, ds)

        storeUsername(response.user.userName, ds)
        storeProfilePicUri(response.user.profilePic, ds)

        //todo handle response data

        storeSignInState(data = SignInStatus.OLD_USER, ds)
    }
}