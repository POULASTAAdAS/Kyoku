package com.poulastaa.auth.presentation.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.ResetPasswordRepository
import com.poulastaa.auth.domain.model.DtoResetPasswordState
import com.poulastaa.auth.domain.model.PasswordStatus
import com.poulastaa.auth.presentation.R
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.JWTToken
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import com.poulastaa.core.presentation.ui.R as CoreR

@HiltViewModel
internal class ResetPasswordViewmodel @Inject constructor(
    private val validator: AuthValidator,
    private val repo: ResetPasswordRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ResetPasswordUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ResetPasswordUiState()
    )

    private val _uiEvent = Channel<ResetPasswordUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var token: JWTToken? = null

    fun loadToken(token: JWTToken) {
        if (token.isBlank()) {
            viewModelScope.launch {
                _uiEvent.send(
                    ResetPasswordUiEvent.EmitToast(
                        UiText.StringResource(
                            CoreR.string.something_went_wrong_try_again
                        )
                    )
                )

                _uiEvent.send(ResetPasswordUiEvent.NavigateBack)
            }

            return
        }

        this.token = token
    }

    fun onAction(action: ResetPasswordUiAction) {
        if (_state.value.isLoading || _state.value.isReturnScreenVisible) return

        when (action) {
            is ResetPasswordUiAction.OnPasswordChange -> _state.update {
                it.copy(
                    password = it.password.copy(
                        prop = TextProp(action.password.trim())
                    )
                )
            }

            ResetPasswordUiAction.OnPasswordVisibilityToggle -> _state.update {
                it.copy(
                    password = it.password.copy(
                        isPasswordVisible = it.password.isPasswordVisible.not()
                    )
                )
            }

            is ResetPasswordUiAction.OnConformPasswordChange -> _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        prop = TextProp(action.password.trim())
                    )
                )
            }

            ResetPasswordUiAction.OnConformPasswordVisibilityToggle -> _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        isPasswordVisible = it.conformPassword.isPasswordVisible.not()
                    )
                )
            }

            ResetPasswordUiAction.OnSummit -> {
                if (validate().not()) return
                _state.update { it.copy(isLoading = true) }

                viewModelScope.launch {
                    delay(1_000)

                    token?.let { token ->
                        when (val res = repo.updatePassword(
                            password = _state.value.password.prop.value.trim(),
                            token = token
                        )) {
                            is Result.Error -> when (res.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    ResetPasswordUiEvent.EmitToast(
                                        UiText.StringResource(
                                            CoreR.string.please_check_internet_connection
                                        )
                                    )
                                )

                                else -> _uiEvent.send(
                                    ResetPasswordUiEvent.EmitToast(
                                        UiText.StringResource(
                                            CoreR.string.something_went_wrong_try_again
                                        )
                                    )
                                )
                            }

                            is Result.Success -> when (res.data) {
                                DtoResetPasswordState.SUCCESS -> _state.update {
                                    it.copy(isReturnScreenVisible = true)
                                }.also { delayForTheAnimationToComplete() }

                                DtoResetPasswordState.SAME_AS_OLD_PASSWORD -> _state.update {
                                    it.copy(
                                        password = it.password.copy(
                                            prop = it.password.prop.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.same_as_old_password)
                                            )
                                        ),
                                        conformPassword = it.conformPassword.copy(
                                            prop = it.conformPassword.prop.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.same_as_old_password)
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }

                    _state.update { it.copy(isLoading = false) }
                }
            }

            ResetPasswordUiAction.OnNavigateBack -> _uiEvent.trySend(ResetPasswordUiEvent.NavigateBack)
        }
    }

    private fun delayForTheAnimationToComplete() = viewModelScope.launch {
        _uiEvent.send(ResetPasswordUiEvent.TriggerAnimation)
        delay(6.seconds.inWholeMilliseconds)
        _uiEvent.send(ResetPasswordUiEvent.PopUpToIntroScreen)
    }

    private fun validate(): Boolean {
        var isPasswordValid = false
        var isConformPasswordValid = false

        when (validator.validatePassword(_state.value.password.prop.value.trim())) {
            PasswordStatus.VALID -> isPasswordValid = true

            PasswordStatus.EMPTY -> _state.update {
                it.copy(
                    password = it.password.copy(
                        prop = it.password.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.empty_password)
                        )
                    )
                )
            }

            PasswordStatus.TOO_SHORT -> _state.update {
                it.copy(
                    password = it.password.copy(
                        prop = it.password.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.password_too_short)
                        )
                    )
                )
            }

            PasswordStatus.TOO_LONG -> _state.update {
                it.copy(
                    password = it.password.copy(
                        prop = it.password.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.password_too_long)
                        )
                    )
                )
            }

            PasswordStatus.INVALID -> _state.update {
                it.copy(
                    password = it.password.copy(
                        prop = it.password.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.invalid_password)
                        )
                    )
                )
            }
        }

        when (validator.validatePassword(
            _state.value.conformPassword.prop.value.trim()
        )) {
            PasswordStatus.VALID -> isConformPasswordValid = true

            PasswordStatus.EMPTY -> _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        prop = it.conformPassword.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.empty_password)
                        )
                    )
                )
            }

            PasswordStatus.TOO_SHORT -> _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        prop = it.conformPassword.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.password_too_short)
                        )
                    )
                )
            }

            PasswordStatus.TOO_LONG -> _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        prop = it.conformPassword.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.password_too_long)
                        )
                    )
                )
            }

            PasswordStatus.INVALID -> _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        prop = it.conformPassword.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.invalid_password)
                        )
                    )
                )
            }
        }

        if (_state.value.password.prop.value.trim() != _state.value.conformPassword.prop.value.trim()) {
            isConformPasswordValid = false

            _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        prop = it.conformPassword.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.password_does_not_match)
                        )
                    )
                )
            }
        }

        return isPasswordValid && isConformPasswordValid
    }
}