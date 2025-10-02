package com.poulastaa.auth.presentation.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.auth.presentation.intro.model.IntroUiState
import com.poulastaa.core.presentation.designsystem.TextProp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class IntroViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(IntroUiState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = IntroUiState()
        )

    private val _uiEvent = Channel<IntroUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var emailLogInJob: Job? = null

    fun onAction(action: IntroUiAction) {
        if ((_state.value.isGoogleAuthLoading || _state.value.isEmailAuthLoading) &&
            action != IntroUiAction.ObPasswordVisibilityToggle
        ) return

        when (action) {
            is IntroUiAction.OnEmailChange -> {
                _state.update {
                    it.copy(
                        email = it.email.copy(
                            isValidEmail = false,
                            prop = TextProp(
                                value = action.email
                            )
                        )
                    )
                }
            }

            is IntroUiAction.OnPasswordChange -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            prop = TextProp(
                                value = action.password
                            )
                        )
                    )
                }
            }

            IntroUiAction.ObPasswordVisibilityToggle -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            isPasswordVisible = it.password.isPasswordVisible.not()
                        )
                    )
                }
            }

            IntroUiAction.OnForgotPasswordClick -> viewModelScope.launch {
                _uiEvent.send(
                    IntroUiEvent.Navigate(
                        IntroAllowedNavigationScreens.ForgotPassword(
                            _state.value.email.prop.value
                        )
                    )
                )
            }

            IntroUiAction.OnEmailSubmit -> {
                // TODO: validate

                _state.update {
                    it.copy(
                        isEmailAuthLoading = true
                    )
                }
            }

            IntroUiAction.OnEmailSingUpClick -> viewModelScope.launch {
                _uiEvent.send(
                    IntroUiEvent.Navigate(
                        IntroAllowedNavigationScreens.SingUp(
                            _state.value.email.prop.value
                        )
                    )
                )
            }

            IntroUiAction.OnGoogleAuthClick -> {
                _state.update {
                    it.copy(
                        isGoogleAuthLoading = true
                    )
                }
            }
        }
    }
}