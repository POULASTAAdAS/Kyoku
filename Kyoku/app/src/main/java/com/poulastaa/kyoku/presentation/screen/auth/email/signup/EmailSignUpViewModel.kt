package com.poulastaa.kyoku.presentation.screen.auth.email.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.UserCreationStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpResponse
import com.poulastaa.kyoku.data.model.api.auth.email.ResendVerificationMailStatus
import com.poulastaa.kyoku.data.model.api.req.EmailSignUpReq
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.auth.email.signup.EmailSignUpState
import com.poulastaa.kyoku.data.model.screens.auth.email.signup.EmailSignUpUiEvent
import com.poulastaa.kyoku.domain.repository.AuthRepository
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.usecase.AuthUseCases
import com.poulastaa.kyoku.domain.usecase.ValidateConformPassword
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.domain.usecase.ValidatePassword
import com.poulastaa.kyoku.domain.usecase.ValidateUserName
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.utils.storeAuthType
import com.poulastaa.kyoku.utils.storeCookieOrAccessToken
import com.poulastaa.kyoku.utils.storeEmail
import com.poulastaa.kyoku.utils.storePassword
import com.poulastaa.kyoku.utils.storeProfilePicUri
import com.poulastaa.kyoku.utils.storeRefreshToken
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.storeUsername
import com.poulastaa.kyoku.utils.toEmailSignUpReq
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailSignUpViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val authUseCases: AuthUseCases,
    private val api: AuthRepository
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = EmailSignUpState(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    private var emailVerificationJob: Job? = null
    private var resendVerificationMailJob: Job? = null

    private var email: String? = null
    private var password: String? = null

    var state by mutableStateOf(EmailSignUpState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: EmailSignUpUiEvent) {
        when (event) {
            is EmailSignUpUiEvent.OnEmailEnter -> {
                state = state.copy(
                    email = event.data,
                    isEmailError = false,
                    emailSupportingText = ""
                )
            }

            is EmailSignUpUiEvent.OnUsernameEnter -> {
                state = state.copy(
                    userName = event.data,
                    isUserNameError = false,
                    userNameSupportingText = ""
                )
            }

            is EmailSignUpUiEvent.OnPasswordEnter -> {
                state = state.copy(
                    password = event.data,
                    isPasswordError = false,
                    passwordSupportingText = ""
                )
            }

            is EmailSignUpUiEvent.OnConformPasswordEnter -> {
                state = state.copy(
                    conformPassword = event.data,
                    isConformPasswordError = false,
                    conformPasswordSupportingText = ""
                )
            }

            is EmailSignUpUiEvent.OnAutoFillEmail -> {
                state = state.copy(
                    email = event.email,
                    isUserNameError = false,
                    userNameSupportingText = ""
                )
            }

            is EmailSignUpUiEvent.OnAutoFillPassword -> {
                state = state.copy(
                    password = event.password,
                    conformPassword = event.password,
                    isPasswordError = false,
                    passwordSupportingText = "",
                    isConformPasswordError = false,
                    conformPasswordSupportingText = ""
                )
            }

            is EmailSignUpUiEvent.OnAutoFillUserName -> {
                state = state.copy(
                    userName = event.username,
                    isUserNameError = false,
                    userNameSupportingText = ""
                )
            }

            EmailSignUpUiEvent.OnLogInClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = UiEvent.Navigate(Screens.AuthEmailLogin.route))
                }
                state = EmailSignUpState()
            }

            EmailSignUpUiEvent.OnPasswordVisibilityChange -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            is EmailSignUpUiEvent.OnContinueClick -> {
                if (!state.isLoading)
                    if (checkInternetConnection()) {
                        val result = validate()

                        if (result) {
                            state = state.copy(
                                isLoading = true
                            )

                            email = state.email
                            password = state.password

                            val localList = event.activity.resources.configuration.locales

                            if (!localList.isEmpty) startEmailSignIn(
                                state.toEmailSignUpReq(
                                    localList[0].country
                                )
                            )
                            else onEvent(EmailSignUpUiEvent.SomeErrorOccurredOnAuth)
                        }

                    } else onEvent(EmailSignUpUiEvent.EmitToast("Please Check Your Internet Connection"))
            }

            is EmailSignUpUiEvent.EmitEmailSupportingText -> {
                state = state.copy(
                    emailSupportingText = event.message,
                    isEmailError = true,
                    isLoading = false
                )
            }

            is EmailSignUpUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(element = UiEvent.ShowToast(event.message))
                }
            }

            EmailSignUpUiEvent.SomeErrorOccurredOnAuth -> onEvent(EmailSignUpUiEvent.EmitToast("Opus Something went wrong"))

            EmailSignUpUiEvent.OnAuthCanceled -> {
                state = state.copy(
                    isLoading = false
                )
            }

            EmailSignUpUiEvent.OnResendVerificationMailClick -> {
                state = state.copy(
                    isResendMailEnabled = false
                )

                resendVerificationMail()
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

        val validUserName = when (authUseCases.validateUserName(state.userName)) {
            ValidateUserName.UsernameVerificationType.TYPE_USERNAME_FIELD_EMPTY -> {
                state = state.copy(
                    isUserNameError = true,
                    userNameSupportingText = "Username can't be empty"
                )
                false
            }

            ValidateUserName.UsernameVerificationType.TYPE_USERNAME_TO_LONG -> {
                state = state.copy(
                    isUserNameError = true,
                    userNameSupportingText = "Username to long"
                )
                false
            }

            ValidateUserName.UsernameVerificationType.TYPE_INVALID_USERNAME -> {
                state = state.copy(
                    isUserNameError = true,
                    userNameSupportingText = "Username can't contain spatial characters"
                )
                false
            }

            ValidateUserName.UsernameVerificationType.TYPE_USERNAME_SUCCESS -> {
                true
            }
        }

        val validConformPassword =
            when (authUseCases.validateConformPassword(state.password, state.conformPassword)) {
                ValidateConformPassword.ConformPasswordVerificationType.TYPE_SAME_PASSWORD -> {
                    true
                }

                ValidateConformPassword.ConformPasswordVerificationType.TYPE_EMPTY_PASSWORD -> {
                    state = state.copy(
                        isConformPasswordError = true,
                        conformPasswordSupportingText = "Password is empty"
                    )
                    false
                }

                ValidateConformPassword.ConformPasswordVerificationType.TYPE_DIFFERENT_PASSWORD -> {
                    state = state.copy(
                        isConformPasswordError = true,
                        conformPasswordSupportingText = "Password are not same"
                    )
                    false
                }
            }

        return validEmail && validPassword && validUserName && validConformPassword
    }

    private fun startEmailSignIn(req: EmailSignUpReq) {
        viewModelScope.launch(Dispatchers.IO) {
            api.emailSignUp(req)?.let { response ->
                when (response.status) {
                    UserCreationStatus.CREATED -> setUpUser(response)

                    UserCreationStatus.CONFLICT -> {
                        onEvent(EmailSignUpUiEvent.EmitEmailSupportingText("Email already in use"))
                    }

                    UserCreationStatus.EMAIL_NOT_VALID -> {
                        onEvent(EmailSignUpUiEvent.EmitEmailSupportingText("Please enter a valid email"))
                    }

                    else -> Unit
                }

                return@launch
            }

            onEvent(EmailSignUpUiEvent.SomeErrorOccurredOnAuth)
        }
    }

    private fun setUpUser(response: EmailSignUpResponse) {
        storeCookieOrAccessToken(data = "Bearer ${response.accessToken}", ds = ds)
        storeRefreshToken(data = response.refreshToken, ds)

        storeAuthType(AuthType.JWT_AUTH, ds)

        storeUsername(response.user.userName, ds)
        storeProfilePicUri(response.user.profilePic, ds)

        if (email != null && password != null) {
            storeEmail(email!!, ds)
            storePassword(password!!, ds)
        }

        onEvent(EmailSignUpUiEvent.EmitToast("An verification mail is sent to you please conform it to continue"))

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

    private fun checkEmailVerificationState(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            for (i in 1..70) { // 3.5 minute
                delay(3000) // 3's

                api.isEmailVerified(state.email).let {
                    if (it) {
                        state = EmailSignUpState()
                        storeSignInState(data = SignInStatus.NEW_USER, ds) // login user to app
                    }
                }
            }

            // took to long to validate email
            onEvent(EmailSignUpUiEvent.EmitToast("Please try SigningIn again"))
            onEvent(EmailSignUpUiEvent.OnAuthCanceled)
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

    private fun resendVerificationMail() {
        if (email != null)
            viewModelScope.launch(Dispatchers.IO) {
                api.resendVerificationMail(email!!).let { status ->
                    when (status) {
                        ResendVerificationMailStatus.VERIFICATION_MAIL_SEND -> {
                            onEvent(EmailSignUpUiEvent.EmitToast("An verification mail is sent to you please conform it to continue"))

                            // start checking
                            emailVerificationJob?.cancel()
                            emailVerificationJob = checkEmailVerificationState()

                            // set timer for resend email
                            resendVerificationMailJob?.cancel()
                            resendVerificationMailJob = resendVerificationMailTimer()
                        }

                        else -> {
                            onEvent(EmailSignUpUiEvent.OnAuthCanceled)
                            onEvent(EmailSignUpUiEvent.SomeErrorOccurredOnAuth)
                        }
                    }
                }
            }
        else {
            onEvent(EmailSignUpUiEvent.EmitToast("Please try SigningIn again"))
            onEvent(EmailSignUpUiEvent.OnAuthCanceled)
        }
    }
}