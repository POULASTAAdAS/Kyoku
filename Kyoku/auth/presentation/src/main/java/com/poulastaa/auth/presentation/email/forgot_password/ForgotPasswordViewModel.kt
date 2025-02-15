package com.poulastaa.auth.presentation.email.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.model.ForgotPasswordStatus
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val validator: AuthValidator,
    private val repo: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ForgotPasswordUiState()
        )

    private val _uiEvent = Channel<ForgotPasswordUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadEmail(email: String) {
        _state.update {
            it.copy(
                email = it.email.copy(
                    value = email,
                    isValid = validator.isValidEmail(email),
                    isErr = false,
                    errText = UiText.DynamicString("")
                )
            )
        }
    }

    fun onAction(action: ForgotPasswordUiAction) {
        when (action) {
            is ForgotPasswordUiAction.OnEmailChange -> {
                _state.update {
                    it.copy(
                        email = it.email.copy(
                            value = action.email,
                            isValid = validator.isValidEmail(action.email),
                            isErr = false,
                            errText = UiText.DynamicString("")
                        )
                    )
                }
            }

            ForgotPasswordUiAction.OnSubmitClick -> {
                if (!state.value.email.isValid) {
                    _state.update {
                        it.copy(
                            email = it.email.copy(
                                isErr = true,
                                errText = UiText.StringResource(R.string.error_invalid_email)
                            )
                        )
                    }

                    return
                }

                if (_state.value.isMakingApiCall) return

                _state.update {
                    it.copy(
                        isMakingApiCall = true
                    )
                }

                viewModelScope.launch {
                    val result = repo.sendForgotPasswordMail(state.value.email.value.trim())

                    when (result) {
                        is Result.Error -> {
                            if (result.error == DataError.Network.NO_INTERNET) _uiEvent.send(
                                ForgotPasswordUiEvent.EmitToast(UiText.StringResource(R.string.error_no_internet))
                            ) else _uiEvent.send(
                                ForgotPasswordUiEvent.EmitToast(UiText.StringResource(R.string.error_something_went_wrong))
                            )
                        }

                        is Result.Success -> {
                            when (result.data) {
                                ForgotPasswordStatus.FORGOT_PASSWORD_MAIL_SEND -> _uiEvent.send(
                                    ForgotPasswordUiEvent.EmitToast(UiText.StringResource(R.string.password_reset_mail_sent))
                                )

                                ForgotPasswordStatus.USER_NOT_FOUND -> {
                                    _uiEvent.send(
                                        ForgotPasswordUiEvent.EmitToast(UiText.StringResource(R.string.error_user_not_found))
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_user_not_found)
                                            )
                                        )
                                    }
                                }

                                ForgotPasswordStatus.EMAIL_NOT_VALID -> {
                                    _uiEvent.send(
                                        ForgotPasswordUiEvent.EmitToast(UiText.StringResource(R.string.error_invalid_email))
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_invalid_email)
                                            )
                                        )
                                    }
                                }

                                else -> _uiEvent.send(
                                    ForgotPasswordUiEvent.EmitToast(UiText.StringResource(R.string.error_something_went_wrong))
                                )
                            }
                        }
                    }

                    _state.update {
                        it.copy(
                            isMakingApiCall = false
                        )
                    }
                }
            }
        }
    }
}