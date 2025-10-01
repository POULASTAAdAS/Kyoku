package com.poulastaa.auth.presentation.singup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.presentation.intro.model.EmailTextProp
import com.poulastaa.auth.presentation.intro.model.PasswordTextProp
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.auth.presentation.singup.model.UsernameTextProp
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

@HiltViewModel
internal class EmailSingUpViewmodel @Inject constructor() : ViewModel() {
    private val _uiEvent = Channel<EmailSingUpUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(EmailSingUpUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EmailSingUpUiState()
    )

    fun populateEmail(email: Email) {
        _state.update {
            it.copy(
                email = EmailTextProp(
                    isValidEmail = false,// todo validate
                    prop = TextProp(email)
                )
            )
        }
    }

    fun onAction(action: EmailSingUpUiAction) {
        if (_state.value.isLoading) return

        when (action) {
            is EmailSingUpUiAction.OnUsernameChange -> _state.update {
                it.copy(
                    username = UsernameTextProp(
                        isValidUsername = false, // todo validate
                        prop = TextProp(action.username)
                    )
                )
            }

            is EmailSingUpUiAction.OnEmailChange -> _state.update {
                it.copy(
                    email = EmailTextProp(
                        isValidEmail = false, // todo validate
                        prop = TextProp(action.email)
                    )
                )
            }

            is EmailSingUpUiAction.OnPasswordChange -> _state.update {
                it.copy(
                    password = PasswordTextProp(
                        isPasswordVisible = false, // todo validate
                        prop = TextProp(action.password)
                    )
                )
            }

            is EmailSingUpUiAction.OnConformPasswordChange -> _state.update {
                it.copy(
                    password = PasswordTextProp(
                        isPasswordVisible = false, // todo validate
                        prop = TextProp(action.password)
                    )
                )
            }

            EmailSingUpUiAction.OnSubmit -> {
                // todo validate all again

                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }
            }

            EmailSingUpUiAction.OnEmailLogInClick -> viewModelScope.launch {
                _uiEvent.send(EmailSingUpUiEvent.OnNavigateBack)
            }
        }
    }
}