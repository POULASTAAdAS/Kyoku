package com.poulastaa.auth.presentation.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.presentation.forgot_password.ForgotPasswordUiEvent.OnNavigate
import com.poulastaa.auth.presentation.forgot_password.model.ForgotPasswordAllowedNavigationScreens
import com.poulastaa.auth.presentation.forgot_password.model.ForgotPasswordAllowedNavigationScreens.Verify
import com.poulastaa.auth.presentation.intro.model.EmailTextProp
import com.poulastaa.core.presentation.Email
import com.poulastaa.core.presentation.designsystem.TextProp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class ForgotPasswordViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.minutes.inWholeMilliseconds),
        initialValue = ForgotPasswordUiState()
    )

    private val _uiEvent = Channel<ForgotPasswordUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun populateEmailField(email: Email) {
        _state.update {
            it.copy(
                email = EmailTextProp(
                    isValidEmail = true, // todo validate
                    prop = TextProp(email)
                )
            )
        }
    }

    fun onAction(action: ForgotPasswordUiAction) {
        when (action) {
            is ForgotPasswordUiAction.OnEmailChange -> _state.update {
                it.copy(
                    email = EmailTextProp(
                        isValidEmail = false, // todo validate
                        prop = TextProp(action.email)
                    )
                )
            }

            ForgotPasswordUiAction.OnNavigateBack -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        OnNavigate(
                            ForgotPasswordAllowedNavigationScreens.NavigateBack
                        )
                    )
                }
            }

            ForgotPasswordUiAction.OnSummit -> {
                if (_state.value.isLoading) return

                viewModelScope.launch {
                    _uiEvent.send(
                        OnNavigate(
                            Verify(_state.value.email.prop.value.trim())
                        )
                    )
                }
            }

            ForgotPasswordUiAction.OnVerifyClick -> {
                if (_state.value.isVerifyButtonEnabled.not()) return

                viewModelScope.launch {
                    _uiEvent.send(OnNavigate(Verify()))
                }
            }
        }
    }
}