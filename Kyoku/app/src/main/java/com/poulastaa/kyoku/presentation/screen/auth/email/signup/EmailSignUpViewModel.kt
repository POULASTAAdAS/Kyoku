package com.poulastaa.kyoku.presentation.screen.auth.email.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.auth.AuthUiEvent
import com.poulastaa.kyoku.data.model.auth.email.login.EmailLoginState
import com.poulastaa.kyoku.data.model.auth.email.signup.EmailSignUpState
import com.poulastaa.kyoku.data.model.auth.email.signup.EmailSignUpUiEvent
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.usecase.UseCases
import com.poulastaa.kyoku.domain.usecase.ValidateConformPassword
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.domain.usecase.ValidatePassword
import com.poulastaa.kyoku.domain.usecase.ValidateUserName
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailSignUpViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val dataStore: DataStoreOperation,
    private val useCases: UseCases,
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

    var state by mutableStateOf(EmailSignUpState())
        private set

    private val _uiEvent = Channel<AuthUiEvent>()
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
                    _uiEvent.send(element = AuthUiEvent.Navigate(Screens.AuthEmailLogin.route))
                }
                state = EmailSignUpState()
            }

            EmailSignUpUiEvent.OnPasswordVisibilityChange -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            EmailSignUpUiEvent.OnContinueClick -> {
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

        val validUserName = when (useCases.validateUserName(state.userName)) {
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
            when (useCases.validateConformPassword(state.password, state.conformPassword)) {
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
}